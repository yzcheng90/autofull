# autofull-spring-boot-starter
自动填充属性框架

基于 springboot 和 mybatis plus

### 依赖
```java
<dependency>
  <groupId>com.github.yzcheng90</groupId>
  <artifactId>autofull-spring-boot-starter</artifactId>
  <version>1.3.0</version>
</dependency>
```

### 配置
```yaml
#redis 配置
spring:
  redis:
    cache:
      host: 127.0.0.1:6379
      password:
#填充框架配置
autofull:
  show-log: false
  max-level: 1
#文件系统配置 @AutoFullOssUrl 注解使用
oss:
  enable: true
  url: http://192.168.0.212:9000
  previewUrl: http://192.168.0.212:9000
  access-key: xxxx
  secret-key: xxxxx
  bucket-name: xxx
```

### 功能
> v1.0.0
>- @AutoFullBean  自动填充Bean
>- @AutoFullBeanSQL  自动填充Bean自定义SQL
>- @AutoFullField  自动填充字段
>- @AutoFullFieldSQL  自动填充字段自定义SQL
>- @AutoFullList  自动填充List
>- @AutoFullListSQL  自动填充List自定义SQL
>- @AutoFullJoin  多字段拼接
>- @AutoFullOssUrl  自动拼接OSS预览地址
>
> v1.1.0
> - @AutoFullMask 数据脱敏 支持手机号和身份证 比如：138****8888
>
> v1.2.0
>- @AutoFullBean
>- @AutoFullBeanSQL
>- @AutoFullList
>- @AutoFullListSQL
>- 以上四个注解新增参数 childLevel （是否支持查询子级）
>- 比如 用户表中有角色，角色表中还有权限，如果在用户表使用注解查询角色时 childLevel = true 那么查询用户的时候自动查询角色和权限
>
>- 新增日志打印开关
>
> v1.2.1
>- 修复使用 @AutoFullField、@AutoFullFieldSQL 时类型转换错误
>- 修复使用 @AutoFullBean 时不是泛型获取不到类型错误
>
> v1.3.0
>- 新增@AutoDecodeMask 参数自动解密注解
>- 新增redis 缓存,第一次填充数据就会缓存到redis,如果有对该表修改则删除改表缓存

### 示例代码：

> 这里以 x-springboot 工程为例
>
> 实现功能：查询用户和角色

##### SysUser    bean

```java
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUser extends Model<SysUser> {

  /**
	* 用户ID
  */
  @TableId(value = "user_id", type = IdType.AUTO)
  private Long userId;

  ..........

  // 当前用户所有角色
  @TableField(exist = false)
  @AutoFullListSQL(sql = " select * from sys_role where create_user_id = {userId}")
  private List<SysRole> roleIdList;


  // 当前用户所有菜单
  @TableField(exist = false)
  @AutoFullList(table = "sys_menu",conditionField = "userId")
  private List<SysMenu> menuList;

}
```

> 注意：{userId}  这个userId 字段必须是 SysUser 这个bean 里面有的字段，否则查询不到

**controller**

```java
@RestController
@RequestMapping("/sys/user")
@AllArgsConstructor
public class SysUserController extends AbstractController {
    
    private final SysUserService sysUserService;
    
    @RequestMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
       // 查询用户
       List<SysUser> list = sysUserService.list();
       // 填充（这里去执行 bean 中配置的注解相关处理）
       AutoFullHandler.full(list);
       return R.ok().setData(list);
    }
    
}
```

> AutoFullHandler.full 这个方法可以封装到 R（返回类）里面 ，比如：

```java
public <T> R autoFullData(IPage<T> iPage){
    AutoFullHandler.full(iPage);
    super.put("data", iPage);
    return this;
}

public <T> R autoFullData(List<T> list){
    AutoFullHandler.full(list);
    super.put("data", list);
    return this;
}

public <T> R autoFullData(T entity){
    AutoFullHandler.full(entity);
    super.put("data", entity);
    return this;
}
```

> 然后

```java
return R.ok().autoFullData(list);
```



### 注解介绍：

- AutoFullBean   ( 填充一个对象)
  > ```java
  > @AutoFullBean(table="sys_role", conditionField = "userId")
  > public SysRole sysRole;
  > ```

- AutoFullBeanSQL  ( 填充一个对象 自定义sql)
  > ```java
  > @AutoFullListSQL(sql = " select * from sys_role where create_user_id = {userId}")
  > public List<SysRole> sysRole;
  > ```

- AutoFullField  ( 填充一个字段 )
  > ```java
  > @AutoFullField(table="表名",conditionField = "条件字段",queryField = "sName")
  > public String sName;
  > ```

- AutoFullFieldSQL  ( 填充一个字段 自定义sql)
    >  ```java
    >  @AutoFullFieldSQL(sql = "select name from xxxx where id = {id} ")
    >  public String name;
    > ```

- AutoFullList  ( 填充一个List对象 ）
    >  ```java
    >  @AutoFullList(table = "表名",conditionField = "字段名")
    >  public List<SysRole> roles;
    >  ```

- AutoFullListSQL  ( 填充一个List对象 自定义sql)
    >  ```java
    >  @AutoFullFieldSQL(sql = "select * from xxxx where id = {id} ")
    >  public List<SysRole> roles;
    >  ```

- AutoFullJoin  （字段拼接）
    >  ```java
    >  @AutoFullJoin(value = "湖南省长沙市岳麓区{park}5栋{number}")
    >  public String address;
    >  ```

- AutoFullOssUrl  ( 填充OSS预览地址)
    >  ```java
    >  @ApiModelProperty(value = "文件存储路径")
    >  @JsonProperty(value = "sFileUrl")
    >  @AutoFullOssUrl
    >  public String sFileUrl;
    >  ```
    >
  >比如数据库保存的是  2020/10/01/xxx.jpg
  >
  >返回给前台要完整路径，比如  http://192.168.0.212:9000/xxx/2020/10/01/xxx.jpg
  >
  >使用这个功能需要在yml中配置



 **最后**

- 交流QQ群：17470566
- 本人QQ：913624256
- 如果喜欢，记得star fork 谢谢您的关注 本项目会持续维护

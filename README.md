# autofull-spring-boot-starter （自动填充属性框架）

【autofull】 -- 为偷懒而生。

设计初衷，为了摆脱重复的劳动力和冗余代码，以极简的代码实现功能，易维护

### 实现功能
> - 关联表查询无需写业务逻辑及SQL
> - 多表关联查询无需写业务逻辑，只需要自定义SQL到返回字段上面即可
> - 敏感字段返回前断，只需要一个注解就可自动加解密
> - 所有绑定数据库查询的注解均默认使用redis 缓存，减少数据库操作
> - 当执行数据库曾、删、改操作时，涉及到缓存数据则自动删除对应表缓存，已免造成脏读

基于 springboot 和 mybatis plus

### 依赖
```java
<dependency>
  <groupId>com.github.yzcheng90</groupId>
  <artifactId>autofull-spring-boot-starter</artifactId>
  <version>1.3.3</version>
</dependency>
```

### 示例代码：
>
> 实现功能：查询用户和角色

##### SysUser    bean

```java
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUser extends Model<SysUser> {

  // 用户ID
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
@AllArgsConstructor
public class SysUserController {
    
    private final SysUserService sysUserService;
    
    @AutoFullData
    @RequestMapping("/list")
	public Object list(@RequestParam Map<String, Object> params){
       // 查询用户
       List<SysUser> list = sysUserService.list();
       return list;
    }
    
}
```

### 相关博客
> CSDN《[自动填充系列](https://blog.csdn.net/qq_15273441/category_10912977.html)》 

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
>- 新增redis 缓存,第一次填充数据就会缓存到redis,如果有对该表修改则删除该表缓存
>
> v1.3.1
>- SqlSessionFactory 换成 JdbcTemplate 解决SqlSession 8小时后连接断开问题
>
> v1.3.2
>- 优化重构部分代码
>- 升级依赖hutool 5.8.8
>- 升级依赖mybatis plus 3.5.5
>
> v1.3.3
>- 新增@AutoFullData 使用aop填充


 **最后**

- 交流QQ群：17470566
- 本人QQ：913624256
- 如果喜欢，记得star fork 谢谢您的关注 本项目会持续维护

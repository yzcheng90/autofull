# zhjg-common-autofull
 自动填充属性框架



### 使用方法：

> 1、pom中的 parent 改成自己项目
>
> 2、在bean中的字段上加上对应注解
>
> 3、最后使用 AutoFullHandler.full(obj);

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

	@TableField(exist = false)
    @AutoFullListSQL(sql = " select * from sys_role where create_user_id = {userId}")
	private List<SysRole> roleIdList;
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

  ```java
  @Documented
  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.RUNTIME)
  public @interface AutoFullBean {
  
      /**
       * 表名
       **/
      String table() default "";
  
      /**
       * 条件字段
       **/
      String conditionField() default "";
  
  }
  ```

  > ```java
  > @AutoFullBean(table="sys_role", conditionField = "userId")
  > ```

- AutoFullBeanSQL  ( 填充一个对象 自定义sql)

  ```java
  @Documented
  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.RUNTIME)
  public @interface AutoFullBeanSQL {
  
      /**
       * sql
       **/
      String sql() default "";
  
  }
  ```

  > ```java
  > @AutoFullListSQL(sql = " select * from sys_role where create_user_id = {userId}")
  > ```

- AutoFullField  ( 填充一个字段 )

  ```java
  @Documented
  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.RUNTIME)
  public @interface AutoFullField {
  
      /**
       * 表名
       **/
      String table() default "";
  
      /**
       * 条件字段
       **/
      String conditionField() default "";
  
      /**
       * 查询字段
       **/
      String queryField() default "";
  
  }
  ```

  > ```java
  > 
  > @AutoFullField(table="表名",conditionField = "条件字段",queryField = "sName")
  > public String sName;
  > ```

- AutoFullFieldSQL  ( 填充一个字段 自定义sql)

  ```java
  @Documented
  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.RUNTIME)
  public @interface AutoFullFieldSQL {
  
      /**
       * 自定义 SQL
       **/
      String sql() default "";
  
  }
  ```

  ```java
  @AutoFullFieldSQL(sql = "select name from xxxx where id = {id} ")
  public String name;
  ```

- AutoFullList  ( 填充一个List对象 ）

  ```java
  @Documented
  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.RUNTIME)
  public @interface AutoFullList {
  
      /**
       * 表名
       **/
      String table() default "";
  
      /**
       * 条件字段
       **/
      String conditionField() default "";
  
  }
  ```

  ```java
  @AutoFullList(table = "表名",conditionField = "字段名")
  public List<SysRole> roles;
  ```

- AutoFullListSQL  ( 填充一个List对象 自定义sql)

  ```java
  @Documented
  @Target({ElementType.FIELD})
  @Retention(RetentionPolicy.RUNTIME)
  public @interface AutoFullListSQL {
  
      /**
       * 表名
       **/
      String sql() default "";
  
  }
  ```

  ```java
  @AutoFullFieldSQL(sql = "select * from xxxx where id = {id} ")
  public List<SysRole> roles;
  ```

- AutoFullJoin  （字段拼接）

  ```java
  @AutoFullJoin(value = "湖南省长沙市岳麓区{park}5栋{number}")
  public String address;
  ```

- AutoFullOssUrl  ( 填充OSS预览地址)

  ```java
  @ApiModelProperty(value = "文件存储路径")
  @JsonProperty(value = "sFileUrl")
  @AutoFullOssUrl
  public String sFileUrl;
  ```

  >比如数据库保存的是  2020/10/01/xxx.jpg
  >
  >返回给前台要完整路径，比如  http://192.168.0.212:9000/xxx/2020/10/01/xxx.jpg
  >
  >使用这个功能需要在yml中配置

  ```yaml
  # 文件系统
  oss:
    url: http://192.168.0.212:9000
    previewUrl: http://192.168.0.212:9000
    access-key: xxxxx
    secret-key: xxxxx
    bucket-name: xxxx
  ```

### 自定义扩展：

> 1、自己创建一个注解  比如： `AutuFullxxx`
>
> 2、再写一个实现类 AutuFullxxxService

```java
@Slf4j
@Component
@AutoFullConfiguration(type = AutuFullxxx.class)
public class AutuFullxxxService implements Handler{

    @Override
    public String sql(String table, String queryField, String alias, String conditionField, String condition) {
        return null;
    }

    @Override
    public String sql(String sql, String conditionField) {
        return null;
    }

    @Override
    public void result(Annotation annotation, Field[] fields, Field field, Object obj) {
        try {
            if(annotation instanceof AutuFullxxx){
                AutuFullxxx fieldAnnotation = field.getAnnotation(AutuFullxxx.class);
                if(fieldAnnotation != null){
                    field.setAccessible(true);
                    String value = fieldAnnotation.value();
                   	// 具体实现逻辑
                    field.set(obj,value);
                }
            }
        } catch (IllegalAccessException e) {
            log.error("填充字符连接失败,{}",e);
            e.printStackTrace();
        }
    }
}
```

 **最后**

- 交流QQ群：17470566
- 本人QQ：913624256
- 如果喜欢，记得star fork 谢谢您的关注 本项目会持续维护
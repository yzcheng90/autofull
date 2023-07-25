# autofull-spring-boot-starter （自动填充属性框架）

![Image text](https://img.shields.io/badge/autofull-v1.5.2-green.svg)
![Image text](https://img.shields.io/badge/Mybatis_plus-3.5.5-green.svg)
![Image text](https://img.shields.io/badge/hutool-5.8.12-green.svg)

[更新日志](https://github.com/yzcheng90/zhjg-common-autofull/tree/master/doc/update.md)

【AutoFull】 -- 为偷懒而生。

设计初衷，为了摆脱重复的劳动力和冗余代码，以极简的代码实现功能，易维护

### 实现功能
> - 关联表查询无需写业务逻辑及SQL
> - 多表关联查询无需写业务逻辑，只需要自定义SQL到返回字段上面即可
> - 敏感字段返回前断，只需要一个注解就可自动加解密
> - 所有绑定数据库查询的注解均默认使用redis 缓存，减少数据库操作
> - 当执行数据库曾、删、改操作时，涉及到缓存数据则自动删除对应表缓存，已免造成脏读

基于 springboot 和 mybatis plus

### 依赖
```xml
<dependency>
  <groupId>com.github.yzcheng90</groupId>
  <artifactId>autofull-spring-boot-starter</artifactId>
  <version>1.5.3</version>
</dependency>
```

### Example
[《autofull-spring-boot-starter-example》](https://github.com/yzcheng90/autofull/tree/master/autofull-spring-boot-starter-example)

### 示例代码：
>
> 实现功能：查询用户和菜单

##### Bean

```java
@Data
@TableName("sys_user")
public class SysUser {

  @TableId(value = "user_id", type = IdType.AUTO)
  public Long userId;

  @TableField(exist = false)
  @AutoFullList(table = "sys_menu",conditionField = "userId")
  public List<SysMenu> menuList;

  class SysMenu {
     
        public Long menuId;
        public String menuName;
        public Long userId;
  }

}
```

> 注意：这个userId 字段必须是 SysUser 这个bean 里面有的字段，否则查询不到

**controller**

```java
@RestController
public class SysUserController {
    
    @Autowired
    public SysUserService sysUserService;
    
    @AutoFullData
    @RequestMapping("/list")
	public Object list(@RequestParam Map<String, Object> params){
       List<SysUser> list = sysUserService.list();
       return list;
    }
    
}
```

**返回结果**

> 访问 ：http://localhost:8080/list 

```json
{
  "userId": 1,
  "menuList": [{
      "menuId": 1,
      "menuName": "用户管理",
      "userId": 1
   }]
}
```


### 相关博客
> CSDN《[自动填充系列](https://blog.csdn.net/qq_15273441/category_10912977.html)》 


 **最后**

- 交流QQ群：17470566
- 本人QQ：913624256
- 如果喜欢，记得star fork 谢谢您的关注 本项目会持续维护

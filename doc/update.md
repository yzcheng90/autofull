### 功能
>
>v1.3.7
>- 修复long，integer的反序列化转换错误
>
>
>v1.3.6
>- 修复转驼峰bug
>
> v1.3.5
>- 修复填充的条件字段未使用驼锋规范而找不到字段的问题
>- 数据脱敏使用hutool工具类，类型支持 CHINESE_NAME,ID_CARD,FIXED_PHONE,MOBILE_PHONE,ADDRESS,EMAIL,PASSWORD,CAR_LICENSE,BANK_CARD
>
>
> v1.3.4
>- 新增R类 返回对象封装
>
> v1.3.3
>- 新增@AutoFullData 使用aop填充
>
> v1.3.2
>- 优化重构部分代码
>- 升级依赖hutool 5.8.8
>- 升级依赖mybatis plus 3.5.5
>
> v1.3.1
>- SqlSessionFactory 换成 JdbcTemplate 解决SqlSession 8小时后连接断开问题
>
> v1.3.0
>- 新增@AutoDecodeMask 参数自动解密注解
>- 新增redis 缓存,第一次填充数据就会缓存到redis,如果有对该表修改则删除该表缓存
>
> v1.2.1
>- 修复使用 @AutoFullField、@AutoFullFieldSQL 时类型转换错误
>- 修复使用 @AutoFullBean 时不是泛型获取不到类型错误
>
> v1.2.0
>- @AutoFullBean
>- @AutoFullBeanSQL
>- @AutoFullList
>- @AutoFullListSQL
>- 以上四个注解新增参数 childLevel （是否支持查询子级）
>- 比如 用户表中有角色，角色表中还有权限，如果在用户表使用注解查询角色时 childLevel = true 那么查询用户的时候自动查询角色和权限
>- 新增日志打印开关
>
> v1.1.0
> - @AutoFullMask 数据脱敏 支持手机号和身份证 比如：138****8888
>
> v1.0.0
>- @AutoFullBean  自动填充Bean
>- @AutoFullBeanSQL  自动填充Bean自定义SQL
>- @AutoFullField  自动填充字段
>- @AutoFullFieldSQL  自动填充字段自定义SQL
>- @AutoFullList  自动填充List
>- @AutoFullListSQL  自动填充List自定义SQL
>- @AutoFullJoin  多字段拼接
>- @AutoFullOssUrl  自动拼接OSS预览地址



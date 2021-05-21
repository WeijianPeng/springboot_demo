# springboot_demo
## springboot项目框架搭建
1、领域驱动设计搭建spring boot项目基本框架（也涉及微服务拆分）
2、利用flywaydb插件进行数据库版本控制
3、利用dbunit框架进行单元测试数据库记录操作
4、checkstyle检查java的编码命名
5、jacoco检查代码单元测试覆盖率


## 项目代码目录结构
### 1、com.china.demo 源码文件夹目录
---application   //应用层，供controller层调用，调用service层功能，一般不允许跨层调用（如调用infrastructure层的类）
---controller
    ---assembler  // 组合层，对于通过application层不能直接提供的数据模型，在该层组合。
    ---common    // 公共层，包含公共逻辑类，如http上下文处理（用户相关信息获取等）
    ---dto            // 数据转换层（请求/响应与领域模型之间的转换）
        ---request     // 请求的数据模型
        ---response  // 返回响应的数据模型
    ---exception    // 存放拦截异常的类
---domain           // 领域模型层。所有的模型和业务逻辑（包括领域类、service类、repository类）。只能调用infrastructure层，不能调用外层（其他业务application和service）
    ---exception   // 用于存放通用的异常处理类和异常处理逻辑
    ---xxx             // 领域模型
        ---xxx.java                    // 领域模型属性类
        ---xxxRepository.java  // 访问数据层的接口
        ---xxxService.java       //  该领域模型的业务逻辑，（可以调用其他领域service类）
---infrastructure
    ---agent     // 对接第三方系统
        ---dto   // 请求第三方系统的数据模型
    ---persistence  //持久化层
        ---xxx          //某个数据模型
            ---xxxDBO.java   // 数据模型类，支持与领域模型类之间互相转化
            ---xxxMapper.java // mapper类，单表查询不需要写任何代码，利用tk.mybatis框架接口即可。
        ---xxxRepositoryImpl.java // 具体业务数据库查询逻辑
---Appliction.java

### 2、resource文件夹目录
---db
    ---migration   //flyway框架实现数据库表修改
    ---mapper  // 多表查询的xml脚本目录
---application.yml
---application-dev.yml
---application-st.yml
---application-uat.yml
---application-prd.yml

###  3、test文件夹目录
com.china.demo  //测试利用DBunit框架完成数据库记录操作。
---controller // controller接口测试
---domain  // 领域层业务逻辑测试

###  4、根目录的checkstyle和jacoco
/根目录
---config
    ---checkstyle
        ---checkstyle.xml  代码检查的具体规则定义
    ---gradle
        ---jacoco.gradle   用于检查项目代码的单元测试覆盖率  要求>80%
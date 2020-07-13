金融分布式架构SOFAStack > 中间件 > SOFABoot > 创建工程：https://help.aliyun.com/document_detail/133100.html
Web 工程初始目录结构如下：
├── cloud-frame
│   └── vi-bm
│       ├── pom.xml
│       └── src
│           ├── main
│           │   ├── java
│           │   │   └── com
│           │   │       └── emis
│           │   │           └── vi
│           │   │                └── bm
│           │   │                     └── ViBmApplication.java => 启动函数
│           │   └── resources
│           │       ├── META-INF
│           │       │   └── vi-bm => Spring 配置文件存放处，放到指定的 vi-bm 目录下
│           │       │       └── vi-bm-web.xml
│           │       ├── config => 配置目录，详见「配置解决方案」（暂时没用到）
│           │       │   └── application.properties => 应用的日志配置文件
│           │       │   └── application-dev.properties
│           │       │   └── application-test.properties
│           │       ├── logback-spring.xml => 应用的日志配置文件
│           │       └── static => WEB 工程的静态页面存放处
│           │           └── index.html
│           └── test => 应用的测试模块，内置启动了 Spring Boot，方便业务测试
│               └── java
│                   └── com
│                       └── emis
│                           └── vi
│                               └── bm
│                                    └── test
│                                         └── ViBmApplicationTests.java
└── pom.xml => 应用的 Maven 配置文件

说明：
默认情况下，静态页面都存放至 src/main/resources/static 目录下以进行统一管理。
SOFABoot 的全局属性配置解决方案和日志配置解决方案，请参考 SOFABoot 技术栈的 配置解决方案：
https://help.aliyun.com/document_detail/133140.html?spm=a2c4g.11186623.2.22.4f262f85o8QhZS。

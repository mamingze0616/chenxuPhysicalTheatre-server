# 使用 maven bom 管理依赖，减少版本号的冲突。
因为 jsqlparser 5.0+ 版本不再支持 jdk8 针对这个问题解耦 jsqlparser 依赖。
正确打开姿势，引入 mybatis-plus-bom 模块，然后引入 ..starter 和 ..jsqlparser.. 依赖
// spring boot3 引入可选模块
implementation("com.baomidou:mybatis-plus-spring-boot3-starter")

// jdk 11+ 引入可选模块
implementation("com.baomidou:mybatis-plus-jsqlparser")

// spring boot2 引入可选模块
implementation("com.baomidou:mybatis-plus-boot-starter")

// jdk 8+ 引入可选模块
implementation("com.baomidou:mybatis-plus-jsqlparser-4.9")
# gradle 使用java-platform适配bom
再结合atalogs的情况下使用 Platforms


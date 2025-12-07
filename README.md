# demo-boot3-g
SpringBoot 3.5.8 及相关组件配置和使用案例


## requirement

- JDK 17 ( jdk 17 + )
- gradle-8.14.3-bin ( gradle 7.2 + )


## gradle 8 使用

### 打包名称

Gradle 8 中用于定制 JAR 包名称的主要属性，帮助你理解不同选项的用途：
> 属性 说明示例输出 (设 project.name='app', project.version='1.0')
- archiveFileName 推荐使用。直接设置完整的文件名。 myapp.jar
- archiveBaseName 设置文件名的基础部分。 myapp-1.0.jar(需与版本等组合)
- archiveVersion 设置文件的版本部分。 app-1.0.jar(需与基础名组合)
- archiveClassifier 设置分类器（如 sources, javadoc）。 app-1.0-sources.jar

最佳实践建议：对于大多数情况，直接使用 archiveFileName 来定义完整的文件名是最简单和清晰的方式。
如果你的构建流程非常标准化，需要严格分离基础名、版本和分类器，则可以考虑使用后面几个属性进行组合。


**使用项目属性动态设置名称**

```gradle
jar {
    archiveFileName = "${project.name}-${project.version}.jar"
}
```

**使用groovy 动态设置名称**


```groovy
// groovy 脚本
tasks.named('jar') {
    //archiveFileName.set("${project.name}-${project.version}.jar")

    archiveFileName.set {
        // 例如，通过-Penv=prod传入环境参数
        def env = project.findProperty('env') ?: 'dev'
        def timestamp = new Date().format('yyyyMMdd')
        "${project.name}-${project.version}-${env}-${timestamp}.jar"
    }
}
```
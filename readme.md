# System iCee

<p align="center" ><del><code>post.png</code></del></p>

<p align="center" >一个用于快速调用Java项目资源文件的工具包</p>

<p align="center" ><a href="#"><del>English</del></a> | <i>Chinese</i></p>
<p align="center" ><a href="errors.md">错误码表</a> | <a href="#"><del>核心程序文档</del></a></p>

<br/>

---

### 简介

System iCee 是一个 Sukazyo Workshop 计划中的跨聊天平台(和系统平台)的 bot 程序。

现在计划上，这个项目将不会再包括任何实际 bot 功能，而只是一个 bot 平台。
当年设计上的各种功能会以插件的形式建立新库 iXxxCee 之类的。

<br/>

### 开发计划

正在开发的内容
- [ ] 配置文件
- [ ] ~~插件~~模块加载系统
- [ ] 生命周期
- [ ] 任务队列以及任务队列
- [ ] 命令接口优化

还要等一等...的内容
- [ ] CommonBot
- [ ] Telegram 支持
- [ ] 更加复杂~~好用~~灵活的启动逻辑

计划/考虑中的内容
- [ ] 多语言的 JavaDoc 之类的（尽管很想但是相关资料很不好找）
- [ ] 自动构建一类的东西（目前看起来并不是那么的必要，而且这个东西配置起来极~麻烦，不过总之还是要有的）
- [ ] iGradleCee，用于 iCee 的~~插件~~外置模块开发环境（不确定是否需要，毕竟太远了，而且 Sukazyo 对此不了解，所以优先级很低）

<br/>

### 开源许可

System iCee 暂时使用 GPLv3 协议，这意味着 iCee 的插件也会被传染上 GPL 许可证。

因此，Sukazyo 正在考虑一些别的许可证，使得 iCee 对插件的开源要求不那么严格。

#### 贡献指南

在当前，随便使用 pull-request / issue 即可。

<br/>

### iCee 背后的巨人们

#### 依赖的开源项目们

- [Mirai QQ](https://github.com/mamoe/mirai) 感谢 mamoe 团队做出的绝妙的支持 Java 的 Kotlin QQ API！
- [Java Discord API](https://github.com/DV8FromTheWorld/JDA) Discord 的 API 类库
- [typesafe/config](https://mvnrepository.com/artifact/com.typesafe/config) HOCON 格式配置文件的 Java 工具类库（但是suk没有找到它的仓库或者官方页面...）
- [log4j2](https://logging.apache.org/log4j/2.x/)
- [Google Gson](https://github.com/google/gson)

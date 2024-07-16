# Bean Nuts Acorn Hydra\<Beta V2.1\> (九头龙，分布式操作系统)
<p align="center">
  <strong>
  事务编排, 多域配置, 自定义线程进程模型, 任务编排管理器, 自定义文件系统, 
  抽象化部署模式和抽象云部署, 高性能RPC, 爬虫，搜索引擎，大数据处理，数据仓库，云计算
   </strong>
</p>

<p align="center">
  <a href="https://Geniusay.github.io/ChopperBot-Doc/">
    <img src="https://img.shields.io/badge/文档-简体中文-blue.svg" alt="简体中文文档" />
  </a>

   <a href="https://github.com/Geniusay/ChopperBot/blob/master/CHANGELOG.md" >
    <img src="https://img.shields.io/badge/ChangeLog-English-blue.svg" alt="Update Log" />
  </a>

   <a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/index.html">
        <img src="https://img.shields.io/badge/JDK-11+-green.svg" />
    </a>
   <a target="_blank" href='https://github.com/Geniusay/ChopperBot'>
        <img src="https://img.shields.io/github/stars/DragonsPrime/Hydra.svg" alt="github stars"/>
   </a>

   <a target="_blank" href=''>
        <img src="https://img.shields.io/badge/Process-Developing-yellow" alt="github stars"/>
   </a>
</p>

<p align='center'>
  <b>简体中文</b> | <a href="https://www.nutsky.com">友情链接</a> | <a href="https://www.dragonking.me">DragonKing</a> 
</p>

## 📖 摘要 / 简介
<p><strong>你想拥有‘上帝之眼’吗？你渴望力量吗？你希望一切信息尽在掌控吗？这个时代，数据即使世界！</strong></p>
<p><strong>Hydra + Radium 成就你的梦想，专为"TJ"人打造的数据“主脑”，一切信息尽在掌握之中，为你构造独一无二的的个人TB-PB级数仓，一切数据为所欲为！</p></strong>
<p>简易和多种部署模式，不一定非要Linux！不一定非要K8S！不一定非要Hadoop！集成统一的丐版配置也能玩转TB-PB级数据。</p>
<p>不会集群？不会中间件？不会分布式？不会云计算？我是小白？Don`t worry，持续更新的保姆级教程和实例最小系统，系统可大可小，丐版技术也能玩转云和大数据。</p>

### 什么是 Hydra，他能干嘛？
Hydra 是由 DragonKing(undefined) 和其团队原创的，面向跨平台PB级别云计算、数据仓库、多任务调度、MapReduce、通信、服务化、抽象化分布式操作系统。
01. 支持统一高度抽象化的任务、事务、服务等编排，一套接口，可分级、可本地、可集群。
02. 可多级、可嵌套的编排系统，支持配置域管理、复杂配置动态解耦、可继承和重写的多域配置管理。
03. 可事务化抽象进程、线程模型，让远端服务通过RPC或通信组件通过一套接口，像本地进程一样进行统一管理。
04. 可事务图化编排方法论设计，就像TensorFlow，更抽象简单的服务、任务设计模式。事务和任务编排支持序列和并行两种模式，更支持性能模式。确保事务绝对执行、回滚、性能执行、并行等多种范式。
05. 面向统一解释器模式方法论和过程化设计，事务和任务编排逻辑化，支持循环控制、条件控制、散转控制、原子化等。
06. 抽象统一任务管理器体系，统一生命周期设计，多类任务一套“任务管理器”，就像本地系统一样简单。
07. 抽象统一系统架构体系，可中心化、可联邦化、可链式化，一切皆有可能。
08. 抽象统一文件系统，基于Common VFS 统一文件系统管理，从复杂底层存储中解放。
09. 抽象统一数据处理体系，泛容器化思想，抽象化DAO、DTO、Data Manipulation架构，一切皆可是Map、List、Set和Table等。
10. 抽象化部署模式和抽象云部署，无论是任何系统、本地进程、虚拟机部署、容器部署等。Hydra为您统一，“小程序”化进程模型，就像Springboot一样简单。
11. 基于分治和MapReduce思想设计，面向大数据处理处理系统设计。
12. 高性能RPC设计基于Netty和NIO，更好的性能呈现。
13. 传统实例化、IOC化、C/C++风格化，多种对象生命周期模式，更有趣的系统设计。
14. 可分级、分组、嵌套、级联的设计方法论，确保更灵活的大型系统设计，确保系统结构清晰、规整、可视、整整齐齐。
15. 无需担心抽象，无需担心"吹牛逼"，我们尽可能通过实际案例和有效代码，展示系统功能，也欢迎commit。——以实现小型爬虫搜索引擎为例。


### 🏆 10万行源码，3A史诗巨献
该仓库为Java 11实现版本（由C/C++版本重构），非GUI部分大部分不直接基于第三方框架。
此外由于本项目工程量、复杂度和工作量较大，本人精力、能力有限，错误和功能完整性不足是不可避免的，还希望各位读者大佬批评指正。\
其次已知Issue部分还有一定工作量，如项目Maven管理目前还不够完善、部分远端任务还未完全实现、C/C++味太重等。\
最后，由于本人精力和工作时间等问题，该公开版本为beta版本，一部分功能未完全实现，后续会不断迭代，欢迎关注。
<p><strong>作为 **TJ 人，完美主义癌，只做大更新，每次更新虽慢但保质保量！</p></strong>

### 子系统、框架和实例系统
#### Bean Nuts Hazelnut Sauron Radium (索伦·镭，分布式爬虫引擎)
该部分为分布式爬虫引擎、爬虫大数据处理、清洗、持久化框架系统的实现。
#### Bean Nuts Hazelnut Sauron Shadow (索伦·暗影，以爬虫、小型搜索引擎为例)
该部分基于Pinecone、Ulfhedinn、Slime、Hydra、Radium等子框架最终设计的搜索引擎（数据采集、数据处理侧）应用实例。
数据检索引擎演示实例参考SauronEyes (https://omnis.nutgit.com, 暂时基于本团队Summer框架、后续会用Springboot重构)



* [一、描述](#一描述)
    * [1.1、框架组成](#11框架组成)
        * [1.1.1、Pinecone 基础运行支持库](#111基础运行支持库)
             * [1.1.1.1、扩展容器](#1111扩展容器)
             * [1.1.1.2、工具库](#1112工具库)
        * [1.1.2、Slime 大数据系统支持框架](#112大数据系统支持框架)
        * [1.1.3、Ulfhedinn 基础运行支持库，第三方依赖版](#113大数据系统支持库)
        * [1.1.4、Hydra 分布式、任务系统框架](#114分布式、任务系统框架)
        * [1.1.5、Radium 分布式爬虫系统框架](#114分布式、任务系统框架)
    * [1.2、功能模块组成](#12功能模块组成)
        * [1.2.1、网络通信库](#121网络通信库)
            * [1.2.1.1、流处理模块](#1211流处理模块)
            * [1.2.1.2、网络操作模块](#1212网络操作模块)
            * [1.2.1.3、非阻塞网络流](#1213非阻塞网络流)
            * [1.2.1.4、常见网络应用协议库](#1214常见网络应用协议库)
            * [1.2.1.5、常见网络通信库](#1215常见网络通信库)

* [二、编译、使用](#二编译、使用)
* [三、目录结构说明](#三目录结构说明)
    * [3.1、TODO](#31TODO)

* [四、使用许可](#四使用许可)
* [五、参考文献](#五参考文献)
* [六、致谢](#六致谢)

## 一、📝 描述
### 1.1、框架组成
#### 1.1.1、Pinecone 基础运行支持库
##### 1.1.1.1、 扩展容器
1. LinkedTreeMap
2. ScopeMap (多域查找树、Map), 实现和支持类似动态语言（如JS、PHP、Python等）的底层继承数据结构，支持两类子模型（单继承、多继承），
可以实现多域查找的功能。
3. Dictium、Dictionary（字典接口模型），实现和支持类似动态语言（如PHP、Python等）的Array、字典查找，Map和可索引对象进一步抽象化。
4. Multi*Map (多种MultiValueMap范式)，实现支持多种多值Map的实现，如MultiCollectionMap、MultiSetMap等。
5. Top (TopN问题通用解决)，实现和支持堆法、有序树法、多值有序树法三种实现。
6. distinct (差异器)，实现传统Set法、分治法、Bloom等的集合差异分析器。
7. affinity (亲缘性器)，实现和支持对亲缘抽象字典的继承、重写等。
8. tabulate (遍历器)，实现以列表式对抽象字典的内部递归，并列表化和分析亲缘关系。
##### 1.1.1.2、工具库
1. JSON库，BSON，JPlus(JSON++)库 (面向可二次开发、设计的自由JSON设计)，可以重写JSONEncoder、JSONDecoder、JSONCompiler、JSONDecompiler、注入器等。
2. Name命名空间库
3. lang (Java包和扩展库)，支持各类类扫描方法、包扫描方法、遍历和收集方法、加载、多域扫描等。
4. TODO
TODO
#### 1.1.2、Hydra 
##### 1.1.2.1、系统架构、骨架设计
1. HierarchySystem，阶级系统(Master-Slaver推广架构)
2. FederalSystem，联邦系统(面向投票式设计) [BETA, 20250101]
3. BlockSystem，块式系统(面向边缘、链式系统设计) [BETA, 20250101]
4. CascadeComponent, 级联组件设计，支持亲缘性回收控制，“The Omega Device”，级联回收主键引用。

##### 1.1.2.2、事物和统一任务编排系统
1. Orchestration (事务、任务编排子系统)，面向统一解释器模式方法论和过程化设计，事务和任务编排逻辑化，支持循环控制、条件控制、散转控制、原子化等，更支持事务完整性设计。
2. Auto (简易命令模式，可编程自动机系统)，实现支持Automaton简易生产-消费命令队列，实现支持PeriodicAutomaton可编程Timer，实现支持Marshalling流水线指令编排器。(更多Timer和算法持续更新中)
##### 1.1.2.3、小程序系统，统一任务调度系统
Servgram，小程序系统，是的这很微信，不过是服务端的小程序哦！进一步抽象和推广进程思想，任何服务介质（本地、虚拟机、容器等），一切服务、一切任务等。
一切统一和谐，一套调度、一套接口、一套操作，生命周期整整齐齐（满足你的控制欲），更可冗余确保稳定。\
配合任务编排和事务编排，多个任务，一套系统全包干。
(TODO，远端进程进一步实现、实现统一分布式锁接口)
##### 1.1.2.4、统一消息分发系统
##### 1.1.2.5、WolfMC RPC
二进制RPC协议支持，更多RPC协议持续更新中。
##### 1.1.2.6、统一服务注册、发现、管理系统［TODD］
##### 1.1.2.7、统一资源管理、分配接口系统［TODO］
##### 1.1.2.8、图形管理界面［TODO］
##### 1.1.2.9、TODO

#### 1.1.3、Slime 史莱姆大数据支持库
##### 1.1.3.1、统一块抽象、管理、分配系统（泛块式、抽象页面（连续、离散、自定义）、帧、分区、簇等）
##### 1.1.3.2、Mapper、Querier 抽象映射、查询器，统一接口多种实现（本地、数据库、缓存、数据仓库等）
##### 1.1.3.3、统一缓存库和查询优化库、支持LRU、冷热优化、页面缓存、页面LRU、多级缓存等多策略实现。
##### 1.1.3.4、Source抽象数据源库、支持RDB-ibatis、NoSQL、缓存、文件等扩展。
##### 1.1.3.5、Reducer库[TODO]，更多Reduce策略实现、接口

#### 1.1.4、Radium 分布式爬虫和搜索引擎数据取回、任务编排、处理、持久化框架
##### 1.1.4.1、一站式爬虫数据处理范式
基于Map-Reduce思想，面向TB-PB级别数据处理，统一任务编号、映射、处理。
范式包含 Reaver（掠夺者，数据取回器），Stalker（潜伏者，面向批量爬虫索引嗅探），Embezzler（洗钱者，面向批处理爬虫数据处理）。
##### 1.1.4.2、统一多任务调度、配置、编排系统
支持事务型、Best-Effort等多种任务粒度控制。
支持分组、嵌套、多级任务调度，支持子任务继承父任务关系、血缘性。
支持任务回滚、熔断等接口设计。
TODO



## 四、🔬 使用许可
- MIT (保留本许可后，可随意分发、修改，欢迎参与贡献)

## 五、📚 参考文献
(参考文献包括Nuts家族 C/C++、Java等子语言运行支持库、本项目框架、本项目等所有涉及的子项目的总参考文献、源码、设计、
专利等相关资料。便于读者了解相关技术（设计）的源头和底层方法论，作者向相关参考项目（以及未直接列出项目）作者表示崇高敬意和感谢。)
01. C/C++ STL (容器、运行支持库设计，算法、设计模式和数据结构)
02. Java JDK  (容器、运行支持库设计，算法、设计模式和数据结构)
03. PHP 5.6 Source (解释器、相关支持库设计)
04. MySQL Source (参考多个设计思想和部分思想实现)
05. Linux Kernel (参考多个设计思想和部分思想实现)
06. Win95 Kernel (Reveal Edition)，Win32Apis，Runtime framework
07. WinNT 窗口事件思想、回调函数注入等
08. C/C++ Boost
09. C/C++ ACL -- One advanced C/C++ library for Unix/Windows.
10. Java Springframework Family (How IOC/AOP/etc works)
11. Hadoop MapReduce (How it works)
12. Python TensorFlow (Graph, how it orchestras)
13. Javascript DOM 设计、CSS选择器等
14. 其他若干个小框架、工具库、语言等（如Apache Commons、org.json、fastcgi、libevent等），本文表示崇高敬意和感谢。

# 📈 项目活跃表
![Alt](https://repobeats.axiom.co/api/embed/0ae23655bb105addf8d90a999df36f690d615af7.svg "Repobeats analytics image")

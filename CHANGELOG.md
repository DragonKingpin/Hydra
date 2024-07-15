# 更新日志

```markdown
   格式：
    ## [版本号] - 日期
    ### 模板名称 (可选 console-ui, console, FileModule, common, ...)
    - 🎈新增: {模块名称} {功能介绍}
    - 🐞Bug: #{issue号} {bug描述}
    - ⛏修复: #{issue号} {修复描述}
    - 📝文档: {文件名} 添加注释
    - 🚀性能: {类} {方法} {描述}
    - 🎨样式: 
    - 🧹重构:
    - 🧪测试: {类|方法} {测试结果}
    - 🛑更名: {旧名} ➡ {新名}
    - ❌移除: {模块|方法}
    - 🚧施工


    ------

```

------
# 目录
* [V 1.0.1]()
* [V 1.0.0]()
------
## [V 1.0.0] - 2023.5.18
### Messenger
- ❌移除: `Message,MessageType`,移除所有的模块中与`Message`有关的代码
- 🧹重构: 重构`Message`为`UlfUMCMessage`,构建了全新协议UlfUMC
- 🎈新增: 新增`MessageBuilder,MessageFactory`用来构建专属的`UlfUMCMessage`类
------
## [V 1.0.0] - 2023.5.13
### 🎈 TaskJuggler
任务调度模块，主要负责节点的任务分配和处理
- 🎈新增: 新增`Heist 劫匪` 作为`HeistCenter`的工作线程，负责处理单个任务，有失败重试机制
- 🎈新增: 新增`HeistCenter` 任务调度中心,负责初始化任务进度，与`Harbor 港口`通信，是整个任务调度的**核心类**
- 🎈新增: 新增`HeistConfig` 作为整个`Heist`家族的配置类
- 🎈新增: 新增`Harbor 港口` 与 Master节点通信的核心类，目前具备向master节点发送任务查询申请，任务缓存，`HeistCenter`任务获取
- 🎈新增: 新增`MqConfig` 消息队列初始化
### 🎈 com.pinecone
整个项目的核心公共代码所在地
- 🎈新增: 新增`SystemConfig` 整个系统的配置类，可获取一些系统的全局变量
- 🎈新增: 新增`RadiumConstPool` 项目的公共常量池，存放一些常量
- 🎈新增: 新增`SystemUtils` 系统工具类,定制一些独属于该系统的工具
- 🎈🚧新增: 新增`TimeUtil` 时间工具 (不推荐使用)
### 🎈 Messenger
负责定义消息类型，消息结构以及消息队列的一些全局变量管理，专门用来定义消息的模块
- 🎈新增: 新增`Message` 消息类，目前消息队列通信的**核心类**
- 🎈新增: 新增`MessageType` 消息类型类,目前有`Query,Post,Reply,ReplyPost,ShutDown`
- 🎈新增: 新增`MessageConverterConfig` 主要负责mq中类传输的转化
- 🎈新增: 新增`FunctionNamePool` 主要存放Master中对应的方法名称
- 🎈新增: 新增`MqPool` 消息队列全局变量池
### 🎈 Console
项目启动模块
### 🎈 File
文件操作模块
- 🎈新增: 新增 `JsonFileUtil` 工具类，用于进行json文件的读写操作
- 🎈新增: 新增 `FileUtil` 工具类，用于进行文件复制文件删除等操作
- 🎈新增: 新增 `FileCondition` 方法，用于对文件递归删除进行条件过滤
- 🧪测试: 测试 `FileUtil` 工具类, 测试 `JsonFileUtil` 工具类
- 🎈新增: 新增 `FileCacheManagerInstance` 将整个FileCacheManager转变为全局单例，防止重复使用调用
- 🎈新增: 新增 `GlobalFileCache` 全局文件缓存，也负责为`FileCacheManagerInstance`提供初始化的文件缓存队列
- 🎈新增: 新增 `FileCache` 文件缓冲池类，负责缓存文件内容，文件的读取，修改，追加，能够根据刷入时间或者写入字节，来进行自动刷盘操作
- 🎈新增: 新增 `FileCacheManager` 文件缓冲池管理类，管理所有文件缓存池，轮询查看每个文件是否需要自动刷入，目前包含巡逻线程与刷入线程
- 🎈新增: 新增 `FileCacheManagerInit` 用于启动初始化FileCacheManager
------

# Hydra Lepton Architecture Top Schema

## 定位

`Lepton` 分支是 Hydra 世界观下的轻量系统架构分支。

其设计目标不是构建一个重型中央控制平台，而是构建一个：

- 最小宿主
- 弱制度
- 边缘化
- 去中心化
- 可按需挂接能力

的轻系统原型。

这条路线用于承载：

- 小系统
- 局部自治系统
- 边缘节点系统
- 插件式宿主
- 轻量服务壳体
- 不需要帝国化治理的业务系统

---

## 顶层继承 Schema

```text
Pinecore
└─ Hydraco
   └─ Drake
      └─ DrakeHypha
         └─ Lepton
            └─ Meson
               └─ ConcreteLightSystem
```

---

## 核心对象分层

### 1. Hydraco

最小 Hydra 语义归属接口。

语义：

- 表示该系统仍然属于 Hydra 语义家族
- 仅保留最基础的系统元归属
- 不携带 Kingdom / Centrum / Hierarchy 等制度语义

### 2. Drake

轻架构分支的元原型。

语义：

- 标记该系统属于 Drake 轻分支
- 与重型 `Hydrogen` 分支并列
- 表达“轻系统不是重系统的裁剪版，而是另一条正交路线”

### 3. DrakeHypha

菌丝式系统抽象。

语义：

- `Drake + BlockSystem`
- 指向 block / edge / decentralized 风格
- 强调弱节点、边缘节点、局部扩张、柔性连接

### 4. Lepton

轻系统实现协议。

语义：

- `DrakeHypha + Slf4jTraceable`
- 要求一个轻系统至少可以被追踪、被记录生命周期
- 不要求系统具备中央管理、组件树、层级制度

### 5. Meson

轻系统典型抽象基类。

语义：

- 作为 `Lepton` 的 archetype
- 提供最小启动外壳
- 提供统一 welcome / booting / lifecycle logging
- 不提供重型平台能力

---

## 设计原则

### 1. 最小宿主原则

`Meson` 只提供“系统壳体”能力：

- 启动
- logger
- lifecycle trace
- 基础构造器透传

它不应默认承担平台职责。

### 2. 去制度化原则

`Lepton` 路线不预设：

- 中央控制
- 王国结构
- 服务等级制度
- 联邦治理
- 进程帝国

### 3. 能力后挂原则

所有能力以可选挂件方式进入，而不是默认全家桶注入。

推荐模式：

```text
Meson
├─ Logging
├─ LifecycleHooks
├─ OptionalConfigProvider
├─ OptionalTaskLauncher
├─ OptionalResourceFacade
└─ BusinessDomain
```

### 4. 不升级原则

若系统开始需要：

- hierarchy
- centrum
- federation
- imperium
- process/image
- heavy middleware

则说明该系统已经越过 `Lepton` 路线边界，应迁移到 `Hydrogen` 路线，而不是继续污染 `Meson`。

---

## 默认内建能力

`Lepton / Meson` 默认只建议内建：

- `Framework` 基础生命周期能力
- `Slf4jTraceable`
- logger 初始化
- 生命周期日志输出
- 统一启动 welcome 语义

---

## 默认禁止内建能力

`Lepton / Meson` 默认不应直接内建：

- `HierarchySystem`
- `Hydrogen`
- `HydraKingdom`
- `Centrum`
- `UniformCentralSystem`
- `HySkeleton`
- `SystemComponentManager`
- `ScopedSystem`
- `ProcessManager`
- `UProcess`
- `ImageLoader`
- `ImperiumPrivy`
- `KernelLordFederation`
- `KernelMicroSystemCabinet`
- `StorageSystem`
- `SystemDaemon`
- `InterWareDirector`
- `ServersScope`
- `GlobalConfigScope`

---

## 系统角色定义

### Lepton 的系统角色

`Lepton` 的系统角色是：

- 一个轻量宿主
- 一个业务承载壳
- 一个能力挂载点
- 一个边缘自治节点

而不是：

- 中央控制核心
- 分布式帝国主脑
- 重型操作系统宿主

---

## Meson 的职责边界

`Meson` 应负责：

- 提供轻系统公共抽象基类
- 收敛统一日志风格
- 收敛统一启动输出
- 作为后续具体轻系统的共同父类

`Meson` 不应负责：

- 统一系统调度
- 全局资源编排
- 中央配置作用域
- 多层级治理体系
- 跨子系统联邦
- 进程化系统平台

---

## 推荐扩展层次

建议 `Meson` 子类的能力增长层次如下：

```text
ConcreteLightSystem
├─ BaseLogging
├─ BaseLifecycle
├─ LocalConfig
├─ OptionalModuleLoader
├─ OptionalTaskFacade
└─ DomainLogic
```

不建议一上来扩成：

```text
ConcreteLightSystem
├─ ProcessManager
├─ ImageLoader
├─ Imperium
├─ Federation
├─ GlobalScope
└─ CentralControl
```

---

## 抽象定义

可以将 `Lepton / Meson` 的顶层设计抽象定义为：

> `Lepton` 是 Hydra 世界观下的轻系统实现协议，面向 block / edge / decentralized 的弱制度系统。
>
> `Meson` 是 `Lepton` 的典型抽象基类，只提供最小生命周期壳体与追踪能力，不承载 `Hydrogen` 路线中的中央化、层级化与平台化系统职责。

---

## 适用场景

推荐使用 `Lepton / Meson` 的场景：

- 单体轻服务
- 嵌入式业务宿主
- 本地执行代理
- 小型边缘节点
- 工具型系统壳
- 插件容器
- 不需要中心治理的局部能力服务

不推荐使用 `Lepton / Meson` 的场景：

- 大型分布式调度平台
- 需要 hierarchy 的系统
- 需要 centrum 的系统
- 需要统一 process/image 管理的系统
- 需要 subsystem federation 的系统

---

## 一句话 Schema

`Lepton / Meson` 不是缩小版 `Hydrogen / Tritium`，而是 Hydra 体系中一条独立的轻宿主路线：以最小制度成本复用框架基础设施，并允许业务系统按需增量挂接能力。

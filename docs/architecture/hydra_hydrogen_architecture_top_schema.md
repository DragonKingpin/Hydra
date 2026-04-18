# Hydra Hydrogen Architecture Top Schema

## 定位

`Hydrogen` 分支是 Hydra 世界观下的重型系统架构主线。

其目标不是提供一个最小宿主，而是提供一个：

- 可制度化
- 可层级化
- 可平台化
- 可中央化
- 可操作系统化

的系统宿主骨架。

这条路线面向：

- 大型系统
- 平台系统
- 中央控制系统
- 分布式治理系统
- 多子系统协作系统
- 需要统一进程、镜像、资源、配置和联邦治理的系统

---

## 顶层继承 Schema

```text
Framework
└─ Hydra
   └─ Hydradom
      └─ Tritium
         └─ EnderHydra
            └─ ConcreteEmpireSystem
```

接口抽象主线：

```text
Hydraco
└─ Hydrogen
   └─ HydraKingdom
      └─ TritiumSystem
         └─ HydraEmpire
```

---

## 核心对象分层

### 1. Hydraco

最小 Hydra 元归属接口。

语义：

- 表示系统处于 Hydra 家族下
- 为后续重型系统协议提供根归属

### 2. Hydrogen

重型系统总协议。

语义：

- `Hydraco + ScopedSystem + MultiComponentSystem`
- 要求系统具备：
  - 组件管理器
  - 服务标识
  - 工作路径
  - Debug 状态
  - TracerScope

这是 Hydra 重系统路线的真正入口协议。

### 3. Hydra

`Hydrogen` 的基础抽象实现。

语义：

- 提供 `HySkeleton`
- 提供系统级 component manager
- 提供 `serviceID / workingPath / debugMode`
- 提供重型系统宿主底座

### 4. HydraKingdom

`Hydrogen + HierarchySystem`

语义：

- 系统不仅是宿主，而且被赋予层级治理语义
- 将系统纳入 Kingdom 体系

### 5. Hydradom

Kingdom 级基础抽象实现。

语义：

- 在 `Hydra` 基础上挂入 `KernelMicroSystemCabinet`
- 为微系统、子系统装配提供容器

### 6. Tritium

Hydra 重型主线的典型中央集权实现。

语义：

- 实现 `HierarchySystem`
- 建立 `Master / Paladin / Minion / Slave` 金字塔语义
- 内建重型系统骨架与公共基础设施

### 7. EnderHydra

Tritium 路线的帝国化落地实现。

语义：

- 在 Tritium 基础上继续装配：
  - process subsystem
  - image subsystem
  - imperium privy
  - lord federation
- 成为“可运行的中央帝国系统”

---

## Hydrogen 分支的制度结构

### 1. Component System

`Hydrogen` 路线天然拥有组件管理制度：

- `HySkeleton`
- `SystemSkeleton`
- `SystemCascadeComponentManager`
- 级联组件树

这意味着系统是“组件宿主”，而不只是类实例。

### 2. Scope System

`Hydrogen` 路线天然拥有作用域系统：

- `ScopedSystem`
- global config scope
- primary config scope

这意味着系统是“全局作用域宿主”。

### 3. Hierarchy System

`HydraKingdom / Tritium` 路线天然拥有层级制度：

- `HierarchySystem`
- `Master`
- `Paladin`
- `Minion`
- `Slave`

这意味着系统在设计上天然带治理阶级。

### 4. Centralized System

当系统进入 `Centrum / UniformCentralSystem` 语义后，将具备：

- GUID allocator
- kernel object fundamental config
- imperium privy
- centralized control plane

这意味着系统在设计上天然走向“中央元系统”。

### 5. Federation System

在 `Hydradom / Tritium / EnderHydra` 路线中，系统可以继续长出：

- `KernelMicroSystemCabinet`
- `KernelLordFederation`
- 多子系统联邦

这意味着系统天然支持帝国化子系统组织。

---

## Tritium 的默认基础设施

`Tritium` 级系统默认内建：

- tracer scope
- config scope
- middleware director
- servers scope
- storage system
- system daemon
- resource dispenser center
- dynamic factory
- service hierarchy
- lifecycle logging

这说明 `Tritium` 已经不是简单宿主，而是系统平台。

---

## EnderHydra 的继续加重

`EnderHydra` 在 `Tritium` 基础上继续默认装配：

- system guid allocator
- guid allocator72
- virtual exe image instrument
- image loader
- process manager
- root proxied uprocess
- kernel object config
- imperium privy
- central kernel lord federation
- skynet subsystem
- redqueen central control

这意味着 `EnderHydra` 已经进入“帝国中央操作系统”语义，而不再是普通应用系统。

---

## Hydrogen 分支的职责边界

`Hydrogen` 路线适合承担：

- 重型宿主职责
- 平台职责
- 制度职责
- 中央控制职责
- 系统治理职责
- 多子系统编组职责

它不适合：

- 极轻量单体系统
- 插件壳系统
- 只需要 logger + lifecycle 的小宿主
- 不需要中央控制的小型工具系统

---

## 推荐使用条件

当一个系统具备如下任一需求时，推荐进入 `Hydrogen` 路线：

- 需要 component manager
- 需要统一 config scope
- 需要 hierarchy
- 需要 micro system cabinet
- 需要 federation
- 需要 process manager / image loader
- 需要 central control
- 需要 imperium

---

## 不应滥用条件

若系统只需要：

- 基础启动
- 日志
- 局部模块装配
- 少量本地任务
- 单体业务逻辑

则不应直接使用 `Hydrogen / Tritium / EnderHydra` 路线，否则会引入过量制度负担。

---

## 抽象定义

可以将 `Hydrogen` 顶层设计抽象定义为：

> `Hydrogen` 是 Hydra 世界观下的重型系统总协议，它将系统定义为一个可被组件化、作用域化、层级化、中央化和制度化治理的宿主平台。
>
> `Tritium` 是该路线的典型中央集权 archetype，`EnderHydra` 则是进一步具象化为帝国型中央操作系统宿主的典型落地实现。

---

## 适用场景

推荐使用 `Hydrogen` 路线的场景：

- 大型分布式系统
- 多节点治理系统
- 中央平台系统
- 需要统一进程与镜像管理的系统
- 需要子系统联邦与组织层级的系统
- 操作系统式业务平台

不推荐使用 `Hydrogen` 路线的场景：

- 小型工具系统
- 插件壳系统
- 轻量边缘节点
- 本地自治单体

---

## 一句话 Schema

`Hydrogen` 路线是 Hydra 体系中的重型平台主线，它将系统建模为一个具备组件树、作用域、等级制度、中央控制和平面治理能力的宿主平台，并可进一步演化为 `Tritium / EnderHydra` 这样的帝国型系统。

# Odin / Troll / Proc 架构速记

短文档，用于快速恢复远程进程控制与任务执行上下文。

## 当前边界

近期停在 `pipeCreate`：

```text
任务实例调度
  -> Dispatcher
  -> TrollTaskExecutionLauncher
  -> RemoteProcessManagerServer.createRemoteUProcess
  -> MediatedRemoteProcess mirror
```

暂不全面打通完整 Launch 生命周期。

## 模块 Topo

```text
odin-architecture
  抽象接口、实体、控制语义

odin-framework-runtime
  RPC 无关的 RemoteProcessManagerServer core

odin-runtime-husky-control
  Husky/WolfMC 双工控制实现

odin-runtime-grpc-control
  后续 gRPC 控制实现

odin-framework-conduct
  Regiment / Dispatcher / Scheduler / Troll launcher
```

## RPC 口径

```text
WolfMC = 底层 Netty++ 通信库
Ulf    = 节点/接口抽象体系
Husky  = 基于 WolfMC 的双工 RPC 标准实现
```

不要叫 Wolf control / Ulf control，本线统一叫 Husky control。

## 核心拓扑

旧拓扑：

```text
Regiment
  -> RavenRemoteProcessManagerServer
     -> DuplexAppointServer
        -> Husky/WolfMC
```

问题：`RavenRemoteProcessManagerServer` 同时管业务 core、RPC、Husky 生命周期、controller 注册。

新拓扑：

```text
Regiment
  -> RavenRemoteProcessManagerServer
     -> RemoteProcessControlTransportRegistry
        -> HuskyRemoteProcessControlTransport
           -> DuplexAppointServer
              -> Husky/WolfMC
```

原则：`RavenRemoteProcessManagerServer` 不再依赖 Husky/WolfMC 类型。

## 生命周期 Topo

当前正确顺序：

```text
Regiment.startRemoteProcessServer()
  register ProcessorLifecycleController
  compile ProcessorLifecycleIface
  start transport

HuskyTransport.startService()
  new WolvesAppointServer
  register ReactiveSlaveProcessLifecycleController
  compile MasterProcessLifecycleIface
  flush pending controllers
  flush pending iface compiles
  execute
```

三类动作：

```text
declare: hook transport / register controller / compile iface
prepare: 创建内部对象 / 注册内建控制器 / flush pending
start:   execute，开始接流量
```

## 两个原子语义

不要再混成 `mountController`。

```text
registerController(Object controller)
  注册本地 RPC handler/controller

compileIface(Class<?> ifaceClass, boolean bAsIface)
  Husky 专属：把 Java @Iface 当运行期 proto 契约编译
```

`Iface` 在 Husky 中相当于 Java runtime proto：

```text
Java Interface -> ClassDigest / MethodDigest -> Dynamic Protobuf
```

gRPC 不支持 runtime iface compile；后续直接抛 `NotImplementedException`。

多 transport 下不能无脑广播 `compileIface`，当前通过：

```text
supportsRuntimeIfaceCompile()
```

控制。

## Join Regiment 链路

```text
Legionary.joinRegiment()
  -> ProcessorLifecycleIface.joinRegiment(request)
  -> ProcessorLifecycleController.joinRegiment(request)
  -> CollectiveTaskRegiment.invokeJoinRegiment(request)
  -> RavenTaskDispatcher.registerProcessor(name, clientId)
```

如果 processor 不存在，应返回带 `errorMsg` 的 `RegimentJoinResponse`，不应返回 `null`。

## 本轮踩坑

### controlRPCDriver 名称

`odin.json5`：

```text
controlRPCDriver = WolfKing
```

测试态必须按同名注册到 IoC 容器。主配置里 `WolfKing Enable=false` 时，`MessagersManager` 不会自动创建。

### 重复启动

`Odin.vitalize()` 已经会启动 remote process server。测试里再次调用 `startRemoteProcessServer()` 时，transport 必须幂等：

```text
重复 start pass
重复 registerController pass
重复 compileIface pass
```

### 抽象边界

错误抽象：

```text
mountController(controller, iface)
```

原因：混合了 controller 注册和 iface 编译两个原子动作。

当前抽象：

```text
registerController(controller)
compileIface(iface, false)
```

## 底线

1. `Regiment` 不直接拿 `DuplexAppointServer`。
2. `RavenRemoteProcessManagerServer` 不依赖 Husky/WolfMC。
3. Husky runtime compile 留在 Husky transport。
4. gRPC 不假装支持 `compileIface`。
5. 多 transport 操作必须看 capability。

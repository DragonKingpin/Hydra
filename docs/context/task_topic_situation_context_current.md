# Task Topic Situation Context Current

- Type: Current Reduced Context
- Language: zh_cn
- Scope: Odin / Hydra 任务调度专题；实例推进、出港检查、远程投递、状态回写。
- Reduction Date: 2026-05-01
- Sampling: 聚焦采样；覆盖 `RavenTaskScheduler`、`RavenTaskSchedulePreparator`、`RavenInstanceScheduleImpetus`、dispatcher / processor / launcher 链路、实例 mapper XML、当前开发库 DDL 片段与本轮对齐结论。
- Rule: 本文件是 Task 调度专题的当前态势入口，不是 Saurons 全项目总入口；后续恢复该专题时优先读本文件，再读代码锚点。

## 1. 专题定位

### [Confirmed] 当前系统角色

Task 专题是 Odin / Hydra 体系内的任务调度与任务实例执行链路。

目标链路不是简单 job runner，而是分阶段实例推进系统：

```text
任务到达调度时间
-> 生成任务实例
-> 冻结实例依赖图
-> 出港依赖检查
-> 资源 / 配额适配
-> 分派到执行处理器
-> 本地或远程进程创建与启动
-> 实例 / 执行记录 / 事件状态回写
-> 下游实例在后续推进轮次中重新变为可出港候选
```

当前讨论中的“打通调度链路”，至少包括三条主线：

```text
自动调度
自动发送到服务器
自动更新状态
```

资源系统暂不作为本轮核心目标，但时间周期必须纳入自动调度设计。

### [Confirmed] 当前主入口

当前调度门面：

```text
Odin/odin-framework-conduct/src/main/java/com/walnut/odin/conduct/schedule/RavenTaskScheduler.java
```

当前 `fetch()` 仍是人工 / 外部 tick 风格，且只推进已有实例：

```java
//this.mTaskSchedulePreparator.prepareSchedulableTasksDaily( LocalDateTime.now() );
this.mInstanceScheduleImpetus.impelPrelaunchInstances( LocalDateTime.now() );
```

含义：

```text
当前不会自动生成新周期实例。
当前只会把已经存在、且处在预出港相关状态的实例继续往后推。
```

因此，不能直接把自动 tick 打开。先要解决实例生成幂等性、已有实例启动路径、时间周期语义和冻结依赖生成。

## 2. 当前施工纪律

### [Rule] 读写纪律

- Windows 文本文件读取强制按 UTF-8。
- 不默认跑测试；用户明确要求后再跑。
- 不默认新增测试文件；用户明确要求后再写。
- 不做大范围目录扫描；本项目体量巨大，按锚点定向阅读。
- 文档中如存在 `<!-- impregnable --> ... <!-- impregnable /-->` 块，不得修改其中内容。

### [Rule] Java 风格

- 使用 `this`，不省略。
- 控制流与方法调用保持现有空格风格，例如 `method( arg )`。
- 花括号不省略。
- 成员变量沿用现有本地 Hungarian-ish 风格。
- 新增顶层接口应进入 `Pinenut` 体系。

### [Rule] SQL / MyBatis 风格

- 不使用 `SELECT *`。
- MyBatis `@Param` 名称不用 Hungarian 记法。
- GUID 值直接使用 `GUID` 类型。
- XML 中必要时用 CDATA 处理比较符。
- 本轮不调整索引和表结构；相关索引存在历史与代码约束。

## 3. 当前组件态势

### [Confirmed] 调度组件

```text
RavenTaskScheduler
    -> RavenScheduleAllocator
    -> RavenTaskSchedulePreparator
    -> RavenInstanceScheduleImpetus
```

分工：

```text
RavenTaskSchedulePreparator
    扫描可调度任务，准备任务实例，后续应负责冻结实例依赖。

RavenInstanceScheduleImpetus
    推进预出港实例，当前已承接依赖检查与资源适配前置门。

RavenScheduleAllocator
    做优先级 / 资源配额适配；本轮资源细节先不展开。
```

### [Confirmed] 投递与启动组件

```text
RavenTaskDispatcher
-> RavenTaskExecutionProcessor
-> TrollTaskExecutionLauncher
-> local or remote UProcess
```

远程进程创建链路已经存在：

```text
TrollTaskExecutionLauncher.createRemotely
RemoteProcessManagerServer.createRemoteUProcess
```

但“远程进程代码存在”不等于自动投递链路已经完整。还要看 processor 注册、远程 client 绑定、指定 processor 语义、dispatch 策略和失败状态回写。

## 4. 本轮已完成施工

### [Confirmed] 出港依赖检查已落地

旧注释“依赖 mapper 记得看看”对应的第一道出港门已经完成。

变更文件：

```text
Odin/odin-architecture/src/main/java/com/walnut/odin/conduct/schedule/entity/DependencyBlockage.java
Odin/odin-framework-conduct/src/main/java/com/walnut/odin/conduct/schedule/entity/GenericDependencyBlockage.java
Odin/odin-architecture/src/main/java/com/walnut/odin/task/mapper/InstanceAtlasNodeMapper.java
Odin/odin-framework-atlas/src/main/resources/mapper/kernel/task/InstanceAtlasNodeMapper.xml
Odin/odin-framework-conduct/src/main/java/com/walnut/odin/conduct/schedule/RavenInstanceScheduleImpetus.java
```

新增批量依赖阻塞查询：

```text
InstanceAtlasNodeMapper.fetchDependencyBlockages( instanceGuids, finishedStatus )
```

返回领域对象 `DependencyBlockage`，不是临时 map。

### [Confirmed] 当前出港推进规则

`impelSchedulableInstances` 当前流程已经收紧为：

```text
按 ID 窗口拉取候选实例
-> 批量查询依赖阻塞
-> New / DependencyWait 先过依赖门
-> 无依赖阻塞则进入 ResourceWait
-> ResourceWait 进入资源适配
-> 适配成功后进入 DepartureStandby
-> 只有 DepartureStandby 可以进入 pipeLaunch
```

硬规则：

```text
DependencyWait 不可启动。
ResourceWait 不可直接启动。
只有 DepartureStandby 可以进入 pipeLaunch。
```

### [Confirmed] 冻结依赖读取模型

当前出港检查假设实例依赖已经冻结完成，只消费冻结结果。

本轮确认使用三表模型：

```text
odin_task_instance_atlas_nodes child_node
    child_node.instance_guid = 当前实例 GUID
    child_node.guid          = 当前冻结实例图节点 GUID

odin_taks_ins_atlas_adjacent instance_edge
    instance_edge.guid        = 当前冻结实例图节点 GUID
    instance_edge.parent_guid = 上游实例 GUID

hydra_task_instances parent_instance
    parent_instance.guid       = 上游实例 GUID
    parent_instance.run_status = 上游实例运行状态
```

`parent_instance.run_status IS NULL` 视为未完成。

注意：`odin_taks_ins_atlas_adjacent` 的表名拼写就是当前开发库结构，不要自行纠正成 `task`。

## 5. 当前核心卡口

### [Critical Open] 已有实例启动路径可能重新初始化实例

这是当前最危险的执行链路问题。

预出港推进处理的是已经由准备阶段插入过的实例。但启动链路中：

```text
TrollTaskExecutionLauncher.launchLocally / launchRemotely
-> createLocally / createRemotely
-> initializeInstance
```

而 `initializeInstance` 会向 `hydra_task_instances` 插入实例。

风险：

```text
准备阶段已经插入实例
推进阶段启动已有实例
启动链路又尝试 initialize / insert
```

这会导致重复插入、状态污染或语义错位。自动调度打开前，必须拆开：

```text
create / initialize new instance
    只用于创建新实例记录。

launch existing prepared instance
    用于启动已经存在的调度实例，只更新状态、执行记录和进程信息。
```

### [Open] 时间周期语义尚未收紧

时间周期集中在：

```text
RavenTaskSchedulePreparator.prepareTaskScheduleTimeOffset
```

已知风险：

- `next == null` 时把 `thisScheduleTime` 设为 `LocalDateTime.now()`，语义偏临时。
- `advanced != next` 后更新 task node 的代码目前是注释状态。
- `business_time`、`expect_time`、`next_schedule_time` 尚未明确绑定关系。
- `LaunchFeature.bizTimeEpoch` 默认是 `now - 1 day`，`prepareInstance` 没有从 `TaskScheduleContext.thisScheduleTime` 显式传入。
- 重复 tick 可能产生重复实例。

自动调度要成立，必须先把以下语义定死：

```text
next_schedule_time
thisScheduleTime
expect_time
business_time
run_count / sequence
```

### [Open] 实例生成幂等性未完成

`prepareSchedulableTasksDaily` 会从可调度 task node 生成实例。

打开自动调度前，需要至少有一条防重复规则：

```text
task_guid + business_time + run_count / sequence
```

初期可先 mapper 查询防重，后续若表约束允许，再考虑唯一索引或更强约束。

当前不调整索引。

### [Open] 冻结实例依赖生成未完成

出港检查现在只消费冻结依赖，不读取实时任务图。

但 `RavenTaskSchedulePreparator.prepareInstanceLineage` 仍处于过渡状态：

```text
从 runtime task graph 读取父节点
插入 instance atlas node
邻接边语义尚未完全对齐“冻结实例依赖”
```

最终需要的准备阶段冻结逻辑：

```text
当前 task instance
-> 当前 task 的上游 task nodes
-> 同一业务周期 / 调度批次下的上游 task instances
-> 写入 odin_task_instance_atlas_nodes / odin_taks_ins_atlas_adjacent
```

这部分不应塞回出港检查里。出港检查只判断“上游实例是否完成”。

### [Open] Processor 投递需要运行态闭环

远程进程链路已经写完，但自动发送到服务器还取决于：

- processor 记录是否已配置。
- remote client 是否已注册到 dispatcher。
- `controlClientId` 是否可用。
- dispatch strategy 是否能选出可用 processor。
- `LaunchFeature.withProcessorDesignated` 的指定 processor 语义是否与 DB / runtime 一致。
- 远程创建失败时是否能回写实例状态和错误事件。

这条线要按“可投递、可失败、可恢复”看，不只看 happy path。

### [Open] 状态回写尚不完整

当前 launcher 已有若干主状态写入：

```text
ProcessStandby
Running
Finished
```

还缺少稳定闭环：

```text
Error
Killed
remote create failed
local process start failed
process finished with non-zero result
dispatcher no processor available
```

需要统一确认每个状态变化写哪些表：

```text
hydra_task_instances
odin_task_instance_exec
odin_task_instance_event
```

以及字段所有权：

```text
submit_time
schedule_time
start_time
finish_time
exec_state
run_status
message / error
```

### [Open] 事件流需要补齐，但不做大重构

已有事件类型包括：

```text
TaskTimeReady
CheckDependencyReady
DepartureReady
TaskRun
TaskExecSuccess
TaskExecFail
TaskSuccess
TaskFail
TaskKilled
```

当前事件写入并不完整。后续应把关键状态迁移绑定事件，但不在本轮重写成完整 event-sourcing 系统。

### [Open] 下游唤醒当前可先走轮询

当前可接受策略：

```text
上游完成
-> 下一轮 impelPrelaunchInstances 扫描 DependencyWait
-> 依赖检查重新通过
-> 下游继续推进
```

事件驱动唤醒可以后续做，不作为当前打通链路的阻断项。

## 6. 临界施工顺序

### [Priority 1] 拆分已有实例启动路径

先处理最危险点：

```text
不要在启动已有实例时再次 initialize / insert instance。
```

建议施工边界：

```text
TrollTaskExecutionLauncher
RavenTaskExecutionProcessor
RavenTaskDispatcher
必要的 instance / exec mapper 方法
```

目标：

```text
DepartureStandby 实例进入 launch existing path。
只更新状态、执行记录、进程绑定信息。
不新增 hydra_task_instances 记录。
```

### [Priority 2] 收紧状态迁移表

围绕启动路径先建立最小状态闭环：

```text
DepartureStandby
-> ProcessStandby
-> Running
-> Finished / Error / Killed
```

每一步明确：

```text
实例状态写什么
exec_state 写什么
事件写什么
失败信息写哪里
```

### [Priority 3] 整理 processor 投递门

确认自动远程投递最小条件：

```text
processor 可发现
remote client 可绑定
dispatch strategy 可选中
designated processor 可被尊重
不可投递时实例进入明确等待或错误状态
```

这一步之后，“自动发送到服务器”才算进入可验证状态。

### [Priority 4] 收紧时间周期与业务时间

统一：

```text
next_schedule_time
thisScheduleTime
business_time
expect_time
LaunchFeature.bizTimeEpoch
```

这一步必须在重新打开 `prepareSchedulableTasksDaily` 前完成。

### [Priority 5] 加实例生成幂等防线

先做代码层防重复：

```text
准备生成实例前，按 task + business_time + sequence 语义查询是否已存在。
```

DB 唯一约束暂不动。

### [Priority 6] 完成冻结实例依赖生成

在 preparation 阶段完成：

```text
任务依赖图
-> 同周期实例映射
-> 冻结实例节点
-> 冻结实例边
```

出港检查继续只读冻结结果。

### [Priority 7] 恢复组合 tick

前面卡口收紧后，再恢复：

```text
prepareSchedulableTasksDaily( now )
impelPrelaunchInstances( now )
```

然后再考虑将 `fetch()` 外围包装成周期驱动。

## 7. 当前非目标

### [Not Now] 不做资源系统

资源 / 配额的深层模型先不写。`RavenScheduleAllocator` 暂作为现有适配壳使用。

### [Not Now] 不调索引和表结构

当前不做 DB/index 优化。三表依赖检查先按现有结构走。

### [Not Now] 不做全局重扫

后续每次只按锚点读文件。不要为了“全局理解”扫完整项目。

### [Not Now] 不重构为事件溯源

事件需要补齐，但先服务状态闭环，不做架构改写。

## 8. 关键代码锚点

### [Ready] 调度入口

```text
Odin/odin-framework-conduct/src/main/java/com/walnut/odin/conduct/schedule/RavenTaskScheduler.java
Odin/odin-framework-conduct/src/main/java/com/walnut/odin/conduct/schedule/RavenTaskSchedulePreparator.java
Odin/odin-framework-conduct/src/main/java/com/walnut/odin/conduct/schedule/RavenInstanceScheduleImpetus.java
Odin/odin-framework-conduct/src/main/java/com/walnut/odin/conduct/schedule/RavenScheduleAllocator.java
```

### [Ready] 投递 / 启动

```text
Odin/odin-framework-conduct/src/main/java/com/walnut/odin/dispatch/RavenTaskDispatcher.java
Odin/odin-framework-conduct/src/main/java/com/walnut/odin/dispatch/RavenTaskExecutionProcessor.java
Odin/odin-framework-runtime/src/main/java/com/walnut/odin/task/troll/TrollTaskExecutionLauncher.java
```

### [Ready] 实例 / Mapper

```text
Hydra/hydra-kom-default-driver/src/main/resources/mapper/kernel/task/InstanceNodeMapper.xml
Hydra/hydra-framework-runtime/src/main/java/com/pinecone/hydra/task/kom/instance/GenericInstanceEntry.java
Odin/odin-architecture/src/main/java/com/walnut/odin/task/mapper/InstanceAtlasNodeMapper.java
Odin/odin-framework-atlas/src/main/resources/mapper/kernel/task/InstanceAtlasNodeMapper.xml
Odin/odin-framework-atlas/src/main/resources/mapper/kernel/task/InstanceManipulator.xml
Odin/odin-framework-atlas/src/main/resources/mapper/kernel/task/InstanceEventMapper.xml
```

### [Ready] 状态 / 事件枚举

```text
Hydra/hydra-framework-runtime/src/main/java/com/pinecone/hydra/task/TaskInstanceStatus.java
Hydra/hydra-framework-runtime/src/main/java/com/pinecone/hydra/task/TaskInstanceExecState.java
Hydra/hydra-framework-runtime/src/main/java/com/pinecone/hydra/task/InstanceEventType.java
```

### [Ready] 本轮新增出港依赖对象

```text
Odin/odin-architecture/src/main/java/com/walnut/odin/conduct/schedule/entity/DependencyBlockage.java
Odin/odin-framework-conduct/src/main/java/com/walnut/odin/conduct/schedule/entity/GenericDependencyBlockage.java
```

## 9. 恢复入口

### [Ready] 推荐阅读顺序

1. `docs/import_root.md`
2. `docs/ins/taboo_root.dki`
3. `docs/context/task_topic_situation_context_current.md`
4. `Odin/odin-framework-conduct/src/main/java/com/walnut/odin/conduct/schedule/RavenInstanceScheduleImpetus.java`
5. `Odin/odin-framework-runtime/src/main/java/com/walnut/odin/task/troll/TrollTaskExecutionLauncher.java`
6. `Odin/odin-framework-conduct/src/main/java/com/walnut/odin/dispatch/RavenTaskDispatcher.java`
7. `Odin/odin-framework-conduct/src/main/java/com/walnut/odin/conduct/schedule/RavenTaskSchedulePreparator.java`

### [Ready] 当前最短施工口令

```text
不要启动 DependencyWait。
不要让 ResourceWait 直接启动。
只有 DepartureStandby 能 launch。
启动已有实例时不要重新 insert instance。
自动 tick 前先保证时间周期和实例生成幂等。
出港只读冻结依赖，不读实时 task graph。
```

## 10. 总结

### [Confirmed]

Task 调度专题已经完成第一道关键出港门：依赖检查改为批量查询冻结实例依赖，且推进流程已限制为只有 `DepartureStandby` 可以进入启动链路。

### [Open]

下一阶段真正的主卡口不是依赖检查，而是：

```text
已有实例启动路径不要重复初始化
状态 / exec / event 回写闭环
processor 远程投递运行态
时间周期与业务时间
实例生成幂等
冻结实例依赖生成
```

其中第一优先级是拆分已有实例启动路径。这个不处理，自动调度越早打开，越容易把实例表和状态机打乱。

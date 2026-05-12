# Hydra Device Control Tombstone

- Type: Tombstone / handoff context
- Language: zh_cn
- Date: 2026-05-12
- Scope: Hydra 设备中台 / Deploy 设备元数据 / Device Control 最小系统 / Sparta smoke test
- Workspace: `E:\MyFiles\CodeScript\Project\Hazelnut\Sauron\Saurons`

## 1. 当前目标

本轮目标是先独立搭出新时代的 `hydra-device-control` 最小系统，不急着接 GRPC，也不和 `hydra-service-control` 混成一坨。

核心定位：

```text
Hydra Device Control
-> 设备注册 / 注销 / 查询
-> 设备元数据 CRUD
-> 设备拓扑关系维护
-> 底层挂到 Deploy / KOM / Imperium tree
```

设计判断：

- 设备分类架构基本可用，主要问题是旧 deploy 元数据模型太 ancient。
- 不再保留 `hydra_device_runtime`、`hydra_device_binding` 这类注册中心式表。
- 设备元数据应该落回 deploy 节点自身表，按不同设备类型分表表达。
- 路径挂载物体不是本轮重点，设备 CRUD 和元数据正确性优先。
- `hydra-device-control` 先作为独立模块建设，上游 RPC 以后可以从系统 IOC / 中央对象注册局拿到 manager。

## 2. 关键模块与文件

新模块：

```text
Hydra/hydra-device-control
```

主要文件：

```text
Hydra/hydra-device-control/pom.xml
Hydra/hydra-device-control/src/main/java/com/pinecone/hydra/device/control/server/DeviceControlManager.java
Hydra/hydra-device-control/src/main/java/com/pinecone/hydra/device/control/server/UniformDeviceControlManager.java
Hydra/hydra-device-control/src/main/java/com/pinecone/hydra/device/control/server/DeviceLifecycleService.java
Hydra/hydra-device-control/src/main/java/com/pinecone/hydra/device/control/server/DeviceMetaService.java
Hydra/hydra-device-control/src/main/java/com/pinecone/hydra/device/control/server/DeviceTopologyService.java
Hydra/hydra-device-control/src/main/java/com/pinecone/hydra/device/control/dto/DeviceMetaDTO.java
Hydra/hydra-device-control/src/main/java/com/pinecone/hydra/device/control/dto/DeviceRegistrationDTO.java
Hydra/hydra-device-control/src/main/java/com/pinecone/hydra/device/control/dto/DeviceTopologyDTO.java
Hydra/hydra-device-control/src/main/java/com/pinecone/hydra/device/control/constant/DeviceNodeType.java
```

注意：

- 原 `DeviceNodeKind` 已按用户要求改为 `DeviceNodeType`。
- 测试类用 Hydra 游戏中的个人名 `Selene`，避免泛泛的 smoke 名字。

## 3. Deploy 元数据现代化

数据库 SQL 位置：

```text
C:/Users/undefined/Desktop/hydranium.sql
```

本轮已生成并由用户执行过现代化 SQL。重要约束：

- 表默认字符集使用 `utf8` / `utf8_general_ci`。
- 长内容性字段使用 `utf8mb4`。
- `title` 这类短字段不算长内容字段。
- deploy 表结构要匹配 mapper XML 字段。
- `vendor`、`model`、`serial_number`、`ip_address`、`status` 等设备元数据已经进入相关 deploy 节点表。

废弃方向：

```text
DeployInsMapping
GenericDeployInsMapping
DeployNodeManipulator
DeployServiceInsMappingManipulator
NodeMetaManipulator
DeployNodeMapper
DeployNodeMetaMapper
DeployServiceInsMappingMapper
```

这些是旧式部署设备 / 服务实例映射 / 节点 meta 模型，本轮方向是删掉或不再使用。

## 4. Mapper XML 自治加载

用户明确要求：不要在全局 `ArchMappingDriver` 里塞 deploy mapper，参考 Odin mapper driver，deploy 自己自治。

已落方向：

```text
Hydra/hydra-kom-default-driver/src/main/java/com/pinecone/hydra/deploy/ibatis/hydranium/DeployMappingDriver.java
```

关键点：

- deploy mapper XML 由 `DeployMappingDriver` 自己加载。
- 解决过 `DeployNamespaceMapper.getGuidsByName` statement not found。
- 不要回退到全局 hack。

相关 mapper XML 目录：

```text
Hydra/hydra-kom-default-driver/src/main/resources/mapper/kernel/deploy
```

## 5. Deploy Tree / Path 关键修复

核心文件：

```text
Hydra/hydra-framework-device/src/main/java/com/pinecone/hydra/deploy/kom/UniformDeployInstrument.java
Hydra/hydra-architecture/src/main/java/com/pinecone/hydra/unit/imperium/RegimentedImperialTree.java
```

已知修复：

- `UniformDeployInstrument` 增加 `containerElementManipulator`。
- `folderManipulators` 包含 namespace、cluster、physical host、virtual machine、container。
- `fileManipulators` 包含 cluster、physical host、virtual machine、container、quick。
- `affirmTreeNodeByPath` 创建非根路径时按直接子节点查找，避免同名节点跨路径误命中。
- `queryElement(path)` 优先走直接树遍历 `queryElementByDirectPath`，再 fallback 到旧 selector。
- `RegimentedImperialTree.affirmOwnedNode` 做幂等化：
  - 同 owner 不重复插入。
  - owner 不同则移动。
  - 避免旧 root placeholder owned edge 导致 `TooManyResultsException`。

重要语义：

```text
KOM / Reparse 层公开的 affirmOwnedNode(parentGuid, childGuid)
-> GenericReparseKOMTreeAddition 内部会翻译成 imperialTree.affirmOwnedNode(childGuid, parentGuid)
```

不要把这两层参数顺序混掉。

## 6. 最新修复点

最后一个失败：

```text
java.lang.AssertionError: host should exist by path
```

原因：

```text
DeviceLifecycleService.hasDeviceByPath()
仍然使用 DeployInstrument.queryGUIDByPath(path)
```

但当前 deploy path 真实可用查询已经改为：

```text
DeviceControlManager.queryDeviceByPath(path)
-> DeployInstrument.queryElement(path)
-> UniformDeployInstrument.queryElementByDirectPath(path)
```

因此最后修复：

```text
Hydra/hydra-device-control/src/main/java/com/pinecone/hydra/device/control/server/DeviceLifecycleService.java
```

变更：

- `hasDeviceByPath()` 改为走 `mDeviceControlManager.queryDeviceByPath( path ) != null`。
- `deregisterDeviceByPath()` 改为先查 `ElementNode`，再按 guid 删除。

## 7. 测试入口

测试类：

```text
Sparta/sparta-core-console/src/test/java/com/sparta/TestDeviceControlManager.java
```

测试覆盖：

- `DeviceAppointServer` hook / start / evict 生命周期。
- 注册 namespace、cluster、physical host、virtual machine、container。
- 按 path 查询 host 存在。
- 按 guid 查询 host 存在。
- 查询 host 元数据。
- 局部更新 host meta，验证 vendor 保留，status / ip 更新。
- affirm host -> vm owned relation。
- 查询 host children 包含 vm。
- 查询 vm children 包含 container。
- finally 清理测试注册的节点。

通过输出：

```text
Device control smoke test passed: root/deviceControlSmoke1778531959595
```

## 8. 测试命令

Java：

```text
D:\ProgramFiles\ToolChains\Java\jdk11x64\bin\java.exe
```

Maven：

```text
D:\ProgramFiles\JetBrains\IntelliJ IDEA 2025.3.2\plugins\maven\lib\maven3\bin\mvn.cmd
```

Git：

```text
D:\ProgramFiles\ToolChains\git_2_4_x64\bin\git.exe
```

Maven 需要显式设置 `JAVA_HOME`：

```powershell
$env:JAVA_HOME='D:\ProgramFiles\ToolChains\Java\jdk11x64'
& 'D:\ProgramFiles\JetBrains\IntelliJ IDEA 2025.3.2\plugins\maven\lib\maven3\bin\mvn.cmd' -pl Hydra/hydra-device-control,Sparta/sparta-core-console -am -DskipTests install
```

直接跑 main：

```powershell
$java='D:\ProgramFiles\ToolChains\Java\jdk11x64\bin\java.exe'
$cpFile='E:\MyFiles\CodeScript\Project\Hazelnut\Sauron\Saurons\Sparta\sparta-core-console\target\test-classpath.txt'
$depCp=Get-Content -Raw $cpFile
$cp="E:\MyFiles\CodeScript\Project\Hazelnut\Sauron\Saurons\Sparta\sparta-core-console\target\classes;E:\MyFiles\CodeScript\Project\Hazelnut\Sauron\Saurons\Sparta\sparta-core-console\target\test-classes;$depCp"
& $java -cp $cp com.sparta.TestDeviceManager
```

重要坑：

- 只跑 `test-compile` 后直接 `java -cp`，可能仍拿本地 Maven 仓库里的旧 `hydra-device-control` jar。
- 需要先 `install` reactor 依赖，再跑 main，否则可能误判补丁没生效。

## 9. 已验证状态

已跑：

```text
mvn -pl Hydra/hydra-device-control,Sparta/sparta-core-console -am -DskipTests test-compile
mvn -pl Hydra/hydra-device-control,Sparta/sparta-core-console -am -DskipTests install
java -cp ... com.sparta.TestDeviceManager
```

结果：

```text
BUILD SUCCESS
Device control smoke test passed
java exit code 0
```

测试发生在真实 Hydranium 数据库上，不只是编译通过。

## 10. 后续建议

下一步不要直接上 GRPC。建议顺序：

1. 收紧 device control 的接口边界，明确哪些方法属于 RPC surface。
2. 给 `DeviceMetaDTO.applyTo` 和各 deploy element 的字段映射做一次逐字段校验。
3. 对 `removeDevice` 的树关系清理语义做复核，确认 child / owner / path cache 是否有残留。
4. 为 `queryElementByDirectPath` 增加更窄的测试，覆盖同名节点在不同父节点下不会串路径。
5. 再考虑 Wolf RPC appoint server。
6. 最后再接 GRPC adapter，避免一口气把 transport 和模型问题搅在一起。

## 11. 工作区注意事项

当前 worktree 很脏，包含大量用户已有改动、IDE 文件、target 输出、新模块和 Odin 相关变更。

原则：

- 不要 reset。
- 不要 checkout 覆盖用户改动。
- 不要清理 target，除非用户明确要求。
- 如果需要看 git 状态，使用：

```powershell
& 'D:\ProgramFiles\ToolChains\git_2_4_x64\bin\git.exe' status --short
```

本 tombstone 只记录设备中台相关上下文，不代表整个 Saurons 当前全局状态。

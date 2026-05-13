# Hydra Skynet Device / Sparta Console Tombstone

- Type: Tombstone / handoff context
- Language: zh_cn
- Date: 2026-05-13
- Scope: Hydra Device 身份证系统 / Skynet Device Regiment / Sparta Skynet Console 后端第一轮 / 前后端打通方案
- Hydra Workspace: `/Users/wujunhong/projs/Hydra`
- Shadow Workspace: `/Users/wujunhong/projs/shadow-prime`

## 1. 当前目标

本轮主线是把 Hydra Device 从早期 deploy 语义升级为 Skynet 下的设备管理内核基础，并在 shadow-prime 的 Sparta 中台后端先落第一轮可用业务 API。

核心定位：

```text
DeviceInstrument
-> 设备身份证系统
-> 备案 / GUID / KOM handle / 路径挂载
-> 不负责运行期 registry / heartbeat / 调度

DeviceManager
-> 运行期控制面入口
-> 当前先承载设备 meta 更新、注册、注销、查询
-> 后续承载 RPC / registry / lifecycle runtime

CollectiveDeviceRegiment
-> Skynet/Skynut 设备团部 facade
-> 上层业务只依赖这个接口
```

用户的关键架构判断：

- Hydra 是一套接近 WinNT kernel + Linux mount/path 交互模型的 Winux 风格系统。
- Instrument 是身份证系统，不应该管理运行期元数据。
- Device 领域可以改，但 KOM / Imperium / Reparse 内核树不应随意动，动之前必须确认。
- Device 不应引入 `DeviceKind` / `DeviceNodeType` 这类重复类型系统，Element 本身就是身份证，必须通过已存在 Element 类型注册。
- Service 侧是半成品，只可参考 RPC 和控制链路，不要被旧语义带坏。

## 2. 重要标准

已阅读标准文件：

```text
/Users/wujunhong/projs/Hydra/docs/standard/coding_standard.md
/Users/wujunhong/projs/Hydra/docs/standard/mysql_table_standard.md
```

当前需要继续遵守：

- Java 代码倾向使用 `this.`。
- 控制语句风格为 `if ( ... )`。
- 业务 DTO / VO / Query / Result 统一放在 `entity` 目录，用户明确强调 `!important`。
- 不做 legacy wrapper，当前当新项目写，保持干净。
- 不因第一版最小功能而把架构做成玩具。

## 3. Root / Null Path 语义

这是后续前后端打通时最容易踩坑的点。

Hydra Device Instrument 不会默认创建 `/` 节点：

```text
null / no parent
-> 表示所有原始根节点集合
-> 不存在一个真实 `/` ElementNode
```

因此业务层需要 VIP 处理虚拟根：

```text
null -> /
""   -> /
"/"  -> /
```

但这个 `/` 只用于业务 API 和前端树展示：

- 不进 Instrument。
- 不入库。
- 不创建 GUID。
- 不作为真实 Device Element。
- 不把 `root` 作为保留名，`root` 未来可以是普通目录名。

`/api/device/tree` 的根返回必须是：

```json
{
  "name": "/",
  "path": "",
  "virtual": true,
  "children": [
    { "name": "server", "path": "server", "count": 15 },
    { "name": "compute", "path": "compute", "count": 50 }
  ]
}
```

真实路径仍为相对根路径：

```text
server/prod/hydra-server-001
compute/gpu/hydra-worker-gpu-01
```

不要传：

```text
/server/prod/hydra-server-001
root/server/prod/hydra-server-001
```

前端展示可以显示：

```text
/server/prod
/compute/gpu
```

但调用 API 时传真实 path：

```text
server/prod
compute/gpu
```

## 4. 数据表能力与字段方向

用户已经把 deploy 表重构为 device 表，当前基础表包括：

```text
hydra_device_cluster_node
hydra_device_container
hydra_device_namespace_node
hydra_device_node_path
hydra_device_node_tree
hydra_device_nodes
hydra_device_physical_host
hydra_device_quick
hydra_device_virtual_machine
```

当前表字段方向：

- 先重点关注核心算力设备管理，不急着 peripheral 全域模型。
- 不要过度加调度字段。
- `deployment_profile` 用来表达部署设备算力/资源 profile，例如：
  - `CPU_COMPUTE`
  - `GPU_COMPUTE`
  - `NPU_COMPUTE`
  - `OTHER_COMPUTE`
  - `MIXED_COMPUTE`
  - `MASS_STORAGE_NODE`
  - `SUPER_NODE`
- `topology_role` 用来表达拓扑角色，例如：
  - `CONTROL_PLANE`
  - `DATA_PLANE`
  - `WORKER_NODE`
  - `EDGE_NODE`
  - `GATEWAY_NODE`
  - `STORAGE_NODE`
  - `NETWORK_NODE`
  - `OBSERVABILITY_NODE`
  - `SUPER_NODE`
- `SUPER_NODE` 语义是计算、存储、网络等多种资源混合的大节点。
- `MIXED_COMPUTE` 只表达计算资源混合，例如 CPU + GPU，不应该包含存储和网络。
- `CONTROL_NODE` 容易把控制平面混入设备部署角色，建议用 `CONTROL_PLANE` 这类更明确语义。

当前已进入表/实体方向的重要字段：

```text
code
category
class_code
deployment_profile
topology_role
region
zone
location
management_protocol
management_host
management_port
credential_ref
lifecycle_status
enabled
tags
resource_summary
cpu_cores
memory_mb
storage_gb
gpu_count
gpu_model
cpu_limit
memory_limit_mb
```

设计原则：

- 高频筛选 / 展示 / 管理字段表字段化。
- 不把所有东西塞 JSON，否则表没有意义。
- 低频扩展字段继续使用 `extra_information` / `hardware_specs` 等文本 JSON 容器。
- 运行期数据如 heartbeat、动态 registry、租约等暂不进入 Instrument 表。

## 5. Hydra / Skynet 内核侧已完成方向

Skynet deploy 语义已经转向 device 语义。

模块现代化：

```text
Skynet/skynet-cloud-deploy
-> Skynet/skynet-framework-device
```

包名方向：

```text
com.acorn.skynet.device.conduct
```

已引入或现代化的核心类：

```text
/Users/wujunhong/projs/Hydra/Skynet/skynet-framework-device/src/main/java/com/acorn/skynet/device/conduct/CollectiveDeviceRegiment.java
/Users/wujunhong/projs/Hydra/Skynet/skynet-framework-device/src/main/java/com/acorn/skynet/device/conduct/SkyCollectiveDeviceRegiment.java
/Users/wujunhong/projs/Hydra/Skynet/skynet-framework-device/src/main/java/com/acorn/skynet/device/conduct/SkynetDeviceDeploy.java
```

旧 service deploy 方向删除或不再使用：

```text
CollectiveServiceDeployRegiment
SkyCollectiveServiceDeployRegiment
CloudDeploy
```

Skynet 系统入口已接 Device：

```text
/Users/wujunhong/projs/Hydra/Skynet/skynet-system/src/main/java/com/acorn/skynet/Skynet.java
/Users/wujunhong/projs/Hydra/Skynet/skynet-system/src/main/java/com/acorn/skynet/system/SkynetSubsystem.java
```

构造链路：

```text
DeviceMappingDriver
-> UniformDeviceInstrument
-> UniformDeviceManager
-> SkyCollectiveDeviceRegiment
```

RPC hook 方向：

```text
WolvesAppointServer
-> HuskyDeviceAppointServer
-> DeviceManager
```

SkynetSubsystem 暴露：

```text
deviceRegiment()
deviceInstrument()
deviceManager()
```

现代化配置：

```text
/Users/wujunhong/projs/Hydra/system/setup/lords/skynet.json5
```

包含方向：

```text
metaDependent
kernelConfig
device.identity
device.profile
```

注意：

- `skynet-framework-device` 属于 Acorn 坚果包，即网络系统工程 / Skynet 体系。
- 不属于 Pinecone / Walnut / Almond。
- 早期由 Shadow 等大服务统一承载，长期可以拆成专门微服务。

## 6. Device Control / RPC 命名约束

已明确不要改的东西：

```text
hydra-device-control 模块名暂不改
DeviceControlException 暂不改
DeviceControlRPCException 暂不改
DeviceValidationException 暂不改
registry.server 包名暂不改
```

已经改或应保持的语义：

```text
DeviceControlManager -> DeviceManager
```

但异常体系暂时保持 `DeviceControl*`，避免扩大到全域异常体系。

Pinenut 约束：

- Hydra 下原则上每一个对象都是 Pinenut 子民，匿名对象除外。
- 级联实现即可，父类或父接口终末实现了就不用每个类重复 `implements Pinenut`。
- `Slf4jTraceable` 已经实现对应体系时不需要重复。

## 7. Sparta 业务后端第一轮已落地

实际业务模块：

```text
/Users/wujunhong/projs/shadow-prime/Saurons/Sparta/sparta-skynet-console
```

注意：之前有一次上下文里误写成 `sparta-service-console`，当前真实落点是 `sparta-skynet-console`。

新增包：

```text
com.sparta.skynet.console.device
```

已新增/修改文件：

```text
/Users/wujunhong/projs/shadow-prime/Saurons/Sparta/sparta-skynet-console/pom.xml
/Users/wujunhong/projs/shadow-prime/Saurons/Sparta/sparta-skynet-console/src/main/java/com/sparta/skynet/console/device/controller/DevicePathController.java
/Users/wujunhong/projs/shadow-prime/Saurons/Sparta/sparta-skynet-console/src/main/java/com/sparta/skynet/console/device/entity/DeviceMetaDTO.java
/Users/wujunhong/projs/shadow-prime/Saurons/Sparta/sparta-skynet-console/src/main/java/com/sparta/skynet/console/device/entity/DeviceNodeVO.java
/Users/wujunhong/projs/shadow-prime/Saurons/Sparta/sparta-skynet-console/src/main/java/com/sparta/skynet/console/device/entity/DevicePathRequest.java
/Users/wujunhong/projs/shadow-prime/Saurons/Sparta/sparta-skynet-console/src/main/java/com/sparta/skynet/console/device/entity/DevicePathResult.java
/Users/wujunhong/projs/shadow-prime/Saurons/Sparta/sparta-skynet-console/src/main/java/com/sparta/skynet/console/device/service/DevicePathService.java
/Users/wujunhong/projs/shadow-prime/Saurons/Sparta/sparta-skynet-console/src/main/java/com/sparta/skynet/console/device/service/DevicePathServiceImpl.java
/Users/wujunhong/projs/shadow-prime/Saurons/Sparta/sparta-skynet-console/src/main/java/com/sparta/skynet/console/device/support/DevicePathResolver.java
/Users/wujunhong/projs/shadow-prime/Saurons/Sparta/sparta-skynet-console/src/main/java/com/sparta/skynet/console/device/support/DeviceRegimentProvider.java
```

POM 已补 system dependency：

```xml
<dependency>
    <groupId>com.acorn.skynet</groupId>
    <artifactId>skynet-framework-device</artifactId>
    <version>2.1.0</version>
    <scope>system</scope>
    <systemPath>${sauron.libs.dir}/Skynet/skynet-framework-device/target/skynet-framework-device-2.1.0.jar</systemPath>
</dependency>
```

后端当前 API：

```text
POST   /api/device/physical-host/**
POST   /api/device/virtual-machine/**
POST   /api/device/container/**
POST   /api/device/cluster/**
POST   /api/device/quick/**
POST   /api/device/namespace/**

GET    /api/device/path/**
GET    /api/device/guid/{guid}

PUT    /api/device/path/**
DELETE /api/device/guid/{guid}
```

明确不要使用：

```text
/api/v2
```

第一轮实现特点：

- `DevicePathController` 捕获 trailing `/**`，由 `DevicePathResolver` 从 `HttpServletRequest` 解析真实 path。
- `DevicePathServiceImpl` 只依赖 `CollectiveDeviceRegiment`。
- 创建时构造真实 Hydra Element 类：
  - `GenericPhysicalHostElement`
  - `GenericVirtualMachineElement`
  - `GenericContainerElement`
  - `GenericClusterElement`
  - `GenericQuickElement`
  - `GenericNamespace`
- 创建时 `name` 来自 path tail，不以 body 内 `name` 覆盖路径身份。
- `DeviceMetaDTO.applyTo` 使用非空字段更新，避免 PUT 缺字段导致已有 meta 被清空。
- `DeviceMetaDTO` 支持 common meta 与物理机 / VM / 容器 / cluster / quick 部分类型字段。
- `DeviceRegimentProvider` 在没有上游 bean 时构造：
  - `DeviceMappingDriver`
  - `UniformDeviceInstrument`
  - `UniformDeviceManager`
  - `SkyCollectiveDeviceRegiment`

## 8. 后端下一步设计：页面聚合 API

第一版 PRD 页面需要补充查询聚合 API：

```text
GET  /api/device/tree
GET  /api/device/list
GET  /api/device/summary
GET  /api/device/filter-options
```

建议新增目录：

```text
com.sparta.skynet.console.device
  controller
    DeviceQueryController
  service
    DeviceQueryService
    DeviceQueryServiceImpl
  support
    DeviceVirtualRootSupport
    DeviceTreeAssembler
    DeviceNodeMatcher
  entity
    DeviceTreeNodeVO
    DeviceListQuery
    DeviceListResult
    DeviceSummaryVO
    DeviceFilterOptionsVO
```

仍然保持 DTO / VO / Query / Result 都放 `entity`。

`DeviceVirtualRootSupport` 负责：

```text
null -> ""
""   -> ""
"/"  -> ""
```

返回前端时：

```text
name    = "/"
path    = ""
virtual = true
```

`GET /api/device/list` 建议参数：

```text
path=compute/gpu
category=COMPUTE
deploymentProfile=GPU_COMPUTE
topologyRole=WORKER_NODE
lifecycleStatus=ONLINE
region=region-a
keyword=hydra-server
page=1
pageSize=20
```

第一轮可以先从 `CollectiveDeviceRegiment` / `DeviceInstrument` 拉出节点后内存过滤。等设备量变大后再下沉到 mapper 查询。

列表返回建议字段：

```json
{
  "guid": "...",
  "name": "hydra-worker-gpu-01",
  "path": "compute/gpu/hydra-worker-gpu-01",
  "displayPath": "/compute/gpu",
  "category": "COMPUTE",
  "deploymentProfile": "GPU_COMPUTE",
  "topologyRole": "WORKER_NODE",
  "ipAddress": "10.20.1.31",
  "lifecycleStatus": "ONLINE",
  "status": "NORMAL",
  "resourceSummary": "GPU 8 x A100 / CPU 32C / 128G",
  "lastHeartbeatText": null
}
```

`lastHeartbeatText` 第一轮可以为空或 `--`，不要现在引入运行期 registry。

## 9. 前端第一版 PRD 页面设计

参考用户给的设备管理中心页面图，第一版应做真实可用管理台，不做 landing page。

页面布局：

```text
左侧：节点目录
右侧：设备列表主工作区
```

左侧节点目录：

- 根节点展示为 `/`。
- 根节点来自业务虚拟 root，不来自真实 Instrument 节点。
- 子节点来自 `/api/device/tree`。
- 点击目录后刷新右侧 list，真实查询 path 不带 `/` 前缀。
- 搜索节点只筛树，不改变真实路径。

顶部区域：

- 面包屑：`设备管理中心 / 设备列表`。
- 选中目录展示建议使用 `/compute/gpu`。
- 操作按钮：
  - 创建设备
  - 批量导入
  - 刷新
  - 视图选项

统计卡片：

- 总设备
- 在线设备
- 维护中
- 离线设备
- 已退役

筛选区：

- 设备类型 / 算力类型：`deploymentProfile`
- 生命周期状态：`lifecycleStatus`
- 区域：`region`
- 标签：`tags`
- 搜索：名称 / IP / code / model

表格列：

- 设备名称
- 设备类型
- 生命周期状态
- 主机地址
- 所属目录
- 资源摘要
- 最近心跳
- 操作：详情 / 编辑 / 更多

创建弹窗：

- Element 类型：
  - 物理机
  - 虚拟机
  - 容器
  - 集群
  - Quick
  - Namespace
- 父目录。
- 设备名称。
- 由父目录 + 设备名称拼接真实 path。
- 填写 `code`、`deploymentProfile`、`topologyRole`、`ipAddress`、`resourceSummary` 等。

前端内部类型建议：

```ts
type DeviceTreeNode = {
  name: string
  path: string
  displayPath: string
  virtual?: boolean
  count?: number
  children?: DeviceTreeNode[]
}
```

其中根节点：

```ts
{
  name: "/",
  path: "",
  displayPath: "/",
  virtual: true
}
```

## 10. 验证记录

Hydra Skynet framework-device 已成功构建：

```bash
cd /Users/wujunhong/projs/Hydra/Skynet
mvn -pl skynet-framework-device -am -DskipTests install
```

成功结果：

```text
BUILD SUCCESS
```

Sparta Skynet Console 已成功构建：

```bash
cd /Users/wujunhong/projs/shadow-prime/Saurons/Sparta
mvn -pl sparta-skynet-console -am -DskipTests install
```

成功结果：

```text
BUILD SUCCESS
```

编译前为了修本地依赖缓存，执行过：

```bash
cd /Users/wujunhong/projs/shadow-prime
mvn -N -DskipTests install

cd /Users/wujunhong/projs/shadow-prime/Saurons
mvn -pl Tres/tres-framework-architecture,Tres/tres-web-architecture -am -DskipTests install
```

残差扫描：

```bash
rg "/api/v2|DeviceKind|DeviceNodeType" \
  /Users/wujunhong/projs/shadow-prime/Saurons/Sparta/sparta-skynet-console/src/main/java \
  /Users/wujunhong/projs/Hydra/Skynet/skynet-framework-device/src/main/java
```

结果：

```text
no match
```

## 11. 当前工作区注意事项

shadow-prime 当前 `git status` 里有大量既有变动和 target 输出。不要无脑清理用户未确认的内容。

本轮业务相关重点：

```text
?? Saurons/Sparta/sparta-skynet-console/
M  Saurons/Sparta/pom.xml
M  pom.xml
```

Hydra 侧本轮/前序相关重点：

```text
M  Skynet/pom.xml
M  Skynet/skynet-system/pom.xml
M  Skynet/skynet-system/src/main/java/com/acorn/skynet/Skynet.java
M  Skynet/skynet-system/src/main/java/com/acorn/skynet/system/SkynetSubsystem.java
M  system/setup/lords/skynet.json5
?? Skynet/skynet-framework-device/
```

注意：

- `Skynet/skynet-framework-device/target/skynet-framework-device-2.1.0.jar` 是 shadow-prime systemPath 编译需要的 jar，当前不要随手删除，除非确认后重建。
- shadow-prime 的 `sparta-skynet-console/target` 已在最后清理掉。
- Tres 相关 target 也已清理过。

## 12. 明确不要做的事

不要做：

```text
创建真实 "/" Device 节点
引入 /api/v2
引入 DeviceKind
引入 DeviceNodeType
把 root 当保留目录名
动 KOM / Imperium / Reparse 内核树
把运行期 registry / heartbeat 塞进 Instrument
把 DTO / VO 放到 dto / vo 目录
把所有字段塞 extra_information JSON
引入调度字段
重命名 hydra-device-control artifact
重命名 registry.server 包
补 DeviceControlException 全域异常体系
```

需要改 KOM 内核前必须先和用户确认。

## 13. 下一步推荐施工顺序

推荐下一轮按这个顺序：

1. 后端补 `DeviceVirtualRootSupport`，把 `null` / `""` / `"/"` 统一 normalize 为虚拟根。
2. 补 `GET /api/device/tree`，根节点返回 `name="/"`、`path=""`、`virtual=true`。
3. 补 `GET /api/device/list`，先支持 path / keyword / deploymentProfile / topologyRole / lifecycleStatus / region 过滤。
4. 补 `GET /api/device/summary`，统计总量和生命周期状态。
5. 补 `GET /api/device/filter-options`，返回前端 select options。
6. 前端实现设备管理中心第一版页面：
   - 左树
   - 统计卡片
   - 筛选条
   - 表格
   - 创建设备弹窗
   - 编辑设备弹窗
7. 做端到端 smoke：
   - 创建 namespace
   - 创建 physical-host
   - 查询 tree
   - 查询 list
   - 更新 meta
   - 删除 guid

## 14. 小猫猫备注

这个系统的关键不是“做一个设备列表页面”，而是把 Hydra 的 null-root 多根模型、WinNT 风格内核对象、Linux 风格路径交互、Skynet 云拓扑归属、Device 身份证系统这几件事捋顺。

第一版业务页面要克制：

- 它要像操作系统设备管理器，不像 CMDB 表单玩具。
- 先把核心算力设备管理做稳。
- 运行期 registry / heartbeat / 调度以后长出来，不在身份证系统里抢戏。


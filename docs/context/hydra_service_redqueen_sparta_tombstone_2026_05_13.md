# Hydra Service / RedQueen / Sparta Tombstone - 2026-05-13

> 用途：给后续上下文恢复、迁移、继续施工使用。  
> 范围：Hydra Service 内核现代化、Sparta 中台 Service 门面分离、Skynet/RedQueen 边界修正、前端 reparse 入口与安装环境诊断。  
> 当前时间基准：2026-05-13，用户工作区主要在 macOS 本机。

## 1. 当前工程根

主要工程：

- Hydra 主工程：`/Users/wujunhong/projs/Hydra`
- Shadow 后端：`/Users/wujunhong/projs/shadow-prime`
- Shadow 前端：`/Users/wujunhong/GolandProjects/shadow-platform-prime-fe`
- 前端 reparse 软链：`/Users/wujunhong/projs/shadow-prime/reparse/shadow-platform-prime-fe`
- Hydra reparse 软链：`/Users/wujunhong/projs/shadow-prime/reparse/hydra`

已创建软链：

```bash
/Users/wujunhong/projs/shadow-prime/reparse/shadow-platform-prime-fe \
  -> /Users/wujunhong/GolandProjects/shadow-platform-prime-fe
```

## 2. 用户关键偏好和边界

- 用户要求小猫猫语气，但工程输出仍要稳。
- 不要主动搞 Git；用户明确说 Git 已经分治。
- 后端业务门面接口放在 `shadow-prime/Saurons/Sparta` 下。
- `Hydra` 内原则上不能放具体业务领域逻辑。
- `RedQueen` 不放后端门面接口。
- `RedQueen` 暂时不用动，至少本轮后续没有动。
- `Odin` 不碰；用户明确说 Odin 是大家伙。
- 新 API 不走 `/api/v2/*`，直接 `/api/*`。
- DTO / VO 统一放 `entity` 包，Hydraify 风格。
- 不建 `sparta-service-architecture`，用户确认：`no sparta-service-architecture [confimed]`。

## 3. 架构结论快照

当前边界应理解为：

- `Hydra/Hydra/hydra-framework-service`：Service 身份证 / KOM 基础模型。
- `Hydra/RedQueen`：Service / 计算编排系统域，未来可作为 Service 系统能力的承载方，但本轮未改。
- `shadow-prime/Saurons/Sparta/sparta-redqueen-console`：Service 中台后端门面。
- `shadow-prime/Saurons/Sparta/sparta-skynet-console`：Skynet 设备 / 部署 / 拓扑门面，已与 Service 门面分离。

当前产品门面链路：

```text
/api/redqueen/service/*
  -> sparta-redqueen-console
  -> ServiceIdentityConsoleService
  -> ServiceInstrument
  -> Hydra Service KOM / mapper
```

注意：当前 `ServiceInstrument` 在 Sparta RedQueen Console 的 provider 内构造，后续可按用户新要求再改成“从 RedQueen 一层层取出来注册”。本 Tombstone 记录的是截至本轮实际施工后的真实状态。

## 4. Hydra Service 内核现代化已完成

目标：现代化 Service mapper，去掉古老 annotation SQL，改为 XML mapper，适配 neo / xmlify 方向。

涉及路径：

```text
/Users/wujunhong/projs/Hydra/Hydra/hydra-kom-default-driver
```

已完成：

- 从 `com.pinecone.hydra.service.ibatis.*` 相关 Service mapper 接口中移除 MyBatis SQL annotations。
- 新增 XML mapper 到：

```text
/Users/wujunhong/projs/Hydra/Hydra/hydra-kom-default-driver/src/main/resources/mapper/kernel/service
```

- 在 `ServiceMappingDriver` 中增加 XML object scope：

```java
ibatisClient.addXMLObjectScope("mapper.kernel.service")
```

已验证命令：

```bash
cd /Users/wujunhong/projs/Hydra/Hydra
mvn -pl hydra-kom-default-driver -am compile -DskipTests
```

结果：通过。

## 5. Sparta Service 门面迁移历史

初始误放：

- 最早创建在 `sparta-service-console`
- 后迁到 `sparta-skynet-console`
- 后确认 Service 门面不属于 Skynet，拆到 `sparta-redqueen-console`

当前正确落位：

```text
/Users/wujunhong/projs/shadow-prime/Saurons/Sparta/sparta-redqueen-console
```

父模块已加入：

```text
/Users/wujunhong/projs/shadow-prime/Saurons/Sparta/pom.xml
```

包含：

```xml
<module>sparta-redqueen-console</module>
```

当前目录结构：

```text
sparta-redqueen-console/src/main/java/com/sparta/redqueen/console
  controller
    ServiceIdentityController.java
  entity
    ServiceManifestEvalDTO.java
    ServiceNodeVO.java
    ServicePathDTO.java
    ServiceUpdateDTO.java
  service
    ServiceIdentityConsoleService.java
    ServiceIdentityConsoleServiceImpl.java
  support
    ServiceIdentityInstrumentProvider.java
    ServiceNodeVOFactory.java
```

当前 API base：

```text
/api/redqueen/service
```

已实现接口：

```text
GET  /api/redqueen/service/query/path?path=...
GET  /api/redqueen/service/query/guid?guid=...
PUT  /api/redqueen/service/add/namespace
PUT  /api/redqueen/service/add/application
PUT  /api/redqueen/service/add/service
GET  /api/redqueen/service/query/services
PUT  /api/redqueen/service/update/service
POST /api/redqueen/service/manifest/eval
```

当前功能范围：

- 查询 Service KOM path
- 查询 Service GUID
- affirm namespace / application / service
- 查询全部 service
- 更新 service meta
- eval manifest，使用 `ServiceJSONDecoder(new JSONMaptron(statement))`

明确未做：

- 未实现 Service runtime registry 产品能力。
- 未接 `ServiceManager` RPC 运行注册中心。
- 未改 `Hydra/RedQueen`。
- 未碰 Odin。

## 6. sparta-skynet-console 当前状态

当前路径：

```text
/Users/wujunhong/projs/shadow-prime/Saurons/Sparta/sparta-skynet-console
```

当前职责：Skynet / Device console。

当前保留目录：

```text
sparta-skynet-console/src/main/java/com/sparta/skynet/console/device
  controller
  entity
  service
  support
```

已从 Skynet Console 移除：

- `ServiceIdentityController`
- Service DTO / VO
- `ServiceIdentityConsoleService`
- `ServiceIdentityInstrumentProvider`
- `ServiceNodeVOFactory`
- `hydra-framework-service` 依赖

当前 device 侧仍存在：

```text
DevicePathController.java
DeviceMetaDTO.java
DeviceNodeVO.java
DevicePathRequest.java
DevicePathResult.java
DevicePathService.java
DevicePathServiceImpl.java
DevicePathResolver.java
DeviceRegimentProvider.java
```

注意：这一块包含 `CollectiveDeviceRegiment` / `SkyCollectiveDeviceRegiment` / `DeviceManager` / `DeviceInstrument` provider 逻辑。是否继续收敛到 Skynet 系统态，留作后续。

## 7. 当前 Maven 验证状态

已验证 `sparta-redqueen-console`：

```bash
cd /Users/wujunhong/projs/shadow-prime
mvn -pl :sparta-redqueen-console -am compile -DskipTests
```

结果：`BUILD SUCCESS`。

过程中 Maven 有既有 warning：

- 一些旧模块引用的 `Skynet/skynet-system/target/skynet-system-2.1.0.jar` 不存在。
- Odin / Manhattan 等旧模块存在 duplicate dependency warning。
- JDK source/target 11 没用 `--release 11` 的 compiler warning。

这些 warning 不是本轮新增的阻塞项。

## 8. 当前 ServiceInstrument Provider 状态

当前文件：

```text
/Users/wujunhong/projs/shadow-prime/Saurons/Sparta/sparta-redqueen-console/src/main/java/com/sparta/redqueen/console/support/ServiceIdentityInstrumentProvider.java
```

当前逻辑：

- 注入 `EnderHydra parentSystem`
- 构造 `ServiceMappingDriver`
- 使用 `MySQLKingSystem`
- 返回 `new UniformServiceInstrument(serviceMappingDriver)`
- `@ConditionalOnMissingBean(ServiceInstrument.class)`

当前代码语义仍是 Console 本地构造 ServiceInstrument。用户随后纠正过一版期望：

- `RedQueen` 不放门面接口。
- 后端门面在 `Sparta`。
- 后续如果要改系统态挂载，应该从 RedQueen 一层层取出来注册。
- 但用户随后明确说“redqueen 不用动哦”，所以本轮没有把该 provider 改成 RedQueen 获取。

后续若要继续推进，更稳的施工点：

1. 仍保留 `sparta-redqueen-console` 的门面 API。
2. 等 RedQueen 侧准备好 `ServiceCentralControl` 只读出口后，再把 `ServiceIdentityInstrumentProvider` 改为只注册 RedQueen 暴露出来的实例。
3. Console 不再自己 new `UniformServiceInstrument`。

## 9. 曾讨论但未施工的 RedQueen 系统态方案

这个方案已讨论，但最终用户要求本轮不动 RedQueen，因此未执行。

目标形态：

```text
EnderHydra
  -> redQueen()
    -> ServiceCentralControl
      -> serviceInstrument()
      -> collectiveServiceRegiment()
```

RedQueen 未来可在 `prepare_system_skeleton()` 内：

- 构造或获取 `ServiceInstrument`
- 挂载到 `KernelObjectRootMountPoint.ServiceMeta`，即 `/meta/service`
- 构造 `RedCollectiveServiceRegiment`
- 暂不启动 `ServiceManager` RPC，直到产品需要运行注册中心

Console 未来只做：

```java
parentSystem.redQueen().serviceInstrument()
```

或者先通过 `ServiceCentralControl` 接口拿。

## 10. 操作系统注册表 / KOM 相关旧链路参考

旧 Trimus 挂载链路参考：

```text
/Users/wujunhong/projs/shadow-prime/Saurons/Tres/trimus-framework-architecture/src/main/java/com/sauron/tres/system/ArchTrimus.java
```

其模式：

```text
prepare_kernel_object_models()
  -> construct Instrument / Regiment
  -> instrument.mount(KernelObjectRootMountPoint.TaskMeta.getMountPoint(), ravenTaskInstrument)

register_kom_resource_before()
  -> getMountedInstrument(TaskMeta)
  -> context.registerBean(...)
```

Hydra root mount enum：

```text
/Users/wujunhong/projs/Hydra/Hydra/hydra-architecture/src/main/java/com/pinecone/hydra/system/imperium/KernelObjectRootMountPoint.java
```

关键 mount point：

```java
Registry    -> /config/registry
TaskMeta    -> /meta/task
ServiceMeta -> /meta/service
DeployMeta  -> /dev/deploy
SysImages   -> /system/public/global/exe/images
```

## 11. RedQueen 当前源码状态快照

RedQueen 位置：

```text
/Users/wujunhong/projs/Hydra/RedQueen
```

模块：

```text
redqueen-architecture
redqueen-computation-suit
redqueen-framework-service
redqueen-system
```

当前系统壳：

```text
/Users/wujunhong/projs/Hydra/RedQueen/redqueen-system/src/main/java/com/acorn/redqueen/RedQueen.java
```

现状：

- `RedQueen extends ArchModularizedSubsystem implements ServiceCentralControl`
- `prepare_system_skeleton()` 当前为空

Service regiment：

```text
/Users/wujunhong/projs/Hydra/RedQueen/redqueen-framework-service/src/main/java/com/acorn/redqueen/service/conduct
  CollectiveServiceRegiment.java
  RedCollectiveServiceRegiment.java
```

`CollectiveServiceRegiment` 当前能力：

```java
ServiceManager serviceManager();
ServiceInstrument serviceInstrument();
void startServiceManage() throws ServiceControlException;
```

## 12. 前端环境诊断

前端路径：

```text
/Users/wujunhong/GolandProjects/shadow-platform-prime-fe
```

软链路径：

```text
/Users/wujunhong/projs/shadow-prime/reparse/shadow-platform-prime-fe
```

用户遇到的安装错误：

```text
npm ERR! path .../node_modules/deasync
npm ERR! command sh -c -- node ./build.js
ModuleNotFoundError: No module named 'distutils'
```

原因：

- 用户实际 `npm install` 时 node-gyp 使用了 Python 3.13：

```text
Python version 3.13.1 found at "/opt/homebrew/opt/python@3.13/bin/python3.13"
```

- `node-gyp@9.1.0` 仍 import `distutils.version.StrictVersion`
- Python 3.12+ 已无内置 `distutils`
- 所以 `deasync` native build 失败

另外还有 Node warning：

- 当前用户用 Node `v16.20.2`
- `svelte@5.53.3` 要求 Node `>=18`
- `jsonpath-plus@10.4.0` 要求 Node `>=18.0.0`
- 来源大概率是 `vanilla-jsoneditor@3.11.0`

项目特征：

- Vue 2.7
- Vue CLI 4.4
- Webpack 4
- `package-lock.json` 存在
- `sass` 而非必须 `node-sass`

建议临界安装命令：

```bash
cd /Users/wujunhong/GolandProjects/shadow-platform-prime-fe

export PATH="/Users/wujunhong/GolandProjects/ethercraft-client/.tools/node-v16.20.2-darwin-arm64/bin:$PATH"
export HOME="$PWD/.home"
export PYTHON="/opt/homebrew/bin/python3.11"
export npm_config_python="/opt/homebrew/bin/python3.11"
export NPM_CONFIG_CACHE="$PWD/.npm-cache"

rm -rf node_modules/deasync
npm install
```

关键点：

- 只设 `PYTHON=...` 不够稳。
- 需要 `npm_config_python=/opt/homebrew/bin/python3.11`。

长期更顺路线：

- 改用 Node 18。
- Vue CLI 4 / Webpack 4 运行时可能需要：

```bash
export NODE_OPTIONS=--openssl-legacy-provider
```

## 13. 本轮实际修改清单

Hydra：

- `hydra-kom-default-driver` Service mapper xmlify 已完成。
- 新增或调整 Service XML mapper resources。
- `ServiceMappingDriver` 加 XML object scope。

Shadow 后端：

- 新增模块：

```text
/Users/wujunhong/projs/shadow-prime/Saurons/Sparta/sparta-redqueen-console
```

- 修改父 pom：

```text
/Users/wujunhong/projs/shadow-prime/Saurons/Sparta/pom.xml
```

- 从 `sparta-skynet-console` 迁出 Service 门面。
- `sparta-skynet-console` 移除 `hydra-framework-service` 依赖。
- `sparta-skynet-console` 保留 device 子域。

Shadow reparse：

- 创建前端软链：

```text
/Users/wujunhong/projs/shadow-prime/reparse/shadow-platform-prime-fe
```

## 14. 后续推荐施工顺序

### 14.1 短线：继续产品门面

1. 保持 `sparta-redqueen-console` 作为 Service 后端门面。
2. 补前端页面时调用 `/api/redqueen/service/*`。
3. 先做 Service 身份证信息管理，不扩运行注册中心。

### 14.2 中线：ServiceInstrument 来源收口

等用户确认可以动 RedQueen 后：

1. 在 `ServiceCentralControl` 暴露 `serviceInstrument()` / `collectiveServiceRegiment()`。
2. 在 `RedQueen.prepare_system_skeleton()` 做系统态构造和 `/meta/service` mount。
3. `sparta-redqueen-console` 的 provider 改成只从 `EnderHydra.redQueen()` 获取实例。
4. 删除 Console 内 `new UniformServiceInstrument` 的职责。

### 14.3 Skynet 线

`sparta-skynet-console/device` 当前仍在 Console provider 中构造 DeviceInstrument / DeviceManager / CollectiveDeviceRegiment。

后续更正统方向：

- Skynet 系统态构造设备拓扑 / deploy regiment。
- Sparta Skynet Console 只拿系统暴露的 regiment。

但本轮用户明确让先分离 Service，不展开 Skynet。

## 15. 恢复现场时建议先跑

后端 Service 门面：

```bash
cd /Users/wujunhong/projs/shadow-prime
mvn -pl :sparta-redqueen-console -am compile -DskipTests
```

Hydra Service mapper：

```bash
cd /Users/wujunhong/projs/Hydra/Hydra
mvn -pl hydra-kom-default-driver -am compile -DskipTests
```

查看 Service 门面位置：

```bash
find /Users/wujunhong/projs/shadow-prime/Saurons/Sparta/sparta-redqueen-console/src/main/java -type f | sort
```

查看 Skynet Console 是否仍只剩 device：

```bash
find /Users/wujunhong/projs/shadow-prime/Saurons/Sparta/sparta-skynet-console/src/main/java -type f | sort
```

前端安装环境诊断：

```bash
cd /Users/wujunhong/projs/shadow-prime/reparse/shadow-platform-prime-fe
node -v
npm -v
/opt/homebrew/bin/python3.11 --version
```


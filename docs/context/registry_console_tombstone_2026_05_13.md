# Registry Console / Hydra Registry Tombstone - 2026-05-13

> 用途：给后续上下文恢复、迁移、继续施工使用。  
> 范围：Hydra Registry 表现代化、Registry 内核 XMLify、Shadow Sparta Registry Console、前端配置中台入口、运行期 404/500 排障。  
> 当前时间基准：2026-05-13，用户工作区主要在 macOS 本机，时区 Asia/Shanghai。

## 1. 当前工程根

主要工程：

- Hydra 主工程：`/Users/wujunhong/projs/Hydra`
- Shadow 后端：`/Users/wujunhong/projs/shadow-prime`
- Shadow 前端：`/Users/wujunhong/GolandProjects/shadow-platform-prime-fe`

相关重点路径：

- Registry 内核：`/Users/wujunhong/projs/Hydra/Hydra/hydra-framework-config`
- Registry KOM driver：`/Users/wujunhong/projs/Hydra/Hydra/hydra-kom-default-driver`
- Registry XML mapper：`/Users/wujunhong/projs/Hydra/Hydra/hydra-kom-default-driver/src/main/resources/mapper/kernel/registry`
- Shadow Registry Console：`/Users/wujunhong/projs/shadow-prime/Saurons/Sparta/sparta-registry-console`
- Shadow runtime system：`/Users/wujunhong/projs/shadow-prime/Saurons/Tres/shadow-framework-system`
- 前端配置中台：`/Users/wujunhong/GolandProjects/shadow-platform-prime-fe/src/views/config/registry`

## 2. 用户关键偏好和边界

- 对话可用“小猫猫”语气，但工程文档要清楚、可恢复、可迁移。
- Registry 是重点，不搞统一账号系统。
- Hydra 是“内核 WinNTify，交互 Linuxify”，用户称为 `winnus`。
- 路径风格不要学 Windows，交互层使用 Unix-like path。
- `/conf/registry` 是 express / mount 层语义，不是 Registry Console 每次都要走的真实查询前缀。
- Registry Console 直接从 registry instrument/root 开始寻址，避免每次穿 express，避免性能灾难。
- `配置中台` 和 `推送中台` 是前端一级兄弟节点，不是推送中台下面的子节点。
- 任务树已有页面不要重复造轮子，应提取公共树工作台层。
- 多语言功能要参考兄弟项目优秀实现，不能散乱硬编码。
- 不要主动回滚 unrelated dirty work；当前三边仓库都有较多脏改和 target 输出。

## 3. Registry 产品语义快照

Registry 这条线的产品定位：

- 类 Windows Registry 的内核配置树，但路径和交互 Unixify。
- 类 Apollo 的配置中台能力，但底座不是普通 namespace/app cluster 配置表，而是 Hydra KOM Registry。
- 后续产品应长成“配置中台 / 注册表”：
  - 浏览 Registry 树。
  - 查看 namespace / properties / text node。
  - 编辑 properties / text value。
  - 创建 namespace / properties / text node。
  - 支持 selector 查询。
  - 后续再做版本、发布、审计、快照、回滚、权限等中台能力。

本轮临界边界：

- 先打通 Registry 内核现代化和 Console 基础读写。
- 不马上做完整 Apollo 化业务逻辑。
- 不做发布系统、环境维度、灰度、历史版本。
- 不再把 `hydra_registry_conf_node_meta` / `hydra_registry_ns_node_meta` 当有效设计表。

## 4. Registry 表现代化结论

历史表里有两类“上古 meta 表”：

- `hydra_registry_conf_node_meta`
- `hydra_registry_ns_node_meta`

经过 marshaling 和 mapper 检查后，最终结论：

- `hydra_registry_conf_node_properties` 不是目标方向。
- `RegistryPropertiesMapper` 不是旧贵族遗老，它仍承载 properties 序列化系统。
- `hydra_registry_conf_node_text_value` 和 `hydra_registry_node_attributes` 有真实业务含义。
- `hydra_registry_conf_node_meta` / `hydra_registry_ns_node_meta` 可以从实际依赖中清掉。
- `hydra_registry_nodes.node_meta_guid` 字段保留，默认 `NULL`，作为顶级 KOM 兼容槽位，暂不连接 meta 表。

当前临界态设计：

```text
Java 顶级 KOM 槽位保留
hydra_registry_nodes.node_meta_guid 字段保留
conf/ns 两张 meta 空表删除
Registry operator 不再使用 node_meta_guid
node_meta_guid 默认 NULL
```

用户后来把字段名还原为 `node_meta_guid`，其他表结构沿用现代化 SQL。不要再改成 `node_metadata_guid`。

现代化后的 path cache 表名：

```text
hydra_registry_node_cache_path
```

不要继续使用旧表名：

```text
hydra_registry_node_path
```

## 5. Hydra Registry 内核现代化已做

主要路径：

```text
/Users/wujunhong/projs/Hydra/Hydra/hydra-framework-config
/Users/wujunhong/projs/Hydra/Hydra/hydra-kom-default-driver
```

已做方向：

- `ArchConfigNodeOperator` 不再生成 / 写入 / 查询 / 删除 `ConfigNodeMeta` 表数据。
- `NamespaceNodeOperator` 不再生成 / 写入 / 查询 / 删除 `NamespaceMeta` 表数据。
- `RegistryMasterManipulator` 去掉 meta manipulator 暴露。
- `RegistryMasterManipulatorImpl` 去掉 config/ns meta mapper 和 manipulator 依赖。
- `RegistryNodeMetaMapper.java` / `RegistryNSNodeMetaMapper.java` 已删除。
- `RegistryNodeMetaManipulator.java` / `RegistryNSNodeMetaManipulator.java` 已删除。
- `ConfigNodeMeta` / `NamespaceMeta` entity 暂时没有作为第一轮删除目标，因为接口上还有 getter/setter 残留，后续可再清理。

重要保留：

- `Properties`
- `TextFile` / `TextValue`
- `Namespace`
- `Attributes`
- `node_meta_guid` 字段

## 6. Registry XMLify 状态

Registry mapper 已从 annotation SQL 迁移到 XML mapper。

XML 文件位于：

```text
/Users/wujunhong/projs/Hydra/Hydra/hydra-kom-default-driver/src/main/resources/mapper/kernel/registry
```

当前文件：

```text
RegistryAttributesMapper.xml
RegistryConfigNodeMapper.xml
RegistryNSNodeMapper.xml
RegistryNodeOwnerMapper.xml
RegistryNodePathCacheMapper.xml
RegistryPropertiesMapper.xml
RegistryTextFileMapper.xml
RegistryTreeMapper.xml
```

关键 statement 已存在：

```text
com.pinecone.hydra.registry.ibatis.RegistryTreeMapper.fetchRoot
com.pinecone.hydra.registry.ibatis.RegistryNodePathCacheMapper.queryGUIDByPath
```

曾经 500 根因：

```text
org.apache.ibatis.binding.BindingException:
Invalid bound statement (not found):
com.pinecone.hydra.registry.ibatis.RegistryTreeMapper.fetchRoot

org.apache.ibatis.binding.BindingException:
Invalid bound statement (not found):
com.pinecone.hydra.registry.ibatis.RegistryNodePathCacheMapper.queryGUIDByPath
```

误判路径：

- 一开始以为是 Spring `hydraniumSqlSessionFactory` mapperLocations 未扫到。
- 曾在 Shadow Spring datasource 中显式加入 `classpath*:mapper/kernel/registry/*.xml`。
- 但这不足以解决 Registry runtime，因为 Registry KOM driver 不走 Spring 的那个 SqlSessionFactory。

真实根因：

- `ArchTres.prepare_kernel_object_models()` 创建的是：

```text
RegistryMappingDriver(this, MySQLKingSystem IbatisClient, dispenserCenter)
```

- `RegistryMappingDriver` 继承 `ArchMappingDriver`。
- `ArchMappingDriver` 原来只加载 task 相关 XML scope。
- Registry XML mapper 没被 `MySQLKingSystem` 的 IbatisClient 加载。

最终修复点：

```text
/Users/wujunhong/projs/Hydra/Hydra/hydra-kom-default-driver/src/main/java/com/pinecone/hydra/registry/ibatis/hydranium/RegistryMappingDriver.java
```

新增：

```java
ibatisClient.addXMLObjectScope( "mapper.kernel.registry" );
```

如果重启后仍然报 `Invalid bound statement`，优先判断：

- 5082 后端没有重启。
- IntelliJ Debug classpath 仍在使用旧 `hydra-kom-default-driver`。
- Maven 没 reload / 没 install 新 jar。
- runtime 用的不是当前 target/classes 或新 jar。

## 7. Registry `/` 语义，必须记住

这是后续最容易踩坑的点。

Registry instrument 不会默认创建真实 `/` 节点。

内核语义：

```text
parent_guid IS NULL = 所有原始根节点
```

所以：

```text
/ 不是真实 RegistryTreeNode
/ 是 Console / API 层的虚拟根
```

Console 层必须 VIP 特判：

- `roots()` 返回虚拟 `/`。
- `queryPath("/")` 返回虚拟 `/`。
- `children("/")` 返回 `komRegistry.fetchRoot()` 的真实根节点列表。
- `getProperties("/")` 返回空 map。
- `listProperties("/")` 返回空 list。
- `getTextValue("/")` 返回空 DTO。
- 不要对 `/` 调 `komRegistry.queryElement("/")`。
- 不要指望 mapper 中存在 `/` 的 cache path。

当前实现位置：

```text
/Users/wujunhong/projs/shadow-prime/Saurons/Sparta/sparta-registry-console/src/main/java/com/sparta/registry/console/service/RegistryConsoleServiceImpl.java
```

关键方法：

```text
roots()
queryPath(String path)
children(String path)
isVirtualRootPath(String path)
virtualRootNode()
rootChildren()
normalizeRegistryPath(String path)
```

当前 `virtualRootNode()`：

```text
name = "/"
path = "/"
metaType = "RegistryRoot"
nodeType = "Root"
```

## 8. Shadow Sparta Registry Console 后端

模块路径：

```text
/Users/wujunhong/projs/shadow-prime/Saurons/Sparta/sparta-registry-console
```

父 POM 已加入：

```text
/Users/wujunhong/projs/shadow-prime/Saurons/Sparta/pom.xml
```

模块：

```xml
<module>sparta-registry-console</module>
```

主要结构：

```text
com.sparta.registry.console
  controller
    RegistryNodeController.java
    RegistrySelectorController.java
    RegistryValueController.java
  entity
    RegistryAffinityDTO.java
    RegistryCopyDTO.java
    RegistryMoveDTO.java
    RegistryNodeVO.java
    RegistryPathDTO.java
    RegistryPropertiesDTO.java
    RegistryPropertyDTO.java
    RegistryPropertyVO.java
    RegistryRenameDTO.java
    RegistrySelectorDTO.java
    RegistryTextValueDTO.java
  service
    RegistryConsoleService.java
    RegistryConsoleServiceImpl.java
  support
    RegistryNodeVOFactory.java
```

后端设计要点：

- 使用共享 `KOMRegistry` bean。
- 不再 new 一个新的 `GenericKOMRegistry`。
- 不再每次从 `CentralizedRuntimeInstrument` 里动态取 instrument，避免性能很差。
- Registry 是共享内核对象，Console 侧应注入并缓存稳定引用。
- DTO / VO 放在 `entity` 包，贴近当前 Sparta 代码风格。

当前 API 前缀：

```text
/api/registry
```

前端 dev 代理后访问形态：

```text
http://localhost:9528/registry-api/api/registry/...
```

示例 API：

```text
GET    /api/registry/node/roots
GET    /api/registry/node/query/path?path=/
GET    /api/registry/node/query/guid?guid=...
GET    /api/registry/node/children?path=/
PUT    /api/registry/node/affirm/namespace
PUT    /api/registry/node/affirm/properties
PUT    /api/registry/node/affirm/text
PUT    /api/registry/node/rename
DELETE /api/registry/node/remove?path=...
GET    /api/registry/value/properties?path=...
GET    /api/registry/value/property/list?path=...
PUT    /api/registry/value/property
DELETE /api/registry/value/property?path=...&key=...
GET    /api/registry/value/text?path=...
PUT    /api/registry/value/text
POST   /api/registry/selector/query
POST   /api/registry/selector/query-j
POST   /api/registry/selector/query-all
```

## 9. Shadow Runtime Wiring

Registry Console 后端接入 Shadow runtime 的相关修改点：

```text
/Users/wujunhong/projs/shadow-prime/Saurons/Tres/shadow-framework-system/pom.xml
/Users/wujunhong/projs/shadow-prime/Saurons/Tres/shadow-framework-system/src/main/java/com/sauron/tres/system/conf/InletComponentScanConfig.java
/Users/wujunhong/projs/shadow-prime/Saurons/Tres/shadow-framework-system/src/main/java/com/sauron/shadow/system/conf/ComponentScanConfig.java
```

另外曾为运行时缺类加入依赖：

```text
hydra-device-control
skynet-framework-device
```

相关路径：

```text
/Users/wujunhong/projs/shadow-prime/Saurons/Tres/shadow-system-client/pom.xml
/Users/wujunhong/projs/shadow-prime/Saurons/Tres/tres-spring-kernel/pom.xml
```

曾遇到缺类：

```text
java.lang.NoClassDefFoundError:
com/pinecone/hydra/device/registry/server/DeviceManager

java.lang.NoClassDefFoundError:
com/acorn/skynet/device/conduct/CollectiveDeviceRegiment
```

处理方向：

- 不是 Registry Console 自身类缺失。
- 是 ShadowBoot 装载系统 lord / subsystem 时，classpath 缺 Hydra Device / Skynet Device 相关包。
- 加 dependency 后需要 Maven reload 和重启 Debug runtime。

## 10. Spring Hydranium Datasource 注意事项

曾修改两个 Spring datasource config：

```text
/Users/wujunhong/projs/shadow-prime/Saurons/Tres/shadow-framework-system/src/main/java/com/sauron/shadow/system/conf/HydraniumDataSourceConfig.java
/Users/wujunhong/projs/shadow-prime/Saurons/Tres/shadow-system-client/src/main/java/com/sauron/shadow/system/conf/HydraniumDataSourceConfig.java
```

加入显式 mapper locations：

```text
classpath*:mapper/hydranium/*.xml
classpath*:mapper/kernel/registry/*.xml
```

原因：

- Spring `{hydranium,kernel/registry}` 这类 pattern 曾测试为 0 resources。
- 显式 merge 更稳。

但重要提醒：

- 这个不是 Registry KOM runtime `Invalid bound statement` 的最终修复。
- 真正修复仍然是 `RegistryMappingDriver` 中 `addXMLObjectScope("mapper.kernel.registry")`。
- Spring mapperLocations 未来只对 Spring 管理的 mapper 有意义。

## 11. Frontend Registry Console

前端仓库：

```text
/Users/wujunhong/GolandProjects/shadow-platform-prime-fe
```

已新增 / 修改重点：

```text
src/api/registry-console.js
src/router/modules/config.js
src/views/config/registry/index.vue
src/components/kernel-tree-workbench/PathToolbar.vue
src/components/kernel-tree-workbench/TreeNavigator.vue
src/components/kernel-tree-workbench/WorkbenchShell.vue
src/config/api-path.js
src/config/service-endpoint.js
src/i18n/local-messages.js
src/lang/index.js
src/utils/local-i18n.js
src/utils/route-title.js
vue.config.js
.env.development
```

产品入口：

```text
/config/registry
```

导航层级：

```text
配置中台
  注册表

推送中台
  ...
```

注意：

- `配置中台` 和 `推送中台` 是一级兄弟节点。
- 不是“推送中台 / 配置中台 / 注册表”。

Registry API 前端 endpoint：

```env
VUE_APP_REGISTRY_API = '/registry-api'
VUE_APP_REGISTRY_API_TARGET = 'http://localhost:5082'
```

`.env.development` 当前还包括：

```env
VUE_APP_ODIN_API = '/odin-api'
VUE_APP_ODIN_API_TARGET = 'http://localhost:5082'
VUE_APP_PUSH_API = '/push-api'
VUE_APP_PUSH_API_TARGET = 'http://localhost:5082'
VUE_APP_REGISTRY_API = '/registry-api'
VUE_APP_REGISTRY_API_TARGET = 'http://localhost:5082'
```

`vue.config.js` 代理已包含：

```text
VUE_APP_REGISTRY_API -> VUE_APP_REGISTRY_API_TARGET
```

如果前端仍然请求：

```text
/push-api/api/registry/...
```

说明前端 dev server 没重启或旧 bundle 仍在运行。

正确请求应为：

```text
/registry-api/api/registry/...
```

## 12. 多语言状态

用户指出旧多语言功能 malfunction，乱七八糟。

当前处理方向：

- 引入本地消息聚合，参考兄弟项目：

```text
/Users/wujunhong/GolandProjects/ethercraft-client
```

- 当前新增：

```text
/Users/wujunhong/GolandProjects/shadow-platform-prime-fe/src/i18n/local-messages.js
/Users/wujunhong/GolandProjects/shadow-platform-prime-fe/src/lang/index.js
/Users/wujunhong/GolandProjects/shadow-platform-prime-fe/src/utils/local-i18n.js
/Users/wujunhong/GolandProjects/shadow-platform-prime-fe/src/utils/route-title.js
```

已有语言示例包括：

```text
route.push
route.config
registry.nav.subtitle
```

后续原则：

- 菜单标题使用 `titleKey`。
- 页面文案从 i18n/local message 取。
- 不要在 Registry 页面继续散落大量硬编码。
- 如果要彻底修多语言，需要统一 Sidebar / Breadcrumb / TagsView / HeaderSearch / Navbar 的 title resolution。

## 13. Odin 任务树顺手修正

用户指出当前 Odin 任务树请求不对：

```text
GET http://localhost:9528/odin-api/api/v2/task/query/task/path?path=root
```

期望去掉重复 `task`：

```text
GET http://localhost:9528/odin-api/api/v2/task/query/path?path=root
```

已改前端：

```text
/Users/wujunhong/GolandProjects/shadow-platform-prime-fe/src/config/api-path.js
```

`taskQueryPath` 改为：

```text
/api/v2/task/query/path
```

后端兼容映射：

```text
/Users/wujunhong/projs/shadow-prime/Saurons/Sparta/sparta-odin-console/src/main/java/com/sparta/odin/console/controller/TaskInstrumentController.java
```

兼容：

```java
@GetMapping({ "query/path", "query/task/path" })
```

这样新旧前端请求都能落到同一方法。

任务树视觉：

- 用户说旧任务树还是圆圆风格，本轮顺手引入了矩形化整理样式。
- 前端新增 / 修改中有：

```text
src/views/odin/task-tree/styles/task-tree-rectify.css
src/views/odin/task-tree/index.vue
```

后续如果继续整理，应优先复用 `kernel-tree-workbench` 公共层，而不是给 Odin / Registry 各自再造一套。

## 14. 404 / 500 排障历史

### 14.1 前端 404

曾出现：

```text
GET http://localhost:9528/push-api/api/registry/node/query/path?path=%2F 404
GET http://localhost:9528/push-api/api/registry/node/roots 404
```

原因：

- 前端把 Registry API 走到了 `push-api`。

修复方向：

- 新增独立 `registry-api` endpoint。
- `.env.development` 配置 `VUE_APP_REGISTRY_API`。
- `vue.config.js` 增加代理。
- `src/api/registry-console.js` 使用 `REGISTRY_API`，不走 `PUSH_API`。

### 14.2 Registry API 500

曾出现：

```text
GET http://localhost:9528/registry-api/api/registry/node/roots 500
GET http://localhost:9528/registry-api/api/registry/node/query/path?path=%2F 500
```

后端日志核心：

```text
Invalid bound statement (not found):
com.pinecone.hydra.registry.ibatis.RegistryNodePathCacheMapper.queryGUIDByPath

Invalid bound statement (not found):
com.pinecone.hydra.registry.ibatis.RegistryTreeMapper.fetchRoot
```

分两层处理：

1. `queryPath("/")` 不应该打到 KOM，已在业务层做虚拟根特判。
2. `fetchRoot` 是真实需要的 mapper statement，必须由 `RegistryMappingDriver` 加载 `mapper.kernel.registry`。

如果当前运行仍 500：

- 先重启 5082 ShadowBoot。
- 确认 runtime classpath 加载新 `RegistryMappingDriver`。
- 确认 XML mapper 被打进 jar / classes。

## 15. 验证记录

Hydra Registry / Driver 编译：

```bash
cd /Users/wujunhong/projs/Hydra/Hydra
mvn -q -pl hydra-framework-config,hydra-kom-default-driver -am -DskipTests package
```

结果：已通过。

Sparta Registry Console 编译：

```bash
cd /Users/wujunhong/projs/shadow-prime/Saurons/Sparta
mvn -q -pl sparta-registry-console -am -DskipTests package
mvn -q -pl sparta-registry-console -am -DskipTests install
```

结果：已通过。

字节码确认：

```text
javap 确认 RegistryMappingDriver 包含 mapper.kernel.registry
javap 确认 RegistryConsoleServiceImpl 包含虚拟根相关方法
```

前端：

```text
Registry / Odin touched files eslint 通过
```

已知限制：

- `shadow-framework-system` 全量 compile / package 可能因 unrelated 旧 local/nexus business jars 缺失失败。
- 当前 5082 是 IntelliJ Debug `ShadowBoot`，需要重启才会加载新 classpath。

## 16. 当前脏工作树提醒

Hydra repo 当前很脏，包含：

- Registry 内核现代化改动。
- Registry XML mapper 新目录。
- Service XMLify 相关改动。
- Device / Skynet 相关改动。
- 大量 `.idea`、`.iml`、`target/` 输出。

Shadow repo 当前很脏，包含：

- `sparta-registry-console` 新模块。
- `sparta-redqueen-console`、`sparta-skynet-console` 等其他进行中模块。
- Shadow runtime wiring 修改。
- 系统 setup JSON 修改。
- 大量 `target/` 输出。

Frontend repo 当前很脏，包含：

- Registry Console 页面。
- Config Center 路由。
- `kernel-tree-workbench` 公共组件。
- 多语言基础改造。
- Odin task tree 矩形化样式。
- `.env.development` 和 proxy 修改。

后续接手不要做：

```bash
git reset --hard
git checkout -- .
```

除非用户明确要求。

## 17. 重启检查清单

后端 5082：

1. 在 Hydra 中确认 `hydra-kom-default-driver` 已 package / install。
2. IntelliJ Maven reload。
3. 确认 ShadowBoot runtime classpath 使用新 classes / jar。
4. 重启 `com.sauron.shadow.ShadowBoot`。
5. 观察启动期是否仍有 `NoClassDefFoundError`。

前端 9528：

1. `.env.development` 改动后必须重启 dev server。
2. 确认 dev server 使用 `VUE_APP_REGISTRY_API = /registry-api`。
3. 浏览器硬刷新，避免旧 bundle。

优先 smoke test：

```bash
curl 'http://localhost:9528/registry-api/api/registry/node/roots'
curl 'http://localhost:9528/registry-api/api/registry/node/query/path?path=%2F'
curl 'http://localhost:9528/registry-api/api/registry/node/children?path=%2F'
curl 'http://localhost:9528/odin-api/api/v2/task/query/path?path=root'
```

预期：

- `/roots` 返回虚拟 `/`。
- `/query/path?path=/` 返回虚拟 `/`。
- `/children?path=/` 返回真实 root children，或空数组，但不应 500。
- Odin 新路径应 200；旧 `/query/task/path` 可兼容。

## 18. 后续施工建议

短线优先：

1. 重启 5082 ShadowBoot，验证 Registry XML mapper 是否真正加载。
2. 如果仍 500，先查 runtime classpath，不要再改业务代码。
3. 验证 `/` 虚拟根三件套：roots / queryPath("/") / children("/")。
4. 打开 `/config/registry` 验证树加载和路径跳转。
5. 修前端错误态：后端 500/404 时页面不要抛 uncaught promise。

中线内核：

1. 清理 `ConfigNodeMeta` / `NamespaceMeta` entity 和接口残留。
2. 整理 `node_meta_guid` 顶级 KOM 槽位注释，明确“保留但 Registry 当前不用”。
3. 给 Registry XML mapper 补最小集成测试或 mapper statement 检测。
4. 检查 `long_path` 写入策略，目前 path cache 先只按现有接口适配。

中线产品：

1. Registry Console 支持新增节点弹窗、属性编辑、text editor、删除确认。
2. Selector 查询做成右侧工具面板。
3. 增加审计 / 历史 / 快照 / diff。
4. 增加环境 / 发布 / 回滚时，再参考 Apollo；不要过早污染内核 Registry 模型。

前端公共层：

1. 继续沉淀 `kernel-tree-workbench`，让 Odin task tree 和 Registry tree 共用布局、路径栏、树导航。
2. 统一多语言 title resolution。
3. 继续把圆角风格整理为更中台化、矩形、密集、稳定的界面。

## 19. 一句话恢复提示

下次上下文恢复时，先记住：

```text
Registry 的 / 是 Console 虚拟根；真实根是 parent_guid IS NULL。
Registry XML mapper 必须由 RegistryMappingDriver 加载 mapper.kernel.registry。
前端 Registry 走 /registry-api，不走 /push-api，也不走 /conf/registry。
node_meta_guid 字段保留但默认 NULL，conf/ns meta 表已从代码依赖中清掉。
```

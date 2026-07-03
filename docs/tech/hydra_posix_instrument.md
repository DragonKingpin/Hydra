# Hydra POSIX Instrument

本文是 Hydra Instrument、Privy、UOFS/TitanFS、Red、S3、Shuttle 之间的标准权威关系文件。
避免路径语义散落在各模块中重复定义。

## Hydra Instrument 架构

Instrument 指的是中央权力架构中的编制器，是专门的元信息组织数据库，用于给各种对象进行身份授予、编制化、索引等信息编组目的。

在 Hydra 中每个对象都被严格管理，拥有唯一的对象路径和 GUID，它们都在 Instrument 的管辖范围内。
Instrument 采用 GUID/Handle 为核心寻址方式作为对象身份证系统，同时采用 KOM 树将对象进行标准 POSIX 路径投影。

Privy 是 Instrument 体系的最高权威投影系统，持有或暴露 ExpressInstrument 能力。用于统筹各个领域的 Instrument 并投影到统一路径体系中。

## 子系统关系
### Titan
Titan 是 Hydra 的存储领域，并提供 UOFS、卷系统、桶系统、S3 等核心功能的底层支持。
其中由于 Hydra 的类 Unix 特性，文件即对象的思想，因此对象系统的路径化访问由 Titan 领域贯彻落实。

### UOFS / TitanFS
UOFS 是 Hydra 底层文件系统。TitanFS 是 UOFS 在 Titan 存储域中的商品名或领域别名。

UOFS/TitanFS 负责 bucket、volume、folder、file 等真实存储模型。

### Red Protocol
Red Protocol 是 Hydra 体系的 "传送门" 协议。

背景：由于 Hydra 是分布式的范式，不能像传统操作系统一样 "丝滑" 的进行中心化的本地路径寻址，需要考虑网络系统的高延迟、不稳定、难以随机寻址的特性。
但是又希望类似操作系统一样拥有权威的中心化对象控制和访问系统，因此特地设计的一款协议，便于全局的对象权威化、胶水路由、存储和读写访问。

#### Red-Titan
Red-Titan 是实际存储微服务，提供真实且终末的 UOFS/TitanFS 对象存储网络服务。它直通 UOFS/TitanFS，负责对象读写、列表、元数据、Range 等能力。

Red-Titan 支持对象命名空间下的 red:// 与 s3:// 访问，但原则上不承载 red:/// 形式的 Privy 内核路径。

#### Red-Shuttle
Red-Shuttle 是网关级胶水服务。它代理 Red-Titan，同时挂载权威 Privy，提供内核句柄路径映射能力。

Red-Shuttle 是 `red:///...` 与 S3 内核桶映射的权威入口。

### S3
S3 是 Red 的底层存储范围协议，负责标准 bucket/key 对象访问。S3 不定义 Privy 路径，也不直接定义 Hydra 内核对象语义。

## RED 协议族
RED 是 S3++ 协议，兼容 S3，同时提供统一对象存储与内核路径访问，为三层架构：

1. S3 Object Protocol：标准 bucket/key 对象访问，底层读写语义（读写门面协议层）。
2. UOFS/TitanFS Object Projection：把 UOFS 文件系统投影为对象存储。
3. RED Kernel/Presentation Protocol：负责 `red://`、`red:///`、`__SYSTEM__`、`__RED__`、Privy 路由与控制面。

示例：

```text
s3://localhost:1234/root@bucket/demo.txt
red://localhost:1234/root@bucket/demo.txt
red://localhost:1234/bucket_full_name/demo.txt
{protocol}://{endpoint}/{bucket_identifier}/demo.txt
{protocol}://{endpoint}/{user_name}@{bucket}/demo.txt
  -> Red-Titan -> UOFS/TitanFS

red://{endpoint}/proc/123/status
red:///proc/123/status
s3://{endpoint}/__SYSTEM__/proc/123/status
  -> Red-Shuttle -> Privy -> ExpressInstrument
```

## Privy 权威路径
Privy 路径必须由 Hydra 内核权威定义，不能由业务模块散落手写，采用类 Linux 风格。

当前核心根路径应来自 `KernelRootMountPoint` 与 `KernelObjectRootMountPoint`：

```text
/       Kernel root
/conf   Config
/dev    Device
/home   UserHome
/mnt    Mount
/sys    System
/proc   Process
/var    Variable
/meta   Meta
```

## 预留标签
当前存储与 RED 相关预留标签如下：

```text
__SYSTEM__  合法，S3 协议内核桶映射。
__RED__     合法，RED 控制面，替代旧的 __red__。
__API__     合法，服务 API 面，替代旧的 __api__。
```

其中 __SYSTEM__ 只用于 S3/path-style 协议中的内核桶映射，不代表真实 UOFS bucket。
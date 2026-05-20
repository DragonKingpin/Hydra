# UOFS / Red Kernel Shuttle Tombstone

Date: 2026-05-21

Purpose: preserve the current system design context for recovery, migration, and future implementation work around UOFS, Red protocol, TitanAether, Red Shuttle, distributed kernel namespace traversal, and S3-compatible object exchange.

## Executive Summary

The mainline has moved from the early `uofs://` draft into the `red://` protocol line.

Red is the UOFS transport and addressing protocol. It is intentionally a POSIX + WinNT + S3 hybrid:

- POSIX-like paths for kernel namespace traversal.
- WinNT-like hidden kernel handle/topology semantics.
- S3-like object storage exchange at the network and data plane.

The core rule is that there should not be two parallel path protocols, one S3 and one Red. Red is the unified protocol:

```text
red:///<kernel-path>
red://<bucket>/<path>
```

Red is effectively S3++:

- It must remain compatible with basic S3 semantics so ordinary S3 clients can still work where appropriate.
- It also carries distributed kernel topology duties such as `/proc`, `/etc`, `/dev`, `/mnt`, global metadata distribution, and Privy projection.

Current priority is no longer "MVP wire something quickly". The priority is architecture stabilization:

1. Keep the Aetherium module layering clean.
2. Keep S3 core reusable and do not reimplement S3 again in Red.
3. Keep Spring out of Redstone core packages.
4. Host Spring controllers/services in Shadow/Spartanian only.
5. Build Red Shuttle as a durable async proxy/exchange layer before adding full Privy proxy semantics.

## Current Module Architecture

The accepted Aetherium layering is:

```text
redstone-aetherium-arch
  -> redstone-aetherium-s3
    -> redstone-aetherium-red
      -> redstone-aetherium-shuttle
      -> sparta-titan-aetherium-red
```

Module meaning:

```text
redstone-aetherium-arch
  Shared object/resource abstractions. No Spring.

redstone-aetherium-s3
  S3 protocol core support library. No concrete service implementation. No Spring.

redstone-aetherium-red
  Red protocol core support library. Reuses S3 package behavior instead of duplicating it. No Spring.

redstone-aetherium-shuttle
  Red shuttle/proxy/client-side exchange support. No Spring. Provides HTTP client, target routing, async exchange, lifecycle, and config objects.

sparta-titan-aetherium-red
  Titan-side Red core service implementation. This is the concrete service version.

red-shuttle-service
  Shadow/Spartanian-hosted Spring service wrapper around redstone-aetherium-shuttle.
```

Important workspace paths:

```text
E:\MyFiles\CodeScript\Project\Hazelnut\Sauron\Saurons\Aetherium
E:\MyFiles\CodeScript\Project\Hazelnut\Sauron\sparta-titan
E:\MyFiles\CodeScript\Project\Hazelnut\Sauron\shadow-prime\Saurons\spartanian
```

## Package Direction

Kernel Red/Aether package names:

```text
com.walnut.redstone.ether
com.walnut.redstone.ether.shuttle
```

Naming rule:

- Do not put `Red` on every core type.
- Use simple names in arch/core packages.
- Use `Red` only where the protocol boundary or concrete identity requires it.

Accepted core arch names:

```text
ObjectExpressInstrument
ObjectExchange
ObjectOperation
OperationContext
ResourceUri
ResourcePath
ResourceNamespace
ResourceRoute
ResourceProjection
ResourceType
ResourceCapability
```

Important semantic decision:

```text
ObjectStore -> ObjectExpressInstrument
```

This should align with Pinecone regime/instrument vocabulary, not invent a second "store" universe.

Avoid introducing `ResourceHandle` as a first-class opened handle abstraction for the first round. Kernel object handles already exist as Hydra kernel objects, especially:

```text
E:\MyFiles\CodeScript\Project\Hazelnut\Sauron\Saurons\Hydra\hydra-architecture\src\main\java\com\pinecone\hydra\system\ko
```

`ElementObject` is the kernel-side handle concept. On the user/client side, ordinary S3 drivers already have their own effective handle/stream lifecycle.

## Protocol Semantics

Canonical Red URI forms:

```text
red:///<kernel-path>
red://<bucket>/<path>
```

Examples:

```text
red:///proc/pid/status
red:///etc/...
red:///dev/...
red:///mnt/...
red:///conf/...
red://root@block-striped/avatar.png
red://public/process-images/demo.jar
```

Interpretation:

```text
red:///...
  Empty authority. Kernel namespace path. Resolve through Privy / authority kernel topology.

red://bucket/...
  Non-empty authority. Standard S3ified object storage path.
```

The root namespace `/` can be understood as a special bucket-like kernel namespace. For example:

```text
GET /proc/pid/status
```

is conceptually an object/file read, even if the content is projected from a kernel instrument.

Reserved extension space is allowed:

```text
red:///__xxx__
```

This can host future protocol metadata, control records, reserved capabilities, or introspection surfaces.

## Kernel Namespace Model

Hydra is expected to be fully microservice/distributed in formal deployment. There will be authority nodes providing consistent kernel services. Those authority nodes can reverse-proxy Titan S3/Red services for `/mnt`, `/dev`, and other addressable resources.

Current Privy reference:

```text
E:\MyFiles\CodeScript\Project\Hazelnut\Sauron\Saurons\Hydra\hydra-system-reign\src\main\java\com\pinecone\hydra\reign\UnixInstitutionalizedMetaImperiumPrivy.java
```

Current test/reference:

```text
E:\MyFiles\CodeScript\Project\Hazelnut\Sauron\Saurons\Sparta\sparta-core-console\src\test\java\com\ender\TestEnderHydra.java
```

`UnixInstitutionalizedMetaImperiumPrivy` is currently not fully finished. The intended final model is Linux-like:

```text
/etc
/proc
/dev
/mnt
/sys
/conf
```

The command-line and path interaction should feel POSIX-like, while the underlying kernel object/control style remains closer to WinNT.

Mental model:

```text
Windows instrumentation implements Unix-like paths.
Titan itself is an Instrument.
Accessing /bucket can mean accessing an Instrument.
\Device-like hidden topology exists, but Red exposes a distributed POSIX/S3-shaped facade.
```

## TitanAether State

The old `sparta-titan-s3` line has been upgraded conceptually toward:

```text
sparta-titan-aetherium-red
```

Preferred service name:

```text
TitanAether
```

Do not call it `TitanAetherium`; Aetherium is the kernel/protocol family, TitanAether is the Titan-side service.

Important endpoint observed/used:

```text
http://localhost:5481
```

Example S3 read test shape:

```text
/root@block-striped/avatar.png
```

The system successfully reached the point where a S3-style read service address could be tested against Titan.

## Red Shuttle Current State

Red Shuttle currently has two layers:

```text
redstone-aetherium-shuttle
red-shuttle-service
```

### Redstone Core Layer

Path:

```text
E:\MyFiles\CodeScript\Project\Hazelnut\Sauron\Saurons\Aetherium\redstone-aetherium-shuttle
```

Current core classes:

```text
com.walnut.redstone.ether.shuttle.lifecycle.ShuttleKernel
com.walnut.redstone.ether.shuttle.lifecycle.ShuttleLifecycle
com.walnut.redstone.ether.shuttle.lifecycle.ShuttleStatus
com.walnut.redstone.ether.shuttle.exchange.ShuttleExchange
com.walnut.redstone.ether.shuttle.exchange.ShuttleRequest
com.walnut.redstone.ether.shuttle.exchange.ShuttleResponse
com.walnut.redstone.ether.shuttle.exchange.ShuttleMethod
com.walnut.redstone.ether.shuttle.http.HttpClient5AsyncFactory
com.walnut.redstone.ether.shuttle.http.HttpClient5AsyncShuttleExchange
com.walnut.redstone.ether.shuttle.route.ShuttleTargetResolver
com.walnut.redstone.ether.shuttle.config.*
com.walnut.redstone.ether.shuttle.error.*
```

Current capability:

- Apache HttpClient 5 async client.
- Configurable target list.
- Configurable pool, timeout, retry, header policy, proxy, TLS objects.
- `ShuttleKernel` lifecycle.
- Basic async exchange.

Important constraint:

```text
redstone-aetherium-shuttle must not depend on Spring.
```

It is a long-term middle-platform kernel/service library, not a web app.

### Shadow / Spartanian Service Wrapper

Path:

```text
E:\MyFiles\CodeScript\Project\Hazelnut\Sauron\shadow-prime\Saurons\spartanian\red-shuttle-service
```

Current service classes:

```text
com.sparta.red.shuttle.config.RedShuttleConfigLoader
com.sparta.red.shuttle.service.RedShuttleKernelProvider
com.sparta.red.shuttle.service.RedShuttleConsoleService
com.sparta.red.shuttle.service.RedShuttleConsoleServiceImpl
com.sparta.red.shuttle.controller.RedShuttleController
```

Current exposed routes:

```text
GET /api/red-shuttle/status
GET /api/red-shuttle/config
GET /api/red-shuttle/proxy/{target}/**
```

Current limitation:

- Only GET proxy is exposed.
- Request and response bodies currently become byte arrays in key places.
- Header policy is only partly realized.
- Proxy/TLS/retry config objects exist but are not fully applied.
- PrivyProxy is not implemented yet.
- `red:///` kernel path semantics are not exposed yet.

## Red Shuttle Config

Kernel config:

```text
E:\MyFiles\CodeScript\Project\Hazelnut\Sauron\shadow-prime\system\setup\spartanian\RedShuttleKernel.json5
```

Current target:

```json5
{
  "name": "local-titan-aether",
  "kind": "TitanAether",
  "baseUrl": "http://localhost:5481",
  "enabled": true,
  "weight": 100
}
```

Default target:

```text
local-titan-aether
```

Spring kernel config:

```text
E:\MyFiles\CodeScript\Project\Hazelnut\Sauron\shadow-prime\system\setup\spartanian\RedShuttleSpringKernel.json5
```

Current Red Shuttle service port:

```text
5477
```

JSON5 style rule:

- Keep keys quoted even though JSON5 allows unquoted keys.
- Use Shadow's existing config style.
- Keep config hosted in Shadow side, not in Redstone core.

## Shadow / Spartanian Hosting State

Initial attempt placed `red-shuttle-service` under:

```text
E:\MyFiles\CodeScript\Project\Hazelnut\Sauron\shadow-prime\Saurons\Sparta
```

It was moved to:

```text
E:\MyFiles\CodeScript\Project\Hazelnut\Sauron\shadow-prime\Saurons\spartanian\red-shuttle-service
```

Spartanian module split:

```text
spartanian-kernel
  Ordinary jar. Shadow-scanned bootstrap and RedShuttleSpringKernel.

red-shuttle-service
  Ordinary jar. Spring controller/service wrapper.

spartanian-system
  Independent main/fat jar development entry.
```

Shadow scans:

```text
com.sauron.tres
```

Therefore the Shadow bootstrap package is:

```text
com.sauron.tres.spartanian
```

Red Shuttle Spring boot/config classes were moved away from `com.sauron.tres` into:

```text
com.walnut.spartanian.red
```

This prevents Shadow's main Spring context from directly scanning Red Shuttle controllers. The child Spring kernel should own those controllers.

## VIP Bootstrap Fix

Problem observed:

The first embedded Shadow bootstrap did this:

```java
new Spartanian(new String[0], Pinecone.sys()).init(...)
```

This was wrong because `Spartanian extends ArchTres`, and `ArchTres -> EnderHydra -> Tritium` constructs the full Hydra skeleton. Even `new Spartanian(...)` triggers `Tritium.prepare_system_skeleton()`, which calls into `EnderHydra.prepare_modularized_subsystem()` and loads:

```text
KernelSkynetLord
KernelRedQueenLord
```

This caused Shadow to load Skynet/RedQueen again and then produced a cast failure in Heist paths:

```text
ClassCastException: com.sauron.tres.spartanian.Spartanian cannot be cast to com.sauron.shadow.system.Shadium
```

Fix applied:

```text
E:\MyFiles\CodeScript\Project\Hazelnut\Sauron\shadow-prime\Saurons\spartanian\spartanian-kernel\src\main\java\com\sauron\tres\spartanian\shadow\SpartanianShadowBootstrap.java
E:\MyFiles\CodeScript\Project\Hazelnut\Sauron\shadow-prime\Saurons\spartanian\spartanian-kernel\src\main\java\com\sauron\tres\spartanian\shadow\SpartanianRedShuttleVipLauncher.java
```

Current intended behavior:

- `SpartanianShadowBootstrap` injects existing Shadow `parentSystem`.
- It creates `SpartanianRedShuttleVipLauncher`.
- The VIP launcher starts only `RedShuttleSpringKernel`.
- It does not instantiate `Spartanian`.
- It does not call `Spartanian.init()`.
- It does not call `Spartanian.vitalize()`.
- It does not summon a second EnderHydra.

VIP child context registers:

```text
guidAllocator
ulfInstanceManufacturer
systemInstanceScope
komRegistry
redShuttleSpringKernel
redShuttleVipLauncher
```

This keeps independent `Spartanian` mode possible while keeping Shadow embedded mode lightweight.

## Maven / Jar Policy

Hydra/Shadow side should not reference local module paths by adding sibling modules such as:

```xml
<module>../Saurons/Aetherium/redstone-aetherium-arch</module>
```

For Titan/Hydra/Shadow integration, prefer jar dependencies consistent with existing project standards.

Current Red Shuttle service POM still uses a systemPath jar for:

```text
redstone-aetherium-shuttle
```

This is acceptable as a temporary bridge but should be normalized later.

Also all packages should remain:

```xml
<packaging>jar</packaging>
```

Formatting standard:

- No compact one-line getters/setters in Hydra/Redstone-side code.
- Use normal multiline method bodies:

```java
public String getUri() {
    return this.uri;
}
```

Entity/bean member variables can avoid Hungarian naming. Kernel implementation classes can still use established kernel naming style where appropriate.

Known environment note:

At the time of this tombstone, `mvn` was not available on the active shell PATH, so compile verification may need to be run from the developer environment.

## Current Shuttle Capability Assessment

Current status:

```text
Shadow main system
  -> VIP RedShuttleSpringKernel :5477
    -> red-shuttle-service controller
      -> ShuttleKernel
        -> Apache HttpClient5 async
          -> local-titan-aether http://localhost:5481
```

Useful smoke URLs:

```text
http://localhost:5477/api/red-shuttle/status
http://localhost:5477/api/red-shuttle/config
http://localhost:5477/api/red-shuttle/proxy/local-titan-aether/...
```

The current state is a working architectural skeleton for an async HTTP proxy shuttle, not yet a complete Red/UOFS semantic shuttle.

## Next Mainline Tasks

Do not jump directly into full PrivyProxy. The next round should stabilize the exchange layer first.

Recommended order:

1. Complete shuttle exchange surface.

```text
GET
HEAD
PUT
POST
DELETE
```

All should map into one `ShuttleRequest` shape.

2. Make body forwarding stream-safe.

Current byte-array buffering is acceptable for tiny status/debug paths but not for S3/Red object movement. Red Shuttle must eventually support large object transfer without reading everything into memory.

3. Fully implement header policy.

Config exists:

```text
forwardHost
forwardAuthorization
forwardCookie
forwardRange
forwardContentType
forwardContentLength
blockedHeaders
```

The implementation must actually respect these rules.

4. Apply proxy/TLS/retry config.

Config classes already exist. The HTTP client factory/exchange should consume them correctly.

5. Normalize dependency pathing.

Move away from fragile `systemPath` when the local ecosystem is ready for proper jar dependency resolution.

6. Add Red semantic route layer.

Only after proxy exchange is stable:

```text
red://<bucket>/<path>     -> TitanAether/S3 object path
red:///<kernel-path>      -> Privy/authority kernel path
```

7. Implement PrivyProxy.

This is intentionally deferred because it includes:

```text
HTTP client
HTTP service
PrivyProxy
cross-boundary kernel topology traversal
```

This should not be mixed with the first exchange-layer stabilization.

## Deferred Work

Do not prioritize these until Red Shuttle exchange and Red protocol skeleton are stable:

- Full `/proc`, `/etc`, `/dev`, `/mnt` proxy implementation.
- Full Privy authority recursion.
- Process Manager product UI.
- UProcess/Troll process manager productization.
- Odin task manager product surface.
- Image registry and process image pull.
- Remote process kill/restart/retry controls.

## Mental Model

Red/UOFS is the distributed kernel shuttle:

```text
Kernel namespace path
  -> authority Privy / local Privy
  -> Red resource projection
  -> TitanAether or kernel Instrument
  -> S3-compatible object exchange where possible
```

For object storage:

```text
red://bucket/key
  -> S3-compatible exchange
  -> TitanAether
```

For kernel namespace:

```text
red:///proc/pid/status
  -> Privy path
  -> projected kernel object
  -> object-like read/write/control operation
```

The protocol should feel like "everything is a file", but the implementation is distributed, authority-aware, and object-storage-compatible.

The guiding principle:

```text
One Red path system.
S3 compatibility underneath.
Kernel topology above.
No duplicate S3-vs-Red protocol split.
```

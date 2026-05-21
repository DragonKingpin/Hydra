# Red Kernel Shuttle Tombstone

Date: 2026-05-21

Purpose: preserve the current Red Shuttle, TitanAether, and Privy kernel mapped-file context after the first working data-plane and kernel-namespace implementation round. This file supersedes the older UOFS draft context. The active line is Red.

## Executive Summary

The mainline is now:

```text
Red Shuttle :5477
  -> S3 object bucket paths
      -> TitanAether :5481
  -> EmptyString VIP bucket / kernel paths
      -> Privy / ExpressInstrument
      -> JSON/Text mapped-file projection
```

Red remains the unified protocol:

```text
red://<bucket>/<path>   -> object/S3-compatible namespace
red:///<kernel-path>    -> kernel namespace, bucket = ""
```

The key design decision from this round:

```text
red:///<kernel-path> is not a second protocol.
It is the EmptyString VIP bucket.
Operations stay S3/HTTP-shaped.
```

Current working user-facing shape:

```text
GET  http://localhost:5477/root@direct-object/avatar.png
GET  http://localhost:5477/
GET  http://localhost:5477/proc
HEAD http://localhost:5477/proc
PUT  http://localhost:5477/proc       -> 405, readonly kernel namespace
GET  http://localhost:5477/__red__/locate?path=/proc
```

## Repositories

Primary workspaces:

```text
/Users/wujunhong/projs/Hydra/Aetherium
/Users/wujunhong/projs/Hydra
/Users/wujunhong/projs/shadow-prime
/Users/wujunhong/projs/sparta-titan
```

Reparse link created:

```text
/Users/wujunhong/projs/shadow-prime/reparse/sparta-titan -> /Users/wujunhong/projs/sparta-titan
```

## Current Architecture

Aetherium layering remains:

```text
redstone-aetherium-arch
  -> redstone-aetherium-s3
    -> redstone-aetherium-red
      -> redstone-aetherium-shuttle
      -> sparta-titan-aether-red
```

Shadow/Spartanian wrapper:

```text
/Users/wujunhong/projs/shadow-prime/Saurons/spartanian
  red-shuttle-service
  spartanian-kernel
  spartanian-system
```

Redstone core must stay Spring-free. Spring controllers, dependency injection, and Hydra system object wiring live in Shadow/Spartanian.

## Red Shuttle Current Routes

New control namespace:

```text
GET /__red__/status
GET /__red__/config
GET /__red__/locate
GET/HEAD/PUT/POST/DELETE /__red__/proxy/{target}/**
```

Legacy aliases retained temporarily:

```text
GET /api/red-shuttle/status
GET /api/red-shuttle/config
GET/HEAD/PUT/POST/DELETE /api/red-shuttle/proxy/{target}/**
```

Data-plane root proxy:

```text
GET/HEAD/PUT/POST/DELETE /**
```

Control paths are excluded from data-plane proxy:

```text
/__red__/**
/api/red-shuttle/**
```

## TitanAether Data Plane

TitanAether runs at:

```text
http://localhost:5481
```

Known working direct Titan file:

```text
http://localhost:5481/root@direct-object/avatar.png
```

Known working Shuttle path after restart:

```text
http://localhost:5477/root@direct-object/avatar.png
```

Current Shuttle target config:

```text
/Users/wujunhong/projs/shadow-prime/system/setup/spartanian/RedShuttleKernel.json5
```

Target:

```json5
{
  "name": "local-titan-aether",
  "kind": "TitanAether",
  "baseUrl": "http://localhost:5481",
  "enabled": true,
  "weight": 100,
  "pathPrefixes": [
    "/",
    "/root@block-striped"
  ]
}
```

`local-titan-aether` is a Shuttle target name, not a business path. It is used by debug proxy:

```text
http://localhost:5477/__red__/proxy/local-titan-aether/root@direct-object/avatar.png
```

Normal callers should prefer the data-plane root path:

```text
http://localhost:5477/root@direct-object/avatar.png
```

## Route Semantics

Current core locator:

```text
/Users/wujunhong/projs/Hydra/Aetherium/redstone-aetherium-shuttle/src/main/java/com/walnut/redstone/ether/shuttle/route/StaticShuttlePathLocator.java
```

Recognized route types:

```text
S3_OBJECT
KERNEL_NAMESPACE
CONTROL
UNKNOWN
```

Route logic:

```text
/__red__/...        -> CONTROL
/                   -> KERNEL_NAMESPACE, bucket = ""
/proc/...           -> KERNEL_NAMESPACE, bucket = ""
/conf/...           -> KERNEL_NAMESPACE, bucket = ""
/dev/...            -> KERNEL_NAMESPACE, bucket = ""
/home/...           -> KERNEL_NAMESPACE, bucket = ""
/mnt/...            -> KERNEL_NAMESPACE, bucket = ""
/sys/...            -> KERNEL_NAMESPACE, bucket = ""
/var/...            -> KERNEL_NAMESPACE, bucket = ""
/meta/...           -> KERNEL_NAMESPACE, bucket = ""
/root@.../...       -> S3_OBJECT
```

Kernel namespace locate result uses:

```text
bucket = ""
targetName = "__kernel__"
routeType = KERNEL_NAMESPACE
```

## Privy / Kernel Mapped File

Privy is not modeled as another HTTP proxy. It is the kernel namespace backend behind the EmptyString VIP bucket.

Current Hydra concept:

```text
ImperiumPrivy
  -> ExpressInstrument
    -> mounted instruments / direct mapped handles
```

Important Hydra references:

```text
/Users/wujunhong/projs/Hydra/Hydra/hydra-architecture/src/main/java/com/pinecone/hydra/system/imperium/ImperiumPrivy.java
/Users/wujunhong/projs/Hydra/Hydra/hydra-system-reign/src/main/java/com/pinecone/hydra/reign/UnixInstitutionalizedMetaImperiumPrivy.java
/Users/wujunhong/projs/Hydra/Hydra/hydra-architecture/src/main/java/com/pinecone/hydra/system/imperium/KernelRootMountPoint.java
/Users/wujunhong/projs/Hydra/Hydra/hydra-architecture/src/main/java/com/pinecone/hydra/system/imperium/KernelObjectRootMountPoint.java
```

Kernel root mount points:

```text
conf
dev
home
mnt
sys
proc
var
meta
```

Object root mount examples:

```text
conf/kernel
conf/registry
meta/task
meta/service
dev/deploy
sys/public/global/exe/images
```

## Redstone Core Kernel Files

Core package:

```text
/Users/wujunhong/projs/Hydra/Aetherium/redstone-aetherium-shuttle/src/main/java/com/walnut/redstone/ether/shuttle/kernel
```

Files:

```text
KernelNamespaceBackend.java
KernelNamespaceExchange.java
KernelMappedFile.java
KernelMappedFileMeta.java
KernelMappedFileEncoder.java
GenericKernelMappedFileEncoder.java
```

Responsibilities:

```text
KernelNamespaceBackend
  Spring-free backend interface. Passed in by wrapper. Default write capability is false.

KernelNamespaceExchange
  Executes kernel namespace requests.
  GET  -> backend.read + encoder
  HEAD -> backend.stat
  PUT/POST/DELETE -> 405 Method Not Allowed

KernelMappedFile / KernelMappedFileMeta
  File-like projection envelope for kernel objects.

GenericKernelMappedFileEncoder
  First-round JSON/Text/Binary encoder.
```

Encoder behavior:

```text
String       -> text/plain; charset=utf-8
byte[]       -> application/octet-stream
Map/List     -> application/json; charset=utf-8
Pinenut      -> toJSONString()
Other object -> JSON.stringify(...) or fallback JSON string
null/missing -> 404
```

First version is intentionally JSON/Text oriented.

## Shadow Privy Adapter

Shadow-side package:

```text
/Users/wujunhong/projs/shadow-prime/Saurons/spartanian/red-shuttle-service/src/main/java/com/sparta/red/shuttle/privy
```

Files:

```text
PrivyKernelNamespaceBackend.java
PrivyKernelNamespaceConfig.java
```

Responsibilities:

```text
PrivyKernelNamespaceConfig
  Creates KernelNamespaceBackend bean from ExpressInstrument.

PrivyKernelNamespaceBackend
  Adapts ExpressInstrument to KernelNamespaceBackend.
  Projects root mount tables and basic TreeNode/KOMInstrument data to JSON maps.
  Readonly by default.
```

VIP launcher now registers `ExpressInstrument` into the child Red Shuttle Spring context:

```text
/Users/wujunhong/projs/shadow-prime/Saurons/spartanian/spartanian-kernel/src/main/java/com/sauron/tres/spartanian/shadow/SpartanianRedShuttleVipLauncher.java
```

The child context also already registers:

```text
guidAllocator
ulfInstanceManufacturer
systemInstanceScope
redShuttleSpringKernel
redShuttleVipLauncher
komRegistry
expressInstrument
```

## Red Shuttle Service Flow

Current key class:

```text
/Users/wujunhong/projs/shadow-prime/Saurons/spartanian/red-shuttle-service/src/main/java/com/sparta/red/shuttle/service/RedShuttleConsoleServiceImpl.java
```

Exchange flow:

```text
RedShuttleController
  -> RedShuttleConsoleServiceImpl.exchange(request)
    -> StaticShuttlePathLocator.locatePath(request.path)
      -> KERNEL_NAMESPACE
          -> KernelNamespaceExchange
          -> PrivyKernelNamespaceBackend
          -> GenericKernelMappedFileEncoder
      -> S3_OBJECT
          -> ShuttleKernel.exchange(request)
          -> HttpClient5AsyncShuttleExchange
          -> TitanAether
```

## Readonly Rule

The first kernel namespace version is readonly.

Allowed:

```text
GET
HEAD
```

Blocked:

```text
PUT
POST
DELETE
```

Blocked operations return:

```text
405 Method Not Allowed
Allow: GET, HEAD
X-Red-Kernel-Readonly: true
```

Future writable support must be whitelist-based, not default-open. Candidate future writable mount:

```text
/mnt/**
```

Do not make kernel objects writable without explicit path capability policy.

## Dependency Notes

`red-shuttle-service` currently uses `systemPath` Aetherium jars. Because `systemPath` does not bring transitive dependencies, these are explicitly listed:

```text
redstone-aetherium-shuttle
redstone-aetherium-red
redstone-aetherium-s3
```

Runtime classpath issue fixed in this round:

```text
NoClassDefFoundError: com/walnut/redstone/ether/red/uri/RedUriParser
```

Cause:

```text
redstone-aetherium-shuttle depended on redstone-aetherium-red,
but Shadow runtime classpath only had the shuttle jar.
```

Fix:

```text
Add redstone-aetherium-red and redstone-aetherium-s3 as explicit systemPath dependencies in red-shuttle-service/pom.xml.
```

Long-term cleanup:

```text
Install/publish Aetherium jars normally and remove systemPath.
```

## Verification Commands

Aetherium core:

```bash
cd /Users/wujunhong/projs/Hydra/Aetherium
mvn -pl redstone-aetherium-shuttle -am -DskipTests compile
mvn -pl redstone-aetherium-shuttle -am -DskipTests package
```

Spartanian wrapper:

```bash
cd /Users/wujunhong/projs/shadow-prime/Saurons/spartanian
mvn -pl spartanian-kernel,red-shuttle-service -am -DskipTests compile
```

These passed after the current implementation.

## Smoke Test URLs

After restarting `ShadowBoot`:

```text
GET  http://localhost:5477/__red__/status
GET  http://localhost:5477/__red__/config
GET  http://localhost:5477/__red__/locate?path=/proc
GET  http://localhost:5477/__red__/locate?uri=red:///proc
GET  http://localhost:5477/
GET  http://localhost:5477/proc
HEAD http://localhost:5477/proc
PUT  http://localhost:5477/proc
GET  http://localhost:5477/root@direct-object/avatar.png
```

Titan direct control test:

```text
GET http://localhost:5481/root@direct-object/avatar.png
```

Expected behavior:

```text
/__red__/status            -> Shuttle status JSON
/__red__/locate?path=/proc -> routeType KERNEL_NAMESPACE
/                            -> kernel root JSON
/proc                        -> JSON/Text mapped file or 404 if not mounted
PUT /proc                    -> 405
/root@direct-object/avatar.png -> image from Titan
```

## Naming and Style Notes

Hydra/Aetherium kernel-side code should use the project standard:

```text
this. always present
spaces around control parentheses
Hungarian naming for kernel-side members and regular variables where applicable
Arch/Generic naming conventions
```

Reference:

```text
/Users/wujunhong/projs/Hydra/docs/standard/coding_standard.md
```

Shadow business wrapper code can be less strict about Hungarian naming unless it is kernel-flavored code. Entity/bean code is not forced either way.

## Current TODO

Immediate:

```text
1. Restart ShadowBoot and smoke test kernel mapped-file routes.
2. Confirm / returns useful kernel root JSON.
3. Confirm /proc locate is KERNEL_NAMESPACE.
4. Confirm /root@direct-object/avatar.png still goes to Titan.
5. Fix any runtime projection issue from real ExpressInstrument contents.
```

Near term:

```text
1. Improve PrivyKernelNamespaceBackend path resolution beyond direct mounted handles.
2. Add directory listing projection for mounted KOMInstrument children.
3. Add richer TreeNode and registry node JSON projection.
4. Make /__red__/locate include capability flags: readable, writable, directory, backend.
5. Add explicit kernel mount prefix config instead of hardcoded root mount list.
6. Preserve root bucket-list compatibility if S3 clients require GET / for buckets.
```

Medium term:

```text
1. Streaming body support for large object movement.
2. Full request/response header policy hardening.
3. Proxy/TLS/retry config application.
4. Normal Maven dependency pathing.
5. Writable kernel paths via strict whitelist, probably /mnt first.
6. Privy authority recursion for distributed topology.
```

Deferred:

```text
Full /proc, /etc, /dev, /mnt semantics.
Remote authority traversal.
Process manager product UI.
Kernel write operations.
Full Privy control operations.
```

## Mental Model

The stable mental model:

```text
Red path
  -> bucket resolver
    -> bucket != ""
        -> S3 object namespace
        -> TitanAether
    -> bucket == ""
        -> EmptyString VIP bucket
        -> Privy / ExpressInstrument
        -> kernel mapped file
        -> JSON/Text encoder
```

Privy is the council, not a raw proxy. It provides kernel object information and mappings. Red Shuttle presents those mappings as files while keeping operations aligned with S3/HTTP.


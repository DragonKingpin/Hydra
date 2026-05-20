# UOFS Kernel Shuttle Tombstone

Date: 2026-05-20

Purpose: preserve the current system design context for recovery, migration, and future implementation work around UOFS, Titan, kernel namespace traversal, and process image foundations.

## Executive Summary

The current mainline decision is to stop chasing the Process Manager UI and process image productization for the moment. The foundational blocker is lower: the kernel needs a distributed traversal layer. UOFS should become the "kernel shuttle" that allows recursive kernel namespaces, service-local Privy instances, and Titan storage services to interoperate without relying on in-memory-only mounts.

The immediate priority is:

1. Fix `sparta-titan` Maven path resolution so the Titan service workspace builds.
2. Complete the UOFS protocol semantics so kernel paths and object paths do not conflict.
3. Build a UOFS SDK/client layer that can resolve through authority Privy nodes but transfer data directly from Titan services.
4. Only after that return to image pull, image registry, and process manager runtime work.

## Key Decision

UOFS has two related but distinct address semantics:

```text
uofs:///proc/...
uofs:///mnt/...
uofs:///conf/...
uofs:///sys/...
```

These are kernel namespace paths. The empty authority in `uofs:///...` means "interpret this as a Unix-like Imperium path".

```text
uofs://bucket/key
uofs://user@bucket/key
```

These are direct object namespace paths. A non-empty authority means "interpret this as object storage address".

This avoids collision between Linux-like namespace paths and object bucket paths.

## Namespace Model

`UnixInstitutionalizedMetaImperiumPrivy` is currently a Linux-like in-memory kernel namespace provider. It owns an `ExpressInstrument`, with namespaces such as:

```text
/proc
/mnt
/conf
/sys
```

Important source:

```text
/Users/wujunhong/projs/Hydra/Hydra/hydra-system-reign/src/main/java/com/pinecone/hydra/reign/UnixInstitutionalizedMetaImperiumPrivy.java
```

`EnderHydra` currently initializes `VirtualExeImageInstrument` and mounts it into the Imperium under the system image mount point.

Important source:

```text
/Users/wujunhong/projs/Hydra/Archcraft/ender-system-hydra/src/main/java/com/walnut/archcraft/ender/EnderHydra.java
```

Problem: this Privy is process-local and in-memory, so it is not suitable as the only distributed topology carrier. UOFS should provide the distributed traversal substrate.

## UOFS URI Semantics

Resolver rule:

```java
if ("uofs".equals(uri.getScheme())) {
    if (uri.getAuthority() == null || uri.getAuthority().isBlank()) {
        return KernelPathRef(uri.getPath());
    }
    return ObjectRef(uri.getAuthority(), uri.getPath());
}
```

Examples:

```text
uofs:///sys/public/global/exe/images/demo
```

Means:

```text
KernelPathRef(path="/sys/public/global/exe/images/demo")
```

Example:

```text
uofs://public/process-images/demo.jar
```

Means:

```text
ObjectRef(user=default/root by policy, bucket=public, key=process-images/demo.jar)
```

Example:

```text
uofs://root@public/process-images/demo.jar
```

Means:

```text
ObjectRef(user=root, bucket=public, key=process-images/demo.jar)
```

## /mnt Bridge

Kernel namespace can explicitly bridge into object storage through `/mnt`:

```text
uofs:///mnt/root/public/process-images/demo.jar
```

This should resolve in two stages:

```text
KernelPathRef("/mnt/root/public/process-images/demo.jar")
  -> ObjectRef(user=root, bucket=public, key=process-images/demo.jar)
```

The rule is that only `/mnt` is allowed to become object storage. `/proc`, `/conf`, `/sys` remain kernel namespace paths.

## Recursive Authority Model

Each service may have its own Privy, and the architecture is recursive. This resembles DNS:

- Local Privy can answer for local mounts.
- Parent/root/authority Privy can answer for delegated namespaces.
- The authority node is deterministic for a namespace.
- Authority should resolve names and return records, not proxy all data by default.

Default strategy:

```text
Control plane: authority Privy / resolver
Data plane: direct Titan service access
```

Authority lookup should return a resolution record:

```json
{
  "canonicalUri": "uofs://root@public/process-images/demo.jar",
  "authority": "uofs-root",
  "service": "titan",
  "endpoint": "http://127.0.0.1:9529",
  "bucket": "public",
  "key": "process-images/demo.jar",
  "accessMode": "DIRECT",
  "ttlMillis": 60000
}
```

SDK flow:

```text
uofs:///mnt/root/public/a/b.bin
  -> ask local resolver
  -> recurse to authority if needed
  -> receive Titan endpoint + bucket/key
  -> cache resolution
  -> read/write directly against Titan service
```

Proxy mode is allowed only for bootstrap, restricted networks, debug, auth mediation, and small metadata queries.

## sparta-titan Workspace Findings

Workspace:

```text
/Users/wujunhong/projs/sparta-titan
```

Parent POM:

```text
/Users/wujunhong/projs/sparta-titan/pom.xml
```

Modules:

```text
sparta-titan-arch
sparta-cdn-service
sparta-titan-console
sparta-titan-s3
sparta-titan-system
```

Current Titan S3/UOFS service pieces:

```text
/Users/wujunhong/projs/sparta-titan/sparta-titan-s3/src/main/java/com/sparta/titan/s3/service/DefaultUofsS3GatewayService.java
/Users/wujunhong/projs/sparta-titan/sparta-titan-s3/src/main/java/com/sparta/titan/s3/service/UofsS3GatewayService.java
/Users/wujunhong/projs/sparta-titan/sparta-titan-s3/src/main/java/com/sparta/titan/s3/controller/UofsS3GatewayController.java
/Users/wujunhong/projs/sparta-titan/sparta-titan-s3/src/main/java/com/sparta/titan/s3/controller/TitanS3ApiController.java
```

`DefaultUofsS3GatewayService` already has useful service-layer primitives:

- list buckets
- create bucket
- list objects
- put object
- read object
- range read
- delete object
- map S3 bucket/key to `KOMFileSystem`
- use `UFileChannel`

But `UofsS3GatewayController` is currently only a stub returning empty bucket XML and health. The HTTP S3-compatible glue is incomplete.

## Maven Issue

Error observed:

```text
Could not find artifact com.sauron.tres:tres-framework-architecture:jar:1.2.7
at specified path /Users/wujunhong/projs/shadow-prime/Tres/tres-framework-architecture/target/tres-framework-architecture-1.2.7.jar
```

Root cause:

`sparta-titan/pom.xml` defines:

```xml
<sauron.shadow.dir>/Users/wujunhong/projs/shadow-prime</sauron.shadow.dir>
```

Submodule POMs reference:

```xml
${sauron.shadow.dir}/Tres/...
```

Actual jars are under:

```text
/Users/wujunhong/projs/shadow-prime/Saurons/Tres/...
```

Confirmed existing jars:

```text
/Users/wujunhong/projs/shadow-prime/Saurons/Tres/tres-framework-architecture/target/tres-framework-architecture-1.2.7.jar
/Users/wujunhong/projs/shadow-prime/Saurons/Tres/tres-web-architecture/target/tres-web-architecture-1.2.7.jar
/Users/wujunhong/projs/shadow-prime/Saurons/Tres/tres-spring-kernel/target/tres-spring-kernel-1.2.7.jar
```

Recommended fix:

```xml
<sauron.shadow.dir>/Users/wujunhong/projs/shadow-prime/Saurons</sauron.shadow.dir>
```

This preserves the existing submodule references:

```xml
${sauron.shadow.dir}/Tres/...
```

Observed build state:

```text
mvn -q -DskipTests validate
```

completed.

```text
mvn -q -DskipTests compile
```

failed at `sparta-cdn-service` because of the incorrect Tres systemPath.

## UOFS SDK P0

The SDK should be protocol-first and service-topology-aware.

Proposed packages/classes:

```text
UofsUri
UofsObjectRef
UofsKernelPathRef
UofsResolution
UofsResolver
UofsRecursiveResolver
UofsClient
UofsHttpClient
UofsObjectMeta
UofsPutOptions
UofsRange
```

Core client API:

```java
interface UofsClient {
    UofsObjectMeta head(UofsObjectRef ref);
    InputStream get(UofsObjectRef ref);
    InputStream getRange(UofsObjectRef ref, long start, long end);
    void put(UofsObjectRef ref, InputStream input, long size, UofsPutOptions options);
    List<UofsObjectMeta> list(UofsObjectRef prefix);
    void delete(UofsObjectRef ref);
}
```

Resolver API:

```java
interface UofsResolver {
    UofsResolution resolve(URI uri);
}
```

Resolution cache:

```text
key: canonical uofs uri
value: UofsResolution
ttl: from authority record
```

Canonicalization:

```text
uofs:///mnt/root/public/a/b.bin
uofs://root@public/a/b.bin
```

Should canonicalize to:

```text
uofs://root@public/a/b.bin
```

when they point to the same object.

## Immediate Implementation Order

1. Fix `sparta-titan/pom.xml` `sauron.shadow.dir`.
2. Compile `sparta-titan` far enough to expose the next real issue.
3. Complete `UofsS3GatewayController` routes and bind them to `UofsS3GatewayService`.
4. Add SDK module, preferably `sparta-titan-sdk` or `titan-uofs-sdk`.
5. Implement URI parsing and canonicalization tests.
6. Implement `UofsHttpClient` against the S3-compatible Titan service.
7. Add authority resolution shape but keep the first implementation simple/local.
8. Later connect Hydra kernel/Imperium lookup to SDK resolution.

## Deferred Work

Do not prioritize these until UOFS protocol and SDK are stable:

- Process Manager UI
- Process image productization
- Image registry
- Remote image pull
- Process kill/restart/retry controls
- Business integration with Odin/Heist

## Key Mental Model

UOFS should become the distributed "kernel shuttle":

```text
Local kernel namespace
  -> recursive UOFS authority resolver
  -> canonical object reference
  -> direct Titan data plane
```

This preserves Linux-like kernel paths while enabling distributed object access.

The DNS analogy is intentional:

```text
Authority Privy = root/authoritative name server
Titan service = local data server
SDK cache = resolver cache
uofs:///... = kernel namespace query
uofs://bucket/key = resolved/direct object query
```


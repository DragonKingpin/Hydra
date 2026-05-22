# Context State Dump
- Type: Tombstone dump
- Scope: Odin remote process control, Husky/gRPC transport split, Hydra gRPC SDK extraction
- Focus: Current mainline architecture state, landed code changes, protocol/config decisions, next continuation path
- Recovery Intent: Restore enough context to continue Odin gRPC control implementation without re-deriving module boundaries or lifecycle topology

## Executive Summary

[Confirmed] Current mainline is:

```text
Odin remote process control
  -> RPC-independent core server
  -> transport abstraction
     -> Husky transport, current working Java/WolfMC path
     -> gRPC transport, new module skeleton and proto source
```

`gladiator` is out of scope for the current line. The active work is Java server-side architecture hygiene and gRPC transport preparation.

The immediate completed work in this checkpoint:

```text
Hydra/hydra-lib-grpc-sdk
  extracted generic gRPC appoint/client/server base from service-sdk

Hydra/hydra-lib-grpc-service-sdk
  now depends on hydra-lib-grpc-sdk
  keeps only service registry grpc domain code

Odin/odin-runtime-grpc-control
  added first gRPC remote process control module
  includes proto source and compileable transport/session skeleton
```

## Core Terminology

[Confirmed]

```text
WolfMC = lower-level Netty++ communication substrate
Ulf    = node/interface abstraction family
Husky  = duplex RPC middleware implemented on WolfMC
gRPC   = static proto based transport for non-Husky clients
```

Do not call this line Wolf control or Ulf control. The old Java duplex RPC path is `Husky control`.

## Current Module Topology

### Odin

```text
/Users/wujunhong/projs/Hydra/Odin
  odin-architecture
    remote process control abstraction

  odin-framework-runtime
    RPC-independent RemoteProcessManagerServer implementation

  odin-runtime-husky-control
    Husky transport implementation over WolvesAppointServer / DuplexAppointServer

  odin-runtime-grpc-control
    gRPC transport module, protocol source, lifecycle skeleton

  odin-framework-conduct
    Regiment / Dispatcher / Scheduler / Troll launcher

  odin-system
    system assembly
```

### Hydra gRPC

```text
/Users/wujunhong/projs/Hydra/Hydra
  hydra-lib-grpc-sdk
    generic gRPC client/server/config/process base

  hydra-lib-grpc-service-sdk
    service registry gRPC implementation only
```

Dependency intent:

```text
hydra-lib-grpc-sdk
  -> hydra-message-control
  -> pinecone
  -> grpc runtime

hydra-lib-grpc-service-sdk
  -> hydra-lib-grpc-sdk
  -> hydra-service-control

odin-runtime-grpc-control
  -> hydra-lib-grpc-sdk
  -> odin-architecture
  -> odin-framework-runtime
```

`odin-runtime-grpc-control` must not depend on `hydra-lib-grpc-service-sdk`.

## Landed Code Changes

### New Hydra Module

[Confirmed] Added:

```text
/Users/wujunhong/projs/Hydra/Hydra/hydra-lib-grpc-sdk/pom.xml
/Users/wujunhong/projs/Hydra/Hydra/hydra-lib-grpc-sdk/src/main/java/com/pinecone/hydra/grpc/client/GrpcAppointClient.java
/Users/wujunhong/projs/Hydra/Hydra/hydra-lib-grpc-sdk/src/main/java/com/pinecone/hydra/grpc/client/GrpcClientConfig.java
/Users/wujunhong/projs/Hydra/Hydra/hydra-lib-grpc-sdk/src/main/java/com/pinecone/hydra/grpc/server/GrpcAppointServer.java
/Users/wujunhong/projs/Hydra/Hydra/hydra-lib-grpc-sdk/src/main/java/com/pinecone/hydra/grpc/server/GrpcProcess.java
/Users/wujunhong/projs/Hydra/Hydra/hydra-lib-grpc-sdk/src/main/java/com/pinecone/hydra/grpc/server/GrpcServerConfig.java
```

[Confirmed] Removed old source package from:

```text
/Users/wujunhong/projs/Hydra/Hydra/hydra-lib-grpc-service-sdk/src/main/java/com/pinecone/hydra/grpc
```

Package names are intentionally preserved:

```java
com.pinecone.hydra.grpc.client
com.pinecone.hydra.grpc.server
```

This means existing service-sdk imports do not need source edits.

### Hydra POM Changes

[Confirmed] `Hydra/Hydra/pom.xml` now includes:

```xml
<module>hydra-lib-grpc-sdk</module>
<module>hydra-lib-grpc-service-sdk</module>
```

[Confirmed] `hydra-lib-grpc-service-sdk/pom.xml` now depends on:

```xml
<dependency>
  <groupId>com.pinecone.hydra.sdk.grpc</groupId>
  <artifactId>hydra-lib-grpc-sdk</artifactId>
  <version>1.2.1</version>
  <scope>compile</scope>
</dependency>
```

The old direct gRPC runtime dependencies were removed from service-sdk and moved to the generic SDK.

### New Odin Module

[Confirmed] Added:

```text
/Users/wujunhong/projs/Hydra/Odin/odin-runtime-grpc-control
  pom.xml
  proto/remote_process_lifecycle.proto
  src/main/java/com/walnut/odin/proc/server/transport/grpc/GrpcCorrelationWaiter.java
  src/main/java/com/walnut/odin/proc/server/transport/grpc/GrpcRemoteProcessControlClientile.java
  src/main/java/com/walnut/odin/proc/server/transport/grpc/GrpcRemoteProcessControlConstants.java
  src/main/java/com/walnut/odin/proc/server/transport/grpc/GrpcRemoteProcessControlEventHooker.java
  src/main/java/com/walnut/odin/proc/server/transport/grpc/GrpcRemoteProcessControlException.java
  src/main/java/com/walnut/odin/proc/server/transport/grpc/GrpcRemoteProcessControlService.java
  src/main/java/com/walnut/odin/proc/server/transport/grpc/GrpcRemoteProcessControlSession.java
  src/main/java/com/walnut/odin/proc/server/transport/grpc/GrpcRemoteProcessControlTransport.java
  src/main/java/com/walnut/odin/proc/server/transport/grpc/GrpcRemoteProcessControlTransportFactory.java
  src/main/java/com/walnut/odin/proc/server/transport/grpc/GrpcRemoteProcessFrameMapper.java
```

[Confirmed] Added module to:

```text
/Users/wujunhong/projs/Hydra/Odin/pom.xml
```

Current `odin-runtime-grpc-control` is a compileable skeleton. It does not yet bind the generated `RemoteProcessLifecycleGrpc` service because generated proto Java files are not present yet.

## Protocol Decisions

### Proto Compile Policy

[Confirmed]

```text
Do not compile proto from Maven.
Do not require global protoc / global env.
Do not hand-write generated Java classes.
Keep proto source in repo.
Generate Java manually using compiler when needed.
Commit generated Java files afterward.
```

Current proto source:

```text
/Users/wujunhong/projs/Hydra/Odin/odin-runtime-grpc-control/proto/remote_process_lifecycle.proto
```

Generated Java target package:

```java
com.walnut.odin.proc.server.transport.grpc.lifecycle
```

Generated Java should later land under:

```text
/Users/wujunhong/projs/Hydra/Odin/odin-runtime-grpc-control/src/main/java/com/walnut/odin/proc/server/transport/grpc/lifecycle
```

This `lifecycle` package follows the existing service-sdk domain split style:

```text
service-sdk/grpc/server/lifecycle
service-sdk/grpc/server/meta
service-sdk/grpc/server/cs
```

### Proto Service Name

[Confirmed]

```proto
service RemoteProcessLifecycle {
  rpc Control(stream RemoteProcessControlFrame) returns (stream RemoteProcessControlFrame);
}
```

This belongs to lifecycle semantics, not a generic process CRUD domain.

### Frame Types

[Confirmed] `CLIENT_HELLO` was rejected as not Hydra enough. Use:

```text
CLIENT_MUSTER
CLIENT_READY
```

Current first frame set:

```text
CLIENT_MUSTER
CLIENT_READY
CREATE_REMOTE_PROCESS
VITALIZE_REMOTE_PROCESS
START_REMOTE_PROCESS
QUERY_PROCESS_RUNTIME_META
HAS_OWN_PROCESS
CONTAIN_PROCESS
COMMAND_RESULT
PROCESS_CREATED
PROCESS_TERMINATED
PROCESS_RUNTIME_META
HEARTBEAT
APOPTOSIS
ERROR
```

## Lifecycle Topology

### Existing Husky Topology

[Confirmed]

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

Key split:

```text
registerController(Object controller)
  local RPC controller/handler registration

compileIface(Class<?> ifaceClass, boolean bAsIface)
  Husky-only Java-interface-as-runtime-proto compilation
```

`mountController(...)` was removed because it mixed those two atomic semantics.

### gRPC Target Topology

[Confirmed target]

```text
Transport start
  -> start GrpcAppointServer
  -> bind generated RemoteProcessLifecycle service
  -> accept bidirectional streams

Client
  -> CLIENT_MUSTER

Server
  -> bind clientId/session
  -> CLIENT_READY

Server commands
  -> CREATE_REMOTE_PROCESS
  -> VITALIZE_REMOTE_PROCESS
  -> START_REMOTE_PROCESS
  -> QUERY_PROCESS_RUNTIME_META

Client replies/events
  -> COMMAND_RESULT
  -> PROCESS_CREATED
  -> PROCESS_TERMINATED
  -> PROCESS_RUNTIME_META
  -> ERROR
```

gRPC does not support runtime iface compile. Direct calls to `compileIface(...)` on gRPC transport throw `NotImplementedException`, while server-wide code must use capability checks.

## Configuration Decisions

[Confirmed] No business config files were changed in this checkpoint.

For gRPC config keys, use lowercase only. No legacy compatibility:

```text
host
port
enable
```

Server config keys:

```text
handshakeTimeoutMillis
keepAliveTimeoutSec
keepAliveAckTimeoutSec
maximumConnections
maxInboundMessageSize
maxInboundMetadataSize
permitKeepAliveWithoutCalls
```

Client config keys:

```text
idleTimeoutMillis
keepAliveTimeoutSec
autoReconnect
enableHeartbeat
heartbeatIntervalMillis
```

[Confirmed] Fixed one spelling in `GrpcClientConfig`:

```text
heartbeatIntervalMills -> heartbeatIntervalMillis
```

No `Enable` / `HeartbeatInterval` compatibility was added.

## Current Implementation Limits

[Ready] Existing Husky line is still the functional path.

[In Progress] gRPC module compiles but is not a working control transport yet.

Current intentional placeholders:

```text
GrpcRemoteProcessControlTransport.startRemoteUProcess(...)
GrpcRemoteProcessControlTransport.createRemoteUProcess(...)
GrpcRemoteProcessControlTransport.vitalizeRemoteUProcess(...)
GrpcRemoteProcessControlTransport.queryProcessRuntimeMeta(...)
GrpcRemoteProcessFrameMapper.toFrame(...)
GrpcRemoteProcessFrameMapper.fromFrame(...)
```

These currently throw `NotImplementedException` or wrap it in Odin remote-process exceptions because generated proto classes are not present.

[Open] The service binding class must be implemented after protoc generation. Expected generated class:

```text
com.walnut.odin.proc.server.transport.grpc.lifecycle.RemoteProcessLifecycleGrpc
```

## Verification

[Confirmed] Hydra public gRPC SDK extraction compiled:

```bash
cd /Users/wujunhong/projs/Hydra/Hydra
mvn -pl hydra-lib-grpc-sdk,hydra-lib-grpc-service-sdk -am -DskipTests compile
```

Result:

```text
BUILD SUCCESS
```

[Confirmed] Cross-reactor Odin gRPC skeleton compiled:

```bash
cd /Users/wujunhong/projs/Hydra
mvn -pl :hydra-lib-grpc-sdk,:odin-runtime-grpc-control -am -DskipTests compile
```

Result:

```text
BUILD SUCCESS
```

Note: running from root requires artifact selectors, not module paths.

## Important Build Notes

[Confirmed] `hydra-lib-grpc-sdk` needed dependency on `hydra-message-control` because:

```text
GrpcAppointClient / GrpcAppointServer
  implements AppointNodus
  AppointNodus lives in hydra-message-control
```

This is accepted because `AppointNodus` is communication-node bloodline, not service-registry semantics.

[Confirmed] Pinenut bloodline is already cascaded for appoint nodes:

```text
GrpcAppointServer implements AppointNodus
AppointNodus extends Messagus
Messagus extends Nodus
Nodus extends Pinenut
```

Do not redundantly add `implements Pinenut` when the top-level interface already carries it.

## Dirty Worktree Warning

[Risk] Repository has many unrelated dirty files and generated `target/` directories. Do not clean or revert unrelated files unless explicitly requested.

Relevant files touched by this checkpoint:

```text
/Users/wujunhong/projs/Hydra/Hydra/pom.xml
/Users/wujunhong/projs/Hydra/Hydra/hydra-lib-grpc-sdk/**
/Users/wujunhong/projs/Hydra/Hydra/hydra-lib-grpc-service-sdk/pom.xml
/Users/wujunhong/projs/Hydra/Hydra/hydra-lib-grpc-service-sdk/src/main/java/com/pinecone/hydra/grpc/**
/Users/wujunhong/projs/Hydra/Odin/pom.xml
/Users/wujunhong/projs/Hydra/Odin/odin-runtime-grpc-control/**
```

The `hydra-lib-grpc-service-sdk/src/main/java/com/pinecone/hydra/grpc/**` files are intentionally deleted because they moved to `hydra-lib-grpc-sdk`.

## Next Continuation Path

### Step 1: Generate Proto Java

[Ready] Use virtual/local protoc toolchain, not Maven.

Input:

```text
/Users/wujunhong/projs/Hydra/Odin/odin-runtime-grpc-control/proto/remote_process_lifecycle.proto
```

Output target:

```text
/Users/wujunhong/projs/Hydra/Odin/odin-runtime-grpc-control/src/main/java
```

Expected generated package:

```text
com/walnut/odin/proc/server/transport/grpc/lifecycle
```

### Step 2: Bind Generated gRPC Service

[Open] After generated files exist:

```text
GrpcRemoteProcessControlService
  should extend/compose RemoteProcessLifecycleGrpc.RemoteProcessLifecycleImplBase
  should implement Control(stream RemoteProcessControlFrame)
```

`GrpcRemoteProcessControlTransport.startService()` should add the generated service to:

```text
GrpcAppointServer.serverBuilder()
```

before executing the server.

### Step 3: Implement Frame Mapper

[Open]

```text
UProcessMirrorDTO <-> UProcessMirror proto
UProcessRuntimeMeta <-> ProcessRuntimeMeta proto
RemoteVitalizationResponse <-> CommandResult / ProcessRuntimeMeta proto
GUID pid <-> ProcessId proto
```

Keep mapper isolated:

```text
GrpcRemoteProcessFrameMapper
```

Do not leak proto classes into Odin architecture interfaces.

### Step 4: Implement Command Correlation

[Open]

```text
server command frame
  -> correlation_guid
  -> GrpcCorrelationWaiter.prepare(...)
  -> session responseObserver.onNext(frame)
  -> client result frame
  -> waiter.complete(...)
```

Need timeouts and cleanup.

### Step 5: Wire Config Later

[Deferred] No config file changes yet.

Future likely shape:

```json5
"OdinProcGrpcKing": {
  "host": "0.0.0.0",
  "port": 5888,
  "enable": true,
  "handshakeTimeoutMillis": 0,
  "keepAliveTimeoutSec": 30,
  "keepAliveAckTimeoutSec": 20,
  "maximumConnections": 1000000,
  "maxInboundMessageSize": 4194304,
  "maxInboundMetadataSize": 8192,
  "permitKeepAliveWithoutCalls": true
}
```

Then Odin system assembly should hook:

```text
GrpcRemoteProcessControlTransport
```

only when configured.

## Deferred Branches

[Deferred] `gladiator-go` implementation.

[Deferred] Python gladiator; explicitly deprioritized because deployment as service is clumsy.

[Deferred] Complete LaunchSequence / full process lifecycle. Current task line stays around remote process control transport architecture and future `pipeCreate` integration.

[Deferred] Task scheduler instance generation and dependency freezing line. Separate major architecture branch.

## Recovery Pointer

Resume from:

```text
Generate remote_process_lifecycle.proto Java classes, then replace gRPC skeleton placeholders with real RemoteProcessLifecycle stream binding and frame mapper.
```

Before coding, inspect:

```text
/Users/wujunhong/projs/Hydra/Odin/odin-runtime-grpc-control
/Users/wujunhong/projs/Hydra/Odin/odin-runtime-husky-control
/Users/wujunhong/projs/Hydra/Odin/odin-architecture/src/main/java/com/walnut/odin/proc/server/transport
/Users/wujunhong/projs/Hydra/Hydra/hydra-lib-grpc-sdk
```

Then run:

```bash
cd /Users/wujunhong/projs/Hydra
mvn -pl :hydra-lib-grpc-sdk,:odin-runtime-grpc-control -am -DskipTests compile
```

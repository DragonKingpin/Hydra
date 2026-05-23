# Gladiator gRPC Control Tombstone - 2026-05-23

## Status

Gladiator first-round gRPC control smoke path is initially connected.

The work is paused intentionally. Future maintenance should resume from this tombstone instead of rediscovering the whole chain.

## Project Locations

- Go agent root: `E:\MyFiles\CodeScript\Project\Hazelnut\Sauron\shadow-prime\Knight\gladiator`
- Go implementation root: `E:\MyFiles\CodeScript\Project\Hazelnut\Sauron\shadow-prime\Knight\gladiator\gladius`
- Odin gRPC control module: `E:\MyFiles\CodeScript\Project\Hazelnut\Sauron\Saurons\Odin\odin-runtime-grpc-control`
- Odin runtime process module: `E:\MyFiles\CodeScript\Project\Hazelnut\Sauron\Saurons\Odin\odin-framework-runtime`
- Sparta smoke test: `E:\MyFiles\CodeScript\Project\Hazelnut\Sauron\Saurons\Sparta\sparta-core-console\src\test\java\com\task\TestGladiator.java`

## Current Architecture Understanding

Gladiator is the Go-side remote process control agent under Knight. Its short-term role is not the final high-privilege host controller yet. For the current phase it is a remote execution agent and distributed process terminal for Odin/Hydra.

There are two process meanings in this line:

- OS process on the host machine.
- Hydra distributed process as the management abstraction.

The first round does not bind C++ native system libraries, NT/Rtl APIs, kernel handle enumeration, or deep host intelligence. Those belong to a future high-privilege mode. The first round should stay focused on gRPC control, process command dispatch, and a minimal local execution model.

## Naming Decisions

- Public system interface: `Gladius`
- Implementation: `GladiusSystem`
- User jokingly mentioned `DragonGladius`; final serious implementation stays `GladiusSystem`.
- Go package layout preference:
  - `system.arch`
  - `system.core`
  - `proc.arch`
  - `proc.core`
- Process ID allocator naming preference:
  - `NextProcessID`
  - not `NextProcessGUID`

## Go Module Decisions

Go module:

```go
module github.com/DragonKingpin/gladiator
```

Proto `go_package` target selected:

```proto
option go_package = "github.com/DragonKingpin/gladiator/proc/iface/lifecycle;lifecycle";
```

Go must support at least Go 1.25.0. Local toolchain used during work:

```text
D:\ProgramFiles\ToolChains\go_1_26_2\bin\go.exe
```

## Config Decision

Gladius should use the outer config path:

```text
./system/setup/config.json5
```

Do not default to:

```text
./gladius/system/setup/config.json5
```

This was fixed after GoLand execution reported:

```text
open ./gladius/system/setup/config.json5: The system cannot find the path specified.
```

## Go Side Implemented Pieces

Gladius system skeleton was added under `Knight/gladiator/gladius`.

Important files:

- `gladius/cmd/gladiator/main.go`
- `gladius/system/arch/gladius.go`
- `gladius/system/core/gladius_system.go`
- `gladius/system/core/startup_command_parser.go`
- `gladius/system/core/startup_command_dispatcher.go`
- `gladius/system/core/runtime_util.go`
- `gladius/system/core/config_loader.go`
- `gladius/system/arch/config_loader.go`
- `gladius/system/arch/runtime.go`
- `gladius/system/arch/entity/startup_command.go`

The entrypoint should remain thin:

```text
StartupCommandParser -> StartupCommandDispatcher -> GladiusSystem.Vitalize -> wait signal -> Shutdown
```

The first-round command handler currently prints/logs command activity and returns success responses. Real OS process execution is intentionally not implemented yet.

## Go GUID / Process ID Work

Puma-style GUID allocator was added:

- `gladius/system/arch/guid/guid.go`
- `gladius/system/arch/guid/guid_allocator.go`
- `gladius/system/core/guid/uuid_v7_guid_allocator.go`

Gladius owns and exposes the allocator:

- `GladiusSystem.guidAllocator`
- `GladiusSystem.GuidAllocator()`

The process command handler now has:

```go
func (h *ConsoleProcessCommandHandler) NextProcessID() string
```

When Odin sends no PID during create/vitalize, Gladius generates a process ID and returns it. This fixed the Java-side failure:

```text
java.lang.IllegalArgumentException: Invalid UUID: null
```

Important note: the current `UUIDV7GuidAllocator` is UUIDv7-shaped using random bytes with version/variant bits. It does not fully encode a UUIDv7 timestamp. This was acceptable for the first smoke path, but should be revisited if strict UUIDv7 semantics become important.

The helper `nextGUID(nil)` was changed to use a fallback allocator instead of returning an empty string, so missing injection does not silently create empty frame/process IDs.

## Odin / Java gRPC Control Fixes

Odin gRPC control response mapping was fixed after the Java side hit:

```text
java.lang.NullPointerException
    at com.pinecone.framework.unit.trie.UniTrieMaptron.getStringKey(UniTrieMaptron.java:76)
```

Root cause:

- Java sent `imageAddress` in `UProcessMirrorDTO`.
- Go returned only PID/status/name in `CommandResult`.
- Java `GrpcRemoteProcessFrameMapper.toVitalizationResponse()` did not restore image context.
- `RavenRemoteProcessManagerServer.createMediatedRemoteProcess()` then used `response.getImageAddress()` and got `null`.

Files changed:

- `Odin\odin-runtime-grpc-control\src\main\java\com\walnut\odin\proc\server\transport\grpc\GrpcRemoteProcessFrameMapper.java`
- `Odin\odin-runtime-grpc-control\src\main\java\com\walnut\odin\proc\server\transport\grpc\GrpcRemoteProcessControlTransport.java`

Current mapping behavior:

- PID comes from Go response if present.
- fallback PID comes from the original `UProcessMirrorDTO`.
- image address, URI flag, startup arguments, environment variables, and local PID are copied from the original `UProcessMirrorDTO` into `RemoteVitalizationResponse`.

## Odin Defensive Error Improvement

`ArchRemoteProcessManagerNode.afterMediatedRemoteProcess(...)` now checks empty image address explicitly before querying image loaders.

File:

- `Odin\odin-framework-runtime\src\main\java\com\walnut\odin\proc\ArchRemoteProcessManagerNode.java`

This converts obscure trie NPEs into a clearer error:

```text
[MirrorCompromised] image address is required for mediated remote process.
```

## TestGladiator Smoke Test State

`TestGladiator.java` starts an Odin gRPC control server on port `5888`, waits for Gladiator client `10001`, then runs create/start/query/vitalize smoke commands.

It also intentionally waits before control operations so the user can start the Go client:

```text
[GladiatorTest] Start Knight/gladiator now, then wait for clientId 10001.
```

The earlier `No active gRPC client session: 10001` issue was from controlling a client before the Go side connected. The test was adjusted to wait for client attachment.

## Fake Image Mount

The test URI is:

```java
new URI( "uofs:///gladiator/demo/echo" )
```

This image is not part of the real system image registry. The fix was to mount a fake temporary image in the test, without changing lower-level Odin/Hydra code.

File changed:

- `Sparta\sparta-core-console\src\test\java\com\task\TestGladiator.java`

Mounted fake image:

- mount dir: `gladiator/demo`
- image name: `echo`
- URI: `uofs:///gladiator/demo/echo`

The fake image uses `LocalHostedClassImage` and an `ArchEntryPointRunnable`, following the existing pattern in `TestRemoteProcess.java`.

This fixed:

```text
java.lang.IllegalStateException: [MirrorCompromised] `uofs:///gladiator/demo/echo` is not a valid image address.
```

## Compile Verification Already Done

Odin modules:

```powershell
$env:JAVA_HOME='D:\ProgramFiles\ToolChains\Java\jdk11x64'
& 'D:\ProgramFiles\JetBrains\IntelliJ IDEA 2025.3.2\plugins\maven\lib\maven3\bin\mvn.cmd' -pl ':odin-runtime-grpc-control',':odin-framework-runtime' -am -DskipTests compile
```

Result:

```text
BUILD SUCCESS
```

Sparta smoke test compilation:

```powershell
$env:JAVA_HOME='D:\ProgramFiles\ToolChains\Java\jdk11x64'
& 'D:\ProgramFiles\JetBrains\IntelliJ IDEA 2025.3.2\plugins\maven\lib\maven3\bin\mvn.cmd' -pl ':sparta-core-console' -am -DskipTests test-compile
```

Result:

```text
BUILD SUCCESS
```

Go build:

```powershell
cd E:\MyFiles\CodeScript\Project\Hazelnut\Sauron\shadow-prime\Knight\gladiator
$env:GOPROXY='off'
$env:GOSUMDB='off'
D:\ProgramFiles\ToolChains\go_1_26_2\bin\go.exe build -o .\system\temp\gladius-check.exe ./gladius/cmd/gladiator
```

Result:

```text
BUILD SUCCESS
```

## GoLand / GOPROXY Note

GoLand was observed running:

```text
go get -v -u all
```

This is bad for this project because it tries to update the entire module graph and may hang or fail on proxy/network issues.

Recommended local dev defaults when dependencies are already cached:

```powershell
$env:GOPROXY='off'
$env:GOSUMDB='off'
```

If network fetching is required, a proxy can be configured, but avoid `go get -u all` for normal run/debug.

## Next Suggested Work When Resuming

Do not continue this immediately unless explicitly resumed.

Recommended next phase:

1. Stabilize `RemoteProcessLifecycle` protocol semantics.
2. Add Go-side `ProcessRegistry`.
3. Add `ProcessHandle` state model:
   - `Created`
   - `Starting`
   - `Running`
   - `Exited`
   - `Failed`
   - `Killed`
4. Make `create` register only.
5. Make `start` execute a command.
6. Make `query` return registry runtime metadata.
7. Add stdout/stderr tail buffer later.
8. Add a minimal safety policy before exposing this beyond tests.

First execution implementation should be command-line based only. Do not introduce C++/native host intelligence in the next small step.

## Important Philosophy

Gladiator has a future high-privilege host-control role, but short-term work should remain disciplined:

- first make gRPC reliable;
- then make process registry reliable;
- then add command execution;
- then add governance and security controls;
- only later consider native system probes or high-privilege host inspection.

This keeps the project from turning into an uncontrolled privileged toy.

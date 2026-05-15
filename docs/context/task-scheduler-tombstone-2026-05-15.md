# Task Scheduler Tombstone - 2026-05-15

This tombstone preserves the current scheduler mainline context so a later session can quickly recover the business state without replaying the full conversation.

## Current Business Mainline

The current goal is not yet the full task execution lifecycle. The active mainline is the scheduler creation and prelaunch chain:

```text
task due
 -> create or reuse task instance
 -> freeze instance DAG lineage
 -> dependency / resource / queue checks
 -> claim DepartureStandby -> ProcessCreating
 -> pipeCreatePrepared
 -> create process
 -> ProcessStandby
```

`pipeLaunch`, `process.start()`, and the complete `Running -> Finished/Error` lifecycle are still the next business stage, not the current completed scope.

## Scheduler Entry Points

Core scheduler:

```text
Odin/odin-framework-conduct/src/main/java/com/walnut/odin/conduct/schedule/RavenTaskScheduler.java
```

Current pulse shape:

```java
public void pulseSchedule( LocalDateTime pulseTime ) {
    this.mTaskSchedulerReconciler.reconcileLightweight( pulseTime );
    this.mTaskSchedulePreparator.prepareHourlySchedulableTasksAndWait( pulseTime );
    this.mTaskSchedulePreparator.prepareFastSchedulableTasksAndWait( pulseTime );
    this.mInstanceScheduleImpetus.impelPrelaunchInstances( pulseTime );
}

public void pulseScheduleDaily( LocalDateTime pulseTime ) {
    this.mTaskSchedulePreparator.prepareDailySchedulableTasksAndWait( pulseTime );
}
```

`fetch()` was a temporary test entry and has been superseded by `pulseSchedule`.

Current cycle grouping:

```text
DailyTaskScheduleCycles  = Month / Week / Day
HourlyTaskScheduleCycles = Hour
FastTaskScheduleCycles   = Minute
```

Minute-level tasks are handled by the fast scan path with a look-ahead window and bounded per-task creation to avoid flooding stale minute instances.

## Instance Creation Semantics

Core preparator:

```text
Odin/odin-framework-conduct/src/main/java/com/walnut/odin/conduct/schedule/RavenTaskSchedulePreparator.java
```

Important time semantics:

```text
expect_time   = cron-derived expected fire time
business_time = business bucket resolved from expect_time and schedule cycle
fire_time     = scheduler pulse / generation batch time
schedule_time = time when scheduler claims the instance for process creation
start_time    = process runtime start time
finish_time   = process termination time
```

Example:

```text
business_time = 2026-05-15 20:00:00
expect_time   = 2026-05-15 20:20:00
```

This means `expect_time` is not the same as `business_time`. The current idempotency anchor for scheduler-created instances is:

```text
task_guid + business_time
```

The code also has query exits for `task_guid + expect_time`, but the current scheduler reuse path uses business time because one business batch should produce one instance.

`TaskScheduleTimeResolver` owns the business-time bucketing:

```text
Odin/odin-framework-conduct/src/main/java/com/walnut/odin/conduct/schedule/TaskScheduleTimeResolver.java
```

Current bucketing:

```text
Month / Week / Day -> day start
Hour               -> hour start
Minute             -> minute start
Undefined          -> expect_time
```

## Frozen Instance DAG Lineage

Lineage freezer:

```text
Odin/odin-framework-conduct/src/main/java/com/walnut/odin/conduct/schedule/lineage/RavenTaskInstanceLineageFreezer.java
```

Core model:

```text
live task graph:
  hydra_atlas_vgraph_adjacent
  guid        = child task graph node guid
  parent_guid = parent task graph node guid

frozen instance graph:
  odin_task_instance_atlas_nodes
  odin_taks_ins_atlas_adjacent
  guid        = child instance atlas node guid
  parent_guid = parent instance atlas node guid
```

The dependency check must use the frozen instance graph, not the live task graph.

Current freezing path is two-phase:

```text
1. create/reuse all instances and prepare lineage frames
2. persist instance atlas nodes
3. resolve parent instance atlas nodes
4. persist instance atlas edges
5. ensure exec/event records
6. persist task next_schedule_time
```

Parent resolution order:

```text
1. parent in current batch -> in-memory lineage frame
2. parent not in current batch -> existing parent instance atlas node by parent task + business_time
3. cannot resolve -> fail instead of silently marking child source
```

Dependency blockage SQL is connected through:

```text
child instance
 -> child instance atlas node
 -> frozen instance edge
 -> parent instance atlas node
 -> parent hydra_task_instances
```

Parent is considered ready only when parent `run_status = Finished`.

## Exec And Event Tables

Relevant tables:

```text
hydra_task_instances
odin_task_instance_exec
odin_task_instance_event
```

Business meaning:

```text
hydra_task_instances:
  one task can have many instances, but one task + business_time should represent one scheduler-created business instance.

odin_task_instance_exec:
  one instance can have many exec rows for retry / rerun / backfill execution attempts.

odin_task_instance_event:
  append-style event table for instance state and scheduling events.
```

Current preparator ensures initial exec/event rows idempotently for reused instances.

`odin_task_instance_exec` has been extended with `image_path` in the database so process image information can be recorded on exec as well as instance.

## Impetus / Prelaunch State

Core impetus:

```text
Odin/odin-framework-conduct/src/main/java/com/walnut/odin/conduct/schedule/RavenInstanceScheduleImpetus.java
```

Current prelaunch flow:

```text
New / DependencyWait / ResourceWait / DepartureStandby
 -> dependency check
 -> resource fitting
 -> DepartureStandby
 -> atomic claim DepartureStandby -> ProcessCreating with schedule_time
 -> pipeCreatePrepared
```

`ProcessCreating` was added as the claimed-but-not-yet-created middle state.

This prevents repeated scheduler pulses from creating multiple processes for the same instance.

## Process Creation Boundary

Launcher:

```text
Odin/odin-framework-runtime/src/main/java/com/walnut/odin/task/troll/TrollTaskExecutionLauncher.java
```

The scheduler path now uses prepared creation:

```text
pipeCreatePrepared
 -> create prepared process
 -> afterProcessCreated
 -> ProcessCreating/New/DepartureStandby -> ProcessStandby
```

Creation failure currently marks:

```text
ProcessCreating/New/DepartureStandby -> Error
exec -> Fail
```

Important: `launchLocally` / `launchRemotely` still perform actual process start and use direct instance updates in:

```text
afterOwnedProcessStarted
afterOwnedProcessTerminated
```

Those paths are not yet fully pulled into lifecycle CAS.

## Current Functional Status

Already functionally in place:

```text
pulseSchedule
 -> hourly / fast task scanning
 -> instance create or reuse
 -> expect_time / business_time separation
 -> frozen instance DAG lineage
 -> initial exec/event idempotency
 -> dependency blockage check on frozen instance graph
 -> resource fitting
 -> atomic ProcessCreating claim
 -> prepared process creation
 -> ProcessStandby
```

Last known compile validation:

```powershell
$env:JAVA_HOME='D:\ProgramFiles\ToolChains\Java\jdk11x64'
$env:PATH="$env:JAVA_HOME\bin;D:\ProgramFiles\JetBrains\IntelliJ IDEA 2025.3.2\plugins\maven\lib\maven3\bin;$env:PATH"
mvn.cmd -pl Odin/odin-framework-conduct -am -DskipTests compile
```

Result at that time:

```text
BUILD SUCCESS
```

## Main Functional Gaps

The most important remaining gap is `reconcileLightweight`.

Current file:

```text
Odin/odin-framework-conduct/src/main/java/com/walnut/odin/conduct/recovery/KernelTaskSchedulerReconciler.java
```

It is currently an empty implementation. It should recover or repair short-lived scheduler states left by an interrupted pulse, especially stale `ProcessCreating`.

Priority recovery target:

```text
ProcessCreating stuck because scheduler claimed the instance but crashed before process creation completed.
```

Possible policy:

```text
if ProcessCreating is stale and no process binding / no confirmed created process:
  ProcessCreating -> DepartureStandby
  exec -> ready/submitted rollback or failure marker depending on exact evidence

if ProcessCreating is stale but process is confirmed created:
  ProcessCreating -> ProcessStandby
```

The second functional gap is the runtime lifecycle after `ProcessStandby`:

```text
ProcessStandby -> Running -> Finished/Error
```

This needs lifecycle CAS and exec/event updates rather than direct `instance.update()`.

The third functional gap is upstream failure semantics:

```text
parent Error / Killed / AuditFailed
 -> child should not wait forever
```

A business state needs to be chosen later, such as `UpstreamFailed`, `Skipped`, or manual-intervention wait.

The fourth functional gap is retry / rerun / backfill:

```text
one instance
 -> multiple exec rows
 -> retry_cnt/current_retry_number/run_count/sequence_cnt semantics
```

The tables support this, but the scheduler mainline does not yet fully drive it.

## Recommended Next Step

Next construction should start with `reconcileLightweight`, because it protects the already-landed scheduler creation mainline.

Concrete first slice:

```text
1. Add mapper query for stale ProcessCreating instances by update_time / schedule_time threshold.
2. Add lifecycle transition for ProcessCreating -> DepartureStandby or Error.
3. Update exec state consistently when recovering.
4. Keep it small and deterministic.
5. Compile odin-framework-conduct with Maven.
```

After that, return to the mainline and decide whether the next business milestone is:

```text
A. stop at ProcessStandby as prepared process pool
B. continue scheduler pulse into ProcessStandby -> Running
```

## Important Local Conventions

Project conventions to preserve:

```text
Top-level interfaces/classes should implement or extend Pinenut.
Anonymous and temporary objects do not need Pinenut.
Interfaces belong in *-architecture / arch modules.
Implementations belong in framework modules.
Avoid kernel naming with Service.
Comments should be in English.
Windows text files should be read/written as UTF-8.
Manual edits should use apply_patch.
```

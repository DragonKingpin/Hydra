# Task Scheduler Source / Lineage Checkpoint

Date: 2026-05-15

This document is a tombstone / checkpoint for the Odin task scheduler source-node normalization and instance-lineage freezing work.

The purpose is to preserve enough context for migration, recovery, and future continuation without relying on chat history.

## 1. Current Thread Position

We are pushing the Hydra/Odin task scheduling architecture forward. The near-term goal is not the full launch lifecycle. The current line is:

```text
task schedule due
 -> create/reuse task instance
 -> freeze instance DAG lineage
 -> later instance impetus checks dependency/resource/queue
 -> pipeCreate
 -> ProcessStandby
```

Explicitly not doing yet:

- full LaunchSequence completion
- `pipeLaunch`
- `process.start()`
- full Running / Finished lifecycle
- Tick subsystem

The most recent completed subline is the source-node normalization:

```text
is_isolated -> is_source
```

Terminology is now:

- `source`: strict graph source point, in-degree is 0.
- Top-level task graph source is a cache field backed by edge table truth.
- Instance atlas source is a frozen snapshot field.

## 2. Important Entry Points

Test / run entry:

```text
/Users/wujunhong/projs/Hydra/Sparta/sparta-core-console/src/test/java/com/sparta/TestRuntime.java
```

Main scheduler:

```text
/Users/wujunhong/projs/Hydra/Odin/odin-framework-conduct/src/main/java/com/walnut/odin/conduct/schedule/RavenTaskScheduler.java
```

Current working scheduler package:

```text
/Users/wujunhong/projs/Hydra/Odin/odin-framework-conduct
```

Coding standards:

```text
/Users/wujunhong/projs/Hydra/docs/standard/coding_standard.md
/Users/wujunhong/projs/Hydra/docs/standard/mysql_table_standard.md
```

Key style notes:

- Use `this.` for member access and method calls.
- Use spaces around non-generic parentheses, e.g. `if ( condition )`.
- Keep lines under 140 chars where possible.
- For Java/C++ member names, local style prefers Hungarian-ish fields such as `mX`, `mszX`, `mnX`, `mbX`, though nearby existing code is mixed.
- MySQL: no `SELECT *`, no single-letter aliases, comments on fields for DDL, uppercase SQL keywords preferred.

## 3. Database State After User Changes

The user manually changed the DB shape.

### 3.1 Top-Level Task Graph Nodes

Table:

```sql
CREATE TABLE `hydra_atlas_vgraph_nodes` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `guid` varchar(72) DEFAULT NULL,
  `task_guid` varchar(72) DEFAULT NULL,
  `node_name` varchar(330) DEFAULT NULL,
  `node_description` text,
  `is_source` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否为源点缓存: 0=否, 1=是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_guid` (`guid`) USING BTREE,
  KEY `idx_node_name` (`node_name`) USING BTREE,
  KEY `idx_task_guid` (`task_guid`) USING BTREE,
  KEY `idx_update` (`update_time`) USING BTREE,
  KEY `idx_source_update` (`is_source`,`update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

The field may have been deleted and re-added during discussion. Current intended DB state includes `is_source`.

Semantics:

```text
hydra_atlas_vgraph_nodes.is_source = live task graph source cache
```

Authority:

```text
hydra_atlas_vgraph_adjacent is the authoritative edge table.
is_source is a cache / index field.
```

Maintenance rule:

```text
add edge parent -> child:
    insert hydra_atlas_vgraph_adjacent(guid = child, parent_guid = parent)
    update hydra_atlas_vgraph_nodes set is_source = 0 where guid = child

remove edge:
    delete edge
    if no remaining parent edge for child, set is_source = 1
```

Current implementation only handles add-child maintenance. Remove-edge/source re-affirmation support exists as mapper method but is not wired to a concrete remove-edge path yet.

### 3.2 Instance Atlas Nodes

Table:

```sql
CREATE TABLE `odin_task_instance_atlas_nodes` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `guid` varchar(72) DEFAULT NULL,
  `instance_guid` varchar(72) DEFAULT NULL,
  `node_name` varchar(330) DEFAULT NULL,
  `is_source` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否为实例源点快照: 0=否, 1=是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_guid` (`guid`) USING BTREE,
  KEY `idx_node_name` (`node_name`) USING BTREE,
  KEY `idx_instance_guid` (`instance_guid`) USING BTREE,
  KEY `idx_instance_atlas_guid_instance` (`guid`,`instance_guid`),
  KEY `idx_source_update` (`is_source`,`update_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

Semantics:

```text
odin_task_instance_atlas_nodes.is_source = frozen instance graph source snapshot
```

This should be calculated during instance lineage freezing from the frozen parent instance node edges, not from the live graph field alone.

### 3.3 Instance Edge Table Semantics

Table:

```text
odin_taks_ins_atlas_adjacent
```

Important spelling note: table name is currently `odin_taks_ins_atlas_adjacent`, with `taks`, not `task`.

Correct semantics after this checkpoint:

```text
odin_taks_ins_atlas_adjacent.guid        = child_instance_node_guid
odin_taks_ins_atlas_adjacent.parent_guid = parent_instance_node_guid
```

Old broken behavior in code before this change:

```text
guid        = random new guid
parent_guid = parent task graph node guid
```

That old behavior was wrong because dependency SQL expects instance graph edges.

## 4. Scheduling Design Decisions Already Aligned

### 4.1 Scheduling Channels

The task preparation scheduler will eventually split schedule cycles into at least two formal channels:

```text
Hourly channel:
    TaskScheduleCycle.Month
    TaskScheduleCycle.Week
    TaskScheduleCycle.Day
    TaskScheduleCycle.Hour

Minute channel:
    TaskScheduleCycle.Minute
```

Do not include Tick cycles in this class/pipeline. Tick belongs to a later dedicated subsystem.

Previously discussed names:

```text
HourlyTaskScheduleCycles
MinuteTaskScheduleCycles
prepareHourlySchedulableTasks(...)
prepareMinuteSchedulableTasks(...)
```

Do not keep old `Daily` compatibility APIs when actually doing that cleanup. The user explicitly said no legacy/compatibility names for this still-developing task system.

### 4.2 Time Semantics

Important time-field meanings:

```text
expectTime:
    expected / desired schedule fire time. First scheduling eligibility time.

businessTime:
    business/data partition time. Not necessarily exact run/fire time.

fireTime:
    instance creation/batch generation time.

scheduleTime:
    actual scheduler dispatch time. Should be set when Impetus actually schedules/pipeCreates the instance.

thisScheduleTime:
    current schedule cursor used to compute/advance nextScheduleTime.
    User asked not to rename or obsess over it for now.

nextScheduleTime:
    next task-level schedule cursor persisted to task table.
```

Do not set `scheduleTime = expectTime`.

Do not use `businessTime` as the only schedule-fire identity. Better idempotency identity should be based on task + expect time / fire cursor semantics.

### 4.3 Launch Boundary

Current near-term boundary:

```text
pipeCreate
```

Not:

```text
pipeLaunch
```

Existing process event reference:

```text
/Users/wujunhong/projs/Hydra/Hydra/hydra-framework-runtime/src/main/java/com/pinecone/hydra/proc/event/ProcessEvent.java
```

Relevant events:

```text
Prepare
Created
Vitalized
Terminated
Error
```

The current target is to reach `Created` / `ProcessStandby` semantics.

## 5. Code Changes Completed In This Checkpoint

### 5.1 Hydra Base Interface

Changed:

```text
/Users/wujunhong/projs/Hydra/Hydra/hydra-architecture/src/main/java/com/pinecone/hydra/unit/vgraph/source/VectorGraphManipulator.java
```

Old APIs removed/renamed:

```text
fetchIsolatedNodes(...)
fetchIsolatedNodesById(...)
countIsolatedNodes()
selectIsolatedNodeIndexMeta()
```

New APIs:

```text
fetchSourceNodes(...)
fetchSourceNodesById(...)
selectSourceNodeIndexMeta()
```

`countSourceNodes()` already existed and is retained.

### 5.2 Odin Runtime Atlas API

Changed:

```text
/Users/wujunhong/projs/Hydra/Odin/odin-architecture/src/main/java/com/walnut/odin/atlas/graph/RuntimeAtlasInstrument.java
```

Old APIs renamed:

```text
fetchIsolatedNodesAll()    -> fetchSourceNodesAll()
fetchIsolatedNodes(...)    -> fetchSourceNodes(...)
fetchIsolatedNodesById(...) -> fetchSourceNodesById(...)
getIsolatedNodeIndexMeta() -> getSourceNodeIndexMeta()
queryMaxIsolatedNodePage(...) -> queryMaxSourceNodePage(...)
```

### 5.3 Task Atlas Node Entity

Changed:

```text
/Users/wujunhong/projs/Hydra/Odin/odin-framework-atlas/src/main/java/com/walnut/odin/atlas/graph/entity/TaskAtlasNode.java
```

Field/API renamed:

```text
isolated -> source
isIsolated() -> isSource()
setIsolated(...) -> setSource(...)
```

### 5.4 RuntimeVGraphMapper Java

Changed:

```text
/Users/wujunhong/projs/Hydra/Odin/odin-framework-atlas/src/main/java/com/walnut/odin/atlas/mapper/RuntimeVGraphMapper.java
```

Important changes:

- Added `markNonSource( GUID guid )`.
- Added `affirmSourceIfNoParent( GUID guid )`.
- `insertNodeByEdge(...)` now inserts edge then marks child non-source.
- `addChild( GUID parentGuid, GraphNode graphNode )` now inserts edge then marks child non-source.
- `addChild( GUID parentGuid, GUID childGuid )` changed from abstract mapper method to default Java method, and now inserts edge then marks child non-source.
- Priority/source count queries now use `is_source = 1`.
- Source paging API names now use `fetchSource*`.

Open note:

```text
affirmSourceIfNoParent(...) exists but is not yet wired to remove-edge flows.
```

### 5.5 RuntimeVGraphMapper XML

Changed:

```text
/Users/wujunhong/projs/Hydra/Odin/odin-framework-atlas/src/main/resources/mapper/kernel/task/RuntimeVGraphMapper.xml
```

Important changes:

- Result map now maps `is_source` to `source`.
- All selected `is_isolated` fields changed to `is_source`.
- Source/root/handle queries use `WHERE is_source = 1`.
- Old isolated queries renamed:

```text
fetchIsolatedNodesById0 -> fetchSourceNodesById0
fetchIsolatedNodes0     -> fetchSourceNodes0
selectIsolatedNodeIndexMeta -> selectSourceNodeIndexMeta
```

- Removed XML `<insert id="addChild">` because Java default method now uses `insertNodeAdjacent(...)` and `markNonSource(...)`.
- Added `markNonSource`.
- Added `affirmSourceIfNoParent`.

Potential follow-up:

```text
RuntimeVGraphMapper.xml queryInDegree/queryOutDegree names appear semantically inverted by conventional graph theory:
    queryInDegree currently counts WHERE parent_guid = nodeGuid
    queryOutDegree currently counts WHERE guid = nodeGuid

This was not changed in this checkpoint to avoid unrelated behavior drift.
```

### 5.6 UniformRuntimeAtlas

Changed:

```text
/Users/wujunhong/projs/Hydra/Odin/odin-framework-atlas/src/main/java/com/walnut/odin/atlas/graph/UniformRuntimeAtlas.java
```

Important changes:

- Old isolated methods renamed to source methods.
- `queryMaxSourceNodePage(...)` now uses `countSourceNodes()`.
- Added null guards in:

```text
queryGraphNodeByTaskGuid(...)
queryTaskElementByGuid(...)
```

The null guards are important because instance lineage resolution may query parent task graph nodes and should not explode if the graph/task mapping is absent.

### 5.7 Instance Atlas Node Entity/API

Changed:

```text
/Users/wujunhong/projs/Hydra/Odin/odin-architecture/src/main/java/com/walnut/odin/conduct/entity/InstanceAtlasNode.java
/Users/wujunhong/projs/Hydra/Odin/odin-framework-conduct/src/main/java/com/walnut/odin/conduct/entity/GenericInstanceAtlasNode.java
```

Old:

```text
isIsolated()
setIsIsolated(...)
field isIsolated
```

New:

```text
isSource()
setSource(...)
field source
```

### 5.8 InstanceAtlasNodeMapper Java/XML

Changed:

```text
/Users/wujunhong/projs/Hydra/Odin/odin-architecture/src/main/java/com/walnut/odin/task/mapper/InstanceAtlasNodeMapper.java
/Users/wujunhong/projs/Hydra/Odin/odin-framework-atlas/src/main/resources/mapper/kernel/task/InstanceAtlasNodeMapper.xml
```

Important changes:

- Insert now writes `is_source`.
- Result map maps `is_source` to `source`.
- Added:

```java
InstanceAtlasNode queryByInstanceGuid( GUID instanceGuid );

InstanceAtlasNode queryByTaskGuidAndExpectTime( GUID taskGuid, LocalDateTime expectTime );
```

- Dependency blockage SQL corrected from:

```text
child_node -> instance_edge -> parent_instance directly
```

to:

```text
child_node
 -> instance_edge
 -> parent_node
 -> parent_instance
```

Correct SQL chain:

```sql
child_node.`guid` = instance_edge.`guid`
instance_edge.`parent_guid` = parent_node.`guid`
parent_node.`instance_guid` = parent_instance.`guid`
```

### 5.9 RavenTaskSchedulePreparator Instance Lineage

Changed:

```text
/Users/wujunhong/projs/Hydra/Odin/odin-framework-conduct/src/main/java/com/walnut/odin/conduct/schedule/RavenTaskSchedulePreparator.java
```

Old behavior:

```text
prepareInstanceLineage(...)
    create instance atlas node
    set isIsolated based on live parent graph ids
    insert node
    for each parent graph id:
        insert adjacent(guid = random, parent_guid = parent graph node id)
```

New behavior:

```text
prepareTaskInstances(...)
    create all instances first
    build ScheduledTaskInstanceLineage frames
    prepareInstanceLineages(lineages)
    persist exec/event after lineage is persisted
```

New helper class:

```text
ScheduledTaskInstanceLineage
```

New helper methods:

```text
prepareInstanceLineageFrame(...)
resolveParentInstanceAtlasNode(...)
prepareInstanceLineages(...)
persistInstanceAtlasNodes(...)
persistInstanceAtlasAdjacents(Collection<...>)
persistInstanceAtlasAdjacents(ScheduledTaskInstanceLineage)
```

Important two-phase persistence:

```text
persistInstanceAtlasNodes(lineages)
persistInstanceAtlasAdjacents(lineages)
```

Reason:

```text
All instance atlas nodes must be present before edges are inserted.
Edges reference child/parent instance atlas node guids.
```

This was intentionally split into functions for readability.

Current lineage edge semantics:

```text
adjacent.guid        = child instance atlas node guid
adjacent.parentGuid  = parent instance atlas node guid
```

Current source snapshot semantics:

```text
lineage.instanceAtlasNode.source = lineage.adjacents.isEmpty()
```

Parent resolution order:

```text
1. If parent graph node is in current in-memory batch, use its ScheduledTaskInstanceLineage instance atlas node.
2. Else resolve graph node -> task element -> query existing parent instance atlas node by task_guid + expect_time.
```

Current failure behavior:

```text
If a parent graph node exists but parent instance atlas node cannot be resolved, throw IllegalStateException.
```

This prevents falsely marking a dependency child as source when parent lineage is missing.

## 6. Verification Completed

### 6.1 Residual Old-Name Search

Ran:

```bash
rg -n "fetchIsolated|countIsolated|selectIsolated|getIsolated|queryMaxIsolated|is_isolated|isIsolated|setIsIsolated|isolated" \
  /Users/wujunhong/projs/Hydra/Odin \
  /Users/wujunhong/projs/Hydra/Hydra \
  -g '!**/target/**'
```

Result:

```text
No source results.
```

### 6.2 Compile

Because Hydra base interface changed, first installed `hydra-architecture`:

```bash
cd /Users/wujunhong/projs/Hydra/Hydra
mvn -pl hydra-architecture -DskipTests install
```

Result:

```text
BUILD SUCCESS
```

Then compiled Odin relevant modules:

```bash
cd /Users/wujunhong/projs/Hydra/Odin
mvn -pl odin-architecture,odin-framework-atlas,odin-framework-conduct -am -DskipTests compile
```

Result:

```text
BUILD SUCCESS
```

After function extraction, compiled again:

```bash
cd /Users/wujunhong/projs/Hydra/Odin
mvn -pl odin-framework-conduct -am -DskipTests compile
```

Result:

```text
BUILD SUCCESS
```

## 7. Known Worktree Context / Dirty State Warning

There are many unrelated dirty files in the broader `/Users/wujunhong/projs/Hydra` worktree, including `.idea`, `.iml`, `target/`, Sparta files, system config, and other projects.

Do not revert unrelated files.

Files intentionally changed by this checkpoint:

```text
/Users/wujunhong/projs/Hydra/Hydra/hydra-architecture/src/main/java/com/pinecone/hydra/unit/vgraph/source/VectorGraphManipulator.java

/Users/wujunhong/projs/Hydra/Odin/odin-architecture/src/main/java/com/walnut/odin/atlas/graph/RuntimeAtlasInstrument.java
/Users/wujunhong/projs/Hydra/Odin/odin-architecture/src/main/java/com/walnut/odin/conduct/entity/InstanceAtlasNode.java
/Users/wujunhong/projs/Hydra/Odin/odin-architecture/src/main/java/com/walnut/odin/task/mapper/InstanceAtlasNodeMapper.java

/Users/wujunhong/projs/Hydra/Odin/odin-framework-atlas/src/main/java/com/walnut/odin/atlas/graph/UniformRuntimeAtlas.java
/Users/wujunhong/projs/Hydra/Odin/odin-framework-atlas/src/main/java/com/walnut/odin/atlas/graph/entity/TaskAtlasNode.java
/Users/wujunhong/projs/Hydra/Odin/odin-framework-atlas/src/main/java/com/walnut/odin/atlas/mapper/RuntimeVGraphMapper.java
/Users/wujunhong/projs/Hydra/Odin/odin-framework-atlas/src/main/resources/mapper/kernel/task/InstanceAtlasNodeMapper.xml
/Users/wujunhong/projs/Hydra/Odin/odin-framework-atlas/src/main/resources/mapper/kernel/task/RuntimeVGraphMapper.xml

/Users/wujunhong/projs/Hydra/Odin/odin-framework-conduct/src/main/java/com/walnut/odin/conduct/entity/GenericInstanceAtlasNode.java
/Users/wujunhong/projs/Hydra/Odin/odin-framework-conduct/src/main/java/com/walnut/odin/conduct/schedule/RavenTaskSchedulePreparator.java

/Users/wujunhong/projs/Hydra/docs/todo/task-scheduler-source-lineage-checkpoint-2026-05-15.md
```

## 8. Open Risks / Follow-Up Items

### 8.1 Source Cache Reaffirmation On Edge Removal

Added:

```text
RuntimeVGraphMapper.affirmSourceIfNoParent(...)
```

But not yet wired to a remove-edge workflow.

If future code removes a parent edge, source cache must be restored if no remaining parent edge exists.

Recommended future API:

```java
void removeChild( GUID parentGuid, GUID childGuid );
```

Semantics:

```text
delete edge where guid = childGuid and parent_guid = parentGuid
affirmSourceIfNoParent(childGuid)
```

### 8.2 Top-Level Source Backfill

If DB data already existed before source-cache maintenance was fixed, run a one-time backfill:

```sql
UPDATE `hydra_atlas_vgraph_nodes` AS node
LEFT JOIN (
    SELECT DISTINCT `guid`
    FROM `hydra_atlas_vgraph_adjacent`
) AS parented
ON parented.`guid` = node.`guid`
SET node.`is_source` = IF( parented.`guid` IS NULL, 1, 0 );
```

### 8.3 Instance Lineage Idempotency Is Not Done

Current `prepareTaskInstances(...)` always creates and inserts a new instance via:

```text
this.mTaskExecutionLauncher.initializeInstance(...)
```

There is not yet idempotent "find existing instance by task_guid + expect_time" protection.

This is still needed for robust scheduler preparation.

Likely future work:

- Add instance query by `task_guid + expect_time`.
- Ensure scheduler preparation does not duplicate instances for same schedule fire.
- Add unique key if architecture agrees:

```text
hydra_task_instances(task_guid, expect_time)
```

or a more complete identity if manual/cycle split requires it.

### 8.4 Parent Instance Resolution Uses expectTime

Current parent instance atlas resolution uses:

```text
parent task guid + context.thisScheduleTime / expectTime
```

This is the best current approximation for same-round DAG instance grouping.

Potential future improvement:

```text
instance batch identity
```

Possible schema-level identity:

```text
fire_time / schedule_fire_guid / batch_guid
```

No new DB field was added in this checkpoint.

### 8.5 Transaction Boundary Not Yet Explicit

Instance creation, lineage node insert, edge insert, exec insert, and event insert are not currently wrapped in an explicit local transaction in `RavenTaskSchedulePreparator`.

Failure midway may leave partial state.

Future atom:

```text
prepare one batch in a transaction:
    insert instances
    insert instance atlas nodes
    insert instance atlas edges
    insert exec rows
    insert TaskTimeReady events
```

Need inspect project transaction framework before implementing.

### 8.6 `LaunchFeature.bizTimeEpoch` Is Still Suspicious

Known pre-existing issue:

```text
RavenTaskSchedulePreparator.prepareInstance(...)
    LaunchFeature feature = new LaunchFeature();
    it.setExpectTime(context.getThisScheduleTime());
    this.mTaskExecutionLauncher.initializeInstance(that, feature);
```

`TrollTaskExecutionLauncher.initializeInstance(...)` computes businessTime from `feature.getBizTimeEpoch()`.

`LaunchFeature` default `bizTimeEpoch` may be `now.minusDays(1)`, which is likely wrong for scheduler-created instances.

Future fix:

```text
feature.setBizTimeEpoch(context.getThisScheduleTime())
```

or whichever method name exists on `LaunchFeature`.

### 8.7 `createLocally/createRemotely` May Duplicate Instance Insert

Known design issue:

```text
TrollTaskExecutionLauncher.createLocally/createRemotely currently call initializeInstance(...)
```

If scheduler already created an instance and later Impetus calls create/pipeCreate, this may duplicate insertion unless a "prepared instance" flag or alternative path exists.

Previously discussed future fix:

```text
LaunchFeature.instancePrepared / preparedInstance
```

Then `createLocally/createRemotely` can skip `initializeInstance(...)`.

### 8.8 Impetus Boundary Still Needs pipeCreate

Known current goal:

```text
RavenInstanceScheduleImpetus should stop at pipeCreate, not pipeLaunch.
```

Need inspect/update:

```text
/Users/wujunhong/projs/Hydra/Odin/odin-framework-conduct/src/main/java/com/walnut/odin/conduct/schedule/RavenInstanceScheduleImpetus.java
```

Also set:

```text
schedule_time = actual dispatch time
```

when Impetus really schedules/pipeCreates.

### 8.9 Schedule Cycle Channel Split Not Done

Current code likely still has:

```text
DailyTaskScheduleCycles = Month, Week, Day, Hour
prepareSchedulableTasksDaily(...)
fetchSchedulableTasksDaily(...)
```

Future cleanup:

```text
HourlyTaskScheduleCycles = Month, Week, Day, Hour
MinuteTaskScheduleCycles = Minute

prepareHourlySchedulableTasks(...)
prepareMinuteSchedulableTasks(...)
fetchHourlySchedulableTasks(...)
fetchMinuteSchedulableTasks(...)
```

No legacy aliases, per user instruction.

Tick is not included.

### 8.10 Potential MyBatis SQL Standards Cleanup

Some existing mapper SQL uses aliases like `havn`, `vatm`, `httn`. These are not single-letter aliases, so acceptable.

Need be careful not to introduce single-letter aliases in new SQL.

### 8.11 QueryInDegree / QueryOutDegree Naming Suspicion

In `RuntimeVGraphMapper.xml`:

```sql
queryInDegree:
    WHERE parent_guid = #{nodeGuid}

queryOutDegree:
    WHERE guid = #{nodeGuid}
```

Given edge table semantics:

```text
guid = child
parent_guid = parent
```

This appears inverted under standard graph terminology.

Not touched in this checkpoint because it may have existing caller expectations.

## 9. Recommended Next Construction Topology

### Atom 1: Verify DB Source Field Exists Everywhere

Before runtime test:

```sql
SHOW COLUMNS FROM `hydra_atlas_vgraph_nodes` LIKE 'is_source';
SHOW COLUMNS FROM `odin_task_instance_atlas_nodes` LIKE 'is_source';
```

Then backfill top-level source cache if needed.

### Atom 2: Minimal Graph Source Test

Create a small task graph:

```text
A -> B -> C
```

Expected top-level source cache:

```text
A.is_source = 1
B.is_source = 0
C.is_source = 0
```

Also test a single-node graph:

```text
X.is_source = 1
```

### Atom 3: Minimal Instance Lineage Freeze Test

For same schedule fire / expect time:

```text
A -> B -> C
```

Expected instance atlas:

```text
instance_node(A).is_source = 1
instance_node(B).is_source = 0
instance_node(C).is_source = 0
```

Expected instance edges:

```text
B_instance_node_guid -> A_instance_node_guid
C_instance_node_guid -> B_instance_node_guid
```

### Atom 4: Dependency Blockage Test

For B not finished and A not finished:

```text
fetchDependencyBlockages(B_instance_guid)
```

Expected:

```text
B blocked by A
```

After A is marked finished:

```text
B no longer blocked by A
```

### Atom 5: Idempotent Instance Preparation

Implement lookup by `task_guid + expect_time` before creating new instance.

Current mapper candidate:

```text
InstanceNodeManipulator / InstanceNodeMapper
```

Need add to Hydra runtime mapper, not just Odin instance atlas mapper.

### Atom 6: Business Time Epoch

Fix scheduler-created instance `businessTime` by explicitly setting `LaunchFeature.bizTimeEpoch` from schedule cursor.

### Atom 7: Impetus pipeCreate Boundary

Make instance scheduler impetus perform:

```text
dependency check
resource/queue check
pipeCreate
status -> ProcessStandby
schedule_time -> now
```

Do not `pipeLaunch` yet.

## 10. Recovery Notes For Future Assistant

If resuming with no chat history:

1. Read this document first.
2. Run residual search:

```bash
rg -n "is_isolated|isolated|fetchIsolated|setIsIsolated" \
  /Users/wujunhong/projs/Hydra/Odin \
  /Users/wujunhong/projs/Hydra/Hydra \
  -g '!**/target/**'
```

3. Compile:

```bash
cd /Users/wujunhong/projs/Hydra/Hydra
mvn -pl hydra-architecture -DskipTests install

cd /Users/wujunhong/projs/Hydra/Odin
mvn -pl odin-framework-conduct -am -DskipTests compile
```

4. Avoid reverting unrelated dirty files.
5. Continue with idempotent instance preparation or Impetus pipeCreate boundary depending on user direction.

## 11. Short Mental Model

The source-line is now:

```text
Task graph:
    edge table is truth
    is_source is hot cache

Instance graph:
    frozen instance nodes are inserted first
    frozen instance edges are inserted second
    is_source is frozen snapshot calculated from frozen parent edges

Dependency check:
    child instance guid
    -> child instance atlas node
    -> instance edge
    -> parent instance atlas node
    -> parent hydra_task_instance
```

The reason this matters:

```text
Do not schedule downstream instance until upstream parent instance is finished.
Do not let live task graph mutation rewrite already frozen instance DAG truth.
```


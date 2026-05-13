# Red Mine Tombstone - 2026-05-13

## Purpose

This Tombstone captures the current construction state for Red Industry / Idea Mining, known locally as **点子矿业 / 红矿 / Red Mine**.

It is written for context recovery, migration, and continued implementation. It focuses on what exists now, what design decisions were made, what files matter, what still smells risky, and how to resume without re-discovering the whole battlefield.

Current user preference and tone:

- User calls the assistant 小猫猫 and prefers fast, concrete execution after saying `deal`.
- User likes plans before large changes, but once approved expects implementation.
- User strongly dislikes inconsistent frontend style, oversized UI, hand-rolled canvas code, and half-i18n.
- The visual target is close to Lunar Flare / 魔法总裁, not a new visual universe.

## Current Feature Scope

Red Mine is the first-round **industrial concept clue tree** system:

- Workspaces are now called **矿区** in UI.
- A mine tree is attached directly under a workspace.
- The old extra folder layer was normalized away.
- Mine nodes are concept-level nodes, not formal Red Idea production objects.
- Primary relation controls layout.
- Weak reference relation renders as dashed secondary relation.
- Tree editor is structured data driven, not a free-form whiteboard.

Current topology:

```text
红色工业
└─ 点子矿业
   ├─ 矿区 / Workspace
   │  ├─ 矿树 / Tree
   │  │  ├─ 矿点 / Node
   │  │  ├─ 主干关系 / Primary relation
   │  │  └─ 弱引用 / WeakRef edge
```

Removed topology:

```text
Workspace
└─ Folder
   └─ Tree
```

The `red_mine_folder` layer is gone conceptually and should be gone physically in DB.

## Database Notes

The normalized schema uses:

- `red_mine_workspace`
- `red_mine_tree`
- `red_mine_node`
- `red_mine_edge`
- `red_mine_operation_log`

Folder layer removal DB migration that must have been applied manually:

```sql
ALTER TABLE `red_mine_tree` DROP COLUMN `folder_guid`;
ALTER TABLE `red_mine_operation_log` DROP COLUMN `folder_guid`;
DROP TABLE IF EXISTS `red_mine_folder`;
```

If creating a tree fails with:

```text
Field 'folder_guid' doesn't have a default value
```

then the DB migration above has not been applied to the active database.

Operation log is intentionally thin. It is not a full audit table. It should only sample important lifecycle actions:

- Create tree
- Archive tree
- Delete/archive node
- Delete/archive weak ref
- Prune
- Promote to idea, later

Avoid logging noisy UI operations such as add/remove plugin.

## Backend State

Main backend root:

```text
/Users/wujunhong/GolandProjects/ethercraft-cradle/jaguar/industry/red/mine
```

Important backend files:

```text
jaguar/industry/red/mine/api/dto/dto.go
jaguar/industry/red/mine/api/handler/handler.go
jaguar/industry/red/mine/api/handler/mine_handler.go
jaguar/industry/red/mine/api/router/router.go
jaguar/industry/red/mine/core/mapper/mine_mapper.go
jaguar/industry/red/mine/core/record/mine_record.go
jaguar/industry/red/mine/core/service/service.go
```

Mapper XML files:

```text
assets/mapper/jaguar/MineWorkspaceMapper.xml
assets/mapper/jaguar/MineTreeMapper.xml
assets/mapper/jaguar/MineNodeMapper.xml
assets/mapper/jaguar/MineEdgeMapper.xml
assets/mapper/jaguar/MineOperationLogMapper.xml
```

Core API shape:

```text
GET  /api/red/mine/workspaces
POST /api/red/mine/workspaces
PUT  /api/red/mine/workspaces/:workspaceGuid
POST /api/red/mine/workspaces/:workspaceGuid/archive

GET  /api/red/mine/workspaces/:workspaceGuid/trees
POST /api/red/mine/workspaces/:workspaceGuid/trees
GET  /api/red/mine/trees/:treeGuid/render-model
PUT  /api/red/mine/trees/:treeGuid
POST /api/red/mine/trees/:treeGuid/archive

POST /api/red/mine/nodes
PUT  /api/red/mine/nodes/:nodeGuid
POST /api/red/mine/nodes/:nodeGuid/candidate
POST /api/red/mine/nodes/:nodeGuid/prune
POST /api/red/mine/nodes/:nodeGuid/restore
POST /api/red/mine/nodes/:nodeGuid/archive

POST /api/red/mine/edges
POST /api/red/mine/edges/:edgeGuid/archive
```

Render model shape:

```json
{
  "workspace": {},
  "tree": {},
  "nodes": [],
  "primary_edges": [],
  "weak_edges": [],
  "stats": {},
  "view_config": {}
}
```

Primary edges are generated from node `primary_parent_guid`; weak refs come from `red_mine_edge`.

Backend verification command that passed:

```bash
cd /Users/wujunhong/GolandProjects/ethercraft-cradle
go test ./jaguar/industry/red/mine/... ./jaguar/system/core
```

## Frontend State

Frontend root:

```text
/Users/wujunhong/GolandProjects/ethercraft-cradle/app_client
```

Note: From the cradle repo root, `app_client` currently appears as an untracked directory. Inside `app_client`, it behaves like its own frontend working tree with many changed files. Be careful not to confuse the two git contexts.

Frontend API file:

```text
app_client/src/api/red-mine.js
```

Routes:

```text
/red/mine/workspaces
/red/mine/workspaces/:workspaceGuid
/red/mine/trees/:treeGuid
```

Router entries live in:

```text
app_client/src/router/index.js
```

Route title translator was extended in:

```text
app_client/src/utils/route-title.js
```

Shared Red Industry visual layer:

```text
app_client/src/styles/red-industry.scss
```

This file was created to make Red Mine visually match Lunar Flare without modifying `lunar-flare/styles/flare.scss`.

Important rule:

Do not break Lunar Flare / 魔法总裁 pages. If visual reuse is needed, copy or generalize carefully through `red-industry.scss`; do not casually mutate the existing Lunar Flare style file.

## Frontend Files

Red Mine pages and components:

```text
src/views/red-mine/workspaces.vue
src/views/red-mine/workspace.vue
src/views/red-mine/tree-editor.vue
src/views/red-mine/components/MineShell.vue
```

ReactFlow canvas layer:

```text
src/views/red-mine/flow/MineTreeFlowHost.vue
src/views/red-mine/flow/mineTreeFlowApp.js
src/views/red-mine/flow/mineTreeFlowModel.js
src/views/red-mine/flow/mineTreeLayout.js
src/views/red-mine/flow/mineTreeNode.js
src/views/red-mine/flow/mineTreeEdge.js
src/views/red-mine/flow/mineTreeFlow.scss
```

i18n layer:

```text
src/views/red-mine/i18n/index.js
src/views/red-mine/i18n/zh-CN.js
src/views/red-mine/i18n/en-US.js
src/views/red-mine/i18n/es-ES.js
```

Dependency notes:

```json
"@xyflow/react": "^12.10.2",
"d3-hierarchy": "^3.1.2"
```

`@xyflow/react` already existed. `d3-hierarchy` was added manually into `package.json` / `package-lock.json`, and also installed manually under `node_modules/d3-hierarchy` because normal npm tooling was not available in this environment.

## Frontend Design Decisions

### 1. Workspace equals Mine Area

The UI says 矿区, but the backend and API still use workspace naming:

- `red_mine_workspace`
- `workspaceGuid`
- `/workspaces`

This is intentional. Do not re-add folder layer unless product direction changes.

### 2. Left Sidebar Behavior

The sidebar is based on Lunar Flare style:

- Click a workspace / mine area: expand or collapse only.
- Do not navigate on mine area click.
- Click a tree under it to open the tree editor.

This was explicitly corrected after user feedback.

### 3. ReactFlow + d3-hierarchy

The hand-rolled SVG canvas was replaced with:

- `@xyflow/react` for graph rendering and interactions.
- `d3-hierarchy` for deterministic tree layout.

Do not go back to handmade SVG layout. User called that too handcraft/retro.

Layout constants currently live in:

```text
src/views/red-mine/flow/mineTreeLayout.js
```

Current spacing was reduced after user complained nodes were too fat:

```js
const NODE_GAP_X = 218
const NODE_GAP_Y = 68
```

`fitView` max zoom is limited to avoid a small graph becoming huge:

```js
flow.fitView({ padding: 0.18, duration: 180, minZoom: 0.35, maxZoom: 0.82 })
```

### 4. Softer Color

Root node blue was softened from bright blue to:

```scss
#5f8fc7
```

Primary edge:

```scss
#a9b6c5
```

Weak edge:

```scss
#d8892a
```

### 5. i18n

The frontend standard requires i18n for all titles, descriptions, categories. Red Mine now has its own i18n package supporting:

- `zh-CN`
- `en-US`
- `es-ES`

Hardcoded Chinese in `src/views/red-mine` outside i18n has been scanned out.

Useful scan:

```bash
cd /Users/wujunhong/GolandProjects/ethercraft-cradle/app_client
rg "[\\p{Han}]" src/views/red-mine -g '!src/views/red-mine/i18n/**'
```

Expected result: no output.

### 6. Search Input Icon

Element UI `prefix-icon` had vertical centering issues. Red Mine workspace search now avoids `prefix-icon` and uses a custom absolute-positioned icon:

```vue
<label class="red-mine-search-control">
  <i class="el-icon-search" aria-hidden="true" />
  <el-input ... />
</label>
```

This is in:

```text
src/views/red-mine/workspaces.vue
```

If the icon looks wrong again, inspect `.red-mine-search-control` first.

## Current Tree Editor Interactions

Tree editor:

```text
src/views/red-mine/tree-editor.vue
```

Current behavior:

- Click node: select node and show inspector.
- Right-click node: opens lightweight context menu.
- Right-click menu supports:
  - Edit
  - New
  - Delete
- Delete calls `archiveMineNode`, not physical deletion.
- Candidate/prune/restore use backend status endpoints.
- Weak ref creation still uses existing Element dialog.
- Child creation still has existing dialog, but new white-dot draft flow is preferred for fast tree growth.

### Draft Node / Pre-node Behavior

Clicking the right-side white dot on a node creates a temporary draft node:

- It does not immediately write to database.
- It appends a frontend-only node to the render model.
- It appends a frontend-only primary edge.
- It selects the draft and focuses the right inspector title input.
- If the user clicks elsewhere, presses Esc, or closes the inspector before saving, the draft disappears.
- If the user saves, `createMineNode` is called and the server returns a real node.

Draft node fields:

```js
{
  guid: `__draft__:${parentGuid}:${Date.now()}`,
  tree_guid: treeGuid,
  title: '',
  memo: '',
  node_type: 'Concept',
  status: 'Draft',
  primary_parent_guid: parentGuid,
  sort_order: 0,
  tags_json: '[]',
  isDraft: true
}
```

Draft support is spread across:

```text
tree-editor.vue
MineTreeFlowHost.vue
mineTreeFlowApp.js
mineTreeFlowModel.js
mineTreeNode.js
mineTreeEdge.js
mineTreeFlow.scss
```

### White Dot Event

The visible connector is now a button in `mineTreeNode.js`:

```js
className: 'mine-flow-node__connector mine-flow-node__connector--right'
```

It calls `onCreateDraft(node.guid)`.

The transparent ReactFlow Handle has `pointer-events: none` in CSS so it does not steal the white-dot click.

### Weak Ref Render Bug

Observed bug:

Weak dashed edges sometimes did not render until activating/clicking the tree window.

Current mitigation:

- `useUpdateNodeInternals()` is called after graph changes.
- `ResizeObserver` observes the canvas and updates node internals after size changes.
- `fitView` is also called after graph node/edge count changes.

Implementation location:

```text
src/views/red-mine/flow/mineTreeFlowApp.js
```

If weak edges still occasionally disappear:

1. Inspect if handles exist in DOM before edges render.
2. Consider using `useNodesState/useEdgesState` instead of supplying plain `nodes/edges` props.
3. Consider `key={graphSignature}` remount only when node/edge topology changes.
4. Avoid excessive remounting on selection-only changes.

## Known UX Gaps / Next Work

### P0-ish follow-ups

1. Verify white-dot draft creation in real Chrome 123.
2. Verify dashed weak refs on initial page load, after reload, and after toggling "all weak refs".
3. Verify right-click menu positioning when page is scrolled or devtools is open.
4. Decide delete semantics:
   - Currently archive only.
   - If node has children, children are not automatically reparented.
   - Product may later need cascade archive or blocked delete.
5. Replace remaining dialogs with inline panels if strictly following the frontend standard:
   - New mine area dialog
   - New mine tree dialog
   - Add weak ref dialog
   - Old child dialog

### P1 follow-ups

1. Add a proper weak-ref right-click menu:
   - Delete/archive weak ref
   - Edit memo
2. Add edge creation gesture for weak refs.
3. Add status filters in editor.
4. Add outline view.
5. Add candidate/pruned list as tabs or compact panel.
6. Persist manual node positions to `render_json` only if user asks for manual layout. Current layout is auto layout from d3.

### P2 follow-ups

1. Full convergence map UX.
2. Start/landing node actions.
3. Block / merge / path edge types.
4. Promote mine node to Red Idea production.
5. Red Idea production feedback into mine trees.
6. Similarity / sampling / LLM exploration, later.

## i18n Details

Red Mine translator:

```text
src/views/red-mine/i18n/index.js
```

It exports:

```js
redMineLocales
defaultRedMineLocale
normalizeRedMineLocale
translateRedMine
redMineLabels
```

`redMineLabels(locale)` returns a compact label object passed into ReactFlow so React components do not import Vue/store i18n directly.

Route titles:

```text
src/router/index.js
src/utils/route-title.js
```

Router meta for red-mine routes uses:

```js
i18nScope: 'redMine'
titleKey: '...'
```

## Validation Already Run

Frontend lint:

```bash
cd /Users/wujunhong/GolandProjects/ethercraft-cradle/app_client
./node_modules/.bin/eslint src/views/red-mine/tree-editor.vue src/views/red-mine/flow/*.js src/views/red-mine/flow/*.vue src/views/red-mine/i18n/*.js
./node_modules/.bin/eslint src/views/red-mine src/utils/route-title.js src/router/index.js
```

Frontend build:

```bash
cd /Users/wujunhong/GolandProjects/ethercraft-cradle/app_client
./node_modules/.bin/vue-cli-service build --mode staging
```

Build passes with only existing webpack bundle size warnings:

```text
asset size limit
entrypoint size limit
webpack performance recommendations
```

Backend tests:

```bash
cd /Users/wujunhong/GolandProjects/ethercraft-cradle
go test ./jaguar/industry/red/mine/... ./jaguar/system/core
```

Passed earlier in this construction thread.

## Current Git / Workspace Notes

From:

```text
/Users/wujunhong/GolandProjects/ethercraft-cradle
```

There are many unrelated dirty or untracked files, including `.DS_Store`, system temp files, Red Idea changes, and `app_client` as an untracked directory from the parent repo perspective. Do not clean or reset anything casually.

From:

```text
/Users/wujunhong/GolandProjects/ethercraft-cradle/app_client
```

Relevant current red-mine work includes:

```text
src/api/red-mine.js
src/styles/red-industry.scss
src/views/red-mine/**
src/router/index.js
src/utils/route-title.js
package.json
package-lock.json
```

There are also unrelated modified files in app_client from earlier work:

```text
src/components/ether-tree-explorer/tree-context-menu.vue
src/components/ether-tree-explorer/tree-grid.vue
src/views/red-idea/source/components/MarkdownSourceView.vue
src/views/red-idea/templates/detail.vue
src/views/red-idea/templates/index.vue
src/views/red-idea/workspace/index.vue
```

Do not revert these unless explicitly asked.

## Resume Checklist

When resuming:

1. Read this file.
2. Check active DB has the normalized schema, no `red_mine_folder`, no `folder_guid`.
3. Start backend if needed.
4. Start frontend dev server if needed.
5. Open:

```text
http://127.0.0.1:9527/#/red/mine/workspaces
```

6. Create or enter a mine area.
7. Open a mine tree.
8. Test:
   - Node click selection
   - Right-click menu
   - White-dot draft node
   - Draft disappears on click-away
   - Draft saves to DB
   - Delete archives node
   - Weak refs render before and after focusing tree window

## Important Product Constraints

Keep these principles:

- Mine points are concept clumps, not Red Idea production objects.
- Do not introduce SPEC / Source / Explain / Design concepts into Red Mine V0.1 UI.
- Canvas is a renderer for structured data, not a free-form whiteboard.
- Primary relation drives layout.
- Weak refs do not alter layout.
- Keep UI aligned with Lunar Flare / 魔法总裁.
- Use existing libraries for graph layout and interaction. Do not hand-roll full graph rendering again.
- Keep i18n complete for zh-CN, en-US, es-ES.

## Last Known User Ask Before Tombstone

User asked:

```text
dump 详细的 Tombstone 到
/Users/wujunhong/projs/Hydra/docs/context

便于后续上下文恢复、迁移、继续施工
```

This file is the response artifact.

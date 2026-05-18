@import /docs/skills/tombstone_dump_skill.md

# 项目 Docs 目录架构形式化说明

## 最高系统命名法
对于所有的 `/docs/*` 下的直接孩子以及级联的孩子文件有效。
文件名采用：Snake Case 命名法，e.g. `snake_snack_snake.md`


@scope(在没有明确补充说明之前，不要随便载入`/docs/context/`下的文件)
@taboo 严禁全量读取`/docs/context/`下的孩子文件
## context
path: /docs/context/
下面放上下文 Checkpoint、Stack、Tombstone、Snap、Core。
系统命名法：
{{Module Design}}_{{Extra}}_{{Level}}_{{Version/Stage}}_{{ModelName}}_{{ModelVersion}}.TYPE
其中：`Level ∈ [Checkpoint、Stack、Tombstone、Snap、Core]`
e.g. `dec_design_tombstone_stage1_opus_4_6.md`

## scheme
path: /docs/scheme/
下面放顶层策划。
严格定义：顶层策划包括 最高形式化的点子、最高需求设计、PRD、架构设计、内核设计等最高策划蓝图。
抽象点子：最高抽象，仅描述业务意义。
形式点子：可执行展开的，谓语逻辑完备的形式化描述，必须包括完整的事务链、事件描述。
需求设计：详细描述业务的需求、关注业务的逻辑链，不要混杂技术细节、产品细节、商业计划等。
PRD: 产品设计文档，描述产品级设计细节，关注用户体验、交互设计、功能细节等。
架构设计：描述架构级设计细节，关注系统架构、系统设计、系统接口等。
内核设计：最高技术设计，描述系统内核架构、技术选型、核心算法、核心结构等。

## design
path: /docs/design/
下面放模块、子系统的设计。
严格定义：模块、子系统的设计包括 由顶层设计拆分出来的模块、子系统的中间层设计，关注局部设计。

## procedure
path: /docs/procedure/
下面放临界施工方案。
严格定义：临界施工方案表示面向具体施工细节的清单，关注实现细节、实现流程、改动细节等核心逻辑，便于用户确认是否执行和后续回顾和审计。
通常在欲施工前生成临界确认方案。

## skills
path: /docs/skills/
下面放具体的技能说明书。

## standard
path: /docs/standard/
下面放全局静态标准。

## std
path: /docs/std/
下面放本项目实例的具体标准、增量标准。

## tech
path: /docs/tech/
下面放本项目沉淀的技术架构、技术说明书、技术方案。


# Dragon King Instruction for LLM (DKI)
# Chat LLM Formal Instruction Language Syntax

## Purpose | 目的
DKI is a lightweight inline instruction convention for prompt writing, and does not require a strict parser.
It is a compact instruction notation written directly inside natural language prompts, intended to be readable by both humans and LLMs.

It is designed for:
code generation tools
design / architecture discussion tools
context loading and continuation
prompt-level attention and scope control


## Fundamental Symbol | 基础符号
`->` production / 产生式诱导
`>>`, `<<` semantic effect direction / 作用方向
`<...>` meta slot / 元语法参数槽

## Core Syntax | 核心文法
### Basic Objects
```ebnf
PATH    -> STRING
TOPIC   -> STRING
CONTEXT -> STRING
TOKEN   -> STRING
EXPR    -> STRING
```


## Predicate Syntax | 谓语文法
### Predicate Principle
```text
NLP Tag Tree: V >> O / Predicate >> Object
```

Prompt Instruction Syntax
@TOKEN ARG / Direct predicate-object form: apply TOKEN directly to ARG. / 直接动宾式：TOKEN 直接作用于 ARG。
@TOKEN(EXPR) / Eval form: evaluate EXPR first, then decide subsequent logic. / 求值式：先对 EXPR 推断或求值，再决定后续逻辑。
自然语言中 @引导的TOKEN，请注意分析，通常后面跟随具体谓语指令，指令明确没有左递归，一遍扫描即可。

常用上下文载入指令：
@import PATH / 导入文件到当前上下文，谓语意图：恢复已经结构化过的上下文。
@scan PATH / 扫描具体文件，谓语意图：加载Blob的对象，进行 profile 理解。
@invoke PATH / 执行具体命令，通常是命令行，谓语意图：看现象。

常用上下文熵控制指令：
@dropout TOPIC / 丢弃，谓语意图：丢弃某一主题的上下文信息。
@taboo TOPIC / 禁忌，谓语意图：明确禁止关注的主题。

常用上下文控制指令：
@scope(CONTEXT) / 条件主语成立：当且仅当 CONTEXT 满足时，后续逻辑成立。
@focus TOPIC / 提高某主题优先级，作为当前核心主线。
@freeze <TOPIC | CONTEXT> / 表示先固定某结论、某前提或某上下文，不要来回漂移。


## Directive | 直接精简指令
注意：对于某次模型调用时，一般单独出现，或伴随极短文本，长文本中不会出现。
execute / Execute the most recent explicit plan. / 执行前面提出的计划。
confirmed / Validation or review is complete; the current plan / action is approved for execution. / 核验完成，可以执行。
revoke / Revoke the most recent explicit operation, decision, or execution intent. / 撤销上一步操作。
halt / Do not continue the current line unless explicitly resumed. / 立即停止当前执行、展开或动作链；除非后续明确恢复，否则不要继续当前链路。
jmp <TOPIC | CONTEXT> / Jump to the specified topic or context and continue from there. / 跳转到指定主题或上下文，并从该处继续。


## Introduction | 备注说明
本文的指令单词选择，尽可能选用自然语言中的低频单词，避免和指令后续嵌入的正文造成上下文污染。





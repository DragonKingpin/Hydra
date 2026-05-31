# Gladius Shell Syntax - POSIX Shell Contract

Gladius Shell Syntax is the formal language contract for the Gladius interactive shell and script kernel.

This document describes the kernel naming taxonomy, the boundary of the shell language layer, the current minimal POSIX-first syntax target, and the construction route toward a Turing-complete shell subsystem.

Gladius Shell Syntax source is the language seed. Lexer tokens, AST, parser rules, expansion rules, runtime behavior, and builtin command contracts are rendered views.

## 1. Bean Nuts Nomenclature

```text
Bean Nuts Almond Dragon Gladius Shell
```

## 2. Boundary

Gladius Shell Syntax describes:

```text
shell source text
lexical tokenization
reserved word recognition
AST shape
parser precedence
quote preservation
minimal expansion model
pipeline / list / redirect skeleton
if / else / while / until / for / case / function skeleton
shell variable and special builtin skeleton
runtime execution contract
builtin command contract
ShellIO contract
```

Gladius Shell Syntax does not describe:

```text
host operating system shell behavior
Windows command prompt semantics
PowerShell semantics
file permission model
job control
terminal signal handling
process group management
interactive UI rendering details
remote transport protocol
full Bash compatibility
```

## 3. Package Layout

The language kernel lives under:

```text
gladius/shell/lang
```

Initial package layout:

```text
gladius/shell/lang/ast
gladius/shell/lang/lexer
gladius/shell/lang/parser
gladius/shell/lang/expand
gladius/shell/lang/runtime
```

Compatibility profiles, if needed, should live below the language kernel:

```text
gladius/shell/lang/bashcompat
```

The top-level package `gladius/bash` is forbidden for the POSIX-first kernel because it incorrectly names the subsystem as a Bash implementation.

## 4. Minimal Language Target

The current complete minimal target supports:

```sh
A=1 echo "$A"
echo hi | cat
cmd1 && cmd2 || cmd3
echo hi > out.txt
cat < in.txt
cat <> rw.txt
echo hi >> out.txt
missing 2> err.txt
missing > combined.txt 2>&1
cat <&-
echo hidden >&-
printf "%s\n" alpha beta | grep -n a | head -n 1
! false
set -o pipefail
missing_command | cat
set +o pipefail
set -f
printf "%s\n" *.txt
set +f

if test "$x" = ok; then
  echo yes
else
  echo no
fi

while test "$x" != done; do
  echo "$x"
done

until test "$x" = done; do
  echo "$x"
  x=done
done

for name in alpha beta; do
  test "$name" = stop && break
  test "$name" = skip && continue
  echo "$name"
done

case "$name" in
  alpha|beta) echo known ;;
  *) echo unknown ;;
esac

hello() {
  test "$1" = stop && return 7
  echo hi
}
hello

( A=inner; echo "$A" )
{ echo group; } > group.txt

cat <<EOF
hello $A
EOF

x=$(printf "%s\n" hello)
y=`echo world`
i=$((i + 1))

. ./profile.gladius
source ./profile.gladius
eval 'echo "$A"'

export A
unset A
readonly A
alias ll='ls'
unalias ll
command -v echo
type -a echo
times
```

The current complete minimal target does not support:

```text
arrays
job control
trap
signal handling
permission mutation commands
full POSIX `set` option surface
full POSIX `read` option surface
full POSIX command search path semantics
```

## 5. EBNF

```ebnf
Script
    ::= Separator* List? Separator* EOF ;

List
    ::= AndOr ( ListSeparator AndOr )* ListSeparator? ;

ListSeparator
    ::= ";"
     |  NEWLINE ;

AndOr
    ::= Pipeline ( AndOrOperator Pipeline )* ;

AndOrOperator
    ::= "&&"
     |  "||" ;

Pipeline
    ::= "!"? Command ( "|" Command )* ;

Command
    ::= SimpleCommand
     |  CompoundCommand Redirect* ;

SimpleCommand
    ::= SimpleCommandPart+ ;

SimpleCommandPart
    ::= AssignmentWord
     |  Word
     |  Redirect ;

CompoundCommand
    ::= GroupCommand
     |  SubshellCommand
     |  IfClause
     |  WhileClause
     |  UntilClause
     |  ForClause
     |  CaseClause
     |  FunctionDefinition ;

GroupCommand
    ::= "{" Separator* List? Separator* "}" ;

SubshellCommand
    ::= "(" Separator* List? Separator* ")" ;

IfClause
    ::= "if" Separator* List Separator* "then" Separator* List
        ( Separator* "else" Separator* List )?
        Separator* "fi" ;

WhileClause
    ::= "while" Separator* List Separator* "do" Separator* List Separator* "done" ;

UntilClause
    ::= "until" Separator* List Separator* "do" Separator* List Separator* "done" ;

ForClause
    ::= "for" Name ( Separator* "in" ForWords? )? Separator* "do" Separator* List Separator* "done" ;

ForWords
    ::= Word+ ;

CaseClause
    ::= "case" Word Separator* "in" Separator* CaseItem* Separator* "esac" ;

CaseItem
    ::= CasePatternList ")" Separator* List? Separator* ";;"? Separator* ;

CasePatternList
    ::= Word ( "|" Word )* ;

FunctionDefinition
    ::= Name "(" ")" Separator* CompoundCommand ;

Redirect
    ::= IoNumber? "<" Word
     |  IoNumber? "<>" Word
     |  IoNumber? ">" Word
     |  IoNumber? ">>" Word
     |  IoNumber? "<<" Word
     |  IoNumber? "<<-" Word
     |  IoNumber? "<" "&" IoNumber
     |  IoNumber? "<" "&" "-"
     |  IoNumber? ">" "&" IoNumber
     |  IoNumber? ">" "&" "-"
     |  IoNumber? ">>" "&" IoNumber ;

IoNumber
    ::= Digit+ ;

AssignmentWord
    ::= Name "=" Word? ;

Word
    ::= WordPart+ ;

WordPart
    ::= UnquotedText
     |  SingleQuotedText
     |  DoubleQuotedText ;

Name
    ::= NameStart NamePart* ;

NameStart
    ::= Letter
     |  "_" ;

NamePart
    ::= Letter
     |  Digit
     |  "_" ;

Separator
    ::= NEWLINE
     |  ";" ;
```

## 6. Lexer Rules

```text
1. Lexer emits punctuation and WORD-like tokens.
2. Reserved words are not globally classified by lexer.
3. Reserved words are recognized by parser only in command position.
4. Quote information MUST be preserved in WordPart.
5. Lexer MUST NOT perform parameter expansion.
6. Lexer MUST NOT perform field splitting.
7. Lexer MUST NOT perform pathname expansion.
8. Lexer MUST NOT execute command substitution.
9. Unterminated quote is a lexical error.
10. Unsupported operators should fail explicitly instead of being silently treated as words.
11. `#` starts a comment when it appears at a token boundary outside quotes.
```

Initial token set:

```text
WORD
ASSIGNMENT_WORD
SEMI
DSEMI
AND_IF
OR_IF
PIPE
AMP
LESS
LESSGREAT
DLESS
DLESSDASH
GREAT
DGREAT
NEWLINE
LPAREN
RPAREN
LBRACE
RBRACE
EOF
```

## 7. AST Rules

AST node family:

```text
Script
List
Pipeline
Command
SimpleCommand
CompoundCommand
GroupCommand
SubshellCommand
IfClause
WhileClause
UntilClause
ForClause
CaseClause
CaseItem
FunctionDefinition
Assignment
Word
WordPart
Redirect
```

AST rules:

```text
1. Parser outputs AST only.
2. Parser MUST NOT execute commands.
3. Parser MUST NOT expand variables.
4. Parser MUST NOT resolve files.
5. Parser MUST preserve redirects on both simple commands and compound commands.
6. Pipeline commands MUST be Command nodes, not only SimpleCommand nodes.
7. Pipeline MAY carry a negation flag for leading `!`.
8. Function definition body MUST be a Command node.
9. if condition and branch bodies MUST be List nodes.
10. while condition and body MUST be List nodes.
11. until condition and body MUST be List nodes.
12. for body MUST be a List node.
13. case item body MAY be empty.
14. Word MUST preserve quote context for later expansion.
15. Here-doc body text is attached to Redirect nodes after parser pre-scan.
```

## 8. Expansion Contract

Expansion is a separate phase after parsing.

Minimal expansion supports:

```text
quote removal
$VAR
${VAR}
${VAR-word}
${VAR:-word}
${VAR=word}
${VAR:=word}
${VAR+word}
${VAR:+word}
${VAR?word}
${VAR:?word}
${#VAR}
${VAR%pattern}
${VAR%%pattern}
${VAR#pattern}
${VAR##pattern}
$?
$#
$@
$*
$1 ... $9
field splitting for unquoted expansion results
command substitution with $(...)
command substitution with `...`
arithmetic expansion with $((...))
tilde expansion for `~` and `~/...`
pathname expansion with dotfile rule
```

Expansion does not support:

```text
process substitution
brace expansion
```

Expansion rules:

```text
1. Single quoted text MUST NOT be expanded.
2. Double quoted text MAY expand parameters but MUST NOT perform field splitting.
3. Unquoted expansion MAY perform field splitting.
4. Default IFS is space, tab, newline.
5. Pathname expansion MUST use Gladius Mini FS.
6. Expansion MUST NOT access host paths directly.
7. `"$@"` expands to separate fields while preserving each positional parameter.
8. `"$*"` expands to one field joined by the first character of `IFS`, or a space when `IFS` is unset or empty.
9. `$0` expands to the Gladius shell name.
10. `${10}` and higher braced positional parameters are supported.
11. Unbraced `$10` follows POSIX-like single-digit behavior: `$1` followed by literal `0`.
12. `${VAR:-word}` uses word when VAR is unset or null.
13. `${VAR:=word}` assigns word when VAR is unset or null.
14. `${VAR:+word}` uses word when VAR is set and non-null.
15. `${VAR:?word}` fails expansion when VAR is unset or null.
16. Pathname expansion supports `*`, `?`, and bracket expressions accepted by path-style matching.
17. Pathname expansion applies only to unquoted fields.
18. Unmatched pathname patterns remain literal.
19. Relative pathname patterns return relative logical paths.
20. Absolute pathname patterns return absolute logical paths.
21. Pathname expansion results MUST be sorted.
22. Unquoted field splitting MUST use the current `IFS` variable.
23. `$(...)` command substitution executes Gladius Shell source through a child runtime and captures stdout.
24. Backtick command substitution follows the same child-runtime rule.
25. Command substitution MUST NOT invoke a host shell.
26. Command substitution MUST trim trailing newline bytes from captured stdout.
27. `$((...))` arithmetic expansion evaluates integer arithmetic.
28. Arithmetic expansion supports variables and integer `+`, `-`, `*`, `/`, `%`, unary signs, and parentheses.
29. Expansion recursion guards MUST come from ShellContext, not package constants.
30. `~` and `~/...` expand from `HOME` when the field is unquoted.
31. Pathname expansion MUST NOT match dotfiles unless the pattern segment starts with `.`.
32. `set -f` disables pathname expansion.
33. `set -u` makes unset unbraced and braced parameters fail expansion, except defaulting operators that explicitly handle unset values.
34. Non-whitespace IFS delimiters preserve middle empty fields during unquoted field splitting.
35. Trailing non-whitespace IFS delimiters do not create an extra final field.
36. Mixed IFS values preserve middle empty fields caused by non-whitespace delimiters while trimming IFS whitespace around fields.
```

## 9. Runtime Contract

Runtime executes AST against a Gladius shell session.

Runtime owns:

```text
environment snapshot
shell variable store
exported variable set
positional parameter frame
last exit code
function registry
ShellIO
stdin / stdout / stderr buffers
lightweight fd table for non-standard fd bindings
loop guard
call depth guard
source depth guard
eval depth guard
Mini FS path access
```

Runtime rules:

```text
1. List sequence returns the status of the last executed pipeline.
2. `&&` executes the right pipeline only when the left status is zero.
3. `||` executes the right pipeline only when the left status is non-zero.
4. Pipeline passes stdout of one command as stdin of the next command.
5. Pipeline returns the status of the last command by default.
6. Leading `!` inverts the final pipeline status.
7. With `pipefail` enabled, pipeline returns the status of the rightmost non-zero command, or zero when all commands succeed.
8. Redirect `<` reads stdin from Mini FS.
9. Redirect `<>` opens a Mini FS target for read-write stdin.
10. Redirect `>` writes stdout to Mini FS and truncates target.
11. Redirect `>>` appends stdout to Mini FS target.
12. Redirect `2>` writes stderr to Mini FS and truncates target.
13. Redirect `2>>` appends stderr to Mini FS target.
14. Redirect `2>&1` duplicates stdout into stderr in left-to-right redirect order.
15. Redirect `1>&2` duplicates stderr into stdout in left-to-right redirect order.
16. Redirect `>&-` closes stdout and `2>&-` closes stderr by routing output to discard.
17. Redirect `<&-` closes stdin and future reads from stdin fail.
18. Redirect `n>file` and `n>>file` may bind lightweight ShellIO fd table writers for non-standard fd numbers.
19. Redirect `n>&m` duplicates stdout/stderr/fd-table writers in left-to-right redirect order.
20. Redirect `n>&-` closes non-standard fd table entries.
21. Redirect-only simple commands apply their redirects and return status zero when redirect setup succeeds.
22. Redirect `<<` supplies a here-doc body as stdin.
23. Redirect `<<-` supplies a here-doc body as stdin after stripping leading tabs from body lines.
24. Quoted here-doc delimiters suppress expansion of the here-doc body.
25. Unquoted here-doc bodies MAY expand parameters, command substitution, and arithmetic expansion.
26. Runtime MUST enforce MaxHeredocBytes.
27. Group commands execute in the current runtime frame.
28. Subshell commands execute in a child runtime frame and MUST NOT leak variable assignments back to the parent.
29. `if` executes condition list and selects branch by zero / non-zero status.
30. `errexit` MUST be suppressed in if / while / until conditions and in non-final AND-OR list positions.
31. `while` repeats while condition list exits with zero.
32. `until` repeats while condition list exits with non-zero.
33. `for` expands its word list, binds each item to the loop variable, and executes the body.
34. `case` expands the selector, matches items in order, and executes the first matching body.
35. `break` exits loop frames.
36. `continue` resumes loop frames.
37. Runtime MUST enforce MaxLoopIterations.
38. Runtime MUST enforce MaxCallDepth.
39. Runtime MUST enforce MaxCommandSubstitutionDepth.
40. Runtime MUST enforce MaxArithmeticDepth.
41. Runtime MUST enforce MaxExpansionDepth.
42. Runtime MUST enforce MaxSourceDepth.
43. Runtime MUST enforce MaxEvalDepth.
44. Runtime MUST enforce MaxScriptBytes for sourced and file-run scripts.
45. Function definition registers a function in the runtime function registry.
46. Function invocation creates a new call frame.
47. Function call frame sets `$1..$9`, `$#`, `$@`, and `$*`.
48. Assignment-only commands update the current shell variable store.
49. Assignment prefixes before `export` and `readonly` are retained in the current shell variable store.
50. `export` marks shell variables for exported environment output.
51. `unset` removes shell variables from the current shell session.
52. `readonly` marks shell variables as immutable.
53. `set --` replaces positional parameters.
54. `set -e/+e` toggles errexit.
55. `set -f/+f` toggles pathname expansion.
56. `set -u/+u` toggles unset-parameter expansion errors.
57. `set -o pipefail` and `set +o pipefail` toggle pipeline status behavior.
58. `set -o` prints shell option states.
59. `set +o` prints reusable shell option commands.
60. `set -x/+x` records the xtrace option flag; trace emission is a later output contract.
61. `.` and `source` execute a script file in the current runtime frame.
62. `. file arg...` and `source file arg...` temporarily replace positional parameters while the file runs.
63. `return` is allowed inside functions and sourced scripts.
64. Top-level `return` is an error and terminates the current script execution.
65. `eval` parses and executes its arguments as Gladius Shell source in the current runtime frame.
66. Alias definitions are session-scoped and may affect later commands in the same multi-line script.
67. `command` bypasses shell functions while preserving builtin execution.
68. `type` and `command -v` share resolver output for alias, function, special builtin, builtin, and controlled file entries.
69. Text output MUST go through ShellIO stdout / stderr.
70. ShellResult Data is structured side-channel data, not the primary text stream.
71. When AllowExec is enabled, an unresolved command MAY be routed to controlled external execution.
72. Controlled external execution MUST use argv APIs directly and MUST NOT invoke a host shell.
73. Runtime MUST NOT invoke Bash, PowerShell, cmd.exe, or another host shell to interpret Gladius Shell source.
```

## 10. Builtin Minimal Set

The current runtime-complete builtin set:

```text
echo
:
printf
true
false
test
[
grep
wc
head
tail
pwd
cd
ls
mkdir
stat
cat
rm
env
context
id
whoami
hostname
which
exec
alias
unalias
times
sort
uniq
cut
tr
history
help
```

The current special builtin set:

```text
return
break
continue
export
unset
readonly
shift
read
set
command
type
.
source
eval
```

The next stream-processing builtin set:

```text
sed-min
```

## 11. grep Minimal Contract

Minimal `grep` supports:

```text
grep PATTERN [FILE...]
grep -i PATTERN [FILE...]
grep -n PATTERN [FILE...]
grep -v PATTERN [FILE...]
```

Rules:

```text
1. PATTERN uses Go regular expression syntax in the first stream-processing implementation.
2. When FILE is absent, grep reads stdin.
3. When FILE is present, grep reads files through Mini FS.
4. Recursive grep is not part of the first contract.
5. Binary file policy is not part of the first contract.
```

## 12. Stream Builtin Minimal Contract

Minimal `printf` supports:

```text
printf FORMAT [ARG...]
%s
%b
%d
%i
%u
%c
%%
```

Minimal `wc` supports:

```text
wc [FILE...]
wc -l [FILE...]
wc -w [FILE...]
wc -c [FILE...]
```

Minimal `head` and `tail` support:

```text
head [-n COUNT] [FILE...]
tail [-n COUNT] [FILE...]
```

Rules:

```text
1. When FILE is absent, stream builtins read stdin.
2. When FILE is present, stream builtins read files through Mini FS.
3. The first implementation is text-oriented and buffered.
4. Binary policy is not part of the first contract.
```

## 13. Function Contract

Function syntax:

```sh
name() {
  list
}
```

Function rules:

```text
1. Function name MUST be a valid shell Name.
2. Function definition registers a function and returns status zero.
3. Function invocation shadows builtin lookup only according to explicit runtime policy.
4. Function call creates a new positional parameter frame.
5. Environment variables are inherited.
6. Function definitions persist in the session runtime.
7. MaxCallDepth defaults to 64.
8. `return N` exits the nearest function frame with status N.
9. `return` outside function is an error in script runtime.
```

## 14. Turing-Complete Minimality

The minimal system becomes Turing-complete when the following are available:

```text
variables
assignment
conditional branch
loop
function call
status inspection
test predicate
```

Minimal demonstration shape:

```sh
step() {
  echo "$1"
}

x=start
while test "$x" != done; do
  step "$x"
  x=done
done
```

Arithmetic expansion is available through `$((...))` for integer arithmetic. `expr` and `let` are separate command-level conveniences and are not required for the language core.

## 15. Construction Route

Phase 1: AST / Lexer / Parser.

```text
1. Define Command interface.
2. Convert Pipeline.Commands from SimpleCommand to Command.
3. Add group command.
4. Add if / then / else / fi parser.
5. Add while / do / done parser.
6. Add until / do / done parser.
7. Add for / in / do / done parser.
8. Add case / in / pattern / esac parser.
9. Add function definition parser.
10. Preserve compound redirects.
11. Add parser golden tests.
```

Phase 2: Expansion.

```text
1. Implement quote removal.
2. Implement $VAR and ${VAR}.
3. Implement special parameters.
4. Implement configurable IFS field splitting.
5. Implement ${VAR:-word}, ${VAR:=word}, ${VAR:+word}, and ${VAR:?word}.
6. Implement ${VAR-word}, ${VAR=word}, ${VAR+word}, and ${VAR?word}.
7. Implement ${#VAR}, ${VAR%pattern}, ${VAR%%pattern}, ${VAR#pattern}, and ${VAR##pattern}.
8. Implement quoted "$@" field preservation.
9. Implement pathname expansion through Mini FS.
10. Implement command substitution through a child Gladius runtime.
11. Implement arithmetic expansion.
12. Add expansion tests independent from runtime.
```

Phase 3: Runtime.

```text
1. Execute simple command against existing builtins.
2. Execute list and and-or operators.
3. Execute pipeline with streaming ShellIO pipe stages.
4. Execute redirect through Mini FS.
5. Execute if.
6. Execute while with loop guard.
7. Execute until with loop guard.
8. Execute for.
9. Execute case.
10. Execute break and continue.
11. Register and invoke functions.
12. Execute group and subshell commands.
13. Execute source and eval in the current runtime frame.
14. Add runtime integration tests.
```

Phase 4: ShellEngine integration.

```text
1. Route ShellEngine through the language parser and runtime.
2. Use ShellIO for builtin stdout and stderr.
3. Preserve structured Data only as side-channel data.
4. Register help entries by category.
5. Keep command completion derived from the help registry.
6. Remove legacy parser and lexer code.
```

Phase 5: UNIX IO.

```text
1. Add ShellIO as the standard command IO surface.
2. Route pipeline stages through streaming ShellIO pipes.
3. Route `<`, `>`, and `>>` through Mini FS.
4. Route `2>`, `2>>`, `2>&1`, and `1>&2`.
5. Route `<<` and `<<-` here-doc stdin through ShellIO.
6. Route `<>`, `>&-`, `2>&-`, and `<&-` for stdin / stdout / stderr.
7. Keep stdout and stderr distinct in ShellResult.
8. Add ShellFrame as the future transport frame shape.
9. Defer PTY and arbitrary fd table support.
```

Phase 6: Expansion guards.

```text
1. Keep MaxExpansionDepth in ShellContext.
2. Keep MaxCommandSubstitutionDepth in ShellContext.
3. Keep MaxArithmeticDepth in ShellContext.
4. Keep MaxHeredocBytes in ShellContext.
5. Keep MaxSourceDepth in ShellContext.
6. Keep MaxEvalDepth in ShellContext.
7. Keep MaxScriptBytes in ShellContext.
8. Expose guard values through the context builtin.
9. Runtime and expansion code MUST read guard values from the active ShellContext.
```

Phase 7: Controlled external lookup.

```text
1. Keep function / special builtin / builtin lookup first.
2. If command is unresolved and AllowExec is false, return unknown command.
3. If command is unresolved and AllowExec is true, route to controlled external execution.
4. Reuse AllowedPrograms and argv-only execution policy.
5. Do not invoke a host shell for external lookup.
```

Phase 8: POSIX 60% boundary suite.

```text
1. Keep a ShellEngine-level POSIX 60 suite for expansion, redirection, resolver, and special builtin boundaries.
2. Verify `"$@"`, `"$*"`, IFS splitting, pattern trimming, and parameter length.
3. Verify redirect-only commands, `<>`, `<&-`, `>&-`, and stderr/stdout duplication.
4. Verify function / builtin / file resolver output through `command -v` and `type`.
5. Verify `export`, `readonly`, `unset`, `set -e`, and source positional parameter behavior.
```

Phase 9: POSIX 70% boundary suite.

```text
1. Verify lightweight fd table behavior for `n>file`, `n>&m`, `n>&-`, and stdout/stderr/fd-table duplication.
2. Verify resolver order for alias, function, special builtin, builtin, and controlled file entries.
3. Verify `command` bypasses shell functions while preserving builtin dispatch.
4. Verify mixed IFS field splitting and quoted field preservation.
5. Verify top-level `return` terminates script execution as an error.
6. Verify `set -o` and `set +o` option-state output.
```

## 16. Standard Construction Demo

### 16.1 Source

```sh
ok() {
  echo "ok:$1"
}

if true; then
  echo start
  echo hello | cat
  for name in alpha beta; do
    echo "$name"
  done
  case "$1" in
    done) echo case-ok ;;
    *) echo case-miss ;;
  esac
  ok done
else
  echo failed
fi
```

### 16.2 AST Skeleton

```text
Script
  List
    Pipeline
      FunctionDefinition ok
        GroupCommand
          List
            Pipeline
              SimpleCommand echo "ok:$1"
    Operator ;
    Pipeline
      IfClause
        Condition
          SimpleCommand true
        Then
          SimpleCommand echo start
          Pipeline
            SimpleCommand echo hello
            SimpleCommand cat
          ForClause name in alpha beta
            SimpleCommand echo "$name"
          CaseClause "$1"
            CaseItem done
              SimpleCommand echo case-ok
            CaseItem *
              SimpleCommand echo case-miss
          SimpleCommand ok done
        Else
          SimpleCommand echo failed
```

### 16.3 Runtime Result

```text
start
hello
alpha
beta
case-ok
ok:done
```

## 17. Red Lines

```text
1. Do not call host Bash / PowerShell / cmd.exe to interpret Gladius Shell source.
2. Do not mix Windows path grammar into Gladius Shell grammar.
3. Do not perform expansion in lexer.
4. Do not perform execution in parser.
5. Do not bypass Mini FS for shell language file access.
6. Do not name the POSIX-first language kernel as Bash.
7. Do not add permissions, job control, or signal semantics in the first runtime contract.
8. Do not silently accept unsupported syntax.
9. Do not use ShellResult Data as stdout.
10. Do not keep legacy parser or lexer fallback after ShellEngine is routed through Gladius Shell Syntax.
```

## 18. POSIX Standardization Situation

Gladius Shell is currently in the POSIX-first core standardization phase.

The current standardization target is not full POSIX shell compatibility. The target is a clean, test-backed POSIX core that is strong enough for common scripts while keeping host-shell behavior outside the language layer.

Current core progress:

```text
lexer / token stream              stable core
AST shape                         stable core
parser precedence                 stable core
compound command grammar          stable core
pipeline / list execution          stable core
function / source / eval runtime   stable core
ShellIO pipeline surface           stable core
lightweight fd table               usable core
basic redirection                  stable core
here-doc                           stable core
parameter expansion                usable core
field splitting                    usable core
pathname expansion                 usable core
command substitution               usable core
arithmetic expansion               usable core
special builtin surface            usable core
resolver / type / command -v       usable core
alias session behavior             usable core
controlled external execution      policy-backed core
```

The current POSIX 70% boundary suites cover:

```text
"$@" and "$*"
IFS splitting
parameter length and pattern trimming
redirect-only commands
<>, <&-, >&-, 2>&1
function / builtin resolver output
command -v
type -a / type -t
export / readonly / unset
set -e / +e
source arguments and return
pipeline IO
group and subshell execution
lightweight fd table behavior
resolver order for alias / function / special / builtin
command builtin function bypass
mixed IFS splitting
top-level return termination
set -o / set +o option output
```

Current standardization position:

```text
syntax skeleton        near complete for the POSIX-core subset
runtime skeleton       near complete for the POSIX-core subset
expansion model        strong usable core, still not fully POSIX-exact
redirection model      lightweight fd-table core, not full POSIX fd lifecycle
builtin set            broad enough for common text scripts
resolver model         standardized usable core
alias model            session-level usable core, not full token-time POSIX aliasing
external execution     intentionally controlled and non-host-shell
```

Remaining core gaps before stricter POSIX compatibility:

```text
full arbitrary fd lifecycle and input fd duplication
full POSIX alias recognition timing
full POSIX field splitting edge cases
full special builtin fatal-error semantics
full command hashing semantics
full set option surface
trap / signal handling
job control
terminal process group behavior
advanced permission and umask semantics
```

Strategic rule:

```text
Gladius Shell should continue to standardize POSIX shell semantics where they are language-level and platform-independent.
Host differences must be absorbed by Mini FS, ShellIO, controlled external execution, or explicit compatibility adapters.
```

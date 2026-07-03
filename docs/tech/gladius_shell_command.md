# Gladius Shell Command - Builtin Command Contract

Gladius Shell Command is the command surface contract for the Gladius interactive shell and script kernel.

This document describes the current builtin taxonomy, supported command set, routing boundary, Rattlesnake native command proxy, and construction rules for future command additions.

Gladius Shell Command is a command contract document. Shell grammar, parsing, expansion, redirection, and runtime language behavior are described by `gladius_shell_syntax.md`.

## 1. Boundary

Gladius Shell Command describes:

```text
builtin command taxonomy
direct Gladius builtin command set
special builtin command set
Rattlesnake proxy command set
command ownership boundary
minimal option contracts
stdout / stderr / exit-code expectations
command testing expectations
future addition rules
```

Gladius Shell Command does not describe:

```text
shell source grammar
AST parser rules
full POSIX standard compatibility
host shell behavior
PowerShell / cmd.exe / Bash compatibility
native OS API implementation details
Rattlesnake C++ command internals
Ulfhedinn native manager internals
interactive terminal UI rendering
remote transport protocols
```

## 2. Package Layout

Direct Gladius shell builtins live under:

```text
gladius/shell/core/builtin
```

Shell engine registration lives in:

```text
gladius/shell/core/shell_engine.go
```

Rattlesnake command routing lives under:

```text
gladius/rattlesnake/arch
gladius/rattlesnake/core
gladius/rattlesnake/render
```

Auto and integration tests live under:

```text
gladius/test
gladius/test/auto
```

## 3. Command Ownership

Gladius owns shell-side commands that are platform-independent, Mini FS oriented, script friendly, and cheap to implement in Go.

Rattlesnake owns native/system commands that need OS APIs, process/thread inspection, handles, modules, memory maps, network sockets, storage metrics, or filesystem link semantics.

Ulfhedinn owns low-level native manager implementation. Gladius and Rattlesnake should not duplicate Ulfhedinn native API logic.

Ownership table:

```text
shell language / pipeline / redirection       Gladius
text processing                               Gladius
logical path transforms                       Gladius
Mini FS file mutation                         Gladius
controlled external execution                 Gladius
native process / thread / kill                Rattlesnake
native network socket tables                  Rattlesnake
native memory / disk / drive metrics          Rattlesnake
native links / readlink / OS realpath         Rattlesnake
native file status, when routed as system API Rattlesnake
low-level OS API calls                        Ulfhedinn
```

Strategic rule:

```text
Do not add a command to Gladius only because Linux has it.
Add it only when it improves remote shell scripting without dragging native OS semantics into Go.
```

## 4. Direct Builtin Set

The current direct Gladius builtin set is:

```text
pwd
cd
ls
mkdir
stat
cat
touch
tee
rmdir
rm
cp
mv
truncate
peek
env
context
id
whoami
hostname
true
false
date
sleep
echo
:
printf
test
[
grep
wc
head
tail
sort
uniq
cut
tr
sed
diff
find
chmod
patch
basename
dirname
realpath
curl
wget
unzip
zip
tar
which
whereis
exec
xargs
alias
unalias
times
history
help
```

These commands execute inside the Gladius shell engine and use ShellIO for stdout / stderr.

## 5. Special Builtin Set

The current special builtin set is:

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

Special builtins are shell language/runtime controls. They may mutate the current runtime frame, positional parameters, shell variables, options, aliases, or function execution state.

They are not ordinary external commands.

## 6. Rattlesnake Proxy Set

When a Rattlesnake command router is attached, Gladius registers these commands as proxy builtins:

```text
uname
ps
top
pstree
pidstat
threads
threadstat
lsof
handles
modules
pmap
kill
pkill
killall
renice
ln
readlink
realpath
stat
free
df
drives
lsblk
iostat
vmstat
netstat
ss
pgrep
pidof
```

Proxy commands are invoked from Gladius but execute through the Rattlesnake provider chain.

Rattlesnake proxy commands may render human-readable tables through `gladius/rattlesnake/render`, but their underlying provider result is native JSON-capable command output.

## 7. Routing Rule

Command resolution order is:

```text
alias
function
special builtin
direct builtin
Rattlesnake proxy builtin, if registered
controlled external execution, if enabled
unknown command
```

The exact resolver behavior is part of the shell runtime contract. The command surface must preserve this rule:

```text
Rattlesnake proxy commands are Gladius builtins from the user's point of view.
```

This makes these pipelines legal:

```sh
ps | grep Rattle
pgrep Rattlesnake | wc -l
netstat | head -n 5
ss | grep LISTEN
```

## 8. Direct Builtin Categories

### 8.1 Navigation And Files

```text
pwd
cd
ls
mkdir
stat
cat
touch
tee
rmdir
rm
cp
mv
truncate
peek
```

Rules:

```text
1. These commands use ShellPathResolver.
2. File access must go through the mounted logical path system.
3. Commands must call writable checks before mutating mounted paths.
4. Commands must not interpret Windows path grammar as shell syntax.
5. Recursive deletion must be guarded.
6. `rm -rf /` and mount-root deletion must fail.
7. Text output must go through ShellIO.
```

Current minimal contracts:

```text
pwd                         print logical cwd
cd directory                change logical cwd
ls [directory]              list directory contents
mkdir [-p] directory        create directory
stat path                   display file or directory information
cat file                    print file contents
touch [-c] file...          create files or update timestamps
tee [-a] file...            write stdin to stdout and files
rmdir directory...          remove empty directories
rm [-f] [-r] path...        guarded remove
cp [-f] [-r] source dest    copy file or directory
mv [-f] source dest         move or rename
truncate -s size file...    create or resize files
peek options file           preview lines, bytes, tail, or hex
```

### 8.2 Identity And Environment

```text
env
context
id
whoami
hostname
```

Rules:

```text
1. These commands are shell/session-facing helpers.
2. They must not require native OS process inspection.
3. `context` manages interaction context variables.
4. `env` prints exported shell environment variables.
```

### 8.3 POSIX Basics

```text
true
false
date
sleep
echo
:
printf
test
[
```

Current minimal contracts:

```text
true                         exit 0
false                        exit 1
date [-u] [+FORMAT]          print current date/time
sleep DURATION...            delay, respecting context cancellation
echo [-n] [arg...]           print arguments
:                            no-op success
printf format [arg...]       formatted output
test expression              evaluate minimal POSIX test expression
[ expression ]               bracket form of test
```

`date` supports a small strftime-compatible token surface:

```text
%Y %y %m %d %H %M %S %z %Z %a %A %b %B %%
```

`sleep` supports:

```text
bare seconds
fractional seconds
Go-style duration units such as ms, s, m
multiple arguments summed
```

### 8.4 Text And Stream Processing

```text
grep
wc
head
tail
sort
uniq
cut
tr
sed
diff
find
xargs
```

Rules:

```text
1. When FILE is absent, stream commands read stdin.
2. When FILE is present, stream commands read through ShellPathResolver.
3. Commands should be small, predictable, and script friendly.
4. First implementations are buffered and text-oriented.
5. Do not grow these into full GNU command clones without an explicit product reason.
```

Current minimal contracts:

```text
grep [-inv] pattern [file...]                    print matching lines
wc [-lwc] [file...]                              count lines, words, bytes
head [-n count] [file...]                        display first lines
tail [-n count|-c count] [-f] [file...]          display tail lines/bytes or follow file
sort [file...]                                   sort input lines
uniq [file...]                                   filter adjacent duplicate lines
cut -d delim -f list [file...]                   select delimited fields
tr SET1 SET2                                     translate characters
sed [-n] 's/old/new/[gp]' [file...]              minimal substitution
diff [-u] old new                                compare text files
find [path] [-name pattern] [-type f|d] ...      find files
xargs [-r] [-n count] [command [arg...]]         build command lines from stdin
```

`find` currently supports:

```text
-name pattern
-type f|d
-maxdepth n
-mindepth n
```

`sed` is a minimal substitution command, not a full sed interpreter.

`diff` returns a non-zero exit code when differences are found, while still producing useful stdout.

### 8.5 Path Transform

```text
basename
dirname
realpath
which
whereis
```

Rules:

```text
1. `basename` and `dirname` are pure logical path transforms.
2. Direct Gladius `realpath` resolves through ShellPathResolver.
3. Rattlesnake `realpath` resolves native OS paths when routed as a proxy command.
4. `which` reports the first controlled command candidate, not host shell lookup.
5. `whereis` reports all controlled command candidates.
```

### 8.6 Mutation And Patch

```text
chmod
patch
```

Current minimal contracts:

```text
chmod MODE file...        numeric octal mode only
patch [-pN] [file]        minimal unified diff application from stdin
```

Rules:

```text
1. `chmod` is best-effort on Windows and does not implement ACL semantics.
2. `patch` supports text unified diffs.
3. `patch` may apply multiple file hunks, but does not implement binary patches, rename/delete metadata, fuzz, reverse patching, or backup files.
4. Both commands must use ShellPathResolver and writable checks.
```

### 8.7 Network And Archives Through Ulfhedinn

```text
curl
wget
unzip
zip
tar
```

Rules:

```text
1. These are direct Gladius builtins backed by Ulfhedinn utility functions.
2. Path arguments must be rewritten through ShellPathResolver.
3. They are not host shell invocations.
4. They should preserve normal stdout / stderr command behavior.
```

### 8.8 Shell State

```text
alias
unalias
times
history
help
```

Rules:

```text
1. These commands operate on session state.
2. Alias definitions are session-scoped.
3. History is in-memory shell session history.
4. Help is generated from the shell help registry.
```

### 8.9 Controlled External Execution

```text
exec
which
whereis
```

Rules:

```text
1. External execution is enabled only when `ShellConfig.AllowExec` is true.
2. External execution must use argv APIs directly.
3. External execution must not invoke Bash, PowerShell, cmd.exe, or another host shell.
4. External command discovery uses Gladius environment PATH semantics first.
5. System PATH fallback is available only when explicitly enabled.
6. `which` prints the first candidate that would be used.
7. `whereis` prints all known candidates for one or more command names.
```

The optional environment file lives at:

```text
system/setup/environment.json5
```

Minimal schema:

```json5
{
  "version": 1,

  "paths": [
    "D:/ProgramFiles/ToolChains/git_2_4_x64/cmd",
    "D:/ProgramFiles/ToolChains/go_1_26_2/bin"
  ],

  "commands": [
    {
      "name": "python3",
      "path": "D:/ProgramFiles/ToolChains/Python/python38_x64/python.exe"
    }
  ],

  "systemPath": "off",

  "env": {
    "GIT_TERMINAL_PROMPT": "0"
  }
}
```

External lookup order:

```text
1. Existing shell resolution remains first: alias, function, special builtin, direct builtin, Rattlesnake proxy.
2. `commands` exact definitions are checked first inside Gladius environment commands.
3. `paths` are searched in declaration order.
4. System PATH behavior is selected by `systemPath`.
5. `systemPath: "off"` disables host PATH lookup.
6. `systemPath: "fallback"` checks Gladius environment first, then host PATH.
7. `systemPath: "first"` checks host PATH first, then Gladius environment.
```

Windows path candidate rules:

```text
git       -> git.exe, git.cmd, git.bat, git.com, git
git.exe   -> git.exe
git.xxxx  -> git.xxxx
```

POSIX path candidate rules:

```text
git       -> git
git.xxxx  -> git.xxxx
```

Environment merge order for external processes:

```text
host os.Environ()
environment.json5 env
Gladius session env
command assignment overrides
```

## 9. Rattlesnake Categories

### 9.1 Host And Resource

```text
uname
free
df
drives
lsblk
iostat
vmstat
```

These commands expose host identity, memory, filesystem, drive, block-device, disk IO, and VM/resource counters through Rattlesnake native managers.

### 9.2 Process And Thread

```text
ps
top
pstree
pidstat
threads
threadstat
pgrep
pidof
kill
pkill
killall
renice
```

These commands must remain Rattlesnake-owned because they require native process/thread enumeration, process control, priority handling, or signal/termination mapping.

`pgrep`, `pidof`, `pkill`, and `killall` are process matcher commands:

```text
pgrep    pattern process lookup
pidof    exact process name lookup
pkill    pattern process termination
killall  exact process name termination
```

On Windows, signal-style options are mapped through the current Rattlesnake process termination behavior.

### 9.3 Handles, Modules, And Memory Maps

```text
lsof
handles
modules
pmap
```

These commands must remain Rattlesnake-owned. They depend on native handle tables, module enumeration, memory maps, and platform-specific permission behavior.

### 9.4 Network

```text
netstat
ss
```

These commands must remain Rattlesnake-owned. They expose socket tables and owner/process metadata through native OS data sources.

### 9.5 Native Filesystem

```text
ln
readlink
realpath
stat
```

These commands are Rattlesnake-owned when native filesystem semantics matter.

Rules:

```text
1. `ln` and `readlink` must not be reimplemented in Gladius Go code.
2. Windows symlink creation may require privilege or developer-mode policy.
3. Hard links, symbolic links, reparse points, and native realpath behavior belong to the native layer.
4. Gladius direct `realpath` and `stat` exist for logical Mini FS usage; Rattlesnake proxy `realpath` and `stat` exist for native OS usage.
```

## 10. Output Contract

All commands produce a ShellResult:

```text
Ok
Command
Cwd
ExitCode
Stdout
Stderr
Data
ErrorCode
ErrorMessage
```

Output rules:

```text
1. Human command output goes to Stdout.
2. Error messages go to Stderr.
3. ExitCode 0 means success.
4. Non-zero ExitCode means command failure or meaningful command status.
5. ShellResult Data is a structured side channel and must not replace stdout.
6. Pipelines consume stdout, not Data.
7. Commands must not print directly to process stdout/stderr.
8. Commands should include trailing newlines where normal shell output expects them.
```

Rattlesnake proxy rules:

```text
1. Gladius prepares Rattlesnake command args for JSON-capable output.
2. Rattlesnake results may be rendered as tables for human mode.
3. `--json`, `--pretty`, and `--raw` select output modes where supported by the renderer.
4. Provider errors are converted into ShellResult errors.
```

## 11. Exit Code Contract

General rules:

```text
0    success
1    normal command-level false/failure/no-match/difference where applicable
>1   command error, parse error, IO error, or provider error
```

Examples:

```text
false                         exits 1
test false-expression         exits 1
grep no-match                 exits 1
diff different-files          exits 1 with diff output
pgrep no-match                exits 1 through Rattlesnake
rm unsafe target              exits non-zero with stderr
unknown command               exits non-zero
```

## 12. Path Contract

Direct Gladius commands use logical shell paths:

```sh
cd /mnt/work
cat src/main.txt
rm -rf ./tmp
realpath ./file
```

Rattlesnake native filesystem proxy commands operate on native OS paths unless an explicit path-rewrite adapter is added.

Current important boundary:

```text
Gladius direct path commands understand mounted logical paths.
Rattlesnake native file commands understand native OS paths.
```

Future path bridge, if implemented, must be explicit:

```text
1. Detect Rattlesnake filesystem proxy commands.
2. Rewrite mounted logical path args to native paths before provider execution.
3. Preserve non-path options and option values.
4. Avoid rewriting command patterns or arbitrary text values.
5. Test Windows and POSIX path forms.
```

## 13. Test Contract

Command-level tests should cover:

```text
success path
failure path
stdout
stderr
exit code
stdin behavior
pipeline behavior
redirection behavior
logical path resolution
guarded mutation behavior
context cancellation where relevant
```

Core test groups:

```text
gladius/test                         shell command behavior
gladius/test/auto                    topology and integration smoke
gladius/shell/core/builtin           unit-level builtin behavior
gladius/rattlesnake/core             Rattlesnake router/session behavior
gladius/rattlesnake/render           Rattlesnake output rendering behavior
```

Topology smoke must verify:

```text
Gladius ShellEngine
Gladius command router
Rattlesnake JSONL REPL fork pool
Rattlesnake C++ commands
Ulfhedinn native managers
OS API data source
```

Topology smoke must not:

```text
directly invoke Rattlesnake as a separate test target
leave forked Rattlesnake sessions running
leave temp files, temp links, or temp processes behind
skip Rattlesnake silently when the topology under test requires it
```

Current comprehensive smoke entry:

```text
gladius/test/auto/gladius_comprehensive_smoke_test.go
```

## 14. Command Addition Rules

Before adding a command, answer:

```text
1. Is this a shell/script convenience or a native OS capability?
2. Does it require native process/thread/network/storage/filesystem APIs?
3. Does it operate on logical Mini FS paths or native OS paths?
4. Can it be implemented cleanly with ShellIO and ShellPathResolver?
5. Does it need structured JSON output, or only text output?
6. What are the minimal options needed for real scripts?
7. What is the no-match / false / failure exit code?
8. What cleanup is required in tests?
```

Decision rule:

```text
If native OS semantics are the hard part, put it in Rattlesnake.
If shell composition is the hard part, put it in Gladius.
If low-level OS API calls are the hard part, put them in Ulfhedinn.
```

## 15. Red Lines

```text
1. Do not invoke a host shell to implement Gladius commands.
2. Do not add PowerShell or cmd.exe semantics to Gladius command parsing.
3. Do not reimplement `ln` or `readlink` in Gladius Go code.
4. Do not bypass ShellPathResolver for direct Gladius file commands.
5. Do not bypass writable guards for mutation commands.
6. Do not silently add native OS behavior to direct Gladius builtins.
7. Do not turn minimal text commands into GNU clone projects without an explicit product reason.
8. Do not use ShellResult Data as a substitute for stdout.
9. Do not allow topology tests to leak Rattlesnake fork sessions, temp files, temp links, or temp processes.
10. Do not skip native topology tests silently when Rattlesnake is the subject under test.
```

## 16. Current Position

Gladius command capability is currently strong enough for common remote development workflows:

```text
navigation and file basics          usable
file mutation                       usable with guards
text pipelines                      usable
script state commands               usable
date/sleep/chmod/patch              minimal usable
archive/download helpers            usable through Ulfhedinn
controlled external execution       policy-backed
Rattlesnake process/resource bridge usable
Rattlesnake network bridge          usable
Rattlesnake native filesystem bridge usable for native paths
```

Current limitations:

```text
direct commands are intentionally minimal, not full GNU coreutils
chmod is numeric-mode only and best-effort on Windows
sed is substitution-only
patch is minimal unified-diff only
Rattlesnake native filesystem proxy paths are native paths unless rewritten explicitly
Windows symlink creation may fail without required privilege/policy
job control and terminal process groups are not implemented
```

Short-term command strategy:

```text
Stop broad command growth.
Stabilize behavior, tests, path bridging, and failure semantics.
Keep Rattlesnake responsible for native/system surfaces.
Keep Gladius focused on clean shell composition.
```

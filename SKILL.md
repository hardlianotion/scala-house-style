---
name: scala-house-style
description: The user's Scala layout rule (a space before every argument, parameter and type-parameter list, as in `def f [T] (x: Int)`, `List [A]`, `Some (x)`) and the tool that applies it. Use whenever writing or editing Scala code in the user's projects, before reporting the edit done or committing, and when asked to check or fix Scala formatting or style.
---

# Scala house style

The user writes Scala with a space before every argument list, parameter list and type-parameter list:

```scala
case class Node [T] private [hex] (var data: T, children: Array [TreeNode [T]]) extends TreeNode [T]:
  def setData (data: T): Unit = ...

def expectedCount (stats: ProcessState [State], asOf: Float) (at: Float): Float =
  Some (x).map (f).getOrElse (List [A] ())

@SuppressWarnings (Array ("org.wartremover.warts.Var"))
```

That covers definitions, calls, patterns (`case Some (x)`), annotations, `f ()`, `this (...)` and successive lists
(`) (`). Prefix operators keep no space: `-(a + b)`, `~(mask)`, `!(cond)`.

No general formatter holds this style: Scalafmt has no option for definitions or type brackets, and IntelliJ's
`format.sh` breaks Scala 3 files without a loaded project. **Do not run Scalafmt or any other formatter** on the user's
code; it rewrites the rest of the layout too.

## Applying it

```sh
~/.claude/skills/scala-house-style/bin/scala-space [paths...]          # fix, default: the current directory
~/.claude/skills/scala-house-style/bin/scala-space --check [paths...]  # change nothing; non-zero exit if any file would change
```

Write code in the style to begin with, then run `scala-space` on the files you touched (or `src`) before saying the
work is done. It only adds the missing spaces, so its diff is small; read it. It needs no build or compile: the rule
is syntactic (a Scalafix rule, `bin/SpaceBeforeLists.scala`), runs in a few seconds over a large project, and skips
`target/`, `.metals/`, `.bloop/`, `.scala-build/` and `project/project/`.

## Layout it does not touch

Line breaks, indentation and blank lines are kept as written: match the surrounding code by hand. The user's code
uses Scala 3 indented syntax (no braces where optional), two-space indentation, and keeps long lines rather than
wrapping at a fixed column.

## Requirements

`cs` (Coursier) on the path. The script launches Scalafix 0.14.9 on JDK 21 through coursier (`cs launch --jvm 21`),
because Scalafix's command-line tool fails on JDK 27; the first run downloads both.

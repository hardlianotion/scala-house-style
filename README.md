# scala-house-style

A Scala layout rule and the tool that applies it: **a space before every argument list, parameter list and
type-parameter list**.

```scala
case class Node [T] private [hex] (var data: T, children: Array [TreeNode [T]]) extends TreeNode [T]:
  def setData (data: T): Unit = ...

def expectedCount (stats: ProcessState [State], asOf: Float) (at: Float): Float =
  Some (x).map (f).getOrElse (List [A] ())
```

It covers definitions, calls, patterns (`case Some (x)`), annotations, `f ()`, `this (...)` and successive lists
(`) (`). Prefix operators keep no space: `-(a + b)`, `~(mask)`, `!(cond)`.

No general formatter produces this: Scalafmt has no option for definitions or type brackets. `scala-space` adds the
missing spaces and changes nothing else: line breaks, indentation and blank lines stay as written.

## Usage

```sh
bin/scala-space [paths...]           # fix the .scala files under the paths (default: the current directory)
bin/scala-space --check [paths...]   # change nothing; print the diff and exit non-zero (32) if a file would change
```

```console
$ bin/scala-space --check src
--- src/main/scala/A.scala
+++ <expected fix>
-  def f[T](x: Int): List[T] = List[T]()
+  def f [T] (x: Int): List [T] = List [T] ()
$ bin/scala-space src
$ bin/scala-space --check src && echo clean
clean
```

It needs no build and no compile: the rule is syntactic, so it runs on any checkout in a few seconds. It skips
`target/`, `.metals/`, `.bloop/`, `.scala-build/`, `project/project/` and `.git/`. `--check` suits a pre-commit hook
or CI step.

## Requirements

- [Coursier](https://get-coursier.io) (`cs`) on the path. The script launches Scalafix 0.14.9 on JDK 21 through it
  (`cs launch --jvm 21`); the first run downloads both. JDK 21 is pinned because Scalafix's command-line tool fails on
  JDK 27.
- zsh (macOS's default shell) to run the script.

## As a Claude Code skill

`SKILL.md` makes this a [Claude Code skill](https://docs.claude.com/en/docs/claude-code/skills): agents writing Scala
learn the rule, write in it, and run `scala-space` on the files they touched before reporting the work done. Install
it for every project by linking the checkout into your skills folder:

```sh
git clone git@github.com:hardlianotion/scala-house-style.git
ln -s "$PWD/scala-house-style" ~/.claude/skills/scala-house-style
```

A project's `AGENTS.md` or `CLAUDE.md` can then say: after any edit, run `scala-space` on the files touched; never
run Scalafmt.

## How it works

`bin/SpaceBeforeLists.scala` is a Scalafix syntactic rule. It walks the token stream and adds a space before every `(`
or `[` that directly follows an alphanumeric identifier, a closing `)` or `]`, `this` or `super`. Symbolic
identifiers are left alone, which is what keeps prefix operators such as `-(` unspaced. `bin/scala-space` finds the
files and runs the rule with the Scala 3 dialect.

## License

Copyright (C) 2026 Etuka Onono

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
version. It is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See [LICENSE](LICENSE) for the full text.

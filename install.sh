#!/bin/bash
# install.sh: installs the skill as a copy in ~/.claude/skills/scala-house-style (SKILL.md, LICENSE and bin/, with
# scala-space and its rule), so it runs without this checkout, and scala-space in ~/.local/bin. Rerun it after a
# change; it replaces what it installed before, including a link to a checkout.
# macOS bash 3.2.
set -eu

repo="$(cd "$(dirname "$0")" && pwd -P)"
dst="$HOME/.claude/skills/scala-house-style"
bindir="$HOME/.local/bin"

if [ -L "$dst" ]; then rm "$dst"; fi   # the link only, never what it points to
mkdir -p "$dst" "$bindir"
rsync -a --delete --exclude .DS_Store --include SKILL.md --include LICENSE --include 'bin/***' --exclude '*' "$repo/" "$dst/"

# scala-space finds its rule beside itself, so the command runs the installed copy.
out="$bindir/scala-space"
if [ -L "$out" ]; then rm "$out"; fi
printf '#!/bin/sh\n# Installed by scala-house-style/install.sh.\nexec "%s/bin/scala-space" "$@"\n' "$dst" > "$out"
chmod 755 "$out"

echo "installed: the skill in $dst; scala-space in $bindir"

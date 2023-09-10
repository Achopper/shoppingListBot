#!/bin/bash

if [ $# -ne 1 ]; then
  echo "use: $0 <code>"
  exit 1
fi

file="/etc/systemd/system/bot.env"
replacement="$1"

if [ ! -f "$file" ]; then
  echo "Can't open $file"
  exit 1
fi

sed -i "s/CODE_PLACEHOLDER/$replacement/g" "$file"



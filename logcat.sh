#!/bin/sh
PACKAGE=$1
APPPID=`adb -d shell ps | grep "${PACKAGE}" | cut -c10-15 | sed -e 's/ //g'`
adb -d logcat -v long \
 | tr -d '\r' | sed -e '/^\[.*\]/ {N; s/\n/ /}' | grep -v '^$' \
 | grep " ${APPPID}:"

#!/usr/bin/env bash
# Note, we are using "echo 3", but it is not recommended in production instead use "echo 1"
echo 3 > /proc/sys/vm/drop_caches
echo "Cache cleared!"
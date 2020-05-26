#!/usr/bin/env bash

rm -rf target/
mkdir -p target/classes
javac -source 1.8 -target 1.8 -d target/classes -cp 'lib/*' `find src/main/java -name "*.java"`
echo "Build process ended"
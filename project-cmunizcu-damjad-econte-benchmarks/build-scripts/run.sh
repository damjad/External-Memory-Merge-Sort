#!/usr/bin/env bash
java -cp 'src/main/conf/:lib/*:target/classes' com.bdma.dsa.cde.benchmarks.MWaySortBenchmarkMain "$@"
java -cp 'src/main/conf/:lib/*:target/classes' com.bdma.dsa.cde.benchmarks.StreamsBenchmarkMain "$@"
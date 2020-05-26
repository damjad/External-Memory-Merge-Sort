package com.bdma.dsa.cde.benchmarks;

import com.bdma.dsa.cde.Main;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;

public class MWaySortBenchmark
{


    @Benchmark
    public void testMethod() throws IOException
    {
        System.setProperty("log4j.configurationFile","src/main/conf/mway-merge-sort/log4j2.properties");
        Main.main(new String[]{"src/main/conf/mway-merge-sort/test/module-config.properties"});


    }


}

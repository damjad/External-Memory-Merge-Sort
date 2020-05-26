package com.bdma.dsa.cde.benchmarks;

import com.bdma.dsa.cde.streams.InputStream;
import com.bdma.dsa.cde.streams.OutputStream;
import com.bdma.dsa.cde.streams.integer.InputStreamFactory;
import com.bdma.dsa.cde.streams.integer.OutputStreamFactory;
import com.bdma.dsa.cde.utils.PropertiesHandler;
import org.openjdk.jmh.annotations.Benchmark;

import java.io.IOException;
import java.util.PriorityQueue;

public class InMemorySortBenchMark
{
    @Benchmark
    public void testMethod() throws IOException
    {
        System.setProperty("log4j.configurationFile","src/main/conf/mway-merge-sort/log4j2.properties");
        String dataFile = PropertiesHandler.getInstance("src/main/conf/mway-merge-sort/test/module-config.properties").getProperty("data.file");
        InputStream<Integer> inputStream = InputStreamFactory.createInstance();
        OutputStream<Integer> outputStream = OutputStreamFactory.createInstance();

        inputStream.open(dataFile);
        outputStream.create(PropertiesHandler.getInstance().getProperty("mway-merge-sort.sorted-data-file"));

        PriorityQueue<Integer> x = new PriorityQueue<>();

        while (!inputStream.isEndOfStream())
        {
            x.add(inputStream.readNext());
        }

        while (!x.isEmpty())
        {
            outputStream.write(x.poll());
        }
        outputStream.close();
        outputStream.remove();
    }
}

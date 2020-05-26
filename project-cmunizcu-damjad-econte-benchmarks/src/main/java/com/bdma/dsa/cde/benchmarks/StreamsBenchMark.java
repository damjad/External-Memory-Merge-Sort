package com.bdma.dsa.cde.benchmarks;

import com.bdma.dsa.cde.streams.InputStream;
import com.bdma.dsa.cde.streams.OutputStream;
import com.bdma.dsa.cde.streams.integer.InputStreamFactory;
import com.bdma.dsa.cde.streams.integer.OutputStreamFactory;
import com.bdma.dsa.cde.utils.PropertiesHandler;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class StreamsBenchMark
{
    @State(Scope.Benchmark)
    public static class BenchProps
    {
        public static final Random random = new Random();

        @TearDown(Level.Trial)
        public void tearDown() throws IOException
        {
            System.out.println("In setup()");
            new File(PropertiesHandler.getInstance().getProperty("benchmark.streams.temp_data_dir")).delete();
        }

    }


    @Benchmark
    public void aTestOutput(Blackhole blackhole) throws IOException
    {
        OutputStream<Integer> outputStream;
        int n_elements;
        n_elements = PropertiesHandler.getInstance("src/main/conf/streams/test/module-config.properties").getInteger("data.size");
        outputStream = OutputStreamFactory.createInstance();
        outputStream.create(getFileName());

        for (int i = 0; i < n_elements; i++)
        {
            outputStream.write(getRandInt());
        }

        outputStream.close();

        InputStream<Integer> inputStream;

        // Just for the compiler to initialize properties handler.
        blackhole.consume(PropertiesHandler.getInstance("src/main/conf/streams/test/module-config.properties").getInteger("data.size"));

        inputStream = InputStreamFactory.createInstance();
        inputStream.open(getFileName());

        while (!inputStream.isEndOfStream())
        {
            blackhole.consume(inputStream.readNext());
        }

        inputStream.close();
        inputStream.remove();
    }

    private int getRandInt()
    {
        return BenchProps.random.nextInt();
    }

    private String getFileName() throws IOException
    {
        return PropertiesHandler.getInstance().getProperty("benchmark.streams.temp_data_dir") +
                PropertiesHandler.getInstance().getProperty("benchmark.streams.temp_data_prefix") +
                Thread.currentThread().getId() + ".dat";
    }

}

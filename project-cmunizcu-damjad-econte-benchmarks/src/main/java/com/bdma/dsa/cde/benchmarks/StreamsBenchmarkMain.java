package com.bdma.dsa.cde.benchmarks;

import com.bdma.dsa.cde.streams.InputStream;
import com.bdma.dsa.cde.utils.PropertiesHandler;
import com.bdma.dsa.cde.utils.Utils;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.openjdk.jmh.runner.options.WarmupMode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class StreamsBenchmarkMain
{
    // It should be the min of ulimit of OS and 30.
    private static final int MAX_STREAMS = 30;

    public static void main(String[] args) throws RunnerException, IOException
    {

        PropertiesHandler.getInstance("src/main/conf/streams/module-config.properties");

        String[] streamImplementations = PropertiesHandler.getInstance().getProperty("benchmark.streams.implementations").split(",");

        List<String> bufferBasedStreams = new ArrayList<>();
        bufferBasedStreams.add("MEMORY_MAPPED");
        bufferBasedStreams.add("BUFFERED");

        int n_elements_start = PropertiesHandler.getInstance().getInteger("benchmark.streams.n_elements.start");
        int n_elements_end = PropertiesHandler.getInstance().getInteger("benchmark.streams.n_elements.end");
        int n_elements_step = PropertiesHandler.getInstance().getInteger("benchmark.streams.n_elements.step");

        int b_buffers_start = PropertiesHandler.getInstance().getInteger("benchmark.streams.b_buffers.start");
        int b_buffers_end  = PropertiesHandler.getInstance().getInteger("benchmark.streams.b_buffers.end");
        int b_buffers_step = PropertiesHandler.getInstance().getInteger("benchmark.streams.b_buffers.step");


        int k_streams_start = PropertiesHandler.getInstance().getInteger("benchmark.streams.k_streams.start");
        int k_streams_end = PropertiesHandler.getInstance().getInteger("benchmark.streams.k_streams.end");
        int k_streams_step = PropertiesHandler.getInstance().getInteger("benchmark.streams.k_streams.step");

        for (String streamImpl : streamImplementations)
        {
            for (int n = n_elements_start; n <= n_elements_end; n = n + n_elements_step)
            {
                int b = bufferBasedStreams.contains(streamImpl)? b_buffers_start : b_buffers_end;
                for (; b <= b_buffers_end; b = b + b_buffers_step)
                {
                    for (int k = k_streams_start; k <= MAX_STREAMS && k <= k_streams_end; k = k + k_streams_step)
                    {
                        String propsFile = "src/main/conf/streams/test/module-config.properties";
                        
                        createPropertyFile(streamImpl ,n, b, k, propsFile);
                        runBenchmark(streamImpl, n, b, k);
                    }
                }
            }
        }

    }

    private static void runBenchmark(String streamImpl, int n, int b, int k) throws RunnerException, IOException
    {
        Options opts = new OptionsBuilder()
                .include(".*.StreamsBenchMark.*")
                .shouldFailOnError(true)
                .forks(PropertiesHandler.getInstance().getInteger("benchmark.streams.jmh.forks"))
                .threads(k) // open k parallel streams
                .warmupIterations(PropertiesHandler.getInstance().getInteger("benchmark.streams.jmh.warmup.iterations"))
                .warmupMode(WarmupMode.INDI)
                .measurementIterations(PropertiesHandler.getInstance().getInteger("benchmark.streams.jmh.measurement.iterations"))
                .mode(Mode.SingleShotTime)
                .timeout(TimeValue.days(12l))
                .timeUnit(TimeUnit.SECONDS)
                //.output(PropertiesHandler.getInstance().getProperty("benchmark.streams.jmh.results-dir")+"/streamsLogs_"+streamImpl+"_"+n +"_"+b+"_"+k+".csv")
                .result(PropertiesHandler.getInstance().getProperty("benchmark.streams.jmh.results-dir")+"/streamsResults_"+streamImpl+"_"+n +"_"+b+"_"+k+".csv")
                .resultFormat(ResultFormatType.CSV)
                .build();
        new Runner(opts).run();
    }

    private static void createPropertyFile(String streamImpl, int n_elements, int b_buffers, int k_streams, String propsFile) throws IOException
    {
        Properties props = (Properties) PropertiesHandler.getInstance().getModuleProperties().clone();
        props.setProperty("in.stream.type", streamImpl + "_INPUT_STREAM");
        props.setProperty("out.stream.type", streamImpl + "_OUTPUT_STREAM");

        props.setProperty("stream.buffer.size", String.valueOf(b_buffers));
        props.setProperty("stream.file.size", String.valueOf(b_buffers));

        props.setProperty("data.size", String.valueOf(n_elements));
        props.store(Files.newOutputStream(new File(propsFile).toPath()), String.format("N_ELEMENTS: %s\n\r" +
                "b_buffers: %s\n\r" +
                "k_streams: %s\n\r" +
                "Note: It is a system generated file",n_elements, b_buffers, k_streams));

    }

    private static InputStream<Integer> createDataFile(int n_elements, String filePath) throws IOException
    {
        return Utils.createInputDataFileInts(n_elements, new PriorityQueue<>(),filePath);
    }
}

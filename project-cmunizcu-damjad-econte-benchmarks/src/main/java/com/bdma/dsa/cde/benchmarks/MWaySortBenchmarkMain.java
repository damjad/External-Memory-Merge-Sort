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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class MWaySortBenchmarkMain
{
    public static void main(String[] args) throws RunnerException, IOException
    {

        PropertiesHandler.getInstance(args.length> 0? args[0]:"src/main/conf/mway-merge-sort/module-config.properties");

        int n_elements_start = PropertiesHandler.getInstance().getInteger("benchmark.mway-merge-sort.n_elements.start");
        int n_elements_end = PropertiesHandler.getInstance().getInteger("benchmark.mway-merge-sort.n_elements.end");
        int n_elements_step = PropertiesHandler.getInstance().getInteger("benchmark.mway-merge-sort.n_elements.step");
        int n_iters = ceil(Double.valueOf(n_elements_end - n_elements_start + 1)/Double.valueOf(n_elements_step));

        int m_buffers_start = PropertiesHandler.getInstance().getInteger("benchmark.mway-merge-sort.m_buffers.start");
        int m_buffers_end  = PropertiesHandler.getInstance().getInteger("benchmark.mway-merge-sort.m_buffers.end");
        int m_buffers_step = PropertiesHandler.getInstance().getInteger("benchmark.mway-merge-sort.m_buffers.step");
        int m_iters = ceil(Double.valueOf(m_buffers_end - m_buffers_start + 1)/Double.valueOf(m_buffers_step));


        int d_streams_start = PropertiesHandler.getInstance().getInteger("benchmark.mway-merge-sort.d_streams.start");
        int d_streams_end = PropertiesHandler.getInstance().getInteger("benchmark.mway-merge-sort.d_streams.end");
        int d_streams_step = PropertiesHandler.getInstance().getInteger("benchmark.mway-merge-sort.d_streams.step");
        int d_iters = (int) Math.ceil(Double.valueOf(d_streams_end - d_streams_start + 1)/Double.valueOf(d_streams_step));


        double total_iterations = 0;

        double iterations_completed = 0;


        //System.exit(0);
        for (int x = n_elements_start; x <= n_elements_end; x += n_elements_step)
        {
            int i = pow2(x) - 1;
            for (int j = pow2(m_buffers_start); j <= pow2(i) && j <= pow2(m_buffers_end); j = j * pow2(m_buffers_step))
            {
                for (int k = pow2(d_streams_start); k <= j && k<= ceil(pow2(x)/j) && k <= pow2(d_streams_end); k = k * pow2(d_streams_step))
                {
                    ++total_iterations;
                }
            }

        }

        System.out.println("Number of iterations are :" + total_iterations);
        //System.exit(0);

        for (int x = n_elements_start; x <= n_elements_end; x += n_elements_step)
        {
            int i = pow2(x) - 1;
            String inputFile = PropertiesHandler.getInstance().getProperty("benchmark.mway-merge-sort.data-file-prefix") + i;

            PropertiesHandler.getInstance().getModuleProperties().setProperty("stream.file.size", String.valueOf(i));
            InputStream<Integer> dataFile = createDataFile(i,inputFile);
            dataFile.close();

            //runMemoryBasedBenchmark(i);

            for (int j = pow2(m_buffers_start); j <= pow2(i) && j <= pow2(m_buffers_end); j = j * pow2(m_buffers_step))
            {
                for (int k = pow2(d_streams_start); k <= j && k<= ceil(pow2(x)/j) && k <= pow2(d_streams_end); k = k * pow2(d_streams_step))
                {
                    System.out.println("N = " + i);
                    System.out.println("M = " + j);
                    System.out.println("d = " + k);

                    String propsFile = "src/main/conf/mway-merge-sort/test/module-config.properties";

                    createPropertyFile(i, j, k, inputFile, propsFile);
                    runBenchmark(i, j, k);

                    ++iterations_completed;
                    System.out.format("Progress: %s (%s/%s)\n\r", iterations_completed/total_iterations * 100, iterations_completed, total_iterations );
                    System.out.println("--------------------------------------");


                }
            }

            dataFile.remove();

        }

        System.out.println("Progress: 100");
    }

    private static  int ceil(double i)
    {
        return (int) Math.ceil(i);
    }
    private static int pow2(int i)
    {
        return (int) Math.pow(2, i);
    }
    private static void runBenchmark(int i, int j, int k) throws RunnerException, IOException
    {
        excuteCommand("/mnt/1717A37A1971CE02/WorkSpaces/BDMA/database-system-architecture/project-cmunizcu-damjad-econte/project-cmunizcu-damjad-econte-benchmarks/build-scripts/extra.sh");

        Options opts = new OptionsBuilder()
                .include(".*.MWaySortBenchmark.*")
                .shouldFailOnError(false)
                .forks(PropertiesHandler.getInstance().getInteger("benchmark.mway-merge-sort.jmh.forks"))
                .warmupIterations(PropertiesHandler.getInstance().getInteger("benchmark.mway-merge-sort.jmh.warmup.iterations"))
                .warmupMode(WarmupMode.INDI)
                .measurementIterations(PropertiesHandler.getInstance().getInteger("benchmark.mway-merge-sort.jmh.measurement.iterations"))
                .mode(Mode.SingleShotTime)
                .timeout(TimeValue.days(12l))
                .threads(1)
                .timeUnit(TimeUnit.SECONDS)
                //.output(Pcom.bdma.dsa.cde.utils.Utils.createInputDataFileIntsropertiesHandler.getInstance().getProperty("benchmark.mway-merge-sort.jmh.results-dir")+"/logs_"+i +"_"+j+"_"+k+".csv")
                .result(PropertiesHandler.getInstance().getProperty("benchmark.mway-merge-sort.jmh.results-dir")+"/results_"+i +"_"+j+"_"+k+".csv")
                .resultFormat(ResultFormatType.CSV)
                .shouldDoGC(true)
                //.jvmArgsAppend("-Xms"+ (int)Math.max(i * 20.0d / 1024.0d/1024.0d, 1024) +"m", "-Xmx2048m")
                .build();
        new Runner(opts).run();
    }

    private static void createPropertyFile(int n_elements, int m_buffers, int d_buffers, String inputFile, String propsFile) throws IOException
    {
        Properties props = (Properties) PropertiesHandler.getInstance().getModuleProperties().clone();
        props.setProperty("mway-merge-sort.memory_available",String.valueOf(m_buffers));
        props.setProperty("mway-merge-sort.parallel_streams", String.valueOf(d_buffers));
        props.setProperty("data.file", inputFile);
        props.setProperty("stream.file.size", String.valueOf(n_elements));
        props.store(Files.newOutputStream(new File(propsFile).toPath()), String.format("N_ELEMENTS: %s\n\r" +
                "M_BUFFERS: %s\n\r" +
                "D_STREAMS: %s\n\r" +
                "Note: It is a system generated file",n_elements, m_buffers, d_buffers));

    }

    public static InputStream<Integer> createDataFile(int n_elements, String filePath) throws IOException
    {
        return Utils.createInputDataFileInts(n_elements,filePath);
    }

    public static void excuteCommand(String filePath) throws IOException{
        File file = new File(filePath);
        Process proc = null;
        if(!file.isFile()){
            throw new IllegalArgumentException("The file " + filePath + " does not exist");
        }
        if(isLinux()){
            proc = Runtime.getRuntime().exec(new String[] {"/bin/sh", "-c", filePath}, null);
        }else if(isWindows()){
            proc = Runtime.getRuntime().exec("cmd /c start " + filePath);
        }

        BufferedReader read = new BufferedReader(new InputStreamReader(
                proc.getInputStream()));
        try {
            proc.waitFor();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        while (read.ready()) {
            System.out.println(read.readLine());
        }
    }
    public static boolean isLinux(){
        String os = System.getProperty("os.name");
        return os.toLowerCase().indexOf("linux") >= 0;
    }

    public static boolean isWindows(){
        String os = System.getProperty("os.name");
        return os.toLowerCase().indexOf("windows") >= 0;
    }
}

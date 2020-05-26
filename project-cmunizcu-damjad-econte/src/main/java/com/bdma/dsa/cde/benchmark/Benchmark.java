package com.bdma.dsa.cde.benchmark;

import com.bdma.dsa.cde.benchmark.threads.ReadThread;
import com.bdma.dsa.cde.benchmark.threads.WriteThread;
import com.bdma.dsa.cde.utils.Constants;
import com.bdma.dsa.cde.utils.PropertiesHandler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by edoardo on 25/11/2018.
 */
public class Benchmark {

    private PropertiesHandler propertiesHandler;
    private int numValues, maxStreams;
    private String baseFile, outFile;

    public Benchmark(String configFile) {
        try {
            this.propertiesHandler = PropertiesHandler.getInstance(configFile);
        }
        catch (IOException e) {
            System.err.println("Please enter a valid path of a file or check file permissions. Error details: " + e.getMessage());
            System.exit(Constants.UNSUCCESSFUL_EXIT_CODE);
        }
    }

    public void run() {
        if (!checkAndSetParams()) {
            throw new IllegalArgumentException("Missing parameters in the property file.");
        }

        int numStreams = 0;
        for (; numStreams < 10; numStreams++) {
            runBenchmark(numStreams);
        }
    }

    /**
     * Opening numStreams concurrent streams to write and read at the same time
     * Evaluating the execution time
     * @param numStreams number of streams to open concurrently
     */
    private void runBenchmark(int numStreams) {
        List<WriteThread> wtList;
        List<ReadThread> rtList;
        Constants.OutputStreamTypes outType;
        Constants.InputStreamTypes inType;

        // Running benchmarks for the four implementations
        // 1
        outType = Constants.OutputStreamTypes.SIMPLE_OUTPUT_STREAM;
        inType = Constants.InputStreamTypes.SIMPLE_INPUT_STREAM;
        runBenchmarkForImplementation(numStreams, inType, outType, 1);

    }

    private void runBenchmarkForImplementation(
                                            int numStreams,
                                            Constants.InputStreamTypes inType,
                                            Constants.OutputStreamTypes outType,
                                            int implementationID
                                            ) {
        List<WriteThread> wtList = new LinkedList<>();
        List<ReadThread> rtList = new LinkedList<>();
        final long startTime = System.nanoTime();
        // Starting execution
        for (int i=0; i < numStreams; i++) {
            WriteThread wt = new WriteThread(this.baseFile, this.numValues, outType);
            wt.start();
            wtList.add(wt);
        }
        for (WriteThread wt : wtList) {
            try {
                wt.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(Constants.UNSUCCESSFUL_EXIT_CODE);
            }
            ReadThread rt = new ReadThread(wt.getFilePath(), inType);
            rt.start();
            rtList.add(rt);
        }

        for (ReadThread rt : rtList) {
            try {
                rt.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(Constants.UNSUCCESSFUL_EXIT_CODE);
            }
        }
        final long endTime = System.nanoTime();
        writeExecTime(endTime-startTime);
    }

    private void writeExecTime (long execTime) {
        // Writing the execution time on the file
    }

    private boolean checkAndSetParams() {
        if (this.propertiesHandler.getProperty(Constants.MAX_STREAMS) == null)
            return false;
        if (this.propertiesHandler.getProperty(Constants.NUMBER_VALUES) == null)
            return false;
        if (this.propertiesHandler.getProperty(Constants.BASE_FILE) == null)
            return false;
        if (this.propertiesHandler.getProperty(Constants.OUT_FILE) == null)
            return false;

        this.maxStreams = Integer.parseInt(this.propertiesHandler.getProperty(Constants.MAX_STREAMS));
        this.numValues = Integer.parseInt(this.propertiesHandler.getProperty(Constants.NUMBER_VALUES));
        this.baseFile = this.propertiesHandler.getProperty(Constants.BASE_FILE);
        this.outFile = this.propertiesHandler.getProperty(Constants.OUT_FILE);

        return true;

    }
}

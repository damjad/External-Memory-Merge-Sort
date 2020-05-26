package com.bdma.dsa.cde.sorting;

import com.bdma.dsa.cde.streams.InputStream;
import com.bdma.dsa.cde.streams.OutputStream;
import com.bdma.dsa.cde.streams.integer.InputStreamFactory;
import com.bdma.dsa.cde.streams.integer.OutputStreamFactory;
import com.bdma.dsa.cde.utils.PropertiesHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class MultiwayMergeSortInt
{
    private static final Logger LOGGER = LogManager.getLogger(MultiwayMergeSortInt.class);

    private static File tempDir;

    public static void sort(InputStream<Integer> inputStream, int nParallelStreams, int availableMemoryBlocks, String outputFilePath) throws IOException
    {
        int i;
        createTempDir();

        LinkedList<String> inputStreamQueue = splitInputStream(inputStream, availableMemoryBlocks);


        int index = 0;
        List<InputStream<Integer>> intermediateInputList = new ArrayList<>(nParallelStreams);//at most size d

        // merge sorted streams until there remains only one.
        while (inputStreamQueue.size() > 1)
        {
            ++index;

            //loading d or the whole queue in an intermediate list to use dway_sort, remove from queue
            for (i = 0; i < nParallelStreams && !inputStreamQueue.isEmpty(); ++i)
            {
                InputStream<Integer> is = InputStreamFactory.createInstance();
                is.open(inputStreamQueue.poll());
                intermediateInputList.add(is);
            }

            //creating intermediate output stream to save the result of dway algorithm
            OutputStream<Integer> intermediateOutput = OutputStreamFactory.createInstance();
            intermediateOutput.create(tempDir.getName() + File.separator + "inter_" + index + ".txt");

            //sorting
            DWayMergeSort.sort(intermediateInputList, intermediateOutput);

            //closing every conection to intermediate input files
            for (InputStream<Integer> inter : intermediateInputList)
            {
                inter.close();
                inter.remove();
            }

            //closing the output stream of the dway sort
            intermediateOutput.close();

            intermediateInputList.clear();

            //open the sorted file as an inputStream and save it at the end of the queue
            //InputStream<Integer> intermediateInput = InputStreamFactory.createInstance();
            //intermediateInput.open(intermediateOutput.getFilePath());
            //inputStreamQueue.offer(intermediateInput);
            inputStreamQueue.offer(intermediateOutput.getFilePath());
        }

        //InputStream<Integer> in = inputStreamQueue.poll();
        //in.close();
        String filePath = inputStreamQueue.poll();
        try
        {
            File afile = new File(filePath);

            if (afile.renameTo(new File(outputFilePath)))
            {
                LOGGER.info("File moved successful!");
            }
            else
            {
                LOGGER.info("File failed to move! " + afile.getPath());
            }

        }
        catch (Exception e)
        {
            LOGGER.error("",e);
        }

        tempDir.delete();
    }

    /**
     * splits input file into d sorted streams using a priority queue.
     *
     * @param inputStream
     * @param availableMemoryBlocks
     */
    public static LinkedList<String> splitInputStream(InputStream<Integer> inputStream, int availableMemoryBlocks) throws IOException
    {
        Integer element;
        int memory = 0;
        int index = 0;
        PriorityQueue<Integer> queue = new PriorityQueue<>(availableMemoryBlocks);
        //LinkedList<InputStream<Integer>> inputStreamList = new LinkedList<>();
        LinkedList<String> inputStreamList = new LinkedList<>();


        while (!inputStream.isEndOfStream())
        {
            element = inputStream.readNext();
            queue.add(element);
            memory += 1;
            if (memory >= availableMemoryBlocks)
            {
                inputStreamList.add(createSortedTempFile(index, queue));

                memory = 0;
                index++;
            }
        }
        if (!queue.isEmpty())
        {

            inputStreamList.add(createSortedTempFile(index, queue));

        }

        return inputStreamList;
    }

    private static String createSortedTempFile(int index, PriorityQueue<Integer> queue) throws IOException
    {
        OutputStream<Integer> tempOStream = OutputStreamFactory.createInstance();
        //InputStream<Integer> tempIStream = InputStreamFactory.createInstance();

        tempOStream.create(tempDir.getName() + File.separator + "file_" + index + ".txt");//this filePath needs to be improve
        while (!queue.isEmpty())
            tempOStream.write(queue.poll());

        tempOStream.close();
        //tempIStream.open(tempOStream.getFilePath());

        return tempOStream.getFilePath();
    }

    private static void createTempDir() throws IOException
    {
        tempDir = new File(PropertiesHandler.getInstance().getProperty("temp.dir"));
        // if the directory does not exist, create it
        if (!tempDir.exists())
        {
            //instead of using System.out LOGGER should be use, Im not sure how!!!
            LOGGER.info("creating directory: " + tempDir.getName());
            boolean result = false;

            try
            {
                tempDir.mkdir();
                result = true;
            }
            catch (SecurityException se)
            {
                LOGGER.error("Error creating the folder");
            }
            if (result)
            {
                LOGGER.error("DIR created");
            }
        }
    }

    private MultiwayMergeSortInt()
    {
        //
    }

}

package com.bdma.dsa.cde.sorting;

import com.bdma.dsa.cde.beans.Block;
import com.bdma.dsa.cde.streams.InputStream;
import com.bdma.dsa.cde.streams.OutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class DWayMergeSort
{
    private static final Logger LOGGER = LogManager.getLogger(DWayMergeSort.class);

    /**
     * sorts d sorted streams.
     * @param streamList
     * @param outputStream
     * @param <T>
     * @throws IOException
     */
    public static <T extends Comparable<T>> void sort(List<InputStream<T>> streamList, OutputStream<T> outputStream) throws IOException
    {
        // first time read each stream.
        List<InputStream<T>> readAbleStreamList;
        PriorityQueue<Block<T>> queue;

        readAbleStreamList = streamList.stream().filter(DWayMergeSort::canReadStream).collect(Collectors.toList());

        if (readAbleStreamList.isEmpty())
        {
            return;
        }


        queue = new PriorityQueue<>(readAbleStreamList.size());
        readAbleStreamList.stream().
                forEach(g -> queue.add(formNextBlock(g)));

        // read the minimum stream until it's finished.
        do
        {
            Block<T> e = queue.poll();
            if (null != e)
            {
                outputStream.write(e.getData());
                if (canReadStream(e.getStreamReference()))
                {
                    e.setData(e.getStreamReference().readNext());
                    queue.add(e);
                }
                else{
                    readAbleStreamList.remove(e.getStreamReference());
                }
            }

        }
        while (!readAbleStreamList.isEmpty());

        while (!queue.isEmpty())
        {
            outputStream.write(queue.poll().getData());
        }

    }

    private static <T extends Comparable<T>> boolean canReadStream(InputStream<T> stream)
    {
        try
        {
            return !stream.isEndOfStream();
        }
        catch (IOException e)
        {
            LOGGER.error("An error has occurred while reading through a stream" + stream, e);
            throw new RuntimeException(e);
        }
    }

    private static <T extends Comparable<T>> Block<T> formNextBlock(InputStream<T> stream)
    {
        try
        {
            return new Block<>(stream.readNext(), stream);
        }
        catch (IOException e)
        {
            LOGGER.error("An error has occurred while reading through a stream" + stream, e);
            throw new RuntimeException(e);
        }
    }

    private DWayMergeSort()
    {
        // useless
    }
}

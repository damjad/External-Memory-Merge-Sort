package com.bdma.dsa.cde.utils;

import com.bdma.dsa.cde.streams.InputStream;
import com.bdma.dsa.cde.streams.OutputStream;
import com.bdma.dsa.cde.streams.integer.InputStreamFactory;
import com.bdma.dsa.cde.streams.integer.OutputStreamFactory;

import java.io.IOException;
import java.util.Queue;
import java.util.Random;

public class Utils
{
    public static InputStream<Integer> createInputDataFileInts(int n_elements, Queue<Integer> priorityQueue, String filePath) throws IOException
    {
        InputStream<Integer> integerInputStream = InputStreamFactory.createInstance();
        OutputStream<Integer> outputStream = OutputStreamFactory.createInstance();
        outputStream.create(filePath);


        Random random = new Random();

        for (int i = 0; i < n_elements; i++)
        {
            int e = random.nextInt(1000);

            priorityQueue.add(e);
            outputStream.write(e);
        }

        outputStream.close();
        integerInputStream.open(outputStream.getFilePath());

        return integerInputStream;
    }

    public static InputStream<Integer> createInputDataFileInts(int n_elements, String filePath) throws IOException
    {
        InputStream<Integer> integerInputStream = InputStreamFactory.createInstance();
        OutputStream<Integer> outputStream = OutputStreamFactory.createInstance();
        outputStream.create(filePath);


        Random random = new Random();

        for (int i = 0; i < n_elements; i++)
        {
            int e = random.nextInt(1000);
            outputStream.write(e);
        }

        outputStream.close();
        integerInputStream.open(outputStream.getFilePath());

        return integerInputStream;
    }
}

package com.bdma.dsa.cde.sorting;

import com.bdma.dsa.cde.streams.InputStream;
import com.bdma.dsa.cde.streams.integer.InputStreamFactory;
import com.bdma.dsa.cde.utils.Utils;
import com.bdma.dsa.cde.utils.PropertiesHandler;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Queue;

public class MultiwayMergeSortIntTest
{
    @BeforeClass
    public static void init() throws IOException
    {
        PropertiesHandler.getInstance("src/test/conf/module-config.properties");

    }

    @Test
    public void bigInputFileTest() throws IOException
    {

//        InputStream<Integer> in = InputStreamFactory.createInstance();
//
//        try {
//            in.open("data/data.txt");
//        }
//        catch (NullPointerException exc){
//            System.out.println("Null pointer exception");
//            return;
//        }
//
//        int M = 5000, d = 10;
//        MultiwayMergeSortInt.sort(in, d, M);
//        in.close();
//
//        in = InputStreamFactory.createInstance();
//        in.open("data/sorted_data.txt");
//        int p = in.readNext();
//        int q;
//        // Probably not the right way to do it.
//        while(!in.isEndOfStream()){
//            q = in.readNext();
//            Assert.assertTrue("The algorithm is not correct "+p+" is greater than "+q,p < q);
//        }
//        in.close();

    }

    @Test
    public void testSanity() throws IOException
    {
        InputStream<Integer> integerInputStream;
        int n_elements = Integer.parseInt(PropertiesHandler.getInstance().getProperty("multiway-merge-sort.n_elements"));
        String sortedOutputFilePath = PropertiesHandler.getInstance().getProperty("multiway-merge-sort.sanity-test.sorted-file");

        Queue<Integer> priorityQueue = new PriorityQueue<>(n_elements);

        integerInputStream = Utils.createInputDataFileInts(n_elements, priorityQueue, PropertiesHandler.getInstance().getProperty("multiway-merge-sort.sanity-test.generated-file"));

        int M = 5, d = 5;
        MultiwayMergeSortInt.sort(integerInputStream, d, M, sortedOutputFilePath);

        InputStream<Integer> in;
        in = InputStreamFactory.createInstance();
        in.open(sortedOutputFilePath);

        while(!in.isEndOfStream()){
            Assert.assertEquals("Check if the data sorted by priority queue is equal to data sorted by M-way merge sort", priorityQueue.remove(), in.readNext());
        }
        in.close();
        in.remove();
        integerInputStream.close();
        integerInputStream.remove();
    }
}

package com.bdma.dsa.cde.sorting;

import com.bdma.dsa.cde.streams.InputStream;
import com.bdma.dsa.cde.streams.OutputStream;
import com.bdma.dsa.cde.streams.TestInputStream;
import com.bdma.dsa.cde.streams.TestOutStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DWayMergeSortTest
{
    @Test
    public void oneStreamZeroElementsTest() throws IOException
    {
        InputStream<Integer> is = new TestInputStream();
        OutputStream<Integer> os = new TestOutStream();
        
        List<InputStream<Integer>> lst = new ArrayList<>();
        lst.add(is);

        DWayMergeSort.sort(lst, os);

        Assert.assertTrue("Check list OS is empty", ((TestOutStream) os).getElements().isEmpty());
    }

    @Test
    public void oneStreamOneElementsTest() throws IOException
    {
        InputStream<Integer> is = new TestInputStream();
        OutputStream<Integer> os = new TestOutStream();

        ((TestInputStream) is).addElements(0);

        List<InputStream<Integer>> lst = new ArrayList<>();
        lst.add(is);

        DWayMergeSort.sort(lst, os);

        Assert.assertFalse("Check list OS is not empty", ((TestOutStream) os).getElements().isEmpty());
        Assert.assertEquals("Check the contents of OS", Integer.valueOf(0), ((TestOutStream) os).getElements().get(0));
    }

    @Test
    public void oneStreamTwoElementsTest() throws IOException
    {
        InputStream<Integer> is = new TestInputStream();
        OutputStream<Integer> os = new TestOutStream();

        ((TestInputStream) is).addElements(0);
        ((TestInputStream) is).addElements(1);

        List<InputStream<Integer>> lst = new ArrayList<>();
        lst.add(is);

        DWayMergeSort.sort(lst, os);

        Assert.assertFalse("Check list OS is not empty", ((TestOutStream) os).getElements().isEmpty());
        Assert.assertEquals("Check the contents of OS", Integer.valueOf(0), ((TestOutStream) os).getElements().get(0));
        Assert.assertEquals("Check the contents of OS", Integer.valueOf(1), ((TestOutStream) os).getElements().get(1));
    }

    @Test
    public void oneStreamThreeElementsTest() throws IOException
    {
        InputStream<Integer> is = new TestInputStream();
        OutputStream<Integer> os = new TestOutStream();

        ((TestInputStream) is).addElements(0);
        ((TestInputStream) is).addElements(1);
        ((TestInputStream) is).addElements(2);

        List<InputStream<Integer>> lst = new ArrayList<>();
        lst.add(is);

        DWayMergeSort.sort(lst, os);

        Assert.assertFalse("Check list OS is not empty", ((TestOutStream) os).getElements().isEmpty());
        Assert.assertEquals("Check the contents of OS", Integer.valueOf(0), ((TestOutStream) os).getElements().get(0));
        Assert.assertEquals("Check the contents of OS", Integer.valueOf(1), ((TestOutStream) os).getElements().get(1));
    }

    @Test
    public void twoStreamThreeElementsTest() throws IOException
    {
        OutputStream<Integer> os = new TestOutStream();
        InputStream<Integer> is = new TestInputStream();
        List<InputStream<Integer>> lst = new ArrayList<>();

        ((TestInputStream) is).addElements(0);
        ((TestInputStream) is).addElements(2);
        ((TestInputStream) is).addElements(4);

        lst.add(is);

        is = new TestInputStream();

        ((TestInputStream) is).addElements(1);
        ((TestInputStream) is).addElements(2);
        ((TestInputStream) is).addElements(10);

        lst.add(is);

        DWayMergeSort.sort(lst, os);

        Assert.assertFalse("Check list OS is not empty", ((TestOutStream) os).getElements().isEmpty());
        Assert.assertEquals("Check the contents of OS", Integer.valueOf(0), ((TestOutStream) os).getElements().get(0));
        Assert.assertEquals("Check the contents of OS", Integer.valueOf(1), ((TestOutStream) os).getElements().get(1));
        Assert.assertEquals("Check the contents of OS", Integer.valueOf(2), ((TestOutStream) os).getElements().get(2));
        Assert.assertEquals("Check the contents of OS", Integer.valueOf(2), ((TestOutStream) os).getElements().get(3));
        Assert.assertEquals("Check the contents of OS", Integer.valueOf(4), ((TestOutStream) os).getElements().get(4));
    }
}
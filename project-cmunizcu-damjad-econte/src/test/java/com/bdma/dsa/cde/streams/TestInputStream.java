package com.bdma.dsa.cde.streams;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.PriorityQueue;

public class TestInputStream implements InputStream<Integer>
{

    private PriorityQueue<Integer> elements = new PriorityQueue<>();

    public TestInputStream addElements(Integer... e)
    {
        elements.addAll(Arrays.asList(e));
        return this;
    }

    @Override
    public void open(String filePath) throws IOException
    {
        //
    }

    @Override
    public void open(File file) throws IOException
    {
        //
    }

    @Override
    public Integer readNext() throws IOException
    {
        return elements.poll();
    }

    @Override
    public boolean isEndOfStream() throws IOException
    {
        return elements.isEmpty();
    }

    @Override
    public void close() throws IOException
    {
        elements.clear();
    }
    @Override
    public void remove()throws  IOException{

    }

    @Override
    public String getFilePath(){
        return " ";
    }
}
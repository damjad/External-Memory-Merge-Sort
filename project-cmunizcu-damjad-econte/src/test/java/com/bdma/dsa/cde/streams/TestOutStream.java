package com.bdma.dsa.cde.streams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * For testing Purposes.
 */
public class TestOutStream implements OutputStream<Integer>
{
    private List<Integer> elements = new ArrayList<>();

    public List<Integer> getElements()
    {
        return elements;
    }

    @Override
    public void create(String filePath) throws IOException
    {
        // Do Nothing
    }

    @Override
    public void write(Integer element) throws IOException
    {
        elements.add(element);
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

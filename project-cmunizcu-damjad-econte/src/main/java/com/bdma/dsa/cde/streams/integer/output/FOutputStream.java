package com.bdma.dsa.cde.streams.integer.output;

import com.bdma.dsa.cde.streams.OutputStream;

import java.io.*;
import java.io.BufferedOutputStream;

/**
 * Created by edoardo on 20/11/2018.
 */
public class FOutputStream implements OutputStream<Integer>
{
    private FileOutputStream os;
    private BufferedOutputStream bos;
    private DataOutputStream ds;
    private File file;
    private int bufferSize;

    public FOutputStream(int bufferSize) {
        this.bufferSize = bufferSize;
    }


    @Override
    public void create(String filePath) throws IOException {
        this.file = new File(filePath);
        this.os = new FileOutputStream(this.file);
        this.bos = new BufferedOutputStream(os, this.bufferSize*4);
        this.ds = new DataOutputStream(bos);
    }

    @Override
    public void close() throws IOException {
        this.ds.close();
    }

    @Override
    public void write(Integer element) throws IOException {
        this.ds.writeInt(element);
    }

    @Override
    public String getFilePath() {
        return this.file.getPath();
    }

    @Override
    public void remove() throws IOException {
        if (!this.file.delete()) {
            throw new IOException("File not deleted. Check permissions.");
        }
    }
}

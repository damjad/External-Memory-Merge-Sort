package com.bdma.dsa.cde.streams.integer.input;

import com.bdma.dsa.cde.streams.InputStream;

import java.io.*;
import java.io.BufferedInputStream;

public class FInputStream implements InputStream<Integer>
{
    private FileInputStream is;
    private BufferedInputStream bis;
    private DataInputStream ds;
    private boolean isBufferFull;
    private int intBuffer;
    private File file;
    private int bufferSize;

    public FInputStream(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Override
    public void open(String filePath) throws IOException {
        this.open(new File(filePath));
    }

    @Override
    public void open(File file) throws IOException {
        this.file = file;
        this.is = new FileInputStream(file);
        this.bis = new BufferedInputStream(this.is, this.bufferSize*4);
        this.ds = new DataInputStream(this.bis);
        this.isBufferFull = false;
    }

    @Override
    public void close() throws IOException {
        this.ds.close();
    }

    @Override
    public Integer readNext() throws IOException {
        if (this.isBufferFull) {
            this.isBufferFull = false;
            return this.intBuffer;
        }
        return this.ds.readInt();
    }

    @Override
    public boolean isEndOfStream() throws IOException {
        if (this.isBufferFull) {
            return false;
        }
        try {
            this.intBuffer = this.ds.readInt();
        }
        catch (EOFException e) {
            return true;
        }
        this.isBufferFull = true;
        return false;
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

package com.bdma.dsa.cde.streams.integer.input;

import java.io.*;


public class SimpleInputStream implements com.bdma.dsa.cde.streams.InputStream<Integer>
{
    private java.io.InputStream is;
    private DataInputStream ds;
    private int intBuffer;
    private boolean isBufferFull;
    private File file;

    @Override
    public void open(String filePath) throws IOException
    {
        //closeBeforeOpen();
        this.open(new File(filePath));
    }

    @Override
    public void close() throws IOException {
        this.ds.close();
    }

    @Override
    public void open(File file) throws IOException
    {
        //closeBeforeOpen();
        this.file = file;
        this.is = new FileInputStream(file);
        this.ds = new DataInputStream(this.is);
        this.isBufferFull = false;
    }

    private void closeBeforeOpen() throws IOException {
        if (this.is != null) {
            this.is.close();
        }
    }

    @Override
    public Integer readNext() throws IOException
    {
        if (this.isBufferFull) {
            this.isBufferFull = false;
            return this.intBuffer;
        }
        return this.ds.readInt();
    }

    @Override
    public boolean isEndOfStream() throws IOException
    {
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

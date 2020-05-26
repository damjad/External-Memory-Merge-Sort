package com.bdma.dsa.cde.streams.integer.input;

import com.bdma.dsa.cde.streams.InputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class BufferedInputStream implements InputStream<Integer>
{
    private int bufferSize;
    private ByteBuffer buffer;
    private int bufferIndex, valuesRead;
    private java.io.InputStream is;
    private File file;

    public BufferedInputStream(int bufferSize) {
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
        //this.ds = new DataInputStream(this.is);
        this.buffer = ByteBuffer.allocate(this.bufferSize*4);
        this.bufferIndex = this.bufferSize;
        this.valuesRead = this.bufferSize;
    }

    @Override
    public Integer readNext() throws IOException {
        if (this.bufferIndex == this.valuesRead) {  // Empty buffer
            this.isEndOfStream();
            this.bufferIndex = 1;
            return this.buffer.getInt(0);
        }
        else {
            this.bufferIndex++;
            return this.buffer.getInt((this.bufferIndex-1)*4);
        }
    }

    @Override
    public boolean isEndOfStream() throws IOException {
        if (this.bufferIndex == this.valuesRead) {
            int bytesRead = this.is.read(this.buffer.array(), 0, this.bufferSize*4);
            this.valuesRead = bytesRead/4;
            if (bytesRead == -1) {
                return true;
            }
            this.bufferIndex = 0;
        }
        return false;
    }

    @Override
    public void close() throws IOException {
        this.is.close();
        this.bufferIndex = this.bufferSize;
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

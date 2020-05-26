package com.bdma.dsa.cde.streams.integer.output;

import com.bdma.dsa.cde.streams.OutputStream;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * Created by edoardo on 20/11/2018.
 */
public class BufferedOutputStream  implements OutputStream<Integer> {
    private int bufferSize;
    private ByteBuffer buffer;
    private int bufferIndex;
    private java.io.OutputStream os;
    private File file;
    //private DataOutputStream ds;

    public BufferedOutputStream(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Override
    public void close() throws IOException {
        // Flush the buffer
        this.os.write(this.buffer.array(), 0, this.bufferIndex*4);
        this.bufferIndex = 0;
        this.os.close();
    }

    @Override
    public void create(String filePath) throws IOException {
        this.file = new File(filePath);
        this.os = new FileOutputStream(this.file);
        //this.ds = new DataOutputStream(this.os);
        // Initialize the buffer
        this.bufferIndex = 0;
        this.buffer = ByteBuffer.allocate(bufferSize*4);
    }

    @Override
    public void write(Integer element) throws IOException {
        if (this.bufferIndex < this.bufferSize) {
            this.buffer.putInt(this.bufferIndex*4, element);
            this.bufferIndex++;
        }
        else {
            this.os.write(buffer.array());
            this.buffer.putInt(0, element);
            this.bufferIndex = 1;
        }
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

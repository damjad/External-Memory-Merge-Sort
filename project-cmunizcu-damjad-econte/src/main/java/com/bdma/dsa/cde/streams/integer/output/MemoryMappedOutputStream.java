package com.bdma.dsa.cde.streams.integer.output;

import com.bdma.dsa.cde.streams.OutputStream;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by edoardo on 20/11/2018.
 */
public class MemoryMappedOutputStream implements OutputStream<Integer>
{

    private RandomAccessFile file;
    private MappedByteBuffer buffer;
    private long fileLengthInt;
    private long fileLength;
    private File inputFile;

    public MemoryMappedOutputStream(long fileLengthInt) {
        this.fileLengthInt = fileLengthInt;
        this.fileLength = fileLengthInt * 4; // Byte length
    }

    @Override
    public void close() throws IOException {
        this.file.setLength(this.buffer.position());
        this.file.close();
    }

    @Override
    public void create(String filePath) throws IOException {
        inputFile = new File(filePath);
        this.file = new RandomAccessFile(inputFile, "rw");
        this.buffer = file.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, this.fileLength);
    }

    @Override
    public void write(Integer element) throws IOException {
        if (buffer.position() == buffer.limit()) {
            // Extend the file
            this.file.setLength(2*this.file.length());
            int position = this.buffer.position();
            this.buffer = file.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, this.file.length());
            this.buffer.position(position);
        }
        this.buffer.putInt(element);
    }

    @Override
    public String getFilePath() {
        return this.inputFile.getPath();
    }

    @Override
    public void remove() throws IOException {
        if (!this.inputFile.delete()) {
            throw new IOException("File not deleted. Check permissions.");
        }
    }
}

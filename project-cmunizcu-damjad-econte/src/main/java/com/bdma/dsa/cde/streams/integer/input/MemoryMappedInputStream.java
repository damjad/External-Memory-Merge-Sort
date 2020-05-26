package com.bdma.dsa.cde.streams.integer.input;

import com.bdma.dsa.cde.streams.InputStream;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MemoryMappedInputStream implements InputStream<Integer>
{

    private FileChannel fileChannel;
    private MappedByteBuffer buffer;
    private File file;

    @Override
    public void close() throws IOException {
        this.fileChannel.close();
    }

    @Override
    public void open(String filePath) throws IOException {
        this.open(new File(filePath));
    }

    @Override
    public void open(File file) throws IOException {
        this.file = file;
        this.fileChannel = new RandomAccessFile(file, "r").getChannel();
        this.buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, this.fileChannel.size());
    }

    @Override
    public Integer readNext() throws IOException {
        if (this.isEndOfStream())
            throw new EOFException("EOF reached before expected.");
        return buffer.getInt();
    }

    @Override
    public boolean isEndOfStream() throws IOException {
        return this.buffer.capacity() == this.buffer.position();
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

package com.bdma.dsa.cde.streams.integer.output;

import com.bdma.dsa.cde.streams.OutputStream;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by edoardo on 20/11/2018.
 */
public class SimpleOutputStream implements OutputStream<Integer>
{
    private java.io.OutputStream os;
    private DataOutputStream ds;
    private File file;

    @Override
    public void create(String filePath) throws IOException{
        if (this.os != null) {
            this.os.close();
        }
        this.file = new File(filePath);
        this.os = new FileOutputStream(this.file);
        this.ds = new DataOutputStream(os);
    }

    @Override
    public void close() throws IOException {
        this.ds.close();
    }

    @Override
    public void write(Integer element) throws IOException{
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

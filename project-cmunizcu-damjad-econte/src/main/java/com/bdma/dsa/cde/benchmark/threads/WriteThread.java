package com.bdma.dsa.cde.benchmark.threads;

import com.bdma.dsa.cde.streams.OutputStream;
import com.bdma.dsa.cde.streams.integer.OutputStreamFactory;
import com.bdma.dsa.cde.utils.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by edoardo on 25/11/2018.
 */
public class WriteThread extends Thread {

    private String filePath;
    private int numElements;
    private Constants.OutputStreamTypes streamType;
    private Map<String, Object> param;


    public WriteThread(String filePath, int numElements, Constants.OutputStreamTypes type) {
        this(filePath, numElements, type, new HashMap<>());
    }

    public WriteThread(String filePath, int numElements, Constants.OutputStreamTypes type, Map<String, Object> param) {
        this.filePath = filePath.concat(String.valueOf(this.getId()));
        this.numElements = numElements;
        this.streamType = type;
        this.param = param;
    }

    @Override
    public void run() {
        OutputStream<Integer> os = OutputStreamFactory.createInstance(this.streamType);
        if (os == null) {
            throw new Error("Error in creating the stream");
        }

        try {
            os.create(this.filePath);
            for (int i=0; i < this.numElements; i++) {
                os.write(i);
            }
            os.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFilePath() {
        return filePath;
    }
}

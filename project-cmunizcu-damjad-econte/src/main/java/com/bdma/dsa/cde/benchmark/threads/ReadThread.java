package com.bdma.dsa.cde.benchmark.threads;

import com.bdma.dsa.cde.streams.InputStream;
import com.bdma.dsa.cde.streams.integer.InputStreamFactory;
import com.bdma.dsa.cde.utils.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by edoardo on 25/11/2018.
 */
public class ReadThread extends Thread {

    private String filePath;
    private Constants.InputStreamTypes streamType;
    private Map<String, Object> param;

    public ReadThread(String filePath, Constants.InputStreamTypes streamType) {
        this(filePath, streamType, new HashMap<>());
    }
    public ReadThread(String filePath, Constants.InputStreamTypes streamType, Map<String, Object> param) {
        this.filePath = filePath;
        this.streamType = streamType;
        this.param = param;
    }

    @Override
    public void run() {
        InputStream<Integer> is = InputStreamFactory.createInstance(this.streamType, (this.param));
        try {
            is.open(this.filePath);
            while (!is.isEndOfStream()) {
                is.readNext();
            }
            is.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

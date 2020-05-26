package com.bdma.dsa.cde.streams.integer;

import com.bdma.dsa.cde.streams.OutputStream;
import com.bdma.dsa.cde.streams.integer.output.BufferedOutputStream;
import com.bdma.dsa.cde.streams.integer.output.FOutputStream;
import com.bdma.dsa.cde.streams.integer.output.MemoryMappedOutputStream;
import com.bdma.dsa.cde.streams.integer.output.SimpleOutputStream;
import com.bdma.dsa.cde.utils.Constants;
import com.bdma.dsa.cde.utils.PropertiesHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by edoardo on 20/11/2018.
 */
public abstract class OutputStreamFactory {

    public static OutputStream<Integer> createInstance(Constants.OutputStreamTypes type, Map<String, Object> param) {
        switch(type) {
            case SIMPLE_OUTPUT_STREAM:
                return new SimpleOutputStream();
            case F_OUTPUT_STREAM:
                if (!param.containsKey(Constants.BUFFER_SIZE) ||
                        !(param.get(Constants.BUFFER_SIZE) instanceof Integer))
                    throw new IllegalArgumentException("BUFFER_SIZE -> <integerValue> required");
                return new FOutputStream((int)param.get(Constants.BUFFER_SIZE));
            case BUFFERED_OUTPUT_STREAM:
                if (!param.containsKey(Constants.BUFFER_SIZE) ||
                        !(param.get(Constants.BUFFER_SIZE) instanceof Integer))
                    throw new IllegalArgumentException("BUFFER_SIZE -> <integerValue> required");
                return new BufferedOutputStream((int)param.get(Constants.BUFFER_SIZE));
            case MEMORY_MAPPED_OUTPUT_STREAM:
                if (!param.containsKey(Constants.INIT_FILE_SIZE) ||
                        !(param.get(Constants.INIT_FILE_SIZE) instanceof Integer))
                    throw new IllegalArgumentException("INIT_FILE_SIZE -> <integerValue> required");
                return new MemoryMappedOutputStream((int)param.get(Constants.INIT_FILE_SIZE));
            default:
                return null;
        }
    }

    public static OutputStream<Integer> createInstance(Constants.OutputStreamTypes type) {
        Map<String, Object> param = new HashMap<>();
        if (type.equals(Constants.OutputStreamTypes.BUFFERED_OUTPUT_STREAM) ||
                type.equals(Constants.OutputStreamTypes.F_OUTPUT_STREAM)) {
            if (getBufferSize() == null)
                return null;
            param.put(Constants.BUFFER_SIZE, getBufferSize());
        }
        else if (type.equals(Constants.OutputStreamTypes.MEMORY_MAPPED_OUTPUT_STREAM)) {
            if (getInitFileSize() == null)
                return null;
            param.put(Constants.INIT_FILE_SIZE, getInitFileSize());
        }
        return createInstance(type, param);
    }

    public static OutputStream<Integer> createInstance () throws IOException {
        Constants.OutputStreamTypes type;

        String stringType = PropertiesHandler.getInstance().getProperty(Constants.OUT_STREAM_TYPE);
        if (stringType == null) {
            return null;
        }
        switch (stringType) {
            case "SIMPLE_OUTPUT_STREAM":
                type = Constants.OutputStreamTypes.SIMPLE_OUTPUT_STREAM;
                break;
            case "F_OUTPUT_STREAM":
                type = Constants.OutputStreamTypes.F_OUTPUT_STREAM;
                break;
            case "BUFFERED_OUTPUT_STREAM":
                type = Constants.OutputStreamTypes.BUFFERED_OUTPUT_STREAM;
                break;
            case "MEMORY_MAPPED_OUTPUT_STREAM":
                type = Constants.OutputStreamTypes.MEMORY_MAPPED_OUTPUT_STREAM;
                break;
            default:
                return null;
        }

        return createInstance(type);
    }


    private static Integer getBufferSize() {
        String stringBufferSize;
        try {
            stringBufferSize = PropertiesHandler.getInstance().getProperty(Constants.BUFFER_SIZE);
        }
        catch (IOException e) {
            stringBufferSize = null;
        }
        if (stringBufferSize != null) {
            return Integer.parseInt(stringBufferSize);
        }
        return null;
    }

    private static Integer getInitFileSize() {
        String stringInitFileSize;
        try {
            stringInitFileSize = PropertiesHandler.getInstance().getProperty(Constants.INIT_FILE_SIZE);
        } catch (IOException e) {
            stringInitFileSize = null;
        }
        if (stringInitFileSize != null) {
            return Integer.parseInt(stringInitFileSize);
        }
        return null;
    }

}

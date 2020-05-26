package com.bdma.dsa.cde.streams.integer;

import com.bdma.dsa.cde.streams.InputStream;
import com.bdma.dsa.cde.streams.integer.input.BufferedInputStream;
import com.bdma.dsa.cde.streams.integer.input.FInputStream;
import com.bdma.dsa.cde.streams.integer.input.MemoryMappedInputStream;
import com.bdma.dsa.cde.streams.integer.input.SimpleInputStream;
import com.bdma.dsa.cde.utils.Constants;
import com.bdma.dsa.cde.utils.PropertiesHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by edoardo on 20/11/2018.
 */
public abstract class InputStreamFactory {

    public static InputStream<Integer> createInstance (Constants.InputStreamTypes type, Map<String, Object> param) {
        switch (type) {
            case SIMPLE_INPUT_STREAM:
                return new SimpleInputStream();
            case F_INPUT_STREAM:
                if (!param.containsKey(Constants.BUFFER_SIZE) ||
                        !(param.get(Constants.BUFFER_SIZE) instanceof Integer))
                    throw new IllegalArgumentException("BUFFER_SIZE -> <integerValue> required");
                return new FInputStream((int)param.get(Constants.BUFFER_SIZE));
            case MEMORY_MAPPED_INPUT_STREAM:
                return new MemoryMappedInputStream();
            case BUFFERED_INPUT_STREAM:
                if (!param.containsKey(Constants.BUFFER_SIZE) ||
                        !(param.get(Constants.BUFFER_SIZE) instanceof Integer))
                    throw new IllegalArgumentException("BUFFER_SIZE -> <integerValue> required");
                return new BufferedInputStream((int)param.get(Constants.BUFFER_SIZE));
            default:
                return null;
        }
    }

    public static InputStream<Integer> createInstance (Constants.InputStreamTypes type) {
        Map<String, Object> param = new HashMap<>();
        if (type.equals(Constants.InputStreamTypes.BUFFERED_INPUT_STREAM) ||
                type.equals(Constants.InputStreamTypes.F_INPUT_STREAM)) {
            if (getBufferSize() == null)
                return null;
            param.put(Constants.BUFFER_SIZE, getBufferSize());
        }
        return createInstance(type, param);
    }

    public static InputStream<Integer> createInstance () throws IOException {
        //Why do I need to pass propertiesHandler as parameter if it is a Singleton and just one!!!!
        Constants.InputStreamTypes type;

        String stringType = PropertiesHandler.getInstance().getProperty(Constants.IN_STREAM_TYPE);
        if (stringType == null) {
            return null;
        }
        switch (stringType) {
            case "SIMPLE_INPUT_STREAM":
                type = Constants.InputStreamTypes.SIMPLE_INPUT_STREAM;
                break;
            case "F_INPUT_STREAM":
                type = Constants.InputStreamTypes.F_INPUT_STREAM;
                break;
            case "BUFFERED_INPUT_STREAM":
                type = Constants.InputStreamTypes.BUFFERED_INPUT_STREAM;
                break;
            case "MEMORY_MAPPED_INPUT_STREAM":
                type = Constants.InputStreamTypes.MEMORY_MAPPED_INPUT_STREAM;
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
        } catch (IOException e) {
            stringBufferSize = null;
        }
        if (stringBufferSize != null) {
            return Integer.parseInt(stringBufferSize);
        }
        return null;
    }
}

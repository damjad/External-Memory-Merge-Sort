package com.bdma.dsa.cde.utils;

public class Constants
{
    public static final int UNSUCCESSFUL_EXIT_CODE = 1;
    public static final String BUFFER_SIZE = "stream.buffer.size";
    public static final String INIT_FILE_SIZE = "stream.file.size";
    public static final String MAX_STREAMS = "stream.max-number";
    public static final String NUMBER_VALUES = "values.max-number";
    public static final String BASE_FILE = "file.base";
    public static final String OUT_FILE = "file.out";
    public static final String IN_STREAM_TYPE = "in.stream.type";
    public static final String OUT_STREAM_TYPE = "out.stream.type";

    public static final String M_WAY_MEMORY_AVAILABLE = "mway-merge-sort.memory_available";
    public static final String M_WAY_PARLLEL_STREAMS = "mway-merge-sort.parallel_streams";
    public static final String M_WAY_OUTPUT_FILE = "mway-merge-sort.sorted-data-file";

    public enum InputStreamTypes {
        SIMPLE_INPUT_STREAM, F_INPUT_STREAM, BUFFERED_INPUT_STREAM, MEMORY_MAPPED_INPUT_STREAM
    }
    public enum OutputStreamTypes {
        SIMPLE_OUTPUT_STREAM, F_OUTPUT_STREAM, BUFFERED_OUTPUT_STREAM, MEMORY_MAPPED_OUTPUT_STREAM
    }



    private Constants() throws IllegalAccessException
    {
        throw new IllegalAccessException("Illegal access to Constants()");
    }
}

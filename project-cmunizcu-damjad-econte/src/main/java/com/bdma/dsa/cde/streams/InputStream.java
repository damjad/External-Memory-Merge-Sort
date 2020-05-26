package com.bdma.dsa.cde.streams;

import java.io.File;
import java.io.IOException;

public interface InputStream<T extends Comparable<T>> extends Cloneable
{
    /**
     * Open an existing file for reading
     * @param filePath
     * @return
     */
    void open(String filePath) throws IOException;

    /**
     * open an existing file for reading
     * @param file
     * @return
     */
    void open(File file) throws IOException;

    /**
     * read the next element from the stream
     * @return
     */
    T readNext() throws IOException;

    /**
     * @return true if the end of stream has been reached
     */
    boolean isEndOfStream() throws IOException;

    /**
     * Simple getter for the file path associated to the stream
     * @return the file path
     */
    String getFilePath();

    /**
     * method to close the stream
     * @throws IOException
     */
    void close() throws IOException;

    /**
     * Remove the file associated to the stream
     * @throws IOException
     */
    void remove() throws IOException;
}

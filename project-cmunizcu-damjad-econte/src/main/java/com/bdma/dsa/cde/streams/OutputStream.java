package com.bdma.dsa.cde.streams;


import java.io.IOException;

public interface OutputStream<T extends Comparable<T>> extends Cloneable
{
    /**
     * Create a new file.
     * @param filePath
     * @return
     */
    void create(String filePath) throws IOException;

    /**
     * Write an element to the stream.
     * @param element
     * @return
     */
    void write(T element) throws IOException;

    /**
     * Gets the file path associated to the stream
     * @return the file path
     */
    String getFilePath();

    /**
     * Method to close the stream
     * @throws IOException
     */
    void close() throws IOException;

    /**
     * Remove the file associated to the stream
     * @throws IOException
     */
    void remove() throws IOException;
}

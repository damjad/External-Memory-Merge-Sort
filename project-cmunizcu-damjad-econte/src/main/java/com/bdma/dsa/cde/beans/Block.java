package com.bdma.dsa.cde.beans;

import com.bdma.dsa.cde.streams.InputStream;

import java.util.Objects;

public class Block<T extends Comparable<T>> implements Comparable<Block<T>>
{
    T data;
    InputStream<T> streamReference;

    public Block(T data, InputStream<T> streamReference)
    {
        this.data = data;
        this.streamReference = streamReference;
    }

    public T getData()
    {
        return data;
    }

    public InputStream<T> getStreamReference()
    {
        return streamReference;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    @Override
    public int compareTo(Block<T> o)
    {
        return this.getData().compareTo(o.getData());
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block<?> block = (Block<?>) o;
        return Objects.equals(data, block.data) &&
                Objects.equals(streamReference, block.streamReference);
    }
}

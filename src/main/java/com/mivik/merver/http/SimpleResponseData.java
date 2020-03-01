package com.mivik.merver.http;

import java.io.IOException;
import java.io.OutputStream;

public class SimpleResponseData implements ResponseData {
    private byte[] data;
    public int off,len;

    public SimpleResponseData(byte[] data) {
        this(data, 0, data.length);
    }

    public SimpleResponseData(byte[] data, int off, int len) {
        if (off+len>data.length) throw new ArrayIndexOutOfBoundsException();
        this.data = data;
        this.off=off;
        this.len=len;
    }

    @Override
    public int getContentLength() {
        return len;
    }

    @Override
    public void write(OutputStream out) throws IOException {
        out.write(data, off, len);
    }
}
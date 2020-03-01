package com.mivik.merver.http;

import java.io.IOException;
import java.io.OutputStream;

public interface ResponseData {
    int getContentLength();

    void write(OutputStream out) throws IOException;
}
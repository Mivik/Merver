package com.mivik.merver.config;

public class HttpConfig extends Config {
	public int maxContentLength = 4096;
	public int defaultBufferSize = 2048;

	@Override
	public void set(Config src) {
		super.set(src);
		if (!(src instanceof HttpConfig)) return;
		HttpConfig t = (HttpConfig) src;
		maxContentLength = t.maxContentLength;
		defaultBufferSize = t.defaultBufferSize;
	}
}
package com.mivik.merver;

public class Config {
	int maxContentLength = 4096;
	int defaultBufferSize = 2048;
	boolean verbose = false;

	public Config() {
	}

	public Config(Config src) {
		maxContentLength = src.maxContentLength;
	}

	public Config setVerbose(boolean verbose) {
		this.verbose = verbose;
		return this;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public Config setDefaultBufferSize(int defaultBufferSize) {
		this.defaultBufferSize = defaultBufferSize;
		return this;
	}

	public int getDefaultBufferSize() {
		return defaultBufferSize;
	}

	public Config setContentLength(int length) {
		this.maxContentLength = length;
		return this;
	}

	public int getMaxContentLength() {
		return maxContentLength;
	}
}
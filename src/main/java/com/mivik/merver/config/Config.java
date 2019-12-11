package com.mivik.merver.config;

public class Config {
	public boolean verbose = false;
	public boolean useBuffer = true;

	public Config() {
	}

	public Config(Config t) {
		set(t);
	}

	public void set(Config t) {
		verbose = t.verbose;
		useBuffer = t.useBuffer;
	}
}
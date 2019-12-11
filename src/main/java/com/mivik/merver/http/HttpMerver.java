package com.mivik.merver.http;

import com.mivik.merver.MLog;
import com.mivik.merver.Merver;
import com.mivik.merver.config.HttpConfig;

import java.io.InputStream;
import java.io.OutputStream;

public abstract class HttpMerver extends Merver<HttpConfig> {
	@Override
	public final void process(InputStream in, OutputStream out) {
		try {
			Request req = new Request(in, config);
			Response res = new Response();
			process(req, res);
			res.writeTo(out);
		} catch (Throwable t) {
			MLog.e("Failed to process request", t);
		}
	}

	protected abstract void process(Request req, Response res);

	@Override
	protected HttpConfig createConfig() {
		return new HttpConfig();
	}
}
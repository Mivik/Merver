package com.mivik.merver.http;

import com.mivik.merver.MLog;
import com.mivik.merver.Merver;
import com.mivik.merver.config.HttpConfig;

import java.io.InputStream;
import java.io.OutputStream;

public abstract class HttpMerver extends Merver<HttpConfig> {
	@Override
	public final void process(InputStream in, OutputStream out) {
		Request req = null;
		try {
			req = new Request(in, config);
		} catch (Throwable t) {
			MLog.e("Failed to process request", t);
		}
		Response res = new Response();
		process(req, res);
		try {
			res.writeTo(out);
		} catch (Throwable t) {
			MLog.e("Failed to write response", t);
		}
	}

	protected abstract void process(Request req, Response res);

	@Override
	protected HttpConfig createConfig() {
		return new HttpConfig();
	}
}
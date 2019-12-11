package com.mivik.merver.http;

public class RawHttpMerver extends HttpMerver {
	@Override
	public void process(Request req, Response res) {
		System.out.println("=======HTTP BEGIN=======");
		System.out.println(req);
		System.out.println("========HTTP END========");
		res.setResponseCode(200);
		res.setData("");
	}
}
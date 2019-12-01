package com.mivik.merver;

public class SimpleMerver extends Merver {
	@Override
	public void process(Request req, Response res) {
		System.out.println("=======REQUEST BEGIN=======");
		System.out.println(req);
		System.out.println("========REQUEST END========");
		res.setResponseCode(200);
		res.setData("");
	}
}
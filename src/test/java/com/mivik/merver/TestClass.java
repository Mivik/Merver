package com.mivik.merver;

import com.mivik.merver.http.RawHttpMerver;

public class TestClass {

	public static void main(String[] args) throws Throwable {
		Merver merver = new RawHttpMerver();
		merver.getConfig().verbose = true;
		merver.listen(1926);
	}
}
package com.mivik.merver;

public class TestClass {

	public static void main(String[] args) throws Throwable {
		Merver merver = new SimpleMerver();
		merver.getConfig().setVerbose(true);
		merver.listen(1926);
	}
}
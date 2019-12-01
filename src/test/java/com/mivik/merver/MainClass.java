package com.mivik.merver;

import com.mivik.merver.Merver;
import com.mivik.merver.SimpleMerver;

import java.util.Scanner;

public class MainClass {
	private static Merver merver;

	public static void main(String[] args) throws Throwable {
		merver = new SimpleMerver();
		merver.listen(1926);
	}
}
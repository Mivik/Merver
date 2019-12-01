package com.mivik.merver;

import com.mivik.merver.Merver;
import com.mivik.merver.SimpleMerver;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Scanner;

public class MainClass {
	private static Merver merver;

	public static void main(String[] args) throws Throwable {
		merver = new SimpleMerver();
		merver.getConfig().setVerbose(true);
		merver.listen(1926);
	}
}
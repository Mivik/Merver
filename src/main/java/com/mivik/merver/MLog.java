package com.mivik.merver;

import java.io.PrintStream;

public class MLog {
	public static final String TAG = "Merver";

	private MLog() {
	}

	private static void print(PrintStream writer, String des, String msg, Throwable thr) {
		writer.print('[');
		writer.print(des);
		writer.print("] ");
		writer.print(msg);
		if (thr != null) {
			writer.println();
			thr.printStackTrace(writer);
		}
		writer.println();
	}

	public static final void v(String msg) {
		v(msg, null);
	}

	public static final void v(String msg, Throwable thr) {
		print(System.out, "VERBOSE", msg, thr);
	}

	public static final void i(String msg) {
		i(msg, null);
	}

	public static final void i(String msg, Throwable thr) {
		print(System.out, "INFO", msg, thr);
	}

	public static final void w(String msg) {
		w(msg, null);
	}

	public static final void w(String msg, Throwable thr) {
		print(System.out, "WARNING", msg, thr);
	}

	public static final void e(String msg) {
		e(msg, null);
	}

	public static final void e(String msg, Throwable thr) {
		print(System.err, "ERROR", msg, thr);
	}

	public static final void f(String msg) {
		f(msg, null);
	}

	public static final void f(String msg, Throwable thr) {
		print(System.err, "FATAL", msg, thr);
	}
}
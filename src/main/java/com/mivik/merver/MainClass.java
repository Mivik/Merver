package com.mivik.merver;

import java.io.File;

public class MainClass {
	public static void main(String[] args) {
		File file = null;
		int port = 1926;
		for (int i = 0; i < args.length; i++) {
			if (args.equals("--port") || args.equals("-p")) {
				if (i == args.length - 1) throw new IllegalArgumentException(args[i]);
				port = Integer.parseInt(args[++i]);
			} else {
				file = new File(args[0]);
				if (!file.exists()) {
					System.err.println("File \"" + args[0] + "\" does not exist");
					System.exit(1);
					return;
				}
				if (!file.isDirectory()) {
					System.err.println("Expected a direcotry");
					System.exit(1);
					return;
				}
			}
		}
		FileMerver merver = new FileMerver(file);
		merver.getConfig().setVerbose(true);
		merver.listen(port);
	}
}
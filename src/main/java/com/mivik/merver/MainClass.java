package com.mivik.merver;

import java.io.File;

public class MainClass {
	private static Merver merver = null;

	private static void reportErr(String msg) {
		System.err.println(msg);
		System.exit(1);
			}

			private static void checkNull() {
				if (merver == null) return;
				reportErr("Only one merver can be specified");
			}

			public static void main(String[] args) {
				int port = 1926;
				for (int i = 0; i < args.length; i++) {
					String arg = args[i];
					if (arg.equals("--port") || arg.equals("-p")) {
						if (i == args.length - 1) reportErr("Expected a number: " + arg);
						port = Integer.parseInt(args[++i]);
					} else if (arg.equals("--dir") || arg.equals("-d")) {
						checkNull();
						if (i == args.length - 1) reportErr("Expected a directory: " + arg);
						arg = args[++i];
						File file = new File(arg);
						if (!file.exists()) reportErr("File \"" + arg + "\" does not exist");
						if (!file.isDirectory()) reportErr("Expected a direcotry");
				merver = new FileMerver(file);
			} else if (arg.equals("--simple") || arg.equals("-s")) {
				checkNull();
				merver = new SimpleMerver();
			} else reportErr("Unrecognized option: " + arg);
		}
		if (merver == null) reportErr("Merver not specified");
		merver.getConfig().setVerbose(true);
		merver.listen(port);
	}
}
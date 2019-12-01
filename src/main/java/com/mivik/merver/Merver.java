package com.mivik.merver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class Merver implements Closeable {
	private ServerSocket socket;
	private boolean running;
	protected Config config;

	public Merver() {
		config = new Config();
	}

	public Merver(Config config) {
		this.config = config;
	}

	public boolean isRunning() {
		return running;
	}

	public void setConfig(Config config) {
		if (config == null) throw new IllegalArgumentException("Config cannot be null");
		this.config = config;
	}

	public Config getConfig() {
		return config;
	}

	public void listen(final int port) {
		synchronized (this) {
			if (running) return;
			running = true;
		}
		try {
			socket = new ServerSocket(port);
			if (config.verbose) MLog.v("Created server at " + port);
			while (running) {
				Socket con = null;
				try {
					con = socket.accept();
					InputStream in = new BufferedInputStream(con.getInputStream());
					OutputStream out = new BufferedOutputStream(con.getOutputStream());
					Response res = new Response();
					process(new Request(in, config), res);
					res.writeTo(out);
					in.close();
					out.close();
					con.close();
				} catch (Throwable t) {
					MLog.e("Failed to process request from client", t);
				} finally {
					if (con != null) con.close();
				}
			}
		} catch (IOException e) {
			MLog.e("Server Error", e);
			running = false;
		}
	}

	public void stop() {
		if (!running) return;
		running = false;
		try {
			socket.close();
		} catch (IOException e) {
			MLog.e("Failed to close socket", e);
		}
	}

	public abstract void process(Request req, Response res);

	@Override
	public void close() throws IOException {
		if (!running) return;
		running = false;
		if (socket != null) socket.close();
	}
}
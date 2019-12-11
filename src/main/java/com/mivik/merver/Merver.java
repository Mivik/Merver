package com.mivik.merver;

import com.mivik.merver.config.Config;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class Merver<T extends Config> implements Closeable {
	private ServerSocket socket;
	private boolean running;
	protected T config;

	public Merver() {
		config = createConfig();
	}

	public Merver(T config) {
		this.config = config;
	}

	public boolean isRunning() {
		return running;
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
				try (final Socket con = socket.accept()) {
					new Thread() {
						@Override
						public void run() {
							try {
								InputStream in = con.getInputStream();
								OutputStream out = con.getOutputStream();
								if (config.useBuffer) {
									in = new BufferedInputStream(in);
									out = new BufferedOutputStream(out);
								}
								process(in, out);
								in.close();
								out.close();
							} catch (Throwable t) {
								MLog.e("Failed to process", t);
							}
						}
					}.start();
				} catch (Throwable t) {
					MLog.e("Failed to process", t);
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

	public abstract void process(InputStream in, OutputStream out);

	protected abstract T createConfig();

	@Override
	public void close() throws IOException {
		if (!running) return;
		running = false;
		if (socket != null) socket.close();
	}
}
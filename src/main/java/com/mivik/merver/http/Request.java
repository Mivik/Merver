package com.mivik.merver.http;

import com.mivik.merver.BadRequestException;
import com.mivik.merver.MReader;
import com.mivik.merver.config.HttpConfig;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Request implements Constant {
	private String method;
	private String path;
	private String version;
	private Map<String, String> header = new HashMap<>();
	private byte[] data;

	public Request() {
		reset();
	}

	public Request(InputStream input, HttpConfig config) throws IOException, BadRequestException {
		reset();
		loadFrom(input, config);
	}

	public void loadFrom(InputStream input, HttpConfig config) throws IOException, BadRequestException {
		reset();
		MReader reader = new MReader(input);
		if ((method = reader.readTil(SPACE)) == null) throw new BadRequestException("Incomplete method");
		if ((path = reader.readTil(SPACE)) == null) throw new BadRequestException("Incomplete path");
		if ((version = reader.readSinglyTil(ENDL)) == null) throw new BadRequestException("Incomplete http version");
		String line;
		while (true) {
			line = reader.readSinglyTil(ENDL);
			if (line == null) throw new BadRequestException("Incomplete header");
			if (line.length() == 0) break;
			int ind = line.indexOf(':');
			if (ind == -1) throw new BadRequestException("Illegal header line: " + line);
			header.put(line.substring(0, ind), line.substring(ind + 2));
		}
		if (!header.containsKey("Host")) throw new BadRequestException("Missing host");

		if (!isPost()) return;
		int len;
		String ret;
		if ((ret = header.get("Content-Length")) != null) {
			try {
				len = Integer.parseInt(ret);
			} catch (NumberFormatException e) {
				throw new BadRequestException("Illegal ContentLength: " + ret);
			}
		} else len = config.maxContentLength;
		data = new byte[len];
		int read = input.read(data, 0, len);
		if (read != len)
			throw new BadRequestException("Content-Length described in header does not match the real one: Expected " + len + ", got " + read);
	}

	public void reset() {
		method = null;
		path = null;
		version = null;
		data = null;
		header.clear();
	}

	public boolean isGet() {
		return method.equals("GET");
	}

	public boolean isPost() {
		return method.equals("POST");
	}

	public String getUserAgent() {
		return header.get("User-Agent");
	}

	public String getCookie() {
		return header.get("Cookie");
	}

	public String getHost() {
		return header.get("Host");
	}

	public String getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	public String getVersion() {
		return version;
	}

	public String getHeader(String key) {
		return header.get(key);
	}

	public Set<String> getHeaderKeys() {
		return header.keySet();
	}

	public Collection<String> getHeaderValues() {
		return header.values();
	}

	public byte[] getData() {
		return data;
	}

	public void writeTo(OutputStream output) throws IOException {
		Writer writer = new OutputStreamWriter(output);
		writer.write(method);
		writer.write(SPACE);
		writer.write(path);
		writer.write(SPACE);
		writer.write(version);
		writer.write(ENDL);
		for (String one : header.keySet()) {
			writer.write(one);
			writer.write(": ");
			writer.write(header.get(one));
			writer.write(ENDL);
		}
		writer.write(ENDL);
		if (isPost()) {
			writer.flush();
			output.write(data);
			writer.write(ENDL);
		}
		writer.flush();
	}

	@Override
	public String toString() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			writeTo(out);
			out.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return out.toString();
	}
}
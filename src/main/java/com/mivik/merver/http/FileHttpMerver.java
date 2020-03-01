package com.mivik.merver.http;

import com.mivik.merver.MLog;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileHttpMerver extends HttpMerver implements Constant {
	private static Map<String, String> CONTENT_TYPE_MAP = null;

	public static final String getContentType(String name) {
		if (CONTENT_TYPE_MAP == null) {
			CONTENT_TYPE_MAP = new HashMap<>();
			final String data = "css:text/css#gif:image/gif#html:text/html#ico:image/x-icon#jpeg:image/jpeg#jpg:image/jpeg#js:text/javascript#json:application/json#pdf:application/pdf#png:image/png#svg:image/svg+xml#swf:application/x-shockwave-flash#tiff:image/tiff#txt:text/plain#wav:audio/x-wav#wma:audio/x-ms-wma#wmv:video/x-ms-wmv#xml:text/xml#";
			int st = 0, ind;
			while ((ind = data.indexOf(':', st)) != -1) {
				String ww = data.substring(st, ind);
				st = ind + 1;
				ind = data.indexOf('#', st);
				CONTENT_TYPE_MAP.put(ww, data.substring(st, ind));
				st = ind + 1;
			}
		}
		int ind = name.lastIndexOf('.');
		if (ind == -1) return DEFAULT_CONTENT_TYPE;
		String ret = CONTENT_TYPE_MAP.get(name.substring(ind + 1));
		if (ret == null) return DEFAULT_CONTENT_TYPE;
		return ret;
	}

	private File root;

	public FileHttpMerver() {
	}

	public FileHttpMerver(String path) {
		this(new File(path));
	}

	public FileHttpMerver(File root) {
		setRootDirectory(root);
	}

	public void setRootDirectory(File root) {
		if (root == null) throw new IllegalArgumentException("Root directory cannot be null");
		if (!root.exists()) throw new IllegalArgumentException("Root directory does not exist");
		if (!root.isDirectory()) throw new IllegalArgumentException("Root must be a directory");
		this.root = root;
	}

	public File getRootDirectory() {
		return root;
	}

	private void fileNotFound(String name, Response res) {
		res.setResponseCode(404);
		res.setData("Requested path \"" + name + "\" was not found on this server.");
	}

	public void sendFile(File file, String name, Response res) {
		if (config.verbose) MLog.v("Request " + name);
		if (!file.exists()) {
			if (config.verbose) MLog.v("File not found: " + file);
			fileNotFound(name, res);
			return;
		}
		if (file.isDirectory()) {
			file = new File(file, "index.html");
			if ((!file.exists()) || (!file.isFile())) {
				fileNotFound(name, res);
				return;
			}
		}
		InputStream input = null;
		ByteArrayOutputStream output = null;
		try {
			res.setContentType(getContentType(file.getName()));
			input = new FileInputStream(file);
			output = new ByteArrayOutputStream();
			byte[] buffer = new byte[config.defaultBufferSize];
			int read;
			while ((read = input.read(buffer)) != -1) output.write(buffer, 0, read);
			input.close();
			output.close();
			res.setResponseCode(200);
			res.setData(output.toByteArray());
		} catch (IOException e) {
			String des = "An error occurred while reading file \"" + name + "\"";
			MLog.e(des, e);
			res.setResponseCode(500);
			StringWriter out = new StringWriter();
			PrintWriter writer = new PrintWriter(out);
			writer.println(des);
			e.printStackTrace(writer);
			writer.close();
			try {
				out.close();
			} catch (IOException er) {
				throw new RuntimeException(er);
			}
			res.setData(out.toString());
		} finally {
			try {
				if (input != null) input.close();
				if (output != null) output.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void process(Request req, Response res) {
		String name = req.getParsedPath().getPath();
		sendFile(new File(root, name), name, res);
	}
}
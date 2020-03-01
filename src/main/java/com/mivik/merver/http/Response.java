package com.mivik.merver.http;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Response implements Constant {
	private static Map<Integer, String> RESPONSE_CODE_MAP = null;
	private static SimpleDateFormat DATE_FORMAT = null;

	public static final String getDescriptionForResponseCode(int code) {
		if (RESPONSE_CODE_MAP == null) {
			RESPONSE_CODE_MAP = new HashMap<>();
			final String data = "100:Continue#101:Switching Protocol#102:Processing#103:Early Hints#200:OK#201:Created#202:Accepted#203:Non-Authoritative Information#204:No Content#205:Reset Content#206:Partial Content#207:Multi-Status#208:Already Reported#226:IM Used#300:Multiple Choice#301:Moved Permanently#302:Found#303:See Other#304:Not Modified#305:Use Proxy#306:unused#307:Temporary Redirect#302:Found#308:Permanent Redirect#301:Moved Permanently#400:Bad Request#401:Unauthorized#402:Payment Required#403:Forbidden#404:Not Found#405:Method Not Allowed#406:Not Acceptable#407:Proxy Authentication Required#408:Request Timeout#409:Conflict#410:Gone#411:Length Required#412:Precondition Failed#413:Payload Too Large#414:URI Too Long#415:Unsupported Media Type#416:Requested Range Not Satisfiable#417:Expectation Failed#418:I'm a teapot#421:Misdirected Request#422:Unprocessable Entity#423:Locked#424:Failed Dependency#425:Too Early#426:Upgrade Required#428:Precondition Required#429:Too Many Requests#431:Request Header Fields Too Large#451:Unavailable For Legal Reasons#500:Internal Server Error#501:Not Implemented#502:Bad Gateway#503:Service Unavailable#504:Gateway Timeout#505:HTTP Version Not Supported#506:Variant Also Negotiates#507:Insufficient Storage#508:Loop Detected#510:Not Extended#511:Network Authentication Required#";
			int st = 0, ind;
			while ((ind = data.indexOf(':', st)) != -1) {
				int ww = Integer.parseInt(data.substring(st, ind));
				st = ind + 1;
				ind = data.indexOf('#', st);
				RESPONSE_CODE_MAP.put(ww, data.substring(st, ind));
				st = ind + 1;
			}
		}
		String ret = RESPONSE_CODE_MAP.get(code);
		if (ret == null) return "Unknown";
		return ret;
	}

	private String version;
	private int code;
	private Map<String, String> header = new HashMap<>();
	private ResponseData data;

	public Response() {
		reset();
	}

	public Response redirect(String location, boolean permanent) {
		setHeader("Location", location);
		setResponseCode(permanent ? 301 : 302);
		return this;
	}

	public Response setVersion(String version) {
		this.version = version;
		return this;
	}

	public String getVersion() {
		return version;
	}

	public Response setResponseCode(int code) {
		this.code = code;
		return this;
	}

	public int getResponseCode() {
		return code;
	}

	public void reset() {
		version = DEFAULT_VERSION;
		code = 0;
		header.clear();
		data = null;
		header.put("Server", "Merver V1.0");
		setContentType(DEFAULT_CONTENT_TYPE);
		setCharset(Charset.defaultCharset().name());
		markDate();
	}

	public Response setData(byte[] data) {
		return setData(new SimpleResponseData(data));
	}

	public Response setData(String data) {
		return setData(data.getBytes());
	}

	public Response setData(byte[] data, int off, int len) {
		return setData(new SimpleResponseData(data, off, len));
	}

	public Response setData(ResponseData data) {
		this.data = data;
		setHeader("Content-Length", Integer.toString(data == null ? 0 : data.getContentLength()));
		return this;
	}

	public Response setContentType(String contentType) {
		header.put("Content-Type", contentType);
		return this;
	}

	public String getContentType() {
		return header.get("Content-Type");
	}

	public Response setCharset(String charsetName) {
		header.put("Charset", charsetName);
		return this;
	}

	public String getCharset() {
		return header.get("Charset");
	}

	public Response markDate() {
		if (DATE_FORMAT == null)
			DATE_FORMAT = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.US);
		header.put("Date", DATE_FORMAT.format(new Date()));
		return this;
	}

	public String getDate() {
		return header.get("Date");
	}

	public int getContentLength() {
		return Integer.parseInt(header.get("Content-Length"));
	}

	public Response setHeader(String key, String data) {
		header.put(key, data);
		return this;
	}

	public String getHeader(String key) {
		return header.get(key);
	}

	public boolean hasHeader(String key) {
		return header.containsKey(key);
	}

	public void writeTo(OutputStream output) throws IOException {
		Writer writer = new OutputStreamWriter(output);
		writer.write(version);
		writer.write(SPACE);
		writer.write(Integer.toString(code));
		writer.write(SPACE);
		writer.write(getDescriptionForResponseCode(code));
		writer.write(ENDL);
		for (String one : header.keySet()) {
			writer.write(one);
			writer.write(": ");
			writer.write(header.get(one));
			writer.write(ENDL);
		}
		writer.write(ENDL);
		if (data != null) {
			writer.flush();
			data.write(output);
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
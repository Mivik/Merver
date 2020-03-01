package com.mivik.merver.http;

import com.mivik.merver.BadRequestException;

import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class URLPath {
	private String path;
	private Map<String, String> para = new HashMap<>();

	private URLPath() {
	}

	public URLPath(String path, Map<String, String> para) {
		this.path = path;
		this.para.putAll(para);
	}

	public String getPath() {
		return path;
	}

	public Map<String, String> getParameters() {
		return para;
	}

	public String getParameter(String key) {
		return para.get(key);
	}

	public void setParameter(String key, String value) {
		para.put(key, value);
	}

	public String removeParameter(String key) {
		return para.remove(key);
	}

	public void clearParameters() {
		para.clear();
	}

	public Set<String> getParameterKeys() {
		return para.keySet();
	}

	public static URLPath parse(String path) throws BadRequestException {
		path = URLDecoder.decode(path);
		URLPath ret = new URLPath();
		int ind = path.indexOf('?');
		if (ind == -1) {
			ret.path = makePathSafe(path);
			return ret;
		}
		ret.path = makePathSafe(path.substring(0, ind));
		for (String one : split(path, ind + 1, '&')) {
			ind = one.indexOf('=');
			if (ind == -1) throw new BadRequestException("'=' not found in \"" + one + "\"");
			ret.para.put(one.substring(0, ind), one.substring(ind + 1));
		}
		return ret;
	}

	public static String[] split(String msg, int st, char c) {
		int ind;
		ArrayList<String> ret = new ArrayList<>();
		while ((ind = msg.indexOf(c, st)) != -1) {
			ret.add(msg.substring(st, ind));
			st = ind + 1;
		}
		ret.add(msg.substring(st));
		return ret.toArray(new String[0]);
	}

	private static String makePathSafe(String path) {
		StringBuilder ret = new StringBuilder();
		for (String one : split(path, 0, '/')) {
			if (one.length() == 0) continue;
			if (one.charAt(0) == '.') {
				if (one.length() == 1) continue;
				if (one.charAt(1) == '.' && one.length() == 2) continue;
			}
			ret.append('/').append(one);
		}
		return ret.toString();
	}

	public static String encodeURI(String one) {
		return encodeURI(one, "-_.!~*`();/?:@&=+$,#");
	}

	public static String encodeURIComponent(String one) {
		return encodeURI(one, "-_.!~*`()");
	}

	private static String encodeURI(String one, String safe) {
		StringBuilder ret = new StringBuilder(one.length());
		StringBuilder tmp = new StringBuilder();
		for (int i = 0; i < one.length(); i++) {
			char c = one.charAt(i);
			if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || safe.indexOf(c) != -1) {
				if (tmp.length() != 0) {
					for (byte b : tmp.toString().getBytes()) hexEncode(ret, b);
					tmp.setLength(0);
				}
				ret.append(c);
			} else tmp.append(c);
		}
		if (tmp.length() != 0)
			for (byte b : tmp.toString().getBytes()) hexEncode(ret, b);
		return ret.toString();
	}

	public static String decodeURI(String one) {
		StringBuilder ret = new StringBuilder(one.length());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		for (int i = 0; i < one.length(); i++) {
			char c = one.charAt(i);
			if (c == '%') {
				if (i + 2 < one.length()) {
					int c1 = hexDecode(one.charAt(i + 1));
					int c2 = hexDecode(one.charAt(i + 2));
					if (c1 != -1 && c2 != -1) {
						out.write((c1 << 4) | c2);
						i += 2;
					}
				}
			} else {
				if (out.size() != 0) {
					ret.append(out.toString());
					out = new ByteArrayOutputStream();
				}
				ret.append(c);
			}
		}
		if (out.size() != 0) ret.append(out.toString());
		return ret.toString();
	}

	public static void hexEncode(StringBuilder ret, byte b) {
		final String HEX = "0123456789ABCDEF";
		ret.append('%').append(HEX.charAt((b >> 4) & 15)).append(HEX.charAt(b & 15));
	}

	private static int hexDecode(char c) {
		if (c >= '0' && c <= '9') return c - '0';
		if (c >= 'A' && c <= 'Z') return c - 'A' + 10;
		return -1;
	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder(encodeURI(path));
		if (!para.isEmpty()) {
			ret.append('?');
			for (Map.Entry<String, String> one : para.entrySet())
				ret.append(encodeURIComponent(one.getKey())).append('=').append(encodeURIComponent(one.getValue())).append('&');
			if (ret.charAt(ret.length() - 1) == '&') ret.deleteCharAt(ret.length() - 1);
		}
		return ret.toString();
	}
}
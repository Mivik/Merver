package com.mivik.merver;

import java.io.*;

public class MReader implements Closeable {
	private Reader reader;
	private boolean eof = false;

	public MReader(InputStream in) {
		this(new InputStreamReader(in));
	}

	public MReader(Reader reader) {
		this.reader = reader;
	}

	public int read() throws IOException {
		return reader.read();
	}

	public String readTil(char... tokens) throws IOException {
		StringBuffer ret = new StringBuffer();
		int c;
		w:
		while (true) {
			c = reader.read();
			if (c == -1) {
				eof = true;
				if (ret.length() == 0) return null;
				break;
			}
			char t = (char) c;
			for (int i = 0; i < tokens.length; i++) if (t == tokens[i]) break w;
			ret.append(t);
		}
		return ret.toString();
	}

	public String readTil(String pattern) throws IOException {
		StringBuffer ret = new StringBuffer();
		int c;
		int i = -1;
		int[] nxt = getKMPNext(pattern, null);
		while (true) {
			c = reader.read();
			if (c == -1) {
				eof = true;
				if (ret.length() == 0) return null;
				break;
			}
			char t = (char) c;
			ret.append(t);
			while (i != -1 && t != pattern.charAt(i + 1)) i = nxt[i];
			if (t == pattern.charAt(i + 1)) {
				if (++i == pattern.length() - 1) {
					ret.delete(ret.length() - pattern.length(), ret.length());
					break;
				}
			}
		}
		return ret.toString();
	}

	public String readSinglyTil(String pattern) throws IOException {
		StringBuffer ret = new StringBuffer();
		int c;
		int i = 0;
		while (true) {
			c = reader.read();
			if (c == -1) {
				eof = true;
				if (ret.length() == 0) return null;
				break;
			}
			char t = (char) c;
			ret.append(t);
			if (t == pattern.charAt(i)) {
				if (++i == pattern.length()) {
					ret.delete(ret.length() - pattern.length(), ret.length());
					break;
				}
			} else i = 0;
		}
		return ret.toString();
	}

	public String readLine() throws IOException {
		return readTil('\n');
	}

	public String readWord() throws IOException {
		return readTil(' ', '\t', '\r', '\n');
	}

	public Reader getReader() {
		return reader;
	}

	public static int[] getKMPNext(String s, int[] nxt) {
		if (nxt == null) nxt = new int[s.length()];
		int i, j;
		j = -1;
		nxt[0] = -1;
		for (i = 1; i < s.length(); i++) {
			while (j != -1 && s.charAt(i) != s.charAt(j + 1)) j = nxt[j];
			if (s.charAt(i) == s.charAt(j + 1)) ++j;
			nxt[i] = j;
		}
		return nxt;
	}

	public byte readByte() throws IOException {
		int c;
		byte ret = 0;
		boolean neg = false;
		while (true) {
			c = reader.read();
			if (c == -1) {
				eof = true;
				return ret;
			}
			if (c == '-') neg = true;
			else if (c >= '0' && c <= '9') {
				do {
					ret = (byte) ((ret << 1) + (ret << 3) + (c - '0'));
					c = reader.read();
				} while (c >= '0' && c <= '9');
				return neg ? (byte) -ret : ret;
			} else neg = false;
		}
	}

	public short readShort() throws IOException {
		int c;
		short ret = 0;
		boolean neg = false;
		while (true) {
			c = reader.read();
			if (c == -1) {
				eof = true;
				return ret;
			}
			if (c == '-') neg = true;
			else if (c >= '0' && c <= '9') {
				do {
					ret = (short) ((ret << 1) + (ret << 3) + (c - '0'));
					c = reader.read();
				} while (c >= '0' && c <= '9');
				return neg ? (short) -ret : ret;
			} else neg = false;
		}
	}

	public int readInt() throws IOException {
		int c;
		int ret = 0;
		boolean neg = false;
		while (true) {
			c = reader.read();
			if (c == -1) {
				eof = true;
				return ret;
			}
			if (c == '-') neg = true;
			else if (c >= '0' && c <= '9') {
				do {
					ret = (ret << 1) + (ret << 3) + (c - '0');
					c = reader.read();
				} while (c >= '0' && c <= '9');
				return neg ? -ret : ret;
			} else neg = false;
		}
	}

	public long readLong() throws IOException {
		int c;
		long ret = 0;
		boolean neg = false;
		while (true) {
			c = reader.read();
			if (c == -1) {
				eof = true;
				return ret;
			}
			if (c == '-') neg = true;
			else if (c >= '0' && c <= '9') {
				do {
					ret = (ret << 1) + (ret << 3) + (c - '0');
					c = reader.read();
				} while (c >= '0' && c <= '9');
				return neg ? -ret : ret;
			} else neg = false;
		}
	}

	public float readFloat() throws IOException {
		int c;
		float ret = 0;
		boolean neg = false;
		while (true) {
			c = reader.read();
			if (c == -1) {
				eof = true;
				return ret;
			}
			if (c == '-') neg = true;
			else if (c >= '0' && c <= '9') {
				read:
				while (true) {
					ret = ret * 10 + (c - '0');
					c = reader.read();
					if (c == -1) {
						eof = true;
						return ret;
					}
					if (c == '.') {
						float p = 1;
						while (true) {
							c = reader.read();
							if (c == -1) {
								eof = true;
								break read;
							}
							if (c < '0' || c > '9') break read;
							ret += (p /= 10) * (c - '0');
						}
					} else if (c < '0' || c > '9') break read;
				}
				return neg ? -ret : ret;
			} else neg = false;
		}
	}

	public double readDouble() throws IOException {
		int c;
		float ret = 0;
		boolean neg = false;
		while (true) {
			c = reader.read();
			if (c == -1) {
				eof = true;
				return ret;
			}
			if (c == '-') neg = true;
			else if (c >= '0' && c <= '9') {
				read:
				while (true) {
					ret = ret * 10 + (c - '0');
					c = reader.read();
					if (c == -1) {
						eof = true;
						return ret;
					}
					if (c == '.') {
						float p = 1;
						while (true) {
							c = reader.read();
							if (c == -1) {
								eof = true;
								break read;
							}
							if (c < '0' || c > '9') break read;
							ret += (p /= 10) * (c - '0');
						}
					} else if (c < '0' || c > '9') break read;
				}
				return neg ? -ret : ret;
			} else neg = false;
		}
	}

	public boolean isEOF() {
		return eof;
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}
}
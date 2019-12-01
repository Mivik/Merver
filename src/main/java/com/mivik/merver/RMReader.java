package com.mivik.merver;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.CharBuffer;

public class RMReader extends MReader {
	public RMReader(InputStream in) {
		super(new RawReader(in));
	}

	public static class RawReader extends Reader {
		private InputStream in;

		public RawReader(InputStream in) {
			this.in = in;
		}

		@Override
		public int read() throws IOException {
			return in.read();
		}

		@Override
		public int read(CharBuffer target) throws IOException {
			int len = target.remaining();
			int i, c;
			for (i = 0; i < len; i++) {
				c = in.read();
				if (c == -1) break;
				target.put((char) c);
			}
			return i;
		}

		@Override
		public int read(char[] chars, int off, int len) throws IOException {
			int i, c;
			for (i = 0; i < len; i++) {
				c = in.read();
				if (c == -1) break;
				chars[i + off] = (char) c;
			}
			return i;
		}

		@Override
		public void close() throws IOException {
			in.close();
		}
	}
}

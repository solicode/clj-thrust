package clj_thrust;

import java.io.IOException;
import java.io.Writer;

public class RpcWriter extends Writer {
	public static final String BOUNDARY = "--(Foo)++__THRUST_SHELL_BOUNDARY__++(Bar)--";

	private StringBuilder buffer;
	private RpcListener rpcListener;

	public RpcWriter() {
		this(null);
	}

	public RpcWriter(RpcListener rpcListener) {
		buffer = new StringBuilder();
		lock = buffer;
		this.rpcListener = rpcListener;
	}

	@Override
	public void write(int c) {
		buffer.append((char)c);

		if(c == '\n') {
			tryDispatch();
		}
	}

	@Override
	public void write(char[] cbuf, int off, int len) {
		if(off < 0 || off > cbuf.length || len < 0
				|| (off + len) > cbuf.length || (off + len) < 0) {
			throw new IndexOutOfBoundsException();
		}

		if(len == 0) {
			return;
		}

		buffer.append(cbuf, off, len);

		tryDispatch();
	}

	@Override
	public void write(String s) {
		buffer.append(s);

		tryDispatch();
	}

	@Override
	public void write(String s, int off, int len) {
		buffer.append(s, off, off + len);

		tryDispatch();
	}

	private void tryDispatch() {
		if(rpcListener == null) {
			return;
		}

		int start = 0;
		int boundaryIndex = buffer.indexOf(BOUNDARY);

		while(boundaryIndex >= 0) {
			rpcListener.received(new RpcEvent(buffer.substring(start, boundaryIndex)));

			start = boundaryIndex + BOUNDARY.length();
			boundaryIndex = buffer.indexOf(BOUNDARY, start);
		}

		if(start > 0) {
			buffer.setLength(0);
		}
	}

	@Override
	public void flush() {
	}

	@Override
	public void close() throws IOException {
	}
}

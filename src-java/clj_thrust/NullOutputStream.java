package clj_thrust;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Used for discarding data written into output stream. You can essentially
 * think of this as /dev/null for Java output streams.
 */
public class NullOutputStream extends OutputStream {
	@Override
	public void write(int b) throws IOException {
	}
}

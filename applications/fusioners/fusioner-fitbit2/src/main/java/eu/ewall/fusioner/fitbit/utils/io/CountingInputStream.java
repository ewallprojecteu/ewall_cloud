package eu.ewall.fusioner.fitbit.utils.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * This input stream keeps track of the current position in an underlying input
 * stream. It assumes that the current position in the underlying stream at
 * construction is 0. The counting input stream does not support mark and
 * reset.
 * 
 * @author Dennis Hofs
 */
public class CountingInputStream extends InputStream {
	private InputStream input;
	private long position = 0;
	
	/**
	 * Constructs a new counting input stream.
	 * 
	 * @param input the underlying stream at position 0
	 */
	public CountingInputStream(InputStream input) {
		this.input = input;
	}
	
	/**
	 * Returns the current position.
	 * 
	 * @return the current position
	 */
	public long getPosition() {
		return position;
	}

	@Override
	public int read() throws IOException {
		int result = input.read();
		if (result != -1)
			position++;
		return result;
	}

	@Override
	public int available() throws IOException {
		return input.available();
	}

	@Override
	public void close() throws IOException {
		input.close();
	}

	@Override
	public synchronized void mark(int readlimit) {
	}

	@Override
	public boolean markSupported() {
		return false;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int result = input.read(b, off, len);
		if (result > 0)
			position += result;
		return result;
	}

	@Override
	public int read(byte[] b) throws IOException {
		int result = input.read(b);
		if (result > 0)
			position += result;
		return result;
	}

	@Override
	public synchronized void reset() throws IOException {
	}

	@Override
	public long skip(long n) throws IOException {
		long result = input.skip(n);
		position += result;
		return result;
	}
}

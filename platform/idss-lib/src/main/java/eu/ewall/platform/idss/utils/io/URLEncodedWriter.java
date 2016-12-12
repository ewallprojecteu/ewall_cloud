package eu.ewall.platform.idss.utils.io;

import java.io.IOException;
import java.io.Writer;

import java.net.URLEncoder;

/**
 * This class encodes strings using the {@link URLEncoder URLEncoder} and
 * character set UTF-8.
 * 
 * @author Dennis Hofs
 */
public class URLEncodedWriter extends Writer {
	private Writer writer;
	
	/**
	 * Constructs a new URL-encoded writer.
	 * 
	 * @param writer the writer to which the URL-encoded strings should be
	 * written
	 */
	public URLEncodedWriter(Writer writer) {
		this.writer = writer;
	}
	
	@Override
	public void close() throws IOException {
		writer.close();
	}

	@Override
	public void flush() throws IOException {
		writer.flush();
	}
	
	@Override
	public void write(char[] cbuf) throws IOException {
		write(new String(cbuf));
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		write(new String(cbuf, off, len));
	}

	@Override
	public void write(int c) throws IOException {
		write(Character.toString((char)c));
	}
	
	@Override
	public void write(String str) throws IOException {
		writer.write(URLEncoder.encode(str, "UTF-8"));
	}
	
	@Override
	public void write(String str, int off, int len) throws IOException {
		write(str.substring(off, off + len));
	}
}

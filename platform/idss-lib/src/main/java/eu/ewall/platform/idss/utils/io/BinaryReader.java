package eu.ewall.platform.idss.utils.io;

import java.io.*;

import java.nio.*;

import java.nio.charset.*;

/**
 * A binary reader can read elementary data types from an input stream. It
 * can read data that has been written with a {@link BinaryWriter
 * BinaryWriter}. The read methods throw an {@link EOFException EOFException}
 * if the end of the stream is reached. If the stream comes from a file, this
 * is probably the end of the file. Otherwise it probably means that the
 * connection to the stream was lost.
 * 
 * <p>After performing a read() method, you can get the number of bytes that
 * have been read with {@link #getReadSize() getReadSize()}.</p>
 * 
 * @author Dennis Hofs
 */
public class BinaryReader {
	private InputStream in;
	private Charset utf;
	private int readSize = 0;
	
	/**
	 * Constructs a new binary reader that will read from the specified input
	 * stream.
	 * 
	 * @param in the input stream
	 */
	public BinaryReader(InputStream in) {
		this.in = in;
		utf = Charset.forName("UTF-8");
	}
	
	/**
	 * Returns the number of bytes that were read during the last read()
	 * method, if the method was successful.
	 * 
	 * @return the number of bytes that were read
	 */
	public int getReadSize() {
		return readSize;
	}

	/**
	 * Reads a string. It will first read an integer with the length
	 * of the encoded string. Then it reads the encoded string and decodes it
	 * with UTF-8.
	 * 
	 * @return the string
	 * @throws EOFException if the end of the stream is reached
	 * @throws IOException if a reading error occurs
	 */
	public String readString() throws EOFException, IOException {
		int len = readInt();
		readSize = 4 + len;
		byte[] bs = readBytes(len);
		ByteBuffer bb = ByteBuffer.wrap(bs);
		CharBuffer cb = utf.decode(bb);
		return cb.toString();
	}
	
	/**
	 * Reads a string. It will first read an unsigned short with the length
	 * of the encoded string. Then it reads the encoded string and decodes it
	 * with UTF-8.
	 * 
	 * @return the string
	 * @throws EOFException if the end of the stream is reached
	 * @throws IOException if a reading error occurs
	 */
	public String readShortString() throws EOFException, IOException {
		int len = readUnsignedShort();
		readSize = 2 + len;
		byte[] bs = readBytes(len);
		ByteBuffer bb = ByteBuffer.wrap(bs);
		CharBuffer cb = utf.decode(bb);
		return cb.toString();
	}

	/**
	 * Reads a byte.
	 * 
	 * @return the byte
	 * @throws EOFException if the end of the stream is reached
	 * @throws IOException if a reading error occurs
	 */
	public byte readByte() throws EOFException, IOException {
		readSize = 1;
		return readBytes(1)[0];
	}
	
	/**
	 * Reads an unsigned byte and returns its value as a short.
	 * 
	 * @return the unsigned byte
	 * @throws EOFException if the end of the stream is reached
	 * @throws IOException if a reading error occurs
	 */
	public short readUnsignedByte() throws EOFException, IOException {
		readSize = 1;
		byte b = readBytes(1)[0];
		return (short)(b & 0xFF);
	}

	/**
	 * Reads a 16-bit signed short in big-endian order and returns its value as
	 * a short.
	 * 
	 * @return the signed short
	 * @throws EOFException if the end of the stream is reached
	 * @throws IOException if a reading error occurs
	 */
	public short readShort() throws EOFException, IOException {
		readSize = 2;
		byte[] bs = readBytes(2);
		short n = 0;
		for (int i = 0; i < bs.length; i++) {
			n += (short)((bs[i] & 0xFF) << ((1-i) * 8));
		}
		return n;
	}

	/**
	 * Reads a 16-bit unsigned short in big-endian order and returns its value
	 * as an int.
	 * 
	 * @return the unsigned short
	 * @throws EOFException if the end of the stream is reached
	 * @throws IOException if a reading error occurs
	 */
	public int readUnsignedShort() throws EOFException, IOException {
		readSize = 2;
		byte[] bs = readBytes(2);
		int n = 0;
		for (int i = 0; i < bs.length; i++) {
			n += (bs[i] & 0xFF) << ((1-i) * 8);
		}
		return n;
	}
	
	/**
	 * Reads a 32-bit signed integer in big-endian order and returns its value
	 * as an int.
	 * 
	 * @return the signed integer
	 * @throws EOFException if the end of the stream is reached
	 * @throws IOException if a reading error occurs
	 */
	public int readInt() throws EOFException, IOException {
		readSize = 4;
		byte[] bs = readBytes(4);
		int n = 0;
		for (int i = 0; i < bs.length; i++) {
			n += (bs[i] & 0xFF) << ((3-i) * 8);
		}
		return n;
	}

	/**
	 * Reads a 32-bit unsigned int in big-endian order and returns its value
	 * as a long.
	 * 
	 * @return the unsigned int
	 * @throws EOFException if the end of the stream is reached
	 * @throws IOException if a reading error occurs
	 */
	public long readUnsignedInt() throws EOFException, IOException {
		readSize = 4;
		byte[] bs = readBytes(4);
		long n = 0;
		for (int i = 0; i < bs.length; i++) {
			n += (bs[i] & 0xFFL) << ((3-i) * 8);
		}
		return n;
	}
	
	/**
	 * Reads a 64-bit signed long in big-endian order and returns its value
	 * as a long.
	 * 
	 * @return the signed long
	 * @throws EOFException if the end of the stream is reached
	 * @throws IOException if a reading error occurs
	 */
	public long readLong() throws EOFException, IOException {
		readSize = 8;
		byte[] bs = readBytes(8);
		long n = 0;
		for (int i = 0; i < bs.length; i++) {
			n += (bs[i] & 0xFFL) << ((7-i) * 8);
		}
		return n;
	}
	
	/**
	 * Reads a float from 32 bits in big-endian order.
	 * 
	 * @return the float
	 * @throws EOFException if the end of the stream is reached
	 * @throws IOException if a reading error occurs
	 */
	public float readFloat() throws EOFException, IOException {
		readSize = 4;
		int bits = readInt();
		return Float.intBitsToFloat(bits);
	}
	
	/**
	 * Reads a double from 64 bits in big-endian order.
	 * 
	 * @return the double
	 * @throws EOFException if the end of the stream is reached
	 * @throws IOException if a reading error occurs
	 */
	public double readDouble() throws EOFException, IOException {
		readSize = 8;
		long bits = readLong();
		return Double.longBitsToDouble(bits);
	}
	
	/**
	 * Reads a boolean from one byte. It returns true for byte 0x01 and false
	 * for byte 0x00. Otherwise it throws an exception.
	 * 
	 * @return the boolean
	 * @throws EOFException if the end of the stream is reached
	 * @throws IOException if a reading error occurs or the byte value is
	 * invalid for a boolean
	 */
	public boolean readBoolean() throws EOFException, IOException {
		readSize = 1;
		int b = readByte() & 0xFF;
		if (b == 0x01)
			return true;
		else if (b == 0x00)
			return false;
		else {
			throw new IOException("Invalid byte value for boolean: " +
					String.format("0x%02X", new Integer(b)));
		}
	}
	
	/**
	 * Reads the specified number of bytes.
	 * 
	 * @param n the number of bytes to read
	 * @return the read bytes
	 * @throws EOFException if the end of the stream is reached
	 * @throws IOException if a reading error occurs
	 */
	private byte[] readBytes(int n) throws EOFException, IOException {
		byte[] bs = new byte[n];
		int read = 0;
		while (read < n) {
			int len = in.read(bs, read, n - read);
			if (len <= 0)
				throw new EOFException("End of stream");
			read += len;
		}
		return bs;
	}
}

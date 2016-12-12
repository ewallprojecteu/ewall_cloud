package eu.ewall.platform.idss.utils.io;

import java.io.IOException;
import java.io.OutputStream;

import java.nio.ByteBuffer;

import java.nio.charset.Charset;

/**
 * A binary writer can write elementary data types to an output stream. The
 * written data can be read by a {@link BinaryReader BinaryReader}.
 * 
 * @author Dennis Hofs
 */
public class BinaryWriter {
	private OutputStream out;
	private Charset utf;
	
	/**
	 * Constructs a new binary writer that will write to the specified output
	 * stream.
	 * 
	 * @param out the output stream
	 */
	public BinaryWriter(OutputStream out) {
		this.out = out;
		utf = Charset.forName("UTF-8");
	}
	
	/**
	 * Writes a string. The string is encoded with UTF-8. This method first
	 * writes an integer with the length of the encoded string. Then it writes
	 * the encoded string.
	 * 
	 * @param s the string
	 * @throws IOException if a writing error occurs
	 */
	public void writeString(String s) throws IOException {
		ByteBuffer bb = utf.encode(s);
		byte[] bs = new byte[bb.remaining()];
		bb.get(bs);
		writeInt(bs.length);
		out.write(bs);
	}
	
	/**
	 * Writes a string. The string is encoded with UTF-8. This method first
	 * writes an unsigned short with the length of the encoded string. Then it
	 * writes the encoded string.
	 * 
	 * @param s the string
	 * @throws IOException if a writing error occurs
	 */
	public void writeShortString(String s) throws IOException {
		ByteBuffer bb = utf.encode(s);
		byte[] bs = new byte[bb.remaining()];
		bb.get(bs);
		writeUnsignedShort(bs.length);
		out.write(bs);
	}

	/**
	 * Writes a byte.
	 * 
	 * @param b the byte
	 * @throws IOException if a writing error occurs
	 */
	public void writeByte(byte b) throws IOException {
		out.write(b & 0xFF);
	}

	/**
	 * Writes the value of a short as an unsigned byte. If the value does not
	 * fit in an unsigned byte, then this method throws an IOException.
	 * 
	 * @param n the unsigned short value
	 * @throws IOException if the value does not fit in an unsigned byte or if
	 * a writing error occurs
	 */
	public void writeUnsignedByte(short n) throws IOException {
		if ((n & 0xFF00) != 0)
			throw new IOException("Value out of range: " + n);
		out.write((byte)(n & 0xFF));
	}

	/**
	 * Writes the value of a short as a 16-bit signed short in big-endian
	 * order.
	 * 
	 * @param n the short
	 * @throws IOException if a writing error occurs
	 */
	public void writeShort(short n) throws IOException {
		byte[] bs = new byte[2];
		for (int i = 0; i < bs.length; i++) {
			bs[i] = (byte)((n >> ((1-i) * 8)) & 0xFF);
		}
		out.write(bs);
	}
	
	/**
	 * Writes the value of an int as a 16-bit unsigned short in big-endian
	 * order. If the value does not fit in a 16-bit unsigned short, then this
	 * method throws an IOException.
	 * 
	 * @param n the unsigned short value
	 * @throws IOException if the value does not fit in a 16-bit unsigned short
	 * or if a writing error occurs
	 */
	public void writeUnsignedShort(int n) throws IOException {
		byte[] bs = new byte[2];
		if ((n & 0xFFFF0000) != 0)
			throw new IOException("Value out of range: " + n);
		for (int i = 0; i < bs.length; i++) {
			bs[i] = (byte)((n >> ((1-i) * 8)) & 0xFF);
		}
		out.write(bs);
	}

	/**
	 * Writes the value of an int as a 32-bit signed integer in big-endian
	 * order.
	 * 
	 * @param n the integer
	 * @throws IOException if a writing error occurs
	 */
	public void writeInt(int n) throws IOException {
		byte[] bs = new byte[4];
		for (int i = 0; i < bs.length; i++) {
			bs[i] = (byte)((n >> ((3-i) * 8)) & 0xFF);
		}
		out.write(bs);
	}
	
	/**
	 * Writes the value of a long as a 32-bit unsigned int in big-endian order.
	 * If the value does not fit in a 32-bit unsigned int, then this method
	 * throws an IOException.
	 * 
	 * @param n the unsigned int value
	 * @throws IOException if the value does not fit in a 32-bit unsigned int
	 * or if a writing error occurs
	 */
	public void writeUnsignedInt(long n) throws IOException {
		byte[] bs = new byte[4];
		if ((n & 0xFFFFFFFF00000000L) != 0)
			throw new IOException("Value out of range: " + n);
		for (int i = 0; i < bs.length; i++) {
			bs[i] = (byte)((n >> ((3-i) * 8)) & 0xFF);
		}
		out.write(bs);
	}

	/**
	 * Writes the value of a long as a 64-bit signed long in big-endian
	 * order.
	 * 
	 * @param n the long
	 * @throws IOException if a writing error occurs
	 */
	public void writeLong(long n) throws IOException {
		byte[] bs = new byte[8];
		for (int i = 0; i < bs.length; i++) {
			bs[i] = (byte)((n >> ((7-i) * 8)) & 0xFF);
		}
		out.write(bs);
	}
	
	/**
	 * Writes the value of a float in 32 bits in big-endian order.
	 * 
	 * @param f the float
	 * @throws IOException if a writing error occurs
	 */
	public void writeFloat(float f) throws IOException {
		writeInt(Float.floatToIntBits(f));
	}

	/**
	 * Writes the value of a double in 64 bits in big-endian order.
	 * 
	 * @param d the double
	 * @throws IOException if a writing error occurs
	 */
	public void writeDouble(double d) throws IOException {
		writeLong(Double.doubleToLongBits(d));
	}

	/**
	 * Writes the value of a boolean in one byte. It writes 1 for true, 0 for
	 * false.
	 * 
	 * @param b the boolean
	 * @throws IOException if a writing error occurs
	 */
	public void writeBoolean(boolean b) throws IOException {
		if (b)
			writeByte((byte)0x01);
		else
			writeByte((byte)0x00);
	}
}

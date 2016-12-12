package eu.ewall.fusioner.fitbit.utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This class contains static utility methods for zip files.
 * 
 * @author Dennis Hofs (RRD)
 */
public class ZipUtils {
	
	/**
	 * Unzips a zip file to the specified output directory.
	 * 
	 * @param zipFile the zip file
	 * @param outputDir the output directory
	 * @throws IOException if a reading or writing error occurs
	 */
	public static void unzip(File zipFile, File outputDir) throws IOException {
		FileUtils.mkdir(outputDir);
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(
				zipFile));
		try {
			byte[] bs = new byte[2048];
			int len;
			ZipEntry entry;
			while ((entry = zipIn.getNextEntry()) != null) {
				try {
					File outFile = new File(outputDir, entry.getName());
					FileOutputStream output = new FileOutputStream(outFile);
					try {
						while ((len = zipIn.read(bs)) > 0) {
							output.write(bs, 0, len);
						}
					} finally {
						output.close();
					}
				} finally {
					zipIn.closeEntry();
				}
			}
		} finally {
			zipIn.close();
		}
	}
}

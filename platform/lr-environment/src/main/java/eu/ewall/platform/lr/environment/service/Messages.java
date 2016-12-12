package eu.ewall.platform.lr.environment.service;

import java.io.IOException;
import java.util.Properties;

public class Messages {
	private Properties prop;

	public Messages() {
		prop = new Properties();
	}
	
	public Properties getProp() {
//		InputStream stream = null;
		
		try {
//			stream = new FileInputStream("messages_"+code+".properties");
			prop.load(Messages.class.getResourceAsStream("/messages_en.properties"));
			
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
//			if (stream != null) {
//				try {
//					stream.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
		}
		
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}
	
	
}

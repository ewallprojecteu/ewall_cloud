/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.resource;

/**
 * The Class Base64Image.
 */
public class Base64Image extends Resource {

	/** The mime type str. */
	private String mimeTypeStr;
	
	/** The base64 encoded image str. */
	private String base64EncodedImageStr;
	
	/**
	 * Instantiates a new base64 image.
	 */
	public Base64Image() {
	}
	
	/**
	 * Instantiates a new base64 image.
	 *
	 * @param name the name
	 * @param mimeTypeStr the mime type str
	 * @param base64EncodedImageStr the base64 encoded image str
	 */
	public Base64Image(String name, String mimeTypeStr, String base64EncodedImageStr) {
		this.name = name;
		this.mimeTypeStr = mimeTypeStr;
		this.base64EncodedImageStr = base64EncodedImageStr;
	}
	
	/**
	 * Gets the base64 encoded image str.
	 *
	 * @return the base64 encoded image str
	 */
	public String getBase64EncodedImageStr() {
		return base64EncodedImageStr;
	}
	
	/**
	 * Sets the base64 encoded image str.
	 *
	 * @param base64EncodedImageStr the new base64 encoded image str
	 */
	public void setBase64EncodedImageStr(String base64EncodedImageStr) {
		this.base64EncodedImageStr = base64EncodedImageStr;
	}
	
	/**
	 * Gets the mime type str.
	 *
	 * @return the mime type str
	 */
	public String getMimeTypeStr() {
		return mimeTypeStr;
	}
	
	/**
	 * Sets the mime type str.
	 *
	 * @param mimeTypeStr the new mime type str
	 */
	public void setMimeTypeStr(String mimeTypeStr) {
		this.mimeTypeStr = mimeTypeStr;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.hateoas.ResourceSupport#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("name: ");
		buffer.append(name);
		buffer.append("\n");
		buffer.append("description: ");
		buffer.append(description);
		buffer.append("\n");
		buffer.append("mimeType: ");
		buffer.append(mimeTypeStr);
		buffer.append("\n");
		buffer.append("base64EncodedImage: ");
		buffer.append(base64EncodedImageStr);
		buffer.append("\n");

		return buffer.toString();
	}
}

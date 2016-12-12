/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.datamanager.dao.profile;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;

import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import eu.ewall.platform.commons.datamodel.resource.Base64Image;
import eu.ewall.platform.middleware.datamanager.dao.config.dbtype.EWallDBType;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class UserDao.
 */
public class UserPhotoDao {

	/** The log. */
	private static final Logger LOG = LoggerFactory
			.getLogger(UserPhotoDao.class);

	/** The Constant PHOTOS_COLLECTION. */
	public static final String PHOTOS_COLLECTION = "gfsPhoto";

	/** The mongo ops. */
	private MongoOperations mongoOps;

	/** The gfs photo. */
	private GridFS gfsPhoto;

	/**
	 * Instantiates a new user dao.
	 */
	public UserPhotoDao() {
		mongoOps = MongoDBFactory
				.getMongoOperationsForDBType(EWallDBType.SYSTEM);
	}

	/**
	 * Gets the grid fs photo instance.
	 *
	 * @return the grid fs photo instance
	 */
	protected GridFS getGridFSPhotoInstance() {
		if (this.gfsPhoto == null) {
			this.gfsPhoto = new GridFS(
					MongoDBFactory.getDBForDBType(EWallDBType.SYSTEM), "photo");
		}
		return this.gfsPhoto;
	}

	/**
	 * Gets the photo by username.
	 *
	 * @param username
	 *            the username
	 * @return the photo by username
	 */
	public GridFSDBFile getPhotoByUsername(String username) {
		if (username.isEmpty() == true)
			return null;

		LOG.debug("Retrieveing photo for user with username: " + username
				+ " from mongodb.");

		String photoName = username + "Photo";

		GridFSDBFile imageForOutput = getGridFSPhotoInstance().findOne(
				photoName);

		return imageForOutput;
	}

	/**
	 * Save user photo.
	 *
	 * @param username
	 *            the username
	 * @param base64Image
	 *            the base64 image
	 * @return true, if successful
	 */
	public boolean saveUserPhoto(String username, Base64Image base64Image) {
		if (username.isEmpty() == true) {
			return false;
		}

		LOG.debug("Saving photo for user with username " + username
				+ " to mongodb.");

		GridFSInputFile gfsFile = null;
		String photoName = username + "Photo";
		GridFSDBFile existingGFile = getGridFSPhotoInstance()
				.findOne(photoName);
		/*
		 * Real update is not supported, so old photo has to be deleted first
		 * and then new one created
		 */
		if (existingGFile != null) {
			deleteUserPhoto(username);
		}

		byte[] decodedBinaryImage = Base64.decodeBase64(base64Image
				.getBase64EncodedImageStr());
		gfsFile = getGridFSPhotoInstance().createFile(decodedBinaryImage);

		if (gfsFile != null) {
			gfsFile.setFilename(username + "Photo");
			gfsFile.setContentType(base64Image.getMimeTypeStr());
			gfsFile.save();
			return true;
		}
		return false;
	}

	/**
	 * Delete user photo.
	 *
	 * @param username
	 *            the username
	 * @return true, if successful
	 */
	public boolean deleteUserPhoto(String username) {

		if (username.isEmpty() == true)
			return false;

		LOG.debug("Deleting photo for user with username " + username
				+ " from mongodb.");

		String photoName = username + "Photo";
		getGridFSPhotoInstance().remove(
				getGridFSPhotoInstance().findOne(photoName));
		return true;

	}

	/**
	 * Collection exits.
	 *
	 * @return true, if successful
	 */
	public boolean collectionExits() {
		return mongoOps.collectionExists(PHOTOS_COLLECTION);
	}
}

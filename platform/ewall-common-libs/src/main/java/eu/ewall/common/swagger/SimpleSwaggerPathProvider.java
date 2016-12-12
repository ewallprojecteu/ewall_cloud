package eu.ewall.common.swagger;

import com.mangofactory.swagger.paths.SwaggerPathProvider;

/**
 * Path provider for Swagger that simply returns a value configured in the constructor.
 * Allows to handle cases when the application is deployed at a URL such as http://myhost/mypath/myapp and the
 * RelativeSwaggerPathProvider would return only the context path (myapp) leading to API calls to http://myhost/myapp,
 * which won't work. Setting the path to mypath/myapp explicitly solves the problem.
 */
public class SimpleSwaggerPathProvider extends SwaggerPathProvider {

	/** The application path. */
	private String applicationPath;
	
	/**
	 * Instantiates a new simple swagger path provider.
	 *
	 * @param applicationPath the (relative or absolute) base url of the application
	 */
	public SimpleSwaggerPathProvider(String applicationPath) {
		this.applicationPath = applicationPath;
	}
	
	/* (non-Javadoc)
	 * @see com.mangofactory.swagger.paths.SwaggerPathProvider#applicationPath()
	 */
	@Override
	protected String applicationPath() {
		return applicationPath;
	}

	/* (non-Javadoc)
	 * @see com.mangofactory.swagger.paths.SwaggerPathProvider#getDocumentationPath()
	 */
	@Override
	protected String getDocumentationPath() {
		return "/";
	}

}

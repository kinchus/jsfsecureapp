/**
 * 
 */
package es.qworks.jsfsecureapp.service.exception;

/**
 * Base class for errors in the service layer. 
 * Inheritance point is set to Throwable for less overheading.
 * 
 * @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 *
 */
public class ServiceError extends Throwable {

	private static final long serialVersionUID = -5644708987907593906L;

	/**
	 * 
	 */
	public ServiceError() {
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public ServiceError(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public ServiceError(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public ServiceError(Throwable arg0) {
		super(arg0);
	}

}

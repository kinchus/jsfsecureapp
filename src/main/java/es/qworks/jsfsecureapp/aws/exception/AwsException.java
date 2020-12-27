package es.qworks.jsfsecureapp.aws.exception;

import com.amazonaws.AmazonServiceException;

/**
 * @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 *
 */
public class AwsException extends Throwable {

	
	private static final long serialVersionUID = 1L;
	
	private String serviceName = "unspecified";
	private String serviceCall = "unspecified";
	private String origin = "unspecified";

	/**
	 * @param msg
	 */
	public AwsException(String msg) {
		super(msg);
	}

	/**
	 * @param awsError 
	 * 
	 */
	public AwsException(AmazonServiceException awsError) {
		super(awsError.getErrorMessage(), awsError);
		setServiceName(awsError.getServiceName());
		setOrigin(awsError.getErrorType().name());
	}

	/**
	 * @param serviceCall
	 * @param awsError
	 */
	public AwsException(String serviceCall, AmazonServiceException awsError) {
		super(awsError.getMessage(), awsError);
		setServiceName(awsError.getServiceName());
		setServiceCall(serviceCall);
		setOrigin(awsError.getErrorType().name());
	}
	
	/**
	 * 
	 * @param serviceCall
	 * @param message
	 * @param awsError
	 */
	public AwsException(String serviceCall, String message , AmazonServiceException awsError) {
		super(message, awsError);
		setServiceName(awsError.getServiceName());
		setServiceCall(serviceCall);
		setOrigin(awsError.getErrorType().name());
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * @return the serviceCall
	 */
	public String getServiceCall() {
		return serviceCall;
	}

	/**
	 * @param serviceCall the serviceCall to set
	 */
	public void setServiceCall(String serviceCall) {
		this.serviceCall = serviceCall;
	}

	/**
	 * @return the origin
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}


}

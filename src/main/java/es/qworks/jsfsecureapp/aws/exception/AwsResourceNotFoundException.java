package es.qworks.jsfsecureapp.aws.exception;

import com.amazonaws.AmazonServiceException;

/**
 * @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 *
 */
public class AwsResourceNotFoundException extends AwsException {

	private static final long serialVersionUID = 1361452077909983615L;

	/**
	 * @param resource
	 */
	public AwsResourceNotFoundException(String resource) {
		super("The specified resource does not exist: " + resource);
	}

	/**
	 * @param awsError
	 */
	public AwsResourceNotFoundException(AmazonServiceException awsError) {
		super(awsError);
	}

	/**
	 * @param serviceCall
	 * @param cause
	 */
	public AwsResourceNotFoundException(String serviceCall, AmazonServiceException cause) {
		super(serviceCall, cause);
	}

}

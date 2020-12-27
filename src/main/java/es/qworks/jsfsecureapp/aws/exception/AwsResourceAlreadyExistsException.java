package es.qworks.jsfsecureapp.aws.exception;

import com.amazonaws.AmazonServiceException;

/**
 * @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 *
 */
public class AwsResourceAlreadyExistsException extends AwsException {

	private static final long serialVersionUID = 1394845624607729205L;

	/**
	 * @param resource
	 */
	public AwsResourceAlreadyExistsException(String resource) {
		super("The specified resource already exists in the system: " + resource);
	}

	/**
	 * @param awsError
	 */
	public AwsResourceAlreadyExistsException(AmazonServiceException awsError) {
		super(awsError);
	}

	/**
	 * @param awsCall
	 * @param awsError
	 */
	public AwsResourceAlreadyExistsException(String awsCall, AmazonServiceException awsError) {
		super(awsCall, awsError);
	}

}

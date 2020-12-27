package es.qworks.jsfsecureapp.aws;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;

/**
 * @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 *
 */
public class AwsFactory {

	private static AWSCognitoService cognitoService = null;
	
	/**
	 * @return the AWS Cognito service
	 */
	@Produces
	@SessionScoped
	public static synchronized AWSCognitoService getCognitoService() {
		if (cognitoService == null) {
			cognitoService = new AWSCognitoService();
		}
		return cognitoService;
	}
	

}

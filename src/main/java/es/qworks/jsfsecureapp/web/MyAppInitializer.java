package es.qworks.jsfsecureapp.web;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.qworks.jsfsecureapp.aws.AWSCognitoService;
import es.qworks.jsfsecureapp.dao.mongo.MongoManager;


/**
 * 
 * @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 */
@WebListener
public class MyAppInitializer implements ServletContextListener {

	private static Logger LOG = LoggerFactory.getLogger(MyAppInitializer.class);
	
	private static final String ENV_BACKEND_S3BUCKET = "BACKEND_LOCATION";
	private static String backendLocation = System.getenv(ENV_BACKEND_S3BUCKET);
	
	
	
    /**
     * @param sce
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
    	LOG.info("Application context initialization for {}", sce.getServletContext().getContextPath());
    
    	if (backendLocation == null) {
    		LOG.error("IoTCloud Backend location cannot be null");
    		System.exit(1);
    	}
    	
    	try {
			configureMongoDB();
			configureAWS(); 
			
			
		} catch (Exception e) {
			LOG.error("{} configuring system: {}", e, e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		
		// Initialization in background
		new Thread(new Runnable() {
			@Override
			public void run() {
				LOG.debug("Initialize MongoDB client");
				MongoManager.getInstance().getDatastore();
				MongoManager.getInstance().getMorphia();
			}			
		}).start();
    }

    /**
     * @param sce
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    	LOG.debug("Destroy context path  \"{}\"", sce.getServletContext().getContextPath());
    	LOG.debug("Closing MongoDB Connections");
    	MongoManager.getInstance().close();
    }
    
    
    private void configureMongoDB() {
    	LOG.debug("Initializing MongoDB Client");
    	MongoManager.getInstance().getClient();
    }
    
    private void configureAWS() {
    	LOG.debug("Initializing AWS-Cognito Client");
    	AWSCognitoService.init();
    }
    
}
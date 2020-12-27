/**
 * 
 */
package es.qworks.jsfsecureapp.web.view;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.qworks.jsfsecureapp.model.User;
import es.qworks.jsfsecureapp.service.UserService;
import es.qworks.jsfsecureapp.web.beans.annotations.LoggedUser;

/**
 * @author jmgd6647
 *
 */
public abstract class AbstractBackingBean implements Serializable {

	private static Logger LOG = LoggerFactory.getLogger(AbstractBackingBean.class);

	private static final long serialVersionUID = 1113245860396311388L;
	
	/** ACTION_EDIT */
	public static final String ACTION_EDIT 	= "edit";
	/** ACTION_NEW */
	public static final String ACTION_NEW 	= "new";
	/** REDIRECT */
	public static final String REDIRECT 	= "?faces-redirect=true";
	
	@Inject
	@LoggedUser
	private User currentUser = null;
	@Inject
	private UserService userService;
	
	private String viewId;
	private String outcome;
	private String action = ACTION_NEW;
	
	
	/**
	 * 
	 */
	public AbstractBackingBean() {}
	
	/**
	 * @param thisViewId 
	 * 
	 */
	public AbstractBackingBean(String thisViewId) {
		setViewId(thisViewId);
	}
	
	/**
	 * @param thisViewId 
	 * @param action 
	 * 
	 */
	public AbstractBackingBean(String thisViewId, String action) {
		setViewId(thisViewId);
		setAction(action);
	}
	
	/**
	 * 
	 */
	@PostConstruct
	public void init() {
		if (getCurrentUser() == null) {
			LOG.error("Not logged in");
			throw new RuntimeException("User is not logged in");
		}
	}
	
	/**
	 * @return the viewId
	 */
	public String getViewId() {
		return viewId;
	}
	
	/**
	 * @return the viewId
	 */
	public String getViewIdRedirect() {
		return viewId + REDIRECT;
	}


	/**
	 * @param viewId the viewId to set
	 */
	public void setViewId(String viewId) {
		this.viewId = viewId;
	}


	/**
	 * @return
	 */
	public String getOutcome() {
		return outcome;
	}

	/**
	 * @param outcome
	 */
	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}
	
	
	/**
	 * @param outcome
	 * @return
	 */
	public String redirect(String outcome) {
		return outcome + REDIRECT;
	}
	
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		if (!getCurrentContext().isPostback() && !getCurrentContext().isValidationFailed()) {
			this.action = action;
		}
	}

	/**
	 * @return the action
	 */
	public Boolean isActionNEW() {
		if (action != null) {
			return action.equals(ACTION_NEW);
		}
		return false;
	}
	
	/**
	 * @return the action
	 */
	public Boolean isActionEDIT() {
		if (action != null) {
			return action.equals(ACTION_EDIT);
		}
		return false;
	}
	
	/**
	 * @param viewAction 
	 * @return the action
	 */
	public Boolean actionEquals(String viewAction) {
		if ((action != null)&& (viewAction != null)) {
			return action.equals(viewAction);
		}
		return false;
	}

	/**
	 * @return the currentUser
	 */
	public User getCurrentUser() {
		return currentUser;
	}

	/**
	 * @return the isAdmin
	 */
	public Boolean isAdmin() {
		return (getCurrentUser().hasRole("ADMINISTRATOR"));
	}
	
	/**
	 * @return the customerService
	 */
	protected UserService getUserService() {
		return userService;
	}

	/**
     * @param summary
     * @param detail
     */
    protected void logUserAction(String summary, String detail) {
    	
    }
		

	/**
	 * @return
	 */
	protected FacesContext getCurrentContext() {
		return FacesContext.getCurrentInstance();
	}

	/**
	 * @return
	 */
	protected ExternalContext getExternalContext() {
		ExternalContext ectx = null;
		FacesContext fctx = getCurrentContext();
		if (fctx != null) {
			ectx = fctx.getExternalContext();
		}
		return ectx;
	}

	/**
     * @param severity
     * @param message
     */
    protected FacesMessage sendMessage(Severity severity, String message) {
    	return sendMessage(severity, "", message);
    }
    
    /**
     * @param severity
     * @param summary
     * @param detail
     * @return
     */
    protected FacesMessage sendMessage(Severity severity, String summary, String detail) {
    	return sendMessage(null, severity, summary, detail);
    }
    
    /**
     * @param clientId
     * @param severity
     * @param detail
     * @return
     */
    protected FacesMessage sendMessage(String clientId, Severity severity, String detail) {
    	// getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message = new FacesMessage(severity, null, detail);
        FacesContext.getCurrentInstance().addMessage(clientId, message);
        return message;
    }
    
    
    /**
     * @param severity
     * @param summary
     */
    protected FacesMessage sendMessage(String clientId, Severity severity, String summary, String detail) {
    	// getExternalContext().getFlash().setKeepMessages(true);
        FacesMessage message = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(clientId, message);
        return message;
    }
    
    /**
     * @param severity
     * @param message
     * @return
     */
    protected FacesMessage sendKeepMessage(Severity severity, String message) {
    	return sendMessage(severity, "", message);
    }
    
    /**
     * @param severity
     * @param summary
     */
    protected FacesMessage sendKeepMessage(Severity severity, String summary, String detail) {
    	return sendMessage(null, severity, "", detail);
    }
    
    /**
     * @param severity
     * @param summary
     */
    protected FacesMessage sendKeepMessage(String clientId, Severity severity, String summary, String detail) {
        FacesMessage message = new FacesMessage(severity, summary, detail);
        getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().addMessage(clientId, message);
        return message;
    }
    
    /**
     * @param destination
     */
    protected void redirectTo(String destination) {
    	ExternalContext ctx = getExternalContext();
    	try {
			ctx.redirect(destination);
		} catch (IOException e) {
			LOG.error("Couldn't redirect to {}: {}", destination, e.getMessage());
			e.printStackTrace();
		}
    }
    
    
    /**
     * @param urlStr
     * @param mimeType
     * @param filename
     * @return
     */
    protected StreamedContent getStreamFromURL(String urlStr, String mimeType, String filename) {
    
		StreamedContent file = null;
		
		if (urlStr != null) {
			InputStream stream = null;
			try {
				URL url = new URL(urlStr);
				stream = url.openStream();
				file = new DefaultStreamedContent(
					stream, 
					mimeType, 
					filename);
			} catch (IOException e) {}
		}
		
		return file;
    }

    
    
}

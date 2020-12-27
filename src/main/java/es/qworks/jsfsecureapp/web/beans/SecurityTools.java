package es.qworks.jsfsecureapp.web.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import es.qworks.jsfsecureapp.model.User;
import es.qworks.jsfsecureapp.web.beans.annotations.LoggedUser;

/**
 * 
 * @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 */
@Named
@RequestScoped
public class SecurityTools {
	
	@Inject
	@LoggedUser
	private User loggedUser;
	
    /**
     * @return
     */
    public Subject getSubject() {
        return SecurityUtils.getSubject();
    }
    
    /**
     * @param role
     * @return
     */
    public Boolean subjectHasRole(String role) {
    	Subject user = getSubject();
    	if ((user != null) && (user.hasRole(role))) {
    		return true;
    	}
    	return false;
    }
}
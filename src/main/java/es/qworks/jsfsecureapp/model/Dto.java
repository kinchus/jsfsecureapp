package es.qworks.jsfsecureapp.model;

import java.io.Serializable;

/**
 * 
 * @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
 */
public abstract class Dto implements Serializable {

	private static final long serialVersionUID = 4424906505037431428L;
	
	private Long id;
	
	/**
	 * Constructor 
	 */
	public Dto() {
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = Long.parseLong(id, 16);
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
}

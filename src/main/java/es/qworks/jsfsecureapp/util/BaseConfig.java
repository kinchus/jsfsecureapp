/*
 * Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package es.qworks.jsfsecureapp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class for simplifying reading and processing configuration files.
 */
public class BaseConfig {
	
	/**
	 * @author <a href="mailto:garciadjx@gmail.com">J.M. Garcia</a>
	 *
	 */
	public interface ConfigKey {
		
		/**
		 * @return
		 */
		String getKey();
		
		/**
		 * @return
		 */
		Class<?> getType();
	}

	private static final Logger LOG = LoggerFactory.getLogger(BaseConfig.class);
	
	private Path path = null;
    private Properties properties = new Properties(); 
    
    protected boolean fileParsed = false;
    
    /**
     * 
     */
    public BaseConfig() {
    }
    
    /**
     * @param filename
     */
    public BaseConfig(String filename) {
    	try {
			setPropertiesFile(filename);
		} catch (IOException e) {
			LOG.error("Couldn't read configuration from file {}", filename);
		}
    }
    
    /**
     * Looks for the requested properties file and loads it.
     * Search order:
     * 	1. User directory
     *  2. Local resource file
     *  3. Classpath file
     *  
     * @param filename
     * @throws IOException
     * @throws FileNotFoundException
     */
    protected void setPropertiesFile(String filename) throws IOException, FileNotFoundException {
    	InputStream stream = null;
    	String userPath = System.getProperty("user.home");
    	File file = Paths.get(userPath, filename).toFile();
		if (file.exists())  {
			LOG.trace("Configuration found in USER_HOME", filename);
			stream = new FileInputStream(file); 
		}
		else {
			file = new File(filename);
		}
		
		if (file.exists())  {
			LOG.trace("Configuration found in current path", filename);
			stream = new FileInputStream(file);
        }
        else {
        	URL res = getClass().getClassLoader().getResource(filename);
        	if (res != null) {
        		LOG.trace("Configuration found in classpath", filename);
    			file = new File(res.getPath());
        		stream = res.openStream();
        	}
        }
        
        if (stream != null) {
        	LOG.trace("Loading configuration from file {}", file);
        	properties.load(stream);
        	// path = file.toPath();
        	fileParsed = true;
			
		}
        else {
        	LOG.trace("Configuration file {} not found", file.toString());
        	throw new FileNotFoundException();
        }
    }
    
    
    /**
     * @param key
     * @return
     */
    public boolean hasProperty(ConfigKey key) {
		if (getProperty(key) != null) {
			return true;
		}
		return false;
	}
    
      
    /**
     * @param <T>
     * @param key
     * @return
     */
   @SuppressWarnings("unchecked")
   public <T> T getProperty(ConfigKey key) {
		return (T)getProperty(key, null);
	}
    
   	/**
   	 * @param <T>
   	 * @param key
   	 * @param defVal
   	 * @return
   	 */
   	@SuppressWarnings("unchecked")
	public <T> T getProperty(ConfigKey key, Object defVal) {
   		
   		
   		String strVal = System.getenv(getSystemVarName(key.getKey()));
   		
   		if (strVal == null) {
   			strVal = getProperty(key.getKey());
   		}
   		
   		if (StringUtil.isBlank(strVal)) {
			return (T)defVal;
		}
		else if (key.getType() == String.class) {
			return (T)strVal;
		}
		else if (key.getType() == Boolean.class) {
			return (T)((Boolean)Boolean.parseBoolean(strVal));
		}
		else if (key.getType() == Integer.class) {
			return (T)((Integer)Integer.parseInt(strVal));
		}
		else if (key.getType() == Long.class) {
			return (T)((Long)Long.parseLong(strVal));
		}
		else if (key.getType() == Float.class) {
			return (T)((Float)Float.parseFloat(strVal));
		}
		else if (key.getType() == Double.class) {
			return (T)((Double)Double.parseDouble(strVal));
		}
		
		return null;
	}
   	
   	/**
   	 * @param <T>
   	 * @param key
   	 * @return
   	 * @throws Exception
   	 */
   	@SuppressWarnings("unchecked")
	public <T> T requireProperty(ConfigKey key) throws Exception {
		
		String strVal = getProperty(key.getKey());
		try {
			if (strVal == null) {
				throw new Exception("Configuration property not found: " + key.getKey());
			}
			else if (key.getType() == String.class) {
				return (T)strVal;
			}
			else if (key.getType() == Boolean.class) {
				return (T)((Boolean)Boolean.parseBoolean(strVal));
			}
			else if (key.getType() == Integer.class) {
				return (T)((Integer)Integer.parseInt(strVal));
			}
			else if (key.getType() == Long.class) {
				return (T)((Long)Long.parseLong(strVal));
			}
			else if (key.getType() == Float.class) {
				return (T)((Float)Float.parseFloat(strVal));
			}
			else if (key.getType() == Double.class) {
				return (T)((Double)Double.parseDouble(strVal));
			}
		}
		catch(ParseException e) {
			throw new Exception("Wrong value found for property " + key.getKey() + ": " + strVal);
		}
		catch(NumberFormatException e) {
			throw new Exception("Wrong value found for property " + key.getKey() + ": " + strVal);
		}
		
		throw new Exception("Unmanaged property value type: " + key.getType());
	}
    
    /**
     * @param property
     * @param defaultValue 
     * @return
     */
    public String getProperty(String property, String defaultValue) {
    	String val = getProperty(property);
    	if (StringUtil.isBlank(val)) {
    		val = defaultValue;
    	}
    	return val;
    }
 
    /**
     * 
     * @param property
     * @return
     */
    public String getProperty(String property) {
    	if (properties == null) {
    		return null;
    	}

    	return properties.getProperty(property); 
    }
    
    /**
     * @param property
     * @param value 
     */
    public void setProperty(String property, Object value) {
    	properties.setProperty(property, value.toString());
    }
    
    /**
     * @param key
     * @return
     */
    public boolean validateProperty(ConfigKey key) {
    	if (!properties.containsKey(key.getKey())) {
    		return false;
    	}
    	try {
			requireProperty(key);
		} catch (Exception e) {
			LOG.error(e.getMessage());;
			return false;
		}
    	return true;
    }
    
    /**
     * @throws IOException
     */
    public void persist() throws IOException {
    	if (path == null) {
    		return;
    	}
    	FileOutputStream fos = new FileOutputStream(path.toFile());
    	properties.store(fos, null);
    }
    
    /**
     * @param property
     * @return
     */
    public static String getSystemVarName(String property) {
    	return property.replace('.', '_').toUpperCase();
    }

}

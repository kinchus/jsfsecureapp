/**
 * 
 */
package es.qworks.jsfsecureapp.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author jmgd6647
 *
 */
public class StringUtil {

	
	/**
	 * @param str
	 * @return true si str es null o es una cadena vac�a 
	 */
	public static Boolean isBlank(String str) {
		if ((str == null) || str.isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * @param str
	 * @return true si str es null o es una cadena vac�a 
	 */
	public static String trim(String str) {
		if ((str == null) || str.isEmpty()) {
			return null;
		}
		return str.replaceAll(" ", "");
	}
	
	/**
	 * Returns the String representation of this object when it is not null
	 * If it is null then this method also returns null
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj) {
		if ((obj == null)) {
			return null;
		}
		else {
			return obj.toString();
		}
	}
	
	/**
	 * @param str
	 * @return
	 */
	public static InputStream toInputStream(String str) {
		InputStream ret = null;
		ret = new ByteArrayInputStream(str.getBytes());
		return ret;
	}
	
	/**
	 * @param f
	 * @return
	 * @throws FileNotFoundException
	 */
	public static String file2String(File f) throws FileNotFoundException {
		return file2String(new FileReader (f));
	}
	
	/**
	 * @param istream
	 * @return
	 */
	public static String file2String(InputStream istream) {
		return file2String(new InputStreamReader(istream));
	}
	
	/**
	 * @param r
	 * @return
	 */
	public static String file2String(Reader r) {
		String ret = null;
	    BufferedReader buffReader = new BufferedReader(r);
		try {
			String	line = null;
			StringBuilder  stringBuilder = new StringBuilder();
			while((line = buffReader.readLine()) != null) {
				stringBuilder.append(line);
		        stringBuilder.append('\n');
			}
		    ret = stringBuilder.toString();
		    buffReader.close();
		} catch (IOException e) {} 
		
		return ret;
	    
	}
	
	/**
	 * Returns the lastDigits last string digits
	 * @param str
	 * @param lastDigits
	 * @return
	 */
	public static String last(String str, int lastDigits) {
		String hexStr = null;
		Long hexId = null;
		int sz = str.length();
		if (isBlank(str)) {
			return null;
		}
		else if (str.length() >= lastDigits) {
			hexStr = str.substring(sz - lastDigits);
		}
		else {
			hexStr = str;
			
		}
		
		try {
			hexId = Long.parseLong(hexStr, 16);
		}
		catch (NumberFormatException e) {
			return null;
		}
		
		return String.format("%0" + lastDigits + "x", hexId);
		
	}

}

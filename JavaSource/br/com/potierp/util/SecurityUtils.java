package br.com.potierp.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

public class SecurityUtils {
	
	private static Logger log = Logger.getLogger(SecurityUtils.class);
	
	/**
	 * Criptografa <code>String</code> com o algoritmo MD5.
	 * @param value
	 * @return
	 */
	public static String criptografaStringMD5(String value){
		String transfValue = "";
		MessageDigest md = null;
		try{
			md = MessageDigest.getInstance("MD5");
			BigInteger hash = new BigInteger(1, md.digest(value.getBytes()));
			transfValue = hash.toString(16);
		}catch(NoSuchAlgorithmException nsae){
			log.error(nsae.getMessage(), nsae);
		}		
		return transfValue;
	}

}

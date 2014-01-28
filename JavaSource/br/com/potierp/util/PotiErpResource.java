/**
 * fabio.masson 31/08/2009
 */
package br.com.potierp.util;

/**
 * @author fabio.masson 
 * 31/08/2009 11:29:31
 * 
 * $LastChangedBy: fabio.masson $
 * 
 * $LastChangedDate: 2009-09-28 10:50:44 -0300 (seg, 28 set 2009) $
 * <br>
 * Guarda configuracoes do resourcebundle.
 */
public class PotiErpResource {
	
	/**
	 * Define resource name.
	 */
	private static final String resourceName = "resources"; 

	/**
	 * Construtor privado, classe utilitaria.
	 */
	private PotiErpResource(){}

	/**
	 * @return Resource name do Logos.
	 */
	public static String getResource() {
		return resourceName;
	}

}

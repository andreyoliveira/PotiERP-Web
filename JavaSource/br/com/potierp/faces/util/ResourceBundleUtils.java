package br.com.potierp.faces.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author fabio.masson 
 * 31/08/2009 10:34:04
 * 
 * $LastChangedBy: fabio.masson $
 * 
 * $LastChangedDate: 2009-09-28 10:50:22 -0300 (seg, 28 set 2009) $
 * <br>
 * Classe utilit�ria para Manipula��o de ResourceBundles.
 */
public final class ResourceBundleUtils {

	/**
	 * Construtor privado da classe utilit�ria.
	 */
	private ResourceBundleUtils() {
	}

	/**
	 * Retorna ClasseLoader corrente.
	 * 
	 * @param defaultObject
	 *            Objeto do qual ser� obtido o classLoader caso n�o seja
	 *            poss�vel obter do objeto da Thread corrente.
	 * @return ClassLoader corrente.
	 */
	protected static ClassLoader getCurrentClassLoader(
			final Object defaultObject) {

		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		if (loader == null) {
			loader = defaultObject.getClass().getClassLoader();
		}

		return loader;
	}

	/**
	 * Retorna uma mensagem de um ResourceBundle.
	 * 
	 * @param key
	 *            Chave.
	 * @param params
	 *            Par�metros para formata��o.
	 * @param locale
	 *            Locale para utiliza��o do bundle.
	 * @return Mensagem i18n.
	 */
	public static String getMessageResourceString(final String key,
			final Object[] params, final Locale locale, final String[] resources) {

		String text = null;
		ResourceBundle bundle;
		for (String bundleName : resources) {
			bundle = ResourceBundle.getBundle(bundleName, locale,
					getCurrentClassLoader(params));

			try {
				text = bundle.getString(key);
			} catch (MissingResourceException e) {
				text = null;
			}

			if (text != null) {
				if (params != null) {
					MessageFormat mf = new MessageFormat(text, locale);
					text = mf.format(params, new StringBuffer(), null).toString();
				}
				return text;
			}
		}
		return "?? key " + key + " not found ??"; 
	}

	/**
	 * Retorna o <i>Texto</i> da Mensagem contido no properties (default). Caso nao encontre nenhum retorna messageId.
	 * @param messageId
	 * @param params
	 * @param locale
	 * @return
	 */
	public static String getDisplayText(final String messageId, final Object[] params, final Locale locale,
			final String resourceDefault) {
		return ResourceBundleUtils.getDisplayText(resourceDefault, messageId, params, locale);
	}
	
	/**
	 * Retorna o <i>Texto</i> da Mensagem contido no properties. Caso nao encontre nenhum retorna messageId.
	 * 
	 * @param bundleName
	 * @param id
	 * @param params
	 * @param locale
	 * @return
	 */
	public static String getDisplayText(final String bundleName,
			final String messageId, final Object[] params, final Locale locale) {
		String text = null;
		try {
			ResourceBundle bundle = ResourceBundle.getBundle(bundleName,
					locale, FacesUtil.getCurrentClassLoader(params));
			text = bundle.getString(messageId);
		} catch (MissingResourceException e) {
			text = messageId;
		}
		if (params != null) {
			MessageFormat mf = new MessageFormat(text, locale);
			text = mf.format(params, new StringBuffer(), null).toString();
		}
		return text;
	}

}
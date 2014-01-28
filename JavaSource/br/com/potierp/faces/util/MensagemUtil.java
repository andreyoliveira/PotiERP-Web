package br.com.potierp.faces.util;

import static javax.faces.context.FacesContext.getCurrentInstance;

import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 * @author
 * 
 * $LastChangedBy: $
 * 
 * $LastChangedDate: $
 * <br>
 * Classe utilitária para tratamento de Mensagens.
 */
public class MensagemUtil {

	/**
	 * Construtor default.
	 */
	private MensagemUtil() {}

	/**
	 * @return locale.
	 */
	public static Locale getLocale() {
		Locale locale = null;

		UIViewRoot viewRoot = getCurrentInstance().getViewRoot();
		if (viewRoot != null)
			locale = viewRoot.getLocale();
		if (locale == null)
			locale = Locale.getDefault();

		return locale;
	}
	
	/**
	 * Adiciona uma <b>Mensagem de Erro</b> ao <code>FacesContext</code>.
	 * 
	 * @param message
	 *            Mensagem ou chave da mensagem que vai ser exibida.
	 */
	public static void addMessageError(final String message, final String resourceDefault) {
		addMessageError(message, null, resourceDefault);
	}

	/**
	 * Adiciona uma <b>Mensagem de Erro</b> ao <code>FacesContext</code>.
	 * 
	 * @param message
	 *            Mensagem ou chave da mensagem que vai ser exibida.
	 * @param params
	 *            Parametros que a mensagem exibe.
	 */
	public static void addMessageError(final String message, final Object[] params, final String resourceDefault) {
		addMessage(message, params, FacesMessage.SEVERITY_ERROR, resourceDefault);
	}

	/**
	 * Cria uma mensagem de Erro no FacesContext baseado no
	 * <code>toString()</code> da Exception passada.
	 * 
	 * @param exception
	 */
	public static void addMessageError(final Exception exception, final String resourceDefault) {
		addMessageError(exception.toString(),resourceDefault);
	}

	/**
	 * Adiciona no <code>FacesContext</code> o <code>FacesMessage</code>
	 * baseado na <i>Mensagem</i> passada.
	 * 
	 * @param message
	 *            Mensagem ou chave da mensagem que vai ser exibida.
	 * @param severity
	 *            Repassa para o <code>FacesMessage</code> qual a
	 *            <code>Severity</code>.
	 */
	public static void addMessage(final String message,
			final FacesMessage.Severity severity, final String resourceDefault) {
		addMessage(message, null, severity, resourceDefault);
	}

	/**
	 * Adiciona no <code>FacesContext</code> o <code>FacesMessage</code>
	 * baseado na <i>Mensagem</i> passada.
	 * 
	 * @param message
	 *            Mensagem ou chave da mensagem que vai ser exibida.
	 * @param params
	 *            Parametros que a mensagem exibe.
	 * @param severity
	 *            Repassa para o <code>FacesMessage</code> qual a
	 *            <code>Severity</code>.
	 */
	public static void addMessage(final String message, final Object[] params,
			final FacesMessage.Severity severity, final String resourceDefault) {
		FacesMessage facesMsg = getMessage(message, params, severity, resourceDefault);
		getCurrentInstance().addMessage(null, facesMsg);
	}
	
	

	/**
	 * Retorna <i>FacesMessage</i> conforme os dados de Mensagem e <i>Gravidade</i>
	 * da mensagem.
	 * 
	 * @param message
	 *            Mensagem ou chave da mensagem que vai ser exibida.
	 * @param severity
	 *            Repassa para o <code>FacesMessage</code> qual a
	 *            <code>Severity</code>.
	 * @return
	 */
	public static FacesMessage getMessage(final String message,
			final Object[] params, final FacesMessage.Severity severity, final String resourceDefault) {
		String msg = ResourceBundleUtils.getDisplayText(message, params, getLocale(), resourceDefault);
		FacesMessage facesMsg = new FacesMessage(severity, msg, msg);

		return facesMsg;
	}
	
	/**
	 * Devolve uma mensagem conforme a <i>Key</i> e  o <i>Locale</i>, usando os <i>params</i>.
	 * 
	 * @see br.metodista.siga.util.ResourceBundleUtils#getMessageResourceString(final
	 *      String bundleName, final String key, final Object[] params, final
	 *      Locale locale)
	 */
	public static String getResourceBundleMessage(final String key,
			final Object[] params, final String[] resources) {
		return getResourceBundleMessage(key, params, getLocale(), resources);
	}

	/**
	 * Devolve uma mensagem conforme a <i>Key</i> e  o <i>Locale</i>, usando os <i>params</i>.
	 * 
	 * @see br.metodista.siga.util.ResourceBundleUtils#getMessageResourceString(final
	 *      String bundleName, final String key, final Object[] params, final
	 *      Locale locale)
	 */
	public static String getResourceBundleMessage(final String key,
			final Object[] params, final Locale locale, final String[] resources) {
		return ResourceBundleUtils.getMessageResourceString(key, params, locale, resources);
	}

	/**
	 * Devolve uma mensagem conforme a <i>Key</i>.
	 * 
	 * @see br.metodista.siga.util.ResourceBundleUtils#getMessageResourceString(final
	 *      String bundleName, final String key, final Object[] params, final
	 *      Locale locale)
	 */
	public static String getResourceBundleMessage(final String key, final String[] resources) {
		FacesContext context = FacesContext.getCurrentInstance();

		return getResourceBundleMessage(key,null,context.getViewRoot().getLocale(), resources);
	}
	
	/**
	 * Devolve uma mensagem conforme a <i>Key</i> o <i>Locale</i>.
	 * 
	 * @param key
	 * @param locale
	 * @return
	 */
	public static String getResourceBundleMessage(final String key, final Locale locale, final String[] resources) {
		return getResourceBundleMessage(key, null, locale, resources);
	}
	
}

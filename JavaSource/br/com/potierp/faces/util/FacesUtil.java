package br.com.potierp.faces.util;


import static javax.faces.context.FacesContext.getCurrentInstance;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.potierp.service.exception.ServiceException;

/**
 * @author fabio.masson 
 * 31/08/2009 10:47:07
 * 
 * $LastChangedBy: fabio.masson $
 * 
 * $LastChangedDate: 2009-09-28 10:50:22 -0300 (seg, 28 set 2009) $
 * <br>
 * Classe com opera��es utilitarias ao framework JSF.
 */
public final class FacesUtil {

	/**
	 * Construtor padr�o.
	 */
	private FacesUtil() {}

	/**
	 * @param context
	 * @return IP do usu�rio.
	 */
	public static String getIP(final FacesContext context) {
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		return request.getRemoteAddr();
	}

	/**
	 * Retorna qual a vers�o do browser.
	 * @param context
	 * @return
	 */
	public static String getBrowser(final FacesContext context) {
		List<String> browsers = getParametersContext("browser-version", ",");
		String userAgent = context.getExternalContext().getRequestHeaderMap().get("user-agent");
		if (browsers.isEmpty())
			return userAgent;
		for (String b: browsers) {
			b = b.trim();
			if (userAgent.toUpperCase().indexOf(b) != -1)
				return b;
		}
		return "";
	}

	/**
	 * Retorna uma string com o valor do parametro (ServletContext) solicitado.
	 * @param parameterName
	 * @return
	 */
	public static String getParametersContext(final String parameterName) {
		return getCurrentInstance().getExternalContext().getInitParameter(parameterName);
	}

	/**
	 * Retorna os valores do parametro (ServletContext) solicitado, em uma List. Aplica split utilizando o 
	 * parametro separator.
	 * @param parameterName
	 * @param separator
	 * @return
	 */
	public static List<String> getParametersContext(final String parameterName, final String separator) {
		String values = getParametersContext(parameterName);
		List<String> params = new ArrayList<String>();
		if (values == null || values.equals(""))
			return params;
		for (String s: values.split(separator)){
			params.add(s);
		}
		return Collections.unmodifiableList(params);
	}
	
	/**
	 * Retorna o Managed Bean pelo <code>FacesContext</code>, baseado no Nome
	 * do mapeamento do Bean.
	 * 
	 * @param beanName
	 *            Nome do Managed Bean.
	 * @return Managed Bean.
	 */
	@SuppressWarnings("deprecation")
	public static Object getMBean(final String beanName) {
		StringBuilder mappedName = new StringBuilder().append("#{").append(
				beanName).append("}");
		return getCurrentInstance().getApplication().createValueBinding(
				mappedName.toString()).getValue(getCurrentInstance());
	}

	/**
	 * Retorna o valor a chave da mensagem, no arquivo de mensagens (i18n).
	 * @param facesContext
	 * @param key Chave para localizar a mensagem no arquivo de propriedades.
	 * @return
	 */
	public static String getResourceMessage(final FacesContext facesContext, final String key, final Object[] param, 
			final String[] resources) {
		return MensagemUtil.getResourceBundleMessage(key, param, resources);
	}
	
	/**
	 * Retorna o valor a chave da mensagem, no arquivo de mensagens (i18n).
	 * @param facesContext
	 * @param key Chave para localizar a mensagem no arquivo de propriedades.
	 * @return
	 */
	public static String getResourceMessage(final FacesContext facesContext, final String key, final String[] resources) {
		return MensagemUtil.getResourceBundleMessage(key, resources);
	}

	/**
	 * Retorna FacesMessage do tipo Info.
	 * @param facesContext
	 * @param key
	 * @return
	 */
	public static FacesMessage getMensagemInfo(final FacesContext facesContext, final String key, final String[] resources) {
		String msg = getResourceMessage(facesContext, key, resources);
		return new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
	}

	/**
	 * Retorna FacesMessage do Tipo Info para determinada key respeitando parametros(argumentos da mensagem).
	 * 
	 * @param facesContext
	 * @param key
	 * @param param
	 * @param resources
	 * @return
	 */
	public static FacesMessage getMensagemInfo(final FacesContext facesContext, final String key, final Object[] param, 
			final String[] resources) {
		String msg = getResourceMessage(facesContext, key, param, resources);
		return new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
	}
	
	/**
	 * Pega o locale conforme a arvore de componentes do faces (UIViewRoot).
	 * @return locale.
	 */
	public static Locale getLocale(final FacesContext facesContext) {
		Locale locale = null;
		UIViewRoot viewRoot = facesContext.getViewRoot();
		if (viewRoot != null)
			locale = viewRoot.getLocale();
		if (locale == null)
			locale = Locale.getDefault();

		return locale;
	}

	/**
	 * Retorna FacesMessage de Erro.
	 * @param facesContext
	 * @param e Utiliza ServiceException para encontrar a mensagem no arquivo resource.
	 * @return
	 */
	public static FacesMessage getMensagemErro(final FacesContext facesContext, final ServiceException e, final String[] resources) {
		Object[] params = e.getParams();
		String msg = e.getMessage();
		try {
			msg = getResourceMessage(facesContext, e.getMessage(), resources);
		} catch (Exception ex) {
			msg = e.getMessage();
		}
		
		if (msg != null) {
			if (params != null) {
				MessageFormat mf = new MessageFormat(msg, getLocale(facesContext));
				msg = mf.format(params, new StringBuffer(), null).toString();
			}
		}
		return new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
	}

	/**
	 * Retorna FacesMessage da mensagem de erro.
	 * @param facesContext
	 * @param mensagem Utiliza mensagem da exception para encontrar a mensagem no arquivo resource.
	 * @return
	 */
	public static FacesMessage getMensagemErro(final FacesContext facesContext, final String mensagem, final String[] resources) {
		String msg = mensagem;
		try {
			msg = getResourceMessage(facesContext, mensagem, resources);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
	}
	
	/**
	 * Retorna FacesMessage do Tipo Error para determinada key respeitando parametros(argumentos da mensagem).
	 * @param facesContext
	 * @param key
	 * @param param
	 * @param resources
	 * @return
	 */
	public static FacesMessage getMensagemErro(final FacesContext facesContext, final String key, final Object[] param, 
			final String[] resources) {
		String msg = getResourceMessage(facesContext, key, param, resources);
		return new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
	}
	
	/**
	 * Itera na lista de components, procurando elementos EditableValueHolder para zerar o valor.
	 * @param childList
	 */
	public static void resetInput(final List<UIComponent> components) {
		if (components == null || components.isEmpty())
			return;
		
		for (UIComponent component: components) {
			if (component instanceof EditableValueHolder) {
				((EditableValueHolder)component).setSubmittedValue(null);
				((EditableValueHolder)component).setValue(null);
			}
			List<UIComponent> components2 = component.getChildren();
			resetInput(components2);
		}
	}

	/**
	 * Quando carregamos um Resource Bundle, a classe ResourceBundle faz uma busca por arquivos que sao
	 * Resource Bundle no classpath. Neste caso, queremos que ele procure no classpath do aplicativo web.
	 * Portanto, se n�o invocarmos este metodo, em getDisplayString nao podemos garantir que o metodo
	 * ira funcionar direito a nao ser que esta classe utilitaria estivesse no mesmo diret�rio (WEB-INF) 
	 * do resto da aplicacao.
	 * @param defaultObject
	 * @return
	 */
	public static ClassLoader getCurrentClassLoader(final Object defaultObject) {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader == null) {
			loader = defaultObject.getClass().getClassLoader();
		}
		return loader;
	}
	
	/**
	 * <b>UTILIZA-LO SE ESTIVER TENTANDO RECUPERAR UMA CHAVE NUM AMBIENTE ONDE NAO ESTA SENDO GERENCIADO PELO
	 * FACES OU SE AINDA NAO FOI INICIALIZADO O CICLO DE VIDA DELE (EX.: ContextListener)</b>
	 * Este metodo carrega a bundle usando o metodo estatico ResourceBundle.getBundle, passando uma intancia
	 * de ClassLoader recuperado em getCurrentClassLoader. Ele entao ira recuperar uma String da bundle
	 * para o ID/key passado, usando o metodo ResourceBundle.getString. Se um array de parametros for passado,
	 * inserimo-os numa String usando a classe MessageFormat.
	 * @param bundleName
	 * @param key
	 * @param params
	 * @param locale
	 * @return
	 */
	public static String getResourceMessage(final String bundleName, final String key, 
			final Object[] params, final Locale locale) {
		String text = null;
		ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale, getCurrentClassLoader(params));
		try {
			text = bundle.getString(key);
		} catch (MissingResourceException e) {
			text = "!! chave " + key + " nao encontrada !!";
		}
		if (params != null) {
			MessageFormat mf = new MessageFormat(text, locale);
			text = mf.format(params, new StringBuffer(), null).toString();
		}
		return text;
	}

	/**
	 * @param facesContext
	 * @return HttpServletRequest do facesContext.
	 */
	public static HttpServletRequest getRequest(final FacesContext facesContext) {
		return (HttpServletRequest) facesContext.getExternalContext().getRequest();
	}

	/**
	 * @param facesContext
	 * @return HttpSession do facesContext.
	 */
	public static HttpSession getSession(final FacesContext facesContext) {
		return (HttpSession) facesContext.getExternalContext().getSession(false);
	}

	/**
	 * @param facesContext
	 * @return ServletContext do facesContext.
	 */
	public static ServletContext getContext(final FacesContext facesContext) {
		return (ServletContext) facesContext.getExternalContext().getContext();
	}

	/**
	 * Retorna <i>Objeto</i> da <code>Response</code>.
	 * @return object
	 */
	public static HttpServletResponse getResponse(final FacesContext facesContext) {
		return (HttpServletResponse)facesContext.getExternalContext().getResponse();
	}

}

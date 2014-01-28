package br.com.potierp.faces.managed;

import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import br.com.potierp.faces.converter.MockEnum;
import br.com.potierp.faces.util.FacesUtil;
import br.com.potierp.infra.bd.BaseEntityPotiErp;
import br.com.potierp.infra.exception.PotiErpMensagensException;
import br.com.potierp.infra.file.FileDownloadBean;
import br.com.potierp.infra.file.FileUtils;
import br.com.potierp.infra.log.TraceInfo;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.model.Usuario;
import br.com.potierp.service.exception.ServiceException;
import br.com.potierp.util.AttributeNames;
import br.com.potierp.util.PotiErpResource;

/**
 * Classe Abstrata com operações comum a managedbeans.
 * 
 * @author 
 *  	   <p>
 *         $LastChangedBy: $
 *         <p>
 *         $LastChangedDate: $
 *
 */
public abstract class BaseMB {

	/**
	 * unchecked.
	 */
	protected static final String UNCHECKED = "unchecked";

	/**
	 * Construtor default.
	 */
	public BaseMB() {
		super();
	}

	/**
	 * Recupera o traceInfo.
	 * @return
	 */
	protected TraceInfo getTraceInfo(){
		return (TraceInfo) getSession(AttributeNames.TRACE_INFO.getName());
	}

	/**
	 * @return facesContext.
	 */
	protected FacesContext getFacesContext() {
		return getCurrentInstance();
	}

	/**
	 *
	 * @return application.
	 */
	protected Application getApplication() {
		return getFacesContext().getApplication();
	}
	
	/**
	 * Metodo responsável pela exclusão de todos os objetos da sessão.
	 */
	protected void clearSession() {
		for(AttributeNames attrName: AttributeNames.values()){
			if(!attrName.isPersistent())
				removeSession(attrName);
		}
	}

	/**
	 * Retorna <i>Objeto</i> da <code>Session</code> pelo <i>Nome</i>.
	 *
	 * @param objName
	 *            Filtro.
	 * @return
	 */
	protected Object getSession(final String objName) {
		HttpSession session = FacesUtil.getSession(getFacesContext());
		return session.getAttribute(objName);
	}

	/**
	 * Retorna <i>Objeto</i> da <code>Session</code> pelo <i>Nome</i>.
	 *
	 * @param attrName
	 * 			filtro.
	 * @return
	 */
	protected Object getSession(final AttributeNames attrName) {
		HttpSession session = FacesUtil.getSession(getFacesContext());
		return session.getAttribute(attrName.getName());
	}
	
	/**
	 * Retorna um clone do objeto que está na sessão.
	 * @param <T>
	 * @param attrName
	 * @return
	 * @throws CloneNotSupportedException
	 */
	@SuppressWarnings("unchecked")
	protected <T extends BaseEntityPotiErp & Cloneable> T getSessionClone(final AttributeNames attrName) throws CloneNotSupportedException{
		HttpSession session = FacesUtil.getSession(getFacesContext());
		return (T) ((T)session.getAttribute(attrName.getName())).clone();
	}

	/**
	 * Coloca <i>Objeto</i> na <code>Session</code>.
	 *
	 * @param objName
	 *            Nome do Objeto.
	 * @param objSession
	 *            Objeto.
	 */
	protected void setSession(final String objName, final Object objSession) {
		HttpSession session = FacesUtil.getSession(getFacesContext());
		session.setAttribute(objName, objSession);
	}

	/**
	 * Coloca <i>Objeto</i> na <code>Session</code>.
	 * @param attrName
	 * 			nome do objeto
	 * @param objSession
	 * 			objeto
	 */
	protected void setSession(final AttributeNames attrName, final Object objSession) {
		HttpSession session = FacesUtil.getSession(getFacesContext());
		session.setAttribute(attrName.getName(), objSession);
	}

	/**
	 * @return Id da Session.
	 */
	protected String getSessionId() {
		HttpSession session = FacesUtil.getSession(getFacesContext());
		if (session == null)
			return "";
		return session.getId();
	}

	/**
	 * Retorna <i>Objeto</i> do <code>Request</code> pelo <i>Nome</i>.
	 *
	 * @param objName
	 *            Filtro.
	 * @return
	 */
    protected Object getRequest(final String objName) {
		HttpServletRequest request = FacesUtil.getRequest(getFacesContext());
		return request.getAttribute(objName);
	}

    /**
     * Colocar <i>Objeto</i> do <code>Request</code>.
     *
     * @param objName
     *            Nome do objeto.
     * @param objValue
     *            Valor do objeto.
     * @return
     */
    protected void setRequest(final String objName, final Object objValue) {
        HttpServletRequest req = (HttpServletRequest) getFacesContext().getExternalContext().getRequest();
        req.setAttribute(objName, objValue);
    }

	/**
	 * Retorna valor do parametro de request solicitado conforme o nome.
	 * Retorna null se o parametro nao existir.
	 * @param parameterName
	 * @return
	 */
	protected String getRequestParameter(final String parameterName) {
		HttpServletRequest request = FacesUtil.getRequest(getFacesContext());
		return request.getParameter(parameterName);
	}

	/**
	 * Retorna <i>Objeto</i> do <code>ServletContext</code> pelo <i>Nome</i>.
	 *
	 * @param objName
	 *            Filtro.
	 * @return
	 */
    protected Object getContext(final String objName) {
    	ServletContext context = FacesUtil.getContext(getFacesContext());
		return context.getAttribute(objName);
	}

    /**
     * Recupera uma mensagem do resource.
     * @param key
     * @return
     */
    protected String getMensagem(final String key) {
		return FacesUtil.getResourceMessage(FacesContext.getCurrentInstance(), key, getResources());
	}

	/**
     * Remove um objeto da sessão.
     * @param objName Nome do objeto a ser removido.
     */
    protected void removeSession(final String objName) {
        HttpSession session = FacesUtil.getSession(getFacesContext());
        session.removeAttribute(objName);
    }

    /**
     * Remove um objeto da sessão.
     * @param attrName Nome do objeto a ser removido.
     */
    protected void removeSession(final AttributeNames attrName) {
        HttpSession session = FacesUtil.getSession(getFacesContext());
        session.removeAttribute(attrName.getName());
    }

	/**
	 * Adiciona uma mensagem de informacao no contexto.
	 * @param msg
	 */
	protected void addMensagemInformativa(final String msg) {
		getFacesContext().addMessage(null, FacesUtil.getMensagemInfo(getFacesContext(), msg, getResources()));
	}


	/**
	 * Adiciona uma mensagem de informacao no contexto.
	 * @param mensagensFacesEnum
	 */
	protected void addMensagemInformativa(final MensagensFacesEnum mensagensFacesEnum) {
		getFacesContext().addMessage(null, FacesUtil.getMensagemInfo(getFacesContext(), mensagensFacesEnum.getKey(), getResources()));
	}

	/**
	 * Adiciona uma mensagem de informacao no contexto.
	 * @param msg
	 */
	protected void addMensagemInformativa(final String msg, final Object[] param) {
		getFacesContext().addMessage(null, FacesUtil.getMensagemInfo(getFacesContext(), msg, param, getResources()));
	}

	/**
	 * Adiciona uma mensagem de erro no contexto a partir de um ServiceException (getParam).
	 * @param e
	 */
	protected void addMensagemErro(final ServiceException se) {
		getFacesContext().addMessage(null, FacesUtil.getMensagemErro(getFacesContext(), se, getResources()));
	}

	/**
	 * Adiciona uma mensagem de erro no contexto a partir de uma LogosMensagensException.
	 * @param rme
	 */
	protected void addMensagemErro(final PotiErpMensagensException rme) {
		for (String key: rme.getMensagens().keySet()) {
			getFacesContext().addMessage(null,
					FacesUtil.getMensagemErro(getFacesContext(), key, rme.getMensagens().get(key), getResources()));
		}
	}

	/**
	 * Adiciona uma mensagem informativa no contexto a partir de uma LogosMensagensException.
	 * @param rme
	 */
	protected void addMensagemInformativa(final PotiErpMensagensException rme) {
		for (String key: rme.getMensagens().keySet()) {
			getFacesContext().addMessage(null,
					FacesUtil.getMensagemInfo(getFacesContext(), key, rme.getMensagens().get(key), getResources()));
		}
	}

	/**
	 * Adiciona uma mensagem de erro no contexto a partir de um exception (getMessage()).
	 * @param e
	 */
	protected void addMensagemErro(final Exception e) {
		if (e.getCause() instanceof ServiceException) {
			ServiceException se = (ServiceException) e.getCause();
			if (se.getParams() != null) {
				addMensagemErro(se);
			}
			return;
		}
		if (e instanceof ServiceException) {
			addMensagemErro((ServiceException)e);
			return;
		}
		getFacesContext().addMessage(null, FacesUtil.getMensagemErro(getFacesContext(),
				e.getMessage() != null ? e.getMessage() : e.toString(),getResources()));
	}

	/**
	 * Adiciona uma mensagem de erro no contexto a partir de um exception (getMessage()).
	 * @param e
	 */
	protected void addMensagemErro(final String clientId, final Exception e) {
		if (e.getCause() instanceof ServiceException) {
			ServiceException se = (ServiceException) e.getCause();
			if (se.getParams() != null) {
				getFacesContext().addMessage(clientId, FacesUtil.getMensagemErro(getFacesContext(),
						se.getMessage() != null ? se.getMessage() : se.toString(),getResources()));
			}
			return;
		}
		if (e instanceof ServiceException) {
			getFacesContext().addMessage(clientId, FacesUtil.getMensagemErro(getFacesContext(),
					e.getMessage() != null ? e.getMessage() : e.toString(),getResources()));
			return;
		}
		getFacesContext().addMessage(clientId, FacesUtil.getMensagemErro(getFacesContext(),
				e.getMessage() != null ? e.getMessage() : e.toString(),getResources()));
	}

	/**
	 * Adiciona um mock na lista de itens.
	 * @param lista
	 * @param mock
	 */
	public void addMock(final List<SelectItem> lista, final MockEnum mock){
		lista.add(new SelectItem(mock, getMensagem(mock.getMockKey())));
	}

	/**
	 * Adiciona um mock na lista de itens.
	 *
	 * @param lista
	 * @param mock
	 */
	public void addMock(final Collection<SelectItem> lista, final MockEnum mock){
		lista.add(new SelectItem(mock, getMensagem(mock.getMockKey())));
	}

	/**
	 * Adiciona um mock na lista de itens.
	 * @param lista
	 * @param mock
	 */
	public void addMockSimple(final List<SelectItem> lista, final MockEnum mock){
		lista.add(new SelectItem(null, getMensagem(mock.getMockKey())));
	}

	/**
	 * Adiciona uma mensagem de erro no contexto a patir de um string.
	 * @param e
	 */
	protected void addMensagemErro(final String messageException) {
		getFacesContext().addMessage(null, FacesUtil.getMensagemErro(getFacesContext(), messageException,getResources()));
	}

	/**
	 * Adiciona uma mensagem de erro no contexto a patir de um string.
	 *
	 * @param mensagensFacesEnum
	 */
	protected void addMensagemErro(final MensagensFacesEnum mensagensFacesEnum) {
		getFacesContext().addMessage(null, FacesUtil.getMensagemErro(getFacesContext(), mensagensFacesEnum.getKey(),getResources()));
	}

	/**
	 * Adiciona uma mensagem de erro no contexto a patir de um string (Associada a um campo).
	 * @param clientId
	 * @param messageException
	 */
	protected void addMensagemErro(final String clientId, final String messageException) {
		getFacesContext().addMessage(clientId, FacesUtil.getMensagemErro(getFacesContext(), messageException,getResources()));
	}

	/**
	 * Adiciona uma mensagem de informacao no contexto.
	 *
	 * @param mensagensFacesEnum
	 * @param param
	 */
	protected void addMensagemErro(final MensagensFacesEnum mensagensFacesEnum, final Object[] param) {
		if (param == null || param.length == 0) {
			addMensagemErro(mensagensFacesEnum.getKey());
			return;
		}
		getFacesContext().addMessage(null, FacesUtil.getMensagemErro(getFacesContext(), mensagensFacesEnum.getKey(), param, getResources()));
	}


	/**
	 * Adiciona uma mensagem de informacao no contexto.
	 * @param msg
	 */
	protected void addMensagemErro(final String msg, final Object[] param) {
		if (param == null || param.length == 0) {
			addMensagemErro(msg);
			return;
		}
		getFacesContext().addMessage(null, FacesUtil.getMensagemErro(getFacesContext(), msg, param, getResources()));
	}

	/**
	 * Adiciona uma mensagem de informacao no contexto.
	 * @param clientId
	 * @param msg
	 * @param param
	 */
	protected void addMensagemErro(final String clientId, final String msg, final Object[] param) {
		if (param == null || param.length == 0) {
			addMensagemErro(clientId, msg);
			return;
		}
		getFacesContext().addMessage(clientId, FacesUtil.getMensagemErro(getFacesContext(), msg, param, getResources()));
	}

	/**
	 * @return Elementos da arvore de componentes do faces.
	 */
	protected List<UIComponent> getViewRootComponents() {
		UIViewRoot viewRoot = getFacesContext().getViewRoot();
		return viewRoot.getChildren();
	}

	/**
	 * @return array de String com nome dos resource bundle da aplicacao.
	 */
	protected String[] getResources(){
		return new String[]{PotiErpResource.getResource()};
	}

	/**
	 * Registra o arquivo para download.
	 * @param fileName
	 * @param filePath
	 */
	public void registraArquivoParaDownload(final byte[] conteudoArquivo,
				final String nomePadraoArquivo, final String extensao,
				final String pathArquivo) throws IOException {
		String nomeArquivo = FileUtils.gerarNomeUnicoArquivo(nomePadraoArquivo, extensao);
		FileUtils.writeFile(conteudoArquivo, nomeArquivo, pathArquivo);
		FileDownloadBean downloadBean = new FileDownloadBean();
		downloadBean.setFileName(nomeArquivo);
		downloadBean.setFilePath(pathArquivo);
		setSession(AttributeNames.UMESP_DOWNLOAD_FILE.getName(), downloadBean);
	}

	/**
	 * Método responsável por limpar os componentes de um formulario.
	 * @param pComponent
	 */
	public static void limparComponentesFormulario(final UIComponent pComponent) {
		if (pComponent instanceof EditableValueHolder) {
			EditableValueHolder editableValueHolder = (EditableValueHolder) pComponent;
			editableValueHolder.setSubmittedValue(null);
			editableValueHolder.setValue(null);
			editableValueHolder.setValid(true);
		}
		for (UIComponent child : pComponent.getChildren()) {
			limparComponentesFormulario(child);
		}
	}
	
	protected void cleanSubmittedValues(final UIComponent component) {
		if (component instanceof EditableValueHolder) {
			EditableValueHolder evh = (EditableValueHolder) component;
			evh.setSubmittedValue(null);
			evh.setValue(null);
			evh.setLocalValueSet(false);
			evh.setValid(true);
		}
		if (component != null && component.getChildCount() > 0) {
			for (UIComponent child : component.getChildren()) {
				cleanSubmittedValues(child);
			}
		}
	}

	/**
	 * Verifica se o download de arquivos est� habilitado.
	 * @return
	 */
	public boolean isDownloadHabilitado() {
		return getSession(AttributeNames.UMESP_DOWNLOAD_FILE.getName()) != null;
	}
	
	/**
     * @return <i>Usuario</i> da <code>Session</code>, se existir.
     */
    public Usuario getUsuario() {
        return (Usuario) this.getSession(AttributeNames.USUARIO.getName());
    }

    /**
     * @param pessoa
     * Configura o <i>Usuario</i> na <code>Session</code>, e gera o TraceInfo conforme a sigla de perfil selecionado.
     */
    protected void setUsuario(final Usuario usuario) {
        setSession(AttributeNames.USUARIO.getName(), usuario);
       	setTraceInfo(usuario);
    }

	private void setTraceInfo(final Usuario usuario) {
		HttpSession session = FacesUtil.getSession(getFacesContext());
		HttpServletRequest request = FacesUtil.getRequest(getFacesContext());
		
		if (session != null) {
			TraceInfo trace = (TraceInfo) session.getAttribute(AttributeNames.TRACE_INFO.getName());
			if (trace == null) {
				trace = configTraceInfo(request, usuario);
				session.setAttribute(AttributeNames.TRACE_INFO.getName(), trace);
			}
		}
	}
	
    /**
     * Gera o TraceInfo e seta na sessão.
     *
     * @param request
     * @param loggedUser
     */
	private TraceInfo configTraceInfo(final HttpServletRequest request, final Usuario usuario) {
		String ip = request.getRemoteAddr();
        String browser = getBrowser(request);
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        Date dataAtual = new Date();
        TraceInfo trace = new TraceInfo(dataAtual, usuario,
				"PotiErp", ip, browser, sessionId, "");
        return trace;
	}

	/**
	 * Retorna qual a versão do browser.
	 *
	 * @param context
	 * @return
	 */
	private static String getBrowser(final HttpServletRequest request) {
		List<String> browsers = getParametersContext(request, "browser-version", ",");
		String userAgent = request.getHeader("user-agent");
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
	 * Retorna os valores do parametro (ServletContext) solicitado, em uma List.
	 * Aplica split utilizando o parametro separator.
	 *
	 * @param request
	 * @param parameterName
	 * @param separator
	 * @return
	 */
	private static List<String> getParametersContext(final HttpServletRequest request, final String parameterName, final String separator) {
		ServletContext servletContext = request.getSession().getServletContext();
		String values = servletContext.getInitParameter(parameterName);
		List<String> params = new ArrayList<String>();
		if (values == null || values.equals(""))
			return params;
		for (String s: values.split(separator)){
			params.add(s);
		}
		return Collections.unmodifiableList(params);
	}

	/**
	 * @return Logger do ManagedBean.
	 */
	public abstract Logger getLogger();

}
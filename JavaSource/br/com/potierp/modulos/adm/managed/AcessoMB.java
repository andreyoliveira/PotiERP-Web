package br.com.potierp.modulos.adm.managed;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import br.com.potierp.business.adm.facade.AdmModulo;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.faces.util.MensagemUtil;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.model.Usuario;
import br.com.potierp.util.AttributeNames;
import br.com.potierp.util.NavigationEnum;
import br.com.potierp.util.SecurityUtils;

public class AcessoMB extends BaseMB {
	
	private static Logger log = Logger.getLogger(AcessoMB.class);
	
	public AcessoMB(){
	}
	
	private String username;
	
	private String password;
	
	@EJB
	private AdmModulo moduloAdministrativo;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Logger getLogger() {
		return log;
	}
	
	public String doEfetuarLogin(){
		Usuario usuario = new Usuario(this.username, SecurityUtils.criptografaStringMD5(this.password));
		try{
			usuario = moduloAdministrativo.autenticarUsuario(usuario);
			super.setUsuario(usuario);
			return getCaminhoSistema(usuario);
		}catch(PotiErpException ex){
			addMensagemErro(MensagensFacesEnum.ERRO_USUARIO_E_OU_SENHA_INVALIDOS);
		}
		return "";
	}
	
	private String getCaminhoSistema(Usuario usuario) {
		if(usuario.isSenhaExpirada()){
			return NavigationEnum.ALTERACAO_SENHA.getValor();
		}
		return NavigationEnum.PRINCIPAL.getValor();
	}

	/**
	 * Carrega a tela de login.
	 * @return
	 */
	public String doAcessarLogin() {
		super.removeSession(AttributeNames.USUARIO.getName());
		clearSession();
		return NavigationEnum.LOGIN.getValor();
	}
	
	/**
	 * Desloga e redireciona para a pagina devida.
	 * @throws IOException 
	 */
	public String doLogout() throws IOException {
		HttpSession session = (HttpSession)getFacesContext().getExternalContext().getSession(false);
		session.invalidate();
		HttpServletResponse response = (HttpServletResponse) getFacesContext().getExternalContext().getResponse();
		response.sendRedirect(MensagemUtil.getResourceBundleMessage("logout.urlRedirect", getResources()));
		//TODO [FELIPE] Acertar a parte de logout, a URL, RESOURCES.
 		getFacesContext().responseComplete();
		return "";
	}
	
}
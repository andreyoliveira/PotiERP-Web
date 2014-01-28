package br.com.potierp.modulos.adm.managed;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import br.com.potierp.business.adm.facade.AdmModulo;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.exception.PotiErpMensagensException;
import br.com.potierp.infra.helper.SelectionEntity;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.model.TipoPerfilErp;
import br.com.potierp.model.Usuario;
import br.com.potierp.util.DateUtil;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;
import br.com.potierp.util.SecurityUtils;

public class UsuarioMB extends BaseMB{

	private static Logger log = Logger.getLogger(UsuarioMB.class);
	
	@EJB
	private AdmModulo moduloAdministrativo;
	
	private List<TipoPerfilErp> listaTipoPerfilDisponiveis = new LinkedList<TipoPerfilErp>();
	
	private List<SelectItem> listaTipoPerfilDisponiveisSI = new LinkedList<SelectItem>();
	
	private Usuario usuario = new Usuario();
	
	private String passwordAtual;
	
	private SelectionList<Usuario> listUsuario = new SelectionList<Usuario>();
	
	private boolean checkUsuarioAll = false;
	
	private String filtroDataExpiraSenha = "";
	
	private String filtroUsername = "";
	
	private Integer scrollerPage = 1;
	
	public List<TipoPerfilErp> getListaTipoPerfilDisponiveis() {
		if(listaTipoPerfilDisponiveis == null || listaTipoPerfilDisponiveis.isEmpty()){
			carregarListaTipoPerfilDisponiveis();
		}
		return listaTipoPerfilDisponiveis;
	}

	public void setListaTipoPerfilDisponiveis(List<TipoPerfilErp> listaTipoPerfilDisponiveis) {
		this.listaTipoPerfilDisponiveis = listaTipoPerfilDisponiveis;
	}
	
	public List<SelectItem> getListaTipoPerfilDisponiveisSI() {
		if(listaTipoPerfilDisponiveisSI == null){
			listaTipoPerfilDisponiveisSI = new LinkedList<SelectItem>();
		}
		if(listaTipoPerfilDisponiveisSI.isEmpty()){
			for(TipoPerfilErp tipoPerfil : getListaTipoPerfilDisponiveis()){
				listaTipoPerfilDisponiveisSI.add(new SelectItem(tipoPerfil, tipoPerfil.getDescricao()));
			}
		}
		return listaTipoPerfilDisponiveisSI;
	}

	public void setListaTipoPerfilDisponiveisSI(
			List<SelectItem> listaTipoPerfilDisponiveisSI) {
		this.listaTipoPerfilDisponiveisSI = listaTipoPerfilDisponiveisSI;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
	public String getPasswordAtual() {
		return passwordAtual;
	}

	public void setPasswordAtual(String passwordAtual) {
		this.passwordAtual = passwordAtual;
	}

	private void carregarListaTipoPerfilDisponiveis() {
		try{
			this.listaTipoPerfilDisponiveis = new LinkedList<TipoPerfilErp>(moduloAdministrativo.buscarTodosTipoPerfil());
		}catch(PotiErpException e){
			this.addMensagemErro(MensagensFacesEnum.ERRO_BUSCAR_PERFIS);
			log.error(e.getMessage(), e);
		}
		
	}

	public Logger getLogger() {
		return log;
	}
	
	public void doExcluir() {
		try {
			moduloAdministrativo.excluirUsuario(usuario);
			doNovo();
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_EXCLUIR);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public void doExcluirLote(){
		try {
			List<Usuario> list = listUsuario.getItensSelecionados();
			moduloAdministrativo.excluirListaUsuarios(list);
			doNovo();
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_EXCLUIR_LISTA);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public void doSalvar() {
		try{
			if(isDigitouSenhasDiferentes(this.usuario.getPassword(), this.usuario.getRetypePassword())){
				return;
			}
			boolean isNew = this.usuario.isNew();
			if(isNew){
				usuario.setPassword(SecurityUtils.criptografaStringMD5(this.usuario.getPassword()));
			}
			Usuario usuarioCadastrado = moduloAdministrativo.salvarUsuario(usuario);
			if(usuarioCadastrado != null && usuarioCadastrado.getId() != null && usuarioCadastrado.getId() > 0){
				if(isNew){
					addMensagemInformativa(MensagensFacesEnum.SUCESSO_INSERIR);
				}else{
					addMensagemInformativa(MensagensFacesEnum.SUCESSO_ALTERAR);
				}
				doNovo();
			}else{
				addMensagemErro(MensagensFacesEnum.ERRO_SALVAR);
			}
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_SALVAR);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	private boolean isDigitouSenhasDiferentes(String password, String retypePassword){
		if(password != null && retypePassword != null){
			if(!password.trim().equals(retypePassword.trim())){
				addMensagemErro(MensagensFacesEnum.ERRO_SENHAS_DIFERENTES);
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	
	@SuppressWarnings("unchecked")
	public boolean filtraDataExpiraSenha(Object current){
		Usuario usuarioAtual = ((SelectionEntity<Usuario>) current).getEntity();
		Date dataAtual = usuarioAtual.getDataExpiraSenha();
		if (filtroDataExpiraSenha == null || filtroDataExpiraSenha.length()==0) {
			return true;
	    }
		
		if(dataAtual == null)
			return false;
		
		if(DateUtil.dateToPtBrString(dataAtual).startsWith(filtroDataExpiraSenha.toString())){
			return true;
		}else{
			return false;
		} 
	}
	
	@SuppressWarnings("unchecked")
	public boolean filtraUsername(Object current){
		Usuario usuarioAtual = ((SelectionEntity<Usuario>) current).getEntity();
		String username = usuarioAtual.getUsername();
		if (filtroUsername == null || filtroUsername.length()==0) {
			return true;
	    }
        if (username.toLowerCase().startsWith(filtroUsername.toLowerCase())) {
            return true;
        }else {
            return false; 
        }
	}

	
	public void doAlterarSenha(){
		try{
			if(isDigitouSenhasDiferentes(this.usuario.getPassword(), this.usuario.getRetypePassword())){
				return;
			}
			Usuario usuarioTemp = moduloAdministrativo.buscarUsuarioPorId(usuario.getId());
			usuario = moduloAdministrativo.alterarSenhaDoUsuario(usuarioTemp, SecurityUtils.criptografaStringMD5(this.usuario.getPassword()));
			addMensagemInformativa(MensagensFacesEnum.SUCESSO_ALTERAR_SENHA);
		}catch(PotiErpException e){
			addMensagemErro(MensagensFacesEnum.ERRO_ALTERAR_SENHA);
		}
	}
	
	public void clean(){
		this.usuario = new Usuario();
		this.listaTipoPerfilDisponiveis.clear();
		this.listaTipoPerfilDisponiveisSI.clear();
		this.usuario.setPerfis(new ArrayList<TipoPerfilErp>());
		checkUsuarioAll = false;
		this.filtroDataExpiraSenha = "";
		this.filtroUsername = "";
	}
	
	public void doNovo(){
		clean();		
		
	}
	
	public SelectionList<Usuario> getListUsuario() {
		doConsultar();
		return listUsuario;
	}

	public void setListUsuario(SelectionList<Usuario> listUsuario) {
		this.listUsuario = listUsuario;
	}
	
	public void doConsultar(){
		try {
			List<Usuario> list = moduloAdministrativo.buscarTodosUsuarios();
			listUsuario = new SelectionList<Usuario>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}

	public boolean isCheckUsuarioAll() {
		return checkUsuarioAll;
	}

	public void setCheckUsuarioAll(boolean checkUsuarioAll) {
		this.checkUsuarioAll = checkUsuarioAll;
	}

	public String getFiltroDataExpiraSenha() {
		return filtroDataExpiraSenha;
	}

	public void setFiltroDataExpiraSenha(String filtroDataExpiraSenha) {
		this.filtroDataExpiraSenha = filtroDataExpiraSenha;
	}

	public String getFiltroUsername() {
		return filtroUsername;
	}

	public void setFiltroUsername(String filtroUsername) {
		this.filtroUsername = filtroUsername;
	}

	public Integer getScrollerPage() {
		return scrollerPage;
	}

	public void setScrollerPage(Integer scrollerPage) {
		this.scrollerPage = scrollerPage;
	}
	
	public String doCadastroDeUsuario(){
		return NavigationEnum.CADASTRO_DE_USUARIO.getValor();
	}
	
	public boolean isAlterarSenhaUsuario(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_SENHA_USUARIO.getCodigo());
	}
	
	public boolean isIncluirUsuario(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_USUARIO.getCodigo());
	}
	
	public boolean isAlterarUsuario(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_USUARIO.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_USUARIO.getCodigo());
	}
	
	public boolean isExcluirUsuario(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_USUARIO.getCodigo());
	}
	
	public boolean isConsultarUsuario(){
		return isIncluirUsuario() || isExcluirUsuario() || isAlterarUsuario() || isConsultar();
	}
	
	public boolean isManterUsuario(){
		return isIncluirUsuario() || isAlterarUsuario();
	}
}
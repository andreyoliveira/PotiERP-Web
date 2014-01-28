package br.com.potierp.modulos.adm.managed;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.EJB;

import org.apache.log4j.Logger;

import br.com.potierp.business.adm.facade.AdmModulo;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.exception.PotiErpMensagensException;
import br.com.potierp.infra.helper.SelectionEntity;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.model.Funcionalidade;
import br.com.potierp.model.TipoPerfilErp;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class PerfilMB extends BaseMB {
	
	private static Logger log = Logger.getLogger(PerfilMB.class);
	
	private TipoPerfilErp perfil = new TipoPerfilErp();
	
	private SelectionList<TipoPerfilErp> listPerfil = new SelectionList<TipoPerfilErp>();
	
	private SelectionList<Funcionalidade> listaFuncionalidade = new SelectionList<Funcionalidade>();
	
	@EJB
	private AdmModulo moduloAdm;
	
	private boolean checkPerfilAll = false;
	
	private Integer scrollerPage = 1;
	
	private List<Funcionalidade> listaFuncionalidadesDisponiveis = new ArrayList<Funcionalidade>();
	
	public PerfilMB(){
		
	}
	
	public TipoPerfilErp getPerfil() {
		return perfil;
	}

	public void setPerfil(TipoPerfilErp perfil) {
		this.perfil = perfil;
	}

	public SelectionList<TipoPerfilErp> getListPerfil() {
		doConsultar();
		return listPerfil;
	}

	public void setListPerfil(SelectionList<TipoPerfilErp> listPerfil) {
		this.listPerfil = listPerfil;
	}
	

	public SelectionList<Funcionalidade> getListaFuncionalidade() {
		carregarFuncionalidadesDisponiveis();
		return listaFuncionalidade;
	}

	public void setListaFuncionalidade(
			SelectionList<Funcionalidade> listaFuncionalidade) {
		this.listaFuncionalidade = listaFuncionalidade;
	}

	public boolean isCheckPerfilAll() {
		return checkPerfilAll;
	}

	public void setCheckPerfilAll(boolean checkPerfilAll) {
		this.checkPerfilAll = checkPerfilAll;
	}

	public Integer getScrollerPage() {
		return scrollerPage;
	}

	public void setScrollerPage(Integer scrollerPage) {
		this.scrollerPage = scrollerPage;
	}

	public void doNovo(){
		perfil = new TipoPerfilErp();
		perfil.setFuncionalidades(new LinkedList<Funcionalidade>());
		listaFuncionalidadesDisponiveis.clear();
		checkPerfilAll = false;
	}
	
	public void doSalvar(){
		try {
			boolean isNew = perfil.isNew();
			perfil.setFuncionalidades(listaFuncionalidade.getItensSelecionados());
			perfil = moduloAdm.salvarTipoPerfil(perfil);
			if(perfil != null && perfil.getId() != null && perfil.getId() > 0){
				if(isNew){
					addMensagemInformativa(MensagensFacesEnum.SUCESSO_INSERIR);
				}else{
					addMensagemInformativa(MensagensFacesEnum.SUCESSO_ALTERAR);
				}
			}
			doNovo();
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_SALVAR);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public void doExcluir(){
		try {
			moduloAdm.excluirTipoPerfil(perfil);
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
			List<TipoPerfilErp> list = listPerfil.getItensSelecionados();
			moduloAdm.excluirListaPerfis(list);
			doNovo();
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_EXCLUIR_LISTA);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public void doConsultar(){
		try {
			List<TipoPerfilErp> list = moduloAdm.buscarTodosTipoPerfil();
			listPerfil = new SelectionList<TipoPerfilErp>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}

	public Logger getLogger() {
		return log;
	}
	
	public List<Funcionalidade> getListaFuncionalidadesDisponiveis() {
		if(listaFuncionalidadesDisponiveis == null || listaFuncionalidadesDisponiveis.isEmpty()){
			carregarFuncionalidadesDisponiveis();
		}
		return listaFuncionalidadesDisponiveis;
	}

	public void setListaFuncionalidadesDisponiveis(List<Funcionalidade> listaFuncionalidadesDisponiveis) {
		this.listaFuncionalidadesDisponiveis = listaFuncionalidadesDisponiveis;
	}

	private void carregarFuncionalidadesDisponiveis() {
		try{
			List<Funcionalidade> funcionalidadesDisponiveis = moduloAdm.buscarTodasFuncionalidades();
			this.listaFuncionalidade = new SelectionList<Funcionalidade>(funcionalidadesDisponiveis);
			if(this.perfil != null && this.perfil.getFuncionalidades() != null && !this.perfil.getFuncionalidades().isEmpty()){
				for(Funcionalidade funcionalidadePerfil : this.perfil.getFuncionalidades()){
					for(SelectionEntity<Funcionalidade> selection : listaFuncionalidade){
						if(funcionalidadePerfil.equals(selection.getEntity())){
							selection.setSelecionado(true);
							break;
						}
					}
				}
			}
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public String doCadastroDePerfil(){
		return NavigationEnum.CADASTRO_DE_PERFIL.getValor();
	}
	
	public boolean isIncluirPerfil(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_PERFIL.getCodigo());
	}
	
	public boolean isAlterarPerfil(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_PERFIL.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_PERFIL.getCodigo());
	}
	
	public boolean isExcluirPerfil(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_PERFIL.getCodigo());
	}
	
	public boolean isConsultarPerfil(){
		return isIncluirPerfil() || isExcluirPerfil() || isAlterarPerfil() || isConsultar();
	}
	
	public boolean isManterPerfil(){
		return isIncluirPerfil() || isAlterarPerfil();
	}
	
}
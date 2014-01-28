package br.com.potierp.modulos.adm.managed;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.apache.log4j.Logger;

import br.com.potierp.business.adm.facade.AdmModulo;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.exception.PotiErpMensagensException;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.model.Funcionalidade;
import br.com.potierp.model.TipoPerfilErp;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class PerfilNovoMB extends BaseMB {
	
	private static Logger log = Logger.getLogger(PerfilMB.class);

	private TipoPerfilErp perfil = new TipoPerfilErp();
	
	private TipoPerfilErp perfilSelecionado = new TipoPerfilErp();
	
	private SelectionList<TipoPerfilErp> listPerfil = new SelectionList<TipoPerfilErp>();
	
	private SelectionList<Funcionalidade> listFuncionalidade = new SelectionList<Funcionalidade>();
	
	private SelectionList<Funcionalidade> listFuncionalidadesSelecionadas = new SelectionList<Funcionalidade>();
	
	private Boolean disableEdicao;
	
	@EJB
	private AdmModulo admModulo;
	
	public void doNovo() throws PotiErpException {
		this.perfil = new TipoPerfilErp();
		perfil.setFuncionalidades(new ArrayList<Funcionalidade>());
		this.listFuncionalidade.clear();
		this.listFuncionalidadesSelecionadas.clear();
		carregarItensFuncionalidades();
		this.disableEdicao = false;
	}
	
	public void doExcluir() {
		try {
			if(this.perfil != null && this.perfil.getId() != null) {
				this.admModulo.excluirTipoPerfil(this.perfil);
			}
			doNovo();
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_EXCLUIR);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public void doExcluirLote() {
		try {
			List<TipoPerfilErp> perfisSelecionados = this.listPerfil.getItensSelecionados();
			this.admModulo.excluirListaPerfis(perfisSelecionados);
			doNovo();
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_EXCLUIR_LISTA);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public void doAdicionarItemFuncionalidade() {
		List<Funcionalidade> funcionalidadesSelecionadas = this.listFuncionalidade.getItensSelecionados();
		if(!funcionalidadesSelecionadas.isEmpty()) {
		
			List<Funcionalidade> funcionalidadesJaSelecionadas = new ArrayList<Funcionalidade>();
			funcionalidadesJaSelecionadas = this.listFuncionalidadesSelecionadas.getAllItens();
			
			for(Funcionalidade f : funcionalidadesSelecionadas) {
				funcionalidadesJaSelecionadas.add(f);
			}
			
			this.listFuncionalidadesSelecionadas.clear();
			this.listFuncionalidadesSelecionadas = new SelectionList<Funcionalidade>(funcionalidadesJaSelecionadas);
			this.listFuncionalidade.removeSelectedItem();
		} else {
			addMensagemErro(MensagensFacesEnum.ERRO_SELECIONE_UM_ITEM_DA_LISTAGEM);
		}
	}
	
	public void doAdicionarTodasFuncionalidades() {
		List<Funcionalidade> funcionalidades = this.listFuncionalidade.getAllItens();
		if(!funcionalidades.isEmpty()) {
		
			List<Funcionalidade> funcionalidadesJaSelecionadas = new ArrayList<Funcionalidade>();
			funcionalidadesJaSelecionadas = this.listFuncionalidadesSelecionadas.getAllItens();
			
			for(Funcionalidade f : funcionalidades) {
				funcionalidadesJaSelecionadas.add(f);
			}
			
			this.listFuncionalidadesSelecionadas.clear();
			this.listFuncionalidadesSelecionadas = new SelectionList<Funcionalidade>(funcionalidadesJaSelecionadas);
			this.listFuncionalidade.clear();
		} else {
			addMensagemErro(MensagensFacesEnum.ERRO_NAO_HA_ITENS_A_SELECIONAR, new Object[] {"Funcionalidade"});
		}
	}
	
	public void doRemoverItemFuncionalidade() {
		List<Funcionalidade> funcionalidadesSelecionadas = this.listFuncionalidadesSelecionadas.getItensSelecionados();
		if(!funcionalidadesSelecionadas.isEmpty()) {
		
			List<Funcionalidade> funcionalidadesOriginais = new ArrayList<Funcionalidade>();
			funcionalidadesOriginais = this.listFuncionalidade.getAllItens();
			
			for(Funcionalidade f : funcionalidadesSelecionadas) {
				funcionalidadesOriginais.add(f);
			}
			
			this.listFuncionalidade.clear();
			this.listFuncionalidade = new SelectionList<Funcionalidade>(funcionalidadesOriginais);
			this.listFuncionalidadesSelecionadas.removeSelectedItem();
		} else {
			addMensagemErro(MensagensFacesEnum.ERRO_SELECIONE_UM_ITEM_DA_LISTAGEM);
		}
	}
	
	public void doRemoverTodasFuncionalidades() {
		List<Funcionalidade> funcionalidadesSelecionadas = this.listFuncionalidadesSelecionadas.getAllItens();
		if(!funcionalidadesSelecionadas.isEmpty()) {
		
			List<Funcionalidade> funcionalidadesOriginais = new ArrayList<Funcionalidade>();
			funcionalidadesOriginais = this.listFuncionalidade.getAllItens();
			
			for(Funcionalidade f : funcionalidadesSelecionadas) {
				funcionalidadesOriginais.add(f);
			}
			
			this.listFuncionalidade.clear();
			this.listFuncionalidade = new SelectionList<Funcionalidade>(funcionalidadesOriginais);
			this.listFuncionalidadesSelecionadas.clear();
		} else {
			addMensagemErro(MensagensFacesEnum.ERRO_NAO_HA_ITENS_A_REMOVER, new Object[] {"Funcionalidade"});
		}
	}
	
	public void doSalvar() {
		try {
			boolean isNew = perfil.isNew();
			perfil.setFuncionalidades(this.listFuncionalidadesSelecionadas.getAllItens());
			perfil = admModulo.salvarTipoPerfil(perfil);
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
	
	public void doSelecionarPerfil() throws PotiErpException {
		try {
			this.perfil = this.perfilSelecionado.clone();
			List<Funcionalidade> funcionalidadesSelecionadas = new ArrayList<Funcionalidade>(this.perfil.getFuncionalidades());
			this.listFuncionalidadesSelecionadas = new SelectionList<Funcionalidade>(funcionalidadesSelecionadas);
			
			List<Funcionalidade> funcionalidades = this.admModulo.buscarTodasFuncionalidades();
			for(Funcionalidade f : funcionalidadesSelecionadas) {
				funcionalidades.remove(f);
			}
			this.listFuncionalidadesSelecionadas = new SelectionList<Funcionalidade>(funcionalidades);
			
			this.disableEdicao = true;
			
		} catch (CloneNotSupportedException e) {
			addMensagemErro(MensagensFacesEnum.ERRO_ALTERAR);
		}
	}
	
	public void doConsultar(){
		try {
			List<TipoPerfilErp> list = this.admModulo.buscarTodosTipoPerfil();
			listPerfil = new SelectionList<TipoPerfilErp>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}

	public String doPerfil() {
		this.doConsultar();
		return NavigationEnum.CADASTRO_DE_PERFIL.getValor();
	}
	
	public boolean isIncluirPerfil() {
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_PERFIL.getCodigo());
	}
	
	public boolean isAlterarPerfil() {
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_PERFIL.getCodigo());
	}
	
	public boolean isConsultar() {
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_PERFIL.getCodigo());
	}
	
	public boolean isExcluirPerfil() {
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_PERFIL.getCodigo());
	}
	
	public boolean isConsultarPerfil() {
		return isIncluirPerfil() || isExcluirPerfil() || isAlterarPerfil() || isConsultar();
	}
	
	public TipoPerfilErp getPerfil() throws PotiErpException {
		carregarItensFuncionalidades();
		return perfil;
	}
	
	private void carregarItensFuncionalidades() throws PotiErpException {
		List<Funcionalidade> todasFuncionalidades = new ArrayList<Funcionalidade>();
		todasFuncionalidades = this.admModulo.buscarTodasFuncionalidades();
		this.listFuncionalidade = new SelectionList<Funcionalidade>(todasFuncionalidades);
	}

	public void setPerfil(TipoPerfilErp perfil) {
		this.perfil = perfil;
	}

	public TipoPerfilErp getPerfilSelecionado() {
		return perfilSelecionado;
	}

	public void setPerfilSelecionado(TipoPerfilErp perfilSelecionado) {
		this.perfilSelecionado = perfilSelecionado;
	}

	public SelectionList<TipoPerfilErp> getListPerfil() {
		return listPerfil;
	}

	public void setListPerfil(SelectionList<TipoPerfilErp> listPerfil) {
		this.listPerfil = listPerfil;
	}

	public SelectionList<Funcionalidade> getListFuncionalidade() {
		return listFuncionalidade;
	}

	public void setListFuncionalidade(
			SelectionList<Funcionalidade> listFuncionalidade) {
		this.listFuncionalidade = listFuncionalidade;
	}

	public SelectionList<Funcionalidade> getListFuncionalidadesSelecionadas() {
		return listFuncionalidadesSelecionadas;
	}

	public void setListFuncionalidadesSelecionadas(
			SelectionList<Funcionalidade> listFuncionalidadesSelecionadas) {
		this.listFuncionalidadesSelecionadas = listFuncionalidadesSelecionadas;
	}

	public Boolean getDisableEdicao() {
		return disableEdicao;
	}

	public void setDisableEdicao(Boolean disableEdicao) {
		this.disableEdicao = disableEdicao;
	}

	@Override
	public Logger getLogger() {
		return this.log;
	}

}

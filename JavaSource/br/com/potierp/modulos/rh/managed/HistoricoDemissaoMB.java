package br.com.potierp.modulos.rh.managed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import br.com.potierp.business.rh.comparator.NomeFuncionarioComparator;
import br.com.potierp.business.rh.facade.RhModulo;
import br.com.potierp.faces.converter.MockEnum;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.exception.PotiErpMensagensException;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.model.Funcionario;
import br.com.potierp.model.HistoricoDemissao;
import br.com.potierp.model.MotivoDemissaoEnum;
import br.com.potierp.model.Pessoa;
import br.com.potierp.model.SituacaoFuncionario;
import br.com.potierp.model.TipoAvisoPrevioEnum;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

/**
 * @author Andrey Oliveira
 */
public class HistoricoDemissaoMB extends BaseMB {

	private static Logger log = Logger.getLogger(HistoricoDemissaoMB.class);
	
	private Funcionario funcionarioSelecionado = new Funcionario();
	
	private Funcionario funcionario = new Funcionario();
	
	private HistoricoDemissao historicoDemissaoSelecionado = new HistoricoDemissao();
	
	private HistoricoDemissao historicoDemissao = new HistoricoDemissao();
	
	private SelectionList<HistoricoDemissao> listHistoricoDemissao = new SelectionList<HistoricoDemissao>(new ArrayList<HistoricoDemissao>());
	
	private List<Funcionario> sugestoesFuncionarios;
	
	private Integer scrollerPage = 1;
	
	private Boolean checkHistoricoAll;
	
	private List<SelectItem> itensMotivoDemissao = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensTipoAvisoPrevio = new ArrayList<SelectItem>();
	
	private Boolean disableEdicao = false;
	
	@EJB
	private RhModulo rhModulo;

	public void buscarFuncionario(){
		try {
			if(isRegistroEmpregadoPreenchido()){
				if(sugestoesFuncionarios == null){
					carregarSugestoes();
				}
				funcionario = getFuncionarioPorRE();
				if(funcionario == null){
					addMensagemErro(MensagensFacesEnum.ERRO_FUNCIONARIO_NAO_ENCONTRADO);
					funcionario = new Funcionario();
					funcionario.setPessoa(new Pessoa());
				}
			}else{
				funcionario = new Funcionario();
				funcionario.setPessoa(new Pessoa());
			}
			historicoDemissao.setFuncionario(funcionario);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	private Funcionario getFuncionarioPorRE() throws Exception {
		if(sugestoesFuncionarios != null){
			for(Funcionario func : sugestoesFuncionarios){
				if(func.getCodigoRegistro() != null && func.getCodigoRegistro().equals(historicoDemissao.getFuncionario().getCodigoRegistro())){
					Funcionario funcionarioTemp = func.clone();
					funcionarioTemp.setPessoa(func.getPessoa().clone());
					return funcionarioTemp;
				}
			}
		}
		return null;
	}
	
	private boolean isRegistroEmpregadoPreenchido() {
		return historicoDemissao.getFuncionario() != null
				&& historicoDemissao.getFuncionario().getCodigoRegistro() != null
				&& historicoDemissao.getFuncionario().getCodigoRegistro() > 0;
	}
	
	private void carregarSugestoes() throws PotiErpException {
		sugestoesFuncionarios = rhModulo.consultarTodosFuncionariosComNomeRE();
	}
	
	public List<Funcionario> sugestao(final Object evento) throws Exception {
		if(this.sugestoesFuncionarios == null) {
			carregarSugestoes();
		}
		
		List<Funcionario> funcionarios = new ArrayList<Funcionario>();
		try {
			String prefixo = evento.toString().trim().toLowerCase();
			for(Funcionario func : sugestoesFuncionarios) {
				if(func.getPessoa().getNome().trim().toLowerCase().startsWith(prefixo)){
					funcionarios.add(func);
				}
			}
		} catch (Exception e) {
			addMensagemErro(e.getMessage());
		}
		Collections.sort(funcionarios, new NomeFuncionarioComparator());
		return funcionarios;
	}
	
	public void selecionarFuncionario() throws Exception {
		this.funcionario = this.funcionarioSelecionado.clone();
		this.funcionario.setPessoa(this.funcionarioSelecionado.getPessoa().clone());
		this.funcionarioSelecionado = new Funcionario();
		this.historicoDemissao.setFuncionario(this.funcionario);
	}
	
	public void doConsultar() {
		try {
			List<HistoricoDemissao> historicosDemissoes = rhModulo.consultarTodosHistoricosDemissoes(getTraceInfo());
			this.listHistoricoDemissao = new SelectionList<HistoricoDemissao>(new ArrayList<HistoricoDemissao>(historicosDemissoes));
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public void doAlterar() {
		try {
			historicoDemissao = this.historicoDemissaoSelecionado.clone();
			Funcionario funcionario = this.historicoDemissaoSelecionado.getFuncionario().clone();
			funcionario.setPessoa(this.historicoDemissaoSelecionado.getFuncionario().getPessoa().clone());
			this.historicoDemissao.setFuncionario(funcionario);
			this.disableEdicao = true;
		} catch (CloneNotSupportedException e) {
			addMensagemErro(MensagensFacesEnum.ERRO_ALTERAR);
		}
	}
	
	public void doSalvar() {
		try {
			validarObrigatorios();
			SituacaoFuncionario situacaoFuncionario = new SituacaoFuncionario();
			situacaoFuncionario.setId(6L);
			historicoDemissao.getFuncionario().setSituacaoFuncionario(situacaoFuncionario);
			this.rhModulo.salvarHistoricoDemissao(historicoDemissao, getTraceInfo());
			doNovo();
			doConsultar();
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_SALVAR);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	private void validarObrigatorios() throws PotiErpMensagensException {
		if(historicoDemissao.getFuncionario() == null || historicoDemissao.getFuncionario().getId() == null) {
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {"Funcionário"});
		} else if(historicoDemissao.getDataDemissao() == null) {
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {"Data de Demissão"});
		} else if(historicoDemissao.getMotivo() == null) {
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {"Motivo da Demissãoo"});
		} else if(historicoDemissao.getTipoAvisoPrevio() == null) {
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {"Tipo de Aviso Prévio"});
		}
	}
	
	public void doExcluirLote() {
		List<HistoricoDemissao> historicosDemissoes = this.listHistoricoDemissao.getItensSelecionados();
		if(!historicosDemissoes.isEmpty()) {
			this.listHistoricoDemissao.removeSelectedItem();
			doNovo();
			try {
				this.rhModulo.excluirListaHistoricoDemissao(historicosDemissoes, getTraceInfo());
			} catch (PotiErpException e) {
				addMensagemErro(MensagensFacesEnum.ERRO_EXCLUIR_LISTA);
			}
		} else {
			addMensagemErro(MensagensFacesEnum.ERRO_SELECIONE_UM_ITEM_DA_LISTAGEM);
		}
	}
	
	public void doNovo() {
		this.historicoDemissao = new HistoricoDemissao();
		this.historicoDemissaoSelecionado = new HistoricoDemissao();
		this.funcionario = new Funcionario();
		this.funcionarioSelecionado = new Funcionario();
		this.checkHistoricoAll = false;
		this.disableEdicao = false;
	}
	
	public String doHistoricoDemissao() {
		doConsultar();
		return NavigationEnum.HISTORICO_DEMISSAO.getValor();
	}
	
	public boolean isConsultarHistoricoDemissao() {
		return isConsultar();
	}
	
	public boolean isConsultar() {
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_VALE_TRANSPORTE.getCodigo());
	}
	
	public Funcionario getFuncionarioSelecionado() {
		return funcionarioSelecionado;
	}

	public void setFuncionarioSelecionado(final Funcionario funcionarioSelecionado) {
		this.funcionarioSelecionado = funcionarioSelecionado;
	}

	public Funcionario getFuncionario() {
		if(funcionario.getPessoa() == null) {
			funcionario.setPessoa(new Pessoa());
		}
		return funcionario;
	}

	public void setFuncionario(final Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public HistoricoDemissao getHistoricoDemissaoSelecionado() {
		return historicoDemissaoSelecionado;
	}

	public void setHistoricoDemissaoSelecionado(
			final HistoricoDemissao historicoDemissaoSelecionado) {
		this.historicoDemissaoSelecionado = historicoDemissaoSelecionado;
	}

	public HistoricoDemissao getHistoricoDemissao() {
		if(historicoDemissao.getFuncionario() == null) {
			Funcionario funcionario = new Funcionario();
			funcionario.setPessoa(new Pessoa());
			historicoDemissao.setFuncionario(funcionario);
		}
		return historicoDemissao;
	}

	public void setHistoricoDemissao(final HistoricoDemissao historicoDemissao) {
		this.historicoDemissao = historicoDemissao;
	}

	public SelectionList<HistoricoDemissao> getListHistoricoDemissao() {
		return listHistoricoDemissao;
	}

	public void setListHistoricoDemissao(
			final SelectionList<HistoricoDemissao> listHistoricoDemissao) {
		this.listHistoricoDemissao = listHistoricoDemissao;
	}

	public List<Funcionario> getSugestoesFuncionarios() {
		return sugestoesFuncionarios;
	}

	public void setSugestoesFuncionarios(final List<Funcionario> sugestoesFuncionarios) {
		this.sugestoesFuncionarios = sugestoesFuncionarios;
	}

	public Integer getScrollerPage() {
		return scrollerPage;
	}

	public void setScrollerPage(final Integer scrollerPage) {
		this.scrollerPage = scrollerPage;
	}

	public Boolean getCheckHistoricoAll() {
		return checkHistoricoAll;
	}

	public void setCheckHistoricoAll(final Boolean checkHistoricoAll) {
		this.checkHistoricoAll = checkHistoricoAll;
	}

	public List<SelectItem> getItensMotivoDemissao() {
		List<MotivoDemissaoEnum> listMotivoDemissao = getListMotivoDemissao();
		if(listMotivoDemissao != null) {
			itensMotivoDemissao.clear();
			addMockSimple(itensMotivoDemissao, MockEnum.SELECIONE);
			for(MotivoDemissaoEnum motivo : listMotivoDemissao) {
				SelectItem item = new SelectItem(motivo, motivo.getMotivo());
				itensMotivoDemissao.add(item);
			}
		}
		return itensMotivoDemissao;
	}
	
	public List<MotivoDemissaoEnum> getListMotivoDemissao() {
		if(itensMotivoDemissao.isEmpty()) {
			return new ArrayList<MotivoDemissaoEnum>(Arrays.asList(MotivoDemissaoEnum.values()));
		}
		return null;
	}

	public void setItensMotivoDemissao(final List<SelectItem> itensMotivoDemissao) {
		this.itensMotivoDemissao = itensMotivoDemissao;
	}

	public List<SelectItem> getItensTipoAvisoPrevio() {
		List<TipoAvisoPrevioEnum> listTipoAvisoPrevio = getListTipoAvisoPrevio();
		if(listTipoAvisoPrevio != null) {
			itensTipoAvisoPrevio.clear();
			addMockSimple(itensTipoAvisoPrevio, MockEnum.SELECIONE);
			for(TipoAvisoPrevioEnum tipo : listTipoAvisoPrevio) {
				SelectItem item = new SelectItem(tipo, tipo.getTipo());
				itensTipoAvisoPrevio.add(item);
			}
		}
		return itensTipoAvisoPrevio;
	}
	
	public List<TipoAvisoPrevioEnum> getListTipoAvisoPrevio() {
		if(itensTipoAvisoPrevio.isEmpty()) {
			return new ArrayList<TipoAvisoPrevioEnum>(Arrays.asList(TipoAvisoPrevioEnum.values()));
		}
		return null;
	}

	public void setItensTipoAvisoPrevio(final List<SelectItem> itensTipoAvisoPrevio) {
		this.itensTipoAvisoPrevio = itensTipoAvisoPrevio;
	}

	public Boolean getDisableEdicao() {
		return disableEdicao;
	}

	public void setDisableEdicao(final Boolean disableEdicao) {
		this.disableEdicao = disableEdicao;
	}

	@Override
	public Logger getLogger() {
		return log;
	}
	
}

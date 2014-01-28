package br.com.potierp.modulos.rh.historico.managed;

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
import br.com.potierp.model.Afastamento;
import br.com.potierp.model.Funcionario;
import br.com.potierp.model.MotivoAfastamentoEnum;
import br.com.potierp.model.Pessoa;
import br.com.potierp.model.SituacaoFuncionario;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

/**
 * @author Doug
 *
 */
public class HistoricoAfastamentoMB extends BaseMB{
	
	private static Logger log = Logger.getLogger(HistoricoAfastamentoMB.class);
	
	private Boolean checkAfastamentoAll;
	
	private Integer scrollerPage = 1;
	
	private SelectionList<Afastamento> listHistoricoAfastamento = new SelectionList<Afastamento>(new ArrayList<Afastamento>());
	
	private Afastamento afastamento = new Afastamento();
	
	private Afastamento afastamentoSelecionado = new Afastamento();
	
	private Boolean disableEdicao;
	
	private List<Funcionario> sugestoesFuncionarios;
	
	private Funcionario funcionario = new Funcionario();
	
	private Funcionario funcionarioSelecionado = new Funcionario();
	
	private List<SelectItem> itensMotivoAfastamento = new ArrayList<SelectItem>();
	
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
			afastamento.setFuncionario(funcionario);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	private Funcionario getFuncionarioPorRE() throws Exception {
		if(sugestoesFuncionarios != null){
			for(Funcionario func : sugestoesFuncionarios){
				if(func.getCodigoRegistro() != null && func.getCodigoRegistro().equals(afastamento.getFuncionario().getCodigoRegistro())){
					Funcionario funcionarioTemp = func.clone();
					funcionarioTemp.setPessoa(func.getPessoa().clone());
					return funcionarioTemp;
				}
			}
		}
		return null;
	}
	
	private boolean isRegistroEmpregadoPreenchido() {
		return afastamento.getFuncionario() != null
				&& afastamento.getFuncionario().getCodigoRegistro() != null
				&& afastamento.getFuncionario().getCodigoRegistro() > 0;
	}
	
	private void carregarSugestoes() throws PotiErpException {
		sugestoesFuncionarios = rhModulo.consultarTodosFuncionariosComNomeRE();
	}
	
	public List<Funcionario> sugestao(final Object evento) throws Exception{
		
		if(sugestoesFuncionarios == null){
			carregarSugestoes();
		}
		
		List<Funcionario> funcionarios = new ArrayList<Funcionario>();
		try {
			String prefixo = evento.toString().trim().toLowerCase();
			for(Funcionario func : sugestoesFuncionarios){
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
		afastamento.setFuncionario(funcionario);
	}

	public void doNovo(){
		this.afastamento = new Afastamento();
		this.afastamentoSelecionado = new Afastamento();
		this.funcionario = new Funcionario();
		this.funcionarioSelecionado = new Funcionario();
		this.checkAfastamentoAll = false;
		this.disableEdicao = false;
	}
	
	public void doSalvar(){
		try {
			validarObrigatorios();
			SituacaoFuncionario situacaoFuncionario = new SituacaoFuncionario();
			if(afastamento.getDataRetorno() == null) {
				situacaoFuncionario.setId(2L);
			}else{
				situacaoFuncionario.setId(1L);
			}
			this.afastamento.getFuncionario().setSituacaoFuncionario(situacaoFuncionario);
			this.rhModulo.salvarAfastamento(afastamento, getTraceInfo());
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
		if(afastamento.getFuncionario() == null || afastamento.getFuncionario().getId() == null) {
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {"Funcion�rio"});
		} else if(afastamento.getDataAfastamento() == null) {
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {"Data do Afastamento"});
		} else if(afastamento.getDataUltimoDiaTrabalho() == null) {
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {"�ltimo dia de trabalho"});
		} else if(afastamento.getMotivo() == null) {
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {"Motivo do afastamento"});
		}
	}

	public void doConsultar() {
		try {
			List<Afastamento> afastamentos = rhModulo.consultarTodosAfastamento(getTraceInfo());
			this.listHistoricoAfastamento = new SelectionList<Afastamento>(afastamentos);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public void doExcluirLote() {
		List<Afastamento> afastamentos = this.listHistoricoAfastamento.getItensSelecionados();
		if(!afastamentos.isEmpty()) {
			this.listHistoricoAfastamento.removeSelectedItem();
			doNovo();
			try {
				this.rhModulo.excluirListaAfastamento(afastamentos, getTraceInfo());
			} catch (PotiErpException e) {
				addMensagemErro(MensagensFacesEnum.ERRO_EXCLUIR_LISTA);
			}
		}else {
			addMensagemErro(MensagensFacesEnum.ERRO_SELECIONE_UM_ITEM_DA_LISTAGEM);
		}
	}
	
	public void doAlterar() {
		try {
			afastamento = this.afastamentoSelecionado.clone();
			Funcionario funcionario = this.afastamentoSelecionado.getFuncionario().clone();
			funcionario.setPessoa(this.afastamentoSelecionado.getFuncionario().getPessoa().clone());
			this.afastamento.setFuncionario(funcionario);
			this.disableEdicao = true;
		} catch (CloneNotSupportedException e) {
			addMensagemErro(MensagensFacesEnum.ERRO_ALTERAR);
		}
	}
	
	public String doHistoricoAfastamento() {
		doConsultar();
		return NavigationEnum.HISTORICO_AFASTAMENTO.getValor();
	}
	
	public boolean isIncluirHistoricoAfastamento(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isAlterarHistoricoAfastamento(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_VALE_TRANSPORTE.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isExcluirHistoricoAfastamento(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isConsultarHistoricoAfastamento(){
		return isIncluirHistoricoAfastamento() || isExcluirHistoricoAfastamento() || isAlterarHistoricoAfastamento() || isConsultar();
	}
	
	/* (non-Javadoc)
	 * @see br.com.potierp.faces.managed.BaseMB#getLogger()
	 */
	@Override
	public Logger getLogger() {
		return log;
	}
	
	/**
	 * @return the checkAfastamentoAll
	 */
	public Boolean getCheckAfastamentoAll() {
		return checkAfastamentoAll;
	}

	/**
	 * @param checkAfastamentoAll the checkAfastamentoAll to set
	 */
	public void setCheckAfastamentoAll(final Boolean checkAfastamentoAll) {
		this.checkAfastamentoAll = checkAfastamentoAll;
	}

	/**
	 * @return the scrollerPage
	 */
	public Integer getScrollerPage() {
		return scrollerPage;
	}

	/**
	 * @param scrollerPage the scrollerPage to set
	 */
	public void setScrollerPage(final Integer scrollerPage) {
		this.scrollerPage = scrollerPage;
	}

	/**
	 * @return the listHistoricoAfastamento
	 */
	public SelectionList<Afastamento> getListHistoricoAfastamento() {
		return listHistoricoAfastamento;
	}

	/**
	 * @param listHistoricoAfastamento the listHistoricoAfastamento to set
	 */
	public void setListHistoricoAfastamento(
			final SelectionList<Afastamento> listHistoricoAfastamento) {
		this.listHistoricoAfastamento = listHistoricoAfastamento;
	}

	public Boolean getDisableEdicao() {
		return disableEdicao;
	}

	public void setDisableEdicao(final Boolean disableEdicao) {
		this.disableEdicao = disableEdicao;
	}
	
	public Afastamento getAfastamento() {
		if(afastamento.getFuncionario() == null) {
			Funcionario funcionario = new Funcionario();
			funcionario.setPessoa(new Pessoa());
			afastamento.setFuncionario(funcionario);
		}
		return afastamento;
	}

	public void setAfastamento(final Afastamento afastamento) {
		this.afastamento = afastamento;
	}

	public List<Funcionario> getSugestoesFuncionarios() {
		return sugestoesFuncionarios;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public Funcionario getFuncionarioSelecionado() {
		return funcionarioSelecionado;
	}

	public void setSugestoesFuncionarios(final List<Funcionario> sugestoesFuncionarios) {
		this.sugestoesFuncionarios = sugestoesFuncionarios;
	}

	public void setFuncionario(final Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public void setFuncionarioSelecionado(final Funcionario funcionarioSelecionado) {
		this.funcionarioSelecionado = funcionarioSelecionado;
	}

	public Afastamento getAfastamentoSelecionado() {
		return afastamentoSelecionado;
	}

	public void setAfastamentoSelecionado(final Afastamento afastamentoSelecionado) {
		this.afastamentoSelecionado = afastamentoSelecionado;
	}

	public List<SelectItem> getItensMotivoAfastamento() {
		List<MotivoAfastamentoEnum> listMotivoAfastamento = getListMotivoAfastamento();
		if(listMotivoAfastamento != null) {
			itensMotivoAfastamento.clear();
			addMockSimple(itensMotivoAfastamento, MockEnum.SELECIONE);
			for(MotivoAfastamentoEnum motivo : listMotivoAfastamento) {
				SelectItem item = new SelectItem(motivo, motivo.getMotivo());
				itensMotivoAfastamento.add(item);
			}
		}
		return itensMotivoAfastamento;
	}
	
	public List<MotivoAfastamentoEnum> getListMotivoAfastamento() {
		if(itensMotivoAfastamento.isEmpty()) {
			return new ArrayList<MotivoAfastamentoEnum>(Arrays.asList(MotivoAfastamentoEnum.values()));
		}
		return null;
	}

	public void setItensMotivoAfastamento(final List<SelectItem> itensMotivoAfastamento) {
		this.itensMotivoAfastamento = itensMotivoAfastamento;
	}

	

}

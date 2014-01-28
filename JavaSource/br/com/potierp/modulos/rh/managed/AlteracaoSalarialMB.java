package br.com.potierp.modulos.rh.managed;

import java.math.BigDecimal;
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
import br.com.potierp.model.AlteracaoSalarial;
import br.com.potierp.model.Cargo;
import br.com.potierp.model.Funcionario;
import br.com.potierp.model.MotivoEnum;
import br.com.potierp.model.Pessoa;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class AlteracaoSalarialMB extends BaseMB{
	
	private static Logger log = Logger.getLogger(AlteracaoSalarialMB.class);
	
	private Integer scrollerPage = 1;
	
	private Boolean isDisableRe = false;
	
	private Boolean isDisableNome = false;
	
	private Boolean checkAlteracaoSalarialAll;
	
	private SelectionList<AlteracaoSalarial> listAlteracaoSalarial = new SelectionList<AlteracaoSalarial>(new ArrayList<AlteracaoSalarial>());
	
	private AlteracaoSalarial alteracaoSalarialSelecionado = new AlteracaoSalarial();
	
	private AlteracaoSalarial alteracaoSalarial = new AlteracaoSalarial();
	
	private Funcionario funcionarioSelecionado = new Funcionario();
	
	private Funcionario funcionario = new Funcionario();
	
	private List<Funcionario> sugestoesFuncionarios;
	
	private List<SelectItem> itensCargoAtual = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensCargoAnterior = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensMotivo = new ArrayList<SelectItem>();
	
	@EJB
	private RhModulo rhModulo;

	/* (non-Javadoc)
	 * @see br.com.potierp.faces.managed.BaseMB#getLogger()
	 */
	@Override
	public Logger getLogger() {
		return log;
	}
	
	/**
	 * @return the listAlteracaoSalarial
	 */
	public SelectionList<AlteracaoSalarial> getListAlteracaoSalarial() {
		return listAlteracaoSalarial;
	}

	/**
	 * @param listAlteracaoSalarial the listAlteracaoSalarial to set
	 */
	public void setListAlteracaoSalarial(final SelectionList<AlteracaoSalarial> listAlteracaoSalarial) {
		this.listAlteracaoSalarial = listAlteracaoSalarial;
	}

	/**
	 * @return the checkAlteracaoSalarialAll
	 */
	public Boolean getCheckAlteracaoSalarialAll() {
		return checkAlteracaoSalarialAll;
	}

	/**
	 * @param checkAlteracaoSalarialAll the checkAlteracaoSalarialAll to set
	 */
	public void setCheckAlteracaoSalarialAll(final Boolean checkAlteracaoSalarialAll) {
		this.checkAlteracaoSalarialAll = checkAlteracaoSalarialAll;
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

	public void doExcluirLote() {
		List<AlteracaoSalarial> alteracoesSalariais = this.listAlteracaoSalarial.getItensSelecionados();
		if(!alteracoesSalariais.isEmpty()) {
			this.listAlteracaoSalarial.removeSelectedItem();
			doNovo();
			try {
				this.rhModulo.excluirListaAlteracaoSalarial(alteracoesSalariais);
			} catch (PotiErpException e) {
				addMensagemErro(MensagensFacesEnum.ERRO_EXCLUIR_LISTA);
			}
		}else {
			addMensagemErro(MensagensFacesEnum.ERRO_SELECIONE_UM_ITEM_DA_LISTAGEM);
		}
	}

	public void doNovo() {
		this.funcionario = new Funcionario();
		this.funcionarioSelecionado = new Funcionario();
		this.alteracaoSalarial = new AlteracaoSalarial();
		this.alteracaoSalarialSelecionado = new AlteracaoSalarial();
		this.checkAlteracaoSalarialAll = false;
		this.isDisableRe = false;
		this.isDisableNome = false;
		itensCargoAnterior.clear();
		addMock(itensCargoAnterior, MockEnum.SELECIONE);
	}
	
	public void doSalvar(){
		try {
			validarObrigatorios();
			alteracaoSalarial.setFuncionario(funcionario.clone());
			rhModulo.salvarAlteracaoSalarial(alteracaoSalarial);
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
		if(alteracaoSalarial.getSalarioAtual() == null || alteracaoSalarial.getSalarioAtual().compareTo(BigDecimal.ONE) < 1){
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[]{"Salário Atual"});
		}else if(alteracaoSalarial.getReajuste() == null || alteracaoSalarial.getReajuste().compareTo(BigDecimal.ONE) < 1){
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[]{"Reajuste"});
		}else if (alteracaoSalarial.getCargoAnterior() == null){
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[]{"Cargo Anterior"});
		}else if(alteracaoSalarial.getCargoAtual() == null){
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[]{"Cargo Atual"});
		}
	}

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
			alteracaoSalarial.setFuncionario(funcionario);
			alteracaoSalarial.setCargoAtual(funcionario.getCargo());
			alteracaoSalarial.setSalarioAnterior(funcionario.getSalario());
			carregarCargosAnteriores();
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	private void carregarCargosAnteriores() {
		itensCargoAnterior.clear();
		addMock(itensCargoAnterior, MockEnum.SELECIONE);
		if(funcionario.getCargo() != null){
			SelectItem item = new SelectItem(funcionario.getCargo(), funcionario.getCargo().getCargo());
			itensCargoAnterior.add(item);
		}
	}
	
	private Funcionario getFuncionarioPorRE() throws Exception {
		if(sugestoesFuncionarios != null){
			for(Funcionario func : sugestoesFuncionarios){
				if(func.getCodigoRegistro() != null && func.getCodigoRegistro().equals(alteracaoSalarial.getFuncionario().getCodigoRegistro())){
					Funcionario funcionarioTemp = func.clone();
					funcionarioTemp.setPessoa(func.getPessoa().clone());
					Cargo cargo = rhModulo.consultarCargo(func.getCargo());
					funcionarioTemp.setCargo(cargo);
					return funcionarioTemp;
				}
			}
		}
		return null;
	}
	
	private boolean isRegistroEmpregadoPreenchido() {
		return alteracaoSalarial.getFuncionario() != null
				&& alteracaoSalarial.getFuncionario().getCodigoRegistro() != null
				&& alteracaoSalarial.getFuncionario().getCodigoRegistro() > 0;
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
		alteracaoSalarial.setFuncionario(funcionario);
		Cargo cargo = rhModulo.consultarCargo(funcionario.getCargo());
		funcionario.setCargo(cargo);
		alteracaoSalarial.setSalarioAnterior(funcionario.getSalario());
		carregarCargosAnteriores();
	}
	
	public boolean isIncluirAlteracaoSalarial(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isAlterarAlteracaoSalarial(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_VALE_TRANSPORTE.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isExcluirAlteracaoSalarial(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isConsultarAlteracaoSalarial(){
		return isIncluirAlteracaoSalarial() || isExcluirAlteracaoSalarial() || isAlterarAlteracaoSalarial() || isConsultar();
	}
	
	public boolean isManterAlteracaoSalarial() {
		return isIncluirAlteracaoSalarial() || isAlterarAlteracaoSalarial();
	}
	
	public String doAlteracaoSalarial() {
		doConsultar();
		return NavigationEnum.ALTERACAO_SALARIAL.getValor();
	}
	
	public void doConsultar() {
		try {
			List<AlteracaoSalarial> alteracoesSalariais = rhModulo.consultarTodosAlteracaoSalarial();
			this.listAlteracaoSalarial = new SelectionList<AlteracaoSalarial>(new ArrayList<AlteracaoSalarial>(alteracoesSalariais));
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public void doAlterar() {
		try {
			alteracaoSalarial = this.alteracaoSalarialSelecionado.clone();
			Funcionario funcionario = this.alteracaoSalarialSelecionado.getFuncionario().clone();
			funcionario.setPessoa(this.alteracaoSalarialSelecionado.getFuncionario().getPessoa().clone());
			this.alteracaoSalarial.setFuncionario(funcionario);
			this.isDisableRe = true;
			this.isDisableNome = true;
			Cargo cargo = alteracaoSalarialSelecionado.getCargoAnterior();
			if(cargo != null){
				itensCargoAnterior.clear();
				SelectItem item = new SelectItem(cargo, cargo.getCargo());
				itensCargoAnterior.add(item);
			}
		} catch (CloneNotSupportedException e) {
			addMensagemErro(MensagensFacesEnum.ERRO_ALTERAR);
		}
	}
	
	/**
	 * @return the alteracaoSalarialSelecionado
	 */
	public AlteracaoSalarial getAlteracaoSalarialSelecionado() {
		return alteracaoSalarialSelecionado;
	}

	/**
	 * @param alteracaoSalarialSelecionado the alteracaoSalarialSelecionado to set
	 */
	public void setAlteracaoSalarialSelecionado(final AlteracaoSalarial alteracaoSalarialSelecionado) {
		this.alteracaoSalarialSelecionado = alteracaoSalarialSelecionado;
	}

	/**
	 * @return the alteracaoSalarial
	 */
	public AlteracaoSalarial getAlteracaoSalarial() {
		if(alteracaoSalarial.getFuncionario() == null) {
			Funcionario funcionario = new Funcionario();
			funcionario.setPessoa(new Pessoa());
			alteracaoSalarial.setFuncionario(funcionario);
		}
		return alteracaoSalarial;
	}

	/**
	 * @param alteracaoSalarial the alteracaoSalarial to set
	 */
	public void setAlteracaoSalarial(final AlteracaoSalarial alteracaoSalarial) {
		this.alteracaoSalarial = alteracaoSalarial;
	}

	/**
	 * @return the funcionarioSelecionado
	 */
	public Funcionario getFuncionarioSelecionado() {
		return funcionarioSelecionado;
	}

	/**
	 * @param funcionarioSelecionado the funcionarioSelecionado to set
	 */
	public void setFuncionarioSelecionado(final Funcionario funcionarioSelecionado) {
		this.funcionarioSelecionado = funcionarioSelecionado;
	}

	/**
	 * @return the funcionario
	 */
	public Funcionario getFuncionario() {
		return funcionario;
	}

	/**
	 * @param funcionario the funcionario to set
	 */
	public void setFuncionario(final Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	/**
	 * @return the isDisableRe
	 */
	public Boolean getIsDisableRe() {
		return isDisableRe;
	}

	/**
	 * @param isDisableRe the isDisableRe to set
	 */
	public void setIsDisableRe(final Boolean isDisableRe) {
		this.isDisableRe = isDisableRe;
	}

	/**
	 * @return the isDisableNome
	 */
	public Boolean getIsDisableNome() {
		return isDisableNome;
	}

	/**
	 * @param isDisableNome the isDisableNome to set
	 */
	public void setIsDisableNome(final Boolean isDisableNome) {
		this.isDisableNome = isDisableNome;
	}

	public List<SelectItem> getItensCargoAtual() throws PotiErpException {
		List<Cargo> listCargos = getCargos();
		if(listCargos != null){
			itensCargoAtual.clear();
			addMock(itensCargoAtual, MockEnum.SELECIONE);
			for(Cargo cargo : listCargos){
				SelectItem item = new SelectItem(cargo, cargo.getCargo());
				itensCargoAtual.add(item);
			}
		}
		return itensCargoAtual;
	}
	
	private List<Cargo> getCargos() throws PotiErpException{
		if(itensCargoAtual.isEmpty()){
			return rhModulo.consultarTodosCargos();
		}
		return null;
	}

	public void setItensCargoAtual(final List<SelectItem> itensCargoAtual) {
		this.itensCargoAtual = itensCargoAtual;
	}
	
	/**
	 * @return the itensCargoAnterior
	 */
	public List<SelectItem> getItensCargoAnterior() {
		if(itensCargoAnterior.isEmpty()){
			itensCargoAnterior.clear();
			addMock(itensCargoAnterior, MockEnum.SELECIONE);
		}
		return itensCargoAnterior;
	}

	/**
	 * @param itensCargoAnterior the itensCargoAnterior to set
	 */
	public void setItensCargoAnterior(final List<SelectItem> itensCargoAnterior) {
		this.itensCargoAnterior = itensCargoAnterior;
	}

	public List<SelectItem> getItensMotivo() {
		List<MotivoEnum> listMotivo = getListMotivo();
		if(listMotivo != null){
			itensMotivo.clear();
			addMockSimple(itensMotivo, MockEnum.SELECIONE);
			for(MotivoEnum motivo : listMotivo){
				SelectItem item = new SelectItem(motivo, motivo.getMotivo());
				itensMotivo.add(item);
			}
		}
		return itensMotivo;
	}
	
	public List<MotivoEnum> getListMotivo() {
		if(itensMotivo.isEmpty()){ 
			return new ArrayList<MotivoEnum>(Arrays.asList(MotivoEnum.values()));
		}
		return null;
	}

	public void setItensMotivo(final List<SelectItem> itensMotivo) {
		this.itensMotivo = itensMotivo;
	}
}
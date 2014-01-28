package br.com.potierp.modulos.rh.managed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import br.com.potierp.model.AlteracaoCliente;
import br.com.potierp.model.Cliente;
import br.com.potierp.model.Funcionario;
import br.com.potierp.model.LocalTrabalho;
import br.com.potierp.model.Pessoa;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class AlteracaoClienteMB extends BaseMB{
	
	private static Logger log = Logger.getLogger(AlteracaoClienteMB.class);
	
	private Integer scrollerPage = 1;
	
	private Boolean isDisableRe = false;
	
	private Boolean isDisableNome = false;
	
	private Boolean checkAlteracaoClienteAll;
	
	private SelectionList<AlteracaoCliente> listAlteracaoCliente = new SelectionList<AlteracaoCliente>(new ArrayList<AlteracaoCliente>());
	
	private AlteracaoCliente alteracaoClienteSelecionado = new AlteracaoCliente();
	
	private AlteracaoCliente alteracaoCliente = new AlteracaoCliente();
	
	private Funcionario funcionarioSelecionado = new Funcionario();
	
	private Funcionario funcionario = new Funcionario();
	
	private List<Funcionario> sugestoesFuncionarios;
	
	private List<SelectItem> itensClientesAnteriores = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensClientesAtuais = new ArrayList<SelectItem>();
	
	@EJB
	private RhModulo rhModulo;

	/** (non-Javadoc)
	 * @see br.com.potierp.faces.managed.BaseMB#getLogger()
	 */
	@Override
	public Logger getLogger() {
		return log;
	}
	
	/**
	 * @return the listAlteracaoCliente
	 */
	public SelectionList<AlteracaoCliente> getListAlteracaoCliente() {
		return listAlteracaoCliente;
	}

	/**
	 * @param listAlteracaoCliente the listAlteracaoCliente to set
	 */
	public void setListAlteracaoCliente(final SelectionList<AlteracaoCliente> listAlteracaoCliente) {
		this.listAlteracaoCliente = listAlteracaoCliente;
	}

	/**
	 * @return the checkAlteracaoClienteAll
	 */
	public Boolean getCheckAlteracaoClienteAll() {
		return checkAlteracaoClienteAll;
	}

	/**
	 * @param checkAlteracaoClienteAll the checkAlteracaoClienteAll to set
	 */
	public void setCheckAlteracaoClienteAll(final Boolean checkAlteracaoClienteAll) {
		this.checkAlteracaoClienteAll = checkAlteracaoClienteAll;
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
		List<AlteracaoCliente> alteracoesClientes = this.listAlteracaoCliente.getItensSelecionados();
		if(!alteracoesClientes.isEmpty()) {
			this.listAlteracaoCliente.removeSelectedItem();
			doNovo();
			try {
				this.rhModulo.excluirListaAlteracaoCliente(alteracoesClientes);
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
		this.alteracaoCliente = new AlteracaoCliente();
		this.alteracaoClienteSelecionado = new AlteracaoCliente();
		this.checkAlteracaoClienteAll = false;
		this.isDisableRe = false;
		this.isDisableNome = false;
		itensClientesAnteriores.clear();
		addMock(itensClientesAnteriores, MockEnum.SELECIONE);
	}
	
	public void doSalvar(){
		try {
			validarObrigatorios();
			alteracaoCliente.setFuncionario(funcionario.clone());
			rhModulo.salvarAlteracaoCliente(alteracaoCliente);
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
		if(alteracaoCliente.getFuncionario() == null || alteracaoCliente.getFuncionario().getId() == null){
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[]{"Funcionário"});
		}else if(alteracaoCliente.getClienteAnterior() == null || alteracaoCliente.getClienteAnterior().getId() == null){
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[]{"Cliente Anterior"});
		}else if(alteracaoCliente.getClienteAtual() == null || alteracaoCliente.getClienteAtual().getId() == null){
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[]{"Cliente Atual"});
		}else if(alteracaoCliente.getDataTransferencia() == null){
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[]{"Data Transferência"});
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
			alteracaoCliente.setFuncionario(funcionario);
			carregarClientesAnteriores();
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	private void carregarClientesAnteriores() {
		itensClientesAnteriores.clear();
		addMock(itensClientesAnteriores, MockEnum.SELECIONE);
		if(funcionario.getLocaisTrabalho() != null 
				&& !funcionario.getLocaisTrabalho().isEmpty()){
			Set<Cliente> setClientes = new HashSet<Cliente>();
			for(LocalTrabalho localTrabalho : funcionario.getLocaisTrabalho()){
				Cliente cliente = localTrabalho.getCliente();
				setClientes.add(cliente);
			}
			for(Cliente cliente : setClientes){
				SelectItem item = new SelectItem(cliente, cliente.getCodigo() + " - " + cliente.getNomeFantasia());
				itensClientesAnteriores.add(item);
			}
		}
	}

	private Funcionario getFuncionarioPorRE() throws Exception {
		if(sugestoesFuncionarios != null){
			for(Funcionario func : sugestoesFuncionarios){
				if(func.getCodigoRegistro() != null 
						&& func.getCodigoRegistro().equals(alteracaoCliente.getFuncionario().getCodigoRegistro())){
					Funcionario funcionarioTemp = func.clone();
					funcionarioTemp.setPessoa(func.getPessoa().clone());
					List<LocalTrabalho> locaisTrabalho = rhModulo.getLocaisTrabalhoPorFuncionario(funcionarioTemp);
					funcionarioTemp.setLocaisTrabalho(locaisTrabalho);
					return funcionarioTemp;
				}
			}
		}
		return null;
	}
	
	private boolean isRegistroEmpregadoPreenchido() {
		return alteracaoCliente.getFuncionario() != null
				&& alteracaoCliente.getFuncionario().getCodigoRegistro() != null
				&& alteracaoCliente.getFuncionario().getCodigoRegistro() > 0;
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
		List<LocalTrabalho> locaisTrabalho = rhModulo.getLocaisTrabalhoPorFuncionario(funcionario);
		funcionario.setLocaisTrabalho(locaisTrabalho);
		alteracaoCliente.setFuncionario(funcionario);
		carregarClientesAnteriores();
	}
	
	public boolean isIncluirAlteracaoCliente(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isAlterarAlteracaoCliente(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_VALE_TRANSPORTE.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isExcluirAlteracaoCliente(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isConsultarAlteracaoCliente(){
		return isIncluirAlteracaoCliente() || isExcluirAlteracaoCliente() || isAlterarAlteracaoCliente() || isConsultar();
	}
	
	public boolean isManterAlteracaoCliente() {
		return isIncluirAlteracaoCliente() || isAlterarAlteracaoCliente();
	}
	
	public String doAlteracaoCliente() {
		doConsultar();
		return NavigationEnum.ALTERACAO_CLIENTE.getValor();
	}
	
	public void doConsultar() {
		try {
			List<AlteracaoCliente> alteracoesSalariais = rhModulo.consultarTodosAlteracaoCliente();
			this.listAlteracaoCliente = new SelectionList<AlteracaoCliente>(new ArrayList<AlteracaoCliente>(alteracoesSalariais));
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public void doAlterar() {
		try {
			alteracaoCliente = this.alteracaoClienteSelecionado.clone();
			Funcionario funcionario = this.alteracaoClienteSelecionado.getFuncionario().clone();
			funcionario.setPessoa(this.alteracaoClienteSelecionado.getFuncionario().getPessoa().clone());
			this.alteracaoCliente.setFuncionario(funcionario);
			this.isDisableRe = true;
			this.isDisableNome = true;
			itensClientesAnteriores.clear();
			Cliente cliente = alteracaoClienteSelecionado.getClienteAnterior();
			SelectItem item = new SelectItem(cliente, cliente.getCodigo() + " - " + cliente.getNomeFantasia());
			itensClientesAnteriores.add(item);
		} catch (CloneNotSupportedException e) {
			addMensagemErro(MensagensFacesEnum.ERRO_ALTERAR);
		}
	}
	
	/**
	 * @return the alteracaoClienteSelecionado
	 */
	public AlteracaoCliente getAlteracaoClienteSelecionado() {
		return alteracaoClienteSelecionado;
	}

	/**
	 * @param alteracaoClienteSelecionado the alteracaoClienteSelecionado to set
	 */
	public void setAlteracaoClienteSelecionado(final AlteracaoCliente alteracaoClienteSelecionado) {
		this.alteracaoClienteSelecionado = alteracaoClienteSelecionado;
	}

	/**
	 * @return the alteracaoCliente
	 */
	public AlteracaoCliente getAlteracaoCliente() {
		if(alteracaoCliente.getFuncionario() == null) {
			Funcionario funcionario = new Funcionario();
			funcionario.setPessoa(new Pessoa());
			alteracaoCliente.setFuncionario(funcionario);
		}
		return alteracaoCliente;
	}

	/**
	 * @param alteracaoCliente the alteracaoCliente to set
	 */
	public void setAlteracaoCliente(final AlteracaoCliente alteracaoCliente) {
		this.alteracaoCliente = alteracaoCliente;
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

	/**
	 * @return the itensClientesAnteriores
	 * @throws Exception 
	 */
	public List<SelectItem> getItensClientesAnteriores() throws Exception {
		if(itensClientesAnteriores.isEmpty()){
			itensClientesAnteriores.clear();
			addMock(itensClientesAnteriores, MockEnum.SELECIONE);
		}
		return itensClientesAnteriores;
	}

	/**
	 * @param itensClientesAnteriores the itensClientesAnteriores to set
	 */
	public void setItensClientesAnteriores(final List<SelectItem> itensClientesAnteriores) {
		this.itensClientesAnteriores = itensClientesAnteriores;
	}

	/**
	 * @return the itensClientesAtuais
	 * @throws Exception
	 */
	public List<SelectItem> getItensClientesAtuais() throws Exception {
		List<Cliente> listClientes = getClientes();
		if(listClientes != null){
			itensClientesAtuais.clear();
			addMock(itensClientesAtuais, MockEnum.SELECIONE);
			for(Cliente cliente : listClientes){
				SelectItem item = new SelectItem(cliente, cliente.getCodigo() + " - " + cliente.getNomeFantasia());
				itensClientesAtuais.add(item);
			}
		}
		return itensClientesAtuais;
	}
	
	private List<Cliente> getClientes() throws PotiErpException{
		if(itensClientesAtuais.isEmpty()){
			return rhModulo.consultarTodosClientes();
		}
		return null;
	}
	
	/**
	 * @param itensClientesAtuais the itensClientesAtuais to set
	 */
	public void setItensClientesAtuais(final List<SelectItem> itensClientesAtuais) {
		this.itensClientesAtuais = itensClientesAtuais;
	}
}
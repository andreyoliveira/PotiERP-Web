package br.com.potierp.modulos.rh.managed;

import java.util.ArrayList;
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
import br.com.potierp.infra.helper.SelectionEntity;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.model.Cliente;
import br.com.potierp.model.Funcionario;
import br.com.potierp.model.Pessoa;
import br.com.potierp.model.Responsavel;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class ResponsavelMB extends BaseMB{
	
	private static Logger log = Logger.getLogger(ResponsavelMB.class);
	
	private Integer scrollerPage = 1;
	
	private Integer scrollerPageCliente = 1;
	
	private Boolean isDisableRe = false;
	
	private Boolean isDisableNome = false;
	
	private Boolean checkResponsavelAll;
	
	private Boolean checkClienteAll;
	
	private SelectionList<Responsavel> listResponsavel = new SelectionList<Responsavel>(new ArrayList<Responsavel>());
	
	private Responsavel responsavelSelecionado = new Responsavel();
	
	private Responsavel responsavel = new Responsavel();
	
	private Funcionario funcionarioSelecionado = new Funcionario();
	
	private Funcionario funcionario = new Funcionario();
	
	private List<Funcionario> sugestoesFuncionarios;
	
	private Cliente cliente = new Cliente();
	
	private Cliente clienteSelecionado = new Cliente();
	
	private List<SelectItem> itensClientes = new ArrayList<SelectItem>();
	
	private SelectionList<Cliente> listCliente = new SelectionList<Cliente>(new ArrayList<Cliente>());
	
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
	 * @return the listResponsavel
	 */
	public SelectionList<Responsavel> getListResponsavel() {
		return listResponsavel;
	}

	/**
	 * @param listResponsavel the listResponsavel to set
	 */
	public void setListResponsavel(final SelectionList<Responsavel> listResponsavel) {
		this.listResponsavel = listResponsavel;
	}

	/**
	 * @return the checkResponsavelAll
	 */
	public Boolean getCheckResponsavelAll() {
		return checkResponsavelAll;
	}

	/**
	 * @param checkResponsavelAll the checkResponsavelAll to set
	 */
	public void setCheckResponsavelAll(final Boolean checkResponsavelAll) {
		this.checkResponsavelAll = checkResponsavelAll;
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
		List<Responsavel> responsaveis = this.listResponsavel.getItensSelecionados();
		if(!responsaveis.isEmpty()) {
			this.listResponsavel.removeSelectedItem();
			doNovo();
			try {
				this.rhModulo.excluirListaResponsavel(responsaveis);
			} catch (PotiErpException e) {
				addMensagemErro(MensagensFacesEnum.ERRO_EXCLUIR_LISTA);
			}
		}else {
			addMensagemErro(MensagensFacesEnum.ERRO_SELECIONE_UM_ITEM_DA_LISTAGEM);
		}
	}

	public void doNovo() {
		this.listCliente = new SelectionList<Cliente>(new ArrayList<Cliente>());
		this.cliente = new Cliente();
		this.clienteSelecionado = new Cliente();
		this.funcionario = new Funcionario();
		this.funcionarioSelecionado = new Funcionario();
		this.responsavel = new Responsavel();
		this.responsavelSelecionado = new Responsavel();
		this.checkClienteAll = false;
		this.checkResponsavelAll = false;
		this.isDisableRe = false;
		this.isDisableNome = false;
	}
	
	public void doSalvar(){
		try {
			List<Cliente> clientes = this.listCliente.getAllItens();
			
			if(clientes.isEmpty()){
				addMensagemErro(MensagensFacesEnum.ERRO_NENHUM_CLIENTE_ADICIONADO_NA_LISTAGEM);
			}else{
				this.responsavel.setClientes(this.listCliente.getAllItens());
				rhModulo.salvarResponsavel(responsavel);
				doNovo();
				doConsultar();
			}
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_SALVAR);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
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
			responsavel.setFuncionario(funcionario);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	private Funcionario getFuncionarioPorRE() throws Exception {
		if(sugestoesFuncionarios != null){
			for(Funcionario func : sugestoesFuncionarios){
				if(func.getCodigoRegistro() != null && func.getCodigoRegistro().equals(responsavel.getFuncionario().getCodigoRegistro())){
					Funcionario funcionarioTemp = func.clone();
					funcionarioTemp.setPessoa(func.getPessoa().clone());
					return funcionarioTemp;
				}
			}
		}
		return null;
	}
	
	private boolean isRegistroEmpregadoPreenchido() {
		return responsavel.getFuncionario() != null
				&& responsavel.getFuncionario().getCodigoRegistro() != null
				&& responsavel.getFuncionario().getCodigoRegistro() > 0;
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
		responsavel.setFuncionario(funcionario);
	}
	
	public boolean isIncluirResponsavel(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isAlterarResponsavel(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_VALE_TRANSPORTE.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isExcluirResponsavel(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isConsultarResponsavel(){
		return isIncluirResponsavel() || isExcluirResponsavel() || isAlterarResponsavel() || isConsultar();
	}
	
	public boolean isManterResponsavel() {
		return isIncluirResponsavel() || isAlterarResponsavel();
	}
	
	public String doCadastroDeResponsavel() {
		doConsultar();
		return NavigationEnum.CADASTRO_DE_RESPONSAVEL.getValor();
	}
	
	public void doConsultar() {
		try {
			List<Responsavel> responsaveis = rhModulo.consultarTodosResponsaveis();
			this.listResponsavel = new SelectionList<Responsavel>(new ArrayList<Responsavel>(responsaveis));
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public void doAlterar() {
		try {
			responsavel = this.responsavelSelecionado.clone();
			Funcionario funcionario = this.responsavelSelecionado.getFuncionario().clone();
			funcionario.setPessoa(this.responsavelSelecionado.getFuncionario().getPessoa().clone());
			this.responsavel.setFuncionario(funcionario);
			this.listCliente = new SelectionList<Cliente>(
					new ArrayList<Cliente>(responsavel.getClientes()));
			this.isDisableRe = true;
			this.isDisableNome = true;
		} catch (CloneNotSupportedException e) {
			addMensagemErro(MensagensFacesEnum.ERRO_ALTERAR);
		}
	}
	
	public void doSelecionarCliente() {
		
	}
	
	public void doAdicionarCliente() {
		if(this.cliente != null && this.cliente.getNomeFantasia() != null) {
			SelectionEntity<Cliente> novoCliente = new SelectionEntity<Cliente>(this.cliente);
			if(!this.listCliente.contains(novoCliente)) {
				this.listCliente.add(novoCliente);
			}else {
				addMensagemErro(MensagensFacesEnum.ERRO_CLIENTE_JA_EXISTE_NA_LISTAGEM);
			}
		}else {
			addMensagemErro(MensagensFacesEnum.SELECIONE_UM_CLIENTE_PARA_ADICIONAR);
		}
		
	}
	
	public void doRemoverCliente() {
		this.listCliente.removeSelectedItem();
		this.checkClienteAll = false;
	}
	
	public void doLimparClienteSelecionado() {
		this.cliente = new Cliente();
		this.checkClienteAll = false;
	}
	
	/**
	 * @return the responsavelSelecionado
	 */
	public Responsavel getResponsavelSelecionado() {
		return responsavelSelecionado;
	}

	/**
	 * @param responsavelSelecionado the responsavelSelecionado to set
	 */
	public void setResponsavelSelecionado(final Responsavel responsavelSelecionado) {
		this.responsavelSelecionado = responsavelSelecionado;
	}

	/**
	 * @return the responsavel
	 */
	public Responsavel getResponsavel() {
		if(responsavel.getFuncionario() == null) {
			Funcionario funcionario = new Funcionario();
			funcionario.setPessoa(new Pessoa());
			responsavel.setFuncionario(funcionario);
		}
		return responsavel;
	}

	/**
	 * @param responsavel the responsavel to set
	 */
	public void setResponsavel(final Responsavel responsavel) {
		this.responsavel = responsavel;
	}

	/**
	 * @return the checkClienteAll
	 */
	public Boolean getCheckClienteAll() {
		return checkClienteAll;
	}

	/**
	 * @param checkClienteAll the checkClienteAll to set
	 */
	public void setCheckClienteAll(final Boolean checkClienteAll) {
		this.checkClienteAll = checkClienteAll;
	}

	/**
	 * @return the cliente
	 */
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * @param cliente the cliente to set
	 */
	public void setCliente(final Cliente cliente) {
		this.cliente = cliente;
	}

	/**
	 * @return the itensClientes
	 */
	public List<SelectItem> getItensClientes() {
		try {
			List<Cliente> listClientes = getClientes();
			if(listClientes != null){
				itensClientes.clear();
				addMock(itensClientes, MockEnum.SELECIONE);
				for(Cliente cliente : listClientes){
					String labelCliente = cliente.getCodigo() + " - " + cliente.getNomeFantasia();
					SelectItem item = new SelectItem(cliente, labelCliente);
					itensClientes.add(item);
				}
			}
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_BUSCAR_CLIENTES);
		}
		return itensClientes;
	}
	
	private List<Cliente> getClientes() throws PotiErpException {
		if(itensClientes.isEmpty()){
			return rhModulo.consultarClientesAtivos();
		}
		return null;
	}

	/**
	 * @param itensClientes the itensClientes to set
	 */
	public void setItensClientes(final List<SelectItem> itensClientes) {
		this.itensClientes = itensClientes;
	}

	/**
	 * @return the listCliente
	 */
	public SelectionList<Cliente> getListCliente() {
		return listCliente;
	}

	/**
	 * @param listCliente the listCliente to set
	 */
	public void setListCliente(final SelectionList<Cliente> listCliente) {
		this.listCliente = listCliente;
	}

	/**
	 * @return the clienteSelecionado
	 */
	public Cliente getClienteSelecionado() {
		return clienteSelecionado;
	}

	/**
	 * @param clienteSelecionado the clienteSelecionado to set
	 */
	public void setClienteSelecionado(final Cliente clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
	}

	/**
	 * @return the scrollerPageCliente
	 */
	public Integer getScrollerPageCliente() {
		return scrollerPageCliente;
	}

	/**
	 * @param scrollerPageCliente the scrollerPageCliente to set
	 */
	public void setScrollerPageCliente(final Integer scrollerPageCliente) {
		this.scrollerPageCliente = scrollerPageCliente;
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
}
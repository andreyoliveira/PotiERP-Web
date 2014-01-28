package br.com.potierp.modulos.rh.managed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import br.com.potierp.business.endereco.facade.EnderecoModulo;
import br.com.potierp.business.rh.facade.RhModulo;
import br.com.potierp.faces.converter.MockEnum;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.exception.PotiErpMensagensException;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.model.Empresa;
import br.com.potierp.model.Endereco;
import br.com.potierp.model.Estado;
import br.com.potierp.model.Fornecedor;
import br.com.potierp.model.Pais;
import br.com.potierp.model.Pessoa;
import br.com.potierp.model.SituacaoEnum;
import br.com.potierp.model.Telefone;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class FornecedorMB extends BaseMB {
	
	private static Logger log = Logger.getLogger(FornecedorMB.class);
	
	private Fornecedor fornecedor = new Fornecedor();
	
	private Fornecedor fornecedorSelecionado = new Fornecedor();
	
	private List<SelectItem> itensEmpresa = new ArrayList<SelectItem>();
	
	private SelectionList<Fornecedor> listFornecedor = new SelectionList<Fornecedor>();
	
	@EJB
	private RhModulo rhModulo;
	
	private boolean checkFornecedorAll = false;
	
	private Integer scrollerPage = 1;
	
	@EJB
	private EnderecoModulo enderecoModulo;
	
	private List<SelectItem> itensEstado = new ArrayList<SelectItem>();
	
	private boolean mostrarCnpj = false;
	
	private boolean mostrarCpf = false;
	
	public FornecedorMB() throws Exception {
		doNovo();
	}
	
	public void doNovo() throws Exception{
		fornecedor = new Fornecedor();
		fornecedor.setEndereco(new Endereco());
		fornecedor.setEmpresa(new Empresa());
		fornecedor.setContato1(new Pessoa());
		fornecedor.setContato2(new Pessoa());
		fornecedor.setTelefone1(new Telefone());
		fornecedor.setTelefone2(new Telefone());
		fornecedor.setSituacao(SituacaoEnum.ATIVO);
		fornecedor.setTipoDocumento("CNPJ");
		mostrarCnpj = true;
		mostrarCpf = false;
	}
	
	public void doSalvar(){
		try {
			if(isCamposValidos()){
				validarCamposNumericos();
				rhModulo.salvarFornecedor(fornecedor);
				doNovo();
			}
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_SALVAR);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public void doAlterar() throws CloneNotSupportedException{
		fornecedor = fornecedorSelecionado.clone();
		if(fornecedor.getContato1() == null){
			fornecedor.setContato1(new Pessoa());
			fornecedor.setTelefone1(new Telefone());
		}
		if(fornecedor.getContato2() == null){
			fornecedor.setContato2(new Pessoa());
			fornecedor.setTelefone2(new Telefone());
		}
		verificarTipoDocumento();
	}
	
	private void verificarTipoDocumento() {
		doDesabilitarCampoCnpjCpf();
	}
	
	private void validarCamposNumericos() {
		if(fornecedor.getDiaPagamento() != null && fornecedor.getDiaPagamento().equals(0)){
			fornecedor.setDiaPagamento(null);
		}
	}
	
	private boolean isCamposValidos() {
		return isDataVigenciaContratoValida();
	}

	private boolean isDataVigenciaContratoValida() {
		if(fornecedor.getDataInicioContrato() == null && fornecedor.getDataFinalContrato() != null){
			addMensagemErro(MensagensFacesEnum.ERRO_DATA_INICIO_CONTRATO_DEVE_SER_INFORMADA);
			return false;
		}else if(fornecedor.getDataInicioContrato() != null && fornecedor.getDataFinalContrato() != null){
			if(fornecedor.getDataInicioContrato().compareTo(fornecedor.getDataFinalContrato()) == 1){
				addMensagemErro(MensagensFacesEnum.ERRO_DATA_INICIO_DEVE_SER_MENOR_QUE_DATA_FINAL);
				return false;
			}
		}
		return true;
	}

	public void doExcluir(){
		try {
			rhModulo.excluirFornecedor(fornecedor);
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
			List<Fornecedor> list = listFornecedor.getItensSelecionados();
			rhModulo.excluirListaFornecedor(list);
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
			List<Fornecedor> list = rhModulo.consultarTodosFornecedores();
			listFornecedor = new SelectionList<Fornecedor>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public void doDesabilitarCampoCnpjCpf(){
		if("CPF".equalsIgnoreCase(fornecedor.getTipoDocumento())){
			if(mostrarCnpj && fornecedor.getCpfCnpj() != null && fornecedor.getCpfCnpj().length() > 11){
				fornecedor.setCpfCnpj(null);
			}
			mostrarCpf = true;
			mostrarCnpj = false;
		}else{
			if(mostrarCpf && fornecedor.getCpfCnpj() != null && fornecedor.getCpfCnpj().length() <= 11){
				fornecedor.setCpfCnpj(null);
			}
			mostrarCpf = false;
			mostrarCnpj = true;
		}
	}
	
	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(final Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public SelectionList<Fornecedor> getListFornecedor() {
		doConsultar();
		return listFornecedor;
	}

	public void setListFornecedor(final SelectionList<Fornecedor> listFornecedor) {
		this.listFornecedor = listFornecedor;
	}
	
	public List<SelectItem> getItensEmpresa() throws Exception {
		List<Empresa> listEmpresas = getEmpresas();
		if(listEmpresas != null){
			itensEmpresa.clear();
			addMock(itensEmpresa, MockEnum.SELECIONE);
			for(Empresa empresa : listEmpresas){
				SelectItem item = new SelectItem(empresa, empresa.getNomeFantasia());
				itensEmpresa.add(item);
			}
		}
		return itensEmpresa;
	}

	private List<Empresa> getEmpresas() throws PotiErpException {
		List<Empresa> listEmpresas = rhModulo.consultarTodasEmpresas();
		return listEmpresas;
	}

	public void setItensEmpresa(final List<SelectItem> itensEmpresa) {
		this.itensEmpresa = itensEmpresa;
	}

	public boolean isCheckFornecedorAll() {
		return checkFornecedorAll;
	}

	public void setCheckFornecedorAll(final boolean checkFornecedorAll) {
		this.checkFornecedorAll = checkFornecedorAll;
	}
	
	public Integer getScrollerPage() {
		return scrollerPage;
	}

	public void setScrollerPage(final Integer scrollerPage) {
		this.scrollerPage = scrollerPage;
	}
	
	public Fornecedor getFornecedorSelecionado() {
		return fornecedorSelecionado;
	}

	public void setFornecedorSelecionado(final Fornecedor fornecedorSelecionado) {
		this.fornecedorSelecionado = fornecedorSelecionado;
	}

	@Override
	public Logger getLogger() {
		return log;
	}
	
	public boolean isIncluirFornecedor(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_FORNECEDOR.getCodigo());
	}
	
	public boolean isAlterarFornecedor(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_FORNECEDOR.getCodigo());
	}
	
	public boolean isExcluirFornecedor(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_FORNECEDOR.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_FORNECEDOR.getCodigo());
	}
	
	public boolean isConsultarFornecedor(){
		 return isIncluirFornecedor() || isAlterarFornecedor() || isExcluirFornecedor() || isConsultar();
	}
	
	public boolean isManterFornecedor(){
		return isIncluirFornecedor() || isAlterarFornecedor();
	}
	
	public String doCadastroDeFornecedor(){
		return NavigationEnum.CADASTRO_DE_FORNECEDOR.getValor();
	}
	
	public List<SelectItem> getItensEstado() {
		List<Estado> listEstados = getEstados();
		if(listEstados != null){
			itensEstado.clear();
			addMock(itensEstado, MockEnum.SELECIONE);
			for(Estado estado : listEstados){
				SelectItem item = new SelectItem(estado, estado.getSigla());
				itensEstado.add(item);
			}
		}
		return itensEstado;
	}

	private List<Estado> getEstados() {
		try {
			//TODO Por enquanto o país é setado na mão. Arrumar o cadastro de País.
			Pais pais = this.enderecoModulo.buscarPaisPorSigla("BR");
			List<Estado> listEstados = enderecoModulo.buscarEstadosPorPais(pais);
			Collections.sort(listEstados);
			return listEstados;
		} catch (PotiErpException e) {
			addMensagemErro(e);
			return null;
		}
	}

	public boolean isMostrarCnpj() {
		return mostrarCnpj;
	}

	public void setMostrarCnpj(final boolean mostrarCnpj) {
		this.mostrarCnpj = mostrarCnpj;
	}

	public boolean isMostrarCpf() {
		return mostrarCpf;
	}

	public void setMostrarCpf(final boolean mostrarCpf) {
		this.mostrarCpf = mostrarCpf;
	}
}
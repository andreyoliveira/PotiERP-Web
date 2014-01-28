package br.com.potierp.modulos.rh.managed;

import static br.com.potierp.infra.msg.MensagensFacesEnum.ESTA_JORNADA_DE_TRABALHO_JA_FOI_ADICIONADA;
import static br.com.potierp.infra.msg.MensagensFacesEnum.ESTE_REAJUSTE_JA_FOI_ADICIONADO;
import static br.com.potierp.infra.msg.MensagensFacesEnum.ESTE_SETOR_JA_FOI_ADICIONADO;
import static br.com.potierp.infra.msg.MensagensFacesEnum.ESTE_TIPOSERVICO_E_PERIODICIDADE_JA_FORAM_ADICIONADAS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.component.UIForm;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import br.com.potierp.business.endereco.facade.EnderecoModulo;
import br.com.potierp.business.operacional.facade.OperacionalModulo;
import br.com.potierp.business.rh.facade.RhModulo;
import br.com.potierp.faces.converter.MockEnum;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.exception.PotiErpMensagensException;
import br.com.potierp.infra.helper.SelectionEntity;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.model.Cidade;
import br.com.potierp.model.Cliente;
import br.com.potierp.model.ContratoCliente;
import br.com.potierp.model.Empresa;
import br.com.potierp.model.Endereco;
import br.com.potierp.model.Estado;
import br.com.potierp.model.JornadaTrabalho;
import br.com.potierp.model.Pais;
import br.com.potierp.model.Periodicidade;
import br.com.potierp.model.Pessoa;
import br.com.potierp.model.ReajusteCliente;
import br.com.potierp.model.Setor;
import br.com.potierp.model.SituacaoEnum;
import br.com.potierp.model.Telefone;
import br.com.potierp.model.TipoServico;
import br.com.potierp.modulos.rh.managed.dataprovider.ClienteDataProvider;
import br.com.potierp.modulos.rh.managed.dataprovider.ClienteDataProvider.TipoBuscaClientes;
import br.com.potierp.modulos.rh.managed.dataprovider.PageableExtendedTableDataModel;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class ClienteMB extends BaseMB{

	private static Logger log = Logger.getLogger(ClienteMB.class);
	
	private Cliente cliente = new Cliente();
	
	private Cliente clienteSelecionado = new Cliente();
	
	private List<SelectItem> itensEmpresa = new ArrayList<SelectItem>();
	
	private SelectionList<Cliente> listCliente = new SelectionList<Cliente>();

	private List<SelectItem> itensEstado = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensPeriodicidade = new ArrayList<SelectItem>();
	
	private Setor setorSelecionado = new Setor();
	
	private JornadaTrabalho jornadaTrabalhoSelecionada = new JornadaTrabalho();
	
	private ContratoCliente contratoClienteSelecionado = new ContratoCliente();
	
	private ReajusteCliente reajusteClienteSelecionado = new ReajusteCliente();
	
	private SelectionList<Setor> listSetor = new SelectionList<Setor>(new ArrayList<Setor>());

	private SelectionList<JornadaTrabalho> listJornadaTrabalho = new SelectionList<JornadaTrabalho>(new ArrayList<JornadaTrabalho>());
	
	private SelectionList<ContratoCliente> listContratoCliente = new SelectionList<ContratoCliente>();
	
	private SelectionList<ReajusteCliente> listReajusteCliente = new SelectionList<ReajusteCliente>();
	
	private List<SelectItem> itensSetor = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensJornadaTrabalho = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensTipoServico = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensCidade = new ArrayList<SelectItem>();
	
	@EJB
	private RhModulo rhModulo;
	
	@EJB
	private EnderecoModulo enderecoModulo;
	
	@EJB
	private OperacionalModulo operacionalModulo;
	
	private boolean mostrarCnpj = false;
	
	private boolean mostrarCpf = false;
	
	private boolean isFiltrarClientesAtivos = true;
	
	private boolean isFiltrarClientesInativos = false;
	
	private boolean checkClienteAll = false;
	
	private boolean checkSetorAll = false;
	
	private boolean checkJornadaTrabalhoAll = false;
	
	private boolean checkContratoClienteAll = false;
	
	private boolean checkReajusteClienteAll = false;
	
	private Integer scrollerPage = 1;
	
	private Integer scrollerPageSetor = 1;
	
	private Integer scrollerPageJornadaTrabalho = 1;
	
	private Integer scrollerPageContratoCliente = 1;
	
	private Integer scrollerPageReajusteCliente = 1;
	
	private PageableExtendedTableDataModel<SelectionEntity<Cliente>> dataModel;
	

	
	public ClienteMB() throws Exception {
		doNovo();
	}
	
	public void doNovo() throws Exception{
		cliente = new Cliente();
		cliente.setEndereco(new Endereco());
		cliente.setFiliais(new ArrayList<Cliente>());
		cliente.setEmpresa(new Empresa());
		cliente.setCidade(new Cidade());
		cliente.setContato1(new Pessoa());
		cliente.setContato2(new Pessoa());
		cliente.setTelefone1(new Telefone());
		cliente.setTelefone2(new Telefone());
		cliente.setSituacao(SituacaoEnum.ATIVO);
		cliente.setIsTrabalhaFeriado(false);
		cliente.setIsSabadoDiaUtil(false);
		cliente.setIsDomingoDiaUtil(false);
		cliente.setTipoDocumento("CNPJ");
		cliente.setSetores(new ArrayList<Setor>());
		cliente.setJornadasTrabalho(new ArrayList<JornadaTrabalho>());
		cliente.setContratosCliente(new ArrayList<ContratoCliente>());
		cliente.setReajustesCliente(new ArrayList<ReajusteCliente>());
		mostrarCnpj = true;
		mostrarCpf = false;
		setorSelecionado = new Setor();
		listSetor = new SelectionList<Setor>(new ArrayList<Setor>());
		listJornadaTrabalho = new SelectionList<JornadaTrabalho>(new ArrayList<JornadaTrabalho>());
		listContratoCliente = new SelectionList<ContratoCliente>(new ArrayList<ContratoCliente>());
		listReajusteCliente = new SelectionList<ReajusteCliente>(new ArrayList<ReajusteCliente>());
	}
	
	public void doSalvar(){
		try {
			if(isCamposValidos()){
				validarCamposNumericos();
				rhModulo.salvarCliente(cliente);
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
	
	public void doAdicionarSetor() throws Exception {
		if (!isSetorAdicionado()) {
			Setor setorNovo = this.setorSelecionado.clone();
			this.listSetor.add(new SelectionEntity<Setor>(setorNovo));
			Collection<Setor> clienteSetor = cliente.getSetores();
			if(clienteSetor == null) {
				this.cliente.setSetores(new ArrayList<Setor>());
			}
			this.cliente.getSetores().add(setorNovo);
		}
	}
	
	private boolean isSetorAdicionado() {
		if (setorSelecionado != null) {
			List<Setor> listSetor = this.listSetor.getAllItens();
			if (listSetor.contains(this.setorSelecionado)) {
				addMensagemErro(ESTE_SETOR_JA_FOI_ADICIONADO);
				return true;
			}
		}
		return false;
	}
	
	public void doRemoverSetor() {
		List<Setor> setoresRemovidos = this.listSetor.getItensSelecionados();
		if(!setoresRemovidos.isEmpty()) {
			List<Setor> setores = this.cliente.getSetores();
			setores.removeAll(setoresRemovidos);
			this.listSetor = new SelectionList<Setor>(setores);
		}
	}
	
	public void doAdicionarContratoCliente() throws Exception {
		if(!isContratoDuplicado()){
			ContratoCliente contratoClienteNovo = contratoClienteSelecionado.clone();
			contratoClienteNovo.setPeriodicidade(contratoClienteSelecionado.getPeriodicidade().clone());
			contratoClienteNovo.setTipoServico(contratoClienteSelecionado.getTipoServico().clone());
			contratoClienteNovo.setCliente(cliente);
			this.listContratoCliente.add(new SelectionEntity<ContratoCliente>(contratoClienteNovo));
			Collection<ContratoCliente> contratos = cliente.getContratosCliente();
			if(contratos == null) {
				this.cliente.setContratosCliente(new ArrayList<ContratoCliente>());
			}
			this.cliente.getContratosCliente().add(contratoClienteNovo);
			contratoClienteSelecionado = new ContratoCliente();
		}
	}
	
	private boolean isContratoDuplicado() {
		if (contratoClienteSelecionado != null) {
			List<ContratoCliente> contratos = this.listContratoCliente.getAllItens();
			for(ContratoCliente contratoCliente : contratos){
				if(contratoClienteSelecionado.getTipoServico().equals(contratoCliente.getTipoServico())
						&& contratoClienteSelecionado.getPeriodicidade().equals(contratoCliente.getPeriodicidade())){
					addMensagemErro(ESTE_TIPOSERVICO_E_PERIODICIDADE_JA_FORAM_ADICIONADAS);
					return true;
				}
			}
		}
		return false;
	}
	
	public void doRemoverContratoCliente() {
		List<ContratoCliente> contratosRemovidos = this.listContratoCliente.getItensSelecionados();
		if(!contratosRemovidos.isEmpty()) {
			this.cliente.setContratosCliente(listContratoCliente.getAllItens());
			List<ContratoCliente> contratos = this.cliente.getContratosCliente();
			for(ContratoCliente contratoRemovido : contratosRemovidos){
				for(int i = 0; i <  contratos.size(); i++){
					ContratoCliente contratoCliente = contratos.get(i);
					if(contratoRemovido.getTipoServico().equals(contratoCliente.getTipoServico())
							&& contratoRemovido.getPeriodicidade().equals(contratoCliente.getPeriodicidade())){
						contratos.remove(i);
						break;
					}
				}
			}
			this.listContratoCliente = new SelectionList<ContratoCliente>(contratos);
		}
	}

	public void doLimparSelecionadosContratoCliente() {
		this.contratoClienteSelecionado = new ContratoCliente();
	}
	
	public void doAdicionarReajusteCliente() throws Exception {
		if(!isReajusteDuplicado()){
			ReajusteCliente reajusteClienteNovo = reajusteClienteSelecionado.clone();
			reajusteClienteNovo.setCliente(cliente);
			this.listReajusteCliente.add(new SelectionEntity<ReajusteCliente>(reajusteClienteNovo));
			Collection<ReajusteCliente> reajustes = cliente.getReajustesCliente();
			if(reajustes == null) {
				this.cliente.setReajustesCliente(new ArrayList<ReajusteCliente>());
			}
			this.cliente.getReajustesCliente().add(reajusteClienteNovo);
			reajusteClienteSelecionado = new ReajusteCliente();
		}
	}
	
	private boolean isReajusteDuplicado() {
		if (reajusteClienteSelecionado != null) {
			List<ReajusteCliente> reajustes = this.listReajusteCliente.getAllItens();
			for(ReajusteCliente reajusteCliente : reajustes){
				if(reajusteClienteSelecionado.getDataReajuste().equals(reajusteCliente.getDataReajuste())){
					addMensagemErro(ESTE_REAJUSTE_JA_FOI_ADICIONADO);
					return true;
				}
			}
		}
		return false;
	}
	
	public void doRemoverReajusteCliente() {
		List<ReajusteCliente> reajustesRemovidos = this.listReajusteCliente.getItensSelecionados();
		if(!reajustesRemovidos.isEmpty()) {
			this.cliente.setReajustesCliente(listReajusteCliente.getAllItens());
			List<ReajusteCliente> reajustes = this.cliente.getReajustesCliente();
			for(ReajusteCliente reajusteRemovido : reajustesRemovidos){
				for(int i = 0; i <  reajustes.size(); i++){
					ReajusteCliente reajusteCliente = reajustes.get(i);
					if(reajusteRemovido.getDataReajuste().equals(reajusteCliente.getDataReajuste())
							&& reajusteRemovido.getValor().equals(reajusteCliente.getValor())){
						reajustes.remove(i);
						break;
					}
				}
			}
			this.listReajusteCliente = new SelectionList<ReajusteCliente>(reajustes);
		}
	}

	public void doLimparSelecionadosReajusteCliente() {
		this.reajusteClienteSelecionado = new ReajusteCliente();
	}
	
	public void doAdicionarJornadaTrabalho() throws Exception {
		if (!isJornadaTrabalhoAdicionado()) {
			JornadaTrabalho jornadaTrabalhoNovo = this.jornadaTrabalhoSelecionada.clone();
			this.listJornadaTrabalho.add(new SelectionEntity<JornadaTrabalho>(jornadaTrabalhoNovo));
			Collection<JornadaTrabalho> clienteJornadaTrabalho = cliente.getJornadasTrabalho();
			if(clienteJornadaTrabalho == null) {
				this.cliente.setJornadasTrabalho(new ArrayList<JornadaTrabalho>());
			}
			this.cliente.getJornadasTrabalho().add(jornadaTrabalhoNovo);
		}
	}
	
	private boolean isJornadaTrabalhoAdicionado() {
		if (jornadaTrabalhoSelecionada != null) {
			List<JornadaTrabalho> listJornadaTrabalho = this.listJornadaTrabalho.getAllItens();
			if (listJornadaTrabalho.contains(this.jornadaTrabalhoSelecionada)) {
				addMensagemErro(ESTA_JORNADA_DE_TRABALHO_JA_FOI_ADICIONADA);
				return true;
			}
		}
		return false;
	}
	
	public void doRemoverJornadaTrabalho() {
		List<JornadaTrabalho> jornadasRemovidas = this.listJornadaTrabalho.getItensSelecionados();
		if(!jornadasRemovidas.isEmpty()) {
			List<JornadaTrabalho> jornadasTrabalho = this.cliente.getJornadasTrabalho();
			jornadasTrabalho.removeAll(jornadasRemovidas);
			this.listJornadaTrabalho = new SelectionList<JornadaTrabalho>(jornadasTrabalho);
		}
	}

	public void doLimparSelecionadosJornadaTrabalho() {
		this.jornadaTrabalhoSelecionada = new JornadaTrabalho();
	}

	public void doLimparSelecionadosSetor() {
		this.setorSelecionado = new Setor();
	}
	
	public void limparForm() throws Exception {
		UIForm form = (UIForm) getFacesContext().getViewRoot().findComponent("formDetails");
		cleanSubmittedValues(form);
		doNovo();
		clienteSelecionado = new Cliente();
	}
	
	private void validarCamposNumericos() {
		if(cliente.getDiaPagamento() != null && cliente.getDiaPagamento().equals(0)){
			cliente.setDiaPagamento(null);
		}
	}

	private boolean isCamposValidos() {
		return isDataVigenciaContratoValida();
	}

	private boolean isDataVigenciaContratoValida() {
		if(cliente.getDataInicioContrato() == null && cliente.getDataFinalContrato() != null){
			addMensagemErro(MensagensFacesEnum.ERRO_DATA_INICIO_CONTRATO_DEVE_SER_INFORMADA);
			return false;
		}else if(cliente.getDataInicioContrato() != null && cliente.getDataFinalContrato() != null){
			if(cliente.getDataInicioContrato().compareTo(cliente.getDataFinalContrato()) == 1){
				addMensagemErro(MensagensFacesEnum.ERRO_DATA_INICIO_DEVE_SER_MENOR_QUE_DATA_FINAL);
				return false;
			}
		}
		return true;
	}
	
	public void doAlterar() throws Exception{
		doNovo();
		cliente = rhModulo.consultarClientePorId(clienteSelecionado.getId());
		if(cliente.getContato1() == null){
			cliente.setContato1(new Pessoa());
			cliente.setContato2(new Pessoa());
			cliente.setTelefone1(new Telefone());
			cliente.setTelefone2(new Telefone());
		}
		popularCidade();
		popularSetores();
		popularJornadasTrabalho();
		popularContrato();
		popularReajuste();
		verificarTipoDocumento();
	}

	private void verificarTipoDocumento() {
		doDesabilitarCampoCnpjCpf();
	}

	public void doExcluir(){
		try {
			rhModulo.excluirCliente(cliente);
			doNovo();
			doConsultar();
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
			List<Cliente> list = ((ClienteDataProvider) dataModel.getDataProvider()).getItensSelecionados();
			rhModulo.excluirListaCliente(list, getTraceInfo());
			doNovo();
			doConsultar();
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
			if(isFiltrarClientesAtivos && isFiltrarClientesInativos){
				this.dataModel = new PageableExtendedTableDataModel<SelectionEntity<Cliente>>(
						new ClienteDataProvider(TipoBuscaClientes.TODOS, rhModulo));
			}else if (isFiltrarClientesAtivos){
				this.dataModel = new PageableExtendedTableDataModel<SelectionEntity<Cliente>>(
						new ClienteDataProvider(TipoBuscaClientes.ATIVOS, rhModulo));
			}else if (isFiltrarClientesInativos){
				this.dataModel = new PageableExtendedTableDataModel<SelectionEntity<Cliente>>(
						new ClienteDataProvider(TipoBuscaClientes.INATIVOS, rhModulo));
			}
		} catch (Exception e){
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		}
	}
	
	public void doDesabilitarCampoCnpjCpf(){
		if("CPF".equalsIgnoreCase(cliente.getTipoDocumento())){
			if(mostrarCnpj && cliente.getCpfCnpj() != null && cliente.getCpfCnpj().length() > 11){
				cliente.setCpfCnpj(null);
			}
			mostrarCpf = true;
			mostrarCnpj = false;
		}else{
			if(mostrarCpf && cliente.getCpfCnpj() != null && cliente.getCpfCnpj().length() <= 11){
				cliente.setCpfCnpj(null);
			}
			mostrarCpf = false;
			mostrarCnpj = true;
		}
	}

	public void popularCidade() throws PotiErpException{
		List<Cidade> listCidades = getCidades();
		if(listCidades != null){
			itensCidade.clear();
			addMock(itensCidade, MockEnum.SELECIONE);
			for(Cidade cidade : listCidades){
				SelectItem item = new SelectItem(cidade, cidade.getNome());
				itensCidade.add(item);
			}
		}
	}
	
	private List<Cidade> getCidades() throws PotiErpException {
		if(cliente.getEndereco().getEstado() != null 
				&& cliente.getEndereco().getEstado().getId() != null
				&& !cliente.getEndereco().getEstado().getId().equals(Long.valueOf(0))){
			return enderecoModulo.consultarPorEstado(cliente.getEndereco().getEstado());
		}
		return null;
	}
	
	public void popularSetores() throws PotiErpException{
		List<Setor> setores = this.cliente.getSetores();
		if(setores != null && !setores.isEmpty()){
			this.listSetor = new SelectionList<Setor>(setores);
		}
	}
	
	public void popularJornadasTrabalho() throws PotiErpException{
		List<JornadaTrabalho> jornadasTrabalho = this.cliente.getJornadasTrabalho();
		if(jornadasTrabalho != null && !jornadasTrabalho.isEmpty()){
			this.listJornadaTrabalho = new SelectionList<JornadaTrabalho>(jornadasTrabalho);
		}
	}
	
	public void popularContrato() throws PotiErpException{
		List<ContratoCliente> contratos = this.cliente.getContratosCliente();
		if(contratos != null && !contratos.isEmpty()){
			this.listContratoCliente = new SelectionList<ContratoCliente>(contratos);
		}
	}
	
	public void popularReajuste() throws PotiErpException{
		List<ReajusteCliente> reajustes = this.cliente.getReajustesCliente();
		if(reajustes != null && !reajustes.isEmpty()){
			this.listReajusteCliente = new SelectionList<ReajusteCliente>(reajustes);
		}
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(final Cliente cliente) {
		this.cliente = cliente;
	}

	public SelectionList<Cliente> getListCliente() {
		return listCliente;
	}
	
	public PageableExtendedTableDataModel<SelectionEntity<Cliente>> getDataModel() {
		return dataModel;
	}

	public void setDataModel(final PageableExtendedTableDataModel<SelectionEntity<Cliente>> dataModel) {
		this.dataModel = dataModel;
	}

	public void setListCliente(final SelectionList<Cliente> listCliente) {
		this.listCliente = listCliente;
	}
	
	public List<SelectItem> getItensEmpresa() throws PotiErpException {
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
	
	public List<SelectItem> getItensCidade() throws PotiErpException {
		if(itensCidade.isEmpty()){
			itensCidade.clear();
			addMock(itensCidade, MockEnum.SELECIONE);
		}
		return itensCidade;
	}

	public void setItensCidade(final List<SelectItem> itensCidade) {
		this.itensCidade = itensCidade;
	}

	public boolean isCheckClienteAll() {
		return checkClienteAll;
	}

	public void setCheckClienteAll(final boolean checkClienteAll) {
		this.checkClienteAll = checkClienteAll;
	}
	
	public Integer getScrollerPage() {
		return scrollerPage;
	}
	
	public void setScrollerPage(final Integer scrollerPage) {
		this.scrollerPage = scrollerPage;
	}

	public boolean getIsFiltrarClientesAtivos() {
		return isFiltrarClientesAtivos;
	}

	public void setIsFiltrarClientesAtivos(final boolean isFiltrarClientesAtivos) {
		this.isFiltrarClientesAtivos = isFiltrarClientesAtivos;
	}

	public boolean getIsFiltrarClientesInativos() {
		return isFiltrarClientesInativos;
	}

	public void setIsFiltrarClientesInativos(final boolean isFiltrarClientesInativos) {
		this.isFiltrarClientesInativos = isFiltrarClientesInativos;
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
		
	public Cliente getClienteSelecionado() {
		return clienteSelecionado;
	}

	public void setClienteSelecionado(final Cliente clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
	}

	@Override
	public Logger getLogger() {
		return log;
	}
	
	public boolean isIncluirCliente(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_CLIENTE.getCodigo());
	}
	
	public boolean isAlterarCliente(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_CLIENTE.getCodigo());
	}
	
	public boolean isExcluirCliente(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_CLIENTE.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_CLIENTE.getCodigo());
	}
	
	public boolean isConsultarCliente(){
		return isIncluirCliente() || isAlterarCliente() || isExcluirCliente() || isConsultar();	
	}
	
	public boolean isManterCliente(){
		return isIncluirCliente() || isAlterarCliente();
	}
	
	public String doCadastroDeCliente(){
		doConsultar();
		return NavigationEnum.CADASTRO_DE_CLIENTE.getValor();
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
	
	/**
	 * @return the itensPeriodicidade
	 * @throws Exception 
	 */
	public List<SelectItem> getItensPeriodicidade() throws Exception {
		if(itensPeriodicidade.isEmpty()){
			List<Periodicidade> listPeriodicidades = rhModulo.getAllPeriodicidade();
			if(listPeriodicidades != null){
				itensPeriodicidade.clear();
				addMock(itensPeriodicidade, MockEnum.SELECIONE);
				for(Periodicidade periodicidade : listPeriodicidades){
					SelectItem item = new SelectItem(periodicidade, periodicidade.getNome());
					itensPeriodicidade.add(item);
				}
			}
		}
		return itensPeriodicidade;
	}
	
	/**
	 * @param itensPeriodicidade the itensPeriodicidade to set
	 */
	public void setItensPeriodicidade(final List<SelectItem> itensPeriodicidade) {
		this.itensPeriodicidade = itensPeriodicidade;
	}

	public List<SelectItem> getItensSetor() throws PotiErpException {
		List<Setor> listSetores = this.rhModulo.consultarTodosSetores();
		if(listSetores != null){
			this.itensSetor.clear();
			addMock(this.itensSetor, MockEnum.SELECIONE);
			for(Setor setor : listSetores){
				SelectItem item = new SelectItem(setor, setor.getNome());
				this.itensSetor.add(item);
			}
		}
		return itensSetor;
	}

	public void setItensSetor(final List<SelectItem> itensSetor) {
		this.itensSetor = itensSetor;
	}
	
	public List<SelectItem> getItensJornadaTrabalho() throws PotiErpException {
		List<JornadaTrabalho> jornadasTrabalho = this.rhModulo.consultarTodasJornadasTrabalho();
		if(jornadasTrabalho != null){
			this.itensJornadaTrabalho.clear();
			addMock(this.itensJornadaTrabalho, MockEnum.SELECIONE);
			for(JornadaTrabalho jornadaTrabalho : jornadasTrabalho){
				SelectItem item = new SelectItem(jornadaTrabalho, jornadaTrabalho.getDescricao());
				this.itensJornadaTrabalho.add(item);
			}
		}
		return itensJornadaTrabalho;
	}

	public void setItensJornadaTrabalho(final List<SelectItem> itensJornadaTrabalho) {
		this.itensJornadaTrabalho = itensJornadaTrabalho;
	}

	public Setor getSetorSelecionado() {
		return setorSelecionado;
	}

	public void setSetorSelecionado(final Setor setorSelecionado) {
		this.setorSelecionado = setorSelecionado;
	}

	public SelectionList<Setor> getListSetor() {
		return listSetor;
	}

	public void setListSetor(final SelectionList<Setor> listSetor) {
		this.listSetor = listSetor;
	}

	public boolean isCheckSetorAll() {
		return checkSetorAll;
	}

	public void setCheckSetorAll(final boolean checkSetorAll) {
		this.checkSetorAll = checkSetorAll;
	}

	public Integer getScrollerPageSetor() {
		return scrollerPageSetor;
	}

	public void setScrollerPageSetor(final Integer scrollerPageSetor) {
		this.scrollerPageSetor = scrollerPageSetor;
	}

	public JornadaTrabalho getJornadaTrabalhoSelecionada() {
		return jornadaTrabalhoSelecionada;
	}

	public void setJornadaTrabalhoSelecionada(
			final JornadaTrabalho jornadaTrabalhoSelecionada) {
		this.jornadaTrabalhoSelecionada = jornadaTrabalhoSelecionada;
	}
	
	/**
	 * @return the contratoClienteSelecionado
	 */
	public ContratoCliente getContratoClienteSelecionado() {
		return contratoClienteSelecionado;
	}

	/**
	 * @param contratoClienteSelecionado the contratoClienteSelecionado to set
	 */
	public void setContratoClienteSelecionado(
			final ContratoCliente contratoClienteSelecionado) {
		this.contratoClienteSelecionado = contratoClienteSelecionado;
	}

	public SelectionList<JornadaTrabalho> getListJornadaTrabalho() {
		return listJornadaTrabalho;
	}

	public void setListJornadaTrabalho(
			final SelectionList<JornadaTrabalho> listJornadaTrabalho) {
		this.listJornadaTrabalho = listJornadaTrabalho;
	}
	
	/**
	 * @return the listContratoCliente
	 */
	public SelectionList<ContratoCliente> getListContratoCliente() {
		return listContratoCliente;
	}

	/**
	 * @param listContratoCliente the listContratoCliente to set
	 */
	public void setListContratoCliente(
			final SelectionList<ContratoCliente> listContratoCliente) {
		this.listContratoCliente = listContratoCliente;
	}
	
	/**
	 * @return the reajusteClienteSelecionado
	 */
	public ReajusteCliente getReajusteClienteSelecionado() {
		return reajusteClienteSelecionado;
	}

	/**
	 * @param reajusteClienteSelecionado the reajusteClienteSelecionado to set
	 */
	public void setReajusteClienteSelecionado(
			final ReajusteCliente reajusteClienteSelecionado) {
		this.reajusteClienteSelecionado = reajusteClienteSelecionado;
	}

	/**
	 * @return the listReajusteCliente
	 */
	public SelectionList<ReajusteCliente> getListReajusteCliente() {
		return listReajusteCliente;
	}

	/**
	 * @param listReajusteCliente the listReajusteCliente to set
	 */
	public void setListReajusteCliente(
			final SelectionList<ReajusteCliente> listReajusteCliente) {
		this.listReajusteCliente = listReajusteCliente;
	}

	/**
	 * @return the checkReajusteClienteAll
	 */
	public boolean isCheckReajusteClienteAll() {
		return checkReajusteClienteAll;
	}

	/**
	 * @param checkReajusteClienteAll the checkReajusteClienteAll to set
	 */
	public void setCheckReajusteClienteAll(final boolean checkReajusteClienteAll) {
		this.checkReajusteClienteAll = checkReajusteClienteAll;
	}

	/**
	 * @return the scrollerPageReajusteCliente
	 */
	public Integer getScrollerPageReajusteCliente() {
		return scrollerPageReajusteCliente;
	}

	/**
	 * @param scrollerPageReajusteCliente the scrollerPageReajusteCliente to set
	 */
	public void setScrollerPageReajusteCliente(final Integer scrollerPageReajusteCliente) {
		this.scrollerPageReajusteCliente = scrollerPageReajusteCliente;
	}

	/**
	 * @return the itensTipoServico
	 */
	public List<SelectItem> getItensTipoServico() throws PotiErpException {
		if(this.itensTipoServico.isEmpty()) {
			List<TipoServico> listTipoServico =	this.operacionalModulo.getAllTipoServico(getTraceInfo());
			if(listTipoServico != null) {
				this.itensTipoServico.clear();
				addMock(this.itensTipoServico, MockEnum.SELECIONE);
				for(TipoServico tipoServico : listTipoServico) {
					SelectItem item = new SelectItem(tipoServico, tipoServico.getDescricao());
					this.itensTipoServico.add(item);
				}
			}
		}
		return itensTipoServico;
	}

	/**
	 * @param itensTipoServico the itensTipoServico to set
	 */
	public void setItensTipoServico(final List<SelectItem> itensTipoServico) {
		this.itensTipoServico = itensTipoServico;
	}

	/**
	 * @return the checkContratoClienteAll
	 */
	public boolean isCheckContratoClienteAll() {
		return checkContratoClienteAll;
	}

	/**
	 * @param checkContratoClienteAll the checkContratoClienteAll to set
	 */
	public void setCheckContratoClienteAll(final boolean checkContratoClienteAll) {
		this.checkContratoClienteAll = checkContratoClienteAll;
	}

	/**
	 * @return the scrollerPageContratoCliente
	 */
	public Integer getScrollerPageContratoCliente() {
		return scrollerPageContratoCliente;
	}

	/**
	 * @param scrollerPageContratoCliente the scrollerPageContratoCliente to set
	 */
	public void setScrollerPageContratoCliente(final Integer scrollerPageContratoCliente) {
		this.scrollerPageContratoCliente = scrollerPageContratoCliente;
	}

	public boolean isCheckJornadaTrabalhoAll() {
		return checkJornadaTrabalhoAll;
	}

	public void setCheckJornadaTrabalhoAll(final boolean checkJornadaTrabalhoAll) {
		this.checkJornadaTrabalhoAll = checkJornadaTrabalhoAll;
	}

	public Integer getScrollerPageJornadaTrabalho() {
		return scrollerPageJornadaTrabalho;
	}

	public void setScrollerPageJornadaTrabalho(final Integer scrollerPageJornadaTrabalho) {
		this.scrollerPageJornadaTrabalho = scrollerPageJornadaTrabalho;
	}
}
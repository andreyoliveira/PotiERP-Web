package br.com.potierp.modulos.rh.managed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.component.UIForm;
import javax.faces.model.SelectItem;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.richfaces.model.selection.Selection;
import org.richfaces.model.selection.SimpleSelection;

import br.com.potierp.business.endereco.facade.EnderecoModulo;
import br.com.potierp.business.rh.facade.RhModulo;
import br.com.potierp.faces.converter.MockEnum;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.exception.PotiErpMensagensException;
import br.com.potierp.infra.helper.SelectionEntity;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.model.Cargo;
import br.com.potierp.model.CarteiraHabilitacao;
import br.com.potierp.model.CertificadoReservistaEnum;
import br.com.potierp.model.CestaBasica;
import br.com.potierp.model.Cliente;
import br.com.potierp.model.ConselhoRegional;
import br.com.potierp.model.ContaBancaria;
import br.com.potierp.model.Ctps;
import br.com.potierp.model.CtpsEstrangeiro;
import br.com.potierp.model.Dependente;
import br.com.potierp.model.DiaSemanaEnum;
import br.com.potierp.model.DocumentoMilitar;
import br.com.potierp.model.Empresa;
import br.com.potierp.model.Endereco;
import br.com.potierp.model.EscolaridadeEnum;
import br.com.potierp.model.Estado;
import br.com.potierp.model.EstadoCivilEnum;
import br.com.potierp.model.FormaPagamento;
import br.com.potierp.model.Funcionario;
import br.com.potierp.model.GrauParentesco;
import br.com.potierp.model.JornadaTrabalho;
import br.com.potierp.model.LocalTrabalho;
import br.com.potierp.model.Pais;
import br.com.potierp.model.Pessoa;
import br.com.potierp.model.Pis;
import br.com.potierp.model.RacaEnum;
import br.com.potierp.model.Rg;
import br.com.potierp.model.RneEstrangeiro;
import br.com.potierp.model.RumoTransporteEnum;
import br.com.potierp.model.Setor;
import br.com.potierp.model.SituacaoFgts;
import br.com.potierp.model.SituacaoFuncionario;
import br.com.potierp.model.Telefone;
import br.com.potierp.model.TipoAdmissao;
import br.com.potierp.model.TipoCestaBasica;
import br.com.potierp.model.TipoContaBancariaEnum;
import br.com.potierp.model.TipoContratoEnum;
import br.com.potierp.model.TipoValeRefeicao;
import br.com.potierp.model.TipoValeTransporte;
import br.com.potierp.model.TituloEleitor;
import br.com.potierp.model.ValeRefeicao;
import br.com.potierp.model.ValeTransporte;
import br.com.potierp.model.VinculoEmpregaticio;
import br.com.potierp.modulos.rh.managed.dataprovider.FuncionarioDataProvider;
import br.com.potierp.modulos.rh.managed.dataprovider.FuncionarioDataProvider.TipoBuscaFuncionarios;
import br.com.potierp.modulos.rh.managed.dataprovider.PageableExtendedTableDataModel;
import br.com.potierp.util.DateUtil;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;


public class FuncionarioMB extends BaseMB {
	
	private static Logger log = Logger.getLogger(FuncionarioMB.class);
	
	private Funcionario funcionario = new Funcionario();
	
	private Funcionario funcionarioSelecionado1 = new Funcionario();
	
	private Selection funcionarioSelecionado = new SimpleSelection();
	
	private PageableExtendedTableDataModel<SelectionEntity<Funcionario>> dataModel;
	
	private Cliente clientePesquisa = new Cliente();
	
	private SituacaoFuncionario situacaoFuncionarioPesquisa = new SituacaoFuncionario();
	
	private Dependente dependente = new Dependente();
	
	private Dependente dependenteSelecionado = new Dependente();
	
	private ValeTransporte valeTransporte = new ValeTransporte();
	
	private ValeTransporte valeTransporteSelecionado = new ValeTransporte();
	
	private ValeRefeicao valeRefeicao = new ValeRefeicao();
	
	private ValeRefeicao valeRefeicaoSelecionado = new ValeRefeicao();
	
	private CestaBasica cestaBasica = new CestaBasica();
	
	private CestaBasica cestaBasicaSelecionada = new CestaBasica();
	
	private List<SelectItem> itensTipoTransporte = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensTipoRefeicao = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensTipoCestaBasica = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensRumoTransporte = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensClientes = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensClientesVT = new ArrayList<SelectItem>();

	private List<SelectItem> itensCargo = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensEmpresa = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensEstadosCivis = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensDiaSemana = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensTipoContaBancaria = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensSetor = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensJornadaTrabalho = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensFormaPagamento = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensTipoContrato = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensCertificadoReservista = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensTipoAdmissao = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensVinculoEmpregaticio = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensSituacaoFuncionario = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensGrauParentesco = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensRaca = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensEstado = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensEscolaridade = new ArrayList<SelectItem>();
	
	private SelectionList<Funcionario> listFuncionario = new SelectionList<Funcionario>();
	
	private SelectionList<LocalTrabalho> listLocalTrabalho = new SelectionList<LocalTrabalho>();
	
	private LocalTrabalho localTrabalho = new LocalTrabalho();
	
	private LocalTrabalho localTrabalhoSelecionado = new LocalTrabalho();
	
	private SelectionList<Dependente> listDependente = new SelectionList<Dependente>();
	
	private SelectionList<ValeTransporte> listValeTransporte = new SelectionList<ValeTransporte>();
	
	private SelectionList<ValeRefeicao> listValeRefeicao = new SelectionList<ValeRefeicao>();
	
	private boolean isDadosPessoaisCadastrados = false;
	
	private boolean isEnderecoCadastrado = false;
	
	private boolean isDocumentosCadastrados = false;
	
	private boolean isContatosCadastrados = false;
	
	private boolean isDocumentosEstrangeiroCadastrados = false;
	
	private boolean isDadosFuncionaisCadastrados = false;
	
	private boolean isDependentesCadastrados = false;
	
	private boolean isDadosBancariosCadastrados = false;
	
	private boolean isLocalDeTrabalhoCadastrado = false;
	
	private boolean checkFuncionarioAll = false;
	
	private boolean checkLocalTrabalhoAll = false;
	
	private boolean checkDependenteAll = false;
	
	private boolean checkValeTransporteAll = false;
	
	private boolean checkValeRefeicaoAll = false;
	
	
	private String filtroCodigoRegistro;
	
	private String filtroNome;
	
	private String filtroCtps;
	
	private String filtroSerie;
	
	private String filtroRg;
	
	private String filtroCpf;
	
	private String filtroCargo;
	
	private String filtroDataAdmissao;
	
	private Integer scrollerPage = 1;
	
	private Integer scrollerPageDependentes = 1;
	
	private Long identificador = 0L; 
	
	@EJB
	private EnderecoModulo enderecoModulo;
	
	@EJB
	private RhModulo rhModulo;
	
	public FuncionarioMB() throws Exception {
		doNovo();
		situacaoFuncionarioPesquisa = new SituacaoFuncionario();
	}
	
	public void doNovo() throws Exception{
		funcionario = new Funcionario();
		funcionario.setEmpresa(new Empresa());
		funcionario.setPessoa(new Pessoa());
		funcionario.getPessoa().setEndereco(new Endereco());
		funcionario.setCargo(new Cargo());
		funcionario.setCarteiraHabilitacao(new CarteiraHabilitacao());
		funcionario.setConselhoRegional(new ConselhoRegional());
		funcionario.setContaBancaria(new ContaBancaria());
		funcionario.setCtps(new Ctps());
		funcionario.setCtpsEstrangeiro(new CtpsEstrangeiro());
		funcionario.setDocumentoMilitar(new DocumentoMilitar());
		funcionario.setFormaPagamento(new FormaPagamento());
		funcionario.setPis(new Pis());
		funcionario.setRg(new Rg());
		funcionario.setRneEstrangeiro(new RneEstrangeiro());
		funcionario.setSituacaoFgts(new SituacaoFgts());
		funcionario.setTituloEleitor(new TituloEleitor());
		funcionario.setTipoAdmissao(new TipoAdmissao());
		funcionario.setSituacaoFuncionario(new SituacaoFuncionario());
		funcionario.setVinculoEmpregaticio(new VinculoEmpregaticio());
		funcionario.setTelefoneCelular(new Telefone());
		funcionario.setTelefoneCelularRecado1(new Telefone());
		funcionario.setTelefoneCelularRecado2(new Telefone());
		funcionario.setTelefoneRecado1(new Telefone());
		funcionario.setTelefoneRecado2(new Telefone());
		funcionario.setTelefoneResidencial(new Telefone());
		isDadosPessoaisCadastrados = false;
		isEnderecoCadastrado = false;
		isDocumentosCadastrados = false;
		isContatosCadastrados = false;
		isDocumentosEstrangeiroCadastrados = false;
		isDadosFuncionaisCadastrados = false;
		isDependentesCadastrados = false;
		isDadosBancariosCadastrados = false;
		isLocalDeTrabalhoCadastrado = false;
		listLocalTrabalho = new SelectionList<LocalTrabalho>();
		listDependente = new SelectionList<Dependente>();
		listValeTransporte = new SelectionList<ValeTransporte>();
		listValeRefeicao = new SelectionList<ValeRefeicao>();
		doLimparSelecionadosLocalTrabalho();
		doLimparDependenteSelecionado();
		doLimparValeTransporteSelecionado();
		doLimparValeRefeicaoSelecionado();
		doLimparCestaBasicaSelecionado();
		identificador = 0L;
		limparForm();
	}

	private void doLimparValeRefeicaoSelecionado() {
		valeRefeicao = new ValeRefeicao();
		valeRefeicaoSelecionado = new ValeRefeicao();
	}
	
	private void doLimparCestaBasicaSelecionado() {
		cestaBasica = new CestaBasica();
		cestaBasicaSelecionada = new CestaBasica();
	}

	private void doLimparValeTransporteSelecionado() {
		valeTransporte = new ValeTransporte();
		valeTransporteSelecionado = new ValeTransporte();
	}

	private void doLimparDependenteSelecionado() {
		dependente = new Dependente();
		dependenteSelecionado = new Dependente();
	}

	public void doSalvar(){
		try {
			atribuirDependencias();
			funcionario = rhModulo.salvarFuncionario(funcionario, getTraceInfo());
			doConsultarPesquisa();
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_SALVAR);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public void limparForm() throws Exception {
		UIForm form = (UIForm) getFacesContext().getViewRoot().findComponent("formDetails");
		cleanSubmittedValues(form);
	}

	private void atribuirDependencias() {
		atribuirLocalTrabalho();
		atribuirDependentes();
		atribuirValeTransporte();
		atribuirValeRefeicao();
		atribuirCestaBasica();
	}

	private void atribuirLocalTrabalho() {
		List<LocalTrabalho> list = new ArrayList<LocalTrabalho>();
		for(SelectionEntity<LocalTrabalho> selectionLocalTrabalho : listLocalTrabalho){
			selectionLocalTrabalho.getEntity().setFuncionario(funcionario);
			list.add(selectionLocalTrabalho.getEntity());
		}
		funcionario.setLocaisTrabalho(list);
	}
	
	private void atribuirDependentes() {
		List<Dependente> list = new ArrayList<Dependente>();
		for(SelectionEntity<Dependente> selectionDependente : listDependente){
			selectionDependente.getEntity().setFuncionario(funcionario);
			list.add(selectionDependente.getEntity());
		}
		funcionario.setDependentes(list);
	}
	
	private void atribuirValeTransporte() {
		List<ValeTransporte> list = new ArrayList<ValeTransporte>();
		for(SelectionEntity<ValeTransporte> selectionValeTransporte : listValeTransporte){
			selectionValeTransporte.getEntity().setFuncionario(funcionario);
			list.add(selectionValeTransporte.getEntity());
		}
		funcionario.setValesTransporte(list);
	}
	
	private void atribuirValeRefeicao() {
		List<ValeRefeicao> list = new ArrayList<ValeRefeicao>();
		for(SelectionEntity<ValeRefeicao> selectionValeRefeicao : listValeRefeicao){
			selectionValeRefeicao.getEntity().setFuncionario(funcionario);
			list.add(selectionValeRefeicao.getEntity());
		}
		funcionario.setValesRefeicao(list);
	}
	
	private void atribuirCestaBasica() {
		if(cestaBasica != null && cestaBasica.getTipoCestaBasica() != null){
			cestaBasica.setFuncionario(funcionario);
			funcionario.setCestaBasica(cestaBasica);
		}
	}

	public void doExcluirLote(){
		try {
			List<Funcionario> list = ((FuncionarioDataProvider) dataModel.getDataProvider()).getItensSelecionados();
			rhModulo.excluirListaFuncionario(list, getTraceInfo());
			doConsultarPesquisa();
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_EXCLUIR_LISTA);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}

	public void doConsultarPesquisa(){
		try{
			this.dataModel = new PageableExtendedTableDataModel<SelectionEntity<Funcionario>>(
					new FuncionarioDataProvider(TipoBuscaFuncionarios.PESQUISA,
							clientePesquisa, situacaoFuncionarioPesquisa,
							rhModulo, getTraceInfo()));
			doNovo();
		} catch (Exception e){
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		}
	}

	public void doAlterar() throws Exception{
		doNovo();
		funcionario = rhModulo.consultarFuncionario(funcionarioSelecionado1.getId(), getTraceInfo());
		funcionarioSelecionado1 = new Funcionario();
		clonarFuncionario();
	}
	
	public void doSelecionarFuncionario() throws Exception{
		boolean isSelecionado = funcionarioSelecionado.getKeys() != null;
		if(isSelecionado){
			doNovo();
			Long posicao = (Long) funcionarioSelecionado.getKeys().next();
			funcionario = dataModel.getDataProvider().getItemByKey(posicao).getEntity();
			clonarFuncionario();
		}
	}
	
	private void clonarFuncionario() throws Exception {
		funcionario.setContaBancaria(funcionario.getContaBancaria()==null?new ContaBancaria():funcionario.getContaBancaria());
		funcionario.setCarteiraHabilitacao(funcionario.getCarteiraHabilitacao()==null? new CarteiraHabilitacao():funcionario.getCarteiraHabilitacao());
		funcionario.setConselhoRegional(funcionario.getConselhoRegional()==null?new ConselhoRegional():funcionario.getConselhoRegional());
		funcionario.setCtpsEstrangeiro(funcionario.getCtpsEstrangeiro()==null?new CtpsEstrangeiro():funcionario.getCtpsEstrangeiro());
		funcionario.setDocumentoMilitar(funcionario.getDocumentoMilitar()==null?new DocumentoMilitar():funcionario.getDocumentoMilitar());
		funcionario.setPis(funcionario.getPis()==null?new Pis():funcionario.getPis());
		funcionario.setRneEstrangeiro(funcionario.getRneEstrangeiro()==null?new RneEstrangeiro():funcionario.getRneEstrangeiro());
		funcionario.setTituloEleitor(funcionario.getTituloEleitor()==null?new TituloEleitor():funcionario.getTituloEleitor());
		funcionario.setTelefoneCelular(funcionario.getTelefoneCelular()==null?new Telefone():funcionario.getTelefoneCelular());
		funcionario.setTelefoneCelularRecado1(funcionario.getTelefoneCelularRecado1()==null?new Telefone():funcionario.getTelefoneCelularRecado1());
		funcionario.setTelefoneCelularRecado2(funcionario.getTelefoneCelularRecado2()==null?new Telefone():funcionario.getTelefoneCelularRecado2());
		funcionario.setTelefoneRecado1(funcionario.getTelefoneRecado1()==null?new Telefone():funcionario.getTelefoneRecado1());
		funcionario.setTelefoneRecado2(funcionario.getTelefoneRecado2()==null?new Telefone():funcionario.getTelefoneRecado2());
		funcionario.setTelefoneResidencial(funcionario.getTelefoneResidencial()==null?new Telefone():funcionario.getTelefoneResidencial());
		funcionario.setCargo(funcionario.getCargo()==null?new Cargo():funcionario.getCargo());
		funcionario.setPessoa(funcionario.getPessoa()==null?new Pessoa():funcionario.getPessoa());
		funcionario.setSituacaoFuncionario(funcionario.getSituacaoFuncionario()==null?new SituacaoFuncionario():funcionario.getSituacaoFuncionario());
		funcionario.setTipoAdmissao(funcionario.getTipoAdmissao() == null?new TipoAdmissao():funcionario.getTipoAdmissao());
		clonarLocalTrabalho();
		clonarDependentes();
		clonarValeTransportes();
		clonarValeRefeicoes();
		clonarCestaBasica();
	}
	
	private void clonarDependentes() {
		List<Dependente> list = new ArrayList<Dependente>();
		if(funcionario.getDependentes() != null){
			list.addAll(funcionario.getDependentes());
		}
		listDependente = new SelectionList<Dependente>(list);
	}
	
	private void clonarValeTransportes() {
		List<ValeTransporte> list = new ArrayList<ValeTransporte>();
		if(funcionario.getValesTransporte() != null){
			list.addAll(funcionario.getValesTransporte());
		}
		listValeTransporte = new SelectionList<ValeTransporte>(list);
	}
	
	private void clonarValeRefeicoes() {
		List<ValeRefeicao> list = new ArrayList<ValeRefeicao>();
		if(funcionario.getValesRefeicao() != null){
			list.addAll(funcionario.getValesRefeicao());
		}
		listValeRefeicao = new SelectionList<ValeRefeicao>(list);
	}
	
	private void clonarCestaBasica() throws Exception {
		if(funcionario.getCestaBasica() != null){
			cestaBasica = funcionario.getCestaBasica().clone();
		}
	}

	private void clonarLocalTrabalho() {
		List<LocalTrabalho> list = new ArrayList<LocalTrabalho>();
		if(funcionario.getLocaisTrabalho() != null){
			list.addAll(funcionario.getLocaisTrabalho());
		}
		listLocalTrabalho = new SelectionList<LocalTrabalho>(list);
	}
	
	public void doSelecionarLocalTrabalho() throws Exception{
		localTrabalho = localTrabalhoSelecionado.clone();
		localTrabalhoSelecionado = new LocalTrabalho();
	}

	public void doAdicionarLocalTrabalho() throws Exception{
		if(!isLocalDeTrabalhoAdicionado()){
			if(localTrabalho.getCliente() != null){
				LocalTrabalho localTrabalhoNovo = localTrabalho.clone();
				localTrabalhoNovo.setId(null);
				identificador += 1L;
				localTrabalhoNovo.setIdentificador(identificador);
				listLocalTrabalho.add(new SelectionEntity<LocalTrabalho>(localTrabalhoNovo));
				doLimparSelecionadosLocalTrabalho();
			}
		}
	}

	private boolean isLocalDeTrabalhoAdicionado() {
		if(localTrabalho.getId() != null || localTrabalho.getIdentificador() != null){
			addMensagemErro(MensagensFacesEnum.ESTE_LOCAL_DE_TRABALHO_JA_FOI_ADICIONADO);
			return true;
		}
		return false;
	}
	
	public void doLimparSelecionadosLocalTrabalho() {
		localTrabalho = new LocalTrabalho();
		localTrabalhoSelecionado = new LocalTrabalho();
	}
	
	public void doAlterarLocalTrabalho() throws Exception{
		if(localTrabalho != null && (localTrabalho.getId() != null || localTrabalho.getIdentificador() != null)){
			for(SelectionEntity<LocalTrabalho> localTrabalhoEntity : listLocalTrabalho){
				if(localTrabalhoEntity.getEntity().getId() != null){
					if(localTrabalhoEntity.getEntity().getId().equals(localTrabalho.getId())){
						localTrabalhoEntity.setEntity(localTrabalho.clone());
						break;
					}
				}else{
					if(localTrabalhoEntity.getEntity().getIdentificador().equals(localTrabalho.getIdentificador())){
						localTrabalhoEntity.setEntity(localTrabalho.clone());
						break;
					}
				}
			}
			doLimparSelecionadosLocalTrabalho();
		}else{
			addMensagemErro(MensagensFacesEnum.SELECIONE_UM_LOCAL_DE_TRABALHO_PARA_ALTERAR);
		}
	}

	public void doRemoverLocalTrabalho(){
		SelectionList<LocalTrabalho> listTemporario = new SelectionList<LocalTrabalho>();
		for(int i = 0; i < listLocalTrabalho.size(); i++){
			SelectionEntity<LocalTrabalho> selection = listLocalTrabalho.get(i);
			if(!selection.isSelecionado()){
				listTemporario.add(selection);
			}
		}
		listLocalTrabalho = listTemporario;
		checkLocalTrabalhoAll = false;
	}
	
	public void doLimparDependente(){
		dependente = new Dependente();
	}

	public void doAdicionarDependente() throws Exception{
		if(!isDependenteAdicionado()){
			if (dependente.getNome() != null
					&& !"".equalsIgnoreCase(dependente.getNome().trim())
					&& dependente.getDataInclusao() != null) {
				Dependente dependenteNovo = dependente.clone();
				dependenteNovo.setId(null);
				identificador += 1L;
				dependenteNovo.setIdentificador(identificador);
				listDependente.add(new SelectionEntity<Dependente>(dependenteNovo));
				dependente = new Dependente();
			}else{
				addMensagemErro(MensagensFacesEnum.PELO_MENOS_O_NOME_E_A_DATA_DE_INCLUSAO_DEVEM_SER_INFORMADOS);
			}
		}
	}

	private boolean isDependenteAdicionado() throws PotiErpMensagensException {
		if(dependente.getId() != null || dependente.getIdentificador() != null){
			addMensagemErro(MensagensFacesEnum.ESTE_DEPENDENTE_JA_FOI_ADICIONADO);
			return true;
		}
		return false;
	}

	public void doAlterarDependente() throws Exception{
		if(dependente != null && (dependente.getId() != null || dependente.getIdentificador() != null)){
			for(SelectionEntity<Dependente> dependenteEntity : listDependente){
				if(dependenteEntity.getEntity().getId() != null){
					if(dependenteEntity.getEntity().getId().equals(dependente.getId())){
						dependenteEntity.setEntity(dependente.clone());
						break;
					}
				}else{
					if(dependenteEntity.getEntity().getIdentificador().equals(dependente.getIdentificador())){
						dependenteEntity.setEntity(dependente.clone());
						break;
					}
				}
			}
			dependente = new Dependente();
		}else{
			addMensagemErro(MensagensFacesEnum.SELECIONE_UM_DEPENDENTE_PARA_ALTERAR);
		}
	}

	public void doRemoverDependente(){
		SelectionList<Dependente> listTemporario = new SelectionList<Dependente>();
		for(int i = 0; i < listDependente.size(); i++){
			SelectionEntity<Dependente> selection = listDependente.get(i);
			if(!selection.isSelecionado()){
				listTemporario.add(selection);
			}
		}
		listDependente = listTemporario;
		checkDependenteAll = false;
	}
	
	public void doSelecionarDependente() throws Exception{
		dependente = dependenteSelecionado.clone();
	}
	
	public void doSelecionarValeTransporte() throws Exception{
		valeTransporte = valeTransporteSelecionado.clone();
	}

	public void doAdicionarValeTransporte() throws Exception{
		if(!isValeTransporteAdicionado()){
			if (isValeTransporteValido()) {

				ValeTransporte valeTransporteNovo = valeTransporte.clone();
				valeTransporteNovo.setId(null);
				identificador += 1L;
				valeTransporteNovo.setIdentificador(identificador);
				listValeTransporte.add(new SelectionEntity<ValeTransporte>(valeTransporteNovo));
				doLimparValeTransporte();
			}else{
				addMensagemErro(MensagensFacesEnum.INFORME_TODOS_OS_CAMPOS_PARA_ADICIONAR);
			}
		}
	}

	private boolean isValeTransporteAdicionado() throws PotiErpMensagensException {
		if(valeTransporte.getId() != null || valeTransporte.getIdentificador() != null){
			addMensagemErro(MensagensFacesEnum.ESTE_VALETRANSPORTE_JA_FOI_ADICIONADO);
			return true;
		}
		return false;
	}
	
	public void doAlterarValeTransporte() throws Exception{
		if(valeTransporte != null && (valeTransporte.getId() != null || valeTransporte.getIdentificador() != null)){
			if (isValeTransporteValido()) {
				for(SelectionEntity<ValeTransporte> valeTransporteEntity : listValeTransporte){
					if(valeTransporteEntity.getEntity().getId() != null){
						if(valeTransporteEntity.getEntity().getId().equals(valeTransporte.getId())){
							valeTransporteEntity.setEntity(valeTransporte.clone());
							break;
						}
					}else{
						if(valeTransporteEntity.getEntity().getIdentificador().equals(valeTransporte.getIdentificador())){
							valeTransporteEntity.setEntity(valeTransporte.clone());
							break;
						}
					}
				}
				valeTransporte = new ValeTransporte();

			}else{
				addMensagemErro(MensagensFacesEnum.INFORME_TODOS_OS_CAMPOS_PARA_ALTERAR);
			}
		}else{
			addMensagemErro(MensagensFacesEnum.SELECIONE_UM_VALETRANSPORTE_PARA_ALTERAR);
		}
	}

	private boolean isValeTransporteValido() {
		return valeTransporte.getTipoValeTransporte() != null
				&& valeTransporte.getTipoValeTransporte().getNome() != null
				&& !"".equalsIgnoreCase(valeTransporte.getTipoValeTransporte().getNome().trim())
				&& valeTransporte.getRumoTransporte() != null
				&& valeTransporte.getDataAtribuicao() != null
				&& valeTransporte.getCliente() != null;
	}
	
	public void doLimparValeTransporte(){
		valeTransporte = new ValeTransporte();
	}
	
	public void doRemoverValeTransporte(){
		SelectionList<ValeTransporte> listTemporario = new SelectionList<ValeTransporte>();
		for(int i = 0; i < listValeTransporte.size(); i++){
			SelectionEntity<ValeTransporte> selection = listValeTransporte.get(i);
			if(!selection.isSelecionado()){
				listTemporario.add(selection);
			}
		}
		listValeTransporte = listTemporario;
		checkValeTransporteAll = false;
	}
	
	public void doSelecionarValeRefeicao() throws Exception{
		valeRefeicao = valeRefeicaoSelecionado.clone();
	}

	public void doAdicionarValeRefeicao() throws Exception{
		if(!isValeRefeicaoAdicionado()){
			if (valeRefeicao.getTipoValeRefeicao() != null
					&& valeRefeicao.getTipoValeRefeicao().getNome() != null
					&& !"".equalsIgnoreCase(valeRefeicao.getTipoValeRefeicao().getNome().trim())
					&& valeRefeicao.getDataAtribuicao() != null) {
				ValeRefeicao valeRefeicaoNovo = valeRefeicao.clone();
				valeRefeicaoNovo.setId(null);
				identificador += 1L;
				valeRefeicaoNovo.setIdentificador(identificador);
				listValeRefeicao.add(new SelectionEntity<ValeRefeicao>(valeRefeicaoNovo));
				doLimparValeRefeicao();
			}
		}
	}
	
	private boolean isValeRefeicaoAdicionado() throws PotiErpMensagensException {
		if(valeRefeicao.getId() != null || valeRefeicao.getIdentificador() != null){
			addMensagemErro(MensagensFacesEnum.ESTE_VALEREFEICAO_JA_FOI_ADICIONADO);
			return true;
		}
		return false;
	}

	public void doLimparValeRefeicao() {
		valeRefeicao = new ValeRefeicao();
	}
	
	public void doLimparCestaBasica() {
		cestaBasica = new CestaBasica();
	}
	
	public void doAlterarValeRefeicao() throws Exception{
		if(valeRefeicao != null && (valeRefeicao.getId() != null || valeRefeicao.getIdentificador() != null)){
			for(SelectionEntity<ValeRefeicao> valeRefeicaoEntity : listValeRefeicao){
				if(valeRefeicaoEntity.getEntity().getId() != null){
					if(valeRefeicaoEntity.getEntity().getId().equals(valeRefeicao.getId())){
						valeRefeicaoEntity.setEntity(valeRefeicao.clone());
						break;
					}
				}else{
					if(valeRefeicaoEntity.getEntity().getIdentificador().equals(valeRefeicao.getIdentificador())){
						valeRefeicaoEntity.setEntity(valeRefeicao.clone());
						break;
					}
				}
			}
			valeRefeicao = new ValeRefeicao();
		}else{
			addMensagemErro(MensagensFacesEnum.SELECIONE_UM_VALEREFEICAO_PARA_ALTERAR);
		}
	}
	
	public void doRemoverValeRefeicao(){
		SelectionList<ValeRefeicao> listTemporario = new SelectionList<ValeRefeicao>();
		for(int i = 0; i < listValeRefeicao.size(); i++){
			SelectionEntity<ValeRefeicao> selection = listValeRefeicao.get(i);
			if(!selection.isSelecionado()){
				listTemporario.add(selection);
			}
		}
		listValeRefeicao = listTemporario;
		checkValeRefeicaoAll = false;
	}
	
	public void doPreencheDataBaixaDependente() {
		if(dependente != null && dependente.getDataNascimento() != null){
			dependente.setDataBaixa(DateUtils.addYears(dependente.getDataNascimento(), 14));
		}
	}
	
	public void verificarObrigatorios(){
		verificarCamposObrigatorios();
	}

	private boolean verificarCamposObrigatorios() {
		//Passa por todas as verificações para atualizar o ícone indicativo.
		boolean isDadosPessoais = verificarDadosPessoais();
		boolean isDocumentos = verificarDocumentos();
		boolean isEndereco = verificarEndereco();
		boolean isDadosFuncionais = verificarDadosFuncionais();
		boolean isLocalDeTrabalho = verificarLocalDeTrabalho();
		verificarDocumentosEstrangeiro();
		verificarDadosBancarios();
		verificarDependentes();
		
		//Verifica somente campos obrigatórios.
		return isDadosPessoais && isDocumentos && isEndereco && isDadosFuncionais && isLocalDeTrabalho;
	}
	
	private boolean verificarDadosPessoais() {
		if(isDadosPessoaisPreenchidos()){
			isDadosPessoaisCadastrados = true;
		}else{
			isDadosPessoaisCadastrados = false;
		}
		return isDadosPessoaisCadastrados;
	}
	
	private boolean isDadosPessoaisPreenchidos() {
		return funcionario.getEmpresa() != null
		&& funcionario.getCodigoRegistro() != null
		&& funcionario.getPessoa() != null && !"".equals(funcionario.getPessoa().getNome())
		&& funcionario.getPessoa().getDataNascimento() != null
		&& funcionario.getPessoa().getSexo() != null
		&& funcionario.getDeficienteFisico() != null;
	}
	
	public boolean verificarEndereco() {
		if(isEnderecoPreenchido()){
			isEnderecoCadastrado = true;
		}else{
			isEnderecoCadastrado = false;
		}		
		return isEnderecoCadastrado;
	}
	
	public boolean verificarDocumentos() {
		if(isDocumentosPreenchidos()){
			isDocumentosCadastrados = true;
		}else{
			isDocumentosCadastrados = false;
		}
		return isDocumentosCadastrados;
	}
	
	public boolean verificarContatos() {
		if(isContatosPreenchidos()){
			isContatosCadastrados = true;
		}else{
			isContatosCadastrados = false;
		}
		return isContatosCadastrados;
	}
	
	public boolean verificarDadosFuncionais() {
		if(isDadosFuncionaisPreenchidos()){
			isDadosFuncionaisCadastrados = true;
		}else{
			isDadosFuncionaisCadastrados = false;
		}		
		return isDadosFuncionaisCadastrados;
	}
	
	public boolean verificarDocumentosEstrangeiro() {
		if(isDocumentosEstrangeiroPreenchidos()){
			isDocumentosEstrangeiroCadastrados = true;
		}else{
			isDocumentosEstrangeiroCadastrados = false;
		}		
		return isDocumentosEstrangeiroCadastrados;
	}
	
	public boolean verificarDependentes() {
		if(isDependentesPreenchidos()){
			isDependentesCadastrados = true;
		}else{
			isDependentesCadastrados = false;
		}
		return isDependentesCadastrados;
	}
	
	public boolean verificarDadosBancarios() {
		if(isDadosBancariosPreenchidos()){
			isDadosBancariosCadastrados = true;
		}else{
			isDadosBancariosCadastrados = false;
		}
		return isDadosBancariosCadastrados;
	}
	
	public boolean verificarLocalDeTrabalho() {
		if(isLocalDeTrabalhoPreenchido()){
			isLocalDeTrabalhoCadastrado = true;
		}else{
			isLocalDeTrabalhoCadastrado = false;
		}
		return isLocalDeTrabalhoCadastrado;
	}
	
	private boolean isEnderecoPreenchido() {
		return funcionario.getPessoa().getEndereco() != null
				&& funcionario.getPessoa().getEndereco().getCep() != null && !"".equals(funcionario.getPessoa().getEndereco().getCep())
				&& funcionario.getPessoa().getEndereco().getEndereco() != null && !"".equals(funcionario.getPessoa().getEndereco().getEndereco())
				&& funcionario.getPessoa().getEndereco().getCidade() != null && !"".equals(funcionario.getPessoa().getEndereco().getCidade())
				&& funcionario.getPessoa().getEndereco().getEstado() != null && !"".equals(funcionario.getPessoa().getEndereco().getEstado());
	}
	
	private boolean isDocumentosPreenchidos() {
		return isCtpsPreenchido()
			&& isCpfPreenchido() 
			&& isRgPreenchido()
			&& funcionario.getSituacaoFgts().getOptante() != null;
	}
	
	private boolean isContatosPreenchidos() {
		return isCtpsPreenchido()
			&& isCpfPreenchido() 
			&& isRgPreenchido()
			&& funcionario.getSituacaoFgts().getOptante() != null;
	}

	private boolean isDocumentosEstrangeiroPreenchidos() {
		return isCtpsEstrangeiroPreenchido() && isRneEstrangeiroPreenchido();
	}
	
	private boolean isDependentesPreenchidos() {
		return true;
	}
	
	private boolean isDadosBancariosPreenchidos() {
		return funcionario.getContaBancaria() != null
				&& funcionario.getContaBancaria().getBanco() != null && !"".equals(funcionario.getContaBancaria().getBanco())
				&& funcionario.getContaBancaria().getAgencia() != null && !"".equals(funcionario.getContaBancaria().getAgencia())
				&& funcionario.getContaBancaria().getConta() != null && !"".equals(funcionario.getContaBancaria().getConta())
				&& funcionario.getContaBancaria().getTipoContaBancaria() != null;
	}
	
	private boolean isLocalDeTrabalhoPreenchido(){
		return true;
	}
	
	private boolean isDadosFuncionaisPreenchidos() {
		return funcionario.getSalario() != null
			&& funcionario.getCargo() != null && !"".equals(funcionario.getCargo().getCargo())
			&& funcionario.getDataAdmissao() != null
			&& funcionario.getTipoAdmissao() != null;
	}

	private boolean isCpfPreenchido() {
		return funcionario.getCpf() != null && !"".equals(funcionario.getCpf());
	}

	private boolean isRgPreenchido() {
		if(funcionario.getRg() != null
			&& funcionario.getRg().getRg() != null && !"".equals(funcionario.getRg().getRg())){
			funcionario.getRg().setRg(funcionario.getRg().getRg().toUpperCase());
			return true;
		}
		return false;
	}

	private boolean isCtpsPreenchido() {
		return funcionario.getCtps() != null
				&& funcionario.getCtps().getCtps() != null && !"".equals(funcionario.getCtps().getCtps())
				&& funcionario.getCtps().getSerie() != null && !"".equals(funcionario.getCtps().getSerie());
	}
	
	private boolean isCtpsEstrangeiroPreenchido() {
		return funcionario.getCtpsEstrangeiro() != null
				&& funcionario.getCtpsEstrangeiro().getCtps() != null && !"".equals(funcionario.getCtpsEstrangeiro().getCtps())
				&& funcionario.getCtpsEstrangeiro().getDataEmissao() != null
				&& funcionario.getCtpsEstrangeiro().getDataValidade() != null;
	}
	
	private boolean isRneEstrangeiroPreenchido() {
		return funcionario.getRneEstrangeiro() != null
				&& funcionario.getRneEstrangeiro().getRne() != null && !"".equals(funcionario.getRneEstrangeiro().getRne())
				&& funcionario.getRneEstrangeiro().getDataValidade() != null
				&& funcionario.getRneEstrangeiro().getTipoVisto() != null && !"".equals(funcionario.getRneEstrangeiro().getTipoVisto());
	}
	
	public boolean filtraCodigoRegistro(final Object current) throws Exception {
		@SuppressWarnings("unchecked")
		Funcionario funcionarioAtual = ((SelectionEntity<Funcionario>) current).getEntity();
		Long codigoAtual = funcionarioAtual.getCodigoRegistro();
		if (filtroCodigoRegistro == null || filtroCodigoRegistro.length()==0) {
			return true;
	    }
		if(codigoAtual == null)
			return false;
		
		if(codigoAtual.toString().trim().toLowerCase().startsWith(filtroCodigoRegistro.toString())){
			return true;
		}else{
			return false;
		} 
	}
	
	public boolean filtraNome(final Object current) throws Exception {
		@SuppressWarnings("unchecked")
		Funcionario funcionarioAtual = ((SelectionEntity<Funcionario>) current).getEntity();
		String nomeAtual = funcionarioAtual.getPessoa().getNome();
		if (filtroNome == null || filtroNome.length()==0) {
			return true;
	    }
		if(nomeAtual == null)
			return false;
		
		if(nomeAtual.trim().toLowerCase().startsWith(filtroNome.toString().toLowerCase())){
			return true;
		}else{
			return false;
		} 
	}
	
	public boolean filtraCtps(final Object current) throws Exception {
		@SuppressWarnings("unchecked")
		Funcionario funcionarioAtual = ((SelectionEntity<Funcionario>) current).getEntity();
		Long cptsAtual = funcionarioAtual.getCtps().getCtps();
		if (filtroCtps == null || filtroCtps.length()==0) {
			return true;
	    }
		if(cptsAtual == null)
			return false;
		
		if(cptsAtual.toString().trim().toLowerCase().startsWith(filtroCtps.toString().toLowerCase())){
			return true;
		}else{
			return false;
		} 
	}
	
	public boolean filtraSerie(final Object current) throws Exception {
		@SuppressWarnings("unchecked")
		Funcionario funcionarioAtual = ((SelectionEntity<Funcionario>) current).getEntity();
		Long serieAtual = funcionarioAtual.getCtps().getSerie();
		if (filtroSerie == null || filtroSerie.length()==0) {
			return true;
	    }
		if(serieAtual == null)
			return false;
		
		if(serieAtual.toString().trim().toLowerCase().startsWith(filtroSerie.toString().toLowerCase())){
			return true;
		}else{
			return false;
		} 
	}
	
	public boolean filtraRg(final Object current) throws Exception {
		@SuppressWarnings("unchecked")
		Funcionario funcionarioAtual = ((SelectionEntity<Funcionario>) current).getEntity();
		String rgAtual = funcionarioAtual.getRg().getRg();
		if (filtroRg == null || filtroRg.length()==0) {
			return true;
	    }
		if(rgAtual == null)
			return false;
		
		if(rgAtual.toString().trim().toLowerCase().startsWith(filtroRg.toString().toLowerCase())){
			return true;
		}else{
			return false;
		} 
	}
	
	public boolean filtraCpf(final Object current) throws Exception {
		@SuppressWarnings("unchecked")
		Funcionario funcionarioAtual = ((SelectionEntity<Funcionario>) current).getEntity();
		String cpfAtual = funcionarioAtual.getCpf();
		if (filtroCpf== null || filtroCpf.length()==0) {
			return true;
	    }
		if(cpfAtual == null)
			return false;
		
		if(cpfAtual.toString().trim().toLowerCase().startsWith(filtroCpf.toString().toLowerCase())){
			return true;
		}else{
			return false;
		} 
	}
	
	public boolean filtraCargo(final Object current) throws Exception {
		@SuppressWarnings("unchecked")
		Funcionario funcionarioAtual = ((SelectionEntity<Funcionario>) current).getEntity();
		String cargoAtual = funcionarioAtual.getCargo().getCargo();
		if (filtroCargo== null || filtroCargo.length()==0) {
			return true;
	    }
		if(cargoAtual == null)
			return false;
		
		if(cargoAtual.toString().trim().toLowerCase().startsWith(filtroCargo.toString().toLowerCase())){
			return true;
		}else{
			return false;
		} 
	}
	
	public boolean filtraDataAdmissao(final Object current) throws Exception {
		@SuppressWarnings("unchecked")
		Funcionario funcionarioAtual = ((SelectionEntity<Funcionario>) current).getEntity();
		Date dataAdmissaoAtual = funcionarioAtual.getDataAdmissao();
		if (filtroDataAdmissao == null || filtroDataAdmissao.length()==0) {
			return true;
	    }
		if(dataAdmissaoAtual == null)
			return false;
		if(DateUtil.dateToPtBrString(dataAdmissaoAtual).startsWith(filtroDataAdmissao.toString())){
			return true;
		}else{
			return false;
		} 
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(final Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public SelectionList<Funcionario> getListFuncionario() {
		return listFuncionario;
	}

	public void setListFuncionario(final SelectionList<Funcionario> listFuncionario) {
		this.listFuncionario = listFuncionario;
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
		if(itensEmpresa.isEmpty()){
			return rhModulo.consultarTodasEmpresas();
		}
		return null;
	}

	public void setItensEmpresa(final List<SelectItem> itensEmpresa) {
		this.itensEmpresa = itensEmpresa;
	}
	
	public List<SelectItem> getItensClientes() throws PotiErpException {
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
		return itensClientes;
	}

	private List<Cliente> getClientes() throws PotiErpException {
		if(itensClientes.isEmpty()){
			return rhModulo.consultarClientesAtivos();
		}
		return null;
	}

	public void setItensClientes(final List<SelectItem> itensClientes) {
		this.itensClientes = itensClientes;
	}
	
	public List<SelectItem> getItensClientesVT() throws PotiErpException {
		List<Cliente> listClientes = getClientesVT();
		if(listClientes != null){
			itensClientesVT.clear();
			addMock(itensClientesVT, MockEnum.SELECIONE);
			for(Cliente cliente : listClientes){
				String labelCliente = cliente.getCodigo() + " - " + cliente.getNomeFantasia();
				SelectItem item = new SelectItem(cliente, labelCliente);
				itensClientesVT.add(item);
			}
		}
		return itensClientesVT;
	}

	private List<Cliente> getClientesVT() throws PotiErpException {
		if(itensClientesVT.isEmpty()){
			return rhModulo.consultarClientesAtivos();
		}
		return null;
	}

	public void setItensClientesVT(final List<SelectItem> itensClientes) {
		this.itensClientesVT = itensClientes;
	}
	
	public List<SelectItem> getItensTipoTransporte() throws PotiErpException {
		List<TipoValeTransporte> listTipoValeTransporte = getTiposValeTransporte();
		if(listTipoValeTransporte != null){
			itensTipoTransporte.clear();
			addMock(itensTipoTransporte, MockEnum.SELECIONE);
			for(TipoValeTransporte tipoValeTransporte : listTipoValeTransporte){
				SelectItem item = new SelectItem(tipoValeTransporte,tipoValeTransporte.getCodigo() + " - " 
																	+ tipoValeTransporte.getNome() + " - " 
																	+ tipoValeTransporte.getValor());
				itensTipoTransporte.add(item);
			}
		}
		return itensTipoTransporte;
	}

	private List<TipoValeTransporte> getTiposValeTransporte() throws PotiErpException {
		if(itensTipoTransporte.isEmpty()){ 
			return rhModulo.consultarTodosTiposValeTransporte();
		}
		return null;
	}

	public void setItensTipoTransporte(final List<SelectItem> itensTipoTransporte) {
		this.itensTipoTransporte = itensTipoTransporte;
	}
	
	public List<SelectItem> getItensTipoRefeicao() throws PotiErpException {
		List<TipoValeRefeicao> listTipoValeRefeicao = getTiposValeRefeicao();
		if(listTipoValeRefeicao != null){
			itensTipoRefeicao.clear();
			addMock(itensTipoRefeicao, MockEnum.SELECIONE);
			for(TipoValeRefeicao tipoValeRefeicao : listTipoValeRefeicao){
				SelectItem item = new SelectItem(tipoValeRefeicao,tipoValeRefeicao.getNome());
				itensTipoRefeicao.add(item);
			}
		}
		return itensTipoRefeicao;
	}

	private List<TipoValeRefeicao> getTiposValeRefeicao() throws PotiErpException {
		if(itensTipoRefeicao.isEmpty()){ 
			return rhModulo.consultarTodosTiposValeRefeicao();
		}
		return null;
	}

	public void setItensTipoRefeicao(final List<SelectItem> itensTipoRefeicao) {
		this.itensTipoRefeicao = itensTipoRefeicao;
	}
	
	public List<SelectItem> getItensTipoCestaBasica() throws PotiErpException {
		List<TipoCestaBasica> listTipoCestaBasica = getTiposCestaBasica();
		if(listTipoCestaBasica != null){
			itensTipoCestaBasica.clear();
			addMock(itensTipoCestaBasica, MockEnum.SELECIONE);
			for(TipoCestaBasica tipoCestaBasica : listTipoCestaBasica){
				SelectItem item = new SelectItem(tipoCestaBasica,tipoCestaBasica.getNome());
				itensTipoCestaBasica.add(item);
			}
		}
		return itensTipoCestaBasica;
	}

	private List<TipoCestaBasica> getTiposCestaBasica() throws PotiErpException {
		if(itensTipoCestaBasica.isEmpty()){ 
			return rhModulo.consultarTodosTiposCestaBasica();
		}
		return null;
	}

	public void setItensTipoCestaBasica(final List<SelectItem> itensTipoCestaBasica) {
		this.itensTipoCestaBasica = itensTipoCestaBasica;
	}

	public List<SelectItem> getItensRumoTransporte() {
		List<RumoTransporteEnum> listRumoTransporteEnum = getListRumoTransporteEnum();
		if(listRumoTransporteEnum != null){
			itensRumoTransporte.clear();
			addMockSimple(itensRumoTransporte, MockEnum.SELECIONE);
			for(RumoTransporteEnum rumoTransporte : listRumoTransporteEnum){
				SelectItem item = new SelectItem(rumoTransporte, rumoTransporte.getRumo());
				itensRumoTransporte.add(item);
			}
		}
		return itensRumoTransporte;
	}
	
	public List<RumoTransporteEnum> getListRumoTransporteEnum() {
		if(itensRumoTransporte.isEmpty()){ 
			return new ArrayList<RumoTransporteEnum>(Arrays.asList(RumoTransporteEnum.values()));
		}
		return null;
	}

	public void setItensRumoTransporte(final List<SelectItem> itensRumoTransporte) {
		this.itensRumoTransporte = itensRumoTransporte;
	}

	public List<SelectItem> getItensCargo() throws PotiErpException {
		List<Cargo> listCargos = getCargos();
		if(listCargos != null){
			itensCargo.clear();
			addMock(itensCargo, MockEnum.SELECIONE);
			for(Cargo cargo : listCargos){
				SelectItem item = new SelectItem(cargo, cargo.getCargo());
				itensCargo.add(item);
			}
		}
		return itensCargo;
	}
	
	private List<Cargo> getCargos() throws PotiErpException{
		if(itensCargo.isEmpty()){
			return rhModulo.consultarTodosCargos();
		}
		return null;
	}

	public void setItensCargo(final List<SelectItem> itensCargo) {
		this.itensCargo = itensCargo;
	}
	
	public List<SelectItem> getItensTipoAdmissao() throws PotiErpException {
		List<TipoAdmissao> listTipoAdmissao = getListTipoAdmissao();
		if(listTipoAdmissao != null){
			itensTipoAdmissao.clear();
			addMock(itensTipoAdmissao, MockEnum.SELECIONE);
			for(TipoAdmissao tipoAdmissao : listTipoAdmissao){
				SelectItem item = new SelectItem(tipoAdmissao, tipoAdmissao.getNome());
				itensTipoAdmissao.add(item);
			}
		}
		return itensTipoAdmissao;
	}
	
	public List<TipoAdmissao> getListTipoAdmissao() throws PotiErpException {
		if(itensTipoAdmissao.isEmpty()){ 
			return rhModulo.consultarTodosTiposAdmissoes();
		}
		return null;
	}

	public void setItensTipoAdmissao(final List<SelectItem> itensTipoAdmissao) {
		this.itensTipoAdmissao = itensTipoAdmissao;
	}
	
	public List<SelectItem> getItensVinculoEmpregaticio() throws PotiErpException {
		List<VinculoEmpregaticio> listVinculoEmpregaticio = getListVinculoEmpregaticio();
		if(listVinculoEmpregaticio != null){
			itensVinculoEmpregaticio.clear();
			addMock(itensVinculoEmpregaticio, MockEnum.SELECIONE);
			for(VinculoEmpregaticio vinculoEmpregaticio : listVinculoEmpregaticio){
				SelectItem item = new SelectItem(vinculoEmpregaticio, vinculoEmpregaticio.getNome());
				itensVinculoEmpregaticio.add(item);
			}
		}
		return itensVinculoEmpregaticio;
	}
	
	public List<VinculoEmpregaticio> getListVinculoEmpregaticio() throws PotiErpException {
		if(itensVinculoEmpregaticio.isEmpty()){ 
			return rhModulo.consultarTodosVinculosEmpregaticio();
		}
		return null;
	}

	public void setItensVinculoEmpregaticio(final List<SelectItem> itensVinculoEmpregaticio) {
		this.itensVinculoEmpregaticio = itensVinculoEmpregaticio;
	}
	
	public List<SelectItem> getItensSituacaoFuncionario() throws PotiErpException {
		List<SituacaoFuncionario> listSituacaoFuncionario = getListSituacaoFuncionario();
		if(listSituacaoFuncionario != null){
			itensSituacaoFuncionario.clear();
			addMock(itensSituacaoFuncionario, MockEnum.SELECIONE);
			for(SituacaoFuncionario situacaoFuncionario : listSituacaoFuncionario){
				SelectItem item = new SelectItem(situacaoFuncionario, situacaoFuncionario.getNome());
				itensSituacaoFuncionario.add(item);
			}
		}
		return itensSituacaoFuncionario;
	}
	
	public List<SituacaoFuncionario> getListSituacaoFuncionario() throws PotiErpException {
		if(itensSituacaoFuncionario.isEmpty()){ 
			return rhModulo.consultarTodasSituacoesFuncionario();
		}
		return null;
	}

	public void setItensSituacaoFuncionario(
			final List<SelectItem> itensSituacaoFuncionario) {
		this.itensSituacaoFuncionario = itensSituacaoFuncionario;
	}
	
	public List<SelectItem> getItensGrauParentesco() throws PotiErpException {
		List<GrauParentesco> listGrauParentesco = getListGrauParentesco();
		if(listGrauParentesco != null){
			itensGrauParentesco.clear();
			addMock(itensGrauParentesco, MockEnum.SELECIONE);
			for(GrauParentesco grauParentesco : listGrauParentesco){
				SelectItem item = new SelectItem(grauParentesco, grauParentesco.getDescricao());
				itensGrauParentesco.add(item);
			}
		}
		return itensGrauParentesco;
	}

	private List<GrauParentesco> getListGrauParentesco() throws PotiErpException {
		if(itensGrauParentesco.isEmpty()){ 
			return rhModulo.consultarTodosGrauParentescos();
		}
		return null;
	}

	public void setItensGrauParentesco(final List<SelectItem> itensGrauParentesco) {
		this.itensGrauParentesco = itensGrauParentesco;
	}

	public List<SelectItem> getItensTipoContrato() {
		List<TipoContratoEnum> listTipoContrato = getListTipoContrato();
		if(listTipoContrato != null){
			itensTipoContrato.clear();
			addMockSimple(itensTipoContrato, MockEnum.SELECIONE);
			for(TipoContratoEnum tipoContrato : listTipoContrato){
				SelectItem item = new SelectItem(tipoContrato, tipoContrato.getTipoContrato());
				itensTipoContrato.add(item);
			}
		}
		return itensTipoContrato;
	}
	
	public List<TipoContratoEnum> getListTipoContrato() {
		if(itensTipoContrato.isEmpty()){ 
			return new ArrayList<TipoContratoEnum>(Arrays.asList(TipoContratoEnum.values()));
		}
		return null;
	}

	public void setItensTipoContrato(final List<SelectItem> itensTipoContrato) {
		this.itensTipoContrato = itensTipoContrato;
	}
	
	public List<SelectItem> getItensCertificadoReservista() {
		List<CertificadoReservistaEnum> listCertificadoReservista = getListCertificadoReservista();
		if(listCertificadoReservista != null){
			itensCertificadoReservista.clear();
			addMockSimple(itensCertificadoReservista, MockEnum.SELECIONE);
			for(CertificadoReservistaEnum certificadoReservista : listCertificadoReservista){
				SelectItem item = new SelectItem(certificadoReservista, certificadoReservista.getNome());
				itensCertificadoReservista.add(item);
			}
		}
		return itensCertificadoReservista;
	}
	
	public List<CertificadoReservistaEnum> getListCertificadoReservista() {
		if(itensCertificadoReservista.isEmpty()){ 
			return new ArrayList<CertificadoReservistaEnum>(Arrays.asList(CertificadoReservistaEnum.values()));
		}
		return null;
	}

	public void setItensCertificadoReservista(final List<SelectItem> itensCertificadoReservista) {
		this.itensCertificadoReservista = itensCertificadoReservista;
	}

	public List<SelectItem> getItensEstadosCivis() {
		List<EstadoCivilEnum> listEstadoCivil = getListEstadoCivil();
		if(listEstadoCivil != null){
			itensEstadosCivis.clear();
			addMockSimple(itensEstadosCivis, MockEnum.SELECIONE);
			for(EstadoCivilEnum estadoCivil : listEstadoCivil){
				SelectItem item = new SelectItem(estadoCivil, estadoCivil.getEstadoCivil());
				itensEstadosCivis.add(item);
			}
		}
		return itensEstadosCivis;
	}
	
	public List<SelectItem> getItensRaca() {
		List<RacaEnum> listRaca = getListRaca();
		if(listRaca != null){
			itensRaca.clear();
			addMockSimple(itensRaca, MockEnum.SELECIONE);
			for(RacaEnum raca : listRaca){
				SelectItem item = new SelectItem(raca, raca.getDescricao());
				itensRaca.add(item);
			}
		}
		return itensRaca;
	}
	
	public List<EstadoCivilEnum> getListEstadoCivil() {
		if(itensEstadosCivis.isEmpty()){ 
			return new ArrayList<EstadoCivilEnum>(Arrays.asList(EstadoCivilEnum.values()));
		}
		return null;
	}
	
	public List<RacaEnum> getListRaca() {
		if(itensRaca.isEmpty()){ 
			return new ArrayList<RacaEnum>(Arrays.asList(RacaEnum.values()));
		}
		return null;
	}
	
	public void setItensRaca(final List<SelectItem> itensRaca) {
		this.itensRaca = itensRaca;
	}

	public void setItensEstadosCivis(final List<SelectItem> itensEstadosCivis) {
		this.itensEstadosCivis = itensEstadosCivis;
	}
	
	public List<SelectItem> getItensEscolaridade() {
		List<EscolaridadeEnum> listEscolaridade = getListEscolaridade();
		if(listEscolaridade != null){
			itensEscolaridade.clear();
			addMockSimple(itensEscolaridade, MockEnum.SELECIONE);
			for(EscolaridadeEnum escolaridade : listEscolaridade){
				SelectItem item = new SelectItem(escolaridade, escolaridade.getDescricao());
				itensEscolaridade.add(item);
			}
		}
		return itensEscolaridade;
	}
	
	public List<EscolaridadeEnum> getListEscolaridade() {
		if(itensEscolaridade.isEmpty()){ 
			return new ArrayList<EscolaridadeEnum>(Arrays.asList(EscolaridadeEnum.values()));
		}
		return null;
	}

	public void setItensEscolaridade(final List<SelectItem> itensEscolaridade) {
		this.itensEscolaridade = itensEscolaridade;
	}
	
	public List<SelectItem> getItensDiaSemana() {
		List<DiaSemanaEnum> listDiaSemanaEnum = getListDiaSemanaEnum();
		if(listDiaSemanaEnum != null){
			itensDiaSemana.clear();
			addMockSimple(itensDiaSemana, MockEnum.SELECIONE);
			for(DiaSemanaEnum diaSemanaEnum : listDiaSemanaEnum){
				SelectItem item = new SelectItem(diaSemanaEnum, diaSemanaEnum.getDiaSemana());
				itensDiaSemana.add(item);
			}
		}
		return itensDiaSemana;
	}
	
	public List<DiaSemanaEnum> getListDiaSemanaEnum() {
		if(itensDiaSemana.isEmpty()){ 
			return new ArrayList<DiaSemanaEnum>(Arrays.asList(DiaSemanaEnum.values()));
		}
		return null;
	}

	public void setItensDiaSemana(final List<SelectItem> itensDiaSemana) {
		this.itensDiaSemana = itensDiaSemana;
	}
	
	public List<SelectItem> getItensTipoContaBancaria() {
		List<TipoContaBancariaEnum> listTipoContaBancaria = getListTipoContaBancaria();
		if(listTipoContaBancaria != null){
			itensTipoContaBancaria.clear();
			addMockSimple(itensTipoContaBancaria, MockEnum.SELECIONE);
			for(TipoContaBancariaEnum tipoContaBancaria : listTipoContaBancaria){
				SelectItem item = new SelectItem(tipoContaBancaria, tipoContaBancaria.getTipoConta());
				itensTipoContaBancaria.add(item);
			}
		}
		return itensTipoContaBancaria;
	}
	
	public List<TipoContaBancariaEnum> getListTipoContaBancaria() {
		if(itensTipoContaBancaria.isEmpty()){ 
			return new ArrayList<TipoContaBancariaEnum>(Arrays.asList(TipoContaBancariaEnum.values()));
		}
		return null;
	}

	public void setItensTipoContaBancaria(final List<SelectItem> itensTipoContaBancaria) {
		this.itensTipoContaBancaria = itensTipoContaBancaria;
	}
	
	public List<SelectItem> getItensSetor() throws PotiErpException {
		List<Setor> listSetor = getListSetor();
		if(listSetor != null){
			itensSetor.clear();
			addMock(itensSetor, MockEnum.SELECIONE);
			for(Setor setor : listSetor){
				SelectItem item = new SelectItem(setor, setor.getNome());
				itensSetor.add(item);
			}
		}
		return itensSetor;
	}
	
	public List<Setor> getListSetor() throws PotiErpException {
		if(itensSetor.isEmpty()){ 
			return rhModulo.consultarTodosSetores();
		}
		return null;
	}

	public void setItensSetor(final List<SelectItem> itensSetor) {
		this.itensSetor = itensSetor;
	}

	public List<SelectItem> getItensJornadaTrabalho() throws PotiErpException {
		List<JornadaTrabalho> listJornadas = getJornadas();
		if(listJornadas != null){
			itensJornadaTrabalho.clear();
			addMock(itensJornadaTrabalho, MockEnum.SELECIONE);
			for(JornadaTrabalho jornadaTrabalho : listJornadas){
				SelectItem item = new SelectItem(jornadaTrabalho, jornadaTrabalho.getNome());
				itensJornadaTrabalho.add(item);
			}
		}
		return itensJornadaTrabalho;
	}
	
	private List<JornadaTrabalho> getJornadas() throws PotiErpException{
		if(itensJornadaTrabalho.isEmpty()){
			return rhModulo.consultarTodasJornadasTrabalho();
		}
		return null;
	}

	public void setItensJornadaTrabalho(final List<SelectItem> itensJornadaTrabalho) {
		this.itensJornadaTrabalho = itensJornadaTrabalho;
	}
	
	public List<SelectItem> getItensFormaPagamento() throws PotiErpException {
		List<FormaPagamento> listFormasPagamento = getFormasPagamento();
		if(listFormasPagamento != null){
			itensFormaPagamento.clear();
			addMock(itensFormaPagamento, MockEnum.SELECIONE);
			for(FormaPagamento formaPagamento : listFormasPagamento){
				SelectItem item = new SelectItem(formaPagamento, formaPagamento.getNome());
				itensFormaPagamento.add(item);
			}
		}
		return itensFormaPagamento;
	}

	private List<FormaPagamento> getFormasPagamento() throws PotiErpException {
		if(itensFormaPagamento.isEmpty()){
			return rhModulo.consultarTodasFormasPagamentos();
		}
		return null;
	}

	public void setItensFormaPagamento(final List<SelectItem> itensFormaPagamento) {
		this.itensFormaPagamento = itensFormaPagamento;
	}

	public boolean getIsEnderecoCadastrado() {
		return isEnderecoCadastrado;
	}

	public void setIsEnderecoCadastrado(final boolean isEnderecoCadastrado) {
		this.isEnderecoCadastrado = isEnderecoCadastrado;
	}
	
	public boolean getIsDadosPessoaisCadastrados() {
		return isDadosPessoaisCadastrados;
	}

	public void setIsDadosPessoaisCadastrados(final boolean isDadosPessoaisCadastrados) {
		this.isDadosPessoaisCadastrados = isDadosPessoaisCadastrados;
	}

	public boolean getIsDocumentosCadastrados() {
		return isDocumentosCadastrados;
	}

	public void setIsDocumentosCadastrados(final boolean isDocumentosCadastrados) {
		this.isDocumentosCadastrados = isDocumentosCadastrados;
	}
	
	public boolean getIsDocumentosEstrangeiroCadastrados() {
		return isDocumentosEstrangeiroCadastrados;
	}
	
	public boolean getIsContatosCadastrados() {
		return isContatosCadastrados;
	}

	public void setIsContatosCadastrados(final boolean isContatosCadastrados) {
		this.isContatosCadastrados = isContatosCadastrados;
	}

	public void setIsDocumentosEstrangeiroCadastrados(
			final boolean isDocumentosEstrangeiroCadastrados) {
		this.isDocumentosEstrangeiroCadastrados = isDocumentosEstrangeiroCadastrados;
	}

	public boolean getIsDadosFuncionaisCadastrados() {
		return isDadosFuncionaisCadastrados;
	}

	public void setIsDadosFuncionaisCadastrados(final boolean isDadosFuncionaisCadastrados) {
		this.isDadosFuncionaisCadastrados = isDadosFuncionaisCadastrados;
	}
	
	public ValeTransporte getValeTransporte() {
		return valeTransporte;
	}

	public void setValeTransporte(final ValeTransporte valeTransporte) {
		this.valeTransporte = valeTransporte;
	}
	
	public ValeTransporte getValeTransporteSelecionado() {
		return valeTransporteSelecionado;
	}

	public void setValeTransporteSelecionado(
			final ValeTransporte valeTransporteSelecionado) {
		this.valeTransporteSelecionado = valeTransporteSelecionado;
	}
	
	/**
	 * @return the cestaBasica
	 */
	public CestaBasica getCestaBasica() {
		return cestaBasica;
	}

	/**
	 * @param cestaBasica the cestaBasica to set
	 */
	public void setCestaBasica(final CestaBasica cestaBasica) {
		this.cestaBasica = cestaBasica;
	}

	/**
	 * @return the cestaBasicaSelecionada
	 */
	public CestaBasica getCestaBasicaSelecionada() {
		return cestaBasicaSelecionada;
	}

	/**
	 * @param cestaBasicaSelecionada the cestaBasicaSelecionada to set
	 */
	public void setCestaBasicaSelecionada(final CestaBasica cestaBasicaSelecionada) {
		this.cestaBasicaSelecionada = cestaBasicaSelecionada;
	}

	public ValeRefeicao getValeRefeicaoSelecionado() {
		return valeRefeicaoSelecionado;
	}

	public void setValeRefeicaoSelecionado(final ValeRefeicao valeRefeicaoSelecionado) {
		this.valeRefeicaoSelecionado = valeRefeicaoSelecionado;
	}

	public ValeRefeicao getValeRefeicao() {
		return valeRefeicao;
	}

	public void setValeRefeicao(final ValeRefeicao valeRefeicao) {
		this.valeRefeicao = valeRefeicao;
	}

	public boolean isCheckValeRefeicaoAll() {
		return checkValeRefeicaoAll;
	}

	public void setCheckValeRefeicaoAll(final boolean checkValeRefeicaoAll) {
		this.checkValeRefeicaoAll = checkValeRefeicaoAll;
	}

	public boolean getIsDependentesCadastrados() {
		return isDependentesCadastrados;
	}

	public void setIsDependentesCadastrados(final boolean isDependentesCadastrados) {
		this.isDependentesCadastrados = isDependentesCadastrados;
	}

	public boolean isCheckFuncionarioAll() {
		return checkFuncionarioAll;
	}

	public void setCheckFuncionarioAll(final boolean checkFuncionarioAll) {
		this.checkFuncionarioAll = checkFuncionarioAll;
	}
	
	public boolean isCheckLocalTrabalhoAll() {
		return checkLocalTrabalhoAll;
	}

	public void setCheckLocalTrabalhoAll(final boolean checkLocalTrabalhoAll) {
		this.checkLocalTrabalhoAll = checkLocalTrabalhoAll;
	}

	public Funcionario getFuncionarioSelecionado1() {
		return funcionarioSelecionado1;
	}

	public void setFuncionarioSelecionado1(final Funcionario funcionarioSelecionado1) {
		this.funcionarioSelecionado1 = funcionarioSelecionado1;
	}
	
	/**
	 * @return the funcionarioSelecionado
	 */
	public Selection getFuncionarioSelecionado() {
		return funcionarioSelecionado;
	}

	/**
	 * @param funcionarioSelecionado the funcionarioSelecionado to set
	 */
	public void setFuncionarioSelecionado(final Selection funcionarioSelecionado) {
		this.funcionarioSelecionado = funcionarioSelecionado;
	}

	public boolean getIsDadosBancariosCadastrados() {
		return isDadosBancariosCadastrados;
	}

	public void setIsDadosBancariosCadastrados(final boolean isDadosBancariosCadastrados) {
		this.isDadosBancariosCadastrados = isDadosBancariosCadastrados;
	}
	
	public boolean getIsLocalDeTrabalhoCadastrado() {
		return isLocalDeTrabalhoCadastrado;
	}

	public void setIsLocalDeTrabalhoCadastrado(final boolean isLocalDeTrabalhoCadastrado) {
		this.isLocalDeTrabalhoCadastrado = isLocalDeTrabalhoCadastrado;
	}

	public Integer getScrollerPage() {
		return scrollerPage;
	}

	public void setScrollerPage(final Integer scrollerPage) {
		this.scrollerPage = scrollerPage;
	}
	
	public Integer getScrollerPageDependentes() {
		return scrollerPageDependentes;
	}

	public void setScrollerPageDependentes(final Integer scrollerPageDependentes) {
		this.scrollerPageDependentes = scrollerPageDependentes;
	}

	public SelectionList<LocalTrabalho> getListLocalTrabalho() {
		return listLocalTrabalho;
	}

	public void setListLocalTrabalho(final SelectionList<LocalTrabalho> listLocalTrabalho) {
		this.listLocalTrabalho = listLocalTrabalho;
	}
	
	public SelectionList<Dependente> getListDependente() {
		return listDependente;
	}
	
	public SelectionList<ValeTransporte> getListValeTransporte() {
		return listValeTransporte;
	}

	public void setListValeTransporte(
			final SelectionList<ValeTransporte> listValeTransporte) {
		this.listValeTransporte = listValeTransporte;
	}
	
	public SelectionList<ValeRefeicao> getListValeRefeicao() {
		return listValeRefeicao;
	}

	public void setListValeRefeicao(final SelectionList<ValeRefeicao> listValeRefeicao) {
		this.listValeRefeicao = listValeRefeicao;
	}

	public void setListDependente(final SelectionList<Dependente> listDependente) {
		this.listDependente = listDependente;
	}
	
	public Cliente getClientePesquisa() {
		return clientePesquisa;
	}

	public void setClientePesquisa(final Cliente clientePesquisa) {
		this.clientePesquisa = clientePesquisa;
	}
	
	public boolean isCheckDependenteAll() {
		return checkDependenteAll;
	}

	public void setCheckDependenteAll(final boolean checkDependenteAll) {
		this.checkDependenteAll = checkDependenteAll;
	}
	
	public boolean isCheckValeTransporteAll() {
		return checkValeTransporteAll;
	}

	public void setCheckValeTransporteAll(final boolean checkValeTransporteAll) {
		this.checkValeTransporteAll = checkValeTransporteAll;
	}

	public Dependente getDependente() {
		return dependente;
	}

	public void setDependente(final Dependente dependente) {
		this.dependente = dependente;
	}
	
	public Dependente getDependenteSelecionado() {
		return dependenteSelecionado;
	}

	public void setDependenteSelecionado(final Dependente dependenteSelecionado) {
		this.dependenteSelecionado = dependenteSelecionado;
	}

	public LocalTrabalho getLocalTrabalhoSelecionado() {
		return localTrabalhoSelecionado;
	}

	public void setLocalTrabalhoSelecionado(final LocalTrabalho localTrabalhoSelecionado) {
		this.localTrabalhoSelecionado = localTrabalhoSelecionado;
	}
	
	public LocalTrabalho getLocalTrabalho() {
		return localTrabalho;
	}

	public void setLocalTrabalho(final LocalTrabalho localTrabalho) {
		this.localTrabalho = localTrabalho;
	}
	
	public PageableExtendedTableDataModel<SelectionEntity<Funcionario>> getDataModel() {
		return dataModel;
	}

	public void setDataModel(
			final PageableExtendedTableDataModel<SelectionEntity<Funcionario>> dataModel) {
		this.dataModel = dataModel;
	}

	@Override
	public Logger getLogger() {
		return log;
	}
	
	public String doCadastroDeFuncionario(){
		doConsultarPesquisa();
		return NavigationEnum.CADASTRO_DE_FUNCIONARIO.getValor();
	}
	
	public boolean isIncluirFuncionario(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_FUNCIONARIO.getCodigo());
	}
	
	public boolean isAlterarFuncionario(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_FUNCIONARIO.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_FUNCIONARIO.getCodigo());
	}
	
	public boolean isExcluirFuncionario(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_FUNCIONARIO.getCodigo());
	}
	
	public boolean isConsultarFuncionario(){
		return isIncluirFuncionario() || isExcluirFuncionario() || isAlterarFuncionario() || isConsultar();
	}
	
	public boolean isManterFuncionario(){
		return isIncluirFuncionario() || isAlterarFuncionario();
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

	public SituacaoFuncionario getSituacaoFuncionarioPesquisa() {
		return situacaoFuncionarioPesquisa;
	}

	public void setSituacaoFuncionarioPesquisa(
			final SituacaoFuncionario situacaoFuncionarioPesquisa) {
		this.situacaoFuncionarioPesquisa = situacaoFuncionarioPesquisa;
	}

	/**
	 * @return the filtroCodigoRegistro
	 */
	public String getFiltroCodigoRegistro() {
		return filtroCodigoRegistro;
	}

	/**
	 * @param filtroCodigoRegistro the filtroCodigoRegistro to set
	 */
	public void setFiltroCodigoRegistro(final String filtroCodigoRegistro) {
		this.filtroCodigoRegistro = filtroCodigoRegistro;
	}

	/**
	 * @return the filtroNome
	 */
	public String getFiltroNome() {
		return filtroNome;
	}

	/**
	 * @param filtroNome the filtroNome to set
	 */
	public void setFiltroNome(final String filtroNome) {
		this.filtroNome = filtroNome;
	}

	/**
	 * @return the filtroCtps
	 */
	public String getFiltroCtps() {
		return filtroCtps;
	}

	/**
	 * @param filtroCtps the filtroCtps to set
	 */
	public void setFiltroCtps(final String filtroCtps) {
		this.filtroCtps = filtroCtps;
	}

	/**
	 * @return the filtroSerie
	 */
	public String getFiltroSerie() {
		return filtroSerie;
	}

	/**
	 * @param filtroSerie the filtroSerie to set
	 */
	public void setFiltroSerie(final String filtroSerie) {
		this.filtroSerie = filtroSerie;
	}

	/**
	 * @return the filtroRg
	 */
	public String getFiltroRg() {
		return filtroRg;
	}

	/**
	 * @param filtroRg the filtroRg to set
	 */
	public void setFiltroRg(final String filtroRg) {
		this.filtroRg = filtroRg;
	}

	/**
	 * @return the filtroCpf
	 */
	public String getFiltroCpf() {
		return filtroCpf;
	}

	/**
	 * @param filtroCpf the filtroCpf to set
	 */
	public void setFiltroCpf(final String filtroCpf) {
		this.filtroCpf = filtroCpf;
	}

	/**
	 * @return the filtroCargo
	 */
	public String getFiltroCargo() {
		return filtroCargo;
	}

	/**
	 * @param filtroCargo the filtroCargo to set
	 */
	public void setFiltroCargo(final String filtroCargo) {
		this.filtroCargo = filtroCargo;
	}

	/**
	 * @return the filtroDataAdmissao
	 */
	public String getFiltroDataAdmissao() {
		return filtroDataAdmissao;
	}

	/**
	 * @param filtroDataAdmissao the filtroDataAdmissao to set
	 */
	public void setFiltroDataAdmissao(final String filtroDataAdmissao) {
		this.filtroDataAdmissao = filtroDataAdmissao;
	}
}
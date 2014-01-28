package br.com.potierp.modulos.rh.managed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.component.UIForm;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import br.com.potierp.business.rh.facade.RhModulo;
import br.com.potierp.business.rh.helper.FuncionarioValeTransporteHelper;
import br.com.potierp.business.rh.helper.TipoValeTransporteHelper;
import br.com.potierp.business.rh.service.CalcularValeTransporte;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.exception.PotiErpMensagensException;
import br.com.potierp.infra.helper.SelectionEntity;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.infra.resource.PotiErpProperties;
import br.com.potierp.model.CalculoValeTransporte;
import br.com.potierp.model.Funcionario;
import br.com.potierp.model.PagamentoValeTransporte;
import br.com.potierp.model.Pessoa;
import br.com.potierp.model.SituacaoValeTransporteEnum;
import br.com.potierp.model.TipoCalculoValeTransporteEnum;
import br.com.potierp.relatorio.rh.facade.RelatorioRhModulo;
import br.com.potierp.util.DateUtil;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class ValeTransporteMB extends BaseMB {

	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(ValeTransporteMB.class);

	private CalculoValeTransporte calculoValeTransporte = new CalculoValeTransporte();
	
	private CalculoValeTransporte calculoValeTransporteSelecionado = new CalculoValeTransporte();
	
	private Boolean isExibeFuncionario = false;
	
	private String nomeFuncionario;
	
	private Long re;
	
	private Funcionario funcionario = new Funcionario();
	
	private Funcionario funcionarioSelecionado = new Funcionario();
	
	private List<Funcionario> sugestoesFuncionarios;
	
	private List<SelectItem> itensTipoCalculo = new ArrayList<SelectItem>();
	
	private SelectionList<CalculoValeTransporte> listCalculoValeTransporte = new SelectionList<CalculoValeTransporte>();
	
	private SelectionList<FuncionarioValeTransporteHelper> listFuncionarioValeTransporteHelper = 
		new SelectionList<FuncionarioValeTransporteHelper>();
	
	private SelectionList<TipoValeTransporteHelper> listTipoValeTransporteHelper = 
		new SelectionList<TipoValeTransporteHelper>();
	
	private FuncionarioValeTransporteHelper funcionarioValeTransporteHelperSelecionado = new FuncionarioValeTransporteHelper();
	
	@EJB
	private RhModulo rhModulo;
	
	@EJB
	private RelatorioRhModulo relatorioRhModulo;
	
	private Date dataInicio;
	
	private Date dataFim;
	
	private String tipoCalculo;
	
	private boolean isCalculoHabilitado = false;
	
	private boolean isGravarHabilitado = false;
	
	private boolean checkValeTransporteAll = false;
	
	private Integer scrollerPage = 1;
	
	private Integer scrollerPageDetalhe = 1;
	
	private Integer scrollerPageResumo = 1;
	
	private String filtraMesAno = "";
	
	private String filtroDataInicio = "";
	
	private String filtroDataFim = "";
	
	private String filtroValorTotal = "";
	
	private String filtroQuantidadeTotal = "";
	
	private String filtroUsername = "";
	
	private String filtroSituacao = "";
	
	private String filtroTipoCalculo = "";
	
	public ValeTransporteMB(){
		doNovo();
	}
	
	public void doNovo(){
		calculoValeTransporte = new CalculoValeTransporte();
		calculoValeTransporte.setTipoCalculoEnum(TipoCalculoValeTransporteEnum.PERIODO);
		calculoValeTransporteSelecionado = new CalculoValeTransporte();
		dataInicio = null;
		dataFim = null;
		funcionarioValeTransporteHelperSelecionado = new FuncionarioValeTransporteHelper();
		checkValeTransporteAll = false;
		listFuncionarioValeTransporteHelper = new SelectionList<FuncionarioValeTransporteHelper>();
		listTipoValeTransporteHelper = new SelectionList<TipoValeTransporteHelper>();
		isCalculoHabilitado = false;
		isGravarHabilitado = false;
		inicializaFuncionario();
		doExibirFuncionario();
	}
	
	public void doCalcular(){
		try {
			calculoValeTransporte.setFuncionario(funcionario);
			calculoValeTransporte.setDataInicio(dataInicio);
			calculoValeTransporte.setDataFim(dataFim);
			calculoValeTransporte.setUsuario(getTraceInfo().getUsuario());
			calculoValeTransporte = rhModulo.calcularValeTransporte(calculoValeTransporte);
			popularGridFuncionario();
			listTipoValeTransporteHelper = new SelectionList<TipoValeTransporteHelper>();
			doConsultar();
		} catch (PotiErpMensagensException e) {
			e.printStackTrace();
		} catch (PotiErpException e) {
			e.printStackTrace();
		}
	}

	private void popularGridFuncionario() {
		if(calculoValeTransporte.getFuncionariosValeTransporteHelper() != null 
				&& !calculoValeTransporte.getFuncionariosValeTransporteHelper().isEmpty()){
			listFuncionarioValeTransporteHelper = 
				new SelectionList<FuncionarioValeTransporteHelper>(calculoValeTransporte.getFuncionariosValeTransporteHelper());
		}else{
			listFuncionarioValeTransporteHelper = 
					new SelectionList<FuncionarioValeTransporteHelper>();
		}
	}
	
	public void doDetalhar() throws Exception{
		calculoValeTransporte = calculoValeTransporteSelecionado.clone();
		dataInicio = (Date)calculoValeTransporte.getDataInicio().clone();
		dataFim = (Date)calculoValeTransporte.getDataFim().clone();
		List<PagamentoValeTransporte> pagamentos = rhModulo.consultarPagamentosValeTransportePorCalculo(calculoValeTransporte.getId());
		calculoValeTransporte.setPagamentosValeTransporte(pagamentos);
		CalcularValeTransporte calcular = new CalcularValeTransporte(calculoValeTransporte);
		calcular.calcular();
		if(isSituacaoGravado()){
			desabilitarBotoes();
		}
		popularGridFuncionario();
		doExibirFuncionario();
		carregarFuncionario();
	}

	private void carregarFuncionario() throws Exception {
		if(calculoValeTransporte.getTipoCalculoEnum().equals(TipoCalculoValeTransporteEnum.FUNCIONARIO)){
			this.funcionario = rhModulo.consultarFuncionarioPorCalculoValeTransporte(calculoValeTransporte.getId());
			calculoValeTransporte.setFuncionario(funcionario);
			
		}
	}

	private void desabilitarBotoes() {
		isCalculoHabilitado = true;
		isGravarHabilitado = true;
	}
	
	public void doDetalharFuncionario(){
		listTipoValeTransporteHelper = 
			new SelectionList<TipoValeTransporteHelper>(funcionarioValeTransporteHelperSelecionado.getTiposValeTransporte());
		//doExibirFuncionario();
	}
	
	public void doGravar(){
		try{
			validarParametrosObrigatorios();
			rhModulo.gravarValeTransporte(calculoValeTransporte);
			doNovo();
			limparForm();
			doConsultar();
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_AO_REALIZAR_O_CALCULAR.getKey());
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	private void validarParametrosObrigatorios() throws PotiErpMensagensException {
		List<PagamentoValeTransporte> pagamentos 
		= new ArrayList<PagamentoValeTransporte>(calculoValeTransporte.getPagamentosValeTransporte());
		if(pagamentos == null || pagamentos.isEmpty()){
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_NAO_EXISTE_PAGAMENTOS_PARA_GRAVACAO.getKey());
		}
	}

	public void doConsultar(){
		try{
			List<CalculoValeTransporte> calculos = rhModulo.consultarTodosCalculosValeTransporte();
			listCalculoValeTransporte = new SelectionList<CalculoValeTransporte>(calculos);
		} catch (Exception e){
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		}
	}
	
	public void doListaRecibo() {
		try {
			
			List<CalculoValeTransporte> listCalculoValeTransporteSelecionado = this.listCalculoValeTransporte.getItensSelecionados();
			validarListaRecibo(listCalculoValeTransporteSelecionado);
			
			CalculoValeTransporte calcValeTransporte = listCalculoValeTransporteSelecionado.get(0);
			List<PagamentoValeTransporte> pagamentos = rhModulo.consultarPagamentosValeTransportePorCalculo(calcValeTransporte.getId());
			calcValeTransporte.setPagamentosValeTransporte(pagamentos);
			
			CalcularValeTransporte calcular = new CalcularValeTransporte(calcValeTransporte);
			calcular.calcular();
			
			byte[] relatorio = relatorioRhModulo.getReciboValeTransporte(listCalculoValeTransporteSelecionado);
			super.registraArquivoParaDownload(relatorio, "Recibo Vale Transporte", "pdf", PotiErpProperties.getInstance().getReportPath());
			checkValeTransporteAll = false;
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (Exception e) {
			addMensagemErro(e);
		}
	}
	
	private void validarListaRecibo(final List<CalculoValeTransporte> listCalculoValeTransporteSelecionado) throws PotiErpMensagensException {
		validarPagamentosRecibo(listCalculoValeTransporteSelecionado);
		for(CalculoValeTransporte calculoValeTransporte : listCalculoValeTransporteSelecionado){
			if(calculoValeTransporte.getSituacao().equals(SituacaoValeTransporteEnum.CALCULADO)){
				throw new PotiErpMensagensException(MensagensFacesEnum.SELECIONE_CALCULO_SITUACAO_GRAVADO_PARA_GERAR_RECIBO.getKey());
			}
		}
	}
	
	private void validarPagamentosRecibo(final List<CalculoValeTransporte> listCalculoValeTransporteSelecionado) 
		throws PotiErpMensagensException {
		if (listCalculoValeTransporteSelecionado.isEmpty()) {
			throw new PotiErpMensagensException(MensagensFacesEnum.SELECIONE_PELO_MENOS_UM_CALCULO_GRAVADO_PARA_GERAR_RECIBO.getKey());
		}
	}

	public void doRecibo() {
		try {
			validarRecibo();
			
			List<FuncionarioValeTransporteHelper> funcionarios = new ArrayList<FuncionarioValeTransporteHelper>();
			funcionarios.add(this.funcionarioValeTransporteHelperSelecionado);
			CalculoValeTransporte calculo = this.calculoValeTransporte.clone();
			calculo.setFuncionariosValeTransporteHelper(funcionarios);
			List<CalculoValeTransporte> listCalculoValeTransporteSelecionado = new ArrayList<CalculoValeTransporte>();
			listCalculoValeTransporteSelecionado.add(calculo);
			byte[] relatorio = relatorioRhModulo.getReciboValeTransporte(listCalculoValeTransporteSelecionado);
			super.registraArquivoParaDownload(relatorio, "Recibo Vale Transporte", "pdf", PotiErpProperties.getInstance().getReportPath());
			
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (Exception e) {
			addMensagemErro(e);
		}
	}
	
	private void validarRecibo() throws PotiErpMensagensException{
		if(isSituacaoCalculado()){
			throw new PotiErpMensagensException(MensagensFacesEnum.PARA_GERAR_RECIBO_CALCULO_DEVE_SER_GRAVADO.getKey());
		}
	}

	public void doListaMapa() {
		try {
			List<CalculoValeTransporte> listCalculoValeTransporteSelecionado = this.listCalculoValeTransporte.getItensSelecionados();
			validarPagamentosMapa(listCalculoValeTransporteSelecionado);
			CalculoValeTransporte calcTransporte = listCalculoValeTransporteSelecionado.get(0);
			List<PagamentoValeTransporte> pagamentos = rhModulo.consultarPagamentosValeTransportePorCalculo(calcTransporte.getId());
			calcTransporte.setPagamentosValeTransporte(pagamentos);
			byte[] relatorio = relatorioRhModulo.getMapaValeTransporte(listCalculoValeTransporteSelecionado);
			super.registraArquivoParaDownload(relatorio, "Mapa de Vale Transporte", "pdf", PotiErpProperties.getInstance().getReportPath());
			checkValeTransporteAll = false;
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (Exception e) {
			addMensagemErro(e);
		}
	}
	
	private void validarPagamentosMapa(final List<CalculoValeTransporte> listCalculoValeTransporteSelecionado) throws PotiErpMensagensException {
		if (listCalculoValeTransporteSelecionado.isEmpty()) {
			throw new PotiErpMensagensException(MensagensFacesEnum.SELECIONE_PELO_MENOS_UM_CALCULO_PARA_GERAR_MAPA.getKey());
		}
	}
	
	public void doMapa() {
		try {
			validarMapa();
			List<CalculoValeTransporte> listCalculoValeTransporte = new ArrayList<CalculoValeTransporte>();
			listCalculoValeTransporte.add(calculoValeTransporte);
			byte[] relatorio = relatorioRhModulo.getMapaValeTransporte(listCalculoValeTransporte);
			super.registraArquivoParaDownload(relatorio, "Mapa de Vale Transporte", "pdf", PotiErpProperties.getInstance().getReportPath());
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (Exception e) {
			addMensagemErro(e);
		}
	}
	
	private void validarMapa() throws PotiErpMensagensException{
		if(calculoValeTransporte.getSituacao() == null || (!isSituacaoCalculado() && !isSituacaoGravado())){
			throw new PotiErpMensagensException(MensagensFacesEnum.REALIZE_CALCULO_PARA_GERAR_MAPA.getKey());
		}
	}
	
	public void doExibirFuncionario(){
		if(calculoValeTransporte != null 
				&& calculoValeTransporte.getTipoCalculoEnum() != null 
				&& calculoValeTransporte.getTipoCalculoEnum().equals(TipoCalculoValeTransporteEnum.FUNCIONARIO)){
			isExibeFuncionario = true;
		}else{
			isExibeFuncionario = false;
			funcionario = new Funcionario();
			funcionarioSelecionado = new Funcionario();
		}
	}
	
	public void buscarFuncionarios() {
		try {
			if(isRegistroEmpregadoPreenchido()) {
				if(sugestoesFuncionarios == null) {
					carregarSugestoes();
				}
				funcionario = getFuncionarioPorRE();
				if(funcionario == null) {
					addMensagemErro(MensagensFacesEnum.ERRO_FUNCIONARIO_NAO_ENCONTRADO);
					inicializaFuncionario();
				}
			} else {
				inicializaFuncionario();
			}
		} catch (Exception e) {
			addMensagemErro(e.getMessage());
		}
	}
	
	private void inicializaFuncionario() {
		funcionario = new Funcionario();
		funcionario.setPessoa(new Pessoa());
	}
	
	private boolean isRegistroEmpregadoPreenchido() {
		return funcionario !=null 
				&& funcionario.getCodigoRegistro() != null
				&& funcionario.getCodigoRegistro() > 0;
	}
	
	private Funcionario getFuncionarioPorRE() throws Exception {
		if(sugestoesFuncionarios != null){
			for(Funcionario func : sugestoesFuncionarios){
				if(func.getCodigoRegistro() != null && func.getCodigoRegistro().equals(funcionario.getCodigoRegistro())) {
					return func.clone();
				}
			}
		}
		return null;
	}
	
	public void selecionarFuncionario() throws Exception {
		this.funcionario = this.funcionarioSelecionado.clone();
		this.funcionario.setPessoa(this.funcionarioSelecionado.getPessoa().clone());
		this.funcionarioSelecionado = new Funcionario();
	}
	
	public List<Funcionario> sugestoesFuncionarios(final Object evento) throws Exception{
		
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
		return funcionarios;
	}
	
	private void carregarSugestoes() throws PotiErpException {
		sugestoesFuncionarios = rhModulo.consultarTodosFuncionariosComNomeRE();
	}
	
	public void doExcluir(){
		try{
			List<CalculoValeTransporte> listCalculoValeTransporteSelecionado = this.listCalculoValeTransporte.getItensSelecionados();
			
			if(listCalculoValeTransporteSelecionado != null && !listCalculoValeTransporteSelecionado.isEmpty()){
				CalculoValeTransporte calculo = listCalculoValeTransporteSelecionado.get(0);
				if(calculo.getSituacao().equals(SituacaoValeTransporteEnum.CALCULADO)){
					rhModulo.excluirCalculoValeTransporte(calculo);
					doConsultar();
				}else{
					throw new PotiErpMensagensException(MensagensFacesEnum.REGISTRO_COM_SITUACAO_CALCULADO_PARA_EXCLUIR.getKey());
				}
			}
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (Exception e) {
			addMensagemErro(e);
		}
	}
	
	private boolean isSituacaoCalculado() {
		return calculoValeTransporte != null && calculoValeTransporte.getSituacao().equals(SituacaoValeTransporteEnum.CALCULADO);
	}
	
	private boolean isSituacaoGravado() {
		return calculoValeTransporte != null && calculoValeTransporte.getSituacao().equals(SituacaoValeTransporteEnum.GRAVADO);
	}
	
	public void limparForm() throws Exception {
		UIForm form = (UIForm) getFacesContext().getViewRoot().findComponent("formDetails");
		cleanSubmittedValues(form);
		doNovo();
		funcionarioValeTransporteHelperSelecionado = new FuncionarioValeTransporteHelper();
	}
	
	public boolean filtraMesAno(final Object current) {
		@SuppressWarnings("unchecked")
		CalculoValeTransporte calculoValeTransporte = ((SelectionEntity<CalculoValeTransporte>) current).getEntity();
		String mesAno = calculoValeTransporte.getReferencia();
		if (filtraMesAno == null || filtraMesAno.length()==0) {
			return true;
	    }
		
		if(mesAno == null)
			return false;

		if(mesAno.toLowerCase().startsWith(filtraMesAno.toLowerCase().toString())){
			return true;
		}else{
			return false;
		} 
	}
	
	public boolean filtraDataInicio(final Object current) {
		@SuppressWarnings("unchecked")
		CalculoValeTransporte calculoValeTransporte = ((SelectionEntity<CalculoValeTransporte>) current).getEntity();
		Date dataInicio = calculoValeTransporte.getDataInicio();
		if (filtroDataInicio == null || filtroDataInicio.length()==0) {
			return true;
	    }
		
		if(dataInicio == null)
			return false;
		
		if(DateUtil.dateToPtBrString(dataInicio).startsWith(filtroDataInicio.toString())){
			return true;
		}else{
			return false;
		} 
	}
	
	public boolean filtraDataFim(final Object current) {
		@SuppressWarnings("unchecked")
		CalculoValeTransporte calculoValeTransporte = ((SelectionEntity<CalculoValeTransporte>) current).getEntity();
		Date dataFim = calculoValeTransporte.getDataFim();
		if (filtroDataFim == null || filtroDataFim.length()==0) {
			return true;
	    }
		
		if(dataFim == null)
			return false;
		
		if(DateUtil.dateToPtBrString(dataFim).startsWith(filtroDataFim.toString())){
			return true;
		}else{
			return false;
		} 
	}
	
	public boolean filtraValorTotal(final Object current) {
		@SuppressWarnings("unchecked")
		CalculoValeTransporte calculoValeTransporte = ((SelectionEntity<CalculoValeTransporte>) current).getEntity();
		String valorTotal = calculoValeTransporte.getValorTotal().toString();
		if (filtroValorTotal == null || filtroValorTotal.length()==0) {
			return true;
	    }
		
		if(valorTotal == null)
			return false;
		
		if(valorTotal.startsWith(filtroValorTotal.toString())){
			return true;
		}else{
			return false;
		} 
	}
	
	public boolean filtraQuantidadeTotal(final Object current) {
		@SuppressWarnings("unchecked")
		CalculoValeTransporte calculoValeTransporte = ((SelectionEntity<CalculoValeTransporte>) current).getEntity();
		String quantidadeTotal = calculoValeTransporte.getQuantidadeTotal().toString();
		if (filtroQuantidadeTotal == null || filtroQuantidadeTotal.length()==0) {
			return true;
	    }

		if(quantidadeTotal == null)
			return false;

		if(quantidadeTotal.startsWith(filtroQuantidadeTotal.toString())){
			return true;
		}else{
			return false;
		} 
	}
	
	public boolean filtraSituacao(final Object current) {
		@SuppressWarnings("unchecked")
		CalculoValeTransporte calculoValeTransporte = ((SelectionEntity<CalculoValeTransporte>) current).getEntity();
		String situacao = calculoValeTransporte.getSituacao().toString();
		if (filtroSituacao == null || filtroSituacao.length()==0) {
			return true;
	    }

		if(situacao == null)
			return false;

		if(situacao.toLowerCase().startsWith(filtroSituacao.toLowerCase().toString())){
			return true;
		}else{
			return false;
		} 
	}
	
	public boolean filtraTipoCalculo(final Object current) {
		@SuppressWarnings("unchecked")
		CalculoValeTransporte calculoValeTransporte = ((SelectionEntity<CalculoValeTransporte>) current).getEntity();
		String tipoCalculo = calculoValeTransporte.getTipoCalculoEnum().getTipoCalculo();
		if (filtroTipoCalculo == null || filtroTipoCalculo.length()==0) {
			return true;
	    }

		if(tipoCalculo == null)
			return false;

		if(tipoCalculo.toLowerCase().startsWith(filtroTipoCalculo.toLowerCase().toString())){
			return true;
		}else{
			return false;
		} 
	}

	public boolean filtraUsername(final Object current) {
		@SuppressWarnings("unchecked")
		CalculoValeTransporte calculoValeTransporte = ((SelectionEntity<CalculoValeTransporte>) current).getEntity();
		String username = calculoValeTransporte.getUsuario().getUsername().toString();
		if (filtroUsername == null || filtroUsername.length()==0) {
			return true;
	    }

		if(username == null)
			return false;

		if(username.toLowerCase().startsWith(filtroUsername.toLowerCase().toString())){
			return true;
		}else{
			return false;
		} 
	}
		
	/**
	 * @return the calculoValeTransporte
	 */
	public CalculoValeTransporte getCalculoValeTransporte() {
		return calculoValeTransporte;
	}

	/**
	 * @param calculoValeTransporte the calculoValeTransporte to set
	 */
	public void setCalculoValeTransporte(final CalculoValeTransporte calculoValeTransporte) {
		this.calculoValeTransporte = calculoValeTransporte;
	}

	/**
	 * @return the listCalculoValeTransporte
	 */
	public SelectionList<CalculoValeTransporte> getListCalculoValeTransporte() {
		return listCalculoValeTransporte;
	}

	/**
	 * @param listCalculoValeTransporte the listCalculoValeTransporte to set
	 */
	public void setListCalculoValeTransporte(
			final SelectionList<CalculoValeTransporte> listCalculoValeTransporte) {
		this.listCalculoValeTransporte = listCalculoValeTransporte;
	}
	
	/**
	 * @return the listFuncionarioValeTransporteHelper
	 */
	public SelectionList<FuncionarioValeTransporteHelper> getListFuncionarioValeTransporteHelper() {
		return listFuncionarioValeTransporteHelper;
	}

	/**
	 * @param listFuncionarioValeTransporteHelper the listFuncionarioValeTransporteHelper to set
	 */
	public void setListFuncionarioValeTransporteHelper(
			final SelectionList<FuncionarioValeTransporteHelper> listFuncionarioValeTransporteHelper) {
		this.listFuncionarioValeTransporteHelper = listFuncionarioValeTransporteHelper;
	}

	/**
	 * @return the listTipoValeTransporteHelper
	 */
	public SelectionList<TipoValeTransporteHelper> getListTipoValeTransporteHelper() {
		return listTipoValeTransporteHelper;
	}

	/**
	 * @param listTipoValeTransporteHelper the listTipoValeTransporteHelper to set
	 */
	public void setListTipoValeTransporteHelper(
			final SelectionList<TipoValeTransporteHelper> listTipoValeTransporteHelper) {
		this.listTipoValeTransporteHelper = listTipoValeTransporteHelper;
	}

	/**
	 * @return the checkValeTransporteAll
	 */
	public boolean isCheckValeTransporteAll() {
		return checkValeTransporteAll;
	}
	
	/**
	 * @return the funcionarioValeTransporteHelperSelecionado
	 */
	public FuncionarioValeTransporteHelper getFuncionarioValeTransporteHelperSelecionado() {
		return funcionarioValeTransporteHelperSelecionado;
	}

	/**
	 * @param funcionarioValeTransporteHelperSelecionado the funcionarioValeTransporteHelperSelecionado to set
	 */
	public void setFuncionarioValeTransporteHelperSelecionado(
			final FuncionarioValeTransporteHelper funcionarioValeTransporteHelperSelecionado) {
		this.funcionarioValeTransporteHelperSelecionado = funcionarioValeTransporteHelperSelecionado;
	}

	/**
	 * @param checkValeTransporteAll the checkValeTransporteAll to set
	 */
	public void setCheckValeTransporteAll(final boolean checkValeTransporteAll) {
		this.checkValeTransporteAll = checkValeTransporteAll;
	}
	
	/**
	 * @return the calculoValeTransporteSelecionado
	 */
	public CalculoValeTransporte getCalculoValeTransporteSelecionado() {
		return calculoValeTransporteSelecionado;
	}

	/**
	 * @param calculoValeTransporteSelecionado the calculoValeTransporteSelecionado to set
	 */
	public void setCalculoValeTransporteSelecionado(
			final CalculoValeTransporte calculoValeTransporteSelecionado) {
		this.calculoValeTransporteSelecionado = calculoValeTransporteSelecionado;
	}
	
	public List<SelectItem> getItensTipoCalculo() {
		List<TipoCalculoValeTransporteEnum> listTipoCalculo = getListTipoCalculo();
		if(listTipoCalculo != null){
			itensTipoCalculo.clear();
			for(TipoCalculoValeTransporteEnum tipoCalculo : listTipoCalculo){
				SelectItem item = new SelectItem(tipoCalculo, tipoCalculo.getTipoCalculo());
				itensTipoCalculo.add(item);
			}
		}
		return itensTipoCalculo;
	}
	
	public List<TipoCalculoValeTransporteEnum> getListTipoCalculo() {
		if(itensTipoCalculo.isEmpty()){ 
			return new ArrayList<TipoCalculoValeTransporteEnum>(Arrays.asList(TipoCalculoValeTransporteEnum.values()));
		}
		return null;
	}

	public void setItensTipoCalculo(final List<SelectItem> itensTipoCalculo) {
		this.itensTipoCalculo = itensTipoCalculo;
	}

	/**
	 * @return the isCalculoHabilitado
	 */
	public boolean getIsCalculoHabilitado() {
		return isCalculoHabilitado;
	}

	/**
	 * @param isCalculoHabilitado the isCalculoHabilitado to set
	 */
	public void setIsCalculoHabilitado(final boolean isCalculoHabilitado) {
		this.isCalculoHabilitado = isCalculoHabilitado;
	}

	/**
	 * @return the isGravarHabilitado
	 */
	public boolean getIsGravarHabilitado() {
		return isGravarHabilitado;
	}

	/**
	 * @param isGravarHabilitado the isGravarHabilitado to set
	 */
	public void setIsGravarHabilitado(final boolean isGravarHabilitado) {
		this.isGravarHabilitado = isGravarHabilitado;
	}

	public Integer getScrollerPage() {
		return scrollerPage;
	}

	public void setScrollerPage(final Integer scrollerPage) {
		this.scrollerPage = scrollerPage;
	}
	
	/**
	 * @return the scrollerPageDetalhe
	 */
	public Integer getScrollerPageDetalhe() {
		return scrollerPageDetalhe;
	}

	/**
	 * @param scrollerPageDetalhe the scrollerPageDetalhe to set
	 */
	public void setScrollerPageDetalhe(final Integer scrollerPageDetalhe) {
		this.scrollerPageDetalhe = scrollerPageDetalhe;
	}

	/**
	 * @return the scrollerPageResumo
	 */
	public Integer getScrollerPageResumo() {
		return scrollerPageResumo;
	}

	/**
	 * @param scrollerPageResumo the scrollerPageResumo to set
	 */
	public void setScrollerPageResumo(final Integer scrollerPageResumo) {
		this.scrollerPageResumo = scrollerPageResumo;
	}
	
	/**
	 * @return the filtroDataInicio
	 */
	public String getFiltroDataInicio() {
		return filtroDataInicio;
	}

	/**
	 * @param filtroDataInicio the filtroDataInicio to set
	 */
	public void setFiltroDataInicio(final String filtroDataInicio) {
		this.filtroDataInicio = filtroDataInicio;
	}
	
	/**
	 * @return the filtroDataFim
	 */
	public String getFiltroDataFim() {
		return filtroDataFim;
	}

	/**
	 * @param filtroDataFim the filtroDataFim to set
	 */
	public void setFiltroDataFim(final String filtroDataFim) {
		this.filtroDataFim = filtroDataFim;
	}
	
	/**
	 * @return the filtroValorTotal
	 */
	public String getFiltroValorTotal() {
		return filtroValorTotal;
	}

	/**
	 * @param filtroValorTotal the filtroValorTotal to set
	 */
	public void setFiltroValorTotal(final String filtroValorTotal) {
		this.filtroValorTotal = filtroValorTotal;
	}

	/**
	 * @return the filtroQuantidadeTotal
	 */
	public String getFiltroQuantidadeTotal() {
		return filtroQuantidadeTotal;
	}

	/**
	 * @param filtroQuantidadeTotal the filtroQuantidadeTotal to set
	 */
	public void setFiltroQuantidadeTotal(final String filtroQuantidadeTotal) {
		this.filtroQuantidadeTotal = filtroQuantidadeTotal;
	}

	/**
	 * @return the filtroUsername
	 */
	public String getFiltroUsername() {
		return filtroUsername;
	}

	/**
	 * @param filtroUsername the filtroUsername to set
	 */
	public void setFiltroUsername(final String filtroUsername) {
		this.filtroUsername = filtroUsername;
	}

	/**
	 * @return the filtroSituacao
	 */
	public String getFiltroSituacao() {
		return filtroSituacao;
	}

	/**
	 * @param filtroSituacao the filtroSituacao to set
	 */
	public void setFiltroSituacao(final String filtroSituacao) {
		this.filtroSituacao = filtroSituacao;
	}
	
	/**
	 * @return the filtraMesAno
	 */
	public String getFiltraMesAno() {
		return filtraMesAno;
	}

	/**
	 * @param filtraMesAno the filtraMesAno to set
	 */
	public void setFiltraMesAno(final String filtraMesAno) {
		this.filtraMesAno = filtraMesAno;
	}
	
	/**
	 * @return the filtroTipoCalculo
	 */
	public String getFiltroTipoCalculo() {
		return filtroTipoCalculo;
	}

	/**
	 * @param filtroTipoCalculo the filtroTipoCalculo to set
	 */
	public void setFiltroTipoCalculo(final String filtroTipoCalculo) {
		this.filtroTipoCalculo = filtroTipoCalculo;
	}
	
	/**
	 * @return the dataInicio
	 */
	public Date getDataInicio() {
		return dataInicio;
	}

	/**
	 * @param dataInicio the dataInicio to set
	 */
	public void setDataInicio(final Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	/**
	 * @return the dataFim
	 */
	public Date getDataFim() {
		return dataFim;
	}

	/**
	 * @param dataFim the dataFim to set
	 */
	public void setDataFim(final Date dataFim) {
		this.dataFim = dataFim;
	}

	/**
	 * @return the tipoCalculo
	 */
	public String getTipoCalculo() {
		return tipoCalculo;
	}

	/**
	 * @param tipoCalculo the tipoCalculo to set
	 */
	public void setTipoCalculo(final String tipoCalculo) {
		this.tipoCalculo = tipoCalculo;
	}

	/**
	 * @return the nomeFuncionario
	 */
	public String getNomeFuncionario() {
		return nomeFuncionario;
	}

	/**
	 * @param nomeFuncionario the nomeFuncionario to set
	 */
	public void setNomeFuncionario(final String nomeFuncionario) {
		this.nomeFuncionario = nomeFuncionario;
	}
	
	/**
	 * @return the isExibeFuncionario
	 */
	public Boolean getIsExibeFuncionario() {
		return isExibeFuncionario;
	}

	/**
	 * @param isExibeFuncionario the isExibeFuncionario to set
	 */
	public void setIsExibeFuncionario(final Boolean isExibeFuncionario) {
		this.isExibeFuncionario = isExibeFuncionario;
	}

	/**
	 * @return the re
	 */
	public Long getRe() {
		return re;
	}

	/**
	 * @param re the re to set
	 */
	public void setRe(final Long re) {
		this.re = re;
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
	 * @return the sugestoesFuncionarios
	 */
	public List<Funcionario> getSugestoesFuncionarios() {
		return sugestoesFuncionarios;
	}

	/**
	 * @param sugestoesFuncionarios the sugestoesFuncionarios to set
	 */
	public void setSugestoesFuncionarios(final List<Funcionario> sugestoesFuncionarios) {
		this.sugestoesFuncionarios = sugestoesFuncionarios;
	}

	@Override
	public Logger getLogger() {
		return log;
	}
	
	public boolean isIncluirValeTransporte(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isAlterarValeTransporte(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_VALE_TRANSPORTE.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isExcluirValeTransporte(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isConsultarValeTransporte(){
		return isIncluirValeTransporte() || isExcluirValeTransporte() || isAlterarValeTransporte() || isConsultar();
	}
	
	public boolean isManterValeTransporte(){
		return isIncluirValeTransporte() || isAlterarValeTransporte();
	}
	
	public String doCadastroDeValeTransporte(){
		doConsultar();
		return NavigationEnum.CADASTRO_DE_VALETRANSPORTE.getValor();
	}
}
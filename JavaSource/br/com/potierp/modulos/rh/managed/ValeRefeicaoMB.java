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
import br.com.potierp.business.rh.helper.FuncionarioValeRefeicaoHelper;
import br.com.potierp.business.rh.helper.TipoValeRefeicaoHelper;
import br.com.potierp.business.rh.service.CalcularValeRefeicao;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.exception.PotiErpMensagensException;
import br.com.potierp.infra.helper.SelectionEntity;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.infra.resource.PotiErpProperties;
import br.com.potierp.model.CalculoValeRefeicao;
import br.com.potierp.model.PagamentoValeRefeicao;
import br.com.potierp.model.SituacaoValeRefeicaoEnum;
import br.com.potierp.model.TipoCalculoValeRefeicaoEnum;
import br.com.potierp.relatorio.rh.facade.RelatorioRhModulo;
import br.com.potierp.util.DateUtil;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class ValeRefeicaoMB extends BaseMB {

	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(ValeRefeicaoMB.class);

	private CalculoValeRefeicao calculoValeRefeicao = new CalculoValeRefeicao();
	
	private CalculoValeRefeicao calculoValeRefeicaoSelecionado = new CalculoValeRefeicao();
	
	private Boolean isExibeFuncionario = false;
	
	private String nomeFuncionario;
	
	private Long re;
	
	private List<SelectItem> itensTipoCalculo = new ArrayList<SelectItem>();
	
	private SelectionList<CalculoValeRefeicao> listCalculoValeRefeicao = new SelectionList<CalculoValeRefeicao>();
	
	private SelectionList<FuncionarioValeRefeicaoHelper> listFuncionarioValeRefeicaoHelper = 
		new SelectionList<FuncionarioValeRefeicaoHelper>();
	
	private SelectionList<TipoValeRefeicaoHelper> listTipoValeRefeicaoHelper = 
		new SelectionList<TipoValeRefeicaoHelper>();
	
	private FuncionarioValeRefeicaoHelper funcionarioValeRefeicaoHelperSelecionado = new FuncionarioValeRefeicaoHelper();
	
	@EJB
	private RhModulo rhModulo;
	
	@EJB
	private RelatorioRhModulo relatorioRhModulo;
	
	private Date dataInicio;
	
	private Date dataFim;
	
	private boolean isCalculoHabilitado = false;
	
	private boolean isGravarHabilitado = false;
	
	private boolean checkValeRefeicaoAll = false;
	
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
	
	public ValeRefeicaoMB(){
	}
	
	public void doNovo(){
		calculoValeRefeicao = new CalculoValeRefeicao();
		calculoValeRefeicao.setTipoCalculoEnum(TipoCalculoValeRefeicaoEnum.PERIODO);
		calculoValeRefeicaoSelecionado = new CalculoValeRefeicao();
		dataInicio = null;
		dataFim = null;
		funcionarioValeRefeicaoHelperSelecionado = new FuncionarioValeRefeicaoHelper();
		checkValeRefeicaoAll = false;
		listFuncionarioValeRefeicaoHelper = new SelectionList<FuncionarioValeRefeicaoHelper>();
		listTipoValeRefeicaoHelper = new SelectionList<TipoValeRefeicaoHelper>();
		isCalculoHabilitado = false;
		isGravarHabilitado = false;
	}
	
	public void doCalcular(){
		try {
			calculoValeRefeicao.setTipoCalculoEnum(TipoCalculoValeRefeicaoEnum.PERIODO);
			calculoValeRefeicao.setDataInicio(dataInicio);
			calculoValeRefeicao.setDataFim(dataFim);
			calculoValeRefeicao.setTipoCalculoEnum(TipoCalculoValeRefeicaoEnum.PERIODO);
			calculoValeRefeicao.setUsuario(getTraceInfo().getUsuario());
			calculoValeRefeicao = rhModulo.calcularValeRefeicao(calculoValeRefeicao);
			popularGridFuncionario();
			listTipoValeRefeicaoHelper = new SelectionList<TipoValeRefeicaoHelper>();
			doConsultar();
		} catch (PotiErpMensagensException e) {
			e.printStackTrace();
		} catch (PotiErpException e) {
			e.printStackTrace();
		}
	}

	private void popularGridFuncionario() {
		if(calculoValeRefeicao.getFuncionariosValeRefeicaoHelper() != null){
			listFuncionarioValeRefeicaoHelper = 
				new SelectionList<FuncionarioValeRefeicaoHelper>(calculoValeRefeicao.getFuncionariosValeRefeicaoHelper());
		}
	}
	
	public void doDetalhar() throws Exception{
		calculoValeRefeicao = calculoValeRefeicaoSelecionado.clone();
		dataInicio = (Date)calculoValeRefeicao.getDataInicio().clone();
		dataFim = (Date)calculoValeRefeicao.getDataFim().clone();
		List<PagamentoValeRefeicao> pagamentos = rhModulo.consultarPagamentosValeRefeicaoPorCalculo(calculoValeRefeicao.getId());
		calculoValeRefeicao.setPagamentosValeRefeicao(pagamentos);
		CalcularValeRefeicao calcular = new CalcularValeRefeicao(calculoValeRefeicao);
		calcular.calcular();
		if(isSituacaoGravado()){
			desabilitarBotoes();
		}
		popularGridFuncionario();
	}

	private void desabilitarBotoes() {
		isCalculoHabilitado = true;
		isGravarHabilitado = true;
	}
	
	public void doDetalharFuncionario(){
		listTipoValeRefeicaoHelper = 
			new SelectionList<TipoValeRefeicaoHelper>(funcionarioValeRefeicaoHelperSelecionado.getTiposValeRefeicao());
		//doExibirFuncionario();
	}
	
	public void doGravar(){
		try{
			validarParametrosObrigatorios();
			calculoValeRefeicao.setTipoCalculoEnum(TipoCalculoValeRefeicaoEnum.PERIODO);
			rhModulo.gravarValeRefeicao(calculoValeRefeicao);
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
		List<PagamentoValeRefeicao> pagamentos 
		= new ArrayList<PagamentoValeRefeicao>(calculoValeRefeicao.getPagamentosValeRefeicao());
		if(pagamentos == null || pagamentos.isEmpty()){
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_NAO_EXISTE_PAGAMENTOS_PARA_GRAVACAO.getKey());
		}
	}

	public void doConsultar(){
		try{
			List<CalculoValeRefeicao> calculos = rhModulo.consultarTodosCalculosValeRefeicao();
			listCalculoValeRefeicao = new SelectionList<CalculoValeRefeicao>(calculos);
		} catch (Exception e){
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		}
	}
	
	public void doListaRecibo() {
		try {
			
			List<CalculoValeRefeicao> listCalculoValeRefeicaoSelecionado = this.listCalculoValeRefeicao.getItensSelecionados();
			validarListaRecibo(listCalculoValeRefeicaoSelecionado);
			
			CalculoValeRefeicao calcValeRefeicao = listCalculoValeRefeicaoSelecionado.get(0);
			List<PagamentoValeRefeicao> pagamentos = rhModulo.consultarPagamentosValeRefeicaoPorCalculo(calcValeRefeicao.getId());
			calcValeRefeicao.setPagamentosValeRefeicao(pagamentos);
			CalcularValeRefeicao calcular = new CalcularValeRefeicao(calcValeRefeicao);
			calcular.calcular();
			
			byte[] relatorio = relatorioRhModulo.getReciboValeRefeicao(listCalculoValeRefeicaoSelecionado);
			super.registraArquivoParaDownload(relatorio, "Recibo Vale Refeicao", "pdf", PotiErpProperties.getInstance().getReportPath());
			checkValeRefeicaoAll = false;
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (Exception e) {
			addMensagemErro(e);
		}
	}
	
	private void validarListaRecibo(final List<CalculoValeRefeicao> listCalculoValeRefeicaoSelecionado) throws PotiErpMensagensException {
		validarPagamentosRecibo(listCalculoValeRefeicaoSelecionado);
		for(CalculoValeRefeicao calculoValeRefeicao : listCalculoValeRefeicaoSelecionado){
			if(calculoValeRefeicao.getSituacao().equals(SituacaoValeRefeicaoEnum.CALCULADO)){
				throw new PotiErpMensagensException(MensagensFacesEnum.SELECIONE_CALCULO_SITUACAO_GRAVADO_PARA_GERAR_RECIBO.getKey());
			}
		}
	}
	
	private void validarPagamentosRecibo(final List<CalculoValeRefeicao> listCalculoValeRefeicaoSelecionado) 
		throws PotiErpMensagensException {
		if (listCalculoValeRefeicaoSelecionado.isEmpty()) {
			throw new PotiErpMensagensException(MensagensFacesEnum.SELECIONE_PELO_MENOS_UM_CALCULO_GRAVADO_PARA_GERAR_RECIBO.getKey());
		}
	}

	public void doRecibo() {
		try {
			validarRecibo();
			
			List<FuncionarioValeRefeicaoHelper> funcionarios = new ArrayList<FuncionarioValeRefeicaoHelper>();
			funcionarios.add(this.funcionarioValeRefeicaoHelperSelecionado);
			CalculoValeRefeicao calculo = this.calculoValeRefeicao.clone();
			calculo.setFuncionariosValeRefeicaoHelper(funcionarios);
			List<CalculoValeRefeicao> listCalculoValeRefeicaoSelecionado = new ArrayList<CalculoValeRefeicao>();
			listCalculoValeRefeicaoSelecionado.add(calculo);
			byte[] relatorio = relatorioRhModulo.getReciboValeRefeicao(listCalculoValeRefeicaoSelecionado);
			super.registraArquivoParaDownload(relatorio, "Recibo Vale Refeicao", "pdf", PotiErpProperties.getInstance().getReportPath());
			
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
			List<CalculoValeRefeicao> listCalculoValeRefeicaoSelecionado = this.listCalculoValeRefeicao.getItensSelecionados();
			validarPagamentosMapa(listCalculoValeRefeicaoSelecionado);
			CalculoValeRefeicao calcRefeicao = listCalculoValeRefeicaoSelecionado.get(0);
			List<PagamentoValeRefeicao> pagamentos = rhModulo.consultarPagamentosValeRefeicaoPorCalculo(calcRefeicao.getId());
			calcRefeicao.setPagamentosValeRefeicao(pagamentos);
			byte[] relatorio = relatorioRhModulo.getMapaValeRefeicao(listCalculoValeRefeicaoSelecionado);
			super.registraArquivoParaDownload(relatorio, "Mapa de Vale Refeicao", "pdf", PotiErpProperties.getInstance().getReportPath());
			checkValeRefeicaoAll = false;
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (Exception e) {
			addMensagemErro(e);
		}
	}
	
	private void validarPagamentosMapa(final List<CalculoValeRefeicao> listCalculoValeRefeicaoSelecionado) throws PotiErpMensagensException {
		if (listCalculoValeRefeicaoSelecionado.isEmpty()) {
			throw new PotiErpMensagensException(MensagensFacesEnum.SELECIONE_PELO_MENOS_UM_CALCULO_PARA_GERAR_MAPA.getKey());
		}
	}
	
	public void doMapa() {
		try {
			validarMapa();
			List<CalculoValeRefeicao> listCalculoValeRefeicao = new ArrayList<CalculoValeRefeicao>();
			listCalculoValeRefeicao.add(calculoValeRefeicao);
			byte[] relatorio = relatorioRhModulo.getMapaValeRefeicao(listCalculoValeRefeicao);
			super.registraArquivoParaDownload(relatorio, "Mapa de Vale Refeicao", "pdf", PotiErpProperties.getInstance().getReportPath());
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (Exception e) {
			addMensagemErro(e);
		}
	}
	
	private void validarMapa() throws PotiErpMensagensException{
		if(calculoValeRefeicao.getSituacao() == null || (!isSituacaoCalculado() && !isSituacaoGravado())){
			throw new PotiErpMensagensException(MensagensFacesEnum.REALIZE_CALCULO_PARA_GERAR_MAPA.getKey());
		}
	}
	
	public void doExibirFuncionario(){
		if(calculoValeRefeicao != null 
				&& calculoValeRefeicao.getTipoCalculoEnum() != null 
				&& calculoValeRefeicao.getTipoCalculoEnum().equals(TipoCalculoValeRefeicaoEnum.FUNCIONARIO)){
			isExibeFuncionario = true;
		}else{
			isExibeFuncionario = false;
		}
	}
	
	public void doExcluir(){
		try{
			List<CalculoValeRefeicao> listCalculoValeRefeicaoSelecionado = this.listCalculoValeRefeicao.getItensSelecionados();
			
			if(listCalculoValeRefeicaoSelecionado != null && !listCalculoValeRefeicaoSelecionado.isEmpty()){
				CalculoValeRefeicao calculo = listCalculoValeRefeicaoSelecionado.get(0);
				if(calculo.getSituacao().equals(SituacaoValeRefeicaoEnum.CALCULADO)){
					rhModulo.excluirCalculoValeRefeicao(calculo);
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
		return calculoValeRefeicao != null && calculoValeRefeicao.getSituacao().equals(SituacaoValeRefeicaoEnum.CALCULADO);
	}
	
	private boolean isSituacaoGravado() {
		return calculoValeRefeicao != null && calculoValeRefeicao.getSituacao().equals(SituacaoValeRefeicaoEnum.GRAVADO);
	}
	
	public void limparForm() throws Exception {
		UIForm form = (UIForm) getFacesContext().getViewRoot().findComponent("formDetails");
		cleanSubmittedValues(form);
		doNovo();
		funcionarioValeRefeicaoHelperSelecionado = new FuncionarioValeRefeicaoHelper();
	}
	
	public boolean filtraMesAno(final Object current) {
		@SuppressWarnings("unchecked")
		CalculoValeRefeicao calculoValeRefeicao = ((SelectionEntity<CalculoValeRefeicao>) current).getEntity();
		String mesAno = calculoValeRefeicao.getReferencia();
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
		CalculoValeRefeicao calculoValeRefeicao = ((SelectionEntity<CalculoValeRefeicao>) current).getEntity();
		Date dataInicio = calculoValeRefeicao.getDataInicio();
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
		CalculoValeRefeicao calculoValeRefeicao = ((SelectionEntity<CalculoValeRefeicao>) current).getEntity();
		Date dataFim = calculoValeRefeicao.getDataFim();
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
		CalculoValeRefeicao calculoValeRefeicao = ((SelectionEntity<CalculoValeRefeicao>) current).getEntity();
		String valorTotal = calculoValeRefeicao.getValorTotal().toString();
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
		CalculoValeRefeicao calculoValeRefeicao = ((SelectionEntity<CalculoValeRefeicao>) current).getEntity();
		String quantidadeTotal = calculoValeRefeicao.getQuantidadeTotal().toString();
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
		CalculoValeRefeicao calculoValeRefeicao = ((SelectionEntity<CalculoValeRefeicao>) current).getEntity();
		String situacao = calculoValeRefeicao.getSituacao().toString();
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
		CalculoValeRefeicao calculoValeRefeicao = ((SelectionEntity<CalculoValeRefeicao>) current).getEntity();
		String tipoCalculo = calculoValeRefeicao.getTipoCalculoEnum().getTipoCalculo();
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
		CalculoValeRefeicao calculoValeRefeicao = ((SelectionEntity<CalculoValeRefeicao>) current).getEntity();
		String username = calculoValeRefeicao.getUsuario().getUsername().toString();
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
	 * @return the calculoValeRefeicao
	 */
	public CalculoValeRefeicao getCalculoValeRefeicao() {
		return calculoValeRefeicao;
	}

	/**
	 * @param calculoValeRefeicao the calculoValeRefeicao to set
	 */
	public void setCalculoValeRefeicao(final CalculoValeRefeicao calculoValeRefeicao) {
		this.calculoValeRefeicao = calculoValeRefeicao;
	}

	/**
	 * @return the listCalculoValeRefeicao
	 */
	public SelectionList<CalculoValeRefeicao> getListCalculoValeRefeicao() {
		return listCalculoValeRefeicao;
	}

	/**
	 * @param listCalculoValeRefeicao the listCalculoValeRefeicao to set
	 */
	public void setListCalculoValeRefeicao(
			final SelectionList<CalculoValeRefeicao> listCalculoValeRefeicao) {
		this.listCalculoValeRefeicao = listCalculoValeRefeicao;
	}
	
	/**
	 * @return the listFuncionarioValeRefeicaoHelper
	 */
	public SelectionList<FuncionarioValeRefeicaoHelper> getListFuncionarioValeRefeicaoHelper() {
		return listFuncionarioValeRefeicaoHelper;
	}

	/**
	 * @param listFuncionarioValeRefeicaoHelper the listFuncionarioValeRefeicaoHelper to set
	 */
	public void setListFuncionarioValeRefeicaoHelper(
			final SelectionList<FuncionarioValeRefeicaoHelper> listFuncionarioValeRefeicaoHelper) {
		this.listFuncionarioValeRefeicaoHelper = listFuncionarioValeRefeicaoHelper;
	}

	/**
	 * @return the listTipoValeRefeicaoHelper
	 */
	public SelectionList<TipoValeRefeicaoHelper> getListTipoValeRefeicaoHelper() {
		return listTipoValeRefeicaoHelper;
	}

	/**
	 * @param listTipoValeRefeicaoHelper the listTipoValeRefeicaoHelper to set
	 */
	public void setListTipoValeRefeicaoHelper(
			final SelectionList<TipoValeRefeicaoHelper> listTipoValeRefeicaoHelper) {
		this.listTipoValeRefeicaoHelper = listTipoValeRefeicaoHelper;
	}

	/**
	 * @return the checkValeRefeicaoAll
	 */
	public boolean isCheckValeRefeicaoAll() {
		return checkValeRefeicaoAll;
	}
	
	/**
	 * @return the funcionarioValeRefeicaoHelperSelecionado
	 */
	public FuncionarioValeRefeicaoHelper getFuncionarioValeRefeicaoHelperSelecionado() {
		return funcionarioValeRefeicaoHelperSelecionado;
	}

	/**
	 * @param funcionarioValeRefeicaoHelperSelecionado the funcionarioValeRefeicaoHelperSelecionado to set
	 */
	public void setFuncionarioValeRefeicaoHelperSelecionado(
			final FuncionarioValeRefeicaoHelper funcionarioValeRefeicaoHelperSelecionado) {
		this.funcionarioValeRefeicaoHelperSelecionado = funcionarioValeRefeicaoHelperSelecionado;
	}

	/**
	 * @param checkValeRefeicaoAll the checkValeRefeicaoAll to set
	 */
	public void setCheckValeRefeicaoAll(final boolean checkValeRefeicaoAll) {
		this.checkValeRefeicaoAll = checkValeRefeicaoAll;
	}
	
	/**
	 * @return the calculoValeRefeicaoSelecionado
	 */
	public CalculoValeRefeicao getCalculoValeRefeicaoSelecionado() {
		return calculoValeRefeicaoSelecionado;
	}

	/**
	 * @param calculoValeRefeicaoSelecionado the calculoValeRefeicaoSelecionado to set
	 */
	public void setCalculoValeRefeicaoSelecionado(
			final CalculoValeRefeicao calculoValeRefeicaoSelecionado) {
		this.calculoValeRefeicaoSelecionado = calculoValeRefeicaoSelecionado;
	}
	
	public List<SelectItem> getItensTipoCalculo() {
		List<TipoCalculoValeRefeicaoEnum> listTipoCalculo = getListTipoCalculo();
		if(listTipoCalculo != null){
			itensTipoCalculo.clear();
			for(TipoCalculoValeRefeicaoEnum tipoCalculo : listTipoCalculo){
				SelectItem item = new SelectItem(tipoCalculo, tipoCalculo.getTipoCalculo());
				itensTipoCalculo.add(item);
			}
		}
		return itensTipoCalculo;
	}
	
	public List<TipoCalculoValeRefeicaoEnum> getListTipoCalculo() {
		if(itensTipoCalculo.isEmpty()){ 
			return new ArrayList<TipoCalculoValeRefeicaoEnum>(Arrays.asList(TipoCalculoValeRefeicaoEnum.values()));
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

	@Override
	public Logger getLogger() {
		return log;
	}
	
	public boolean isIncluirValeRefeicao(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isAlterarValeRefeicao(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_VALE_TRANSPORTE.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isExcluirValeRefeicao(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isConsultarValeRefeicao(){
		return isIncluirValeRefeicao() || isExcluirValeRefeicao() || isAlterarValeRefeicao() || isConsultar();
	}
	
	public boolean isManterValeRefeicao(){
		return isIncluirValeRefeicao() || isAlterarValeRefeicao();
	}
	
	public String doCadastroDeValeRefeicao(){
		doConsultar();
		return NavigationEnum.CALCULO_DE_VALEREFEICAO.getValor();
	}
}
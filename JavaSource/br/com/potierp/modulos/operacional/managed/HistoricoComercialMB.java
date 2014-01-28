package br.com.potierp.modulos.operacional.managed;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import br.com.potierp.business.comercial.facade.ComercialModulo;
import br.com.potierp.business.operacional.comparator.NomeClienteComparator;
import br.com.potierp.faces.converter.MockEnum;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.exception.PotiErpMensagensException;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.model.Cliente;
import br.com.potierp.model.HistoricoComercial;
import br.com.potierp.model.MesEnum;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class HistoricoComercialMB extends BaseMB {
	
	private static Logger log = Logger.getLogger(HistoricoComercialMB.class);
	
	private static final BigDecimal ZERO = new BigDecimal(0);
	
	@EJB
	private ComercialModulo moduloComercial;
	
	private HistoricoComercial historicoComercial;
	
	private SelectionList<HistoricoComercial> listHistoricoComercial = new SelectionList<HistoricoComercial>();
	
	private boolean checkHistoricoComercialAll = false;
	
	private Integer scrollerPage = 1;
	
	private List<Cliente> sugestoesClientes;
	
	private Cliente cliente = new Cliente();
	
	private Cliente clienteSelecionado = new Cliente();
	
	private List<SelectItem> itensMes = new ArrayList<SelectItem>();
	
	@Override
	public Logger getLogger() {
		return log;
	}
	
	public HistoricoComercial getHistoricoComercial() {
		if(this.historicoComercial.getCliente() == null) {
			this.historicoComercial.setCliente(new Cliente());
		}
		return historicoComercial;
	}

	public void setHistoricoComercial(final HistoricoComercial historicoComercial) {
		this.historicoComercial = historicoComercial;
	}
	
	public SelectionList<HistoricoComercial> getListHistoricoComercial() {
		return listHistoricoComercial;
	}

	public void setListHistoricoComercial(final SelectionList<HistoricoComercial> listHistoricoComercial) {
		this.listHistoricoComercial = listHistoricoComercial;
	}

	public boolean isCheckHistoricoComercialAll() {
		return checkHistoricoComercialAll;
	}

	public void setCheckHistoricoComercialAll(final boolean checkHistoricoComercialAll) {
		this.checkHistoricoComercialAll = checkHistoricoComercialAll;
	}
	
	public Integer getScrollerPage() {
		return scrollerPage;
	}

	public void setScrollerPage(final Integer scrollerPage) {
		this.scrollerPage = scrollerPage;
	}
	
	public List<Cliente> getSugestoesClientes() {
		return sugestoesClientes;
	}

	public void setSugestoesClientes(final List<Cliente> sugestoesClientes) {
		this.sugestoesClientes = sugestoesClientes;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(final Cliente cliente) {
		this.cliente = cliente;
	}

	public Cliente getClienteSelecionado() {
		return clienteSelecionado;
	}

	public void setClienteSelecionado(final Cliente clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
	}

	public String doCadastroDeHistoricoComercial(){
		doConsultar();
		doNovo();
		return NavigationEnum.HISTORICO_COMERCIAL.getValor();
	}
	
	public boolean isIncluirHistoricoComercial(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_HISTORICO_COMERCIAL.getCodigo());
	}
	
	public boolean isAlterarHistoricoComercial(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_HISTORICO_COMERCIAL.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_HISTORICO_COMERCIAL.getCodigo());
	}
	
	public boolean isExcluirHistoricoComercial(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_HISTORICO_COMERCIAL.getCodigo());
	}
	
	public boolean isConsultarHistoricoComercial(){
		return isIncluirHistoricoComercial() || isExcluirHistoricoComercial() || isAlterarHistoricoComercial() || isConsultar();
	}
	
	public boolean isManterHistoricoComercial(){
		return isIncluirHistoricoComercial() || isAlterarHistoricoComercial();
	}
	
	public void doConsultar(){
		try {
			List<HistoricoComercial> list = moduloComercial.consultarTodos();
			listHistoricoComercial = new SelectionList<HistoricoComercial>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public void doNovo() {
		this.historicoComercial = new HistoricoComercial();
		this.checkHistoricoComercialAll = false;
		this.cliente = new Cliente();
		this.clienteSelecionado = new Cliente();
	}
	
	public void doExcluir(){
		try {
			moduloComercial.excluir(historicoComercial);
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
	
	public void doSalvar(){
		try {
			if(podeSalvarHistoricoComercial()){
				moduloComercial.salvar(historicoComercial);
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
	
	private boolean podeSalvarHistoricoComercial() throws PotiErpException{
		return validaCliente() && validaMes() && validaAno() && validaValorNFe() && validaRetencao() && 
				validaRetencaoISS() && validaAbatimentos() && validaTotalDeducoes() && validaTotalLiquidoNFe();
	}
	
	public boolean validaValorNFe() throws PotiErpException{
		if(this.historicoComercial.getValorNFe() != null && this.historicoComercial.getValorNFe().compareTo(ZERO) > 0){
			return true;
		}
		throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {getMensagem("valorNFe")});
	}

	public boolean validaCliente() throws PotiErpException{
		if(this.historicoComercial.getCliente() != null && this.historicoComercial.getCliente().getId() != null){
			return true;
		}
		throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {getMensagem("cliente")});
	}
	
	public boolean validaMes() throws PotiErpException{
		if(this.historicoComercial.getMes() != null){
			return true;
		}
		throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {getMensagem("mes")});
	}
	
	public boolean validaAno() throws PotiErpException{
		if(this.historicoComercial.getAno() != null && this.historicoComercial.getAno().intValue() > 0){
			return true;
		}
		throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {getMensagem("ano")});
	}
	
	public boolean validaRetencao() throws PotiErpException{
		if(this.historicoComercial.getRetencao() != null){
			return true;
		}
		throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {getMensagem("retencao")});
	}
	
	public boolean validaRetencaoISS() throws PotiErpException{
		if(this.historicoComercial.getRetencaoISS() != null){
			return true;
		}
		throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {getMensagem("retencaoISS")});
	}
	
	public boolean validaAbatimentos() throws PotiErpException{
		if(this.historicoComercial.getAbatimentos() != null){
			return true;
		}
		throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {getMensagem("abatimentos")});
	}
	
	public boolean validaTotalDeducoes() throws PotiErpException{
		if(this.historicoComercial.getTotalDeducoes() != null){
			return true;
		}
		throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {getMensagem("totalDeducoes")});
	}
	
	public boolean validaTotalLiquidoNFe() throws PotiErpException{
		if(this.historicoComercial.getTotalLiquidoNFe() != null && this.historicoComercial.getTotalLiquidoNFe().compareTo(ZERO) > 0){
			return true;
		}
		throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {getMensagem("totalLiquidoNFe")});
	}
	
	public void doExcluirLote(){
		try {
			List<HistoricoComercial> list = listHistoricoComercial.getItensSelecionados();
			moduloComercial.excluir(list);
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
	
	public void buscarCliente() {
		try {
			if(isCodigoClientePreenchido()) {
				if(this.sugestoesClientes == null) {
					carregarSugestoes();
				}
				this.cliente = getClientePorCodigo();
				if(this.cliente == null) {
					addMensagemErro(MensagensFacesEnum.ERRO_CLIENTE_NAO_ENCONTRADO);
					this.cliente = new Cliente();
				}
			} else {
				this.cliente = new Cliente();
			}
			this.historicoComercial.setCliente(this.cliente);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	private boolean isCodigoClientePreenchido() {
		return this.historicoComercial.getCliente() != null &&
				this.historicoComercial.getCliente().getCodigo() != null &&
				!this.historicoComercial.getCliente().getCodigo().equals("");
	}
	
	private Cliente getClientePorCodigo() throws Exception {
		if(this.sugestoesClientes != null) {
			for(Cliente cliente : this.sugestoesClientes) {
				if(cliente.getCodigo() != null && cliente.getCodigo().equals(this.historicoComercial.getCliente().getCodigo())) {
					Cliente clienteTemp = cliente.clone();
					return clienteTemp;
				}
			}
		}
		return null;
	}
	
	public void selecionarCliente() throws Exception {
		this.cliente = this.clienteSelecionado.clone();
		this.clienteSelecionado = new Cliente();
		this.historicoComercial.setCliente(this.cliente);
	}
	
	private void carregarSugestoes() throws PotiErpException {
		this.sugestoesClientes = moduloComercial.consultarTodosClientesComNomeFantasiaCodigo();
	}
	
	public List<Cliente> sugestao(final Object evento) throws Exception {
		if(this.sugestoesClientes == null) {
			carregarSugestoes();
		}
		List<Cliente> clientes = new ArrayList<Cliente>();
		try {
			String prefixo = evento.toString().trim().toLowerCase();
			for(Cliente cliente : this.sugestoesClientes) {
				if(cliente.getNomeFantasia().trim().toLowerCase().startsWith(prefixo)) {
					clientes.add(cliente);
				}
			}
		} catch (Exception e) {
			addMensagemErro(e.getMessage());
		}
		Collections.sort(clientes, new NomeClienteComparator());
		return clientes;
	}
	
	public List<SelectItem> getItensMes() {
		if(itensMes == null || itensMes.isEmpty()){
			carregarItensMes();
		}
		return itensMes;
	}

	private void carregarItensMes() {
		MesEnum[] meses = MesEnum.values();
		itensMes.clear();
		addMockSimple(itensMes, MockEnum.SELECIONE);
		for(MesEnum mes : meses) {
			SelectItem item = new SelectItem(mes.getCodigo(), mes.getMes());
			itensMes.add(item);
		}
	}

	public void setItensMes(final List<SelectItem> itensMes) {
		this.itensMes = itensMes;
	}
	
	public void doAlterar() {
		try {
			this.historicoComercial = this.historicoComercial.clone();
			this.cliente = historicoComercial.getCliente();
			this.clienteSelecionado = historicoComercial.getCliente();
		} catch (CloneNotSupportedException e) {
			addMensagemErro(MensagensFacesEnum.ERRO_ALTERAR);
		}
	}

}
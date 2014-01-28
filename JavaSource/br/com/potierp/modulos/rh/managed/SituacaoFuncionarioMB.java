package br.com.potierp.modulos.rh.managed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import br.com.potierp.business.rh.facade.RhModulo;
import br.com.potierp.faces.converter.MockEnum;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.exception.PotiErpMensagensException;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.model.CorEnum;
import br.com.potierp.model.SituacaoFuncionario;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class SituacaoFuncionarioMB extends BaseMB{

	private static Logger log = Logger.getLogger(SituacaoFuncionarioMB.class);
	
	private SituacaoFuncionario situacaoFuncionario = new SituacaoFuncionario();
	
	private SelectionList<SituacaoFuncionario> listSituacaoFuncionario = new SelectionList<SituacaoFuncionario>();
	
	private List<SelectItem> itensCor = new ArrayList<SelectItem>();
	
	@EJB
	private RhModulo rhModulo;
	
	private boolean checkSituacaoFuncionarioAll = false;
	
	private Integer scrollerPage = 1;
	
	public SituacaoFuncionarioMB(){
	}
	
	public void doNovo(){
		situacaoFuncionario = new SituacaoFuncionario();
		checkSituacaoFuncionarioAll = false;
	}
	
	public void doSalvar(){
		try {
			rhModulo.salvarSituacaoFuncionario(situacaoFuncionario);
			doNovo();
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_SALVAR);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public void doExcluir(){
		try {
			rhModulo.excluirSituacaoFuncionario(situacaoFuncionario);
			doNovo();
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_SALVAR);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public void doExcluirLote(){
		try {
			List<SituacaoFuncionario> list = listSituacaoFuncionario.getItensSelecionados();
			rhModulo.excluirListaSituacaoFuncionario(list);
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
			List<SituacaoFuncionario> list = rhModulo.consultarTodasSituacoesFuncionario();
			listSituacaoFuncionario = new SelectionList<SituacaoFuncionario>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public SituacaoFuncionario getSituacaoFuncionario() {
		return situacaoFuncionario;
	}

	public void setSituacaoFuncionario(final SituacaoFuncionario situacaoFuncionario) {
		this.situacaoFuncionario = situacaoFuncionario;
	}

	public SelectionList<SituacaoFuncionario> getListSituacaoFuncionario() {
		doConsultar();
		return listSituacaoFuncionario;
	}

	public void setListSituacaoFuncionario(final SelectionList<SituacaoFuncionario> listSituacaoFuncionario) {
		this.listSituacaoFuncionario = listSituacaoFuncionario;
	}

	public boolean isCheckSituacaoFuncionarioAll() {
		return checkSituacaoFuncionarioAll;
	}

	public void setCheckSituacaoFuncionarioAll(final boolean checkSituacaoFuncionarioAll) {
		this.checkSituacaoFuncionarioAll = checkSituacaoFuncionarioAll;
	}
	
	public List<SelectItem> getItensCor() {
		List<CorEnum> listCor = getListCor();
		if(listCor != null){
			itensCor.clear();
			addMockSimple(itensCor, MockEnum.SELECIONE);
			for(CorEnum cor : listCor){
				SelectItem item = new SelectItem(cor, cor.getNome());
				itensCor.add(item);
			}
		}
		return itensCor;
	}
	
	public List<CorEnum> getListCor() {
		if(itensCor.isEmpty()){ 
			return new ArrayList<CorEnum>(Arrays.asList(CorEnum.values()));
		}
		return null;
	}

	public void setItensCor(final List<SelectItem> itensCor) {
		this.itensCor = itensCor;
	}
	
	public Integer getScrollerPage() {
		return scrollerPage;
	}

	public void setScrollerPage(final Integer scrollerPage) {
		this.scrollerPage = scrollerPage;
	}

	@Override
	public Logger getLogger() {
		return log;
	}
	
	public boolean isIncluirSituacaoFuncionario(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_SITUACAOFUNCIONARIO.getCodigo());
	}
	
	public boolean isAlterarSituacaoFuncionario(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_SITUACAOFUNCIONARIO.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_SITUACAOFUNCIONARIO.getCodigo());
	}
	
	public boolean isExcluirSituacaoFuncionario(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_SITUACAOFUNCIONARIO.getCodigo());
	}
	
	public boolean isConsultarSituacaoFuncionario(){
		return isIncluirSituacaoFuncionario() || isExcluirSituacaoFuncionario() || isAlterarSituacaoFuncionario() || isConsultar();
	}
	
	public boolean isManterSituacaoFuncionario(){
		return isIncluirSituacaoFuncionario() || isAlterarSituacaoFuncionario();
	}
	
	public String doCadastroDeSituacaoFuncionario(){
		return NavigationEnum.CADASTRO_DE_SITUACAOFUNCIONARIO.getValor();
	}
}
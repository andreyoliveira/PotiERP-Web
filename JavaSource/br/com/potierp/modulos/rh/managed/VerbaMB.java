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
import br.com.potierp.model.BaseVerbasEnum;
import br.com.potierp.model.SituacaoEnum;
import br.com.potierp.model.TipoVerbaEnum;
import br.com.potierp.model.Verba;
import br.com.potierp.model.VerbasSobreEnum;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class VerbaMB extends BaseMB{

	private static Logger log = Logger.getLogger(VerbaMB.class);

	private Verba verba = new Verba();
	
	private SelectionList<Verba> listVerba = new SelectionList<Verba>();
	
	private List<SelectItem> itensBaseVerbas = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensVerbasSobre = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensTipo = new ArrayList<SelectItem>();
	
	@EJB
	private RhModulo rhModulo;
	
	private boolean checkVerbaAll = false;
	
	private Integer scrollerPage = 1;
	
	public VerbaMB(){
	}
	
	public void doNovo(){
		verba = new Verba();
		verba.setSituacao(SituacaoEnum.ATIVO);
		checkVerbaAll = false;
	}
	
	public void doSalvar(){
		try {
			rhModulo.salvarVerba(verba);
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
	
	public void doExcluir(){
		try {
			rhModulo.excluirVerba(verba);
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
	
	public void doExcluirLote(){
		try {
			List<Verba> list = listVerba.getItensSelecionados();
			rhModulo.excluirListaVerba(list);
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
			List<Verba> list = rhModulo.consultarTodasVerbas();
			listVerba = new SelectionList<Verba>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public Verba getVerba() {
		return verba;
	}

	public void setVerba(final Verba verba) {
		this.verba = verba;
	}

	public SelectionList<Verba> getListVerba() {
		return listVerba;
	}

	public void setListVerba(final SelectionList<Verba> listVerba) {
		this.listVerba = listVerba;
	}
	
	public List<SelectItem> getItensBaseVerbas() {
		List<BaseVerbasEnum> listBaseVerbasEnum = getListBaseVerbasEnum();
		if(listBaseVerbasEnum != null){
			itensBaseVerbas.clear();
			addMockSimple(itensBaseVerbas, MockEnum.SELECIONE);
			for(BaseVerbasEnum baseVerba : listBaseVerbasEnum){
				SelectItem item = new SelectItem(baseVerba, baseVerba.getBase());
				itensBaseVerbas.add(item);
			}
		}
		return itensBaseVerbas;
	}
	
	public List<BaseVerbasEnum> getListBaseVerbasEnum() {
		if(itensBaseVerbas.isEmpty()){ 
			return new ArrayList<BaseVerbasEnum>(Arrays.asList(BaseVerbasEnum.values()));
		}
		return null;
	}

	public void setItensBaseVerbas(final List<SelectItem> itensBaseVerbas) {
		this.itensBaseVerbas = itensBaseVerbas;
	}
	
	public List<SelectItem> getItensVerbasSobre() {
		List<VerbasSobreEnum> listVerbasSobreEnum = getListVerbasSobreEnum();
		if(listVerbasSobreEnum != null){
			itensVerbasSobre.clear();
			addMockSimple(itensVerbasSobre, MockEnum.SELECIONE);
			for(VerbasSobreEnum verbasSobre : listVerbasSobreEnum){
				SelectItem item = new SelectItem(verbasSobre, verbasSobre.getSobre());
				itensVerbasSobre.add(item);
			}
		}
		return itensVerbasSobre;
	}
	
	public List<VerbasSobreEnum> getListVerbasSobreEnum() {
		if(itensVerbasSobre.isEmpty()){ 
			return new ArrayList<VerbasSobreEnum>(Arrays.asList(VerbasSobreEnum.values()));
		}
		return null;
	}

	public void setItensVerbasSobre(final List<SelectItem> itensVerbasSobre) {
		this.itensVerbasSobre = itensVerbasSobre;
	}
	
	/**
	 * @return the itensTipo
	 */
	public List<SelectItem> getItensTipo() {
		List<TipoVerbaEnum> listTipoVerbaEnum = getListTipoVerbaEnum();
		if(listTipoVerbaEnum != null){
			itensTipo.clear();
			addMockSimple(itensTipo, MockEnum.SELECIONE);
			for(TipoVerbaEnum tipoVerba : listTipoVerbaEnum){
				SelectItem item = new SelectItem(tipoVerba, tipoVerba.getNome());
				itensTipo.add(item);
			}
		}
		return itensTipo;
	}
	
	public List<TipoVerbaEnum> getListTipoVerbaEnum() {
		if(itensTipo.isEmpty()){ 
			return new ArrayList<TipoVerbaEnum>(Arrays.asList(TipoVerbaEnum.values()));
		}
		return null;
	}

	/**
	 * @param itensTipo the itensTipo to set
	 */
	public void setItensTipo(final List<SelectItem> itensTipo) {
		this.itensTipo = itensTipo;
	}

	public boolean isCheckVerbaAll() {
		return checkVerbaAll;
	}

	public void setCheckVerbaAll(final boolean checkVerbaAll) {
		this.checkVerbaAll = checkVerbaAll;
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
	
	public boolean isIncluirVerba(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_TIPO_VALE_REFEICAO.getCodigo());
	}
	
	public boolean isAlterarVerba(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_TIPO_VALE_REFEICAO.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_TIPO_VALE_REFEICAO.getCodigo());
	}
	
	public boolean isExcluirVerba(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_TIPO_VALE_REFEICAO.getCodigo());
	}
	
	public boolean isConsultarVerba(){
		return isIncluirVerba() || isExcluirVerba() || isAlterarVerba() || isConsultar();
	}
	
	public boolean isManterVerba(){
		return isIncluirVerba() || isAlterarVerba();
	}
	
	public String doCadastroDeVerba(){
		doConsultar();
		return NavigationEnum.CADASTRO_DE_VERBA.getValor();
	}
}
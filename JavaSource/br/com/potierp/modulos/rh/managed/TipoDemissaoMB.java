package br.com.potierp.modulos.rh.managed;

import java.util.List;

import javax.ejb.EJB;

import org.apache.log4j.Logger;

import br.com.potierp.business.rh.facade.RhModulo;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.exception.PotiErpMensagensException;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.model.TipoDemissao;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class TipoDemissaoMB extends BaseMB{

	private static Logger log = Logger.getLogger(TipoDemissaoMB.class);
	
	private TipoDemissao tipoDemissao = new TipoDemissao();
	
	private SelectionList<TipoDemissao> listTipoDemissao = new SelectionList<TipoDemissao>();
	
	@EJB
	private RhModulo rhModulo;
	
	private boolean checkTipoDemissaoAll = false;
	
	private Integer scrollerPage = 1;
	
	public TipoDemissaoMB(){
	}
	
	public void doNovo(){
		tipoDemissao = new TipoDemissao();
		checkTipoDemissaoAll = false;
	}
	
	public void doSalvar(){
		try {
			rhModulo.salvarTipoDemissao(tipoDemissao);
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
			rhModulo.excluirTipoDemissao(tipoDemissao);
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
			List<TipoDemissao> list = listTipoDemissao.getItensSelecionados();
			rhModulo.excluirListaTipoDemissao(list);
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
			List<TipoDemissao> list = rhModulo.consultarTodosTiposDemissoes();
			listTipoDemissao = new SelectionList<TipoDemissao>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public TipoDemissao getTipoDemissao() {
		return tipoDemissao;
	}

	public void setTipoDemissao(TipoDemissao tipoDemissao) {
		this.tipoDemissao = tipoDemissao;
	}

	public SelectionList<TipoDemissao> getListTipoDemissao() {
		doConsultar();
		return listTipoDemissao;
	}

	public void setListTipoDemissao(SelectionList<TipoDemissao> listTipoDemissao) {
		this.listTipoDemissao = listTipoDemissao;
	}

	public boolean isCheckTipoDemissaoAll() {
		return checkTipoDemissaoAll;
	}

	public void setCheckTipoDemissaoAll(boolean checkTipoDemissaoAll) {
		this.checkTipoDemissaoAll = checkTipoDemissaoAll;
	}
	
	public Integer getScrollerPage() {
		return scrollerPage;
	}

	public void setScrollerPage(Integer scrollerPage) {
		this.scrollerPage = scrollerPage;
	}

	@Override
	public Logger getLogger() {
		return log;
	}
	
	public boolean isIncluirTipoDemissao(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_TIPO_DEMISSAO.getCodigo());
	}
	
	public boolean isAlterarTipoDemissao(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_TIPO_DEMISSAO.getCodigo());
	}
	
	public boolean isExcluirTipoDemissao(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_TIPO_DEMISSAO.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_TIPO_DEMISSAO.getCodigo());
	}
	
	public boolean isConsultarTipoDemissao(){
		return isIncluirTipoDemissao() || isAlterarTipoDemissao() || isExcluirTipoDemissao() || isConsultar();
	}
	
	public boolean isManterTipoDemissao(){
		return isIncluirTipoDemissao() || isAlterarTipoDemissao();
	}
	
	public String doCadastroDeTipoDemissao(){
		return NavigationEnum.CADASTRO_DE_TIPO_DE_DEMISSAO.getValor();
	}
	
}
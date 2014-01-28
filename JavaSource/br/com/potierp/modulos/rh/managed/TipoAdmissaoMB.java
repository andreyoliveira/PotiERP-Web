package br.com.potierp.modulos.rh.managed;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;

import org.apache.log4j.Logger;

import br.com.potierp.business.rh.facade.RhModulo;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.exception.PotiErpMensagensException;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.model.TipoAdmissao;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class TipoAdmissaoMB extends BaseMB implements Serializable{

	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(TipoAdmissaoMB.class);
	
	private TipoAdmissao tipoAdmissao = new TipoAdmissao();
	
	private SelectionList<TipoAdmissao> listTipoAdmissao = new SelectionList<TipoAdmissao>();
	
	@EJB
	private RhModulo rhModulo;
	
	private boolean checkTipoAdmissaoAll = false;
	
	private Integer scrollerPage = 1;
	
	public TipoAdmissaoMB(){
	}
	
	public void doNovo(){
		tipoAdmissao = new TipoAdmissao();
		checkTipoAdmissaoAll = false;
	}
	
	public void doSalvar(){
		try {
			rhModulo.salvarTipoAdmissao(tipoAdmissao);
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
			rhModulo.excluirTipoAdmissao(tipoAdmissao);
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
			List<TipoAdmissao> list = listTipoAdmissao.getItensSelecionados();
			rhModulo.excluirListaTipoAdmissao(list);
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
			List<TipoAdmissao> list = rhModulo.consultarTodosTiposAdmissoes();
			listTipoAdmissao = new SelectionList<TipoAdmissao>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public TipoAdmissao getTipoAdmissao() {
		return tipoAdmissao;
	}

	public void setTipoAdmissao(TipoAdmissao tipoAdmissao) {
		this.tipoAdmissao = tipoAdmissao;
	}

	public SelectionList<TipoAdmissao> getListTipoAdmissao() {
		doConsultar();
		return listTipoAdmissao;
	}

	public void setListTipoAdmissao(SelectionList<TipoAdmissao> listTipoAdmissao) {
		this.listTipoAdmissao = listTipoAdmissao;
	}

	public boolean isCheckTipoAdmissaoAll() {
		return checkTipoAdmissaoAll;
	}

	public void setCheckTipoAdmissaoAll(boolean checkTipoAdmissaoAll) {
		this.checkTipoAdmissaoAll = checkTipoAdmissaoAll;
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
	
	public boolean isIncluirTipoAdmissao(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_TIPOADMISSAO.getCodigo());
	}
	
	public boolean isAlterarTipoAdmissao(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_TIPOADMISSAO.getCodigo());
	}

	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_TIPOADMISSAO.getCodigo());
	}
	
	public boolean isExcluirTipoAdmissao(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_TIPOADMISSAO.getCodigo());
	}
	
	public boolean isConsultarTipoAdmissao(){
		return isIncluirTipoAdmissao() || isExcluirTipoAdmissao() || isAlterarTipoAdmissao() || isConsultar();
	}
	
	public boolean isManterTipoAdmissao(){
		return isIncluirTipoAdmissao() || isAlterarTipoAdmissao();
	}
	
	public String doCadastroDeTipoAdmissao(){
		return NavigationEnum.CADASTRO_DE_TIPOADMISSAO.getValor();
	}
}
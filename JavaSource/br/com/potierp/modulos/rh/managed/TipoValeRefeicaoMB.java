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
import br.com.potierp.model.SituacaoEnum;
import br.com.potierp.model.TipoValeRefeicao;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class TipoValeRefeicaoMB extends BaseMB{

	private static Logger log = Logger.getLogger(TipoValeRefeicaoMB.class);

	private TipoValeRefeicao tipoValeRefeicao = new TipoValeRefeicao();
	
	private SelectionList<TipoValeRefeicao> listTipoValeRefeicao = new SelectionList<TipoValeRefeicao>();
	
	@EJB
	private RhModulo rhModulo;
	
	private boolean checkTipoValeRefeicaoAll = false;
	
	private Integer scrollerPage = 1;
	
	public TipoValeRefeicaoMB(){
	}
	
	public void doNovo(){
		tipoValeRefeicao = new TipoValeRefeicao();
		tipoValeRefeicao.setSituacao(SituacaoEnum.ATIVO);
		checkTipoValeRefeicaoAll = false;
	}
	
	public void doSalvar(){
		try {
			rhModulo.salvarTipoValeRefeicao(tipoValeRefeicao);
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
			rhModulo.excluirTipoValeRefeicao(tipoValeRefeicao);
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
			List<TipoValeRefeicao> list = listTipoValeRefeicao.getItensSelecionados();
			rhModulo.excluirListaTipoValeRefeicao(list);
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
			List<TipoValeRefeicao> list = rhModulo.consultarTodosTiposValeRefeicao();
			listTipoValeRefeicao = new SelectionList<TipoValeRefeicao>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public TipoValeRefeicao getTipoValeRefeicao() {
		return tipoValeRefeicao;
	}

	public void setTipoValeRefeicao(final TipoValeRefeicao tipoValeRefeicao) {
		this.tipoValeRefeicao = tipoValeRefeicao;
	}

	public SelectionList<TipoValeRefeicao> getListTipoValeRefeicao() {
		return listTipoValeRefeicao;
	}

	public void setListTipoValeRefeicao(final SelectionList<TipoValeRefeicao> listTipoValeRefeicao) {
		this.listTipoValeRefeicao = listTipoValeRefeicao;
	}

	public boolean isCheckTipoValeRefeicaoAll() {
		return checkTipoValeRefeicaoAll;
	}

	public void setCheckTipoValeRefeicaoAll(final boolean checkTipoValeRefeicaoAll) {
		this.checkTipoValeRefeicaoAll = checkTipoValeRefeicaoAll;
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
	
	public boolean isIncluirTipoValeRefeicao(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_TIPO_VALE_REFEICAO.getCodigo());
	}
	
	public boolean isAlterarTipoValeRefeicao(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_TIPO_VALE_REFEICAO.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_TIPO_VALE_REFEICAO.getCodigo());
	}
	
	public boolean isExcluirTipoValeRefeicao(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_TIPO_VALE_REFEICAO.getCodigo());
	}
	
	public boolean isConsultarTipoValeRefeicao(){
		return isIncluirTipoValeRefeicao() || isExcluirTipoValeRefeicao() || isAlterarTipoValeRefeicao() || isConsultar();
	}
	
	public boolean isManterTipoValeRefeicao(){
		return isIncluirTipoValeRefeicao() || isAlterarTipoValeRefeicao();
	}
	
	public String doCadastroDeTipoValeRefeicao(){
		doConsultar();
		return NavigationEnum.CADASTRO_DE_TIPOVALEREFEICAO.getValor();
	}
}
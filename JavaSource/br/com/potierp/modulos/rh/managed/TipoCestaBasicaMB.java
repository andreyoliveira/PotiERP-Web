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
import br.com.potierp.model.TipoCestaBasica;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class TipoCestaBasicaMB extends BaseMB{

	private static Logger log = Logger.getLogger(TipoCestaBasicaMB.class);

	private TipoCestaBasica tipoCestaBasica = new TipoCestaBasica();
	
	private SelectionList<TipoCestaBasica> listTipoCestaBasica = new SelectionList<TipoCestaBasica>();
	
	@EJB
	private RhModulo rhModulo;
	
	private boolean checkTipoCestaBasicaAll = false;
	
	private Integer scrollerPage = 1;
	
	public TipoCestaBasicaMB(){
	}
	
	public void doNovo(){
		tipoCestaBasica = new TipoCestaBasica();
		tipoCestaBasica.setSituacao(SituacaoEnum.ATIVO);
		checkTipoCestaBasicaAll = false;
	}
	
	public void doSalvar(){
		try {
			rhModulo.salvarTipoCestaBasica(tipoCestaBasica);
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
			rhModulo.excluirTipoCestaBasica(tipoCestaBasica);
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
			List<TipoCestaBasica> list = listTipoCestaBasica.getItensSelecionados();
			rhModulo.excluirListaTipoCestaBasica(list);
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
			List<TipoCestaBasica> list = rhModulo.consultarTodosTiposCestaBasica();
			listTipoCestaBasica = new SelectionList<TipoCestaBasica>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public TipoCestaBasica getTipoCestaBasica() {
		return tipoCestaBasica;
	}

	public void setTipoCestaBasica(final TipoCestaBasica tipoCestaBasica) {
		this.tipoCestaBasica = tipoCestaBasica;
	}

	public SelectionList<TipoCestaBasica> getListTipoCestaBasica() {
		return listTipoCestaBasica;
	}

	public void setListTipoCestaBasica(final SelectionList<TipoCestaBasica> listTipoCestaBasica) {
		this.listTipoCestaBasica = listTipoCestaBasica;
	}

	public boolean isCheckTipoCestaBasicaAll() {
		return checkTipoCestaBasicaAll;
	}

	public void setCheckTipoCestaBasicaAll(final boolean checkTipoCestaBasicaAll) {
		this.checkTipoCestaBasicaAll = checkTipoCestaBasicaAll;
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
	
	public boolean isIncluirTipoCestaBasica(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_TIPO_VALE_REFEICAO.getCodigo());
	}
	
	public boolean isAlterarTipoCestaBasica(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_TIPO_VALE_REFEICAO.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_TIPO_VALE_REFEICAO.getCodigo());
	}
	
	public boolean isExcluirTipoCestaBasica(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_TIPO_VALE_REFEICAO.getCodigo());
	}
	
	public boolean isConsultarTipoCestaBasica(){
		return isIncluirTipoCestaBasica() || isExcluirTipoCestaBasica() || isAlterarTipoCestaBasica() || isConsultar();
	}
	
	public boolean isManterTipoCestaBasica(){
		return isIncluirTipoCestaBasica() || isAlterarTipoCestaBasica();
	}
	
	public String doCadastroDeTipoCestaBasica(){
		doConsultar();
		return NavigationEnum.CADASTRO_DE_TIPOCESTABASICA.getValor();
	}
}
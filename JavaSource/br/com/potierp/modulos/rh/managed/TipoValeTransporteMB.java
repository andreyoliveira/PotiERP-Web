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
import br.com.potierp.model.SituacaoEnum;
import br.com.potierp.model.TipoValeTransporte;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class TipoValeTransporteMB extends BaseMB implements Serializable{

	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(TipoValeTransporteMB.class);

	private TipoValeTransporte tipoValeTransporte = new TipoValeTransporte();
	
	private SelectionList<TipoValeTransporte> listTipoValeTransporte = new SelectionList<TipoValeTransporte>();
	
	@EJB
	private RhModulo rhModulo;
	
	private boolean checkTipoValeTransporteAll = false;
	
	private Integer scrollerPage = 1;
	
	public TipoValeTransporteMB(){
	}
	
	public void doNovo(){
		tipoValeTransporte = new TipoValeTransporte();
		tipoValeTransporte.setSituacao(SituacaoEnum.ATIVO);
		checkTipoValeTransporteAll = false;
	}
	
	public void doSalvar(){
		try {
			rhModulo.salvarTipoValeTransporte(tipoValeTransporte);
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
			rhModulo.excluirTipoValeTransporte(tipoValeTransporte);
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
			List<TipoValeTransporte> list = listTipoValeTransporte.getItensSelecionados();
			rhModulo.excluirListaTipoValeTransporte(list);
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
			List<TipoValeTransporte> list = rhModulo.consultarTodosTiposValeTransporte();
			listTipoValeTransporte = new SelectionList<TipoValeTransporte>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}

	public TipoValeTransporte getTipoValeTransporte() {
		return tipoValeTransporte;
	}

	public void setTipoValeTransporte(final TipoValeTransporte tipoValeTransporte) {
		this.tipoValeTransporte = tipoValeTransporte;
	}

	public SelectionList<TipoValeTransporte> getListTipoValeTransporte() {
		return listTipoValeTransporte;
	}

	public void setListTipoValeTransporte(final SelectionList<TipoValeTransporte> listTipoValeTransporte) {
		this.listTipoValeTransporte = listTipoValeTransporte;
	}

	public boolean isCheckTipoValeTransporteAll() {
		return checkTipoValeTransporteAll;
	}

	public void setCheckTipoValeTransporteAll(final boolean checkTipoValeTransporteAll) {
		this.checkTipoValeTransporteAll = checkTipoValeTransporteAll;
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
	
	public boolean isIncluirTipoValeTransporte(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_TIPO_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isAlterarTipoValeTransporte(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_TIPO_VALE_TRANSPORTE.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_TIPO_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isExcluirTipoValeTransporte(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_TIPO_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isConsultarTipoValeTransporte(){
		return isIncluirTipoValeTransporte() || isExcluirTipoValeTransporte() || isAlterarTipoValeTransporte() || isConsultar();
	}
	
	public boolean isManterTipoValeTransporte(){
		return isIncluirTipoValeTransporte() || isAlterarTipoValeTransporte();
	}
	
	public String doCadastroDeTipoValeTransporte(){
		doConsultar();
		return NavigationEnum.CADASTRO_DE_TIPOVALETRANSPORTE.getValor();
	}
}
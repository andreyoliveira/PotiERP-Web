package br.com.potierp.modulos.operacional.managed;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;

import org.apache.log4j.Logger;

import br.com.potierp.business.operacional.facade.OperacionalModulo;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.exception.PotiErpMensagensException;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.model.TipoServico;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class TipoServicoMB extends BaseMB implements Serializable{

	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(TipoServicoMB.class);

	private TipoServico tipoServico = new TipoServico();
	
	private SelectionList<TipoServico> listTipoServico = new SelectionList<TipoServico>();
	
	@EJB
	private OperacionalModulo operacionalModulo;
	
	private boolean checkTipoServicoAll = false;
	
	private Integer scrollerPage = 1;
	
	public TipoServicoMB(){
	}
	
	public void doNovo(){
		tipoServico = new TipoServico();
		checkTipoServicoAll = false;
	}
	
	public void doSalvar(){
		try {
			operacionalModulo.salvarTipoServico(tipoServico);
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
			operacionalModulo.excluirTipoServico(tipoServico);
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
			List<TipoServico> list = listTipoServico.getItensSelecionados();
			operacionalModulo.excluirListaTipoServico(list);
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
			List<TipoServico> list = operacionalModulo.consultarTodosTipoServico();
			listTipoServico = new SelectionList<TipoServico>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public TipoServico getTipoServico() {
		return tipoServico;
	}

	public void setTipoServico(final TipoServico tipoServico) {
		this.tipoServico = tipoServico;
	}

	public SelectionList<TipoServico> getListTipoServico() {
		return listTipoServico;
	}

	public void setListTipoServico(final SelectionList<TipoServico> listTipoServico) {
		this.listTipoServico = listTipoServico;
	}

	public boolean isCheckTipoServicoAll() {
		return checkTipoServicoAll;
	}

	public void setCheckTipoServicoAll(final boolean checkTipoServicoAll) {
		this.checkTipoServicoAll = checkTipoServicoAll;
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
	
	public boolean isIncluirTipoServico(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_GRAU_PARENTESCO.getCodigo());
	}
	
	public boolean isAlterarTipoServico(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_GRAU_PARENTESCO.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_GRAU_PARENTESCO.getCodigo());
	}
	
	public boolean isExcluirTipoServico(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_GRAU_PARENTESCO.getCodigo());
	}
	
	public boolean isConsultarTipoServico(){
		return isIncluirTipoServico() || isExcluirTipoServico() || isAlterarTipoServico() || isConsultar();
	}
	
	public boolean isManterTipoServico(){
		return isIncluirTipoServico() || isAlterarTipoServico();
	}
	
	public String doCadastroDeTipoServico(){
		doConsultar();
		return NavigationEnum.CADASTRO_DE_TIPOSERVICO.getValor();
	}
}
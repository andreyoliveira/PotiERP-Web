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
import br.com.potierp.model.Adicional;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class AdicionalMB extends BaseMB implements Serializable{

	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(AdicionalMB.class);

	private Adicional adicional = new Adicional();
	
	private SelectionList<Adicional> listAdicional = new SelectionList<Adicional>();
	
	@EJB
	private RhModulo rhModulo;
	
	private boolean checkAdicionalAll = false;
	
	private Integer scrollerPage = 1;
	
	public AdicionalMB(){
	}
	
	public void doNovo(){
		adicional = new Adicional();
		checkAdicionalAll = false;
	}
	
	public void doSalvar(){
		try {
			rhModulo.salvarAdicional(adicional);
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
			rhModulo.excluirAdicional(adicional);
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
			List<Adicional> list = listAdicional.getItensSelecionados();
			rhModulo.excluirListaAdicional(list);
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
			List<Adicional> list = rhModulo.consultarTodosAdicionais();
			listAdicional = new SelectionList<Adicional>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public Adicional getAdicional() {
		return adicional;
	}

	public void setAdicional(final Adicional adicional) {
		this.adicional = adicional;
	}

	public SelectionList<Adicional> getListAdicional() {
		doConsultar();
		return listAdicional;
	}

	public void setListAdicional(final SelectionList<Adicional> listAdicional) {
		this.listAdicional = listAdicional;
	}

	public boolean isCheckAdicionalAll() {
		return checkAdicionalAll;
	}

	public void setCheckAdicionalAll(final boolean checkAdicionalAll) {
		this.checkAdicionalAll = checkAdicionalAll;
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
	
	public boolean isIncluirAdicional(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_ADICIONAL.getCodigo());
	}
	
	public boolean isAlterarAdicional(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_ADICIONAL.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_ADICIONAL.getCodigo());
	}
	
	public boolean isExcluirAdicional(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_ADICIONAL.getCodigo());
	}
	
	public boolean isConsultarAdicional(){
		return isIncluirAdicional() || isExcluirAdicional() || isAlterarAdicional() || isConsultar();
	}
	
	public boolean isManterAdicional(){
		return isIncluirAdicional() || isAlterarAdicional();
	}
	
	public String doCadastroDeTipoAdicional(){
		return NavigationEnum.CADASTRO_DE_TIPO_DE_ADICIONAL.getValor();
	}

}
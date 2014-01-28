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
import br.com.potierp.model.Setor;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class SetorMB extends BaseMB implements Serializable{

	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(SetorMB.class);
	
	private Setor setor = new Setor();
	
	private SelectionList<Setor> listSetor = new SelectionList<Setor>();
	
	@EJB
	private RhModulo rhModulo;
	
	private boolean checkSetorAll = false;
	
	private Integer scrollerPage = 1;
	
	public SetorMB(){
	}
	
	public void doNovo(){
		setor = new Setor();
		checkSetorAll = false;
	}
	
	public void doSalvar(){
		try {
			rhModulo.salvarSetor(setor);
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
			rhModulo.excluirSetor(setor);
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
			List<Setor> list = listSetor.getItensSelecionados();
			rhModulo.excluirListaSetor(list);
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
			List<Setor> list = rhModulo.consultarTodosSetores();
			listSetor = new SelectionList<Setor>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public SelectionList<Setor> getListSetor() {
		doConsultar();
		return listSetor;
	}

	public void setListSetor(SelectionList<Setor> listSetor) {
		this.listSetor = listSetor;
	}

	public boolean isCheckSetorAll() {
		return checkSetorAll;
	}

	public void setCheckSetorAll(boolean checkSetorAll) {
		this.checkSetorAll = checkSetorAll;
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
	
	public boolean isIncluirSetor(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_ADICIONAL.getCodigo());
	}
	
	public boolean isAlterarSetor(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_ADICIONAL.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_ADICIONAL.getCodigo());
	}
	
	public boolean isExcluirSetor(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_ADICIONAL.getCodigo());
	}
	
	public boolean isConsultarSetor(){
		return isIncluirSetor() || isExcluirSetor() || isAlterarSetor() || isConsultar();
	}
	
	public boolean isManterSetor(){
		return isIncluirSetor() || isAlterarSetor();
	}
	
	public String doCadastroDeSetor(){
		return NavigationEnum.CADASTRO_DE_SETOR.getValor();
	}
}
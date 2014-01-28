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
import br.com.potierp.model.GrauParentesco;
import br.com.potierp.model.SituacaoEnum;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class GrauParentescoMB extends BaseMB implements Serializable{

	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(GrauParentescoMB.class);

	private GrauParentesco grauParentesco = new GrauParentesco();
	
	private SelectionList<GrauParentesco> listGrauParentesco = new SelectionList<GrauParentesco>();
	
	@EJB
	private RhModulo rhModulo;
	
	private boolean checkGrauParentescoAll = false;
	
	private Integer scrollerPage = 1;
	
	public GrauParentescoMB(){
	}
	
	public void doNovo(){
		grauParentesco = new GrauParentesco();
		grauParentesco.setSituacao(SituacaoEnum.ATIVO);
		checkGrauParentescoAll = false;
	}
	
	public void doSalvar(){
		try {
			rhModulo.salvarGrauParentesco(grauParentesco);
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
			rhModulo.excluirGrauParentesco(grauParentesco);
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
			List<GrauParentesco> list = listGrauParentesco.getItensSelecionados();
			rhModulo.excluirListaGrauParentesco(list);
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
			List<GrauParentesco> list = rhModulo.consultarTodosGrauParentescos();
			listGrauParentesco = new SelectionList<GrauParentesco>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public GrauParentesco getGrauParentesco() {
		return grauParentesco;
	}

	public void setGrauParentesco(final GrauParentesco grauParentesco) {
		this.grauParentesco = grauParentesco;
	}

	public SelectionList<GrauParentesco> getListGrauParentesco() {
		return listGrauParentesco;
	}

	public void setListGrauParentesco(final SelectionList<GrauParentesco> listGrauParentesco) {
		this.listGrauParentesco = listGrauParentesco;
	}

	public boolean isCheckGrauParentescoAll() {
		return checkGrauParentescoAll;
	}

	public void setCheckGrauParentescoAll(final boolean checkGrauParentescoAll) {
		this.checkGrauParentescoAll = checkGrauParentescoAll;
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
	
	public boolean isIncluirGrauParentesco(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_GRAU_PARENTESCO.getCodigo());
	}
	
	public boolean isAlterarGrauParentesco(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_GRAU_PARENTESCO.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_GRAU_PARENTESCO.getCodigo());
	}
	
	public boolean isExcluirGrauParentesco(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_GRAU_PARENTESCO.getCodigo());
	}
	
	public boolean isConsultarGrauParentesco(){
		return isIncluirGrauParentesco() || isExcluirGrauParentesco() || isAlterarGrauParentesco() || isConsultar();
	}
	
	public boolean isManterGrauParentesco(){
		return isIncluirGrauParentesco() || isAlterarGrauParentesco();
	}
	
	public String doCadastroDeGrauParentesco(){
		doConsultar();
		return NavigationEnum.CADASTRO_DE_GRAUPARENTESCO.getValor();
	}
}
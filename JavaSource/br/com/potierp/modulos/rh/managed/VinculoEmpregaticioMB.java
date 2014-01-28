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
import br.com.potierp.model.VinculoEmpregaticio;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class VinculoEmpregaticioMB extends BaseMB{

	private static Logger log = Logger.getLogger(VinculoEmpregaticioMB.class);
	
	private VinculoEmpregaticio vinculoEmpregaticio = new VinculoEmpregaticio();
	
	private SelectionList<VinculoEmpregaticio> listVinculoEmpregaticio = new SelectionList<VinculoEmpregaticio>();
	
	@EJB
	private RhModulo rhModulo;
	
	private boolean checkVinculoEmpregaticioAll = false;
	
	private Integer scrollerPage = 1;
	
	public VinculoEmpregaticioMB(){
	}
	
	public void doNovo(){
		vinculoEmpregaticio = new VinculoEmpregaticio();
		checkVinculoEmpregaticioAll = false;
	}
	
	public void doSalvar(){
		try {
			rhModulo.salvarVinculoEmpregaticio(vinculoEmpregaticio);
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
			rhModulo.excluirVinculoEmpregaticio(vinculoEmpregaticio);
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
			List<VinculoEmpregaticio> list = listVinculoEmpregaticio.getItensSelecionados();
			rhModulo.excluirListaVinculoEmpregaticio(list);
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
			List<VinculoEmpregaticio> list = rhModulo.consultarTodosVinculosEmpregaticio();
			listVinculoEmpregaticio = new SelectionList<VinculoEmpregaticio>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public VinculoEmpregaticio getVinculoEmpregaticio() {
		return vinculoEmpregaticio;
	}

	public void setVinculoEmpregaticio(VinculoEmpregaticio vinculoEmpregaticio) {
		this.vinculoEmpregaticio = vinculoEmpregaticio;
	}

	public SelectionList<VinculoEmpregaticio> getListVinculoEmpregaticio() {
		doConsultar();
		return listVinculoEmpregaticio;
	}

	public void setListVinculoEmpregaticio(SelectionList<VinculoEmpregaticio> listVinculoEmpregaticio) {
		this.listVinculoEmpregaticio = listVinculoEmpregaticio;
	}

	public boolean isCheckVinculoEmpregaticioAll() {
		return checkVinculoEmpregaticioAll;
	}

	public void setCheckVinculoEmpregaticioAll(boolean checkVinculoEmpregaticioAll) {
		this.checkVinculoEmpregaticioAll = checkVinculoEmpregaticioAll;
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
	
	public boolean isIncluirVinculoEmpregaticio(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_VINCULOEMPREGATICIO.getCodigo());
	}
	
	public boolean isAlterarVinculoEmpregaticio(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_VINCULOEMPREGATICIO.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_VINCULOEMPREGATICIO.getCodigo());
	}
	
	public boolean isExcluirVinculoEmpregaticio(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_VINCULOEMPREGATICIO.getCodigo());
	}
	
	public boolean isConsultarVinculoEmpregaticio(){
		return isIncluirVinculoEmpregaticio() || isExcluirVinculoEmpregaticio() || isAlterarVinculoEmpregaticio() || isConsultar();
	}
	
	public boolean isManterVinculoEmpregaticio(){
		return isIncluirVinculoEmpregaticio() || isAlterarVinculoEmpregaticio();
	}
	
	public String doCadastroDeVinculoEmpregaticio(){
		return NavigationEnum.CADASTRO_DE_VINCULOEMPREGATICIO.getValor();
	}
}
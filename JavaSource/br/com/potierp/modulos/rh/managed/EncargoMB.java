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
import br.com.potierp.model.Encargo;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class EncargoMB extends BaseMB{

	private static Logger log = Logger.getLogger(EncargoMB.class);
	
	private Encargo encargo = new Encargo();
	
	private SelectionList<Encargo> listEncargo = new SelectionList<Encargo>();
	
	@EJB
	private RhModulo rhModulo;
	
	private boolean checkEncargoAll = false;
	
	private Integer scrollerPage = 1;
	
	public EncargoMB(){
	}
	
	public void doNovo() {
		encargo = new Encargo();
		checkEncargoAll = false;
	}
	
	public void doSalvar(){
		try {
			rhModulo.salvarEncargo(encargo);
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
			rhModulo.excluirEncargo(encargo);
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
			List<Encargo> list = listEncargo.getItensSelecionados();
			rhModulo.excluirListaEncargo(list);
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
			List<Encargo> list = rhModulo.consultarTodosEncargos();
			listEncargo = new SelectionList<Encargo>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public Encargo getEncargo() {
		return encargo;
	}

	public void setEncargo(Encargo encargo) {
		this.encargo = encargo;
	}

	public SelectionList<Encargo> getListEncargo() {
		doConsultar();
		return listEncargo;
	}

	public void setListEncargo(SelectionList<Encargo> listEncargo) {
		this.listEncargo = listEncargo;
	}

	public boolean isCheckEncargoAll() {
		return checkEncargoAll;
	}

	public void setCheckEncargoAll(boolean checkEncargoAll) {
		this.checkEncargoAll = checkEncargoAll;
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
	
	public boolean isIncluirEncargo(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_ENCARGO.getCodigo());
	}
	
	public boolean isAlterarEncargo(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_ENCARGO.getCodigo());
	}
	
	public boolean isExcluirEncargo(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_ENCARGO.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_ENCARGO.getCodigo());
	}
	
	public boolean isConsultarEncargo(){
		return isIncluirEncargo() || isAlterarEncargo() || isExcluirEncargo() || isConsultar();
	}
	
	public boolean isManterEncargo(){
		return isIncluirEncargo() || isAlterarEncargo();
	}
	
	public String doCadastroDeTipoEncargo(){
		return NavigationEnum.CADASTRO_DE_TIPO_DE_ENCARGO.getValor();
	}
}
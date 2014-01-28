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
import br.com.potierp.model.Cargo;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class CargoMB extends BaseMB{

	private static Logger log = Logger.getLogger(CargoMB.class);
	
	private Cargo cargo = new Cargo();
	
	private SelectionList<Cargo> listCargo = new SelectionList<Cargo>();
	
	@EJB
	private RhModulo rhModulo;
	
	private boolean checkCargoAll = false;
	
	private Integer scrollerPage = 1;
	
	public CargoMB(){
	}
	
	public void doNovo(){
		cargo = new Cargo();
		checkCargoAll = false;
	}
	
	public void doSalvar(){
		try {
			rhModulo.salvarCargo(cargo);
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
			rhModulo.excluirCargo(cargo);
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
			List<Cargo> list = listCargo.getItensSelecionados();
			rhModulo.excluirListaCargo(list);
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
			List<Cargo> list = rhModulo.consultarTodosCargos();
			listCargo = new SelectionList<Cargo>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public SelectionList<Cargo> getListCargo() {
		doConsultar();
		return listCargo;
	}

	public void setListCargo(SelectionList<Cargo> listCargo) {
		this.listCargo = listCargo;
	}

	public boolean isCheckCargoAll() {
		return checkCargoAll;
	}

	public void setCheckCargoAll(boolean checkCargoAll) {
		this.checkCargoAll = checkCargoAll;
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
	
	public boolean isIncluirCargo(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_CARGO.getCodigo());
	}
	
	public boolean isAlterarCargo(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_CARGO.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_CARGO.getCodigo());
	}
	
	public boolean isExcluirCargo(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_CARGO.getCodigo());
	}
	
	public boolean isConsultarCargo(){
		return isIncluirCargo() || isExcluirCargo() || isAlterarCargo() || isConsultar();
	}
	
	public boolean isManterCargo(){
		return isIncluirCargo() || isAlterarCargo();
	}
	
	public String doCadastroDeCargo(){
		doConsultar();
		return NavigationEnum.CADASTRO_DE_CARGO.getValor();
	}

}
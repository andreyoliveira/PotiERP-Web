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
import br.com.potierp.model.Beneficio;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class BeneficioMB extends BaseMB{

	private static Logger log = Logger.getLogger(BeneficioMB.class);
	
	private Beneficio beneficio = new Beneficio();
	
	private SelectionList<Beneficio> listBeneficio = new SelectionList<Beneficio>();
	
	@EJB
	private RhModulo rhModulo;
	
	private boolean checkBeneficioAll = false;
	
	private Integer scrollerPage = 1;
	
	public BeneficioMB(){
	}
	
	public void doNovo() throws Exception{
		beneficio = new Beneficio();
		checkBeneficioAll = false;
	}
	
	public void doSalvar(){
		try {
			rhModulo.salvarBeneficio(beneficio);
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
			rhModulo.excluirBeneficio(beneficio);
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
			List<Beneficio> list = listBeneficio.getItensSelecionados();
			rhModulo.excluirListaBeneficio(list);
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
			List<Beneficio> list = rhModulo.consultarTodosBeneficios();
			listBeneficio = new SelectionList<Beneficio>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public Beneficio getBeneficio() {
		return beneficio;
	}

	public void setBeneficio(Beneficio beneficio) {
		this.beneficio = beneficio;
	}

	public SelectionList<Beneficio> getListBeneficio() {
		doConsultar();
		return listBeneficio;
	}

	public void setListBeneficio(SelectionList<Beneficio> listBeneficio) {
		this.listBeneficio = listBeneficio;
	}

	public boolean isCheckBeneficioAll() {
		return checkBeneficioAll;
	}

	public void setCheckBeneficioAll(boolean checkBeneficioAll) {
		this.checkBeneficioAll = checkBeneficioAll;
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
	
	public boolean isIncluirBeneficio(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_BENEFICIO.getCodigo());
	}
	
	public boolean isAlterarBeneficio(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_BENEFICIO.getCodigo());
	}
	
	public boolean isExcluirBeneficio(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_BENEFICIO.getCodigo());
	}
	
	public boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_BENEFICIO.getCodigo());
	}
	
	public boolean isConsultarBeneficio(){
		return isIncluirBeneficio() || isExcluirBeneficio() || isAlterarBeneficio() || isConsultar();
	}
	
	public boolean isManterBeneficio(){
		return isIncluirBeneficio() || isAlterarBeneficio();
	}
	
	public String doCadastroDeTipoBeneficio(){
		return NavigationEnum.CADASTRO_DE_TIPO_DE_BENEFICIO.getValor();
	}
	
}
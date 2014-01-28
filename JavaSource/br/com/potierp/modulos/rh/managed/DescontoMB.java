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
import br.com.potierp.model.Desconto;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class DescontoMB extends BaseMB{

	private static Logger log = Logger.getLogger(DescontoMB.class);
	
	private Desconto desconto = new Desconto();
	
	private SelectionList<Desconto> listDesconto = new SelectionList<Desconto>();
	
	@EJB
	private RhModulo rhModulo;
	
	private boolean checkDescontoAll = false;
	
	private Integer scrollerPage = 1;
	
	public DescontoMB(){
	}
	
	public void doNovo() throws Exception{
		desconto = new Desconto();
		checkDescontoAll = false;
	}
	
	public void doSalvar(){
		try {
			rhModulo.salvarDesconto(desconto);
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
			rhModulo.excluirDesconto(desconto);
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
			List<Desconto> list = listDesconto.getItensSelecionados();
			rhModulo.excluirListaDesconto(list);
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
			List<Desconto> list = rhModulo.consultarTodosDescontos();
			listDesconto = new SelectionList<Desconto>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public Desconto getDesconto() {
		return desconto;
	}

	public void setDesconto(Desconto desconto) {
		this.desconto = desconto;
	}

	public SelectionList<Desconto> getListDesconto() {
		doConsultar();
		return listDesconto;
	}

	public void setListDesconto(SelectionList<Desconto> listDesconto) {
		this.listDesconto = listDesconto;
	}

	public boolean isCheckDescontoAll() {
		return checkDescontoAll;
	}

	public void setCheckDescontoAll(boolean checkDescontoAll) {
		this.checkDescontoAll = checkDescontoAll;
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
	
	public boolean isIncluirDesconto(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_DESCONTO.getCodigo());
	}
	
	public boolean isAlterarDesconto(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_DESCONTO.getCodigo());
	}
	
	public boolean isExcluirDesconto(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_DESCONTO.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_DESCONTO.getCodigo());
	}
	
	public boolean isConsultarDesconto(){
		return isIncluirDesconto() || isAlterarDesconto() || isExcluirDesconto() || isConsultar();
	}
	
	public boolean isManterDesconto(){
		return isIncluirDesconto() || isAlterarDesconto();
	}
	
	public String doCadastroDeTipoDesconto(){
		return NavigationEnum.CADASTRO_DE_TIPO_DE_DESCONTO.getValor();
	}
	
}
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
import br.com.potierp.model.FormaPagamento;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class FormaPagamentoMB extends BaseMB{

	private static Logger log = Logger.getLogger(FormaPagamentoMB.class);
	
	private FormaPagamento formaPagamento = new FormaPagamento();
	
	private SelectionList<FormaPagamento> listFormaPagamento = new SelectionList<FormaPagamento>();
	
	@EJB
	private RhModulo rhModulo;
	
	private boolean checkFormaPagamentoAll = false;
	
	private Integer scrollerPage = 1;
	
	public FormaPagamentoMB(){
	}
	
	public void doNovo(){
		formaPagamento = new FormaPagamento();
		checkFormaPagamentoAll = false;
	}
	
	public void doSalvar(){
		try {
			rhModulo.salvarFormaPagamento(formaPagamento);
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
			rhModulo.excluirFormaPagamento(formaPagamento);
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
			List<FormaPagamento> list = listFormaPagamento.getItensSelecionados();
			rhModulo.excluirListaFormaPagamento(list);
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
			List<FormaPagamento> list = rhModulo.consultarTodasFormasPagamentos();
			listFormaPagamento = new SelectionList<FormaPagamento>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public SelectionList<FormaPagamento> getListFormaPagamento() {
		doConsultar();
		return listFormaPagamento;
	}

	public void setListFormaPagamento(SelectionList<FormaPagamento> listFormaPagamento) {
		this.listFormaPagamento = listFormaPagamento;
	}

	public boolean isCheckFormaPagamentoAll() {
		return checkFormaPagamentoAll;
	}

	public void setCheckFormaPagamentoAll(boolean checkFormaPagamentoAll) {
		this.checkFormaPagamentoAll = checkFormaPagamentoAll;
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
	
	public String doCadastroDeFormaPagamento(){
		return NavigationEnum.CADASTRO_DE_FORMAPAGAMENTO.getValor();
	}
	
	public boolean isIncluirFormaPagamento(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_FORMAPAGAMENTO.getCodigo());
	}
	
	public boolean isAlterarFormaPagamento(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_FORMAPAGAMENTO.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_FORMAPAGAMENTO.getCodigo());
	}
	
	public boolean isExcluirFormaPagamento(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_FORMAPAGAMENTO.getCodigo());
	}
	
	public boolean isConsultarFormaPagamento(){
		return isIncluirFormaPagamento() || isExcluirFormaPagamento() || isAlterarFormaPagamento() || isConsultar();
	}
	
	public boolean isManterFormaPagamento(){
		return isIncluirFormaPagamento() || isAlterarFormaPagamento();
	}
}
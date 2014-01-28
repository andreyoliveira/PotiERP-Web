package br.com.potierp.modulos.financeiro.managed;

import java.util.List;

import javax.ejb.EJB;

import org.apache.log4j.Logger;

import br.com.potierp.business.financeiro.facade.FinanceiroModulo;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.exception.PotiErpMensagensException;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.model.SolicitacaoPagamento;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class SolicitacaoPagamentoMB extends BaseMB {
	
	private static final Logger log = Logger.getLogger(SolicitacaoPagamentoMB.class);
	
	@EJB
	private FinanceiroModulo moduloFinanceiro;
	
	private SolicitacaoPagamento solicitacao = new SolicitacaoPagamento();
	
	private Integer scrollerPage = 1;
	
	private boolean checkAllSolicitacao = false;
	
	private SelectionList<SolicitacaoPagamento> listSolicitacoes = new SelectionList<SolicitacaoPagamento>();
	
	public SolicitacaoPagamentoMB(){
		super();
	}
	
	public void doNova() throws Exception{
		solicitacao = new SolicitacaoPagamento();
		checkAllSolicitacao = false;
	}
	
	public void doSalvar(){
		try {
			moduloFinanceiro.salvar(solicitacao);
			doNova();
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
			moduloFinanceiro.excluir(solicitacao);
			doNova();
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
			List<SolicitacaoPagamento> list = listSolicitacoes.getItensSelecionados();
			moduloFinanceiro.excluir(list);
			doNova();
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
			List<SolicitacaoPagamento> list = moduloFinanceiro.consultarTodas();
			listSolicitacoes = new SelectionList<SolicitacaoPagamento>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}

	public SolicitacaoPagamento getSolicitacao() {
		return solicitacao;
	}

	public void setSolicitacao(final SolicitacaoPagamento solicitacao) {
		this.solicitacao = solicitacao;
	}
	
	public Integer getScrollerPage() {
		return scrollerPage;
	}

	public void setScrollerPage(final Integer scrollerPage) {
		this.scrollerPage = scrollerPage;
	}

	public boolean isCheckAllSolicitacao() {
		return checkAllSolicitacao;
	}

	public void setCheckAllSolicitacao(final boolean checkAllSolicitacao) {
		this.checkAllSolicitacao = checkAllSolicitacao;
	}

	public SelectionList<SolicitacaoPagamento> getListSolicitacoes() {
		doConsultar();
		return listSolicitacoes;
	}

	public void setListSolicitacoes(final SelectionList<SolicitacaoPagamento> listSolicitacoes) {
		this.listSolicitacoes = listSolicitacoes;
	}

	@Override
	public Logger getLogger() {
		return log;
	}
	
	public boolean isIncluirSolicitacaoDePagamento(){
		return this.getUsuario().possuiFuncionalidade(FuncionalidadeEnum.INCLUIR_SOLICITACAO_PAGAMENTO.getCodigo());
	}
	
	public boolean isAlterarSolicitacaoDePagamento(){
		return this.getUsuario().possuiFuncionalidade(FuncionalidadeEnum.ALTERAR_SOLICITACAO_PAGAMENTO.getCodigo());
	}
	
	public boolean isExcluirSolicitacaoDePagamento(){
		return this.getUsuario().possuiFuncionalidade(FuncionalidadeEnum.EXCLUIR_SOLICITACAO_PAGAMENTO.getCodigo());
	}
	
	private boolean isConsultar(){
		return this.getUsuario().possuiFuncionalidade(FuncionalidadeEnum.CONSULTAR_SOLICITACAO_PAGAMENTO.getCodigo());
	}
	
	public boolean isConsultarSolicitacaoDePagamento(){
		return isIncluirSolicitacaoDePagamento() || isAlterarSolicitacaoDePagamento() || isExcluirSolicitacaoDePagamento() || isConsultar();
	}
	
	public boolean isManterSolicitacaoDePagamento(){
		return isIncluirSolicitacaoDePagamento() || isAlterarSolicitacaoDePagamento();
	}
	
	public String doCadastroDeSolicitacaoDePagamento(){
		return NavigationEnum.CADASTRO_DE_SOLICITACAO_PAGAMENTO.getValor();
	}

}

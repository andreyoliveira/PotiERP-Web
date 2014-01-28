package br.com.potierp.modulos.rh.managed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import br.com.potierp.business.endereco.facade.EnderecoModulo;
import br.com.potierp.faces.converter.MockEnum;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.exception.PotiErpMensagensException;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.model.Cidade;
import br.com.potierp.model.Estado;
import br.com.potierp.model.Pais;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class CidadeMB extends BaseMB implements Serializable{

	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(CidadeMB.class);

	private Cidade cidade = new Cidade();
	
	private SelectionList<Cidade> listCidade = new SelectionList<Cidade>();
	
	private List<SelectItem> itensEstado = new ArrayList<SelectItem>();
	
	@EJB
	private EnderecoModulo enderecoModulo;
	
	private boolean checkCidadeAll = false;
	
	private Integer scrollerPage = 1;
	
	public CidadeMB(){
	}
	
	public void doNovo(){
		cidade = new Cidade();
		checkCidadeAll = false;
	}
	
	public void doSalvar(){
		try {
			enderecoModulo.salvarCidade(cidade);
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
			enderecoModulo.excluirCidade(cidade);
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
			List<Cidade> list = listCidade.getItensSelecionados();
			enderecoModulo.excluirListaCidade(list);
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
			List<Cidade> list = enderecoModulo.consultarTodasCidades();
			listCidade = new SelectionList<Cidade>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(final Cidade cidade) {
		this.cidade = cidade;
	}

	public SelectionList<Cidade> getListCidade() {
		return listCidade;
	}
	
	public List<SelectItem> getItensEstado() {
		List<Estado> listEstados = getEstados();
		if(listEstados != null){
			itensEstado.clear();
			addMock(itensEstado, MockEnum.SELECIONE);
			for(Estado estado : listEstados){
				SelectItem item = new SelectItem(estado, estado.getSigla());
				itensEstado.add(item);
			}
		}
		return itensEstado;
	}

	private List<Estado> getEstados() {
		try {
			//TODO Por enquanto o país é setado na mão. Arrumar o cadastro de País.
			Pais pais = this.enderecoModulo.buscarPaisPorSigla("BR");
			List<Estado> listEstados = enderecoModulo.buscarEstadosPorPais(pais);
			Collections.sort(listEstados);
			return listEstados;
		} catch (PotiErpException e) {
			addMensagemErro(e);
			return null;
		}
	}

	public void setListCidade(final SelectionList<Cidade> listCidade) {
		this.listCidade = listCidade;
	}

	public boolean isCheckCidadeAll() {
		return checkCidadeAll;
	}

	public void setCheckCidadeAll(final boolean checkCidadeAll) {
		this.checkCidadeAll = checkCidadeAll;
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
	
	public boolean isIncluirCidade(){//TODO MUDAR PERMISSAO
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_ADICIONAL.getCodigo());
	}
	
	public boolean isAlterarCidade(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_ADICIONAL.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_ADICIONAL.getCodigo());
	}
	
	public boolean isExcluirCidade(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_ADICIONAL.getCodigo());
	}
	
	public boolean isConsultarCidade(){
		return isIncluirCidade() || isExcluirCidade() || isAlterarCidade() || isConsultar();
	}
	
	public boolean isManterCidade(){
		return isIncluirCidade() || isAlterarCidade();
	}
	
	public String doCadastroDeTipoCidade(){
		doConsultar();
		return NavigationEnum.PARAMETRO_DE_CIDADE.getValor();
	}

}
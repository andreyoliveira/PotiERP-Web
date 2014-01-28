package br.com.potierp.modulos.rh.managed;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import br.com.potierp.business.rh.facade.RhModulo;
import br.com.potierp.faces.converter.MockEnum;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.exception.PotiErpMensagensException;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.model.ApontamentosFolha;
import br.com.potierp.model.Funcionario;
import br.com.potierp.model.Pessoa;
import br.com.potierp.model.Verba;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class ApontamentosFolhaMB extends BaseMB{

	private static Logger log = Logger.getLogger(ApontamentosFolhaMB.class);

	private ApontamentosFolha apontamentosFolha = new ApontamentosFolha();
	
	private List<SelectItem> itensVerba = new ArrayList<SelectItem>();
	
	private SelectionList<ApontamentosFolha> listApontamentosFolha = new SelectionList<ApontamentosFolha>();
	
	@EJB
	private RhModulo rhModulo;
	
	private boolean checkApontamentosFolhaAll = false;
	
	private Integer scrollerPage = 1;
	
	public ApontamentosFolhaMB(){
		doNovo();
	}
	
	public void doNovo(){
		apontamentosFolha = new ApontamentosFolha();
		apontamentosFolha.setFuncionario(new Funcionario());
		apontamentosFolha.getFuncionario().setPessoa(new Pessoa());
		checkApontamentosFolhaAll = false;
	}
	
	public void buscarFuncionario(){
		try {
			if(isRegistroEmpregadoPreenchido()){
				Funcionario funcionario = rhModulo.consultarFuncionarioPorRe(apontamentosFolha.getFuncionario());
				if(funcionario != null){
					apontamentosFolha.setFuncionario(funcionario);
				}else{
					addMensagemErro(MensagensFacesEnum.ERRO_FUNCIONARIO_NAO_ENCONTRADO);
					apontamentosFolha.setFuncionario(new Funcionario());
					apontamentosFolha.getFuncionario().setPessoa(new Pessoa());
				}
			}else{
				apontamentosFolha.setFuncionario(new Funcionario());
				apontamentosFolha.getFuncionario().setPessoa(new Pessoa());
			}
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	private boolean isRegistroEmpregadoPreenchido() {
		return apontamentosFolha != null 
				&& apontamentosFolha.getFuncionario() != null
				&& apontamentosFolha.getFuncionario().getCodigoRegistro() != null
				&& apontamentosFolha.getFuncionario().getCodigoRegistro() > 0;
	}
	
	public void doSalvar(){
		/*try {
			rhModulo.salvarApontamentosFolha(apontamentosFolha);
			doNovo();
			doConsultar();
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_SALVAR);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}*/
	}
	
	public void doExcluir(){
		/*try {
			rhModulo.excluirApontamentosFolha(apontamentosFolha);
			doNovo();
			doConsultar();
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_SALVAR);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}*/
	}
	
	public void doExcluirLote(){
		/*try {
			List<ApontamentosFolha> list = listApontamentosFolha.getItensSelecionados();
			rhModulo.excluirListaApontamentosFolha(list);
			doNovo();
			doConsultar();
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_EXCLUIR_LISTA);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}*/
	}
	
	public void doConsultar(){
		/*try {
			List<ApontamentosFolha> list = rhModulo.consultarTodasApontamentosFolhas();
			listApontamentosFolha = new SelectionList<ApontamentosFolha>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}*/
	}
	
	public ApontamentosFolha getApontamentosFolha() {
		return apontamentosFolha;
	}

	public void setApontamentosFolha(final ApontamentosFolha apontamentosFolha) {
		this.apontamentosFolha = apontamentosFolha;
	}
	
	public List<SelectItem> getItensVerba() throws PotiErpException {
		List<Verba> listVerba = getTiposVerba();
		if(listVerba != null){
			itensVerba.clear();
			addMock(itensVerba, MockEnum.SELECIONE);
			for(Verba verba : listVerba){
				SelectItem item = new SelectItem(verba,verba.getNome());
				itensVerba.add(item);
			}
		}
		return itensVerba;
	}

	private List<Verba> getTiposVerba() throws PotiErpException {
		if(itensVerba.isEmpty()){ 
			return rhModulo.consultarTodasVerbas();
		}
		return null;
	}

	public void setItensVerba(final List<SelectItem> itensVerba) {
		this.itensVerba = itensVerba;
	}

	public SelectionList<ApontamentosFolha> getListApontamentosFolha() {
		return listApontamentosFolha;
	}

	public void setListApontamentosFolha(final SelectionList<ApontamentosFolha> listApontamentosFolha) {
		this.listApontamentosFolha = listApontamentosFolha;
	}
	
	public boolean isCheckApontamentosFolhaAll() {
		return checkApontamentosFolhaAll;
	}

	public void setCheckApontamentosFolhaAll(final boolean checkApontamentosFolhaAll) {
		this.checkApontamentosFolhaAll = checkApontamentosFolhaAll;
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
	
	public boolean isIncluirApontamentosFolha(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_TIPO_VALE_REFEICAO.getCodigo());
	}
	
	public boolean isAlterarApontamentosFolha(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_TIPO_VALE_REFEICAO.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_TIPO_VALE_REFEICAO.getCodigo());
	}
	
	public boolean isExcluirApontamentosFolha(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_TIPO_VALE_REFEICAO.getCodigo());
	}
	
	public boolean isConsultarApontamentosFolha(){
		return isIncluirApontamentosFolha() || isExcluirApontamentosFolha() || isAlterarApontamentosFolha() || isConsultar();
	}
	
	public boolean isManterApontamentosFolha(){
		return isIncluirApontamentosFolha() || isAlterarApontamentosFolha();
	}
	
	public String doApontamentosFolha(){
		doConsultar();
		return NavigationEnum.APONTAMENTOS_FOLHA.getValor();
	}
}
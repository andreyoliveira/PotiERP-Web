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
import br.com.potierp.model.Dependente;
import br.com.potierp.model.Funcionario;
import br.com.potierp.model.Pessoa;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class DependenteMB extends BaseMB implements Serializable{

	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(DependenteMB.class);

	private Dependente dependente = new Dependente();
	
	private SelectionList<Dependente> listDependente = new SelectionList<Dependente>();
	
	@EJB
	private RhModulo rhModulo;
	
	private boolean checkDependenteAll = false;
	
	private Integer scrollerPage = 1;
	
	public DependenteMB(){
	}
	
	public void doNovo(){
		dependente = new Dependente();
		dependente.setFuncionario(new Funcionario());
		dependente.getFuncionario().setPessoa(new Pessoa());
		checkDependenteAll = false;
	}
	
	public void doSalvar(){
		try {
			if (isFuncionarioEncontrado()){
				rhModulo.salvarDependente(dependente);
				doNovo();
			}else{
				addMensagemErro(MensagensFacesEnum.ERRO_FUNCIONARIO_NAO_INFORMADO);
			}
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_SALVAR);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}

	private boolean isFuncionarioEncontrado() {
		return dependente.getFuncionario().getPessoa().getNome() != null
				&& !"".equals(dependente.getFuncionario().getPessoa()
						.getNome());
	}
	
	public void doExcluir(){
		try {
			rhModulo.excluirDependente(dependente);
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
			List<Dependente> list = listDependente.getItensSelecionados();
			rhModulo.excluirListaDependente(list);
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
			List<Dependente> list = rhModulo.consultarTodosDependentes();
			listDependente = new SelectionList<Dependente>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public void buscarFuncionario(){
		try {
			if(isRegistroEmpregadoPreenchido()){
				Funcionario funcionario = rhModulo.consultarFuncionarioPorRe(dependente.getFuncionario());
				if(funcionario != null){
					dependente.setFuncionario(funcionario);
				}else{
					addMensagemErro(MensagensFacesEnum.ERRO_FUNCIONARIO_NAO_ENCONTRADO);
					dependente.setFuncionario(new Funcionario());
					dependente.getFuncionario().setPessoa(new Pessoa());
				}
			}else{
				dependente.setFuncionario(new Funcionario());
				dependente.getFuncionario().setPessoa(new Pessoa());
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
		return dependente.getFuncionario() != null
				&& dependente.getFuncionario().getCodigoRegistro() != null
				&& dependente.getFuncionario().getCodigoRegistro() > 0;
	}
	
	public Dependente getDependente() {
		return dependente;
	}

	public void setDependente(final Dependente dependente) {
		this.dependente = dependente;
	}

	public SelectionList<Dependente> getListDependente() {
		doConsultar();
		return listDependente;
	}

	public void setListDependente(final SelectionList<Dependente> listDependente) {
		this.listDependente = listDependente;
	}

	public boolean isCheckDependenteAll() {
		return checkDependenteAll;
	}

	public void setCheckDependenteAll(final boolean checkDependenteAll) {
		this.checkDependenteAll = checkDependenteAll;
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
	
	public boolean isIncluirDependente(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_DEPENDENTE.getCodigo());
	}
	
	public boolean isAlterarDependente(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_DEPENDENTE.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_DEPENDENTE.getCodigo());
	}
	
	public boolean isExcluirDependente(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_DEPENDENTE.getCodigo());
	}
	
	public boolean isConsultarDependente(){
		return isIncluirDependente() || isExcluirDependente() || isAlterarDependente() || isConsultar();
	}
	
	public boolean isManterDependente(){
		return isIncluirDependente() || isAlterarDependente();
	}
	
	public String doCadastroDeDependente(){
		return NavigationEnum.CADASTRO_DE_DEPENDENTES.getValor();
	}
}
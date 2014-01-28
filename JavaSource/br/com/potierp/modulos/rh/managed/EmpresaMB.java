package br.com.potierp.modulos.rh.managed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.richfaces.component.UIDatascroller;

import br.com.potierp.business.endereco.facade.EnderecoModulo;
import br.com.potierp.business.rh.facade.RhModulo;
import br.com.potierp.faces.converter.MockEnum;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.exception.PotiErpMensagensException;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.model.Empresa;
import br.com.potierp.model.Endereco;
import br.com.potierp.model.Estado;
import br.com.potierp.model.Pais;
import br.com.potierp.model.Telefone;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class EmpresaMB extends BaseMB{

	private static Logger log = Logger.getLogger(EmpresaMB.class);
	
	private Empresa empresa = new Empresa();
	
	private SelectionList<Empresa> listEmpresa = new SelectionList<Empresa>();
	
	@EJB
	private RhModulo rhModulo;
	
	private boolean checkEmpresaAll = false;
	
	private Integer scrollerPage = 1;
	
	private UIDatascroller datascroller;
	
	@EJB
	private EnderecoModulo enderecoModulo;
	
	private List<SelectItem> itensEstado = new ArrayList<SelectItem>();
	
	public EmpresaMB() throws Exception {
		doNovo();
	}
	
	public void doNovo() throws Exception{
		empresa = new Empresa();
		empresa.setEndereco(new Endereco());
		empresa.setFiliais(new ArrayList<Empresa>());
		empresa.setTelefone1(new Telefone());
		empresa.setTelefone2(new Telefone());
	}
	
	public void doSalvar(){
		try {
			rhModulo.salvarEmpresa(empresa);
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
			rhModulo.excluirEmpresa(empresa);
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
			List<Empresa> list = listEmpresa.getItensSelecionados();
			rhModulo.excluirListaEmpresa(list);
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
			List<Empresa> list = rhModulo.consultarTodasEmpresas();
			listEmpresa = new SelectionList<Empresa>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public SelectionList<Empresa> getListEmpresa() {
		doConsultar();
		return listEmpresa;
	}

	public void setListEmpresa(SelectionList<Empresa> listEmpresa) {
		this.listEmpresa = listEmpresa;
	}

	public boolean isCheckEmpresaAll() {
		return checkEmpresaAll;
	}

	public void setCheckEmpresaAll(boolean checkEmpresaAll) {
		this.checkEmpresaAll = checkEmpresaAll;
	}
	
	public Integer getScrollerPage() {
		return scrollerPage;
	}

	public void setScrollerPage(Integer scrollerPage) {
		this.scrollerPage = scrollerPage;
	}
	
	

	public UIDatascroller getDatascroller() {
		return datascroller;
	}

	public void setDatascroller(UIDatascroller datascroller) {
		this.datascroller = datascroller;
	}

	@Override
	public Logger getLogger() {
		return log;
	}
	
	public boolean isIncluirEmpresa(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_EMPRESA.getCodigo());
	}
	
	public boolean isAlterarEmpresa(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_EMPRESA.getCodigo());
	}
	
	public boolean isExcluirEmpresa(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_EMPRESA.getCodigo());
	}
	
	public boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_EMPRESA.getCodigo());
	}
	
	public boolean isConsultarEmpresa(){
		return isIncluirEmpresa() || isAlterarEmpresa() || isExcluirEmpresa() || isConsultar();			
	}
	
	public boolean isManterEmpresa(){
		return isIncluirEmpresa() || isAlterarEmpresa();
	}
	
	public String doCadastroDeEmpresa(){
		return NavigationEnum.CADASTRO_DE_EMPRESA.getValor();
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
	
}
package br.com.potierp.modulos.rh.managed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.component.UIForm;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import br.com.potierp.business.endereco.facade.EnderecoModulo;
import br.com.potierp.business.rh.facade.RhModulo;
import br.com.potierp.faces.converter.MockEnum;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.exception.PotiErpMensagensException;
import br.com.potierp.infra.helper.SelectionEntity;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.model.Cidade;
import br.com.potierp.model.Estado;
import br.com.potierp.model.Feriado;
import br.com.potierp.model.Pais;
import br.com.potierp.model.TipoFeriadoEnum;
import br.com.potierp.util.DateUtil;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

/**
 * @author Doug
 *
 */
public class FeriadoMB extends BaseMB {

	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(FeriadoMB.class);

	private Feriado feriado = new Feriado();
	
	private Feriado feriadoSelecionado = new Feriado();
	
	private SelectionList<Feriado> listFeriado = new SelectionList<Feriado>();
	
	private List<SelectItem> itensPais = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensEstado = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensCidade = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensTipoFeriado = new ArrayList<SelectItem>();
	
	private String filtroData = "";
	
	private String filtroNome = "";
	
	private String filtroCidade = "";
	
	private String filtroEstado = "";
	
	private String filtroPais = ""; 
	
	@EJB
	private RhModulo rhModulo;
	
	@EJB
	private EnderecoModulo enderecoModulo;
	
	private boolean checkFeriadoAll = false;
	
	private Integer scrollerPage = 1;
	
	public FeriadoMB(){
		doNovo();
	}
	
	public void doNovo(){
		feriado = new Feriado();
		feriado.setCidade(new Cidade());
		feriado.setEstado(new Estado());
		checkFeriadoAll = false;
	}
	
	public void doSalvar(){
		try {
			verificarCidadeEstado();
			rhModulo.salvarFeriado(feriado);
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

	private void verificarCidadeEstado() {
		if(feriado.getCidade() != null && feriado.getCidade().getId() == null){
			feriado.setCidade(null);
		}
		if(feriado.getEstado() != null && feriado.getEstado().getId() == null){
			feriado.setEstado(null);
		}
	}

	public void doAlterar() throws Exception{
		doNovo();
		feriado = rhModulo.consultarFeriadoPorId(feriadoSelecionado.getId());
		popularCidade();
	}

	public void doExcluir(){
		try {
			rhModulo.excluirFeriado(feriado);
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
			List<Feriado> list = listFeriado.getItensSelecionados();
			rhModulo.excluirListaFeriado(list);
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
			List<Feriado> list = rhModulo.consultarTodosFeriados();
			listFeriado = new SelectionList<Feriado>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public void limparForm(){
		UIForm form = (UIForm) getFacesContext().getViewRoot().findComponent("formDetails");
		limparComponentesFormulario(form);
		doNovo();
		feriadoSelecionado = new Feriado();
	}
	
	public boolean filtraData(final Object current) {
		@SuppressWarnings("unchecked")
		Feriado feriadoAtual = ((SelectionEntity<Feriado>) current).getEntity();
		Date dataAtual = feriadoAtual.getData();
		if (filtroData == null || filtroData.length()==0) {
			return true;
	    }
		
		if(dataAtual == null)
			return false;
		
		if(DateUtil.dateToPtBrString(dataAtual).startsWith(filtroData.toString())){
			return true;
		}else{
			return false;
		} 
	}
	
	public boolean filtraNome(final Object current) {
		@SuppressWarnings("unchecked")
		Feriado feriadoAtual = ((SelectionEntity<Feriado>) current).getEntity();
		String nomeATual = feriadoAtual.getNome();
		if (filtroNome == null || filtroNome.length()==0) {
			return true;
	    }
		
		if(nomeATual == null)
			return false;
		
		if(nomeATual.startsWith(filtroNome.toString())){
			return true;
		}else{
			return false;
		} 
	}
	
	public boolean filtraCidade(final Object current) {
		@SuppressWarnings("unchecked")
		Feriado feriadoAtual = ((SelectionEntity<Feriado>) current).getEntity();
		String cidadeAtual = feriadoAtual.getCidade() != null?feriadoAtual.getCidade().getNome():null;
		if (filtroCidade == null || filtroCidade.length()==0) {
			return true;
	    }
		
		if(cidadeAtual == null)
			return false;
		
		if(cidadeAtual.startsWith(filtroCidade.toString())){
			return true;
		}else{
			return false;
		} 
	}
	
	public boolean filtraEstado(final Object current) {
		@SuppressWarnings("unchecked")
		Feriado feriadoAtual = ((SelectionEntity<Feriado>) current).getEntity();
		String estadoATual = feriadoAtual.getEstado() != null?feriadoAtual.getEstado().getNome():null;
		if (filtroEstado == null || filtroEstado.length()==0) {
			return true;
	    }
		
		if(estadoATual == null)
			return false;
		
		if(estadoATual.startsWith(filtroEstado.toString())){
			return true;
		}else{
			return false;
		} 
	}
	
	public boolean filtraPais(final Object current) {
		@SuppressWarnings("unchecked")
		Feriado feriadoAtual = ((SelectionEntity<Feriado>) current).getEntity();
		String paisATual = feriadoAtual.getPais() != null?feriadoAtual.getPais().getNome():null;
		if (filtroPais == null || filtroPais.length()==0) {
			return true;
	    }
		
		if(paisATual == null)
			return false;
		
		if(paisATual.startsWith(filtroPais.toString())){
			return true;
		}else{
			return false;
		} 
	}
	
	public SelectionList<Feriado> getListFeriado() {
		doConsultar();
		return listFeriado;
	}

	public void setListFeriado(final SelectionList<Feriado> listFeriado) {
		this.listFeriado = listFeriado;
	}

	public boolean isCheckFeriadoAll() {
		return checkFeriadoAll;
	}

	public void setCheckFeriadoAll(final boolean checkFeriadoAll) {
		this.checkFeriadoAll = checkFeriadoAll;
	}
	
	public Integer getScrollerPage() {
		return scrollerPage;
	}

	public void setScrollerPage(final Integer scrollerPage) {
		this.scrollerPage = scrollerPage;
	}
	
	public Feriado getFeriadoSelecionado() {
		return feriadoSelecionado;
	}

	public void setFeriadoSelecionado(final Feriado feriadoSelecionado) {
		this.feriadoSelecionado = feriadoSelecionado;
	}

	@Override
	public Logger getLogger() {
		return log;
	}
	
	public boolean isIncluirFeriado(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isAlterarFeriado(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_VALE_TRANSPORTE.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isExcluirFeriado(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isConsultarFeriado(){
		return isIncluirFeriado() || isExcluirFeriado() || isAlterarFeriado() || isConsultar();
	}
	
	public boolean isManterFeriado(){
		return isIncluirFeriado() || isAlterarFeriado();
	}
	
	public String doCadastroDeFeriado(){
		doConsultar();
		return NavigationEnum.CADASTRO_DE_FERIADO.getValor();
	}

	/**
	 * @return the feriado
	 */
	public Feriado getFeriado() {
		return feriado;
	}

	/**
	 * @param feriado the feriado to set
	 */
	public void setFeriado(final Feriado feriado) {
		this.feriado = feriado;
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
	
	/**
	 * @param itensEstado the itensEstado to set
	 */
	public void setItensEstado(final List<SelectItem> itensEstado) {
		this.itensEstado = itensEstado;
	}
	
	public void popularCidade() throws PotiErpException{
		List<Cidade> listCidades = getCidades();
		if(listCidades != null){
			itensCidade.clear();
			addMock(itensCidade, MockEnum.SELECIONE);
			for(Cidade cidade : listCidades){
				SelectItem item = new SelectItem(cidade, cidade.getNome());
				itensCidade.add(item);
			}
		}
	}
	
	private List<Cidade> getCidades() throws PotiErpException {
		if(feriado.getEstado() != null 
				&& feriado.getEstado().getId() != null
				&& !feriado.getEstado().getId().equals(Long.valueOf(0))){
			return enderecoModulo.consultarPorEstado(feriado.getEstado());
		}
		return null;
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

	/**
	 * @return the itensPais
	 */
	public List<SelectItem> getItensPais() {
		List<Pais> listPais = getPaises();
		if(listPais != null){
			itensPais.clear();
			addMock(itensPais, MockEnum.SELECIONE);
			for(Pais pais : listPais){
				SelectItem item = new SelectItem(pais, pais.getSigla());
				itensPais.add(item);
			}
		}
		return itensPais;
	}

	/**
	 * @return
	 */
	private List<Pais> getPaises() {
		try {
			List<Pais> listPais = this.enderecoModulo.buscarTodosPaises();
			Collections.sort(listPais);
			return listPais;
		} catch (PotiErpException e) {
			addMensagemErro(e);
			return null;
		}
	}

	/**
	 * @param itensPais the itensPais to set
	 */
	public void setItensPais(final List<SelectItem> itensPais) {
		this.itensPais = itensPais;
	}

	/**
	 * @return the itensCidade
	 */
	public List<SelectItem> getItensCidade() {
		if(itensCidade.isEmpty()){
			itensCidade.clear();
			addMock(itensCidade, MockEnum.SELECIONE);
		}
		return itensCidade;
	}

	/**
	 * @param itensCidade the itensCidade to set
	 */
	public void setItensCidade(final List<SelectItem> itensCidade) {
		this.itensCidade = itensCidade;
	}

	/**
	 * @return the filtroData
	 */
	public String getFiltroData() {
		return filtroData;
	}

	/**
	 * @param filtroData the filtroData to set
	 */
	public void setFiltroData(final String filtroData) {
		this.filtroData = filtroData;
	}

	/**
	 * @return the filtroNome
	 */
	public String getFiltroNome() {
		return filtroNome;
	}

	/**
	 * @param filtroNome the filtroNome to set
	 */
	public void setFiltroNome(final String filtroNome) {
		this.filtroNome = filtroNome;
	}

	/**
	 * @return the filtroCidade
	 */
	public String getFiltroCidade() {
		return filtroCidade;
	}

	/**
	 * @param filtroCidade the filtroCidade to set
	 */
	public void setFiltroCidade(final String filtroCidade) {
		this.filtroCidade = filtroCidade;
	}

	/**
	 * @return the filtroEstado
	 */
	public String getFiltroEstado() {
		return filtroEstado;
	}

	/**
	 * @param filtroEstado the filtroEstado to set
	 */
	public void setFiltroEstado(final String filtroEstado) {
		this.filtroEstado = filtroEstado;
	}

	/**
	 * @return the filtroPais
	 */
	public String getFiltroPais() {
		return filtroPais;
	}

	/**
	 * @param filtroPais the filtroPais to set
	 */
	public void setFiltroPais(final String filtroPais) {
		this.filtroPais = filtroPais;
	}

	/**
	 * @return the itensTipoFeriado
	 */
	public List<SelectItem> getItensTipoFeriado() {
		List<TipoFeriadoEnum> listTipoFeriado = getTipoFeriado();
		if(listTipoFeriado != null){
			itensTipoFeriado.clear();
			addMockSimple(itensTipoFeriado, MockEnum.SELECIONE);
			for(TipoFeriadoEnum tipoFeriado : listTipoFeriado){
				SelectItem item = new SelectItem(tipoFeriado, tipoFeriado.getTipoFeriado());
				itensTipoFeriado.add(item);
			}
		}
		return itensTipoFeriado;
	}

	/**
	 * @return
	 */
	private List<TipoFeriadoEnum> getTipoFeriado() {
		if(itensTipoFeriado.isEmpty()){ 
			return new ArrayList<TipoFeriadoEnum>(Arrays.asList(TipoFeriadoEnum.values()));
		}
		return null;
	}

	/**
	 * @param itensTipoFeriado the itensTipoFeriado to set
	 */
	public void setItensTipoFeriado(final List<SelectItem> itensTipoFeriado) {
		this.itensTipoFeriado = itensTipoFeriado;
	}
}

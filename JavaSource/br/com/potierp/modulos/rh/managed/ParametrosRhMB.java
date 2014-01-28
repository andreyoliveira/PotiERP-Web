package br.com.potierp.modulos.rh.managed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
import br.com.potierp.model.ParametrosRh;
import br.com.potierp.model.TipoParametroEnum;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class ParametrosRhMB extends BaseMB implements Serializable{

	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(ParametrosRhMB.class);

	private ParametrosRh parametrosRh = new ParametrosRh();
	
	private List<SelectItem> itensTipoParametro = new ArrayList<SelectItem>();
	
	private SelectionList<ParametrosRh> listParametrosRh = new SelectionList<ParametrosRh>();
	
	@EJB
	private RhModulo rhModulo;
	
	private Integer scrollerPage = 1;
	
	public ParametrosRhMB(){
		doNovo();
	}
	
	public void doNovo(){
		parametrosRh = new ParametrosRh();
	}
	
	public void doSalvar(){
		try {
			rhModulo.salvarParametrosRh(parametrosRh);
			doNovo();
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_SALVAR);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public void doConsultar(){
		try {
			List<ParametrosRh> list = rhModulo.consultarTodosParametrosRh();
			listParametrosRh = new SelectionList<ParametrosRh>(list);
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public ParametrosRh getParametrosRh() {
		return parametrosRh;
	}

	public void setParametrosRh(final ParametrosRh parametrosRh) {
		this.parametrosRh = parametrosRh;
	}

	public SelectionList<ParametrosRh> getListParametrosRh() {
		doConsultar();
		return listParametrosRh;
	}
	
	/**
	 * @return the itensTipoParametro
	 */
	public List<SelectItem> getItensTipoParametro() {
		List<TipoParametroEnum> listTipoParametro = getListTipoParametro();
		if(listTipoParametro != null){
			itensTipoParametro.clear();
			addMockSimple(itensTipoParametro, MockEnum.SELECIONE);
			for(TipoParametroEnum tipoParametro : listTipoParametro){
				SelectItem item = new SelectItem(tipoParametro, tipoParametro.getTipo());
				itensTipoParametro.add(item);
			}
		}
		return itensTipoParametro;
	}
	
	public List<TipoParametroEnum> getListTipoParametro() {
		if(itensTipoParametro.isEmpty()){ 
			return new ArrayList<TipoParametroEnum>(Arrays.asList(TipoParametroEnum.values()));
		}
		return null;
	}

	/**
	 * @param itensTipoParametro the itensTipoParametro to set
	 */
	public void setItensTipoParametro(final List<SelectItem> itensTipoParametro) {
		this.itensTipoParametro = itensTipoParametro;
	}

	public void setListParametrosRh(final SelectionList<ParametrosRh> listParametrosRh) {
		this.listParametrosRh = listParametrosRh;
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
	
	public boolean isIncluirParametrosRh(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_ADICIONAL.getCodigo());
	}
	
	public boolean isAlterarParametrosRh(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_ADICIONAL.getCodigo());
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_ADICIONAL.getCodigo());
	}
	
	public boolean isExcluirParametrosRh(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_ADICIONAL.getCodigo());
	}
	
	public boolean isConsultarParametrosRh(){
		return isIncluirParametrosRh() || isExcluirParametrosRh() || isAlterarParametrosRh() || isConsultar();
	}
	
	public boolean isManterParametrosRh(){
		return isIncluirParametrosRh() || isAlterarParametrosRh();
	}
	
	public String doParametrosRh(){
		return NavigationEnum.PARAMETROS_RH.getValor();
	}
}
package br.com.potierp.modulos.rh.managed.dataprovider;

import java.util.List;

import org.richfaces.model.DataProvider;

import br.com.potierp.business.rh.facade.RhModulo;
import br.com.potierp.infra.helper.SelectionEntity;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.infra.log.TraceInfo;
import br.com.potierp.model.Cliente;
import br.com.potierp.model.Funcionario;
import br.com.potierp.model.SituacaoFuncionario;

public class FuncionarioDataProvider implements DataProvider<SelectionEntity<Funcionario>>{
	
	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = 1L;
	
	public enum TipoBuscaFuncionarios{
		
		ATIVOS,
		
		INATIVOS,
		
		TODOS,
		
		PESQUISA;
	}
	
	private RhModulo rhModulo;
	
	private TipoBuscaFuncionarios tipoBusca;
	
	private Cliente cliente;
	
	private SituacaoFuncionario situacaoFuncionario;
	
	private SelectionList<Funcionario> selectionListFuncionario;
	
	private TraceInfo traceInfo;
	
	public FuncionarioDataProvider(final TipoBuscaFuncionarios tipoBusca, final RhModulo rhModulo, final TraceInfo traceInfo){
		super();
		this.tipoBusca = tipoBusca;
		this.rhModulo = rhModulo;
		this.traceInfo = traceInfo;
	}

	public FuncionarioDataProvider(final TipoBuscaFuncionarios tipoBusca,
			final Cliente cliente,
			final SituacaoFuncionario situacaoFuncionario,
			final RhModulo rhModulo,
			final TraceInfo traceInfo) {
		super();
		this.tipoBusca = tipoBusca;
		this.rhModulo = rhModulo;
		this.cliente = cliente;
		this.situacaoFuncionario = situacaoFuncionario;
		this.traceInfo = traceInfo;
	}

	@Override
	public List<SelectionEntity<Funcionario>> getItemsByRange(final int firstRow, final int lastRow){
		try{
			List<Funcionario> funcionarios = null;
			//if(tipoBusca == TipoBuscaFuncionarios.PESQUISA && (cliente != null || situacaoFuncionario != null)){
				funcionarios = rhModulo.consultarFuncionariosPesquisa(cliente, situacaoFuncionario);
			//}else{
				//funcionarios = rhModulo.consultarTodosFuncionarios(firstRow, lastRow);
			//}
			return selectionListFuncionario = new SelectionList<Funcionario>(funcionarios);
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Funcionario> getItensSelecionados(){
		try {
			return this.selectionListFuncionario.getItensSelecionados();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int getRowCount() {
		try{
			Number count;
			if(selectionListFuncionario == null){
				getItemsByRange(0,10000);
			}
			count = selectionListFuncionario.size();
			return count != null?count.intValue():0;
		} catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public SelectionEntity<Funcionario> getItemByKey(final Object key) {
		try{
			return new SelectionEntity<Funcionario>(rhModulo.consultarFuncionario((Long)key, traceInfo));
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Object getKey(final SelectionEntity<Funcionario> funcionario) {
		return funcionario.getEntity().getId();
	}
	
}
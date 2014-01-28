package br.com.potierp.modulos.rh.managed.dataprovider;

import java.util.List;

import org.richfaces.model.DataProvider;

import br.com.potierp.business.rh.facade.RhModulo;
import br.com.potierp.infra.helper.SelectionEntity;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.model.Cliente;
import br.com.potierp.model.Funcionario;

public class ClienteDataProvider implements DataProvider<SelectionEntity<Cliente>>{
	
	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = 1L;
	
	public enum TipoBuscaClientes{
		
		ATIVOS,
		
		INATIVOS,
		
		TODOS;
	}
	
	private RhModulo rhModulo;
	
	private TipoBuscaClientes tipoBusca;
	
	private SelectionList<Cliente> selectionListCliente;
	
	public ClienteDataProvider(final TipoBuscaClientes tipoBusca, final RhModulo rhModulo){
		super();
		this.tipoBusca = tipoBusca;
		this.rhModulo = rhModulo;
	}

	@Override
	public List<SelectionEntity<Cliente>> getItemsByRange(final int firstRow, final int lastRow){
		try{
			List<Cliente> clientes;
			if(tipoBusca == TipoBuscaClientes.ATIVOS){
				clientes = rhModulo.consultarClientesAtivos(firstRow, lastRow);
			}else if(tipoBusca == TipoBuscaClientes.INATIVOS){
				clientes = rhModulo.consultarClientesInativos(firstRow, lastRow);
			}else{
				clientes = rhModulo.consultarTodosClientes(firstRow, lastRow);
			}
			return this.selectionListCliente = new SelectionList<Cliente>(clientes);
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Cliente> getItensSelecionados(){
		try {
			return this.selectionListCliente.getItensSelecionados();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int getRowCount() {
		try{
			if(tipoBusca == TipoBuscaClientes.ATIVOS){
				return rhModulo.consultarTotalClientesAtivos().intValue();
			}else if(tipoBusca == TipoBuscaClientes.INATIVOS){
				return rhModulo.consultarTotalClientesInativos().intValue();
			}else{
				return rhModulo.consultarTotalClientes().intValue();
			}
		} catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public SelectionEntity<Cliente> getItemByKey(final Object key) {
		try{
			return new SelectionEntity<Cliente>(rhModulo.consultarClientePorId((Long)key));
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Object getKey(final SelectionEntity<Cliente> cliente) {
		return cliente.getEntity().getId();
	}

}

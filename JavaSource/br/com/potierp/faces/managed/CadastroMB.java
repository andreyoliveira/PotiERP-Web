package br.com.potierp.faces.managed;

import java.util.List;

/**
 * @author  
 * 
 * $LastChangedBy: $
 * 
 * $LastChangedDate: $
 * <br>
 * Contrato com regra de operações CRUD para mbeans.
 */
public interface CadastroMB {

	/**
	 * @return Action Salvar.
	 */
	void doSalvar();

	/**
	 * @return Action Excluir
	 */
	String doExcluir();

	/**
	 * @return Action Editar.
	 */
	String doEditar();

	/**
	 * @return Action Listar
	 */
	List<?> getLista();

}
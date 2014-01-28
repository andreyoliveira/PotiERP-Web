package br.com.potierp.infra.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Lista de SelectionEntity.
 * 
 * @param <T>
 * @author walter.okuma
 * 20/07/2010 01:29:37
 *         <p>
 *         $LastChangedBy: walter.okuma@UMESP.EDU.DTI $
 *         <p>
 *         $LastChangedDate: 2010-07-19 22:44:16 -0300 (seg, 19 jul 2010) $
 */
public class SelectionList<T> extends ArrayList<SelectionEntity<T>> {

	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = -5068148474026643266L;
	
	/**
	 * Construtor.
	 */
	public SelectionList() {
	}
	
	/**
	 * Construtor.
	 */
	public SelectionList(final List<T> lista) {
		for(T t: lista) {
			super.add(new SelectionEntity<T>(t));
		}
	}
	
	/**
	 * Obtem a entity que est� no �ndice passado como par�metro.
	 * @param index
	 * @return
	 */
	public T getEntity(final int index) {
		return this.get(index).getEntity();
	}
	
	/**
	 * Verifica se h� item selecionado.
	 * @return
	 */
	public boolean containsSelecionado() {
		for(SelectionEntity<T> selection: this) {
			if(selection.isSelecionado()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Retorna uma lista com os itens selecionados.
	 * @return
	 */
	public List<T> getItensSelecionados() {
		List<T> lista = new ArrayList<T>();
		for(SelectionEntity<T> selection: this) {
			if(selection.isSelecionado()) {
				lista.add(selection.getEntity());
			}
		}
		return lista;
	}
	
	public void removeSelectedItem() {
		List<SelectionEntity<T>> selectedList = new ArrayList<SelectionEntity<T>>();
		for(SelectionEntity<T> selection: this) {
			if(selection.isSelecionado()) {
				selectedList.add(selection);
			}
		}
		this.removeAll(selectedList);
	}
	
	/**
	 * Retorna uma lista com todos os itens.
	 * @return
	 */
	public List<T> getAllItens() {
		List<T> lista = new ArrayList<T>();
		for(SelectionEntity<T> selection: this) {
			lista.add(selection.getEntity());
		}
		return lista;
	}
}
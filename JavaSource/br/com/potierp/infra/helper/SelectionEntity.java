package br.com.potierp.infra.helper;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Helper que serve para representar entidades que podem ser selecionadas.
 * @author estevao.costa
 * 19/07/2010
 *  	   <p>
 *         $LastChangedBy: walter.okuma@UMESP.EDU.DTI $
 *         <p>
 *         $LastChangedDate: 2010-07-20 18:21:41 -0300 (ter, 20 jul 2010) $
 * @param <T>
 */
public class SelectionEntity<T> implements Serializable {

	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = -9152404807068587936L;
	 
	/**
	 * Flag indicando se a Entidade est� selecionada.
	 */
	private boolean selecionado;

	/**
	 * Entidade.
	 */
	private T entity;
	
	/**
	 * Construtor Default.
	 */
	public SelectionEntity() {
	}
	
	/**
	 * Construtor que recebe a entity.
	 * @param entity
	 */
	public SelectionEntity(final T entity){
		this.entity = entity;
	}

	/**
	 * @return the selecionado
	 */
	public boolean isSelecionado() {
		return selecionado;
	}

	/**
	 * @param selecionado the selecionado to set
	 */
	public void setSelecionado(final boolean selecionado) {
		this.selecionado = selecionado;
	}

	/**
	 * @return the entity
	 */
	public T getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(final T entity) {
		this.entity = entity;
	}

	/** 
	 * (non-Javadoc).
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(41, 83)
		.append(this.getEntity())
		.append(this.isSelecionado()).toHashCode();
	}

	/** 
	 * (non-Javadoc).
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		final SelectionEntity<T> outro = (SelectionEntity<T>) obj;
		return new EqualsBuilder()
			.append(this.getEntity(), outro.getEntity())
			.append(this.isSelecionado(), outro.isSelecionado()).isEquals();
	}

	/** 
	 * (non-Javadoc).
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this)
		.append(this.getEntity())
		.append(this.isSelecionado()).toString();
	}
}
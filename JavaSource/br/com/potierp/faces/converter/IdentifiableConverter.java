package br.com.potierp.faces.converter;

import java.util.Collection;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;

import br.com.potierp.core.Identifiable;

/**
 * @author 
 *
 * $LastChangedBy: $
 *
 * $LastChangedDate: $
 * <pre>
 * Converter genérico para elementos que implementam a interface Identifiable.
 * Busca através da expression language fornecida os elementos no Managed-Bean e os identifica pela da sua identidade.
 * </pre>
 */
public class IdentifiableConverter extends BaseConverter {

	/**
	 * Constante que representa a propriedade value.
	 */
	private static final String PROPRIEDADE_VALOR = "value"; 
	/**
	 * (non-Javadoc).
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, 
	 * 	javax.faces.component.UIComponent, java.lang.Object)
	 */
	public String getAsString(final FacesContext context, final UIComponent component, final Object obj) {
		if (obj instanceof Identifiable) {
			Identifiable entity = (Identifiable) obj;
			return entity.getIdentity().toString();
		}
		return null;
	}
	
	/**
	 * (non-Javadoc).
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, 
	 * 	javax.faces.component.UIComponent, java.lang.String)
	 */
	public Object getAsObject(final FacesContext context, final UIComponent component, final String key) {
		try {
			if(!isEntitySelected(key)) {
				return null;
			}
			Collection<Object> identifiableList = getIdentifiableList(context, component);
			return findIdentifiable(key, identifiableList);
		} catch (Exception e) {
			throw new ConverterException(new FacesMessage("Erro ao converter Entidade."));
		}
	}

	/**
	 * Verifica se alguma entidade foi selecionada.
	 * @param key
	 */
	private boolean isEntitySelected(final String key) {
		return key != null && !"".equals(key.trim()) && !MockEnum.isMock(key);
	}
	
	/**
	 * Encontra o identifiable na lista.
	 * @param identificador
	 * @param itens
	 * @return
	 */
	private Object findIdentifiable(final String identificador, final Collection<Object> itens) {
		if (itens != null) {
			for(Object item : itens) {
				Identifiable identifiable = getIdentifiable(item);
				if (identificador.equals(identifiable.getIdentity().toString())) {
					return identifiable;
				}
			}
		}
		return null;
	}

	/**
	 * Obtem a instancia de Identifiable.
	 * O identifiable pode vir na sua forma natural ou encapsulada em um SelectItem.
	 * @param item
	 * @return
	 */
	private Identifiable getIdentifiable(final Object item) {
		Identifiable entity = null;
		if (item instanceof SelectItem){
			SelectItem si = (SelectItem) item;
			entity = (Identifiable) si.getValue();
		}else if (item instanceof Identifiable){
			entity = (Identifiable) item;
		}
		return entity;
	}

	/**
	 * Através da EL do componente, obtem os itens do MB.
	 * 
	 * @param context
	 * @param component
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Collection<Object> getIdentifiableList(final FacesContext context, final UIComponent component) {
		for(UIComponent filho : component.getChildren()) {
			if (filho instanceof UISelectItems) {
				ValueExpression el = filho.getValueExpression(PROPRIEDADE_VALOR);
				ELContext elContext = context.getELContext();
				return (Collection<Object>)el.getValue(elContext);
			}
		}
		ValueExpression el = component.getValueExpression(PROPRIEDADE_VALOR);
		ELContext elContext = context.getELContext();
		return (Collection<Object>)el.getValue(elContext);
	}
}
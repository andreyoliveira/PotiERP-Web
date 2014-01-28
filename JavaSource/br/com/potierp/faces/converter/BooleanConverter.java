package br.com.potierp.faces.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * Classe responsável pela conversão de true/false para Sim/Não.
 *
 * @author 
 *
 *  	   <p>
 *         $LastChangedBy: $
 *         <p>
 *         $LastChangedDate: $
 */
public class BooleanConverter extends BaseConverter {

	/**
	 * Sim.
	 */
	private static final String SIM = "Sim";

	/**
	 * Nao.
	 */
	private static final String NAO = "Não";

	
	/**
	 * Metodo responsável pela conversão de String.
	 * Converte:
	 * Sim => True
	 * Não => False
	 */
	@Override
	public Object getAsObject(final FacesContext facesContext, final UIComponent component, final String isBoolean) {
		if(isBoolean != null && !"".equalsIgnoreCase(isBoolean)){
			return isBoolean.equalsIgnoreCase(SIM) ? true : false;
		}
		return isBoolean;
	}

	/**
	 * Metodo responsável pela conversão do boolean.
	 * Converte:
	 * true => Sim
	 * false => Não
	 */
	@Override
	public String getAsString(final FacesContext facesContext, final UIComponent component, final Object isBoolean) {
		if(isBoolean instanceof Boolean){
			Boolean isAtivo = (Boolean)isBoolean;
			if(isAtivo != null){
				return isAtivo.equals(true) ? SIM : NAO;
			}
		}
		return null;
	}
}
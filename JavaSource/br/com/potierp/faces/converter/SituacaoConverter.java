package br.com.potierp.faces.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * Classe responsável pela conversão de S/N para Ativo/Inativo.
 *
 * @author 
 * 16/06/2010
 *  	   <p>
 *         $LastChangedBy: $
 *         <p>
 *         $LastChangedDate: $
 */
public class SituacaoConverter extends BaseConverter {

	/**
	 * Ativo.
	 */
	private static final String ATIVO = "Ativo";

	/**
	 * Inativo.
	 */
	private static final String INATIVO = "Inativo";

	/**
	 * Sim.
	 */
	private static final String SIM = "S";

	/**
	 * Não.
	 */
	private static final String NAO = "N";

	/**
	 * Metodo responsável pela conversão da situação.
	 * Converte:
	 * Ativo => S
	 * Inativo => N
	 */
	@Override
	public Object getAsObject(final FacesContext facesContext, final UIComponent component, final String isAtivo) {
		if(isAtivo != null && !"".equalsIgnoreCase(isAtivo)){
			return isAtivo.equalsIgnoreCase(ATIVO) ? SIM : NAO;
		}
		return isAtivo;
	}

	/**
	 * Metodo responsável pela conversão da situação.
	 * Converte:
	 * S => Ativo
	 * N => Inativo
	 */
	@Override
	public String getAsString(final FacesContext facesContext, final UIComponent component, final Object situacao) {
		if(situacao instanceof String){
			String isAtivo = (String)situacao;
			if(isAtivo != null && !"".equalsIgnoreCase(isAtivo)){
				return isAtivo.equalsIgnoreCase(SIM) ? ATIVO : INATIVO;
			}
		}
		return null;
	}
}
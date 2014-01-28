package br.com.potierp.faces.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.com.potierp.infra.msg.MensagensFacesEnum;

import com.sun.faces.util.MessageFactory;

/**
 * Valida se o dia informado esta dentro do padrão de 31 dias.
 *
 * @author renan
 * 
 */
public class DiaDoMesValidator implements Validator {

	/**
	 * (non-Javadoc).
	 * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	public void validate(final FacesContext context, final UIComponent component, final Object value) {
		if (value instanceof Integer) {
			if(((Integer) value).intValue() == 0 || ((Integer) value).intValue() > 30){
				geraMensagemErro(context, component);
			}
		}
	}
	
	/**
	 * Gera Mensagem de Erro da validação.
	 * 
	 * @param context
	 * @param component
	 */
	void geraMensagemErro(final FacesContext context, final UIComponent component) {
		throw new ValidatorException(
				MessageFactory.getMessage(context,
						MensagensFacesEnum.DIA_DO_MES_DEVE_ESTAR_ENTRE_UM_E_TRINTA.getKey(),
						MessageFactory.getLabel(context, component)));
	}
}
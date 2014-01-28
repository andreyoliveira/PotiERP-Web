package br.com.potierp.faces.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.com.potierp.infra.msg.MensagensFacesEnum;

import com.sun.faces.util.MessageFactory;

/**
 * Valida se o campo foi preenchido apenas com espaços em branco.
 *
 * @author
 *  	   <p>
 *         $LastChangedBy: $
 *         <p>
 *         $LastChangedDate: $
 */
public class EspacoValidator implements Validator {

	/**
	 * Identificador do validator.
	 */
	public static final String VALIDATOR_ID = "EspacoValidator";

	/**
	 * (non-Javadoc).
	 * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	public void validate(final FacesContext context, final UIComponent component, final Object value) {
		boolean valido = true;
		if (value == null) {
			valido = false;
		}
		if (value instanceof String) {
			String valor = (String) value;
			if (valor.trim().equals("")) {
				valido = false;
			}
		}
		if (!valido) {
			geraMensagemErro(context, component);
		}
	}

	/**
	 * Gera Mensagem de Erro da validação.
	 * @param context
	 * @param component
	 */
	void geraMensagemErro(final FacesContext context, final UIComponent component) {
		throw new ValidatorException(
				MessageFactory.getMessage(context,
						MensagensFacesEnum.CAMPO_NAO_ACEITA_SOMENTE_ESPACOS.getKey(),
						MessageFactory.getLabel(context, component)));
	}
}
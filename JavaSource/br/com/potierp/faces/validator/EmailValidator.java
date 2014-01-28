package br.com.potierp.faces.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.com.potierp.infra.msg.MensagensFacesEnum;

import com.sun.faces.util.MessageFactory;

/**
 * Valida se o email esta correto.
 *
 * @author renan
 * 
 */
public class EmailValidator implements Validator {

	/**
	 * Regex que valida o conteudo do texto.
	 */
	public static final String REGEX = ".+@.+\\.[a-z]+";

	/**
	 * (non-Javadoc).
	 * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	public void validate(final FacesContext context, final UIComponent component, final Object value) {
		if (value instanceof String) {
			Pattern p = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(value.toString());
			if (!m.matches())
				geraMensagemErro(context, component);
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
						MensagensFacesEnum.EMAIL_INVALIDO.getKey(),
						MessageFactory.getLabel(context, component)));
	}
}
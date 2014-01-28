package br.com.potierp.faces.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * Valida se o campo html foi preenchido apenas com espa�os em branco.
 *
 * @author fabio.masson
 * 06/05/2010
 *  	   <p>
 *         $LastChangedBy: fabio.masson@UMESP.EDU.DTI $
 *         <p>
 *         $LastChangedDate: 2010-05-06 14:48:11 -0300 (qui, 06 mai 2010) $
 */
public class EspacoHtmlValidator extends EspacoValidator {

	/**
	 * Identificador do validator.
	 */
	public static final String VALIDATOR_ID = "EspacoHtmlValidator";

	/**
	 * Regex que valida o conteudo do texto.
	 */
	public static final String REGEX = "<[^img].*?>|\\s|&nbsp;|#000000";

	/**
	 * (non-Javadoc).
	 * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public void validate(final FacesContext context, final UIComponent component, final Object value) {
		super.validate(context, component, value);
		if (value instanceof String) {
			Pattern p = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(value.toString());
			if (m.replaceAll("").length() == 0)
				super.geraMensagemErro(context, component);
		}
	}
}
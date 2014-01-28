package br.com.potierp.faces.validator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.com.potierp.infra.msg.MensagensFacesEnum;

import com.sun.faces.util.MessageFactory;

/**
 * @author 
 * 
 * $LastChangedBy: 
 * $LastChangedDate: 
 * <br>
 * 
 * Validator para CNPJ.
 */
public class CnpjValidator implements Validator  {
	
	
	/**
	 * (non-Javadoc).
	 * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	public void validate(final FacesContext context, final UIComponent component, final Object value) {
		boolean valido = validaCNPJ(String.valueOf(value));

		if (!valido){
			geraMensagemErro(context, component);
		}
	}

	/**
	 * Valida CNPJ do usuário.
	 * 
	 * @param cnpj String valor com 14 dígitos
	 */
	public static boolean validaCNPJ(final String cnpj) {
		if (cnpj == null || cnpj.length() != 14)
			return false;

		try {
			Long.parseLong(cnpj);
		} catch (NumberFormatException e) { // CNPJ não possui somente números
			return false;
		}

		if(cnpj.equals("00000000000000")){
			return false;
		}
		
		int soma = 0;
		String zero = "0";
		String cnpjCalc = cnpj.substring(0, 12);
		char[] chrCnpj = cnpj.toCharArray();
		for (int i = 0; i < 4; i++)
			if (chrCnpj[i] - 48 >= 0 && chrCnpj[i] - 48 <= 9)
				soma += (chrCnpj[i] - 48) * (6 - (i + 1));

		for (int i = 0; i < 8; i++)
			if (chrCnpj[i + 4] - 48 >= 0 && chrCnpj[i + 4] - 48 <= 9)
				soma += (chrCnpj[i + 4] - 48) * (10 - (i + 1));

		int dig = 11 - soma % 11;
		cnpjCalc = (new StringBuilder(String.valueOf(cnpjCalc))).append(
				dig != 10 && dig != 11 ? Integer.toString(dig) : zero)
				.toString();
		soma = 0;
		for (int i = 0; i < 5; i++)
			if (chrCnpj[i] - 48 >= 0 && chrCnpj[i] - 48 <= 9)
				soma += (chrCnpj[i] - 48) * (7 - (i + 1));

		for (int i = 0; i < 8; i++)
			if (chrCnpj[i + 5] - 48 >= 0 && chrCnpj[i + 5] - 48 <= 9)
				soma += (chrCnpj[i + 5] - 48) * (10 - (i + 1));

		dig = 11 - soma % 11;
		cnpjCalc = (new StringBuilder(String.valueOf(cnpjCalc))).append(
				dig != 10 && dig != 11 ? Integer.toString(dig) : zero)
				.toString();

		if (!cnpj.equals(cnpjCalc))
			return false;
		
		return true;
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
						MensagensFacesEnum.CNPJ_INVALIDO.getKey(), 
						MessageFactory.getLabel(context, component)));
	}

}
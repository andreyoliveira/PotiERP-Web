package br.com.potierp.faces.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.util.PotiErpResource;

/**
 * @author 
 * 
 * $LastChangedBy: 
 * $LastChangedDate: 
 * <br>
 * Conveter para CPF.
 */
public class CpfConverter extends BaseConverter {
	
	/**
	 * Irá converter CPF formatado para um sem pontos e traço.
	 * Ex.: 075.374.998-33 torna-se 07537499833.
	 */
	public Object getAsObject(final FacesContext context, final UIComponent component, final String key) {
		if (key == null || "".equals(key.trim())) {
			return null;
		}
		try {
			String cpf = key;
	         if (key!= null && !key.equals("")){
	        	 cpf = cpf.replaceAll("\\.","");
	        	 cpf = cpf.replaceAll("\\-","");
	         }

	         return cpf;
		} catch (Exception e) {
			throw new ConverterException(new FacesMessage(
					getMensagem(MensagensFacesEnum.ERRO_CONVERTER_ENTIDADE.getKey(), PotiErpResource.getResource())));
		}
	}

	/**
	 * Irá converter CPF não formatado para um com pontos e traço.
	 * Ex.: 07537499833 torna-se 075.374.998-33.
	 */
	public String getAsString(final FacesContext context, final UIComponent component, final Object obj) {
		try {
			String cpf = obj.toString();
			String ponto = ".";
			if (obj != null && cpf.length() == 11)
				cpf = cpf.substring(0, 3) 
					+ ponto + cpf.substring(3, 6) + ponto
					+ cpf.substring(6, 9) + "-" + cpf.substring(9, 11);
	
			return cpf;
		} catch (Exception e) {
			throw new ConverterException(new FacesMessage(
					getMensagem(MensagensFacesEnum.ERRO_CONVERTER_ENTIDADE.getKey(), PotiErpResource.getResource())));
		}
	}
}
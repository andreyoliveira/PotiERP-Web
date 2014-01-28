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
 * $LastChangedBy: $ 
 * $LastChangedDate: $ 
 * <br>
 * Conveter para CEP.
 */
public class CepConverter extends BaseConverter {
	
	 /**
	  * Irá converter CEP formatado para um sem traço.
	  * Ex.: 09853-040 torna-se 09853040.
	  */
	public Object getAsObject(final FacesContext context, final UIComponent component, final String value){
		try{ 
			 String cep = value;
			 if (value!= null && !value.equals(""))
			       cep = cep.replaceAll("\\-", "");
		 
			  return cep;
		} catch (Exception e) {
			throw new ConverterException(new FacesMessage(
					getMensagem(MensagensFacesEnum.ERRO_CONVERTER_ENTIDADE.getKey(), PotiErpResource.getResource())));
		}
     }
 
	 /**
	  * Irá converter CEP não formatado para um com traço.
	  * Ex.: 09853040 torna-se 09853-040.
	  */
     public String getAsString(final FacesContext context, final UIComponent component, final Object value){
    	try {
			String cep = value.toString();
			if (cep != null && cep.length() == 8)
			cep = cep.substring(0, 5) +
				"-" + cep.substring(5);
			return cep;
		} catch (Exception e) {
			throw new ConverterException(new FacesMessage(
					getMensagem(MensagensFacesEnum.ERRO_CONVERTER_ENTIDADE.getKey(), PotiErpResource.getResource())));
		}
     }
}
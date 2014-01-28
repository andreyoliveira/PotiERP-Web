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
 * Conveter para CNPJ.
 */
public class CnpjConverter extends BaseConverter {
	
	 /**
	  * Irá converter CNPJ formatado para um sem pontos, traço e barra.
	  * Ex.: 07.374.998/0001-33 torna-se 07374998000133.
	  */
	public Object getAsObject(final FacesContext context, final UIComponent component, final String value){
		try{ 
			 String cnpj = value;
			 if (value!= null && !value.equals(""))
			       cnpj = cnpj.replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\/", "");
		 
			  return cnpj;
		} catch (Exception e) {
			throw new ConverterException(new FacesMessage(
					getMensagem(MensagensFacesEnum.ERRO_CONVERTER_ENTIDADE.getKey(), PotiErpResource.getResource())));
		}
     }
 
	 /**
	  * Irá converter CNPJ não formatado para um com pontos, traço e barra.
	  * Ex.: 07374998000133 torna-se 07.374.998/0001-33.
	  */
     public String getAsString(final FacesContext context, final UIComponent component, final Object value){
    	try {
			String cnpj = value.toString();
			String ponto = ".";
			if (cnpj != null && cnpj.length() == 14)
			cnpj = cnpj.substring(0, 2) 
				+ ponto 
				+ cnpj.substring(2, 5) 
				+ ponto 
				+ cnpj.substring(5, 8) +
				"/" + cnpj.substring(8, 12) +
				"-" + cnpj.substring(12, 14);
			 
			return cnpj;
		} catch (Exception e) {
			throw new ConverterException(new FacesMessage(
					getMensagem(MensagensFacesEnum.ERRO_CONVERTER_ENTIDADE.getKey(), PotiErpResource.getResource())));
		}
     }
}
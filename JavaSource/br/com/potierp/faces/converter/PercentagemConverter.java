package br.com.potierp.faces.converter;

import java.math.BigDecimal;

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
 * Conveter para Percentagem.
 */
public class PercentagemConverter extends BaseConverter{
	
	 /**
	  * Irá converter no formatado para um sem ponto ou virgula
	  * Ex.: 20,30 torna-se 2030.
	  */
	@Override
	public Object getAsObject(final FacesContext facesContext,
			final UIComponent uiComponent, final String value) {
		if (value != null) {
			String recebeValor = value.replaceAll(",", ".").replaceAll("%", "").trim();
			if (recebeValor.length() > 0) {
				try {
					return new BigDecimal(recebeValor);
				} catch (Exception e) {
					throw new ConverterException(new FacesMessage(
							getMensagem(MensagensFacesEnum.ERRO_CONVERTER_ENTIDADE.getKey(), PotiErpResource.getResource())));
				}
			}
		}
		return null;
	}

	/**
	 * Irá converter o percentual não formatado para um com virgula.
	 * Ex.: 5555 torna-se 55,55.
	 */
	@Override
	public String getAsString(final FacesContext facesContext,
			final UIComponent uiComponent, final Object value) {
		try {
			
			if (value != null) {
				if(value instanceof BigDecimal){
					String valor = value.toString();
					return valor.replaceAll(".", ",").concat("%");
				}
			}
			return "";
		} catch (Exception e) {
			throw new ConverterException(new FacesMessage(
					getMensagem(MensagensFacesEnum.ERRO_CONVERTER_ENTIDADE.getKey(), PotiErpResource.getResource())));
		}
	}
}
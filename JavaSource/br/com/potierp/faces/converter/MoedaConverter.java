package br.com.potierp.faces.converter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.util.PotiErpResource;

/**
 * Converter de Moeda.
 * 
 * @author
 *  	   <p>
 *         $LastChangedBy: $
 *         <p>
 *         $LastChangedDate: $
 */
public class MoedaConverter extends BaseConverter{
	
	/**
	 * Irá converter no formatado para um sem ponto ou virgula.<br> 
	 * Ex.: 20,30 torna-se 2030.
	 * 
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.String)
	 */
	public Object getAsObject(final FacesContext facesContext,
			final UIComponent uiComponent, final String value) {

		FacesContext fc = FacesContext.getCurrentInstance();
		Locale l = fc.getViewRoot().getLocale();

		if (value != null) {
			String recebeValor = value.trim();
			if (recebeValor.length() > 0) {
				try {
					return new BigDecimal(NumberFormat.getNumberInstance(l).parse(recebeValor).toString()).setScale(4, BigDecimal.ROUND_HALF_EVEN);
				} catch (Exception e) {
					throw new ConverterException(new FacesMessage(
							getMensagem(MensagensFacesEnum.ERRO_CONVERTER_ENTIDADE.getKey(), PotiErpResource.getResource())));
					
				}
			}
		}
		return null;
	}

	/**
	 * Irá converter a moeda não formatada para uma com virgula.<br> 
	 * Ex.: 5555 torna-se 55,55.
	 * 
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.Object)
	 */
	public String getAsString(final FacesContext facesContext,
			final UIComponent uiComponent, final Object value) {
		try {
			
			if (value == null) {
				return "";
			}
			if (value instanceof String) {
				return (String) value;
			}

			FacesContext fc = FacesContext.getCurrentInstance();
			Locale l = fc.getViewRoot().getLocale();

			NumberFormat formatador = NumberFormat.getNumberInstance(l);
			formatador.setMinimumFractionDigits(2);
			formatador.setMaximumFractionDigits(4);
			formatador.setGroupingUsed(true);

			return formatador.format(value);

		} catch (Exception e) {
			throw new ConverterException(new FacesMessage(
					getMensagem(MensagensFacesEnum.ERRO_CONVERTER_ENTIDADE.getKey(), PotiErpResource.getResource())));
		}
	}
}
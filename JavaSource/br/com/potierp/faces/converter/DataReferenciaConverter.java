package br.com.potierp.faces.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.util.PotiErpResource;

/**
 * @author
 *  	   <p>
 *         $LastChangedBy: $
 *         <p>
 *         $LastChangedDate: $
 *
 * Converter Data de referência.
 */
public class DataReferenciaConverter extends BaseConverter {

	/**
	 * Irá converter a data de referencia para um sem barra e ano na frente do mês.
	 * Ex.: 07/2010 torna-se 201007
	 */
	public Object getAsObject(final FacesContext context, final UIComponent component, final String key) {
		if (key == null || "".equals(key.trim())) {
			return null;
		}
		try {
			String dataReferencia = key;
			if (key!= null && !key.equals("")){
				dataReferencia = dataReferencia.replaceAll("/","");
				dataReferencia = dataReferencia.substring(2,6) + dataReferencia.substring(0,2);
			}
			
			return dataReferencia;
		} catch (Exception e) {
			throw new ConverterException(new FacesMessage(
					getMensagem(MensagensFacesEnum.ERRO_CONVERTER_ENTIDADE.getKey(), PotiErpResource.getResource())));
		}
	}
	
	/**
	 * Irá converter a data de referência sem barras para um com barras.
	 * Ex.: 201008 torna-se 08/2010.
	 */
	public String getAsString(final FacesContext context, final UIComponent component, final Object obj) {
		try {
			String dataReferencia = obj.toString();
			String barra = "/";
			if (obj != null && dataReferencia.length() == 6)
				dataReferencia = dataReferencia.substring(4, 6) 
					+ barra + dataReferencia.substring(0, 4);
			
			return dataReferencia;
		} catch (Exception e) {
			throw new ConverterException(new FacesMessage(
					getMensagem(MensagensFacesEnum.ERRO_CONVERTER_ENTIDADE.getKey(), PotiErpResource.getResource())));
		}
	}
}
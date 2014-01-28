package br.com.potierp.faces.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.util.PotiErpResource;

/**
 * Converter de Horários.
 * 
 * @author
 *  	   <p>
 *         $LastChangedBy: $
 *         <p>
 *         $LastChangedDate: $
 */
public class HorarioStringConverter extends BaseConverter {
	
	/**
	 * String que representa o ":" .
	 */
	private static final String DOIS_PONTOS = ":";

	/**
	 * Irá retirar a formatação de um horário tirando os dois pontos
	 * Ex. 23:30 para 2330. 
	 */
	public Object getAsObject(final FacesContext facesContext,
			final UIComponent uiComponent, final String value) {

		try {
			String horario = value;
			return horario.replaceAll(DOIS_PONTOS, "");
		} catch (Exception ex) {
			throw new ConverterException(new FacesMessage(
					MensagensFacesEnum.ERRO_CONVERTER_ENTIDADE.getKey(),
					PotiErpResource.getResource()));
		}
	}

	/**
	 * Irá converter um horário não formatado para um horário formatado.
	 * Ex. 2330 ficará 23:30.
	 */
	public String getAsString(final FacesContext facesContext,
			final UIComponent uiComponent, final Object value) {
		try {
			if(value instanceof String) {
				if(value != null) {
					if(!value.toString().equals("")) {
						String horario = value.toString();
						String minutos = DOIS_PONTOS
						+ horario.substring(horario.length() - 2, horario.length());
						String hora = horario.length() == 4 ? horario.substring(0, horario.length() - 2) 
								: horario.substring(0, horario.length() - 1);
						return hora + minutos;
					} else {
						return "";
					}
				}
			}
			return null;
		} catch(Exception ex) {
			throw new ConverterException(new FacesMessage(
					MensagensFacesEnum.ERRO_CONVERTER_ENTIDADE.getKey(),
					PotiErpResource.getResource()));
		}
	}
}
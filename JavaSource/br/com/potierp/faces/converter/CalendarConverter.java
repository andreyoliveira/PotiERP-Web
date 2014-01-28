package br.com.potierp.faces.converter;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.validator.DateValidator;

import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.util.PotiErpResource;

import com.sun.faces.util.MessageFactory;

/**
 * @author
 * 
 * $LastChangedBy: $
 * 
 * $LastChangedDate: $
 * <br>
 * Classe responsável pela conversão de um Date para Calendar.
 */
public class CalendarConverter extends BaseConverter{
	
	/**
	 * Formatos validos para a conversão de data.
	 */
	private static final String[] VALID_DATE_FORMATS = new String[]{"dd/MM/yyyy"};

	/**
	 * Metodo responsável pela conversão de um DateString para Calendar.
	 */
	public Object getAsObject(final FacesContext facesContext, final UIComponent uiComponent, final String date) {
		try {
			
			if(!"".equals(date) && (date != null)){
				DateValidator dateValidator = DateValidator.getInstance();
				// verifica se a data é valida antes de converter
				boolean isDataValida = dateValidator.isValid(date, new Locale("pt", "BR"));
				if(!isDataValida) {
					geraMensagemErro(facesContext, uiComponent);
					return null;
				}
					
				Calendar calendar = Calendar.getInstance();
				Date data = DateUtils.parseDate(date, VALID_DATE_FORMATS);
				calendar.setTime(data);
				return calendar;
			}
		} catch (ParseException e) {
			throw new ConverterException(new FacesMessage(
					getMensagem(MensagensFacesEnum.ERRO_CONVERTER_CALENDAR.getKey(), PotiErpResource.getResource())));
		}
		return null;
	}

	/**
	 * Metodo responsável pela conversão de Calendar para um DateString.
	 */
	public String getAsString(final FacesContext facesContext, final UIComponent uiComponent, final Object obj) {
		if(obj instanceof Calendar){
			FastDateFormat fastDateFormat =  FastDateFormat.getInstance(VALID_DATE_FORMATS[0]);
			return fastDateFormat.format((Calendar)obj);
		}
		return null;
	}
	
	/**
	 * Gera Mensagem de Erro da validação.
	 * @param context
	 * @param component
	 */
	void geraMensagemErro(final FacesContext context, final UIComponent component) {
		throw new ConverterException(MessageFactory.getMessage(	context,
				MensagensFacesEnum.ERRO_CONVERTER_DATA_INVALIDA.getKey(),
				MessageFactory.getLabel(context, component)));
	}
}
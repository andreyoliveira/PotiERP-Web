package br.com.potierp.modulos.adm.managed;

import java.util.Iterator;

import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.richfaces.component.UIRichMessages;

import br.com.potierp.faces.managed.BaseMB;

public class ErroMB extends BaseMB{
	
	public ErroMB(){
	}
	
	private static Logger log = Logger.getLogger(ErroMB.class);
	
	private UIRichMessages erroGlobalComponent;
	
	private UIRichMessages infoGlobalComponent;
	
	
	public String getErrorMessage() {
		return isGlobalErrorMessage()?"1":"0";
	}
	
	public String getInfoMessage() {
		return isGlobalInfoMessage()?"1":"0";
	}
	
	public String getFieldMessage(){
		return isFieldMessage()?"1":"0";
	}
	
	public boolean isGlobalErrorMessage(){
		return erroGlobalComponent.getMessages(FacesContext.getCurrentInstance()).hasNext();
	}
	
	public boolean isGlobalInfoMessage(){
		return infoGlobalComponent.getMessages(FacesContext.getCurrentInstance()).hasNext();
	}
	
	public boolean isFieldMessage(){
		if(getFacesContext().getClientIdsWithMessages().hasNext()){
			for (Iterator<String> clientsMessages = getFacesContext().getClientIdsWithMessages(); clientsMessages.hasNext();) {
				if(clientsMessages.next() == null){
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public UIRichMessages getErroGlobalComponent() {
		return erroGlobalComponent;
	}

	public void setErroGlobalComponent(final UIRichMessages erroGlobalComponent) {
		this.erroGlobalComponent = erroGlobalComponent;
	}

	public UIRichMessages getInfoGlobalComponent() {
		return infoGlobalComponent;
	}

	public void setInfoGlobalComponent(final UIRichMessages infoGlobalComponent) {
		this.infoGlobalComponent = infoGlobalComponent;
	}

	@Override
	public Logger getLogger() {
		return log;
	}
	
}
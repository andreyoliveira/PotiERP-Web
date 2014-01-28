package br.com.potierp.modulos.operacional.helper;

import java.util.List;

import br.com.potierp.model.ProgramacaoVisita;
import br.com.potierp.model.Responsavel;

public class AcompanhamentoVisitaResponsavelHelper implements Cloneable{
	
	private Responsavel responsavel;
	
	private Boolean isVisitaCompleta;
	
	private Boolean isAguardandoVisita;
	
	private List<AcompanhamentoVisitaClienteHelper> listAcompanhamentoClientes;
	
	public AcompanhamentoVisitaResponsavelHelper() {
	}
	
	public void carregar(final List<ProgramacaoVisita> programacaoVisitas){
	}

	/**
	 * @return the responsavel
	 */
	public Responsavel getResponsavel() {
		return responsavel;
	}

	/**
	 * @param responsavel the responsavel to set
	 */
	public void setResponsavel(final Responsavel responsavel) {
		this.responsavel = responsavel;
	}

	/**
	 * @return the isVisitaCompleta
	 */
	public Boolean getIsVisitaCompleta() {
		if(!listAcompanhamentoClientes.isEmpty()){
			isVisitaCompleta = true;
			for(AcompanhamentoVisitaClienteHelper dataProgramacaoVisitaHelper : listAcompanhamentoClientes){
				if(!dataProgramacaoVisitaHelper.getDataProgramacaoVisita().getIsVisitado()){
					isVisitaCompleta = false;
					break;
				}
			}
		}
		return isVisitaCompleta;
	}

	/**
	 * @param isVisitaCompleta the isVisitaCompleta to set
	 */
	public void setIsVisitaCompleta(final Boolean isVisitaCompleta) {
		this.isVisitaCompleta = isVisitaCompleta;
	}

	/**
	 * @return the isAguardandoVisita
	 */
	public Boolean getIsAguardandoVisita() {
		if(!listAcompanhamentoClientes.isEmpty()){
			isAguardandoVisita = false;
			for(AcompanhamentoVisitaClienteHelper dataProgramacaoVisitaHelper : listAcompanhamentoClientes){
				if(dataProgramacaoVisitaHelper.getDataProgramacaoVisita().getIsAguardando()){
					isAguardandoVisita = true;
					break;
				}
			}
		}
		return isAguardandoVisita;
	}

	/**
	 * @param isAguardandoVisita the isAguardandoVisita to set
	 */
	public void setIsAguardandoVisita(final Boolean isAguardandoVisita) {
		this.isAguardandoVisita = isAguardandoVisita;
	}

	/**
	 * @return the listAcompanhamentoClientes
	 */
	public List<AcompanhamentoVisitaClienteHelper> getListAcompanhamentoClientes() {
		return listAcompanhamentoClientes;
	}

	/**
	 * @param listAcompanhamentoClientes the listAcompanhamentoClientes to set
	 */
	public void setListAcompanhamentoClientes(
			final List<AcompanhamentoVisitaClienteHelper> listAcompanhamentoClientes) {
		this.listAcompanhamentoClientes = listAcompanhamentoClientes;
	}
	
	@Override
	public AcompanhamentoVisitaResponsavelHelper clone() throws CloneNotSupportedException {
		return (AcompanhamentoVisitaResponsavelHelper)super.clone();
	}
}
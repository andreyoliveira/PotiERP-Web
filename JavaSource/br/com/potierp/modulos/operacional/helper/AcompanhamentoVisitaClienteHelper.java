package br.com.potierp.modulos.operacional.helper;

import br.com.potierp.model.Cliente;
import br.com.potierp.model.DataProgramacaoVisita;

public class AcompanhamentoVisitaClienteHelper implements Cloneable{
	
	private Cliente cliente;
	
	private DataProgramacaoVisita dataProgramacaoVisita;
	
	public AcompanhamentoVisitaClienteHelper() {
	}

	/**
	 * @return the cliente
	 */
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * @param cliente the cliente to set
	 */
	public void setCliente(final Cliente cliente) {
		this.cliente = cliente;
	}

	/**
	 * @return the dataProgramacaoVisita
	 */
	public DataProgramacaoVisita getDataProgramacaoVisita() {
		return dataProgramacaoVisita;
	}

	/**
	 * @param dataProgramacaoVisita the dataProgramacaoVisita to set
	 */
	public void setDataProgramacaoVisita(final DataProgramacaoVisita dataProgramacaoVisita) {
		this.dataProgramacaoVisita = dataProgramacaoVisita;
	}
	
	@Override
	public AcompanhamentoVisitaClienteHelper clone() throws CloneNotSupportedException {
		return (AcompanhamentoVisitaClienteHelper) super.clone();
	}
}
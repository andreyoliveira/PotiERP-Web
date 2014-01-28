package br.com.potierp.modulos.financeiro.managed;

import org.apache.log4j.Logger;

import br.com.potierp.faces.managed.BaseMB;

public class FinanceiroMB extends BaseMB {
	
	private static Logger log = Logger.getLogger(FinanceiroMB.class);

	@Override
	public Logger getLogger() {
		return this.log;
	}

}

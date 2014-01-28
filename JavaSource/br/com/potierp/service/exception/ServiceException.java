package br.com.potierp.service.exception;

/**
 * @author fabio.masson 
 * 31/08/2009 10:45:40
 * 
 * $LastChangedBy: fabio.masson $
 * 
 * $LastChangedDate: 2009-09-28 10:50:49 -0300 (seg, 28 set 2009) $
 * 
 * <br>
 * Exception referente a camada web-jsf.
 */
public abstract class ServiceException extends RuntimeException {

	/**
	 * Serial Id.
	 */
	private static final long serialVersionUID = -5907963923676056267L;
	
	/**
	 * par�metros, que podem ser utilizados via bundle.
	 */
	private Object[] params;

	/**
	 * Construtor definido para compatibilizada com API de checked Exception.
	 * @param e
	 */
	public ServiceException(final Exception e) {
		super(e);
	}

	/**
	 * Construtor recebe String e outra exception.
	 * @param msg : mensagem do erro.
	 * @param e : armazena a outra exception.
	 */
	public ServiceException(final String msg, final Exception e) {
		super(msg, e);
	}

	/**
	 * Construtor recebe String e outra exception.
	 * @param msg : mensagem do erro
	 * @param params : paranetros que podem ser utilizados com bundle.
	 */
	public ServiceException(final String msg, final Object[] params) {
		super(msg);
		this.params = params;
	}

	/**
	 * Construtor recebe String representando a mensagem do erro.
	 * @param msg
	 */
	public ServiceException(final String msg) {
		super(msg);
	}

	/**
	 * @return the params
	 */
	public Object[] getParams() {
		return params;
	}

}

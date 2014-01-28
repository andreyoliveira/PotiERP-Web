package br.com.potierp.faces.util;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.apache.log4j.Logger;

/** 
 * @author fabio.masson 
 * 31/08/2009 10:29:49
 * 
 * $LastChangedBy: fabio.masson $
 * 
 * $LastChangedDate: 2009-09-28 10:50:22 -0300 (seg, 28 set 2009) $
 * <pre>
 * Este PhaseListener pode ser utilizado para facilitar o entendimento do ciclo das fases de request do faces.
 * Durante a migra��o de fases, logs sao gerados.
 * </pre>
 * 
 */
public class LogPhaseListener implements PhaseListener {
	
	/**
     * Logger.
     */
    private static Logger log = Logger.getLogger(LogPhaseListener.class);

	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = 1245827657460084642L;

	/**
	 * pre-processamento.
	 */
	public void afterPhase(final PhaseEvent phaseEvent) {
		log.info("afterPhase: "+phaseEvent.getPhaseId()+"  "+phaseEvent.getSource());
	}

	/**
	 * pos-processamento.
	 */
	public void beforePhase(final PhaseEvent phaseEvent) {
		log.info("beforePhase: "+phaseEvent.getPhaseId()+" "+phaseEvent.getSource());
	}

	/**
	 */
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}
}

/**
 * 
 */
package br.com.potierp.infra.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import br.com.potierp.infra.log.TraceInfo;
import br.com.potierp.util.AttributeNames;
import br.com.potierp.util.TraceUtil;

/**
 * Filtro para injeta o traceInfo no threadLocal qdo uma requisicao eh feita.
 * @author eder.magalhaes
 *		   <p>
 *         $LastChangedBy: eder.magalhaes@UMESP.EDU.DTI $
 *         <p>
 *         $LastChangedDate: 2008-12-10 19:36:46 -0200 (qua, 10 dez 2008) $
 */
public class TraceFilter implements Filter {

	/**
     * Logger.
     */
    private static Logger log = Logger.getLogger(TraceFilter.class);

    /**
     * (non-Javadoc).
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    public void init(final FilterConfig filterConfig) throws ServletException {
	}

    /**
     * (non-Javadoc).
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, 
     * 		javax.servlet.FilterChain)
     */
    public void doFilter(final ServletRequest req, final ServletResponse resp,
    		final FilterChain filterChain) throws IOException, ServletException {
    	HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpSession session = request.getSession(false);
		TraceInfo trace = null;
		boolean configura = false;
		//coloca
		if (session != null) {
			trace = (TraceInfo) session.getAttribute(AttributeNames.TRACE_INFO.getName());
			if (trace != null) {
				TraceUtil.setCurrentTraceInfo(trace);
				log.debug("TraceInfo configurado em threaLocal: "+trace);
				configura = true;
			}
		}

		filterChain.doFilter(request, response);
		
		//limpa
		if (configura && trace != null) {
			TraceUtil.clean();
			log.debug("TraceInfo liberado em threaLocal: "+trace);
		}
	}

    /**
     * (non-Javadoc).
     * @see javax.servlet.Filter#destroy()
     */
	public void destroy() {
	}

}
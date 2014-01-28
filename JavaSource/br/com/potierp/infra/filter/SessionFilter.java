/**
 * fabio.masson 19/03/2010
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

/**
 * Faz o controle de sess�o do logos.
 *
 * @author fabio.masson
 * 19/03/2010
 *  	   <p>
 *         $LastChangedBy: $
 *         <p>
 *         $LastChangedDate: $
 */
public class SessionFilter implements Filter {

	/**
	 * Tela de sess�o expirada do logos.
	 */
	private static final String SESSION_ERROR_PATH = "/warnSession.jsf";


	/**
	 * (non-Javadoc).
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(final ServletRequest req, final ServletResponse resp,
			final FilterChain filter) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String url = request.getRequestURL().toString();

		// verifica se a sess�o existe
		HttpSession session = request.getSession(false);
		if(session == null && !url.contains(SESSION_ERROR_PATH)) {
			String contexto = request.getContextPath();
			String login = contexto + SESSION_ERROR_PATH;
			// envia para a p�gina de sess�o expirada
			response.sendRedirect(login);
		}else {
			filter.doFilter(request, response);
		}
	}

	/**
	 * (non-Javadoc).
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(final FilterConfig arg0) throws ServletException {}

	/**
	 * (non-Javadoc).
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {}

}
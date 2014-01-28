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

import br.com.potierp.model.Usuario;
import br.com.potierp.util.AttributeNames;

public class SecurityFilter implements Filter {
	
	/**
     * Logger.
     */
    private static Logger log = Logger.getLogger(SecurityFilter.class);
    
	/**
	 * Acesso ao PotiERP pela tela de Login.
	 */
	private static final String LOGIN_PATH = "/login.jsf";

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String url = request.getRequestURI();
		String requestPath = url.substring(url.lastIndexOf("/"), url.length());
		
		if(requestPath.equals(LOGIN_PATH)){
			filterChain.doFilter(request, response);
			return;
		}
		
		//Efetua a liberação da requisição para o praxis, após ter efetuado o login do praxis ou do portal/logos.
		HttpSession session = request.getSession(false);
		if (session != null) {
			Usuario usuario = (Usuario) session.getAttribute(AttributeNames.USUARIO.getName());
			if (usuario != null && usuario.isAutenticado()) {
				log.info("Requisicao liberada para " + usuario.getUsername());
				filterChain.doFilter(request, response);
				return;
			}
		}
		response.sendRedirect(request.getContextPath() + LOGIN_PATH);

	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	}
}
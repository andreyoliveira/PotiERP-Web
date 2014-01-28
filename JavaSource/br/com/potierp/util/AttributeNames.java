/**
 * fabio.masson 04/09/2009
 */
package br.com.potierp.util;

/**
 * Contém nome do Atributo(s) e informações para manipulação na camada Web, nos
 * respectivos contextos.
 *
 * @author renan
 *  	   <p>
 *         $LastChangedBy: $
 *         <p>
 *         $LastChangedDate: $
 */
public enum AttributeNames {

	/**
	 * Iinformações do usuário logado.
	 */
	UMESP_INFO_USUARIO("UMESP_INFO_USUARIO", true),
	/**
	 * Iinformações do dominio.
	 */
	UMESP_DOMAIN("UMESP_DOMAIN", true),

	/**
	 * Iinformações do usuário logado.
	 */
	TRACE_INFO("TRACE_INFO", true),

	/**
	 * Docente selecionado na pesquisa.
	 */
	DOCENTE_SELECIONADO_PESQUISA("DOCENTE_SELECIONADO_PESQUISA"),

	/**
	 * Diretores que foram carregados na pesquisa.
	 */
	LISTA_DIRETORES_PESQUISA("LISTA_DIRETORES_PESQUISA"),

	/**
	 * Filtros que foram informados na pesquisa de diretor.
	 */
	FILTROS_DIRETORES_PESQUISA("FILTROS_DIRETORES_PESQUISA"),

	/**
	 * Faixas de horario que foram carregados na pesquisa.
	 */
	LISTA_FAIXASHORARIO_PESQUISA("LISTA_FAIXASHORARIO_PESQUISA"),

	/**
	 * Filtros que foram informados na pesquisa de faixa de horario.
	 */
	FILTROS_FAIXASHORARIO_PESQUISA("FILTROS_FAIXASHORARIO_PESQUISA"),
	
	/**
	 * Faixa Horário Por Turno que foram carregados na pesquisa.
	 */
	LISTA_FAIXA_HORARIO_POR_TURNO_PESQUISA("LISTA_FAIXA_HORARIO_POR_TURNO_PESQUISA"),
	
	/**
	 * Lista de Faixa de Horário Selecionado.
	 */
	LIST_FAIXA_HORARIO_SELECIONADO("LIST_FAIXA_HORARIO_SELECIONADO"),

	/**
	 * Iinformações do download.
	 */
	UMESP_DOWNLOAD_FILE("UMESP_DOWNLOAD_FILE"),
	
	/**
	 * Pólo selecionado na listagem.
	 */
	POLO_SELECIONADO("POLO_SELECIONADO"),
	
	/**
	 * Pacote oferecimento optativa selecionado na listagem.
	 */
	PACOTE_OFERECIMENTO_OPTATIVA("PACOTE_OFERECIMENTO_OPTATIVA"),
	
	/**
	 * Usuário logado na tela.
	 */
	USUARIO("USUARIO");

	/**
	 * Construtor da enum.
	 * @param name
	 */
	private AttributeNames(final String name) {
		this.name = name;
	}
	
	/**
	 * Construtor da enum onde � informado o nome do atributo
	 * e se o mesmo pode ser removido da sess�o.
	 * 
	 * @param name
	 * @param persistent
	 */
	private AttributeNames(final String name, final Boolean persistent) {
		this.name = name;
		this.persistent = persistent;
	}

	/**
	 * Nome do atributo armazenado.
	 */
	private String name;
	
	/**
	 * Indica se o objeto pode ser removido da sess�o.
	 */
	private Boolean persistent;

	/**
	 * @return name.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * @return
	 */
	public Boolean isPersistent(){
		return this.persistent!= null ? this.persistent : false;
	}
}
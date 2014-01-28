package br.com.potierp.faces.converter;

import br.com.potierp.core.Identifiable;

/**
 * @author 
 *
 * $LastChangedBy:  $
 *
 * $LastChangedDate: $
 * <br>
 * Enum com os possíveis mocks utilizados em combos pelo sistema.
 */
public enum MockEnum implements Identifiable {

	/**
	 * Selecione.
	 */
	SELECIONE(Long.valueOf(-1),"selecione"),
	
	/**
	 * Todos.
	 */
	TODOS(Long.valueOf(-2),"todos"),
	
	/**
	 * Todas.
	 */
	TODAS(Long.valueOf(-3),"todas"),
	
	/**
	 * Nenhum.
	 */
	NENHUM(Long.valueOf(-4),"nenhum");
	
	/**
	 * Identificador do mock.
	 */
	private Long mockId;
	
	/**
	 * Key para a descrição do mock.
	 */
	private String mockKey;
	
	/**
	 * Construtor privado.
	 * @param mockId
	 */
	private MockEnum(final Long mockId, final String mockKey) {
		this.mockId = mockId;
		this.mockKey = mockKey;
	}

	/**
	 * @return the mockId
	 */
	public Long getMockId() {
		return this.mockId;
	}

	/**
	 * @return the mockKey
	 */
	public String getMockKey() {
		return this.mockKey;
	}
	
	/**
	 * (non-Javadoc).
	 * @see br.metodista.praxis.infra.Identifiable#getIdentity()
	 */
	public Object getIdentity() {
		return mockId;
	}
	
	/**
	 * Gets the mock by id.
	 * 
	 * @param mockId
	 * @return
	 */
	public static MockEnum getMock(final Long mockId) {
		if (mockId != null) {
			for (MockEnum mock : MockEnum.values()) {
				if (mockId.equals(mock.getMockId())) {
					return mock;
				}
			}
		}
		return null;
	}
	
	/**
	 * Verifica se o id passado pertence a um mock.
	 * 
	 * @param mockId
	 * @return
	 */
	public static boolean isMock(final Object mockId) {
		if (mockId instanceof Long) {
			return getMock((Long)mockId) != null;
		} else if (mockId instanceof String) {
			try {
				Long id = Long.valueOf((String)mockId);
				return getMock(id) != null;
			} catch(NumberFormatException ex) {
				return false;
			}
		}
		return false;
	}
}
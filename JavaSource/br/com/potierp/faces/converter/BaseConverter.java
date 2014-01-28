package br.com.potierp.faces.converter;

import javax.faces.convert.Converter;

import br.com.potierp.faces.util.MensagemUtil;
import br.com.potierp.util.StringUtils;


/**
 * @author
 * 
 * $LastChangedBy: $
 * 
 * $LastChangedDate: $
 * 
 * Define a utilizacao de <i>separador</i> ao mecanismo de converter que adota geracao de chave composta 
 * pelos atributos em uma String.
 * </pre>
 */
public abstract class BaseConverter implements Converter {

	/**
	 * Id inválido, utilizado pelos Mocks.
	 */
	public static final String MOCK_ID = "-1";
	
	/**
	 * Label utilizado pelos Mocks.
	 */
	public static final String MOCK_LABEL = "mock";
	
	/**
	 * Separador da String.
	 */
	private char separador;

	/**
	 * Construtor default, usa separador "*".
	 */
	public BaseConverter() {
		this.separador = '*';
	}

	/**
	 * Construtor recebe separador.
	 * @param separador
	 */
	public BaseConverter(final char separador) {
		this.separador = separador;
	}

	/**
	 * @return Separador.
	 */
	protected char getSeparador() {
		return this.separador;
	}

	/**
	 * Faz um split na string key informada (valor convertido).
	 * @param Key
	 * @return Array de String a partir do key informado.
	 */
	protected String[] getValues(final String key) {
		return key.split(""+getSeparador());
	}

	/**
	 * @param fields
	 * @return
	 */
	protected String toString(final Object ... fields) {
		if (fields == null || fields.length == 0)
			return "";
		StringBuffer sb = new StringBuffer("");
		for (Object o: fields) {
			sb.append(o.toString()).append(getSeparador());
		}
		return sb.toString();
	}
	
	/**
	 * Muda o formato do enter de \n para \\n. Problemas com o xhtml que interpreta a quebra de linha.
	 * @param s
	 * @return
	 */
	protected String encondeEnter(final String s) {
		return StringUtils.convertStringInStringOfBytes(s);
	}

	/**
	 * Volta o formato do enter com \n.
	 * @param s
	 * @return
	 */
	protected String decodeEnter(final String s) {
		return StringUtils.convertStringOfBytesInString(s);
	}

	/**
	 * Busca no resourcebundle a mensagem.
	 * @param key
	 * @param resource
	 * @return
	 */
	protected String getMensagem(final String key, final String resource) {
		return getMensagem(key, null, new String[]{resource});
	}

	/**
	 * Busca no bundle informado a mensagem conforme chave informada, com a utilização de argumentos.
	 * @param key
	 * @param params
	 * @param resources
	 * @return
	 */
	protected String getMensagem(final String key, final Object[] params, final String[] resources) {
		return MensagemUtil.getResourceBundleMessage(key, params, resources);
	}
}
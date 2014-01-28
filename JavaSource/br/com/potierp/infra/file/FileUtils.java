package br.com.potierp.infra.file;

import java.io.File;
import java.io.IOException;

/**
 * Classe utilit�ria para manipula��o de arquivos.
 *
 * @author walter.okuma
 * 02/06/2010 17:34:44
 *         <p>
 *         $LastChangedBy: $
 *         <p>
 *         $LastChangedDate: $
 */
public final class FileUtils {
	
	/**
	 * Construtor default, sem acesso externo.
	 */
	private FileUtils() {
	}

	/**
	 * Gera o nome do arquivo.
	 * @param nomePadraoArquivo
	 * @param extensao
	 * @return
	 */
	public static String gerarNomeUnicoArquivo(final String nomePadraoArquivo, final String extensao) {
		return nomePadraoArquivo + "_" + System.currentTimeMillis() + "." + extensao;
	}
	
	/**
	 * verifica se o arquivo existe no cache de downloads. o caminho do cache de
	 * downloads � setado na vari�vel de ambiente <b><i>SIGA_DOWNLOAD_CACHE</i></b>.
	 * 
	 * @param arquivo -
	 *            nome do arquivo (sem path)
	 * @return
	 */
	public static boolean fileExists(final String arquivo, final String path) {
		File file = new File(path, arquivo);
		return file.exists();
	}
	
	/**
	 * Se existir o arquivo no cache, exclui.
	 * @param arquivo - Nome do arquivo para ser excluido (sem path)
	 * @return
	 */
	public static boolean deleteFile(final String arquivo, final String path) {
		File file = new File(path, arquivo);
		return file.delete();
	}
	
	/**
	 * grava o arquivo no cache de download.
	 * 
	 * @param content -
	 *            conteudo do arquivo
	 * @param name -
	 *            nome do arquivo (sem path)
	 * @throws IOException
	 */
	public static void writeFile(final byte[] content, final String name, final String path)
			throws IOException {
		File file = new File(path, name);
		org.apache.commons.io.FileUtils.writeByteArrayToFile(file, content);
	}
	
    /**
     * Obtem um objeto File do cache.
     * @return
     */
    public static File getCachedFile(final String fileName, final String path) {
        File file = new File(path, fileName);
        if (!file.exists()) {
            throw new IllegalArgumentException("O arquivo n�o existe: " + path + "/" + fileName);
        }
        return file;
    }
}
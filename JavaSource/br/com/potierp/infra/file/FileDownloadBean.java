package br.com.potierp.infra.file;

import java.io.Serializable;

/**
 * Bean respons�vel pelas propriedades de um download.
 * 
 * @author walter.okuma
 * 02/06/2010 14:38:18
 *         <p>
 *         $LastChangedBy: $
 *         <p>
 *         $LastChangedDate: $
 */
public class FileDownloadBean implements Serializable {
	
	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = -8531958128380861586L;
	
	/**
	 * Mime type default para o download.
	 */
	public static final String DEFAULT_DOWNLOAD_MIME_TYPE = "application/octet-stream";
	
	/**
	 * Mime Type.
	 */
	private String mimeType = DEFAULT_DOWNLOAD_MIME_TYPE;
	
	/**
	 * Nome do arquivo.
	 */
	private String fileName;
	
	/**
	 * Path onde o arquivo est� armazenado.
	 */
	private String filePath;
	
	/**
	 * Construtor.
	 */
	public FileDownloadBean() {
	}

	// M�todos de neg�cio.
	/**
	 * Valida se as configura��es est�o certas para o download ser executado.
	 * @return
	 */
	public boolean validate() {
		return this.mimeType != null && this.filePath != null && this.fileName != null;
	}
	
	// Getters and Setters.
	/**
	 * @return the mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * @param mimeType the mimeType to set
	 */
	public void setMimeType(final String mimeType) {
		this.mimeType = mimeType;
	}
	
	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(final String filePath) {
		this.filePath = filePath;
	}
	
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}
}
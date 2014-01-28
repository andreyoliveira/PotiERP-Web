package br.com.potierp.infra.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import br.com.potierp.util.AttributeNames;

/**
 * Classe respons�vel por executar o download de um arquivo do sistema.
 * 
 * @author walter.okuma
 * 02/06/2010 14:06:21
 *         <p>
 *         $LastChangedBy: $
 *         <p>
 *         $LastChangedDate: $
 */
public class ManipuladorArquivoServlet extends HttpServlet {

	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = 3789700578460824777L;

	/**
	 * Logger.
	 */
	private static final Logger log = Logger.getLogger(ManipuladorArquivoServlet.class);

	/**
	 * P�gina de erro de download.
	 */
	public static final String PAGINA_ERRO_DOWNLOAD = "./metodista/erroDownload.jsf";
	
	/**
	 * Tamanho do buffer para download. 100Kb.
	 */
	protected static final int DOWNLOAD_BUFFER_SIZE = 100 * 1024;

	/**
	 * Recebe as requisi��es.
	 */
	@Override
	public void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * Recebe as requisi��es.
	 */
	@Override
	public void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException, IOException {
		HttpSession sessao = request.getSession(false);
		try {
			FileDownloadBean download = getFileDownloadBeanComValidacao(sessao);
			File file = FileUtils.getCachedFile(download.getFileName(), download.getFilePath());
			this.download(response, download.getFileName(), file, download.getMimeType());
		} catch (Exception ex) {
			log.error("Erro ao executar download de um arquivo.", ex);
			String paginaErro = PAGINA_ERRO_DOWNLOAD;
			response.sendRedirect(paginaErro);
		} finally {
			this.limparSessao(sessao);
		}
	}
	
	/**
	 * Obtem a configura��o do download.
	 * @param sessao
	 * @return
	 * @throws IllegalStateException
	 */
	private FileDownloadBean getFileDownloadBeanComValidacao(final HttpSession sessao) throws IllegalStateException {
		FileDownloadBean download = (FileDownloadBean) sessao.getAttribute(AttributeNames.UMESP_DOWNLOAD_FILE.getName());
		if (download == null || !download.validate()) {
			throw new IllegalStateException("Configura��o do arquivo para download n�o encontrado.");
		}
		return download;
	}
	
	/**
	 * Limpa os atributos da sess�o relacionados com o download do arquivo.
	 * @param sessao
	 */
	private void limparSessao(final HttpSession sessao) {
		if (sessao != null) {
			sessao.removeAttribute(AttributeNames.UMESP_DOWNLOAD_FILE.getName());
		}
	}

	/**
	 * Executa o download de um arquivo.
	 * 
	 * @param fileName
	 * @param contentType
	 * @param bytes
	 * @param file
	 */
	private void download(final HttpServletResponse response,
			final String fileName, final File file, final String contentType) throws IOException {
		log.debug("Realizando download de um arquivo.");
		ServletOutputStream out = null;
		InputStream input = null;
		try {
			out = response.getOutputStream();
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			response.setContentType(contentType);
			response.setContentLength((int) file.length());

			int readedBytes = 0;
			byte[] buffer = new byte[DOWNLOAD_BUFFER_SIZE];
			input = new FileInputStream(file);
			while ((readedBytes = input.read(buffer)) > -1) {
				out.write(buffer, 0, readedBytes);
				out.flush();
			}
			log.debug("Download efetuado com sucesso.");
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException ioex) {
				ioex.printStackTrace();
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException ioex) {
				ioex.printStackTrace();
			}
		}
	}
}
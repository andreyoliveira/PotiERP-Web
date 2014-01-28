package br.com.potierp.modulos.rh.managed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.component.UIForm;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import br.com.potierp.business.rh.facade.RhModulo;
import br.com.potierp.faces.converter.MockEnum;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.exception.PotiErpMensagensException;
import br.com.potierp.infra.helper.SelectionEntity;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.model.IntervaloJornada;
import br.com.potierp.model.JornadaTrabalho;
import br.com.potierp.model.TipoRefeicaoEnum;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class JornadaTrabalhoMB extends BaseMB {

	private static Logger log = Logger.getLogger(JornadaTrabalhoMB.class);

	private JornadaTrabalho jornadaTrabalho = new JornadaTrabalho();

	private JornadaTrabalho jornadaTrabalhoSelecionado = new JornadaTrabalho();

	private SelectionList<JornadaTrabalho> listJornadaTrabalho = new SelectionList<JornadaTrabalho>();

	private IntervaloJornada intervaloJornada = new IntervaloJornada();

	private List<SelectItem> itensTipoRefeicao = new ArrayList<SelectItem>();

	private SelectionList<IntervaloJornada> listIntervaloJornada = new SelectionList<IntervaloJornada>();

	private IntervaloJornada intervaloJornadaSelecionado = new IntervaloJornada();

	@EJB
	private RhModulo rhModulo;

	private boolean checkJornadaTrabalhoAll = false;

	private boolean checkIntervaloJornadaAll = false;

	private boolean bloquearHorarios = false;

	private Integer scrollerPage = 1;

	public JornadaTrabalhoMB() throws Exception {
		doNovo();
	}

	public void doNovo() throws Exception {
		jornadaTrabalho = new JornadaTrabalho();
		jornadaTrabalhoSelecionado = new JornadaTrabalho();
		intervaloJornada = new IntervaloJornada();
		intervaloJornadaSelecionado = new IntervaloJornada();
		checkJornadaTrabalhoAll = false;
		desbloquearHorarios();
	}

	private void bloquearHorarios() {
		bloquearHorarios = true;
	}

	private void desbloquearHorarios() {
		bloquearHorarios = false;
	}

	public void doSalvar() {
		try {
			jornadaTrabalho.setIntervalosJornada(listIntervaloJornada
					.getAllItens());
			rhModulo.salvarJornadaTrabalho(jornadaTrabalho);
			doNovo();
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_SALVAR);
		} catch (Exception e) {
			addMensagemErro(e.getMessage());
		}
	}

	public void doSalvarEIncluirOutro() {
		try {
			jornadaTrabalho.setIntervalosJornada(listIntervaloJornada
					.getAllItens());
			rhModulo.salvarJornadaTrabalho(jornadaTrabalho);
			doNovo();
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_SALVAR);
		} catch (Exception e) {
			addMensagemErro(e.getMessage());
		}
	}

	public void doAlterar() throws CloneNotSupportedException, PotiErpException {
		jornadaTrabalho = rhModulo
				.consultarPorIdComIntervalos(jornadaTrabalhoSelecionado.getId());
		if (Boolean.TRUE.equals(jornadaTrabalho.isUtilizada())) {
			bloquearHorarios();
		}
		if (jornadaTrabalho != null) {
			listIntervaloJornada = new SelectionList<IntervaloJornada>(
					new ArrayList<IntervaloJornada>(
							jornadaTrabalho.getIntervalosJornada()));
		}
	}

	public void doExcluir() {
		try {
			rhModulo.excluirJornadaTrabalho(jornadaTrabalho);
			doNovo();
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_JORNADA_ASSOCIADA);
		} catch (Exception e) {
			addMensagemErro(e.getMessage());
		}
	}

	public void doExcluirLote() {
		try {
			List<JornadaTrabalho> list = listJornadaTrabalho
					.getItensSelecionados();
			rhModulo.excluirListaJornadaTrabalho(list);
			doNovo();
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_EXCLUIR_LISTA);
		} catch (Exception e) {
			addMensagemErro(e.getMessage());
		}
	}

	public void doConsultar() {
		try {
			List<JornadaTrabalho> list = rhModulo
					.consultarTodasJornadasTrabalho();
			listJornadaTrabalho = new SelectionList<JornadaTrabalho>(list);
		} catch (Exception e) {
			addMensagemErro(e.getMessage());
		}
	}

	public void doSelecionarIntervaloJornada()
			throws CloneNotSupportedException {
		intervaloJornada = intervaloJornadaSelecionado.clone();
	}

	public void doAdicionarIntervaloJornada() throws CloneNotSupportedException {
		if (isIntervaloValido()) {
			jornadaTrabalho = jornadaTrabalhoSelecionado.clone();
			IntervaloJornada intervaloJornadaNovo = this.intervaloJornada
					.clone();
			intervaloJornadaNovo.setId(null);
			intervaloJornadaNovo.setJornadaTrabalho(jornadaTrabalho);
			listIntervaloJornada.add(new SelectionEntity<IntervaloJornada>(
					intervaloJornadaNovo));
			doLimparIntervaloJornada();
		} else {
			addMensagemErro(MensagensFacesEnum.INFORME_TODOS_OS_CAMPOS_PARA_ADICIONAR);
		}
	}

	/**
	 * @return
	 */
	private boolean isIntervaloValido() {
		return intervaloJornada.getTempo() != null
				&& intervaloJornada.getTipoRefeicao() != null
				&& !"".equalsIgnoreCase(intervaloJornada.getTipoRefeicao()
						.getTipoRefeicao().trim());
	}

	public void doAlterarIntervaloJornada() throws CloneNotSupportedException {
		if (intervaloJornadaSelecionado != null) {
			if (isIntervaloValido()) {
				intervaloJornadaSelecionado.setTempo(intervaloJornada
						.getTempo());
				intervaloJornadaSelecionado.setTipoRefeicao(intervaloJornada
						.getTipoRefeicao());
				doLimparIntervaloJornada();
			} else {
				addMensagemErro(MensagensFacesEnum.INFORME_TODOS_OS_CAMPOS_PARA_ALTERAR);
			}
		} else {
			addMensagemErro(MensagensFacesEnum.SELECIONE_UM_INTERVALO_PARA_ALTERAR);
		}
	}

	public void doRemoverIntervaloJornada() {
		SelectionList<IntervaloJornada> listTemporario = new SelectionList<IntervaloJornada>();
		for (int i = 0; i < listIntervaloJornada.size(); i++) {
			SelectionEntity<IntervaloJornada> selection = listIntervaloJornada
					.get(i);
			if (!selection.isSelecionado()) {
				listTemporario.add(selection);
			}
		}
		listIntervaloJornada = listTemporario;
		checkIntervaloJornadaAll = false;
	}

	public void doLimparIntervaloJornada() {
		intervaloJornada = new IntervaloJornada();
	}

	public void limparForm() throws Exception {
		UIForm form = (UIForm) getFacesContext().getViewRoot().findComponent("formDetails");
		cleanSubmittedValues(form);
		doNovo();
	}

	public JornadaTrabalho getJornadaTrabalho() {
		return jornadaTrabalho;
	}

	public void setJornadaTrabalho(final JornadaTrabalho jornadaTrabalho) {
		this.jornadaTrabalho = jornadaTrabalho;
	}

	public SelectionList<JornadaTrabalho> getListJornadaTrabalho() {
		doConsultar();
		return listJornadaTrabalho;
	}

	public void setListJornadaTrabalho(
			final SelectionList<JornadaTrabalho> listJornadaTrabalho) {
		this.listJornadaTrabalho = listJornadaTrabalho;
	}

	public boolean isCheckJornadaTrabalhoAll() {
		return checkJornadaTrabalhoAll;
	}

	public void setCheckJornadaTrabalhoAll(final boolean checkJornadaTrabalhoAll) {
		this.checkJornadaTrabalhoAll = checkJornadaTrabalhoAll;
	}

	public boolean isBloquearHorarios() {
		return bloquearHorarios;
	}

	public void setBloquearHorarios(final boolean bloquearHorarios) {
		this.bloquearHorarios = bloquearHorarios;
	}

	public Integer getScrollerPage() {
		return scrollerPage;
	}

	public void setScrollerPage(final Integer scrollerPage) {
		this.scrollerPage = scrollerPage;
	}

	public JornadaTrabalho getJornadaTrabalhoSelecionado() {
		return jornadaTrabalhoSelecionado;
	}

	public void setJornadaTrabalhoSelecionado(
			final JornadaTrabalho jornadaTrabalhoSelecionado) {
		this.jornadaTrabalhoSelecionado = jornadaTrabalhoSelecionado;
	}

	/**
	 * @return the itensTipoRefeicao
	 */
	public List<SelectItem> getItensTipoRefeicao() {
		List<TipoRefeicaoEnum> listTipoRefeicao = getListTipoRefeicao();
		if (listTipoRefeicao != null) {
			itensTipoRefeicao.clear();
			addMockSimple(itensTipoRefeicao, MockEnum.SELECIONE);
			for (TipoRefeicaoEnum tipoRefeicao : listTipoRefeicao) {
				SelectItem item = new SelectItem(tipoRefeicao,
						tipoRefeicao.getTipoRefeicao());
				itensTipoRefeicao.add(item);
			}
		}
		return itensTipoRefeicao;
	}

	/**
	 * @return
	 */
	private List<TipoRefeicaoEnum> getListTipoRefeicao() {
		if (itensTipoRefeicao.isEmpty()) {
			return new ArrayList<TipoRefeicaoEnum>(
					Arrays.asList(TipoRefeicaoEnum.values()));
		}
		return null;
	}

	/**
	 * @param itensTipoRefeicao
	 *            the itensTipoRefeicao to set
	 */
	public void setItensTipoRefeicao(List<SelectItem> itensTipoRefeicao) {
		this.itensTipoRefeicao = itensTipoRefeicao;
	}

	/**
	 * @return the intervaloJornada
	 */
	public IntervaloJornada getIntervaloJornada() {
		return intervaloJornada;
	}

	/**
	 * @param intervaloJornada
	 *            the intervaloJornada to set
	 */
	public void setIntervaloJornada(IntervaloJornada intervaloJornada) {
		this.intervaloJornada = intervaloJornada;
	}

	@Override
	public Logger getLogger() {
		return log;
	}

	public boolean isIncluirJornada() {
		return super
				.getUsuario()
				.getFuncionalidades()
				.contains(
						FuncionalidadeEnum.INCLUIR_JORNADA_TRABALHO.getCodigo());
	}

	public boolean isAlterarJornada() {
		return super
				.getUsuario()
				.getFuncionalidades()
				.contains(
						FuncionalidadeEnum.ALTERAR_JORNADA_TRABALHO.getCodigo());
	}

	public boolean isExcluirJornada() {
		return super
				.getUsuario()
				.getFuncionalidades()
				.contains(
						FuncionalidadeEnum.EXCLUIR_JORNADA_TRABALHO.getCodigo());
	}

	private boolean isConsultar() {
		return super
				.getUsuario()
				.getFuncionalidades()
				.contains(
						FuncionalidadeEnum.CONSULTAR_JORNADA_TRABALHO
								.getCodigo());
	}

	public boolean isConsultarJornada() {
		return isIncluirJornada() || isAlterarJornada() || isExcluirJornada()
				|| isConsultar();
	}

	public boolean isManterJornada() {
		return isIncluirJornada() || isAlterarJornada();
	}

	public String doCadastroDeJornadaDeTrabalho() {
		return NavigationEnum.CADASTRO_DE_JORNADA_DE_TRABALHO.getValor();
	}

	/**
	 * @return the listIntervaloJornada
	 */
	public SelectionList<IntervaloJornada> getListIntervaloJornada() {
		return listIntervaloJornada;
	}

	/**
	 * @param listIntervaloJornada
	 *            the listIntervaloJornada to set
	 */
	public void setListIntervaloJornada(
			SelectionList<IntervaloJornada> listIntervaloJornada) {
		this.listIntervaloJornada = listIntervaloJornada;
	}

	/**
	 * @return the intervaloJornadaSelecionado
	 */
	public IntervaloJornada getIntervaloJornadaSelecionado() {
		return intervaloJornadaSelecionado;
	}

	/**
	 * @param intervaloJornadaSelecionado
	 *            the intervaloJornadaSelecionado to set
	 */
	public void setIntervaloJornadaSelecionado(
			IntervaloJornada intervaloJornadaSelecionado) {
		this.intervaloJornadaSelecionado = intervaloJornadaSelecionado;
	}

	/**
	 * @return the checkIntervaloJornadaAll
	 */
	public boolean isCheckIntervaloJornadaAll() {
		return checkIntervaloJornadaAll;
	}

	/**
	 * @param checkIntervaloJornadaAll
	 *            the checkIntervaloJornadaAll to set
	 */
	public void setCheckIntervaloJornadaAll(boolean checkIntervaloJornadaAll) {
		this.checkIntervaloJornadaAll = checkIntervaloJornadaAll;
	}

}
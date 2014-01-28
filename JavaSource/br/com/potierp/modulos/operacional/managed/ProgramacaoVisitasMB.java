package br.com.potierp.modulos.operacional.managed;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import br.com.potierp.business.operacional.facade.OperacionalModulo;
import br.com.potierp.business.operacional.helper.DataProgramacaoVisitaHelper;
import br.com.potierp.business.rh.facade.RhModulo;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.model.Cliente;
import br.com.potierp.model.DataProgramacaoVisita;
import br.com.potierp.model.Funcionario;
import br.com.potierp.model.Pessoa;
import br.com.potierp.model.ProgramacaoVisita;
import br.com.potierp.model.Responsavel;
import br.com.potierp.util.NavigationEnum;

public class ProgramacaoVisitasMB extends BaseMB {
	
	//TODO [RALPH] - REVER LIMPEZA DE FORMULÁRIO AO SELECIONAR FUNCIONARIOS / CLIENTES E TB AO CLICAR EM LIMPAR
	
	private static Logger log = Logger.getLogger(ProgramacaoVisitasMB.class);
	
	private Integer scrollerPageResponsavel = 1;
	
	private Integer scrollerPageCliente = 1;
	
	private Integer scrollerPageData = 1;
	
	private SelectionList<Responsavel> listResponsavel = new SelectionList<Responsavel>(new ArrayList<Responsavel>());
	
	private Responsavel responsavelSelecionado = new Responsavel();
	
	private Responsavel responsavel = new Responsavel();
	
	private Funcionario funcionario = new Funcionario();
	
	private SelectionList<Cliente> listCliente = new SelectionList<Cliente>(new ArrayList<Cliente>());
	
	private Cliente cliente = new Cliente();
	
	private Cliente clienteSelecionado = new Cliente();
	
	private boolean exibeDatas = false;
	
	private ProgramacaoVisita programacaoVisita = new ProgramacaoVisita();
	
	private int mesSelecionado;
	
	private List<SelectItem> itensMeses = new ArrayList<SelectItem>();
	
	private Map<Integer, List<DataProgramacaoVisitaHelper>> mapaDataProgramacaoVisitaHelper = new HashMap<Integer, List<DataProgramacaoVisitaHelper>>();
	
	@EJB
	private RhModulo rhModulo;
	
	@EJB
	private OperacionalModulo operacionalModulo;
	
	public String doCadastroProgramacaoVisitasSupervisoras() {
		try {
			doCarregarComboMeses();
			doConsultarResponsaveis();
			return NavigationEnum.CADASTRO_PROGRAMACAO_VISITAS_SUPERVISORAS.getValor();
		} catch(PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
			return "";
		}
	}
	
	private void doConsultarResponsaveis() throws PotiErpException {
		List<Responsavel> responsaveis = rhModulo.consultarTodosResponsaveis();
		this.listResponsavel = new SelectionList<Responsavel>(new ArrayList<Responsavel>(responsaveis));
	}
	
	private void doCarregarComboMeses() {
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
		Calendar cal = new GregorianCalendar();
		mesSelecionado = cal.get(Calendar.MONTH);
		for(int mes = 0; mes <= cal.getActualMaximum(Calendar.MONTH); mes++) {
			cal.set(Calendar.MONTH, mes);
			itensMeses.add(new SelectItem(mes, sdf.format(cal.getTime())));
		}
	}
	
	public void doExibirClientes() {
		try {
			dolimparDatasVisitas();
			setScrollerPageCliente(1);
			responsavel = this.responsavelSelecionado.clone();
			Funcionario funcionario = this.responsavelSelecionado.getFuncionario().clone();
			funcionario.setPessoa(this.responsavelSelecionado.getFuncionario().getPessoa().clone());
			this.responsavel.setFuncionario(funcionario);
			this.listCliente = new SelectionList<Cliente>(new ArrayList<Cliente>(responsavel.getClientes()));
		} catch (CloneNotSupportedException e) {
			addMensagemErro(MensagensFacesEnum.ERRO_ALTERAR);
		}
	}
	
	private void dolimparDatasVisitas() {
		this.exibeDatas = false;
		this.programacaoVisita = new ProgramacaoVisita();
		this.mapaDataProgramacaoVisitaHelper = new HashMap<Integer, List<DataProgramacaoVisitaHelper>>();
	}
	
	public void doExibirDatas() {
		try {
			this.exibeDatas = true;
			setScrollerPageData(1);
			cliente = this.clienteSelecionado.clone();
			carregaListaDatas();
		} catch(PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (CloneNotSupportedException e) {
			addMensagemErro(MensagensFacesEnum.ERRO_ALTERAR);
		}
	}
	
	private void carregaListaDatas() throws PotiErpException {
		ProgramacaoVisita programacao = operacionalModulo.consultarPorResponsavelECliente(responsavelSelecionado.getId(), clienteSelecionado.getId());
		if(programacao!=null) {
			this.programacaoVisita = programacao;
		} else {
			doGerarProgramacaoVisita();
		}
		this.mapaDataProgramacaoVisitaHelper = DataProgramacaoVisitaHelper.gerarListaDatasHelper(programacaoVisita);
	}
	
	public void atualizaDataScrollerMesSelecionado() {
		this.scrollerPageData = 1;
	}
	
	public List<DataProgramacaoVisitaHelper> getListaProgramacaoVisitaHelper() {
		return mapaDataProgramacaoVisitaHelper.get(new Integer(mesSelecionado));
	}
	
	private void doGerarProgramacaoVisita() {
		this.programacaoVisita = new ProgramacaoVisita();
		this.programacaoVisita.setResponsavel(responsavelSelecionado);
		this.programacaoVisita.setIdCliente(clienteSelecionado.getId());
		this.programacaoVisita.setDatasProgramacaoVisitas(new ArrayList<DataProgramacaoVisita>());
	}
	
	public void doSalvar() {
		log.info("Salvando alteracoes de programacaoVisita.");
		try {
			List<DataProgramacaoVisita> listaDatasProgramacaoVisitas = DataProgramacaoVisitaHelper.recuperaListaDataProgramacaoVisita();
			programacaoVisita.setDatasProgramacaoVisitas(listaDatasProgramacaoVisitas);	
			operacionalModulo.salvarProgramacaoVisita(programacaoVisita);
			
			if(programacaoVisita.isNew()) {
				addMensagemInformativa(MensagensFacesEnum.SUCESSO_INSERIR);
			} else {
				addMensagemInformativa(MensagensFacesEnum.SUCESSO_ALTERAR);
			}
			
			carregaListaDatas();
			
		} catch (PotiErpException pe) {
			log.error(pe.getMessage(), pe);
			addMensagemErro(pe.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			addMensagemErro(e);
		}
	}
	
	public void doCancelar() {
		try {
			doLimparCampos();
			doConsultarResponsaveis();
		} catch (PotiErpException pe) {
			log.error(pe.getMessage(), pe);
			addMensagemErro(MensagensFacesEnum.ERRO_CANCELAR);
		}
	}
	
	private void doLimparCampos() {
		funcionario = new Funcionario();
		
		listResponsavel = new SelectionList<Responsavel>(new ArrayList<Responsavel>());
		responsavel = new Responsavel();
		responsavelSelecionado = new Responsavel();
		
		listCliente = new SelectionList<Cliente>(new ArrayList<Cliente>());
		cliente = new Cliente();
		clienteSelecionado = new Cliente();
		
		dolimparDatasVisitas();
		
		setScrollerPageResponsavel(1);
		setScrollerPageCliente(1);
		setScrollerPageData(1);
		
		dolimparDatasVisitas();
	}
	
	
	
	@Override
	public Logger getLogger() {
		return log;
	}
	
	public SelectionList<Responsavel> getListResponsavel() {
		return listResponsavel;
	}
	
	public void setListResponsavel(final SelectionList<Responsavel> listResponsavel) {
		this.listResponsavel = listResponsavel;
	}
	
	public Responsavel getResponsavelSelecionado() {
		return responsavelSelecionado;
	}

	public void setResponsavelSelecionado(final Responsavel responsavelSelecionado) {
		this.responsavelSelecionado = responsavelSelecionado;
	}

	public Responsavel getResponsavel() {
		if(responsavel.getFuncionario() == null) {
			Funcionario funcionario = new Funcionario();
			funcionario.setPessoa(new Pessoa());
			responsavel.setFuncionario(funcionario);
		}
		return responsavel;
	}

	public void setResponsavel(final Responsavel responsavel) {
		this.responsavel = responsavel;
	}
	
	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(final Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public SelectionList<Cliente> getListCliente() {
		return listCliente;
	}

	public void setListCliente(final SelectionList<Cliente> listCliente) {
		this.listCliente = listCliente;
	}
	
	public Integer getScrollerPageResponsavel() {
		return scrollerPageResponsavel;
	}

	public void setScrollerPageResponsavel(final Integer scrollerPageResponsavel) {
		this.scrollerPageResponsavel = scrollerPageResponsavel;
	}

	public Integer getScrollerPageCliente() {
		return scrollerPageCliente;
	}

	public void setScrollerPageCliente(final Integer scrollerPageCliente) {
		this.scrollerPageCliente = scrollerPageCliente;
	}

	public Integer getScrollerPageData() {
		return scrollerPageData;
	}

	public void setScrollerPageData(final Integer scrollerPageData) {
		this.scrollerPageData = scrollerPageData;
	}
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(final Cliente cliente) {
		this.cliente = cliente;
	}

	public Cliente getClienteSelecionado() {
		return clienteSelecionado;
	}

	public void setClienteSelecionado(final Cliente clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
	}
	
	public boolean isExibeDatas() {
		return exibeDatas;
	}

	public void setExibeDatas(final boolean exibeDatas) {
		this.exibeDatas = exibeDatas;
	}

	public ProgramacaoVisita getProgramacaoVisita() {
		return programacaoVisita;
	}

	public void setProgramacaoVisita(final ProgramacaoVisita programacaoVisita) {
		this.programacaoVisita = programacaoVisita;
	}

	public int getMesSelecionado() {
		return mesSelecionado;
	}

	public void setMesSelecionado(final int mesSelecionado) {
		this.mesSelecionado = mesSelecionado;
	}

	public List<SelectItem> getItensMeses() {
		return itensMeses;
	}

	public void setItensMeses(final List<SelectItem> itensMeses) {
		this.itensMeses = itensMeses;
	}
}

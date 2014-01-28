package br.com.potierp.modulos.operacional.managed;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;

import org.apache.log4j.Logger;

import br.com.potierp.business.operacional.facade.OperacionalModulo;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.model.Funcionario;
import br.com.potierp.model.Pessoa;
import br.com.potierp.model.ProgramacaoVisita;
import br.com.potierp.model.Responsavel;
import br.com.potierp.modulos.operacional.helper.AcompanhamentoVisitaClienteHelper;
import br.com.potierp.modulos.operacional.helper.AcompanhamentoVisitaResponsavelHelper;
import br.com.potierp.util.NavigationEnum;

public class AcompanhamentoVisitasMB extends BaseMB {
	
	private static Logger log = Logger.getLogger(AcompanhamentoVisitasMB.class);
	
	@EJB
	private OperacionalModulo operacionalModulo;
	
	private List<AcompanhamentoVisitaResponsavelHelper> listResponsavel = new ArrayList<AcompanhamentoVisitaResponsavelHelper>();
	
	private AcompanhamentoVisitaResponsavelHelper responsavelSelecionado = new AcompanhamentoVisitaResponsavelHelper();
	
	private Responsavel responsavel = new Responsavel();
	
	private List<AcompanhamentoVisitaClienteHelper> listCliente = new ArrayList<AcompanhamentoVisitaClienteHelper>();
	
	private AcompanhamentoVisitaClienteHelper clienteSelecionado = new AcompanhamentoVisitaClienteHelper();
	
	private Date dataProgramada = new Date();
	
	private Integer scrollerPageResponsavel = 1;
	
	private Integer scrollerPageCliente = 1;
	
	public void doCarregarResponsaveis(){
		try {
			doNovo();
			List<ProgramacaoVisita> programacoesVisitas = 
				operacionalModulo.consultarPorDataProgramada(dataProgramada);
			carregarResponsaveis(programacoesVisitas);
		}catch(Exception ex){
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		}
	}
	
	private void carregarResponsaveis(final List<ProgramacaoVisita> programacoesVisitas) throws Exception {
		if(programacoesVisitas != null && !programacoesVisitas.isEmpty()){
			Long idResponsavel = -1L;
			AcompanhamentoVisitaResponsavelHelper acompanhamentoResponsavel = new AcompanhamentoVisitaResponsavelHelper();
			acompanhamentoResponsavel.setListAcompanhamentoClientes(new ArrayList<AcompanhamentoVisitaClienteHelper>());
			for(ProgramacaoVisita programacaoVisita : programacoesVisitas){
				Responsavel resp = programacaoVisita.getResponsavel();
				if(idResponsavel == -1){
					acompanhamentoResponsavel.setResponsavel(resp.clone());
					AcompanhamentoVisitaClienteHelper acompanhamentoVisitaClienteHelper = new AcompanhamentoVisitaClienteHelper();
					acompanhamentoVisitaClienteHelper.setCliente(programacaoVisita.getCliente());
					acompanhamentoVisitaClienteHelper.setDataProgramacaoVisita(programacaoVisita.getDatasProgramacaoVisitas().iterator().next().clone());
					acompanhamentoResponsavel.getListAcompanhamentoClientes().add(acompanhamentoVisitaClienteHelper.clone());
				}else if(!resp.getId().equals(idResponsavel)){
					listResponsavel.add(acompanhamentoResponsavel.clone());
					acompanhamentoResponsavel = new AcompanhamentoVisitaResponsavelHelper();
					acompanhamentoResponsavel.setListAcompanhamentoClientes(new ArrayList<AcompanhamentoVisitaClienteHelper>());
					acompanhamentoResponsavel.setResponsavel(resp.clone());
					AcompanhamentoVisitaClienteHelper acompanhamentoVisitaClienteHelper = new AcompanhamentoVisitaClienteHelper();
					acompanhamentoVisitaClienteHelper.setCliente(programacaoVisita.getCliente());
					acompanhamentoVisitaClienteHelper.setDataProgramacaoVisita(programacaoVisita.getDatasProgramacaoVisitas().iterator().next().clone());
					acompanhamentoResponsavel.getListAcompanhamentoClientes().add(acompanhamentoVisitaClienteHelper.clone());
				}else{
					AcompanhamentoVisitaClienteHelper acompanhamentoVisitaClienteHelper = new AcompanhamentoVisitaClienteHelper();
					acompanhamentoVisitaClienteHelper.setCliente(programacaoVisita.getCliente());
					acompanhamentoVisitaClienteHelper.setDataProgramacaoVisita(programacaoVisita.getDatasProgramacaoVisitas().iterator().next().clone());
					acompanhamentoResponsavel.getListAcompanhamentoClientes().add(acompanhamentoVisitaClienteHelper.clone());
				}
				idResponsavel = resp.getId();
			}
			listResponsavel.add(acompanhamentoResponsavel.clone());
		}
	}
	
	private void doNovo(){
		responsavel = new Responsavel();
		responsavelSelecionado = new AcompanhamentoVisitaResponsavelHelper();
		listResponsavel = new ArrayList<AcompanhamentoVisitaResponsavelHelper>();
		listCliente = new ArrayList<AcompanhamentoVisitaClienteHelper>();
	}
	
	public void doExibirClientes() {
		responsavel = this.responsavelSelecionado.getResponsavel();
		this.listCliente = new ArrayList<AcompanhamentoVisitaClienteHelper>(responsavelSelecionado.getListAcompanhamentoClientes());
	}
	
	public void doVisitou() throws Exception {
		clienteSelecionado.getDataProgramacaoVisita().setIsVisitado(true);
		clienteSelecionado.getDataProgramacaoVisita().setIsAguardando(false);
		operacionalModulo.salvar(clienteSelecionado.getDataProgramacaoVisita());
	}
	
	public void doNaoVisitou() throws Exception {
		clienteSelecionado.getDataProgramacaoVisita().setIsVisitado(false);
		clienteSelecionado.getDataProgramacaoVisita().setIsAguardando(false);
		operacionalModulo.salvar(clienteSelecionado.getDataProgramacaoVisita());
	}

	public void doAguardando() throws Exception {
		clienteSelecionado.getDataProgramacaoVisita().setIsVisitado(false);
		clienteSelecionado.getDataProgramacaoVisita().setIsAguardando(true);
		operacionalModulo.salvar(clienteSelecionado.getDataProgramacaoVisita());
	}
	
	@Override
	public Logger getLogger() {
		return log;
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
	
	/**
	 * @return the listResponsavel
	 */
	public List<AcompanhamentoVisitaResponsavelHelper> getListResponsavel() {
		return listResponsavel;
	}

	/**
	 * @param listResponsavel the listResponsavel to set
	 */
	public void setListResponsavel(
			final List<AcompanhamentoVisitaResponsavelHelper> listResponsavel) {
		this.listResponsavel = listResponsavel;
	}

	/**
	 * @return the responsavelSelecionado
	 */
	public AcompanhamentoVisitaResponsavelHelper getResponsavelSelecionado() {
		return responsavelSelecionado;
	}

	/**
	 * @param responsavelSelecionado the responsavelSelecionado to set
	 */
	public void setResponsavelSelecionado(
			final AcompanhamentoVisitaResponsavelHelper responsavelSelecionado) {
		this.responsavelSelecionado = responsavelSelecionado;
	}

	/**
	 * @return the listCliente
	 */
	public List<AcompanhamentoVisitaClienteHelper> getListCliente() {
		return listCliente;
	}

	/**
	 * @param listCliente the listCliente to set
	 */
	public void setListCliente(final List<AcompanhamentoVisitaClienteHelper> listCliente) {
		this.listCliente = listCliente;
	}

	/**
	 * @return the clienteSelecionado
	 */
	public AcompanhamentoVisitaClienteHelper getClienteSelecionado() {
		return clienteSelecionado;
	}

	/**
	 * @param clienteSelecionado the clienteSelecionado to set
	 */
	public void setClienteSelecionado(
			final AcompanhamentoVisitaClienteHelper clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
	}

	/**
	 * @return the dataProgramada
	 */
	public Date getDataProgramada() {
		return dataProgramada;
	}

	/**
	 * @param dataProgramada the dataProgramada to set
	 */
	public void setDataProgramada(final Date dataProgramada) {
		this.dataProgramada = dataProgramada;
	}

	public String doAcompanhamentoVisitas(){
		return NavigationEnum.ACOMPANHAMENTO_VISITAS_SUPERVISORAS.getValor();
	}
}

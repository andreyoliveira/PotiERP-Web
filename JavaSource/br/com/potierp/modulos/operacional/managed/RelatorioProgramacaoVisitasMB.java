package br.com.potierp.modulos.operacional.managed;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import br.com.potierp.business.operacional.facade.OperacionalModulo;
import br.com.potierp.business.rh.facade.RhModulo;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.infra.resource.PotiErpProperties;
import br.com.potierp.model.Cliente;
import br.com.potierp.model.Funcionario;
import br.com.potierp.model.Pessoa;
import br.com.potierp.model.ProgramacaoVisita;
import br.com.potierp.model.Responsavel;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

/**
 * 
 * @author Ralph
 *
 */
public class RelatorioProgramacaoVisitasMB extends BaseMB{
	
	private static Logger log = Logger.getLogger(RelatorioProgramacaoVisitasMB.class);
	
	private Responsavel responsavel = new Responsavel();
	
	private Responsavel responsavelSelecionado = new Responsavel();
	
	private List<Responsavel> sugestoesResponsaveis;
	
	private int mesSelecionado;
	
	private List<SelectItem> itensMeses = new ArrayList<SelectItem>();
	
	@EJB
	private RhModulo rhModulo;
	
	@EJB
	private OperacionalModulo operacionalModulo;
	
	public void doGerar() {
		try {
			if(isResponsavelSelecionado()){
					List<ProgramacaoVisita> listaProgramacaoVisita = new ArrayList<ProgramacaoVisita>();
					listaProgramacaoVisita = operacionalModulo.consultarPorResponsavel(responsavel.getId());
					popularClientesComProgramacaoVisita(listaProgramacaoVisita);
					
					byte[] rel = getRelatorio(listaProgramacaoVisita);
					if(rel != null) {
						super.registraArquivoParaDownload(rel, "Programacao de Visitas", "pdf", PotiErpProperties.getInstance().getReportPath());
					} 
			}else{
				addMensagemErro(MensagensFacesEnum.ERRO_FUNCIONARIO_NAO_INFORMADO);
			}
		} catch (Exception e) {
			addMensagemErro(MensagensFacesEnum.ERRO_NAO_FOI_POSSIVEL_GERAR_O_RELATORIO);
		}
	}
	
	private byte[] getRelatorio(final List<ProgramacaoVisita> listaProgramacaoVisita) throws PotiErpException {
		return operacionalModulo.getRelatorioProgramacaoVisita(listaProgramacaoVisita, responsavel, mesSelecionado);
	}
	
	private void popularClientesComProgramacaoVisita(final List<ProgramacaoVisita> listaProgramacaoVisita) {
		final List<ProgramacaoVisita> listaProgramacaoVisitaRemover = new ArrayList<ProgramacaoVisita>();
		for(ProgramacaoVisita programacaoVisita : listaProgramacaoVisita) {
			for (Cliente cliente : responsavel.getClientes()) {
				if(programacaoVisita.getIdCliente().equals(cliente.getId())) {
					programacaoVisita.setCliente(cliente);
					break;
				}
			}
			if(programacaoVisita.getCliente() == null)
				listaProgramacaoVisitaRemover.add(programacaoVisita);
		}
		
		listaProgramacaoVisita.removeAll(listaProgramacaoVisitaRemover);
	}
	
	private boolean isResponsavelSelecionado() {
		return responsavel != null && responsavel.getId() != null;
	}
	
	public void doLimpar() {
		inicializaResponsavel();
	}
	
	public void buscarResponsavel() {
		try {
			if(isRegistroEmpregadoPreenchido()) {
				if(sugestoesResponsaveis == null) {
					carregarSugestoes();
				}
				responsavel = getResponsavelPorRE();
				if(responsavel == null) {
					addMensagemErro(MensagensFacesEnum.ERRO_FUNCIONARIO_NAO_ENCONTRADO);
					inicializaResponsavel();
				}
			} else {
				inicializaResponsavel();
			}
		} catch (Exception e) {
			addMensagemErro(e.getMessage());
		}
	}
	
	private void inicializaResponsavel() {
		responsavel = new Responsavel();
		responsavel.setFuncionario(new Funcionario());
		responsavel.getFuncionario().setPessoa(new Pessoa());
	}
	
	private Responsavel getResponsavelPorRE() throws Exception {
		if(sugestoesResponsaveis != null){
			for(Responsavel resp : sugestoesResponsaveis){
				if(resp.getFuncionario().getCodigoRegistro() != null && resp.getFuncionario().getCodigoRegistro().equals(responsavel.getFuncionario().getCodigoRegistro())) {
					return resp.clone();
				}
			}
		}
		return null;
	}
	
	private boolean isRegistroEmpregadoPreenchido() {
		return responsavel !=null 
				&& responsavel.getFuncionario() != null
				&& responsavel.getFuncionario().getCodigoRegistro() != null
				&& responsavel.getFuncionario().getCodigoRegistro() > 0;
	}
	
	public String doRelatorioProgramacaoVisitas() {
		doCarregarComboMeses();
		return NavigationEnum.RELATORIO_PROGRAMACAO_VISITAS.getValor();
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
	
	public boolean isConsultarRelatorioProgramacaoVisitas() {
		return isConsultar();
	}
	
	private boolean isConsultar() {
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_VALE_TRANSPORTE.getCodigo());
	}
	
	public List<Responsavel> sugestaoResponsaveis(final Object evento) throws Exception{
		
		if(sugestoesResponsaveis == null){
			carregarSugestoes();
		}
		
		List<Responsavel> responsaveis = new ArrayList<Responsavel>();
		try {
			String prefixo = evento.toString().trim().toLowerCase();
			for(Responsavel resp : sugestoesResponsaveis){
				if(resp.getFuncionario().getPessoa().getNome().trim().toLowerCase().startsWith(prefixo)){
					responsaveis.add(resp);
				}
			}
		} catch (Exception e) {
			addMensagemErro(e.getMessage());
		}
		return responsaveis;
	}
	
	private void carregarSugestoes() throws PotiErpException {
		sugestoesResponsaveis = rhModulo.consultarTodosResponsaveis();
	}

	public void selecionarResponsavel() throws Exception {
		this.responsavel = this.responsavelSelecionado.clone();
		this.responsavel.setFuncionario(this.responsavelSelecionado.getFuncionario().clone());
		this.responsavel.getFuncionario().setPessoa(this.responsavelSelecionado.getFuncionario().getPessoa().clone());
		this.responsavelSelecionado = new Responsavel();
	}

	
	public Responsavel getResponsavel() {
		if(responsavel.getFuncionario() == null)
			inicializaResponsavel();		
		return responsavel;
	}

	public void setResponsavel(final Responsavel responsavel) {
		this.responsavel = responsavel;
	}

	public Responsavel getResponsavelSelecionado() {
		return responsavelSelecionado;
	}

	public void setResponsavelSelecionado(final Responsavel responsavelSelecionado) {
		this.responsavelSelecionado = responsavelSelecionado;
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

	@Override
	public Logger getLogger() {
		return log;
	}
}

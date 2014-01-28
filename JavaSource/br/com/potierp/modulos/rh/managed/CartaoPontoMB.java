package br.com.potierp.modulos.rh.managed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import br.com.potierp.business.rh.comparator.NomeFuncionarioComparator;
import br.com.potierp.business.rh.facade.RhModulo;
import br.com.potierp.faces.converter.MockEnum;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.infra.resource.PotiErpProperties;
import br.com.potierp.model.Cliente;
import br.com.potierp.model.Funcionario;
import br.com.potierp.model.MesEnum;
import br.com.potierp.model.Pessoa;
import br.com.potierp.model.RelatoriosMensaisEnum;
import br.com.potierp.model.SituacaoFuncionario;
import br.com.potierp.relatorio.rh.facade.RelatorioRhModulo;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

/**
 * @author Andrey Oliveira
 */
public class CartaoPontoMB extends BaseMB {

	private static Logger log = Logger.getLogger(CartaoPontoMB.class);
	
	private Funcionario funcionario = new Funcionario();
	
	private Funcionario funcionarioSelecionado = new Funcionario();
	
	private List<Funcionario> sugestoesFuncionarios;
	
	private RelatoriosMensaisEnum relatorio = RelatoriosMensaisEnum.CARTAO_PONTO;
	
	private List<SelectItem> itensMes = new ArrayList<SelectItem>();
	
	private String ano;
	
	private String mes;
	
	@EJB
	private RhModulo rhModulo;
	
	@EJB
	private RelatorioRhModulo relatorioRhModulo;
	
	public void doGerar() {
		try {
			if(isFuncionarioSelecionado()) {
				funcionario = rhModulo.consultarFuncionario(funcionario.getId(), getTraceInfo());
				List<Funcionario> listFuncionariosSelecionados = new ArrayList<Funcionario>();
				listFuncionariosSelecionados.add(funcionario);
				byte[] rel = getRelatorio(listFuncionariosSelecionados);
				if(rel != null) {
					super.registraArquivoParaDownload(rel, relatorio.getRelatorio(), "pdf", PotiErpProperties.getInstance().getReportPath());
				} 
			} else {
				addMensagemErro(MensagensFacesEnum.ERRO_FUNCIONARIO_NAO_INFORMADO);
			}
		} catch (Exception e) {
			addMensagemErro(MensagensFacesEnum.ERRO_NAO_FOI_POSSIVEL_GERAR_O_RELATORIO);
		}
	}
	
	private byte[] getRelatorio(final List<Funcionario> listFuncionariosSelecionados) throws PotiErpException {
		if(relatorio.equals(RelatoriosMensaisEnum.CARTAO_PONTO)) {
			return relatorioRhModulo.getCartaoPonto(listFuncionariosSelecionados, mes, ano);
		}
		return null;
	}
	
	private boolean isFuncionarioSelecionado() {
		return funcionario != null && funcionario.getId() != null;
	}
	
	public void doLimpar() {
		this.funcionario = new Funcionario();
		this.ano = "";
		this.mes = "";
	}
	
	public void buscarFuncionario(){
		try {
			if(isRegistroEmpregadoPreenchido()){
				if(sugestoesFuncionarios == null){
					carregarSugestoes();
				}
				funcionario = getFuncionarioPorRE();
				if(funcionario == null){
					addMensagemErro(MensagensFacesEnum.ERRO_FUNCIONARIO_NAO_ENCONTRADO);
					funcionario = new Funcionario();
					funcionario.setPessoa(new Pessoa());
				}
			}else{
				funcionario = new Funcionario();
				funcionario.setPessoa(new Pessoa());
			}
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	private Funcionario getFuncionarioPorRE() throws Exception {
		if(sugestoesFuncionarios != null){
			for(Funcionario func : sugestoesFuncionarios){
				if(func.getCodigoRegistro() != null && func.getCodigoRegistro().equals(funcionario.getCodigoRegistro())){
					return func.clone();
				}
			}
		}
		return null;
	}

	private boolean isRegistroEmpregadoPreenchido() {
		return funcionario != null
				&& funcionario.getCodigoRegistro() != null
				&& funcionario.getCodigoRegistro() > 0;
	}
	
	public List<Funcionario> sugestao(final Object evento) throws Exception{
		
		if(sugestoesFuncionarios == null){
			carregarSugestoes();
		}
		
		List<Funcionario> funcionarios = new ArrayList<Funcionario>();
		try {
			String prefixo = evento.toString().trim().toLowerCase();
			for(Funcionario func : sugestoesFuncionarios){
				if(func.getPessoa().getNome().trim().toLowerCase().startsWith(prefixo)){
					funcionarios.add(func);
				}
			}
		} catch (Exception e) {
			addMensagemErro(e.getMessage());
		}
		Collections.sort(funcionarios, new NomeFuncionarioComparator());
		return funcionarios;
	}
	
	private void carregarSugestoes() throws PotiErpException {
		sugestoesFuncionarios = rhModulo.consultarFuncionariosPesquisa(new Cliente(), new SituacaoFuncionario());
	}

	public void selecionarFuncionario() throws Exception {
		this.funcionario = this.funcionarioSelecionado.clone();
		this.funcionario.setPessoa(this.funcionarioSelecionado.getPessoa().clone());
		this.funcionarioSelecionado = new Funcionario();
	}
	
	public String doCartaoPonto() {
		return NavigationEnum.CARTAO_PONTO.getValor();
	}
	
	public boolean isConsultarCartaoPonto(){
		return isConsultar();
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_VALE_TRANSPORTE.getCodigo());
	}
	
	public Funcionario getFuncionario() {
		if(funcionario.getPessoa() == null) {
			funcionario.setPessoa(new Pessoa());
		}
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Funcionario getFuncionarioSelecionado() {
		return funcionarioSelecionado;
	}

	public void setFuncionarioSelecionado(Funcionario funcionarioSelecionado) {
		this.funcionarioSelecionado = funcionarioSelecionado;
	}

	public List<Funcionario> getSugestoesFuncionarios() {
		return sugestoesFuncionarios;
	}

	public void setSugestoesFuncionarios(List<Funcionario> sugestoesFuncionarios) {
		this.sugestoesFuncionarios = sugestoesFuncionarios;
	}

	public List<SelectItem> getItensMes() {
		MesEnum[] meses = MesEnum.values();
		itensMes.clear();
		addMockSimple(itensMes, MockEnum.SELECIONE);
		for(MesEnum mes : meses) {
			SelectItem item = new SelectItem(mes.getMes(), mes.getMes());
			itensMes.add(item);
		}
		return itensMes;
	}

	public void setItensMes(List<SelectItem> itensMes) {
		this.itensMes = itensMes;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	@Override
	public Logger getLogger() {
		return log;
	}

}

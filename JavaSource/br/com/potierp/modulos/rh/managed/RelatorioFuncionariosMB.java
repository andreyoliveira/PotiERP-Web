package br.com.potierp.modulos.rh.managed;

import java.util.ArrayList;
import java.util.Arrays;
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
import br.com.potierp.model.Pessoa;
import br.com.potierp.model.RelatoriosAdmissionaisEnum;
import br.com.potierp.model.SituacaoFuncionario;
import br.com.potierp.relatorio.rh.facade.RelatorioRhModulo;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

public class RelatorioFuncionariosMB extends BaseMB{
	
	private static Logger log = Logger.getLogger(RelatorioFuncionariosMB.class);
	
	private List<String> projecoesFuncionarioSelecionadas = new ArrayList<String>();
	
	private List<SelectItem> projecoesFuncionario = new ArrayList<SelectItem>();
	
	private Funcionario funcionario = new Funcionario();
	
	private Funcionario funcionarioSelecionado = new Funcionario();
	
	private List<Funcionario> sugestoesFuncionarios;
	
	private RelatoriosAdmissionaisEnum relatorio;
	
	@EJB
	private RhModulo rhModulo;
	
	@EJB
	private RelatorioRhModulo relatorioRhModulo;
	
	private List<SelectItem> itensRelatorios = new ArrayList<SelectItem>();
	
	public void doGerar() {
		try{
			if(isFuncionarioSelecionado()){
				if(relatorio != null) {
					funcionario = rhModulo.consultarFuncionario(funcionario.getId(), getTraceInfo());
					List<Funcionario> listFuncionariosSelecionados = new ArrayList<Funcionario>();
					listFuncionariosSelecionados.add(funcionario);
					byte[] rel = getRelatorio(listFuncionariosSelecionados);
					if(rel != null){
						super.registraArquivoParaDownload(rel, relatorio.getRelatorio(), "pdf", PotiErpProperties.getInstance().getReportPath());
					}
				}else {
					addMensagemErro(MensagensFacesEnum.ERRO_RELATORIO_NAO_INFORMADO);
				}
			}else{
				addMensagemErro(MensagensFacesEnum.ERRO_FUNCIONARIO_NAO_INFORMADO);
			}
		} catch (Exception e) {
			addMensagemErro(MensagensFacesEnum.ERRO_NAO_FOI_POSSIVEL_GERAR_O_RELATORIO);
		}
	}

	private byte[] getRelatorio(final List<Funcionario> listFuncionariosSelecionados) throws PotiErpException {
		if(relatorio.equals(RelatoriosAdmissionaisEnum.SALARIO_FAMILIA)){
			return relatorioRhModulo.getSalarioFamilia(listFuncionariosSelecionados);
		} else if(relatorio.equals(RelatoriosAdmissionaisEnum.SOLICITACAO_VALE_TRANSPORTE)){
			return relatorioRhModulo.getSolicitacaoValeTransporte(listFuncionariosSelecionados);
		} else if(relatorio.equals(RelatoriosAdmissionaisEnum.CONTRATO_EXPERIENCIA)) {
			return relatorioRhModulo.getContratoExperiencia(listFuncionariosSelecionados);
		} else if(relatorio.equals(RelatoriosAdmissionaisEnum.FICHA_REGISTRO)) {
			return relatorioRhModulo.getFichaRegistro(listFuncionariosSelecionados);
		} else if(relatorio.equals(RelatoriosAdmissionaisEnum.DECLARACAO_PARA_FINS_IR)) {
			return relatorioRhModulo.getDeclaracaoEncargos(listFuncionariosSelecionados);
		} else if(relatorio.equals(RelatoriosAdmissionaisEnum.DESVIO_DE_FUNCAO)) {
			return relatorioRhModulo.getDesvioFuncao(listFuncionariosSelecionados);
		} else if(relatorio.equals(RelatoriosAdmissionaisEnum.AUTORIZACAO_DESCONTO_REFEICAO)){
			return relatorioRhModulo.getAutorizacaoDescontoRefeicao(listFuncionariosSelecionados);
		} else if(relatorio.equals(RelatoriosAdmissionaisEnum.AUTORIZACAO_DESCONTO_UNIFORMES)){
			return relatorioRhModulo.getAutorizacaoDescontoUniformes(listFuncionariosSelecionados);
		} else if (relatorio.equals(RelatoriosAdmissionaisEnum.AUTORIZACAO_DESCONTO_CONTRIBUICAO_ASSISTENCIAL_FAMILIAR)){
			return relatorioRhModulo.getAutorizacaoDescContribAssitencialFamiliar(listFuncionariosSelecionados);
		} else if(relatorio.equals(RelatoriosAdmissionaisEnum.TERMO_RESPONSABILIDADE_CRACHA)) {
			return relatorioRhModulo.getTermoResponsabilidadeCracha(listFuncionariosSelecionados);
		}
		return null;
	}

	private boolean isFuncionarioSelecionado() {
		return funcionario != null && funcionario.getId() != null;
	}
	
	public void doLimpar() {
		funcionario = new Funcionario();
		funcionario.setPessoa(new Pessoa());
		relatorio = null;
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
	
	public String doRelatorioFuncionarios() {
		return NavigationEnum.RELATORIO_FUNCIONARIOS.getValor();
	}
	
	public boolean isConsultarRelatorioFuncionarios(){
		return isConsultar();
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_VALE_TRANSPORTE.getCodigo());
	}
	
	/**
	 * @return the funcionario
	 */
	public Funcionario getFuncionario() {
		if(funcionario.getPessoa() == null){
			funcionario.setPessoa(new Pessoa());
		}
		return funcionario;
	}

	/**
	 * @param funcionario the funcionario to set
	 */
	public void setFuncionario(final Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	/**
	 * @return the itensRelatorio
	 */
	public List<SelectItem> getItensRelatorios() {
		List<RelatoriosAdmissionaisEnum> listRelatoriosAdmissionais = getRelatoriosAdmissionais();
		if(listRelatoriosAdmissionais != null){
			itensRelatorios.clear();
			addMockSimple(itensRelatorios, MockEnum.SELECIONE);
			for(RelatoriosAdmissionaisEnum relatoriosAdmissionais : listRelatoriosAdmissionais){
				SelectItem item = new SelectItem(relatoriosAdmissionais, relatoriosAdmissionais.getRelatorio());
				itensRelatorios.add(item);
			}
		}
		return itensRelatorios;
	}
	
	public void limparFuncionario(){
		if (funcionario == null || funcionario.getPessoa() == null
				|| funcionario.getPessoa().getNome() == null
				|| funcionario.getPessoa().getNome().equals("")) {
			funcionario = new Funcionario();
			funcionario.setPessoa(new Pessoa());
		}
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

	/**
	 * @return
	 */
	private List<RelatoriosAdmissionaisEnum> getRelatoriosAdmissionais() {
		if(itensRelatorios.isEmpty()){ 
			return new ArrayList<RelatoriosAdmissionaisEnum>(Arrays.asList(RelatoriosAdmissionaisEnum.values()));
		}
		return null;
	}

	/**
	 * @param itensRelatorio the itensRelatorio to set
	 */
	public void setItensRelatorios(final List<SelectItem> itensRelatorio) {
		this.itensRelatorios = itensRelatorio;
	}

	/**
	 * @return the relatorio
	 */
	public RelatoriosAdmissionaisEnum getRelatorio() {
		return relatorio;
	}

	/**
	 * @param relatorio the relatorio to set
	 */
	public void setRelatorio(final RelatoriosAdmissionaisEnum relatorio) {
		this.relatorio = relatorio;
	}

	/**
	 * @return the funcionarioSelecionado
	 */
	public Funcionario getFuncionarioSelecionado() {
		return funcionarioSelecionado;
	}

	/**
	 * @param funcionarioSelecionado the funcionarioSelecionado to set
	 */
	public void setFuncionarioSelecionado(final Funcionario funcionarioSelecionado) {
		this.funcionarioSelecionado = funcionarioSelecionado;
	}
	
	/**
	 * @return the projecoesFuncionarioSelecionadas
	 */
	public List<String> getProjecoesFuncionarioSelecionadas() {
		return projecoesFuncionarioSelecionadas;
	}

	/**
	 * @param projecoesFuncionarioSelecionadas the projecoesFuncionarioSelecionadas to set
	 */
	public void setProjecoesFuncionarioSelecionadas(
			final List<String> projecoesFuncionarioSelecionadas) {
		this.projecoesFuncionarioSelecionadas = projecoesFuncionarioSelecionadas;
	}

	/**
	 * @return the projecoesFuncionario
	 */
	public List<SelectItem> getProjecoesFuncionario() {
		
		projecoesFuncionario.add(new SelectItem("Teste", "Teste"));
		projecoesFuncionario.add(new SelectItem("Teste 1", "Teste 1"));
		projecoesFuncionario.add(new SelectItem("Teste 2", "Teste 2"));
		return projecoesFuncionario;
	}

	/**
	 * @param projecoesFuncionario the projecoesFuncionario to set
	 */
	public void setProjecoesFuncionario(final List<SelectItem> projecoesFuncionario) {
		this.projecoesFuncionario = projecoesFuncionario;
	}

	@Override
	public Logger getLogger() {
		return log;
	}
}
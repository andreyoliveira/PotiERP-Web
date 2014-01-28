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
import br.com.potierp.model.RelatoriosDiversosEnum;
import br.com.potierp.model.SituacaoFuncionario;
import br.com.potierp.relatorio.rh.facade.RelatorioRhModulo;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

/**
 * @author Andrey Oliveira
 *
 */
public class RelatoriosDiversosMB extends BaseMB{
	
	private static Logger log = Logger.getLogger(RelatoriosDiversosMB.class);
	
	private Funcionario funcionario = new Funcionario();
	
	private Funcionario funcionarioSelecionado = new Funcionario();
	
	private List<Funcionario> sugestoesFuncionarios;
	
	private RelatoriosDiversosEnum relatorio;
	
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
		if(relatorio.equals(RelatoriosDiversosEnum.PROTOCOLO_ENTREGA_ATESTADO_MEDICO)){
			return relatorioRhModulo.getProtocoloEntregaAtestadoMedico(listFuncionariosSelecionados);
		} else if(relatorio.equals(RelatoriosDiversosEnum.PROTOCOLO_ENTREGA_CTPS)){
			return relatorioRhModulo.getReciboDevolucaoCTPS(listFuncionariosSelecionados);
		} else if(relatorio.equals(RelatoriosDiversosEnum.AUTORIZACAO_DESCONTO_EQUIPAMENTO)) {
			return relatorioRhModulo.getTermoAutorizacaoDescontoEquipamento(listFuncionariosSelecionados);
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
	
	public String doRelatoriosDiversos() {
		return NavigationEnum.RELATORIOS_DIVERSOS.getValor();
	}
	
	public boolean isConsultarRelatoriosDiversos(){
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
		List<RelatoriosDiversosEnum> listRelatoriosDiversos = getRelatoriosDiversos();
		if(listRelatoriosDiversos != null){
			itensRelatorios.clear();
			addMockSimple(itensRelatorios, MockEnum.SELECIONE);
			for(RelatoriosDiversosEnum relatoriosDiversos : listRelatoriosDiversos){
				SelectItem item = new SelectItem(relatoriosDiversos, relatoriosDiversos.getRelatorio());
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
	private List<RelatoriosDiversosEnum> getRelatoriosDiversos() {
		if(itensRelatorios.isEmpty()){ 
			return new ArrayList<RelatoriosDiversosEnum>(Arrays.asList(RelatoriosDiversosEnum.values()));
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
	public RelatoriosDiversosEnum getRelatorio() {
		return relatorio;
	}

	/**
	 * @param relatorio the relatorio to set
	 */
	public void setRelatorio(final RelatoriosDiversosEnum relatorio) {
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

	@Override
	public Logger getLogger() {
		return log;
	}
}
package br.com.potierp.modulos.rh.managed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;

import org.apache.log4j.Logger;

import br.com.potierp.business.rh.comparator.NomeFuncionarioComparator;
import br.com.potierp.business.rh.facade.RhModulo;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.exception.PotiErpMensagensException;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.model.Ferias;
import br.com.potierp.model.Funcionario;
import br.com.potierp.model.Pessoa;
import br.com.potierp.model.SituacaoFuncionario;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

/**
 * @author Andrey Oliveira
 */
public class FeriasMB extends BaseMB {

	private static Logger log = Logger.getLogger(FeriasMB.class);
	
	private Funcionario funcionarioSelecionado = new Funcionario();
	
	private Funcionario funcionario = new Funcionario();
	
	private Ferias feriasSelecionada = new Ferias();
	
	private Ferias ferias = new Ferias();
	
	private SelectionList<Ferias> listFerias = new SelectionList<Ferias>(new ArrayList<Ferias>());
	
	private List<Funcionario> sugestoesFuncionarios;
	
	private Integer scrollerPage = 1;
	
	private Boolean checkFeriasAll;
	
	private Boolean disableEdicao = false;
	
	@EJB
	private RhModulo rhModulo;
	
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
			ferias.setFuncionario(funcionario);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	private Funcionario getFuncionarioPorRE() throws Exception {
		if(sugestoesFuncionarios != null){
			for(Funcionario func : sugestoesFuncionarios){
				if(func.getCodigoRegistro() != null && func.getCodigoRegistro().equals(ferias.getFuncionario().getCodigoRegistro())){
					Funcionario funcionarioTemp = func.clone();
					funcionarioTemp.setPessoa(func.getPessoa().clone());
					return funcionarioTemp;
				}
			}
		}
		return null;
	}
	
	private boolean isRegistroEmpregadoPreenchido() {
		return ferias.getFuncionario() != null
				&& ferias.getFuncionario().getCodigoRegistro() != null
				&& ferias.getFuncionario().getCodigoRegistro() > 0;
	}
	
	private void carregarSugestoes() throws PotiErpException {
		sugestoesFuncionarios = rhModulo.consultarTodosFuncionariosComNomeRE();
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
	
	public void selecionarFuncionario() throws Exception {
		this.funcionario = this.funcionarioSelecionado.clone();
		this.funcionario.setPessoa(this.funcionarioSelecionado.getPessoa().clone());
		this.funcionarioSelecionado = new Funcionario();
		ferias.setFuncionario(funcionario);
	}
	
	public void doConsultar() {
		try {
			List<Ferias> ferias = rhModulo.consultarTodasFerias(getTraceInfo());
			this.listFerias = new SelectionList<Ferias>(new ArrayList<Ferias>(ferias));
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public void doAlterar() {
		try {
			ferias = this.feriasSelecionada.clone();
			Funcionario funcionario = this.feriasSelecionada.getFuncionario().clone();
			funcionario.setPessoa(this.feriasSelecionada.getFuncionario().getPessoa().clone());
			this.ferias.setFuncionario(funcionario);
			this.disableEdicao = true;
		} catch (CloneNotSupportedException e) {
			addMensagemErro(MensagensFacesEnum.ERRO_ALTERAR);
		}
	}
	
	public void doSalvar() {
		try {
			validarObrigatorios();
			SituacaoFuncionario situacaoFuncionario = new SituacaoFuncionario();
			if(ferias.getRetornoTrabalho() != null) {
				situacaoFuncionario.setId(1L);
			} else {
				situacaoFuncionario.setId(7L);
			}
			this.ferias.getFuncionario().setSituacaoFuncionario(situacaoFuncionario);
			this.rhModulo.salvarFerias(ferias, getTraceInfo());
			doNovo();
			doConsultar();
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_SALVAR);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	private void validarObrigatorios() throws PotiErpMensagensException {
		if(ferias.getFuncionario() == null || ferias.getFuncionario().getId() == null) {
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {"Funcionário"});
		} else if(ferias.getPeriodoInicialAquisitivo() == null) {
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {"Período Inicial Aquisitivo"});
		} else if(ferias.getPeriodoFinalAquisitivo() == null) {
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {"Período Final Aquisitivo"});
		} else if(ferias.getPeriodoInicialGozo() == null) {
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {"Período Inicial Gozo"});
		} else if(ferias.getPeriodoFinalGozo() == null) {
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {"Período Final Gozo"});
		} else if(ferias.getFeriasColetivas() == null) {
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {"Férias Coletivas"});
		}
	}
	
	public void doExcluirLote() {
		List<Ferias> ferias = this.listFerias.getItensSelecionados();
		if(!ferias.isEmpty()) {
			this.listFerias.removeSelectedItem();
			try {
				this.rhModulo.excluirListaFerias(ferias, getTraceInfo());
			} catch (PotiErpException e) {
				addMensagemErro(MensagensFacesEnum.ERRO_EXCLUIR_LISTA);
			}
		}
	}
	
	public void doNovo() {
		this.ferias = new Ferias();
		this.feriasSelecionada = new Ferias();
		this.funcionario = new Funcionario();
		this.funcionarioSelecionado = new Funcionario();
		this.checkFeriasAll = false;
		this.disableEdicao = false;
	}
	
	public String doFerias() {
		doConsultar();
		return NavigationEnum.FERIAS.getValor();
	}
	
	public boolean isConsultarFerias() {
		return isConsultar();
	}
	
	private boolean isConsultar() {
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_VALE_TRANSPORTE.getCodigo());
	}
	
	public Funcionario getFuncionarioSelecionado() {
		return funcionarioSelecionado;
	}

	public void setFuncionarioSelecionado(final Funcionario funcionarioSelecionado) {
		this.funcionarioSelecionado = funcionarioSelecionado;
	}

	public Funcionario getFuncionario() {
		if(funcionario.getPessoa() == null) {
			funcionario.setPessoa(new Pessoa());
		}
		return funcionario;
	}

	public void setFuncionario(final Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public List<Funcionario> getSugestoesFuncionarios() {
		return sugestoesFuncionarios;
	}

	public void setSugestoesFuncionarios(final List<Funcionario> sugestoesFuncionarios) {
		this.sugestoesFuncionarios = sugestoesFuncionarios;
	}

	public Integer getScrollerPage() {
		return scrollerPage;
	}

	public void setScrollerPage(final Integer scrollerPage) {
		this.scrollerPage = scrollerPage;
	}

	public Ferias getFeriasSelecionada() {
		return feriasSelecionada;
	}

	public void setFeriasSelecionada(final Ferias feriasSelecionada) {
		this.feriasSelecionada = feriasSelecionada;
	}

	public Ferias getFerias() {
		if(ferias.getFuncionario() == null) {
			Funcionario funcionario = new Funcionario();
			funcionario.setPessoa(new Pessoa());
			ferias.setFuncionario(funcionario);
		}
		return ferias;
	}

	public void setFerias(final Ferias ferias) {
		this.ferias = ferias;
	}

	public SelectionList<Ferias> getListFerias() {
		return listFerias;
	}

	public void setListFerias(final SelectionList<Ferias> listFerias) {
		this.listFerias = listFerias;
	}

	public Boolean getCheckFeriasAll() {
		return checkFeriasAll;
	}

	public void setCheckFeriasAll(final Boolean checkFeriasAll) {
		this.checkFeriasAll = checkFeriasAll;
	}

	public Boolean getDisableEdicao() {
		return disableEdicao;
	}

	public void setDisableEdicao(final Boolean disableEdicao) {
		this.disableEdicao = disableEdicao;
	}

	@Override
	public Logger getLogger() {
		return log;
	}
}

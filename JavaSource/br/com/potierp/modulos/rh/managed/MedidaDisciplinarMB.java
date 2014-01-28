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
import br.com.potierp.infra.exception.PotiErpMensagensException;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.model.Funcionario;
import br.com.potierp.model.MedidaDisciplinar;
import br.com.potierp.model.MedidaDisciplinarAdotadaEnum;
import br.com.potierp.model.MotivoMedidaDisciplinarEnum;
import br.com.potierp.model.Pessoa;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

/**
 * @author Andrey Oliveira
 * @since 22/03/2012
 *
 */
public class MedidaDisciplinarMB extends BaseMB {

	private static Logger log = Logger.getLogger(MedidaDisciplinarMB.class);

	private Boolean checkMedidaDisciplinarAll;
	
	private Integer scrollerPage = 1;
	
	private SelectionList<MedidaDisciplinar> listMedidaDisciplinar = new SelectionList<MedidaDisciplinar>(new ArrayList<MedidaDisciplinar>());
	
	private MedidaDisciplinar medidaDisciplinar = new MedidaDisciplinar();
	
	private MedidaDisciplinar medidaDisciplinarSelecionada = new MedidaDisciplinar();
	
	private Boolean disableEdicao;
	
	private List<Funcionario> sugestoesFuncionarios;
	
	private Funcionario funcionario = new Funcionario();
	
	private Funcionario funcionarioSelecionado = new Funcionario();
	
	private List<SelectItem> itensMotivoMedidaDisciplinar = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensMedidaDisciplinarAdotada = new ArrayList<SelectItem>();
	
	private Boolean enableObservacao = false;
	
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
			medidaDisciplinar.setFuncionario(funcionario);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	private Funcionario getFuncionarioPorRE() throws Exception {
		if(sugestoesFuncionarios != null){
			for(Funcionario func : sugestoesFuncionarios){
				if(func.getCodigoRegistro() != null && func.getCodigoRegistro().equals(medidaDisciplinar.getFuncionario().getCodigoRegistro())){
					Funcionario funcionarioTemp = func.clone();
					funcionarioTemp.setPessoa(func.getPessoa().clone());
					return funcionarioTemp;
				}
			}
		}
		return null;
	}
	
	private boolean isRegistroEmpregadoPreenchido() {
		return medidaDisciplinar.getFuncionario() != null
				&& medidaDisciplinar.getFuncionario().getCodigoRegistro() != null
				&& medidaDisciplinar.getFuncionario().getCodigoRegistro() > 0;
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
		medidaDisciplinar.setFuncionario(funcionario);
	}
	
	public void doNovo() {
		this.medidaDisciplinar = new MedidaDisciplinar();
		this.medidaDisciplinarSelecionada = new MedidaDisciplinar();
		this.funcionario = new Funcionario();
		this.funcionarioSelecionado = new Funcionario();
		this.checkMedidaDisciplinarAll = false;
		this.disableEdicao = false;
		this.enableObservacao = false;
		
	}
	
	public void doSalvar() {
		try {
			this.validarObrigatorios();
			this.rhModulo.salvarMedidaDisciplinar(this.medidaDisciplinar, getTraceInfo());
			this.doNovo();
			this.doConsultar();
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_SALVAR);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	private void validarObrigatorios() throws PotiErpMensagensException {
		if(this.medidaDisciplinar.getFuncionario() == null || this.medidaDisciplinar.getFuncionario().getId() == null) {
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {"Funcion�rio"});
		} else if(this.medidaDisciplinar.getDataInicialOcorrencia() == null) {
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {"Data Inicial Ocorrência"});
		} else if(this.medidaDisciplinar.getDataFinalOcorrencia() == null) {
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {"Data Final Ocorrência"});
		} else if(this.medidaDisciplinar.getMotivo() == null) {
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {"Motivo Medida Disciplinar"});
		} else if(this.medidaDisciplinar.getMedidaAdotada() == null) {
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {"Medida Disciplinar Adotada"});
		} else if(this.medidaDisciplinar.getMotivo() != null && this.medidaDisciplinar.getMotivo().equals(MotivoMedidaDisciplinarEnum.OUTROS) &&
				(this.medidaDisciplinar.getObservacao() == null || this.medidaDisciplinar.getObservacao().equals(""))) {
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {"Observa��o"});
		}
	}
	
	public void doConsultar() {
		try {
			List<MedidaDisciplinar> medidas = this.rhModulo.consultarTodasMedidasDisciplinares(getTraceInfo());
			this.listMedidaDisciplinar = new SelectionList<MedidaDisciplinar>(medidas);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public void doExcluirLote() {
		List<MedidaDisciplinar> medidas = this.listMedidaDisciplinar.getItensSelecionados();
		if(!medidas.isEmpty()) {
			this.listMedidaDisciplinar.removeSelectedItem();
			this.doNovo();
			try {
				this.rhModulo.excluirListaMedidasDisciplinares(medidas, getTraceInfo());
			} catch (PotiErpException e) {
				addMensagemErro(MensagensFacesEnum.ERRO_EXCLUIR_LISTA);
			}
		} else {
			addMensagemErro(MensagensFacesEnum.ERRO_SELECIONE_UM_ITEM_DA_LISTAGEM);
		}
	}
	
	public void doAlterar() {
		try {
			this.medidaDisciplinar = this.medidaDisciplinarSelecionada.clone();
			Funcionario funcionario = this.medidaDisciplinarSelecionada.getFuncionario().clone();
			funcionario.setPessoa(this.medidaDisciplinarSelecionada.getFuncionario().getPessoa().clone());
			this.medidaDisciplinar.setFuncionario(funcionario);
			this.disableEdicao = true;
			doEnableObservacao();
		} catch (CloneNotSupportedException e) {
			addMensagemErro(MensagensFacesEnum.ERRO_ALTERAR);
		}
	}
	
	public void doEnableObservacao() {
		if(this.medidaDisciplinar.getMotivo().equals(MotivoMedidaDisciplinarEnum.OUTROS)) {
			this.enableObservacao = true;
		} else {
			this.enableObservacao = false;
		}
	}
	
	public String doMedidaDisciplinar() {
		this.doConsultar();
		return NavigationEnum.MEDIDA_DISCIPLINAR.getValor();
	}
	
	public boolean isIncluirMedidaDisciplinar() {
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isAlterarMedidaDisciplinar() {
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isConsultar() {
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isExcluirMedidaDisciplinar() {
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isConsultarMedidaDisciplinar() {
		return isIncluirMedidaDisciplinar() || isExcluirMedidaDisciplinar() || isAlterarMedidaDisciplinar() || isConsultar();
	}
	
	public Boolean getCheckMedidaDisciplinarAll() {
		return checkMedidaDisciplinarAll;
	}

	public void setCheckMedidaDisciplinarAll(Boolean checkMedidaDisciplinarAll) {
		this.checkMedidaDisciplinarAll = checkMedidaDisciplinarAll;
	}

	public Integer getScrollerPage() {
		return scrollerPage;
	}

	public void setScrollerPage(Integer scrollerPage) {
		this.scrollerPage = scrollerPage;
	}

	public SelectionList<MedidaDisciplinar> getListMedidaDisciplinar() {
		return listMedidaDisciplinar;
	}

	public void setListMedidaDisciplinar(
			SelectionList<MedidaDisciplinar> listMedidaDisciplinar) {
		this.listMedidaDisciplinar = listMedidaDisciplinar;
	}

	public MedidaDisciplinar getMedidaDisciplinar() {
		if(this.medidaDisciplinar.getFuncionario() == null) {
			Funcionario funcionario = new Funcionario();
			funcionario.setPessoa(new Pessoa());
			this.medidaDisciplinar.setFuncionario(funcionario);
		}
		return medidaDisciplinar;
	}

	public void setMedidaDisciplinar(MedidaDisciplinar medidaDisciplinar) {
		this.medidaDisciplinar = medidaDisciplinar;
	}

	public MedidaDisciplinar getMedidaDisciplinarSelecionada() {
		return medidaDisciplinarSelecionada;
	}

	public void setMedidaDisciplinarSelecionada(
			MedidaDisciplinar medidaDisciplinarSelecionada) {
		this.medidaDisciplinarSelecionada = medidaDisciplinarSelecionada;
	}

	public Boolean getDisableEdicao() {
		return disableEdicao;
	}

	public void setDisableEdicao(Boolean disableEdicao) {
		this.disableEdicao = disableEdicao;
	}

	public List<Funcionario> getSugestoesFuncionarios() {
		return sugestoesFuncionarios;
	}

	public void setSugestoesFuncionarios(List<Funcionario> sugestoesFuncionarios) {
		this.sugestoesFuncionarios = sugestoesFuncionarios;
	}

	public Funcionario getFuncionario() {
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

	public List<SelectItem> getItensMotivoMedidaDisciplinar() {
		List<MotivoMedidaDisciplinarEnum> listMotivo = getListMotivoMedidaDisciplinar();
		if(listMotivo != null) {
			itensMotivoMedidaDisciplinar.clear();
			addMockSimple(itensMotivoMedidaDisciplinar, MockEnum.SELECIONE);
			for(MotivoMedidaDisciplinarEnum motivo : listMotivo) {
				SelectItem item = new SelectItem(motivo, motivo.getMotivo());
				itensMotivoMedidaDisciplinar.add(item);
			}
		}
		return itensMotivoMedidaDisciplinar;
	}
	
	public List<MotivoMedidaDisciplinarEnum> getListMotivoMedidaDisciplinar() {
		if(itensMotivoMedidaDisciplinar.isEmpty()) {
			return new ArrayList<MotivoMedidaDisciplinarEnum>(Arrays.asList(MotivoMedidaDisciplinarEnum.values()));
		}
		return null;
	}

	public void setItensMotivoMedidaDisciplinar(
			List<SelectItem> itensMotivoMedidaDisciplinar) {
		this.itensMotivoMedidaDisciplinar = itensMotivoMedidaDisciplinar;
	}

	public List<SelectItem> getItensMedidaDisciplinarAdotada() {
		List<MedidaDisciplinarAdotadaEnum> listMedida = getListMedidaDisciplinarAdotada();
		if(listMedida != null) {
			itensMedidaDisciplinarAdotada.clear();
			addMockSimple(itensMedidaDisciplinarAdotada, MockEnum.SELECIONE);
			for(MedidaDisciplinarAdotadaEnum medida : listMedida) {
				SelectItem item = new SelectItem(medida, medida.getMedidaDisciplinar());
				itensMedidaDisciplinarAdotada.add(item);
			}
		}
		return itensMedidaDisciplinarAdotada;
	}
	
	public List<MedidaDisciplinarAdotadaEnum> getListMedidaDisciplinarAdotada() {
		if(itensMedidaDisciplinarAdotada.isEmpty()) {
			return new ArrayList<MedidaDisciplinarAdotadaEnum>(Arrays.asList(MedidaDisciplinarAdotadaEnum.values()));
		}
		return null;
	}

	public void setItensMedidaDisciplinarAdotada(
			List<SelectItem> itensMedidaDisciplinarAdotada) {
		this.itensMedidaDisciplinarAdotada = itensMedidaDisciplinarAdotada;
	}

	public Boolean getEnableObservacao() {
		return enableObservacao;
	}

	public void setEnableObservacao(Boolean enableObservacao) {
		this.enableObservacao = enableObservacao;
	}

	@Override
	public Logger getLogger() {
		return log;
	}
}

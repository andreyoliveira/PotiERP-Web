package br.com.potierp.modulos.operacional.managed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import br.com.potierp.business.operacional.comparator.NomeClienteComparator;
import br.com.potierp.business.operacional.facade.OperacionalModulo;
import br.com.potierp.faces.converter.MockEnum;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.exception.PotiErpMensagensException;
import br.com.potierp.infra.helper.SelectionList;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.model.Cliente;
import br.com.potierp.model.ProgramacaoServico;
import br.com.potierp.model.TipoServico;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

/**
 * 
 * @author Andrey Oliveira
 *
 */
public class ProgramacaoServicoMB extends BaseMB {
	
	private static Logger log = Logger.getLogger(ProgramacaoServicoMB.class);
	
	private SelectionList<ProgramacaoServico> listProgramacaoServico = new SelectionList<ProgramacaoServico>(new ArrayList<ProgramacaoServico>());
	
	private ProgramacaoServico programacaoServico = new ProgramacaoServico();
	
	private ProgramacaoServico programacaoServicoSelecionada = new ProgramacaoServico();
	
	private List<Cliente> sugestoesClientes;
	
	private Cliente cliente = new Cliente();
	
	private Cliente clienteSelecionado = new Cliente();
	
	private List<SelectItem> itensTipoServico = new ArrayList<SelectItem>();
	
	private Boolean checkProgramacaoServicoAll;
	
	private Integer scrollerPage = 1;
	
	private Boolean disableEdicao;
	
	@EJB
	private OperacionalModulo operacionalModulo;

	public void buscarCliente() {
		try {
			if(isCodigoClientePreenchido()) {
				if(this.sugestoesClientes == null) {
					carregarSugestoes();
				}
				this.cliente = getClientePorCodigo();
				if(this.cliente == null) {
					addMensagemErro(MensagensFacesEnum.ERRO_CLIENTE_NAO_ENCONTRADO);
					this.cliente = new Cliente();
				}
			} else {
				this.cliente = new Cliente();
			}
			this.programacaoServico.setCliente(this.cliente);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	private void carregarSugestoes() throws PotiErpException {
		this.sugestoesClientes = operacionalModulo.consultarTodosClientesComNomeFantasiaCodigo();
	}
	
	public List<Cliente> sugestao(final Object evento) throws Exception {
		if(this.sugestoesClientes == null) {
			carregarSugestoes();
		}
		List<Cliente> clientes = new ArrayList<Cliente>();
		try {
			String prefixo = evento.toString().trim().toLowerCase();
			for(Cliente cliente : this.sugestoesClientes) {
				if(cliente.getNomeFantasia().trim().toLowerCase().startsWith(prefixo)) {
					clientes.add(cliente);
				}
			}
		} catch (Exception e) {
			addMensagemErro(e.getMessage());
		}
		Collections.sort(clientes, new NomeClienteComparator());
		return clientes;
	}
	
	private boolean isCodigoClientePreenchido() {
		return this.programacaoServico.getCliente() != null &&
				this.programacaoServico.getCliente().getCodigo() != null &&
				!this.programacaoServico.getCliente().getCodigo().equals("");
	}
	
	private Cliente getClientePorCodigo() throws Exception {
		if(this.sugestoesClientes != null) {
			for(Cliente cliente : this.sugestoesClientes) {
				if(cliente.getCodigo() != null && cliente.getCodigo().equals(this.programacaoServico.getCliente().getCodigo())) {
					Cliente clienteTemp = cliente.clone();
					return clienteTemp;
				}
			}
		}
		return null;
	}
	
	public void selecionarCliente() throws Exception {
		this.cliente = this.clienteSelecionado.clone();
		this.clienteSelecionado = new Cliente();
		this.programacaoServico.setCliente(this.cliente);
	}
	
	public void doNovo( ) {
		this.programacaoServico = new ProgramacaoServico();
		this.programacaoServicoSelecionada = new ProgramacaoServico();
		this.cliente = new Cliente();
		this.clienteSelecionado = new Cliente();
		this.checkProgramacaoServicoAll = false;
		this.disableEdicao = false;
	}
	
	public void doSalvar() {
		try {
			validarObrigatorios();
			this.operacionalModulo.salvarProgramacaoServico(this.programacaoServico, getTraceInfo());
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
	
	private void validarObrigatorios() throws PotiErpException {
		if(this.programacaoServico.getCliente() == null || this.programacaoServico.getCliente().getId() == null) {
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {"Cliente"});
		} else if(this.programacaoServico.getTipoServico() == null || this.programacaoServico.getTipoServico().getId() == null) {
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {"Tipo de Servi�o"});
		} else if(this.programacaoServico.getDataPrevisao() == null) {
			throw new PotiErpMensagensException(MensagensFacesEnum.ERRO_CAMPO_OBRIGATORIO.getKey(), new Object[] {"Data da Previs�o"});
		}
	}
	
	public void doConsultar() {
		try {
			List<ProgramacaoServico> programacoes = this.operacionalModulo.consultarTodasProgramacoesServico(getTraceInfo());
			this.listProgramacaoServico = new SelectionList<ProgramacaoServico>(programacoes);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
	}
	
	public void doExcluirLote() {
		List<ProgramacaoServico> programacoes = this.listProgramacaoServico.getItensSelecionados();
		if(!programacoes.isEmpty()) {
			this.listProgramacaoServico.removeSelectedItem();
			doNovo();
			try {
				this.operacionalModulo.excluirListaProgramacaoServico(programacoes, getTraceInfo());
			} catch (PotiErpException e) {
				addMensagemErro(MensagensFacesEnum.ERRO_EXCLUIR_LISTA);
			}
		} else {
			addMensagemErro(MensagensFacesEnum.ERRO_SELECIONE_UM_ITEM_DA_LISTAGEM);
		}
	}
	
	public void doAlterar() {
		try {
			this.programacaoServico = this.programacaoServicoSelecionada.clone();
			Cliente cliente = this.programacaoServicoSelecionada.getCliente().clone();
			this.programacaoServico.setCliente(cliente);
			this.disableEdicao = true;
		} catch (CloneNotSupportedException e) {
			addMensagemErro(MensagensFacesEnum.ERRO_ALTERAR);
		}
	}
	
	public String doProgramacaoServico() {
		doConsultar();
		return NavigationEnum.PROGRAMACAO_SERVICO.getValor();
	}
	
	public boolean isIncluirProgramacaoServico() {
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.INCLUIR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isAlterarProgramacaoServico() {
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.ALTERAR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isConsultar() {
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isExcluirProgramacaoServico() {
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.EXCLUIR_VALE_TRANSPORTE.getCodigo());
	}
	
	public boolean isConsultarProgramacaoServico() {
		return isIncluirProgramacaoServico() || isExcluirProgramacaoServico() || isAlterarProgramacaoServico() || isConsultar();
	}
	
	public SelectionList<ProgramacaoServico> getListProgramacaoServico() {
		return listProgramacaoServico;
	}

	public void setListProgramacaoServico(
			final SelectionList<ProgramacaoServico> listProgramacaoServico) {
		this.listProgramacaoServico = listProgramacaoServico;
	}

	public ProgramacaoServico getProgramacaoServico() {
		if(this.programacaoServico.getCliente() == null) {
			Cliente cliente = new Cliente();
			this.programacaoServico.setCliente(cliente);
		}
		
		if(this.programacaoServico.getTipoServico() == null) {
			TipoServico tipoServico = new TipoServico();
			this.programacaoServico.setTipoServico(tipoServico);
		}
		return programacaoServico;
	}

	public void setProgramacaoServico(final ProgramacaoServico programacaoServico) {
		this.programacaoServico = programacaoServico;
	}

	public ProgramacaoServico getProgramacaoServicoSelecionada() {
		return programacaoServicoSelecionada;
	}

	public void setProgramacaoServicoSelecionada(
			final ProgramacaoServico programacaoServicoSelecionada) {
		this.programacaoServicoSelecionada = programacaoServicoSelecionada;
	}

	public List<Cliente> getSugestoesClientes() {
		return sugestoesClientes;
	}

	public void setSugestoesClientes(final List<Cliente> sugestoesClientes) {
		this.sugestoesClientes = sugestoesClientes;
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

	public List<SelectItem> getItensTipoServico() throws PotiErpException {
		if(this.itensTipoServico.isEmpty()) {
			List<TipoServico> listTipoServico =	this.operacionalModulo.getAllTipoServico(getTraceInfo());
			if(listTipoServico != null) {
				this.itensTipoServico.clear();
				addMock(this.itensTipoServico, MockEnum.SELECIONE);
				for(TipoServico tipoServico : listTipoServico) {
					SelectItem item = new SelectItem(tipoServico, tipoServico.getDescricao());
					this.itensTipoServico.add(item);
				}
			}
		}
		return itensTipoServico;
	}
	
	public void setItensTipoServico(final List<SelectItem> itensTipoServico) {
		this.itensTipoServico = itensTipoServico;
	}

	public Boolean getCheckProgramacaoServicoAll() {
		return checkProgramacaoServicoAll;
	}

	public void setCheckProgramacaoServicoAll(final Boolean checkProgramacaoServicoAll) {
		this.checkProgramacaoServicoAll = checkProgramacaoServicoAll;
	}

	public Integer getScrollerPage() {
		return scrollerPage;
	}

	public void setScrollerPage(final Integer scrollerPage) {
		this.scrollerPage = scrollerPage;
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

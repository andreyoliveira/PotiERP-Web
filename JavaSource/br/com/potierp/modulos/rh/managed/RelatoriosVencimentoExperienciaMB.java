package br.com.potierp.modulos.rh.managed;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import br.com.potierp.business.endereco.facade.EnderecoModulo;
import br.com.potierp.business.rh.facade.RhModulo;
import br.com.potierp.faces.converter.MockEnum;
import br.com.potierp.faces.managed.BaseMB;
import br.com.potierp.infra.exception.PotiErpException;
import br.com.potierp.infra.exception.PotiErpMensagensException;
import br.com.potierp.infra.msg.MensagensFacesEnum;
import br.com.potierp.infra.resource.PotiErpProperties;
import br.com.potierp.model.Cidade;
import br.com.potierp.model.Cliente;
import br.com.potierp.model.Funcionario;
import br.com.potierp.model.Responsavel;
import br.com.potierp.relatorio.rh.facade.RelatorioRhModulo;
import br.com.potierp.util.FuncionalidadeEnum;
import br.com.potierp.util.NavigationEnum;

/**
 * @author Doug
 *
 */
public class RelatoriosVencimentoExperienciaMB extends BaseMB{
	
	private static Logger log = Logger.getLogger(RelatoriosVencimentoExperienciaMB.class);
	
	private List<Funcionario> listFuncionarios = new ArrayList<Funcionario>();
	
	private Date dataInicio;
	
	private Date dataFim;
	
	private Cidade cidade;
	
	private Responsavel responsavel;
	
	private List<SelectItem> itensCidade = new ArrayList<SelectItem>();
	
	private List<SelectItem> itensResponsavel = new ArrayList<SelectItem>();
	
	@EJB
	private RelatorioRhModulo relatorioRhModulo;
	
	@EJB
	private RhModulo rhModulo;
	
	@EJB
	private EnderecoModulo enderecoModulo;
	
	public String doRelatoriosVencimentoExperiencia() {
		return NavigationEnum.RELATORIOS_VENCIMENTO_EXPERIENCIA.getValor();
	}
	
	public boolean isConsultarRelatoriosVencimentoExperiencia(){
		return isConsultar();
	}
	
	private boolean isConsultar(){
		return super.getUsuario().getFuncionalidades().contains(FuncionalidadeEnum.CONSULTAR_VALE_TRANSPORTE.getCodigo());
	}
	
	public void doGerar() {
		try{
			buscarFuncionarios();
			if(!listFuncionarios.isEmpty()){
				byte[] rel = relatorioRhModulo.getVencimentoExperiencia(listFuncionarios, this.dataInicio, this.dataFim, this.cidade, this.responsavel);
				if(rel != null){
					super.registraArquivoParaDownload(rel, "Vencimento Experiência", "pdf", PotiErpProperties.getInstance().getReportPath());
				}
			}else{
				addMensagemErro(MensagensFacesEnum.ERRO_FUNCIONARIOS_NAO_FORAM_ENCONTRADOS);
			}
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (Exception e) {
			addMensagemErro(e);
		}
	}
	
	/**
	 * 
	 */
	private void buscarFuncionarios() {
		try {
			if(isDatasPreenchidas()){
				Collection<Cliente> clientes = new ArrayList<Cliente>();
				if(this.responsavel != null) {
					clientes = this.responsavel.getClientes();
				}
				listFuncionarios = rhModulo.consultarFuncionarioPorDataAdmissao(this.dataInicio, this.dataFim, this.cidade, clientes);
			}
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(MensagensFacesEnum.ERRO_CARREGAR_LISTAGEM);
		} catch (Exception e){
			addMensagemErro(e.getMessage());
		}
		
	}

	/**
	 * @return
	 */
	private boolean isDatasPreenchidas() {
		return dataInicio != null && dataFim != null;
	}

	public void doLimpar() {
		listFuncionarios = new ArrayList<Funcionario>();
		this.dataFim = null;
		this.dataFim = null;
		this.cidade = new Cidade();
		//this.responsavel = new Responsavel();
	}

	/**
	 * @return the dataInicio
	 */
	public Date getDataInicio() {
		return dataInicio;
	}

	/**
	 * @param dataInicio the dataInicio to set
	 */
	public void setDataInicio(final Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	/**
	 * @return the dataFim
	 */
	public Date getDataFim() {
		return dataFim;
	}

	/**
	 * @param dataFim the dataFim to set
	 */
	public void setDataFim(final Date dataFim) {
		this.dataFim = dataFim;
	}

	/**
	 * @return the itensCidade
	 */
	public List<SelectItem> getItensCidade() {
		try {
			if(itensCidade.isEmpty()) {
				List<Cidade> cidades = enderecoModulo.consultarTodasCidades();
				itensCidade.clear();
				addMock(itensCidade, MockEnum.SELECIONE);
				for (Cidade cidade : cidades) {
					itensCidade.add(new SelectItem(cidade, cidade.getNome()));
				}
			}
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(new PotiErpMensagensException(pe.getMessage()));
		} catch (Exception e){
			addMensagemErro(MensagensFacesEnum.ERRO_BUSCAR_CIDADES);
		}
		return itensCidade;
	}

	/**
	 * @param itensCidade the itensCidade to set
	 */
	public void setItensCidade(final List<SelectItem> itensCidade) {
		this.itensCidade = itensCidade;
	}

	/**
	 * @return the cidade
	 */
	public Cidade getCidade() {
		return cidade;
	}

	/**
	 * @param cidade the cidade to set
	 */
	public void setCidade(final Cidade cidade) {
		this.cidade = cidade;
	}

	/**
	 * @return the itensResponsavel
	 */
	public List<SelectItem> getItensResponsavel() {
		try {
			if(itensResponsavel.isEmpty()) {
				List<Responsavel> responsaveis = rhModulo.consultarTodosResponsaveis();
				itensResponsavel.clear();
				addMock(itensResponsavel, MockEnum.SELECIONE);
				for (Responsavel resp : responsaveis) {
					String nomeResponsavel = resp.getFuncionario().getPessoa().getNome();
					itensResponsavel.add(new SelectItem(resp, nomeResponsavel));
				}
			}
		} catch (PotiErpMensagensException pme) {
			addMensagemErro(pme);
		} catch (PotiErpException pe) {
			addMensagemErro(new PotiErpMensagensException(pe.getMessage()));
		} catch (Exception e){
			addMensagemErro(MensagensFacesEnum.ERRO_BUSCAR_RESPONSAVEIS);
		}
		
		return itensResponsavel;
	}

	/**
	 * @param itensResponsavel the itensResponsavel to set
	 */
	public void setItensResponsavel(final List<SelectItem> itensResponsavel) {
		this.itensResponsavel = itensResponsavel;
	}

	/**
	 * @return the responsavel
	 */
	public Responsavel getResponsavel() {
		return responsavel;
	}

	/**
	 * @param responsavel the responsavel to set
	 */
	public void setResponsavel(final Responsavel responsavel) {
		this.responsavel = responsavel;
	}

	/* (non-Javadoc)
	 * @see br.com.potierp.faces.managed.BaseMB#getLogger()
	 */
	@Override
	public Logger getLogger() {
		return log;
	}

}

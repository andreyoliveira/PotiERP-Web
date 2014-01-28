package br.com.potierp.util;

/**
 * Enum com as informações para as navegações.
 * @author felipe
 * $LastChangedBy: felipe $
 * 
 * $LastChangedDate: 2009-09-29 18:33:47 -0300 (ter, 29 set 2009) $
 */
public enum NavigationEnum {
	
	/*
	 * Página de Login do Sistema.
	 */
	LOGIN("login"),
	
	/*
	 * Página principal do Sistema.
	 */
	PRINCIPAL("index"),
	
	/*
	 * Página de alteração de senha do usuário.
	 */
	ALTERACAO_SENHA("alteracaoSenha"),
	
	CADASTRO_DE_EMPRESA("cadastroDeEmpresa"),
	
	CADASTRO_DE_CLIENTE("cadastroDeCliente"),
	
	CADASTRO_DE_HORARIOS("cadastroDeHorariosJornada"),
	
	CADASTRO_DE_TIPO_DE_DEMISSAO("cadastroDeTipoDemissao"),
	
	CADASTRO_DE_TIPO_DE_ADICIONAL("cadastroDeTipoAdicional"),
	
	CADASTRO_DE_DEPENDENTES("cadastroDeDependentes"),
	
	CADASTRO_DE_TIPO_DE_DESCONTO("cadastroDeTipoDesconto"),
	
	CADASTRO_DE_TIPO_DE_BENEFICIO("cadastroDeTipoBeneficio"),
	
	CADASTRO_DE_TIPO_DE_ENCARGO("cadastroDeTipoEncargo"),
	
	CADASTRO_DE_USUARIO("cadastroDeUsuario"),
	
	CADASTRO_DE_FORNECEDOR("cadastroDeFornecedor"),
	
	CADASTRO_DE_JORNADA_DE_TRABALHO("cadastroJornadaTrabalho"),
	
	CADASTRO_DE_PERFIL("cadastroPerfil"),
	
	CADASTRO_DE_FUNCIONARIO("cadastroFuncionario"),
	
	CADASTRO_DE_FORMAPAGAMENTO("cadastroFormaPagamento"),
	
	CADASTRO_DE_CARGO("cadastroCargo"),
	
	CADASTRO_DE_SETOR("cadastroSetor"),
	
	CADASTRO_DE_SITUACAOFUNCIONARIO("cadastroSituacaoFuncionario"),
	
	CADASTRO_DE_TIPOADMISSAO("cadastroTipoAdmissao"),
	
	CADASTRO_DE_VINCULOEMPREGATICIO("cadastroVinculoEmpregaticio"),
	
	CADASTRO_DE_TIPOVALETRANSPORTE("cadastroTipoValeTransporte"),
	
	CADASTRO_DE_TIPOVALEREFEICAO("cadastroTipoValeRefeicao"),
	
	CADASTRO_DE_TIPOCESTABASICA("cadastroTipoCestaBasica"),
	
	CADASTRO_DE_VALETRANSPORTE("cadastroValeTransporte"),
	
	CALCULO_DE_VALEREFEICAO("calculoValeRefeicao"),
	
	CADASTRO_DE_GRAUPARENTESCO("cadastroGrauParentesco"),
	
	CADASTRO_DE_VERBA("cadastroVerba"),
	
	PARAMETRO_DE_CIDADE("parametroCidade"),
	
	CADASTRO_DE_FERIADO("cadastroFeriado"),
	
	CADASTRO_DE_RESPONSAVEL("cadastroResponsavel"),
	
	RELATORIOS_ADMISSIONAIS("relatoriosAdmissionais"),
	
	RELATORIO_FUNCIONARIOS("relatorioFuncionarios"),
	
	RELATORIOS_DIVERSOS("relatoriosDiversos"),
	
	RELATORIOS_VENCIMENTO_EXPERIENCIA("relatoriosVencimentoExperiencia"),
	
	CARTAO_PONTO("cartaoPonto"),
	
	PARAMETROS_RH("parametrosRh"),
	
	APONTAMENTOS_FOLHA("apontamentosFolha"),
	
	ALTERACAO_SALARIAL("alteracaoSalarial"),
	
	ALTERACAO_CLIENTE("alteracaoCliente"),
	
	FERIAS("ferias"),
	
	HISTORICO_DEMISSAO("historicoDemissao"),
	
	HISTORICO_AFASTAMENTO("historicoAfastamento"),
	
	CADASTRO_DE_SOLICITACAO_PAGAMENTO("cadastroSolicitacaoPagamento"),
	
	MEDIDA_DISCIPLINAR("medidaDisciplinar"),
	
	CADASTRO_PROGRAMACAO_VISITAS_SUPERVISORAS("cadastroProgramacaoVisitas"),
	
	CADASTRO_DE_TIPOSERVICO("cadastroTipoServico"),
	
	ACOMPANHAMENTO_VISITAS_SUPERVISORAS("acompanhamentoVisitas"),
	
	PROGRAMACAO_SERVICO("programacaoServico"),
	
	RELATORIO_PROGRAMACAO_VISITAS("relatorioProgramacaoVisitas"),	
	
	RELATORIO_ACOMPANHAMENTO_VISITAS("relatorioAcompanhamentoVisitas"), 
	
	HISTORICO_COMERCIAL("historicoComercial");
	
	/**
	 * Construtor da Enum.
	 * 
	 * @param valor
	 *  Nome da página de redirecionamento.
	 */
	NavigationEnum(final String valor) {
		this.valor = valor;
	}

	/**
	 * Nome da página de redirecionamento.
	 */
	private String valor;

	/**
	 * Devolve a página de redirecionamento.
	 * 
	 * @return Página de redirecionamento.
	 */
	public String getValor() {
		return this.valor;
	}
}
package br.com.potierp.infra.msg;

/**
 * Enum que classifica mensagens (i18n) do sistema PotiErp.
 *
 * @author renan
 *  	   <p>
 *         $LastChangedBy: $
 *         <p>
 *         $LastChangedDate: $
 */
public enum MensagensFacesEnum {

	/*---------- MENSAGENS PARA UM CADASTRO COMUM ------------*/

	/**
	 * Dados incluidos com sucesso!
	 */
	SUCESSO_INSERIR("sucessoInserir"),

	/**
	 * Dados excluidos com sucesso!
	 */
	SUCESSO_EXCLUIR("sucessoExcluir"),
	
	/**
	 * Dados alterados com sucesso!
	 */
	SUCESSO_ALTERAR("sucessoAlterar"),

	/**
	 * Ocorreu erro ao salvar! Verifique os dados do cadastro est�o ok.
	 */
	ERRO_SALVAR("erroAoSalvar"),

	/**
	 * Erro ao excluir o registro!
	 */
	ERRO_EXCLUIR("erroAoExcluir"),
	
	/**
	 * Erro ao excluir os registros!
	 */
	ERRO_EXCLUIR_LISTA("erroAoExcluirRegistros"),

	/**
	 * Erro ao alterar o registro!
	 */
	ERRO_ALTERAR("erroAoAlterar"),

	/**
	 * Operação não Permitida!
	 */
	OPERACAO_NAO_PERMITIDA("operacaoNaoPermitida"),

	/**
	 * Erro ao Carregar a Listagem!
	 */
	ERRO_CARREGAR_LISTAGEM("erroAoCarregarListagem"),
	
	/**
	 * Para essa operação selecione pelo menos um item da listagem
	 */
	ERRO_SELECIONE_UM_ITEM_DA_LISTAGEM("erroSelecioneUmItemDaListagem"),
	
	/**
	 * Erro ao cancelar
	 */
	ERRO_CANCELAR("erroAoCancelar"),
	

	/*-------------- MENSAGENS DE CONVERTER ---------------*/

	/**
	 * Erro ao converter Entidade.
	 */
	ERRO_CONVERTER_ENTIDADE("erro.converter.entidade"),

	/**
	 * Erro durante a conversão de data.
	 */
	ERRO_CONVERTER_CALENDAR("erro.converter.calendar"),

	/**
	 * Data inválida!
	 */
	ERRO_CONVERTER_DATA_INVALIDA("erro.converter.data.invalida"),
	
	/**
	 * Cpf inválido!
	 */
	CPF_INVALIDO("erro.cpf.invalido"),
	
	/**
	 * Cnpj inválido!
	 */
	CNPJ_INVALIDO("erro.cnpj.invalido"),
	
	/**
	 * E-Mail inválido!
	 */
	EMAIL_INVALIDO("erro.email.invalido"),
	
	/**
	 * O campo {0} não aceita somente espaço em branco.
	 */
	CAMPO_NAO_ACEITA_SOMENTE_ESPACOS("warn.espaco"),
	
	/**
	 * Erro ao autenticar usuário, tente novamente.
	 */
	ERRO_AO_AUTENTICAR_USUARIO("erro.login.erroAutenticacao"),
	
	/**
	 * {0}: Horario Inicial deve ser menos que o Horario Final.
	 */
	ERRO_HORARIOINICIAL_DEVE_SER_MENOR_QUE_HORARIOFINAL("erroHorarioInicialDeveSerMenosQueHorarioFinal"),
	
	/**
	 * {0}: Intervalo Inicial deve ser menos que o Intervalo Final.
	 */
	ERRO_INTERVALOINICIAL_DEVE_SER_MENOR_QUE_INTERVALOFINAL("erroIntervaloInicialDeveSerMenosQueIntervaloFinal"),
	
	/**
	 * {0}: Horário Inicial e Horário Final devem ser Preenchidos.
	 */
	ERRO_HORARIO_INICIAL_E_HORARIO_FINAL_DEVEM_SER_PREENCHIDOS("erroHorarioInicialEHorarioFinalDevemSerPreenchidos"),
	
	/**
	 * {0}: Todos os horários do dia devem ser preenchidos.
	 */
	ERRO_INTERVALO_INICIAL_E_INTERVALO_FINAL_DEVEM_SER_PREENCHIDOS("erroIntervaloInicialEIntervaloFinalDevemSerPreenchidos"),
	
	/**
	 * A Lista de Horários de ser preenchida.
	 */
	ERRO_LISTA_DE_HORARIOS_DEVE_SER_PREENCHIDA("erroListaDeHorariosDeveSerPreenchida"), 
	
	/**
	 * Senha alterada com Sucesso.
	 */
	SUCESSO_ALTERAR_SENHA("sucessoAlterarSenha"),
	
	/**
	 * Erro ao alterar senha. Por favor, tente novamente.
	 */
	ERRO_ALTERAR_SENHA("erroAlterarSenha"),
	
	/**
	 * Senha atual inválida.
	 */
	SENHA_ATUAL_INVALIDA("senhaAtualInvalida"), 
	
	/**
	 * Você digitou senhas diferentes.
	 */
	ERRO_SENHAS_DIFERENTES("erroSenhasDiferentes"),
	
	/**
	 * Erro ao buscar a coleção de perfis.
	 */
	ERRO_BUSCAR_PERFIS("erroAlterarPerfis"),
	
	/**
	 * Erro ao buscar a lista de clientes.
	 */
	ERRO_BUSCAR_CLIENTES("erroBuscarClientes"),

	/**
	 * Erro ao buscar a lista de responsaveis.
	 */
	ERRO_BUSCAR_RESPONSAVEIS("erroBuscarResponsaveis"),
	
	/**
	 * Erro ao buscar a lista de cidades.
	 */
	ERRO_BUSCAR_CIDADES("erroBuscarCidades"),
	
	/**
	 * Campo Dia aceita somente números de 1 à 31.
	 */
	DIA_DO_MES_DEVE_ESTAR_ENTRE_UM_E_TRINTA("erroDiaDoMesDeveEstarEntreUmETrinta"),
	
	/**
	 * Data Inicio do Contrato deve ser informada junto com a Data Fim do Contrato.
	 */
	ERRO_DATA_INICIO_CONTRATO_DEVE_SER_INFORMADA("erroDataInicioContratoDeveSerInformada"),
	
	/**
	 * Data inicio deve ser menor que a data final.
	 */
	ERRO_DATA_INICIO_DEVE_SER_MENOR_QUE_DATA_FINAL("erroDataInicioDeveSerMenorDataFinal"),
	
	/**
	 * Usuário e/ou senha inválidos.
	 */
	ERRO_USUARIO_E_OU_SENHA_INVALIDOS("erroUsuarioEOuSenhaInvalidos"),
	
	/**
	 * É necessário cadastrar um endereço.
	 */
	ERRO_E_NECESSARIO_CADASTRAR_UM_ENDERECO("erroEhNecessarioCadastrarUmEndereco"),
	
	/**
	 * É necessário cadastrar os documentos do funcionário.
	 */
	ERRO_E_NECESSARIO_CADASTRAR_OS_DOCUMENTOS("erroEhNecessarioCadastrarOsDocumentos"),
	
	/**
	 * É necessário cadastrar os dados funcionais do funcionário.
	 */
	ERRO_E_NECESSARIO_CADASTRAR_OS_DADOS_FUNCIONAIS("erroEhNecessarioCadastrarOsDadosFuncionais"),
	
	/**
	 * É necessário cadastrar pelo menos um contato.
	 */
	ERRO_E_NECESSARIO_CADASTRAR_UM_CONTATO("erroEhNecessarioCadastrarPeloMenosUmContato"),
	
	/**
	 * Funcionário não encontrado!
	 */
	ERRO_FUNCIONARIO_NAO_ENCONTRADO("erroFuncionarioNaoEncontrado"),
	
	/**
	 * Nenhum Funcionário Admitido encontrado!
	 */
	ERRO_FUNCIONARIOS_NAO_FORAM_ENCONTRADOS("erroFuncionariosNaoForamEncontrados"),
	
	/**
	 * Funcionário não informado!
	 */
	ERRO_FUNCIONARIO_NAO_INFORMADO("erroFuncionarioNaoInformado"),
	
	/**
	 * Relatório não informado!
	 */
	ERRO_RELATORIO_NAO_INFORMADO("erroRelatorioNaoInformado"),
	
	/**
	 * Favor informar todos os campos obrigatórios.
	 */
	ERRO_EH_NECESSARIO_INFORMAR_OS_CAMPOS_OBRIGATORIOS("erroEhNecessarioInformarOsCamposObrigatorios"),
	
	/**
	 * Este dependente já foi adicionado.
	 */
	ESTE_DEPENDENTE_JA_FOI_ADICIONADO("erroDependenteJaFoiAdicionado"),
	
	/**
	 * Selecione um dependente para alterar.
	 */
	SELECIONE_UM_DEPENDENTE_PARA_ALTERAR("erroSelecioneUmDependenteParaAlterar"),
	
	/**
	 * Este local de trabalho esta adicionado. Para modificar, clique em Alterar. 
	 */
	ESTE_LOCAL_DE_TRABALHO_JA_FOI_ADICIONADO("erroLocalTrabalhoJaFoiAdicionado"),
	
	/**
	 * Este setor já está adicionado.
	 */
	ESTE_SETOR_JA_FOI_ADICIONADO("erroSetorJaFoiAdicionado"),

	/**
	 * Esta jornada de trabalho já está adicionada.
	 */
	ESTA_JORNADA_DE_TRABALHO_JA_FOI_ADICIONADA("erroJornadaTrabalhoJaFoiAdicionada"),
	
	/**
	 * Já existe um tipo de serviço com a mesma periodicidade adicionada. 
	 */
	ESTE_TIPOSERVICO_E_PERIODICIDADE_JA_FORAM_ADICIONADAS("erroTipoServicoPeriodicidadeJaAdicionadas"),
	
	/**
	 * Selecione um local de trabalho para alterar.
	 */
	SELECIONE_UM_LOCAL_DE_TRABALHO_PARA_ALTERAR("erroSelecioneUmLocalDeTrabalhoParaAlterar"),
	
	/**
	 * Selecione um cliente para adicionar.
	 */
	SELECIONE_UM_CLIENTE_PARA_ADICIONAR("erroSelecioneUmClienteParaAdicionar"),
	
	/**
	 * Este local de trabalho esta adicionado. Para modificar, clique em Alterar. 
	 */
	ESTE_VALETRANSPORTE_JA_FOI_ADICIONADO("erroValeTransporteJaFoiAdicionado"),
	
	/**
	 * Este intervalo de jornada esta adicionado. Para modificar, clique em Alterar.
	 */
	ESTE_INTERVALOJORNADA_JA_FOI_ADICIONADO("erroIntervaloJornadaJaFoiAdicionado"),
	
	/**
	 * Selecione um local de trabalho para alterar.
	 */
	SELECIONE_UM_VALETRANSPORTE_PARA_ALTERAR("erroSelecioneUmValeTransporteParaAlterar"),
	
	/**
	 * Selecione um intervalo para alterar.
	 */
	SELECIONE_UM_INTERVALO_PARA_ALTERAR("erroSelecioneUmIntervaloParaAlterar"),
	
	/**
	 * Este local de trabalho esta adicionado. Para modificar, clique em Alterar. 
	 */
	ESTE_VALEREFEICAO_JA_FOI_ADICIONADO("erroValeRefeicaoJaFoiAdicionado"),
	
	/**
	 * Selecione um local de trabalho para alterar.
	 */
	SELECIONE_UM_VALEREFEICAO_PARA_ALTERAR("erroSelecioneUmValeRefeicaoParaAlterar"),
	
	/**
	 * Pelo Menos o Nome e a Data de Inclusão devem ser informados.
	 */
	PELO_MENOS_O_NOME_E_A_DATA_DE_INCLUSAO_DEVEM_SER_INFORMADOS("erroPeloMenosNomeEDataInclusao"),
	
	/**
	 * Informe os Dias Trabalhados. 
	 */
	INFORME_QTD_DIAS_TRABALHADOS("erroInformeDiasTrabalhados"),
	
	/**
	 * Informe todos os campos para adicionar.
	 */
	INFORME_TODOS_OS_CAMPOS_PARA_ADICIONAR("erroInfomeTodosOsCamposParaAdicionar"),
	
	/**
	 * Informe todos os campos para alterar.
	 */
	INFORME_TODOS_OS_CAMPOS_PARA_ALTERAR("erroInfomeTodosOsCamposParaAlterar"),
	
	/**
	 * Erro ao realizar o cálculo.
	 */
	ERRO_AO_REALIZAR_O_CALCULAR("erroAoRealizarCalculo"),
	
	/**
	 * Não existe pagamentos para gravação.
	 */
	ERRO_NAO_EXISTE_PAGAMENTOS_PARA_GRAVACAO("erroNaoExistePagamentosParaGravacao"),
	
	/**
	 * O cliente já existe na listagem.
	 */
	ERRO_CLIENTE_JA_EXISTE_NA_LISTAGEM("erroClienteJaExisteNaListagem"),
	
	/**
	 * Selecione pelo menos um cálculo com situação GRAVADO para gerar o Recibo.
	 */
	SELECIONE_PELO_MENOS_UM_CALCULO_GRAVADO_PARA_GERAR_RECIBO("selecionePeloMenosUmCalculoComSituacaoGravadoParaGerarORecibo"),
	
	/**
	 * Selecione pelo menos um cálculo para gerar o Mapa.
	 */
	SELECIONE_PELO_MENOS_UM_CALCULO_PARA_GERAR_MAPA("selecionePeloMenosUmCalculoParaGerarOMapa"),
	
	/**
	 * Selecione somente cálculos com a situação GRAVADO para gerar o recibo.
	 */
	SELECIONE_CALCULO_SITUACAO_GRAVADO_PARA_GERAR_RECIBO("selecioneSomenteCalculosComSituacaoGravadoParaGerarORecibo"),
	
	/**
	 * Para gerar o recibo, o cálculo deve ser gravado.
	 */
	PARA_GERAR_RECIBO_CALCULO_DEVE_SER_GRAVADO("paraGerarOReciboOCalculoDeveSerGravado"),
	
	/**
	 * Realize um cálculo para gerar o Mapa.
	 */
	REALIZE_CALCULO_PARA_GERAR_MAPA("erroRealizeUmCalculoParaGerarOMapa"),
	
	/**
	 * Esta jornada esta associada a um funcionário. Não é possivel realizar a exclusão.
	 */
	ERRO_JORNADA_ASSOCIADA("erroJornadaAssociada"),
	
	/**
	 * Para excluir, selecione um registro com a situação CALCULADO. 
	 */
	REGISTRO_COM_SITUACAO_CALCULADO_PARA_EXCLUIR("erroParaExcluirSelecioneUmRegistroComASituacaoCalculado"),
	
	/**
	 * 
	 */
	ERRO_NAO_FOI_POSSIVEL_GERAR_O_RELATORIO("erroNaoFoiPossivelGerarORelatorio"),
	
	/**
	 * 
	 */
	ERRO_NENHUM_CLIENTE_ADICIONADO_NA_LISTAGEM("erroNenhumClienteAdicionadoNaListagem"),
	
	/**
	 * 
	 */
	ERRO_CAMPO_OBRIGATORIO("erro.campo.obrigatorio"),
	
	/**
	 * Cliente n�o encontrado!
	 */
	ERRO_CLIENTE_NAO_ENCONTRADO("erroClienteNaoEncontrado"),
	
	/**
	 * Este reajuste já foi adicionado.
	 */
	ESTE_REAJUSTE_JA_FOI_ADICIONADO("erroReajusteJaAdicionado"),
	
	/**
	 * Não há itens de {TIPO DO ITEM} a selecionar.
	 */
	ERRO_NAO_HA_ITENS_A_SELECIONAR("erroNaoHaItensASelecionar"),
	
	/**
	 * Não há itens de {TIPO DO ITEM} a remover.
	 */
	ERRO_NAO_HA_ITENS_A_REMOVER("erroNaoHaItensARemover");
	
	/**
	 * Chave.
	 */
	private String key;

	/**
	 * Construtor.
	 * @param key
	 */
	private MensagensFacesEnum(final String key) {
		this.key = key;
	}

	/**
	 * @return A Chave da mensagem.
	 */
	public String getKey() {
		return key;
	}
}

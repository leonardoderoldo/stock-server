package br.com.stock.server.domain.wallet

enum class PaymentStatus {

    AUTHORIZED,
    NOT_AUTHORIZED,
    NOT_AUTHORIZED_INVALID_AMOUNT,
    PASSWORD_ATTEMPTS_EXCEEDED,
    LOST_CARD,
    STOLEN_CARD,
    OVERDUE_CARD,
    EXCESS_VALUE,
    BLOCKED_CARD,
    EXPIRED_CARD,
    CANCELLED_CARD,
    ERROR;

}


//
//MENSAGEM	TIPO DE CÓDIGO	                            ELO	VISA	MASTERCARD/HIPER	AMEX	AMEX - DE/PARA CIELO	MENSAGEM POS/ECOMMERCE
//GENÉRICA	REVERSÍVEL	                                05	05	05	100	FA	CONTATE A CENTRAL DO SEU CARTÃO
//SALDO/LIMITE INSUFICIENTE	REVERSÍVEL	                51	51	51	116	A5	NÃO AUTORIZADA
//SALDO/LIMITE INSUFICIENTE	REVERSÍVEL	                51	51	51	121	A5	NÃO AUTORIZADA
//SENHA INVÁLIDA	REVERSÍVEL	                        55	55 ou 86	55	117	A6	SENHA INVÁLIDA
//TRANSAÇÃO NÃO PERMITIDA PARA O CARTÃO	IRREVERSÍVEL	57	57	57	200	FD	TRANSAÇÃO NÃO PERMITIDA PARA O CARTÃO- NÃO TENTE NOVAMENTE
//NÚMERO CARTÃO NÃO PERTENCE AO EMISSOR | NÚMERO CARTÃO INVÁLIDO	IRREVERSÍVEL	14 ou 56	06	14 ou 01	122	08	VERIFIQUE OS DADOS DO CARTÃO
//VIOLAÇÃO DE SEGURANÇA	IRREVERSÍVEL	                63	06	14	122	08	VERIFIQUE OS DADOS DO CARTÃO
//SUSPEITA DE FRAUDE	REVERSÍVEL	                    59	59	63	100	FA	CONTATE A CENTRAL DO SEU CARTÃO
//COMERCIANTE INVÁLIDO	IRREVERSÍVEL	                58	SEM CÓDIGO CORRESPONDENTE	03	109	DA	TRANSAÇÃO NÃO PERMITIDA - NÃO TENTE NOVAMENTE
//COMERCIANTE INVÁLIDO	REVERSÍVEL	                    SEM CÓDIGO CORRESPONDENTE	03	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	TRANSAÇÃO NÃO PERMITIDA
//REFAZER A TRANSAÇÃO (EMISSOR SOLICITA RETENTATIVA)	REVERSÍVEL	4	SEM CÓDIGO CORRESPONDENTE	SE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	REFAZER A TRANSAÇÃO
//CONSULTAR CREDENCIADOR	REVERSÍVEL	                6	SEM CÓDIGO CORRESPONDENTE	SE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	LOJISTA, CONTATE O ADQUIRENTE
//PROBLEMA NO ADQUIRENTE	IRREVERSÍVEL	19	19	30	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	ERRO NO CARTÃO – NÃO TENTE NOVAMENTE
//ERRO NO CARTÃO	IRREVERSÍVEL	12	06	SEM CÓDIGO CORRESPONDENTE	115	A2	VERIFIQUE OS DADOS DO CARTÃO
//ERRO DE FORMATO (MENSAGERIA)	IRREVERSÍVEL	30	12	30	181	A3	ERRO NO CARTÃO - NÃO TENTE NOVAMENTE
//VALOR DA TRANSAÇÃO INVÁLIDA	IRREVERSÍVEL	13	13	13	110	JB	VALOR DA TRANSAÇÃO NÃO PERMITIDO - NÃO TENTE NOVAMENTE
//VALOR DA PARCELA INVÁLIDA	IRREVERSÍVEL	23	SEM CÓDIGO CORRESPONDENTE	12	115	A2	PARCELAMENTO INVÁLIDO - NÃO TENTE NOVAMENTE
//EXCEDIDAS TENTATIVAS DE SENHA | COMPRAS	REVERSÍVEL	38	75	75	106	A4	EXCEDIDAS TENTATIVAS DE SENHA.CONTATE A CENTRAL DO SEU CARTÃO
//CARTÃO PERDIDO	IRREVERSÍVEL	41	41	41	200	FD	TRANSAÇÃO NÃO PERMITIDA - NÃO TENTE NOVAMENTE
//CARTÃO ROUBADO	IRREVERSÍVEL	43	43	43	200	FD	TRANSAÇÃO NÃO PERMITIDA - NÃO TENTE NOVAMENTE
//CARTÃO VENCIDO / DT EXPIRAÇÃO INVÁLIDA	IRREVERSÍVEL	54	06	54	101	BV	VERIFIQUE OS DADOS DO CARTÃO
//TRANSAÇÃO NÃO PERMITIDA | CAPACIDADE DO TERMINAL	IRREVERSÍVEL	57	58	58	116	A5	TRANSAÇÃO NÃO PERMITIDA - NÃO TENTE NOVAMENTE
//VALOR EXCESSO | SAQUE	REVERSÍVEL	61	61 ou N4	61	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	VALOR EXCEDIDO. CONTATE A CENTRAL DO SEU CARTÃO
//CARTÃO DOMÉSTICO - TRANSAÇÃO INTERNACIONAL	IRREVERSÍVEL	62	SEM CÓDIGO CORRESPONDENTE	62	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	CARTÃO NÃO PERMITE TRANSAÇÃO INTERNACIONAL
//CARTÃO DOMÉSTICO - TRANSAÇÃO INTERNACIONAL	REVERSÍVEL	SEM CÓDIGO CORRESPONDENTE	62	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	CARTÃO NÃO PERMITE TRANSAÇÃO INTERNACIONAL
//VALOR MÍNIMO DA TRANSAÇÃO INVÁLIDO	IRREVERSÍVEL	64	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	VALOR DA TRANSAÇÃO NÃO PERMITIDO - NÃO TENTE NOVAMENTE
//QUANT. DE SAQUES EXCEDIDO	REVERSÍVEL	65	65	65	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	QUANTIDADE DE SAQUES EXCEDIDA. CONTATE A CENTRAL DO SEU CARTÃO
//SENHA VENCIDA / ERRO DE CRIPTOGRAFIA DE SENHA	IRREVERSÍVEL	74	74 ou 81	88	180	A7	SENHA INVÁLIDA - NÃO TENTE NOVAMENTE
//EXCEDIDAS TENTATIVAS DE SENHA | SAQUE	REVERSÍVEL	75	75	75	106	A4	EXCEDIDAS TENTATIVAS DE SENHA.CONTATE A CENTRAL DO SEU CARTÃO
//CONTA DESTINO INVÁLIDA OU INEXISTENTE	IRREVERSÍVEL	76	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	CONTA DESTINO INVÁLIDA - NÃO TENTE NOVAMENTE
//CONTA ORIGEM INVÁLIDA OU INEXISTENTE	IRREVERSÍVEL	77	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	CONTA ORIGEM INVÁLIDA - NÃO TENTE NOVAMENTE
//CARTÃO NOVO SEM DESBLOQUEIO	REVERSÍVEL	78	78	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	DESBLOQUEIE O CARTÃO
//CARTÃO INVÁLIDO (criptograma)	IRREVERSÍVEL	82	82	88	180	A7	ERRO NO CARTÃO - NÃO TENTE NOVAMENTE
//EMISSOR FORA DO AR	REVERSÍVEL	91	91	91	912	A1	FALHA DE COMUNICAÇÃO - TENTE MAIS TARDE
//FALHA DO SISTEMA	REVERSÍVEL	96	96	96	911	AE	FALHA DE COMUNICAÇÃO - TENTE MAIS TARDE
//DIFERENÇA - PRÉ AUTORIZAÇÃO	IRREVERSÍVEL	99	N8	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	VALOR DIFERENTE DA PRÉ AUTORIZAÇÃO - NÃO TENTE NOVAMENTE
//FUNÇÃO INCORRETA (DÉBITO)	IRREVERSÍVEL	AB	52 ou 53	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	UTILIZE FUNÇÃO CRÉDITO
//FUNÇÃO INCORRETA (CRÉDITO)	IRREVERSÍVEL	AC	39	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	UTILIZE FUNÇÃO DÉBITO
//TROCA DE SENHA / DESBLOQUEIO	IRREVERSÍVEL	P5	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SENHA INVÁLIDA - NÃO TENTE NOVAMENTE
//NOVA SENHA NÃO ACEITA	REVERSÍVEL	P6	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SENHA INVÁLIDA UTILIZE A NOVA SENHA
//RECOLHER CARTÃO (NÃO HÁ FRAUDE)	IRREVERSÍVEL	SEM CÓDIGO CORRESPONDENTE	04	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	CONTATE A CENTRAL DO SEU CARTÃO - NÃO TENTE NOVAMENTE
//ERRO POR MUDANÇA DE CHAVE DINÂMICA	IRREVERSÍVEL	SEM CÓDIGO CORRESPONDENTE	06	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	ERRO NO CARTÃO - NÃO TENTE NOVAMENTE
//FRAUDE CONFIRMADA	IRREVERSÍVEL	57	07	04	200	FD	TRANSAÇÃO NÃO PERMITIDA PARA O CARTÃO - NÃO TENTE NOVAMENTE
//EMISSOR Ñ LOCALIZADO - BIN INCORRETO	IRREVERSÍVEL	SEM CÓDIGO CORRESPONDENTE	15	15	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	DADOS DO CARTÃO INVÁLIDO - NÃO TENTE NOVAMENTE
//(negativa do adquirente) NÃO CUMPRIMENTO PELAS LEIS DE ANTE LAVAGEM DE DINHEIRO	IRREVERSÍVEL	SEM CÓDIGO CORRESPONDENTE	64	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	CONTATE A CENTRAL DO SEU CARTÃO - NÃO TENTE NOVAMENTE
//REVERSÃO INVÁLIDA	IRREVERSÍVEL	SEM CÓDIGO CORRESPONDENTE	76	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	CONTATE A CENTRAL DO SEU CARTÃO - NÃO TENTE NOVAMENTE
//NÃO LOCALIZADO PELO ROTEADOR	IRREVERSÍVEL	SEM CÓDIGO CORRESPONDENTE	92	92	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	CONTATE A CENTRAL DO SEU CARTÃO - NÃO TENTE NOVAMENTE
//TRANSAÇÃO NEGADA POR INFRAÇÃO DE LEI	IRREVERSÍVEL	57	SEM CÓDIGO CORRESPONDENTE	57	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	TRANSAÇÃO NÃO PERMITIDA PARA O CARTÃO - NÃO TENTE NOVAMENTE
//TRANSAÇÃO NEGADA POR INFRAÇÃO DE LEI	REVERSÍVEL	SEM CÓDIGO CORRESPONDENTE	93	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	TRANSAÇÃO NÃO PERMITIDA PARA O CARTÃO
//VALOR DO TRACING DATA DUPLICADO	IRREVERSÍVEL	SEM CÓDIGO CORRESPONDENTE	94	94	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	CONTATE A CENTRAL DO SEU CARTÃO - NÃO TENTENOVAMENTE
//SURCHARGE NÃO SUPORTADO	REVERSÍVEL	SEM CÓDIGO CORRESPONDENTE	B1	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	CONTATE A CENTRAL DO SEU CARTÃO
//SURCHARGE NÃO SUPORTADO PELA REDE DE DÉBITO	REVERSÍVEL	SEM CÓDIGO CORRESPONDENTE	B2	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	CONTATE A CENTRAL DO SEU CARTÃO
//FORÇAR STIP	REVERSÍVEL	SEM CÓDIGO CORRESPONDENTE	N0	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	CONTATE A CENTRAL DO SEU CARTÃO
//SAQUE NÃO DISPONÍVEL	IRREVERSÍVEL	SEM CÓDIGO CORRESPONDENTE	N3	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SAQUE NÃO DISPONÍVEL - NÃO TENTE NOVAMENTE
//SUSPENSÃO DE PAGAMENTO RECORRENTE PARA UM SERVIÇO	IRREVERSÍVEL	SEM CÓDIGO CORRESPONDENTE	R0	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SUSPENSÃO DE PAGAMENTO RECORRENTE PARA SERVIÇO - NÃO TENTE NOVAMENTE
//SUSPENSÃO DE PAGAMENTO RECORRENTE PARA TODOS SERVIÇO	IRREVERSÍVEL	SEM CÓDIGO CORRESPONDENTE	R1	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SUSPENSÃO DE PAGAMENTO RECORRENTE PARA SERVIÇO - NÃO TENTE NOVAMENTE
//TRANSAÇÃO NÃO QUALIFICADA PARA VISA PIN	IRREVERSÍVEL	SEM CÓDIGO CORRESPONDENTE	R2	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	TRANSAÇÃO NÃO PERMITIDA PARA O CARTÃO - NÃO TENTE NOVAMENTE
//SUSPENSÃO DE TODAS AS ORDENS DE AUTORIZAÇÃO	IRREVERSÍVEL	SEM CÓDIGO CORRESPONDENTE	R3	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	SUSPENSÃO DE PAGAMENTO RECORRENTE PARA SERVIÇO - NÃO TENTE NOVAMENTE
//NÃO É POSSÍVEL LOCALIZAR O REGISTRO NO ARQUIVO	IRREVERSÍVEL	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	25	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	CONTATE A CENTRAL DO SEU CARTÃO - NÃO TENTE NOVAMENTE
//ARQUIVO NÃO DISPONÍVEL PARA ATUALIZAÇÃO	IRREVERSÍVEL	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	28	SEM CÓDIGO CORRESPONDENTE	SEM CÓDIGO CORRESPONDENTE	CONTATE A CENTRAL DO SEU CARTÃO - NÃO TENTE NOVAMENTE
//Outros códigos de retorno



//CÓDIGO RESPOSTA	DEFINIÇÃO	SIGNIFICADO	AÇÃO	PERMITE RETENTATIVA
//00	Transação autorizada com sucesso.	Transação autorizada com sucesso.	Transação autorizada com sucesso.	Não
//02	Transação não autorizada. Transação referida.	Transação não autorizada. Referida (suspeita de fraude) pelo banco emissor.	Transação não autorizada. Entre em contato com seu banco emissor.	Não
//09	Transação cancelada parcialmente com sucesso.	Transação cancelada parcialmente com sucesso	Transação cancelada parcialmente com sucesso	Não
//11	Transação autorizada com sucesso para cartão emitido no exterior	Transação autorizada com sucesso.	Transação autorizada com sucesso.	Não
//21	Cancelamento não efetuado. Transação não localizada.	Não foi possível processar o cancelamento. Se o erro persistir, entre em contato com a Cielo.	Não foi possível processar o cancelamento. Tente novamente mais tarde. Persistindo o erro, entrar em contato com a loja virtual.	Não
//22	Parcelamento inválido. Número de parcelas inválidas.	Não foi possível processar a transação. Número de parcelas inválidas. Se o erro persistir, entre em contato com a Cielo.	Não foi possível processar a transação. Valor inválido. Refazer a transação confirmando os dados informados. Persistindo o erro, entrar em contato com a loja virtual.	Não
//24	Quantidade de parcelas inválido.	Não foi possível processar a transação. Quantidade de parcelas inválido. Se o erro persistir, entre em contato com a Cielo.	Não foi possível processar a transação. Quantidade de parcelas inválido. Refazer a transação confirmando os dados informados. Persistindo o erro, entrar em contato com a loja virtual.	Não
//60	Transação não autorizada.	Transação não autorizada. Tente novamente. Se o erro persistir o portador deve entrar em contato com o banco emissor.	Não foi possível processar a transação. Tente novamente mais tarde. Se o erro persistir, entre em contato com seu banco emissor.	Apenas 4 vezes em 16 dias.
//67	Transação não autorizada. Cartão bloqueado para compras hoje.	Transação não autorizada. Cartão bloqueado para compras hoje. Bloqueio pode ter ocorrido por excesso de tentativas inválidas. O cartão será desbloqueado automaticamente à meia noite.	Transação não autorizada. Cartão bloqueado temporariamente. Entre em contato com seu banco emissor.	A partir do dia seguinte, apenas 4 vezes em 16 dias.
//70	Transação não autorizada. Limite excedido/sem saldo.	Transação não autorizada. Limite excedido/sem saldo.	Transação não autorizada. Entre em contato com seu banco emissor.	A partir do dia seguinte, apenas 4 vezes em 16 dias.
//72	Cancelamento não efetuado. Saldo disponível para cancelamento insuficiente.	Cancelamento não efetuado. Saldo disponível para cancelamento insuficiente. Se o erro persistir, entre em contato com a Cielo.	Cancelamento não efetuado. Tente novamente mais tarde. Se o erro persistir, entre em contato com a loja virtual.	Não
//80	Transação não autorizada. Divergencia na data de transação/pagamento.	Transação não autorizada. Data da transação ou data do primeiro pagamento inválida.	Transação não autorizada. Refazer a transação confirmando os dados.	Não
//83	Transação não autorizada. Erro no controle de senhas	Transação não autorizada. Erro no controle de senhas	Transação não autorizada. Refazer a transação confirmando os dados. Se o erro persistir, entre em contato com seu banco emissor.	Não
//85	Transação não permitida. Falha da operação.	Transação não permitida. Houve um erro no processamento.Solicite ao portador que digite novamente os dados do cartão, se o erro persistir pode haver um problema no terminal do lojista, nesse caso o lojista deve entrar em contato com a Cielo.	Transação não permitida. Informe os dados do cartão novamente. Se o erro persistir, entre em contato com a loja virtual.	Não
//89	Erro na transação.	Transação não autorizada. Erro na transação. O portador deve tentar novamente e se o erro persistir, entrar em contato com o banco emissor.	Transação não autorizada. Erro na transação. Tente novamente e se o erro persistir, entre em contato com seu banco emissor.	Apenas 4 vezes em 16 dias.
//90	Transação não permitida. Falha da operação.	Transação não permitida. Houve um erro no processamento.Solicite ao portador que digite novamente os dados do cartão, se o erro persistir pode haver um problema no terminal do lojista, nesse caso o lojista deve entrar em contato com a Cielo.	Transação não permitida. Informe os dados do cartão novamente. Se o erro persistir, entre em contato com a loja virtual.	Não
//97	Valor não permitido para essa transação.	Transação não autorizada. Valor não permitido para essa transação.	Transação não autorizada. Valor não permitido para essa transação.	Não
//98	Sistema/comunicação indisponível.	Transação não autorizada. Sistema do emissor sem comunicação. Se for geral, verificar SITEF, GATEWAY e/ou Conectividade.	Sua Transação não pode ser processada, Tente novamente mais tarde. Se o erro persistir, entre em contato com a loja virtual.	Apenas 4 vezes em 16 dias.
//475	Timeout de Cancelamento	A aplicação não respondeu dentro do tempo esperado.	Realizar uma nova tentativa após alguns segundos. Persistindo, entrar em contato com o Suporte.	Não
//999	Sistema/comunicação indisponível.	Transação não autorizada. Sistema do emissor sem comunicação. Tente mais tarde. Pode ser erro no SITEF, favor verificar !	Sua Transação não pode ser processada, Tente novamente mais tarde. Se o erro persistir, entre em contato com a loja virtual.	A partir do dia seguinte, apenas 4 vezes em 16 dias.
//AA	Tempo Excedido	Tempo excedido na comunicação com o banco emissor. Oriente o portador a tentar novamente, se o erro persistir será necessário que o portador contate seu banco emissor.	Tempo excedido na sua comunicação com o banco emissor, tente novamente mais tarde. Se o erro persistir, entre em contato com seu banco.	Apenas 4 vezes em 16 dias.
//AF	Transação não permitida. Falha da operação.	Transação não permitida. Houve um erro no processamento.Solicite ao portador que digite novamente os dados do cartão, se o erro persistir pode haver um problema no terminal do lojista, nesse caso o lojista deve entrar em contato com a Cielo.	Transação não permitida. Informe os dados do cartão novamente. Se o erro persistir, entre em contato com a loja virtual.	Não
//AG	Transação não permitida. Falha da operação.	Transação não permitida. Houve um erro no processamento.Solicite ao portador que digite novamente os dados do cartão, se o erro persistir pode haver um problema no terminal do lojista, nesse caso o lojista deve entrar em contato com a Cielo.	Transação não permitida. Informe os dados do cartão novamente. Se o erro persistir, entre em contato com a loja virtual.	Não
//AH	Transação não permitida. Cartão de crédito sendo usado com débito. Use a função crédito.	Transação não permitida. Cartão de crédito sendo usado com débito. Solicite ao portador que selecione a opção de pagamento Cartão de Crédito.	Transação não autorizada. Tente novamente selecionando a opção de pagamento cartão de crédito.	Não
//AI	Transação não autorizada. Autenticação não foi realizada.	Transação não autorizada. Autenticação não foi realizada. O portador não concluiu a autenticação. Solicite ao portador que reveja os dados e tente novamente. Se o erro persistir, entre em contato com a Cielo informando o BIN (6 primeiros dígitos do cartão)	Transação não autorizada. Autenticação não foi realizada com sucesso. Tente novamente e informe corretamente os dados solicitado. Se o erro persistir, entre em contato com o lojista.	Não
//AJ	Transação não permitida. Transação de crédito ou débito em uma operação que permite apenas Private Label. Tente novamente selecionando a opção Private Label.	Transação não permitida. Transação de crédito ou débito em uma operação que permite apenas Private Label. Solicite ao portador que tente novamente selecionando a opção Private Label. Caso não disponibilize a opção Private Label verifique na Cielo se o seu estabelecimento permite essa operação.	Transação não permitida. Transação de crédito ou débito em uma operação que permite apenas Private Label. Tente novamente e selecione a opção Private Label. Em caso de um novo erro entre em contato com a loja virtual.	Não
//AV	Transação não autorizada. Dados Inválidos	Falha na validação dos dados da transação. Oriente o portador a rever os dados e tentar novamente.	Falha na validação dos dados. Reveja os dados informados e tente novamente.	Apenas 4 vezes em 16 dias.
//BD	Transação não permitida. Falha da operação.	Transação não permitida. Houve um erro no processamento.Solicite ao portador que digite novamente os dados do cartão, se o erro persistir pode haver um problema no terminal do lojista, nesse caso o lojista deve entrar em contato com a Cielo.	Transação não permitida. Informe os dados do cartão novamente. Se o erro persistir, entre em contato com a loja virtual.	Não
//BL	Transação não autorizada. Limite diário excedido.	Transação não autorizada. Limite diário excedido. Solicite ao portador que entre em contato com seu banco emissor.	Transação não autorizada. Limite diário excedido. Entre em contato com seu banco emissor.	A partir do dia seguinte, apenas 4 vezes em 16 dias.
//BM	Transação não autorizada. Cartão Inválido	Transação não autorizada. Cartão inválido. Pode ser bloqueio do cartão no banco emissor ou dados incorretos. Tente usar o Algoritmo de Lhum (Mod 10) para evitar transações não autorizadas por esse motivo.	Transação não autorizada. Cartão inválido. Refaça a transação confirmando os dados informados.	Não
//BN	Transação não autorizada. Cartão ou conta bloqueado.	Transação não autorizada. O cartão ou a conta do portador está bloqueada. Solicite ao portador que entre em contato com seu banco emissor.	Transação não autorizada. O cartão ou a conta do portador está bloqueada. Entre em contato com seu banco emissor.	Não
//BO	Transação não permitida. Falha da operação.	Transação não permitida. Houve um erro no processamento. Solicite ao portador que digite novamente os dados do cartão, se o erro persistir, entre em contato com o banco emissor.	Transação não permitida. Houve um erro no processamento. Digite novamente os dados do cartão, se o erro persistir, entre em contato com o banco emissor.	Apenas 4 vezes em 16 dias.
//BP	Transação não autorizada. Conta corrente inexistente.	Transação não autorizada. Não possível processar a transação por um erro relacionado ao cartão ou conta do portador. Solicite ao portador que entre em contato com o banco emissor.	Transação não autorizada. Não possível processar a transação por um erro relacionado ao cartão ou conta do portador. Entre em contato com o banco emissor.	Não
//BP176	Transação não permitida.	Parceiro deve checar se o processo de integração foi concluído com sucesso.	Parceiro deve checar se o processo de integração foi concluído com sucesso.	—
//C1	Transação não permitida. Cartão não pode processar transações de débito.	Troque a modalidade de pagamento ou o cartão utilizado.	Troque a modalidade de pagamento ou o cartão utilizado.	Não
//C2	Transação não permitida.	Dados incorretos. Favor rever os dados preenchidos na tela de pagamento.	Dados incorretos. Favor rever os dados preenchidos na tela de pagamento.	Não
//C3	Transação não permitida.	Período inválido para este tipo de transação.	Período inválido para este tipo de transação.	Não
//CF	Transação não autorizada.C79:J79 Falha na validação dos dados.	Transação não autorizada. Falha na validação dos dados. Solicite ao portador que entre em contato com o banco emissor.	Transação não autorizada. Falha na validação dos dados. Entre em contato com o banco emissor.	Não
//CG	Transação não autorizada. Falha na validação dos dados.	Transação não autorizada. Falha na validação dos dados. Solicite ao portador que entre em contato com o banco emissor.	Transação não autorizada. Falha na validação dos dados. Entre em contato com o banco emissor.	Não
//DF	Transação não permitida. Falha no cartão ou cartão inválido.	Transação não permitida. Falha no cartão ou cartão inválido. Solicite ao portador que digite novamente os dados do cartão, se o erro persistir, entre em contato com o banco	Transação não permitida. Falha no cartão ou cartão inválido. Digite novamente os dados do cartão, se o erro persistir, entre em contato com o banco	Apenas 4 vezes em 16 dias.
//DM	Transação não autorizada. Limite excedido/sem saldo.	Transação não autorizada. Limite excedido/sem saldo.	Transação não autorizada. Entre em contato com seu banco emissor.	A partir do dia seguinte, apenas 4 vezes em 16 dias.
//DQ	Transação não autorizada. Falha na validação dos dados.	Transação não autorizada. Falha na validação dos dados. Solicite ao portador que entre em contato com o banco emissor.	Transação não autorizada. Falha na validação dos dados. Entre em contato com o banco emissor.	Não
//DS	Transação não permitida para o cartão	Transação não autorizada. Transação não permitida para o cartão.	Transação não autorizada. Entre em contato com seu banco emissor.	Apenas 4 vezes em 16 dias.
//EB	Transação não autorizada. Limite diário excedido.	Transação não autorizada. Limite diário excedido. Solicite ao portador que entre em contato com seu banco emissor.	Transação não autorizada. Limite diário excedido. Entre em contato com seu banco emissor.	A partir do dia seguinte, apenas 4 vezes em 16 dias.
//EE	Transação não permitida. Valor da parcela inferior ao mínimo permitido.	Transação não permitida. Valor da parcela inferior ao mínimo permitido. Não é permitido parcelas inferiores a R$ 5,00. Necessário rever calculo para parcelas.	Transação não permitida. O valor da parcela está abaixo do mínimo permitido. Entre em contato com a loja virtual.	Não
//EK	Transação não permitida para o cartão	Transação não autorizada. Transação não permitida para o cartão.	Transação não autorizada. Entre em contato com seu banco emissor.	Apenas 4 vezes em 16 dias.
//FC	Transação não autorizada. Ligue Emissor	Transação não autorizada. Oriente o portador a entrar em contato com o banco emissor.	Transação não autorizada. Entre em contato com seu banco emissor.	Não
//FE	Transação não autorizada. Divergencia na data de transação/pagamento.	Transação não autorizada. Data da transação ou data do primeiro pagamento inválida.	Transação não autorizada. Refazer a transação confirmando os dados.	Não
//FF	Cancelamento OK	Transação de cancelamento autorizada com sucesso. ATENÇÂO: Esse retorno é para casos de cancelamentos e não para casos de autorizações.	Transação de cancelamento autorizada com sucesso	Não
//FG	Transação não autorizada. Ligue AmEx 08007285090.	Transação não autorizada. Oriente o portador a entrar em contato com a Central de Atendimento AmEx.	Transação não autorizada. Entre em contato com a Central de Atendimento AmEx no telefone 08007285090	Não
//GA	Aguarde Contato	Transação não autorizada. Referida pelo Lynx Online de forma preventiva.	Transação não autorizada. Entre em contato com o lojista.	Não
//GD	Transação não permitida.	Transação não permitida. Entre em contato com a Cielo.	Transação não permitida. Entre em contato com a Cielo.	—
//HJ	Transação não permitida. Código da operação inválido.	Transação não permitida. Código da operação Coban inválido.	Transação não permitida. Código da operação Coban inválido. Entre em contato com o lojista.	Não
//IA	Transação não permitida. Indicador da operação inválido.	Transação não permitida. Indicador da operação Coban inválido.	Transação não permitida. Indicador da operação Coban inválido. Entre em contato com o lojista.	Não
//KA	Transação não permitida. Falha na validação dos dados.	Transação não permitida. Houve uma falha na validação dos dados. Solicite ao portador que reveja os dados e tente novamente. Se o erro persistir verifique a comunicação entre loja virtual e Cielo.	Transação não permitida. Houve uma falha na validação dos dados. reveja os dados informados e tente novamente. Se o erro persistir entre em contato com a Loja Virtual.	Não
//KB	Transação não permitida. Selecionado a opção incorrente.	Transação não permitida. Selecionado a opção incorreta. Solicite ao portador que reveja os dados e tente novamente. Se o erro persistir deve ser verificado a comunicação entre loja virtual e Cielo.	Transação não permitida. Selecionado a opção incorreta. Tente novamente. Se o erro persistir entre em contato com a Loja Virtual.	Não
//KE	Transação não autorizada. Falha na validação dos dados.	Transação não autorizada. Falha na validação dos dados. Opção selecionada não está habilitada. Verifique as opções disponíveis para o portador.	Transação não autorizada. Falha na validação dos dados. Opção selecionada não está habilitada. Entre em contato com a loja virtual.	Não
//N7	Transação não autorizada. Código de segurança inválido.	Transação não autorizada. Código de segurança inválido. Oriente o portador corrigir os dados e tentar novamente.	Transação não autorizada. Reveja os dados e informe novamente.	Não
//U3	Transação não permitida. Falha na validação dos dados.	Transação não permitida. Houve uma falha na validação dos dados. Solicite ao portador que reveja os dados e tente novamente. Se o erro persistir verifique a comunicação entre loja virtual e Cielo.	Transação não permitida. Houve uma falha na validação dos dados. reveja os dados informados e tente novamente. Se o erro persistir entre em contato com a Loja Virtual.	Não
//BR	Transação não autorizada. Conta encerrada	A conta do portador está encerrada. Solicite ao portador que entre em contato com seu banco emissor.	A conta do portador está encerrada. Solicite ao portador que entre em contato com seu banco emissor.	Não
//46	Transação não autorizada. Conta encerrada	A conta do portador está encerrada. Solicite ao portador que entre em contato com seu banco emissor.	A conta do portador está encerrada. Solicite ao portador que entre em contato com seu banco emissor.	Não
//6P	Transação não autorizada. Dados Inválidos	Falha na validação dos dados da transação. Oriente o portador a rever os dados e tentar novamente.	Falha na validação dos dados. Reveja os dados informados e tente novamente.	Apenas 4 vezes em 16 dias.

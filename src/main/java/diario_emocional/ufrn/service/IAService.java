package diario_emocional.ufrn.service;

import diario_emocional.ufrn.dto.ContextoSemanalDTO;
import diario_emocional.ufrn.dto.IA.IAResponseCriarDTO;
import diario_emocional.ufrn.dto.IA.GeminiResponseDTO;
import diario_emocional.ufrn.dto.IA.IAResponseDTO;
import diario_emocional.ufrn.entity.*;
import diario_emocional.ufrn.exception.ResourceNotFoundException;
import diario_emocional.ufrn.repository.AtividadeObrigatoriaRepository;
import diario_emocional.ufrn.repository.AvalicaoSentimentoRepository;
import diario_emocional.ufrn.repository.RelatoDiaRepository;
import diario_emocional.ufrn.repository.RelatoHigieneSonoRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class IAService {


    private final HttpClient httpClient;
    private final RelatoDiaRepository relatoDiaRepository;
    private final RelatoHigieneSonoRepository relatoHigieneSonoRepository;
    private final AtividadeObrigatoriaRepository atividadeObrigatoriaRepository;
    private final AvalicaoSentimentoRepository avalicaoSentimentoRepository;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api-key}")
    private String apiKey;
    private final ChatClient chatClient;

    public IAService(RelatoDiaRepository relatoDiaRepository,  RelatoHigieneSonoRepository relatoHigieneSonoRepository, AtividadeObrigatoriaRepository atividadeObrigatoriaRepository, AvalicaoSentimentoRepository avalicaoSentimentoRepository, ObjectMapper objectMapper, ChatModel chatModel, ChatClient.Builder chatClientBuilder){
        this.chatClient = chatClientBuilder.build();
        this.httpClient = HttpClient.newHttpClient();
        this.relatoDiaRepository = relatoDiaRepository;
        this.relatoHigieneSonoRepository = relatoHigieneSonoRepository;
        this.atividadeObrigatoriaRepository = atividadeObrigatoriaRepository;
        this.avalicaoSentimentoRepository = avalicaoSentimentoRepository;
        this.objectMapper = objectMapper;
    }

    public String gerarPromptSugestoesRelatoDia(Long usuarioId, LocalDate data){
        String textoContexto = """
        Você é um assistente de reflexão pessoal de um diário emocional.

        Analise os relatos diários do usuário dos últimos 7 dias e,
        com base nos acontecimentos, interesses, sentimentos e temas
        recorrentes identificados nesses relatos, sugira um único tópico
        para o usuário escrever no diário hoje.

        REGRAS:
        - Gere apenas uma sugestão.
        - A sugestão deve ser personalizada com base nos relatos.
        - Não invente acontecimentos que não estejam presentes nos relatos.
        - Evite repetir exatamente assuntos já abordados nos relatos.
        - Procure identificar algo interessante que possa ser explorado
          ou aprofundado pelo usuário.
        - A sugestão deve incentivar reflexão pessoal.
        - Não faça diagnósticos psicológicos.
        - Não dê conselhos médicos ou terapêuticos.
        - Não explique sua análise.
        - Retorne somente um objeto JSON válido.

        FORMATO DA RESPOSTA:
        {
          "sugestao": "Tópico: pergunta ou provocação relacionada ao tópico."
        }

        EXEMPLO:
        {
          "sugestao": "Trilha Sonora: Você ouviu muito Spotify hoje. Qual música não sai da sua cabeça e por quê?"
        }
        """;


        List<RelatoDia> relatos7dias = this.relatoDiaRepository.findAllByUsuarioIdAndDataRegistroBetween(usuarioId, data.minusDays(7), data);

        if(relatos7dias.isEmpty()){
            throw new ResourceNotFoundException("Não existe nenhum relato do dia nos últimos 7 dias.");
        }

        String relatosJson = objectMapper.writeValueAsString(relatos7dias);


        String input = """
            %s

            RELATOS DOS ÚLTIMOS 7 DIAS:
            %s
            """.formatted(
                textoContexto,
                relatosJson
        );

        return objectMapper.writeValueAsString(input);
    }


    public GeminiResponseDTO retornarSugestoesRelatoDia(String prompt) throws IOException, InterruptedException {


        String json = """
        {
          "contents": [
            {
              "parts": [
                {
                  "text": %s
                }
              ]
            }
          ],
          "generationConfig": {
            "responseMimeType": "application/json",
            "responseSchema": {
              "type": "OBJECT",
              "properties": {
                "sugestao": {
                  "type": "STRING"
                }
              },
              "required": [
                "sugestao"
              ]
            }
          }
        }
        """.formatted(prompt);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash-lite:generateContent?key="
                                + apiKey
                ))
                .header(
                        "x-goog-api-key",apiKey
                )
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response;

        try{
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (HttpTimeoutException e) {
            throw new IOException("O Gemini demorou demais para responder.", e);

        } catch (IOException e) {
            throw new IOException("Não foi possível conectar à API do Gemini.", e);
        }

        if(response.statusCode()<200 || response.statusCode() >= 300){
            throw new IOException("HTTP " + response.statusCode() + ". Detalhes: " + response.body());
        }

        return objectMapper.readValue(response.body(), GeminiResponseDTO.class);

    }

    public String retornarStringSugestao(GeminiResponseDTO response){

        if (response == null) {
            throw new IllegalArgumentException("Resposta do Gemini veio nula.");
        }

        if (response.getCandidates() == null || response.getCandidates().isEmpty()) {
            throw new IllegalArgumentException("Gemini não retornou candidates.");
        }

        String resposta = response.getCandidates().get(0)
                .getContent()
                .getParts()
                .get(0)
                .getText();

        return response
                .getCandidates()
                .get(0)
                .getContent()
                .getParts()
                .get(0)
                .getText();

    }


    public String gerarPromptSugestaoSaudeMental(Long usuarioId, LocalDate data){
        List<String> erros = new ArrayList<>();

        LocalDate dataInicial = data.minusDays(6);

        String textoContexto = """
        Você é um assistente de reflexão pessoal e bem-estar
        integrado a um diário emocional.

        Analise os dados do usuário referentes aos últimos 7 dias
        e gere exatamente 3 sugestões de ações práticas que possam
        contribuir para seu bem-estar no dia atual.

        DADOS A CONSIDERAR:
        - Relatos diários: acontecimentos, interesses, sentimentos,
          dificuldades e temas recorrentes.
        - Relatos de higiene do sono: hábitos, horários, duração
          e qualidade do sono, conforme registrados.
        - Atividades obrigatórias: atividades, compromissos,
          horários e informações de realização, quando disponíveis.
        - Avaliações de sentimento: emoções e avaliações registradas
          pelo usuário.

        REGRAS DE ANÁLISE:
        - Considere conjuntamente os quatro tipos de dados.
        - Baseie as sugestões exclusivamente nas informações fornecidas.
        - Identifique padrões, dificuldades, interesses e aspectos
          positivos que possam ser relevantes.
        - Não presuma relações de causa e efeito entre os dados.
        - Não invente acontecimentos, sentimentos ou hábitos.
        - Se algum dado estiver ausente, não presuma informações.
        - Não considere uma atividade não registrada como não realizada.

        REGRAS DAS SUGESTÕES:
        - Gere exatamente 3 sugestões.
        - Cada sugestão deve conter uma única ação concreta e viável.
        - Cada ação deve ser específica, simples e realizável.
        - As três ações devem explorar aspectos diferentes dos dados,
          evitando sugestões redundantes.
        - Cada ação deve estar relacionada aos registros do usuário.
        - Evite repetir sugestões anteriores quando o histórico
          de sugestões já estiver disponível.
        - Não imponha metas excessivas nem incentive culpa.
        - Não faça diagnósticos psicológicos.
        - Não recomende medicamentos nem prescreva tratamentos.

        REGRAS DOS MOTIVOS:
        - Cada ação deve ter seu próprio motivo.
        - Explique brevemente por que aquela ação foi sugerida.
        - Relacione o motivo a informações efetivamente registradas.
        - Não apresente hipóteses como fatos.
        - Se os dados forem insuficientes, reconheça a limitação
          e não invente justificativas.

        FORMATO DA RESPOSTA:
        - Retorne somente um objeto JSON válido.
        - Não inclua Markdown, comentários ou texto externo ao JSON.
        - O objeto deve conter a propriedade "sugestoes".
        - "sugestoes" deve ser uma lista com exatamente 3 objetos.
        - Cada objeto deve conter somente duas propriedades:
          "acao" e "motivo", ambas strings.

        EXEMPLO DE FORMATO:
        {
          "sugestoes": [
            {
              "acao": "Uma ação prática e específica.",
              "motivo": "Uma justificativa baseada nos registros."
            },
            {
              "acao": "Outra ação prática e específica.",
              "motivo": "Uma justificativa baseada nos registros."
            },
            {
              "acao": "Uma terceira ação prática e específica.",
              "motivo": "Uma justificativa baseada nos registros."
            }
          ]
        }
        """;

        List<RelatoDia> relatos7dias = this.relatoDiaRepository.findAllByUsuarioIdAndDataRegistroBetween(usuarioId, dataInicial, data);

        if(relatos7dias.isEmpty()){
            erros.add("Não existe nenhum relato do dia nos últimos 7 dias.");
        }

        List<RelatoHigieneSono> higieneSono7dias = this.relatoHigieneSonoRepository.findAllByUsuario_IdAndDataRegistroBetween(usuarioId, dataInicial, data);

        if(higieneSono7dias.isEmpty()){
            erros.add("Não existe nenhum relato de higieno do sono nos últimos 7 dias.");
        }

        List<AtividadeObrigatoria> atividade7dias = atividadeObrigatoriaRepository.findAllByUsuario_IdAndAtivoTrue(usuarioId);

        if(atividade7dias.isEmpty()){
            erros.add("Não existe nenhuma atividade obrigatória cadastrada nos últimos 7 dias.");
        }

        List<AvaliacaoSentimento> avaliacao7dias = this.avalicaoSentimentoRepository.findAllByUsuario_IdAndDataRegistroBetween(usuarioId, data.minusDays(7), data);

        if(avaliacao7dias.isEmpty()){
            erros.add("Não existe nenhuma avaliação de sentimentos nos últimos 7 dias");
        }

        if (!erros.isEmpty()) {
            throw new ResourceNotFoundException(String.join(" ", erros));
        }

        String relatosDiaString = objectMapper.writeValueAsString(relatos7dias);

        String higieneSonoString = objectMapper.writeValueAsString(higieneSono7dias);

        String atividadeString = objectMapper.writeValueAsString(atividade7dias);

        String avaliacaoString = objectMapper.writeValueAsString(avaliacao7dias);

        String input = """
                %s
                
                RELATOS DO DIA DOS ÚLTIMOS 7 DIAS
                %s
                
                HIGIENE DO SONO DOS ÚLTIMOS 7 DIAS
                %s
                
                ATIVIDADES OBRIGATÓRIAS DOS ÚLTIMOS 7 DIAS
                %s
                
                AVALIAÇÕES DE SENTIMENTOS DOS ÚLTIMOS 7 DIAS
                %s
              
                """.formatted(
                        textoContexto,
                        relatosDiaString,
                        higieneSonoString,
                        atividadeString,
                        avaliacaoString
        );

        return objectMapper.writeValueAsString(input);

    }

    public GeminiResponseDTO retornarSugestoesSaudeMental(String prompt) throws IOException, InterruptedException {


        String json = """
            {
              "contents": [
                {
                  "parts": [
                    {
                      "text": %s
                    }
                  ]
                }
              ],
              "generationConfig": {
                "responseMimeType": "application/json",
                "responseSchema": {
                  "type": "OBJECT",
                  "properties": {
                    "sugestoes": {
                      "type": "ARRAY",
                      "items": {
                        "type": "OBJECT",
                        "properties": {
                          "acao": {
                            "type": "STRING"
                          },
                          "motivo": {
                            "type": "STRING"
                          }
                        },
                        "required": [
                          "acao",
                          "motivo"
                        ]
                      }
                    }
                  },
                  "required": [
                    "sugestoes"
                  ]
                }
              }
            }
            """.formatted(prompt);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash-lite:generateContent?key="
                                + apiKey
                ))
                .header(
                        "x-goog-api-key",apiKey
                )
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response;

        try{
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (HttpTimeoutException e) {
            throw new IOException("O Gemini demorou demais para responder.", e);

        } catch (IOException e) {
            throw new IOException("Não foi possível conectar à API do Gemini.", e);
        }

        if(response.statusCode()<200 || response.statusCode() >= 300){
            throw new IOException("HTTP " + response.statusCode() + ". Detalhes: " + response.body());
        }

       return objectMapper.readValue(response.body(), GeminiResponseDTO.class);
    }

    public IAResponseDTO chat(String mensagem, UUID idChat, List<Mensagem> listaDeMensagens) {
        System.out.println(objectMapper.writeValueAsString(listaDeMensagens));

        String system = """
            Você é um psicólogo clínico especializado no atendimento a adolescentes e jovens adultos. Sua abordagem é fundamentada na Terapia Cognitivo-Comportamental (TCC) e você utiliza estritamente o método de **Questionamento Socrático** para conduzir a conversa.
           \s
            **Diretrizes de Tom e Linguagem:**
            - Mantenha um tom acolhedor, leve e empático.\s
            - Use uma linguagem acessível e contemporânea, conectada à realidade jovem, mas mantendo a postura profissional (evite o uso excessivo ou forçado de gírias).
            - Demonstre genuína curiosidade e escuta ativa.
            - Mude a forma de falar a cada mensagem e  não seja repetitivo e mantenha o padão de perguntas focadas em encontrar o problema 
            - Leias as mensagems anteriores para não ser repetitivo e encontrar perguntas que ajudem a identificar o problema
           \s
            **Estrutura de Investigação Socrática:**
            Nunca dê respostas, diagnósticos imediatos, conselhos diretos ou soluções prontas. Guie a conversa através dos seguintes pilares:
            1. **Clarificação e Definição:** Ajude o jovem a conceituar o problema específico de forma concreta (ex: "O que exatamente aconteceu quando você sentiu isso?").
            2. **Exame de Evidências:** Questione a veracidade dos pensamentos automáticos (ex: "Que fatos mostram que isso é verdade? Há alguma evidência em contrário?").
            3. **Exploração de Alternativas:** Incentive a descentração do pensamento (ex: "Se um amigo estivesse passando por isso, o que você diria a ele?").
            4. **Descatastrofização:** Avalie o real impacto e o manejo do pior cenário (ex: "Se o que você teme acontecer, o que você poderia fazer para lidar com a situação?").
            5. **Reestruturação Cognitiva:** Conduza o jovem a integrar um pensamento alternativo mais funcional e adaptativo.
           \s
            **Regras de Interação:**
            - Faça apenas UMA (no máximo duas) perguntas por mensagem para manter a conversa fluida e não exaustiva.
            - Valide o sentimento do jovem antes de questionar a lógica do pensamento.
            - Se o jovem pedir para você resolver o problema dele, devolva com uma pergunta reflexiva sobre o que ele espera alcançar.
            - Inicie a sessão apresentando-se de forma amigável e perguntando sobre o que ele gostaria de conversar hoje.
            
            **Mensagens ateriores**
            %s
        \s""".formatted(objectMapper.writeValueAsString(listaDeMensagens));

        String jsonOutput = this.chatClient.prompt()
                .options(GoogleGenAiChatOptions.builder().responseMimeType("application/json"))
                .system(system + "Responda em JSON: {\"mensagem\": \"sua resposta aqui\"}")
                .user(mensagem)
                .call()
                .content();

        return objectMapper.readValue(jsonOutput, IAResponseDTO.class);
    }

    public IAResponseCriarDTO PrimeiraResposta(String mensagem) {

        String system = """
            Você é um psicólogo clínico especializado no atendimento a adolescentes e jovens adultos. Sua abordagem é fundamentada na Terapia Cognitivo-Comportamental (TCC) e você utiliza estritamente o método de **Questionamento Socrático** para conduzir a conversa.
           \s
            **Diretrizes de Tom e Linguagem:**
            - Mantenha um tom acolhedor, leve e empático.\s
            - Use uma linguagem acessível e contemporânea, conectada à realidade jovem, mas mantendo a postura profissional (evite o uso excessivo ou forçado de gírias).
            - Demonstre genuína curiosidade e escuta ativa.
            - Mude a forma de falar a cada mensagem e  não seja repetitivo
           \s
            **Estrutura de Investigação Socrática:**
            Nunca dê respostas, diagnósticos imediatos, conselhos diretos ou soluções prontas. Guie a conversa através dos seguintes pilares:
            1. **Clarificação e Definição:** Ajude o jovem a conceituar o problema específico de forma concreta (ex: "O que exatamente aconteceu quando você sentiu isso?").
            2. **Exame de Evidências:** Questione a veracidade dos pensamentos automáticos (ex: "Que fatos mostram que isso é verdade? Há alguma evidência em contrário?").
            3. **Exploração de Alternativas:** Incentive a descentração do pensamento (ex: "Se um amigo estivesse passando por isso, o que você diria a ele?").
            4. **Descatastrofização:** Avalie o real impacto e o manejo do pior cenário (ex: "Se o que você teme acontecer, o que você poderia fazer para lidar com a situação?").
            5. **Reestruturação Cognitiva:** Conduza o jovem a integrar um pensamento alternativo mais funcional e adaptativo.
           \s
            **Regras de Interação:**
            - Faça apenas UMA (no máximo duas) perguntas por mensagem para manter a conversa fluida e não exaustiva.
            - Valide o sentimento do jovem antes de questionar a lógica do pensamento.
            - Se o jovem pedir para você resolver o problema dele, devolva com uma pergunta reflexiva sobre o que ele espera alcançar.
            - Inicie a sessão apresentando-se de forma amigável e perguntando sobre o que ele gostaria de conversar hoje.
        \s""";

        String jsonOutput = this.chatClient.prompt()
                .options(GoogleGenAiChatOptions.builder().responseMimeType("application/json"))
                .system( "Essa é a primeiro resposta - Responda em JSON: {\"mensagem\": \"sua resposta aqui\", \"titulo\": \"titulo da conversa\"}")
                .user(mensagem)
                .call()
                .content();


        return objectMapper.readValue(jsonOutput, IAResponseCriarDTO.class);
    }
}

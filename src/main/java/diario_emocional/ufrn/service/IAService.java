package diario_emocional.ufrn.service;

import diario_emocional.ufrn.dto.IA.IAResponseCriarDTO;
import diario_emocional.ufrn.dto.IA.GeminiResponseDTO;
import diario_emocional.ufrn.dto.IA.IAResponseDTO;
import diario_emocional.ufrn.entity.Mensagem;
import diario_emocional.ufrn.entity.RelatoDia;
import diario_emocional.ufrn.exception.ResourceNotFoundException;
import diario_emocional.ufrn.repository.RelatoDiaRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Service
public class IAService {


    private final HttpClient httpClient;
    private final RelatoDiaRepository relatoDiaRepository;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api-key}")
    private String apiKey;
    private final ChatClient chatClient;

    public IAService(RelatoDiaRepository relatoDiaRepository, ObjectMapper objectMapper, ChatModel chatModel, ChatClient.Builder chatClientBuilder){
        this.chatClient = chatClientBuilder.build();
        this.httpClient = HttpClient.newHttpClient();
        this.relatoDiaRepository = relatoDiaRepository;
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
            throw new ResourceNotFoundException("Não existe nenhum relato dos últimos 7 dias.");
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

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString());

        return  objectMapper.readValue(
                response.body(),
                GeminiResponseDTO.class
        );
    }

    public String retornarStringSugestao(GeminiResponseDTO response){

        return response
                .getCandidates()
                .get(0)
                .getContent()
                .getParts()
                .get(0)
                .getText();

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

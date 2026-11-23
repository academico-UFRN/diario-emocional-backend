package diario_emocional.ufrn.framework.service;

import diario_emocional.ufrn.framework.dto.IA.IAResponseCriarDTO;
import diario_emocional.ufrn.framework.dto.IA.GeminiResponseDTO;
import diario_emocional.ufrn.framework.dto.IA.IAResponseDTO;
import diario_emocional.ufrn.framework.entity.*;
import diario_emocional.ufrn.framework.prompts.ChatSocraticPromptTemplate;
import diario_emocional.ufrn.framework.prompts.SocraticPromptSpecialization;
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
import java.net.http.HttpTimeoutException;
import java.util.Comparator;
import java.util.List;


@Service
public class IAService {


    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final SocraticPromptSpecialization socraticPromptSpecialization;

    @Value("${gemini.api-key}")
    private String apiKey;
    private final ChatClient chatClient;

    public IAService(
            ObjectMapper objectMapper,
            ChatModel chatModel,
            ChatClient.Builder chatClientBuilder,
            SocraticPromptSpecialization socraticPromptSpecialization
    ) {
        this.chatClient = chatClientBuilder.build();
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = objectMapper;
        this.socraticPromptSpecialization = socraticPromptSpecialization;
    }

    public String gerarPromptSugestoesRelatoDia(List<RelatoDia> relatos7dias){
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

        return response
                .getCandidates()
                .get(0)
                .getContent()
                .getParts()
                .get(0)
                .getText();

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
            """.formatted(objectMapper.writeValueAsString(prompt));

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

    public IAResponseDTO gerarRespostaNoChatSocratico(
            String tema,
            String mensagem,
            List<Mensagem> listaDeMensagens,
            List<? extends Avaliacao> ultimasAvaliacoes
    ) {
        List<Mensagem> historicoCronologico = listaDeMensagens.stream()
                .sorted(Comparator.comparing(Mensagem::getCriadoEm))
                .toList();
        ChatSocraticPromptTemplate template = criarTemplateSocratico(
                tema,
                objectMapper.writeValueAsString(historicoCronologico),
                objectMapper.writeValueAsString(ultimasAvaliacoes),
                false
        );

        String jsonOutput = this.chatClient.prompt()
                .options(GoogleGenAiChatOptions.builder().responseMimeType("application/json"))
                .system(template.montarPrompt())
                .user(mensagem)
                .call()
                .content();

        IAResponseDTO resposta = objectMapper.readValue(jsonOutput, IAResponseDTO.class);
        if (resposta == null) {
            throw new IllegalStateException("A IA não retornou uma resposta válida para o chat.");
        }
        validarResposta(resposta.pergunta(), resposta.concluiu());
        return resposta;
    }

    public IAResponseCriarDTO gerarPrimeiraRespostaNoChatSocratico(
            String mensagem,
            List<? extends Avaliacao> ultimasAvaliacoes
    ) {
        ChatSocraticPromptTemplate template = criarTemplateSocratico(
                mensagem,
                "[]",
                objectMapper.writeValueAsString(ultimasAvaliacoes),
                true
        );

        String jsonOutput = this.chatClient.prompt()
                .options(GoogleGenAiChatOptions.builder().responseMimeType("application/json"))
                .system(template.montarPrompt())
                .user(mensagem)
                .call()
                .content();

        IAResponseCriarDTO resposta = objectMapper.readValue(jsonOutput, IAResponseCriarDTO.class);
        if (resposta == null) {
            throw new IllegalStateException("A IA não retornou uma resposta válida para o chat.");
        }
        validarResposta(resposta.pergunta(), resposta.concluiu());
        if (resposta.titulo() == null || resposta.titulo().isBlank()) {
            throw new IllegalStateException("A IA não retornou um título válido para o chat.");
        }
        return resposta;
    }

    private ChatSocraticPromptTemplate criarTemplateSocratico(
            String tema,
            String historico,
            String contextoAdicional,
            boolean solicitarTitulo
    ) {
        return new ChatSocraticPromptTemplate(
                tema,
                historico,
                contextoAdicional,
                "Marque como concluída somente quando a pessoa indicar que alcançou o resultado "
                        + "que buscava ou que não deseja continuar.",
                solicitarTitulo,
                socraticPromptSpecialization.orientacoesDeDominio()
        );
    }

    private void validarResposta(String pergunta, Boolean concluiu) {
        if (pergunta == null || pergunta.isBlank()) {
            throw new IllegalStateException("A IA não retornou uma pergunta válida para o chat.");
        }
        if (concluiu == null) {
            throw new IllegalStateException("A IA não informou se a conversa foi concluída.");
        }
    }
}

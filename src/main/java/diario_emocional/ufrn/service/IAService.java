package diario_emocional.ufrn.service;

import diario_emocional.ufrn.dto.IA.GeminiResponseDTO;
import diario_emocional.ufrn.entity.RelatoDia;
import diario_emocional.ufrn.exception.ResourceNotFoundException;
import diario_emocional.ufrn.repository.RelatoDiaRepository;
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


@Service
public class IAService {


    private final HttpClient httpClient;
    private final RelatoDiaRepository relatoDiaRepository;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api-key}")
    private String apiKey;

    public IAService(RelatoDiaRepository relatoDiaRepository, ObjectMapper objectMapper){
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

    
}

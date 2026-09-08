package diario_emocional.ufrn.entity;


import jakarta.persistence.Embeddable;

@Embeddable
public class Sentimento {
    
    private String sentimento;
    private Integer intensidade;

    public Sentimento() {}

    public Sentimento(String sentimento, Integer intensidade) {
        validar(sentimento, intensidade);

        this.sentimento = sentimento;
        this.intensidade = intensidade;
    }


    private void validar(String sentimento, Integer intensidade) {
        if (sentimento == null || sentimento.isBlank()) {
           throw new IllegalArgumentException("Sentimendo não deve estar vazio ou nulo!");
        }
        if (intensidade == null || intensidade > 5 || intensidade < 1) {
            throw new IllegalArgumentException("A intensidade do sentimento deve ser uma nota de 1 a 5.");
        }
    }

    public String getSentimento() {
        return sentimento;
    }

    public Integer getIntensidade() {
        return intensidade;
    }
}

package diario_emocional.ufrn.entity.sentimento;


import jakarta.persistence.Embeddable;

@Embeddable
public class Sentimento {
    
    private String sentimento;
    private Integer intesidade;

    public void setSentimento(String sentiment) {
        this.sentimento = sentiment;
    }

    public void setIntesidade(Integer intesity) {
        this.intesidade = intesity;
    }

    public String getSentimento() {
        return sentimento;
    }

    public Integer getIntesidade() {
        return intesidade;
    }
}

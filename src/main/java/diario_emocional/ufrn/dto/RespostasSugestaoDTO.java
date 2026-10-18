package diario_emocional.ufrn.dto;

import java.util.List;

public class RespostasSugestaoDTO {

    private List<SugestaoDTO> sugestoes;

    public List<SugestaoDTO> getSugestoes() {
        return this.sugestoes;
    }

    public void setSugestoes(List<SugestaoDTO> sugestoes) {
        this.sugestoes = sugestoes;
    }
}
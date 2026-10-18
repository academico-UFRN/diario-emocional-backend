package diario_emocional.ufrn.dto;

public class SugestaoDTO {

    private String acao;
    private String motivo;

    public String getAcao() {
        return this.acao;
    }

    public String getMotivo() {
        return this.motivo;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
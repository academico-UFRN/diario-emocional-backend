package diario_emocional.ufrn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "sugestao")
public class Sugestao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String acao;

    private String motivo;

    @ManyToOne
    @JoinColumn(name = "solicitacao_id", nullable = false)
    @JsonIgnore
    private Solicitacao solicitacao;

    public Sugestao(String acao, String motivo) {
        this.acao = acao;
        this.motivo = motivo;
    }
    public Long getId() {
        return id;
    }

    public String getAcao() {
        return acao;
    }

    public String getMotivo() {
        return motivo;
    }

    public Solicitacao getSolicitacao() {
        return solicitacao;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public void setSolicitacao(Solicitacao solicitacao) {
        this.solicitacao = solicitacao;
    }
}

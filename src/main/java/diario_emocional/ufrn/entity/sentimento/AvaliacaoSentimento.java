package diario_emocional.ufrn.entity.sentimento;

import diario_emocional.ufrn.entity.Usuario;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name= "avalicao-de-sentimentos")
public class AvaliacaoSentimento {

    @Id
    @Column(name = "data_registro")
    private LocalDate dataRegistro;

    @Column(name = "avalicao_dia")
    private Integer avaliacaoDia;

    @ElementCollection
    @CollectionTable(
            name = "avaliacao_sentimentos_lista",
            joinColumns = @JoinColumn(name = "data_registro")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Sentimento> sentimentos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "avaliacao_gatilhos_lista",
            joinColumns = @JoinColumn(name = "data_registro")
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Column(name = "gatilho")
    private List<String> gatilhos = new ArrayList<>();

    @Column(name = "texto_livre")
    private String textoLivre;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public Integer getAvaliacaoDia() {
        return avaliacaoDia;
    }

    public List<Sentimento> getSentimentos() {
        return sentimentos;
    }

    public List<String> getGatilhos() {
        return gatilhos;
    }


    public String getTextoLivre() {
        return textoLivre;
    }

    public void From(AvalicaoSentimentoRequestDTO dto){
        this.avaliacaoDia = dto.avaliacaoDia();
        this.textoLivre = dto.textoLivre();
        if (dto.gatilhos() != null) {
            this.gatilhos.clear();
            this.gatilhos.addAll(dto.gatilhos());
        }

        if (dto.sentimentos() != null) {
            this.sentimentos.clear();
            List<Sentimento> novosSentimentos = dto.sentimentos().stream()
                    .map(s -> {
                        Sentimento sentimento = new Sentimento();
                        sentimento.setSentimento(s.sentimento());
                        sentimento.setIntesidade(s.intensidade());
                        return sentimento;
                    }).toList();
            this.sentimentos.addAll(novosSentimentos);
        }
    }
}

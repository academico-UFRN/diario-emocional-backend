package diario_emocional.ufrn.instancia_estudantes.entity;

import diario_emocional.ufrn.framework.entity.Avaliacao;
import diario_emocional.ufrn.framework.entity.Sentimento;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "avalicao-de-sentimentos")
public class AvaliacaoEstudante extends Avaliacao {

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

    public AvaliacaoEstudante() {
        super();
    }

    public AvaliacaoEstudante(Integer avaliacaoDia, String textoLivre, List<String> gatilhos, List<Sentimento> sentimentos) {
        super(avaliacaoDia, textoLivre);
        this.gatilhos = (gatilhos != null) ? new ArrayList<>(gatilhos) : new ArrayList<>();
        this.sentimentos = (sentimentos != null) ? new ArrayList<>(sentimentos) : new ArrayList<>();
    }

    public List<Sentimento> getSentimentos() {
        return sentimentos;
    }

    public void setSentimentos(List<Sentimento> sentimentos) {
        this.sentimentos = (sentimentos != null) ? new ArrayList<>(sentimentos) : new ArrayList<>();
    }

    public List<String> getGatilhos() {
        return gatilhos;
    }

    public void setGatilhos(List<String> gatilhos) {
        this.gatilhos = (gatilhos != null) ? new ArrayList<>(gatilhos) : new ArrayList<>();
    }
}

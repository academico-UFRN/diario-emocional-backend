package diario_emocional.ufrn.entity;

import diario_emocional.ufrn.dto.AvaliacaoSentimentoRequestDTO;
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

    protected AvaliacaoSentimento() {}

    // Construtor completo com validação

    public AvaliacaoSentimento(Integer avaliacaoDia, String textoLivre, List<String> gatilhos, List<Sentimento> sentimentos) {
        validar(avaliacaoDia, textoLivre, sentimentos);

        this.avaliacaoDia = avaliacaoDia;
        this.textoLivre = textoLivre;
        this.gatilhos = (gatilhos != null) ? gatilhos : new ArrayList<>();
        this.sentimentos = (sentimentos != null) ? sentimentos : new ArrayList<>();
    }

    public void updateEntityFromDTO(AvaliacaoSentimentoRequestDTO dto) {
        validar(dto.avaliacaoDia(), dto.textoLivre(), dto.sentimentos().stream().map(s -> new Sentimento(s.sentimento(), s.intensidade()))
                .toList());

        this.setAvaliacaoDia(dto.avaliacaoDia());
        this.setTextoLivre(dto.textoLivre());

        this.getGatilhos().clear();
        if (dto.gatilhos() != null) {
            this.getGatilhos().addAll(dto.gatilhos());
        }

        this.getSentimentos().clear();
        this.getSentimentos().addAll(
                dto.sentimentos().stream()
                        .map(s -> new Sentimento(s.sentimento(), s.intensidade()))
                        .toList()
        );
    }

    // Concat string and retunr erro - To do

    private void validar(Integer avaliacaoDia, String textoLivre, List<Sentimento> sentimentos) {
        if (avaliacaoDia == null || avaliacaoDia < 1 || avaliacaoDia > 5) {
            throw new IllegalArgumentException("A avaliação do dia deve ser uma nota de 1 a 5.");
        }
        if (textoLivre != null && textoLivre.length() > 250) {
            throw new IllegalArgumentException("O texto livre não pode ultrapassar 250 caracteres.");
        }
        if (sentimentos == null || sentimentos.isEmpty()) {
            throw new IllegalArgumentException("É necessário 1 ou mais sentimentos");
        }
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setAvaliacaoDia(Integer avaliacaoDia) {
        this.avaliacaoDia = avaliacaoDia;
    }

    public void setTextoLivre(String textoLivre) {
        this.textoLivre = textoLivre;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
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

    public Usuario getUsuario() {
        return usuario;
    }
}

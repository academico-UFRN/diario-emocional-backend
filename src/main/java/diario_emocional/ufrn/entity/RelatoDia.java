package diario_emocional.ufrn.entity;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "relato-do-dia")
public class RelatoDia {

    @Id
    private LocalDate dataRegistro;

    private String titulo;

    private String conteudoHtml;

    private boolean isFavorito;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    //efeito cascata, alteração em relato = alteração em avaliacao
    //remove orfãos que ficaram soltos de relato
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "avaliacao_sentimento_id", unique = true)
    private AvaliacaoSentimento avaliacaoSentimento;

    public LocalDate getDataRegistro(){
        return this.dataRegistro;
    }

    public void setUsuario(Usuario usuario){
        this.usuario=usuario;
    }

}

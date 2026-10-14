package diario_emocional.ufrn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "solicitacao")
public class Solicitacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataRegistro;

    @OneToMany(
            mappedBy = "solicitacao",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Sugestao> sugestoes;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    public void setSugestoes(List<Sugestao> sugestoes){
        this.sugestoes = sugestoes;
    }

    public void setDataRegistro(LocalDate data){
        this.dataRegistro = data;
    }
}

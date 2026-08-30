package diario_emocional.ufrn.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name= "avalicao-de-sentimentos")
public class AvaliacaoSentimento {

    @Id
    private LocalDate dataRegistro;

}

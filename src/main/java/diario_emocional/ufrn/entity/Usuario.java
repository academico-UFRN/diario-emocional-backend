package diario_emocional.ufrn.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Usuario {
    @Id
    @GeneratedValue
    private Long id;
}

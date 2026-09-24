package diario_emocional.ufrn.repository;

import diario_emocional.ufrn.entity.AtividadeObrigatoria;
import diario_emocional.ufrn.entity.Lembrete;
import diario_emocional.ufrn.entity.Usuario;
import diario_emocional.ufrn.enums.DiaSemana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface LembreteRepository extends JpaRepository<Lembrete, Long> {

    }

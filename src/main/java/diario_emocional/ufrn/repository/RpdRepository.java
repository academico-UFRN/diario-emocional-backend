package diario_emocional.ufrn.repository;

import diario_emocional.ufrn.entity.RegistroPensamentoDisfuncional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RpdRepository extends JpaRepository<RegistroPensamentoDisfuncional, Long> {
    List<RegistroPensamentoDisfuncional> findAllByUsuario_IdOrderByDataHoraRegistroDesc(Long usuarioId);

    Optional<RegistroPensamentoDisfuncional> findByIdAndUsuario_Id(Long id, Long usuarioId);
}

package diario_emocional.ufrn.mapper;

import diario_emocional.ufrn.dto.RpdRequestDTO;
import diario_emocional.ufrn.dto.RpdResponseDTO;
import diario_emocional.ufrn.entity.RegistroPensamentoDisfuncional;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RpdMapper {

    public RegistroPensamentoDisfuncional toEntity(RpdRequestDTO dto) {
        RegistroPensamentoDisfuncional entity = new RegistroPensamentoDisfuncional();
        atualizar(entity, dto);
        return entity;
    }

    public void atualizar(RegistroPensamentoDisfuncional entity, RpdRequestDTO dto) {
        entity.setIntensidadeCrise(dto.intensidadeCrise());
        entity.setEmocoesPrincipais(dto.emocoesPrincipais());
        entity.setGatilhoSituacao(dto.gatilhoSituacao());
        entity.setPensamentoAutomatico(dto.pensamentoAutomatico());
        entity.setDistorcoesCognitivas(dto.distorcoesCognitivas());
        entity.setEvidenciasContraArgumento(dto.evidenciasContraArgumento());
        entity.setPensamentoAlternativo(dto.pensamentoAlternativo());
        entity.setIntensidadeReavaliada(dto.intensidadeReavaliada());
    }

    public RpdResponseDTO toDTO(RegistroPensamentoDisfuncional entity) {
        return new RpdResponseDTO(
                entity.getId(),
                entity.getDataHoraRegistro(),
                entity.getIntensidadeCrise(),
                List.copyOf(entity.getEmocoesPrincipais()),
                entity.getGatilhoSituacao(),
                entity.getPensamentoAutomatico(),
                List.copyOf(entity.getDistorcoesCognitivas()),
                entity.getEvidenciasContraArgumento(),
                entity.getPensamentoAlternativo(),
                entity.getIntensidadeReavaliada()
        );
    }
}

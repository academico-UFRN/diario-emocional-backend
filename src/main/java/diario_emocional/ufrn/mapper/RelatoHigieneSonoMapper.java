package diario_emocional.ufrn.mapper;

import diario_emocional.ufrn.dto.RelatoHigieneSonoRequestDTO;
import diario_emocional.ufrn.dto.RelatoHigieneSonoResponseDTO;
import diario_emocional.ufrn.entity.RelatoHigieneSono;
import org.springframework.stereotype.Component;

@Component
public class RelatoHigieneSonoMapper {

    public RelatoHigieneSono toEntity(RelatoHigieneSonoRequestDTO dto) {
        RelatoHigieneSono relato = new RelatoHigieneSono();
        atualizar(relato, dto);
        return relato;
    }

    public void atualizar(RelatoHigieneSono relato, RelatoHigieneSonoRequestDTO dto) {
        relato.setDataRegistro(dto.dataRegistro());
        relato.setHoraDormir(dto.horaDormir());
        relato.setHoraAcordar(dto.horaAcordar());
        relato.setQualidadeSono(dto.qualidadeSono());
        relato.setUsouCelular(dto.usouCelular());
        relato.setTevePesadelos(dto.tevePesadelos());
        relato.setComentarioSonhos(dto.comentarioSonhos());
        relato.setNivelDisposicao(dto.nivelDisposicao());
    }

    public RelatoHigieneSonoResponseDTO toDTO(RelatoHigieneSono relato) {
        return new RelatoHigieneSonoResponseDTO(
                relato.getId(),
                relato.getDataRegistro(),
                relato.getHoraDormir(),
                relato.getHoraAcordar(),
                relato.getDuracaoSonoMinutos(),
                relato.getQualidadeSono(),
                relato.isUsouCelular(),
                relato.isTevePesadelos(),
                relato.getComentarioSonhos(),
                relato.getNivelDisposicao()
        );
    }
}
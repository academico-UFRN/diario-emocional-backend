package diario_emocional.ufrn.mapper;

import diario_emocional.ufrn.dto.IA.MensagemResponseDTO;
import diario_emocional.ufrn.entity.Mensagem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MensagemMapper {

    public MensagemResponseDTO toDTO(Mensagem mensagem) {
        if (mensagem == null) {
            return null;
        }

        return new MensagemResponseDTO(
                mensagem.getId(),
                mensagem.getConteudo(),
                mensagem.getPapel(),
                mensagem.getCriadoEm()
        );
    }

    public List<MensagemResponseDTO> toDTOList(
            List<Mensagem> mensagens
    ) {
        return mensagens.stream()
                .map(this::toDTO)
                .toList();
    }
}
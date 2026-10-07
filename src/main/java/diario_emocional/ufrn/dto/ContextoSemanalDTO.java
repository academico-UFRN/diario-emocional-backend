package diario_emocional.ufrn.dto;

import diario_emocional.ufrn.entity.AtividadeObrigatoria;
import diario_emocional.ufrn.entity.AvaliacaoSentimento;
import diario_emocional.ufrn.entity.RelatoDia;
import diario_emocional.ufrn.entity.RelatoHigieneSono;

import java.util.List;

public class ContextoSemanalDTO {

    private List<RelatoDia> relatos;
    private List<RelatoHigieneSono> higieneSono;
    private List<AtividadeObrigatoria> atividades;
    private List<AvaliacaoSentimento> sentimentos;

}
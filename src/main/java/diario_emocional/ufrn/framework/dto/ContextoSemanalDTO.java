package diario_emocional.ufrn.framework.dto;

import diario_emocional.ufrn.framework.entity.AtividadeObrigatoria;
import diario_emocional.ufrn.framework.entity.Avaliacao;
import diario_emocional.ufrn.framework.entity.RelatoDia;
import diario_emocional.ufrn.framework.entity.RelatoHigieneSono;

import java.util.List;

public class ContextoSemanalDTO {

    private List<RelatoDia> relatos;
    private List<RelatoHigieneSono> higieneSono;
    private List<AtividadeObrigatoria> atividades;
    private List<Avaliacao> sentimentos;

}
package repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import model.Avaliacao;
import model.Medico;

public class AvaliacaoRepository {

    private static final List<Avaliacao> avaliacoes = new ArrayList<>();

    public static void salvar(Avaliacao avaliacao) {
        avaliacoes.add(avaliacao);
    }

    public static List<Avaliacao> listarPorMedico(Medico medico) {
        return avaliacoes.stream()
                .filter(a -> a.getMedico().equals(medico))
                .collect(Collectors.toList());
    }

    public static double calcularMedia(Medico medico) {
        List<Avaliacao> lista = listarPorMedico(medico);

        if (lista.isEmpty()) return 0;

        return lista.stream()
                .mapToInt(Avaliacao::getNota)
                .average()
                .orElse(0);
    }

    public static int quantidade(Medico medico) {
        return listarPorMedico(medico).size();
    }
}

package repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import model.Consulta;
import model.Medico;
import model.Paciente;
import util.CaminhoCSV;

public class ConsultaRepository {

    private static final String ARQUIVO = CaminhoCSV.consultas();

    private static String limpar(String texto) {
        if (texto == null) return "null";
        return texto.replace(";", ",").replace("\n", " ");
    }

    private static List<Consulta> listarTudo() {
        List<Consulta> consultas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {

            String cabecalho = br.readLine();
            if (cabecalho == null) return consultas;

            String linha;
            int linhaNumero = 1;

            while ((linha = br.readLine()) != null) {
                linhaNumero++;

                String[] d = linha.split(";", -1);

                if (d.length < 9) {
                    System.err.println("Linha inválida (" + linhaNumero + "): " + linha);
                    continue;
                }

                Paciente paciente = PacienteRepository.buscarPorEmail(d[0]);
                Medico medico = MedicoRepository.listar().stream()
                        .filter(m -> m.getEmail().equals(d[1]))
                        .findFirst()
                        .orElse(null);

                if (paciente == null || medico == null) {
                    System.err.println("Consulta ignorada (paciente ou médico inexistente): " + linha);
                    continue;
                }

                Consulta c = new Consulta(
                        paciente,
                        medico,
                        LocalDate.parse(d[2]),
                        Boolean.parseBoolean(d[3])
                );

                boolean realizada = Boolean.parseBoolean(d[4]);

                if (realizada) {

                    String sintomas = null;
                    String tratamento = null;

                    if (!"null".equals(d[7])) {
                        sintomas = d[7];
                    }

                    if (!"null".equals(d[8])) {
                        tratamento = d[8];
                    }

                    c.realizar(sintomas, tratamento);
                }

                if (!"null".equals(d[5])) {

                    String comentario = null;

                    if (!"null".equals(d[6])) {
                        comentario = d[6];
                    }

                    c.avaliar(
                            Integer.parseInt(d[5]),
                            comentario
                    );
                }

                consultas.add(c);
            }

        } catch (Exception e) {
            System.err.println("Erro ao ler arquivo de consultas:");
            e.printStackTrace();
        }

        return consultas;
    }

    public static void salvar(Consulta consulta) {
        List<Consulta> consultas = listarTudo();
        consultas.add(consulta);
        escrever(consultas);
    }

    public static long contarConsultasDoMedicoNoDia(Medico medico, LocalDate data) {
        return listarTudo().stream()
                .filter(c -> c.getMedico().getEmail().equals(medico.getEmail()))
                .filter(c -> c.getData().equals(data))
                .filter(c -> !c.isEmEspera())
                .count();
    }

    public static List<Consulta> listarDoMedicoPendentes(Medico medico) {
        return listarTudo().stream()
                .filter(c -> c.getMedico().getEmail().equals(medico.getEmail()))
                .filter(c -> !c.isRealizada())
                .filter(c -> !c.isEmEspera())
                .collect(Collectors.toList());
    }

    public static List<Consulta> listarPorPaciente(Paciente paciente) {
        return listarTudo().stream()
                .filter(c -> c.getPaciente().getEmail().equals(paciente.getEmail()))
                .collect(Collectors.toList());
    }

    public static List<Consulta> listarParaAvaliacao(Paciente paciente) {
        return listarTudo().stream()
                .filter(c -> c.getPaciente().getEmail().equals(paciente.getEmail()))
                .filter(Consulta::isRealizada)
                .filter(c -> !c.isAvaliada())
                .collect(Collectors.toList());
    }

    public static List<Consulta> listarAvaliacoesDoMedico(Medico medico) {
        return listarTudo().stream()
                .filter(c -> c.getMedico().getEmail().equals(medico.getEmail()))
                .filter(Consulta::isAvaliada)
                .collect(Collectors.toList());
    }

    public static double calcularMediaDoMedico(Medico medico) {
        return listarAvaliacoesDoMedico(medico).stream()
                .mapToInt(Consulta::getNota)
                .average()
                .orElse(0);
    }

    public static void atualizarConsulta(Consulta atualizada) {
        List<Consulta> consultas = listarTudo();

        for (Consulta c : consultas) {
            if (c.getPaciente().getEmail().equals(atualizada.getPaciente().getEmail())
                    && c.getMedico().getEmail().equals(atualizada.getMedico().getEmail())
                    && c.getData().equals(atualizada.getData())) {

                if (atualizada.isRealizada()) {
                    c.realizar(
                            atualizada.getSintomas(),
                            atualizada.getTratamento()
                    );
                }

                if (atualizada.isAvaliada()) {
                    c.avaliar(
                            atualizada.getNota(),
                            atualizada.getComentario()
                    );
                }

                c.setEmEspera(atualizada.isEmEspera());
                break;
            }
        }

        escrever(consultas);
    }

    public static void cancelarConsulta(Consulta consulta) {
        List<Consulta> consultas = listarTudo();

        consultas.removeIf(c ->
                c.getPaciente().getEmail().equals(consulta.getPaciente().getEmail()) &&
                c.getMedico().getEmail().equals(consulta.getMedico().getEmail()) &&
                c.getData().equals(consulta.getData())
        );

        promoverDaListaDeEspera(consulta.getMedico(), consulta.getData(), consultas);
        escrever(consultas);
    }

    private static void promoverDaListaDeEspera(
            Medico medico,
            LocalDate data,
            List<Consulta> consultas) {

        consultas.stream()
                .filter(c -> c.getMedico().getEmail().equals(medico.getEmail()))
                .filter(c -> c.getData().equals(data))
                .filter(Consulta::isEmEspera)
                .findFirst()
                .ifPresent(c -> c.setEmEspera(false));
    }

    private static void escrever(List<Consulta> consultas) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO))) {

            pw.println(
                "emailPaciente;emailMedico;data;emEspera;realizada;" +
                "nota;comentario;sintomas;tratamento"
            );

            for (Consulta c : consultas) {

                String nota = "null";
                if (c.getNota() != null) {
                    nota = c.getNota().toString();
                }

                pw.println(
                        c.getPaciente().getEmail() + ";" +
                        c.getMedico().getEmail() + ";" +
                        c.getData() + ";" +
                        c.isEmEspera() + ";" +
                        c.isRealizada() + ";" +
                        nota + ";" +
                        limpar(c.getComentario()) + ";" +
                        limpar(c.getSintomas()) + ";" +
                        limpar(c.getTratamento())
                );
            }

        } catch (IOException e) {
     
        }
    }
}
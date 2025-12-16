package repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import model.Medico;
import util.CaminhoCSV;

public class MedicoRepository {

    private static final String ARQUIVO = CaminhoCSV.medicos();

    private static String limpar(String texto) {
        if (texto == null) return "null";
        return texto.replace(";", ",").replace("\n", " ");
    }

    public static List<Medico> listar() {
        return listarTudo();
    }

    public static Medico buscarPorEmailEMatricula(String email, String matricula) {
        return listarTudo().stream()
                .filter(m -> m.getEmail().equalsIgnoreCase(email)
                        && m.getMatricula().equals(matricula))
                .findFirst()
                .orElse(null);
    }

    public static void salvar(Medico medico) {
        List<Medico> medicos = listarTudo();

        boolean existe = medicos.stream()
                .anyMatch(m -> m.getEmail().equalsIgnoreCase(medico.getEmail()));

        if (existe) return;

        medicos.add(medico);
        escrever(medicos);
    }

    private static List<Medico> listarTudo() {
        List<Medico> medicos = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {

            String cabecalho = br.readLine();
            if (cabecalho == null) {
                return medicos;
            }

            String linha;
            int linhaNum = 1;

            while ((linha = br.readLine()) != null) {
                linhaNum++;

                String[] d = linha.split(";", -1);
                if (d.length < 6) {
                    continue;
                }

                List<String> planos = null;
                if (!"null".equals(d[5]) && !d[5].isBlank()) {
                    planos = List.of(d[5].split(","));
                }

                Medico medico = new Medico(
                        d[0],
                        d[1],
                        d[2],
                        d[3],
                        d[4],
                        planos
                );

                medicos.add(medico);
            }

        } catch (Exception e) {
            
        }

        return medicos;
    }

    public static void atualizar(Medico medicoAtualizado) {
        List<Medico> medicos = listarTudo();

        for (Medico m : medicos) {
            if (m.getEmail().equalsIgnoreCase(medicoAtualizado.getEmail())) {
                m.setNome(medicoAtualizado.getNome());
                m.setSenha(medicoAtualizado.getSenha());
                m.setEspecialidade(medicoAtualizado.getEspecialidade());
            }
        }

        escrever(medicos);
    }

    private static void escrever(List<Medico> medicos) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO))) {

            pw.println("nome;especialidade;email;senha;matricula;planos");

            for (Medico m : medicos) {

                String planos = "null";
                if (m.getPlanosAtendidos() != null) {
                    planos = String.join(",", m.getPlanosAtendidos());
                }

                pw.println(
                        limpar(m.getNome()) + ";" +
                        limpar(m.getEspecialidade()) + ";" +
                        m.getEmail() + ";" +
                        limpar(m.getSenha()) + ";" +
                        m.getMatricula() + ";" +
                        planos
                );
            }

        } catch (IOException e) {
          
        }
    }
}

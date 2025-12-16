package repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import model.Paciente;
import util.CaminhoCSV;

public class PacienteRepository {

    private static final String ARQUIVO = CaminhoCSV.pacientes();

    private static String limpar(String texto) {
        if (texto == null) return "null";
        return texto.replace(";", ",").replace("\n", " ");
    }

    public static List<Paciente> listar() {
        return listarTudo();
    }

    public static Paciente buscarPorEmail(String email) {
        return listarTudo().stream()
                .filter(p -> p.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public static void salvar(Paciente paciente) {
        List<Paciente> pacientes = listarTudo();

        boolean existe = pacientes.stream()
                .anyMatch(p -> p.getEmail().equalsIgnoreCase(paciente.getEmail()));

        if (existe) {
        	return;
        }

        pacientes.add(paciente);
        escrever(pacientes);
    }

    private static List<Paciente> listarTudo() {
        List<Paciente> pacientes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {

            String cabecalho = br.readLine();
            if (cabecalho == null) return pacientes;

            String linha;
            int linhaNum = 1;

            while ((linha = br.readLine()) != null) {
                linhaNum++;

                String[] d = linha.split(";", -1);
                if (d.length < 6) {
                    System.err.println("Paciente inválido linha " + linhaNum + ": " + linha);
                    continue;
                }

                int idade;
                try {
                    idade = Integer.parseInt(d[1]);
                } catch (NumberFormatException e) {
                    System.err.println("Idade inválida linha " + linhaNum);
                    continue;
                }

                pacientes.add(new Paciente(
                        d[0],
                        idade,
                        Boolean.parseBoolean(d[2]),
                        "null".equals(d[3]) ? null : d[3],
                        d[4],
                        d[5]
                ));
            }

        } catch (Exception e) {
           
        }

        return pacientes;
    }

    public static void atualizar(Paciente atualizado) {
        List<Paciente> pacientes = listarTudo();

        for (Paciente p : pacientes) {
            if (p.getEmail().equalsIgnoreCase(atualizado.getEmail())) {
                p.setNome(atualizado.getNome());
                p.setIdade(atualizado.getIdade());
                p.setSenha(atualizado.getSenha());
                p.setPossuiPlano(atualizado.isPossuiPlano());
                p.setPlano(atualizado.getPlano());
            }
        }

        escrever(pacientes);
    }

    private static void escrever(List<Paciente> pacientes) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARQUIVO))) {

            pw.println("nome;idade;possuiPlano;plano;email;senha");

            for (Paciente p : pacientes) {

                String plano = "null";
                if (p.getPlano() != null) {
                    plano = limpar(p.getPlano());
                }

                pw.println(
                        limpar(p.getNome()) + ";" +
                        p.getIdade() + ";" +
                        p.isPossuiPlano() + ";" +
                        plano + ";" +
                        p.getEmail() + ";" +
                        limpar(p.getSenha())
                );
            }

        } catch (IOException e) {

        }
    }
}

package util;

import model.Paciente;
import model.Medico;

public class Sessao {

    private static Paciente pacienteLogado;
    private static Medico medicoLogado;

    public static void setPaciente(Paciente paciente) {
        pacienteLogado = paciente;
    }

    public static Paciente getPaciente() {
        return pacienteLogado;
    }

    public static void setMedico(Medico medico) {
        medicoLogado = medico;
    }

    public static Medico getMedico() {
        return medicoLogado;
    }

    public static void limpar() {
        pacienteLogado = null;
        medicoLogado = null;
    }
}
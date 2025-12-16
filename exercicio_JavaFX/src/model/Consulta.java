package model;

import java.time.LocalDate;

public class Consulta {

    private Paciente paciente;
    private Medico medico;
    private LocalDate data;

    private boolean emEspera;
    private boolean realizada;

    private String sintomas;
    private String tratamento;

    private Integer nota;     
    private String comentario;

    public Consulta(Paciente paciente, Medico medico, LocalDate data, boolean emEspera) {
        this.paciente = paciente;
        this.medico = medico;
        this.data = data;
        this.emEspera = emEspera;
        this.realizada = false;
    }

    public void realizar(String sintomas, String tratamento) {
        this.sintomas = sintomas;
        this.tratamento = tratamento;
        this.realizada = true;
    }

    public void avaliar(int nota, String comentario) {
        this.nota = nota;
        this.comentario = comentario;
    }

    public boolean isAvaliada() {
        return nota != null;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public LocalDate getData() {
        return data;
    }

    public boolean isEmEspera() {
        return emEspera;
    }

    public boolean isRealizada() {
        return realizada;
    }

    public String getSintomas() {
        return sintomas;
    }

    public String getTratamento() {
        return tratamento;
    }

    public Integer getNota() {
        return nota;
    }

    public String getComentario() {
        return comentario;
    }
    
    public void setEmEspera(boolean emEspera) {
        this.emEspera = emEspera;
    }
    
    public String resumoAvaliacao() {
        if (!isAvaliada()) {
            return "Sem avaliação";
        }
        return "⭐ " + nota + "/5";
    }

    @Override
    public String toString() {
        String status;

        if (realizada) {
            status = "✅ Realizada";
        } else {
            status = "🕒 Pendente";
        }

        return "Dr(a). " + medico.getNome()
                + " | " + data
                + " | " + status;
    }
}
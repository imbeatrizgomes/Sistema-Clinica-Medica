package model;

public class Avaliacao {

    private Medico medico;
    private int nota;
    private String comentario;

    public Avaliacao(Medico medico, int nota, String comentario) {
        this.medico = medico;
        this.nota = nota;
        this.comentario = comentario;
    }

    public Medico getMedico() {
        return medico;
    }

    public int getNota() {
        return nota;
    }

    public String getComentario() {
        return comentario;
    }
}

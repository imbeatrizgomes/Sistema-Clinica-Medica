package model;

public class Paciente extends Usuario {

    private int idade;
    private boolean possuiPlano;
    private String plano;
    //O boolean indica se o paciente tem convenio ou não, e String define qual é o convenio especifico

    public Paciente(String nome, int idade, boolean possuiPlano,
                    String plano, String email, String senha) {
        super(nome, email, senha);
        this.idade = idade;
        this.possuiPlano = possuiPlano;
        this.plano = plano;
    }

    public boolean isPossuiPlano() {
        return possuiPlano;
    }

    public String getPlano() {
        return plano;
    }

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}

	public void setPossuiPlano(boolean possuiPlano) {
		this.possuiPlano = possuiPlano;
	}

	public void setPlano(String plano) {
		this.plano = plano;
	}
}
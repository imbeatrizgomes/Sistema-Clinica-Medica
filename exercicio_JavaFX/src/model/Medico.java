package model;

import java.util.List;
import repository.ConsultaRepository;

public class Medico extends Usuario {

    private String especialidade;
    private String matricula;
    private List<String> planosAtendidos;

    public Medico(String nome, String especialidade, String email,
                  String senha, String matricula, List<String> planosAtendidos) {
        super(nome, email, senha);
        this.especialidade = especialidade;
        this.matricula = matricula;
        this.planosAtendidos = planosAtendidos;
    }

    public boolean atendeAlgumPlano() {
        return planosAtendidos != null && !planosAtendidos.isEmpty();
    }

	public String getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(String especialidade) {
		this.especialidade = especialidade;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public List<String> getPlanosAtendidos() {
		return planosAtendidos;
	}

	public void setPlanosAtendidos(List<String> planosAtendidos) {
		this.planosAtendidos = planosAtendidos;
	}

	@Override
	public String toString() {
	    double media = ConsultaRepository.calcularMediaDoMedico(this);

	    String avaliacao;
	    if (media <= 0) {
	        avaliacao = "Sem avaliação";
	    } else {
	        avaliacao = String.format("⭐ %.1f/5", media);
	    }

	    return getNome() + " - " + especialidade + " | " + avaliacao;
	}
}
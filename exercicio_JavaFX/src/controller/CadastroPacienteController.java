package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Paciente;
import repository.PacienteRepository;

public class CadastroPacienteController {

    @FXML
    private TextField campoNome;

    @FXML
    private TextField campoIdade;

    @FXML
    private TextField campoEmail;

    @FXML
    private PasswordField campoSenha;

    @FXML
    private ComboBox<String> campoPlano;

    @FXML
    public void initialize() {
        campoPlano.getItems().addAll("Hapvida", "Unimed", "Amil", "Bradesco Saúde", "Não");
    }

    public void cadastrar(ActionEvent event) {

        String nome = campoNome.getText().trim();
        String idadeTxt = campoIdade.getText().trim();
        String email = campoEmail.getText().trim();
        String senha = campoSenha.getText().trim();
        String planoSelecionado = campoPlano.getValue();

        if (nome.isEmpty() || idadeTxt.isEmpty() || email.isEmpty()
                || senha.isEmpty() || planoSelecionado == null) {
            alertaErro("Preencha todos os campos.");
            return;
        }

        int idade;
        try {
            idade = Integer.parseInt(idadeTxt);
        } catch (NumberFormatException e) {
            alertaErro("Idade inválida.");
            return;
        }

        if (PacienteRepository.buscarPorEmail(email) != null) {
            alertaErro("Já existe paciente com esse e-mail.");
            return;
        }

        boolean temPlano = !planoSelecionado.equalsIgnoreCase("Não");

        String plano = null;
        if (temPlano) {
    	 plano = planoSelecionado;
        }

        Paciente paciente = new Paciente(
        	nome,
        	idade,
        	temPlano,
        	plano,
        	email,
        	senha
        );

     PacienteRepository.salvar(paciente);

        alertaInfo("Paciente cadastrado com sucesso!");
        limparCampos();
    }

    public void voltar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml_novo/Menu.fxml")
            );

            Scene scene = new Scene(loader.load(), 800, 500);
            
            scene.getStylesheets().add(
                getClass().getResource("/css/Menu.css").toExternalForm()
            );

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setTitle("Sistema da Clínica");
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void limparCampos() {
        campoNome.clear();
        campoIdade.clear();
        campoEmail.clear();
        campoSenha.clear();
        campoPlano.setValue(null);
    }

    private void alertaErro(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }

    private void alertaInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
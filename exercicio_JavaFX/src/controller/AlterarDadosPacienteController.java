package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Paciente;
import repository.PacienteRepository;
import util.Sessao;

public class AlterarDadosPacienteController {

    @FXML
    private TextField campoEmail;

    @FXML
    private PasswordField campoSenha;

    private Paciente pacienteLogado;

    @FXML
    public void initialize() {

        pacienteLogado = Sessao.getPaciente();

        if (pacienteLogado == null) {
            alerta("Erro", "Sessão expirada. Faça login novamente.");
            return;
        }

        campoEmail.setText(pacienteLogado.getEmail());
        campoSenha.setText(""); 
    }

    public void salvar(ActionEvent event) {

        String novoEmail = campoEmail.getText().trim();
        String novaSenha = campoSenha.getText().trim();

        if (novoEmail.isEmpty() || novaSenha.isEmpty()) {
            alerta("Erro", "Informe o novo e-mail e a nova senha.");
            return;
        }

        pacienteLogado.setEmail(novoEmail);
        pacienteLogado.setSenha(novaSenha);

        PacienteRepository.atualizar(pacienteLogado);

        alerta("Sucesso", "Dados alterados com sucesso!");

        // forçar novo login por segurança
        Sessao.limpar();
        voltar(event);
    }

    public void voltar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml_novo/LoginPaciente.fxml")
            );

            Scene scene = new Scene(loader.load(), 800, 500);

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();

            stage.setTitle("Login do Paciente");
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void alerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
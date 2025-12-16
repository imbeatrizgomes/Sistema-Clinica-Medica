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

public class LoginPacienteController {

    @FXML
    private TextField campoEmail;

    @FXML
    private PasswordField campoSenha;

    public void entrar(ActionEvent event) {

        String email = campoEmail.getText().trim();
        String senha = campoSenha.getText().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            alerta(Alert.AlertType.ERROR, "Erro", "Informe email e senha.");
            return;
        }

        Paciente paciente = PacienteRepository.buscarPorEmail(email);

        if (paciente == null) {
            alerta(Alert.AlertType.ERROR, "Erro", "Paciente não cadastrado.");
            return;
        }

        if (!paciente.getSenha().equals(senha)) {
            alerta(Alert.AlertType.ERROR, "Erro", "Senha incorreta.");
            return;
        }

        Sessao.setPaciente(paciente);
        trocarTela(event, "/fxml_novo/MenuPaciente.fxml", "Portal do Paciente");
    }

    public void voltar(ActionEvent event) {
        trocarTela(event, "/fxml_novo/Menu.fxml", "Menu Principal");
    }

    private void trocarTela(ActionEvent event, String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Scene scene = new Scene(loader.load(), 800, 500);

            if (fxml.contains("Menu.fxml")) {
                scene.getStylesheets().add(
                    getClass().getResource("/css/Menu.css").toExternalForm()
                );
            }
            
            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();

            stage.setTitle(titulo);
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void alerta(Alert.AlertType tipo, String titulo, String msg) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
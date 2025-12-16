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
import model.Medico;
import repository.MedicoRepository;
import util.Sessao;

public class LoginMedicoController {

    @FXML
    private TextField campoEmail;

    @FXML
    private PasswordField campoSenha;

    @FXML
    private TextField campoMatricula;

    public void entrar(ActionEvent event) {

        String email = campoEmail.getText().trim();
        String senha = campoSenha.getText().trim();
        String matricula = campoMatricula.getText().trim();

        if (email.isEmpty() || senha.isEmpty() || matricula.isEmpty()) {
            alerta(Alert.AlertType.ERROR, "Erro", "Preencha todos os campos.");
            return;
        }

        Medico medico = MedicoRepository.buscarPorEmailEMatricula(email, matricula);

        if (medico == null) {
            alerta(Alert.AlertType.ERROR, "Erro", "Médico não encontrado.");
            return;
        }
        
        Sessao.setMedico(medico);

        if (!medico.getSenha().equals(senha)) {
            alerta(Alert.AlertType.ERROR, "Erro", "Senha incorreta.");
            return;
        }

        trocarTela(event, "/fxml_novo/MenuMedico.fxml", "Portal do Médico");
    }

    public void voltar(ActionEvent event) {
        trocarTela(event, "/fxml_novo/Menu.fxml", "Menu Principal");
    }

    private void trocarTela(ActionEvent event, String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Scene scene = new Scene(loader.load(), 800, 500);

            scene.getStylesheets().add(
                getClass().getResource("/css/Menu.css").toExternalForm()
            );

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

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
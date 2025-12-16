package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuController {

    public void abrirCadastroPaciente(ActionEvent event) {
        trocarTela(event, "CadastroPaciente.fxml", "Cadastro de Paciente");
    }

    public void abrirCadastroMedico(ActionEvent event) {
        trocarTela(event, "CadastroMedico.fxml", "Cadastro de Médico");
    }

    public void abrirLoginPaciente(ActionEvent event) {
        trocarTela(event, "LoginPaciente.fxml", "Login do Paciente");
    }

    public void abrirLoginMedico(ActionEvent event) {
        trocarTela(event, "LoginMedico.fxml", "Login do Médico");
    }

    private void trocarTela(ActionEvent event, String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml_novo/" + fxml)
            );

            Scene scene = new Scene(loader.load(), 500, 400);

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setTitle(titulo);
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuPacienteController {

    public void verMedicos(ActionEvent event) {
        trocarTela(event, "/fxml_novo/MedicosRede.fxml", "Médicos da Rede");
    }

    public void agendarConsulta(ActionEvent event) {
        trocarTela(event, "/fxml_novo/AgendarConsulta.fxml", "Agendar Consulta");
    }

    public void cancelarConsulta(ActionEvent event) {
        trocarTela(event, "/fxml_novo/CancelarConsulta.fxml", "Cancelar Consulta");
    }

    public void avaliarConsulta(ActionEvent event) {
        trocarTela(event, "/fxml_novo/AvaliarConsulta.fxml", "Avaliar Consulta");
    }

    public void minhasConsultas(ActionEvent event) {
        trocarTela(event, "/fxml_novo/MinhasConsultas.fxml", "Minhas Consultas");
    }

    public void alterarDados(ActionEvent event) {
        trocarTela(event, "/fxml_novo/AlterarPaciente.fxml", "Alterar Meus Dados");
    }

    public void voltarMenuPrincipal(ActionEvent event) {
        trocarTela(event, "/fxml_novo/Menu.fxml", "Menu Principal");
    }

    private void trocarTela(ActionEvent event, String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(fxml)
            );

            Scene scene = new Scene(loader.load(), 800, 500);

            scene.getStylesheets().add(
                    getClass().getResource("/css/Menu.css").toExternalForm()
            );

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();

            stage.setTitle(titulo);
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
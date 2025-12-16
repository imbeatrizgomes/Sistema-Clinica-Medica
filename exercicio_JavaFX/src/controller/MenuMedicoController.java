package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuMedicoController {

    public void abrirRealizarConsulta(ActionEvent event) {
        trocarTela(event, "/fxml_novo/RealizarConsulta.fxml", "Realizar Consulta");
    }
    
    public void abrirMeusDados(ActionEvent event) {
        trocarTela(event, "/fxml_novo/AlterarDadosMedico.fxml", "Meus Dados");
    }

    public void voltarMenuPrincipal(ActionEvent event) {
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
}
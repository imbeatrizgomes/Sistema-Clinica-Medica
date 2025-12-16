package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import model.Consulta;
import model.Paciente;
import repository.ConsultaRepository;
import util.Sessao;

public class CancelarConsultaController {

	@FXML
    private ComboBox<Consulta> comboConsultas;

    private Paciente pacienteLogado;

    public void initialize() {
        pacienteLogado = Sessao.getPaciente();
        comboConsultas.getItems().setAll(
                ConsultaRepository.listarPorPaciente(pacienteLogado)
        );
    }

    public void cancelar(ActionEvent event) {

        Consulta consulta = comboConsultas.getValue();

        if (consulta == null) {
            alerta("Erro", "Selecione uma consulta.");
            return;
        }

        ConsultaRepository.cancelarConsulta(consulta);

        alerta("Sucesso", "Consulta cancelada com sucesso!");
        comboConsultas.getItems().remove(consulta);
    }

    private void alerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }

    public void voltar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml_novo/MenuPaciente.fxml")
            );

            Scene scene = new Scene(loader.load(), 500, 400);

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setTitle("Portal do Paciente");
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
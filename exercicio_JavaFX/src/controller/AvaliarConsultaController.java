package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.Consulta;
import model.Paciente;
import repository.ConsultaRepository;
import util.Sessao;

public class AvaliarConsultaController {

    @FXML
    private ComboBox<Consulta> comboConsulta;

    @FXML
    private ComboBox<Integer> comboNota;

    @FXML
    private TextArea campoComentario;

    private Paciente pacienteLogado;

    @FXML
    public void initialize() {
        pacienteLogado = Sessao.getPaciente();

        comboNota.getItems().addAll(1, 2, 3, 4, 5);

        comboConsulta.getItems().setAll(
                ConsultaRepository.listarParaAvaliacao(pacienteLogado)
        );
    }

    @FXML
    public void avaliar(ActionEvent event) {

        Consulta consulta = comboConsulta.getValue();
        Integer nota = comboNota.getValue();
        String comentario = campoComentario.getText();

        if (consulta == null || nota == null) {
            alerta("Erro", "Selecione a consulta e a nota.");
            return;
        }

        consulta.avaliar(nota, comentario);
        ConsultaRepository.atualizarConsulta(consulta);

        alerta("Avaliação enviada", "Obrigado por avaliar o médico!");

        comboConsulta.getItems().remove(consulta);
        comboNota.setValue(null);
        campoComentario.clear();
    }

    public void voltar(ActionEvent event) {
        trocarTela(event, "/fxml_novo/MenuPaciente.fxml", "Portal do Paciente");
    }

    private void trocarTela(ActionEvent event, String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Scene scene = new Scene(loader.load(), 600, 400);

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene().getWindow();

            stage.setTitle(titulo);
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
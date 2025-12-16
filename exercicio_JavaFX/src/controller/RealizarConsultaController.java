package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.Consulta;
import model.Medico;
import repository.ConsultaRepository;
import util.Sessao;

public class RealizarConsultaController {

    @FXML
    private ComboBox<Consulta> comboConsultas;

    @FXML
    private TextArea campoSintomas;

    @FXML
    private TextArea campoTratamento;

    private Medico medicoLogado;

    @FXML
    public void initialize() {
        medicoLogado = Sessao.getMedico();

        comboConsultas.setCellFactory(cb -> new ListCell<Consulta>() {

            @Override
            protected void updateItem(Consulta consulta, boolean empty) {
                super.updateItem(consulta, empty);

                if (empty || consulta == null) {
                    setText(null);
                } else {

                    String status;

                    if (consulta.isEmEspera()) {
                        status = "Em espera";
                    } else {
                        status = "Confirmada";
                    }

                    setText(
                        consulta.getPaciente().getNome()
                        + " | " + consulta.getData()
                        + " | " + status
                    );
                }
            }
        });
        
        comboConsultas.setButtonCell(comboConsultas.getCellFactory().call(null));

        comboConsultas.getItems().setAll(
            ConsultaRepository.listarDoMedicoPendentes(medicoLogado)
        );
    }
    
    @FXML
    public void finalizarConsulta() {

        Consulta consulta = comboConsultas.getValue();
        String sintomas = campoSintomas.getText();
        String tratamento = campoTratamento.getText();

        if (consulta == null || sintomas.isBlank() || tratamento.isBlank()) {
            alerta("Erro", "Preencha todos os campos.");
            return;
        }

        consulta.realizar(sintomas, tratamento);
        ConsultaRepository.atualizarConsulta(consulta);

        if (!consulta.getPaciente().isPossuiPlano()) {
            alerta("Consulta finalizada",
                   "Paciente sem plano.\nGerar cobrança.");
        } else {
            alerta("Consulta finalizada",
                   "Consulta coberta pelo plano.");
        }

        comboConsultas.getItems().remove(consulta);
        campoSintomas.clear();
        campoTratamento.clear();
    }

    public void voltar(ActionEvent event) {
        trocarTela(event, "/fxml_novo/MenuMedico.fxml", "Portal do Médico");
    }

    private void trocarTela(ActionEvent event, String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Scene scene = new Scene(loader.load(), 800, 500);

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
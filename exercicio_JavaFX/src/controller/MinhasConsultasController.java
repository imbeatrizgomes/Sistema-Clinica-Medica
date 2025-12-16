package controller;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Consulta;
import model.Paciente;
import repository.ConsultaRepository;
import util.Sessao;

public class MinhasConsultasController {

    @FXML
    private ListView<Consulta> listaConsultas;

    private Paciente pacienteLogado;

    @FXML
    public void initialize() {
        pacienteLogado = Sessao.getPaciente();

        if (pacienteLogado != null) {
            configurarLista();
            carregarConsultas();
        }
    }

    private void configurarLista() {
        listaConsultas.setCellFactory(lv -> new ListCell<Consulta>() {

            @Override
            protected void updateItem(Consulta consulta, boolean empty) {
                super.updateItem(consulta, empty);

                if (empty || consulta == null) {
                    setText(null);
                } else {
                    String status;
                    
                    if (consulta.isRealizada()) {
                        status = "✅ Realizada";
                    } 
                    else if (consulta.isEmEspera()) {
                        status = "🕒 Em espera";
                    } 
                    else {
                        status = "📅 Confirmada";
                    }

                    setText(
                        "Dr(a). " + consulta.getMedico().getNome()
                        + " | " + consulta.getData()
                        + " | " + status
                    );
                }
            }
        });
    }

    private void carregarConsultas() {
        List<Consulta> consultas =
                ConsultaRepository.listarPorPaciente(pacienteLogado);

        listaConsultas.getItems().setAll(consultas);
    }

    public void voltar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml_novo/MenuPaciente.fxml")
            );

            Scene scene = new Scene(loader.load(), 600, 400);

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
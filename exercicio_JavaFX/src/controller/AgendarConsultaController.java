package controller;

import java.time.LocalDate;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import model.Consulta;
import model.Medico;
import model.Paciente;
import repository.ConsultaRepository;
import repository.MedicoRepository;
import util.Sessao;

public class AgendarConsultaController {

    @FXML
    private ComboBox<Medico> comboMedicos;

    @FXML
    private DatePicker campoData;

    private Paciente pacienteLogado;

    @FXML
    public void initialize() {
        pacienteLogado = Sessao.getPaciente();

        if (pacienteLogado == null) {
            alerta("Erro", "Sessão expirada.");
            return;
        }

        carregarMedicos();
    }

    private void carregarMedicos() {
        List<Medico> medicos = MedicoRepository.listar();

        if (pacienteLogado.isPossuiPlano()) {
            String planoPaciente = pacienteLogado.getPlano();

            medicos = medicos.stream()
                    .filter(m -> m.getPlanosAtendidos() != null
                            && m.getPlanosAtendidos().contains(planoPaciente))
                    .toList();
        } else {
            medicos = medicos.stream()
                    .filter(m -> !m.atendeAlgumPlano())
                    .toList();
        }

        comboMedicos.getItems().setAll(medicos);
    }

    @FXML
    public void agendar(ActionEvent event) {

        Medico medico = comboMedicos.getValue();
        LocalDate data = campoData.getValue();

        if (medico == null || data == null) {
            alerta("Erro", "Selecione médico e data.");
            return;
        }

        if (data.isBefore(LocalDate.now())) {
            alerta("Erro", "Não é possível agendar para datas passadas.");
            return;
        }

        long totalConfirmadas =
                ConsultaRepository.contarConsultasDoMedicoNoDia(medico, data);

        boolean emEspera = totalConfirmadas >= 3;

        Consulta consulta = new Consulta(
                pacienteLogado,
                medico,
                data,
                emEspera
        );

        ConsultaRepository.salvar(consulta);

        if (emEspera) {
            alerta("Lista de espera",
                    "Horários cheios para este dia.\nVocê entrou na lista de espera.");
        } else {
            alerta("Sucesso", "Consulta agendada com sucesso!");
        }

        // limpa campos
        comboMedicos.setValue(null);
        campoData.setValue(null);
    }

    public void voltar(ActionEvent event) {
        trocarTela(event, "/fxml_novo/MenuPaciente.fxml", "Portal do Paciente");
    }

    private void trocarTela(ActionEvent event, String fxml, String titulo) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource(fxml));

            Parent root = loader.load();
            Scene scene = new Scene(root, 800, 500);

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

    private void alerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}

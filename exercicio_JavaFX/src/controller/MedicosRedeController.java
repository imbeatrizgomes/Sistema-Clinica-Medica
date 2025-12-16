package controller;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Consulta;
import model.Medico;
import model.Paciente;
import repository.ConsultaRepository;
import repository.MedicoRepository;
import util.Sessao;

public class MedicosRedeController {

    @FXML
    private ListView<Medico> listaMedicos;

    private Paciente pacienteLogado;

    @FXML
    public void initialize() {
        pacienteLogado = Sessao.getPaciente();
        carregarMedicos();

        listaMedicos.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Medico medico, boolean empty) {
                super.updateItem(medico, empty);

                if (empty || medico == null) {
                    setText(null);
                } else {
                    double media = ConsultaRepository.calcularMediaDoMedico(medico);
                    int qtd = ConsultaRepository.listarAvaliacoesDoMedico(medico).size();

                    String avaliacaoTexto;
                    if (qtd == 0) {
                        avaliacaoTexto = "⭐ Sem avaliações";
                    } else {
                        avaliacaoTexto = String.format("⭐ %.1f (%d avaliações)", media, qtd);
                    }

                    setText(
                            medico.getNome() +
                            " - " + medico.getEspecialidade() +
                            "\n" + avaliacaoTexto
                    );
                }
            }
        });

        listaMedicos.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Medico medico = listaMedicos.getSelectionModel().getSelectedItem();
                if (medico != null) {
                    mostrarAvaliacoes(medico);
                }
            }
        });
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

        listaMedicos.getItems().setAll(medicos);
    }
    
    private void mostrarAvaliacoes(Medico medico) {

        List<Consulta> avaliacoes =
                ConsultaRepository.listarAvaliacoesDoMedico(medico);

        StringBuilder texto = new StringBuilder();

        if (avaliacoes.isEmpty()) {
            texto.append("Este médico ainda não possui avaliações.");
        } else {
            for (Consulta c : avaliacoes) {
                texto.append("⭐ ")
                     .append(c.getNota())
                     .append("/5\n");

                if (c.getComentario() != null && !c.getComentario().isBlank()) {
                    texto.append("Comentário: ")
                         .append(c.getComentario());
                } else {
                    texto.append("Sem comentário");
                }

                texto.append("\n\n");
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Avaliações do Médico");
        alert.setHeaderText(
                medico.getNome() + " - " + medico.getEspecialidade()
        );
        alert.setContentText(texto.toString());
        alert.showAndWait();
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
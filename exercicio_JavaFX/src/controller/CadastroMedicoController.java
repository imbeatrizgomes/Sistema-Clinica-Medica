package controller;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Medico;
import repository.MedicoRepository;

public class CadastroMedicoController {

    @FXML
    private TextField campoNome;
    
    @FXML
    private TextField campoEspecialidade;
    
    @FXML
    private TextField campoEmail;
    
    @FXML
    private PasswordField campoSenha;
    
    @FXML
    private TextField campoMatricula;
    
    @FXML
    private ComboBox<String> comboPlano;

    @FXML
    public void initialize() {
        comboPlano.getItems().addAll(
                "Hapvida",
                "Unimed",
                "Amil",
                "Bradesco Saúde",
                "Todos os planos",
                "Não atende no plano"
        );
    }

    public void cadastrar(ActionEvent event) {

        if (campoNome.getText().isEmpty() ||
            campoEspecialidade.getText().isEmpty() ||
            campoEmail.getText().isEmpty() ||
            campoSenha.getText().isEmpty() ||
            campoMatricula.getText().isEmpty() ||
            comboPlano.getValue() == null) {

            alerta("Erro", "Preencha todos os campos.");
            return;
        }

        List<String> planos = new ArrayList<>();

        String opcao = comboPlano.getValue();

        if (opcao.equals("Todos os planos")) {
            planos.add("Hapvida");
            planos.add("Unimed");
            planos.add("Amil");
            planos.add("Bradesco Saúde");
        } 
        else if (opcao.equals("Não atende no plano")) {

        } 
        else {
            planos.add(opcao);
        }

        Medico medico = new Medico(
            campoNome.getText(),
            campoEspecialidade.getText(),
            campoEmail.getText(),
            campoSenha.getText(),
            campoMatricula.getText(),
            planos
        );

        MedicoRepository.salvar(medico);

        alerta("Sucesso", "Médico cadastrado com sucesso!");
        limparCampos();
    }

    public void voltar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml_novo/Menu.fxml")
            );

            Scene scene = new Scene(loader.load(), 800, 500);
            
            scene.getStylesheets().add(
                getClass().getResource("/css/Menu.css").toExternalForm()
            );

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setTitle("Sistema da Clínica");
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void limparCampos() {
        campoNome.clear();
        campoEspecialidade.clear();
        campoEmail.clear();
        campoSenha.clear();
        campoMatricula.clear();
        comboPlano.setValue(null);
    }

    private void alerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Medico;
import repository.MedicoRepository;
import util.Sessao;

public class AlterarDadosMedicoController {

    @FXML
    private TextField campoNome;

    @FXML
    private TextField campoEspecialidade;

    @FXML
    private TextField campoEmail;

    @FXML
    private PasswordField campoSenha;

    private Medico medicoLogado;

    @FXML
    public void initialize() {
        medicoLogado = Sessao.getMedico();

        if (medicoLogado == null) {
            alerta("Erro", "Sessão expirada.");
            return;
        }

        campoNome.setText(medicoLogado.getNome());
        campoEspecialidade.setText(medicoLogado.getEspecialidade());
        campoEmail.setText(medicoLogado.getEmail());
        campoSenha.setText(medicoLogado.getSenha());
    }

    @FXML
    public void salvarAlteracoes(ActionEvent event) {

        String nome = campoNome.getText().trim();
        String especialidade = campoEspecialidade.getText().trim();
        String senha = campoSenha.getText().trim();

        if (nome.isEmpty() || especialidade.isEmpty() || senha.isEmpty()) {
            alerta("Erro", "Preencha todos os campos.");
            return;
        }

        medicoLogado.setNome(nome);
        medicoLogado.setEspecialidade(especialidade);
        medicoLogado.setSenha(senha);

        MedicoRepository.atualizar(medicoLogado);

        alerta("Sucesso", "Dados atualizados com sucesso!");
        irParaMenuPrincipal(event);
    }
    
    private void irParaMenuPrincipal(ActionEvent event) {
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

    private void alerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
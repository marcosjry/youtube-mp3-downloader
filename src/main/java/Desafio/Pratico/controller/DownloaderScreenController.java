package Desafio.Pratico.controller;

import Desafio.Pratico.Downloader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.security.InvalidParameterException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DownloaderScreenController {

    private Stage primaryStage;

    @FXML
    private Label tituloMusica;

    @FXML
    private TextField idMusica;

    @FXML
    private TextField pathToSaveDownload;

    @FXML
    private TextArea displayListaPrompt;

    @FXML
    private TextArea displayDownloadedPrompt;

    @FXML
    private Button botaoIniciarDownload;

    private Downloader downloader = new Downloader();

    @FXML
    public void initialize() {
        this.tituloMusica.setText("Downloader MP3 do Youtube");
        botaoIniciarDownload.setText("▶ Iniciar Download");
    }

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    @FXML
    private void adicionarMusica() throws InterruptedException {
        String musica = idMusica.getText();
        if(musica.isEmpty() || pathToSaveDownload.getText().isEmpty()) {
            this.showAlert("ID inválido/Vazio ou Caminho para salvar arquivos está vazio.", "Digite um ID ou um Caminho para salvar arquivos válido.");
            Thread.sleep(100);
            displayListaPrompt.clear();
        } else {
            downloader.adicionaMusica(musica);
            displayListaPrompt.setText(this.retornaPromptLista());
        }
    }

    @FXML
    private void iniciarDownload() {
        try {
            this.validaIniciarDownload();
            if(this.downloader.getUrl().isEmpty()) throw new InterruptedException("Erro durante o Download das Músicas");
            if(this.pathToSaveDownload.getText().isEmpty()) throw new InterruptedException("Caminho para salvar arquivos inválido.");

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<?> future = executorService.submit(() -> {
                this.downloader.getUrl().forEach(musica -> {
                    this.downloader.baixaMusica(musica, pathToSaveDownload.getText());
                    await(300);
                    String musicaBaixada = "ID Música [%i] = Baixada [✔]".replace("%i", String.valueOf(this.downloader.getUrl().indexOf(musica)));
                    displayDownloadedPrompt.appendText(musicaBaixada+"\n");
                });
                displayDownloadedPrompt.appendText("Download Completado.");
                this.downloader.getUrl().clear();
                this.paraAnimacao(executorService);
            });
            botaoIniciarDownload.setText("⏹ Parar Download");
            botaoIniciarDownload.setOnAction(e -> {
                future.cancel(true);
                this.paraAnimacao(executorService);
            });
        } catch (Exception e) {
           System.err.println(e.getMessage());
        }
    }

    private void validaIniciarDownload() {
        try {
            if(downloader.getUrl().isEmpty()) {
                this.showAlert("ID inválido/Vazio", "Digite um ID válido");
                throw new InvalidParameterException("Erro ao tentar Baixar Músicas");
            }
        } catch (InvalidParameterException e) {
            System.err.println(e.getMessage());
        }
    }

    private void paraAnimacao(ExecutorService executorService) {
        if (executorService != null) {
            executorService.shutdownNow();
        }

        Platform.runLater(() -> {
            botaoIniciarDownload.setText("▶ Iniciar Download");
            botaoIniciarDownload.setOnAction(e -> iniciarDownload());
        });
    }

    private void await(int value) {
        try {
            Thread.sleep(value);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void limparMusicas() {
        displayListaPrompt.clear();
    }

    private void showAlert(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private String retornaPromptLista() {
        var lista = this.downloader.getUrl();
        StringBuilder resultado = new StringBuilder();
        lista.forEach(musica -> {
            int indice = lista.indexOf(musica);
            String formato = "ID Música [%i] = %s".replace("%i", String.valueOf(indice))
                    .replace("%s", this.downloader.buscaNomeMusica(musica));
            resultado.append(formato).append("\n");
        });

        return resultado.toString();
    }

    private void setMusica() {}

}

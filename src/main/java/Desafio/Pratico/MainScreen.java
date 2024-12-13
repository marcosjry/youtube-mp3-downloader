package Desafio.Pratico;

import Desafio.Pratico.controller.DownloaderScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainScreen extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {

            // Carrega o FXML da tela da Musica
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/music.fxml")); // caminho correto do .fxml
            Parent root = loader.load();

            // Injeta o primaryStage no controlador da tela da Musica
            DownloaderScreenController musicController = loader.getController();
            musicController.setPrimaryStage(primaryStage);

            Scene scene = new Scene(root);
            primaryStage.setResizable(false);
            primaryStage.setMinWidth(750);
            primaryStage.setMinHeight(450);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Baixar Músicas");
            primaryStage.show();

        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }
}

module Desafio.Pratico {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.youtube.downloader;
    requires java.desktop;

    // Exporte o pacote contendo a classe Main
    exports Desafio.Pratico;
    opens Desafio.Pratico.controller to javafx.fxml;

    // Abra o pacote se for necessário acesso reflexivo pelo FXMLLoader
    opens Desafio.Pratico to javafx.fxml;
    exports Desafio.Pratico.controller;
}
package alissouali.amdalatjinadu.tp5.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TicTacToeController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
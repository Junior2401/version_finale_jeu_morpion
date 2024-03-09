package alissouali.amdalatjinadu.morpion.service;

import alissouali.amdalatjinadu.morpion.vue.TicTacToeApplication;
import alissouali.amdalatjinadu.morpion.model.TicTacToeModel;
import alissouali.amdalatjinadu.morpion.service.Owner;
import alissouali.amdalatjinadu.morpion.vue.TicTacToeApplication;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class TicTacToeSquare extends Button {
    private int line;
    private int col;
    private TicTacToeApplication parent;
    private static TicTacToeModel model = TicTacToeModel.getInstance();
    private ObjectProperty<Owner> ownerProperty = new SimpleObjectProperty<>(Owner.NONE);
    private BooleanProperty winnerProperty =  new SimpleBooleanProperty(false);

    public ObjectProperty<Owner> ownerProperty() {
        return ownerProperty;
    }

    public BooleanProperty winnerProperty() {
        return winnerProperty;
    }

    public void setWinnerProperty(){
        winnerProperty.set(true);
        this.setStyle("-fx-font-weight: bold; -fx-font-size: 20; -fx-background-color: red;");
    }

    public TicTacToeSquare(int line, int col, TicTacToeApplication parent){
        this.line = line;
        this.col = col;
        this.parent = parent;
        this.setPrefHeight(500);
        this.setPrefWidth(500);
        this.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,
                CornerRadii.EMPTY,
                Insets.EMPTY)));

        this.setOnMouseClicked(
                e -> {
                    if(model.legalMove(this.line, this.col).get()){
                        this.setText(model.getCurrentPlayerName());
                        ownerProperty.set(model.turnProperty().get());
                        this.parent.updateGameResume();
                        model.play(line, col);

                        //Si le jeu est terminé ?
                        if(model.gameOver().get()){
                            parent.updateGameSquareGride();
                        }

                    }else{
                        System.out.println("line : "+this.line+" col :  "+this.col);
                    }

                }
        );
        this.setOnMouseEntered(
                e -> {
                    if(!model.legalMove(this.line, this.col).get())
                    {
                        this.setStyle("-fx-background-color: red;");
                    }else{
                        this.setStyle("-fx-background-color: green;");
                    }

        });
        this.setOnMouseExited(
                e -> {
                    if(!winnerProperty.get())
                        this.setStyle("");
                }
        );
    }



}

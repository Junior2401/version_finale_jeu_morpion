package alissouali.amdalatjinadu.morpion.vue;

import alissouali.amdalatjinadu.morpion.model.*;
import alissouali.amdalatjinadu.morpion.service.Owner;
import alissouali.amdalatjinadu.morpion.service.*;
import alissouali.amdalatjinadu.morpion.model.TicTacToeModel;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TicTacToeApplication extends Application {
    private static TicTacToeModel model = TicTacToeModel.getInstance();
    private TicTacToeSquare[][] squares = new TicTacToeSquare[TicTacToeModel.getInstance().getBoardHeight()][TicTacToeModel.getInstance().getBoardWidth()];
    private List<TicTacToeSquare> listGameButton = new ArrayList<>();

    GridPane gride;
    private Label scoreXLabel = new Label(0+" Case cocher par X");
    private Label scoreOLabel = new Label(0+" Case cocher par O");
    private Label scoreEmptyLabel = new Label((model.getBoardWidth()*model.getBoardHeight())+" Cases libres");
    private Label winnerLabel = new Label("");

    //private Label message = new Label(model.getEndOfGameMessage().getValue());
    private Label message = new Label("");
    private Button restartButton = new Button("Restart");
    private GridPane gameButtonGridePane;





    @Override
    public void start(Stage primaryStage) throws IOException {
        BorderPane root = new BorderPane();
        GridPane gridPane = new GridPane();
        HBox scoreList = new HBox(scoreXLabel, scoreOLabel, scoreEmptyLabel);
        restartButton.setStyle("-fx-padding: 20px;");
        VBox bottomList = new VBox(restartButton, message, scoreList);
        bottomList.setMargin(restartButton, new Insets(20));
        scoreList.setSpacing(150);
        scoreList.setAlignment(Pos.CENTER);
        scoreList.fillHeightProperty();
        scoreList.setPrefSize(100, 100);
        message.setFont(javafx.scene.text.Font.font("Arial", 18));
        scoreXLabel.setFont(javafx.scene.text.Font.font("Arial", 16));
        scoreOLabel.setFont(javafx.scene.text.Font.font("Arial", 16));
        scoreEmptyLabel.setFont(javafx.scene.text.Font.font("Arial", 16));
        Insets margins = new Insets(20); // 20 pixels de marge de tous les côtés
        HBox.setMargin(scoreList, margins);
        bottomList.setMargin(message, new Insets(20));

        this.gameButtonGridePane= createGameSquareGride(model.getBoardWidth(), model.getBoardHeight());
        root.setCenter(this.gameButtonGridePane);
        root.setBottom(gridPane);
        root.setBottom(bottomList);

        restartButton.setOnAction(event -> {
            this.resetGame();
        });

        Scene scene = new Scene(root, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("TicTacToe");

        // Définir le style CSS pour la scène directement dans le code
        scene.getRoot().setStyle("-fx-background-color: lightblue;");

        // Obtenir la taille de l'écran
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        // Définir la taille de la scène à la taille de l'écran
        primaryStage.setScene(scene);
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());


        primaryStage.show();

    }


    private GridPane createGameSquareGride(int line, int col) {
        gride = new GridPane();
        gride.setGridLinesVisible(true);
        gride.setPadding(new javafx.geometry.Insets(30));
        gride.setMinHeight(150);
        gride.setMinWidth(150);
        gride.setStyle("-fx-background-color: white;");
        listGameButton = new ArrayList<>();
        // Créer une bordure avec une largeur de 3 pixels en noir
        BorderStroke borderStroke = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, BorderStroke.THICK);
        Border border = new Border(borderStroke);
       // gride.add(winnerLabel, 0, 3);
        for (int i=0; i<line; i++){
            for (int j=0; j<col; j++){
                TicTacToeSquare b = new TicTacToeSquare(i, j, this);
                listGameButton.add(b);
                gride.add(b,j, i);
            }
        }
        return gride;
    }


    public void updateGameSquareGride() {
        for (int i=0; i< model.getBoardWidth(); i++){
            for (int j=0; j< model.getBoardHeight(); j++){
                if(model.getWinningSquare(i,j).get()){
                    listGameButton.get(this.computeIndex(i,j)).setWinnerProperty();
                    System.out.print(" test : "+this.computeIndex(i,j)+" "+listGameButton.get(this.computeIndex(i,j)).getText());
                }
            }
        }

        updateGameResume();
        this.gride.setDisable(true);

    }


    /**
     * Reinit all the model data en IHM
     */

    private void resetGame(){
        model.restart();
        this.gameButtonGridePane.setDisable(false);
        for(TicTacToeSquare btn : listGameButton){
            btn.setText("");
            btn.setStyle("");
            btn.setOnMouseExited(
                    e -> {
                        btn.setStyle("");
                    }
            );
        }
        scoreXLabel.setText(0+" Case cocher par X");
        scoreXLabel.setStyle("-fx-background-color: green; -fx-padding: 10px;");
        scoreOLabel.setText(0+" Case cocher par O");
        scoreOLabel.setStyle("-fx-background-color: red; -fx-padding: 10px;");
        scoreEmptyLabel.setText((model.getBoardWidth()*model.getBoardHeight())+" Cases libres");


    }


    public void updateGameResume(){
        int scoreX = model.getScore(Owner.FIRST).intValue();
        int scoreO = model.getScore(Owner.SECOND).intValue();
        scoreXLabel.setText(scoreX+" Case cocher par X");
        scoreXLabel.setStyle("-fx-background-color: green; -fx-padding: 10px;");
        scoreOLabel.setText(scoreO+" Case cocher par X");
        scoreOLabel.setStyle("-fx-background-color: red; -fx-padding: 10px;");
        scoreEmptyLabel.setText(((model.getBoardWidth()*model.getBoardHeight())-(scoreX+scoreO))+" Cases libres");
        message.setText(model.getEndOfGameMessage().getValue());
    }

    private int computeIndex(int x, int y){
        return  x * model.getBoardWidth() + y;
    }
    private int computeIndex2(int x, int y){
        return  y * model.getBoardWidth() + x;
    }

    public static void main(String[] args) {
        /*Application app = new  TicTacToeApplication ();
        app.start(app);*/
        launch(args);
    }
}
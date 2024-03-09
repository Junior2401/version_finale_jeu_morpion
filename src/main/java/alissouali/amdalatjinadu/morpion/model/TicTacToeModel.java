package alissouali.amdalatjinadu.morpion.model;

import alissouali.amdalatjinadu.morpion.service.Owner;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.NumberExpression;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class TicTacToeModel {
    /**
     * Taille du plateau de jeu (pour être extensible).
     */
    private final static int BOARD_WIDTH = 3;
    private final static int BOARD_HEIGHT = 3;
    private int counter = 0;
    /**
     * Nombre de pièces alignés pour gagner (idem).
     */
    private final static int WINNING_COUNT = 3;

    /**
     * Joueur courant.
     */
    private final ObjectProperty<Owner> turn = new SimpleObjectProperty<>(Owner.FIRST);

    /**
     * Vainqueur du jeu, NONE si pas de vainqueur.
     */
    private final ObjectProperty<Owner> winner = new SimpleObjectProperty<>(Owner.NONE);

    /**
     * Plateau de jeu.
     */
    private final ObjectProperty<Owner>[][] board;

    /**
     * Positions gagnantes.
     */
     private final BooleanProperty[][] winningBoard;

     /**
     * Constructeur privé.
     */
     private TicTacToeModel() {
         board = new ObjectProperty[BOARD_WIDTH][BOARD_HEIGHT];
         winningBoard = new BooleanProperty[BOARD_WIDTH][BOARD_HEIGHT];
         this.initBoard();

     }


     public int getBoardWidth(){
         return this.BOARD_WIDTH;
     }

     public int getBoardHeight(){
         return this.BOARD_HEIGHT;
     }

    /**
     * @return la seule instance possible du jeu.
     */
    public static TicTacToeModel getInstance() {
        return TicTacToeModelHolder.INSTANCE;
    }

    /**
    * Classe interne selon le pattern singleton.
    */
    private static class TicTacToeModelHolder {
        private static final TicTacToeModel INSTANCE = new TicTacToeModel();
    }


    /**
     * Annuler la partie et rénitialiser le système
     */
    public void restart() {
        this.initBoard();
        this.winner.set(Owner.NONE);this.counter = 0;
        this.turn.set(Owner.FIRST);
    }


    public void reset(){
        for(int i=0; i<BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                this.board[i][j].set(Owner.NONE);
            }
        }
    }

    /**
     * Rétourne le joueur courant
     * @return
     */
    public final ObjectProperty<Owner> turnProperty() {
        return this.turn;
    }


    /**
     * Retourner la valeur des cases de coordonnée de paramètre suivant
     * @param row
     * @param column
     * @return
     */
    public final ObjectProperty<Owner> getSquare(int row, int column) {
        if(validSquare(row, column))
            return board[row][column];
        else
            throw new IndexOutOfBoundsException("Les coordonnées renseignées sont incorrect");
    }

    /**
     * Retourne la valeur de la case de coordonnée de paramètre suivant parmi les cases gagnantes
     * @param row
     * @param column
     * @return
     */
    public final BooleanProperty getWinningSquare(int row, int column) {
        if(validSquare(row, column))
            return winningBoard[row][column];
        else
            throw new IndexOutOfBoundsException("Les coordonnées renseignées sont incorrect");
    }

    /**
    * Cette fonction ne doit donner le bon résultat que si le jeu
    * est terminé. L’affichage peut être caché avant la fin du jeu.
    *
    * @return résultat du jeu sous forme de texte
    */
    public final StringExpression getEndOfGameMessage() {
        if(gameOver().get()){
            if(winner.get()==Owner.NONE)
                return Bindings.format("Game over. Match null");
            else
                return Bindings.format("Game over. le gagnant est le %s", winner.get() == Owner.FIRST ? "premier joueur" : "deuxième joueur");
        }else{
            return Bindings.format("");
        }
    }


    /**
     * Setter de winner
     * @param winner
     */
    public void setWinner(Owner winner) {
        this.winner.set(winner);
    }


    /**
     * Vérifier si aucun des deux joueurs n'a joué dans la case de coordonnée suivant
     * @param row
     * @param column
     * @return
     */
    public boolean validSquare(int row, int column) {
        if(row>=0 && column>=0 && row<BOARD_WIDTH && column<BOARD_HEIGHT)
            return true;
        else
            return  false;
    }


    /**
     * Passer la main à son adversaire de jeu);
    }
     */
    public void nextPlayer() {
        turn.set(turn.get().opposite());
    }

    /**
     * Jouer dans la case (row, column) quand c’est possible.
     */

    public void play(int row, int column) {
        if(validSquare(row, column)){
            if(this.legalMove(row, column).get()){
                this.board[row][column].set(turn.get());
                this.counter++;
                this.gameOver();
            }
        }

    }

    /**
     * @return true s’il est possible de jouer dans la case
            * c’est-à-dire la case est libre et le jeu n’est pas terminé
     */

        public BooleanBinding legalMove(int row, int column) {
            return  new BooleanBinding() {
                @Override
                protected boolean computeValue() {
                    if(validSquare(row, column))
                    {
                        if(board[row][column].get()==Owner.NONE)
                            return  true;
                        else
                            return  false;
                    }
                    else
                        throw new IndexOutOfBoundsException("Les coordonnées renseignées sont incorrect");
                }
            };
        }


    /**
     *retourne le nombre coup jouer par le joueur owner
     * @param owner
     * @return
     */
    public NumberExpression getScore(Owner owner) {
        SimpleIntegerProperty cpt = new SimpleIntegerProperty(0);

        for(int i=0; i<BOARD_WIDTH; i++){
            for(int j=0; j<BOARD_HEIGHT; j++){
                if(board[i][j].get()==owner)
                    cpt.set(cpt.get() + 1);
            }
        }
        return cpt;
    }

    /**
     * Vérifier s'il y a un gagnant sur les lignes
     * @return Owner, le gagnant trouvé ou  Owner.NONE s'il n'y a pas eu de gagnant
     */
    public List<Pair<Integer, Integer>> checkLineWin(){
        List<Pair<Integer, Integer>> list = new ArrayList<>();
        for(int i = 0; i < BOARD_WIDTH; i++) {
            if (board[i][0].get() != Owner.NONE)
            {
                Pair<Integer, Integer> p0 = new Pair(i,0);
                list.add(p0);

                for (int j = 1; j < BOARD_HEIGHT; j++) {
                    if (board[i][j].get() != board[i][0].get()) {
                        list.clear();
                        break;
                    }else{
                        Pair<Integer, Integer> p = new Pair(i,j);
                        list.add(p);
                    }
                }
                if(list.size()>1)
                    return list;
            }
        }
        return  list;
    }

    /**
     * Vérifier s'il y a un gagnant sur les colonnes
     * @return Owner, le gagnant trouvé ou  Owner.NONE s'il n'y a pas eu de gagnant
     */
    public List<Pair<Integer, Integer>> checkColumnWin(){
        List<Pair<Integer, Integer>> list = new ArrayList<>();
        for (int j = 0; j < BOARD_HEIGHT; j++) {
            if (board[0][j].get() != Owner.NONE) {
                Pair<Integer, Integer> p0 = new Pair(0,j);
                list.add(p0);
                for (int i = 1; i < BOARD_WIDTH; i++) {
                    if (board[i][j].get() != board[0][j].get()) {
                        list.clear();
                        break;
                    }else{
                        Pair<Integer, Integer> p = new Pair(i,j);
                        list.add(p);
                    }
                }
                if(list.size()>1)
                    return list;
            }
        }
        return  list;
    }

    /**
     * Vérifier s'il y a un gagnant sur les colonnes
     * @return Owner, le gagnant trouvé ou  Owner.NONE s'il n'y a pas eu de gagnant
     */
    public List<Pair<Integer, Integer>> checkDiagonalPrincipalWin(){
        List<Pair<Integer, Integer>> list = new ArrayList<>();
        if (BOARD_WIDTH == BOARD_HEIGHT && board[0][0].get() != Owner.NONE) {
            Pair<Integer, Integer> p0 = new Pair(0,0);
            list.add(p0);
            for (int i = 1; i < BOARD_WIDTH; i++) {
                if (board[i][i].get() != board[0][0].get()) {
                    list.clear();
                    break;
                }else{
                    Pair<Integer, Integer> p = new Pair(i,i);
                    list.add(p);
                }
            }
            if (list.size()>1) {
                return list;
            }
        }
        return  list;
    }

    /**
     * Vérifier s'il y a un gagnant sur les colonnes
     * @return Owner, le gagnant trouvé ou  Owner.NONE s'il n'y a pas eu de gagnant
     */
    public List<Pair<Integer, Integer>> checkDiagonalInverseWin(){
        List<Pair<Integer, Integer>> list = new ArrayList<>();
        if (BOARD_WIDTH == BOARD_HEIGHT && board[0][BOARD_WIDTH - 1].get() != Owner.NONE) {
            Pair<Integer, Integer> p0 = new Pair(0,BOARD_WIDTH-1);
            list.add(p0);
            for(int i = 1; i < BOARD_WIDTH; i++){
                if(board[i][BOARD_WIDTH - 1 - i].get() != board[0][BOARD_WIDTH - 1].get()){
                    list.clear();
                    break;
                }else{
                    Pair<Integer, Integer> p = new Pair(i,BOARD_WIDTH - 1 - i);
                    list.add(p);
                }
            }
            if(list.size()>1)
            {
                return list;
            }
        }
        // Aucun gagnant
        return  list;
    }

    /**
     * @return true si le jeu est terminé
     * (soit un joueur a gagné, soit il n’y a plus de cases à jouer)
     */
    public BooleanBinding gameOver() {
        if(!this.interpretCheckResult(checkLineWin())){
            if(!this.interpretCheckResult(checkColumnWin())){
                if(!this.interpretCheckResult(checkDiagonalPrincipalWin())){
                    if(!this.interpretCheckResult(checkDiagonalInverseWin())){
                        /**
                         * Aucun gagnant trouver
                         * Nous allons verifier s'il est toujours possible de continuer
                         */
                        if(this.counter<BOARD_WIDTH*BOARD_HEIGHT){
                            return new BooleanBinding() {
                                @Override
                                protected boolean computeValue() {
                                    return false;
                                }
                            };
                        }

                    }
                }
            }
        }

        return new BooleanBinding() {
            @Override
            protected boolean computeValue() {
               /// printTable(winningBoard);
                return true;
            }
        };

       /* new BooleanBinding() {
            @Override
            protected boolean computeValue() {
                return true;
            }
        };*/
    }

    /**
     * Récuperer le nom du joueur actuel ; X = Owner.FIRST et O = Owner.SECOND, Owner.NONE =
     *
     * * @return le nom traduire
     */
    public String getCurrentPlayerName(){
        return this.turn.get()==Owner.FIRST ? "X" : (this.turn.get() ==Owner.SECOND ? "O" : "");
    }
    /**
     * Inteprete le resultat de la vérification en mettre à jour les attribut d'état
     * @param list, contient la liste eventuelle des positions gagnantes
     * @return true le résutat est passant (il y a de gagnant) ou false sinon (pas de gagnant)
     */
    private boolean interpretCheckResult(List<Pair<Integer, Integer>> list){
        if(list.size() > 0){
            int ligne = list.get(0).getKey();
            int colonne = list.get(0).getValue();
            Owner current_winner = this.board[ligne][colonne].get();
            this.winner.set(current_winner);
            System.out.println("Succes =>"+list.size());
            for(Pair<Integer, Integer> pair :  list){
                System.out.println("Inteprete=>"+pair.getKey()+" "+pair.getValue());
                this.winningBoard[pair.getKey()][pair.getValue()] = new SimpleBooleanProperty(true);
            }
            return  true;
        }else{
            return false;
        }
    }

    private void printTable(BooleanProperty[][] winningBoard) {
        for(int i=0; i<BOARD_WIDTH; i++){
            for(int j=0; j<BOARD_HEIGHT; j++){
                System.out.print(winningBoard[i][j].get()+" ;");
            }
        }
    }

    /**
     * Initializer le board et le winningBoard
     */
    private void initBoard(){
        for(int i=0; i<BOARD_WIDTH; i++){
            for(int j=0; j<BOARD_HEIGHT; j++){
                board[i][j] = new SimpleObjectProperty<>(Owner.NONE);
                winningBoard[i][j] = new SimpleBooleanProperty(false);
            }
        }
    }

}



import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

public class Game {
    private LinkedList<Card> gameDeck;
    private Stack<Card> auxDeck;
    private Card[][] gameField;
    private Card[][] finalDecks;

    public Game(){
        Deck cards = new Deck();
        auxDeck = new Stack();
        gameField = new Card[20][7];
        finalDecks = new Card[13][4];
        gameDeck = cards.getDeck();
        Collections.shuffle(gameDeck);
        startGame();
    }
/*
    private void startGame2() {
        this.gameField[0][0] = new Card(Card.Seed.PICCHE, Card.Value.K,13, Card.Color.NERO);
        this.gameField[0][0].setHidden(false);
        this.gameField[1][0] = new Card(Card.Seed.QUADRI, Card.Value.Q,12, Card.Color.ROSSO);
        this.gameField[1][0].setHidden(false);
        this.gameField[2][0] = new Card(Card.Seed.FIORI, Card.Value.J,11, Card.Color.NERO);
        this.gameField[2][0].setHidden(false);
        this.gameField[3][0] = new Card(Card.Seed.QUADRI, Card.Value.DIECI,10, Card.Color.ROSSO);
        this.gameField[3][0].setHidden(false);
        this.gameField[0][1] = new Card(Card.Seed.FIORI, Card.Value.K,13, Card.Color.NERO);
        this.gameField[0][1].setHidden(false);
        this.gameField[0][2] = new Card(Card.Seed.PICCHE, Card.Value.Q,11, Card.Color.NERO);
        this.gameField[0][2].setHidden(false);
        this.gameField[0][3] = new Card(Card.Seed.PICCHE, Card.Value.J,1, Card.Color.NERO);
        this.gameField[0][3].setHidden(false);
        this.gameField[0][4] = new Card(Card.Seed.PICCHE, Card.Value.DUE,2, Card.Color.NERO);
        this.gameField[0][4].setHidden(false);
    }

 */
    //Metodo che inizializza la griglia a inizio gioco
    private void startGame(){
        for (int i = 0; i <= 6; i++){
            for (int j = i; j <= 6; j++){
                this.gameField[i][j] = this.gameDeck.pop();
                if (j==i){
                    this.gameField[i][j].setHidden(false);
                }
            }
        }
    }
    //Mostra se presente l'ultima carta del deck ausiliario
    public void showCard(){
        pickCard();
        if(!auxDeck.isEmpty()) {System.out.println("Ultima carta pescata: "+auxDeck.peek());} //stampala
    }
    //Pesca una carta dal deck principale al deck ausiliario
    private void pickCard() {
        Iterator<Card> iterator = auxDeck.iterator();
        if (gameDeck.isEmpty()) {
            while (iterator.hasNext()) {
                Card c = iterator.next();
                c.setHidden(true);
                gameDeck.addLast(c);
            }
            auxDeck.clear();
        }
        auxDeck.push(gameDeck.pop()); //togli una carta dal mazzo principale e mettila nell'ausiliario
        auxDeck.peek().setHidden(false);
    }

    //Metodo che ritorna true se la carta da muovere sarà posizionata sopra una carta con colore diverso e valore maggiore di 1
    private boolean canMoveCard(Card c, int destRow, int destCol) throws ArrayIndexOutOfBoundsException{
        if((destRow==0 && c.getValues() != Card.Value.K) || (destCol>6 || destCol<0) || (destRow>20 || destRow<0) ){
            return false;
        }else return destRow == 0 && c.getValues() == Card.Value.K
                ||
                this.gameField[destRow - 1][destCol] != null &&
                c != null &&
                c.getColor() != this.gameField[destRow - 1][destCol].getColor() &&
                c.getRealCardValue() == this.gameField[destRow - 1][destCol].getRealCardValue() - 1 &&
                this.gameField[destRow][destCol] == null;
    }

    //Dal deck ausiliario al campo di gioco
    public void moveCardFromDeck(int destRow, int destCol) throws ArrayIndexOutOfBoundsException{
        if (!this.auxDeck.isEmpty() && canMoveCard(auxDeck.peek(), destRow, destCol)){
            this.gameField[destRow][destCol] = auxDeck.pop();
        }else System.out.println("Mossa non valida");
    }

    //Muove le carte dentro il campo
    public void moveCards(int rowStart, int colStart, int rowDest, int colDest){
        if ((rowStart >= 0 && rowStart < this.gameField.length)
                && (colStart >= 0 && colStart < this.gameField[rowStart].length)){
            if (canMoveCard(this.gameField[rowStart][colStart], rowDest, colDest)){//soloto controllo tra la  posizione originale e la finake
                if (rowStart > 0) {//se non siamo nella prima riga
                    this.gameField[rowStart - 1][colStart].setHidden(false);//imposta la carta precedente come visibile
                }do {//continua a spostare le carte finchè non trovi uno slot vuoto
                    this.gameField[rowDest][colDest]=this.gameField[rowStart][colStart];
                    this.gameField[rowStart][colStart]=null;//libera gli slot dove avevamo le carte
                    rowStart ++;
                    rowDest ++;
                }while(this.gameField[rowStart][colStart]!=null);
            }else System.out.println("Mossa non valida");
        }else System.out.println("Hai scelto qualcosa di sbagliato!");
    }
    //Trova una riga libera dentro una colonna del deck finale
    private int whichRow(int destCol){
        int destRow=0;
        if (destCol > 0 && destCol < this.finalDecks[destRow].length) {
            for (destRow = 0; destRow < this.finalDecks.length; destRow++) {
                if (this.finalDecks[destRow][destCol] == null) {
                    return destRow;
                }
            }
        }
        return destRow=this.finalDecks.length;
    }
    //Metodo di controllo se può mettere una carta all'interno del deck finale
    private boolean canMoveToFinalGrid(Card c, int destCol) throws ArrayIndexOutOfBoundsException {
        if ((destCol > 4 || destCol < 0) || (this.finalDecks[0][destCol] == null && c.getValues() != Card.Value.A))
        {
            return false;
        }else { int destRow=whichRow(destCol);
                System.out.println(destRow);
                if (destRow == 0 && c.getValues() == Card.Value.A
                    ||
                    this.finalDecks[destRow][destCol] == null &&
                    //Aggiunta la condizione che nella riga zero deve stare solo l'asso
                    !c.getHidden() &&        // Qui non ho trovato molte informazioni in alcune versioni devi mettere il RE e in altri si mette la prima carta pescata                this.finalDecks[destRow - 1][destCol] != null &&
                    //c != null &&
                    c.getColor() == this.finalDecks[destRow - 1][destCol].getColor() &&
                    c.getSeeds() == this.finalDecks[destRow - 1][destCol].getSeeds() &&
                    c.getRealCardValue() == this.finalDecks[destRow - 1][destCol].getRealCardValue() + 1 &&
                    this.finalDecks[destRow][destCol] == null) {
                    return true;
                }
            }
        return false;
    }
    //Dalla carta pescata al deck finale
    public void moveFromDeckToFinalGrid(int destCol) throws ArrayIndexOutOfBoundsException{
        if (canMoveToFinalGrid(auxDeck.peek(), destCol)){
            this.finalDecks[whichRow(destCol)][destCol] = auxDeck.pop();
        }else System.out.println("Mossa non valida");
    }
    //Dal campo alla griglia finale
    public void moveCardToFinalGrid(int rowStart, int colStart, int colDest){
        if ((rowStart >= 0 && rowStart < this.gameField.length)
                && (colStart >= 0 && colStart < this.gameField[rowStart].length)){
               int rowDest=whichRow(colDest);
            if (canMoveToFinalGrid(this.gameField[rowStart][colStart], colDest)){//soloto controllo tra la  posizione originale e la finake
                if (rowStart > 0) {//se non siamo nella prima riga
                    this.gameField[rowStart - 1][colStart].setHidden(false);//imposta la carta precedente come visibile
                }
                do {//continua a spostare le carte finchè non trovi uno slot vuoto
                    this.finalDecks[rowDest][colDest]=this.gameField[rowStart][colStart];
                    this.gameField[rowStart][colStart]=null;//libera gli slot dove avevamo le carte
                    rowStart ++;
                    rowDest ++;
                }while(this.gameField[rowStart][colStart]!=null);
            }else System.out.println("Mossa non valida");
        }else System.out.println("Hai scelto qualcosa di sbagliato!");
    }
    //Metodo che controlla se hai vinto o meno
    public boolean win(){
        for (Card[] finalDeck : this.finalDecks) {
            for (Card card : finalDeck) {
                if (card != null) {
                    System.out.println("yuppi");
                    return true;
                }
            }
        }return false;
    }

    //Bisogna creare il "posto" per gli assi e quindi i 4 mazzi ausiliari per vincere
    public String toString() {
        String result = "";
        for (int i = 0; i < this.gameField.length; i++) {
            result += "\n[";
            for (int j = 0; j < this.gameField[i].length; j++) {
                String value = this.gameField[i][j] != null ?
                        String.valueOf(this.gameField[i][j]) /*"card"*/ : "               ";
                if (i<10){
                    result += "[" + " "+ i + "-" + j + " " + value + "]";
                }else
                    result += "[" + i + "-" + j + " " + value + "]";
            }
            result += "]";
        }
        String finalDecks = "";
        for (int i = 0; i < this.finalDecks.length; i++) {
            finalDecks += "\n[";
            for (int j = 0; j < this.finalDecks[i].length; j++) {
                String value = this.finalDecks[i][j] != null ?
                        String.valueOf(this.finalDecks[i][j]) /*"card"*/ : "               ";
                if (i<10){
                    finalDecks += "[" + " "+ i + "-" + j + " " + value + "]";
                }else
                finalDecks += "[" + i + "-" + j + " " + value + "]";
            }
            finalDecks += "]";
        }
        return finalDecks + "\n" + result;
    }
    /*
    public String toString2() {

        String result = "";
        for (int i = 0; i < this.gameField.length; i++) {
            result += "\n[";
            for (int j = 0; j < this.gameField[i].length; j++) {
                String value = this.gameField[i][j] != null ?
                        String.valueOf(this.gameField[i][j])  : "               ";
                if (i < 10) {
                    result += "[" + " " + i + "-" + j + " " + value + "]";

                } else
                    result += "[" + i + "-" + j + " " + value + "]";

                result += "]";
            }
        }
        String finalDecks = "";
        for (int i = 0; i < this.finalDecks.length; i++) {
            finalDecks += "\n[";
            for (int j = 0; j < this.finalDecks[i].length; j++) {
                String value = this.finalDecks[i][j] != null ?
                        String.valueOf(this.finalDecks[i][j])  : "              ";
                if (i<10){
                    finalDecks += "[" + " "+ i + "-" + j + " " + value + "]";
                }else
                    finalDecks += "[" + i + "-" + j + " " + value + "]";
            }
            finalDecks += "]";
        }
        return finalDecks + "\n" + result;
    }
    */
}
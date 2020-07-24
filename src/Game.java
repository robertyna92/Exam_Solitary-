import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

public class Game {
    private LinkedList<Card> gameDeck;
    private Stack<Card> auxDeck;
    private Card[][] gameField;
    private Card[][] finalDecks;

    public Game() {
        Deck cards = new Deck();
        auxDeck = new Stack();
        gameField = new Card[20][7];
        finalDecks = new Card[13][4];
        gameDeck = cards.getDeck();
        Collections.shuffle(gameDeck);
        startGame();
    }

    private void startGame2() {
        this.finalDecks[0][0]=new Card(Card.Seed.PICCH, Card.Value.A,1, Card.Color.NERO);
        this.finalDecks[0][0].setHidden(false);
        this.finalDecks[1][0]=new Card(Card.Seed.PICCH, Card.Value.DUE,2, Card.Color.NERO);
        this.finalDecks[1][0].setHidden(false);
        this.finalDecks[2][0]=new Card(Card.Seed.PICCH, Card.Value.TRE,3, Card.Color.NERO);
        this.finalDecks[2][0].setHidden(false);
        this.finalDecks[3][0]=new Card(Card.Seed.PICCH, Card.Value.QUATTRO,4, Card.Color.NERO);
        this.finalDecks[3][0].setHidden(false);
        this.finalDecks[4][0]=new Card(Card.Seed.PICCH, Card.Value.CINQUE,5, Card.Color.NERO);
        this.finalDecks[4][0].setHidden(false);
        this.finalDecks[5][0]=new Card(Card.Seed.PICCH, Card.Value.SEI,6, Card.Color.NERO);
        this.finalDecks[5][0].setHidden(false);
        this.finalDecks[6][0]=new Card(Card.Seed.PICCH, Card.Value.SETTE,7, Card.Color.NERO);
        this.finalDecks[6][0].setHidden(false);
        this.finalDecks[7][0]=new Card(Card.Seed.PICCH, Card.Value.OTTO,8, Card.Color.NERO);
        this.finalDecks[7][0].setHidden(false);
        this.finalDecks[8][0]=new Card(Card.Seed.PICCH, Card.Value.Nove,9, Card.Color.NERO);
        this.finalDecks[8][0].setHidden(false);
        this.finalDecks[9][0]=new Card(Card.Seed.PICCH, Card.Value.DIECI,10, Card.Color.NERO);
        this.finalDecks[9][0].setHidden(false);
        this.finalDecks[10][0]=new Card(Card.Seed.PICCH, Card.Value.J,11, Card.Color.NERO);
        this.finalDecks[10][0].setHidden(false);
        this.finalDecks[11][0]=new Card(Card.Seed.PICCH, Card.Value.Q,12, Card.Color.NERO);
        this.finalDecks[11][0].setHidden(false);
        this.finalDecks[12][0]=new Card(Card.Seed.PICCH, Card.Value.K,13, Card.Color.NERO);
        this.finalDecks[12][0].setHidden(false);

    }
    //Metodo che inizializza la griglia a inizio gioco
    private void startGame() {
        for (int i = 0; i <= 6; i++) {
            for (int j = i; j <= 6; j++) {
                this.gameField[i][j] = this.gameDeck.pop();
                if (j == i) {
                    this.gameField[i][j].setHidden(false);
                }
            }
        }
    }
    //Mostra se presente l'ultima carta del deck ausiliario
    private String showCard() {
        if (!auxDeck.isEmpty()) {
            return "Ultima carta pescata: " + auxDeck.peek();
        }
        return "";
    }
    //Pesca una carta dal deck principale al deck ausiliario
    public void pickCard() {
        Iterator<Card> iterator = auxDeck.iterator(); //Uso iteratore per riempire il gamedeck quando vuoto
        if (gameDeck.isEmpty()) {
            while (iterator.hasNext()) {
                Card c = iterator.next();
                c.setHidden(true);
                gameDeck.addLast(c);
            }
            auxDeck.clear();
        }
        auxDeck.push(gameDeck.pop()); //Togli una carta dal mazzo principale e mettila nell'ausiliario
        auxDeck.peek().setHidden(false); //Imposta la visibilità della carta a visibile
    }
    //Metodo che ritorna true se la carta da muovere sarà posizionata sopra una carta con colore diverso e valore maggiore di 1
    private boolean canMoveCard(Card c, int destRow, int destCol) throws ArrayIndexOutOfBoundsException {
        if (c == null || (destCol > 6 || destCol < 0) || (destRow > 20 || destRow < 0)) {
            return false;
        } else return (destRow == 0 &&
                c.getValues() == Card.Value.K &&
                this.gameField[destRow][destCol]==null)
                ||
                (destRow !=0 &&
                        this.gameField[destRow - 1][destCol] != null &&
                        !c.getHidden() &&
                        c.getColor() != this.gameField[destRow - 1][destCol].getColor() &&
                        c.getRealCardValue() == this.gameField[destRow - 1][destCol].getRealCardValue() - 1 &&
                        this.gameField[destRow][destCol] == null);
    }
    //Dal deck ausiliario al campo di gioco
    public void moveCardFromDeck(int destRow, int destCol){
        if (!this.auxDeck.isEmpty() && canMoveCard(auxDeck.peek(), destRow, destCol)) { //Controlla che il deck non sia vuoto e che la mossa sia valida
            this.gameField[destRow][destCol] = auxDeck.pop();
        } else System.out.println("Mossa non valida");
    }
    //Muove le carte dentro il campo
    public void moveCards(int rowStart, int colStart, int rowDest, int colDest) {
        if ( (rowStart >= 0 && rowStart < this.gameField.length) //Controlli di out of bounds
                && (colStart >= 0 && colStart < this.gameField[rowStart].length)
                && (canMoveCard(this.gameField[rowStart][colStart], rowDest, colDest) ) ) { //solito controllo tra la  posizione originale e la finale
            if (rowStart > 0) {//se non siamo nella prima riga
                this.gameField[rowStart - 1][colStart].setHidden(false);//imposta la carta precedente come visibile
            }
            do { //Continua a spostare le carte finchè non trovi uno slot vuoto
                this.gameField[rowDest][colDest] = this.gameField[rowStart][colStart];
                this.gameField[rowStart][colStart] = null; //Libera gli slot dove avevamo le carte
                rowStart++; //Aumenta entrambe le row
                rowDest++;
            } while (this.gameField[rowStart][colStart] != null);
        } else System.out.println("Mossa non valida");
    }
    //Trova una riga libera dentro una colonna del deck finale
    private int whichRow(int destCol) {
        int destRow = 0;
        if (destCol >= 0 && destCol < this.finalDecks[destRow].length) {
            for (destRow = 0; destRow < this.finalDecks.length; destRow++) {
                if (this.finalDecks[destRow][destCol] == null) {
                    return destRow;
                }
            }
        }
        return this.finalDecks.length-1;
    }
    //Metodo di controllo se può mettere una carta all'interno del deck finale
    private boolean canMoveToFinalGrid(Card c, int destCol){
        int destRow;
        if (c == null || (destCol > 3 || destCol < 0)) {
            return false;
        }   destRow = whichRow(destCol);
        System.out.println(destRow);
        return ((destRow == 0 && c.getValues() == Card.Value.A)
                ||
                (destRow!=0 &&
                        this.finalDecks[destRow][destCol] == null &&
                        !c.getHidden() &&
                        c.getColor() == this.finalDecks[destRow - 1][destCol].getColor() &&
                        c.getSeeds() == this.finalDecks[destRow - 1][destCol].getSeeds() &&
                        c.getRealCardValue() == this.finalDecks[destRow - 1][destCol].getRealCardValue() + 1));
    }
    //Dalla carta pescata al deck finale
    public void moveFromDeckToFinalGrid(int destCol) throws ArrayIndexOutOfBoundsException {
        if (!this.auxDeck.isEmpty() && canMoveToFinalGrid(auxDeck.peek(), destCol)) {
            this.finalDecks[whichRow(destCol)][destCol] = auxDeck.pop();
        } else System.out.println("Mossa non valida");
    }
    //Dal campo alla griglia finale
    public void moveCardToFinalGrid(int rowStart, int colStart, int colDest) {
        int destRow;
        if ((rowStart >= 0 && rowStart < this.gameField.length) &&
                (colStart >= 0 && colStart < this.gameField[rowStart].length) &&
                (canMoveToFinalGrid(this.gameField[rowStart][colStart], colDest)) && //Solito controllo tra la  posizione originale e la finale
                this.gameField[rowStart+1][colStart]==null) {
            if (rowStart > 0) {//Se non siamo nella prima riga
                this.gameField[rowStart - 1][colStart].setHidden(false);//Imposta la carta precedente come visibile
            }
            destRow=whichRow(colDest);
            this.finalDecks[destRow][colDest] = this.gameField[rowStart][colStart];
            this.gameField[rowStart][colStart] = null;//Libera gli slot dove avevamo le carte
        } else System.out.println("Mossa non valida");
    }
    //Metodo che controlla se hai vinto o meno
    public boolean win() {
        boolean winFlag = true;
        for (int i = 0; i <= 3; i++) { //Colonna
            if (this.finalDecks[12][i] != null) { //Controllo se riga non è nulla
                winFlag = false; //Se la riga non è null allora metti il flag a falso
            } else return true; //Altrimenti ritorna direttamente vero
        }
        return winFlag; //ritorna a fine ciclo il flag
    }

    public String toString() {
        String result = "";
        for (int i = 0; i < this.gameField.length; i++) {
            result += "\n[";
            for (int j = 0; j < this.gameField[i].length; j++) {
                String value = this.gameField[i][j] != null ?
                        String.valueOf(this.gameField[i][j]) /*"card"*/ : "               ";
                if (i < 10) {
                    result += "[" + " " + i + "-" + j + " " + value + "]";
                } else
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
                if (i < 10) {
                    finalDecks += "[" + " " + i + "-" + j + " " + value + "]";
                } else
                    finalDecks += "[" + i + "-" + j + " " + value + "]";
            }
            finalDecks += "]";
        }
        return finalDecks + "\n" + result+"\n" + showCard();
    }
}
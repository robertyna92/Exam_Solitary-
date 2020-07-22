public class Card {
    enum Seed{CUORI,QUADR,FIORI,PICCH}
    enum Value{A, DUE,TRE,QUATTRO,CINQUE,SEI,SETTE,OTTO,NOVE, DIECI, J,Q,K;}
    enum Color{ROSSO, NERO}
    private boolean isHidden;
    private int realCardValue;
    private Seed seeds;
    private Value values;
    private Color color;
    public Card(Seed seed,Value value, int realCardValue, Color color){
        this.seeds=seed;
        this.values=value;
        this.realCardValue=realCardValue;
        this.color= color;
        this.isHidden=true;
    }

    public Seed getSeeds() {
        return seeds;
    }

    public Value getValues() {
        return values;
    }

    public int getRealCardValue() {
        return realCardValue;
    }

    public Color getColor(){
        return color;
    }

    public boolean getHidden(){
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    @Override
    public String toString() {
        final String ANSI_RED = "\u001B[31m";
        final String ANSI_RESET = "\u001B[0m";
        if (!getHidden() && getColor()==Color.ROSSO) {

            if (values == Value.A || values == Value.K || values == Value.Q || values == Value.J)
                return ANSI_RED + "\" " + values + " " + "DI" + " " +
                        seeds + " \" " + ANSI_RESET;

                if (getRealCardValue() == 10) {
                    return ANSI_RED + "\"" + getRealCardValue() + " " + "DI" + " " +
                            seeds + " \" " + ANSI_RESET;

                } else {
                    return ANSI_RED + "\" " + getRealCardValue() + " " + "DI" + " " +
                            seeds + " \" " + ANSI_RESET;
                }
            }

        if (getHidden()) {
            return "---------------";
        }
        if (values==Value.A || values==Value.K || values==Value.Q || values==Value.J) {
            return "\" " +values + " " + "DI" + " " +
                    seeds + " \" " ;
        }if (getRealCardValue()==10){
            return "\"" +getRealCardValue() + " " + "DI" + " " +
                    seeds +" \" " ;
        }
            else {
                return "\" " +getRealCardValue() + " " + "DI" + " " +
                        seeds +" \" " ;
            }

    }

}

package org.terminalteam.com;

public class WordLine {
    private int lineNumber;
    private String word;

    private Letter One;
    private Letter Two;
    private Letter Three;
    private Letter Four;
    private Letter Five;
    private Letter Six;
    private Letter Seven;
    private Letter Eight;
    private Letter Nine;
    private Letter Ten;
    private Letter Eleven;
    private Letter Twelve;
    private Letter Thirteen;
    private Letter Fourteen;
    private Letter Fifteen;
    private Letter[] letters;

    public WordLine(int lineNumber, String word) {
        this.lineNumber = lineNumber;
        this.word = word;
        letters = new Letter[]{One, Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Eleven, Twelve, Thirteen, Fourteen, Fifteen};
    }

    public void writeWord() {
        for (int i = 0; i < letters.length; i++) {
            if (i > word.length() - 1) {
                setChar(i, "*");
            } else {
                setChar(i, String.valueOf(word.charAt(i)));
            }
        }
    }

    public void showAsteriscs() {
        for (Letter letter : letters) {
            if (letter.getLetter().equals("*")) {
                //letter.show();
            }
        }
    }




    public String printLine() {
        String lineOne = " ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___ ___\n";
        String lineTwo = "|   |   |   |   |   |   |   |   |   |   |   |   |   |   |   |\n";
        String lineThr = "| " + One + " | " + Two + " | " + Three + " | " + Four + " | " + Five + " | " + Six + " | " + Seven + " | " + Eight + " | " + Nine + " | " + Ten + " | " + Eleven
                + " | " + Twelve + " | " + Thirteen + " | " + Fourteen + " | " + Fifteen + " |\n";
        String lineFou = "|___|___|___|___|___|___|___|___|___|___|___|___|___|___|___|\n";

        return lineOne + lineTwo + lineThr + lineFou;
    }

    public void setChar(int i, String value) {
       switch (i) {
           case 0:
               One = new Letter(value);
               break;
           case 1:
               Two = new Letter(value);
               break;
           case 2:
               Three = new Letter(value);
               break;
           case 3:
               Four = new Letter(value);
               break;
           case 4:
               Five = new Letter(value);
               break;
           case 5:
               Six = new Letter(value);
               break;
           case 6:
               Seven = new Letter(value);
               break;
           case 7:
               Eight = new Letter(value);
               break;
           case 8:
               Nine = new Letter(value);
               break;
           case 9:
               Ten = new Letter(value);
               break;
           case 10:
               Eleven = new Letter(value);
               break;
           case 11:
               Twelve = new Letter(value);
               break;
           case 12:
               Thirteen = new Letter(value);
               break;
           case 13:
               Fourteen = new Letter(value);
               break;
           case 14:
               Fifteen = new Letter(value);
               break;
       }
    }

}

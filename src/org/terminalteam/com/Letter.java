package org.terminalteam.com;

public class Letter {
    private String letter;
    private boolean show;

    Letter(String letter) {
        this.letter = letter.toUpperCase();
        show = false;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public boolean isShown() {
        return show;
    }

    public String getLetter(){
        return this.letter;
    }

    public String toString() {
        if (show) {
            return letter;
        }

        return " ";
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }
}

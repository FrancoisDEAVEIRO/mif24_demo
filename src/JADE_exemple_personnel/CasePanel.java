package JADE_exemple_personnel;

import javax.swing.JPanel;

public class CasePanel extends JPanel {
    private int xPos;
    private int yPos;
    
    public CasePanel(int x, int y){
        xPos = x;
        yPos = y;
    }
    
    
    /**
     * @return the x
     */
    public int getXPos() {
        return xPos;
    }

    /**
     * @param xPos the x to set
     */
    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    /**
     * @return the y
     */
    public int getYPos() {
        return yPos;
    }

    /**
     * @param yPos the y to set
     */
    public void setYPos(int yPos) {
        this.yPos = yPos;
    }
}

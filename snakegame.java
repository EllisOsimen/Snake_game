import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; /*snake segmentes */
import java.util.Random; /*place food randomly */
import javax.swing.*;


public class SnakeGame extends JPanel{
    private class Tile {
        int x;
        int y;

        Tile(int x,int y){
            this.x = x;
            this.y = y;
        }
    }
    int boardwidth;
    int boardheight;
    int tileSize = 25;

    SnakeGame (int boardwidth, int boardheight){
        this.boardheight = boardheight;
        this.boardwidth = boardwidth;
        setPreferredSize(new Dimension(this.boardwidth, this.boardheight));
        setBackground(Color.black);
    }
}

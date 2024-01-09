import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; /*snake segmentes */
import java.util.Random; /*place food randomly */
import javax.swing.*;


public class SnakeGame extends JPanel implements ActionListener, KeyListener{
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
    //Snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;
    //Food
    Tile food;
    Random random;
    //game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
     
    SnakeGame (int boardwidth, int boardheight){
        this.boardheight = boardheight;
        this.boardwidth = boardwidth;
        setPreferredSize(new Dimension(this.boardwidth, this.boardheight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5,5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10,10);
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        //Grid
        for(int i = 0; i <boardheight/tileSize;i++){
            g.drawLine(i*tileSize,0, i*tileSize, boardheight);
            g.drawLine(0,i*tileSize, boardwidth, i*tileSize);
 
        }
        
        //Food
        g.setColor(Color.red);
        g.fillRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize);

        //Snake
        g.setColor(Color.cyan);
        g.fillRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize);
    }
    public void placeFood(){
        food.x = random.nextInt(boardwidth/tileSize); //random int between 0 and boardwidth/tileSize
        food.x = random.nextInt(boardheight/tileSize);

    }
    public void move(){
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
       }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY!=1) {
            velocityX = 0;
            velocityY = -1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY!=-1){
            velocityX = 0;
            velocityY = 1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX!=-1){
            velocityX = 1;
            velocityY = 0;
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX!=1 ){
            velocityX = -1;
            velocityY = 0;
        }
        
        
     }
    @Override
    public void keyReleased(KeyEvent e) {
       }
    @Override
    public void keyTyped(KeyEvent e) {
    }
}

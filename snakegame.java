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
    boolean gameOver = false;
     
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
        //snakebody
        for(int i =0;i<snakeBody.size();i++){
            Tile snakePart = snakeBody.get(i);
            g.fillRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize);
        }
    }
    public void placeFood(){
        food.x = random.nextInt(boardwidth/tileSize); //random int between 0 and boardwidth/tileSize
        food.y = random.nextInt(boardheight/tileSize);

    }
    public boolean collision (Tile tile1,Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }
    public void move(){
        if (collision(snakeHead, food)){
        snakeBody.add(new Tile(food.x, food.y));
        placeFood();
        }

        //snake body
        for(int i = snakeBody.size()-1;i>=0;i--){
            Tile snakePart = snakeBody.get(i);
            if (i==0){
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;

            }
            else{
                Tile prevsnakePart = snakeBody.get(i-1);
                snakePart.x = prevsnakePart.x;
                snakePart.y = prevsnakePart.y;
            }
        }

        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //game over
        for(int i =0;i<snakeBody.size();i++){
            if (snakeHead.x == snakeBody.get(i).x && snakeHead.y == snakeBody.get(i).y) {
                gameOver = true;
            }
        }
        if ((snakeHead.x ==0)||(snakeHead.x == boardwidth)||(snakeHead.y == 0)||(snakeHead.y == boardheight)) {
            gameOver = true;
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver){
            gameLoop.stop();
        }
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

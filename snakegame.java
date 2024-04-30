import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; /*snake segmentes */
import java.util.Random; /*place food randomly */
import javax.swing.*;
import javax.swing.Timer;


public class snakegame extends JPanel implements ActionListener, KeyListener{
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
    int score = 0;
    //Snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;
    //Food
    Tile food;
    Tile speedBoost;
    Tile shrinkBooster;
    Random random;
    //game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;
     
    snakegame (int boardwidth, int boardheight){
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
        Boosters();
        manageShrinkBooster();

        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
        points(g);
    }
    public void points(Graphics g){
    
        // Draw score
        g.setColor(Color.white); 
        g.setFont(new Font("Arial", Font.BOLD, 14)); // Set font for the score
        g.drawString("Score: " + score, boardwidth - 100, 20); // Draw score at the top-right corner
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
        g.setColor(Color.yellow);
        g.fillRect(speedBoost.x * tileSize, speedBoost.y * tileSize, tileSize, tileSize);
        if (shrinkBooster != null) {
            g.setColor(Color.green);  // Choose a color that stands out
            g.fillRect(shrinkBooster.x * tileSize, shrinkBooster.y * tileSize, tileSize, tileSize);
        }
    }
    public void placeFood(){
        food.x = random.nextInt(boardwidth/tileSize); //random int between 0 and boardwidth/tileSize
        food.y = random.nextInt(boardheight/tileSize);

    }
    public void Boosters(){
        do {
            speedBoost = new Tile(random.nextInt(boardwidth / tileSize), random.nextInt(boardheight / tileSize));
        } while (collision(speedBoost, food));
    }
    void increaseSpeed() {
        // Assuming the base timer delay is 100, halve it to double the speed
        gameLoop.setDelay(50);
    
        // Create a timer to reset the speed after 4 seconds
        new Timer(4000, e -> {
            gameLoop.setDelay(100);
            ((Timer)e.getSource()).stop();
        }).start();
    }
    public boolean collision (Tile tile1,Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }
    public void resetGame() {
        score = 0;
        snakeBody.clear(); // Clear the existing snake body
        snakeHead = new Tile(5, 5); // Reset the head position
        placeFood(); // Re-place the food
        Boosters();
        gameOver = false; // Reset game over flag
        velocityX = 0; // Reset movement velocity
        velocityY = 0;
        gameLoop.start(); 
    }
    void manageShrinkBooster() {
        // Place the booster immediately, then make it appear every 20 seconds
        placeShrinkBooster();
        new Timer(20000, e -> {
            placeShrinkBooster();
            // Make the booster visible for only 3 seconds
            new Timer(3000, ev -> {
                shrinkBooster = null;
                ((Timer) ev.getSource()).stop();
                repaint(); // Ensure the game repaints to reflect the booster disappearing
            }).start();
        }).start();
    }
    
    void placeShrinkBooster() {
        do {
            shrinkBooster = new Tile(random.nextInt(boardwidth / tileSize), random.nextInt(boardheight / tileSize));
        } while (collision(shrinkBooster, food) || collision(shrinkBooster, speedBoost));  // Ensure it doesn't overlap
    }
    
    public void move(){
        if (collision(snakeHead, food)){
        snakeBody.add(new Tile(food.x, food.y));
        placeFood();
        score += 1;

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
        if (snakeHead.x < 0 || snakeHead.x >= boardwidth / tileSize || snakeHead.y < 0 || snakeHead.y >= boardheight / tileSize) {
        gameOver = true;
        
    }

    // Check if the snake bites itself
    for (int i = 0; i < snakeBody.size(); i++) {
        if (snakeHead.x == snakeBody.get(i).x && snakeHead.y == snakeBody.get(i).y) {
            gameOver = true;
        }
    }
        if (collision(snakeHead, speedBoost)) {
            increaseSpeed();
            Boosters();  // Re-place the power-up
        }
        if (shrinkBooster != null && collision(snakeHead, shrinkBooster)) {
            int reduceBy = snakeBody.size() / 2;
            while (reduceBy > 0 && !snakeBody.isEmpty()) {
                snakeBody.remove(snakeBody.size() - 1);  // Remove the last element
                reduceBy--;
            }
            shrinkBooster = null;  // Remove the booster once consumed
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver){
            gameLoop.stop();
            JOptionPane.showMessageDialog(this, "You lost! \n"+
            "Points :" +score +"\nPress enter to keep playing", "Game Over", JOptionPane.ERROR_MESSAGE);
            gameLoop.setDelay(100);
        
        }
       }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                resetGame();
            }}
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

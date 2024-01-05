import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardwidth = 800;
        int boardheight = boardwidth;

        JFrame frame = new JFrame("snake");
        frame.setVisible(true);
        frame.setSize(boardwidth,boardheight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SnakeGame snakeGame = new SnakeGame(boardwidth, boardheight);
        frame.add(snakeGame);
        frame.pack();
    }
}
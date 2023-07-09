import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {
    //initialize the board height and width
    int B_HEIGHT = 400, B_WIDTH = 400;
    //initialize the snake
    int MAXDOTS = 1600;
    int[] X = new int[MAXDOTS];
    int[] Y = new int[MAXDOTS];
    int DOT_SIZE = 10;
    int dot;

    int apple_X;
    int apple_Y;
    //images
    Image body, head, apple;
    // initialize the timer
    Timer timer;
    int delay = 100;
    boolean leftDireaction = true;
    boolean rightDireaction = false;
    boolean upDireaction = false;
    boolean downDireaction = false;
    boolean inGame = true;
    Board(){
        ControlAtopter controlAtopter = new ControlAtopter();
        addKeyListener(controlAtopter);
        setFocusable(true);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        setBackground(Color.BLACK);
        snakeInitialization();
        loadTheImages();
    }
    // lets initialize the snake
    public  void snakeInitialization(){
        dot = 3;
        X[0] = 350;
        Y[0] = 350;
        for(int i = 1; i < 3; i++){
            X[i] = X[0] + DOT_SIZE * i;
            Y[i] = Y[0];
        }
        locateApple();
        timer = new Timer(delay, this);
        timer.start();
    }
    public void loadTheImages(){
        ImageIcon bodyIcon = new ImageIcon("src/Resources/dot.png");
        body = bodyIcon.getImage();
        ImageIcon headIcon = new ImageIcon("src/Resources/head.png");
        head = headIcon.getImage();
        ImageIcon appleIcon = new ImageIcon("src/Resources/apple.png");
        apple = appleIcon.getImage();
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        doDrawing(g);
    }
    public void doDrawing(Graphics g){
        if(inGame) {
            g.drawImage(apple, apple_X, apple_Y, this);
            for (int i = 0; i < dot; i++) {
                if (i == 0) {
                    g.drawImage(head, X[i], Y[i], this);
                } else {
                    g.drawImage(body, X[i], Y[i], this);
                }
            }
        }else {
            gameOver(g);
            timer.stop();
        }
    }
    // generate the apple position randomly
    public void locateApple(){
        apple_X = ((int)(Math.random() * 39)) * DOT_SIZE;
        apple_Y = ((int)(Math.random() * 39)) * DOT_SIZE;
    }
   @Override
    public void actionPerformed(ActionEvent actionEvent){
        if(inGame) {
            checkCollision();
            checkApple();
            move();
        }
        repaint();
    }
    // move the snake
    public void move(){
        for(int i = dot - 1; i > 0; i--){
            X[i] = X[i - 1];
            Y[i] = Y[i - 1];
        }
        if(leftDireaction){
            X[0] -= DOT_SIZE;
        }
        if(rightDireaction){
            X[0] += DOT_SIZE;
        }
        if(upDireaction){
            Y[0] -= DOT_SIZE;
        }
        if(downDireaction){
            Y[0] += DOT_SIZE;
        }
    }
    public void checkApple(){
        if(apple_X == X[0] && apple_Y == Y[0]) {
            dot++;
            locateApple();
        }
    }
    public void checkCollision(){
        // this is for body collision
        for(int i = 1; i < dot; i++){
            if(i >4 && X[0] == X[i] && Y[0] == Y[i]){
                inGame = false;
            }
        }
        // this is for boundry collision
        if(X[0] < 0 || Y[0] < 0 || X[0] >= B_WIDTH || Y[0] >= B_HEIGHT){
            inGame = false;
        }
    }
    public  void gameOver(Graphics g){
        String message = "Game Over";
        int score = (dot - 3) * 100;
        String ScoreMessage = "Score :" + Integer.toString(score);
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fontMetrics = getFontMetrics(small);

        g.setColor(Color.WHITE);
        g.setFont(small);
        g.drawString(message, (B_WIDTH-fontMetrics.stringWidth(message))/2, B_HEIGHT / 4);
        g.drawString(ScoreMessage, (B_WIDTH-fontMetrics.stringWidth(ScoreMessage))/2, 3 *(B_HEIGHT / 4));
    }
    private  class ControlAtopter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent keyEvent){
            int key = keyEvent.getKeyCode();
            if(key == keyEvent.VK_LEFT && !rightDireaction){
                leftDireaction = true;
                upDireaction = false;
                downDireaction = false;
            }
            if(key == keyEvent.VK_RIGHT && !leftDireaction){
                rightDireaction = true;
                upDireaction = false;
                downDireaction = false;
            }
            if(key == keyEvent.VK_UP && !downDireaction){
                upDireaction = true;
                leftDireaction = false;
                rightDireaction = false;
            }
            if(key == keyEvent.VK_DOWN && !upDireaction){
                downDireaction = true;
                leftDireaction = false;
                rightDireaction = false;
            }
        }

    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {

    private final int B_width = 300;
    private final int B_height = 300;
    private final int dot_size = 10;
    private final int all_dots = 900;
    private final int rand_pos = 29;
    private final int delay = 140;

    private final int x[] = new int[all_dots];
    private final int y[] = new int[all_dots];

    private int dots;
    private int apple_x;
    private int apple_y;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;

    public Board() {
        initBoard();
    }

    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.BLACK);
        setFocusable(true);

        setPreferredSize(new Dimension(B_width, B_height));
        loadImages();
        initGame();
    }

    private void loadImages() {
        ImageIcon lid = new ImageIcon("src/dot.png");
        ball = lid.getImage();

        ImageIcon iia = new ImageIcon("src/apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("src/head.png");
        head = iih.getImage();
    }

    private void initGame() {
        dots = 3;

        for (int z=0; z<dots; z++){
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        locateApple();

        timer = new Timer(delay, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g){
        if (inGame) {
            g.drawImage(apple, apple_x, apple_y, this);

            for (int z=0;z<dots;z++){
                if (z == 0){
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g){
        String msg = "Game over!";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_width - metr.stringWidth(msg)) / 2, B_height / 2);
    }

    private void checkApple(){
        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            dots++;
            locateApple();
        }
    }

    private void move() {
        for (int z=dots;z>0;z--) {
            x[z] = x[(z-1)];
            y[z] = y[(z-1)];
        }

        if(leftDirection) {
             x[0] -= dot_size;
        }

        if (rightDirection) {
            x[0] += dot_size;
        }

        if (upDirection) {
            y[0] -= dot_size;
        }

        if (downDirection) {
            y[0] += dot_size;
        }
    }

    private void checkCollision() {
        for (int z = dots; z>0; z--) {
            if ((z>4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_height){
            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= B_width) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }

        if (!inGame) {
            timer.stop();
        }
    }

    private void locateApple() {

        int r = (int) (Math.random() * rand_pos);
        apple_x = ((r * dot_size));

        r = (int) (Math.random() * rand_pos);
        apple_y = ((r * dot_size));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SnakePanel extends JPanel implements Runnable {
    public SnakePanel(int w, int h) {
        panel = this;
        setFocusable(true);
        setBackground(new Color(0.4f, 0.4f, 0.6f));

        add(new JMenuBar());
        addKeyListener(new KeyHandler());
        MouseHandler mHandler = new MouseHandler();
        addMouseListener(mHandler);
        addMouseMotionListener(mHandler);

        width = 800;
        height = 600;
        topLeft = new Point(50, 50);
        bottomRight = new Point(topLeft.x + width, topLeft.y + height);
        final Point min = topLeft;
        final Point max = bottomRight;
        middle =  new Point( (min.x + max.x) / 2,
                (min.y + max.y) / 2 );

        delayTime = 70;

        snake = new Snake(5, topLeft);
        snakeDir = Direction.DIR_RIGHT;
        snakeQdr = Quadrant.QD_TWO;
        snakeFood = new Food(topLeft, bottomRight);
        p1 = new Portal(min.x + 20 * segmentWidth, min.y + 10 * segmentWidth);
        p2 = new Portal(max.x - 20 * segmentWidth, max.y - 10 * segmentWidth);

        snakeTitleImg = new ImageIcon("Images/snake.jpg");
        helpScreenBgImg = new ImageIcon("Images/help_bg.jpg");
        quad1Img = new ImageIcon("Images/water.jpg");
        quad2Img = new ImageIcon("Images/grass.jpg");
        quad3Img = new ImageIcon("Images/gravel.jpg");
        quad4Img = new ImageIcon("Images/ice.jpg");

        isMouseInside = false;
        gamePaused = false;
        gameStarted = false;
        helpScreen = false;
        toMove = false;
    }

    public void run() {
        while (true) {
            if (!gamePaused && snake.isAlive() && gameStarted) {
                snake.moveHead(snakeDir);
                toMove = false;
                panel.repaint();

                if ( snake.isCollidingItself() ||
                        checkBoundaryCollision(snakeQdr, snake.headPos()) ) {
                    snake.setAlive(false);
                    int score = (snake.length() - 5) * ( (100 - delayTime) / 10 + 6);
                    if (score > highScore) {
                        highScore = score;
                    }
                    panel.repaint();
                }

                Point h = snake.headPos();
                if (h.x == p1.getX() && h.y == p1.getY()) {
                    Point nPos = p2.getLocation();
                    snake.setHeadPos(nPos);
                    snakeQdr = getQuadrant(nPos);
                }
                if (h.x == p2.getX() && h.y == p2.getY()) {
                    Point nPos = p1.getLocation();
                    snake.setHeadPos(nPos);
                    snakeQdr = getQuadrant(nPos);
                }

                if (snake.haveEatenFood(snakeFood)) {
                    snake.increaseBodyLength();
                    snakeFood.generateNewPos();
                }
            }

            try {
                Thread.sleep(delayTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void paintComponent(Graphics g) {
        if (!snake.isAlive()) {
            displayMessage(g, Color.BLACK, "GAME OVER!", (width / 2) - 100, height / 2, 48);

            g.setFont(new Font("Default", Font.BOLD + Font.ITALIC, 18));
            g.setColor(Color.BLUE);
            g.drawString("Press Enter to Play!", (width / 2) - 50, (height / 2) + 200 );

            return; // Comment it not to show the game end status.
        }

        super.paintComponent(g);
        if (!gameStarted) {
            g.drawImage(snakeTitleImg.getImage(), (width / 3) + 30, 120,
                    snakeTitleImg.getImageObserver());
            g.setFont(new Font("Monospaced", Font.BOLD, 48));
            g.drawString("PORTAL SNAKE", width / 3, 100);
            g.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 24));
            g.drawString("Syed M Madani", width / 2 - 40, (height / 2) + 180);

            g.setFont(new Font("Default", Font.BOLD | Font.ITALIC, 18));
            g.setColor(Color.BLUE);
            g.drawString("Press Enter to Play!", (width / 2) - 50, (height / 2) + 220);
            g.setColor(Color.DARK_GRAY);
            g.drawString("Press F1 for Help", 380, 580);
        }

        if (helpScreen) {
            super.paintComponent(g);
            g.drawImage(helpScreenBgImg.getImage(), 80, 50,
                    helpScreenBgImg.getImageObserver());
            displayMessage(g, Color.BLACK, "HOW TO PLAY?", 300, 100, 38);
            displayMessage(g, Color.BLACK,
                    "Arrow Keys OR W A S D: Changes Snake Direction",
                    width / 8, 180, 24);
            displayMessage(g, Color.BLACK, "Space: Pauses the Game",
                    width / 4, 260, 24);
            displayMessage(g, Color.RED, "Mouse Left Click: Shoots Red Portal",
                    width / 5, 320, 24);
            displayMessage(g, Color.BLUE,"Mouse Right Click: Shoots Blue Portal",
                    width / 5, 400, 24);
            displayMessage(g, Color.DARK_GRAY,
                    "F1: Toggles Help Menu", (width / 4) + 20, 480, 24);
            displayMessage(g, Color.BLACK, "Press F1 to close Help",
                    (width / 3) + 20, 560, 16);
            return;
        }


        if (!gameStarted || !snake.isAlive()) {

            return;
        }

        g.drawImage(quad1Img.getImage(), middle.x, topLeft.y,
                quad1Img.getImageObserver());

        g.drawImage(quad2Img.getImage(), topLeft.x, topLeft.y,
                quad2Img.getImageObserver());

        g.drawImage(quad3Img.getImage(), topLeft.x, middle.y,
                quad3Img.getImageObserver());

        g.drawImage(quad4Img.getImage(), middle.x, middle.y,
                quad4Img.getImageObserver());

        drawQuadrants(g);
        if (isMouseInside) {
            g.setColor(Color.DARK_GRAY);
            g.drawRect(mX, mY, segmentWidth, segmentWidth);
        }
        p1.display(g, Color.RED);
        p2.display(g, Color.BLUE);
        snakeFood.display(g, Color.GREEN);
        snake.display(g, Color.ORANGE);
        displayMessage(g, Color.BLACK,
                "Your Score: " + (snake.length() - 5) * ( (100 - delayTime) / 10 + 6),
                width / 3, 20, 16);
        displayMessage(g, new Color(1.0f, 0.5f, 0.3f),
                "Highest Session Score: " + highScore,
                width / 3, 37, 16);
    }

    private void drawQuadrants(Graphics g) {
        g.setColor(Color.RED);
        g.drawRect(topLeft.x, topLeft.y, width, height);
        g.drawLine(middle.x, topLeft.y, middle.x, bottomRight.y);
        g.drawLine(topLeft.x, middle.y, bottomRight.x, middle.y);
    }

    private void displayMessage(Graphics g, Color c,  String s,
                                int x, int y, int sz) {
        g.setColor(c);
        g.setFont(new Font("Monospaced", Font.BOLD + Font.ITALIC, sz));
        g.drawString(s, x, y);
    }

    private Quadrant getQuadrant(Point p) {
        final Point min = topLeft;
        final Point max = bottomRight;
        final Point mid = middle;

        if ( (p.x >= min.x && p.x < mid.x) && (p.y >= min.y && p.y < mid.y) )
            return Quadrant.QD_TWO;

        if ( (p.x >= mid.x && p.x < max.x) && (p.y >= min.y && p.y < mid.y) )
            return Quadrant.QD_ONE;

        if ( (p.x >= min.x && p.x < mid.x) && (p.y >= mid.y && p.y < max.y) )
            return Quadrant.QD_THREE;

        return Quadrant.QD_FOUR;
    }

    private boolean checkBoundaryCollision(Quadrant q, Point h) {
        int w = segmentWidth;
        final Point min = topLeft;
        final Point max = bottomRight;
        final Point mid = middle;

        switch (q) {
            case QD_ONE: if ( (h.x < mid.x) || (h.x > max.x - w) ||
                    (h.y < min.y) || (h.y > mid.y - w) )
                return true;
                break;

            case QD_TWO: if ( (h.x < min.x) || (h.x > mid.x - w) ||
                    (h.y < min.y) || (h.y > mid.y - w) )
                return true;
                break;

            case QD_THREE: if ( (h.x < min.x) || (h.x > mid.x - w) ||
                    (h.y < mid.y) || (h.y > max.y - w) )
                return true;
                break;

            case QD_FOUR: if ( (h.x < mid.x) || (h.x > max.x - w) ||
                    (h.y < mid.y) || (h.y > max.y - w) )
                return true;
                break;
        }
        return false;
    }

    public void resetGame() {
        snake = new Snake(5, topLeft);
        snakeDir = Direction.DIR_RIGHT;
        snakeQdr = Quadrant.QD_TWO;
        snakeFood.generateNewPos();
        gameStarted = false;
        gamePaused = false;
        isMouseInside = false;
        toMove = false;
    }

    private JPanel panel;
    private Point topLeft;
    private Point bottomRight;
    private Point middle;
    private int width;
    private int height;
    private boolean isMouseInside;
    private int mX, mY;  /* This would be used for portals */
    private boolean toMove;
    private boolean gamePaused;
    private boolean gameStarted;
    private boolean helpScreen;
    private Direction snakeDir;
    private Quadrant snakeQdr;
    private Food snakeFood;
    private Snake snake;
    private Portal p1, p2;
    private ImageIcon snakeTitleImg;
    private ImageIcon helpScreenBgImg;
    private ImageIcon quad1Img, quad2Img, quad3Img, quad4Img;
    public enum Direction { DIR_LEFT, DIR_RIGHT, DIR_UP, DIR_DOWN }
    private enum Quadrant { QD_ONE, QD_TWO, QD_THREE, QD_FOUR }
    public static int highScore;
    public static int delayTime;
    public static final int segmentWidth = 10;

    /* Key Handler */
    class KeyHandler extends KeyAdapter {
        public void keyPressed(KeyEvent e) {

            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    if (snakeDir != Direction.DIR_DOWN && !toMove) {
                        snakeDir = Direction.DIR_UP;
                        toMove = true;
                    }
                    break;

                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    if (snakeDir != Direction.DIR_UP && !toMove) {
                        snakeDir = Direction.DIR_DOWN;
                        toMove = true;
                    }
                    break;

                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    if (snakeDir != Direction.DIR_RIGHT && !toMove) {
                        snakeDir = Direction.DIR_LEFT;
                        toMove = true;
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    if (snakeDir != Direction.DIR_LEFT && !toMove) {
                        snakeDir = Direction.DIR_RIGHT;
                        toMove = true;
                    }
                    break;

                case KeyEvent.VK_SPACE:
                    if (!helpScreen) gamePaused = !gamePaused;
                    break;

                case KeyEvent.VK_ENTER: gameStarted = true;
                    if (!snake.isAlive()) {
                        resetGame();
                    }

                    break;

                case KeyEvent.VK_F1: helpScreen = !helpScreen;
                    gamePaused = !gamePaused;
                    if (!gamePaused && helpScreen) gamePaused  = true;
                    //		if (gamePaused && helpScreen

                    break;

                default:
                    break;
            }
            panel.repaint();
        }
    }

    /* Mouse Handler */
    class MouseHandler extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            if (gamePaused) return;
            int buttonClicked = e.getButton();
            if (buttonClicked == MouseEvent.BUTTON1) {
                p1.setLocation(mX, mY);
            }
            else if (buttonClicked == MouseEvent.BUTTON3) {
                p2.setLocation(mX, mY);
            }
            else if (buttonClicked == MouseEvent.BUTTON2) {
            }
            panel.repaint();
        }

        public void mouseMoved(MouseEvent e) {
            Point mPoint = e.getPoint();
            if ( (mPoint.x >= topLeft.x && mPoint.x < bottomRight.x) &&
                    (mPoint.y >= topLeft.y && mPoint.y < bottomRight.y) ) {
                isMouseInside = true;
                mX = (mPoint.x / segmentWidth) * segmentWidth;
                mY = (mPoint.y / segmentWidth) * segmentWidth;
            }
            panel.repaint();
        }
    }
}
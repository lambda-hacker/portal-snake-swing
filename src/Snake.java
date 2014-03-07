import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Snake {
    public Snake(int initLen, Point refPoint) {
        snakeSegments = new ArrayList<Point>(200);
        isAlive = true;
        w = SnakePanel.segmentWidth;

        int x = refPoint.x + w * initLen;
        int y = refPoint.y + w * 6;
        Point h = new Point(x, y);
        snakeSegments.add(h);
        head = snakeSegments.get(0);

        for (int i = 1; i < initLen; i++) {
            h = new Point(head.x - w * i, head.y);
            snakeSegments.add(h);
        }
    }

    public Point headPos() {
        return head;
    }

    public void setHeadPos(Point pos) {
        snakeSegments.set(0, new Point(pos));
        head = snakeSegments.get(0);
    }

    public int length() {
        return snakeSegments.size();
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean v) {
        isAlive = v;
    }

    public void increaseBodyLength() {
        snakeSegments.add(new Point(-100, -100));
    }

    public boolean haveEatenFood(Food f) {
        final Point fPos = f.getPosition();
        if (head.x == fPos.x && head.y == fPos.y) {
            return true;
        }
        return false;
    }

    public void moveHead(SnakePanel.Direction d) {
        moveBody();
        final Point p = snakeSegments.get(1);
        int nX = 0;
        int nY = 0;

        switch (d) {
            case DIR_UP: nX = p.x; nY = p.y - w;
                break;

            case DIR_DOWN: nX = p.x; nY = p.y + w;
                break;

            case DIR_LEFT: nX = p.x - w; nY = p.y;
                break;

            case DIR_RIGHT: nX = p.x + w; nY = p.y;
                break;
        }
        Point hPt = snakeSegments.get(0);
        hPt.x = nX;
        hPt.y = nY;
        head = hPt;
    }

    public boolean isCollidingItself() {
        for (int i = 1; i < length(); i++) {
            Point p = snakeSegments.get(i);
            if (head.x == p.x && head.y == p.y)
                return true;
        }
        return false;
    }

    public void display(Graphics g, Color color) {
        for (int i = 0; i < length(); i++) {
            g.setColor(color);
            Point p = snakeSegments.get(i);
            g.fillRect(p.x, p.y, w, w);
        }
    }

    private void moveBody() {
        for (int i = length() - 1; i > 0; i--) {
            Point seg = snakeSegments.get(i - 1);
            snakeSegments.set(i, new Point(seg));
        }
    }

    private boolean isAlive;
    private Point head;
    private int w;
    private ArrayList<Point> snakeSegments;
}
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Portal {
    public Portal(int x, int y) {
        position = new Point(x, y);
    }

    public Portal(Point positoin) {
        this.position = position;
    }

    public Point getLocation() {
        return position.getLocation();
    }

    public void setLocation(int x, int y) {
        position.x = x;
        position.y = y;
    }

    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
    }

    public void display(Graphics g, Color color) {
        g.setColor(color);
        g.fillRect(position.x, position.y,
                SnakePanel.segmentWidth, SnakePanel.segmentWidth);
    }

    private Point position;
    private int color;
}
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;

import java.util.Random;

public class Food {
    public Food(Point min, Point max) {
        this.min = min;
        this.max = max;
        this.side = SnakePanel.segmentWidth;
        posGenerator = new Random();
        position = new Point();
        this.generateNewPos();
    }

    public Point getPosition() {
        return position;
    }

    public void generateNewPos() {
        position.x = min.x + side *
                (posGenerator.nextInt(max.x - min.x) / side);

        position.y = min.y + side *
                (posGenerator.nextInt(max.y - min.y) / side);
    }

    public void display(Graphics g, Color color) {
        g.setColor(color);
        g.fillOval(position.x, position.y, side, side);
    }

    private int side;
    private Point position;
    private final Point min, max;
    private Random posGenerator;
}
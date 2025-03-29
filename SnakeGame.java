import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private final int WIDTH = 600;
    private final int HEIGHT = 400;
    private final int BOX_SIZE = 10;
    private final int NUM_BOXES_X = WIDTH / BOX_SIZE;
    private final int NUM_BOXES_Y = HEIGHT / BOX_SIZE;

    private LinkedList<Point> snake;
    private Point food;
    private char direction;
    private boolean gameOver;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        snake = new LinkedList<>();
        snake.add(new Point(NUM_BOXES_X / 2, NUM_BOXES_Y / 2));
        direction = 'R';
        spawnFood();
        gameOver = false;

        Timer timer = new Timer(100, this);
        timer.start();

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (direction != 'D') direction = 'U';
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != 'U') direction = 'D';
                        break;
                    case KeyEvent.VK_LEFT:
                        if (direction != 'R') direction = 'L';
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != 'L') direction = 'R';
                        break;
                }
            }
        });
        setFocusable(true);
    }

    private void spawnFood() {
        Random random = new Random();
        food = new Point(random.nextInt(NUM_BOXES_X), random.nextInt(NUM_BOXES_Y));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            moveSnake();
            checkCollision();
            repaint();
        }
    }

    private void moveSnake() {
        Point head = snake.getFirst();
        Point newHead = new Point(head);

        switch (direction) {
            case 'U':
                newHead.translate(0, -1);
                break;
            case 'D':
                newHead.translate(0, 1);
                break;
            case 'L':
                newHead.translate(-1, 0);
                break;
            case 'R':
                newHead.translate(1, 0);
                break;
        }

        if (newHead.equals(food)) {
            snake.addFirst(newHead);
            spawnFood();
        } else {
            snake.addFirst(newHead);
            snake.removeLast();
        }
    }

    private void checkCollision() {
        Point head = snake.getFirst();
        // Check for wall collision
        if (head.x < 0 || head.x >= NUM_BOXES_X || head.y < 0 || head.y >= NUM_BOXES_Y || snake.subList(1, snake.size()).contains(head)) {
            gameOver = true;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * BOX_SIZE, p.y * BOX_SIZE, BOX_SIZE, BOX_SIZE);
        }
        g.setColor(Color.RED);
        g.fillRect(food.x * BOX_SIZE, food.y * BOX_SIZE, BOX_SIZE, BOX_SIZE);

        if (gameOver) {
            g.setColor(Color.BLACK);
            g.drawString("Game Over", WIDTH / 2 - 30, HEIGHT / 2);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

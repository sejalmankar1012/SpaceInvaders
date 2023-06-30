import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

public class SpaceInvaders extends Canvas implements KeyListener {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PLAYER_SPEED = 5;
    private static int ENEMY_SPEED = 3;

    private Player player;
    private List<Enemy> enemies;
    private boolean isRunning;

    public SpaceInvaders() {
        JFrame frame = new JFrame("Space Invaders");
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(this);
        frame.addKeyListener(this);

        player = new Player(WIDTH / 2, HEIGHT - 50);
        enemies = new ArrayList<>();
        createEnemies();

        isRunning = true;
        frame.setVisible(true);

        startGameLoop();
    }

    private void createEnemies() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                int x = col * 60 + 50;
                int y = row * 40 + 50;
                enemies.add(new Enemy(x, y));
            }
        }
    }

    private void startGameLoop() {
        while (isRunning) {
            update();
            render();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        player.update();

        for (Enemy enemy : enemies) {
            enemy.update();
            if (enemy.intersects(player)) {
                gameOver();
            }
        }
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);

        player.draw(g);

        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }

        g.dispose();
        bs.show();
    }

    private void gameOver() {
        isRunning = false;
        JOptionPane.showMessageDialog(null, "Game Over", "Space Invaders", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT) {
            player.move(-PLAYER_SPEED);
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            player.move(PLAYER_SPEED);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SpaceInvaders::new);
    }

    private class Player {

        private int x;
        private int y;

        public Player(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void move(int dx) {
            x += dx;
            if (x < 0) {
                x = 0;
            } else if (x > WIDTH - 50) {
                x = WIDTH - 50;
            }
        }

        public void update() {
            // Update player logic
        }

        public void draw(Graphics g) {
            g.setColor(Color.GREEN);
            g.fillRect(x, y, 50, 30);
        }
    }

    private class Enemy {

        private int x;
        private int y;

        public Enemy(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void update() {
            x += ENEMY_SPEED;
            if (x < 0 || x > WIDTH - 30) {
                ENEMY_SPEED *= -1;
                y += 10;
            }
        }

        public boolean intersects(Player player) {
            Rectangle playerRect = new Rectangle(player.x, player.y, 50, 30);
            Rectangle enemyRect = new Rectangle(x, y, 30, 30);
            return playerRect.intersects(enemyRect);
        }

        public void draw(Graphics g) {
            g.setColor(Color.RED);
            g.fillRect(x, y, 30, 30);
        }
    }
}

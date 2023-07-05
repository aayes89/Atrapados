import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CogidosGame extends JPanel {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int BLUE_TEAM_SIZE = 3;
    private static final int RED_TEAM_SPEED = 5;
    private static final int BLUE_TEAM_SPEED = 3;
    private static final int HIDE_TIME = 5000;

    private boolean gameStarted = false;
    private boolean gameEnded = false;

    private Rectangle redTeam;
    private List<Rectangle> blueTeam;
    private List<Rectangle> barriers;

    private Timer redTeamTimer;
    private boolean[][] visited;

    public CogidosGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.white);

        redTeam = new Rectangle(50, 50, 50, 50);
        blueTeam = new ArrayList<>();
        for (int i = 0; i < BLUE_TEAM_SIZE; i++) {
            blueTeam.add(new Rectangle(0, 0, 50, 50));
        }

        barriers = new ArrayList<>();
        barriers.add(new Rectangle(200, 200, 20, 200));
        barriers.add(new Rectangle(500, 100, 20, 200));
        barriers.add(new Rectangle(300, 400, 200, 20));
        barriers.add(new Rectangle(100, 500, 200, 20));

        visited = new boolean[WIDTH][HEIGHT];

        redTeamTimer = new Timer(1000, e -> {
            if (gameStarted && !gameEnded) {
                moveRedTeam();
                checkCollisions();
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (!gameStarted) {
                    startGame();
                } else if (gameEnded) {
                    resetGame();
                }
            }
        });

        setFocusable(true);
        requestFocus();
    }

    private void startGame() {
        gameStarted = true;

        // Mueve al equipo azul a ubicaciones aleatorias
        Random random = new Random();
        for (Rectangle blueMember : blueTeam) {
            int x, y;
            do {
                x = random.nextInt(WIDTH - 50);
                y = random.nextInt(HEIGHT - 50);
            } while (intersectsBarrier(x, y));

            blueMember.setLocation(x, y);
        }

        // Inicia un temporizador para que el equipo azul se esconda
        Timer hideTimer = new Timer(HIDE_TIME, e -> {
            redTeam.setLocation(50, 50);
            repaint();
        });
        hideTimer.setRepeats(false);
        hideTimer.start();

        redTeamTimer.start();
        requestFocus();
    }

    private void resetGame() {
        gameStarted = false;
        gameEnded = false;

        redTeamTimer.stop();

        redTeam.setLocation(50, 50);
        for (Rectangle blueMember : blueTeam) {
            blueMember.setLocation(0, 0);
        }

        repaint();
    }

    private void moveRedTeam() {
        int startX = redTeam.x;
        int startY = redTeam.y;

        visited[startX][startY] = true;

        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(startX, startY));

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            int currentX = current.x;
            int currentY = current.y;

            int[] dx = {0, 0,A continuación, se muestra la segunda parte del código optimizado:

```java
            int[] dy = {-1, 1, 0, 0};

            for (int i = 0; i < 4; i++) {
                int nextX = currentX + dx[i] * RED_TEAM_SPEED;
                int nextY = currentY + dy[i] * RED_TEAM_SPEED;

                if (nextX >= 0 && nextX <= WIDTH - redTeam.width && nextY >= 0 && nextY <= HEIGHT - redTeam.height
                        && !visited[nextX][nextY] && !intersectsBarrier(nextX, nextY)) {
                    visited[nextX][nextY] = true;

                    redTeam.setLocation(nextX, nextY);

                    if (checkCollisionsBlueTeam(nextX, nextY)) {
                        return;
                    }

                    queue.add(new Point(nextX, nextY));
                }
            }
        }
    }

    private boolean intersectsBarrier(int x, int y) {
        for (Rectangle barrier : barriers) {
            if (barrier.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkCollisionsBlueTeam(int px, int py) {
        boolean collision = false;
        for (Rectangle blueMember : blueTeam) {
            if (redTeam.intersects(blueMember)) {
                blueMember.setLocation(0, 0);
                boolean remainingMembers = blueTeam.stream().anyMatch(rectangle -> rectangle.getX() != 0 && rectangle.getY() != 0);
                if (!remainingMembers) {
                    gameEnded = true;
                }
                collision = true;
                break;
            }
        }
        return collision;
    }

    private void checkCollisions() {
        for (Rectangle blueMember : blueTeam) {
            if (redTeam.intersects(blueMember)) {
                blueMember.setLocation(0, 0);
                boolean remainingMembers = blueTeam.stream().anyMatch(rectangle -> rectangle.getX() != 0 && rectangle.getY() != 0);
                if (!remainingMembers) {
                    gameEnded = true;
                }
                break;
            }
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!gameStarted) {
            g.setColor(Color.black);
            g.drawString("Haz clic para comenzar el juego", WIDTH / 2 - 80, HEIGHT / 2);
        } else {
            for (Rectangle barrier : barriers) {
                g.setColor(Color.gray);
                g.fillRect(barrier.x, barrier.y, barrier.width, barrier.height);
            }

            g.setColor(Color.red);
            g.fillRect(redTeam.x, redTeam.y, redTeam.width, redTeam.height);

            g.setColor(Color.blue);
            for (Rectangle blueMember : blueTeam) {
                g.fillRect(blueMember.x, blueMember.y, blueMember.width, blueMember.height);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Cogidos Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(new CogidosGame());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

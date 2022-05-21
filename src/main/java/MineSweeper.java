import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MineSweeper extends JFrame {

    private JPanel panel;
    private JLabel label;
    private final int cols;
    private final int rows;
    private final int IMAGE_SIZE = 50;
    private Game game;
    private final MineSweeperSolver solver;

    public static void main(String[] args) {
        new MineSweeper(10, 10);
    }

    public MineSweeper(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        this.game = new Game(cols, rows, 20);
        this.solver = new MineSweeperSolver(game.getVisibleBoard(), new Coord(cols, rows));
        initImages();
        initLabel();
        initPanel();
        initFrame();
    }

    private void initFrame() {
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Minesweeper");
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void initLabel() {
        JButton startButton = new JButton("Start");
        JButton solveButton = new JButton("Solve");
        startButton.addActionListener(e -> startNewGame());
        solveButton.addActionListener(e -> startSolve());
        label = new JLabel(getLabelText());
        add(label, BorderLayout.SOUTH);
        add(startButton, BorderLayout.NORTH);
        add(solveButton, BorderLayout.EAST);
    }

    public void startSolve() {
        if (game.getState() == GameState.PLAYING) {
            solver.setVisibleBoard(game.getVisibleBoard());
            List<CellGroup> solutions = solver.solve();

            while (solutions.size() != 0 && game.getState() == GameState.PLAYING) {
                for (CellGroup group : solutions) {
                    if (group.getCellList().size() == group.getNumOfMines()) {
                        for (Coord coord : group.getCellList()) {
                            if (game.getState() == GameState.PLAYING &&
                                    game.getVisibleBoard()[coord.getX()][coord.getY()] != Cell.FLAGGED) {
                                game.onRightButtonPressed(coord);
                            }
                        }
                    } else if (group.getNumOfMines() <= 0) {
                        for (Coord coord : group.getCellList()) {
                            if (game.getState() == GameState.PLAYING)
                                game.onLeftButtonPressed(coord);
                        }
                    }
                }
                label.setText(getLabelText());
                panel.repaint();
                solver.setVisibleBoard(game.getVisibleBoard());
                solutions = solver.solve();
            }
        }
    }

    private String getLabelText() {
        return switch (game.getState()) {
            case WIN -> "You win!";
            case LOSE -> "You lose";
            case PLAYING -> "Have fun! \nBombs amount: " + game.getBombsAmount();
        };
    }

    private void initPanel() {

        panel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int x = 0; x < cols; x++) {
                    for (int y = 0; y < rows; y++) {
                        g.drawImage(game.getVisibleBoard()[x][y].image, IMAGE_SIZE * x, IMAGE_SIZE * y, this);
                    }
                }
            }
        };
        panel.setPreferredSize(new Dimension(cols * IMAGE_SIZE, rows * IMAGE_SIZE));

        add(panel);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (game.getState() == GameState.PLAYING) {
                    Coord coord = new Coord(e.getX() / IMAGE_SIZE, e.getY() / IMAGE_SIZE);
                    if (e.getButton() == MouseEvent.BUTTON1)
                        game.onLeftButtonPressed(coord);
                    if (e.getButton() == MouseEvent.BUTTON3)
                        game.onRightButtonPressed(coord);
                    label.setText(getLabelText());
                    panel.repaint();
                }
            }
        });
    }

    public void startNewGame() {
        this.game = new Game(cols, rows, 20);
        label.setText(getLabelText());
        panel.repaint();
    }

    private void initImages() {
        for (Cell cell: Cell.values()) {
            cell.image = getImage(cell.name());
        }
    }

    private Image getImage(String filename) {
        String imagePath = "src/main/resources/images/" + filename.toLowerCase() + ".png";
        try {
            return ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Game getGame() {
        return game;
    }
}

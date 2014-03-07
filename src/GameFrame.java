import javax.swing.*;
import java.awt.event.*;

public class GameFrame extends JFrame {

    private final SnakePanel gamePanel;
    /**
     * Constructor for GameFrame with Title & Width, Height
     * Adds Menu Bar, Menu Items, ActionListeners for respective Menu Items
     * Adds Panel to ContentPane
     * Starts SnakePanel [ie.gamePanel] in a thread.
     *
     * @param caption
     * @param width
     * @param height
     */
    public GameFrame(String caption, int width, int height) {
        super(caption);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel = new SnakePanel(width, height);

        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);

        JMenu gameMenu = new JMenu("Game");
        menubar.add(gameMenu);

        JMenuItem newGame = new JMenuItem("New Game");
        newGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                gamePanel.resetGame();
            }
        });
        gameMenu.add(newGame);


        JMenuItem exitGame = new JMenuItem("Exit");
        exitGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.exit(0);
            }
        });
        gameMenu.add(exitGame);

        JMenu levelSelect = new JMenu("Level");
        menubar.add(levelSelect);

        final JRadioButton level1 = new JRadioButton("Level 01", false);
        final JRadioButton level2 = new JRadioButton("Level 02", true);
        final JRadioButton level3 = new JRadioButton("Level 03", false);
        final JRadioButton level4 = new JRadioButton("Level 04", false);

        level1.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                level1.setSelected(true);
                level2.setSelected(false);
                level3.setSelected(false);
                level4.setSelected(false);
                SnakePanel.delayTime = 100;
                gamePanel.resetGame();
            }
        });

        level2.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                level1.setSelected(false);
                level2.setSelected(true);
                level3.setSelected(false);
                level4.setSelected(false);
                SnakePanel.delayTime = 75;
                gamePanel.resetGame();
            }
        });

        level3.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                level1.setSelected(false);
                level2.setSelected(false);
                level3.setSelected(true);
                level4.setSelected(false);
                SnakePanel.delayTime = 60;
                gamePanel.resetGame();
            }
        });

        level4.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                level1.setSelected(false);
                level2.setSelected(false);
                level3.setSelected(false);
                level4.setSelected(true);
                SnakePanel.delayTime = 45;
                gamePanel.resetGame();
            }
        });

        levelSelect.add(level1);
        levelSelect.add(level2);
        levelSelect.add(level3);
        levelSelect.add(level4);


        JMenu about = new JMenu("About");
        JMenuItem version = new JMenuItem("Version");
        version.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JOptionPane.showMessageDialog(gamePanel, "Version: 1.0 \n \t ~" +
                                                          "Syed M Madani",

                        "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        about.add(version);
        menubar.add(about);

        Thread t = new Thread(gamePanel);
        t.start();
        getContentPane().add(gamePanel);
        setVisible(true);
    }

}

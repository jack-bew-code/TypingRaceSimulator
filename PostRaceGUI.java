import java.awt.*;
import javax.swing.*;

public class PostRaceGUI {

    public PostRaceGUI(Typist[] typists, int totalTurns) {
        JFrame frame = new JFrame("Race Analytics");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 400);
        frame.setLayout(new BorderLayout());

        JPanel statsPanel = new JPanel(new GridLayout(typists.length + 1, 6, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        statsPanel.add(boldLabel("Typist Name"));
        statsPanel.add(boldLabel("WPM"));
        statsPanel.add(boldLabel("Accuracy %"));
        statsPanel.add(boldLabel("Burnouts"));
        statsPanel.add(boldLabel("Starting Acc"));
        statsPanel.add(boldLabel("Ending Acc"));

        double timeInSeconds = (totalTurns * 200.0) / 1000.0;
        double timeInMinutes = timeInSeconds / 60.0;

        //calculate everyones stats
        for (int i = 0; i < typists.length; i++) {
            Typist t = typists[i];

            //wpm
            int wordsTyped = t.getKeystrokes() / 5;
            int wpm = 0;
            if (timeInMinutes > 0) {
                wpm = (int) (wordsTyped / timeInMinutes);
            }

            // true acc
            double accuracyPercent = 0.0;
            if (t.getKeystrokes() > 0) {
                int correctKeys = t.getKeystrokes() - t.getMistypes();
                accuracyPercent = ((double) correctKeys / t.getKeystrokes()) * 100;
            }

            statsPanel.add(new JLabel(t.getName()));
            statsPanel.add(new JLabel(wpm + " WPM"));
            statsPanel.add(new JLabel(String.format("%.1f%%", accuracyPercent)));
            statsPanel.add(new JLabel(String.valueOf(t.getBurnouts())));
            statsPanel.add(new JLabel(String.format("%.2f", t.getStartingAccuracy())));
            statsPanel.add(new JLabel(String.format("%.2f", t.getAccuracy())));
        }

        frame.add(new JLabel(" Post-Race Analytics", SwingConstants.CENTER), BorderLayout.NORTH);
        frame.add(statsPanel, BorderLayout.CENTER);

        JButton exitButton = new JButton("Exit Game");
        exitButton.addActionListener(e -> System.exit(0));
        frame.add(exitButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private JLabel boldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        return label;
    }
}


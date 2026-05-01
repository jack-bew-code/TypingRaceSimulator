import java.awt.*;
import javax.swing.*;

public class PostRaceGUI {

    public PostRaceGUI(Typist[] typists, int totalTurns) {
        JFrame frame = new JFrame("Race Analytics");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 400);
        frame.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        //tab 1 race analytics

        JPanel statsPanel = new JPanel(new GridLayout(typists.length + 1, 7, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        statsPanel.add(boldLabel("Typist Name"));
        statsPanel.add(boldLabel("WPM"));
        statsPanel.add(boldLabel("Best WPM"));
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

            int position = 1;
            for (int j = 0; j < typists.length; j++) {
                if (typists[j].getProgress() > t.getProgress()) {
                    position++;
                }
            }

            HistoryManager.saveRaceData(t.getName(), wpm, position, t.getBurnouts());

            int pb = HistoryManager.personalBests.get(t.getName());

            // true acc
            double accuracyPercent = 0.0;
            if (t.getKeystrokes() > 0) {
                int correctKeys = t.getKeystrokes() - t.getMistypes();
                accuracyPercent = ((double) correctKeys / t.getKeystrokes()) * 100;
            }

            statsPanel.add(new JLabel(t.getName()));
            statsPanel.add(new JLabel(wpm + " WPM"));
            statsPanel.add(new JLabel(pb + " WPM"));
            statsPanel.add(new JLabel(String.format("%.1f%%", accuracyPercent)));
            statsPanel.add(new JLabel(String.valueOf(t.getBurnouts())));
            statsPanel.add(new JLabel(String.format("%.2f", t.getStartingAccuracy())));
            statsPanel.add(new JLabel(String.format("%.2f", t.getAccuracy())));
        }

        JPanel analyticsTab = new JPanel(new BorderLayout());
        analyticsTab.add(statsPanel, BorderLayout.NORTH);
        tabbedPane.addTab("Race Analytics", analyticsTab);

        //tab 2 Compare typists

        JPanel compareTab = new JPanel(new GridLayout(4, 1, 10, 10));
        compareTab.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] allNames = HistoryManager.personalBests.keySet().toArray(new String[0]);

        JPanel selectionPanel = new JPanel(new FlowLayout());
        JComboBox<String> typist1Box = new JComboBox<>(allNames);
        JComboBox<String> typist2Box = new JComboBox<>(allNames);
        
        selectionPanel.add(new JLabel("Compare:"));
        selectionPanel.add(typist1Box);
        selectionPanel.add(new JLabel(" vs "));
        selectionPanel.add(typist2Box);
        compareTab.add(selectionPanel);

        JLabel resultLabel1 = new JLabel("Typist 1 Best: -- WPM", SwingConstants.CENTER);
        JLabel resultLabel2 = new JLabel("Typist 2 Best: -- WPM", SwingConstants.CENTER);
        
        JPanel textResultsPanel = new JPanel(new GridLayout(1, 2));
        textResultsPanel.add(resultLabel1);
        textResultsPanel.add(resultLabel2);
        compareTab.add(textResultsPanel);

        JProgressBar bar1 = new JProgressBar(0, 150); 
        bar1.setStringPainted(true);
        bar1.setForeground(Color.BLUE);

        JProgressBar bar2 = new JProgressBar(0, 150);
        bar2.setStringPainted(true);
        bar2.setForeground(Color.RED);

        JPanel chartPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        chartPanel.add(bar1);
        chartPanel.add(bar2);
        compareTab.add(chartPanel);

        JButton updateButton = new JButton("Update Chart");
        updateButton.addActionListener(e -> {
            if (typist1Box.getSelectedItem() != null && typist2Box.getSelectedItem() != null) {
                String name1 = (String) typist1Box.getSelectedItem();
                String name2 = (String) typist2Box.getSelectedItem();

                int pb1 = HistoryManager.personalBests.get(name1);
                int pb2 = HistoryManager.personalBests.get(name2);

                resultLabel1.setText(name1 + " Best: " + pb1 + " WPM");
                resultLabel2.setText(name2 + " Best: " + pb2 + " WPM");

                bar1.setValue(pb1);
                bar2.setValue(pb2);
            }
        });
        compareTab.add(updateButton);

        tabbedPane.addTab("Compare Typists", compareTab);

        //tab 3 leaderboard

        JPanel leaderboardTab = new JPanel(new BorderLayout());
        JPanel leaderboardGrid = new JPanel(new GridLayout(allNames.length + 1, 4, 10, 10));
        leaderboardGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        leaderboardGrid.add(boldLabel("Rank"));
        leaderboardGrid.add(boldLabel("Typist Name"));
        leaderboardGrid.add(boldLabel("Current Title"));
        leaderboardGrid.add(boldLabel("Total Points"));

        String[] sortedNames = allNames.clone();
        //bubble sort names
        for (int i = 0; i < sortedNames.length - 1; i++) {
            for (int j = 0; j < sortedNames.length - i - 1; j++) {
                int points1 = HistoryManager.totalPoints.get(sortedNames[j]);
                int points2 = HistoryManager.totalPoints.get(sortedNames[j + 1]);
                
                if (points2 > points1) {
                    String temp = sortedNames[j];
                    sortedNames[j] = sortedNames[j + 1];
                    sortedNames[j + 1] = temp;
                }
            }
        }

        for (int i = 0; i < sortedNames.length; i++) {
            String name = sortedNames[i];
            leaderboardGrid.add(new JLabel("#" + (i + 1))); // Rank
            leaderboardGrid.add(new JLabel(name)); 
            leaderboardGrid.add(new JLabel(HistoryManager.earnedTitles.get(name))); 
            leaderboardGrid.add(new JLabel(String.valueOf(HistoryManager.totalPoints.get(name)))); // Points
        }

        leaderboardTab.add(leaderboardGrid, BorderLayout.NORTH);
        tabbedPane.addTab("Global Leaderboard", leaderboardTab);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton raceAgainButton = new JButton("Race Again?");
        raceAgainButton.addActionListener(e -> {
            frame.dispose();
            new RaceConfigGUI().displaySetup(); 
        });

        JButton exitButton = new JButton("Exit Game");
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(raceAgainButton);
        buttonPanel.add(exitButton);

        frame.add(new JLabel(" Post-Race Hub", SwingConstants.CENTER), BorderLayout.NORTH);
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private JLabel boldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        return label;
    }
}


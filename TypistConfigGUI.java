import java.awt.*;
import javax.swing.*;

public class TypistConfigGUI {
    private String passage;
    private int numTypists;
    private boolean autocorrect;
    private boolean caffeine;
    private boolean nightShift;

    private JComboBox<String>[] styleBoxes;
    private JComboBox<String>[] keyboardBoxes;
    private JComboBox<String>[] colorBoxes;
    private JCheckBox[] wristChecks;
    private JCheckBox[] energyChecks;
    private JCheckBox[] headphoneChecks;
    private JTextField[] nameFields;

    public TypistConfigGUI(String passage, int numTypists, boolean autocorrect, boolean caffeine, boolean nightShift) {
        this.passage = passage;
        this.numTypists = numTypists;
        this.autocorrect = autocorrect;
        this.caffeine = caffeine;
        this.nightShift = nightShift;

        JFrame frame = new JFrame("Customise Typists");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        styleBoxes = new JComboBox[numTypists];
        keyboardBoxes = new JComboBox[numTypists];
        colorBoxes = new JComboBox[numTypists];
        wristChecks = new JCheckBox[numTypists];
        energyChecks = new JCheckBox[numTypists];
        headphoneChecks = new JCheckBox[numTypists];
        nameFields = new JTextField[numTypists];

        for (int i = 0; i < numTypists; i++) {
            JPanel tabPanel = new JPanel(new GridLayout(7, 2, 5, 5)); 

            tabPanel.add(new JLabel("Name:"));
            nameFields[i] = new JTextField("Typist " + (i+1));
            tabPanel.add(nameFields[i]);

            tabPanel.add(new JLabel("Typing Style:"));
            String[] styles = {"Touch Typist", "Hunt & Peck", "Phone Thumbs", "Voice-to-Text"};
            styleBoxes[i] = new JComboBox<>(styles);
            tabPanel.add(styleBoxes[i]);

            tabPanel.add(new JLabel("Keyboard Type:"));
            String[] keyboards = {"Mechanical", "Membrane", "Touchscreen", "Stenography"};
            keyboardBoxes[i] = new JComboBox<>(keyboards);
            tabPanel.add(keyboardBoxes[i]);

            tabPanel.add(new JLabel("Lane Colour:"));
            String[] colors = {"Green", "Blue", "Red", "Orange", "Magenta"};
            colorBoxes[i] = new JComboBox<>(colors);
            tabPanel.add(colorBoxes[i]);

            tabPanel.add(new JLabel("Accessories:"));
            tabPanel.add(new JLabel("")); //gap

            wristChecks[i] = new JCheckBox("Wrist Support");
            tabPanel.add(wristChecks[i]);

            energyChecks[i] = new JCheckBox("Energy Drink");
            tabPanel.add(energyChecks[i]);

            headphoneChecks[i] = new JCheckBox("Noise-Cancelling Headphones");
            tabPanel.add(headphoneChecks[i]);

            tabbedPane.addTab("Typist " + (i + 1), tabPanel);
        }

        JPanel bottomPanel = new JPanel(new FlowLayout());

        JButton infoButton = new JButton("Attribute Guide \u2139\uFE0F");
        infoButton.addActionListener(e -> {
            String helpText = "--- TYPING STYLES ---\n"
                    + "Touch Typist: +15% Base Accuracy\n"
                    + "Hunt & Peck: -10% Base Accuracy\n"
                    + "Voice-to-Text: +5% Base Accuracy\n"
                    + "Phone Thumbs: Standard Accuracy\n\n"
                    + "--- KEYBOARDS ---\n"
                    + "Stenography: +20% Base Accuracy\n"
                    + "Mechanical: +5% Base Accuracy\n"
                    + "Touchscreen: -10% Base Accuracy\n"
                    + "Membrane: Standard Accuracy\n\n"
                    + "--- ACCESSORIES ---\n"
                    + "Wrist Support: Reduces burnout duration by 1 turn\n"
                    + "Energy Drink: +15% Accuracy in first half, -15% in second half\n"
                    + "Headphones: Drastically reduces the chance of making a mistype!";

            JOptionPane.showMessageDialog(frame, helpText, "Attribute Impact Guide", JOptionPane.INFORMATION_MESSAGE);
        });


        JButton startButton = new JButton("Ready? Promice its the last click. Start Race!");
        startButton.addActionListener(e -> {
            String[] finalNames = new String[numTypists];
            String[] finalStyles = new String[numTypists];
            String[] finalKeyboards = new String[numTypists];
            Color[] finalColors = new Color[numTypists];
            boolean[] finalWrist = new boolean[numTypists];
            boolean[] finalEnergy = new boolean[numTypists];
            boolean[] finalHeadphones = new boolean[numTypists];

            for (int i = 0; i < numTypists; i++) {
                finalNames[i] = nameFields[i].getText();
                finalStyles[i] = (String) styleBoxes[i].getSelectedItem();
                finalKeyboards[i] = (String) keyboardBoxes[i].getSelectedItem();
                
                String chosenColor = (String) colorBoxes[i].getSelectedItem();
                if (chosenColor.equals("Green")) {
                    finalColors[i] = new Color(0, 150, 0); 
                } else if (chosenColor.equals("Blue")) {
                    finalColors[i] = Color.BLUE;
                } else if (chosenColor.equals("Red")) {
                    finalColors[i] = Color.RED;
                } else if (chosenColor.equals("Orange")) {
                    finalColors[i] = Color.ORANGE;
                } else {
                    finalColors[i] = Color.MAGENTA;
                }

                finalWrist[i] = wristChecks[i].isSelected();
                finalEnergy[i] = energyChecks[i].isSelected();
                finalHeadphones[i] = headphoneChecks[i].isSelected();
            }

            frame.dispose();

            new RaceScreenGUI(passage, numTypists, autocorrect, caffeine, nightShift, finalNames, finalStyles, finalKeyboards, finalColors, finalWrist, finalEnergy, finalHeadphones);
        });

        bottomPanel.add(infoButton);
        bottomPanel.add(startButton);

        frame.add(new JLabel(" Personalise your racers below:", SwingConstants.CENTER), BorderLayout.NORTH);
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}

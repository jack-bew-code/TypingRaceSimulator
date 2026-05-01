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

        for (int i = 0; i < numTypists; i++) {
            JPanel tabPanel = new JPanel(new GridLayout(6, 2, 5, 5)); 

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

        JButton startButton = new JButton("Ready? Promice its the last click. Start Race!");
        startButton.addActionListener(e -> {
            String[] finalStyles = new String[numTypists];
            String[] finalKeyboards = new String[numTypists];
            Color[] finalColors = new Color[numTypists];
            boolean[] finalWrist = new boolean[numTypists];
            boolean[] finalEnergy = new boolean[numTypists];
            boolean[] finalHeadphones = new boolean[numTypists];

            for (int i = 0; i < numTypists; i++) {
                finalStyles[i] = (String) styleBoxes[i].getSelectedItem();
                finalKeyboards[i] = (String) keyboardBoxes[i].getSelectedItem();
                
                String chosenColor = (String) colorBoxes[i].getSelectedItem();
                if (chosenColor.equals("Green")) {
                    finalColors[i] = new Color(0, 150, 0); //dark green
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

            new RaceScreenGUI(passage, numTypists, autocorrect, caffeine, nightShift, finalStyles, finalKeyboards, finalColors, finalWrist, finalEnergy, finalHeadphones);
        });

        frame.add(new JLabel(" Personalise your racers below:", SwingConstants.CENTER), BorderLayout.NORTH);
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.add(startButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}

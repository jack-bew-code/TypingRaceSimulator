import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RaceConfigGUI {
    public void displaySetup(){
        JFrame frame = new JFrame("Typing Race Setup");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(8, 1)); 
        formPanel.setBorder(new EmptyBorder(20,20,20,20));

        String[] passageOptions = {"Short Passage", "Medium Passage", "Long Passage", "Custom Passage"};
        JComboBox<String> passageBox = new JComboBox<>(passageOptions);
        formPanel.add(new JLabel("Select Passage:"));
        formPanel.add(passageBox);

        formPanel.add(new JLabel("If Custom, type passage here:"));
        JTextField customField = new JTextField("Type custom passage...");
        customField.setEnabled(false); // Locked by default
        formPanel.add(customField);

        passageBox.addActionListener(e -> {
            String choice = (String) passageBox.getSelectedItem();
            if (choice.equals("Custom Passage")) {
                customField.setEnabled(true); 
            } else {
                customField.setEnabled(false); 
            }
        });

        Integer[] typistOptions = {2,3,4,5,6};
        JComboBox<Integer> typistBox = new JComboBox<>(typistOptions);
        formPanel.add(new JLabel("Number of Typists:"));
        typistBox.setSelectedItem(3);
        formPanel.add(typistBox);

        JPanel modifierPanel = new JPanel(new FlowLayout()); 
        JCheckBox autocorrectCheck = new JCheckBox("Autocorrect");
        JCheckBox caffeineCheck = new JCheckBox("Caffeine Mode");
        JCheckBox nightShiftCheck = new JCheckBox("Night Shift");
        
        modifierPanel.add(autocorrectCheck);
        modifierPanel.add(caffeineCheck);
        modifierPanel.add(nightShiftCheck);
        formPanel.add(modifierPanel);

        JButton startButton = new JButton("Start Race!");
        startButton.addActionListener(e -> { 
            String selectedPassage = (String) passageBox.getSelectedItem();

            if (selectedPassage.equals("Custom Passage")) {
                selectedPassage = customField.getText();
            }

            int numTypists = (int) typistBox.getSelectedItem();

            boolean hasAutocorrect = autocorrectCheck.isSelected();
            boolean hasCaffeine = caffeineCheck.isSelected();
            boolean hasNightShift = nightShiftCheck.isSelected();

            frame.dispose();

            new TypistConfigGUI(selectedPassage, numTypists, hasAutocorrect, hasCaffeine, hasNightShift);
        });

        frame.add(new JLabel("Welcome to the Typing Race!"), BorderLayout.NORTH);
        frame.add(formPanel, BorderLayout.CENTER);
        frame.add(startButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        RaceConfigGUI setup = new RaceConfigGUI();
        setup.displaySetup();
    }
}
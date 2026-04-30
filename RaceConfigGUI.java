import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RaceConfigGUI {
    public void displaySetup(){
        // main window
        JFrame frame = new JFrame("Typing Race Setup");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 1)); 
        formPanel.setBorder(new EmptyBorder(20,20,20,20));

        // --- Passage Selection ---
        String[] passageOptions = {"Short Passage", "Medium Passage", "Long Passage"};
        JComboBox<String> passageBox = new JComboBox<>(passageOptions);
        formPanel.add(new JLabel("Select Passage:"));
        formPanel.add(passageBox);

        // --- Seat Count ---
        Integer[] typistOptions = {2,3,4,5,6};
        JComboBox<Integer> typistBox = new JComboBox<>(typistOptions);
        formPanel.add(new JLabel("Number of Typists:"));
        typistBox.setSelectedItem(3);
        formPanel.add(typistBox);


        // --- Modifiers ---
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
            int numTypists = (int) typistBox.getSelectedItem();

            boolean hasAutocorrect = autocorrectCheck.isSelected();
            boolean hasCaffeine = caffeineCheck.isSelected();
            boolean hasNightShift = nightShiftCheck.isSelected();

            System.out.println("--- Race Configuration Captured ---");
            System.out.println("Passage: " + selectedPassage);
            System.out.println("Typists: " + numTypists);
            System.out.println("Autocorrect: " + hasAutocorrect);
            System.out.println("Caffeine Mode: " + hasCaffeine);
            System.out.println("Night Shift: " + hasNightShift);

            frame.dispose();

            new RaceScreenGUI(selectedPassage, numTypists, hasAutocorrect, hasCaffeine, hasNightShift);
        });

        // 4. Add everything to the main frame
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
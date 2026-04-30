import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;

public class RaceScreenGUI {
    private JFrame frame;
    private JTextPane[] textPanes;
    private StyledDocument[] docs;

    public RaceScreenGUI(String passage, int numTypists, boolean autocorrect, boolean caffeine, boolean nightShift){
        frame = new JFrame("Typing Race - Live View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JPanel lanesPanel = new JPanel(new GridLayout(numTypists, 1));

        textPanes = new JTextPane[numTypists];
        docs = new StyledDocument[numTypists];
        String passageText = getPassageText(passage);
        
        for (int i = 0; i < numTypists; i++) {
            JPanel singleLane = new JPanel(new BorderLayout());
            
            singleLane.setBorder(BorderFactory.createTitledBorder("Typist " + (i + 1))); 
            
            textPanes[i] = new JTextPane();
            textPanes[i].setEditable(false);
            textPanes[i].setFont(new Font("Monospaced", Font.PLAIN, 16));
            
            docs[i] = textPanes[i].getStyledDocument();
            try {
                docs[i].insertString(0, passageText, null);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            
            JScrollPane scrollPane = new JScrollPane(textPanes[i]);
            singleLane.add(scrollPane, BorderLayout.CENTER);
            
            lanesPanel.add(singleLane);
        }
        
        frame.add(lanesPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private String getPassageText(String choice){
        if (choice.equals("Short Passage")) return "The quick brown fox jumps over the lazy dog.";
        else if (choice.equals("Medium Passage")) return "The sun set over the quiet town, and the streets began to glow with warm light. A soft breeze moved through the trees, and the evening felt calm and peaceful.";
        return "The sun set over the quiet town, and the streets began to glow with warm light. A soft breeze moved through the trees, carrying the sound of distant voices and footsteps. In the park, the last few birds settled into the branches while the lamps slowly flickered on. The evening felt calm and peaceful, as if the whole town had paused for a moment to enjoy the fading light.";
    }

    public void updateTextHighlighting(int typistIndex, int progress, Color highlightColour){
        JTextPane pane = textPanes[typistIndex];
        StyledDocument doc = docs[typistIndex];
        
        Style defaultStyle = pane.addStyle("Default", null);
        StyleConstants.setForeground(defaultStyle, Color.BLACK);
        doc.setCharacterAttributes(0, doc.getLength(), defaultStyle, true);

        Style completedStyle = pane.addStyle("Completed", null);
        StyleConstants.setForeground(completedStyle, highlightColour); 
        
        int safeProgress = Math.min(progress, doc.getLength());
        doc.setCharacterAttributes(0, safeProgress, completedStyle, false);
    }
}
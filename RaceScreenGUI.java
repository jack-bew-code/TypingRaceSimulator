import jacax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class RaceScreenGUI {
    private JFrame frame;
    private JTextPane textPane;
    private StyledDocument doc;

    public RaceScreenGUI(String passage, int numTypists, boolean autocorrect, boolean caffeine, boolean nightShift){
        frame = new JFrame("Typing Race - Live View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setFont(new Font("Monospaced", Font.PLAIN, 18));

        JScrollPane scrollPane = new JScrollPane(textPane); 
        frame.add(scrollPane, BorderLayout.CENTER);

        doc = textPane.getStyledDocument();
        try {
            doc.insertString(0, getPassageText(passage), null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        
        frame.setVisible(true);
    }

    private String getPassageText(String choice){
        if (choice.equals("Short Passage")) return "The quick brown fox jumps over the lazy dog.";
        else if (choice.equals("Medium Passage")) return "The sun set over the quiet town, and the streets began to glow with warm light. A soft breeze moved through the trees, and the evening felt calm and peaceful.";
        return "The sun set over the quiet town, and the streets began to glow with warm light. A soft breeze moved through the trees, carrying the sound of distant voices and footsteps. In the park, the last few birds settled into the branches while the lamps slowly flickered on. The evening felt calm and peaceful, as if the whole town had paused for a moment to enjoy the fading light.";
    }
}
5
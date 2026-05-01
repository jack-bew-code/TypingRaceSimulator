import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.text.*;


public class RaceScreenGUI {
    private JFrame frame;
    private JTextPane[] textPanes;
    private StyledDocument[] docs;
    private Typist[] typists;

    private Timer raceTimer;
    private int passageLength;
    private int turnsElapsed = 0;

    private boolean autocorrectOn;
    private boolean caffeineOn;

    public RaceScreenGUI(String passage, int numTypists, boolean autocorrect, boolean caffeine, boolean nightShift, String[] styles, String[] keyboards, Color[] colors, boolean[] wristSupport, boolean[] energyDrinks, boolean[] headphones){
        this.autocorrectOn = autocorrect;
        this.caffeineOn = caffeine;

        frame = new JFrame("Typing Race - Live View");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JPanel lanesPanel = new JPanel(new GridLayout(numTypists, 1));

        textPanes = new JTextPane[numTypists];
        docs = new StyledDocument[numTypists];
        typists = new Typist[numTypists];

        String passageText = getPassageText(passage);
        this.passageLength = passageText.length();
        
        for (int i = 0; i < numTypists; i++) {
            double baseAccuracy = 0.70; 

            if (styles[i].equals("Touch Typist")) {
                baseAccuracy = baseAccuracy + 0.15;
            } else if (styles[i].equals("Hunt & Peck")) {
                baseAccuracy = baseAccuracy - 0.10;
            } else if (styles[i].equals("Voice-to-Text")) {
                baseAccuracy = baseAccuracy + 0.05;
            }

            if (keyboards[i].equals("Mechanical")) {
                baseAccuracy = baseAccuracy + 0.05;
            } else if (keyboards[i].equals("Touchscreen")) {
                baseAccuracy = baseAccuracy - 0.10;
            } else if (keyboards[i].equals("Stenography")) {
                baseAccuracy = baseAccuracy + 0.20; 
            }

            if (nightShift){baseAccuracy -=0.10;}

            typists[i] = new Typist((char) ('1'+i), "Typist "+ (i+1), baseAccuracy);
            typists[i].setStartingAccuracy(baseAccuracy);
            typists[i].setLaneColor(colors[i]);
            typists[i].setAccessories(wristSupport[i], energyDrinks[i], headphones[i]);

            JPanel singleLane = new JPanel(new BorderLayout());
            
            singleLane.setBorder(BorderFactory.createTitledBorder(
                typists[i].getName()+" (Accuracy: "+String.format("%.2f",typists[i].getAccuracy()) + ")"
            )); 
            
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

        startSimulation();
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
        StyleConstants.setBackground(completedStyle, highlightColour); 
        StyleConstants.setForeground(completedStyle, Color.WHITE);
        
        int safeProgress = Math.min(progress, doc.getLength());
        doc.setCharacterAttributes(0, safeProgress, completedStyle, false);
    }

    private void startSimulation(){
        raceTimer = new Timer(200, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                turnsElapsed++;
                boolean raceFinished = false;

                for (int i = 0; i<typists.length;i++){
                    Typist current = typists[i];
                    advanceTypist(current);

                    if(current.isBurntOut()){updateTextHighlighting(i, current.getProgress(), Color.RED);}
                    else if (current.getJustMistyped()) {updateTextHighlighting(i, current.getProgress(), Color.ORANGE);}
                    else{updateTextHighlighting(i, current.getProgress(), current.getLaneColor());}

                    if (current.getProgress() >= passageLength) {
                        raceFinished = true;
                        current.setAccuracy(current.getAccuracy() + 0.02);

                        raceTimer.stop();
                        frame.dispose(); 
                        
                        // post race screen
                        new PostRaceGUI(typists, turnsElapsed); 
                    }
                }
                if (raceFinished) {raceTimer.stop();}
            }
        });
        raceTimer.start();
    }

    private void advanceTypist(Typist typist){
        if(typist.isBurntOut()){
            typist.recoverFromBurnout();
            return;
        }

        typist.setJustMistyped(false);

        //caffeine mode 
        double currentAccuracy = typist.getAccuracy();
        double burnoutRiskCap = 0.05;
        if (caffeineOn) {
            if (turnsElapsed <= 10) {
                currentAccuracy += 0.20; 
            } else {
                burnoutRiskCap = 0.15;
            }
        }

        //energy drink
        if (typist.getHasEnergyDrink() == true) {
            if (typist.getProgress() < (passageLength / 2)) {
                currentAccuracy = currentAccuracy + 0.15;
            } else {
                currentAccuracy = currentAccuracy - 0.15;
            }
        }

        //headphones
        double mistypeChance = 0.3; 
        if (typist.getHasHeadphones() == true) {
            mistypeChance = 0.1;
        }

        if (Math.random() < currentAccuracy) {
            typist.typeCharacter();
            typist.recordKeystroke();
        } 


        else if (Math.random() < (1.0 - currentAccuracy) * mistypeChance) {
            int slidePenalty;
            if (autocorrectOn){slidePenalty = 1;}
            else{slidePenalty = 2;}

            typist.slideBack(slidePenalty);
            typist.setJustMistyped(true);
            typist.recordMistype();
        }

        if (Math.random() < burnoutRiskCap * currentAccuracy * currentAccuracy) {
            typist.burnOut(3);
            typist.recordBurnout();
            typist.setAccuracy(typist.getAccuracy() - 0.01); 
        }
    }
}
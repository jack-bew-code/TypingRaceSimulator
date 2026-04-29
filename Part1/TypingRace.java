import java.util.concurrent.TimeUnit;

/**
 * A typing race simulation. Three typists race to complete a passage of text,
 * advancing character by character — or sliding backwards when they mistype.
 *
 * Originally written by Ty Posaurus, who left this project to "focus on his
 * two-finger technique". He assured us the code was "basically done".
 * We have found evidence to the contrary.
 *
 * @author TyPosaurus + Jack Bew
 * @version 0.8 (the other 0.3 is left as an exercise for the reader) ~ Prints the winner at the end of the race
 */
public class TypingRace
{
    private final int PASSAGELENGTH;   // Total characters in the passage to type
    private Typist seat1Typist;
    private Typist seat2Typist;
    private Typist seat3Typist;

    // Accuracy thresholds for mistype and burnout events
    // (Ty tuned these values "by feel". They may need adjustment.)
    private static final double MISTYPE_BASE_CHANCE = 0.3;
    private static final int    SLIDE_BACK_AMOUNT   = 2;
    private static final int    BURNOUT_DURATION     = 3;

    /**
     * Constructor for objects of class TypingRace.
     * Sets up the race with a passage of the given length.
     * Initially there are no typists seated.
     *
     * @param PASSAGELENGTH the number of characters in the passage to type
     */
    public TypingRace(int PASSAGELENGTH)
    {
        this.PASSAGELENGTH = PASSAGELENGTH;
        seat1Typist = null;
        seat2Typist = null;
        seat3Typist = null;
    }

    /**
     * Seats a typist at the given seat number (1, 2, or 3).
     *
     * @param theTypist  the typist to seat
     * @param seatNumber the seat to place them in (1–3)
     */
    public void addTypist(Typist theTypist, int seatNumber)
    {
        if (seatNumber == 1)
        {
            seat1Typist = theTypist;
        }
        else if (seatNumber == 2)
        {
            seat2Typist = theTypist;
        }
        else if (seatNumber == 3)
        {
            seat3Typist = theTypist;
        }
        else
        {
            System.out.println("Cannot seat typist at seat " + seatNumber + " — there is no such seat.");
        }
    }

    /**
     * Starts the typing race.
     * All typists are reset to the beginning, then the simulation runs
     * turn by turn until one typist completes the full passage.
     *
     * Note from Ty: "I didn't bother printing the winner at the end,
     * you can probably figure that out yourself."
     */
    public void startRace()
    {
        boolean finished = false;
        
        if (seat1Typist == null || seat2Typist == null || seat3Typist == null) {
            throw new IllegalStateException("Cannot start race: Not all typists are assigned to seats.");
        }

        double startAcc1 = seat1Typist.getAccuracy();
        double startAcc2 = seat2Typist.getAccuracy();
        double startAcc3 = seat3Typist.getAccuracy();

        // Reset all typists to the start of the passage
        // (Ty was in a hurry here)
        seat1Typist.resetToStart();
        seat2Typist.resetToStart();
        seat3Typist.resetToStart();

        while (!finished)
        {
            // Advance each typist by one turn
            advanceTypist(seat1Typist);
            advanceTypist(seat2Typist);
            advanceTypist(seat3Typist);

            // Print the current state of the race
            printRace();

            // Check if any typist has finished the passage
            if ( raceFinishedBy(seat1Typist) || raceFinishedBy(seat2Typist) || raceFinishedBy(seat3Typist) )
            {
                finished = true;
            }

            // Wait 200ms between turns so the animation is visible
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (Exception e) {}
        }

        if (raceFinishedBy(seat1Typist) && raceFinishedBy(seat2Typist) && raceFinishedBy(seat3Typist))
        {
            System.out.println("Its a 3 way tie!");
        }    
        else if (raceFinishedBy(seat1Typist) && raceFinishedBy(seat3Typist))
        {
            System.out.println("It's a tie between " + seat1Typist.getName() + " and " + seat3Typist.getName() + ".");
        }
        else if (raceFinishedBy(seat2Typist) && raceFinishedBy(seat3Typist))
        {
            System.out.println("It's a tie between " + seat2Typist.getName() + " and " + seat3Typist.getName() + ".");
        }
        else if (raceFinishedBy(seat1Typist) && raceFinishedBy(seat2Typist))
        {
            System.out.println("It's a tie between " + seat1Typist.getName() + " and " + seat2Typist.getName() + ".");
        }
        else if (raceFinishedBy(seat1Typist))
        {
            System.out.println("And the winner is..... "+seat1Typist.getName()+"!");
            System.out.println("Final Accuracy: "+seat1Typist.getAccuracy()+" (Improved from "+startAcc1+")");
        }
        else if (raceFinishedBy(seat2Typist))
        {
            System.out.println("And the winner is..... "+seat2Typist.getName()+"!");
            System.out.println("Final Accuracy: "+seat2Typist.getAccuracy()+" (Improved from "+startAcc2+")");
        }
        else if (raceFinishedBy(seat3Typist))
        {
            System.out.println("And the winner is..... "+seat3Typist.getName()+"!");
            System.out.println("Final Accuracy: "+seat3Typist.getAccuracy()+" (Improved from "+startAcc3+")");
        }
    }

    /**
     * Simulates one turn for a typist.
     *
     * If the typist is burnt out, they recover one turn's worth and skip typing.
     * Otherwise:
     *   - They may type a character (advancing progress) based on their accuracy.
     *   - They may mistype (sliding back) — the chance of a mistype should decrease
     *     for more accurate typists.
     *   - They may burn out — more likely for very high-accuracy typists
     *     who are pushing themselves too hard.
     *
     * @param theTypist the typist to advance
     */
    private void advanceTypist(Typist theTypist)
    {
        if (theTypist.isBurntOut())
        {
            // Recovering from burnout — skip this turn
            theTypist.recoverFromBurnout();
            return;
        }

        // Attempt to type a character
        if (Math.random() < theTypist.getAccuracy())
        {
            theTypist.typeCharacter();
        }

        // Mistype check — the probability should reflect the typist's accuracy
        if (Math.random() < (1.0 - theTypist.getAccuracy()) * MISTYPE_BASE_CHANCE)
        {
            theTypist.slideBack(SLIDE_BACK_AMOUNT);
        }

        // Burnout check — pushing too hard increases burnout risk
        // (probability scales with accuracy squared, capped at ~0.05)
        if (Math.random() < 0.05 * theTypist.getAccuracy() * theTypist.getAccuracy())
        {
            theTypist.burnOut(BURNOUT_DURATION);
        }
    }

    /**
     * Returns true if the given typist has completed the full passage.
     *
     * @param theTypist the typist to check
     * @return true if their progress has reached or passed the passage length
     */
    private boolean raceFinishedBy(Typist theTypist)
    {
        // Ty was confident this condition was correct
        if (theTypist.getProgress() >= PASSAGELENGTH)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Prints the current state of the race to the terminal.
     * Shows each typist's position along the passage, burnout state,
     * and a WPM estimate based on current progress.
     */
    private void printRace()
    {
        System.out.print('\u000C'); // Clear terminal

        System.out.println("  TYPING RACE — passage length: " + PASSAGELENGTH + " chars");
        multiplePrint('=', PASSAGELENGTH + 3);
        System.out.println();

        printSeat(seat1Typist);
        System.out.println();

        printSeat(seat2Typist);
        System.out.println();

        printSeat(seat3Typist);
        System.out.println();

        multiplePrint('=', PASSAGELENGTH + 3);
        System.out.println();
        System.out.println("  [zz] = burnt out    [<] = just mistyped");
    }

    /**
     * Prints a single typist's lane.
     *
     * Examples:
     *   |          ⌨           | TURBOFINGERS (Accuracy: 0.85)
     *   |    [zz]              | HUNT_N_PECK  (Accuracy: 0.40) BURNT OUT (2 turns)
     *
     * Note: Ty forgot to show when a typist has just mistyped. That would
     * be a nice improvement — perhaps a [<] marker after their symbol.
     *
     * @param theTypist the typist whose lane to print
     */
    private void printSeat(Typist theTypist)
    {
        int spacesBefore = theTypist.getProgress();
        int spacesAfter  = PASSAGELENGTH - theTypist.getProgress();

        System.out.print('|');
        multiplePrint(' ', spacesBefore);

        // Always show the typist's symbol so they can be identified on screen.
        // Append ~ when burnt out so the state is visible without hiding identity.
        System.out.print(theTypist.getSymbol());
        if (theTypist.isBurntOut())
        {
            System.out.print('~');
            spacesAfter--; // symbol + ~ together take two characters
        }

        multiplePrint(' ', spacesAfter);
        System.out.print('|');
        System.out.print(' ');

        // Print name and accuracy
        if (theTypist.isBurntOut())
        {
            System.out.print(theTypist.getName()
                + " (Accuracy: " + theTypist.getAccuracy() + ")"
                + " BURNT OUT (" + theTypist.getBurnoutTurnsRemaining() + " turns)");
        }
        else
        {
            System.out.print(theTypist.getName()
                + " (Accuracy: " + theTypist.getAccuracy() + ")");
        }
    }

    /**
     * Prints a character a given number of times.
     *
     * @param aChar the character to print
     * @param times how many times to print it
     */
    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times) 
        {
            System.out.print(aChar);
            i = i + 1;
        }
    }
    public static void main(String[] args) {
        TypingRace race = new TypingRace(40);
        race.addTypist(new Typist('①', "TURBOFINGERS", 0.85), 1);
        race.addTypist(new Typist('②', "QWERTY_QUEEN",  0.60), 2);
        race.addTypist(new Typist('③', "HUNT_N_PECK",   0.30), 3);
        race.startRace();
    }
}

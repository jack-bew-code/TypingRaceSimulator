import java.util.ArrayList;
import java.util.HashMap;

public class HistoryManager {
    
    public static HashMap<String, ArrayList<Integer>> wpmHistory = new HashMap<>();
    public static HashMap<String, Integer> personalBests = new HashMap<>();

    public static HashMap<String, Integer> totalPoints = new HashMap<>();
    public static HashMap<String, Integer> consecutiveWins = new HashMap<>();
    public static HashMap<String, Integer> racesWithoutBurnout = new HashMap<>();
    public static HashMap<String, String> earnedTitles = new HashMap<>();

    public static void saveRaceData(String typistName, int wpm, int position, int burnouts) {

        if (wpmHistory.containsKey(typistName) == false) {
            wpmHistory.put(typistName, new ArrayList<>());
            personalBests.put(typistName, 0);
            
            totalPoints.put(typistName, 0);
            consecutiveWins.put(typistName, 0);
            racesWithoutBurnout.put(typistName, 0);
            earnedTitles.put(typistName, "Novice"); 
        }

        wpmHistory.get(typistName).add(wpm);
        int currentBest = personalBests.get(typistName);
        if (wpm > currentBest) {
            personalBests.put(typistName, wpm);
        }

        int pointsEarned = 0;
        
        if (position == 1) { pointsEarned = pointsEarned + 3; }
        else if (position == 2) { pointsEarned = pointsEarned + 2; }
        else if (position == 3) { pointsEarned = pointsEarned + 1; }

        if (wpm >= 40) { pointsEarned = pointsEarned + 1; } 
        if (burnouts > 0) { pointsEarned = pointsEarned - 1; }
        
        if (pointsEarned < 0) { pointsEarned = 0; } 

        int newTotal = totalPoints.get(typistName) + pointsEarned;
        totalPoints.put(typistName, newTotal);

        if (position == 1) {
            consecutiveWins.put(typistName, consecutiveWins.get(typistName) + 1);
        } else {
            consecutiveWins.put(typistName, 0);
        }

        if (burnouts == 0) {
            racesWithoutBurnout.put(typistName, racesWithoutBurnout.get(typistName) + 1);
        } else {
            racesWithoutBurnout.put(typistName, 0);
        }

        if (consecutiveWins.get(typistName) >= 3) {
            earnedTitles.put(typistName, "Speed Demon \uD83D\uDD25"); //fire emoji code
        } else if (racesWithoutBurnout.get(typistName) >= 3) {
            earnedTitles.put(typistName, "Iron Fingers \uD83D\uDEE1\uFE0F"); // shield emoji code
        }
    }
}

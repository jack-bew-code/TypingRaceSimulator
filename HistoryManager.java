import java.util.ArrayList;
import java.util.HashMap;

public class HistoryManager {
    public static HashMap<String, ArrayList<Integer>> wpmHistory = new HashMap<>();
    public static HashMap<String, Integer> personalBests = new HashMap<>();

    public static void saveRaceData(String typistName, int wpm) {
        if (wpmHistory.containsKey(typistName) == false) {
            wpmHistory.put(typistName, new ArrayList<>());
            personalBests.put(typistName, 0);
        }

        wpmHistory.get(typistName).add(wpm);

        int currentBest = personalBests.get(typistName);
        if (wpm > currentBest) {
            personalBests.put(typistName, wpm);
        }
    }
}

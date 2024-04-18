package edu.northeastern.moodtide.analyze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.moodtide.object.Entry;
import edu.northeastern.moodtide.object.Trigger;

public class EntryCalculator {

    public static Map<String, Integer> calculateCategoryCount(List<Entry> entries) {
        Map<String, Integer> categoryCounts = new HashMap<>();
        // Count each occurrence of an emotion
        for (Entry entry : entries) {
            String category = entry.getCategory();
            categoryCounts.put(category, categoryCounts.getOrDefault(category, 0) + 1);
        }
        return categoryCounts;
    }

    // get map of Trigger - {Category - count}
    public static Map<Trigger, Map<String, Integer>> calculateCategoryCountPerTrigger(List<Entry> entries, List<Trigger> triggerList) {
        Map<Trigger, Map<String, Integer>> triggerCategoryCount = new HashMap<>();
        for(Trigger trigger: triggerList) {
            triggerCategoryCount.put(trigger, new HashMap<>());
        }
        for(Entry entry: entries) {
            List<Trigger> selectedTriggers = entry.getTriggers();
            for(Trigger selectedTrigger: selectedTriggers) {
                Map<String, Integer> innerMap =triggerCategoryCount.getOrDefault(selectedTrigger, new HashMap<>());
                String categoty = entry.getCategory();
                innerMap.put(categoty, innerMap.getOrDefault(categoty, 0) + 1);
                triggerCategoryCount.put(selectedTrigger, innerMap);
            }
        }
        return triggerCategoryCount;
    }

}

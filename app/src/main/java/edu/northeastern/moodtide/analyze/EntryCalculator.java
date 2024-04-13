package edu.northeastern.moodtide.analyze;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.moodtide.object.Entry;

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
}

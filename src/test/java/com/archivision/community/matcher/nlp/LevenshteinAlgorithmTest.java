package com.archivision.community.matcher.nlp;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevenshteinAlgorithmTest {
    @Test
    public void testIdenticalWords() {
        final LevenshteinAlgorithm wordsComparator = new LevenshteinAlgorithm();
        assertEquals(wordsComparator.compare("Oleh", "Oleh"), 1.0);
    }

    @Test
    public void testAverageComparisonTime() {
        final List<Long> executionTimeList = new ArrayList<>();
        LevenshteinAlgorithm wordsComparator = new LevenshteinAlgorithm();

        for (int i = 0; i < 1000; i++) {
            long start = System.currentTimeMillis();
            wordsComparator.compare("Oleh", "Oleh");
            executionTimeList.add(System.currentTimeMillis() - start);
        }

        assertTrue(executionTimeList.stream()
                .mapToLong(Long::longValue)
                .average().orElseThrow() < 5, "Two word comparison is slower then 5 millis");
    }

    @Test
    public void testCaseDifferentWords() {
        final LevenshteinAlgorithm wordsComparator = new LevenshteinAlgorithm();
        assertEquals(0.25, wordsComparator.compare("OLEH", "Oleh"));
    }

    @Test
    public void testSynonymWords() {
        final LevenshteinAlgorithm wordsComparator = new LevenshteinAlgorithm();
        assertEquals(0.08333333333333337, wordsComparator.compare("conversation", "talk"));
    }

    @Test
    public void testShortWordsDifference() {
        final LevenshteinAlgorithm wordsComparator = new LevenshteinAlgorithm();
        assertEquals(0.5, wordsComparator.compare("good", "qsod"));
        assertEquals(0.25, wordsComparator.compare("good", "bad"));
        assertEquals(0.0, wordsComparator.compare("good", "nice"));
    }

}
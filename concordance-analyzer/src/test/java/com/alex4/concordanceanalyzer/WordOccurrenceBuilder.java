package com.alex4.concordanceanalyzer;

public final class WordOccurrenceBuilder {
    private WordOccurrenceBuilder() {}

    public static WordOccurrence word(String word) {
        WordOccurrence wordOccurrence = new WordOccurrence(word);
        wordOccurrence.registerOccurrence(1);
        return wordOccurrence;
    }

    public static WordOccurrence word(String word, int... sentenceNumbers) {
        WordOccurrence wordOccurrence = new WordOccurrence(word);
        for (int sentenceNumber : sentenceNumbers) {
            wordOccurrence.registerOccurrence(sentenceNumber);
        }
        return wordOccurrence;
    }
}

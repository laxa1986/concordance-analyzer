package com.alex4.concordanceanalyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class WordOccurrence {
    private final String word;
    private final List<Integer> sentenceNumbers = new ArrayList<>();

    public WordOccurrence(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void registerOccurrence(int sentenceNumber) {
        sentenceNumbers.add(sentenceNumber);
    }

    public int getWordCount() {
        return sentenceNumbers.size();
    }

    public List<Integer> getSentenceNumbers() {
        return Collections.unmodifiableList(sentenceNumbers);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final WordOccurrence other = (WordOccurrence) obj;
        return Objects.equals(this.word, other.word)
                && Objects.equals(this.sentenceNumbers, other.sentenceNumbers);
    }

    @Override
    public String toString() {
        return word + '{' +sentenceNumbers + '}';
    }
}
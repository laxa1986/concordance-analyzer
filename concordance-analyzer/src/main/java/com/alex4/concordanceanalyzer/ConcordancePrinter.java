package com.alex4.concordanceanalyzer;

import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;

public class ConcordancePrinter {
    private final PrintStream printStream;

    public ConcordancePrinter(PrintStream printStream) {
        this.printStream = printStream;
    }

    public void print(List<WordOccurrence> concordance) {
        int number = 1;
        for (WordOccurrence wordOccurrence : concordance) {
            String formattedWord = formatWordOccurrence(wordOccurrence, number);
            number++;
            printStream.println(formattedWord);
        }
    }

    private static String formatWordOccurrence(WordOccurrence wordOccurrence, int number) {
        String word = wordOccurrence.getWord();
        int wordCount = wordOccurrence.getWordCount();

        int longerstWord = 20;
        int indent = longerstWord - word.length();
        String spaces = ""; for (int i=0; i<indent; i++) spaces += " ";

        List<String> sentenceNumbers = wordOccurrence.getSentenceNumbers().stream().map(Object::toString).collect(Collectors.toList());
        String numbers = String.join(",", sentenceNumbers);

        return String.format("%d.\t %s%s\t{%d:%s}", number, word, spaces, wordCount, numbers);
    }
}
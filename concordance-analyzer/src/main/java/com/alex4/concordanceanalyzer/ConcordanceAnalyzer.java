package com.alex4.concordanceanalyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static java.lang.Character.*;

public class ConcordanceAnalyzer {
    private final SortedMap<String, WordOccurrence> wordMap;

    public ConcordanceAnalyzer() {
        wordMap = new TreeMap<>();
    }

    private static boolean isLetter(int type) {
        return UPPERCASE_LETTER <= type && type <= OTHER_LETTER;
    }

    private static boolean isSentenceDelimiter(char chr) {
        return chr == '.' || chr == '!' || chr == '?';
    }

    /**
     * Splitting text to sentences and then walking through each is kind of double work,
     * but it allows to make algorithm simpler, which is more important in my case
     *
     * @param text whole text
     * @return text splited on sentences
     */
    private static List<String> splitToSentences(String text) {
        LinkedList<String> sentences = new LinkedList<>();

        int previousSentenceEnd = text.length();
        for (int i=text.length()-1; i>=0; i--) {
            char chr = text.charAt(i);
            int type = Character.getType(chr);
            if (type == SPACE_SEPARATOR) {
                char _chr = text.charAt(i-1);

                // skip "i.e." case - this is not an end of sentence
                // checking i-2 symbol need to respect '...' case
                if (_chr == '.' && i>4 && text.charAt(i-3) == '.' && text.charAt(i-2) != '.') {
                    continue;
                }

                if (isSentenceDelimiter(_chr)) {
                    String sentence = text.substring(i, previousSentenceEnd);
                    previousSentenceEnd = i;
                    sentences.addFirst(sentence);
                }
            }
        }

        String sentence = text.substring(0, previousSentenceEnd);
        sentences.addFirst(sentence);

        return sentences;
    }

    /**
     * Split sentence to tokens by space, so sentence "Tom: Cate" produce list of two tokens: ["Tom:", "Cate"]
     * @param sentence sentence to analyze
     * @return number of tokens
     */
    private static List<String> splitToTokens(String sentence) {
        List<String> tokens = new ArrayList<>();

        int prevCharType = -1;
        int tokenStartPosition = 0;
        for (int i=0; i<sentence.length(); i++) {
            char chr = sentence.charAt(i);
            int type = Character.getType(chr);
            if (type == SPACE_SEPARATOR && prevCharType != SPACE_SEPARATOR) {
                String token = sentence.substring(tokenStartPosition, i);
                tokenStartPosition = i+1;
                tokens.add(token);
            }
            prevCharType = type;
        }

        String token = sentence.substring(tokenStartPosition);
        tokens.add(token);

        return tokens;
    }

    /**
     * "i.e."? -> i.e.
     * "-" -> null
     *
     * @param token string without spaces
     * @return word
     */
    private static String stripSigns(String token) {
        int start = -1;
        int end;

        for (int i=0; i<token.length(); i++) {
            char chr = token.charAt(i);
            int type = Character.getType(chr);
            if (isLetter(type)) {
                start = i;
                break;
            }
        }
        if (start == -1) {
            return null;
        }

        end = start;
        for (int i=token.length()-1; i>=0; i--) {
            char chr = token.charAt(i);
            int type = Character.getType(chr);
            if (isLetter(type)) {
                end = i;
                break;
            }
            // i.e. case
            if (chr == '.' && start == i-3 && token.charAt(i-2) == '.') {
                end = i;
                break;
            }
        }

        return token.substring(start, end+1);
    }

    public List<WordOccurrence> analyze(String text) {
        if (text == null) {
            throw new IllegalArgumentException("null text not allowed");
        }

        text = text.trim();
        if (text.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> sentences = splitToSentences(text);
        int sentenceNumber = 1;
        for (String sentence : sentences) {
            analyzeSentence(sentence, sentenceNumber);
            sentenceNumber++;
        }

        return new ArrayList<>(wordMap.values());
    }

    private void analyzeSentence(String sentence, int sentenceNumber) {
        List<String> tokens = splitToTokens(sentence);
        int wordCount = 0;
        for (String token : tokens) {
            String word = stripSigns(token);
            if (word != null) {
                wordCount++;
                word = word.toLowerCase();
                WordOccurrence wordOccurrence = wordMap.computeIfAbsent(word, WordOccurrence::new);
                wordOccurrence.registerOccurrence(sentenceNumber);
            }
        }

        if (wordCount == 0) {
            throw new IllegalArgumentException("sentence should has at least one word");
        }
    }
}
package com.alex4.concordanceanalyzer;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.alex4.concordanceanalyzer.WordOccurrenceBuilder.word;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;

public class ConcordanceAnalyzerTest {
    private ConcordanceAnalyzer sut = new ConcordanceAnalyzer();

    private static void assertEquals(List<WordOccurrence> concordance, WordOccurrence... words) {
        assertThat(concordance, is(Arrays.asList(words)));
    }

    // --- exceptional cases ---

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAcceptNullArgument() {
        sut.analyze(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAcceptSentenceWithoutWords() {
        sut.analyze("a. ' .");
    }

    // --- corner cases ---

    @Test
    public void shouldReturnEmptyListForEmptyString() {
        List<WordOccurrence> concordance = sut.analyze("");
        assertThat(concordance, is(empty()));
    }

    @Test
    public void shouldIgnoreTrailingSpaces() {
        List<WordOccurrence> concordance1 = sut.analyze("\ta\n");
        List<WordOccurrence> concordance2 = new ConcordanceAnalyzer().analyze("a");
        assertThat(concordance1, equalTo(concordance2));
    }

    @Test
    public void shouldIgnoreTrailingSigns() {
        List<WordOccurrence> concordance1 = sut.analyze("a b");
        List<WordOccurrence> concordance2 = new ConcordanceAnalyzer().analyze("a b.");
        assertThat(concordance1, equalTo(concordance2));
    }

    // --- simple cases ---

    @Test
    public void shouldReturnSingleItemMapForWord() {
        List<WordOccurrence> concordance = sut.analyze("a");
        assertEquals(concordance, word("a"));
    }

    @Test
    public void shouldTransformToLowerCase() {
        List<WordOccurrence> concordance = sut.analyze("A");
        assertEquals(concordance, word("a"));
    }

    @Test
    public void shouldNotCareAboutFirstLetterCase() {
        List<WordOccurrence> concordance = sut.analyze("A a");
        assertEquals(concordance, word("a",1,1));
    }

    @Test
    public void shouldReturnSortedConcordance() {
        List<WordOccurrence> concordance = sut.analyze("b a");
        assertEquals(concordance, word("a"), word("b"));
    }

    @Test
    public void shouldDistinguishMultipleForm() {
        List<WordOccurrence> concordance = sut.analyze("word words");
        assertEquals(concordance, word("word"), word("words"));
    }

    @Test
    public void shouldIncreaseCounterWhenSameWordFound() {
        List<WordOccurrence> concordance = sut.analyze("a a");
        assertEquals(concordance, word("a",1,1));
    }

    // --- numbers ---
    // ??

    // --- mix letters and signs ---

    @Test
    public void shouldIgnoreComas() {
        List<WordOccurrence> concordance = sut.analyze("a, b");
        assertEquals(concordance, word("a"), word("b"));
    }

    @Test
    public void shouldIdentifyIdEst() {
        List<WordOccurrence> concordance = sut.analyze("i.e.");
        assertEquals(concordance, word("i.e."));
    }

    @Test
    public void shouldNotIncreaseSentenceNumberForIdEst() {
        List<WordOccurrence> concordance = sut.analyze("a i.e. a");
        assertEquals(concordance, word("a",1,1), word("i.e."));
    }

    @Test
    public void shouldIdentifyApostrophe() {
        List<WordOccurrence> concordance = sut.analyze("Lisa's watch");
        assertEquals(concordance, word("lisa's"), word("watch"));
    }

    @Test
    public void shouldRespectHyphenDelimitedWord() {
        List<WordOccurrence> concordance = sut.analyze("super-power");
        assertEquals(concordance, word("super-power"));
    }

    @Test
    public void shouldIgnoreNonLetterCharsAroundTheWord() {
        List<WordOccurrence> concordance = sut.analyze("?xercise$");
        assertEquals(concordance, word("xercise"));
    }

    @Test
    public void shouldRespectWordsWithExclamationMarkInTheMiddle() {
        List<WordOccurrence> concordance = sut.analyze("holy sh!t");
        assertEquals(concordance, word("holy"), word("sh!t"));
    }

    @Test
    public void shouldIgnoreSigns() {
        List<WordOccurrence> concordance = sut.analyze("\"a\"-c!?");
        assertEquals(concordance, word("a\"-c"));
    }

    @Test
    public void shouldSkipSignsLikeQuotes() {
        List<WordOccurrence> concordance = sut.analyze("Bar \"Castle\"");
        assertEquals(concordance, word("bar"), word("castle"));
    }

    @Test
    public void shouldIgnoreDashes() {
        List<WordOccurrence> concordance = sut.analyze("I'm - robot");
        assertEquals(concordance, word("i'm"), word("robot"));
    }

    // --- several sentences ---

    @Test
    public void shouldRespectSentenceNumber() {
        List<WordOccurrence> concordance = sut.analyze("a. b");
        assertEquals(concordance, word("a"), word("b", 2));
    }

    @Test
    public void shouldMatchSameWordInDifferentSentences() {
        List<WordOccurrence> concordance = sut.analyze("a. a");
        assertEquals(concordance, word("a", 1,2));
    }

    @Test
    public void exclamationPointAndQuestionMarkAreOneSentence() {
        List<WordOccurrence> concordance = sut.analyze("a!? a");
        assertEquals(concordance, word("a", 1,2));
    }

    @Test
    public void shouldConsiderDotsAsSentenceDelimiter() {
        List<WordOccurrence> concordance = sut.analyze("Hey... never mind");
        assertEquals(concordance, word("hey"), word("mind",2), word("never", 2));
    }
}
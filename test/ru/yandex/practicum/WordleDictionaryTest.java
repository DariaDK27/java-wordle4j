package ru.yandex.practicum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class WordleDictionaryTest {
    private PrintWriter log;
    private WordleDictionary dictionary;

    @BeforeEach
    void setUp() {
        log = new PrintWriter(System.out, true);
        dictionary = new WordleDictionary(List.of("гонец", "герой", "мороз"), log);
    }

    @Test
    void sizeReturnsNumberOfWords() {
        assertEquals(3, dictionary.size());
    }

    @Test
    void containsFindsExistingWord() {
        assertTrue(dictionary.contains("герой"));
    }

    @Test
    void containsDoesNotFindMissingWord() {
        assertFalse(dictionary.contains("арбуз"));
    }

    @Test
    void randomWordIsFromDictionary() {
        for (int i = 0; i < 20; i++) {
            assertTrue(dictionary.contains(dictionary.getRandomWord()));
        }
    }

    @Test
    void randomWordFromEmptyDictionaryThrowsException() {
        WordleDictionary empty = new WordleDictionary(new ArrayList<>(), log);
        assertThrows(IllegalStateException.class, empty::getRandomWord);
    }

    @Test
    void normalizeMakesLowerCaseAndTrimsSpaces() {
        assertEquals("гонец", WordleDictionary.normalize("  ГОНЕЦ  "));
    }

    @Test
    void normalizeReplacesYoWithYe() {
        assertEquals("котел", WordleDictionary.normalize("КОТЁЛ"));
    }

    @Test
    void normalizeNullGivesEmptyString() {
        assertEquals("", WordleDictionary.normalize(null));
    }
}

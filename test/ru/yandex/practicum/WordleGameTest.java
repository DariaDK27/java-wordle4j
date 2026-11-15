package ru.yandex.practicum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.PrintWriter;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class WordleGameTest {
    private PrintWriter log;
    private WordleDictionary dictionary;
    private WordleGame game;

    @BeforeEach
    void setUp() {
        log = new PrintWriter(System.out, true);
        dictionary = new WordleDictionary(List.of("гонец", "герой", "мороз", "арбуз", "котел"), log);
        game = new WordleGame(dictionary, "герой", log);
    }

    @Test
    void hintFromTaskExample() {
        // пример из задания: гонец и герой
        assertEquals("+^-^-", WordleGame.computeHint("гонец", "герой"));
    }

    @Test
    void hintForCorrectWordIsAllPlus() {
        assertEquals("+++++", WordleGame.computeHint("герой", "герой"));
    }

    @Test
    void hintForCompletelyDifferentWordIsAllMinus() {
        assertEquals("-----", WordleGame.computeHint("арбуз", "котел"));
    }

    @Test
    void hintWithRepeatedLettersMarksOnlyExistingOnes() {
        // в слове мороз две буквы о, на 2 и 4 местах
        assertEquals("-+-+-", WordleGame.computeHint("ооооо", "мороз"));
    }

    @Test
    void newGameHasSixSteps() {
        assertEquals(6, game.getSteps());
        assertFalse(game.isFinished());
    }

    @Test
    void wrongWordDecreasesStepsAndReturnsHint() throws WordleGameException {
        String hint = game.makeMove("гонец");
        assertEquals("+^-^-", hint);
        assertEquals(5, game.getSteps());
        assertFalse(game.isFinished());
    }

    @Test
    void correctWordWinsGame() throws WordleGameException {
        game.makeMove("герой");
        assertTrue(game.isWon());
        assertTrue(game.isFinished());
    }

    @Test
    void inputIsNormalized() throws WordleGameException {
        game.makeMove("  КОТЁЛ ");

        assertEquals(5, game.getSteps());
    }

    @Test
    void wordNotInDictionaryThrowsExceptionAndKeepsSteps() {
        assertThrows(WordNotFoundInDictionary.class, () -> game.makeMove("ыыыыы"));
        assertEquals(6, game.getSteps());
    }

    @Test
    void shortWordThrowsException() {
        assertThrows(InvalidWordException.class, () -> game.makeMove("кот"));
        assertEquals(6, game.getSteps());
    }

    @Test
    void emptyWordThrowsException() {
        assertThrows(InvalidWordException.class, () -> game.makeMove(""));
    }

    @Test
    void englishWordThrowsException() {
        assertThrows(InvalidWordException.class, () -> game.makeMove("hello"));
        assertEquals(6, game.getSteps());
    }

    @Test
    void gameEndsAfterSixWrongMoves() throws WordleGameException {
        for (int i = 0; i < 6; i++) {
            game.makeMove("гонец");
        }
        assertEquals(0, game.getSteps());
        assertTrue(game.isFinished());
        assertFalse(game.isWon());
    }

    @Test
    void moveAfterGameEndThrowsException() throws WordleGameException {
        game.makeMove("герой");
        assertThrows(IllegalStateException.class, () -> game.makeMove("гонец"));
    }

    @Test
    void suggestionWithoutMovesIsFromDictionary() {
        assertTrue(dictionary.contains(game.suggestWord()));
    }

    @Test
    void suggestionFitsPreviousHints() throws WordleGameException {
        game.makeMove("гонец");
        assertEquals("герой", game.suggestWord());
    }
}

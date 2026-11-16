package ru.yandex.practicum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class WordleDictionaryLoaderTest {
    private WordleDictionaryLoader loader;

    @BeforeEach
    void setUp() {
        loader = new WordleDictionaryLoader(new PrintWriter(System.out, true));
    }

    @Test
    void loadKeepsOnlyRussianFiveLetterWords(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("words.txt");
        Files.write(file, List.of("Гонец", "кот", "hello", "ёлочка", "КОТЁЛ"), StandardCharsets.UTF_8);
        WordleDictionary dictionary = loader.load(file.toString());
        assertEquals(2, dictionary.size());
        assertTrue(dictionary.contains("гонец"));
        assertTrue(dictionary.contains("котел"));
    }

    @Test
    void loadThrowsExceptionWhenNoSuitableWords(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("words.txt");
        Files.write(file, List.of("кот", "hello"), StandardCharsets.UTF_8);
        assertThrows(WordleDictionaryException.class, () -> loader.load(file.toString()));
    }

    @Test
    void loadThrowsExceptionWhenFileNotFound() {
        assertThrows(IOException.class, () -> loader.load("no_such_file.txt"));
    }

    @Test
    void isRussianWordAcceptsRussianLetters() {
        assertTrue(WordleDictionaryLoader.isRussianWord("гонец"));
    }

    @Test
    void isRussianWordRejectsEnglishLetters() {
        assertFalse(WordleDictionaryLoader.isRussianWord("hello"));
    }
}

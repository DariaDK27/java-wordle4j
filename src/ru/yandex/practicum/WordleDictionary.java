package ru.yandex.practicum;

import java.util.List;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class WordleDictionary {

    private List<String> words;
    private final Set<String> wordsSet;
    private final PrintWriter log;
    private final Random random = new Random();

    public WordleDictionary(List<String> words, PrintWriter log) {
        this.words = words;
        this.wordsSet = new HashSet<>(words);
        this.log = log;
        log.println("Словарь создан, слов в нём: " + words.size());
    }

    public int size() {
        return words.size();
    }

    public boolean contains(String word) {
        return wordsSet.contains(word);
    }

    public List<String> getWords() {
        return words;
    }

    public String getRandomWord() {
        if (words.isEmpty()) {
            throw new IllegalStateException("Словарь пуст, выбрать слово невозможно");
        }
        String word = words.get(random.nextInt(words.size()));
        log.println("Выбрано случайное слово: " + word);
        return word;
    }

    public static String normalize(String word) {
        if (word == null) {
            return "";
        }
        return word.trim().toLowerCase().replace('ё', 'е');
    }
}

package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {
    public static final int WORD_LENGTH = 5;
    private final PrintWriter log;

    public WordleDictionaryLoader(PrintWriter log) {
        this.log = log;
    }

    public  WordleDictionary load(String fileName) throws IOException, WordleDictionaryException {
        List<String> words = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String word = WordleDictionary.normalize(line);
                if (word.length() == WORD_LENGTH && isRussianWord(word)) {
                    words.add(word);
                }
            }
        }

        if (words.isEmpty()) {
            throw new WordleDictionaryException("В файле " + fileName + " нет подходящих слов");
        }

        log.println("Загружен словарь из файла " + fileName + ", подходящих слов: " + words.size());
        return new WordleDictionary(words, log);
    }

    public static boolean isRussianWord(String word) {
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (c < 'а' || c > 'я') {
                return false;
            }
        }
        return true;
    }
}


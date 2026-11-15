package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WordleGame {
    public static final int MAX_STEPS = 6;
    private final String answer;
    private int steps;
    private final WordleDictionary dictionary;
    private final PrintWriter log;

    private final List<String> enteredWords = new ArrayList<>();
    private final List<String> hints = new ArrayList<>();
    private final Set<String> suggestedWords = new HashSet<>();
    private final Random random = new Random();
    private boolean won = false;

    public WordleGame(WordleDictionary dictionary, PrintWriter log) {
        this(dictionary, dictionary.getRandomWord(), log);
    }

    public WordleGame(WordleDictionary dictionary, String answer, PrintWriter log) {
        this.dictionary = dictionary;
        this.answer = answer;
        this.log = log;
        this.steps = MAX_STEPS;
        log.println("Игра создана, шагов: " + steps);
    }

    public int getSteps() {
        return steps;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isWon() {
        return won;
    }

    public boolean isFinished() {
        return won || steps == 0;
    }

    public String makeMove(String input) throws WordleGameException {
        if (isFinished()) {
            throw new IllegalStateException("Игра уже закончена, ход сделать нельзя");
        }

        String word = WordleDictionary.normalize(input);

        if (word.length() != WordleDictionaryLoader.WORD_LENGTH) {
            throw new InvalidWordException("Слово должно состоять из " + WordleDictionaryLoader.WORD_LENGTH + " букв");
        }
        if (!WordleDictionaryLoader.isRussianWord(word)) {
            throw new InvalidWordException("Слово должно состоять только из русских букв");
        }
        if (!dictionary.contains(word)) {
            throw new WordNotFoundInDictionary("Такого слова нет в словаре");
        }

        String hint = computeHint(word, answer);
        enteredWords.add(word);
        hints.add(hint);
        steps--;

        if (word.equals(answer)) {
            won = true;
        }

        StringBuilder message = new StringBuilder();
        message.append("Ход: ").append(word)
                .append(", подсказка: ").append(hint)
                .append(", осталось шагов: ").append(steps);
        log.println(message);

        return hint;
    }

    public String suggestWord() {
        List<String> candidates = new ArrayList<>();
        for (String word : dictionary.getWords()) {
            if (isSuitable(word)) {
                candidates.add(word);
            }
        }

        if (candidates.isEmpty()) {
            throw new IllegalStateException("Не нашлось ни одного подходящего слова, хотя ответ должен подходить");

        }

        List<String> fresh = new ArrayList<>();
        for (String word : candidates) {
            if (!suggestedWords.contains(word)) {
                fresh.add(word);
            }
        }
        List<String> source = fresh.isEmpty() ? candidates : fresh;

        String suggestion = source.get(random.nextInt(source.size()));
        suggestedWords.add(suggestion);
        log.println("Подсказка: " + suggestion + " (подходящих слов было: " + candidates.size() + ")");
        return suggestion;
    }

    private boolean isSuitable(String candidate) {
        for (int i = 0; i < enteredWords.size(); i++) {
            String expectedHint = hints.get(i);
            String hintForCandidate = computeHint(enteredWords.get(i), candidate);
            if (!hintForCandidate.equals(expectedHint)) {
                return false;
            }
        }
        return true;
    }

    public static String computeHint(String guess, String answer) {
        int length = guess.length();
        char[] result = new char[length];
        boolean[] used = new boolean[length];

        for (int i = 0; i < length; i ++) {
            if (guess.charAt(i) == answer.charAt(i)) {
                result[i] = '+';
                used[i] = true;
            }
        }

        for (int i = 0; i < length; i++) {
            if (result[i] == '+') {
                continue;
            }
            result[i] = '-';
            for (int j = 0; j < length; j++) {
                if (!used[j] && guess.charAt(i) == answer.charAt(j)) {
                    result[i] = '^';
                    used[j] = true;
                    break;
                }
            }
        }
        return new String(result);
    }

}

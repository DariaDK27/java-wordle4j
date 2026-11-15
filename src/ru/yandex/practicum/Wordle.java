package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Wordle {
    private static final String DICTIONARY_FILE = "words_ru.txt";
    private static final String LOG_FILE = "wordle.log";

    public static void main(String[] args) {
        try (PrintWriter log = new PrintWriter(new FileWriter(LOG_FILE, StandardCharsets.UTF_8), true)) {
            try {
                run(log);
            } catch (Exception e) {
                log.println("Критическая ошибка:");
                e.printStackTrace(log);
                System.out.println("В программе произошла ошибка. Подробности в файле " + LOG_FILE);
            }
        } catch (IOException e) {
            System.out.println("Не удалось создать лог-файл " + LOG_FILE);
        }
    }

    private static void run(PrintWriter log) throws IOException, WordleDictionaryException {
        WordleDictionaryLoader loader = new WordleDictionaryLoader(log);
        WordleDictionary dictionary = loader.load(DICTIONARY_FILE);
        WordleGame game = new WordleGame(dictionary, log);

        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

        System.out.println("Игра Wordle: угадайте слово из 5 букв за " + WordleGame.MAX_STEPS + " попыток.");
        System.out.println("+ буква на своём месте, ^ буква есть, но в другом месте, - буквы нет.");
        System.out.println("Чтобы получить подсказку, нажмите Enter на пустой строке.");

        while (!game.isFinished()) {
            System.out.println("Осталось попыток: " + game.getSteps());
            System.out.print("> ");

            if (!scanner.hasNextLine()) {
                log.println("Ввод закончился, игра прервана");
                break;
            }
            String input = scanner.nextLine();

            if (WordleDictionary.normalize(input).isEmpty()) {
                String suggestion = game.suggestWord();
                System.out.println("Подсказка: попробуйте слово " + suggestion);
                continue;
            }
            try {
                String hint = game.makeMove(input);
                System.out.println("> " + hint);
            } catch (WordleGameException e) {
                System.out.println(e.getMessage() + ". Попробуйте ещё раз.");
                log.println("Игровая ошибка: " + e.getMessage());
            }
        }
        if (game.isWon()) {
            System.out.println("Поздравляем, вы угадали слово!");
        } else {
            System.out.println("Попытки закончились.");
        }
        System.out.println("Загаданное слово: " + game.getAnswer());
        log.println("Игра завершена, победа: " + game.isWon());
    }

}

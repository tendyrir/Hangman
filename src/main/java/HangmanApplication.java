import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class HangmanApplication {

    private static final String START_GAME = "старт";
    private static final String RESTART_GAME = "заново";
    private static final String LEXICON_PATH = "src/main/resources/words.txt";

    private static int hangmanStatus = 0;
    private static int hitPoints = 6;

    public static void main(String[] args) {
        System.out.println("Введите \"старт\" для начала игры...");
        try (Scanner scanner = new Scanner(System.in)) {

            if (scanner.nextLine().equals(START_GAME)) {
                if (playHangman(scanner)) {
                    System.out.println("Игра успешно завершена!");
                } else {
                    System.out.println("Выход из игры.");
                }
            } else {
                System.out.println("Введенный вами текст не распознан как команда 'старт'.");
            }
        }
    }

    public static boolean playHangman(Scanner scanner) {
        List<String> lexicon = getLexiconContent();
        boolean gameRestart;
        do {
            startGameRound(lexicon);
            System.out.println("Игра закончена. Для перезапуска введите \"заново\" или любой другой текст для выхода.");
            String userInput = scanner.nextLine().toLowerCase();
            gameRestart = userInput.equals(RESTART_GAME);
            hangmanStatus = 0;
            hitPoints = 6;
        } while (gameRestart);

        return true;  // Игра завершена успешно
    }

    public static void startGameRound(List<String> lexicon) {
        String word = getRandomWord(lexicon);
        List<Character> allChars = getAllCharacters(word);
        Set<Character> openChars = new HashSet<>();
        Set<Character> usedChars = new HashSet<>();

        printCurrentGameState(hangmanStatus, openChars, allChars);

        while (true) {
            // проверка, открыты ли все буквы
            if (openChars.containsAll(allChars)) {
                System.out.println("Отлично! Вы отгадали слово");
                break;
            }
            // проверка, остались ли еще попытки
            if (hitPoints == 0) {
                System.out.println("Попытки закончились, вы проиграли! Загаданное слово: " + word);
                break;
            }
            // Ввод игроком новой буквы
            char currentChar = getPlayerInput();
            // Обработка ввода игрока
            processPlayerInput(currentChar, openChars, allChars, usedChars);
            // Печать текущего состояния игры
            printCurrentGameState(hangmanStatus, openChars, allChars);

        }
    }

    public static void processPlayerInput(char newCharacter,
                                          Set<Character> openedCharacters,
                                          List<Character> allCharacters,
                                          Set<Character> usedCharacters) {
        // это корректная буква или нет
        if (Character.isLetter(newCharacter)) {

            // буква правильная или нет
            if (allCharacters.contains(newCharacter)) {
                // открыта или нет
                if (openedCharacters.contains(newCharacter)) {
                    // открыта
                    System.out.println("Эта буква уже открыта!");
                } else {
                    // не открыта - открываем
                    openedCharacters.add(newCharacter);
                }

            } else {

                // если неправильная использовалась ли
                if (usedCharacters.contains(newCharacter)) {
                    // буква использовалась
                    System.out.println("Эта буква уже использовалась!");
                } else {
                    // неправильная и не использовалась - минус попытка и букву в использованные
                --hitPoints;
                ++hangmanStatus;
                usedCharacters.add(newCharacter);
                System.out.println("НЕПРАВИЛЬНО!, осталось попыток: " + hitPoints);
                }
            }

        } else {
            System.out.println("Введите букву!!");
        }

    }

    public static void printCurrentGameState(int status, Set<Character> openedCharacters, List<Character> allCharacters) {
        System.out.println(HangmanStatus.getStatusByIndex(status));
        System.out.println("Загаданное слово: " + getCurrentWordState(openedCharacters, allCharacters));
    }

    private static char getPlayerInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ваша буква: ");
        String newInput = scanner.nextLine();
        return newInput.charAt(0);
    }

    public static List<Character> getAllCharacters(String word) {
        List<Character> allChars = new ArrayList<>();
        for (Character character : word.toCharArray()) {
            allChars.add(character);
        }
        return allChars;
    }

    public static String getRandomWord(List<String> lexicon) {
        return lexicon.get(new Random().nextInt(lexicon.size()));
    }

    public static List<String> getLexiconContent() {
        File lexicon = new File(LEXICON_PATH);
        List<String> lexiconContent = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(lexicon))) {
            while ((line = br.readLine()) != null) {
                lexiconContent.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading lexicon file: " + e.getMessage());
        }
        return lexiconContent;
    }

    public static String getCurrentWordState(Set<Character> openedCharacters, List<Character> allCharacters) {
        StringBuilder sb = new StringBuilder();
        for (Character allChar : allCharacters) {
            if (openedCharacters.contains(allChar)) {
                sb.append(allChar);
            } else {
                sb.append("*");
            }
        }
        return sb.toString();
    }
}

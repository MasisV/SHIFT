import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Main {
    public static List<String> sortByListString = new ArrayList<>();
    public static List<Long> sortByListInt = new ArrayList<>();
    public static List<Float> sortByListFloat = new ArrayList<>();
    public static void main(String[] args) {
        Utils utils = new Utils();
        Scanner scanner = new Scanner(System.in);
        List<String> fileNames = new ArrayList<>();
        boolean exit = false;

        while (true) {
            System.out.println("Введите путь до файла. Для выхода введите exit");
            if (scanner.hasNextLine()) {
                String inputfor = scanner.nextLine();
                if (inputfor.equals("exit")) break;
                fileNames.add(inputfor);
            } else {
                System.out.println("Некорректный ввод");
            }
        }

        for (String fileName : fileNames) {
            utils.addToList(fileName);
        }

        while (!exit) {
            System.out.println("Выберите действие:");
            System.out.println("Введите -s для просмотра краткой статистики");
            System.out.println("Введите -f, для просмотра полной статистики");
            System.out.println("Введите -o, чтобы задать результат");
            System.out.println("Введите -p, чтобы задать результат с указанным префиксом");
            System.out.println("Введите -a, для записи в файл");
            System.out.println("Введите exit для выхода");

            if (scanner.hasNextLine()) {
                String choice = scanner.nextLine();
                switch (choice) {
                    case "-f":
                        utils.fullStatistic();
                        break;
                    case "-s":
                        utils.shortStatistic();
                        break;
                    case "-o":
                        utils.addResult(false);
                        break;
                    case "-p":
                        utils.addResult(true);
                        break;
                    case "-a":
                        utils.appendToFile();
                        exit = true;
                        break;
                    case "exit":
                        exit = true;
                        break;
                    default:
                        System.out.println("Неверный ввод.");
                }
            } else System.out.println("Некоректный ввод");
        }

        System.out.println(sortByListInt);
        System.out.println(sortByListFloat);
        System.out.println(sortByListString);
    }

    public static class Utils {
        public void writeToFile(List<? extends Object> data, String fileName) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                for (Object item : data)
                    writer.write(item + System.lineSeparator());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void addToList(String fileName) {
            try (BufferedReader fReadNamePath = new BufferedReader(new FileReader(fileName))) {
                String input;
                while ((input = fReadNamePath.readLine()) != null) {
                    try {
                        Long intValue = Long.parseLong(input);
                        sortByListInt.add(intValue);
                    } catch (NumberFormatException e) {
                        try {
                            float floatValue = Float.parseFloat(input);
                            sortByListFloat.add(floatValue);
                        } catch (NumberFormatException ex) {
                            sortByListString.add(input);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void fullStatistic() {
            long intMin = sortByListInt.stream().min(Long::compare).orElse(0L);
            long intMax = sortByListInt.stream().max(Long::compare).orElse(0L);
            long intSum = sortByListInt.stream().mapToLong(Long::longValue).sum();
            double intAvg = sortByListInt.stream().mapToLong(Long::longValue).average().orElse(0.0);

            float floatMin = sortByListFloat.stream().min(Float::compare).orElse(0.0f);
            float floatMax = sortByListFloat.stream().max(Float::compare).orElse(0.0f);
            float floatSum = (float) sortByListFloat.stream().mapToDouble(Float::floatValue).sum();
            double floatAvg = sortByListFloat.stream().mapToDouble(Float::floatValue).average().orElse(0.0);

            int shortestStringLength = sortByListString.stream()
                    .filter(str -> !str.isEmpty())
                    .mapToInt(String::length)
                    .min()
                    .orElse(0);
            int longestStringLength = sortByListString.stream().mapToInt(String::length).max().orElse(0);

            System.out.println("Минимальное целое число " + intMin);
            System.out.println("Максимальное целове число " + intMax);
            System.out.println("Сумма целых чисел " + intSum);
            System.out.println("Среднее целых чисел " + intAvg);

            System.out.println("Минимальное вещественное число " + floatMin);
            System.out.println("Максимальное вещественное число " + floatMax);
            System.out.println("Сумма вещественных чисел " + floatSum);
            System.out.println("Среднее вещественных чисел " + floatAvg);

            System.out.println("Размер самой короткой строки " + shortestStringLength);
            System.out.println("Размер самой  длинной строки " + longestStringLength);
        }
        public void shortStatistic() {
            int intShortStatistic = sortByListInt.size();
            int floatShortStatistic = sortByListFloat.size();
            int stringShortScatistic = sortByListString.size();


            System.out.println("Колличесвто целых элементов в файле " + intShortStatistic);
            System.out.println("Колличество вещественных элементов в файл " + floatShortStatistic);
            System.out.println("Колличество строк в файле " + stringShortScatistic);
        }
        public void appendToFile() {
            System.out.println("Введите путь к файлу для записи");
            Scanner scanner = new Scanner(System.in);

            try (FileWriter writer = new FileWriter(scanner.nextLine(), true);
                 BufferedWriter bw = new BufferedWriter(writer)) {
                System.out.println("Введите данные для дозаписи в файл");
                String input = scanner.nextLine();
                bw.write(input + System.lineSeparator());
                System.out.println("Данные успешно добавлены в файл");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void addResult(boolean prefix) {
            Scanner scanner = new Scanner(System.in);

            if (prefix) {
                System.out.println("Введите путь для записи результатов");
                String customFilePath = scanner.nextLine();
                System.out.println("Введите префикс для имен файлов:");
                String filePrefix = scanner.nextLine();

                writeDataWithUniqueName(customFilePath, filePrefix);
            } else {
                System.out.println("Введите путь к файлу, для записи результатов");
                String customFilePath = scanner.nextLine();

                List<Object> combinedData = new ArrayList<>();
                combinedData.addAll(sortByListInt);
                combinedData.addAll(sortByListFloat);
                combinedData.addAll(sortByListString);

                writeToFileWithUniqueName(combinedData, customFilePath);
            }
        }

        private void writeDataWithUniqueName(String customFilePath, String filePrefix) {
            File directory = new File(customFilePath + "/results/");
            directory.mkdirs();

            String intFileName = customFilePath + "/results/" + filePrefix + "integers.txt";
            String floatFileName = customFilePath + "/results/" + filePrefix + "floats.txt";
            String stringFileName = customFilePath + "/results/" + filePrefix + "strings.txt";

            writeToFileWithUniqueName(sortByListInt, intFileName);
            writeToFileWithUniqueName(sortByListFloat, floatFileName);
            writeToFileWithUniqueName(sortByListString, stringFileName);

            System.out.println("Результаты были успешно записаны в новые файлы с префиксом: " + filePrefix);
            System.out.println("Файлы сохранены по следующему пути:");
            System.out.println("Integers: " + intFileName);
            System.out.println("Floats: " + floatFileName);
            System.out.println("Strings: " + stringFileName);
        }

        private void writeToFileWithUniqueName(List<? extends Object> data, String fileName) {
            File file = new File(fileName);
            if (file.exists()) {
                fileName = getUniqueFileName(fileName);
            }
            writeToFile(data, fileName);
        }

        private String getUniqueFileName(String fileName) {
            int counter = 1;
            File file = new File(fileName);
            String baseName = fileName.substring(0, fileName.lastIndexOf(".txt"));
            String extension = ".txt";
            while (file.exists()) {
                fileName = baseName + "_" + counter + extension;
                file = new File(fileName);
                counter++;
            }
            return fileName;
        }
    }
}

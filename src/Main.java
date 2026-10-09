
import java.util.Random;

public class Main {

    public static void main(String[] args)
            throws InterruptedException {

        System.out.println("Лабораторная работа №1");
        System.out.println("Тема: Создание потоков");
        System.out.println("Вариант №9");

        // Создание общего массива
        int[] mas = new int[100];
        Random random = new Random();

        // Заполнение массива случайными числами
        for (int i = 0; i < mas.length; i++) {
            mas[i] = random.nextInt(100) + 1;
        }

        // Вывод массива
        System.out.println("\nИсходный массив:");

        for (int i = 0; i < mas.length; i++) {
            System.out.printf("%4d", mas[i]);

            if ((i + 1) % 10 == 0) {
                System.out.println();
            }
        }

        // Создание объектов студентов
        Student1 student1 = new Student1(mas);
        Student2 student2 = new Student2(mas);

        System.out.println("\nЗапуск четырех потоков:");

        // Запускаем четыре потока
        student1.start();
        student2.start();

        // Ожидаем завершения всех потоков
        student1.waitForFinish();
        student2.waitForFinish();

        System.out.println("\nВсе четыре потока завершены!");

        // Информация об авторах
        System.out.println("\nИнформация о студентах:");

        pechatPoBukvam("Студент 1: Кирилл Друми-Никон");
        pechatPoBukvam("Студент 2: Даниела Мотынга");
        pechatPoBukvam("Группа: CR-243");

        System.out.println("\nЛабораторная работа завершена!");
    }

    // Вывод текста с задержкой 100 миллисекунд
    public static void pechatPoBukvam(String tekst)
            throws InterruptedException {

        for (int i = 0; i < tekst.length(); i++) {

            // Выводим один символ
            System.out.print(tekst.charAt(i));

            // Принудительно обновляем вывод
            System.out.flush();

            // Пауза 100 миллисекунд
            Thread.sleep(100);
        }

        // Переход на новую строку
        System.out.println();
    }
}

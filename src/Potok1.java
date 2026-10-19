import java.util.function.Consumer;

public class Potok1 implements Runnable {

    private final int[] mas;
    private final Consumer<String> vivod;

    public Potok1(int[] mas) {
        this(mas, System.out::println);
    }

    public Potok1(int[] mas, Consumer<String> vivod) {
        this.mas = mas;
        this.vivod = vivod;
    }

    @Override
    public void run() {
        String imya = Thread.currentThread().getName();
        int[] chisla = new int[4];
        int n = 0;
        int grupp = 0;

        for (int i = 0; i < mas.length; i++) {
            if (mas[i] % 2 == 0) {
                chisla[n] = mas[i];
                n++;

                if (n == 4) {
                    int para1 = chisla[0] + chisla[1];
                    int para2 = chisla[2] + chisla[3];
                    int summa = para1 + para2;
                    grupp++;

                    vivod.accept(imya + ": Группа " + grupp
                            + ": (" + chisla[0] + " + " + chisla[1] + ")"
                            + " + (" + chisla[2] + " + " + chisla[3] + ")"
                            + " = " + summa);

                    n = 0;
                }
            }
        }

        vivod.accept(imya + ": Обработано групп по две пары: " + grupp);
        if (n > 0) {
            vivod.accept(imya + ": Осталось чётных без полной группы: " + n);
        }
        vivod.accept(imya + ": Работа завершена");
    }
}

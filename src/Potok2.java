
import java.util.function.Consumer;

public class Potok2 implements Runnable {

    private final int[] mas;
    private final Consumer<String> vivod;

    public Potok2(int[] mas) {
        this(mas, System.out::println);
    }

    public Potok2(int[] mas, Consumer<String> vivod) {
        this.mas = mas;
        this.vivod = vivod;
    }

    @Override
    public void run() {

        String imya = Thread.currentThread().getName();

        vivod.accept(imya + ": Начало вычислений");

        for (int i = mas.length - 1; i >= 2; i -= 4) {

            int pervoe = mas[i];
            int vtoroe = mas[i - 2];

            int raznost = pervoe - vtoroe;

            vivod.accept(
                    imya + ": Позиции " + (i + 1) +
                            " и " + (i - 1) +
                            " | " + pervoe + " - " +
                            vtoroe + " = " + raznost
            );
        }

        vivod.accept(imya + ": Работа завершена");
    }
}

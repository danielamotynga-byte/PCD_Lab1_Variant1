
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

        vivod.accept(imya + ": Начало вычислений");

        Integer pervoe = null;
        Integer predyduschee = null;

        for (int i = 0; i < mas.length; i++) {

            if (mas[i] % 2 != 0) {
                continue;
            }

            if (pervoe == null) {
                pervoe = mas[i];
                continue;
            }

            int vtoroe = mas[i];
            int proizvedenie = pervoe * vtoroe;

            vivod.accept(imya + ": " + pervoe +
                    " * " + vtoroe + " = " + proizvedenie);

            if (predyduschee != null) {

                int raznost = predyduschee - proizvedenie;

                vivod.accept(imya + ": Разность " +
                        predyduschee + " - " +
                        proizvedenie + " = " + raznost);

                predyduschee = null;

            } else {
                predyduschee = proizvedenie;
            }

            pervoe = null;
        }

        vivod.accept(imya + ": Работа завершена");
    }
}

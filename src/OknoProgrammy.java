
import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class OknoProgrammy extends JFrame {

    private final int[] mas = new int[100];

    private final JTextArea massiv = sozdatPole();
    private final JTextArea potok11 = sozdatPole();
    private final JTextArea potok12 = sozdatPole();
    private final JTextArea potok21 = sozdatPole();
    private final JTextArea potok22 = sozdatPole();
    private final JTextArea avtory = sozdatPole();

    private final JLabel status =
            new JLabel("Сначала сгенерируйте массив");

    private final JButton knopkaMassiv =
            new JButton("Сгенерировать массив");

    private final JButton knopkaPotoki =
            new JButton("Запустить 4 потока");

    private Timer timer;

    public OknoProgrammy() {

        setTitle("Лабораторная работа №1 | Вариант 9");
        setSize(950, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel zagolovok = new JLabel(
                "Создание потоков выполнения",
                SwingConstants.CENTER
        );

        zagolovok.setFont(
                new Font("Arial", Font.BOLD, 24)
        );

        JTabbedPane vkladki = new JTabbedPane();

        vkladki.addTab("Массив",
                new JScrollPane(massiv));

        vkladki.addTab("Студент 1 - Th1",
                new JScrollPane(potok11));

        vkladki.addTab("Студент 1 - Th2",
                new JScrollPane(potok12));

        vkladki.addTab("Студент 2 - Th1",
                new JScrollPane(potok21));

        vkladki.addTab("Студент 2 - Th2",
                new JScrollPane(potok22));

        JPanel knopki = new JPanel();

        knopki.add(knopkaMassiv);
        knopki.add(knopkaPotoki);

        knopkaPotoki.setEnabled(false);

        JPanel niz = new JPanel(new BorderLayout(8, 8));

        JLabel podpis = new JLabel("Авторы лабораторной:");
        avtory.setRows(3);

        JPanel panelAvtorov = new JPanel(new BorderLayout());
        panelAvtorov.add(podpis, BorderLayout.NORTH);
        panelAvtorov.add(
                new JScrollPane(avtory),
                BorderLayout.CENTER
        );

        niz.add(knopki, BorderLayout.NORTH);
        niz.add(panelAvtorov, BorderLayout.CENTER);
        niz.add(status, BorderLayout.SOUTH);

        niz.setBorder(
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        );

        setLayout(new BorderLayout(10, 10));

        add(zagolovok, BorderLayout.NORTH);
        add(vkladki, BorderLayout.CENTER);
        add(niz, BorderLayout.SOUTH);

        knopkaMassiv.addActionListener(
                e -> generirovatMassiv()
        );

        knopkaPotoki.addActionListener(
                e -> zapustitPotoki()
        );
    }

    private static JTextArea sozdatPole() {

        JTextArea pole = new JTextArea();

        pole.setEditable(false);
        pole.setFont(
                new Font(Font.MONOSPACED, Font.PLAIN, 14)
        );
        pole.setMargin(new Insets(12, 12, 12, 12));

        return pole;
    }

    private void generirovatMassiv() {

        if (timer != null) {
            timer.stop();
        }

        Random random = new Random();

        StringBuilder tekst = new StringBuilder();
        tekst.append("Исходный массив:\n\n");

        for (int i = 0; i < mas.length; i++) {

            mas[i] = random.nextInt(100) + 1;

            tekst.append(
                    String.format("%4d", mas[i])
            );

            if ((i + 1) % 10 == 0) {
                tekst.append("\n");
            }
        }

        massiv.setText(tekst.toString());

        potok11.setText("");
        potok12.setText("");
        potok21.setText("");
        potok22.setText("");
        avtory.setText("");

        knopkaPotoki.setEnabled(true);

        status.setText(
                "Массив создан. Можно запускать потоки."
        );
    }

    private void zapustitPotoki() {

        knopkaMassiv.setEnabled(false);
        knopkaPotoki.setEnabled(false);

        potok11.setText("");
        potok12.setText("");
        potok21.setText("");
        potok22.setText("");
        avtory.setText("");

        status.setText("Выполняются четыре потока...");

        // Передача результатов в графическое окно
        Consumer<String> vivod = tekst ->
                SwingUtilities.invokeLater(() -> {

                    JTextArea pole;

                    if (tekst.startsWith("Студент 1 / Th1:")) {
                        pole = potok11;
                    } else if (tekst.startsWith("Студент 1 / Th2:")) {
                        pole = potok12;
                    } else if (tekst.startsWith("Студент 2 / Th1:")) {
                        pole = potok21;
                    } else {
                        pole = potok22;
                    }

                    pole.append(tekst + "\n");
                    pole.setCaretPosition(
                            pole.getDocument().getLength()
                    );
                });

        SwingWorker<Void, Void> worker =
                new SwingWorker<>() {

                    @Override
                    protected Void doInBackground()
                            throws InterruptedException {

                        Student1 student1 =
                                new Student1(mas, vivod);

                        Student2 student2 =
                                new Student2(mas, vivod);

                        student1.start();
                        student2.start();

                        student1.waitForFinish();
                        student2.waitForFinish();

                        return null;
                    }

                    @Override
                    protected void done() {

                        try {
                            get();

                            status.setText(
                                    "Все 4 потока завершены успешно!"
                            );

                            pechatAvtorov();

                        } catch (InterruptedException ex) {

                            Thread.currentThread().interrupt();
                            status.setText("Выполнение прервано");

                        } catch (ExecutionException ex) {

                            status.setText(
                                    "Ошибка: " + ex.getCause()
                            );

                        } finally {

                            knopkaMassiv.setEnabled(true);
                            knopkaPotoki.setEnabled(true);
                        }
                    }
                };

        worker.execute();
    }

    // Печатаем данные по одной букве каждые 100 мс
    private void pechatAvtorov() {

        String informaciya =
                "Студент 1: Имя Фамилия\n" +
                        "Студент 2: Имя Фамилия\n" +
                        "Группа: Ваша группа";

        final int[] poziciya = {0};

        timer = new Timer(100, e -> {

            if (poziciya[0] < informaciya.length()) {

                avtory.append(
                        String.valueOf(
                                informaciya.charAt(poziciya[0])
                        )
                );

                poziciya[0]++;

            } else {
                ((Timer) e.getSource()).stop();
            }
        });

        timer.start();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            OknoProgrammy okno = new OknoProgrammy();
            okno.setVisible(true);

        });
    }
}

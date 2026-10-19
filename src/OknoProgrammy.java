import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.function.Consumer;

public class OknoProgrammy extends JFrame {

    private final int[] mas = new int[100];
    private final JTextArea zhurnal = new JTextArea();
    private final JButton knopkaMassiv = new JButton("Сгенерировать массив");
    private final JButton knopkaStart = new JButton("Запустить 4 потока");
    private Timer timerAvtorov;

    public OknoProgrammy() {
        setTitle("Лабораторная №1 — Вариант 1");
        setSize(750, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        zhurnal.setEditable(false);
        zhurnal.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

        JPanel panel = new JPanel();
        panel.add(knopkaMassiv);
        panel.add(knopkaStart);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(zhurnal), BorderLayout.CENTER);

        knopkaStart.setEnabled(false);
        knopkaMassiv.addActionListener(e -> generirovatMassiv());
        knopkaStart.addActionListener(e -> zapustitPotoki());
    }

    private void generirovatMassiv() {
        if (timerAvtorov != null) timerAvtorov.stop();

        Random random = new Random();
        StringBuilder tekst = new StringBuilder("Массив из 100 чисел:\n");

        for (int i = 0; i < mas.length; i++) {
            mas[i] = random.nextInt(100) + 1;
            tekst.append(String.format("%4d", mas[i]));
            if ((i + 1) % 10 == 0) tekst.append('\n');
        }

        zhurnal.setText(tekst + "\n");
        knopkaStart.setEnabled(true);
    }

    private void zapustitPotoki() {
        if (timerAvtorov != null) timerAvtorov.stop();
        knopkaMassiv.setEnabled(false);
        knopkaStart.setEnabled(false);
        zhurnal.append("Запуск четырёх потоков...\n");

        Consumer<String> vivod = s ->
                SwingUtilities.invokeLater(() -> zhurnal.append(s + "\n"));

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                Student1 student1 = new Student1(mas, vivod);
                Student2 student2 = new Student2(mas, vivod);

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
                    zhurnal.append("\nВсе 4 потока завершены!\n");
                    pechatatAvtorov();
                } catch (Exception ex) {
                    zhurnal.append("Ошибка: " + ex.getMessage() + "\n");
                } finally {
                    knopkaMassiv.setEnabled(true);
                    knopkaStart.setEnabled(true);
                }
            }
        };

        worker.execute();
    }

    private void pechatatAvtorov() {
        String tekst = "Мотынга Даниела, Друми-Никон Кирилл, CR-243";
        final int[] i = {0};

        timerAvtorov = new Timer(100, e -> {
            if (i[0] < tekst.length()) {
                zhurnal.append(String.valueOf(tekst.charAt(i[0]++)));
                zhurnal.setCaretPosition(zhurnal.getDocument().getLength());
            } else {
                ((Timer) e.getSource()).stop();
                zhurnal.append("\n");
            }
        });
        timerAvtorov.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OknoProgrammy().setVisible(true));
    }
}

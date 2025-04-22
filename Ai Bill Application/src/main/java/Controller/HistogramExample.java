package Controller;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Random;

// 数据生成类
class DataGenerator {
    public static int[] generateData(int numberOfDataPoints, int maxValue) {
        Random random = new Random();
        int[] data = new int[numberOfDataPoints];
        for (int i = 0; i < numberOfDataPoints; i++) {
            data[i] = random.nextInt(maxValue);
        }
        return data;
    }
}

// 直方图计算类
class Histogram {
    private int binSize;
    private int[] data;

    public Histogram(int[] data, int binSize) {
        this.data = data;
        this.binSize = binSize;
    }

    public HashMap<Integer, Integer> computeFrequency() {
        HashMap<Integer, Integer> frequencyMap = new HashMap<>();
        for (int number : data) {
            int bin = number / binSize;
            frequencyMap.put(bin, frequencyMap.getOrDefault(bin, 0) + 1);
        }
        return frequencyMap;
    }
}

// 直方图 GUI 绘制类
class HistogramPanel extends JPanel {
    HashMap<Integer, Integer> frequencyMap;

    public HistogramPanel() {
        this.frequencyMap = new HashMap<>();
    }

    public void updateData(HashMap<Integer, Integer> frequencyMap) {
        this.frequencyMap = frequencyMap;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int barWidth = 40;
        int gap = 10;
        int maxFrequency = frequencyMap.values().stream().mapToInt(v -> v).max().orElse(1);

        int index = 0;
        for (Integer key : frequencyMap.keySet()) {
            int height = (int) ((frequencyMap.get(key) / (double) maxFrequency) * getHeight());
            g.setColor(Color.BLUE);
            g.fillRect(index * (barWidth + gap), getHeight() - height, barWidth, height);
            g.setColor(Color.BLACK);
            g.drawRect(index * (barWidth + gap), getHeight() - height, barWidth, height);
            g.drawString("Bin " + key, index * (barWidth + gap) + 5, getHeight() - 5);
            index++;
        }
    }
}


// 主窗口类
public class HistogramExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("数据分析界面");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new HistogramPanelContainer());
            frame.setVisible(true);
        });
    }
}

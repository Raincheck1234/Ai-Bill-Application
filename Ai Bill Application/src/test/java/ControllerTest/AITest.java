package ControllerTest;

import Controller.HistogramPanelContainer;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

public class AITest {

    @Test
    public void testHistogramPanel() throws Exception {
        EventQueue.invokeAndWait(() -> {
            JFrame frame = new JFrame("数据分析界面 - 测试");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new HistogramPanelContainer());
            frame.setVisible(true);
        });




        // 保持窗口显示一段时间，防止测试结束时窗口立即关闭
        Thread.sleep(5000);
    }

}




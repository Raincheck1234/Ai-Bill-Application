package Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

class ButtonRenderer extends DefaultTableCellRenderer {
    private final JPanel panel = new JPanel(new BorderLayout()); // 使用 BorderLayout
    private final JButton button = new JButton();

    public ButtonRenderer() {
        button.setFocusPainted(false); // 移除按钮的焦点边框
        button.setPreferredSize(new Dimension(80, 30)); // 设置按钮的固定尺寸
        panel.add(button, BorderLayout.CENTER); // 将按钮添加到面板中心
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // 根据单元格的值设置按钮文本
        button.setText(value != null ? value.toString() : "");
        return panel;
    }
}
package Controller;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private final JPanel panel = new JPanel(new BorderLayout()); // 使用 BorderLayout
    private final JButton button = new JButton();
    private int rowIndex;
    private final MenuUI menuUI;

    public ButtonEditor(MenuUI menuUI) {
        this.menuUI = menuUI;

        // 设置按钮样式
        button.setFocusPainted(false); // 移除按钮的焦点边框
        button.setPreferredSize(new Dimension(80, 30)); // 设置按钮的固定尺寸

        // 添加按钮到面板
        panel.add(button, BorderLayout.CENTER);

        // 为按钮添加事件监听器
        button.addActionListener(e -> {
            System.out.println("按钮点击事件触发: " + button.getText());
            fireEditingStopped(); // 停止编辑
            String buttonText = button.getText();
            if ("Modify".equals(buttonText)) {
                menuUI.editRow(rowIndex); // 调用 MenuUI 的 editRow 方法
            } else if ("Delete".equals(buttonText)) {
                int confirm = JOptionPane.showConfirmDialog(panel, "确定要删除此行吗？", "确认删除", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    menuUI.deleteRow(rowIndex); // 调用 MenuUI 的 deleteRow 方法
                }
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.rowIndex = row; // 更新当前行索引
        button.setText(value != null ? value.toString() : ""); // 根据单元格的值设置按钮文本
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return button.getText(); // 返回按钮的当前文本
    }
}

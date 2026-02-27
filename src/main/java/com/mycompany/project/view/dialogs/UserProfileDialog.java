package com.mycompany.project.view.dialogs;

import com.mycompany.project.model.User;
import com.mycompany.project.view.utils.Theme;
import javax.swing.*;
import java.awt.*;

public class UserProfileDialog extends JDialog {

    public UserProfileDialog(Window owner, User user) {
        super(owner, "الملف الشخصي", ModalityType.APPLICATION_MODAL);
        setSize(400, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        initComponents(user);
    }

    private void initComponents(User user) {
        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
        header.setBackground(Theme.PRIMARY_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel nameLabel = new JLabel(user.getFullName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        nameLabel.setForeground(Color.WHITE);
        header.add(nameLabel);

        add(header, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel(new GridLayout(0, 1, 10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        content.setBackground(Color.WHITE);

        addDetail(content, "اسم المستخدم:", user.getUsername());
        addDetail(content, "الدور:", user.getRole());
        addDetail(content, "العمر:", String.valueOf(user.getAge()));
        addDetail(content, "النوع:", user.getGender());

        // Close Button
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JButton closeBtn = new JButton("إغلاق");
        Theme.styleButton(closeBtn);
        closeBtn.addActionListener(e -> dispose());
        footer.add(closeBtn);

        add(content, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    private void addDetail(JPanel panel, String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setForeground(Theme.PRIMARY_COLOR);

        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        v.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(l, BorderLayout.EAST); // Right align for label (Arabic style)
        row.add(v, BorderLayout.WEST);

        panel.add(row);
    }
}

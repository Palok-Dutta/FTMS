import javax.swing.*;

import java.awt.*;
import java.sql.*;

public class Utility {
    public static void SetColor(Component component, Color bgColor, Color forColor)
    {
        component.setBackground(bgColor);
        component.setForeground(forColor);
    }

    public static void ShowErrorInput(JFrame frame, String message)
    {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void AddLabel(Container comp, String labelName)
    {
        JLabel label = new JLabel(labelName);
        comp.add(label);
    }

    public static JTextField AddTextField(JFrame frame, int counter)
    {
        JTextField textField = new JTextField(counter);
        frame.add(textField);
        return textField;
    }

    public static String GetText(JTextField textField)
    {
        String text = textField.getText();
        return text;
    }

    public static JPasswordField AddPasswordField(JFrame frame, int counter)
    {
        JPasswordField passwordField = new JPasswordField(counter);
        frame.add(passwordField);
        return passwordField;
    }

    public static JButton AddButton(Container frame, String buttonName, Color bgColor, Color forColor, int fontSize)
    {
        JButton button = new JButton(buttonName);
        button.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(43, 60, 80)));
        frame.add(button);
        Utility.SetColor(button, bgColor, forColor);
        return button;
    }

    public static void ButtonAction(Runnable action, JButton button)
    {
        button.addActionListener(e ->{
            action.run();
        });
    }

    public static JComboBox<String> LoadClubComboBox() {
        JComboBox<String> cb = new JComboBox<>();
        try (Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT club_id, club_name FROM club")) {
            while (rs.next()) {
                int id = rs.getInt("club_id");
                String name = rs.getString("club_name");
                cb.addItem(id + " - " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cb;
    }

    public static JComboBox<String> LoadAgentComboBox() {
        JComboBox<String> cb = new JComboBox<>();
        try (Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT agent_id, agent_name FROM agent")) {
            while (rs.next()) {
                int id = rs.getInt("agent_id");
                String name = rs.getString("agent_name");
                cb.addItem(id + " - " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cb;
    }

    public static JComboBox<String> LoadPlayerComboBox() {
        JComboBox<String> cb = new JComboBox<>();
        try (Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT player_id, player_name FROM player")) {
            while (rs.next()) {
                int id = rs.getInt("player_id");
                String name = rs.getString("player_name");
                cb.addItem(id + " - " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cb;
    }

    public static JComboBox<String> LoadCoachComboBox() {
        JComboBox<String> cb = new JComboBox<>();
        try (Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT coach_id, coach_name FROM coach")) {
            while (rs.next()) {
                cb.addItem(rs.getInt("coach_id") + " - " + rs.getString("coach_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cb;
    }

    public static JComboBox<String> LoadScoutComboBox() {
        JComboBox<String> cb = new JComboBox<>();
        try (Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT scout_id, scout_name FROM scout")) {
            while (rs.next()) {
                cb.addItem(rs.getInt("scout_id") + " - " + rs.getString("scout_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cb;
    }

    public static JButton CreateSidebarButton(String text, boolean isParent)
    {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(52, 73, 94));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(220, 42));
        btn.setPreferredSize(new Dimension(220, 42));
        return btn;
    }

    public static JButton CreateChildButton(String text)
    {
        JButton btn = new JButton("    └─ " + text);
        btn.setBackground(new Color(36, 52, 68));
        btn.setForeground(new Color(170, 190, 210));
        btn.setFont(new Font("Arial", Font.PLAIN, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(220, 36));
        btn.setPreferredSize(new Dimension(220, 36));
        return btn;
    }

    public static JPanel CreateChildPanel()
    {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(36, 52, 68));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setVisible(false); // hidden by default
        panel.setMaximumSize(new Dimension(220, 300));
        return panel;
    }

    public static JPanel CreateCard(String title, String count, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Colored top bar
        JPanel topBar = new JPanel();
        topBar.setBackground(color);
        topBar.setPreferredSize(new Dimension(0, 6));
        card.add(topBar, BorderLayout.NORTH);

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(138, 154, 176));
        card.add(titleLabel, BorderLayout.CENTER);

        // Count
        JLabel countLabel = new JLabel(count);
        countLabel.setFont(new Font("Arial", Font.BOLD, 32));
        countLabel.setForeground(new Color(26, 43, 74));
        card.add(countLabel, BorderLayout.SOUTH);

        return card;
    }
    public static String GetCount(String query) {
        try (Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) return String.valueOf(rs.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "0";
    }

    public static void AddFormFields(JPanel formPanel, String[] labels, JComponent[] fields) {
        formPanel.setLayout(new java.awt.GridLayout(0, 1, 5, 5)); 
        
        for (int i = 0; i < labels.length; i++) {
            formPanel.add(new JLabel(labels[i] + ":"));
            formPanel.add(fields[i]);
        }
    }
}

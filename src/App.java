import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class App extends JFrame {

    JTextField searchBox;
    JTextArea resultArea;

    public App() {
        setTitle("Student Search");
        setSize(1000, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        searchBox = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        JButton pointsTableButton = new JButton("Points Table");
        JButton fixureButton = new JButton("Fixures");

        resultArea = new JTextArea(30, 30);
        resultArea.setEditable(false);

        JPanel topPanel = new JPanel(); 
        topPanel.add(searchBox);        
        topPanel.add(searchBtn);        

        add(topPanel, BorderLayout.NORTH);   
        
        JPanel middPanel = new JPanel();
        middPanel.add(pointsTableButton);
        middPanel.add(fixureButton);
        add(middPanel, BorderLayout.CENTER);

        add(new JScrollPane(resultArea), BorderLayout.SOUTH); 

        searchBtn.addActionListener(e -> searchPlayer());
        pointsTableButton.addActionListener(e -> displayPointsTable());

        setVisible(true);
    }

    void searchPlayer() {
        String name = searchBox.getText().trim();
        String url = "jdbc:mysql://localhost:3306/tms";
        String user = "root";    
        String pass = "";

        try {
            Connection con = DriverManager.getConnection(url, user, pass);

            String query = "SELECT player.player_name, team.team_name, " +
                            "player.jersey_no, player.batting_style, player.bowling_style " +
                            "from player " +
                            "join team on player.team_id = team.team_id " +
                            "where player.player_name like ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, "%" + name + "%"); // partial match

            ResultSet rs = ps.executeQuery();

            resultArea.setText(""); // clear old results
            boolean found = false;

            while (rs.next()) {
                found = true;
                resultArea.append("Name: " + rs.getString("player_name") + "\n");
                resultArea.append("Team: " + rs.getString("team_name") + "\n");
                resultArea.append("Jersey No: " + rs.getInt("jersey_no") + "\n");
                resultArea.append("Batting Style: " + rs.getString("batting_style") + "\n");
                resultArea.append("Bowling Style: " + rs.getString("bowling_style") + "\n");
                resultArea.append("-------------------\n");
            }

            if (!found) resultArea.setText("No student found.");
            con.close();

        } catch (SQLException ex) {
            resultArea.setText("DB Error: " + ex.getMessage());
        }
    }

    void displayPointsTable() {
        String url = "jdbc:mysql://localhost:3306/tms";
        String user = "root";
        String pass = "";

        try {
            Connection con = DriverManager.getConnection(url, user, pass);

            String query = "SELECT tournament.tournament_name, team.team_name, " +
                        "points_table.matches_played, points_table.won, points_table.lost, points_table.points " +
                        "FROM points_table " +
                        "JOIN team ON points_table.team_id = team.team_id " +
                        "JOIN tournament ON points_table.tournament_id = tournament.tournament_id";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            // Column headers
            String[] columns = {"Tournament", "Team", "Played", "Won", "Lost", "Points"};

            // Collect rows
            java.util.List<Object[]> rows = new java.util.ArrayList<>();
            while (rs.next()) {
                Object[] row = {
                    rs.getString("tournament_name"),
                    rs.getString("team_name"),
                    rs.getInt("matches_played"),
                    rs.getInt("won"),
                    rs.getInt("lost"),
                    rs.getInt("points")
                };
                rows.add(row);
            }

            // Convert to 2D array
            Object[][] data = rows.toArray(new Object[0][]);

            // Create table
            JTable table = new JTable(data, columns);

            // Show it
            JFrame frame = new JFrame("Table");

            frame.setSize(600, 400);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.add(new JScrollPane(table), BorderLayout.CENTER);
            frame.setVisible(true);

            con.close();

        } catch (SQLException ex) {
            resultArea.setText("DB Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new App();
    }
}
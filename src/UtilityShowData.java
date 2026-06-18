import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;


public class UtilityShowData{
    private static JScrollPane currentScrollPane = null;


    public static JPanel CreateDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 242, 245));

        // Title
        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(26, 43, 74));
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        panel.add(title, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(2, 4, 15, 15));
        cardsPanel.setBackground(new Color(240, 242, 245));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        cardsPanel.add(Utility.CreateCard("Total Clubs",     Utility.GetCount("SELECT COUNT(*) FROM club"),          new Color(46, 119, 208)));
        cardsPanel.add(Utility.CreateCard("Total Players",   Utility.GetCount("SELECT COUNT(*) FROM player"),        new Color(40, 167, 69)));
        cardsPanel.add(Utility.CreateCard("Total Coaches",   Utility.GetCount("SELECT COUNT(*) FROM coach"),         new Color(253, 126, 20)));
        cardsPanel.add(Utility.CreateCard("Total Agents",    Utility.GetCount("SELECT COUNT(*) FROM agent"),         new Color(111, 66, 193)));
        cardsPanel.add(Utility.CreateCard("Total Scouts",    Utility.GetCount("SELECT COUNT(*) FROM scout"),         new Color(23, 162, 184)));
        cardsPanel.add(Utility.CreateCard("Total Transfers", Utility.GetCount("SELECT (SELECT COUNT(*) FROM player_transfer) +" +
                                                                                "(SELECT COUNT(*) FROM coach_transfer) AS total_count"), 
                                                                                new Color(220, 53, 69)));

        panel.add(cardsPanel, BorderLayout.CENTER);
        return panel;
    }
    public static class ListPanelResult {
        public JPanel panel;
        public DefaultTableModel tableModel;
        public JPanel formPanel;

        public ListPanelResult(JPanel panel, DefaultTableModel tableModel, JPanel formPanel) {
            this.panel = panel;
            this.tableModel = tableModel;
            this.formPanel = formPanel;
        }
    }
    private static ListPanelResult ShowListPanel(String[] columns)
    {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //Create Table
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(scrollPane);

        return new ListPanelResult(panel, tableModel, null);
    }

    public static JPanel CreateClubListPanel() {
        String[] columns = {"Club Name", "League", "Squad Value"};
        ListPanelResult result = ShowListPanel(columns);
        JPanel panel = result.panel;
        DefaultTableModel tableModel = result.tableModel;

        LoadClubData(tableModel);

        return panel;
    }
    public static JPanel CreatePlayerListPanel()
    {
        String[] columns = {"Name", "Age", "Nationality", "Position", "Market Value", "Club", "Agent"};
        ListPanelResult result = ShowListPanel(columns);
        JPanel panel = result.panel;
        DefaultTableModel tableModel = result.tableModel;
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setPreferredSize(new java.awt.Dimension(280, 400));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Player"));

        LoadPlayerData(tableModel);

        JTextField txtName = new JTextField(15);
        JTextField txtAge = new JTextField(5);
        JTextField txtNationality = new JTextField(15);
        JTextField txtPosition = new JTextField(15);
        JTextField txtMarketValue = new JTextField(15);

        JComboBox<String> cbClub = Utility.LoadClubComboBox();
        JComboBox<String> cbAgent = Utility.LoadAgentComboBox();

        Utility.AddFormFields(formPanel, columns, new JComponent[]{
            txtName, txtAge,  txtNationality,txtPosition, txtMarketValue, cbClub, cbAgent
        });

        JButton addButton = Utility.AddButton(formPanel, "Add", Color.CYAN, Color.BLACK, 20);
        
        Utility.ButtonAction(() -> AddPlayerToRow(
            formPanel, tableModel, 
            txtName.getText(), 
            txtAge.getText(), 
            txtNationality.getText(), 
            txtPosition.getText(), 
            txtMarketValue.getText(), 
            cbClub, 
            cbAgent
        ), addButton);

        panel.add(formPanel, java.awt.BorderLayout.EAST);

        return panel;
    }

    public static JPanel CreateCoachListPanel()
    {
        String[] columns = {"Coach Name", "Nationality", "Experience", "Market Value", "Club", "Agent"};
        ListPanelResult result = ShowListPanel(columns);
        JPanel panel = result.panel;
        DefaultTableModel tableModel = result.tableModel;
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setPreferredSize(new java.awt.Dimension(280, 450)); // ফিল্ড বাড়ায় হাইট একটু বাড়ালাম
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Coach"));

        LoadCoachData(tableModel);

        JTextField txtName = new JTextField(15);
        JTextField txtNationality = new JTextField(15);
        JTextField txtExperience = new JTextField(5);
        JTextField txtMarketValue = new JTextField(15);

        JComboBox<String> cbClub = Utility.LoadClubComboBox();
        JComboBox<String> cbAgent = Utility.LoadAgentComboBox();

        Utility.AddFormFields(formPanel, columns, new JComponent[]{
            txtName, txtNationality, txtExperience, txtMarketValue, cbClub, cbAgent
        });

        JButton addButton = Utility.AddButton(formPanel, "Add", Color.CYAN, Color.BLACK, 20);
        
        Utility.ButtonAction(() -> AddCoachToRow(
            formPanel, tableModel, 
            txtName.getText(), 
            txtNationality.getText(),
            txtExperience.getText(), 
            txtMarketValue.getText(), 
            cbClub, 
            cbAgent
        ), addButton);

        panel.add(formPanel, java.awt.BorderLayout.EAST);

        return panel;
    }

    public static JPanel CreateAgentListPanel()
    {
        String[] columns = {"Agent Name", "Agency", "Commission Rate"};
        ListPanelResult result = ShowListPanel(columns);
        JPanel panel = result.panel;
        DefaultTableModel tableModel = result.tableModel;
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setPreferredSize(new java.awt.Dimension(280, 400));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Agent"));

        LoadAgentData(tableModel);

        JTextField txtName = new JTextField(15);
        JTextField txtAgency = new JTextField(15);
        JTextField txtCommission = new JTextField(10);

        Utility.AddFormFields(formPanel, columns, new JComponent[]{
            txtName, txtAgency, txtCommission
        });

        JButton addButton = Utility.AddButton(formPanel, "Add", Color.CYAN, Color.BLACK, 20);
        
        Utility.ButtonAction(() -> AddAgentToRow(
            formPanel, tableModel, 
            txtName.getText(), 
            txtAgency.getText(), 
            txtCommission.getText()
        ), addButton);

        panel.add(formPanel, java.awt.BorderLayout.EAST);

        return panel;
    }

    public static JPanel CreateScoutListPanel()
    {
        String[] columns = {"Scout Name", "Club"};
        ListPanelResult result = ShowListPanel(columns);
        JPanel panel = result.panel;
        DefaultTableModel tableModel = result.tableModel;
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setPreferredSize(new java.awt.Dimension(280, 400));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Scout"));

        LoadScoutData(tableModel);

        JTextField txtName = new JTextField(15);
        JComboBox<String> cbClub = Utility.LoadClubComboBox();

        Utility.AddFormFields(formPanel, columns, new JComponent[]{
            txtName, cbClub
        });

        JButton addButton = Utility.AddButton(formPanel, "Add", Color.CYAN, Color.BLACK, 20);
        
        Utility.ButtonAction(() -> AddScout(
            formPanel, tableModel, 
            txtName.getText(), 
            cbClub
        ), addButton);

        panel.add(formPanel, java.awt.BorderLayout.EAST);

        return panel;
    }

    public static JPanel CreateReportsPanel()
    {
        String[] columns = {"Market Value", "Prospect Status", "Scout", "Player"};
        ListPanelResult result = ShowListPanel(columns);
        JPanel panel = result.panel;
        DefaultTableModel tableModel = result.tableModel;
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setPreferredSize(new java.awt.Dimension(280, 450));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Scout Report"));

        LoadReportData(tableModel);

        JTextField txtMarketValue = new JTextField(15);
        JTextField txtProspect = new JTextField(15);

        JComboBox<String> cbScout = Utility.LoadScoutComboBox();
        
        JComboBox<String> cbPlayer = Utility.LoadPlayerComboBox();
        cbPlayer.insertItemAt("None", 0);
        cbPlayer.setSelectedIndex(0);

        Utility.AddFormFields(formPanel, columns, new JComponent[]{
            txtMarketValue, txtProspect, cbScout, cbPlayer,
        });

        JButton addButton = Utility.AddButton(formPanel, "Add Report", Color.CYAN, Color.BLACK, 20);
        
        Utility.ButtonAction(() -> AddReport(
            formPanel, tableModel, 
            txtMarketValue.getText().trim(), 
            txtProspect.getText().trim(), 
            cbScout, cbPlayer
        ), addButton);

        panel.add(formPanel, java.awt.BorderLayout.EAST);

        return panel;
    }

    public static JPanel CreateSearchPlayerPanel()
    {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Player"));

        JTextField textField = new JTextField(20);
        searchPanel.add(textField);
        JButton searchButton = Utility.AddButton(searchPanel, "Search", Color.GRAY, Color.black, 14);
        panel.add(searchPanel, BorderLayout.NORTH);

        
        Utility.ButtonAction(() -> {
        String nameInput = textField.getText().trim();
            SearchPlayer(panel, nameInput); 
        }, searchButton);

        return panel;
    }

    public static JPanel CreateSearchCoachPanel()
    {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Coach"));

        JTextField textField = new JTextField(20);
        searchPanel.add(textField);
        JButton searchButton = Utility.AddButton(searchPanel, "Search", Color.GRAY, Color.black, 14);
        panel.add(searchPanel, BorderLayout.NORTH);

        
        Utility.ButtonAction(() -> {
        String nameInput = textField.getText().trim();
            SearchCoach(panel, nameInput); 
        }, searchButton);

        return panel;
    }

    public static JPanel CreateSearchClubPanel()
    {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Club"));

        JTextField textField = new JTextField(20);
        searchPanel.add(textField);
        JButton searchButton = Utility.AddButton(searchPanel, "Search", Color.GRAY, Color.black, 14);
        panel.add(searchPanel, BorderLayout.NORTH);

        
        Utility.ButtonAction(() -> {
        String nameInput = textField.getText().trim();
            SearchClub(panel, nameInput); 
        }, searchButton);

        return panel;
    }


    private static void AddPlayerToRow(JPanel panel, DefaultTableModel model,
        String name, String age, String nationality, String position,
        String marketValue, JComboBox<String> cbClub, JComboBox<String> cbAgent) {

        String query = "INSERT INTO player (player_name, age, nationality, position, " +
                    "market_value, club_id, agent_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            int clubId  = Integer.parseInt(cbClub.getSelectedItem().toString().split(" - ")[0]);
            int agentId = Integer.parseInt(cbAgent.getSelectedItem().toString().split(" - ")[0]);

            pstmt.setString(1, name);
            pstmt.setInt(2, Integer.parseInt(age));
            pstmt.setString(3, nationality);
            pstmt.setString(4, position);
            pstmt.setString(5, marketValue);
            pstmt.setInt(6, clubId);
            pstmt.setInt(7, agentId);

            int inserted = pstmt.executeUpdate();
            if (inserted > 0) {
                LoadPlayerData(model);
                JOptionPane.showMessageDialog(panel, "Player Added!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(panel, "Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(panel, "Age and Market Value must be numbers!");
        }
    }

    private static void AddCoachToRow(JPanel panel, DefaultTableModel model,
        String name, String nationality, String experience, String marketValue, 
        JComboBox<String> cbClub, JComboBox<String> cbAgent) {

        String query = "INSERT INTO coach (coach_name, nationality, experience_years, market_value, club_id, agent_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            int clubId  = Integer.parseInt(cbClub.getSelectedItem().toString().split(" - ")[0]);
            int agentId = Integer.parseInt(cbAgent.getSelectedItem().toString().split(" - ")[0]);

            pstmt.setString(1, name);
            pstmt.setString(2, nationality);
            pstmt.setInt(3, Integer.parseInt(experience));
            pstmt.setString(4, marketValue);
            pstmt.setInt(5, clubId);
            pstmt.setInt(6, agentId);

            int inserted = pstmt.executeUpdate();
            if (inserted > 0) {
                LoadCoachData(model);
                JOptionPane.showMessageDialog(panel, "Coach Added Successfully!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(panel, "Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(panel, "Experience and Market Value must be numbers!");
        }
    }

    private static void AddAgentToRow(JPanel panel, DefaultTableModel model,
        String name, String agency, String commissionRate) {

        String query = "INSERT INTO agent (agent_name, agency, commission_rate) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, name);
            pstmt.setString(2, agency);
            pstmt.setDouble(3, Double.parseDouble(commissionRate));

            int inserted = pstmt.executeUpdate();
            if (inserted > 0) {
                LoadAgentData(model);
                JOptionPane.showMessageDialog(panel, "Agent Added Successfully!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(panel, "Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(panel, "Commission Rate must be a valid number!");
        }
    }

    private static void AddScout(JPanel panel, DefaultTableModel model,
        String name, JComboBox<String> cbClub) {

        String query = "INSERT INTO scout (scout_name, club_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            int clubId = Integer.parseInt(cbClub.getSelectedItem().toString().split(" - ")[0]);

            pstmt.setString(1, name);
            pstmt.setInt(2, clubId);

            int inserted = pstmt.executeUpdate();
            if (inserted > 0) {
                LoadScoutData(model);
                JOptionPane.showMessageDialog(panel, "Scout Added Successfully!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(panel, "Error: " + e.getMessage());
        }
    }

    private static void AddReport(JPanel panel, DefaultTableModel model,
        String marketValue, String prospect, JComboBox<String> cbScout, JComboBox<String> cbPlayer) {

        String query = "INSERT INTO report (market_value, prospect, scout_id, player_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            int scoutId = Integer.parseInt(cbScout.getSelectedItem().toString().split(" - ")[0]);

            pstmt.setString(1, marketValue);
            pstmt.setString(2, prospect);
            pstmt.setInt(3, scoutId);

            if (cbPlayer.getSelectedItem().toString().equals("None")) {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            } else {
                pstmt.setInt(4, Integer.parseInt(cbPlayer.getSelectedItem().toString().split(" - ")[0]));
            }

            int inserted = pstmt.executeUpdate();
            if (inserted > 0) {
                LoadReportData(model);
                JOptionPane.showMessageDialog(panel, "Scout Report Added Successfully!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(panel, "Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(panel, "Market Value must be a valid number!");
        }
    }

    public static void LoadClubData(DefaultTableModel model) {
        model.setRowCount(0);
        String query = "SELECT club_name, league, squad_value FROM club";

        try (Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("club_name"),
                    rs.getString("league"),
                    rs.getString("squad_value")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void LoadPlayerData(DefaultTableModel model) {
        model.setRowCount(0);
        String query = "SELECT p.player_name, p.nationality, p.age, p.position," +
                    "p.market_value, c.club_name, a.agent_name " +
                    "FROM player p " +
                    "JOIN club c ON p.club_id = c.club_id " +
                    "JOIN agent a ON p.agent_id = a.agent_id";

        try (Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("player_name"),
                    rs.getInt("age"),
                    rs.getString("nationality"),
                    rs.getString("position"),
                    rs.getString("market_value"),
                    rs.getString("club_name"),
                    rs.getString("agent_name")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void LoadCoachData(DefaultTableModel model) {
        model.setRowCount(0);
        String query = "SELECT c.coach_name, c.nationality, c.experience_years, c.market_value, cl.club_name, a.agent_name " +
                    "FROM coach c " +
                    "JOIN club cl ON c.club_id = cl.club_id " +
                    "JOIN agent a ON c.agent_id = a.agent_id";

        try (Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("coach_name"),
                    rs.getString("nationality"),
                    rs.getInt("experience_years"),
                    rs.getString("market_value"),
                    rs.getString("club_name"),
                    rs.getString("agent_name")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void LoadAgentData(DefaultTableModel model) {
        model.setRowCount(0);
        String query = "SELECT agent_name, agency, commission_rate FROM agent";

        try (Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("agent_name"),
                    rs.getString("agency"),
                    rs.getDouble("commission_rate")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void LoadScoutData(DefaultTableModel model) {
        model.setRowCount(0);
        String query = "SELECT s.scout_name, c.club_name " +
                    "FROM scout s " +
                    "JOIN club c ON s.club_id = c.club_id";

        try (Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("scout_name"),
                    rs.getString("club_name")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void LoadReportData(DefaultTableModel model) {
        model.setRowCount(0);
        
        String query = "SELECT r.market_value, r.prospect, s.scout_name, p.player_name " +
                    "FROM report r " +
                    "LEFT JOIN scout s ON r.scout_id = s.scout_id " +
                    "LEFT JOIN player p ON r.player_id = p.player_id";

        try (Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String playerName = rs.getString("player_name");
                
                model.addRow(new Object[]{
                    rs.getString("market_value"),
                    rs.getString("prospect"),
                    rs.getString("scout_name"),
                    (playerName != null) ? playerName : "-"
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void SearchPlayer(JPanel panel, String searchKeyword) {
    
        if (currentScrollPane != null)
        {
            panel.remove(currentScrollPane);
        }

        String[] columns = {"Name", "Age", "Nationality", "Position", "Market Value", "Club", "Agent"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        currentScrollPane = scrollPane;
        panel.add(scrollPane, BorderLayout.CENTER);

        String query = "SELECT " +
                    "    p.player_name, " +
                    "    p.age, " +
                    "    p.nationality, " + 
                    "    p.position, " +
                    "    p.market_value, " +
                    "    c.club_name, " +
                    "    a.agent_name " +
                    "FROM player p " +
                    "LEFT JOIN club c ON p.club_id = c.club_id " +
                    "LEFT JOIN agent a ON p.agent_id = a.agent_id " +
                    "WHERE p.player_name LIKE ?"; 

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, "%" + searchKeyword + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getString("player_name"),
                        rs.getInt("age"),
                        rs.getString("nationality"), 
                        rs.getString("position"),    
                        rs.getString("market_value"),
                        rs.getString("club_name"),
                        rs.getString("agent_name")
                    });
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        panel.revalidate();
        panel.repaint();
    }

    public static void SearchCoach(JPanel panel, String searchKeyword) {
    
        if (currentScrollPane != null)
        {
            panel.remove(currentScrollPane);
        }

        String[] columns = {"Coach Name", "Nationality", "Experience", "Market Value", "Club", "Agent"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        currentScrollPane = scrollPane;
        panel.add(scrollPane, BorderLayout.CENTER);

        String query = "SELECT " +
                    "    c.coach_name, " +
                    "    c.nationality, " +
                    "    c.experience_years, " +
                    "    c.market_value, " +
                    "    cl.club_name, " +
                    "    a.agent_name " +
                    "FROM coach c " +
                    "LEFT JOIN club cl ON c.club_id = cl.club_id " +
                    "LEFT JOIN agent a ON c.agent_id = a.agent_id " +
                    "WHERE c.coach_name LIKE ?"; 

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, "%" + searchKeyword + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getString("coach_name"),
                        rs.getString("nationality"),
                        rs.getInt("experience_years"),
                        rs.getString("market_value"),
                        rs.getString("club_name"),
                        rs.getString("agent_name")
                    });
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        panel.revalidate();
        panel.repaint();
    }

    public static void SearchClub(JPanel panel, String searchKeyword) {
    
        if (currentScrollPane != null)
        {
            panel.remove(currentScrollPane);
        }

        String[] columns = {"Club Name", "League", "Squad Value"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        currentScrollPane = scrollPane;
        panel.add(scrollPane, BorderLayout.CENTER);

        String query = "SELECT club_name, league, squad_value FROM club WHERE club_name LIKE ?"; 

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, "%" + searchKeyword + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getString("club_name"),
                        rs.getString("league"),
                        rs.getString("squad_value")
                    });
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        panel.revalidate();
        panel.repaint();
    }

    public static JPanel CreatePlayerTransferPanel()
    {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new java.awt.GridLayout(8, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Execute Player Transfer"));

        JComboBox<String> cbPlayer = Utility.LoadPlayerComboBox();
        JComboBox<String> cbFromClub = Utility.LoadClubComboBox();
        JComboBox<String> cbToClub = Utility.LoadClubComboBox();
        JComboBox<String> cbAgent = Utility.LoadAgentComboBox();
        JTextField txtFee = new JTextField(10);
        JTextField txtDate = new JTextField(10);
        txtDate.setText("2026-06-12"); 
        JTextField txtContactYears = new JTextField(10);

        formPanel.add(new JLabel("Select Player:"));       formPanel.add(cbPlayer);
        formPanel.add(new JLabel("From Club (Current):")); formPanel.add(cbFromClub);
        formPanel.add(new JLabel("To Club (Destination):"));formPanel.add(cbToClub);
        formPanel.add(new JLabel("Handling Agent:"));      formPanel.add(cbAgent);
        formPanel.add(new JLabel("Transfer Fee ($):"));   formPanel.add(txtFee);
        formPanel.add(new JLabel("Transfer Date:"));      formPanel.add(txtDate);
        formPanel.add(new JLabel("Contract Years:"));     formPanel.add(txtContactYears);

        JButton transferButton = Utility.AddButton(formPanel, "Execute Transfer", Color.GREEN, Color.BLACK, 16);
        formPanel.add(new JLabel("")); 
        formPanel.add(transferButton);

        Utility.ButtonAction(() -> ExecutePlayerTransfer(
            panel, cbPlayer, cbFromClub, cbToClub, cbAgent,
            txtFee.getText().trim(), txtDate.getText().trim(), txtContactYears.getText().trim()
        ), transferButton);

        panel.add(formPanel, BorderLayout.CENTER);

        return panel;
    }

    public static JPanel CreateCoachTransferPanel()
    {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new java.awt.GridLayout(8, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Execute Coach Transfer"));

        JComboBox<String> cbCoach = Utility.LoadCoachComboBox();
        JComboBox<String> cbFromClub = Utility.LoadClubComboBox();
        JComboBox<String> cbToClub = Utility.LoadClubComboBox();
        JComboBox<String> cbAgent = Utility.LoadAgentComboBox();
        JTextField txtFee = new JTextField(10);
        JTextField txtDate = new JTextField(10);
        txtDate.setText("2026-06-12"); 
        JTextField txtContactYears = new JTextField(10);

        formPanel.add(new JLabel("Select Coach:"));        formPanel.add(cbCoach);
        formPanel.add(new JLabel("From Club (Current):")); formPanel.add(cbFromClub);
        formPanel.add(new JLabel("To Club (Destination):"));formPanel.add(cbToClub);
        formPanel.add(new JLabel("Handling Agent:"));      formPanel.add(cbAgent);
        formPanel.add(new JLabel("Transfer Fee ($):"));   formPanel.add(txtFee);
        formPanel.add(new JLabel("Transfer Date:"));      formPanel.add(txtDate);
        formPanel.add(new JLabel("Contract Years:"));     formPanel.add(txtContactYears);

        JButton transferButton = Utility.AddButton(formPanel, "Execute Transfer", Color.GREEN, Color.BLACK, 16);
        formPanel.add(new JLabel("")); 
        formPanel.add(transferButton);

        Utility.ButtonAction(() -> ExecuteCoachTransfer(
            panel, cbCoach, cbFromClub, cbToClub, cbAgent,
            txtFee.getText().trim(), txtDate.getText().trim(), txtContactYears.getText().trim()
        ), transferButton);

        panel.add(formPanel, BorderLayout.CENTER);

        return panel;
    }

    public static void AddFormFields(JPanel formPanel, String[] labels, JComponent[] fields) {
        for (int i = 0; i < labels.length; i++) {
            formPanel.add(new JLabel(labels[i]));
            formPanel.add(fields[i]);
        }
    }

    private static void ExecutePlayerTransfer(JPanel panel, JComboBox<String> cbPlayer, 
        JComboBox<String> cbFromClub, JComboBox<String> cbToClub, JComboBox<String> cbAgent, 
        String fee, String date, String contractYears) {

        String insertTransferQuery = "INSERT INTO player_transfer (player_id, from_club_id, to_club_id, transfer_fee, transfer_date, contract_years, agent_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String updatePlayerQuery = "UPDATE player SET club_id = ? WHERE player_id = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Transaction শুরু

            int playerId = Integer.parseInt(cbPlayer.getSelectedItem().toString().split(" - ")[0]);
            int fromClubId = Integer.parseInt(cbFromClub.getSelectedItem().toString().split(" - ")[0]);
            int toClubId = Integer.parseInt(cbToClub.getSelectedItem().toString().split(" - ")[0]);
            int agentId = Integer.parseInt(cbAgent.getSelectedItem().toString().split(" - ")[0]);

            if (fromClubId == toClubId) {
                JOptionPane.showMessageDialog(panel, "Current club and Destination club cannot be the same!");
                return;
            }

            try (PreparedStatement pstmt1 = conn.prepareStatement(insertTransferQuery)) {
                pstmt1.setInt(1, playerId);
                pstmt1.setInt(2, fromClubId);
                pstmt1.setInt(3, toClubId);
                pstmt1.setString(4, fee);
                pstmt1.setString(5, date);
                pstmt1.setInt(6, Integer.parseInt(contractYears));
                pstmt1.setInt(7, agentId); 
                pstmt1.executeUpdate();
            }

            try (PreparedStatement pstmt2 = conn.prepareStatement(updatePlayerQuery)) {
                pstmt2.setInt(1, toClubId); 
                pstmt2.setInt(2, playerId); 
                pstmt2.executeUpdate();
            }

            conn.commit();
            JOptionPane.showMessageDialog(panel, "Transfer Completed Successfully! Player's club updated.");

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            JOptionPane.showMessageDialog(panel, "SQL Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(panel, "Fee and Contract Years must be valid numbers!");
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    private static void ExecuteCoachTransfer(JPanel panel, JComboBox<String> cbCoach, 
        JComboBox<String> cbFromClub, JComboBox<String> cbToClub, JComboBox<String> cbAgent, 
        String fee, String date, String contractYears) {

        String insertTransferQuery = "INSERT INTO coach_transfer (coach_id, from_club_id, to_club_id, transfer_fee, transfer_date, contract_years, agent_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String updateCoachQuery = "UPDATE coach SET club_id = ? WHERE coach_id = ?";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            int coachId = Integer.parseInt(cbCoach.getSelectedItem().toString().split(" - ")[0]);
            int fromClubId = Integer.parseInt(cbFromClub.getSelectedItem().toString().split(" - ")[0]);
            int toClubId = Integer.parseInt(cbToClub.getSelectedItem().toString().split(" - ")[0]);
            int agentId = Integer.parseInt(cbAgent.getSelectedItem().toString().split(" - ")[0]);

            if (fromClubId == toClubId) {
                JOptionPane.showMessageDialog(panel, "Current club and Destination club cannot be the same!");
                return;
            }

            try (PreparedStatement pstmt1 = conn.prepareStatement(insertTransferQuery)) {
                pstmt1.setInt(1, coachId);
                pstmt1.setInt(2, fromClubId);
                pstmt1.setInt(3, toClubId);
                pstmt1.setString(4, fee);
                pstmt1.setString(5, date);
                pstmt1.setInt(6, Integer.parseInt(contractYears));
                pstmt1.setInt(7, agentId); 
                pstmt1.executeUpdate();
            }

            try (PreparedStatement pstmt2 = conn.prepareStatement(updateCoachQuery)) {
                pstmt2.setInt(1, toClubId); 
                pstmt2.setInt(2, coachId); 
                pstmt2.executeUpdate();
            }

            conn.commit(); 
            JOptionPane.showMessageDialog(panel, "Coach Transfer Completed Successfully! Coach's club updated.");

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            JOptionPane.showMessageDialog(panel, "SQL Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(panel, "Fee and Contract Years must be valid numbers!");
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
}

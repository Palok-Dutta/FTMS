import javax.swing.*;
import java.awt.*;

public class SqlProject extends JFrame{
    String url = "";
    String user = "";
    String password = "";

    protected CardLayout cardLayout;
    protected JPanel rightMainPanel;

    public static void main(String[] args)
    {
        SqlProject sqlProject = new SqlProject();
        sqlProject.setVisible(true);
    }

    public SqlProject()
    {
        setTitle("Football Transfer Management System");
        setSize(1100, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(43, 60, 80));
        sidebar.setPreferredSize(new Dimension(220, 650));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // Dashboard
        JButton btnDashboard = Utility.CreateSidebarButton("  Dashboard", true);
        sidebar.add(btnDashboard);

        // Lists group
        JButton btnListGroup = Utility.CreateSidebarButton("  Lists  ", true);
        JPanel listChildren = Utility.CreateChildPanel();
        JButton btnClubList   = Utility.CreateChildButton("Club List");
        JButton btnPlayerList = Utility.CreateChildButton("Player List");
        JButton btnCoachList  = Utility.CreateChildButton("Coach List");
        JButton btnAgentList  = Utility.CreateChildButton("Agent List");
        JButton btnScoutList  = Utility.CreateChildButton("Scout List");
        listChildren.add(btnClubList);
        listChildren.add(btnPlayerList);
        listChildren.add(btnCoachList);
        listChildren.add(btnAgentList);
        listChildren.add(btnScoutList);
        sidebar.add(btnListGroup);
        sidebar.add(listChildren);

        // Search group
        JButton btnSearchGroup = Utility.CreateSidebarButton("  Search  ", true);
        JPanel searchChildren = Utility.CreateChildPanel();
        JButton btnSearchPlayer = Utility.CreateChildButton("Search Player");
        JButton btnSearchCoach  = Utility.CreateChildButton("Search Coach");
        JButton btnSearchClub   = Utility.CreateChildButton("Search Club");
        searchChildren.add(btnSearchPlayer);
        searchChildren.add(btnSearchCoach);
        searchChildren.add(btnSearchClub);
        sidebar.add(btnSearchGroup);
        sidebar.add(searchChildren);

        // Transfers group
        JButton btnTransferGroup = Utility.CreateSidebarButton("  Transfers  ", true);
        JPanel transferChildren = Utility.CreateChildPanel();
        JButton btnPlayerTransfer = Utility.CreateChildButton("Player Transfer");
        JButton btnCoachTransfer  = Utility.CreateChildButton("Coach Transfer");
        transferChildren.add(btnPlayerTransfer);
        transferChildren.add(btnCoachTransfer);
        sidebar.add(btnTransferGroup);
        sidebar.add(transferChildren);

        // Reports
        JButton btnReports = Utility.CreateSidebarButton("  Reports", true);
        sidebar.add(btnReports);

        sidebar.add(Box.createVerticalGlue());

        add(sidebar, BorderLayout.WEST);


        cardLayout = new CardLayout();
        rightMainPanel = new JPanel(cardLayout);

        btnListGroup.addActionListener(e -> {
            boolean v = listChildren.isVisible();
            listChildren.setVisible(!v);
            btnListGroup.setText(v ? "  Lists  " : "  Lists  ▼");
            sidebar.revalidate();
            sidebar.repaint();
        });

        btnSearchGroup.addActionListener(e -> {
            boolean v = searchChildren.isVisible();
            searchChildren.setVisible(!v);
            btnSearchGroup.setText(v ? "  Search  " : "  Search  ▼");
            sidebar.revalidate();
            sidebar.repaint();
        });

        btnTransferGroup.addActionListener(e -> {
            boolean v = transferChildren.isVisible();
            transferChildren.setVisible(!v);
            btnTransferGroup.setText(v ? "  Transfers  " : "  Transfers  ▼");
            sidebar.revalidate();
            sidebar.repaint();
        });

        // Add these
        rightMainPanel.add(UtilityShowData.CreateDashboardPanel(), "DashboardPage");
        rightMainPanel.add(UtilityShowData.CreateClubListPanel(), "ClubListPage");
        rightMainPanel.add(UtilityShowData.CreatePlayerListPanel(), "PlayerListPage");
        rightMainPanel.add(UtilityShowData.CreateCoachListPanel(), "CoachListPage");
        rightMainPanel.add(UtilityShowData.CreateAgentListPanel(), "AgentListPage");
        rightMainPanel.add(UtilityShowData.CreateScoutListPanel(), "ScoutListPage");
        rightMainPanel.add(UtilityShowData.CreateSearchPlayerPanel(), "SearchPlayerPage");
        rightMainPanel.add(UtilityShowData.CreateSearchCoachPanel(), "SearchCoachPage");
        rightMainPanel.add(UtilityShowData.CreateSearchClubPanel(), "SearchClubPage");
        rightMainPanel.add(UtilityShowData.CreatePlayerTransferPanel(), "PlayerTransferPage");
        rightMainPanel.add(UtilityShowData.CreateCoachTransferPanel(), "CoachTransferPage");
        rightMainPanel.add(UtilityShowData.CreateReportsPanel(), "ReportsPage");

        btnDashboard.addActionListener(e -> cardLayout.show(rightMainPanel, "DashboardPage"));
        btnClubList.addActionListener(e -> cardLayout.show(rightMainPanel, "ClubListPage"));
        btnPlayerList.addActionListener(e -> {
            for (java.awt.Component comp : rightMainPanel.getComponents()) {
                if (comp.getName() != null && comp.getName().equals("PlayerListPage")) {
                    rightMainPanel.remove(comp);
                }
            }
            rightMainPanel.add(UtilityShowData.CreatePlayerListPanel(), "PlayerListPage");
            cardLayout.show(rightMainPanel, "PlayerListPage");
        });
        btnCoachList.addActionListener(e -> {
            for (java.awt.Component comp : rightMainPanel.getComponents()) {
                if (comp.getName() != null && comp.getName().equals("CoachListPage")) {
                    rightMainPanel.remove(comp);
                }
            }
            rightMainPanel.add(UtilityShowData.CreateCoachListPanel(), "CoachListPage");
            cardLayout.show(rightMainPanel, "CoachListPage");
        });
        btnAgentList.addActionListener(e -> cardLayout.show(rightMainPanel, "AgentListPage"));
        btnScoutList.addActionListener(e -> cardLayout.show(rightMainPanel, "ScoutListPage"));
        btnSearchPlayer.addActionListener(e -> cardLayout.show(rightMainPanel, "SearchPlayerPage"));
        btnSearchCoach.addActionListener(e -> cardLayout.show(rightMainPanel, "SearchCoachPage"));
        btnSearchClub.addActionListener(e -> cardLayout.show(rightMainPanel, "SearchClubPage"));
        btnPlayerTransfer.addActionListener(e -> cardLayout.show(rightMainPanel, "PlayerTransferPage"));
        btnCoachTransfer.addActionListener(e -> cardLayout.show(rightMainPanel, "CoachTransferPage"));
        btnReports.addActionListener(e -> cardLayout.show(rightMainPanel, "ReportsPage"));

        add(rightMainPanel, BorderLayout.CENTER);
    }
}

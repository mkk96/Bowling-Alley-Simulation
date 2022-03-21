/* ShowScore.java 
 * 
 * class for adhoc queries like get score of player, overall game max score,  
 * min score etc 
 * 
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class ShowScores implements ActionListener, ListSelectionListener {

    private final JFrame win;

    private final JButton finished,maxPlayerScore,minPlayerScore,minScore,maxScore;

    private final JList<Vector> outputList;
    private final JList<Vector> allBowlers;

    private String selectedNick;

    private Vector bowlerdb;
    private final Vector party;
    
    public ShowScores() {

        win = new JFrame("Show Scores");
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        JPanel colPanel = new JPanel();
        colPanel.setLayout(new GridLayout(1, 3));

        // Controls Panel
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridLayout(9, 2));
        controlsPanel.setBorder(new TitledBorder("Queries"));

        maxScore = new JButton("Top Score");
        JPanel maxScorePanel = new JPanel();
        maxScorePanel.setLayout(new FlowLayout());
        maxScore.addActionListener(this);
        maxScorePanel.add(maxScore);
        controlsPanel.add(maxScorePanel);
        
        minScore = new JButton("Lowest Score");
        JPanel minScorePanel = new JPanel();
        minScorePanel.setLayout(new FlowLayout());
        minScore.addActionListener(this);
        minScorePanel.add(minScore);
        controlsPanel.add(minScorePanel);

        maxPlayerScore = new JButton("Player Highest Score");
        JPanel maxPlayerScorePanel = new JPanel();
        maxPlayerScorePanel.setLayout(new FlowLayout());
        maxPlayerScore.addActionListener(this);
        maxPlayerScorePanel.add(maxPlayerScore);
        controlsPanel.add(maxPlayerScorePanel);

        minPlayerScore = new JButton("Player Lowest Score");
        JPanel minPlayerScorePanel = new JPanel();
        minPlayerScorePanel.setLayout(new FlowLayout());
        minPlayerScore.addActionListener(this);
        minPlayerScorePanel.add(minPlayerScore);
        controlsPanel.add(minPlayerScorePanel);

        finished = new JButton("Close");
        JPanel finishedPanel = new JPanel();
        finishedPanel.setLayout(new FlowLayout());
        finished.addActionListener(this);
        finishedPanel.add(finished);
        controlsPanel.add(finishedPanel);

        // Scores Database
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new FlowLayout());
        scorePanel.setBorder(new TitledBorder("Results"));


        party = new Vector();

        Vector empty = new Vector();
        empty.add("(Empty)");

        outputList = new JList(empty);
        outputList.setFixedCellWidth(120);
        outputList.setVisibleRowCount(5); 
        outputList.addListSelectionListener(this);
        JScrollPane scorePane = new JScrollPane(outputList);
        scorePanel.add(scorePane);
       
        
       // Bowler Database
        JPanel bowlerPanel = new JPanel();
        bowlerPanel.setLayout(new FlowLayout());
        bowlerPanel.setBorder(new TitledBorder("Player List"));

        try {
            bowlerdb = new Vector(BowlerFile.getBowlers());
        } catch (Exception e) {
            System.err.println("File Error");
            bowlerdb = new Vector();
        }
        allBowlers = new JList(bowlerdb);
        allBowlers.setVisibleRowCount(8);
        allBowlers.setFixedCellWidth(120);
        JScrollPane bowlerPane = new JScrollPane(allBowlers);
        bowlerPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        allBowlers.addListSelectionListener(this);
        bowlerPanel.add(bowlerPane);

        colPanel.add(scorePanel);
        colPanel.add(controlsPanel);
        colPanel.add(bowlerPanel);
        

        win.getContentPane().add("Center", colPanel);

        win.pack();

        // Center Window on Screen
        Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        win.setLocation(
                ((screenSize.width) / 2) - ((win.getSize().width) / 2),
                ((screenSize.height) / 2) - ((win.getSize().height) / 2));
        win.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(minPlayerScore)) {
            System.out.println("in minPlayerScore");
            party.clear();
            if (selectedNick != null) {
            	try {
                    
    	            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/unit2",
    	    				"root", "mYSQLSERVER");
    	    		
    	    		PreparedStatement statement =connection.prepareStatement("SELECT min(score) from score_history where nick=?");
    	    		statement.setString(1, selectedNick);
    	    		ResultSet resultSet = statement.executeQuery();
    	    		if(resultSet.next()) {
    	    			party.add(Integer.toString(resultSet.getInt(1)));
    	                outputList.setListData(party);
    	    		}
                }
                catch(SQLException exp) {
                	System.out.println(exp);
                }
            }
        }

        if (e.getSource().equals(maxPlayerScore)) {
            System.out.println("in maxPlayerScore");
            party.clear();
            if (selectedNick != null) {
            	try {
                    
    	            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/unit2",
    	    				"root", "mYSQLSERVER");
    	    		
    	    		PreparedStatement statement =connection.prepareStatement("SELECT max(score) from score_history where nick=?");
    	    		statement.setString(1, selectedNick);
    	    		ResultSet resultSet = statement.executeQuery();
    	    		if(resultSet.next()) {
    	    			party.add(Integer.toString(resultSet.getInt(1)));
    	                outputList.setListData(party);
    	    		}
                }
                catch(SQLException exp) {
                	System.out.println(exp);
                }
            }
        }

        if (e.getSource().equals(minScore)) {
            System.out.println("in minScore");
            party.clear();
            try {
            
	            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/unit2",
	    				"root", "mYSQLSERVER");
	    		
	    		PreparedStatement statement =connection.prepareStatement("SELECT min(score) from score_history");
	    		ResultSet resultSet = statement.executeQuery();
	    		if(resultSet.next()) {
	    			party.add(Integer.toString(resultSet.getInt(1)));
	                outputList.setListData(party);
	    		}
            }
            catch(SQLException exp) {
            	System.out.println(exp);
            }
        }
        if (e.getSource().equals(maxScore)) {
            System.out.println("in maxScore");
            party.clear();
            try {
                
	            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/unit2",
	    				"root", "mYSQLSERVER");
	    		
	    		PreparedStatement statement =connection.prepareStatement("SELECT max(score) from score_history");
	    		ResultSet resultSet = statement.executeQuery();
	    		if(resultSet.next()) {
	    			party.add(Integer.toString(resultSet.getInt(1)));
	                outputList.setListData(party);
	    		}
            }
            catch(SQLException exp) {
            	System.out.println(exp);
            }
        }
        if (e.getSource().equals(finished)) {
            win.hide();
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource().equals(allBowlers)) {
            selectedNick =
                    ((String) ((JList) e.getSource()).getSelectedValue());
        }
        
    }
    

}

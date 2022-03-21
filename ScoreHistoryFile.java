/**
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

import java.util.*;
import java.io.*;
import java.sql.*;

public class ScoreHistoryFile {
	
	public static void addScore(String nick, String date, String score)
		throws SQLException {
		
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/unit2",
				"root", "mYSQLSERVER");
		
		PreparedStatement statement =connection.prepareStatement("insert into score_history values (?,?,?)");
		statement.setString(1, nick);
		statement.setString(2, date);
		statement.setString(3, score);
		
		statement.executeUpdate();
		
		System.out.println("Add successfull don't worry");
	}

	public static Vector getScores(String nick)
		throws SQLException {
		
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/unit2",
				"root", "mYSQLSERVER");
		
		PreparedStatement statement =connection.prepareStatement("SELECT * from score_history WHERE nick = ?");
		statement.setString(1, nick);
		ResultSet resultSet = statement.executeQuery();
		
		Vector scores = new Vector();
		
		while(resultSet.next()) {
			scores.add(new Score(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3)));
		}
		
		System.out.println("retrieval successfull don't worry");
		
		return scores;
	}

}

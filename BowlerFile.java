/* BowlerFile.java
 *
 *  Version:
 *  		$Id$
 * 
 *  Revisions:
 * 		$Log: BowlerFile.java,v $
 * 		Revision 1.5  2003/02/02 17:36:45  ???
 * 		Updated comments to match javadoc format.
 * 		
 * 		Revision 1.4  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 * 		
 * 
 */

/**
 * Class for interfacing with Bowler database
 *
 */

import java.util.*;
import java.io.*;
import java.sql.*;

class BowlerFile {

	/** The location of the bowler database */
    /**
     * Retrieves bowler information from the database and returns a Bowler objects with populated fields.
     *
     * @param nickName	the nickName of the bolwer to retrieve
     *
     * @return a Bowler object
     * 
     */
	
	public static Bowler registerPatron(String nickName) {
		Bowler patron = null;

		try {
			// only one patron / nick.... no dupes, no checks

			patron = BowlerFile.getBowlerInfo(nickName);

		} catch (Exception e) {
			System.err.println("Error..." + e);
		}

		return patron;
	}
	
	
	public static Bowler getBowlerInfo(String nickName)
		throws SQLException {
		
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/unit2",
				"root", "mYSQLSERVER");
		
		PreparedStatement statement =connection.prepareStatement("SELECT * from bowlers WHERE nick = ?");
		statement.setString(1, nickName);
		ResultSet resultSet = statement.executeQuery();
		
		String nick,full,email;
		
		if(resultSet.next()) {
			nick = resultSet.getString(1);
			full = resultSet.getString(2);
			email = resultSet.getString(3);
			System.out.println(
					"Nick: "
						+ nick
						+ " Full: "
						+ full
						+ " email: "
						+ email
					);
			
			return (new Bowler(nick,full,email));
		}
		
		System.out.println("Nick not found...");
		return null;
	}

    /**
     * Stores a Bowler in the database
     *
     * @param nickName	the NickName of the Bowler
     * @param fullName	the FullName of the Bowler
     * @param email	the E-mail Address of the Bowler
     *
     */

	public static void putBowlerInfo(
		String nickName,
		String fullName,
		String email)
		throws SQLException {
		
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/unit2",
				"root", "mYSQLSERVER");
		
		PreparedStatement statement =connection.prepareStatement("insert into bowlers values (?,?,?)");
		statement.setString(1, nickName);
		statement.setString(2, fullName);
		statement.setString(3, email);
		
		statement.executeUpdate();
	}

    /**
     * Retrieves a list of nicknames in the bowler database
     *
     * @return a Vector of Strings
     * 
     */

	public static Vector<String> getBowlers()
		throws SQLException {
		
		Vector<String> allBowlers = new Vector<String>();
		
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/unit2",
				"root", "mYSQLSERVER");
		
		PreparedStatement statement =connection.prepareStatement("SELECT nick from bowlers");
		ResultSet resultSet = statement.executeQuery();
		
		while(resultSet.next()) {
			allBowlers.add(resultSet.getString(1));
		}
		return allBowlers;
	}

}
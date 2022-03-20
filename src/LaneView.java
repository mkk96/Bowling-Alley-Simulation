/*
 *  constructs a prototype Lane View
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.Arrays;

public class LaneView implements LaneObserver, ActionListener {

	private int roll;
	private boolean initDone = true;

	JFrame frame;
	Container cpanel;
	Vector bowlers;
	int cur;
	Iterator bowlIt;
	
	JPanel[][] balls;
	JPanel jp;
	JLabel[][] ballLabel;
	JPanel[][] scores;
	JLabel[][] scoreLabel;
	JPanel[][] ballGrid;
	JPanel[] pins;
    String thrower;
	JButton maintenance;
	JButton throw_the_ball;
	 JLabel current_thrower;
	 JLabel curr_throw;
	Lane lane;

	public LaneView(Lane lane, int laneNum) {

		this.lane = lane;

		initDone = true;
		frame = new JFrame("Lane " + laneNum + ":");
		cpanel = frame.getContentPane();
		cpanel.setLayout(new BorderLayout());

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.hide();
			}
		});

		cpanel.add(new JPanel());

	}

	public void show() {
		frame.show();
	}

	public void hide() {
		frame.hide();
	}

	private JPanel makeFrame(Party party) {

		initDone = false;
		bowlers = party.getMembers();
		int numBowlers = bowlers.size();

		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(0, 1));

		balls = new JPanel[numBowlers][23];
		ballLabel = new JLabel[numBowlers][23];
		scores = new JPanel[numBowlers][10];
		scoreLabel = new JLabel[numBowlers][10];
		ballGrid = new JPanel[numBowlers][10];
		pins = new JPanel[numBowlers];

		for (int i = 0; i != numBowlers; i++) {
			for (int j = 0; j != 23; j++) {
				ballLabel[i][j] = new JLabel(" ");
				balls[i][j] = new JPanel();
				balls[i][j].setBorder(
					BorderFactory.createLineBorder(Color.BLACK));
				balls[i][j].add(ballLabel[i][j]);
			}
		}

		for (int i = 0; i != numBowlers; i++) {
			for (int j = 0; j != 9; j++) {
				ballGrid[i][j] = new JPanel();
				ballGrid[i][j].setLayout(new GridLayout(0, 3));
				ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
				ballGrid[i][j].add(balls[i][2 * j], BorderLayout.EAST);
				ballGrid[i][j].add(balls[i][2 * j + 1], BorderLayout.EAST);
			}
			int j = 9;
			ballGrid[i][j] = new JPanel();
			ballGrid[i][j].setLayout(new GridLayout(0, 3));
			ballGrid[i][j].add(balls[i][2 * j]);
			ballGrid[i][j].add(balls[i][2 * j + 1]);
			ballGrid[i][j].add(balls[i][2 * j + 2]);
		}

		for (int i = 0; i != numBowlers; i++) {
			pins[i] = new JPanel();
			pins[i].setBorder(
				BorderFactory.createTitledBorder(
					((Bowler) bowlers.get(i)).getNick()));
			pins[i].setLayout(new GridLayout(0, 10));
			for (int k = 0; k != 10; k++) {
				scores[i][k] = new JPanel();
				scoreLabel[i][k] = new JLabel("  ", SwingConstants.CENTER);
				scores[i][k].setBorder(
					BorderFactory.createLineBorder(Color.BLACK));
				
				//---- changed row, col. row was 0 and cols 1. this is where emoji appears. row 0 means diff row for score and emoji
				scores[i][k].setLayout(new GridLayout(2, 1));
				scores[i][k].add(ballGrid[i][k], BorderLayout.EAST);
				scores[i][k].add(scoreLabel[i][k], BorderLayout.SOUTH);
				pins[i].add(scores[i][k], BorderLayout.EAST);
			}
			panel.add(pins[i]);
		}

		initDone = true;
		return panel;
	}
	
	
	//---- x is a matrix used to display gif only after both the strike has been made by the user
	// it is of size users X games (game means 2 strikes per user). 0 -> first strike 1-> second strike
	// we display the gif only after 2nd strike.
	int x[][] = new int[10][10];

	public void receiveLaneEvent(Party pty, int theIndex, Bowler theBowler, int[][] theCumulScore, HashMap theScore, int theFrameNum, int[] theCurScores, int theBall, boolean mechProblem,boolean status,boolean game_completed) {
		
		if (lane.isPartyAssigned()&& status==false) {
			if(game_completed==true)
			{
				for(int i=0;i<10;i++)
				{
					for(int j=0;j<10;j++)
					{
						x[i][j]=0;
					}
				}
			}
			int numBowlers = pty.getMembers().size();
			while (!initDone) {
				//System.out.println("chillin' here.");
				try {
					Thread.sleep(1);
				} catch (Exception e) {
				}
			}

			if (theFrameNum == 1
				&& theBall == 0
				&& theIndex == 0) {
				System.out.println("Making the frame.");
				cpanel.removeAll();
				cpanel.add(makeFrame(pty), "Center");

				// Button Panel
				JPanel buttonPanel = new JPanel();
				buttonPanel.setLayout(new FlowLayout());

				Insets buttonMargin = new Insets(4, 4, 4, 4);

				maintenance = new JButton("Maintenance Call");
				JPanel maintenancePanel = new JPanel();
				maintenancePanel.setLayout(new FlowLayout());
				maintenance.addActionListener(this);
				maintenancePanel.add(maintenance);
				buttonPanel.add(maintenancePanel);

				cpanel.add(buttonPanel, "South");
				
				//Adding throw button
				JPanel new_buttonPanel = new JPanel();
				new_buttonPanel.setLayout(new FlowLayout());

				Insets new_buttonMargin = new Insets(4, 4, 4, 4);
                throw_the_ball = new JButton("Throw Ball"+theBowler.getNick());
				JPanel throwPanel = new JPanel();
				throwPanel.setLayout(new FlowLayout());
				throw_the_ball.addActionListener(this);
				throwPanel.add(throw_the_ball);
                new_buttonPanel.add(throwPanel);
                cpanel.add(new_buttonPanel, "North");
                
                 jp=new JPanel();
                jp.setLayout(new FlowLayout( FlowLayout.CENTER));
                current_thrower=new JLabel("Current Thrower:");
          
                      
                curr_throw=new JLabel();
                
                jp.add(current_thrower);
                jp.add(curr_throw);
                
                cpanel.add(jp,"East");
                		
				frame.pack();

			}
			System.out.println(theBowler.getNick());
			this.curr_throw.setText(theBowler.getNick());
			int[][] lescores = theCumulScore;
			boolean flag;
			for (int k = 0; k < numBowlers; k++){
				for (int i = 0; i < 21; i++) {
					if (((int[]) ((HashMap) theScore)
						.get(bowlers.get(k)))[i]
						!= -1)
						if (((int[]) ((HashMap) theScore)
							.get(bowlers.get(k)))[i]
							== 10
							&& (i % 2 == 0 || i == 19))
							ballLabel[k][i].setText("X");
						else if (
							i > 0
								&& ((int[]) ((HashMap) theScore)
									.get(bowlers.get(k)))[i]
									+ ((int[]) ((HashMap) theScore)
										.get(bowlers.get(k)))[i
									- 1]
									== 10
								&& i % 2 == 1)
							ballLabel[k][i].setText("/");
						else if ( ((int[])((HashMap) theScore).get(bowlers.get(k)))[i] == -2 ){
							
							ballLabel[k][i].setText("F");
						} else
							ballLabel[k][i].setText(
								(new Integer(((int[]) ((HashMap) theScore)
									.get(bowlers.get(k)))[i]))
									.toString());
				}
				for (int i = 0; i <= theFrameNum - 1; i++) {
					flag=false;
					if (lescores[k][i] != 0){
						if(i==0||(i>0&&lescores[k][i]!=lescores[k][i-1]-1))
						{
							if(i==9)
							{
								scoreLabel[k][i].setText(
										(new Integer(lescores[k][i]+1)).toString());	
							}
							else
							{
						      scoreLabel[k][i].setText((new Integer(lescores[k][i])).toString());
							}
						  
					
						//---- Here the gif is displayed accoring to the score. (diff bw prev and curr cumul. score)
						//try{
							int prev = i>0 ? lescores[k][i-1] : 0;
							int diff = Math.abs(lescores[k][i] - prev);
							//BufferedImage myPicture;
							Icon imgIcon;
							if(diff < 7){
								imgIcon = new ImageIcon(this.getClass().getResource("images/low_diff_small.gif"));
								//myPicture = ImageIO.read(new File("tenor.gif"));
							}
							else if(diff < 9){
								imgIcon = new ImageIcon(this.getClass().getResource("images/medlow_diff_small.gif"));
								//myPicture = ImageIO.read(new File("tenor.gif"));
							}
							else if(diff < 11){
								if(diff==10)
								{
									flag=true;
								}
								imgIcon = new ImageIcon(this.getClass().getResource("images/med_diff_small.gif"));
							}
							else{
								imgIcon = new ImageIcon(this.getClass().getResource("images/high_diff_small.gif"));
							}

							// BufferedImage myPicture = ImageIO.read(new File("1_small.jpg"));
							JLabel picLabel = new JLabel(imgIcon);
							JPanel emojiPanel = new JPanel();
							emojiPanel.setLayout(new FlowLayout());
							emojiPanel.add(picLabel);

							if(x[k][i]==1||flag&&x[k][i]!=-1)
							{//only display after 2nd strike
								scores[k][i].add(emojiPanel);
								x[k][i]=-1;
							}
							if(x[k][i]!=-1)
							{
							x[k][i]++;}

							//System.out.println(k+" "+i+"");
						/*}catch(Exception E){
							System.out.println("Error loading image!");
						}*/
						}
						
					}
				}
			}

		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(maintenance)) {
			lane.pauseGame();
		}
		else if(e.getSource().equals(throw_the_ball))
		{
		   lane.throwBall();
		}
	}

}

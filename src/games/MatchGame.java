package games;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.border.*;

/**
 Made by Vsevolod_FLPPV September 2025; 
 JAVA SE-24;
 Special thanks to Max_STDNKN for a technical support; 
 Open code for free distribution;
 */

public class MatchGame extends JFrame {
	static BufferedImage img = null;
	protected static File matchSound = new File("sound\\Into_the_Depths.wav");
    protected static File noMatchSound = new File("sound\\Operator_Error.wav");
    protected static File gameOverSound = new File("sound\\Far_Away.wav");
    protected static Clip matchSoundClip;
    protected static Clip noMatchSoundClip;
    protected static Clip gameOverSoundClip;
	protected String defaultGameSubject = "Birds";
	protected String defaultImageCover = "QMark1_200x130.jpg";
	protected JPanel gamePanel = new JPanel();
	protected JLabel[] photoLabel = new JLabel[20];	
	protected JPanel resultPanel = new ImagePanel(new ImageIcon("image\\covers\\resultPanelBackground.jpg").getImage());
	protected JCheckBox muteSoundCheckBox = new JCheckBox("Mute", false);
	protected JLabel player1Label = new JLabel();
	protected JLabel player2Label = new JLabel();
	protected JTextField[] scoreTextField = new JTextField[2];
	protected JLabel messageLabel = new JLabel();
	protected JPanel playersPanel = new JPanel();
	protected ButtonGroup playersButtonGroup = new ButtonGroup();
	protected JRadioButton twoPlayersRadioButton = new JRadioButton();
	protected JRadioButton onePlayerRadioButton = new JRadioButton();	
	protected JPanel playWhoPanel = new JPanel();
	protected ButtonGroup playWhoButtonGroup = new ButtonGroup();
	protected JRadioButton playAloneRadioButton = new JRadioButton();
	protected JRadioButton playComputerRadioButton = new JRadioButton();
	protected JPanel difficultyPanel = new JPanel();
	protected ButtonGroup difficultyButtonGroup = new ButtonGroup();
	protected JRadioButton easiestRadioButton = new JRadioButton();
	protected JRadioButton easyRadioButton = new JRadioButton();
	protected JRadioButton hardRadioButton = new JRadioButton();
	protected JRadioButton hardestRadioButton = new JRadioButton();
	protected JPanel buttonsPanel = new JPanel();
	protected JButton startStopButton = new JButton();
	protected JButton exitButton = new JButton();
	protected ImageIcon[] photo = new ImageIcon[10];
	protected ImageIcon defaultImage;
	protected ImageIcon cover;
	protected int[] photoIndex = new int[20];
	protected int photosRemaining;
	protected int[] score = new int[2];
	protected boolean[] photoFound = new boolean[20];
	protected int playerNumber, choiceNumber;
	protected int[] choice = new int[2];
	protected boolean canClick = false; 
	protected boolean gameOver;
	protected static Thread backgroundMusicThread;
	protected static Thread gameSoundThread;
	protected int muteSoundCheckBoxClicksCounter = 0;
	protected int labelSelected;
	protected Timer displayTimer;	
	protected Random myRandom = new Random();
	protected boolean smartComputer;
	protected int difficulty = 1;
	protected Timer delayTimer;
	protected int[] memory = new int[20];
	protected int[] matchFound = new int[2];
	
	Border messageLabelBeveledBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, new Color(155, 0, 0));
	Border playersPanelBeveledBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, Color.DARK_GRAY);
	Border playWhoPanelBeveledBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, Color.DARK_GRAY);
	Border difficultyPanelBeveledBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED, new Color(155, 0, 0), Color.DARK_GRAY);
	Border startStopButtonBeveledBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.WHITE, Color.DARK_GRAY);
	
		
	TitledBorder messageLabelBorder = BorderFactory.createTitledBorder(messageLabelBeveledBorder, "Message", TitledBorder.DEFAULT_POSITION, 
			TitledBorder.DEFAULT_POSITION, new Font("Aerial", Font.PLAIN, 14), Color.WHITE);	
	TitledBorder playersPanelBorder = BorderFactory.createTitledBorder(playersPanelBeveledBorder, "Players", TitledBorder.DEFAULT_POSITION, 
			TitledBorder.DEFAULT_POSITION,	new Font("Aerial", Font.PLAIN, 14), Color.WHITE);
	TitledBorder playWhoPanelBorder = BorderFactory.createTitledBorder(playWhoPanelBeveledBorder, "Opponents", TitledBorder.DEFAULT_POSITION, 
			TitledBorder.DEFAULT_POSITION,	new Font("Aerial", Font.PLAIN, 14), Color.WHITE);	
	TitledBorder difficultyPanelPanelBorder = BorderFactory.createTitledBorder(difficultyPanelBeveledBorder, "Difficulty", TitledBorder.DEFAULT_POSITION, 
			TitledBorder.DEFAULT_POSITION,	new Font("Aerial", Font.PLAIN, 14), Color.WHITE);	
			
	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
		//create frame
		try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
		new MatchGame().setVisible(true);
		
		BackgroundMusicPlayback backgroundMusicPlayback = new BackgroundMusicPlayback();
		backgroundMusicThread = new Thread(backgroundMusicPlayback);
		backgroundMusicThread.setDaemon(true);
		backgroundMusicThread.start();
		
		GameSoundPlayback gameSoundPlayback = new GameSoundPlayback();
		gameSoundThread = new Thread(gameSoundPlayback);
		gameSoundThread.setDaemon(true);
		gameSoundThread.start();		
	}
	
	public MatchGame() {
		setTitle("Match Game");
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		try {
    	    img = ImageIO.read(new File("image\\Emblem008.png"));
    	} catch (IOException e) {
    	}
		setIconImage(img);
		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints gbc;
		gamePanel.setPreferredSize(new Dimension(825, 680));
		gamePanel.setLayout(new GridBagLayout());
		gamePanel.setBackground(Color.black);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 5;
		getContentPane().add(gamePanel, gbc);
		
		for (int i = 0; i < 20; i++){
			photoLabel[i] = new JLabel();
			photoLabel[i].setPreferredSize(new Dimension(200, 130));
			photoLabel[i].setOpaque(true);
			photoLabel[i].setBackground(Color.DARK_GRAY);
			gbc = new GridBagConstraints();
			gbc.gridx = i % 4;
			gbc.gridy = i / 4;
			gbc.insets = new Insets(5, 5, 0, 0);
			if (gbc.gridx == 3) gbc.insets = new Insets(5, 5, 0, 5);
			if (gbc.gridy == 4)	gbc.insets = new Insets(5, 5, 5, 0);
			if (gbc.gridx == 3 && gbc.gridy == 4) gbc.insets = new Insets(5, 5, 5, 5);
			gamePanel.add(photoLabel[i], gbc);
			photoLabel[i].addMouseListener(new MouseAdapter(){
				public void mousePressed(MouseEvent e){
					photoLabelMousePressed(e);
				}
			});
		}
		
		resultPanel.setPreferredSize(new Dimension(200, 680));
		resultPanel.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		getContentPane().add(resultPanel, gbc);
		
		muteSoundCheckBox.setForeground(Color.WHITE);
		muteSoundCheckBox.setHorizontalTextPosition(SwingConstants.LEFT);
		muteSoundCheckBox.setFont(new Font("Verdana", Font.BOLD, 12));
		muteSoundCheckBox.setFocusable(false);
		muteSoundCheckBox.setOpaque(false);
		muteSoundCheckBox.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 0, 40, 5);
		gbc.anchor = GridBagConstraints.EAST;
		resultPanel.add(muteSoundCheckBox, gbc);
		muteSoundCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				muteSoundCheckBoxItemStateChanged(e);				
			}			
		});
		
		player1Label.setText("Player\s1");
		player1Label.setFont(new Font("Aerial", Font.BOLD, 16));
		player1Label.setForeground(Color.RED);
		player1Label.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		resultPanel.add(player1Label, gbc);
		
		scoreTextField[0] = new JTextField();
		scoreTextField[0].setPreferredSize(new Dimension(120, 25));
		scoreTextField[0].setText("0");
		scoreTextField[0].setEditable(false);
		scoreTextField[0].setOpaque(false);
		scoreTextField[0].setForeground(Color.WHITE);
		scoreTextField[0].setHorizontalAlignment(SwingConstants.CENTER);
		scoreTextField[0].setFont(new Font("Aerial", Font.PLAIN, 18));
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.insets = new Insets(5,0,10,0);
		resultPanel.add(scoreTextField[0], gbc);
		
		player2Label.setText("Player\s2");
		player2Label.setFont(new Font("Aerial", Font.BOLD, 16));
		player2Label.setForeground(Color.GREEN);
		player2Label.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		resultPanel.add(player2Label, gbc);
		
		scoreTextField[1] = new JTextField();
		scoreTextField[1].setPreferredSize(new Dimension(120, 25));
		scoreTextField[1].setText("0");
		scoreTextField[1].setEditable(false);
		scoreTextField[1].setOpaque(false);
		scoreTextField[1].setForeground(Color.WHITE);
		scoreTextField[1].setHorizontalAlignment(SwingConstants.CENTER);
		scoreTextField[1].setFont(new Font("Aerial", Font.PLAIN, 18));
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.insets = new Insets(5,0,10,0);
		resultPanel.add(scoreTextField[1], gbc);
		
		messageLabel.setPreferredSize(new Dimension(195, 100));
		messageLabel.setOpaque(false);
		messageLabel.setBorder(messageLabelBorder);
		messageLabel.setForeground(Color.WHITE);
		messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		messageLabel.setText("");
		messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.insets = new Insets(10, 0, 0, 5);
		resultPanel.add(messageLabel, gbc);
		
		playersPanel.setPreferredSize(new Dimension(195, 75));
		playersPanel.setOpaque(false);
		playersPanel.setBorder(playersPanelBorder);
		playersPanel.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.insets = new Insets(10, 0, 0, 0);
		resultPanel.add(playersPanel, gbc);
		
		twoPlayersRadioButton.setText("Two players");
		twoPlayersRadioButton.setOpaque(false);
		twoPlayersRadioButton.setForeground(Color.WHITE);
		twoPlayersRadioButton.setSelected(true);
		twoPlayersRadioButton.setFocusable(false);
		playersButtonGroup.add(twoPlayersRadioButton);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		playersPanel.add(twoPlayersRadioButton, gbc);
		
		twoPlayersRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				twoPlayersRadioButtonActionPerformed(ae);
			}
		});
		
		onePlayerRadioButton.setText("One player");
		onePlayerRadioButton.setOpaque(false);
		onePlayerRadioButton.setForeground(Color.WHITE);
		onePlayerRadioButton.setSelected(true);
		onePlayerRadioButton.setFocusable(false);
		playersButtonGroup.add(onePlayerRadioButton);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 0, 10, 0);
		playersPanel.add(onePlayerRadioButton, gbc);
		
		onePlayerRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				onePlayerRadioButtonActionPerformed(ae);
			}
		});
		
		playWhoPanel.setPreferredSize(new Dimension(195, 75));
		playWhoPanel.setOpaque(false);
		playWhoPanel.setBorder(playWhoPanelBorder);
		playWhoPanel.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.insets = new Insets(10, 0, 0, 0);
		resultPanel.add(playWhoPanel, gbc);
		
		playAloneRadioButton.setText("Play alone");
		playAloneRadioButton.setOpaque(false);
		playAloneRadioButton.setForeground(Color.WHITE);
		playAloneRadioButton.setSelected(true);
		playAloneRadioButton.setFocusable(false);
		playWhoButtonGroup.add(playAloneRadioButton);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		playWhoPanel.add(playAloneRadioButton, gbc);
		playAloneRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				playAloneRadioButtonActionPerformed(ae);
			}
		});
		
		playComputerRadioButton.setText("Computer opponent");
		playComputerRadioButton.setOpaque(false);
		playComputerRadioButton.setForeground(Color.WHITE);
		playComputerRadioButton.setSelected(true);
		playComputerRadioButton.setFocusable(false);
		playWhoButtonGroup.add(playComputerRadioButton);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(0, 0, 5, 0);
		playWhoPanel.add(playComputerRadioButton, gbc);
		playComputerRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				playComputerButtonActionPerformed(ae);
			}
		});
		
		difficultyPanel.setPreferredSize(new Dimension(195, 80));
		difficultyPanel.setOpaque(false);
		difficultyPanel.setBorder(difficultyPanelPanelBorder);
		difficultyPanel.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 8;
		resultPanel.add(difficultyPanel, gbc);
		
		easiestRadioButton.setText("Very easy");
		easiestRadioButton.setOpaque(false);
		easiestRadioButton.setForeground(Color.WHITE);
		easiestRadioButton.setSelected(true);
		easiestRadioButton.setFocusable(false);
		difficultyButtonGroup.add(easiestRadioButton);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		difficultyPanel.add(easiestRadioButton, gbc);
		easiestRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				easiestRadioButtonActionPerformed(e);
				
			}
		});
		
		easyRadioButton.setText("Easiest");
		easyRadioButton.setOpaque(false);
		easyRadioButton.setForeground(Color.WHITE);
		easyRadioButton.setFocusable(false);
		difficultyButtonGroup.add(easyRadioButton);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 10, 0, 0);
		gbc.anchor = GridBagConstraints.WEST;
		difficultyPanel.add(easyRadioButton, gbc);
		easyRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				easyRadioButtonActionPerformed(e);
				
			}
		});
		
		hardRadioButton.setText("Hard");
		hardRadioButton.setOpaque(false);
		hardRadioButton.setForeground(Color.WHITE);
		hardRadioButton.setFocusable(false);
		difficultyButtonGroup.add(hardRadioButton);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.insets = new Insets(0, 0, 5, 0);
		gbc.anchor = GridBagConstraints.WEST;
		difficultyPanel.add(hardRadioButton, gbc);
		hardRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				hardRadioButtonActionPerformed(e);
				
			}
		});
		
		hardestRadioButton.setText("Hardest");
		hardestRadioButton.setOpaque(false);
		hardestRadioButton.setForeground(Color.WHITE);
		hardestRadioButton.setFocusable(false);
		difficultyButtonGroup.add(hardestRadioButton);
		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.insets = new Insets(0, 10, 5, 0);
		gbc.anchor = GridBagConstraints.WEST;
		difficultyPanel.add(hardestRadioButton, gbc);
		hardestRadioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				hardestRadioButtonActionPerformed(e);
				
			}
		});
		
		buttonsPanel.setPreferredSize(new Dimension(195, 70));
		buttonsPanel.setOpaque(false);
		buttonsPanel.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 9;
		gbc.insets = new Insets(20, 0, 20, 0);
		resultPanel.add(buttonsPanel, gbc);
		
		startStopButton.setText("Start");
		startStopButton.setFont(new Font("Verdana", Font.BOLD, 16));
		startStopButton.setBackground(new Color(25, 25, 25));
		startStopButton.setForeground(Color.GREEN);
		startStopButton.setBorder(startStopButtonBeveledBorder);
		startStopButton.setFocusable(false);
		startStopButton.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.ipadx = 10;
		gbc.ipady = 2;
		buttonsPanel.add(startStopButton, gbc);
		startStopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startStopButtonActionPerformed(e);				
			}
			
		});
		
		exitButton.setText("Exit");
		exitButton.setFont(new Font("Verdana", Font.BOLD, 14));
		exitButton.setBackground(new Color(25, 25, 25));
		exitButton.setForeground(Color.RED);
		exitButton.setBorder(startStopButtonBeveledBorder);
		exitButton.setFocusable(false);
		exitButton.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.ipadx = 10;
		gbc.ipady = 2;
		gbc.insets = new Insets(15,0,0,0);
		buttonsPanel.add(exitButton, gbc);
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exitButtonActionPerformed(e);				
			}
		});
		
		pack();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((int)(0.5*(screenSize.getWidth()-getWidth())), (int)(0.5*(screenSize.getHeight()-getHeight())), 
				getWidth(), getHeight());	
		
		photo[0] = new ImageIcon("image\\" + defaultGameSubject + "\\Photo#1_200x130.jpg");
		photo[1] = new ImageIcon("image\\" + defaultGameSubject + "\\Photo#2_200x130.jpg");
		photo[2] = new ImageIcon("image\\" + defaultGameSubject + "\\Photo#3_200x130.jpg");
		photo[3] = new ImageIcon("image\\" + defaultGameSubject + "\\Photo#4_200x130.jpg");
		photo[4] = new ImageIcon("image\\" + defaultGameSubject + "\\Photo#5_200x130.jpg");
		photo[5] = new ImageIcon("image\\" + defaultGameSubject + "\\Photo#6_200x130.jpg");
		photo[6] = new ImageIcon("image\\" + defaultGameSubject + "\\Photo#7_200x130.jpg");
		photo[7] = new ImageIcon("image\\" + defaultGameSubject + "\\Photo#8_200x130.jpg");
		photo[8] = new ImageIcon("image\\" + defaultGameSubject + "\\Photo#9_200x130.jpg");
		photo[9] = new ImageIcon("image\\" + defaultGameSubject + "\\Photo#10_200x130.jpg");
		defaultImage = new ImageIcon("image\\DefaultImage.jpg");
		cover = new ImageIcon("image\\Covers\\" + defaultImageCover);
		for (int i = 0; i < 20; i++) {
			photoLabel[i].setIcon(cover);
		}
		
		setPlayWhoButtons(false);
		setDifficultyButtons(false);
		
		displayTimer = new Timer(500, new ActionListener() {			
			public void actionPerformed(ActionEvent e){
				displayTimerActionPerformed(e);
			}
		});
		
		delayTimer = new Timer(500, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delayTimerActionPerformed(e);
			}
		});		
	}
	
	public void photoLabelMousePressed(MouseEvent e) {
		// determine which label was clicked
		// get upper left corner of clicked label
		Point p = e.getComponent().getLocation();
		// determine index based on p
		for (labelSelected = 0; labelSelected < 20; labelSelected++){
			if (p.x == photoLabel[labelSelected].getX() && p.y == photoLabel[labelSelected].getY())
			break;
		}
		if (!canClick || photoFound[labelSelected])
		return;
		// show image behind selected label box
		canClick = false;
		showSelectedLabel();
	}
	
	public void muteSoundCheckBoxItemStateChanged(ItemEvent e) {
		muteSoundCheckBoxClicksCounter +=1;
		muteSoundCheckBox.setText("Unmute");
		BackgroundMusicPlayback.backgroundMusicClip.stop();			
		if(muteSoundCheckBoxClicksCounter%2==0) {
			muteSoundCheckBox.setText("Mute");
			muteSoundCheckBoxClicksCounter=0;
			BackgroundMusicPlayback.backgroundMusicClip.loop(-1);			
		}
	}
	
	public void twoPlayersRadioButtonActionPerformed(ActionEvent e) { 
		setPlayWhoButtons(false);
		setDifficultyButtons(false);
		player1Label.setText("Player 1");
		player2Label.setText("Player 2");
	}
	
	public void onePlayerRadioButtonActionPerformed(ActionEvent e) {
		setPlayWhoButtons(true);
		player1Label.setText("You");
		if (playAloneRadioButton.isSelected()) {
			player2Label.setText("Guesses");
			setDifficultyButtons(false);
		} else {
			player2Label.setText("Computer");
			setDifficultyButtons(true);
		}
	}
	
	public void playAloneRadioButtonActionPerformed(ActionEvent e) {
		player2Label.setText("Guesses");
		setDifficultyButtons(false);
	}
	
	public void playComputerButtonActionPerformed(ActionEvent e) {
		setDifficultyButtons(true);
		player2Label.setText("Computer");
	}
	
	public void easiestRadioButtonActionPerformed(ActionEvent e) {
		difficulty = 1;
	}
	
	public void easyRadioButtonActionPerformed(ActionEvent e) {
		difficulty = 2;
	}
	
	public void hardRadioButtonActionPerformed(ActionEvent e) {
		difficulty = 3;
	}
	
	public void hardestRadioButtonActionPerformed(ActionEvent e) {
		difficulty = 4;
	}
	
	private int randomChoice() {
		int count, n, rc;
		if (choiceNumber == 1)	n = myRandom.nextInt(photosRemaining) + 1;
		else n = myRandom.nextInt(photosRemaining - 1) + 1;
		count = 0;
		for (rc = 0; rc < 20; rc++)	{
			if (!photoFound[rc])
			count++;
			if (count == n)
			break;
		}
		return(rc);		
	}
	private int smartChoice() {
		int sc;
		if (choiceNumber == 1) {
			matchFound = checkForMatch();
			if (matchFound[0] != 0 && matchFound[1] != 0) sc = matchFound[0];
				else sc = randomChoice();
		}
		else {
			if (matchFound[0] != 0 && matchFound[1] != 0) sc = matchFound[1];
				else {
					matchFound = checkForMatch();
					if (matchFound[0] != 0 && matchFound[1] != 0) {
					if (matchFound[0] != choice[0]) sc = matchFound[0];
						else sc = matchFound[1];
					} else 	sc = randomChoice();
			}
		}
		return (sc);
	}
	
	private void computerTurn()	{
		int threshold = 0;
		if (choiceNumber == 1) {
			switch (difficulty) {
				case 1:	threshold = 25;
				break;
				case 2: threshold = 50;
				break;
				case 3: threshold = 75;
				break;
				case 4: threshold = 90;
				break;
			}
			
			if (myRandom.nextInt(100) < threshold)	smartComputer = true;
				else smartComputer = false;
			}
		
		if (smartComputer) labelSelected = smartChoice();
		else labelSelected = randomChoice();
		delayTimer.start();
	}
	
	private int[] checkForMatch() {
		int[] matches = new int[2];
		matches[0] = 0;
		matches[1] = 0;
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				if (memory[i] != 0 && memory[i] == memory[j] && i != j)	{
					matches[0] = i;
					matches[1] = j;
				}
			}
		}
		return(matches);
	}
	
	public void startStopButtonActionPerformed(ActionEvent e) {
		if (startStopButton.getText().equals("Start Game")) {
			startStopButton.setText("Stop Game");
			score[0] = 0;
			score[1] = 0;
			scoreTextField[0].setText("0");
			scoreTextField[1].setText("0");
			photosRemaining = 20;
			photoIndex = nRandomIntegers(20);
			
			for (int i = 0; i < 20; i++){
				if (photoIndex[i] > 9)
				photoIndex[i] -= 10;
				photoFound[i] = false;
				photoLabel[i].setIcon(cover);
				memory[i] = 0;
			}
			
			playerNumber = 1;
			choiceNumber = 1;
			
			if (twoPlayersRadioButton.isSelected())	messageLabel.setText("Player 1, Pick a Box");
			else messageLabel.setText("Pick a Box");
			
			setNumberPlayersButtons(false);
			setPlayWhoButtons(false);
			setDifficultyButtons(false);
			exitButton.setEnabled(false);
			canClick = true;
			gameOver = false;
		} 
		else {
			// stop game
			startStopButton.setText("Start Game");
			setNumberPlayersButtons(true);
			
			if (onePlayerRadioButton.isSelected()) {
				setPlayWhoButtons(true);
				if (playComputerRadioButton.isSelected()) setDifficultyButtons(true);
			}
			
			exitButton.setEnabled(true);
			canClick = false;
			if (!gameOver)	messageLabel.setText("Game Stopped");
		}
	}	
	
	public void exitButtonActionPerformed(ActionEvent e) {
		System.exit(0);;
	}
	
	private void setNumberPlayersButtons(boolean a){
		onePlayerRadioButton.setEnabled(a);
		twoPlayersRadioButton.setEnabled(a);
	}
	
	private void setPlayWhoButtons(boolean a){
		playAloneRadioButton.setEnabled(a);
		playComputerRadioButton.setEnabled(a);
	}
	
	private void setDifficultyButtons(boolean a){
		easiestRadioButton.setEnabled(a);
		easyRadioButton.setEnabled(a);
		hardRadioButton.setEnabled(a);
		hardestRadioButton.setEnabled(a);
	}
	
	private int[] nRandomIntegers(int n) {
		/*
		Returns n randomly sorted integers 0 -> n - 1
		*/
		int[] nIntegers = new int[n];
		int temp, s;
		Random sortRandom = new Random();
		// initialize array from 0 to n - 1
		for (int i = 0; i < n; i++)	{
			nIntegers[i] = i;
		}
		// i is number of items remaining in list
		for (int i = n; i >= 1; i--){
			s = sortRandom.nextInt(i);
			temp = nIntegers[s];
			nIntegers[s] = nIntegers[i - 1];
			nIntegers[i - 1] = temp;
		}
		return(nIntegers);
	}
	
	private void showSelectedLabel() {
		// one player/solitaire game
		if (onePlayerRadioButton.isSelected() && playAloneRadioButton.isSelected()){
			score[1]++;
			scoreTextField[1].setText(String.valueOf(score[1]/2));
		}
		photoLabel[labelSelected].setIcon(photo[photoIndex[labelSelected]]);
		photoFound[labelSelected] = true;
		displayTimer.start();
		messageLabel.setForeground(Color.YELLOW);
		messageLabel.setText("W\sa\si\st.\s.\s.");
	}
	
	public void displayTimerActionPerformed(ActionEvent e) {
		displayTimer.stop();
		messageLabel.setForeground(Color.WHITE);
		if (choiceNumber == 1)	{
			choice[0] = labelSelected;
			choiceNumber = 2;
			memory[labelSelected] = photoIndex[labelSelected];
			if (twoPlayersRadioButton.isSelected())	{
				messageLabel.setText("Player " + String.valueOf(playerNumber) + ", Pick	Another");
				canClick = true;
			}	else{
					// one player logic
				if (playerNumber == 1)	{
					messageLabel.setText("Pick Another Box");
					canClick = true;
				}
				else
				{
				// play computer logic
					messageLabel.setText("Picking Another");
					computerTurn();
					return;
				}
			}
		} 	else{
			choice[1] = labelSelected;
			choiceNumber = 1;
			memory[labelSelected] = photoIndex[labelSelected];
			if (photoIndex[choice[0]] == photoIndex[choice[1]]) {
				// a match
				GameSoundPlayback.matchSoundClip.start();
				GameSoundPlayback.matchSoundClip.setFramePosition(0);
				photoLabel[choice[0]].setIcon(defaultImage);
				photoLabel[choice[1]].setIcon(defaultImage);
				// clear memory so boxes are not checked again for match
				memory[choice[0]] = 0;
				memory[choice[1]] = 0;
				score[playerNumber - 1]++;
				scoreTextField[playerNumber -1].setText(String.valueOf(score[playerNumber -	1]));
				photosRemaining -= 2;
				
				if (photosRemaining == 0) {
					gameOver = true;
					GameSoundPlayback.gameOverSoundClip.start();
					GameSoundPlayback.gameOverSoundClip.setFramePosition(0);					
					if (twoPlayersRadioButton.isSelected())	{
					if (score[0] > score[1])messageLabel.setText("Player 1 Wins!");
						else if (score[1] > score[0])messageLabel.setText("Player 2 Wins!");
						else messageLabel.setText("It's a Tie!");
					}	else {
					// one player logic
						if (playAloneRadioButton.isSelected()) messageLabel.setText("All Matches Found!");
							else {
							// play computer logic
								if (score[0] > score[1]) messageLabel.setText("You Win!");
								else if (score[1] > score[0]) messageLabel.setText("Computer Wins!");
								else messageLabel.setText("It's a Tie!");
							}
					}
					startStopButton.doClick();						
					return;					 
				}
				// another turn
				if (twoPlayersRadioButton.isSelected()) {
					messageLabel.setText("Player " + String.valueOf(playerNumber) + ", Pick	Again");
					canClick = true;
				}	else {
					// one player logic
					if (playerNumber == 1) {
						messageLabel.setText("Pick a Box");
						canClick = true;
					} else	{
					// play computer logic
						messageLabel.setText("Picking Again");
						computerTurn();
						return;
					}
				}
			}	else {
				//no match
				GameSoundPlayback.noMatchSoundClip.start();
				GameSoundPlayback.noMatchSoundClip.setFramePosition(0);
				photoFound[choice[0]] = false;
				photoFound[choice[1]] = false;
				photoLabel[choice[0]].setIcon(cover);
				photoLabel[choice[1]].setIcon(cover);
				// swap players
				if (twoPlayersRadioButton.isSelected())	{
					if (playerNumber == 1)	playerNumber = 2;
					else playerNumber = 1;
					messageLabel.setText("Player " + String.valueOf(playerNumber) + ", Pick a Box");
					canClick = true;
				}	else {
					// one player logic
					if (playComputerRadioButton.isSelected()) {
						if (playerNumber == 1) playerNumber = 2;
						else playerNumber = 1;
					}
					
					if (playerNumber == 1) {
						messageLabel.setText("Pick a Box");
						canClick = true;
					} else {
					// play computer logic
						messageLabel.setText("Computer Picking");
						choiceNumber = 1;
						computerTurn();
						return;
					}
				}
			}			
		};
	}
	
	public void delayTimerActionPerformed(ActionEvent e) {
		delayTimer.stop();
		showSelectedLabel();	
	}
	
}

package screen;
// Name: Julian Fisla
// Date: 2022-12-19
// Description: The program will take in a text file and will print out the top twenty most frequent words that appear in the text

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import helper.Word;

public class WordFrequencyCounter extends JPanel implements ActionListener, ItemListener {

	// jframe and all its components
	private static JFrame frame = new JFrame();;
	private static JButton add;
	private static JButton exit;
	private static JComboBox comboBox;
	private static JScrollPane scrollPane;
	JTextArea textFileSpace;
	JTextArea textFileOutput;
	
	// array lists that help keep track of all the new files added
	private static ArrayList<String> addedCases = new ArrayList<String> ();
	private static ArrayList<File> addedCasesFiles = new ArrayList<File> ();
	
	// default items are alice.txt and moby.txt
	public static String[] dropDownItems = {"ALICE.txt", "MOBY.txt"};
	
	// constructor
	public WordFrequencyCounter() {
		
		// draw the screen with alice selected as default
		draw("ALICE.txt");
		
	}
	
	// Parameters: The method will take in the name of the file currently selected by the user
	// Description: The method will the calculations for the word frequencies and then draw everything onto the screen that is important
	// Output: Output is void
	private void draw(String fileName) {
		
		// setting up the jframe
		frame.setTitle("jframes is terrible");
		frame.setSize(700, 750);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLayout(null);
		
		// setting up the title icon
		ImageIcon titleIcon = new ImageIcon("titleIcon.png");
		frame.setIconImage(titleIcon.getImage());
		frame.getContentPane().setBackground(new Color(200, 200, 200));
		
		// create the add file button
		add = new JButton("Add File");
		add.addActionListener(this);
		add.setBounds(360, 20, 150, 40);
		
		// create the combo box for files
		comboBox = new JComboBox(dropDownItems);
		comboBox.setSelectedItem(fileName);
		comboBox.addItemListener(this);
		comboBox.setBounds(250, 20, 100, 25);
		
		// create the exit button
		exit = new JButton("Exit");
		exit.addActionListener(this);
		exit.setBounds(360, 70, 150, 40);
		
		// create the text area where the text file will be displayed
		textFileSpace = new JTextArea();
		textFileSpace.setEditable(false);
		textFileSpace.setFont(new Font("Courier New", Font.PLAIN, 12));
		
		// use a scroll pane with the previous text area to access the entire text file on screen
		scrollPane = new JScrollPane(textFileSpace);
		scrollPane.setBounds(50, 200, 425, 500);
		
		// printing out the currently selected file to the text area on screen
		try {
			BufferedReader br = null;
			String fileInput;
			
			// if default files
			if (fileName.equalsIgnoreCase("ALICE.txt") || fileName.equalsIgnoreCase("MOBY.txt")) {
				br = new BufferedReader(new FileReader("assets/examplefiles/" + fileName));
				fileInput = "";
			}
			// if new file
			else {
				for (int i = 0; i < addedCases.size(); i++) {
					if (addedCases.get(i).equalsIgnoreCase(fileName)) {
						File file = addedCasesFiles.get(i);
						FileReader fileReader = new FileReader(file);
						br = new BufferedReader(fileReader);
					}
				}
			}
			// iterate through the text file and print it to the text area
			while ((fileInput = br.readLine()) != null) {
				
				textFileSpace.append(fileInput + "\n");
				
			}
			br.close();
			
		}
		catch (IOException ioException) {
			System.out.println(ioException);
		}
		
		// create the text area for the output of the program
		textFileOutput = new JTextArea();
		textFileOutput.setEditable(false);
		textFileOutput.setBounds(485, 200, 175, 500);
		textFileOutput.setFont(new Font("Courier New", Font.PLAIN, 12));
		
		// output string is the result of the program; the top twenty frequencies in the file
		String output = readInFileAndStoreFrequencies(fileName);
		
		// print out the text
		textFileOutput.setText(output);
		
		// add all the components to the jframe
		frame.add(add);
		frame.add(comboBox);
		frame.add(exit);
		frame.add(scrollPane);
		frame.add(textFileOutput);
		
		frame.setVisible(true);
		
	}

	// main method
	public static void main(String[] args) {
		
		WordFrequencyCounter wordFrequencyCounter = new WordFrequencyCounter();
		
	}

	// Parameters: The method will take in the name of the file currently selected by the user
	// Description: The method will the calculation the top twenty word frequencies in the text file
	// Output: Output is the result of the program; the top twenty words and the program run time 
	private static String readInFileAndStoreFrequencies(String fileName) {

		// start program time
		long time = System.nanoTime();
		
		// hash map that holds a key (the unique string) and a word object that will increase in frequency
		HashMap <String, Word> storeWordFrequencies = new HashMap<>();
		
		// importing files
		try {
			
			BufferedReader br = null;
			String fileInput;
			
			// if default files
			if (fileName.equalsIgnoreCase("ALICE.txt") || fileName.equalsIgnoreCase("MOBY.txt")) {
				br = new BufferedReader(new FileReader("assets/examplefiles/" + fileName));
				fileInput = "";
			}
			// if new file
			else {
				for (int i = 0; i < addedCases.size(); i++) {
					if (addedCases.get(i).equalsIgnoreCase(fileName)) {
						File file = addedCasesFiles.get(i);
						FileReader fileReader = new FileReader(file);
						br = new BufferedReader(fileReader);
					}
				}
			}
			
			// iterate through the entire text file
			while ((fileInput = br.readLine()) != null) {
				
				// replace all commas with spaces
				fileInput = fileInput.replaceAll("[,]", " ");
				
				// split the text line into an array by spaces
				String[] textLine = fileInput.split("\\s+");
				
				// go through the entire array for that line
				for (int i = 0; i < textLine.length; i++) {
					
					String word = textLine[i].toLowerCase();
					
					if (!(word.equals(" "))) {
						
						// remove 's from the current string
						if (word.length() >= 2) {
							while (word.substring(word.length()-2, word.length()).equals("'s")) {
								word = word.substring(0, word.length()-2);
							}
						}
						
						// check for contractions and return the changes
						String[] changes = parseCases(word);
						
						// if there are two words because of the contraction, set the current word and append the second word to the end of the line array
						if (changes[1] == null) {
							word = changes[0];
						}
						else {
							word = changes[0];
							textLine = addArray(textLine, changes[1]);
						}
						
						// replace all characters that are not a digit or letter
						word = word.replaceAll("[^\\p{IsDigit}\\p{IsAlphabetic}]", "");
						
						// check if a word object exists for the word key
						Word currentWord = storeWordFrequencies.get(word);
						
						// if it does exist
						if (currentWord != null) {
							
							// increase the frequency for that word
							currentWord.setFrequency(currentWord.getFrequency() + 1);
						}
						else {
							
							// create a new word with that unique word and add it to the hash map
							currentWord = new Word(word);
							storeWordFrequencies.put(word, currentWord);
						}
						
					}
					
				}
				
			}
			
			br.close();
			
		}
		catch (IOException ioException) {
			
			System.out.println(ioException);
			
		}
		
		// convert the hash map to an array
		Word[] sortedWords = storeWordFrequencies.values().toArray(new Word[0]);
		
		// sort the new array by frequency
		Arrays.sort(sortedWords);
		
		String output = "";
		
		int counter = 0;
		
		// take the top twenty highest frequencies
		if (sortedWords.length >= 20) {
			for (int i = 0; i < 20; i++) {
				
				// if the word is a blank space skip over it
				if (!(sortedWords[counter].getWord().equals(""))) {
					
					// output the word, its ranking, and its frequency
					output += String.format("%2d : %-14s%5d%n", (i + 1), sortedWords[counter].getWord(), sortedWords[counter].getFrequency());
					counter++;
				}
				else {
					counter++;
					i--;
				}
				
			}
		}
		else {
			// if there aren't enough unique words for twenty
			for (int i = 0; i < sortedWords.length; i++) {
				
				// same as before
				if (!(sortedWords[counter].getWord().equals(""))) {
					output += String.format("%2d : %-14s%5d%n", (i + 1), sortedWords[counter].getWord(), sortedWords[counter].getFrequency());
					counter++;
				}
				else {
					counter++;
					i--;
				}
				
			}
		}
		
		// add the program time to the output
		output += ("\nComplete: " + ((System.nanoTime() - time)/1000000) + "ms");
		
		return output;
	}

	// Parameters: The method will take in the current string to check for cases
	// Description: The method will loop through all the possible cases and check if they are equal to the input string
	// Output: Output is a string array that holds two elements; if the word is a contraction there will be two elements, if it isn't there will only be one
	private static String[] parseCases(String word) {

		// the changes for any word
		String[] newArray = new String[2];
		
		// all the cases to check
		String[] cases = {"don't", "won't", "isn't", "hadn't", "shouldn't", "couldn't", "i'm", "you're", "he's", "aren't", "can't", "didn't", 
				"doesn't", "hasn't", "haven't", "he'd", "he'll", "he's", "i'd", "i'll", "i've", "let's", "mightn't", "musn't", "shan't", "she'd", 
				"she'll", "she's", "that's", "there's", "they'd", "they'll", "they're", "they've", "we'd", "we're", "we've", "weren't", 
				"what'll", "what're", "what's", "what've", "where's", "who'd", "who'll", "who're", "who's", "who've", "wouldn't", "you'd", "you'll", "you've", "it's", 
				"it'd", "it'll"};
		
		// all the words to replace the corresponding cases
		String[] alternateWords = {"do not", "will not", "is not", "had not", "should not", "could not", "i am", "you are", "he is", "are not", "can not", "did not", 
				"does not", "has not", "have not", "he had", "he will", "he is", "i would", "i will", "i have", "let us", "might not", "must not", "shall not", "she had", 
				"she will", "she is", "that is", "there is", "they had", "they will", "they are", "they have", "we had", "we are", "we have", "were not", 
				"what will", "what are", "what is", "what have", "where is", "who had", "who will", "who are", "who is", "who have", "would not", "you had", "you will", "you have", "it is", 
				"it had", "it will"};
		
		// go through every case and check if its equal to the current word
		for (int i = 0; i < cases.length; i++) {
			if (word.equals(cases[i])) {
				
				// split the one contraction into two words and return the array
				newArray[0] = alternateWords[i].substring(0, alternateWords[i].indexOf(" ")+1);
				newArray[1] = alternateWords[i].substring(alternateWords[i].indexOf(" "));
				return newArray;
			}
		}
		
		// return just the word and null in the array if there are no contractions
		newArray[0] = word;
		
		return newArray;
	}

	// Parameters: The method will take in a string array and a string
	// Description: The method will append the string to the string array by creating a new array, copying the contents, and putting the string at the end
	// Output: Output is the new string array
	private static String[] addArray(String[] strArray, String string) {
		
		// make a new array with one more length than the previous array
		String[] newArray = new String[strArray.length+1];
		
		// copy the contents of the old array to the new array
		for (int i = 0; i < strArray.length; i++) {
			newArray[i] = strArray[i];
		}
		
		// set the last value to the string
		newArray[newArray.length-1] = string;
		
		return newArray;
	}

	// Parameters: The method takes in a ActionEvent object
	// Description: The method will be called when an action is performed on any jcomponent that adds it as a listener and then adds functionality to that press
	// Output: Output is void
	public void actionPerformed(ActionEvent e) {

		// if the add button was clicked
		if (e.getSource() == add) {
			
			// create file chooser component
			JFileChooser fileChooser = new JFileChooser();
			
			// make sure the user selects a file
			int value = fileChooser.showOpenDialog(null);
			if (value == JFileChooser.APPROVE_OPTION) {
				
				// add the new file to the combo box string array and a file array to hold the data
				dropDownItems = addArray(dropDownItems, fileChooser.getSelectedFile().getName());
				addedCases.add(fileChooser.getSelectedFile().getName());
				addedCasesFiles.add(fileChooser.getSelectedFile());
				
				// remove everything from the jframe
				frame.getContentPane().removeAll();
				
				// redraw it with the newly selected file
				draw(fileChooser.getSelectedFile().getName());
			}
		}
		
		// if the exit button was pressed
		else if (e.getSource() == exit) {
			System.exit(0);
		}
		
	}

	// Parameters: The method takes in a ItemEvent object
	// Description: The method will be called when an item state is changed on any jcomponent that adds it as a listener and then adds functionality to that press
	// Output: Output is void
	public void itemStateChanged(ItemEvent e) {
		
		// if the selected combo box item has changed
		if (e.getStateChange() == ItemEvent.SELECTED) {
			
			// remove everything from the jframe
			frame.getContentPane().removeAll();
			
			// redraw it with the new item
			draw(e.getItem().toString());
		}
		
	}

}

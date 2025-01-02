//Importing the Neccessary Libraries
import javax.swing.*; 					// Swing library.
import java.awt.*;						// AWT library.
import java.io.*;   					//Libraries for Input/Output operations.
import java.util.*; 					// Libraries for collections and other utilities.
import java.util.List;  				//The List interface.
import java.util.stream.Collectors; 	//The Collectors class for stream operations.

public class MedCalculatorClass {
	
	// Create for shortest path in matrix.
	public static int[][] pathFollow;
	//Creates for calculate prosessing time variables.
	public static long StartPartOne; 
	public static long EndPartOne;
	public static long StartPartTwo;
	public static long EndPartTwo;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { // Runs the UI-related tasks in the Event Dispatch Thread.
            JFrame frame = new JFrame("MED Application"); 			// Creates the main window.
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Ensures the application exits when the window is closed.
            frame.setSize(600, 400); // Sets the window size.

            JPanel panel = new JPanel();  // Creates a panel.
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Sets the panel's layout to vertical.

            JLabel label = new JLabel("Select an option:"); // Creates a label.
            panel.add(label);  // Adds the label to the panel.

            JButton part1Button = new JButton("Find nearest 5 words (Part 1)"); // Creates a button for Part 1.
            part1Button.setAlignmentX(Component.CENTER_ALIGNMENT); // Centers the button.
            JButton part2Button = new JButton("Show MED steps (Part 2)"); // Creates a button for Part 2.
            part2Button.setAlignmentX(Component.CENTER_ALIGNMENT); // Centers the button.

            panel.add(Box.createRigidArea(new Dimension(0, 10))); // Adds space between buttons.
            panel.add(part1Button); // Adds Part 1 button to the panel.
            panel.add(Box.createRigidArea(new Dimension(0, 10))); // Adds space between buttons.
            panel.add(part2Button); // Adds Part 2 button to the panel.

            frame.add(panel); // Adds the panel to the frame.
            frame.setVisible(true); // Makes the window visible.

            part1Button.addActionListener(e -> showPart1UI()); // Adds an action listener for Part 1 button.
            part2Button.addActionListener(e -> showPart2UI()); // Adds an action listener for Part 2 button.
        });
    }
    
 // For reading the vocabulary
    public static List<String> readVocabulary(String fileName) throws IOException {
    	List<String> vocabulary = new ArrayList<>(); // Creates an empty list.
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))){ // Starts the file reading process. 
            String line; // A variable to hold each line.
            while ((line = br.readLine()) != null) { // Loops through each line of the file.
                vocabulary.add(line.trim()); // Adds the line (trimmed) to the list.
            }
        }
        return vocabulary; // Returns the vocabulary list.
    }

    public static void showPart1UI() { // Displays the UI for Part 1.
        JFrame frame = new JFrame("Part 1: Find Nearest Words"); // Creates the window for Part 1.
        frame.setSize(400, 300); // Sets the window size.

        JPanel panel = new JPanel(); // Creates a panel.
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Sets the panel's layout to vertical.

        JLabel inputLabel = new JLabel("Enter a word:"); // Label asking the user to enter a word.
        JTextField inputField = new JTextField(20); // Creates a text field for user input.
        JButton findButton = new JButton("Find Nearest Words"); // Creates a button to find nearest words.
        JTextArea resultArea = new JTextArea(10, 30); // Creates a text area to display results.
        resultArea.setEditable(false); // Makes the text area non-editable.

        panel.add(inputLabel); // Adds the label to the panel.
        panel.add(inputField); // Adds the text field to the panel.
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Adds space between text field and button.
        panel.add(findButton); // Adds the button to the panel.
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Adds space between button and text area.
        panel.add(new JScrollPane(resultArea)); // Adds a scrollable text area for results.

        frame.add(panel); // Adds the panel to the frame.
        frame.setVisible(true); // Makes the window visible.

        findButton.addActionListener(e -> {  // Adds an action listener to the button.
            try {
                String inputWord = inputField.getText().trim(); // Gets the word entered by the user.
                List<String> vocabulary = readVocabulary("C:\\Users\\Lenovooo\\Desktop\\vocabulary_tr.txt"); // Reads the vocabulary file.
                StartPartOne = System.currentTimeMillis();
                List<Map.Entry<String, Integer>> nearestWords = findNearestWords(inputWord, vocabulary, 5); // Finds the nearest 5 words.
                EndPartOne = System.currentTimeMillis();

                StringBuilder resultBuilder = new StringBuilder(); // Creates a StringBuilder for the results.
                resultBuilder.append("Nearest words and their MED values:\n"); // Adds a title.

                for (Map.Entry<String, Integer> entry : nearestWords) { // Loops through the nearest words.
                    resultBuilder.append(entry.getKey()) // Adds the word.
                            .append(" (MED: ") // Adds the MED value information.
                            .append(entry.getValue()) // Adds the MED value.
                            .append(")\n"); // Adds a new line.
                }
                
                resultBuilder.append("\n Part One Total Processing Time: "+ (EndPartOne-StartPartOne)+" ms."); // Adds the total processing time for part one.

                resultArea.setText(resultBuilder.toString()); // Adds the results in the text area.
            } catch (IOException ex) { // Catches any IO exceptions.
                resultArea.setText("Error loading vocabulary file."); // Displays an error message.
            }
        });
    }

    public static void showPart2UI() { // Displays the UI for Part 2.
        JFrame frame = new JFrame("Part 2: Show MED Steps"); // Creates the window for Part 2.
        frame.setSize(400, 400); // Sets the window size.

        JPanel panel = new JPanel(); // Creates a panel.
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Sets the panel's layout to vertical.

        JLabel word1Label = new JLabel("Enter the first word:"); // Label to ask for the first word.
        JTextField word1Field = new JTextField(20); // Text field for the first word.
        JLabel word2Label = new JLabel("Enter the second word:"); // Label to ask for the second word.
        JTextField word2Field = new JTextField(20); // Text field for the second word.
        JButton calculateButton = new JButton("Calculate MED"); // Button to calculate MED.
        JTextArea resultArea = new JTextArea(15, 30); // Text area to display results.
        resultArea.setEditable(false); // Makes the text area non-editable.

        panel.add(word1Label); // Adds the first word label to the panel.
        panel.add(word1Field);  // Adds the first word text field to the panel.
        panel.add(Box.createRigidArea(new Dimension(0, 10)));  // Adds space between words.
        panel.add(word2Label);  // Adds the second word label to the panel.
        panel.add(word2Field);  // Adds the second word text field to the panel.
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Adds space between text fields.
        panel.add(calculateButton); // Adds the button to the panel.
        panel.add(Box.createRigidArea(new Dimension(0, 10)));  // Adds space between the button and results.
        panel.add(new JScrollPane(resultArea)); // Adds a scrollable text area for results.

        frame.add(panel); // Adds the panel to the frame.
        frame.setVisible(true); // Makes the window visible.

        calculateButton.addActionListener(e -> { // Adds an action listener to the button.
            String word1 = word1Field.getText().trim(); // Gets the first word.
            String word2 = word2Field.getText().trim(); // Gets the second word.
            pathFollow = new int[word1.length()+1][word2.length()+1];
            
            StartPartTwo = System.currentTimeMillis();
            int[][] medTable = computeMEDTable(word1, word2); // Computes the MED table.
            EndPartTwo = System.currentTimeMillis();
            StringBuilder resultBuilder = new StringBuilder(); // Creates a StringBuilder for results.
            resultBuilder.append("MED Value: ").append(medTable[word1.length()][word2.length()]).append("\n"); // Adds the MED value.
            resultBuilder.append("Operations:\n"); //Adds the Operations information.

            List<String> operations = getMEDOperations(word1, word2, medTable); // Gets the MED operations.
            for (String operation : operations) {  // Loops through the operations.
                resultBuilder.append(operation).append("\n"); // Adds each operation to the results.
            }

            resultBuilder.append("\nMED Matrix:\n"); // Adds the MED matrix title.
            resultBuilder.append("    "); // Adds initial spaces.
            for (int j = 0; j <= word2.length(); j++) { // Adds the columns of the MED matrix.
                if (j == 0) {
                    resultBuilder.append("    ");  // The first column is empty.
                } else {
                    resultBuilder.append("  " +String.format("%4c", word2.charAt(j - 1))); // Adds each character of the second word.
                }
            }
            resultBuilder.append("\n");
            for (int i = 0; i <= word1.length(); i++) { // Adds the rows of the MED matrix.
                if (i == 0) {
                    resultBuilder.append("   "); // The first row is empty.
                } else {
                    resultBuilder.append(String.format("%2c", word1.charAt(i - 1))); // Adds each character of the first word.
                }

                for (int j = 0; j <= word2.length(); j++) { // Adds each cell in the matrix.
                	if (pathFollow[i][j] == -2) {
						resultBuilder.append(String.format("%4d", medTable[i][j]) + "$");
					}
                	else {
                		resultBuilder.append(String.format("%4d", medTable[i][j]) + "  "); // Adds the MED value.
                	}
                    
                }
                resultBuilder.append("\n");
            }
            resultBuilder.append("\n Part Two Total Processing Time: "+ (EndPartTwo-StartPartTwo)+" ms.");// Adds the total processing time for part two.

            resultArea.setText(resultBuilder.toString()); // Sets the results in the text area.
        });
    }
    
    // Finds the nearest words.
    public static List<Map.Entry<String, Integer>> findNearestWords(String word, List<String> vocabulary, int limit) { 
    	
        Map<String, Integer> wordDistances = new HashMap<>(); // Creates a map to store word distances.
             
        for (String vocabWord : vocabulary) { // Loops through each word in the vocabulary.
            int distance = computeMED(word, vocabWord); // Calculates the MED distance.
            wordDistances.put(vocabWord, distance); // Adds the word and its distance to the map.
        }

        return wordDistances.entrySet() // Converts the map to a list.
                .stream() // Starts a stream operation.
                .sorted(Map.Entry.comparingByValue()) // Sorts by the values (distance).
                .limit(limit)// Limits the number of results.
                .collect(Collectors.toList()); // Collects into a list.
    }
    
    // Starts the MED calculation.

    public static int computeMED(String word1, String word2) {
        int[][] dp = computeMEDTable(word1, word2); // Calculates the MED table.
        return dp[word1.length()][word2.length()]; // Returns the MED value.
    }

    // Computes the MED table.
    public static int[][] computeMEDTable(String word1, String word2) { 
        int m = word1.length(); // Gets the length of the first word.
        int n = word2.length(); // Gets the length of the second word.
        int[][] dp = new int[m + 1][n + 1]; // Creates the table.

        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0) {
                    dp[i][j] = j; // Insert all characters of word2
                } else if (j == 0) {
                    dp[i][j] = i; // Remove all characters of word1
                } else if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1]; // Characters match
                } else {
                	
                	// We consider three possibilities in case of a difference:
                
                    int removeCost = dp[i - 1][j] + 1;  // Remove
                    int insertCost = dp[i][j - 1] + 1;  // Insert
                    int replaceCost = dp[i - 1][j - 1] + 2; // Replace (cost 2 for substitution)
                    
                    // Gets the minimum cost
                    dp[i][j] = Math.min(removeCost, Math.min(insertCost, replaceCost));
                }
            }
        }

        return dp; // Returns the MED table
    }
    
    

    // Lists the MED operations.
    public static List<String> getMEDOperations(String word1, String word2, int[][] dp) {
        int i = word1.length(); // Starts at the end of the first word.
        int j = word2.length(); // Starts at the end of the second word.

        List<String> operations = new ArrayList<>(); // Creates a list to hold operations.
        pathFollow[i][j] = -2;

        while (i > 0 || j > 0) { // Loops through the two words.
        	
            if (i > 0 && j > 0 && word1.charAt(i - 1) == word2.charAt(j - 1)) {
            	pathFollow[i-1][j-1] = -2;
                i--;
                j--;
            } 
            // If characters are different and the cost indicates substitution (cost 2)
            else if (i > 0 && j > 0 && word1.charAt(i - 1) != word2.charAt(j - 1) && dp[i][j] == dp[i - 1][j - 1] + 2) 
            {   
            	operations.add("Substitution '" + word1.charAt(i - 1) + "' with '" + word2.charAt(j - 1) + "'");
            	pathFollow[i-1][j-1] = -2;
            	i--;
            	j--; 
            }
            // If removing a character from word1 (cost 1)
            else if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                operations.add("Remove '" + word1.charAt(i - 1) + "'");
                pathFollow[i-1][j] = -2;
                i--;
            }
            // If inserting a character into word1 (cost 1)
            else if (j > 0 && dp[i][j] == dp[i][j - 1] + 1) {
                operations.add("Insert '" + word2.charAt(j - 1) + "'");
                pathFollow[i][j-1] = -2;
                j--;
            } 
        }

        Collections.reverse(operations);
        return operations;
    }   
}


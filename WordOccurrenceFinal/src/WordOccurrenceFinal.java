import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JScrollPane;
import java.awt.Color;

/**
 * @author Craig Mackey
 * @version 6/8/2022
 */
public class WordOccurrenceFinal extends JFrame {
	static Map<String, Integer> count(Scanner scanner) {
	    Map<String, Integer> words = new LinkedHashMap<>();
	    while (scanner.hasNext()) {
	        words.merge(scanner.next(), 1, Integer::sum);
	    }
	    return words;
	}
	/**
	 * private JPanel contentPane;
	 */
	private JPanel contentPane;
	/**
	 * private JTextField txtPleaseSelectA;
	 */
	private JTextField txtPleaseSelectA;
	/**
	 * 	private JButton btn2;
	 */
	private JButton btn2;
	/**
	 * 	private File file;
	 */
	private File file;
	/**
	 * private JTextArea ta;
	 */
	private JTextArea ta;
	
/**
 * @param args
 * Launching the application
 * main method 
 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WordOccurrenceFinal frame = new WordOccurrenceFinal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */ 
	public WordOccurrenceFinal() {
		setTitle("Word Occurrence Program");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtPleaseSelectA = new JTextField();
		txtPleaseSelectA.setEditable(false);
		txtPleaseSelectA.setText("Select a text file to count words and store in MySQL DB.");
		txtPleaseSelectA.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		txtPleaseSelectA.setBounds(29, 6, 387, 41);
		contentPane.add(txtPleaseSelectA);
		txtPleaseSelectA.setColumns(10);
		
		JButton btn1 = new JButton("Open Text File");
		btn1.setForeground(new Color(26, 26, 234));
		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)  {
				
				JOptionPane.showMessageDialog(btn1,
					    "Please make sure you only select a .txt file.",
					    "Inane warning",
					    JOptionPane.WARNING_MESSAGE);
				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setMultiSelectionEnabled(false); 
				
				int returnVal = fileChooser.showOpenDialog(ta);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
				     file = fileChooser.getSelectedFile();
				    ta.append("You chose to open this file: " + file.getName()  + " " + file.getAbsolutePath() + "\n");
				}
	        }
		});
		
		btn1.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		btn1.setBounds(29, 59, 117, 29);
		contentPane.add(btn1);
		
		btn2 = new JButton("Send to MySQL");
		btn2.setForeground(new Color(224, 29, 29));
		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)   {
				
				try (Scanner scanner = new Scanner(file).useDelimiter("\\W+")) {
			        Map<String, Integer> map = null;
					try { map = count(scanner);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
							
					try(FileWriter fw = new FileWriter(file, true);
							BufferedWriter bw = new BufferedWriter(fw);
							PrintWriter out = new PrintWriter(bw)) {
						
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
				} catch (ClassNotFoundException e3) {
					e3.printStackTrace();
				}
				String url = "jdbc:mysql://localhost:3306/word_occurrences";
		        String user = "root";
		        String password = "****";
		        
		        String sql = "INSERT INTO wordScan(countWord, nameWord) VALUES(?,?)";
		        ta.append(sql + "\n");
		        String query = "SELECT * FROM wordScan";
		        ta.append(query + "\n");
		        
		        try (Connection con = DriverManager.getConnection(url, user, password);
		                PreparedStatement preparedStatement = con.prepareStatement(sql)) {
		        	
		        	map.entrySet()
		            .stream().sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue()))
		            .forEach(k -> {
		              try {
		                preparedStatement.setInt(1, k.getValue());
		                preparedStatement.setString(2, k.getKey());
		                preparedStatement.executeUpdate();
		              } catch (SQLException e4) {
		                throw new RuntimeException();
		              }
		            });
		      
		        		Statement statement = con.createStatement();
		                ResultSet rs = statement.executeQuery(query);

		            while (rs.next()) {    
		               System.out.print(rs.getInt(1));
		               System.out.print(": ");
		               System.out.print(rs.getInt(2) + " ");
		               System.out.println(rs.getString(3));
		           }
		                            
		        } catch (SQLException ex) {
		            
		            Logger lgr = Logger.getLogger(WordOccurrenceFinal.class.getName());
		            lgr.log(Level.SEVERE, ex.getMessage(), ex);       
		        }
					} 
			}catch (IOException e3) {
	            ta.append("File is closed. Cannot be read" + "\n");
	            ta.append("The file is "+ file.getAbsolutePath() + file.getName() + "\n");
	            System.out.print(file);
	            e3.printStackTrace();
	        }
				}
		});
		
		btn2.setBounds(299, 59, 117, 29);
		contentPane.add(btn2);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(29, 100, 387, 166);
		contentPane.add(scrollPane);
		
		ta = new JTextArea();
		scrollPane.setViewportView(ta);
	}
}

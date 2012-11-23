import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import edu.purdue.cs.cs180.channel.*;
/**
 * Project 7 -- SafeWalk 2.0 (Request)
 * provides a GUI for users to request a ride from one of 5 location around campus,
 * and allows them to choose the urgency of their need
 * 
 * @author bwencke
 * 
 * @recitation RM5 (Julian Stephen)
 * 
 * @date November 23, 2012
 *
 */
public class Request extends JFrame implements MessageListener {
	private Channel channel = null;
	private static String[] locations = {"CL50 - Class of 1950 Lecture Hall",
					"EE - Electrical Engineering Building", 
					"LWSN - Lawson Computer Science Building", 
					"PMU - Purdue Memorial Union", 
					"PUSH - Purdue University Student Health Center"}; // all the possible pick up locations
	private static String[] urgency = {"Emergency", "Urgent", "Normal"}; // all the possible urgency options
	
	// the GUI elements
	private JComboBox pickLocation, pickUrgency;
	private JButton submitButton;
	private JLabel statusLabel;

	/**
	 * constructs a new Request object
	 * 
	 * @param host a String that represents the hostname of the server
	 * 
	 * @param port an integer that represents the port that the server is running on
	 */
	public Request(String host, int port) {
		// create the locations drop down
		pickLocation = new JComboBox(locations); // put all of the locations in that drop down
		pickLocation.setSelectedIndex(0); // give it a default choice
		
		// create the urgency drop down
		pickUrgency = new JComboBox(urgency); // put all the urgency options in that drop down
		pickUrgency.setSelectedIndex(0); // default choice
		
		// create submit button
		submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				locationSubmit(); // submit the location when clicked
			}
		});

		// create status label
		statusLabel = new JLabel("");

		// create the GUI design by using panels
		// CONTROLS
		Container controlsPane = new JPanel();
		controlsPane.setLayout(new FlowLayout()); // organized the elements horizontally
		controlsPane.add(pickLocation); // add the locations drop down
		controlsPane.add(pickUrgency); // add the urgency drop down
		controlsPane.add(submitButton); // add the submit button

		// LABEL
		Container labelPane = new JPanel();
		labelPane.setLayout(new FlowLayout()); // center the label
		labelPane.add(statusLabel); // add the label

		// add everything to the window
		Container contentPane = this.getContentPane(); // get the window's content pane
		contentPane.setLayout(new GridLayout(2,1)); // organize the panels vertically
		contentPane.add(controlsPane); // add the panel with the controls first
		contentPane.add(labelPane); // then add the panel with the label beneath

		// set window properties
		this.setTitle("Request");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// pack and show!
		this.pack();
		this.setVisible(true);

		// initialize the communication channel using the host and port provided by the user
		initChannel(host, port);
	}

	/**
	 * initializes the communication channel using the host and port provided by the user
	 * 
	 * @param host the hostname of the server
	 * @param port the port number that the server is listening to
	 */
	private void initChannel(String host, int port) {
		try {
			channel = new TCPChannel(host, port); // create new TCPChannel object with specified values
			channel.setMessageListener(this); // have this Request object listen for messages (messageReceived())
		} catch (ChannelException e) {
			e.printStackTrace();
		}
	}

	/**
	 * submits the location request to the server
	 */
	private void locationSubmit() {
		// disable the GUI control elements
		pickLocation.setEnabled(false); // disable the location drop down
		pickUrgency.setEnabled(false); // disable the urgency drop down
		submitButton.setEnabled(false); // disable the submit button

		// send the request to the server
		try {
			channel.sendMessage("Request:" + locations[pickLocation.getSelectedIndex()] + "|" + urgency[pickUrgency.getSelectedIndex()]); // send message with the location and urgency
		} catch (ChannelException e) {
			e.printStackTrace();
		}
	}

	@Override
	/**
	 * listens for a message and sets the label text when one is received
	 * also checks to see if the message is an assigned message
	 */
	public void messageReceived(String arg0, int arg1) {
		if(arg0.startsWith("Searching")) {
			statusLabel.setText("Searching");
		} else {
			String labelText = arg0.substring(arg0.indexOf(':')+1);;
			statusLabel.setText("Assigned: " + labelText);
			pickLocation.setEnabled(true); // enable the location drop down
			pickUrgency.setEnabled(true); // enable the urgency drop down
			submitButton.setEnabled(true); // enable the submit button
		}
		this.pack();
	}

	public static void main(String[] args) {
		new Request(args[0], Integer.parseInt(args[1])); // create new Request object with the values entered by the user
	}
}
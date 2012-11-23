import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import edu.purdue.cs.cs180.channel.*;
/**
 * Project 7 -- Response
 * provides a GUI for volunteers to respond to requests for rides,
 * which then sends the response team to a server
 * 
 * @author cservaas
 * 
 * @recitation RM5 (Julian Stephen)
 * 
 * @date November 24, 2012
 *
 */
public class Response extends JFrame implements MessageListener {
	private Channel channel = null;
	private static String[] places = {"CL50 - Class of 1950 Lecture Hall",
		"EE - Electrical Engineering Building", 
		"LWSN - Lawson Computer Science Building", 
		"PMU - Purdue Memorial Union", 
		"PUSH - Purdue University Student Health Center"}; // all the possible pick up locations
	
	private JButton readyButton; // the field for the readyButton
	private JLabel statusLabel; // the field for the label that displays when the readyButton is pushed.
	private JComboBox locationDropDown;

	/**
	 * creates a new Response object
	 * 
	 * @param host a String that represents the server host
	 * 
	 * @param port an integer that represents the port used by the server
	 */
	public Response(String host, int port) {

		// create drop down
		locationDropDown = new JComboBox(places);
		locationDropDown.setSelectedIndex(0);
		
		// create submit button
		readyButton = new JButton("Ready"); // the readyButton says "ready"
		readyButton.addActionListener(new ActionListener() { //when the button is pressed, it calls readySubmit. 
			public void actionPerformed(ActionEvent e) {
				readySubmit(); // submit the location when clicked
			}
		});

		statusLabel = new JLabel(""); // the status label is initially blank. 

		// CONTAINERS
		Container controlsPane = new JPanel(); // creates the control panel 
		Container labelPane = new JPanel();
		Container contentPane = this.getContentPane(); // get the window's content pane

		// LAYOUTS
		controlsPane.setLayout(new FlowLayout()); // sets the layout for the outer panel
		labelPane.setLayout(new FlowLayout()); // center the label
		contentPane.setLayout(new GridLayout(2,1)); // organize the panels vertically

		// ADD ELEMENTS
		controlsPane.add(locationDropDown); // add the drop down
		controlsPane.add(readyButton); // add the submit button for the control panel. 
		labelPane.add(statusLabel); // add the label
		contentPane.add(controlsPane); // add the panel with the controls first
		contentPane.add(labelPane); // then add the panel with the label beneath		

		// set window properties
		this.setTitle("Response");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// finish up
		this.pack();
		this.setVisible(true);

		// open the communication channel
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
			channel = new TCPChannel(host, port); // create TCPChannel object
			channel.setMessageListener(this); // have this Response object listen for messages (messageReceived())
		} catch (ChannelException e) {
			e.printStackTrace();
		}
	}

	/**
	 * submits the Response to the server
	 */
	private void readySubmit() {
			readyButton.setEnabled(false); // disable the ready button
			locationDropDown.setEnabled(false); // disable drop down

			// tell the server that the volunteer is ready. 
			try {
				channel.sendMessage("Response:" + "Help Team " + channel.getID() + "|" + places[locationDropDown.getSelectedIndex()]); // send message with team and its location
			} catch (ChannelException e) {
				e.printStackTrace();
			}
		}

	@Override
	/**
	 * listens for a location message and displays information accordingly
	 */
	public void messageReceived(String arg0, int arg1) {
		String location = arg0.substring(10); // create String that holds the assignment location
		if(location.length() == 0) {
			statusLabel.setText(arg0.substring(0,9)); // if the location is blank, set the label to "Searching"
		} else {
			statusLabel.setText("Assigned: " + arg0.substring(9)); // else, show the location
			locationDropDown.setEnabled(true); // re-enable
			readyButton.setEnabled(true); // re-enable the readyButton 
		}
		this.pack(); // adjusts the window.

	}

	public static void main(String[] args) {
		new Response(args[0], Integer.parseInt(args[1])); // create new Response object with args values
	}
}
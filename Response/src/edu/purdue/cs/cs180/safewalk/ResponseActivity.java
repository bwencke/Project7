package edu.purdue.cs.cs180.safewalk;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import edu.purdue.cs.cs180.channel.ChannelException;
import edu.purdue.cs.cs180.channel.MessageListener;
import edu.purdue.cs.cs180.channel.TCPChannel;

public class ResponseActivity extends Activity implements MessageListener {

	TCPChannel channel = null;
	Handler mHandler = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_response);

		// the ready button.
		final Button button = (Button) findViewById(R.id.ready_button);
		final TextView status = (TextView) findViewById(R.id.status_textview);

		try {
			channel = new TCPChannel(getString(R.string.host_name), Integer.parseInt(getString(R.string.port_number))); // create new TCPChannel object with specified values
			channel.setMessageListener(this); // have this Request object listen for messages (messageReceived())
		} catch (ChannelException e) {
			e.printStackTrace();
		}

		// A handler is needed since the message received is called from a
		// different Thread, and only the main thread can update the UI.
		// As a workaround, we create a handler in the main thread that displays
		// whatever it receives from the message received.
		mHandler = new Handler() {
			@Override
			public void handleMessage(android.os.Message msg) {
				Message safeWalkMessage = (Message) msg.obj; // load the message object
				String msgStr; // create a String to hold the information to be printed
				switch(safeWalkMessage.getType()) {
					case Searching:
						msgStr = "Searching";
						break;
					case Assigned:
						msgStr = "Assigned: " + safeWalkMessage.getInfo();
						button.setEnabled(true);
						break;
					default:
						msgStr = "There was an error somewhere. Oopsies!";
				}
				status.setText(msgStr); // set text label
			}
		};

		// The on click event.
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				button.setEnabled(false);
				try {
					channel.sendMessage("Response:" + "Help Team " + channel.getID()); // send message with team
				} catch (ChannelException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void messageReceived(String message, int clientID) {
		// Create a handler message, and send it to the Main Thread.
		Message safeWalkMessage = new Message(message, clientID);
		android.os.Message msg = new android.os.Message();
		msg.obj = safeWalkMessage;
		mHandler.sendMessage(msg);
	}

	/**
	 * Close the application if sent to the background.
	 */
	@Override
	protected void onPause() {
	    super.onPause();
	    System.exit(0);
	}
}
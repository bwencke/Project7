package edu.purdue.cs.cs180.safewalk;

/**
 * Unlike the channel message that is general to all messages that can be send
 * on the channel, this class handles the SafeWalk messages only.
 * 
 * @author fmeawad
 * 
 */
public class Message {
	/**
	 * The type of the message.
	 * 
	 * @author fmeawad
	 * 
	 */
	public enum Type {
		/**
		 * When a new request is submitted.
		 */
		Request,
		/**
		 * When a responder is available.
		 */
		Response,
		/**
		 * Looking for a match.
		 */
		Searching,
		/**
		 * A match was found.
		 */
		Assigned
	}

	/**
	 * Message type
	 */
	private Type type;

	/**
	 * Message text
	 */
	private String info;

	/**
	 * The client ID where the message originated.
	 */
	private int clientID;

	/**
	 * Constructor to be used when creating a message from scratch.
	 * 
	 * @param type
	 *            the type of message.
	 * @param info
	 *            the message text.
	 * @param clientID
	 *            to be sent to.
	 */
	public Message(Type type, String info, int clientID) {
		this.type = type;
		this.info = info;
		this.clientID = clientID;
	}

	/**
	 * Constructor overload to be used when constructing a message from a
	 * received message.
	 * 
	 * @param messageString
	 *            the received message text (including the type).
	 * @param clientID
	 *            the clientID it was received from.
	 */
	public Message(String messageString, int clientID) {
		String[] messageParts = messageString.split(":", 2);
		this.type = Type.valueOf(messageParts[0]);
		this.info = messageParts[1];
		this.clientID = clientID;
	}

	/**
	 * Getter.
	 * 
	 * @return
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Getter.
	 * 
	 * @return
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * Getter.
	 * 
	 * @return
	 */
	public int getClientID() {
		return clientID;
	}

	/**
	 * Construct the message from its different components (does not include the
	 * clientID).
	 */
	public String toString() {
		return this.type.name() + ":" + this.info;
	}
}

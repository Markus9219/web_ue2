package at.ac.tuwien.big.we15.lab2.api.impl;

public class Message {
	private String text;
	private MessageType type;
	
	
	public Message(String text, MessageType type) {
		super();
		this.text = text;
		this.type = type;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public MessageType getType() {
		return type;
	}
	public void setType(MessageType type) {
		this.type = type;
	}
	
	
}

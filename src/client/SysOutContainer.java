package client;

import chat.MessageContainer;

public class SysOutContainer implements MessageContainer {
	
	public void newMessage(String message) {
		if (message == null || message.equals(""))
			return;
		String[] messageParts = message.split(MessageContainer.FROM);

		System.out.println(message);
	}
}
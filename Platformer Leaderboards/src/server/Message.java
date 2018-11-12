package server;

import java.util.UUID;

public class Message 
{
	public String Message;
	public UUID Sender;
	public int Socket;
	public int serverListIndex;
	
	public Message(String message, String sender, int socket)
	{
		Message = message;
		Sender = UUID.fromString(sender);
		Socket = socket;
	}
	
	public Message(String message, String sender, int socket, int serverlistindex)
	{
		Message = message;
		Sender = UUID.fromString(sender);
		Socket = socket;
		serverListIndex = serverlistindex;
	}
}

package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;

import main.Main;

public class Server 
{
	public boolean Uninitialized;
	public int Socket;
	public String UUIDOfPlayer;
	public String IP;
	public Thread mainThread;
	public Socket savedSocket;
	public ServerSocket savedServerSocket;
	public BufferedReader Input;
	public BufferedWriter Output;
	public boolean Banned;
	public boolean timedOut;
	public int serverListIndex;
	
	public Server(int socket, String ip)
	{
		Socket = socket;
		IP = ip;
		timedOut = false;
		Uninitialized = true;
	}
	
	public Server(int socket, String ip, String uuidOfPlayer, int serverlistindex)
	{
		Socket = socket;
		IP = ip;
		UUIDOfPlayer = uuidOfPlayer;
		serverListIndex = serverlistindex;
		timedOut = false;
		Uninitialized = true;
	}
	
	public void startMainServer()
	{
		System.out.println("Starting Main Server...");
		mainThread = new Thread() 
        {
            @Override
            public void run() 
            {
                try 
                {
                	ServerSocket serverSock;
                    serverSock = new ServerSocket(023123);
					System.out.println("Main Server started.");
                    Socket clientSock = serverSock.accept();
					clientSock.setSoTimeout(10000);
					savedSocket = clientSock;
                    savedServerSocket = serverSock;
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSock.getOutputStream()));
					String Line = null;
                    while ((Line = in.readLine()) != null) 
                    {
                    	for (int i = 0; i < Main.banList.size(); i++)
                    	{
                    		
                    		if (Main.banList.get(i).UUID.toString().contains(Line))
                    		{
                    			Banned = true;
                    		}
                    		
                    		if (UUID.fromString(Line) == null)
                    		{
                    			Banned = true;
                    		}
                    	}
                    	if (Banned == false)
                    	{
                    		Main.socketIndex += 1;
                        	System.out.println(Line + " Player " + Main.socketIndex + " joined! IP: " + clientSock.getRemoteSocketAddress().toString().replace("/", ""));
                    	}
                    	else
                    	{
                    		System.out.println(Line + " Banned Player " + Main.socketIndex + " attempted to join! IP: " + clientSock.getRemoteSocketAddress().toString().replace("/", ""));
                    	}
                    	break;
                    }
                    
                    if (Banned == false)
                    {
                    	out.write(String.valueOf(Main.socketIndex));
                    	out.newLine();
                    	out.flush();
                    	int Index = Main.serverList.size();
                    	Main.serverList.add(new Server(Main.socketIndex, clientSock.getRemoteSocketAddress().toString().replace("/", ""), Line, Index));
                    }
                    else
                    {
                    	out.write("Banned");
                    	out.newLine();
                    	out.flush();
                    }
                    
                	System.out.println("Restarting Main Server...");
                	in.close();
                	in = null;
                	out.close();
                	out = null;
                	clientSock.close();
                	serverSock.close();
                	startMainServer();
                }
                catch (Exception e)
                {
                	e.printStackTrace();
                }
            }
        };
        mainThread.start();
	}
	
	public void dropServer()
	{
		timedOut = true;
		try
		{
			Input.close();
			Output.close();
			savedSocket.close();
			savedServerSocket.close();
			Input = null;
			Output = null;
			savedSocket = null;
			savedServerSocket = null;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void startCustomServer()
	{
		System.out.println("Custom Server starting...");
		(new Thread() 
        {
            @Override
            public void run() 
            {
                ServerSocket serverSock;
                try 
                {
                    serverSock = new ServerSocket(Socket);
                    System.out.println("Custom Server started.");
                    Socket clientSock = serverSock.accept();
					clientSock.setSoTimeout(10000);
					savedSocket = clientSock;
                    savedServerSocket = serverSock;
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSock.getOutputStream()));
                    Output = out;
                    Input = in;
                    String Line = null;
                    while ((Line = in.readLine()) != null) 
                    {
                    	if (!timedOut)
                    	{
                    		Main.messageList.add(new Message(Line, UUIDOfPlayer, Socket, serverListIndex));
                    	}
                    	else
                    	{
                    		break;
                    	}
					Thread.sleep(10);
                    }
                }
                catch (SocketException e)
                {
                	Main.messageList.clear();
                	timedOut = true;
                	System.out.println("Server " + Socket + " was dropped.");
                	System.out.flush();
                }
                catch (Exception e)
                {
                	Main.messageList.clear();
                	timedOut = true;
                	e.printStackTrace();
                }
            }
        }
        ).start();
	}
	
	public void sendMessage(String toSend)
	{
		try
		{
			if (Output != null && !timedOut)
			{
				Output.write(toSend);
				Output.newLine();
				Output.flush();
			}
		}
		catch (Exception e)
		{
			timedOut = true;
			e.printStackTrace();
		}
	}
}

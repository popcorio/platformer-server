package main;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

import leaderboard.Leaderboard;
import leaderboard.LeaderboardPosition;
import server.Ban;
import server.Command;
import server.Message;
import server.Server;

public class Main 
{
	public static int socketIndex;
	public static Server mainServer = new Server(0, "null");
	public static ArrayList<Server> serverList = new ArrayList<Server>();
	public static ArrayList<Ban> banList = new ArrayList<Ban>();
	public static ArrayList<Message> messageList = new ArrayList<Message>();
	public static ArrayList<LeaderboardPosition> leaderBoard = new ArrayList<LeaderboardPosition>();
	public static String banFile = "bans.txt";
	public static String leaderboardFile = "leaderboard.txt";
	
	public static void main(String[] args)
	{
		Ban.initializeBanList(banFile);
		Leaderboard.initializeLeaderboard(leaderboardFile);
		mainServer.startMainServer();
		serverUpdate();
		Command.startChecker();
	}
	
	public static void serverUpdate()
	{
		(new Thread()
		{
			@Override
			public void run()
			{
				while (true)
				{
					leaderBoard = (ArrayList<LeaderboardPosition>) leaderBoard.stream().filter(b -> !b.toDelete).collect(Collectors.toList());	
					
					for (int i = 0; i < serverList.size(); i++)
					{
						if (serverList.get(i).Uninitialized == true)
						{
							serverList.get(i).startCustomServer();
							serverList.get(i).Uninitialized = false;
						}
					}
					
					for (int i = 0; i < messageList.size(); i++)
					{
						if (messageList.get(i).Message.length() > 0)
						{
							String receivedMessage = messageList.get(i).Message.replaceAll("\n", "");
							
							if (receivedMessage.contains("Record"))
							{
								if (serverList.get(messageList.get(i).serverListIndex).Banned == false)
								{
									String[] givenParameters = receivedMessage.split(" ");
									UUID playerUUID = UUID.fromString(givenParameters[1]);
									String Name = givenParameters[2];
									float Time = Float.parseFloat(givenParameters[3]);
									int Level = Integer.parseInt(givenParameters[4]);
									Leaderboard.changePosition(playerUUID, Name, Time, Level);
									serverList.get(messageList.get(i).serverListIndex).sendMessage("Recorded");
								}
							}
							else if (receivedMessage.contains("Get"))
							{
								int surroundingSize = 5;
								String[] givenParameters = receivedMessage.split(" ");
								UUID playerUUID = UUID.fromString(givenParameters[1]);
								int Level = Integer.parseInt(givenParameters[2]);
								int playerPosition = -1;
								
								for (int j = 0; j < leaderBoard.size(); j++)
								{
									if (leaderBoard.get(j).Level == Level && leaderBoard.get(j).UUIDOfPlayer == playerUUID)
									{
										playerPosition = leaderBoard.get(j).Position;
									}
								}
								
								if (playerPosition != -1)
								{
									for (int j = 0; j < leaderBoard.size(); j++)
									{
										if (leaderBoard.get(j).Level == Level)
										{
											int leaderBoardPos = leaderBoard.get(j).Position;
											if (Math.abs(leaderBoardPos - playerPosition) <= surroundingSize)
											{
												serverList.get(messageList.get(i).serverListIndex).sendMessage(leaderBoardPos + " " + leaderBoard.get(j).nameOfPlayer + " " + leaderBoard.get(j).Time + "\n");
											}
										}
									}
								}
								serverList.get(messageList.get(i).serverListIndex).sendMessage("Finished");
							}
						}
					}
					
					serverList = (ArrayList<Server>) serverList.stream().filter(b -> !b.timedOut).collect(Collectors.toList());
					messageList = new ArrayList<Message>();
					
					try 
					{
						Thread.sleep(10);
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}

package server;

import java.util.Scanner;
import java.util.UUID;

import main.Main;

public class Command 
{
	public static void startChecker()
	{
		(new Thread()
		{
			@Override
			public void run()
			{
				while (true)
				{
					@SuppressWarnings("resource")
					Scanner sc = new Scanner(System.in);
					String userInput = sc.nextLine();
					if (userInput.contains("die"))
					{
						int portToKill = 999999999;
						try
						{
							portToKill = Integer.parseInt(userInput.replace("die ", ""));
						}
						catch (Exception e)
						{
							System.out.println("Invald port or none.");
							System.out.flush();
						}
						if (portToKill != 999999999)
						{
							try
							{
								boolean Dropped = false;
								for (int i = 0; i < Main.serverList.size(); i++)
								{
									if (Main.serverList.get(i).Socket == portToKill)
									{
										Main.serverList.get(i).dropServer();
										System.out.println("Dropped player: " + portToKill);
										System.out.flush();
										Dropped = true;
										break;
									}
								}
								if (!Dropped)
								{
									System.out.println("Unable to drop player: " + portToKill);
									System.out.flush();
								}
							}
							catch (Exception e)
							{
								System.out.println("Unable to drop player: " + portToKill);
								System.out.flush();
								e.printStackTrace();
							}
						}
					}
					else if (userInput.contains("listp"))
					{
						for (int i = 0; i < Main.serverList.size(); i++)
						{
							System.out.println("Player: " + Main.serverList.get(i).Socket + " IP: " + Main.serverList.get(i).IP + " UUID: " + Main.serverList.get(i).UUIDOfPlayer);
							System.out.flush();
						}
					}
					else if (userInput.contains("listl"))
					{
						int playerToList = 999999999;
						try
						{
							playerToList = Integer.parseInt(userInput.replace("listl ", ""));
						}
						catch (Exception e)
						{
							for (int i = 0; i < Main.leaderBoard.size(); i++)
							{
								System.out.println("Position: " + Main.leaderBoard.get(i).Position + " Level: " + Main.leaderBoard.get(i).Level + " UUID: " + Main.leaderBoard.get(i).UUIDOfPlayer + " Name: " + Main.leaderBoard.get(i).nameOfPlayer + " Time: " + Main.leaderBoard.get(i).Time);
							}
							System.out.flush();
						}
						
						boolean foundPlayer = false;
						String playerUUID = "";
						for (int i = 0; i < Main.serverList.size(); i++)
						{
							if (Main.serverList.get(i).Socket == playerToList)
							{
								playerUUID = Main.serverList.get(i).UUIDOfPlayer;
								foundPlayer = true;
								break;
							}
						}
						
						if (foundPlayer == true && !playerUUID.equals(""))
						{
							for (int i = 0; i < Main.leaderBoard.size(); i++)
							{
								if (Main.leaderBoard.get(i).UUIDOfPlayer == UUID.fromString(playerUUID))
								{
									System.out.println("Position: " + Main.leaderBoard.get(i).Position + " Level: " + Main.leaderBoard.get(i).Level + " UUID:" + Main.leaderBoard.get(i).UUIDOfPlayer + " Name: " + Main.leaderBoard.get(i).nameOfPlayer + " Time: " + Main.leaderBoard.get(i).Time);
								}
							}
						}
						else
						{
							System.out.print("Unable to find player: " + playerToList);
						}
						System.out.flush();
					}
					else if (userInput.contains("ban"))
					{
						int playerToBan = 999999999;;
						
						try 
						{
							playerToBan = Integer.parseInt(userInput.replace("ban ", ""));
						}
						catch (Exception e)
						{
							System.out.println("Invalid player or none.");
							System.out.flush();
						}
						
						if (playerToBan != 999999999)
						{
							try
							{
								boolean Done = false;
								for (int i = 0; i < Main.serverList.size(); i++)
								{
									if (Main.serverList.get(i).Socket == playerToBan)
									{
										Main.banList.add(new Ban(Main.serverList.get(i).UUIDOfPlayer.toString(), true));
										Main.serverList.get(i).dropServer();
										System.out.println("Banned player: " + playerToBan);
										System.out.flush();
										Done = true;
										break;
									}
									
									if (!Done)
									{
										System.out.println("Unable to ban player: " + playerToBan);
										System.out.flush();
									}
								}
							}
							catch (Exception e)
							{
								System.out.println("Unable to ban player: " + playerToBan);
								System.out.flush();
								e.printStackTrace();
							}
						}
					}
				}
			}
		}).start();
	}
}

package server;

import java.util.Scanner;

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
					else if (userInput.contains("list"))
					{
						for (int i = 0; i < Main.serverList.size(); i++)
						{
							System.out.println("Player: " + Main.serverList.get(i).Player + " IP: " + Main.serverList.get(i).IP + " UUID: " + Main.serverList.get(i).UUIDOfPlayer);
							System.out.flush();
						}
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
										Main.banList.add(new Ban(Main.serverList.get(i).UUIDOfPlayer.toString()));
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

package leaderboard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

import main.Main;

public class Leaderboard 
{
	public static void initializeLeaderboard(String fileName)
	{
		File fileToWrite = new File(fileName);
		if (fileToWrite.exists() == false)
		{
			try 
			{
				PrintWriter Writer = new PrintWriter(fileName, "UTF-8");
				Writer.println("LEADERBOARD - DO NOT EDIT DURING USAGE");
				Writer.close();
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			} 
		}
		else
		{
			try 
			{
				BufferedReader bufferedReader = new BufferedReader(new FileReader(fileToWrite));
				try
				{
					String curLine = bufferedReader.readLine();
					while (curLine != null)
					{
						if (!curLine.contains("LEADERBOARD"))
						{
							String[] splitLine = curLine.split(" ");
							
							boolean Banned = false;
							
							for (int i = 0; i < Main.banList.size(); i++)
							{
								if (Main.banList.get(i).UUID == UUID.fromString(splitLine[1]))
									Banned = true;
							}
							
							if (Banned == false)
							{
								Main.leaderBoard.add(new LeaderboardPosition(Integer.parseInt(splitLine[0]), UUID.fromString(splitLine[1]), splitLine[2], Integer.parseInt(splitLine[3]), Float.parseFloat(splitLine[4]), false));
							}	
						}
						curLine = bufferedReader.readLine();
					}
				}
				finally
				{
					bufferedReader.close();
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public static int findPlayerNumber(String playerUUID)
	{
		for (int i = 0; i < Main.serverList.size(); i++)
		{
			if (Main.serverList.get(i).UUIDOfPlayer.equals(playerUUID))
			{
				return Main.serverList.get(i).Socket;
			}
		}
		return -1;
	}
	
	public static void changePosition(UUID playerUUID, String Name, float Time, int Level)
	{
		int lowestPos = Main.leaderBoard.size() + 1;
		int ogLowestPos = lowestPos; 
		boolean hasBetterTime = false;
		int serverIndex = findPlayerNumber(playerUUID.toString());
		
		if (serverIndex != -1)
		{
			System.out.println("Player " + serverIndex + " just gave a time of " + Time + "ms on level " + Level);
		}
		
		for (int i = 0; i < Main.leaderBoard.size(); i++)
		{
			if (Main.leaderBoard.get(i).Level != Level)
			{	
				continue;
			}
			
			if (Main.leaderBoard.get(i).UUIDOfPlayer.equals(playerUUID))
			{
				if (Main.leaderBoard.get(i).Time >= Time)
				{
					if (lowestPos == ogLowestPos)
						lowestPos--;
					Main.leaderBoard.get(i).toDelete = true;
					continue;
				}
				else
				{
					hasBetterTime = true;
					break;
				}		
			}
			
			if (Main.leaderBoard.get(i).Time >= Time)
			{
				if (Main.leaderBoard.get(i).Position < lowestPos)
				{
					lowestPos = Main.leaderBoard.get(i).Position;
				}
			}
		}
		
		if (hasBetterTime == false)
		{
			for (int i = 0; i < Main.leaderBoard.size(); i++)
			{
				if (Main.leaderBoard.get(i).Level != Level)
				{	
					continue;
				}
				
				if (Main.leaderBoard.get(i).UUIDOfPlayer.equals(playerUUID))
				{
					continue;
				}
				
				if (Main.leaderBoard.get(i).Time >= Time)
				{
					Main.leaderBoard.get(i).Position++;
				}
			}
		}
		
		Main.leaderBoard = (ArrayList<LeaderboardPosition>) Main.leaderBoard.stream().filter(b -> !b.toDelete).collect(Collectors.toList());
		
		if (ogLowestPos == lowestPos)
		{
			lowestPos = Main.leaderBoard.size() + 1;
		}
		
		if (hasBetterTime == false)
		{
			Main.leaderBoard.add(new LeaderboardPosition(lowestPos, playerUUID, Name, Level, Time, true));
		}
	}
}

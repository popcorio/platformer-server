package leaderboard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.UUID;

import main.Main;

public class Leaderboard 
{
	public int betterTime(int pos1, int time1, int pos2, int time2)
	{
		if (time1 >= time2)
		{
			return pos2;
		}
		return pos1;
	}

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
								Main.leaderBoard.add(new LeaderboardPosition(Integer.parseInt(splitLine[0]), UUID.fromString(splitLine[1]), splitLine[2], Integer.parseInt(splitLine[3]), Float.parseFloat(splitLine[4])));
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
	
	public static void changePosition(UUID playerUUID, String Name, float Time, int Level)
	{
		int lowestPos = Main.leaderBoard.size() + 1;
		for (int i = 0; i < Main.leaderBoard.size(); i++)
		{
			if (Main.leaderBoard.get(i).Level != Level)
			{	
				continue;
			}
			
			if (Main.leaderBoard.get(i).UUIDOfPlayer == playerUUID && Main.leaderBoard.get(i).Level == Level && Main.leaderBoard.get(i).Time > Time)
			{
				Main.leaderBoard.get(i).toDelete = true;				
			}
			
			if (Main.leaderBoard.get(i).Time >= Time)
			{
				if (Main.leaderBoard.get(i).Position < lowestPos)
				{
					lowestPos = Main.leaderBoard.get(i).Position;
				}
				
				Main.leaderBoard.get(i).Position++;
			}
		}
		Main.leaderBoard.add(new LeaderboardPosition(lowestPos, playerUUID, Name, Level, Time));
	}
}
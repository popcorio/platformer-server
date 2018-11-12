package leaderboard;

import java.io.File;
import java.io.PrintWriter;
import java.util.UUID;

import main.Main;

public class LeaderboardPosition 
{
	public int Position;
	public UUID UUIDOfPlayer;
	public String nameOfPlayer;
	public int Level;
	public float Time;
	public boolean toDelete;
	
	public LeaderboardPosition(int pos, UUID playeruuid, String name, int level, float time, boolean toAdd)
	{
		Position = pos;
		UUIDOfPlayer = playeruuid;
		nameOfPlayer = name;
		Level = level;
		Time = time;
		toDelete = false;
		File fileToWrite = new File("leaderboard.txt");
		if (fileToWrite.exists() == true && toAdd == true)
		{
			try 
			{
				PrintWriter Writer = new PrintWriter("leaderboard.txt", "UTF-8");
				for (int i = -1; i < Main.leaderBoard.size(); i++)
				{
					if (i == -1)
					{
						Writer.println("LEADERBOARD - DO NOT EDIT DURING USAGE");
					}
					else
					{
						Writer.println(Main.leaderBoard.get(i).Position + " " + Main.leaderBoard.get(i).UUIDOfPlayer + " " + Main.leaderBoard.get(i).nameOfPlayer + " " + Main.leaderBoard.get(i).Level + " " + Main.leaderBoard.get(i).Time);
					}
				}
				Writer.println(pos + " " + playeruuid + " " + name + " " + level + " " + time);
				Writer.close();
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			} 
		}
	}
}

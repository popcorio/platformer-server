package leaderboard;

import java.io.File;
import java.io.PrintWriter;
import java.util.UUID;

public class LeaderboardPosition 
{
	public int Position;
	public UUID UUIDOfPlayer;
	public String nameOfPlayer;
	public int Level;
	public float Time;
	public boolean toDelete;
	
	public LeaderboardPosition(int pos, UUID playeruuid, String name, int level, float time)
	{
		Position = pos;
		UUIDOfPlayer = playeruuid;
		nameOfPlayer = name;
		Level = level;
		Time = time;
		File fileToWrite = new File("leaderboard.txt");
		if (fileToWrite.exists() == false)
		{
			try 
			{
				PrintWriter Writer = new PrintWriter("leaderboard.txt", "UTF-8");
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

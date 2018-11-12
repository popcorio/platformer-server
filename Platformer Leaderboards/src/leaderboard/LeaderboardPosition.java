package leaderboard;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

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
		File fileToWrite = new File("leaderboard.txt");
		if (fileToWrite.exists() == true && toAdd == true)
		{
			try 
			{
				List<String> allPreviousLines = Files.readAllLines(fileToWrite.toPath());
				PrintWriter Writer = new PrintWriter("leaderboard.txt", "UTF-8");
				for (int i = 0; i < allPreviousLines.size(); i++)
				{
					Writer.println(allPreviousLines.get(i));
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

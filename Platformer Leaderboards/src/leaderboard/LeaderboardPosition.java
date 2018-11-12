package leaderboard;

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
	}
}

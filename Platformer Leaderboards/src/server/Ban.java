package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.UUID;

import main.Main;

public class Ban 
{
	public UUID UUID;
	
	public Ban(String banUUID, boolean toBan)
	{
		UUID = java.util.UUID.fromString(banUUID);
		File fileToWrite = new File(Main.leaderboardFile);
		if (fileToWrite.exists() == true && toBan == true)
		{
			try
			{
				PrintWriter Writer = new PrintWriter(Main.leaderboardFile, "UTF-8");
				for (int i = -1; i < Main.banList.size(); i++)
				{
					if (i == -1)
					{
						Writer.println("BANS - DO NOT EDIT DURING USAGE");
					}
					else
					{
						Writer.println(Main.banList.get(i).UUID);
					}
				}
				Writer.println(banUUID);
				Writer.close();
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void initializeBanList(String fileName)
	{
		File fileToWrite = new File(fileName);
		if (fileToWrite.exists() == false)
		{
			try 
			{
				PrintWriter Writer = new PrintWriter(fileName, "UTF-8");
				Writer.println("BANS - DO NOT EDIT DURING USAGE");
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
						if (!curLine.contains("BANS"))
						{
							Main.banList.add(new Ban(curLine, false));
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
}

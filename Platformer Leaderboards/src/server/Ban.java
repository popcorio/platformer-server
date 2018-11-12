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
	
	public Ban(String banUUID)
	{
		UUID = java.util.UUID.fromString(banUUID);
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
							Main.banList.add(new Ban(curLine));
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

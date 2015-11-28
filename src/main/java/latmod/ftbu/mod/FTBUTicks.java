package latmod.ftbu.mod;

import ftb.lib.*;
import latmod.ftbu.mod.cmd.admin.CmdAdminRestart;
import latmod.ftbu.mod.config.FTBUConfigGeneral;
import latmod.ftbu.world.Backups;
import latmod.lib.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;

public class FTBUTicks
{
	private static MinecraftServer server;
	public static boolean isDediServer = false;
	private static long startMillis = 0L;
	private static long currentMillis = 0L;
	private static long restartSeconds = 0L;
	private static String lastRestartMessage = "";
	
	public static void serverStarted()
	{
		server = FTBLib.getServer();
		isDediServer = server.isDedicatedServer();
		
		currentMillis = startMillis = Backups.lastTimeRun = LMUtils.millis();
		restartSeconds = 0;
		
		if(FTBUConfigGeneral.restartTimer.get() > 0)
		{
			restartSeconds = (long)(FTBUConfigGeneral.restartTimer.get() * 3600D);
			FTBU.mod.logger.info("Server restart in " + LMStringUtils.getTimeString(restartSeconds * 1000L));
		}
	}
	
	@SuppressWarnings("all")
	public static void serverStopped()
	{
		server = null;
		isDediServer = false;
		currentMillis = startMillis = restartSeconds = 0L;
	}
	
	public static void update()
	{
		long t = LMUtils.millis();
		
		if(t - currentMillis >= 750L)
		{
			currentMillis = t;
			
			long secondsLeft = 3600L;
			
			if(restartSeconds > 0L)
			{
				secondsLeft = getSecondsUntilRestart();
				
				String msg = null;
				
				if(secondsLeft <= 10) msg = secondsLeft + " Seconds";
				else if(secondsLeft == 30) msg = "30 Seconds";
				else if(secondsLeft == 60) msg = "1 Minute";
				else if(secondsLeft == 300) msg = "5 Minutes";
				else if(secondsLeft == 600) msg = "10 Minutes";
				
				if(msg != null && !lastRestartMessage.equals(msg))
				{
					lastRestartMessage = msg;
					
					if(secondsLeft <= 0) { CmdAdminRestart.restart(); return; }
					else FTBLib.printChat(BroadcastSender.inst, EnumChatFormatting.LIGHT_PURPLE + "Server will restart after " + msg);//LANG
				}
			}
			
			if(secondsLeft > 60 && Backups.getSecondsUntilNextBackup() <= 0L) Backups.run();
		}
	}
	
	public static long getSecondsUntilRestart()
	{ return Math.max(0L, restartSeconds - (currentSeconds() - startSeconds())); }
	
	public static void forceShutdown(int sec)
	{
		restartSeconds = Math.max(0, sec) + 1L;
		//currentMillis = LatCore.millis();
		//currentSeconds = startSeconds = startMillis / 1000L;
	}
	
	public static long currentMillis()
	{ return currentMillis; }
	
	public static long currentSeconds()
	{ return currentMillis / 1000L; }
	
	public static long startSeconds()
	{ return startMillis / 1000L; }
}
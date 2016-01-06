package latmod.ftbu.mod.config;

import ftb.lib.FTBLib;
import ftb.lib.item.ItemStackTypeAdapter;
import latmod.lib.config.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import java.util.*;

public class FTBUConfigLogin
{
	private static final ConfigEntryStringArray motd = new ConfigEntryStringArray("motd", "Welcome to the server!").setInfo("Message of the day. This will be displayed when player joins the server");
	public static final ConfigEntryString custom_badges = new ConfigEntryString("custom_badges", "").sync().setInfo("URL for per-server custom badges file (Json)\nExample can be seen here:\nhttp://pastebin.com/ZXVhpEZ1");
	private static final ConfigEntryStringArray starting_items = new ConfigEntryStringArray("starting_items", "minecraft:apple 16 0").setInfo("Items to give player when he first joins the server\nFormat: StringID Size Metadata\nDoes not support NBT yet");
	
	public static List<ItemStack> getStartingItems(UUID id)
	{
		ArrayList<ItemStack> list = new ArrayList<>();
		
		for(String s : starting_items.get())
		{
			ItemStack is = ItemStackTypeAdapter.parseItem(s);
			if(is != null) list.add(is);
		}
		
		return list;
	}
	
	public static boolean printMotd(EntityPlayerMP ep)
	{
		for(String s : FTBUConfigLogin.motd.get())
			FTBLib.printChat(ep, s.replace("$player$", ep.getDisplayName()));
		return true;
	}
}
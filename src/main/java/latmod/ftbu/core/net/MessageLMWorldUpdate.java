package latmod.ftbu.core.net;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

import latmod.ftbu.core.world.*;
import latmod.ftbu.mod.FTBU;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.simpleimpl.*;
import cpw.mods.fml.relauncher.*;

public class MessageLMWorldUpdate extends MessageLM<MessageLMWorldUpdate> implements IClientMessageLM<MessageLMWorldUpdate>
{
	public UUID worldID;
	public NBTTagCompound players;
	
	public MessageLMWorldUpdate() { }
	
	public MessageLMWorldUpdate(UUID id)
	{
		worldID = id;
		
		players = new NBTTagCompound();
		LMWorld.server.writePlayersToNet(players);
	}
	
	public void fromBytes(ByteBuf bb)
	{
		worldID = new UUID(bb.readLong(), bb.readLong());
		players = readTagCompound(bb);
	}
	
	public void toBytes(ByteBuf bb)
	{
		bb.writeLong(worldID.getMostSignificantBits());
		bb.writeLong(worldID.getLeastSignificantBits());
		writeTagCompound(bb, players);
	}
	
	public IMessage onMessage(MessageLMWorldUpdate m, MessageContext ctx)
	{ FTBU.proxy.handleClientMessage(m, ctx); return null; }
	
	@SideOnly(Side.CLIENT)
	public void onMessageClient(MessageLMWorldUpdate m, MessageContext ctx)
	{
		LMWorld.client = new LMWorldClient(m.worldID);
		LMWorld.client.readPlayersFromNet(m.players);
	}
}
package latmod.core.recipes;
import net.minecraft.item.ItemStack;

public interface IStackArray
{
	public boolean matches(ItemStack[] ai);
	public StackEntry[] getItems();
}
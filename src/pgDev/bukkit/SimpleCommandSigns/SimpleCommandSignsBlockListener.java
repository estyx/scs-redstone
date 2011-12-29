package pgDev.bukkit.SimpleCommandSigns;

import org.bukkit.block.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.*;


/**
 * SimpleCommandSigns block listener
 * @author DevilBoy
 */
public class SimpleCommandSignsBlockListener extends BlockListener {
    private final SimpleCommandSigns plugin;

    public SimpleCommandSignsBlockListener(final SimpleCommandSigns plugin) {
        this.plugin = plugin;
    }
    
    public void onSignChange(SignChangeEvent event) {
    	if ((event.getLine(0).equalsIgnoreCase(plugin.pluginSettings.commandSignIdentifier) ||
    		 event.getLine(0).equalsIgnoreCase(plugin.pluginSettings.commandSignDisabled)   ||
    		 event.getLine(0).equalsIgnoreCase(ChatColor.GREEN + plugin.pluginSettings.commandSignDisabled) ||
    		 event.getLine(0).equalsIgnoreCase(ChatColor.GREEN + plugin.pluginSettings.commandSignIdentifier)
    		) && plugin.hasPermissions(event.getPlayer(), "scs.create")) {
    		
			event.setLine(0, ChatColor.GREEN + plugin.pluginSettings.commandSignIdentifier);
			
			if (plugin.debug) { // Some debug code
				System.out.println("CommandSign created!");
    		}

			if(plugin.pluginSettings.signAutoLock && plugin.lwc != null && plugin.hasPermissions(event.getPlayer(), "scs.autolock")) {
				Block tS = event.getBlock();
				int blockId = tS.getTypeId();
				int type = 0;
				String world = tS.getWorld().getName();
				String owner = event.getPlayer().getName();
				String password = "";
				int x = tS.getX();
				int y = tS.getY();
				int z = tS.getZ();
				type = com.griefcraft.model.ProtectionTypes.PRIVATE;
				plugin.lwc.getPhysicalDatabase().registerProtection(blockId, type, world, owner, password, x, y, z);
				
				if (plugin.debug) { // Some debug code
					System.out.println("CommandSign locked!");
	    		}
			}
		}
    }
    
    public void onBlockRedstoneChange(BlockRedstoneEvent event)
	{
    	if(!this.plugin.isSign(event.getBlock()))
    		return;
    	
    	Sign signObject = (Sign) event.getBlock().getState();
    	
    	if (!signObject.getLine(0).equals(ChatColor.GREEN + plugin.pluginSettings.commandSignIdentifier) && !signObject.getLine(0).equals(ChatColor.GREEN + plugin.pluginSettings.commandSignDisabled))
    		return;
    	
    	if ((event.getBlock().isBlockPowered() || event.getBlock().isBlockIndirectlyPowered()) && signObject.getLine(0).equals(ChatColor.GREEN + plugin.pluginSettings.commandSignDisabled))
    	{
    		String dspCommand = signObject.getLine(1) + signObject.getLine(2) + signObject.getLine(3);
    		if(dspCommand.startsWith("/"))
    			dspCommand = dspCommand.substring(1);
    		
    		if(!plugin.pluginSettings.disableRedstoneCommand)
    			this.plugin.getServer().dispatchCommand(this.plugin.getServer().getConsoleSender(), dspCommand);
			
			signObject.setLine(0, ChatColor.GREEN + plugin.pluginSettings.commandSignIdentifier);
    	} else if(signObject.getLine(0).equals(ChatColor.GREEN + plugin.pluginSettings.commandSignIdentifier))
    	{
    		signObject.setLine(0, ChatColor.GREEN + plugin.pluginSettings.commandSignDisabled);
    	}
	}
    
    /* Can be used later on
    public void onBlockDamage(BlockDamageEvent event) {
    	Block currentBlock = event.getBlock();
    	if (isSign(currentBlock)) {
    		Sign theSign = (Sign)currentBlock.getState();
    		if (theSign.getLine(0).equals(ChatColor.GREEN + "[SCS]")) {
    			event.getPlayer().performCommand(theSign.getLine(1) + theSign.getLine(2) + theSign.getLine(3));
    		}
    	}
    }*/
    
}

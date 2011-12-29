package pgDev.bukkit.SimpleCommandSigns;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Handle events for all Player related events
 * @author DevilBoy
 */
public class SimpleCommandSignsPlayerListener extends PlayerListener {
    private final SimpleCommandSigns plugin;

    public SimpleCommandSignsPlayerListener(SimpleCommandSigns instance) {
        plugin = instance;
    }

    //Insert Player related code here
    public void onPlayerInteract(PlayerInteractEvent event) {
    	if (event.getAction() == Action.RIGHT_CLICK_BLOCK && plugin.hasPermissions(event.getPlayer(), "scs.use")) {
    		if (plugin.debug) { // Some debug code
				System.out.println(event.getPlayer().getName() + " right-clicked a block of type " + event.getClickedBlock().getType().toString() + "!");
    		}
    		
	    	if (event.getClickedBlock() != null && plugin.isSign(event.getClickedBlock())) {
	    		if (plugin.debug) { // Some debug code
					System.out.println("The block was a sign!");
	    		}
	    		
	    		Sign theSign = (Sign)event.getClickedBlock().getState();
	    		
	    		if (theSign.getLine(0).equals(ChatColor.GREEN + plugin.pluginSettings.commandSignIdentifier) ||(theSign.getLine(0).equals(ChatColor.GREEN + plugin.pluginSettings.commandSignDisabled) && plugin.pluginSettings.disablePlayerOnSignal == false )) {
	    			if (plugin.debug) { // Some debug code
	    				System.out.println("It was a CommandSign!");
	        		}
	    			
	    			String commandString = theSign.getLine(1) + theSign.getLine(2) + theSign.getLine(3);
	    			if (commandString.startsWith("/")) {
	    				commandString = commandString.substring(1);
	    			}
	    			event.getPlayer().performCommand(commandString.replace("%p", event.getPlayer().getName()));
	    		}
	    	}
    	}
    }
}


package pgDev.bukkit.SimpleCommandSigns;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import com.griefcraft.lwc.*;

/**
 * SimpleCommandSigns for Bukkit
 *
 * @author DevilBoy
 */
public class SimpleCommandSigns extends JavaPlugin {
	// Debugging
	boolean debug = false;
	
    // Listeners
	public final SimpleCommandSignsPlayerListener playerListener = new SimpleCommandSignsPlayerListener(this);
    public final SimpleCommandSignsBlockListener blockListener = new SimpleCommandSignsBlockListener(this);
    
    //public final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    
    // Plugin Integrations
    private static PermissionHandler Permissions;
    public LWC lwc = null;
    
    // File Locations
    String pluginMainDir = "./plugins/SimpleCommandSigns";
    String pluginConfigLocation = pluginMainDir + "/SimpleCommandSigns.cfg";
    
    // Plugin Configuration
    public SimpleCommandSignsConfig pluginSettings;

    public void onEnable() {
    	// Load the Configuration
    	try {
        	Properties preSettings = new Properties();
        	if ((new File(pluginConfigLocation)).exists()) {
        		preSettings.load(new FileInputStream(new File(pluginConfigLocation)));
        		pluginSettings = new SimpleCommandSignsConfig(preSettings, this);
        		if (!pluginSettings.upToDate) {
        			pluginSettings.createConfig();
        			System.out.println("SimpleCommandSigns Configuration updated!");
        		}
        	} else {
        		pluginSettings = new SimpleCommandSignsConfig(preSettings, this);
        		pluginSettings.createConfig();
        		System.out.println("SimpleCommandSigns Configuration created!");
        	}
        } catch (Exception e) {
        	System.out.println("Could not load configuration! " + e);
        }
    	
        // Register our events
        PluginManager pm = getServer().getPluginManager();
        //pm.registerEvent(Event.Type.BLOCK_PLACE, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.SIGN_CHANGE, blockListener, Priority.Normal, this);
        //pm.registerEvent(Event.Type.BLOCK_DAMAGE, blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Normal, this);
        if(this.pluginSettings.enableRedstoneSignal)
        	pm.registerEvent(Event.Type.REDSTONE_CHANGE, blockListener, Priority.Normal, this);
        // Permissions turn on!
    	setupPermissions();
    	
    	// LWC turn on!
    	Plugin lwcPlugin = getServer().getPluginManager().getPlugin("LWC");
    	if(lwcPlugin != null) {
	    	System.out.println("[SimpleCommandSigns] LWC plugin found!");
	    	lwc = ((LWCPlugin) lwcPlugin).getLWC();
    	}
        
        // Tell them we loaded up!
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
    }
    public void onDisable() {
    	System.out.println("SimpleCommandSigns is disabled!" );
    }
    
    /* Useless debug thing from the generator
    public boolean isDebugging(final Player player) {
        if (debugees.containsKey(player)) {
            return debugees.get(player);
        } else {
            return false;
        }
    }

    public void setDebugging(final Player player, final boolean value) {
        debugees.put(player, value);
    }*/
    
    // Permissions Methods
    private void setupPermissions() {
        Plugin permissions = this.getServer().getPluginManager().getPlugin("Permissions");

        if (Permissions == null) {
            if (permissions != null) {
                Permissions = ((Permissions)permissions).getHandler();
            } else {
            }
        }
    }
    
    public static boolean hasPermissions(Player player, String node) {
        if (Permissions != null) {
        	return Permissions.has(player, node);
        } else {
            return player.hasPermission(node);
        }
    }
    
    // Here's some functions I can use
    public boolean isSign(Block theBlock) {
    	if (theBlock.getType() == Material.SIGN || theBlock.getType() == Material.SIGN_POST || theBlock.getType() == Material.WALL_SIGN) {
    		return true;
    	} else {
    		return false;
    	}
    }
}


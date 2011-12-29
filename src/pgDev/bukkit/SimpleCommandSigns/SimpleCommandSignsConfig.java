package pgDev.bukkit.SimpleCommandSigns;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Properties;

public class SimpleCommandSignsConfig {
	private Properties properties;
	private final SimpleCommandSigns plugin;
	public boolean upToDate = true;
	
	// List of Config Options
	boolean signAutoLock;
	public String commandSignIdentifier;
	public String commandSignDisabled;
	public Boolean enableRedstoneSignal;
	public Boolean disablePlayerOnSignal;
	public Boolean disableRedstoneCommand;
	
	public SimpleCommandSignsConfig(Properties p, final SimpleCommandSigns plugin) {
        properties = p;
        this.plugin = plugin;
        
        // Grab values here.
        signAutoLock = getBoolean("autoSignLock", false);
        commandSignIdentifier  = getString("csActivator", "[SCS]").trim();
        commandSignDisabled    = getString("csDisabled",  "[SCSd]").trim();
        enableRedstoneSignal   = getBoolean("enableRedstoneSignal", false);
        disablePlayerOnSignal  = getBoolean("disablePlayerOnSignal", false);
        disableRedstoneCommand = getBoolean("disableRedstoneCommand", true);
	}
	
	
	// Value obtaining functions down below
	public int getInt(String label, int thedefault) {
		String value;
        try {
        	value = getString(label);
        	return Integer.parseInt(value);
        } catch (NoSuchElementException e) {
        	return thedefault;
        }
    }
    
    public double getDouble(String label) throws NoSuchElementException {
        String value = getString(label);
        return Double.parseDouble(value);
    }
    
    public File getFile(String label) throws NoSuchElementException {
        String value = getString(label);
        return new File(value);
    }

    public boolean getBoolean(String label, boolean thedefault) {
    	String values;
        try {
        	values = getString(label);
        	return Boolean.valueOf(values).booleanValue();
        } catch (NoSuchElementException e) {
        	return thedefault;
        }
    }
    
    public Color getColor(String label) {
        String value = getString(label);
        Color color = Color.decode(value);
        return color;
    }
    
    public HashSet<String> getSet(String label, String thedefault) {
        String values;
        try {
        	values = getString(label);
        } catch (NoSuchElementException e) {
        	values = thedefault;
        }
        String[] tokens = values.split(",");
        HashSet<String> set = new HashSet<String>();
        for (int i = 0; i < tokens.length; i++) {
            set.add(tokens[i].trim().toLowerCase());
        }
        return set;
    }
    
    public LinkedList<String> getList(String label, String thedefault) {
    	String values;
        try {
        	values = getString(label);
        } catch (NoSuchElementException e) {
        	values = thedefault;
        }
        if(!values.equals("")) {
            String[] tokens = values.split(",");
            LinkedList<String> set = new LinkedList<String>();
            for (int i = 0; i < tokens.length; i++) {
                set.add(tokens[i].trim().toLowerCase());
            }
            return set;
        }else {
        	return new LinkedList<String>();
        }
    }
    
    public String getString(String label) throws NoSuchElementException {
        String value = properties.getProperty(label);
        if (value == null) {
        	upToDate = false;
            throw new NoSuchElementException("Config did not contain: " + label);
        }
        return value;
    }
    
    public String getString(String label, String thedefault) {
    	String value;
    	try {
        	value = getString(label);
        } catch (NoSuchElementException e) {
        	value = thedefault;
        }
        return value;
    }
    
    public String linkedListToString(LinkedList<String> list) {
    	if(list.size() > 0) {
    		String compounded = "";
    		boolean first = true;
        	for (String value : list) {
        		if (first) {
        			compounded = value;
        			first = false;
        		} else {
        			compounded = compounded + "," + value;
        		}
        	}
        	return compounded;
    	}
    	return "";
    }
    
    
    // Config creation method
    public void createConfig() {
    	try{
    		File configfile = new File(plugin.pluginMainDir);
    		if(!configfile.exists()) {
    			configfile.mkdirs();
    		}
    		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(plugin.pluginConfigLocation)));
    		out.write("#\r\n");
    		out.write("# SimpleCommandSigns Configuration\r\n");
    		out.write("#\r\n");
    		out.write("\r\n");
    		out.write("# LWC Sign Automatic Locking\r\n");
    		out.write("#	If you have the LightWeight Chest plugin installed, you can\r\n");
    		out.write("#	have your command signs automatically protected after\r\n");
    		out.write("#	you have created them.\r\n");
    		out.write("autoSignLock=" + signAutoLock + "\r\n");
    		out.write("\r\n");
    		out.write("# Command Sign Identifier String\r\n");
    		out.write("#	This is the line that if you place at the top of your sign will\r\n");
    		out.write("#	convert it into a command sign.\r\n");
    		out.write("#	Warning: Changing this after having placed command signs\r\n");
    		out.write("#	will disable previous signs.\r\n");
    		out.write("csActivator=" + commandSignIdentifier + "\r\n");
    		out.write("csDisable=" + commandSignDisabled + "\r\n");
    		out.write("enableRedstoneSignal=" + enableRedstoneSignal + "\r\n");
    		out.write("disablePlayerOnSignal=" + disablePlayerOnSignal + "\r\n");
    		out.write("disableRedstoneCommand=" + disableRedstoneCommand + "\r\n");
    		out.close();
    	} catch (Exception e) {
    		System.out.println(e);
    		// Not sure what to do? O.o
    	}
    }
}

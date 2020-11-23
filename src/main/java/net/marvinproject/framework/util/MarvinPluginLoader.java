/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package net.marvinproject.framework.util;

import net.marvinproject.framework.MarvinDefinitions;
import net.marvinproject.framework.plugin.MarvinImagePlugin;
import net.marvinproject.framework.plugin.MarvinPlugin;

/**
 * Load plug-ins via MarvinJarLoader
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinPluginLoader {
	
//	/**
//	 * Loads a MarvinPluginImage via MarvinJarLoader
//	 * @param pluginPath		plug-in´s jar file path.
//	 * @return 					a loaded MarvinPluginImage.
//	 */
//	public static MarvinImagePlugin loadImagePlugin(String pluginPath){
//		MarvinImagePlugin l_plugin;
//		String l_className = pluginPath.replace(".jar", "");
//		if(l_className.lastIndexOf(".") != -1){
//			l_className = l_className.substring(l_className.lastIndexOf(".")+1);
//		}
//		l_className = l_className.substring(0,1).toUpperCase()+l_className.substring(1);
//		
//		l_plugin = (MarvinImagePlugin)loadPlugin(MarvinDefinitions.PLUGIN_IMAGE_PATH+pluginPath, l_className);
//		l_plugin.load();
//		return l_plugin;
//	}
//	
//	/**
//	 * Loads a MarvinPlugin via MarvinJarLoader
//	 * @param pluginPath	plug-in´s jar file path.
//	 * @param className		plug-in´s class name.
//	 * @return
//	 */
//	private static MarvinPlugin loadPlugin(String pluginPath, String className){
//		if(!pluginPath.substring(pluginPath.length()-4, pluginPath.length()).equals(".jar")){
//			pluginPath = pluginPath + ".jar";
//		}
//		
//		MarvinPlugin l_plugin;
//		MarvinJarLoader l_loader = new MarvinJarLoader(pluginPath);
//		l_plugin = (MarvinPlugin)l_loader.getObject(className);
//		return l_plugin;
//	}
}

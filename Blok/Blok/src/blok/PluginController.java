package blok;

import interfaces.ICore;
import blok.templates.IPlugin;
import interfaces.IPluginController;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import blok.templates.IUIFactory;
import java.util.jar.*;
import java.io.*;

class PluginController implements IPluginController{

    final ArrayList<IPlugin> pluginsList;
    final ArrayList<String> removePluginFromList;
    String[] allPlugins;
    URLClassLoader ulc;
    
    private ICore core;
    
    PluginController(ICore core) {
        this.core = core;
        this.removePluginFromList = new ArrayList<String>();
        this.pluginsList = new ArrayList<IPlugin>();
    }
            
    @Override
    public boolean initialize() {
        getAllLoadedPlugins(removePluginFromList);
        return true;
    }
        
    @Override
    public ArrayList<String> getName(ArrayList<IPlugin> plugins) {
        ArrayList<String> pluginName = new ArrayList<>();
        for(int i = 0; i < plugins.size(); i++) {
            pluginName.add(plugins.get(i).getClass().getSimpleName());
        }
        return pluginName;
    }
    
    @Override
    public void getAllLoadedPlugins(ArrayList<String> removeList) { //PhysicsSimulator
        removePluginList("PhysicsSimulator");
        File currentDir = new File("plugins/");
        allPlugins = currentDir.list();
        ArrayList<String> pluginsJars = new ArrayList<>();
        for(String pluginName : allPlugins) {
            for(String removePlugin : removePluginFromList) {
                if(!pluginName.contains(removePlugin)) {
                    pluginsJars.add(pluginName);
                } 
            }
        }
        int i;
        URL[] jars = new URL[pluginsJars.size()];
        for (i = 0; i < pluginsJars.size(); i++){
            try {
                jars[i] = (new File("plugins/" + pluginsJars.get(i))).toURI().toURL();
            } catch (MalformedURLException ex) {
                Logger.getLogger(PluginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        ulc = new URLClassLoader(jars);
        
        try {
            this.pluginsList.clear();
            for (int count = 0; count < pluginsJars.size(); count++) {
                String factoryName = pluginsJars.get(count).split("\\.")[0];
                IPlugin factory = (IPlugin) Class.forName(factoryName.toLowerCase() + "." + factoryName, true, ulc).newInstance();
                pluginsList.add((IPlugin) factory);
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
            Logger.getLogger(PluginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public ArrayList<IPlugin> getLoadedPluginsByType(Class cls) throws InstantiationException, IllegalAccessException {
        ArrayList<IPlugin> pluginsByType = new ArrayList<>();
        for (IPlugin plugin : this.pluginsList) {
            if(cls.isAssignableFrom(plugin.getClass())){
                pluginsByType.add(plugin);
            }
        }
        return pluginsByType;
    }
    
    @Override
    public void setCurrentPlugins(int UIChoose, String physicsChoose) {
        try {
            core.getGameController().setCurrentUIFactory((IUIFactory) getLoadedPluginsByType(IUIFactory.class).get(UIChoose));
            core.getGameController().setCurrentPhysicsFactory(physicsChoose);
        }catch (IllegalAccessException | InstantiationException ex) {
            Logger.getLogger(PluginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public ArrayList<String> getClasseNamesInPackage(String url, String packageName) {       
        try{ //String jarName, String packageName
            ArrayList<String> classesFullName = new ArrayList<String>();
            ArrayList<String> classesFinal = new ArrayList<String>();
            String simulatorURL = new File(url).getCanonicalPath();

            try{
                JarInputStream jarFile = new JarInputStream(new FileInputStream(simulatorURL));
                JarEntry jarEntry;

                while(true) {
                    jarEntry = jarFile.getNextJarEntry();
                    if(jarEntry == null){
                        break;
                    }
                    if((jarEntry.getName().startsWith(packageName)) &&
                            (jarEntry.getName().endsWith(".class")) ) {
                        classesFullName.add(jarEntry.getName().replaceAll("/", "\\."));
                    }
                }
                for(int i = 0; i < classesFullName.size(); i++) {
                    if(!classesFullName.get(i).contains("Simulator")) {
                        classesFinal.add(classesFullName.get(i).split("\\.")[1]);
                    }
                }
                return classesFinal;
            }
            catch(Exception e) {
                e.printStackTrace ();
            }
        }
        catch(IOException ex) {
              Logger.getLogger(PluginController.class.getName()).log (Level.SEVERE, null, ex);
        }
        return null;
   }

    @Override
    public void removePluginList(String pluginName) {
        removePluginFromList.add(pluginName);
    }
}

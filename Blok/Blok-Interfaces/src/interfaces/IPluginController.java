package interfaces;

import blok.templates.IPlugin;
import java.util.ArrayList;

public interface IPluginController {
    public boolean initialize();
    public ArrayList<String> getName(ArrayList<IPlugin> plugins);
    public void setCurrentPlugins(int UIChoose, String physicsChoose);
    public void getAllLoadedPlugins(ArrayList<String> removeList);
    public void removePluginList(String pluginName);
    public ArrayList<IPlugin> getLoadedPluginsByType(Class cls) throws InstantiationException, IllegalAccessException;
    public ArrayList<String> getClasseNamesInPackage(String url, String packageName);
}

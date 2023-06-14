package gla.kassad.complexturretsreachzone;

import gla.kassad.complexturretsreachzone.listener.TurretRightClick;
import gla.kassad.complexturretsreachzone.sphere.Radius;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class ComplexTurretsReachZone extends JavaPlugin {

    private Radius radius;

    @Override
    public void onEnable() {
        // Plugin startup logic

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        this.radius = new Radius(Radius.loadRadii());
        importRadiiOnLoad();

        getCommand("ctrz").setExecutor(new MainCommand());
        getCommand("ctrz").setTabCompleter(new MainCommand());

        getServer().getPluginManager().registerEvents(new TurretRightClick(), this);

        configUpdate();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Radius getRadius()
    {
        return radius;
    }

    private void importRadiiOnLoad()
    {
        if (!new File(this.getDataFolder(), "data.yml").exists())
        {
            MainCommand.importRadii(null);
        }
    }

    private void configUpdate()
    {
        int config_version = getConfig().getInt("config_version");
        if (config_version < 3)
        {
            // do some
        }
    }
}

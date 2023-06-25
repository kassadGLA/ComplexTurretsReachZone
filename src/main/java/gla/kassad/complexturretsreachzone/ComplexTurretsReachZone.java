package gla.kassad.complexturretsreachzone;

import gla.kassad.complexturretsreachzone.data.Radius;
import gla.kassad.complexturretsreachzone.listener.TurretRightClick;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class ComplexTurretsReachZone extends JavaPlugin {

    private Radius radius;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        this.radius = new Radius(Radius.loadRadii());
        getCommand("ctrz").setExecutor(new MainCommand());
        getCommand("ctrz").setTabCompleter(new MainCommand());
        getServer().getPluginManager().registerEvents(new TurretRightClick(), this);
        importRadiiOnLoad();
        configUpdate();
    }

    @Override
    public void onDisable() {
        // Add some msg
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
        if (config_version < 4)
        {
            getConfig().set("config.sphere.calculation.ad_factor", 20);
            getConfig().set("config.sphere.calculation.rad_factor", 2);
            getConfig().set("config.sphere.display.frequency", 20);
            getConfig().set("config.sphere.display.duration", 400);
            getConfig().set("config.threads.particles_per_thread", 2147483647);
            getConfig().set("config.threads.calculation_info", false);
            getConfig().set("config_version", 4);
            saveConfig();
        }
    }
}

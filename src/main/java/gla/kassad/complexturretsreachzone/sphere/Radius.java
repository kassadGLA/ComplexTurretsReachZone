package gla.kassad.complexturretsreachzone.sphere;

import gla.kassad.complexturretsreachzone.ComplexTurretsReachZone;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Radius
{
    private static final ComplexTurretsReachZone plugin = ComplexTurretsReachZone.getPlugin(ComplexTurretsReachZone.class);
    private FileConfiguration radii;

    public Radius(FileConfiguration radii)
    {
        this.radii = radii;
    }

    public FileConfiguration getRadii()
    {
        return radii;
    }

    public void setRadii(FileConfiguration radii)
    {
        this.radii = radii;
    }

    public static FileConfiguration loadRadii()
    {
        File path = new File(plugin.getDataFolder(), "data.yml");
        return YamlConfiguration.loadConfiguration(path);
    }
}

package gla.kassad.complexturretsreachzone.listener;

import com.google.common.collect.Lists;
import ct.ajneb97.api.TurretRightClickEvent;
import gla.kassad.complexturretsreachzone.ComplexTurretsReachZone;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class TurretRightClick implements Listener
{
    private static final ComplexTurretsReachZone plugin = ComplexTurretsReachZone.getPlugin(ComplexTurretsReachZone.class);
    private static Set<String> activeSpheres = new HashSet<>();

    @EventHandler
    public void turretClick(TurretRightClickEvent event)
    {
        String turretID = (event.getPlayerTurret().getLocation().getWorld().getName() +
                event.getPlayerTurret().getLocation().getX() +
                event.getPlayerTurret().getLocation().getY() +
                event.getPlayerTurret().getLocation().getZ());
        if (!activeSpheres.contains(turretID))
        {
            Player player = event.getPlayer();
            if (player.getUniqueId().toString().equals(event.getPlayerTurret().getOwnerUUID()))
            {
                activeSpheres.add(turretID);
                String turretType = event.getPlayerTurret().getTurretName();
                int level = event.getPlayerTurret().getTurretLevel();
                double radius;
                if (plugin.getRadius().getRadii().contains(turretType + "." + level))
                {
                    radius = plugin.getRadius().getRadii().getDouble(turretType + "." + level);
                }
                else
                {
                    activeSpheres.remove(turretID);
                    return;
                }
                World world = event.getPlayerTurret().getLocation().getWorld();
                double xC = event.getPlayerTurret().getLocation().getX() + 0.5;
                double yC = event.getPlayerTurret().getLocation().getY() + 2.3;
                double zC = event.getPlayerTurret().getLocation().getZ() + 0.5;
                Particle particle = Particle.valueOf(plugin.getConfig().getString("config.particle.particle_type").toUpperCase());
                Particle.DustOptions data;
                if ("REDSTONE".equals(plugin.getConfig().getString("config.particle.particle_type").toUpperCase()))
                {
                    int r = setRGB(plugin.getConfig().getInt("config.particle.particle_data.color.r"));
                    int g = setRGB(plugin.getConfig().getInt("config.particle.particle_data.color.g"));
                    int b = setRGB(plugin.getConfig().getInt("config.particle.particle_data.color.b"));
                    Color color = Color.RED;
                    color = color.setRed(r);
                    color = color.setGreen(g);
                    color = color.setBlue(b);
                    data = new Particle.DustOptions(color, (float) plugin.getConfig().getDouble("config.particle.particle_data.size"));
                }
                else
                {
                    data = null;
                }
                new BukkitRunnable()
                {
                    int i = 20;

                    @Override
                    public void run()
                    {
                        List<Location> sphere = calculation(player, radius, world, xC, yC, zC);
                        List<List<Location>> sphereLists = Lists.partition(sphere, 10000);
                        for (List<Location> list : sphereLists)
                        {
                            new BukkitRunnable()
                            {
                                @Override
                                public void run()
                                {
                                    for (Location spherePoint : list)
                                    {
                                        spawnParticle(player, spherePoint, particle, data);
                                    }
                                }
                            }.runTaskAsynchronously(plugin);
                        }

                        if (i == 0)
                        {
                            activeSpheres.remove(turretID);
                            this.cancel();
                        }
                        i--;
                    }
                }.runTaskTimerAsynchronously(plugin, 0L, 20L);
            }
        }
    }

    public static List<Location> calculation(Player player, double radius, World world, double xC, double yC, double zC)
    {
        Map<String, Location> map = new HashMap<>();
        String mapKey;
        double pi = 3.14D;
        double angleDivider = radius * 20;
        double rotationAngleDivider = radius * 1;

        double x;
        double y;
        double z = 0;
        double xR;
        double zR;
        double angle = 0;
        double rotationAngle;
        while (angle < pi * 2)
        {
            x = rounder(Math.sin(angle) * radius);
            y = rounder(Math.cos(angle) * radius);
            mapKey = "_" + (x + xC) + (y + yC) + (z + zC);
            map.put(mapKey, locationConstructor(world, x + xC, y + yC, z + zC));
            rotationAngle = pi / rotationAngleDivider;
            while (rotationAngle < pi)
            {
                xR = rounder(Math.cos(rotationAngle) * x);
                zR = rounder(Math.sin(rotationAngle) * x * -1);
                mapKey = "_" + (xR + xC) + (y + yC) + (zR + zC);
                map.put(mapKey, locationConstructor(world, xR + xC, y + yC, zR + zC));
                rotationAngle += pi / rotationAngleDivider;
            }
            angle += pi / angleDivider;
        }
        rotationAngle = 0;
        while (rotationAngle <= pi)
        {
            angle = 0;
            while (angle < pi * 2)
            {
                x = rounder(Math.sin(rotationAngle) * Math.sin(angle) * radius);
                y = rounder(Math.cos(rotationAngle) * radius);
                z = rounder(Math.sin(rotationAngle) * Math.cos(angle) * radius);
                mapKey = "_" + (x + xC) + (y + yC) + (z + zC);
                map.put(mapKey, locationConstructor(world, x + xC, y + yC, z + zC));
                angle += pi / angleDivider;
            }
            rotationAngle += pi / rotationAngleDivider;
        }
        return new ArrayList<>(map.values());
    }

    private static double rounder(double d)
    {
        return (double) Math.round(d * 10) / 10;
    }

    private static Location locationConstructor(World world, double x, double y, double z)
    {
        return new Location(world, x, y, z);
    }

    private static int setRGB(int i)
    {
        if (i<0){
            i=0;
        }
        if(i>255)
        {
            i=255;
        }
        return i;
    }

    private static void spawnParticle(Player player, Location location, Particle particle, Particle.DustOptions data)
    {
        if (data == null)
        {
            player.spawnParticle(particle, location, 1);
        }
        else
        {
            player.spawnParticle(particle, location, 1, data);
        }
    }
}

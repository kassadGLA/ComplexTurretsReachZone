package gla.kassad.complexturretsreachzone;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MainCommand implements CommandExecutor, TabCompleter
{
    private static final ComplexTurretsReachZone plugin = ComplexTurretsReachZone.getPlugin(ComplexTurretsReachZone.class);
    private static final List<String> ARGS = new ArrayList<>(Arrays.asList("reimport", "reload"));
    private static boolean isImporting;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args)
    {
        if (command.getName().equalsIgnoreCase("ctrz"))
        {
            if (sender instanceof Player)
            {
                Player player = (Player) sender;
                if (player.hasPermission("complexturretsreachzone.admin") || player.isOp())
                {
                    perform(args, player);
                }
                else
                {
                    sendMessage(player, "no_permission");
                    return false;
                }
            }
            else
            {
                perform(args, null);
            }
        }
        return true;
    }

    private static void perform(String[] a, Player player)
    {
        if (a.length == 0)
        {
            pluginInfo(player);
        }
        if (a.length == 1)
        {
            if ("reimport".equalsIgnoreCase(a[0]))
            {
                importRadii(player);
            }
            else if ("reload".equalsIgnoreCase(a[0]))
            {
                plugin.reloadConfig();
                sendMessage(player, "reload");
            }
            else
            {
                sendMessage(player, "invalid_argument " + a[0]);
            }
        }
        if (a.length > 1)
        {
            sendMessage(player, "invalid_number_args");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        Player player = (Player) sender;
        if (player.hasPermission("complexturretsreachzone.admin") || player.isOp())
        {
            if (args.length == 1)
            {
                List<String> completions = new ArrayList<>();
                StringUtil.copyPartialMatches(args[0], ARGS, completions);
                return completions;
            }
        }
        return null;
    }

    static void importRadii(Player player)
    {
        if (!isImporting)
        {
            isImporting = true;
            // message
        }
        else
        {
            // message
            return;
        }
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                FileConfiguration radii = new YamlConfiguration();
                File path = new File(plugin.getDataFolder(), "data.yml");
                Map<String, FileConfiguration> turretConfigs = load_ctTurretsYaml();
                String turretType;
                Set<String> levels;
                double radius;
                for (Map.Entry<String, FileConfiguration> keyPair : turretConfigs.entrySet())
                {
                    turretType = keyPair.getKey();
                    levels = keyPair.getValue().getConfigurationSection("turret.upgrades").getKeys(false);
                    for (String lvl : levels)
                    {
                        radius = keyPair.getValue().getDouble("turret.upgrades." + lvl + ".range");
                        radii.set(turretType + "." + lvl, radius);
                    }
                }
                plugin.getRadius().setRadii(radii);
                try
                {
                    radii.save(path);
                } catch (IOException e)
                {
                    throw new RuntimeException(e);
                }

                isImporting = false;
                sendMessage(player, "import");
            }
        }.runTaskAsynchronously(plugin);

    }

    private static Map<String, FileConfiguration> load_ctTurretsYaml()
    {
        File path = new File(Bukkit.getServer().getPluginManager().getPlugin("ComplexTurrets").getDataFolder(), "turrets");
        FileConfiguration turretConfig;
        Map<String, FileConfiguration> mapYaml = new HashMap<>();
        String[] ctYaml = path.list((turretsConfigsPath, fileName) -> fileName.toLowerCase().endsWith(".yml"));
        for (String turretFileName : ctYaml)
        {
            turretConfig = YamlConfiguration.loadConfiguration(new File(path, "/" + turretFileName));
            mapYaml.put(turretFileName.substring(0, turretFileName.length() - 4), turretConfig);
        }
        return mapYaml;
    }

    private static void sendMessage(Player player, String key)
    {
        String invalidArg = "";
        if (key.contains("invalid_argument"))
        {
            String[] key_arg = key.split(" ");
            invalidArg = key_arg[1];
            key = key_arg[0];
        }
        String prefix = plugin.getConfig().getString("messages.prefix");
        String message = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("messages." + key).replace("%prefix%", prefix)).replace("%arg%", invalidArg);
        if (!(player == null))
        {
            player.sendMessage(message);
        }
        else
        {
            plugin.getServer().getConsoleSender().sendMessage(message);
        }
    }

    private static void pluginInfo(Player player)
    {
        String ver = plugin.getDescription().getVersion();
        String message = "\n&7┏\n&7┃ &5ComplexTurretsReachZone\n&7┃   &bversion: &6" +ver + "\n&7┃   &bauthor: &6kassad\n&7┗";
        message = ChatColor.translateAlternateColorCodes('&', message);
        if (!(player == null))
        {
            player.sendMessage(message);
        }
        else
        {
            plugin.getServer().getConsoleSender().sendMessage(message);
        }
    }
}

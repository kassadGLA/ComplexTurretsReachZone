package gla.kassad.complexturretsreachzone.listener;

import gla.kassad.complexturretsreachzone.ComplexTurretsReachZone;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener
{
    private ComplexTurretsReachZone plugin;

    public PlayerJoin(ComplexTurretsReachZone plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        plugin.getPlayers().getPlayersOnline().put(uuid, player);
    }
}

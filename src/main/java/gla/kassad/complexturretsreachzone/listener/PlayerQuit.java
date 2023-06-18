package gla.kassad.complexturretsreachzone.listener;

import gla.kassad.complexturretsreachzone.ComplexTurretsReachZone;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener
{
    private ComplexTurretsReachZone plugin;

    public PlayerQuit(ComplexTurretsReachZone plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event)
    {
        String uuid = event.getPlayer().getUniqueId().toString();
        plugin.getPlayers().getPlayersOnline().remove(uuid);
    }
}

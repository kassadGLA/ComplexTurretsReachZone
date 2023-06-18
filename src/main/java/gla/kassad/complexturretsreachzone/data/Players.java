package gla.kassad.complexturretsreachzone.data;

import org.bukkit.entity.Player;

import java.util.Map;

public class Players
{
    private Map<String, Player> playersOnline;

    public Players(Map<String, Player> playersOnline)
    {
        this.playersOnline = playersOnline;
    }

    public Map<String, Player> getPlayersOnline()
    {
        return playersOnline;
    }
}

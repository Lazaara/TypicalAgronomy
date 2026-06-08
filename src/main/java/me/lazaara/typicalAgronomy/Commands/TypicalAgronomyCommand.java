package me.lazaara.typicalAgronomy.Commands;

import me.lazaara.typicalAgronomy.Listeners.ItemMenuListener;
import me.lazaara.typicalAgronomy.TypicalAgronomy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class TypicalAgronomyCommand implements CommandExecutor, TabCompleter {

    private final TypicalAgronomy plugin = TypicalAgronomy.getPlugin(TypicalAgronomy.class);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(plugin.getLang("messages.typicalagronomy.usage"));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {

            if (!sender.hasPermission("typicalAgronomy.reload")) {
                sender.sendMessage(plugin.getLang("messages.reload.no-permission"));
                return true;
            }

            plugin.reload();
            sender.sendMessage(plugin.getLang("messages.reload.success"));
            return true;

        }

        if (args[0].equalsIgnoreCase("items")) {

            if (!(sender instanceof Player player)) {
                sender.sendMessage(plugin.getLang("messages.items.player-only"));
                return true;
            }

            if (!player.hasPermission("typicalAgronomy.items")) {
                player.sendMessage(plugin.getLang("messages.items.no-permission"));
                return true;
            }

            ItemMenuListener.openCategoryMenu(player);
            return true;

        }

        sender.sendMessage(plugin.getLang("messages.typicalagronomy.usage"));
        return true;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 1) {
            return List.of("items", "reload");
        }

        return List.of();

    }
}

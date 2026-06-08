package me.lazaara.typicalAgronomy.Commands;

import me.lazaara.typicalAgronomy.Listeners.ItemMenuListener;
import me.lazaara.typicalAgronomy.Managers.ItemManager;
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
                sender.sendMessage(plugin.getLang("messages.getitem.player-only"));
                return true;
            }

            if (!player.hasPermission("typicalAgronomy.items")) {
                player.sendMessage(plugin.getLang("messages.getitem.no-permission"));
                return true;
            }

            ItemMenuListener.openCategoryMenu(player);
            return true;

        }

        if (args[0].equalsIgnoreCase("getitem")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getLang("messages.getitem.player-only"));
                return true;
            }

            Player player = (Player) sender;

            if (!player.hasPermission("typicalAgronomy.getitem")) {
                player.sendMessage(plugin.getLang("messages.getitem.no-permission"));
                return true;
            }

            if (args.length < 2) {
                player.sendMessage(plugin.getLang("messages.getitem.usage"));
                return true;
            }

            try {

                int itemID = Integer.parseInt(args[1]);

                if (!ItemManager.itemList.containsKey(itemID)) {
                    player.sendMessage(plugin.getLang("messages.getitem.not-found"));
                    return true;
                }

                ItemManager.GiveItem(itemID, player);

            } catch (NumberFormatException e) {

                player.sendMessage(plugin.getLang("messages.getitem.invalid-id"));

            }

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

        if (args.length == 2 && args[0].equalsIgnoreCase("getitem")) {
            return ItemManager.itemList.keySet().stream()
                    .map(String::valueOf)
                    .collect(java.util.stream.Collectors.toList());
        }

        return List.of();

    }
}

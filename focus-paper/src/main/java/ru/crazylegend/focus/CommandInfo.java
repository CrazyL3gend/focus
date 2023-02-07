package ru.crazylegend.focus;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import ru.crazylegend.focus.command.argument.CommandArguments;
import ru.crazylegend.focus.command.preset.standalone.PermissibleCommand;
import ru.crazylegend.focus.configuration.Messages;
import ru.crazylegend.focus.cooldown.preset.LitePlayersCooldownStorage;
import ru.crazylegend.focus.cooldown.preset.PlayersCooldownStorage;
import ru.crazylegend.focus.util.component.injection.TextComponentInjector;
import ru.crazylegend.focus.util.component.replacement.Replacements;
import ru.crazylegend.focus.util.component.replacement.TextComponentReplacement;
import ru.crazylegend.focus.util.format.DateFormatter;

public class CommandInfo extends PermissibleCommand {

    private final PluginDescriptionFile description;
    private final PlayersCooldownStorage cooldownStorage;
    private final DateFormatter dateFormatter;

    public CommandInfo(Bootstrap plugin, Messages messages) {
        super("focus", "focus.command.info", messages);

        this.description = plugin.getDescription();
        this.cooldownStorage = new LitePlayersCooldownStorage(60);
        this.dateFormatter = new DateFormatter();

        super.register(plugin);
    }

    @Override
    public void executeCommand(CommandSender sender, CommandArguments args) {
        String name = sender.getName();

        // Checks if sender has cooldown or not
        if (cooldownStorage.hasCooldown(name)) {
            long remained = cooldownStorage.getRemainedTime(name);

            if (remained != 0) {
                // Displaying remained time
                String formatted = dateFormatter.format(remained / 1000);
                sender.sendMessage(ChatColor.RED + "You will can use that after " + formatted + ".");
                return;
                // Removes useless nullified cooldown
            } else {
                cooldownStorage.removeCooldown(name);
            }
        }

        // Resets player's cooldown
        cooldownStorage.refreshResetDate(name);

        String pversion = description.getVersion();

        sender.sendMessage(ChatColor.GRAY + "   SoKnight's library information");
        sender.sendMessage(" Plugin version: " + ChatColor.AQUA + pversion);
        sender.sendMessage(" Author & Developer: " + ChatColor.AQUA + "SoKnight");

        String text = " Github: " + ChatColor.AQUA + "Click %link% to open";

        TextComponentReplacement replacement = Replacements.component("%link%", "[here]")
                .onClickOpenUrl("https://github.com/SoKnight/SKLibrary")
                .onHover(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("&aYou will open a link to my github in your browser. &a&lClick!")
                                .create()
                );

        TextComponent component = TextComponentInjector.inject(text, replacement);
        sender.spigot().sendMessage(component);

        sender.sendMessage(" ");
    }

}

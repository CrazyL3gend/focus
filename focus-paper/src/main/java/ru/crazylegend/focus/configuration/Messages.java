package ru.crazylegend.focus.configuration;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.crazylegend.focus.util.Title;
import ru.crazylegend.focus.util.component.replacement.Replacements;
import ru.crazylegend.focus.util.component.replacement.TextComponentReplacement;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * Configuration of messages file which contains methods for working with plugin messages
 */
public class Messages extends AbstractConfiguration {

    public static final String DISABLED_MESSAGE_MARKER = "@#DISABLED";

    /**
     * Create a new messages instance using default data folder path and configuration resource.
     *
     * @param plugin   The plugin that will use this configuration.
     * @param fileName The configuration file name.
     */
    public Messages(
            @NotNull JavaPlugin plugin,
            @NotNull String fileName
    ) {
        super(plugin, fileName);
    }

    /**
     * Create a new messages instance using default data folder path.
     *
     * @param plugin   The plugin that will use this configuration.
     * @param fileName The configuration file name.
     * @param resource The configuration resource in the plugin JAR.
     */
    public Messages(
            @NotNull JavaPlugin plugin,
            @NotNull String fileName,
            @NotNull InputStream resource
    ) {
        super(plugin, fileName, resource);
    }

    /**
     * Create a new messages instance using all custom values.
     *
     * @param plugin         The plugin that will use this configuration.
     * @param fileName       The configuration file name.
     * @param dataFolderPath The path to data folder.
     * @param resource       The configuration resource in the plugin JAR.
     */
    public Messages(
            @NotNull JavaPlugin plugin,
            @NotNull String fileName,
            @NotNull Path dataFolderPath,
            @NotNull InputStream resource
    ) {
        super(plugin, fileName, dataFolderPath, resource);
    }

    /**
     * @deprecated Use {@link #Messages(JavaPlugin, String, InputStream)} instead.
     */
    @Deprecated
    public Messages(JavaPlugin plugin, InputStream resource, String fileName) {
        this(plugin, fileName, resource);
    }

    /**
     * @deprecated Use {@link #Messages(JavaPlugin, String, Path, InputStream)} instead.
     */
    @Deprecated
    public Messages(JavaPlugin plugin, File dataFolder, InputStream resource, String fileName) {
        this(plugin, fileName, dataFolder.toPath(), resource);
    }

    /**
     * Getting colored message from file
     *
     * @param section Section with target message in file
     * @return Formatted string with replaced placeholders or message about not exist message
     */
    public String get(String section) {
        return getOrDefault(section, "Couldn't get message from section '" + section + "' :(");
    }

    /**
     * Getting colored message from file
     *
     * @param section Section with target message in file
     * @param def     Default value to return if specified section will not be found in the file
     * @return Formatted string with replaced placeholders or message about not exist message
     */
    public String getOrDefault(String section, String def) {
        return getColoredString(section, def);
    }

    /**
     * Getting colored message component from file
     *
     * @param section Section with target message component in file
     * @return Formatted component with replaced placeholders or message about not exist message
     */
    public TextComponent getComponent(String section) {
        return getComponent(section, "Couldn't get message component from section '" + section + "' :(");
    }

    /**
     * Getting colored message component from file
     *
     * @param section Section with target message component in file
     * @param def     Default value to return if specified section will not be found in the file
     * @return Formatted component with replaced placeholders or message about not exist message
     */
    public TextComponent getComponent(String section, String def) {
        return new TextComponent(getColoredString(section, def));
    }

    /**
     * Get the title configuration by specified path.
     *
     * @param path The path to title configuration.
     * @return A parsed title configuration (nullable).
     */
    public @Nullable Title getTitle(@NotNull String path) {
        if (!isTitle(path))
            return null;

        String title = getColoredString(path + ".title");
        String subtitle = getColoredString(path + ".subtitle");
        int fadeInTicks = getInt(path + ".fade-in", 0);
        int stayTicks = getInt(path + ".stay", 0);
        int fadeOutTicks = getInt(path + ".fade-out", 0);

        return new Title(title, subtitle, fadeInTicks, stayTicks, fadeOutTicks);
    }

    /**
     * Formatting exist message from configuration by section key
     *
     * @param section      Section with target message in file
     * @param replacements Array of string with this syntax: placeholder, value, placeholder, value...
     * @return Formatted string with replaced placeholders or message about not exist message
     */
    public String getFormatted(String section, Object... replacements) {
        return getFormattedOrDefault(section, "Couldn't get message from section '" + section + "' :(", replacements);
    }

    /**
     * Formatting exist message from configuration by section key
     *
     * @param section      Section with target message in file
     * @param def          Default value to return if specified section will not be found in the file
     * @param replacements Array of string with this syntax: placeholder, value, placeholder, value...
     * @return Formatted string with replaced placeholders or message about not exist message
     */
    public String getFormattedOrDefault(String section, String def, Object... replacements) {
        String message = getColoredString(section);
        return message != null ? format(message, replacements) : def;
    }

    /**
     * Formatting exist message component from configuration by section key
     *
     * @param section      Section with target message in file
     * @param replacements Array of string & component with this syntax: 'placeholder1 value1 placeholder2 value2'...
     * @return Formatted component with replaced placeholders or message about not exist message
     */
    public TextComponent getComponentFormatted(String section, Object... replacements) {
        String message = getFormatted(section, replacements);
        return new TextComponent(message);
    }

    /**
     * Get the title configuration by specified path with formatted texts.
     *
     * @param path         The path to title configuration.
     * @param replacements The replacements array with syntax: 'placeholder1, value1, placeholder2, value2'...
     * @return A parsed title configuration (nullable).
     */
    public @Nullable Title getTitleFormatted(@NotNull String path, Object... replacements) {
        if (!isTitle(path))
            return null;

        String title = format(getColoredString(path + ".title"), replacements);
        String subtitle = format(getColoredString(path + ".subtitle"), replacements);
        int fadeInTicks = getInt(path + ".fade-in", 0);
        int stayTicks = getInt(path + ".stay", 0);
        int fadeOutTicks = getInt(path + ".fade-out", 0);

        return new Title(title, subtitle, fadeInTicks, stayTicks, fadeOutTicks);
    }

    /**
     * Parsing a section in specified path as TextComponent button with 'text', 'hover' and 'command' params
     *
     * @param path         The path to button section with children keys 'text', 'hover' and 'command'
     * @param replacements Array of string with this syntax: 'placeholder1 value1 placeholder2 value2'...
     * @return Parsed button as {@link TextComponent} instance
     */
    @SuppressWarnings("deprecation")
    public TextComponent parseButtonAsComponent(String path, Object... replacements) {
        TextComponent component = getComponentFormatted(path + ".text", replacements);

        String hoverText = getFormattedOrDefault(path + ".hover", null, replacements);
        if (hoverText != null && !hoverText.isEmpty()) {
            BaseComponent[] hoverComponent = TextComponent.fromLegacyText(hoverText);
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverComponent));
        }

        String command = getFormattedOrDefault(path + ".command", null, replacements);
        if (command != null && !command.isEmpty())
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));

        return component;
    }

    /**
     * Parsing a section in specified path as TextComponentReplacement button with 'text', 'hover' and 'command' params
     *
     * @param path         The path to button section with children keys 'text', 'hover' and 'command'
     * @param replacements Array of string with this syntax: 'placeholder1 value1 placeholder2 value2'...
     * @return Parsed button as {@link TextComponentReplacement} instance
     */
    public TextComponentReplacement parseButtonAsReplacement(String path, String placeholder, Object... replacements) {
        TextComponent component = parseButtonAsComponent(path, replacements);
        return Replacements.component(placeholder, component);
    }

    /**
     * Sending message to sender
     *
     * @param sender  Message receiver
     * @param message Message which will be sent
     */
    public void send(CommandSender sender, String message) {
        send(sender, message, false);
    }

    /**
     * Sending message component to sender
     *
     * @param sender  Message receiver
     * @param message Message component which will be sent
     */
    public void send(CommandSender sender, TextComponent message) {
        send(sender, message, false);
    }

    /**
     * Sending messages array to sender
     *
     * @param sender   Message receiver
     * @param messages Messages array which will be sent
     */
    public void send(CommandSender sender, String[] messages) {
        send(sender, messages, false);
    }

    /**
     * Sending messages list to sender
     *
     * @param sender   Message receiver
     * @param messages Messages list which will be sent
     */
    public void send(CommandSender sender, List<String> messages) {
        send(sender, messages.toArray(new String[0]), false);
    }

    /**
     * Sending message to sender
     *
     * @param sender      Message receiver
     * @param message     Message which will be sent
     * @param toActionbar Should method to send this message into player's actionbar if it's possible
     */
    public void send(CommandSender sender, String message, boolean toActionbar) {
        if (sender == null || message == null || isIgnoredMessage(message)) return;

        if (toActionbar && sender instanceof Player) {
            BaseComponent[] msg = TextComponent.fromLegacyText(message);
            ((Player) sender).spigot().sendMessage(ChatMessageType.ACTION_BAR, msg);
        } else {
            sender.sendMessage(message);
        }
    }

    /**
     * Sending message component to sender
     *
     * @param sender      Message receiver
     * @param message     Message component which will be sent
     * @param toActionbar Should method to send this message component into player's actionbar if it's possible
     */
    public void send(CommandSender sender, TextComponent message, boolean toActionbar) {
        if (sender == null || message == null || isIgnoredMessage(message.toPlainText())) return;

        if (toActionbar && sender instanceof Player)
            ((Player) sender).spigot().sendMessage(ChatMessageType.ACTION_BAR, message);
        else
            sender.spigot().sendMessage(message);
    }

    /**
     * Sending messages array to sender
     *
     * @param sender      Message receiver
     * @param messages    Messages array which will be sent
     * @param toActionbar Should method to send this messages into player's actionbar if it's possible
     */
    public void send(CommandSender sender, String[] messages, boolean toActionbar) {
        if (sender == null || messages == null || messages.length == 0) return;

        if (toActionbar && sender instanceof Player) {
            Arrays.stream(messages)
                    .filter(this::isValidMessage)
                    .forEach(message -> {
                        BaseComponent[] msg = TextComponent.fromLegacyText(message);
                        ((Player) sender).spigot().sendMessage(ChatMessageType.ACTION_BAR, msg);
                    });
        } else {
            sender.sendMessage(Arrays.stream(messages).filter(this::isValidMessage).toArray(String[]::new));
        }
    }

    /**
     * Sending messages list to sender
     *
     * @param sender      Message receiver
     * @param messages    Messages list which will be sent
     * @param toActionBar Should method to send this messages into player's actionbar if it's possible
     */
    public void send(CommandSender sender, List<String> messages, boolean toActionBar) {
        if (sender == null || messages == null || messages.isEmpty()) return;

        if (toActionBar && sender instanceof Player) {
            messages.stream()
                    .filter(this::isValidMessage)
                    .forEach(message -> {
                        BaseComponent[] msg = TextComponent.fromLegacyText(message);
                        ((Player) sender).spigot().sendMessage(ChatMessageType.ACTION_BAR, msg);
                    });
        } else {
            sender.sendMessage(messages.stream().filter(this::isValidMessage).toArray(String[]::new));
        }
    }

    /**
     * Getting message from section and sending it to sender
     *
     * @param sender  Message receiver
     * @param section Section with message which will be sent
     */
    public void getAndSend(CommandSender sender, String section) {
        if (sender == null || section == null)
            return;

        if (isTitle(section)) {
            Title title = getTitle(section);
            title.send(sender);
            return;
        }

        boolean toActionBar = sender instanceof Player && isActionbar(section);
        send(sender, get(section), toActionBar);
    }

    /**
     * Getting and formatting message and sending it to sender
     *
     * @param sender       Message receiver
     * @param section      Section with message which will be sent
     * @param replacements Array of string with this syntax: placeholder, value, placeholder, value...
     */
    public void sendFormatted(CommandSender sender, String section, Object... replacements) {
        if (sender == null || section == null) return;

        if (isTitle(section)) {
            Title title = getTitleFormatted(section, replacements);
            title.send(sender);
            return;
        }

        boolean toActionbar = sender instanceof Player && isActionbar(section);
        if (replacements == null || replacements.length == 0)
            send(sender, get(section), toActionbar);
        else
            send(sender, getFormatted(section, replacements), toActionbar);
    }

    /**
     * Getting and formatting message component and sending it to sender
     *
     * @param sender       Message receiver
     * @param section      Section with message component which will be sent
     * @param replacements Array of string with this syntax: 'placeholder1 value1 placeholder2 value2'...
     */
    public void sendComponentFormatted(CommandSender sender, String section, Object... replacements) {
        if (sender == null || section == null) return;

        boolean toActionbar = sender instanceof Player && isActionbar(section);
        if (replacements == null || replacements.length == 0)
            send(sender, getComponent(section), toActionbar);
        else
            send(sender, getComponentFormatted(section, replacements), toActionbar);
    }

    /**
     * Checking if message from specified section must be sent into player's actionbar or not
     *
     * @param section Section of message to check
     * @return 'true' if this message must be sent into player's actionbar or 'false' if not
     */
    public boolean isActionbar(String section) {
        List<String> actionBarPreferred = getList("actionbar");
        if (actionBarPreferred == null || actionBarPreferred.isEmpty())
            return false;

        // Directly contains check
        if (actionBarPreferred.contains(section))
            return true;

        // Check parent sections
        for (String key : actionBarPreferred) {
            if (!key.endsWith(".*"))
                continue;

            // If specified parent section then return true
            if (section.startsWith(key.substring(0, key.length() - 1)))
                return true;
        }

        return false;
    }

    /**
     * Check is a message exists in this path, and it's a title message.
     *
     * @param path A full path to message in configuration.
     * @return A boolean value: 'true' if it's a title or 'false' overwise.
     */
    public boolean isTitle(@NotNull String path) {
        Configuration config = getBukkitConfig();
        if (!config.isConfigurationSection(path))
            return false;

        return config.isSet(path + ".title") || config.isSet(path + ".subtitle");
    }

    private boolean isIgnoredMessage(String text) {
        return text == null || text.equals(DISABLED_MESSAGE_MARKER);
    }

    private boolean isValidMessage(String text) {
        return !isIgnoredMessage(text);
    }

}

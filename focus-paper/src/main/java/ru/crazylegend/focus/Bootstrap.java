package ru.crazylegend.focus;

import com.j256.ormlite.logger.*;
import org.bukkit.plugin.java.JavaPlugin;
import ru.crazylegend.focus.api.service.FocusApi;
import ru.crazylegend.focus.configuration.Configuration;
import ru.crazylegend.focus.configuration.MenuConfiguration;
import ru.crazylegend.focus.configuration.Messages;

import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;

@SuppressWarnings("unused")
public class Bootstrap extends JavaPlugin {

    private FocusApi focusApi;

    static {
        // Fix ORMLite 'Unable to get new instance of class' warnings in the MC console
        // TODO try to find more pretty fix solution in the future releases
        try {
            Field printStreamField = LocalLogBackend.class.getDeclaredField("printStream");
            printStreamField.setAccessible(true);

            PrintStream printStream = new PrintStream(new SilentOutputStream());
            printStreamField.set(null, printStream);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
    }

    @Override
    public void onEnable() {
        focusApi = FocusApi.bootstrapWith(this);

        // Allowing only ORMLite errors logging
        LoggerFactory.setLogBackendFactory(JavaUtilLogBackend::new);
        LoggerFactory.setLogBackendType(LogBackendType.JAVA_UTIL);
        setORMLiteLogLevel(Level.ERROR);

        Configuration config = new Configuration(this, "config.yml");
        config.refresh();

        if (!config.getBoolean("enabled"))
            return;

        Messages messages = new Messages(this, "messages.yml");
        messages.refresh();

        MenuConfiguration menuConfiguration = new MenuConfiguration(this, "menus.yml");
        menuConfiguration.refresh();

        System.out.println(menuConfiguration.getMenu("donate-menu").getMatrix());

        new CommandInfo(this, messages);
    }

    @Override
    public void onDisable() {
        if (focusApi != null && !focusApi.isCancelled()) {
            focusApi.cancel();
        }
    }

    public void setORMLiteLogLevel(Level level) {
        Logger.setGlobalLogLevel(level);
    }

    private static final class SilentOutputStream extends OutputStream {
        @Override
        public void write(int b) {
        }
    }
}

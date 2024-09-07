package kr.tpmc.manager;

import io.github.asdqwenm123.SimplePlayer;
import kr.tpmc.TPGuild;
import kr.tpmc.command.KommandGuild;
import kr.tpmc.listener.ChatListener;
import kr.tpmc.util.FileUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PluginManager {
    private static TPGuild plugin;
    private static Economy economy;

    public static void init() {
        plugin = TPGuild.plugin;

        FileUtil.load();

        SimplePlayer.register(plugin);
        KommandGuild.INSTANCE.register();

        org.bukkit.plugin.PluginManager pm = plugin.getServer().getPluginManager();

        pm.registerEvents(new ChatListener(), plugin);

        setupEconomy();

        TaskManager.run();

        plugin.getLogger().info("TPGuild 시작");
    }

    public static Economy getEconomy() {
        return economy;
    }

    private static void setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        economy = rsp.getProvider();
    }
}

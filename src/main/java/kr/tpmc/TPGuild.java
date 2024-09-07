package kr.tpmc;
import kr.tpmc.manager.PluginManager;
import kr.tpmc.model.Guild;
import kr.tpmc.model.Name;
import kr.tpmc.util.FileUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class TPGuild extends JavaPlugin {
    public static TPGuild plugin;

    @Override
    public void onEnable() {
        plugin = this;

        PluginManager.init();
//        new Guild(new Name("hello"), getServer().getOfflinePlayer("Aslagon").getUniqueId()).addGuild();
//
//        for (int i = 0; i < 101; i++) {
//            new Guild(new Name(i + ""), null).addGuild();
//        }
    }

    @Override
    public void onDisable() {
        FileUtil.save();

        getLogger().info("TPGuild 종료");
    }


}

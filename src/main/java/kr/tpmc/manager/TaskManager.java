package kr.tpmc.manager;

import io.github.asdqwenm123.SimplePlayer;
import kr.tpmc.TPGuild;
import kr.tpmc.model.Guild;
import kr.tpmc.model.Money;
import net.milkbowl.vault.economy.EconomyResponse;

public class TaskManager {
    public static void run() {
        TPGuild.plugin.getServer().getScheduler().runTaskTimer(TPGuild.plugin, () -> {
            for (Guild g : Guild.getGuildList().getGuilds()) {
                g.getMoney().makeScheduledWithdrawal();

                if (g.getMoney().getLoan() > Money.MAXIMUM_LIMIT) {
                    /*EconomyResponse a = */PluginManager.getEconomy().withdrawPlayer(SimplePlayer.getPlayer(g.getOwner()), (double) g.getMoney().getLoan());
//                    System.out.println(a.amount);
//                    System.out.println(a.balance);
//                    System.out.println(a.errorMessage);
                    g.removeGuild();
               }
            }
        }, 0L, 20L);
    }
}

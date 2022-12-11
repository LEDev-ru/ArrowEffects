package ru.ledev.arroweffects;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import ru.ledev.arroweffects.listener.ArrowListener;

import java.util.ArrayList;
import java.util.List;

public class ArrowEffects extends JavaPlugin {

    private static List<Arrow> arrowList = new ArrayList<>();
    private BukkitTask task;

    public static void addArrow(Arrow arrow) {
        arrowList.add(arrow);
    }

    public static void removeArrow(Arrow arrow) {
        arrowList.remove(arrow);
    }

    @Override
    public void onEnable() {
        info("&2Plugin loading...");

        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new ArrowListener(), this);

        Particle particle = Particle.valueOf(getConfig().getString("particle-type", "CLOUD").toUpperCase());
        int particlesCount = Math.max(getConfig().getInt("particles-count"), 1);
        long delay = (long) (Math.max(getConfig().getDouble("particles-spawn-delay"), 0.05) * 20);

        task = getServer().getScheduler().runTaskTimer(this, () -> {
            for (Arrow a : arrowList) {
                Location loc = a.getLocation();
                World world = a.getWorld();

                world.spawnParticle(particle, loc, particlesCount);
            }
        }, 5L, delay);

        info("&2Plugin successfully enabled");
    }

    @Override
    public void onDisable() {
        if (task != null) task.cancel();
        HandlerList.unregisterAll(this);

        info("&4Plugin disabled");
    }

    private void info(String s) {
        getLogger().info(s.trim().replace('&', '§'));
    }
}

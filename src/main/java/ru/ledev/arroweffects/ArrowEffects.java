package ru.ledev.arroweffects;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import ru.ledev.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public final class ArrowEffects extends JavaPlugin implements Listener {

    private static ArrowEffects instance;
    private static List<Arrow> arrowList = new ArrayList<>();

    @Override
    public void onEnable()
    {
        getLogger().info(StringUtils.formatString("&2Plugin loading..."));

        instance = this;
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);

        Server server = getServer();

        Particle particle = Particle.CLOUD;
        int particlesCount = 1;
        long delay = 1;

        try {
            switch (getConfig().getString("particle-type").toUpperCase()) {
                case "EXPLOSION":
                    particle = Particle.EXPLOSION_NORMAL;
                    break;
                case "CRIT":
                    particle = Particle.CRIT;
                    break;
                case "HEART":
                    particle = Particle.HEART;
                    break;
                case "TOTEM":
                    particle = Particle.TOTEM;
                    break;
                case "SMOKE":
                    particle = Particle.SMOKE_NORMAL;
                    break;
            }

            particlesCount = Math.max(getConfig().getInt("particles-count"), 1);

            double secDelay = Math.max(getConfig().getDouble("particles-spawn-delay"), 0.05);
            delay = (long) (secDelay * 20);
        }
        catch (Exception e) {
            getLogger().warning(StringUtils.formatString("&cCONFIG ERROR! Disabling plugin..."));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Particle finalParticle = particle;
        int finalParticlesCount = particlesCount;
        BukkitTask task = server.getScheduler().runTaskTimer(this, () -> {
            for (Arrow a : arrowList) {
                Location loc = a.getLocation();
                World world = a.getWorld();

                world.spawnParticle(finalParticle, loc, finalParticlesCount);
            }
        }, 5L, delay);

        getLogger().info(StringUtils.formatString("&2Plugin successfully enabled"));
    }

    @Override
    public void onDisable()
    {
        getLogger().info(StringUtils.formatString("&4Plugin disabled"));
    }

    public static ArrowEffects getInstance() {
        return instance;
    }

    public static void log(String log) {
        getInstance().getLogger().info(StringUtils.formatString(log));
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        LivingEntity entity = e.getEntity();
        if (!(entity instanceof Player)) return;

        Player player = (Player) entity;
        if (!(player.isOp() || player.hasPermission("ae.effects"))) return;

        Entity projectile = e.getProjectile();
        if (!(projectile instanceof Arrow)) return;

        Arrow arrow = (Arrow) projectile;
        arrowList.add(arrow);
    }

    @EventHandler
    public void onArrowEnd(ProjectileHitEvent e) {
        Projectile projectile = e.getEntity();
        if (!(projectile instanceof Arrow)) return;

        Arrow arrow = (Arrow) projectile;

        arrowList.remove(arrow);
    }
}

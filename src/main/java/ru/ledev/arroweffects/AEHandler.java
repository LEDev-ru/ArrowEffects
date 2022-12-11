package ru.ledev.arroweffects;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class AEHandler implements Listener {
    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        LivingEntity entity = e.getEntity();
        if (!(entity instanceof Player)) return;

        Player player = (Player) entity;
        if (!(player.isOp() || player.hasPermission("ae.effects"))) return;

        Entity projectile = e.getProjectile();
        if (!(projectile instanceof Arrow)) return;

        Arrow arrow = (Arrow) projectile;
        ArrowEffects.getInstance().addArrow(arrow);
    }

    @EventHandler
    public void onArrowEnd(ProjectileHitEvent e) {
        Projectile projectile = e.getEntity();
        if (!(projectile instanceof Arrow)) return;

        Arrow arrow = (Arrow) projectile;

        ArrowEffects.getInstance().removeArrow(arrow);
    }
}

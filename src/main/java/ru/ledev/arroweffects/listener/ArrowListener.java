package ru.ledev.arroweffects.listener;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import static ru.ledev.arroweffects.ArrowEffects.addArrow;
import static ru.ledev.arroweffects.ArrowEffects.removeArrow;

public class ArrowListener implements Listener {
    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        LivingEntity entity = e.getEntity();
        Entity projectile = e.getProjectile();

        if (!(entity instanceof Player) || !(projectile instanceof Arrow)) return;

        Player player = (Player) entity;

        // REFACTOR: Op имеет все права :c
        if (!player.hasPermission("ae.effects")) return;

        Arrow arrow = (Arrow) projectile;
        addArrow(arrow);
    }

    @EventHandler
    public void onArrowEnd(ProjectileHitEvent e) {
        Projectile projectile = e.getEntity();
        if (!(projectile instanceof Arrow)) return;

        Arrow arrow = (Arrow) projectile;

        removeArrow(arrow);
    }
}

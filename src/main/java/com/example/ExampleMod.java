package com.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;

public class ExampleMod implements ModInitializer {
    private int delayCounter = 0;
    // 12 тиков = 0.6 секунды (как полный кулдаун меча в 1.9+).
    // Если хочешь бить чаще — поставь 6 (0.3 сек) или 3 (0.15 сек).
    private final int DELAY_TICKS = 12; 

    @Override
    public void onInitialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.interactionManager == null) return;
            
            // Если цель в прицеле
            if (client.crosshairTarget instanceof EntityHitResult hit) {
                Entity target = hit.getEntity();
                
                // Если таймер дошёл до нуля — бьём
                if (delayCounter <= 0) {
                    client.interactionManager.attackEntity(client.player, target);
                    client.player.swingHand(client.player.getActiveHand());
                    delayCounter = DELAY_TICKS; // Сбрасываем таймер
                }
            }
            
            // Уменьшаем таймер каждый тик
            if (delayCounter > 0) delayCounter--;
        });
    }
}

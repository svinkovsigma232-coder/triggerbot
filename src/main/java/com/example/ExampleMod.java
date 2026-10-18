package com.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import java.util.Random;

public class ExampleMod implements ModInitializer {
    private int delayCounter = 0;
    private final Random random = new Random();
    
    // Базовая задержка в тиках. 12 = 0.6 сек (как кулдаун меча).
    // Бот будет бить НЕ точно каждые 12 тиков, а 12 + случайное число от 0 до 3.
    private final int BASE_DELAY = 12;
    private final int RANDOM_DELAY = 3;

    @Override
    public void onInitialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.interactionManager == null) return;
            
            if (client.crosshairTarget instanceof EntityHitResult hit) {
                Entity target = hit.getEntity();
                
                if (delayCounter <= 0) {
                    // Небольшой шанс "промазать" (например, 1 из 20), чтобы не быть идеальным
                    if (random.nextInt(20) != 0) {
                        client.interactionManager.attackEntity(client.player, target);
                        client.player.swingHand(client.player.getActiveHand());
                    }
                    
                    // Сбрасываем таймер со случайным разбросом
                    delayCounter = BASE_DELAY + random.nextInt(RANDOM_DELAY + 1);
                }
            }
            
            if (delayCounter > 0) delayCounter--;
        });
    }
}

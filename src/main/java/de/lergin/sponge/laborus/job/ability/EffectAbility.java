package de.lergin.sponge.laborus.job.ability;

import de.lergin.sponge.laborus.job.Job;
import de.lergin.sponge.laborus.job.JobAbility;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;
import java.util.List;

@ConfigSerializable
public class EffectAbility extends JobAbility {
    @Setting("potionEffect")
    private PotionEffectConfig effectConfig = new PotionEffectConfig();

    public EffectAbility() {}

    @Override
    public boolean startAbility(Job job, Player player) {
        if (!canStartAbility(job, player)) {
            sendCoolDownNotEndedMessage(job, player);
            return false;
        }

        List<PotionEffect> potionEffects = player.get(Keys.POTION_EFFECTS).orElseGet(ArrayList::new);
        potionEffects.add(effectConfig.get());
        player.offer(Keys.POTION_EFFECTS, potionEffects);

        startCoolDown(job, player);
        sendStartMessage(job, player);

        return true;
    }

    @ConfigSerializable
    private static class PotionEffectConfig {
        @Setting
        int amplifier = 0;
        @Setting
        int duration = 1;
        @Setting
        boolean ambiance = false;
        @Setting
        boolean particles = true;
        @Setting
        PotionEffectType potionType = PotionEffectTypes.SPEED;

        public PotionEffect get(){
            return PotionEffect.builder()
                    .amplifier(amplifier)
                    .duration(duration)
                    .particles(particles)
                    .potionType(potionType)
                    .ambience(ambiance)
                    .build();
        }

        public PotionEffectConfig(){}
    }
}

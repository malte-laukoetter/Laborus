package de.lergin.sponge.laborus.job.ability;

import de.lergin.sponge.laborus.job.Job;
import de.lergin.sponge.laborus.job.JobAbility;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;
import java.util.List;

public class EffectAbility extends JobAbility {
    private final PotionEffect effect;

    public EffectAbility(Job job, ConfigurationNode node) {
        super(
                job,
                node.getNode("name").getString("EffectAbility"),
                node.getNode("coolDown").getInt(60)
        );

        effect = PotionEffect.builder()
                .amplifier(node.getNode("amplifier").getInt(1))
                .duration(node.getNode("duration").getInt(200))
                .potionType(
                        Sponge.getRegistry()
                                .getType(PotionEffectType.class, node.getNode("type").getString())
                                .orElse(PotionEffectTypes.SPEED)
                )
                .particles(node.getNode("particles").getBoolean(true))
                .ambience(node.getNode("ambience").getBoolean(false))
                .build();
    }

    @Override
    public boolean startAbility(Player player) {
        if (!canStartAbility(player)) {
            sendCoolDownNotEndedMessage(player);
            return false;
        }

        List<PotionEffect> potionEffects = player.get(Keys.POTION_EFFECTS).orElse(new ArrayList<>());
        potionEffects.add(effect);
        player.offer(Keys.POTION_EFFECTS, potionEffects);

        startCoolDown(player);
        sendStartMessage(player);

        return true;
    }
}

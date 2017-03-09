package de.lergin.sponge.laborus.job.bonus;

import com.google.common.collect.Sets;
import de.lergin.sponge.laborus.Laborus;
import de.lergin.sponge.laborus.job.JobAction;
import de.lergin.sponge.laborus.job.JobBonus;
import de.lergin.sponge.laborus.job.JobItem;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.service.economy.EconomyService;

import java.math.BigDecimal;

@ConfigSerializable
public class EconomyReward extends JobBonus{
    private final EconomyService service =
            Sponge.getServiceManager().getRegistration(EconomyService.class).get().getProvider();
    @Setting(value = "amountMax", comment = "maximal amount of money")
    private final BigDecimal amountMax = BigDecimal.valueOf(0.0);
    @Setting(value = "amountMin", comment = "minimal amount of money")
    private final BigDecimal amountMin = BigDecimal.valueOf(0.0);
    private final Cause cause = Cause.of(NamedCause.source(Sponge.getPluginManager().fromInstance(Laborus.instance())));

    public EconomyReward() {
        super(Sets.newHashSet(JobAction.BREAK, JobAction.ENTITY_KILL));
    }

    @Override
    public void useBonus(JobItem item, Player player, Object i2) {
        service.getOrCreateAccount(player.getIdentifier()).get().deposit(
                service.getDefaultCurrency(),
                amountMax.divide(amountMin, BigDecimal.ROUND_FLOOR).multiply(BigDecimal.valueOf(Math.random())).add(amountMin),
                cause
        );
    }
}

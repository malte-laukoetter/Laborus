package de.lergin.sponge.laborus.job.bonus;

import de.lergin.sponge.laborus.JobsMain;
import de.lergin.sponge.laborus.job.JobBonus;
import de.lergin.sponge.laborus.job.JobItem;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.service.economy.EconomyService;

import java.math.BigDecimal;

public class EconomyReward extends JobBonus{
    private final EconomyService service =
            Sponge.getServiceManager().getRegistration(EconomyService.class).get().getProvider();
    private final BigDecimal amountMax;
    private final BigDecimal amountMin;
    private final Cause cause = Cause.of(NamedCause.source(Sponge.getPluginManager().fromInstance(JobsMain.instance())));

    public EconomyReward(ConfigurationNode config) {
        super(config);

        amountMax = BigDecimal.valueOf(config.getNode("maxAmount").getDouble());
        amountMin = BigDecimal.valueOf(config.getNode("minAmount").getDouble());
    }

    @Override
    public void useBonus(JobItem item, Player player) {
        service.getOrCreateAccount(player.getIdentifier()).get().deposit(
                service.getDefaultCurrency(),
                amountMax.divide(amountMin, BigDecimal.ROUND_FLOOR).multiply(BigDecimal.valueOf(Math.random())).add(amountMin),
                cause
        );
    }
}

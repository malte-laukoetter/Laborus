package de.lergin.sponge.laborus.job.bonus;

import de.lergin.sponge.laborus.job.JobBonus;
import de.lergin.sponge.laborus.job.JobItem;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;

public class EconomyReward extends JobBonus{
    EconomyService service = Sponge.getServiceManager().getRegistration(EconomyService.class).get().getProvider();
    BigDecimal amountMax;
    BigDecimal amountMin;

    public EconomyReward(ConfigurationNode config) {
        super(config);

        amountMax = BigDecimal.valueOf(config.getNode("maxAmount").getDouble());
        amountMin = BigDecimal.valueOf(config.getNode("minAmount").getDouble());
    }

    @Override
    public void useBonus(JobItem item, Player player) {
        player.sendMessage(Text.of("sadsadsad"));

        service.getOrCreateAccount(player.getIdentifier()).get().deposit(
                service.getDefaultCurrency(),
                amountMax.divide(amountMin, BigDecimal.ROUND_FLOOR).multiply(BigDecimal.valueOf(Math.random())).add(amountMin),
                Cause.of(NamedCause.source("Laborus"))
        );
    }
}

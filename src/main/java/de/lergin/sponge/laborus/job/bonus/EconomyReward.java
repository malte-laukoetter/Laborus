package de.lergin.sponge.laborus.job.bonus;

import com.google.common.collect.Lists;
import de.lergin.sponge.laborus.Laborus;
import de.lergin.sponge.laborus.job.JobAction;
import de.lergin.sponge.laborus.api.JobBonus;
import de.lergin.sponge.laborus.job.JobItem;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;

import java.math.BigDecimal;

@ConfigSerializable
public class EconomyReward extends JobBonus {
    private final EconomyService service =
            Sponge.getServiceManager().getRegistration(EconomyService.class).get().getProvider();
    @Setting(value = "amountMax", comment = "maximal amount of money")
    private double amountMax = 0.0;
    @Setting(value = "amountMin", comment = "minimal amount of money")
    private double amountMin = 0.0;
    @Setting(value = "currency", comment = "the id of the currency to use, defaults to the default currency")
    private String currency = null;
    private final Cause cause = Cause.of(NamedCause.source(Sponge.getPluginManager().fromInstance(Laborus.instance())));

    public EconomyReward() {
        super(Lists.newArrayList(JobAction.BREAK, JobAction.ENTITY_KILL));
    }

    @Override
    public void applyBonus(JobItem item, Player player, Object i2) {
        Currency cur;

        if(currency == null){
            cur = service.getDefaultCurrency();
        }else{
            cur = service.getCurrencies().stream()
                    .filter(c -> c.getId().equals(currency)).findFirst().orElseGet(service::getDefaultCurrency);
        }

        BigDecimal amount = BigDecimal.valueOf(amountMax).add(BigDecimal.valueOf(amountMin).negate())
                .multiply(BigDecimal.valueOf(Math.random())).add(BigDecimal.valueOf(amountMin));

        Account account = service.getOrCreateAccount(player.getUniqueId()).get();

        account.deposit(cur, amount, cause);
    }
}

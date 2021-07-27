package net.hyper_pigeon.better_wandering_trader.mixin;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.hyper_pigeon.better_wandering_trader.BetterWanderingTraderMod;
import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TradeOffers.class)
public class TraderOffersMixin {

    @Final
    @Mutable
    @Shadow
    public static final Int2ObjectMap<TradeOffers.Factory[]> WANDERING_TRADER_TRADES;


    @Shadow
    private native static Int2ObjectMap<TradeOffers.Factory[]> copyToFastUtilMap(
            ImmutableMap<Integer, TradeOffers.Factory[]> immutableMap);

    static {

        WANDERING_TRADER_TRADES = copyToFastUtilMap(ImmutableMap.of(
                1,
                BetterWanderingTraderMod.CONFIG.commonTradeFactory.toTradeFactory(),
                // SellItemFactory.DefaultCommonTrades,
                2,
                BetterWanderingTraderMod.CONFIG.rareTradeFactory.toTradeFactory()
                // SellItemFactory.DefaultRareTrades
            )
        );

        if (BetterWanderingTraderMod.CONFIG.trades.enable_user_added_traded) {

            WANDERING_TRADER_TRADES.put(3, BetterWanderingTraderMod.CONFIG.invisibleTradeFactory.toTradeFactory());
        }

        System.out.println("WT Trades Loaded!");
    }

}
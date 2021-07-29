package net.hyper_pigeon.better_wandering_trader.mixin;


import me.shedaniel.autoconfig.AutoConfig;
import net.hyper_pigeon.better_wandering_trader.config.BetterWanderingTraderConfig;
import net.hyper_pigeon.better_wandering_trader.config.UserTradeListConfigHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WanderingTraderEntity.class)
public abstract class WanderingTraderEntityMixin extends MerchantEntity {

    @Shadow
    public native void fillRecipes();

    @Shadow
    public native void afterUsing(TradeOffer offer); 


    public WanderingTraderEntityMixin(EntityType<? extends MerchantEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("RETURN"), method = "fillRecipes")
    public void add_new_list(CallbackInfo info) {
        BetterWanderingTraderConfig config = AutoConfig.getConfigHolder(BetterWanderingTraderConfig.class).getConfig();
        
        TradeOfferList tradeOfferList = this.getOffers();
        if (!config.trades.enable_base_trades) {
            tradeOfferList.clear();
        }

        if (config.trades.enable_user_added_trades) {
            UserTradeListConfigHandler.addTradeOffers(tradeOfferList, this);
        }
    }
}

package net.lt_schmiddy.super_configurable_wandering_trader.mixin;


import me.shedaniel.autoconfig.AutoConfig;
import net.lt_schmiddy.super_configurable_wandering_trader.config.BetterWanderingTraderConfig;
import net.lt_schmiddy.super_configurable_wandering_trader.trade_info.TradeConfigHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WanderingTraderEntity.class)
public abstract class WanderingTraderEntityMixin extends MerchantEntity {

    // @Shadow
    // public native void fillRecipes();

    @Shadow
    public native void afterUsing(TradeOffer offer); 


    public WanderingTraderEntityMixin(EntityType<? extends MerchantEntity> entityType, World world) {
        super(entityType, world);
    }

    /*
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
    }*/

    
    @Override
    public void fillRecipes() {
        if (this.offers == null) {
            this.getOffers();
            return;
        }

        BetterWanderingTraderConfig config = AutoConfig.getConfigHolder(BetterWanderingTraderConfig.class).getConfig();
        if (config.trades.enable_base_trades) {

            TradeOffers.Factory[] factorys = (TradeOffers.Factory[]) TradeOffers.WANDERING_TRADER_TRADES.get(1);
            TradeOffers.Factory[] factorys2 = (TradeOffers.Factory[]) TradeOffers.WANDERING_TRADER_TRADES.get(2);
            if (factorys != null && factorys2 != null) {
                TradeOfferList tradeOfferList = this.offers;
                this.fillRecipesFromPool(tradeOfferList, factorys, 5);
                int i = this.random.nextInt(factorys2.length);
                TradeOffers.Factory factory = factorys2[i];
                TradeOffer tradeOffer = factory.create(this, this.random);
                if (tradeOffer != null) {
                    tradeOfferList.add(tradeOffer);
                }
            }
        }

        if (config.trades.enable_user_added_trades) {
            TradeOfferList tradeOfferList = this.offers;
            TradeConfigHandler.addTradeOffers(tradeOfferList, this);
        }
    }
    
}

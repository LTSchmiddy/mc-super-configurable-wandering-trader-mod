package net.lt_schmiddy.super_configurable_wandering_trader.abstracts;

public abstract class AWeightedAndSortedTradeGenerator extends ATradeGenerator {
    public int sort_order = 0;
    public float selection_weight = 1;

    @Override
    public int getSortOrder() {
        return sort_order;
    }

    @Override
    public float getWeight() {
        return selection_weight;
    }
}

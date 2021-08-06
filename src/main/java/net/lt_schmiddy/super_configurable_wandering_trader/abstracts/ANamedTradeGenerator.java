package net.lt_schmiddy.super_configurable_wandering_trader.abstracts;

import net.lt_schmiddy.super_configurable_wandering_trader.interfaces.INamedTradeGenerator;

public abstract class ANamedTradeGenerator extends ATradeGenerator implements INamedTradeGenerator {
    public String toString() {
        return "Named Generator: " + getName();
    }
}

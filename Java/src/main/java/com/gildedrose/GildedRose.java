package com.gildedrose;

class GildedRose {

    static final int DEFAULT_DEGRADATION_RATE_BEFORE_SELL_IN_DATE = -1;
    static final int DEFAULT_DEGRADATION_RATE_AFTER_SELL_IN_DATE = -2;
    static final int ITEM_MAX_QUALITY = 50;
    static final int ITEM_MIN_QUALITY = 0;

    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {
        for (final Item item : this.items) {
            final ItemType itemType = ItemType.forItem(item);
            itemType.update(item);
        }
    }
}

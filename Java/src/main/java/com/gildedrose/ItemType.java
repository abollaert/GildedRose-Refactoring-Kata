package com.gildedrose;

import java.util.function.Function;

/**
 * Enumerates the different types of items.
 */
enum ItemType {

    NORMAL(null, true, item -> {
        if (item.sellIn > 0) {
            /** Aged brie. */
            return GildedRose.DEFAULT_DEGRADATION_RATE_BEFORE_SELL_IN_DATE;
        } else {
            return GildedRose.DEFAULT_DEGRADATION_RATE_AFTER_SELL_IN_DATE;
        }
    }),

    AGED_BRIE("aged brie", true, item -> {
        if (item.sellIn > 0) {
            return -1 * GildedRose.DEFAULT_DEGRADATION_RATE_BEFORE_SELL_IN_DATE;
        } else {
            return -1 * GildedRose.DEFAULT_DEGRADATION_RATE_AFTER_SELL_IN_DATE;
        }
    }),

    LEGENDARY("sulfuras", false, item -> 0),

    BACKSTAGE_PASSES("backstage passes", true, item -> {
        if (item.sellIn > 10) {
            return 1;
        } else if (item.sellIn > 5) {
            return 2;
        } else if (item.sellIn > 0) {
            return 3;
        }

        return -1 * item.quality;
    }),

    CONJURED("conjured", true, item -> {
        if (item.sellIn > 0) {
            return 2 * GildedRose.DEFAULT_DEGRADATION_RATE_BEFORE_SELL_IN_DATE;
        } else {
            return 2 * GildedRose.DEFAULT_DEGRADATION_RATE_AFTER_SELL_IN_DATE;
        }
    });

    /**
     * The name prefix for the items.
     */
    private final String namePrefix;

    /**
     * Calculates the next quality : takes an item, returns the quality gradient.
     */
    private final Function<Item, Integer> qualityCalculator;

    /**
     * Indicartes whether the item expires.
     */
    private final boolean expires;

    /**
     * Create a new instance.
     *
     * @param namePrefix        The name prefix.
     * @param expires           Whether the type of item expires.
     * @param qualityCalculator Function that calculates the quality change.
     */
    ItemType(final String namePrefix,
             final boolean expires,
             final Function<Item, Integer> qualityCalculator) {
        this.namePrefix = namePrefix;
        this.expires = expires;
        this.qualityCalculator = qualityCalculator;
    }

    /**
     * Returns the corresponding {@link ItemType} for the given {@link Item}.
     *
     * @param item The {@link Item}.
     * @return The corresponding {@link ItemType}.
     */
    public static ItemType forItem(final Item item) {
        for (final ItemType type : ItemType.values()) {
            if (!type.isDefaultType() &&
                item.name != null &&
                item.name.toLowerCase().startsWith(type.namePrefix)) {
                return type;
            }
        }

        // Everything else goes in the "normal" bin.
        return NORMAL;
    }

    /**
     * Indicates whether this is the default type.
     *
     * @return true for the default type, false for the rest.
     */
    private boolean isDefaultType() {
        return this.namePrefix == null;
    }

    /**
     * Updates the quality of an {@link Item}.
     *
     * @param item The {@link Item} to update the quality of.
     */
    private void updateQuality(final Item item) {
        final int qualityChange = this.qualityCalculator.apply(item);

        if (qualityChange != 0) {
            int newQuality = item.quality + qualityChange;

            if (newQuality > GildedRose.ITEM_MAX_QUALITY) {
                newQuality = GildedRose.ITEM_MAX_QUALITY;
            }

            if (newQuality < GildedRose.ITEM_MIN_QUALITY) {
                newQuality = GildedRose.ITEM_MIN_QUALITY;
            }

            item.quality = newQuality;
        }
    }

    /**
     * Updates the sell in date of an {@link Item}.
     *
     * @param item The {@link Item} to update the quality of.
     */
    private void updateSellIn(final Item item) {
        if (this.expires) {
            item.sellIn -= 1;
        }
    }

    /**
     * Updates an item.
     *
     * @param item The item to update.
     */
    public void update(final Item item) {
        this.updateQuality(item);
        this.updateSellIn(item);
    }
}

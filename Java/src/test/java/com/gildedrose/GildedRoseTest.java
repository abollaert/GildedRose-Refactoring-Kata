package com.gildedrose;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@link GildedRose}.
 */
final class GildedRoseTest {

    private static final String ITEM_AGED_BRIE = "Aged Brie";
    private static final String ITEM_BACKSTAGE_PASSES = "Backstage passes to a TAFKAL80ETC concert";
    private static final String ITEM_SULFURAS = "Sulfuras, Hand of Ragnaros";
    private static final String ITEM_ELIXIR_MONGOOSE = "Elixir of the Mongoose";
    private static final String ITEM_DEXTERITY_VEST = "+5 Dexterity Vest";
    private static final String ITEM_CONJURED_MANA_CAKE = "Conjured Mana Cake";

    @ParameterizedTest
    @ValueSource(strings = {
            ITEM_AGED_BRIE,
            ITEM_BACKSTAGE_PASSES,
            ITEM_ELIXIR_MONGOOSE,
            ITEM_DEXTERITY_VEST,
            ITEM_CONJURED_MANA_CAKE
    })
    void testStandardItemSellInDecreasesAtEndOfDay(final String itemName) {
        final Item item = new Item(itemName, 1, 100);

        final GildedRose gildedRose = new GildedRose(new Item[] { item });
        gildedRose.updateQuality();

        assertThat(item.sellIn).isZero();
    }

    @Test
    void testConjuredItemsDropQualityTwiceAsFastAsStandardItemsBeforeSellInDate() {
        final Item item = new Item(ITEM_CONJURED_MANA_CAKE, 1, 30);

        final GildedRose gildedRose = new GildedRose(new Item[] { item });
        gildedRose.updateQuality();

        assertThat(item.quality).isEqualTo(28);
    }

    @Test
    void testConjuredItemsDropQualityTwiceAsFastAsStandardItemsAfterSellInDate() {
        final Item item = new Item(ITEM_CONJURED_MANA_CAKE, -1, 30);

        final GildedRose gildedRose = new GildedRose(new Item[] { item });
        gildedRose.updateQuality();

        assertThat(item.quality).isEqualTo(26);
    }

    @Test
    void testSulfurasSellInDoesNotDecreaseEndOfDay() {
        final Item item = new Item(ITEM_SULFURAS, 1, 100);

        final GildedRose gildedRose = new GildedRose(new Item[] { item });
        gildedRose.updateQuality();

        assertThat(item.sellIn).isEqualTo(1);
    }

    @Test
    void testSulfurasQualityDoesntChange() {
        final Item item = new Item(ITEM_SULFURAS, 1, 80);

        final GildedRose gildedRose = new GildedRose(new Item[] { item });
        gildedRose.updateQuality();

        assertThat(item.quality).isEqualTo(80);
    }

    @ValueSource(strings = {
            ITEM_ELIXIR_MONGOOSE,
            ITEM_DEXTERITY_VEST
    })
    @ParameterizedTest
    void testStandardItemQualityDecreasesByOneBeforeSellInDate(final String itemName) {
        final Item item = new Item(itemName, 1, 15);

        final GildedRose gildedRose = new GildedRose(new Item[] { item });
        gildedRose.updateQuality();

        assertThat(item.quality).isEqualTo(14);
    }

    @ValueSource(strings = {
            ITEM_ELIXIR_MONGOOSE,
            ITEM_DEXTERITY_VEST
    })
    @ParameterizedTest
    void testStandardItemQualityDecreasesByTwoAfterSellInDate(final String itemName) {
        final Item item = new Item(itemName, 0, 15);

        final GildedRose gildedRose = new GildedRose(new Item[] { item });
        gildedRose.updateQuality();

        assertThat(item.quality).isEqualTo(13);
    }

    @Test
    void testAgedBrieQualityIncreasesByOneBeforeSellInDate() {
        final Item item = new Item(ITEM_AGED_BRIE, 1, 15);

        final GildedRose gildedRose = new GildedRose(new Item[] { item });
        gildedRose.updateQuality();

        assertThat(item.quality).isEqualTo(16);
    }

    @Test
    void testAgedBrieQualityIncreasesByTwoAfterSellInDate() {
        final Item item = new Item(ITEM_AGED_BRIE, 0, 15);

        final GildedRose gildedRose = new GildedRose(new Item[] { item });
        gildedRose.updateQuality();

        assertThat(item.quality).isEqualTo(17);
    }

    @Test
    void testBackstagePassesQualityIncreasesByOneMoreThan10DaysBeforeSellInDate() {
        final Item item = new Item(ITEM_BACKSTAGE_PASSES, 20, 15);

        final GildedRose gildedRose = new GildedRose(new Item[] { item });
        gildedRose.updateQuality();

        assertThat(item.quality).isEqualTo(16);
    }

    @Test
    void testBackstagePassesQualityIncreasesByTwoBetween10DaysAnd5DaysBeforeSellInDate() {
        final Item item = new Item(ITEM_BACKSTAGE_PASSES, 10, 15);

        final GildedRose gildedRose = new GildedRose(new Item[] { item });
        gildedRose.updateQuality();

        assertThat(item.quality).isEqualTo(17);
    }

    @Test
    void testBackstagePassesQualityIncreasesByThreeBetween5DaysAnd0DaysBeforeSellInDate() {
        final Item item = new Item(ITEM_BACKSTAGE_PASSES, 5, 15);

        final GildedRose gildedRose = new GildedRose(new Item[] { item });
        gildedRose.updateQuality();

        assertThat(item.quality).isEqualTo(18);
    }

    @Test
    void testBackstagePassesQualityDropsTo0AfterSellInDate() {
        final Item item = new Item(ITEM_BACKSTAGE_PASSES, 0, 15);

        final GildedRose gildedRose = new GildedRose(new Item[] { item });
        gildedRose.updateQuality();

        assertThat(item.quality).isEqualTo(0);
    }

    @Test
    void testQualityCannotExceed50() {
        final Item item = new Item(ITEM_AGED_BRIE, 1, 50);

        final GildedRose gildedRose = new GildedRose(new Item[] { item });
        gildedRose.updateQuality();

        assertThat(item.quality).isEqualTo(50);
    }

    @Test
    void testQualityCannotGoBelow0() {
        final Item item = new Item(ITEM_ELIXIR_MONGOOSE, 1, 0);

        final GildedRose gildedRose = new GildedRose(new Item[] { item });
        gildedRose.updateQuality();

        assertThat(item.quality).isEqualTo(0);
    }
}

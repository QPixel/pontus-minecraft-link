package dev.rileyy.discordLink.providers;


import dev.rileyy.discordLink.Util;
import me.lucko.spark.api.Spark;
import me.lucko.spark.api.SparkProvider;

public class StatisticsProvider {
    private Spark sparkAPI;
    private boolean enabled = false;
    public StatisticsProvider() {
        try {
            sparkAPI = SparkProvider.get();
            enabled = true;
        } catch (IllegalStateException e) {
            Util.LOGGER.error("Spark is not loaded, disabling statistics features");
        }
    }
    public boolean isEnabled() {
        return enabled;
    }
}

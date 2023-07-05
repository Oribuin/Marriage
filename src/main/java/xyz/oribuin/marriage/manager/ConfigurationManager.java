package xyz.oribuin.marriage.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.config.RoseSetting;
import dev.rosewood.rosegarden.manager.AbstractConfigurationManager;
import xyz.oribuin.marriage.MarriagePlugin;

public class ConfigurationManager extends AbstractConfigurationManager {

    public enum Setting implements RoseSetting {
        MAX_PARTNERS("max-partners", 1, "The maximum amount of partners a player can have."),
        MARRIAGE_COST("marriage-cost", 1000, "The cost of marriage."),
        DIVORCE_COST("divorce-cost", 500, "The cost of divorce."),

        MARRIAGE_ANNOUNCEMENT("marriage-announcement", true, "Whether or not to announce marriages."),
        DIVORCE_ANNOUNCEMENT("divorce-announcement", true, "Whether or not to announce divorces."),


        // smooch
        KISS_ENABLED("kissing.enabled", true, "Whether or not players can kiss their partners."),
        KISS_COOLDOWN("kissing.cooldown", 60, "The cooldown in seconds between kisses."),
        KISS_PARTICLES("kissing.particles", 15, "The amount of particles to spawn when kissing."),

        // marriage request yesyes
        REQUEST_TIMEOUT("request.timeout", 60, "The timeout in seconds for marriage requests."),

        ;

        private final String key;
        private final Object defaultValue;
        private final String[] comments;
        private Object value = null;

        Setting(String key, Object defaultValue, String... comments) {
            this.key = key;
            this.defaultValue = defaultValue;
            this.comments = comments != null ? comments : new String[0];
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public Object getDefaultValue() {
            return this.defaultValue;
        }

        @Override
        public String[] getComments() {
            return this.comments;
        }

        @Override
        public Object getCachedValue() {
            return this.value;
        }

        @Override
        public void setCachedValue(Object value) {
            this.value = value;
        }

        @Override
        public CommentedFileConfiguration getBaseConfig() {
            return MarriagePlugin.getInstance().getManager(ConfigurationManager.class).getConfig();
        }
    }

    public ConfigurationManager(RosePlugin rosePlugin) {
        super(rosePlugin, Setting.class);
    }

    @Override
    protected String[] getHeader() {
        return new String[]{};
    }
}

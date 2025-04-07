package configs;

import constants.TestConstants;
import org.aeonbits.owner.Config;

@Config.Sources({"classpath:${env}.properties", "classpath:default.properties"})
public interface TestPropertiesConfig extends Config {

    @Key("baseUrl")
    @DefaultValue(TestConstants.BASE_URL)
    String getBaseUrl();

    @Key("username")
    String getUsername();

    @Key("password")
    String getPassword();

    //timeouts might depend on the environment based on the amount of data generated and some infrastructure
    @Key("defaultTimeout")
    @DefaultValue(TestConstants.SHORT_TIMEOUT_SECONDS)
    int getShortTimeout();

    @Key("longTimeout")
    @DefaultValue(TestConstants.LONG_TIMEOUT_SECONDS)
    int getLongTimeout();
}

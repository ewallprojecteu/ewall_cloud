package eu.ewall.fusioner.twitter.oauth.factory;

import eu.ewall.fusioner.twitter.config.EnvironmentUtils;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterAppFactory {

    private TwitterAppFactory() {
    }

    public static Twitter getTwitterInstance() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(EnvironmentUtils.getProperty("oauth.consumerKey"))
                .setOAuthConsumerSecret(EnvironmentUtils.getProperty("oauth.consumerSecret"));

        TwitterFactory twitterFactory = new TwitterFactory(cb.build());
        return twitterFactory.getInstance();
    }

    public static TwitterStream getTwitterStreamInstance() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(EnvironmentUtils.getProperty("oauth.consumerKey"))
                .setOAuthConsumerSecret(EnvironmentUtils.getProperty("oauth.consumerSecret"));

        TwitterStreamFactory twitterStreamFactory = new TwitterStreamFactory(cb.build());
        return twitterStreamFactory.getInstance();
    }
}
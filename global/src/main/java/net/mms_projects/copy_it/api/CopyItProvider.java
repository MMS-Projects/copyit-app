package net.mms_projects.copy_it.api;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.services.TimestampService;

import java.security.SecureRandom;

public class CopyItProvider extends DefaultApi10a implements TimestampService
{
    private static final SecureRandom random = new SecureRandom();
    /**
     * This is a list of characters that can be used to generate a nonce
     */
    private static final String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String AUTHORIZATION_URL = "http://api.copyit.mmsdev.org/oauth/authorize?oauth_token=%s";

    @Override
    public String getAccessTokenEndpoint() {
        return "http://api.copyit.mmsdev.org/oauth/access_token";
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "http://api.copyit.mmsdev.org/oauth/request_token";
    }

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    /*
    @Override
    public Verb getRequestTokenVerb() {
        return Verb.GET;
    }
    */

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return String.format(AUTHORIZATION_URL, requestToken.getToken());
    }

    @Override
    public TimestampService getTimestampService() {
        return this;
    }

    @Override
    public String getTimestampInSeconds() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    /**
     * This method is used by Scribe to get a valid nonce.
     *
     * We had to implement the {@link org.scribe.services.TimestampService} our self because our
     * API has its own nonce requirements.
     *
     * @return A valid nonce accepted by our API
     */
    @Override
    public String getNonce() {
        // The nonce length that our API accepts is 8
        char[] nonce = new char[8];

        for (int i = 0; i < nonce.length; i++) {
            // Get a random character out of the list of characters and put it in the character
            // array
            nonce[i] = characters.charAt(random.nextInt(characters.length()));
        }

        // Cast the character array to a string and return it
        return new String(nonce);
    }

}

package net.mms_projects.copy_it.api;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.*;
import org.scribe.services.TimestampService;

import java.security.SecureRandom;

public class CopyItProvider extends DefaultApi10a implements TimestampService
{
    private static final SecureRandom random = new SecureRandom();
    private static final String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String AUTHORIZATION_URL = "http://api.copyit.mmsdev.org/oauth/authorize?oauth_token=%s";

    @Override
    public String getAccessTokenEndpoint()
    {
        return "http://api.copyit.mmsdev.org/oauth/access_token";
    }

    @Override
    public String getRequestTokenEndpoint()
    {
        return "http://api.copyit.mmsdev.org/oauth/request_token";
    }

    @Override
    public Verb getAccessTokenVerb()
    {
        return Verb.GET;
    }

    /*@Override
    public Verb getRequestTokenVerb()
    {
        return Verb.GET;
    }     */

    @Override
    public String getAuthorizationUrl(Token requestToken)
    {
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

    @Override
    public String getNonce() {
        char[] text = new char[8];
        for (int i = 0; i < 8; i++)
            text[i] = characters.charAt(random.nextInt(characters.length()));
        return new String(text);
    }

}

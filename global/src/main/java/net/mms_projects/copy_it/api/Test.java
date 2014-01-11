package net.mms_projects.copy_it.api;

import net.mms_projects.copy_it.sdk.api.exceptions.ApiException;
import net.mms_projects.copy_it.sdk.api.exceptions.http.success.NoContentException;
import net.mms_projects.copy_it.sdk.api.v1.Android;
import net.mms_projects.copy_it.sdk.api.v1.Clipboard;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.util.Scanner;

public class Test
{
    private static final String NETWORK_NAME = "CopyIt";
    private static final String AUTHORIZE_URL = "https://www.google.com/accounts/OAuthAuthorizeToken?oauth_token=";
    private static final String PROTECTED_RESOURCE_URL = "http://api.copyit.mmsdev.org/1/clipboard/update";
    private static final String SCOPE = "https://docs.google.com/feeds/";

    public static void main(String[] args)
    {
        OAuthService service = new ServiceBuilder()
                .provider(CopyItProvider.class)
                .apiKey("401a131e03357df2a563fba48f98749448ed63d37e007f7353608cf81fa70a2d")
                .apiSecret("ba3f5945ba6cfb18ca4869cef2c3daf9d4230e37629f3087b281be6ec8fda2bd")
                .callback("http://example.com/")
                .debug()
                .build();

        Scanner in = new Scanner(System.in);

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();

        // Obtain the Request Token
        System.out.println("Fetching the Request Token...");
        Token requestToken = service.getRequestToken();
        System.out.println("Got the Request Token!");
        System.out.println("(if your curious it looks like this: " + requestToken + " )");
        System.out.println();

        System.out.println("Now go and authorize Scribe here:");
        System.out.println(service.getAuthorizationUrl(requestToken));
        System.out.println("And paste the verifier here");
        System.out.print(">>");
        Verifier verifier = new Verifier(in.nextLine());
        System.out.println();

        // Trade the Request Token and Verfier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        Token accessToken = service.getAccessToken(requestToken, verifier);
        System.out.println("Got the Access Token!");
        System.out.println("(if your curious it looks like this: " + accessToken + " )");
        System.out.println();

        Clipboard clipboard = new Clipboard(accessToken, service, "http://api.copyit.mmsdev.org/1/clipboard/");

        // Show the updated clipboard content
        Clipboard.Responses.Get responseGet = null;
        try {
            responseGet = clipboard.get();

            System.out.println(responseGet.getContent());
            System.out.println(responseGet.getLastUpdated());
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (NoContentException e) {
            System.out.println("Damn! No content is available on the server yet.");
        }

        // Update the clipboard content
        Clipboard.Responses.Update responseSet = null;
        try {
            responseSet = clipboard.update("Blabla", "text/plain");
        } catch (ApiException e) {
            e.printStackTrace();
        }
        System.out.println(responseSet);

        // Show the updated clipboard content
        try {
            responseGet = clipboard.get();

            System.out.println(responseGet.getContent());
            System.out.println(responseGet.getLastUpdated());
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (NoContentException e) {
            System.out.println("Damn! No content is available on the server yet.");
        }

        Android android = new Android(accessToken, service, "http://api.copyit.mmsdev.org/1/android/");
        try {
            String id = "--";

            android.gcmRegister(id);
            android.gcmUnregister(id);
        } catch (ApiException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("That's it man! Go and build something awesome with Scribe! :)");
    }
}
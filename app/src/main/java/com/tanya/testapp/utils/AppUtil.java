package com.tanya.testapp.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tatyana on 05.04.15.
 */
public class AppUtil {
    /**
     * Returns a list with all links contained in the text
     */
    public static List<String> extractUrls(String text)
    {
        List<String> containedUrls = new ArrayList<>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find())
        {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }

    /**
     * Returns a map of url params
     */
    public static Map<String, String> extractParamsFromURL(final String url) throws URISyntaxException {
        return new HashMap<String, String>() {{
            for(NameValuePair p : URLEncodedUtils.parse(new URI(url), "UTF-8"))
                put(p.getName(), p.getValue());
        }};
    }

}

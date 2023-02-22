import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistsItemsRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class HelperMethods
{
    private static final HttpClient httpClient = HttpClientBuilder.create().build();


    public static Track[] GetPlaylistTracks(String playlistID, SpotifyApi api) throws IOException, ParseException, SpotifyWebApiException {

        GetPlaylistsItemsRequest.Builder request = api.getPlaylistsItems(playlistID); //create request builder for getPlaylistItems
        Paging<PlaylistTrack> pagingItem = request.build().execute(); //create reference to the paging object
        int numTracks = pagingItem.getTotal(); //get the total number of tracks

        PlaylistTrack[] tracks;
        Track[] trackList = new Track[numTracks]; //create array to store our track objects
        int numPages = numTracks/100 + 1; //find the number of pages for the playlist
        for(int x = 0; x < numPages; x++) //iterate through them
        {
            int tracksLeft = (x == numPages - 1) ? numTracks % 100: 100; //find the number of tracks on the page (it will be 100 unless it's the last page)
            pagingItem = request.offset((x * 100)).build().execute(); //request the next page of data from the API
            tracks = pagingItem.getItems(); //get the tracks
            for(int y = 0; y < tracksLeft; y++) //iterate through the tracks on the page
            {
                trackList[x * 100 + y] = ((Track) tracks[y].getTrack()); //add them to our array at the correct index
            }
        }

        return trackList; //return the array
    }

    public static String generateClientCredentialsToken(String client_id, String client_secret) throws IOException, InterruptedException
    {
        String encodeBytes = "Basic " + Base64.getEncoder().encodeToString((client_id + ":" + client_secret).getBytes()); // encode client id and secret in base 64
        HttpPost post = new HttpPost("https://accounts.spotify.com/api/token"); // create post request

        post.setHeader("Authorization", encodeBytes); // set authorization header
        post.setHeader("Content-Type", "application/x-www-form-urlencoded"); // set content type header

        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("grant_type", "client_credentials")); // set the grant type to client credentials, we aren't using
        post.setEntity(new UrlEncodedFormEntity(pairs));

        ClassicHttpResponse response = (ClassicHttpResponse) httpClient.execute(post); // execute the request
        HttpEntity entity = response.getEntity(); // get json data from the request
        String token = (new String(entity.getContent().readAllBytes(), StandardCharsets.UTF_8)).split("\"")[3]; // get the access token

        return token; // return it
    }
}


//by u tanna
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.io.*;
import java.util.*;

class GenrePercentage implements Comparable<GenrePercentage>
{
    private String genre;
    private double percentage;

    public GenrePercentage(String g, double p)
    {
        genre = g;
        percentage = p;
    }

    public String getGenre() {
        return genre;
    }

    public double getPercentage() {
        return percentage;
    }

    @Override
    public String toString() {
        return "Genre: " + genre + ", Percent Presence: " + percentage + "%";
    }

    public int compareTo(GenrePercentage other)
    {
        return Double.compare(getPercentage(), other.getPercentage());
    }
}

public class SpotifyLab
{
    static Scanner s = new Scanner(System.in);
    public static void main(String[] args) throws IOException, InterruptedException, ParseException, SpotifyWebApiException {
        Map<String, String> credentials = System.getenv();
        String token = HelperMethods.generateClientCredentialsToken(credentials.get("clientID"), credentials.get("clientSecret"));

        SpotifyApi spotifyApi = new SpotifyApi.Builder().setAccessToken(token).build();

        while(true)
        {
            System.out.print("Enter a Public Playlist ID (0 to exit): ");

            String id = s.nextLine();
            if(id.equals("0"))
                break;

            Track[] tracks = HelperMethods.GetPlaylistTracks(id, spotifyApi);

            System.out.println("Most frequent artist: " +  mostPopularArtist(tracks).getName());

            System.out.println("Originality Percentage: " + evaluateOriginality(tracks) + "%");

            GenrePercentage[] genrePercentages = evaluatePercentages(tracks, spotifyApi);
            for(GenrePercentage gp: genrePercentages)
            {
                System.out.println(gp);
            }
        }
    }

    //Students will have to write this helper method | here is one implementation
    static String[] readIDs(String fileName) throws IOException
    {
        return null; //for compilation
    }

    // returns a percentage based on how original a playlist is, uses popularTracks.txt (this is just one solution)
    static double evaluateOriginality(Track[] tracks) throws IOException
    {
        return 0; //for compilation
    }



    //Students will have to write this method | here is one implementation
    static ArtistSimplified mostPopularArtist(Track[] tracks)
    {
        return null; //for compilation
    }

    //returns each genre present, and what percent of it is found within the playlist (the genres won't add up to 100 as multiple genres overlap)
    public static GenrePercentage[] evaluatePercentages(Track[] tracks, SpotifyApi api) throws IOException, ParseException, SpotifyWebApiException
    {
        return null; //for compilation
    }

    //pre:  "fileName" is the name of a real file containing lines of text - the first line intended to be unused
    //post:returns a String array of all the elements in <filename>.txt, with index 0 unused (heap) O(n)
    public static String[] readFile(String fileName)throws IOException
    {
        int size = getFileSize(fileName);		//holds the # of elements in the file
        String[] list = new String[size];		//a heap will not use index 0;
        Scanner input = new Scanner(new FileReader(fileName));
        int i=0;											//index for placement in the array
        String line;
        while (input.hasNextLine())				//while there is another line in the file
        {
            line=input.nextLine();					//read in the next Line in the file and store it in line
            list[i]= line;								//add the line into the array
            i++;											//advance the index of the array
        }
        input.close();
        return list;
    }

        //pre:  "fileName" is the name of a real file containing lines of text
        //post: returns the number of lines in fileName O(n)
        public static int getFileSize(String fileName)throws IOException
        {
            Scanner input = new Scanner(new FileReader(fileName));
            int size=0;
            while (input.hasNextLine())				//while there is another line in the file
            {
                size++;										//add to the size
                input.nextLine();							//go to the next line in the file
            }
            input.close();									//always close the files when you are done
            return size;
        }
}

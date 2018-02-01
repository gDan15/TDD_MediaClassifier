import media.EpisodeInfo;
import media.MovieInfo;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.print.attribute.standard.Media;

public class FileMatcher {

    static final String EPISODE_RE_PATTERN = ".+\\.([S-s])(\\d+)([E-e])(\\d+)(\\.).+";
    static final Pattern EPISODE_RE = Pattern.compile(EPISODE_RE_PATTERN, Pattern.CASE_INSENSITIVE);

    
    
    static List<List<Object>> findMedia(Path directory) throws IOException {
        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException(directory.toString() + " is not a directory.");
        }

        List<Object> episodesFound = new ArrayList<Object>();
        List<Object> moviesFound = new ArrayList<Object>();
        List<List<Object>> found = new ArrayList<List<Object>>();
        DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory);

        for (Path p : dirStream) {
            if (EPISODE_RE.matcher(p.getFileName().toString()).matches()) {
    
            	episodesFound.add((Object) newEpisodeInfo(p));
                
            } else {
            	moviesFound.add(newMovieInfo(p));
            }
        }
        found.add(episodesFound);
        found.add(moviesFound);

        return found;
    }

    public static EpisodeInfo newEpisodeInfo(Path file) {
    	// (?i) for case insensitive
        Pattern p = Pattern.compile("(.+\\.)((?i)s\\d+(?i)e\\d+)(\\..+)");
        Matcher m = p.matcher(file.getFileName().toString());
        String name = "";
        Integer season = 0;
        Integer episode = 0;
        if (m.find()) {
        	// (?i) for case insensitive
            Pattern epi_season = Pattern.compile("((?i)s)(\\d+)((?i)e)(\\d+)");
            Matcher epi = epi_season.matcher(m.group(2));
            if (epi.find()) {
                season = Integer.parseInt(epi.group(2));
                episode = Integer.parseInt(epi.group(4));
            }
            name = capitalize(m.group(1).replaceAll("\\.", " "));

        }
        return new EpisodeInfo(file, name, season, episode);
    }
    
    public static MovieInfo newMovieInfo(Path file) {
    	//Check if film format is correct, the number between the dots represents the date. 
    	//Example : Avengers.2012.HdMovie
        Pattern p = Pattern.compile("(.+)\\.([12][0-9]{3})\\.(.+)");
        Matcher m = p.matcher(file.getFileName().toString());
        String name = "";

        if (m.find()) {

            name = capitalize(m.group(1).replaceAll("\\.", " "));
        }
        return new MovieInfo(file, name);
    }
    
    public static String capitalize(String s) {
        if (s.length() == 0) return s;
        String[] words = s.split("\\s");
        String r = "";
        for (String l : words) {
            r += l.substring(0, 1).toUpperCase() + l.substring(1).toLowerCase() + " ";
        }
        return r;
    }

}

package media;
import java.nio.file.Path;
public class MovieInfo {
    public Path file;
    public String movieName;


    public MovieInfo(Path filename, String title) {
        this.file = filename;
        this.movieName = title;
    }

    public Path getFile() {
        return file;
    }
    
    public String getMovieName(){
    		return movieName;
    }
}



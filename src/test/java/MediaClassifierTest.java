import media.EpisodeInfo;
import media.MovieInfo;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MediaClassifierTest {
    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    @Test
    public void shouldCapitalize() {
        String s = "DCs legends of Tomorrow";
        String res = FileMatcher.capitalize(s);
        Assert.assertEquals("Dcs Legends Of Tomorrow ", res);
    }

    @Test
    public void createFileInfo() throws IOException {
        Path dir = temp.newFolder("The.Flash.2014.S04E07.PROPER.1080p.HDTV.x264-CRAVERS[rarbg]").toPath();
        EpisodeInfo epi = FileMatcher.newEpisodeInfo(dir);
        EpisodeInfo res = new EpisodeInfo(dir, "The Flash 2014 ", 4, 7);

        Assert.assertEquals(res.getTvShowName(), epi.getTvShowName());
        Assert.assertEquals(res.getFile(), epi.getFile());
        Assert.assertEquals(res.getEpisodeNumber(), epi.getEpisodeNumber());
        Assert.assertEquals(res.getSeasonNumber(), epi.getSeasonNumber());
        Assert.assertTrue(Files.exists(dir));
    }

    @Test
    public void shouldFindEpisode() throws IOException {
        Path dir = temp.newFolder("Torrents").toPath();
        Path file = Files.createDirectory(dir.resolve(String.format("The.Flash.2014.S0%sE%s.PROPER.1080p.HDTV.x264-CRAVERS[rarbg]", 1, 10)));
        EpisodeInfo tmp = new EpisodeInfo(file, "The Flash 2014 ", 1, 10);
        List<List<Object>> media = FileMatcher.findMedia(dir);
        List<EpisodeInfo> episodes = (List<EpisodeInfo>) (List<?>) media.get(0);
        Assert.assertEquals(tmp.getTvShowName(), episodes.get(0).getTvShowName());
        Assert.assertEquals(tmp.getFile(), episodes.get(0).getFile());
        Assert.assertEquals(tmp.getEpisodeNumber(), episodes.get(0).getEpisodeNumber());
        Assert.assertEquals(tmp.getSeasonNumber(), episodes.get(0).getSeasonNumber());
        Assert.assertTrue(Files.exists(dir));
    }
    @Test
    public void shouldFindMovie() throws IOException{
    		Path dir = temp.newFolder("Movies").toPath();
    		Path file = Files.createDirectory(dir.resolve(String.format("Le.Regne.du.Feu.(Reign.Of.Fire).2002.MULTi.1080p.Bluray.HDLight.x264-Zone80")));
    		MovieInfo tmp = new MovieInfo(file, "Le Regne Du Feu (reign Of Fire)");
    		List<List<Object>> media = FileMatcher.findMedia(dir);
    		List<MovieInfo> movie = (List<MovieInfo>) (List<?>) media.get(1);
    		Assert.assertEquals(tmp.getFile(),movie.get(0).getFile());
    		Assert.assertEquals(tmp.getMovieName(),movie.get(0).getMovieName());
    		Assert.assertTrue(Files.exists(dir));
    }
}

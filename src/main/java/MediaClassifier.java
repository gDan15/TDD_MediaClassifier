import media.EpisodeInfo;
import media.TvShow;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MediaClassifier {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Frame("Ultra good media classifying system"));
    }
    
    /**
     * If the TvShow already exist then the episodes are added to the TvShow. If not, create the TvShow and add the episodes to it.
     */
    public static List<TvShow> buildFromRaw(List<EpisodeInfo> episodeInfoList) {
        List<TvShow> list = new ArrayList<>();
        for (EpisodeInfo episodeInfo : episodeInfoList) {
            boolean found = false;
            for (TvShow tvShow : list) {
                if (tvShow.getTitle().equals(episodeInfo.getTvShowName())) {
                    // tvshow exists -> add episode
                    found = true;
                    tvShow.addEpisode(episodeInfo);
                    break;
                }
            }
            if (!found) {
                //tvshow doesn't exist -> create tvshow
                list.add(new TvShow(episodeInfo.getTvShowName()));
                list.get(list.size() - 1).addEpisode(episodeInfo);
            }
        }
        return list;
    }
}
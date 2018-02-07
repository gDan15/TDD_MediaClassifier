import media.Episode;
import media.EpisodeInfo;
import media.Movie;
import media.MovieInfo;
import media.Season;
import media.TvShow;
import visitor.impl.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.io.PrintStream;

public class Panel extends JPanel{
    private static final long serialVersionUID = 1L;


    public Panel(){
        //Defining visual aspect of buttons and text fields
        JButton start = new JButton("Select");
        JLabel startText = new JLabel("Dossier de départ");
        JTextField startPath = new JTextField((20));
        JButton end = new JButton("Select");
        JLabel endText = new JLabel("Dossier de destination");
        JTextField endPath = new JTextField((20));
        JButton organiseSeries = new JButton("Start organise!");
        // Ajout de bouton --------------------------------------------------------------------------------------------
        
        JButton organiseMovies = new JButton("Start organise movies!");
        
        // Ajout de bouton --------------------------------------------------------------------------------------------
        
        final JFileChooser startchoice = new JFileChooser();
        // you can only choose a directory as start choice
        startchoice.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        // you are not allowed to select the "All files option"
        startchoice.setAcceptAllFileFilterUsed(false);

        final JFileChooser endchoice = new JFileChooser();
        // you can only choose a directory as endchoice
        endchoice.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        // you are not allowed to select the "All files option"
        endchoice.setAcceptAllFileFilterUsed(false);

        JTextArea textArea = new JTextArea(15, 30);
        TextAreaOutputStream taOutputStream = new TextAreaOutputStream(textArea);

        start.addActionListener(new ActionListener() {
            /**
             *
             * @param e
             * e is the event of clicking on the "select" button that will allow you to select the path.
             *
             * The aim is to give the path you chose to returnVal
             */
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == start) {
                    int returnVal = startchoice.showOpenDialog(start);
                    // Checking if the chosen file satisfies the contains of type
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = startchoice.getSelectedFile();
                        System.out.println("Opening: " + file.getAbsolutePath() + "." );
                        //shows the selected path in start text field
                        startPath.setText(file.getAbsolutePath());
                    } else {
                        System.out.print("Open command cancelled by user." );
                    }
                }
            }
        });

        end.addActionListener(new ActionListener() {
            /**
             *
             * @param e
             * e is the event of clicking on the "select" button that will allow you to select the path.
             *
             * The aim is to give the path you chose to returnVal
             */
            public void actionPerformed(ActionEvent e) {

                if (e.getSource() == end) {
                    int returnVal = endchoice.showOpenDialog(end);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = endchoice.getSelectedFile();
                        System.out.println("Opening: " + file.getAbsolutePath() + "." );
                        //shows the selected path in end text field
                        endPath.setText(file.getAbsolutePath());
                    } else {
                        System.out.print("Open command cancelled by user." );
                    }
                }
            }
        });

        organiseSeries.addActionListener(new ActionListener() {
            /**
             *
             * @param event
             *      "event" is the event of clicking on the start button which will initiate the organising.
             *      By clicking on the start button you initiate the organising and classifying methods.
             */
            public void actionPerformed(ActionEvent event) {
                //lancer l'organisation
                Path sourceDirectory = Paths.get(startPath.getText());

                List<EpisodeInfo> episodes = null;
//                List<MovieInfo> movies = null;
                try {
                	List<List<Object>> found = new ArrayList<List<Object>>();
                	found = FileMatcher.findMedia(sourceDirectory);
                    episodes = (List<EpisodeInfo>) (List<?>)found.get(0);
                    
//                    movies = (List<MovieInfo>) (List<?>)found.get(1);
                } catch (IOException e) {
                    System.out.println("There was a problem when reading the directory");
                    e.printStackTrace();
                }

                List<TvShow> list = MediaClassifier.buildFromRaw(episodes);
                         
                int i = 1;
                
                
                // Iterate each tvshow in list
                for (TvShow tvshow : list) {
                	//endPath.setText(file.getAbsolutePath());
                	Path endDir = Paths.get(endPath.getText());
                    //Path pat = (Path) endPath;
                    DirectoryCreationVisitor directory = new DirectoryCreationVisitor(endDir);
                    directory.visit(tvshow);
                    for (Map.Entry<Integer, Season> season : tvshow.getSeasons().entrySet()) {
                        System.out.printf("Tv Show Name: %s - Season: %s\n", tvshow.getTitle(), tvshow.getSeasons().get(i).getNumber());
                        i++;

                        int j = 1;
                        for (Map.Entry<Integer, Episode> episode : season.getValue().getEpisodes().entrySet()) {
                            System.out.println(episode.getValue().getNumber());
                          //  direct.visit(episode.getValue());
                        }
                        
                    }
                    i=1;
                    //System.out.printf("Tv Show Name: %s - Season: %s\n", tvshow.getTitle(), tvshow.getSeasons().size());
                }
            }
        });

//        
        organiseMovies.addActionListener(new ActionListener() {
            /**
             *
             * @param event
             *      "event" is the event of clicking on the start button which will initiate the organizing.
             *      By clicking on the start button you initiate the organizing and classifying methods.
             */
            public void actionPerformed(ActionEvent event) {
                //lancer l'organisation
                Path sourceDirectory = Paths.get(startPath.getText());
                List<MovieInfo> movies = null;
                try {
                	List<List<Object>> found = new ArrayList<List<Object>>();
                	found = FileMatcher.findMedia(sourceDirectory);
                	movies = (List<MovieInfo>) (List<?>)found.get(1);
                }catch (IOException e) {
                    System.out.println("There was a problem when reading the directory");
                    e.printStackTrace();
                }
              //lancer l'organisation
            	Path endDir = Paths.get(endPath.getText());
                //Path pat = (Path) endPath;
                DirectoryCreationVisitor directory = new DirectoryCreationVisitor(endDir);
                
                for(MovieInfo info : movies) {
                	Movie movie = new Movie(info.getFile(),info.getMovieName());
                	directory.visit(movie);
                }
            }
        });
//        
        
        // adds the the labels, texts and fields to the displayed panel
        add(startText);
        add(startPath);
        add(start);

        add(endText);
        add(endPath);
        add(end);

        add(organiseSeries);

        // Ajout de bouton --------------------------------------------------------------------------------------------
        
        add(organiseMovies);

        // Ajout de bouton --------------------------------------------------------------------------------------------
        
        add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        System.setOut(new PrintStream(taOutputStream));
    }





}

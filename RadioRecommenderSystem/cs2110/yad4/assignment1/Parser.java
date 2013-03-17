package cs2110.yad4.assignment1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser 
{	
	private Song[] songs;
	private Station[] stations;
	/* We are given the following format for 
	 * 
	 * station files:
	 *    Total number of stations
	 * 	  station1 name ;station1id
	 * 	  station2 name ;station2id
	 * Spaces can surround name but they should be trimmed.
	 * IDs are in no particular order.
	 * 
	 * song files:
	 * 	  Total number of songs
	 *    song1 name ; song1 artist ;songid1 
	 *    song2 name ; song2 artist ;songid2 
	 * Spaces can surround name or artist (or both)
	 * but they should be trimmed.
	 * IDs are in no particular order.
	 */
	/**
	 * Parses the files with information about the songs and stations and stores them in an array
	 * @param path of the directory the files live in
	 * @param the name of the file containing the songs
	 * @param the name of the file containing the stations
	 */
	public Parser(String path, String songFilename, String stationFilename) 
	{
		// read the station files
		File stationFile = new File(path + "\\" + stationFilename);
		Scanner scanner2 = null;
		try 
		{
			scanner2 = new Scanner(stationFile);
		}
		catch (FileNotFoundException e)
		{
			System.out.println("The file requested was not found.");
		}
		// parse the input and add it to the Station array
		int numStations = Integer.parseInt(scanner2.nextLine().trim());
		stations = new Station[numStations];
		for (int i = 0; i < numStations; i++)
		{
			if (scanner2.hasNextLine())
			{
				String rawStations = scanner2.nextLine(); 
				if (rawStations.length() != 0)
				{
					stations[i] = parseOneStation(rawStations);
				}
			}
		}
		scanner2.close(); 
		
		// read the song files
		File songFile = new File(path + "\\" + songFilename);
		Scanner scanner = null;
		try
		{
			scanner = new Scanner(songFile); //find constructor of Scanner that takes only a File object
		}
		catch (FileNotFoundException e)
		{
			System.out.println("The file requested was not found.");
		}
		// parse the input and add it to the Song array
		int numSongs = Integer.parseInt(scanner.nextLine().trim());
		songs = new Song[numSongs];
		for (int i = 0; i < numSongs; i++)
		{
			if (scanner.hasNextLine())
			{
				String rawSongs = scanner.nextLine();
				if (rawSongs.length() != 0)
				{
					songs[i] = parseOneSong(rawSongs, numStations);
				}
			}
		}
	}

	/* playlist files:
	 * 		stationid1;songid1
	 * 		stationid1;songid2
	 * They are in no particular order, Total numbers are not given; read
	 * till end of file. Songs may be repeated
	 */

	/**
	 * Processes the song log from the given file name.
	 * The file is assumed to be in the same path as given when
	 * the parser was constructed.
	 * Returns the current time after the log is processed.
	 * For instance, if given curTime is 1 and the log contain N plays,
	 * the returned time should be N+1.
	 * @param logFilename The name of the play log
	 * @param curTime The current time of the recommendation system
	 * @return The current time after importing the log
	 */
	public int processSongLog(String logFilename, int curTime) //send in System.in as logfile name when Parser done the first time around
	{
		// read the play log file
		int currentTime = curTime;
		File song_log = new File(logFilename);
		Scanner scanner = null;
		try {
			scanner = new Scanner(song_log);
		} catch (FileNotFoundException e) {
			System.out.println("The file requested was not found.");
		}
		
		// parse the lines and record the plays in the appropriate Song and Station objects
		while (scanner.hasNextLine())
		{
			String play = scanner.nextLine();
			String[] rawValues = play.split(";");
			
			//add a play to the song
			Song song = null;
			for (Song i : songs)
			{
				if (i.getID() == Integer.parseInt(rawValues[1])) //use .equals??
				{
					song = i;
				}
			}
			song.addPlay(Integer.parseInt(rawValues[0]), currentTime);
			//increment the playlist length of the station
			Station station = null;
			for (Station i : stations)
			{
				if (i.getID() == Integer.parseInt(rawValues[0]))
				{
					station = i;
				}
			}
			station.incrementLogLength();
			currentTime++;
		}
		return currentTime;
	}

	/**
	 * Takes in one line of input from the station file and creates an appropriate Station object
	 * @param line
	 * @return the Station object specified in the line
	 */
	public Station parseOneStation(String line)
	{
		String[] rawValues = line.split(";");
		Station temp = new Station(rawValues[0].trim(), Integer.parseInt(rawValues[1].trim()));
		return temp;
	}

	/**
	 * Takes in one line of input from the song file and creates an appropriate Song object
	 * @param line
	 * @param the total number of stations that the songs can be played on
	 * @return the Song object specified in the line
	 */
	public Song parseOneSong(String line, int numberOfStations)
	{
		String[] rawValues = line.split(";"); //songname, songartist, songid
		Song temp = new Song(rawValues[0].trim(), rawValues[1].trim(), Integer.parseInt(rawValues[2].trim()), stations);
		return temp;
	}

	/**
	 * Gets the array containing all the Song objects
	 * @return an array containing the Song objects
	 */
	public Song[] getSongs() {
		return songs;
	}

	/**
	 * Gets the array containing all the Station objects
	 * @return an array containing the Station objects
	 */
	public Station[] getStations() {
		return stations;
	}

}

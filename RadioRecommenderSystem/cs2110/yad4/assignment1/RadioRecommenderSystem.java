package cs2110.yad4.assignment1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RadioRecommenderSystem {

	/**
	 * Initializes the Parser and the RadioRecommenderSystem. Asks for user input through the console afterwards.
	 * Should keep asking for input indefinitely. The user can input the following commands:
	 *   importlog <log filename>            - Imports a play log appends it to the current log. 
	 *   similarsong <song ID>              - Finds the most similar song to the given song.
	 *   similarradio <station ID>          - Finds the most similar radio station to the given station.
	 *   stats <song ID>                    - Prints statistics of the given song.
	 *   lastheardon <station ID> <song ID> - Finds the most recent time the song is played on the station.
	 *   lastplayed <song ID>				- Finds the most recent time the song is played on any station.
	 *   recommend <station ID>             - Recommends a song to the given station.
	 *   exit                               - Exits the program.
	 * 
	 * @param args The first argument should contain the folder path for the three files. 
	 */
	public static void main(String[] args) 
	{
		// get the file directories
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Welcome to Yogisha's Radio Recommender System!");
		System.out.println("Please enter the name of the file containing the songs: ");
		String songFileName = null;
		try
		{
			songFileName = reader.readLine();
		}
		catch (IOException e)
		{
			System.out.println("Caught IOException: " + e.getMessage());
		}
		System.out.println("Please enter the name of the file containing the stations: ");
		String stationFileName = null;
		try
		{
			stationFileName = reader.readLine();
		}
		catch (IOException e)
		{
			System.out.println("Caught IOException: " + e.getMessage());
		}
		// initialize everything
		Parser parser = new Parser(args[0], songFileName, stationFileName);
		RadioRecommenderSystem recommender = new RadioRecommenderSystem(parser.getSongs(), parser.getStations());
		recommender.setCurTime(0);
		
		// give instructions to the user

		
		// begin reading input
		boolean ready = true;
		while (ready)
		{
			System.out.println("Please enter one of the following commands and press enter. Enclose the arguments in <>");
			System.out.println("importlog <log filename> - Imports a play log appends it to the current log.");
			System.out.println("similarsong <song ID> - Finds the most similar song to the given song.");
			System.out.println("similarradio <station ID> - Finds the most similar radio station to the given station.");
			System.out.println("stats <song ID> - Prints statistics of the given song.");
			System.out.println("lastheardon <station ID> <song ID> - Finds the most recent time the song is played on the station.");
			System.out.println("lastplayed <song ID> - Finds the most recent time the song is played on any station.");
			System.out.println("recommend <station ID> - Recommends a song to the given station.");
			System.out.println("exit - Exits the program.");
			String input = null;
			try 
			{
				input = reader.readLine();
				input = input.trim();
			}
			catch (IOException e)
			{
				System.out.println("Caught IOException: " + e.getMessage());
			}
			try
			{
				if (input.length() == 0)
				{
					System.out.println("Please enter a valid command.");
				}
				else if (input.startsWith("importlog"))
				{
					String logName = input.substring(input.indexOf("<") + 1, input.length() -1);
					int time = parser.processSongLog(args[0] + "\\" + logName, recommender.getCurTime());
					recommender.setCurTime(time);
					System.out.println("The additional song log data has been parsed.");
					ready = true;
				}
				else if (input.startsWith("similarsong"))
				{
					String songID = input.substring(input.indexOf("<") + 1, input.length() -1);
					Song similar =  recommender.closestSong(Integer.parseInt(songID));
					System.out.println(similar);
				}
				else if (input.startsWith("similarradio"))
				{
					String stationID = input.substring(input.indexOf("<") + 1, input.length() -1);
					Station similar = recommender.closestStation(Integer.parseInt(stationID));
					System.out.println(similar);
				}
				else if (input.startsWith("stats"))
				{
					String songID = input.substring(input.indexOf("<") + 1, input.length() -1);
					Song song = recommender.getSong(Integer.parseInt(songID));
					int[] stats = song.getStatistics();
					for (int i = 0; i < stats.length; i++)
					{
						System.out.println(stats[i]);
					}
				}
				else if (input.startsWith("lastheardon"))
				{
					String stationID = input.substring(input.indexOf("<") + 1, input.indexOf(">"));
					String songID = input.substring(input.indexOf("<", input.indexOf(">")) + 1, input.length() -1);
					System.out.println(recommender.lastHeardByStation(Integer.parseInt(stationID), Integer.parseInt(songID)));
				}
				else if (input.startsWith("lastplayed"))
				{
					String songID = input.substring(input.indexOf("<") + 1, input.length() -1);
					System.out.println(recommender.lastPlayed(Integer.parseInt(songID)));
				}
				else if (input.startsWith("recommend"))
				{
					String stationID = input.substring(input.indexOf("<") + 1, input.length() -1);
					System.out.println(recommender.bestRecommendation(Integer.parseInt(stationID)));
				}
				else if (input.startsWith("exit"))
				{
					reader.close();
					ready = false;
					System.out.println("Thank you. You have successfully exited the program");
				}
				else
				{
					System.out.println("Command not recognized. Please type in one of the pre-defined commands.");
				}
			}
			catch (IncorrectSongIDException e)
			{
				System.out.println("Caught IncorrectSongIDException: " + e.getMessage());
			}
			catch (IncorrectStationIDException e)
			{
				System.out.println("Caught IncorrectStationIDException: " + e.getMessage());
			}
			catch (InsufficientSongsException e)
			{
				System.out.println("Caught InsufficientSongsException: " + e.getMessage());
			}
			catch (InsufficientStationsException e)
			{
				System.out.println("Caught InsufficientStationsException: " + e.getMessage());
			}
			catch (IOException e)
			{
				System.out.println("Caught IOException: " + e.getMessage());
			}
			catch (ArithmeticException e)
			{
				System.out.println("No statistics available at this moment. Please import a play log.");
			}
		}
	}

	private Song[] localSongs;
	private Station[] localStations;
	private int currentTime;
	/**
	 * Constructor. Should store a local copy of the arrays.
	 * @param songs
	 * @param stations
	 */
	public RadioRecommenderSystem(Song[] songs, Station[] stations) 
	{
		localSongs = songs;
		localStations = stations;
		for (int i = 0; i < localStations.length; i++)
		{
			currentTime += localStations[i].getLogLength();
		}
		currentTime++;
	}

	/**
	 * Returns the current time, which is one more than the total number of plays.
	 * @return The current time of this recommendation system.
	 */
	public int getCurTime() 
	{
		return currentTime;
	}

	/**
	 * Sets the current time of this recommendation system.
	 * @param curTime The time to be set
	 */
	public void setCurTime(int curTime) 
	{
		currentTime = curTime;
	}
	
	/**
	 * Gets the Song object to which the specified songID corresponds.
	 * @param songID
	 * @return the Song object pertaining to the songID
	 */
	public Song getSong(int songID)
	{
		Song song = null;
		for (Song i : localSongs)
		{
			if (i.getID() == songID)
			{
				song = i;
			}
		}
		return song;
	}
	
	/**
	 * Gets the Station object to which the specified stationID corresponds.
	 * @param stationID
	 * @return the Station object pertaining to the stationID
	 */
	public Station getStation(int stationID)
	{
		Station station = null;
		for (Station i : localStations)
		{
			if (i.getID() == stationID)
			{
				station = i;
			}
		}
		return station;
	}
	
	/**
	 * Gets the number of times each song was played on the specified station.
	 * @param stationID
	 * @return an array of integers containing the number of times each song was played on this station
	 */
	private int[] getTimesStationPlayed(int stationID)
	{
		int[] times = new int[localSongs.length];
		for (int i = 0; i < localSongs.length; i++)
		{
			int[] stationIDs = localSongs[i].getStationID();
			int[] stationPlays = localSongs[i].getStationPlays();
			int index = 0;
			for (int j = 0; j < stationPlays.length; j++)
			{
				if (stationIDs[j] == stationID)
				{
					index = j;
				}
			}
			times[i] = stationPlays[index];
		}
		return times;
	}
	

	/**
	 * Returns the song which is most similar to the song with the corresponding songID
	 * @param songID ID of the original song
	 * @return Most similar song
	 * @throws InsufficientSongsException
	 * @throws IncorrectSongIDException
	 */
	public Song closestSong(int songID) throws InsufficientSongsException, IncorrectSongIDException 
	{
		if (localSongs.length <= 1)
		{
			throw new InsufficientSongsException();
		}
		Song original = getSong(songID);
		if (original == null)
		{
			throw new IncorrectSongIDException(songID);
		}
		Song similar = null;
		double similarity = 0;
		for (Song i : localSongs)
		{
			if (i.getID() != songID)
			{
				double tempSimilarity = songSimilarity(i, original);
				if (tempSimilarity > similarity)
				{
					similar = i;
				}
			}
		}
		return similar;
	}

	/**
	 * Computes the similarity of two given songs
	 * @param s1 First song
	 * @param s2 Second song
	 * @return Double representing the similarity between the songs
	 */
	public double songSimilarity(Song s1, Song s2) 
	{
		double similarity = 0;
		int[] stations1 = s1.getStationPlays();
		int[] stations2 = s2.getStationPlays();
		double numerator = 0;
		double denom1 = 0;
		double denom2 = 0;
		for (int i = 0; i < stations1.length; i++)
		{
			int s1Plays = stations1[i];
			int s2Plays = stations2[i];
			numerator += (s1Plays * s2Plays);
			denom1 += s1Plays*s1Plays;
			denom2 += s2Plays*s2Plays;
		}
		similarity = numerator / (Math.sqrt(denom1) * Math.sqrt(denom2));
		return similarity;
	}

	/**
	 * Returns the station which is most similar to the station with the corresponding radioID
	 * @param radioID ID of the original radio station
	 * @return Most similar radio station
	 * @throws InsufficientStationsException
	 * @throws IncorrectStationIDException
	 */
	public Station closestStation(int radioID)	throws InsufficientStationsException, IncorrectStationIDException 
	{
		if (localStations.length <= 1)
		{
			throw new InsufficientStationsException();
		}
		Station original = getStation(radioID);
		if (original == null)
		{
			throw new IncorrectStationIDException(radioID);
		}
		double similarity = 0;
		Station similar = null;
		for (Station i : localStations)
		{
			if (i.getID() != radioID)
			{
				double tempSimilarity = stationSimilarity(i, original);
				if (tempSimilarity > similarity)
				{
					similar = i;
				}
			}
		}
		return similar;
	}

	/**
	 * Computes the similarity of two given stations
	 * @param s1 First station
	 * @param s2 Second station
	 * @return Double representing the similarity between the stations
	 */
	public double stationSimilarity(Station s1, Station s2) 
	{
		double similarity = 0;
		int[] s1Plays = getTimesStationPlayed(s1.getID());
		int[] s2Plays = getTimesStationPlayed(s2.getID());
		double numerator = 0;
		double denom1 = 0;
		double denom2 = 0;
		for (int i = 0; i < s1Plays.length; i++)
		{
			numerator += s1Plays[i] * s2Plays[i];
			denom1 += Math.pow(s1Plays[i], 2);
			denom2 += Math.pow(s2Plays[i], 2);
		}
		similarity = numerator / (Math.sqrt(denom1) * Math.sqrt(denom2));
		return similarity;
	}

	/**
	 * Gets the song with the highest recommendation for the given station.
	 * @param radioID ID of the station for which we want a recommendation.
	 * @return Song which is most highly recommended for this station.
	 * @throws InsufficientStationsException
	 * @throws IncorrectStationIDException
	 */
	public Song bestRecommendation(int radioID) throws InsufficientStationsException, IncorrectStationIDException
	{
		if (localStations.length <= 1)
		{
			throw new InsufficientStationsException();
		}
		double recommendation = 0;
		Song best = null;
		for (Song i : localSongs)
		{
			double tempRecommendation = 0;
			try
			{
				if (lastHeardByStation(radioID, i.getID()) != 0)
				{
					tempRecommendation = makeRecommendation(radioID, i.getID());
				}
				if (tempRecommendation > recommendation)
				{
					recommendation = tempRecommendation;
					best = i;
				}
			}
			catch (IncorrectSongIDException e)
			{
				System.out.println("Caught IncorrectSongIDException: " + e.getMessage());
			}
		}
		return best;
	}

	/**
	 * Computes the recommendation of a given song to a particular radio station
	 * @param radioID ID of station being recommended to
	 * @param recSongID Recommended song ID
	 * @return Value indicating how highly recommended is the song
	 * @throws IncorrectStationIDException
	 * @throws IncorrectSongIDException
	 */
	
	//actually throw exceptions!!!
	public double makeRecommendation(int radioID, int recSongID) throws IncorrectStationIDException, IncorrectSongIDException 
	{
		Station station = getStation(radioID);
		if (station == null)
		{
			throw new IncorrectStationIDException(radioID);
		}
		Song recSong = getSong(recSongID);
		if (recSong == null)
		{
			throw new IncorrectSongIDException(recSongID);
		}
		double aStation = station.getLogLength() / localSongs.length;
		double numerator = 0;
		double denominator = 0;
		for (Station i : localStations)
		{
			if (station.getID() != i.getID())
			{
				double ai = i.getLogLength() / localSongs.length;
				int index = 0;
				for (int j = 0; j < localSongs.length; j++)
				{
					if (localSongs[j].getID() == recSongID)
					{
						index = j;
					}
				}
				int ci = getTimesStationPlayed(radioID)[index];
				double similarity = stationSimilarity(station, i);
				numerator += ((ci - ai)*similarity);
				denominator += similarity;
			}
		}
		double rightSide = (numerator / denominator) + aStation;
		double ts = getCurTime() / lastHeardByStation(radioID, recSongID);
		double leftSide = Math.pow(Math.E, -1)/ts;
		return rightSide * leftSide;
	}

	/**
	 * Returns the most recent time the given song is played on the given station.
	 * @param radioID ID of station
	 * @param songID ID of song
	 * @return The most recent time the given song is played on the given station,
	 * or 0 if this song was never played on this station.
	 * @throws IncorrectStationIDException
	 * @throws IncorrectSongIDException
	 */
	public int lastHeardByStation(int radioID, int songID) throws IncorrectStationIDException, IncorrectSongIDException 
	{
		Song song = getSong(songID);
		if (song == null)
		{
			throw new IncorrectSongIDException(songID);
		}
		int time = 0;
		try
		{
			time = song.getLastHeardByStation(radioID);
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			throw new IncorrectStationIDException(radioID);
		}
		return time;
	}

	/**
	 * Returns the most recent time the given song is played on any station.
	 * @param songID ID of song
	 * @return The most recent time the given song is played on any station,
	 * or 0 if this song was never played.
	 * @throws IncorrectSongIDException
	 */
	public int lastPlayed(int songID) throws IncorrectSongIDException 
	{
		Song song = getSong(songID);
		if (song == null)
		{
			throw new IncorrectSongIDException(songID);
		}
		int time = song.getLastPlayTime();
		return time;
	}	

	public class IncorrectSongIDException extends Exception {
		private static final long serialVersionUID = 1L;
		public int wrongID;

		public IncorrectSongIDException(int ID) {
			super("Song with ID #" + ID + " doesn't exist.");
			wrongID = ID;
		}
	}

	public class IncorrectStationIDException extends Exception {
		private static final long serialVersionUID = 1L;
		public int wrongID;

		public IncorrectStationIDException(int ID) {
			super("Station with ID #" + ID + " doesn't exist.");
			wrongID = ID;
		}
	}

	public class InsufficientSongsException extends Exception {
		private static final long serialVersionUID = 1L;

		public InsufficientSongsException() {
			super("Insufficient number of songs required for operation.");
		}
	}

	public class InsufficientStationsException extends Exception {
		private static final long serialVersionUID = 1L;

		public InsufficientStationsException() {
			super("Insufficient number of stations required for operation.");
		}
	}
}

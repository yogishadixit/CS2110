package cs2110.yad4.assignment1;

public class Song 
{	
	private String songName;
	private String songArtist;
	private int songID;
	private int[] stationID;
	private int[] stationPlays;
	private int[] lastHeard;
	
	/**
	 * Creates a song object.
	 * @param name of the song
	 * @param artist of the song
	 * @param ID of the song
	 * @param an array containing all the stations 
	 */
	public Song(String name, String artist, int ID, Station[] stations) 
	{
		songName = name;
		songArtist = artist;
		songID = ID;
		stationID = new int[stations.length];
		stationPlays = new int[stations.length];
		lastHeard = new int[stations.length];
		for (int i = 0; i < stations.length; i++)
		{
			stationID[i] = stations[i].getID();
		}
	}
	
	/**
	 * Returns the name of the song.
	 * @return name of the song
	 */
	public String getName() 
	{
		return songName;
	}

	/**
	 * Returns the name of the artist of the song.
	 * @return artist of the song
	 */
	public String getArtist()
	{
		return songArtist;
	}

	/**
	 * Returns the ID of the song.
	 * @return the ID of the song
	 */
	public int getID() {
		return songID;
	}
	
	/**
	 * Returns the station plays array of the song object.
	 * @return the station plays array of the song object
	 */
	public int[] getStationPlays()
	{
		return stationPlays;
	}
	
	/**
	 * Returns the ID of the stationID 
	 * @return
	 */
	public int[] getStationID()
	{
		return stationID;
	}
	
	/**
	 * Given a specific station ID, it returns its index in the stationID array
	 * @param stationId
	 * @return the index value in the stationID array
	 */
	private int getStation(int stationId)
	{
		int index = -1;
		for (int i = 0; i < stationID.length; i++)
		{
			if (stationID[i] == stationId)
			{
				index = i;
			}
		}
		return index;
	}
	
	/**
	 * Returns the time this song was last played by the given station.
	 * @param stationID The ID of the given station
	 * @return the time this song was last played by the given station
	 */
	public int getLastHeardByStation(int stationID)
	{
		int index = getStation(stationID);
		return lastHeard[index];
	}
	/**
	 * Returns the time this song was last played by any station.
	 * @return The time this song was last played by any station
	 */
	public int getLastPlayTime()
	{
		int last_play_time = 0;
		for (int i : lastHeard)
		{
			if (last_play_time < i)
			{
				last_play_time = i;
			}
		}
		return last_play_time;
	}

	/**
	 * Records that the song has been played on a certain station at a certain time.
	 * @param stationId the song was played on
	 * @param time the song was played on that station
	 */
	public void addPlay(int stationId, int time)
	{
		int index = getStation(stationId);
		stationPlays[index] += 1;
		lastHeard[index] = time;
	}

	/* The returned array should be in this order:
	 * [ avg plays on each station that carries it |
	 * total number of plays across all stations|
	 * this song is played most often on this station | 
	 * max plays on one station |
	 * this song is played least often on this station |
	 * min plays on one station |
	 * ]
	 */
	/**
	 * Returns the statistics of this song in the following order: average plays on each station that carries it
	 * total number of plays across all stations, the station this song is played most on, the max number of times
	 * this song has been played on any station, the station this song is played the least on, the min number of
	 * times this song has been played on any station.
	 * @return an array of the statistics
	 */
	public int[] getStatistics()
	{
		int stationsPlayed = 0, plays = 0, maxPlays = 0, minPlays = stationPlays[0], maxId = 0, minId = 0;
		for (int i = 0; i < stationID.length; i++)
		{
			if (stationPlays[i] != 0)
			{
				stationsPlayed++;
				plays += stationPlays[i];
			}
			if (stationPlays[i] >= maxPlays)
			{
				if (maxPlays == stationPlays[i])
				{
					if(maxId > stationID[i])
					{
						maxId = stationID[i];
					}
				}
				else
				{
					maxPlays = stationPlays[i];
					maxId = stationID[i];
				}
			}
			if (stationPlays[i] <= minPlays)
			{
				if (minPlays == stationPlays[i])
				{
					if (minId < stationID[i])
					{
						minId = stationID[i];
					}
				}
				else
				{
					minPlays = stationPlays[i];
					minId = stationID[i];
				}
			}
		}
		int[] stats = new int[6];
		stats[0] = plays/stationsPlayed;
		stats[1] = plays;
		stats[2] = maxId;
		stats[3] = maxPlays;
		stats[4] = minId;
		stats[5] = minPlays;
		return stats;
	}

	/**
	 * Returns the last time this song was played on the specified station
	 * @param stationID
	 * @return the latest time this song was played on the station
	 */
	public int getLastPlayed(int stationID) 
	{
		int index = getStation(stationID);
		return lastHeard[index];
	}	
	
	/**
	 * Returns a string representation of the parameters of the song.
	 * @return a string containing the ID, the artist name and the name of the song
	 */
	public String toString()
	{
		return songID + ". " + songArtist + " - " + songName;
	}
}

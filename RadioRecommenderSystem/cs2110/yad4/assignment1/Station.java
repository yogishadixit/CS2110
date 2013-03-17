package cs2110.yad4.assignment1;

public class Station
{
	private String stationName;
	private int stationID;
	private int playlistLength;
	
	/**
	 * Create a station object.
	 * @param name of the station
	 * @param ID of the station
	 */
	public Station(String name, int ID) 
	{
		stationName = name;
		stationID = ID;
		playlistLength = 0;
	}
	
	/**
	 * Returns the name of the station
	 * @return Name of the station
	 */
	public String getName() 
	{
		return stationName;
	}
	
	/**
	 * Returns the ID of the station
	 * @return ID of the station
	 */
	public int getID() 
	{
		return stationID;
	}
	
	/**
	 * Returns the length of the station's play list
	 * @return The station's play list
	 */
	public int getLogLength() 
	{
		return playlistLength;
	}
	
	/**
	 * Increase the length of the play list of the station by one.
	 */
	public void incrementLogLength() 
	{
		playlistLength++;
	}
	
	@Override
	/**
	 * Returns a string representation of the parameters of the station.
	 * @return a string containing the ID and the name of the station
	 */
	public String toString() 
	{
		return stationID + ". " + stationName;
	}
}

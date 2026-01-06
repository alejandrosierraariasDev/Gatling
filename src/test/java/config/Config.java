package config;

public class Config {
    public static final String BASE_URL = System.getProperty("baseUrl", "https://videogamedb.uk/api");
    public static final int USERS = Integer.getInteger("users", 5);
    public static final int RAMP_DURATION = Integer.getInteger("ramp", 10);
}
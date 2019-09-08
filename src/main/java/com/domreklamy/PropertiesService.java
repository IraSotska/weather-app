package com.domreklamy;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesService {

  private static PropertiesService instance;
  public static PropertiesService getInstance() {
    if (instance == null) {
      synchronized (PropertiesService.class) {
        if (instance == null) {
          instance = new PropertiesService();
        }
      }
    }
    return instance;
  }


  public final String openWeatherMapKey;
  public final String openWeatherMapBaseUrl;
  public final String defaultDestinationFileName;
  public final String defaultCity;

  private PropertiesService() {
    Properties property = new Properties();

    try (InputStream is = PropertiesService.class.getClassLoader().getResourceAsStream("config.properties")) {
      property.load(is);
      this.openWeatherMapKey = property.getProperty("open-weather-map.appid");
      this.openWeatherMapBaseUrl = property.getProperty("open-weather-map.base-url");
      this.defaultDestinationFileName = property.getProperty("default-destination-file-name");
      this.defaultCity = property.getProperty("default-city");
    } catch (IOException e) {
      throw new RuntimeException("Can't load configuration properties", e);
    }
  }

}

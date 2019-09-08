package com.domreklamy;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.ResponseBody;


public class Controller {

  private final OkHttpClient client = new OkHttpClient();
  private final ObjectMapper mapper = new ObjectMapper();
  private final PropertiesService props = PropertiesService.getInstance();

  public TextField fileNameTextField;
  public TextField fileNameTextField2;
  public TextField cityTextField;
  public TextArea outputTextArea;

  public void onGetWeatherClicked(MouseEvent mouseEvent) throws IOException {

    String city = cityTextField.getText().isEmpty()
        ? props.defaultCity
        : cityTextField.getText();
    String destination = cityTextField.getText().isEmpty()
        ? props.defaultDestinationFileName
        : fileNameTextField.getText();
    resetOutput(destination);

    String jsonResponce = callForData(city);
    if (jsonResponce != null) {
      JsonNode jsonNode = mapper.readTree(jsonResponce);

      try (FileWriter fw = new FileWriter(destination)) {
        appendOutput(fw, "city", city);
        appendOutput(fw, "temp", jsonNode.get("main").get("temp").asText());
        appendOutput(fw, "pressure", jsonNode.get("main").get("pressure").asText());
        appendOutput(fw, "humidity", jsonNode.get("main").get("humidity").asText());
        appendOutput(fw, "windSpeed", jsonNode.get("wind").get("speed").asText());
        appendOutput(fw, "windDeg", jsonNode.get("wind").get("deg").asText());
      }
    }

  }

  private String callForData(String city) throws IOException {
    Request request = new Request.Builder()
        .url(props.openWeatherMapBaseUrl + "/data/2.5/weather?q=" + city + "&appid=" + props.openWeatherMapKey)
        .build();
    Response response = client.newCall(request).execute();

    ResponseBody body = response.body();

    if (response.code() == 404 || response.body() == null) {
      return null;
    } else {
      return body.string();
    }
  }

  private void resetOutput(String destination) throws IOException {
    try (FileWriter fw = new FileWriter(destination)) {
      fw.write("");
    }
    outputTextArea.setText("");
  }

  private void appendOutput(Writer writer, String key, String value) throws IOException {
    String formattedStr = key + " = " + value + "\n";
    writer.append(formattedStr);
    outputTextArea.appendText(formattedStr);
  }

}

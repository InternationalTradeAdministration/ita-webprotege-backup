package webprotegebackup;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.TimerTrigger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Function {

  @FunctionName("TimerTrigger-Java")
  public String functionHandler(
    @TimerTrigger(name = "backup", schedule = "0 0 0 * * *") String timerInfo, final ExecutionContext executionContext) {
    executionContext.getLogger().info("Timer trigger input: " + timerInfo);

    Storage storage = new ProductionStorage();

    Map<String, String> webprotegeProjects = new HashMap<>();
    webprotegeProjects.put("SKOS with UNESKOS", System.getenv("SKOS_WITH_UNESKOS_URL"));
    webprotegeProjects.put("SKOS", System.getenv("SKOS_URL"));

    try {
      for (String project : webprotegeProjects.keySet()) {
        String url = webprotegeProjects.get(project);
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        if (responseCode == 200) {
          storage.save(project + ".zip", inputStreamToBytes(con.getInputStream()));
        } else {
          executionContext.getLogger().severe("ERROR: Couldn't download zip file from WebProtege" + project);
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    return "From timer: \"" + timerInfo + "\"";
  }

  private byte[] inputStreamToBytes(InputStream inputStream) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    int reads = inputStream.read();
    while (reads != -1) {
      baos.write(reads);
      reads = inputStream.read();
    }
    return baos.toByteArray();
  }

}

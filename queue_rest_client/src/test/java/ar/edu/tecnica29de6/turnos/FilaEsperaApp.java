package ar.edu.tecnica29de6.turnos ;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Timer;
import java.util.TimerTask;

public class FilaEsperaApp {
    private static final String API_URL = "http://localhost:8080/api/turnos/fila"; // Cambia esto por la URL real

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                fetchFilaEspera();
            }
        }, 0, 2000); // Ejecutar cada 2 segundos
    }

    private static void fetchFilaEspera() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(API_URL);
            HttpResponse response = httpClient.execute(request);
            String result = EntityUtils.toString(response.getEntity());

            // Aquí puedes procesar y mostrar la fila de espera
            System.out.println("Fila de espera: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

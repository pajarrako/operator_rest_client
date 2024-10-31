package ar.edu.tecnica29de6.turnos;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Scanner;

public class TurnoApp {
    private static String API_URL;
    private static final Gson gson = new Gson();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            mostrarMenu();
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1:
                    API_URL = "http://localhost:8080/api/turnos/asignar";
                    asignarTurno();
                    break;
                case 2:
                    API_URL = "http://localhost:8080/api/turnos/cambiar-puesto";
                    cambiarPuesto();
                    break;
                case 3:
                    API_URL = "http://localhost:8080/api/turnos/atender";
                    atenderTurno();
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    return;
                default:
                    System.out.println("Opción inválida, por favor intenta nuevamente.");
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("Menú de Operaciones:");
        System.out.println("1 - Asignar turno");
        System.out.println("2 - Cambiar puesto de atención");
        System.out.println("3 - Atender");
        System.out.println("0 - Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static void asignarTurno() {
        System.out.print("Ingrese CUIL: ");
        String cuil = scanner.nextLine();
        System.out.print("Ingrese nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese apellido: ");
        String apellido = scanner.nextLine();

        String json = gson.toJson(new Turno(cuil, nombre, apellido));
        enviarSolicitud(json);
    }

    private static void cambiarPuesto() {
        System.out.print("Ingrese número de turno: ");
        String numeroTurno = scanner.nextLine();
        System.out.print("Ingrese nuevo puesto de atención: ");
        String puestoAtencion = scanner.nextLine();

        String json = gson.toJson(new CambioPuesto(numeroTurno, puestoAtencion));
        enviarSolicitud(json);
    }

    private static void atenderTurno() {
        System.out.print("Ingrese número de turno: ");
        String numeroTurno = scanner.nextLine();
        System.out.print("Ingrese estado pendiente: ");
        String estadoPendiente = scanner.nextLine();
        System.out.print("Ingrese estado llamado: ");
        String estadoLlamado = scanner.nextLine();

        String json = gson.toJson(new Atender(numeroTurno, estadoPendiente, estadoLlamado));
        enviarSolicitud(json);
    }

    private static void enviarSolicitud(String json) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(API_URL);
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(json));

            HttpResponse response = httpClient.execute(post);
            String result = EntityUtils.toString(response.getEntity());

            System.out.println("Respuesta de la API: " + result);
        } catch (Exception e) {
            System.err.println("Error al comunicarse con la API: " + e.getMessage());
            e.printStackTrace(); // Agregado para más detalles
        }
    }

    // Clases internas para representar los datos
    static class Turno {
        private String cuil;
        private String nombre;
        private String apellido;

        public Turno(String cuil, String nombre, String apellido) {
            this.cuil = cuil;
            this.nombre = nombre;
            this.apellido = apellido;
        }
    }

    static class CambioPuesto {
        private String numeroTurno;
        private String puestoAtencion;

        public CambioPuesto(String numeroTurno, String puestoAtencion) {
            this.numeroTurno = numeroTurno;
            this.puestoAtencion = puestoAtencion;
        }
    }

    static class Atender {
        private String numeroTurno;
        private String estadoPendiente;
        private String estadoLlamado;

        public Atender(String numeroTurno, String estadoPendiente, String estadoLlamado) {
            this.numeroTurno = numeroTurno;
            this.estadoPendiente = estadoPendiente;
            this.estadoLlamado = estadoLlamado;
        }
    }
}
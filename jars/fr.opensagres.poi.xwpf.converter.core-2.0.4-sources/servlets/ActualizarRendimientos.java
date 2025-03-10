/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jsf.SampleController;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import jpa.session.SampleFacade;
import java.util.regex.*;


/**
 *
 * @author Juan Antonio Villalba Luna
 */
@WebServlet(name = "ActualizarRendimientos", urlPatterns = {"/ActualizarRendimientos"})
public class ActualizarRendimientos extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @EJB
    private SampleFacade sampleFacade;

    @Inject
    private SampleController sampleController; // Inyectamos el controlador


@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        String nombreArchivo = "desconocido"; // Valor predeterminado si no se encuentra

        // Leer el contenido del JSON en un String
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }

        String jsonString = jsonBuffer.toString();

        // Intentar extraer el nombre del archivo con una expresión regular
        Pattern pattern = Pattern.compile("\"archivo\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(jsonString);
        if (matcher.find()) {
            nombreArchivo = new File(matcher.group(1)).getName(); // Extraer solo el nombre del archivo
        }

        Gson gson = new Gson();
        JsonObject requestJson;

        try {
            requestJson = gson.fromJson(jsonString, JsonObject.class);

            if (!requestJson.has("archivo") || !requestJson.has("rendimientos")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"El JSON debe contener los campos 'archivo' y 'rendimientos'.\"}");
                return;
            }

            JsonArray rendimientos = requestJson.getAsJsonArray("rendimientos");

            if (rendimientos == null || rendimientos.size() == 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\n  \"error\":\"El array 'rendimientos' está vacío o no es válido en el archivo: " + nombreArchivo + "\"\n}\n");
                return;
            }

            int totalMuestras = rendimientos.size();
            int actualizadas = 0;
            int noActualizadas = 0;
            StringBuilder errores = new StringBuilder();
            StringBuilder mensajeUpdateRendimientos = new StringBuilder();

            for (JsonElement element : rendimientos) {
                if (!element.isJsonObject()) {
                    errores.append("\n- Uno de los elementos en 'rendimientos' no es un objeto JSON válido.");
                    continue;
                }

                JsonObject item = element.getAsJsonObject();

                if (!item.has("Project_id") || !item.has("Sample_id") || !item.has("sample_name") || !item.has("Performance")) {
                    errores.append("\n- Faltan campos obligatorios en una muestra del archivo: ").append(nombreArchivo);
                    continue;
                }

                int idSample = -1;

                try {
                    String idProject = item.has("Project_id") ? item.get("Project_id").getAsString() : nombreArchivo;
                    idSample = item.get("Sample_id").getAsInt();
                    String sampleName = item.get("sample_name").getAsString();
                    int performance = item.get("Performance").getAsInt();

                    Boolean realPerformanceActualizada = sampleFacade.sampleUpdateRealPerformance(idProject, idSample, performance);

                    if (realPerformanceActualizada) {
                        actualizadas++;
                    } else {
                        noActualizadas++;
                        errores.append(" Error en BD al actualizar sample ID=")
                                .append(idSample)
                                .append(" (No se encuentra el id de proyecto asociado a la muestra). ");
                    }
                } catch (Exception e) {
                    noActualizadas++;
                    errores.append(" Error en los valores de la muestra ID=")
                            .append(idSample == -1 ? "desconocido" : idSample)
                            .append(" (conflictos en los valores de los campos del JSON-BD).");
                }
            }

            mensajeUpdateRendimientos.append("Del archivo: ").append(nombreArchivo)
                    .append(" se actualizaron ").append(actualizadas).append(" de ").append(totalMuestras).append(" muestras.");

            if (noActualizadas > 0) {
                mensajeUpdateRendimientos.append(" No se actualizaron ").append(noActualizadas)
                        .append(" muestras debido a errores.");
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            StringBuilder jsonResponse = new StringBuilder();
            jsonResponse.append("{\n")
                    .append("  \"message\": \"").append(mensajeUpdateRendimientos.toString().replace("\n", "\\n")).append("\"");

            if (errores.length() > 0) {
                jsonResponse.append(",\n  \"errors\": \"").append(errores.toString().replace("\n", "\\n")).append("\"");
            }

            jsonResponse.append("\n}\n");

            response.getWriter().write(jsonResponse.toString());

        } catch (JsonSyntaxException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\n  \"error\":\"Formato JSON inválido en el archivo: " + nombreArchivo + "\"\n}\n");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\n  \"error\":\"Error interno al procesar el archivo JSON.\"\n}\n");
        }
    }

}

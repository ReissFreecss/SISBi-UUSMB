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
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import jpa.session.SampleFacade;

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
        // Leer JSON del cuerpo de la petición
        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }
        
        // Convertir JSON a un objeto Java        
        Gson gson = new Gson();
        
        List list = new ArrayList<>();
        //  JsonArray performance = gson.fromJson(jsonBuffer.toString(), JsonArray.class);
        JsonObject RequestJson = gson.fromJson(jsonBuffer.toString(), JsonObject.class);
        
        //  Project_id
        //  Sample_id
        //  sample_name
        //  Performance
        String nombre_archivo = RequestJson.get("archivo").getAsString(); // "240219_NS500502_0189_AHHTJYBGXJ_Rendimientos.js";
        JsonArray Rendimientos = RequestJson.get("rendimientos").getAsJsonArray();
        int total_muestras = Rendimientos.size();
        int actualizadas = 0;
        int no_actualizadas = 0;
        String mensajeUpdateRendimientos = "";
                        
        for(JsonElement element : Rendimientos) {
            JsonObject item = gson.fromJson(element, JsonObject.class);            
            
            //  21/feb/2025            
            String IdProject = item.get("Project_id").getAsString();
            int IdSample =  item.get("Sample_id").getAsInt();
                        
            Boolean real_performance_actualizada = sampleFacade.sampleUpdateRealPerformance(IdProject, IdSample, item.get("Performance").getAsInt());
            
            if( real_performance_actualizada ) {
                actualizadas++;
            }
            else {
                no_actualizadas++;
                mensajeUpdateRendimientos +="\n- El REAL_PERFORMANCE de la muestra ID("+IdSample+") en el Projecto ID("+IdProject+") no se actualizo.";
            }
        }
        
        mensajeUpdateRendimientos  = "Del archivo "+ nombre_archivo +"\n Se actualizaron "+ actualizadas + " de "+ total_muestras + " muestras.";
        
        if(no_actualizadas > 0) {
            mensajeUpdateRendimientos += "\n\nNo se actualizaron "+no_actualizadas+" de "+ total_muestras +" muestras"
                                      + mensajeUpdateRendimientos;
        }
                                     
                
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\":\""+mensajeUpdateRendimientos+"\"}");        
    }       
}

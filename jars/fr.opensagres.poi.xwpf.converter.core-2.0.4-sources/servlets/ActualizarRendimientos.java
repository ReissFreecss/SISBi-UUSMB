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
import jpa.entities.Sample;
import jpa.session.AbstractFacade;
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

        //  Type tipoWrapperSample = new TypeToken<Wrapper<Sample>>(){}.getType();
        // Convertir JSON a un objeto Java        
        Gson gson = new Gson();
        
        //  Wrapper<Sample> wrapperSample = gson.fromJson(jsonBuffer.toString(), tipoWrapperSample);
        //  Wrapper<Sample> wrapperSample = gson.fromJson(jsonBuffer.toString(), tipoWrapperSample);
        //  System.out.print(wrapperSample);
        
        List list = new ArrayList<>();
        JsonArray performance = gson.fromJson(jsonBuffer.toString(), JsonArray.class);
        
        //  Project_id
        //  Sample_id
        //  sample_name
        //  Performance
        
        for(JsonElement element : performance) {
            JsonObject item = gson.fromJson(element, JsonObject.class);            
            
            //  21/feb/2025
            //  Ok pero requiere ver por que marca error NullPointerException
            String IdProject = item.get("Project_id").getAsString();
            int IdSample =  item.get("Sample_id").getAsInt();
            
            //  System.out.print(item.get("Project_id"));            
            
            //  Sample sample = sampleFacade.sampleByIdProjectIdSample(IdProject, IdSample);
            //  sample.setRealPerformance(item.get("Performance").getAsString());
            sampleFacade.sampleUpdateRealPerformance(IdProject, IdSample, item.get("Performance").getAsString());
        }
        
        /*
        // Guardar los datos usando el controlador
        sampleController.actualizarSampleRealPerformance();
        */
        // Responder al cliente
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\":\"Performance actualizados de las muestras solicitadas\"}");        
    }       
}

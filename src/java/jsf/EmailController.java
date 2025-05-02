/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import jpa.entities.BioinformaticAnalysis;
import jpa.entities.Project;
import jpa.entities.ReportProject;
import jpa.entities.Sample;
import jpa.entities.UserProjectLink;
import jpa.entities.Users;
import jpa.session.UserProjectLinkFacade;

/**
 *
 * @author Leslie_mat
 */
public class EmailController {

    
// Constantes que indican los emails de cada uno de las responsables   
    private static final String MANAGER1 = "sisbi@ibt.unam.mx";
    private static final String MANAGER2 = "yoyis@ibt.unam.mx"; //gloria
    private static final String MANAGER3 = "rgrande@ibt.unam.mx";//ricardo
    private static final String MANAGER4 = "uusmb@ibt.unam.mx "; //todo uusmb
    private static final String MANAGER5 = "carlos.perez@ibt.unam.mx";//carlos
    private static final String MANAGER6 = "alexsf@ibt.unam.mx"; //alejandro
    private static final String MANAGER7 = "lizeth.matias@ibt.unam.mx"; // liz
    private static final String MANAGER8 = "ilse.salinas@ibt.unam.mx";//ilse
    private static final String MANAGER9 = "estefania.herrera@ibt.unam.mx "; //fani
    
//-------------------------------------------------------------------

    // Carlos Perez Calderon - 29-04-2025
    // Contraseñas actualizadas a versiones más seguras
    /*/ Evitando exponer directamente estas credenciales sensibles en el código fuente.
    private static final String HOST = "smtp.gmail.com";
    private static final String SENDER = "sisbi@ibt.unam.mx";
    private static final String USER = "sisbi@ibt.unam.mx";
    private static final String PASS = "whyn zcci kxbx lhpd";*/
    
    
    
    //test
    private static final String HOST = "smtp.gmail.com";
    private static final String SENDER = "NoEnviar_sisbi@ibt.unam.mx";
    private static final String USER = "NoEnviar_sisbi@ibt.unam.mx";
    private static final String PASS = "NoEnviar_whyn zcci kxbx lhpd";


    //private static final int PORT = 587;
    private final Properties properties = new Properties();
    private Session session;
    private static final String FOOTER = "\nCualquier duda o sugerencia, puede hacérnosla saber al correo uusmb@ibt.unam.mx \n"
            + "\n"
            + "Gracias por su preferencia.\n"
            + "\n"
            + "Atte: El equipo UUSMB.\n"
            + "http://www.uusmb.unam.mx/contacto.html";
    private static final String WARNING = "\n(Este mensaje se genera automáticamente y con "
            + "fines exclusivamente informativos y no debe ser contestado dado que su respuesta no será revisada)";
    
    private static final String CABECERA=
            " <table  cellspacing='5' cellpadding='15'>"
            +"<tr><td  bgcolor='#FFFFFF'>"
            +"            <a href='http://www.uusmb.unam.mx/'><img src='http://www.uusmb.unam.mx/SISBI/uusmb/javax.faces.resource/new_logo.png?ln=images' align='left' height='70px'></a>"
            +"            <a href='http://www.uusmb.unam.mx/SISBI/'><img src='http://www.uusmb.unam.mx/imagenes/IR_SISBI.png' align='right' height='50px'></a>"
            +"</td></tr> "
            ;
    private static final String REDES=
            "        <strong>Síguenos:</strong>"
            +"        <a href='https://www.facebook.com/profile.php?id=100086445368260'><img src='http://www.uusmb.unam.mx/imagenes/facebook-rounded-gray.png' height='30px' align='center'></a>"
            +"        <a href='https://twitter.com/uusmb_unam'><img src='http://www.uusmb.unam.mx/imagenes/twitter-rounded-gray.png' height='30px' align='center'></a>"
            +"        <a href='https://www.instagram.com/uusmb_unam/''><img src='http://www.uusmb.unam.mx/imagenes/instagram-rounded-gray.png' height='30px' align='center'></a>"
            +"        <a href='https://www.youtube.com/@unidaduniversitariadesecue2021'><img src='http://www.uusmb.unam.mx/imagenes/youtube-rounded-gray.png' height='30px' align='center'></a>"
            +"        <a href='https://www.linkedin.com/company/unidad-universitaria-de-secuenciaci%C3%B3n-masiva-y-bioinform%C3%A1tica-unam/about/'><img src='http://www.uusmb.unam.mx/imagenes/linkedin-rounded-gray.png' height='30px' align='center'></a>"
            ;
    private static final String PIEPAG=
            "    <tr><td align='center' bgcolor='#FFFFFF'> <font color='#454545' size='3'>Contáctanos +52 (777) 3291777 Ext 38151 | <strong>uusmb@ibt.unam.mx</strong></font></td> </tr>"
            +"    <tr><td align='center' bgcolor='#0b5394' ><font color='white'><strong> Asistencia técnica SISBI:</strong> <br>sisbisupport@ibt.<span></span>unam.<span></span>mx</font></td></tr>"
            +"    <tr><td align='center' bgcolor='#FFFFFF'> <font color='#454545' size='2'>Este mensaje se genera automáticamente con fines exclusivamente informativos <br> y no debe ser contestado dado que su respuesta no sera revisada</font></td> </tr>"
            +"</table>"
            ;
//------------------------------------------------------- 
    @EJB
    private UserProjectLinkFacade UserProjFac;

    @PostConstruct
    private void init() {
        properties.put("mail.smtp.host", HOST);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", 465);
        properties.put("mail.smtp.mail.sender", SENDER);
        properties.put("gmail.smtp.user", USER);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        session = Session.getInstance(properties);
    }

//Email que se manda al personal de la unidad cuando un usuario de de alta muestras**
    public void sendManagerSamEmail(Users us, Project pj, int numSam) {
        init();
        
       /* String textManagerMessage = "Estimados miembros del Laboratorio de Secuenciación Masiva.\n"
                + "\n"
                + "El usuario " + us.getUserName() + " ha dado de alta " + numSam + " muestra(s) para el proyecto \"" + pj.getProjectName() + "\" en el sistema SISBI\n\n"
                + "\n"
                + "Para mayor información consulte : http://www.uusmb.unam.mx/SISBI/ \n";
        */ 
        //style web
         String codeMessagehtml = CABECERA+
                "    <tr><td align='center' bgcolor='#FFFFFF'>"
                +"            <font color='#454545' size='3'>Estimados miembros del Laboratorio de Secuenciación Masiva:<br></font>"
                +"            <font color='#454545' size='2'>"
                +"            Usted está recibiendo este correo porque está registrado en nuestro <br>"
                +"            Sistema de Información de Secuenciación y Bioinformática SISBI.</font>"
                +"    </td>  </tr>"
                +"    <tr><td align='left' bgcolor='#FFFFFF'><font color='#454545' size='3'>"
                +"El usuario <strong>"+ us.getUserName() + "</strong> ha dado de alta <strong>"+numSam+"muestra(s) </strong><br>"                 
                +"en el proyecto <strong>" + pj.getProjectName() + "</strong><br>"                     
                +" </font></td></tr>"
                +"    <td align='center' bgcolor='#FFFFFF'><font color='#454545' size='2' >"
                + "Para mayor información consulte la plataforma SISBI<br>" 
                +REDES
                +"        </font></td>"
                +PIEPAG;
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(MANAGER1));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(MANAGER2));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(MANAGER3));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(MANAGER7));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(MANAGER8));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(MANAGER9));

            message.setSubject("Registro de muestras para el proyecto: " + pj.getProjectName());
            //message.setText(textManagerMessage);
            message.setContent(codeMessagehtml, "text/html");
            Transport t = session.getTransport("smtp");
            t.connect(USER, PASS);
            t.sendMessage(message, message.getAllRecipients());
            t.close();
        } catch (MessagingException me) {
            
            System.out.println(me);
        }
    }

//Email que se manda a los usuarios involucrados en el proyecto cuando alguien da de alta muestras
    public void sendUserSamEmail(Users us, Project pj, int numSam) {
        init();

        String textMessage = "Estimados Colaboradores en el proyecto " + pj.getProjectName() + ".\n"
                + "\n"
                + "El usuario " + us.getUserName() + " ha dado de alta " + numSam + " muestra(s) para el proyecto \"" + pj.getProjectName() + "\" en el sistema SISBI de la UUSMB\n"
                + "\n"
                + "Para mayor información consulte : http://www.uusmb.unam.mx/SISBI/ \n";
        List<UserProjectLink> uplc = UserProjFac.findAll();
        List<Users> users = new ArrayList<>();
        for (UserProjectLink userProjectLink : uplc) {
            if (userProjectLink.getProject().getIdProject().equals(pj.getIdProject())) {
                System.out.println(userProjectLink.getProject().getIdProject());
                users.add(userProjectLink.getUsers());

            }
        }
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));

            for (Users user : users) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            }
            textMessage += FOOTER + WARNING;
            message.setSubject("Registro de muestras para el proyecto: " + pj.getProjectName());
            message.setText(textMessage);
            Transport t = session.getTransport("smtp");
            t.connect(USER, PASS);
            t.sendMessage(message, message.getAllRecipients());
            t.close();
        } catch (MessagingException me) {
            System.out.println(me);
        } catch (NullPointerException exc) {
            exc.printStackTrace();

        }
    }
//Email que se manda al personal de la unidad cuando se crea una nueva cuenta por algun usuario

    public void sendManagerNewUserEmail(Users us) {
        init();
        //funciona pero texto plano
        /*String textMessage = "Estimados miembros del Laboratorio de Secuenciación Masiva.\n"
                + "\n"
                + "Se ha registrado una nueva cuenta en el sistema SISBI con los siguientes datos:\n\n"
                + "Nombre de usuario: " + us.getUserName() + "\n"
                + "Nombre completo: " + us.getFirstName() + " " + us.getPLastName() + " " + us.getMLastName() + "\n"
                + "Departamento: " + us.getDepartment() + "\n"
                + "Dependencia: " + us.getIdDependency().getDependencyName() + "\n"
                + "Correo electrónico: " + us.getEmail() + "\n"
                + "\n"
                + "Para mayor información consulte : http://www.uusmb.unam.mx/SISBI/ \n";
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(MANAGER1));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(MANAGER5));
            message.setSubject("Registro de Usuario: " + us.getUserName());
            message.setText(textMessage);
            Transport t = session.getTransport("smtp");
            t.connect(USER, PASS);
            t.sendMessage(message, message.getAllRecipients());

            t.close();
        } catch (MessagingException me) {
            System.out.println(me);
        }*/
        
        //inicio leslie correo html
        init();
        //style web
        String codeMessagehtml = CABECERA+
                "    <tr><td align='center' bgcolor='#FFFFFF'>"
                +"            <font color='#454545' size='3'>Estimados miembros del Laboratorio de Secuenciación Masiva:<br></font>"
                +"            <font color='#454545' size='2'>"
                +"            Usted está recibiendo este correo porque está registrado en nuestro <br>"
                +"            Sistema de Información de Secuenciación y Bioinformática SISBI.</font>"
                +"    </td>  <tr>"
                +"        <td align='left' bgcolor='#FFFFFF'><font color='#454545' size='3'>"
                + "Se ha registrado una nueva cuenta en el sistema SISBI con los siguientes datos:<br>"
                + "<strong>Nombre de usuario: </strong>" + us.getUserName() + "<br>"
                + "<strong>Nombre completo: </strong>" + us.getFirstName() + " " + us.getPLastName() + " " + us.getMLastName() + "<br>"
                + "<strong>Departamento: </strong>" + us.getDepartment() + "<br>"
                + "<strong>Dependencia: </strong>" + us.getIdDependency().getDependencyName() + "<br>"
                + "<strong>Correo electrónico:</strong> " + us.getEmail() 
                +" </font></td></tr>"
                +"    <td align='center' bgcolor='#FFFFFF'><font color='#454545' size='2' >"
                +REDES
                +"        </font></td>"
                +PIEPAG;
                
                
                
        try {
            MimeMessage messagehtml = new MimeMessage(session);
            messagehtml.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
            messagehtml.addRecipient(Message.RecipientType.TO, new InternetAddress(MANAGER1));
            messagehtml.addRecipient(Message.RecipientType.TO, new InternetAddress(MANAGER5));
            messagehtml.setSubject("Registro de Usuario: " + us.getUserName());
            messagehtml.setContent(codeMessagehtml, "text/html");
            Transport t = session.getTransport("smtp");
            t.connect(USER, PASS);
            t.sendMessage(messagehtml, messagehtml.getAllRecipients());

            t.close();
        } catch (MessagingException me) {
            System.out.println(me);
        }
        //fin leslie
    }

// Email que se la manda al usuario al momento de registrarse en el sistema
    public void sendNewUserEmail(Users us) {
        init();
        //text plain
        /*
        String textMessage = "Estimado Usuario\n"
                + "\n"
                + "Le damos la bienvenida al Sistema de Información de Secuenciación y Bioinformática (SISBI)\n"
                + "Su nombre de usuario es el siguiente: " + us.getUserName() + "\n"
                + "\n"
                + "Le mantendremos informado por esta vía.\n"
                + "\n"
                + "Por favor, utilize este nombre de usuario en nuestro sistema ( http://www.uusmb.unam.mx/SISBI/ ) para realizar lo siguiente:\n"
                + "-Registro de proyecto\n"
                + "-Registro de muestras\n"
                + "-Recepción de resultados\n"
                + "\n"
                + FOOTER + WARNING;
        */
        
        //style web
        String codeMessagehtml = CABECERA
                +"    <tr><td align='center' bgcolor='#FFFFFF'>"
                +"            <font color='#454545' size='3'>Estimado Usuario:<br></font>"
                +"            <font color='#454545' size='2'>"
                +"            Usted está recibiendo este correo porque se ha registrado en nuestro <br>"
                +"            Sistema de Información de Secuenciación y Bioinformática SISBI.</font>"
                +"    </td>  </tr>"
                +"    <tr><td align='center' bgcolor='#FFFFFF'><font color='#454545' size='3'>"
                +"Le damos la bienvenida al Sistema de Información de Secuenciación y Bioinformática (SISBI)<br>"
                +"<strong>Su nombre de usuario:</strong><br>" + us.getUserName()                 
                +"     </font></td></tr>"
                +"        <tr><td align='left' bgcolor='#FFFFFF'><font color='#454545' size='2'>"
                +"Le mantendremos informado por esta vía las actualizaciones de sus proyectos registrados en SISBI.<br>"
                +"Con su nueva cuenta, usted podrá realizar lo siguiente dentro de nuestro sistema:<br>"
                +"<ul><li>Registro de proyectos</li>"
                +"<li>Registro de muestras</li>"
                +"<li>Recepción de resultados</li></ul>"
                +"          </font></td></tr>"
                +"    <tr><td align='center' bgcolor='#FFFFFF'><font color='#454545' size='2' >"
                +"Atte. El equipo UUSMB<br>"            
                +REDES
                +"        </font></td></tr>"
                +PIEPAG;
        
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(us.getEmail()));
            message.setSubject("Registro de Usuario: " + us.getUserName());
            //message.setText(textMessage);
            message.setContent(codeMessagehtml, "text/html");
            Transport t = session.getTransport("smtp");
            t.connect(USER, PASS);
            t.sendMessage(message, message.getAllRecipients());
            t.close();
        } catch (MessagingException me) {
            System.out.println(me);
        }
    }

//Email que se manda al momento que un usuario da de alta otro usuario
    public void sendNewUser4ProjEmail(Users us) {
        init();

        /*String textMessage = "Estimado Usuario\n"
                + "\n"
                + "Le damos la bienvenida al Sistema de Información de Secuenciación y Bioinformática (SISBI)\n"
                + "Su nombre de usuario es el siguiente: " + us.getUserName() + "\n"
                + "\n"
                + "Su contraseña por default es el siguiente: " + us.getPassword() + "\n"
                + "\n"
                + "Le mantendremos informado por esta vía.\n"
                + "\n"
                + "Por favor, utilize este nombre de usuario y contraseña en nuestro sistema ( http://www.uusmb.unam.mx/SISBI/ ) para realizar lo siguiente:\n"
                + "-Registro de proyecto\n"
                + "-Registro de muestras\n"
                + "-Recepción de resultados\n"
                + "\n"
                + "Usted tambien puede cambiar su contraseña o datos personales en la seccion 'Mi Perfil' en su cuenta \n"
                + "\n"
                + FOOTER + WARNING;*/
         //style web
        String codeMessagehtml = CABECERA
                +"    <tr><td align='center' bgcolor='#FFFFFF'>"
                +"            <font color='#454545' size='3'>Estimado Usuario:<br></font>"
                +"            <font color='#454545' size='2'>"
                +"            Usted está recibiendo este correo porque se ha registrado en nuestro <br>"
                +"            Sistema de Información de Secuenciación y Bioinformática SISBI.</font>"
                +"    </td>  </tr>"
                +"    <tr><td align='center' bgcolor='#FFFFFF'><font color='#454545' size='3'>"
                +"Le damos la bienvenida al Sistema de Información de Secuenciación y Bioinformática (SISBI)<br>"
                +"Su nombre de usuario:<strong>" + us.getUserName()+"</strong><br>"
                +"Su contraseña por default:<strong>"+ us.getPassword()+"</strong><br>"
                +"Usted puede cambiar la contraseña o datos personales <br> en el apartado <strong>Mi Perfil</strong> dentro de SISBI"
                +"     </font></td></tr>"
                +"        <tr><td align='left' bgcolor='#FFFFFF'><font color='#454545' size='2'>"
                +"Le mantendremos informado por esta vía las actualizaciones de sus proyectos registrados en SISBI.<br>"
                +"Con su nueva cuenta, usted podrá realizar lo siguiente dentro de nuestro sistema:<br>"
                +"<ul><li>Registro de proyectos</li>"
                +"<li>Registro de muestras</li>"
                +"<li>Recepción de resultados</li></ul>"
                +"          </font></td></tr>"
                +"    <tr><td align='center' bgcolor='#FFFFFF'><font color='#454545' size='2' >"
                +"Atte. El equipo UUSMB<br>"            
                +REDES
                +"        </font></td></tr>"
                +PIEPAG;
        
        
        
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(us.getEmail()));
            message.setSubject("Registro de Usuario: " + us.getUserName());
            //message.setText(textMessage);
            message.setContent(codeMessagehtml, "text/html");
            Transport t = session.getTransport("smtp");
            t.connect(USER, PASS);
            t.sendMessage(message, message.getAllRecipients());
            t.close();
        } catch (MessagingException me) {
            System.out.println(me);
        }
    }

//Email que se le manda al usuario para reestablecer su contraseña 
    public void sendRestoreEmail(String email, String userName, String pass) {
        init();

       /* String textMessage = "Estimado Usuario\n"
                + "\n"
                + "Se ha restablecido la contraseña de su cuenta en el Sistema de Secuenciación y Bioinformatica (SISBI)\n"
                + "\n"
                + "Su nombre de usuario es el siguiente: " + userName + "\n"
                + "\n"
                + "Su nueva contraseña es: " + pass + "\n"
                + "\n"
                + "Por favor, utilize este nombre de usuario y nueva contraseña para acceder a su cuenta en nuestro sistema ( http://www.uusmb.unam.mx/SISBI/ )"
                + "\n"
                + "Usted puede cambiar esta contraseña en la seccion 'Mi prefil' en su cuenta"
                + FOOTER
                + WARNING;*/
        //style web
        String codeMessagehtml = CABECERA
                +"    <tr><td align='center' bgcolor='#FFFFFF'>"
                +"            <font color='#454545' size='3'>Estimado Usuario:<br></font>"
                +"            <font color='#454545' size='2'>"
                +"           Se ha restablecido la contraseña de su cuenta en el sistema SISBI <br></font>"            
                +"    </td>  <tr>"
                +"        <td align='left' bgcolor='#FFFFFF'><font color='#454545' size='3'>"
                +"Su nombre de usuario es: <strong>" + userName +"</strong><br>"
                +"Su nueva contraseña es: <strong>"+pass+"</strong><br>"
                +"        </font></td></tr>"
                +"        <tr><td align='center' bgcolor='#FFFFFF'><font color='#454545' size='2'>"
                +"Por favor, utilize este nombre de usuario y nueva contraseña para acceder a su cuenta en nuestro sistema SISBI. <br>"
                +"Usted puede cambiar esta contraseña en el apartado 'Mi prefil' dentro de SISBI"
                +"          </font></td></tr>"
                +"    <tr><td align='center' bgcolor='#FFFFFF'><font color='#454545' size='2' >"
                +"Atte. El equipo UUSMB<br>"            
                +REDES
                +"        </font></td></tr>"
                +PIEPAG;
        
        
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Solicitud de cambio de contraseña para el usuario " + userName);
            //message.setText(textMessage);
            message.setContent(codeMessagehtml, "text/html");
            Transport t = session.getTransport("smtp");
            t.connect(USER, PASS);
            t.sendMessage(message, message.getAllRecipients());
            t.close();
        } catch (MessagingException me) {
            System.out.println(me);
        }
    }

    //Email que se manda al usuario en cuanto da de alta un proyecto en el sistema
    public void sendProject2UserEmail(String email, String role) {
        init();
        FacesContext context = FacesContext.getCurrentInstance();
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
        /*String textMessage = "Estimado usuario:\n"
                + "Usted ha sido dado de alta en un proyecto en la Unidad Universitaria de Secuenciación Masiva y Bioinformática (UUSMB) con las siguientes especificaciones;\n\n"
                + "Nombre de proyecto: \"" + pj.getProjectName() + "\"\n"
                + "ID de proyecto: \"" + pj.getIdProject() + "\"\n\n"
                + "Su rol en el proyecto es como \"" + role + "\" por lo que apartir de esta fecha recibirá notificaciones del estatus del servicio como colaborador ademas de cotizaciones y solicitudes de pago como responsable.\n"
                + "\nPuede verificar y estar al tanto de su proyecto en su cuenta de la UUSMB http://www.uusmb.unam.mx/SISBI/ "
                + "\n" + FOOTER + WARNING;*/
        
        String codeMessagehtml = CABECERA
                +"    <tr><td align='center' bgcolor='#FFFFFF'>"
                +"            <font color='#454545' size='3'>Estimado Usuario:<br></font>"
                +"            <font color='#454545' size='2'>"
                +"Usted ha sido dado de alta en un proyecto en la Unidad Universitaria de Secuenciación Masiva y Bioinformática <br>"
                + "con las siguientes especificaciones: </font>"            
                +"    </td>  <tr>"
                +"        <td align='left' bgcolor='#FFFFFF'><font color='#454545' size='3'>"
                +"Nombre de proyecto: <strong>" +  pj.getProjectName()+"</strong><br>"
                +"ID de proyecto: <strong>"+pj.getIdProject()+"</strong><br>"
                +"Su rol en el proyecto es: <strong>"+role+"</strong>.<br>"
                +"A partir de esta fecha, usted recibirá notificaciones del estatus del servicio,cotizaciones <br>"
                +" y solicitudes de pago (Sólo en caso de que haya sido designado como responsable del pago)."
                +"        </font></td></tr>"
                +"        <tr><td align='center' bgcolor='#FFFFFF'><font color='#454545' size='2'>"
                +"Por favor, utilize su cuenta en nuestro sistema SISBI para estar al tanto de las actualizaciones de su proyecto. <br>"
                +"          </font></td></tr>"
                +"    <tr><td align='center' bgcolor='#FFFFFF'><font color='#454545' size='2' >"
                +"Atte. El equipo UUSMB<br>"            
                +REDES
                +"        </font></td></tr>"
                +PIEPAG;
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Registro de proyecto: " + pj.getProjectName());
            //message.setText(textMessage);
            message.setContent(codeMessagehtml, "text/html");
            Transport t = session.getTransport("smtp");
            t.connect(USER, PASS);
            t.sendMessage(message, message.getAllRecipients());
            t.close();
        } catch (MessagingException me) {
            System.out.println(me);
        }
    }

//Email que se manda al personal de la unidad en cuanto se de de alta un proyecto 
    public void sendNewProjectManagerEmail(String userName, String role) {
        init();
        FacesContext context = FacesContext.getCurrentInstance();
        Project pj = (Project) context.getExternalContext().getSessionMap().get("project");
        //style plain text
        /*String textMessage = "Estimados miembros del Laboratorio de Secuenciación Masiva.\n"
                + "\n"
                + "El usuario " + userName + " ha registrado un nuevo proyecto en el sistema SISBI con el rol de " + role + ", el cual cuenta con los siguientes datos:\n\n"
                + "Nombre del proyecto: " + pj.getProjectName() + "\n"
                + "ID del proyecto: " + pj.getIdProject() + "\n"
                + "Método de entrega: " + pj.getDeliveryMethod() + "\n"
                + "Descripción: " + pj.getProjectDescription() + "\n"
                + "\n"
                + "Para mayor información consulte : http://www.uusmb.unam.mx/SISBI/ \n";*/
        
        //style web 
        
        String codeMessagehtml = CABECERA
                +"    <tr><td align='center' bgcolor='#FFFFFF'>"
                +"            <font color='#454545' size='3'>Estimados miembros del Laboratorio de Secuenciación Masiva<br></font>"
                +"            <font color='#454545' size='2'>"
                +"            Usted está recibiendo este correo porque se ha registrado en nuestro <br>"
                +"            Sistema de Información de Secuenciación y Bioinformática SISBI.</font>"
                +"    </td>  <tr>"
                +"        <td align='left' bgcolor='#FFFFFF'><font color='#454545' size='3'>"
                +"El usuario " + userName+"con el rol de "+ role +"<br>"
                +"ha registrado un nuevo proyecto en el sistema SISBI con los siguientes datos: <br>"
                +"Nombre del proyecto: " + pj.getProjectName() +"<br>"
                +"ID del proyecto: " + pj.getIdProject() + "<br>"
                +"Método de entrega: " + pj.getDeliveryMethod() +"<br>"
                +"Descripción: " + pj.getProjectDescription() +"<br>"
                +"        </font></td></tr>"
                +"        <tr><td align='center' bgcolor='#FFFFFF'><font color='#454545' size='2'>"           
                +"Para mayor información sobre el proyecto consulte su cuenta SISBI"
                +"          </font></td></tr>"
                +"    <tr><td align='center' bgcolor='#FFFFFF'><font color='#454545' size='2' >"
                +"Atte. El equipo UUSMB<br>"            
                +REDES
                +"        </font></td></tr>"
                +PIEPAG;
        

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(MANAGER1));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(MANAGER5));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(MANAGER9));
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(MANAGER4));
            message.setSubject("Registro de proyecto: " + pj.getProjectName());
            //message.setText(textMessage);
            message.setContent(codeMessagehtml, "text/html");
            Transport t = session.getTransport("smtp");
            t.connect(USER, PASS);
            t.sendMessage(message, message.getAllRecipients());
            t.close();
        } catch (MessagingException me) {
            System.out.println(me);
        }
    }
//Email que se manda a los usuarios para notificar la falta de cotizacion y/o comprobante de pago
    public void sendWarningProject(List<String> emails, int opt, String projName) {

        init();
        String textMessage = "";
        System.out.println(opt);
        switch (opt) {
            case 1:
                textMessage = "Estimado usuario:\n"
                        + "Hemos recibido sus muestras pero la cotización del servicio no ha sido incorporada a su proyecto registrado en nuestro sistema.\n"
                        + "Si por alguna razón no ha recibido su cotización, le pedimos la solicite a uusmb@ibt.unam.mx.\n"
                        + "Por favor, ingrese a SISBI (http://www.uusmb.unam.mx/SISBI/) y seleccione el botón \"Opciones\" del proyecto"
                        + " correspondiente, luego elija el boton \"Detalles del proyecto\" y finalmente use la sección \"Archivos asociados al proyecto\" para "
                        + "subir su cotización y/o comprobante de pago.\n"
                        + "Le recordamos que el no tener cotización puede detener el servicio de secuenciación.\n"
                        + "Si cuenta con la cotización pero no ha realizado el pago, lo invitamos a subir su cotización y hacer el pago lo antes posible.\n"
                        + FOOTER + WARNING;
                break;
            case 2:
                textMessage = "Estimado Usuario:\n"
                        + "Hemos recibido las muestras de su proyecto \"" + projName + "\" pero la cotización del servicio aún no ha sido incorporada en nuestro sistema. "
                        + "Si por alguna razón no ha recibido su cotización, le pedimos la solicite a uusmb@ibt.unam.mx.\n"
                        + "Si ya cuenta con ella, le agradeceremos la ingrese a SISBI (http://www.uusmb.unam.mx/SISBI/) seleccionando el botón \"Opciones\" del proyecto correspondiente,"
                        + " luego elija el botón \"Detalles del proyecto\" y finalmente use la sección \"Archivos asociados al proyecto\" para subir su cotización "
                        + "y/o comprobante de pago.\n"
                        + "Si cuenta con la cotización pero no ha realizado el pago, lo invitamos a subir subir su cotización lo antes posible. En cuanto tenga un comprobante de pago u oficio "
                        + "de transferencia, será muy importante que con el mismo procedimiento lo agregue para integrarlo a su expediente."
                        + FOOTER + WARNING;
                break;
            case 3:
                textMessage = "Estimado Usuario:\n"
                        + "Se concluyó la generación de archivos fastq de secuencias asociadas a su proyecto \"" + projName + "\" y notamos que en nuestro sistema aun no "
                        + "tiene registrado el pago del servicio.\n"
                        + "Si por alguna razón no ha recibido su cotización, le pedimos la solicite a uusmb@ibt.unam.mx.\n"
                        + "Si usted cuenta con un comprobante de pago, favor de subirlo a SISBI (http://www.uusmb.unam.mx/SISBI/) seleccionando el botón"
                        + " \"Opciones\" del proyecto correspondiente, luego el botón \"Detalles del proyecto\" y finalmente use la sección \"Archivos asociados al"
                        + " proyecto\" para adjuntar éste.\n"
                        + "Si por alguna razón no ha podido realizar el pago o tiene un manejo de cuenta con un número de factura asociado, favor de ponerse en contacto con la UUSMB al correo uusmb@ibt.unam.mx\n"
                        + "De antemano agradecemos su atención y respuesta."
                        + FOOTER + WARNING;
                break;
            case 4:
                textMessage = "Estimado Usuario:\n"
                        + "Se concluyó el análisis bioinfomático solicitado en el proyecto \"" + projName + "\", pero para proceder a la entrega es necesario haber ingresado al "
                        + "sistema su comprobante de pago.\n"
                        + "Si por alguna razón no ha recibido su cotización, le pedimos la solicite a uusmb@ibt.unam.mx.\n"
                        + "Por favor, ingrese a SISBI (http://www.uusmb.unam.mx/SISBI/) y seleccione el botón \"Opciones\" del "
                        + "proyecto correspondiente, luego elija el botón \"Detalles del proyecto\" y finalmente use la sección \"Archivos asociados al proyecto\" para subir su "
                        + "cotización y/o comprobante de pago, para poder continuar con la entrega de resultados.\n"
                        + "Le recordamos que la entrega de resultados esta sujeta al pago del servicio, tal como se especifica en términos y condiciones (www.uusmb.unam.mx -> servicios -> términos y condiciones).\n"
                        + "En cuanto tengamos aviso de que ha sido ingresado su comprobante le enviaremos instrucciones para la entrega de los archivos.\n"
                        + "De antemano una disculpa por los inconvenientes que esto pueda generar.\n"
                        + FOOTER + WARNING;
                break;

        }

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
            for (String email : emails) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            }
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(MANAGER1));
            message.setSubject("Aviso sobre el proyecto: " + projName);
            message.setText(textMessage);
            Transport t = session.getTransport("smtp");
            t.connect(USER, PASS);
            t.sendMessage(message, message.getAllRecipients());
            System.out.println(textMessage);
            t.close();

        } catch (MessagingException me) {
            Logger.getLogger(EmailController.class.getName()).log(Level.SEVERE, null, me);
        }

    }
 //email enviados cuando se agrega un comentario al proyecto

	public void sendCommentsProject(List<String> email,String projId,String Comment, String usEmail){
		init();
                //texo log 
                /*System.out.println("Estimado usuario:\n"
		+ "La unidad Universitaria de secuenciacón Masiva y Bionformatica (UUSMB) le informa:\n"
		+Comment+
		"\nPara mayor información consulte en su cuenta de la UUSMB: http://www.uusmb.unam.mx/SISBI/ \n");
                */
                 // texto del correo 
		/*String textMessage="Estimado usuario:\n\n"
		+ "La unidad Universitaria de secuenciacón Masiva y Bionformatica (UUSMB) le informa:\n\n"
		+Comment+
		"\n\nPara mayor información consulte en su cuenta de la UUSMB: http://www.uusmb.unam.mx/SISBI/ \n"
		+ FOOTER + WARNING;*/
                
                String codeMessagehtml = CABECERA
                +"    <tr><td align='center' bgcolor='#FFFFFF'>"
                +"            <font color='#454545' size='3'>Estimado Usuario<br></font>"
                +"            <font color='#454545' size='2'>"
                +"            Usted está recibiendo este correo porque se ha registrado en nuestro <br>"
                +"            Sistema de Información de Secuenciación y Bioinformática SISBI.</font>"
                +"    </td>  <tr>"
                +"        <td align='left' bgcolor='#FFFFFF'><font color='#454545' size='3'>"
                +"La unidad Universitaria de secuenciacón Masiva y Bionformatica (UUSMB) le informa:<br>"               
                + Comment+"<br>"
                +"        </font></td></tr>"
                +"        <tr><td align='center' bgcolor='#FFFFFF'><font color='#454545' size='2'>"           
                +"Para mayor información sobre el proyecto consulte su cuenta SISBI"
                +"          </font></td></tr>"
                +"    <tr><td align='center' bgcolor='#FFFFFF'><font color='#454545' size='2' >"
                +"Atte. El equipo UUSMB<br>"            
                +REDES
                +"        </font></td></tr>"
                +PIEPAG;
                               
	try{
	MimeMessage message= new MimeMessage(session);
	
	message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
	for(String emails: email){
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(emails));
	}
	    message.addRecipient(Message.RecipientType.TO, new InternetAddress(MANAGER1));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(MANAGER2));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(MANAGER3));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(MANAGER7));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(MANAGER8));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(MANAGER9));
	    message.addRecipient(Message.RecipientType.TO, new InternetAddress(usEmail));
	    message.setSubject("Comentarios sobre el proyecto: " + projId);
            //message.setText(textMessage);
            message.setContent(codeMessagehtml, "text/html");
            Transport t = session.getTransport("smtp");
            t.connect(USER, PASS);
            t.sendMessage(message, message.getAllRecipients());
            //System.out.println(textMessage);
            t.close();

        } catch (MessagingException me) {
            Logger.getLogger(EmailController.class.getName()).log(Level.SEVERE, null, me);
        }
    }

//Email que se manda a los usuarios involucrados a cierto proyecto para que se enteren del estado de sus muestras 
    public void sendUpdateStatusSamplesEmail(List<Sample> samples, String estatus, String statusAnt, List<String> recipients, String projectName) {
        init();

        String sampleNames = "";
        for (Sample sample : samples) {

            sampleNames += sample.getSampleName() + "\n";

        }
       /* String textMessage = "Estimado usuario:\n"
                + "La Unidad Universitaria de Secuenciación Masiva y Bioinformática (UUSMB) le informa "
                + "que las siguientes " + samples.size() + " muestras \n"
                + "pertenecientes al proyecto \"" + projectName + "\" "
                + "han cambiado de estatus de \"" + statusAnt + "\" a \"" + estatus + "\": \n\n"
                + sampleNames;*/
        
         String codeMessagehtml = CABECERA
                +"    <tr><td align='center' bgcolor='#FFFFFF'>"
                +"            <font color='#454545' size='3'>Estimado Usuario<br></font>"
                +"            <font color='#454545' size='2'>"
                +"            Usted está recibiendo este correo porque se ha registrado en nuestro <br>"
                +"            Sistema de Información de Secuenciación y Bioinformática SISBI.</font>"
                +"    </td>  <tr>"
                +"        <td align='left' bgcolor='#FFFFFF'><font color='#454545' size='3'>"
                +"La unidad Universitaria de secuenciacón Masiva y Bionformatica le informa que<br>"               
                + "las siguientes <strong>"+samples.size()+" muestras</strong><br>"
                +"pertenecientes al proyecto <strong>"+projectName+"</strong>"
                +" han cambiado de etstaus de <strong>"+statusAnt+ " a "+estatus
                +sampleNames +"</strong><br>";
                

        switch (estatus) {
            case "Recibida":
                codeMessagehtml += "Su(s) muestra(s) ha(n) sido recibida(s).<br>"
                        + "La unidad le notificará por medio del sistema sobre la evaluación <br>de la calidad de su(s) muestra(s) al cambio de estatus siguiente."
                        + "Cualquier informacion referente a sus muestras será notificado,<br> favor de estar al tanto de su proyecto.";
                break;
            case "Secuenciada":
                codeMessagehtml += "En breve, le notificaremos la evaluación de la calidad <br>de las secuencias generadas.";
                break;
            case "Para resecuenciar":
                codeMessagehtml += "El rendimiento solicitado esta por debajo de lo prometido en términos y condiciones. <br>Si usted autorizó la resecuenciacion, su biblioteca se pondrá en fila para ser secuenciada de nuevo.<br>"
                        + "Si no solicita la resecuenciacion favor de mencionarlo en el correo<br> de la primera entrega de sus datos. ";
                break;

            case "En analisis de calidad":
                codeMessagehtml += "Su muestra no cumple con los estándares de calidad establecidos. <br> Si aun así el usuario responsable autoriza continuar con la construcción <br> y/ó secuencia la(s) biblioteca(s), la unidad se deslindara si los resultados <br> no son los esperados.";
                break;
            default:

        }

        codeMessagehtml +="<br>" 
                +"        </font></td></tr>"
                +"        <tr><td align='center' bgcolor='#FFFFFF'><font color='#454545' size='2'>"           
                +"Para mayor información sobre el proyecto consulte su cuenta SISBI"
                +"          </font></td></tr>"
                +"    <tr><td align='center' bgcolor='#FFFFFF'><font color='#454545' size='2' >"
                +"Atte. El equipo UUSMB<br>"            
                +REDES
                +"        </font></td></tr>"
                +PIEPAG;
                /*
                + "Para mayor información consulte en su cuenta de la UUSMB: http://www.uusmb.unam.mx/SISBI/ \n"
                + FOOTER + WARNING;*/
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
            for (String recipient : recipients) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            }
           // message.addRecipient(Message.RecipientType.TO, new InternetAddress("fherdsnop@gmail.com"));
            message.setSubject("Actualizacion de proyecto: " + projectName);
            //message.setText(textMessage);
            message.setContent(codeMessagehtml, "text/html");
            Transport t = session.getTransport("smtp");
            t.connect(USER, PASS);
            System.out.println(codeMessagehtml);
            t.sendMessage(message, message.getAllRecipients());
            t.close();
        } catch (MessagingException me) {
            System.out.println(me);
        }
    }
    
    
    //Leslie: manda correo cuando cambia el estatus 
    //Email que se manda a los usuarios involucrados a cierto proyecto para que se enteren del estado de sus muestras 
    public void sendUpdateStatusProjEmail(  List<String> recipients, String projectID) {
        init();

        /*
        String textMessage = "Estimado usuario:\n"
                + "La Unidad Universitaria de Secuenciación Masiva y Bioinformática (UUSMB) le informa: \n"
                + "El proyecto con identificador " + projectID + " cambio del estatus: \n" 
                + "-- En proceso -- a -- Terminado --  \n";
     
        textMessage += "\n"
                + "Con el objetivo de mejorar la calidad del servicio de la UUSMB y siendo la opinión de los usuarios lo más importante para nosotros\n" 
                + "le agradeceríamos su participación en la siguiente encuesta.\n" 
                + "https://forms.gle/KWxR2ksNBddW6pmKA\n" 
                + "Si usted no responde a la encuesta, se asume que se encuentra cien por ciento satisfecho con el servicio otorgado\n"                 
                + "\n" + "\n" 
                + "La UUSMB está comprometida con la imparcialidad y la confidencialidad de los datos que aquí se entregan.\n" 
                + "Para mayor información consulte en su cuenta de la UUSMB: http://www.uusmb.unam.mx/SISBI/ \n"
                + FOOTER + WARNING;*/
        //style web
        
        String codeMessagehtml = CABECERA
                +"    <tr><td align='center' bgcolor='#FFFFFF'>"
                +"            <font color='#454545' size='3'>Estimado Usuario:<br></font>"
                +"            <font color='#454545' size='2'>"
                +"            Usted está recibiendo este correo porque se ha registrado en nuestro <br>"
                +"            Sistema de Información de Secuenciación y Bioinformática SISBI.</font>"
                +"    </td>  <tr>"
                +"        <td align='center' bgcolor='#FFFFFF'><font color='#454545' size='3'>"
                +"La Unidad Universitaria de Secuenciación Masiva y Bioinformática le informa:<br>"
                +"El proyecto con identificador <strong>"+projectID+"</strong> <br>Cambio del estatus:<br>"
                +"<strong>-- En proceso -- a -- Terminado -- </strong><br>"                 
                +"        </font></td></tr>"
                +"        <tr><td align='center' bgcolor='#FFFFFF'><font color='#454545' size='2'>"
                +"Con el objetivo de mejorar la calidad del servicio de la UUSMB <br>"
                +"y siendo la opinión de los usuarios lo más importante para nosotros,<br>"
                + "le agradeceríamos su participación en la siguiente encuesta<br>"
                +"<a href='https://forms.gle/KWxR2ksNBddW6pmKA'>Encuesta de satisfacción</a> <br>"                
                +"La UUSMB está comprometida con la imparcialidad y la confidencialidad de los datos que aquí se entregan.<br><br>"
                +"Para mayor información sobre el proyecto consulte su cuenta SISBI"
                +"          </font></td></tr>"
                +"    <tr><td align='center' bgcolor='#FFFFFF'><font color='#454545' size='2' >"
                +"Atte. El equipo UUSMB<br>"            
                +REDES
                +"        </font></td></tr>"
                +PIEPAG;
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
            for (String recipient : recipients) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            }
            message.setSubject("Actualizacion de proyecto: " + projectID);
            //message.setText(codeMessagehtml);
            message.setContent(codeMessagehtml, "text/html");
            Transport t = session.getTransport("smtp");
            t.connect(USER, PASS);
            System.out.println(codeMessagehtml);
            t.sendMessage(message, message.getAllRecipients());
            t.close();
        } catch (MessagingException me) {
            System.out.println(me);
        }
    }
    
    
    
    
    //fin 

//Email que se manda a los usuarios involucrados a cierto proyecto para que se enteren de la calidad de sus muestras 
    public void sendUpdateQualitySamplesEmail(List<Sample> samples, String calidad, List<String> recipients, String projectName) {
        init();
        String ans = "";

        switch (calidad) {
            case "Buena/Aceptable":
                ans = " pasaron nuestros estándares de calidad y se procederá a la construcción de la(s) biblioteca(s)";

                break;
            case "Buena/Poco volumen":
                ans = "pasaron nuestros estándares de calidad pero el volumen es insuficiente, por lo que sería ampliamente recomendable enviar más material.\n";

                break;
            case "Parcialmente Degradada":
                ans = "no pasaron nuestros estándares de calidad dado que se detectó degradación parcial en el material genético. Le\n"
                        + "recomendamos ampliamente que nos haga llegar nuevo material genético. En caso de no ser posible el usuario responsable deberá autorizar continuar\n"
                        + "con la construcción y/ó secuencia la(s) biblioteca(s), por lo tanto su(s) muestra(s) tendrán una aceptación condicionada donde la unidad se deslindara si los resultados no son los esperados.\n";

                break;

            case "Degradada":
                ans = "no pasaron nuestros estándares de calidad dado que se detectó un grado importante de degradación en el material genético, lo\n"
                        + "que pone en riesgo la continuidad del servicio. Le recomendamos ampliamente que nos haga llegar nuevo material genético. En caso de no\n"
                        + "ser posible el usuario responsable deberá autorizar continuar con la construcción y/ó secuencia la(s) biblioteca(s), por lo tanto su(s)\n"
                        + "muestra(s) tendrán una aceptación \"condicionada\" donde la unidad se deslindara si los resultados no son los esperados.\n";

                break;
            case "Sin material genético":
                ans = "no pasaron nuestros estándares de calidad dado que no se detectó material genético y/ó su tubo venia vacío. Le\n"
                        + "recomendamos que nos haga llegar nuevo material genético y/ó se ponga en contacto con la unidad.\n";
                break;
            case "Buena/Baja concentración":
                ans = " pasaron nuestros estándares de calidad pero la concentración es baja. Le recomendamos que nos haga llegar\n"
                        + "nuevo material genético y/ó se ponga en contacto con la unidad.\n";
                break;
            default:

        }

        String sampleNames = "";
        for (Sample sample : samples) {

            sampleNames += sample.getSampleName() + "\n";

        }
        String textMessage = "Estimado usuario:\n"
                + "La Unidad Universitaria de Secuenciación Masiva y Bioinformática (UUSMB) le informa "
                + "que las siguientes " + samples.size() + " muestras pertenecientes al proyecto \"" + projectName + "\" "
                + ans + "\n"
                + sampleNames
                + "\n"
                + "Para mayor información consulte en su cuenta de la UUSMB: http://www.uusmb.unam.mx/SISBI/ \n"
                + FOOTER + WARNING;
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
            for (String recipient : recipients) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            }
           // message.addRecipient(Message.RecipientType.TO, new InternetAddress("fherdsnop@gmail.com"));
            message.setSubject("Actualización de proyecto: " + projectName);
            message.setText(textMessage);
            Transport t = session.getTransport("smtp");
            t.connect(USER, PASS);
            System.out.println(textMessage);
            t.sendMessage(message, message.getAllRecipients());
            t.close();
        } catch (MessagingException me) {
            System.out.println(me);
        }
    }

    /*
    Este metodo envia un correo a los miembros de la UUSMB y al usuario responsable o colaborador, para solicitar una cotizacion con la informacion del  proyecto resgitrado en el sistema
    
    @author: Fernando Betanzos
    
    @param:Project- define el nombre del proyecto, samples-es la lista de las muestras asociadas al proyecto,idProj-define el id del proyecto,Bioinformatic-es la lista de analisis bioinformaticos solicitados para dicho proyecto
    DNA-tipo de muestra DNA, RNA-tipo de muestra RNA,PlatDNA-tipo de platafomra DNA,PlatRNA-tipo de plataforma RNA,type-Tipo de usuario(UNAM o no UNAM), user-Nombre del usuario,email- correo del usuario que solicita la cotizacion
    
    @version:20/11/19
     */
    public void SendMailQuotationRequest(String Project, List<Sample> samples, String idProj, List<BioinformaticAnalysis> BioInformatic, String DNA, String RNA, String PlatDNA, String PlatRNA, String Type, String User, String email) {
        init();

        System.out.print("entra al metodo para enviar correo para solicitar una cotizacion");

        String performanceDNA = "";
        String performanceRNA = "";
        
        String performanceDNAOxford = "";
        String performanceRNAOxford = "";
        int dna = 0;
        int rna = 0;
        for (Sample sample : samples) {

            if (sample.getType().toLowerCase().equals("dna genómico") || sample.getType().toLowerCase().equals("dna genomico") || sample.getType().toLowerCase().equals("dna metagenomico") || sample.getType().toLowerCase().equals("amplicon 16s") || sample.getType().toLowerCase().equals("amplicon its")) {
                performanceDNA = sample.getExpectedPerformance();//rendimiento esperado
                if(sample.getExpectedPerformanceOxford() != null){
                    performanceDNAOxford = sample.getExpectedPerformanceOxford();
                }
                dna++;
            } else {
                performanceRNA = sample.getExpectedPerformance();//rendimiento esperado
                if(sample.getExpectedPerformanceOxford() != null){
                    performanceRNAOxford = sample.getExpectedPerformanceOxford();
                }
                rna++;
            }
        }

        String textMessage = "Estimados miembros del Laboratorio de Secuenciación Masiva.\n"
                + "\n"
                + "El Proyecto: " + Project + " requiere una cotización, cuyo costo estara en función de la siguiente información:\n\n"
                + "Id del proyecto: "+idProj+".\nUsuario:" +User+".\nTipo de usuario: "+Type+"\n"  
                + "No. muestras DNA: " + dna +". Rendimiento solicitado Illumina: "+ performanceDNA +". "+ ((!performanceDNAOxford.equals(""))? "Rendimiento solicitado Oxford: " + performanceDNAOxford : "" ) +"  .Tipo de muestra: " +DNA+". Plataforma: "+ PlatDNA +"\n"
                + "No. muestras RNA: " +rna+". Rendimiento solicitado ILlumina: "+performanceRNA+". "+ ((!performanceRNAOxford.equals(""))? "Rendimiento solicitado Oxford: " + performanceRNAOxford : "" ) +" .Tipo de muestra: "+RNA+". Plataforma: "+ PlatRNA+ "\n" +"total de muestras: "+(dna+rna)+"\n\n"
                + "y los siguientes analisis bioinformaticos:\n"+ BioInformatic +"\n\n"
                +"Estimado "+ User+ " si la información descrita en este correo es erronea háganoslo saber por este medio"+"\n\n"
                + "Para mayor información consulte : http://www.uusmb.unam.mx/SISBI/ \n";

        
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
            message.setSubject("Solicitud de cotizacion del proyecto: " + Project);
            //message.addRecipient(Message.RecipientType.TO, new InternetAddress(MANAGER3));//correo del miembro de la uusmb
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(MANAGER5));//correo del miembro de la uusmb
            //message.addRecipient(Message.RecipientType.TO, new InternetAddress(MANAGER6));//correo del miembro de la uusmb
            //message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));//correo del usuario
            message.setText(textMessage);
            Transport t = session.getTransport("smtp");
            t.connect(USER, PASS);
            t.sendMessage(message, message.getAllRecipients());
            t.close();
        } catch (MessagingException me) {
            System.out.println(me);
        }

    }

    //Añadido Team   -----------------------------------------------------   Linabat Inicio
    //metodo Para Enviar Email, cuando se firman los reportes. 
    public void sendMenssageSignReport(ArrayList emailUsers, String nameProject, String typeReport, String userSign, String userSignRole) {
        init();
        String textMessageRev = "El Proyecto " + nameProject + " con el tipo de reporte " + typeReport + ", creador por " + userSign + " esta en espera de Revisión.";
        String textMessageAut = "El Proyecto " + nameProject + " con el tipo de reporte " + typeReport + ", creador por " + userSign + " esta en espera de autorzación.";

        if (userSignRole.equals("tocreate")) {
            for (int i = 0; i < emailUsers.size(); i++) {
                try {
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailUsers.get(i).toString()));
                    message.setSubject("Revisión del Reporte " + nameProject);
                    message.setText(textMessageRev);
                    Transport t = session.getTransport("smtp");
                    t.connect((String) properties.get("gmail.smtp.user"), PASS);
                    t.sendMessage(message, message.getAllRecipients());
                    t.close();
                } catch (MessagingException me) {
                    System.out.println(me);
                }
            }
        }

        if (userSignRole.equals("torevise")) {
            for (int i = 0; i < emailUsers.size(); i++) {
                try {
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailUsers.get(i).toString()));
                    message.setSubject("Autorización del Reporte " + nameProject);
                    message.setText(textMessageAut);
                    Transport t = session.getTransport("smtp");
                    t.connect((String) properties.get("gmail.smtp.user"), PASS);
                    //t.sendMessage(message, message.getAllRecipients());
                    t.close();
                } catch (MessagingException me) {
                    System.out.println(me);
                }
            }
        }
    }

    //Metodos para enviar correos para continuar con el proceso de firma
    //metodo enviar correo a todos los roles revisar
    public void sendEmailRoleFinish(String typeRole, String idProject, String typeReport, String createUser, String reviseUser, ArrayList emails) {
        //obtener los correos de los roles revisar
        String mensaje = "";
        String asunto = "";
        if (typeRole.equals("revise")) {
            asunto = "Reporte listo para revisar";
            mensaje = "El proyecto con id: " + idProject + "con tipo de reporte: " + typeReport + " creado por: " + createUser + " esta listo para su revisión.";
            for (int i = 0; i < emails.size(); i++) {
                System.out.println("::::::::::::::::::::::::::::::::::::::::::: ");
                System.out.println("Correo Enviado a " + emails.get(i));
                System.out.println("Asunto: " + asunto);
                System.out.println("Mensaje: " + mensaje);

                try {
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(emails.get(i).toString()));
                    message.setSubject(asunto);
                    message.setText(mensaje);
                    Transport t = session.getTransport("smtp");
                    t.connect((String) properties.get("gmail.smtp.user"), PASS);
                    //t.sendMessage(message, message.getAllRecipients());
                    showMessage("Correo supuesto mandado con éxito a "+emails.get(i)+".");
                    t.close();
                } catch (MessagingException me) {
                    System.out.println(me);
                    showMessage("Error al mandar correo a : "+emails.get(i)+": "+me.toString());
                }
            }
        } else if (typeRole.equals("authorize")) {
            asunto = "Reporte listo para Autorizar";
            mensaje = "El proyecto con id: " + idProject + "con tipo de reporte: " + typeReport + ", creado por: " + createUser + " y revisado por: " + reviseUser + " está listo para su revisión por el rol de autorizar.";
            for (int i = 0; i < emails.size(); i++) {
                System.out.println("::::::::::::::::::::::::::::::::::::::::::: ");
                System.out.println("Correo Enviado a " + emails.get(i));
                System.out.println("Asunto: " + asunto);
                System.out.println("Mensaje: " + mensaje);

                try {
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(emails.get(i).toString()));
                    message.setSubject(asunto);
                    message.setText(mensaje);
                    Transport t = session.getTransport("smtp");
                    t.connect((String) properties.get("gmail.smtp.user"), PASS);
                    //t.sendMessage(message, message.getAllRecipients());
                    showMessage("Correo supuesto mandado con éxito a "+emails.get(i)+".");
                    t.close();
                } catch (MessagingException me) {
                    System.out.println(me);
                    showMessage("Error al mandar correo a : "+emails.get(i)+": "+me.toString());
                }
            }
        }

    }

    public void sendEmailRoleCorrect(String idProject, String typeReport, String emailUser) {
        init();
        String mensaje = "Estimado Tec. Bioinfo"+ "\n"
                +"Se necesita una correción para el reporte bioinformático con id del proyecto: " + idProject + "\n"
                +"Con tipo de reporte: " + typeReport;
        String asunto = "Corrección para reporte bioinformático"+idProject;

        //System.out.println("::::::::::::::::::::::::::::::::::::::::::: ");
        System.out.println("Correo Enviado a " + emailUser);
        System.out.println("Asunto: " + asunto);
        System.out.println("Mensaje: " + mensaje);
        
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailUser));
            message.setSubject(asunto);
            message.setText(mensaje);
            Transport t = session.getTransport("smtp");
            t.connect((String) properties.get("gmail.smtp.user"), PASS);
            t.sendMessage(message, message.getAllRecipients());
            showMessage("Correo mandado con éxito a "+emailUser);
            t.close();

            t.close();
        } catch (MessagingException me) {
            System.out.println(me);
            //showMessage("Error al mandar correo : "+me.toString());
        }
         

    }
    public void sendEmailErrorTraceback(String mensaje, Exception e){
        init();
        String email = "carlos.perez@ibt.unam.mx";
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        String stackTrace = writer.toString();
        String message_body = "Error en SISBI: "+mensaje+"\nStack trace:\n"+stackTrace;
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(properties.get("mail.smtp.mail.sender").toString()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Error SISBI "+mensaje);
            message.setText(message_body);
            Transport t = session.getTransport("smtp");
            t.connect((String) properties.get("gmail.smtp.user"), PASS);
            t.sendMessage(message, message.getAllRecipients());
            t.close();
            //showMessage("Correo mandado con éxito a "+email);
        } catch (MessagingException me) {
            showMessage("Error al mandar correo con el reporte de error: "+me.toString());
            System.out.println(me);
        }

    }

    //envio de correo para colaboradores y responsables
    public void sendEmailRespoablesColaboradores(String asuntoFinal, String mensajeFinal, String nameProject, ReportProject reportProject, String doc_base_name, String downloaded_base_name, ArrayList emails) {
        String asunto;
        String mensaje;
        String typeReport=reportProject.getName();
        init();

        if (mensajeFinal.equals("")) {
            asunto = "Su reporte del proyecto " + nameProject + " esta en fase "+reportProject.getStatus();
            mensaje = "Su reporte con nombre " + nameProject + " con tipo de reporte: " + typeReport + ", esta disponible desde la plataforma de SISBI, en fase "+reportProject.getStatus();
        } else {
            asunto = asuntoFinal;
            mensaje = mensajeFinal;
        }

        for (int i = 0; i < emails.size(); i++) {
            System.out.println("::::::::::::::::::::::::::::::::::::::::::: ");
            System.out.println("Correo Enviado a " + emails.get(i));
            System.out.println("Asunto: " + asunto);
            System.out.println("Mensaje: " + mensaje);

            try {
                MimeMessage message = new MimeMessage(session);
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                MimeMultipart multipart = new MimeMultipart();
                FileDataSource source = new FileDataSource(PathFiles.DirectoryreportDocuments+doc_base_name);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(doc_base_name);
                multipart.addBodyPart(messageBodyPart);
                message.setFrom(new InternetAddress(properties.get("mail.smtp.mail.sender").toString()));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(emails.get(i).toString()));
                message.setSubject(asunto);
                message.setText(mensaje);
                message.setContent(multipart);
                Transport t = session.getTransport("smtp");
                t.connect((String) properties.get("gmail.smtp.user"), PASS);
                t.sendMessage(message, message.getAllRecipients());
                t.close();
                showMessage("Correo mandado con éxito a "+emails.get(i));
            } catch (MessagingException me) {
                showMessage("Error al mandar correo a "+emails.get(i)+": "+me.toString());
                System.out.println(me);
            }
        }
    }


    //correo prueba Linabat
    public void enviarCorreoPrueba() {
        init();

        String textManagerMessage = "Correo de prueba: equipo Linabat.";
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("linabatoficial@gmail.com"));
            message.setSubject("Registro de muestras para el proyecto: ");
            message.setText(textManagerMessage);
            Transport t = session.getTransport("smtp");
            t.connect((String) properties.get("mail.smtp.user"), PASS);
            // t.sendMessage(message, message.getAllRecipients());
            t.close();
            showMessage("Correo mandado con éxito");
        } catch (MessagingException me) {
            showMessage("Error al mandar correo: "+me.toString());
            System.out.println(me);
        }
    }
//Añadido Team   -----------------------------------------------------   Linabat Final
    
    //Métodos generales
    public void showMessage(String mensaje) {
        FacesMessage message = new FacesMessage(mensaje);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void showError(StackTraceElement[] mensajes) {
        for (StackTraceElement mensaje : mensajes) {
            System.out.print(mensaje.toString());           
        }
    }


}
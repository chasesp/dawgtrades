package edu.uga.dawgtrades.boundary;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.uga.dawgtrades.authentication.SessionManager;
import edu.uga.dawgtrades.model.DTException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Logout 
    extends HttpServlet
{
    private static final long serialVersionUID = 1L;
    
    static  String  templateDir = "WEB-INF/templates";
    static  String  resultTemplateName = "Goodbye.ftl";

    private Configuration  cfg; 

    public void init() 
    {
        // Prepare the FreeMarker configuration;
        // - Load templates from the WEB-INF/templates directory of the Web app.
        //
        cfg = new Configuration();
        cfg.setServletContextForTemplateLoading(
                getServletContext(), 
                "WEB-INF/templates"
                );
    }

    public void doGet( HttpServletRequest req, HttpServletResponse res )
            throws ServletException, IOException
    {
        Template       resultTemplate = null;
        HttpSession    httpSession = null;
        BufferedWriter toClient = null;
        String         ssid = null;

        // Load templates from the WEB-INF/templates directory of the Web app.
        //
        try {
            resultTemplate = cfg.getTemplate( resultTemplateName );
        } 
        catch (IOException e) {
            throw new ServletException( "Logout.doGet: Can't load template in: " + templateDir + ": " + e.toString());
        }

        httpSession = req.getSession( false );
        if( httpSession != null ) {
            ssid = (String) httpSession.getAttribute( "ssid" );
            if( ssid != null ) {
                System.out.println( "Already have ssid: " + ssid );
                try {
                    SessionManager.logout( ssid );
                    httpSession.removeAttribute("ssid");
                    httpSession.invalidate();
                    System.out.println( "Invalidated http session" );
                }
                catch( DTException e ) {
                    e.printStackTrace();
                }
            }
            else
                System.out.println( "ssid is null" );
        }
        else
            System.out.println( "No http session" );
    
        // Prepare the HTTP response:
        // - Use the charset of template for the output
        // - Use text/html MIME-type
        //
        toClient = new BufferedWriter( new OutputStreamWriter( res.getOutputStream(), resultTemplate.getEncoding() ) );

        res.setContentType("text/html; charset=" + resultTemplate.getEncoding());

        // Setup the data-model
        //
        Map<String, String> root = new HashMap<String, String>();

        // Merge the data-model and the template
        //
        try {
            resultTemplate.process( root, toClient );
            toClient.flush();
        } 
        catch (TemplateException e) {
            throw new ServletException( "Error while processing FreeMarker template", e);
        }

        toClient.close();
    }

}

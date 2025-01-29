/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xqj;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;

/**
 *
 * @author Eduardo
 */
public class XQJ_Update {
    
    private static String nomClaseDS = "net.xqj.exist.ExistXQDataSource";
    private static XQConnection obtenConexion() throws ClassNotFoundException, InstantiationException, IllegalAccessException, XQException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        XQDataSource xqs = (XQDataSource) Class.forName(nomClaseDS)
                .getDeclaredConstructor()
                .newInstance();
        xqs.setProperty("serverName", "localhost");
        xqs.setProperty("port", "8080");
        xqs.setProperty("user", "admin");
        xqs.setProperty("password", "");
        return xqs.getConnection();
    }

    private static void muestraErrorXQuery(XQException e) {
        System.err.println("XQuery ERROR mensaje: " + e.getMessage());
        System.err.println("XQuery ERROR causa: " + e.getCause());
        System.err.println("XQuery ERROR código: " + e.getVendorCode());
    }
    
    public static void main(String[] args) {
        
        XQConnection c = null;
        try{
            c = obtenConexion();
            //INSERTAR ANTES DE SAMPER UN NUEVO CLIENTE
            String cad = """
                         update insert <cliente DNI= '09871111K'>
                         <apellidos>MINGUEZA</apellidos>
                         <cp>43001</cp>
                         </cliente>
                         preceding doc ('/db/pruebas/Clientes.xml')//clientes/cliente/apellidos[text()='SAMPER']/..
                         """;
            //CAMBIAR EL CP DE SAMPER
            String cad1 = """
                          update value doc('/db/pruebas/Clientes.xml')/clientes/cliente[apellidos='SAMPER']/CP
                          with '50001'
                          """;
            
            
            //CAMBIAR APELLIDO DE SAMPER
            String cad2 = """
                          update value doc('/db/pruebas/Clientes.xml')/clientes/cliente[apellidos='SAMPER']/apellidos
                          with 'SAMPERE'                            
                          """;
            XQExpression xqe = c.createExpression();
            xqe.executeCommand(cad2);
            
        }catch(XQException e){
            muestraErrorXQuery(e);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if (c != null) {
                try {
                    c.close();
                } catch (XQException ex) {
                    Logger.getLogger(XQJ_Update.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    }
    
}

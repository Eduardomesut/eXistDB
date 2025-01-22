/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package xqj;


import java.lang.reflect.InvocationTargetException;
import javax.xml.xquery.*;


/**
 *
 * @author tarde
 */
public class XQJ_Consulta {
    private static String nomClaseDS = "net.xqj.exist.ExistXQDataSource";
    private static XQConnection obtenConexion() throws ClassNotFoundException, InstantiationException, IllegalAccessException, XQException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException{
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
        try {
            c = obtenConexion();
            // Corrección de la consulta XQuery
            String cad = "for $n in doc('/db/pruebas/Clientes.xml')//cliente return $n/apellidos";
            String cad2 = "for $n in doc('/db/pruebas/Clientes.xml')/clientes/cliente "
                       + "where substring($n/CP, 1, 2) = '29' "
                       + "return concat($n/apellidos, '-', string($n/@DNI))";
            XQExpression xqe = c.createExpression();
            XQResultSequence xqrs = xqe.executeQuery(cad);

            int i = 1;
            while (xqrs.next()) {
              
                System.out.println("Resultado " + (i++) + ":");
                
                XQItemAccessor item = xqrs.getItem();
                
                String resultado = item.getItemAsString(null); 
                System.out.println(resultado);

            }
        } catch (XQException e) {
            muestraErrorXQuery(e);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (c != null) {
                    c.close();
                }
            } catch (XQException xe) {
                xe.printStackTrace();
            }
        }
    }
    
    
}

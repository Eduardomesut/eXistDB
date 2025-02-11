/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.lang.reflect.InvocationTargetException;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQItemAccessor;
import javax.xml.xquery.XQResultSequence;

/**
 *
 * @author Eduardo
 */
public class ExistDBConsultas {

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
        try {
            c = obtenConexion();
            System.out.println("Conexión establecida correctamente.");
            //listado con el nombre del género y la cantidad de libros en cada categoría.
            String cad = """
                         for $genero in distinct-values (doc('/db/pruebaexamen/biblioteca/novelas/Catalogo.xml')//catalogo/libro/genero)
                         let $numero := count(for $repe in doc('/db/pruebaexamen/biblioteca/novelas/Catalogo.xml')//catalogo/libro/genero
                         where $repe = $genero return $repe)
                         return concat('Del género de ', $genero, ' hay ', $numero, ' libros')
                         """;
                       
            //Obtener los libros que tienen más de un género
            String cad1 = """
                          for $libro in doc('/db/pruebaexamen/biblioteca/novelas/Catalogo.xml')//catalogo/libro
                          where contains($libro/genero, ',')
                          return $libro
                          """;
            //libros agrupados por editorial
            String cad2 = """
                          for $genero in distinct-values(doc('/db/pruebaexamen/biblioteca/novelas/Catalogo.xml')//catalogo/libro/genero)
                          return(for $libro in doc('/db/pruebaexamen/biblioteca/novelas/Catalogo.xml')//catalogo/libro
                          where $libro/genero = $genero return $libro)
                          """;
            //Actualizar todos los libros publicados antes de 1900 y agregar un atributo clasico="true"
            String cad3 = """
                          
                          """;
            
            //Insertar un nuevo libro solo si no existe uno con el mismo título
            String cad4 = """
                          
                          """;
            //Eliminar todos los libros con género "Autoayuda" y devolver la lista de eliminados
            String cad5 = """
                          
                          """;

            //Contar cuántos libros pertenecen al género "Distopía":
            String cad6 = """
                          
                          """;
            //Obtener todos los libros publicados después del año 2000:
            String cad7 = """
                          
                          """;
            
            //Listar todos los libros de un autor específico (por ejemplo, Gabriel García Márquez):
            String cad8 = """
                         
                          """;

            
            XQExpression xqe = c.createExpression();
            
            //Para modificaciones
            //xqe.executeCommand(cad6);
            //Para consultas
            //xqe.executeQuery(cad);
            
            
            XQResultSequence xqrs = xqe.executeQuery(cad);
            resultado(xqrs);
//            xqrs = xqe.executeQuery(cad2);
//            resultado(xqrs);
//            xqrs = xqe.executeQuery(cad3);
//            resultado(xqrs);
//            xqe.executeCommand(cad4);
//            xqrs = xqe.executeQuery(cad5);
//            resultado(xqrs);
//            xqe.executeCommand(cad6);
//            xqrs = xqe.executeQuery(cad7);
//            resultado(xqrs);
//            xqrs = xqe.executeQuery(cad8);
//            resultado(xqrs);
            
            
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

    private static void resultado(XQResultSequence xqrs) throws XQException{
        int i = 1;
            System.out.println(xqrs.toString());
            while (xqrs.next()) {

                System.out.println("Resultado " + (i++) + ":");
                // Aquí utilizamos getItem() para obtener un XQItemAccessor
                XQItemAccessor item = xqrs.getItem();
                // Luego obtenemos el resultado como una cadena
                String resultado = item.getItemAsString(null); // No es necesario pasar un Properties
                System.out.println(resultado);

            }
        
    }
}


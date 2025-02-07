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
 * @author tarde
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
            //Consultar si está en la biblioteca la obra "La metamorfosis", sacar resultado por pantalla
            String cad = """
                         for $titulo in doc ('/db/instituto/biblioteca/novelas/Catalogo.xml')//catalogo/libro
                         where $titulo/titulo = 'La metamorfosis'
                         return $titulo
                         """;
            
            
            //Listado de obras de Gabriel García Márquez (puede que haya repeticiones), sacar resultado por pantalla
            String cad1 = """
                          for $titulo in doc ('/db/instituto/biblioteca/novelas/Catalogo.xml')//catalogo/libro
                          where $titulo/autor = 'Gabriel García Márquez' return $titulo
                          """;
            //Listado de obras cuyo género sea "Ficción", sacar resultado por pantalla
            String cad2 = """
                          for $obra in doc ('/db/instituto/biblioteca/novelas/Catalogo.xml')//catalogo/libro
                          where $obra/genero = 'Ficción' return $obra
                          """;
            //Listado de obras de la editorial "Alfaguara", sacar resultado por pantalla
            String cad3 = """
                          for $obra in doc ('/db/instituto/biblioteca/novelas/Catalogo.xml')//catalogo/libro
                                                    where $obra/editorial = 'Alfaguara' return $obra
                          """;
            
            //Cambiar la editorial de la obra "La biblioteca de los muertos" de Ediciones B a Alfaguara, sacar resultado por pantalla,
            String cad4 = """
                          update value doc('/db/instituto/biblioteca/novelas/Catalogo.xml')//catalogo/libro[titulo='La biblioteca de los muertos']/editorial
                                                    with 'Alfaguara'
                          """;
            String cad5 = """
                          for $libro in doc ('/db/instituto/biblioteca/novelas/Catalogo.xml')//catalogo/libro
                          where $libro/titulo = 'La biblioteca de los muertos' return $libro
                          """;

            //Cambiar el género de la obra "Cumbres borrascosas" de Novela gótica a Ficción, sacar resultado por pantalla.
            String cad6 = """
                          update value doc('/db/instituto/biblioteca/novelas/Catalogo.xml')//catalogo/libro[titulo='Cumbres borrascosas']/genero
                          with 'Ficción'
                          """;
            String cad7 = """
                          for $libro in doc ('/db/instituto/biblioteca/novelas/Catalogo.xml')//catalogo/libro
                          where $libro/titulo = 'Cumbres borrascosas' return $libro
                          """;
            
            //Mediante la API XQj, detectar si hay alguna novela repetida e identificarlas, sacar resultado por pantalla. 
            String cad8 = """
                          for $titulo in distinct-values(doc("/db/instituto/biblioteca/novelas/Catalogo.xml")//catalogo/libro/titulo)
                          
                          let $contador := count(
                              for $repe in doc("/db/instituto/biblioteca/novelas/Catalogo.xml")//catalogo/libro/titulo
                              where $repe = $titulo
                              return $repe)
                          where $contador > 1 return concat('El libro "', $titulo, '" está repetido ', $contador, ' veces')
                          """;

            
            XQExpression xqe = c.createExpression();
            
            //Para modificaciones
            //xqe.executeCommand(cad6);
            //Para consultas
            //xqe.executeQuery(cad);
            
            
            XQResultSequence xqrs = xqe.executeQuery(cad);
            resultado(xqrs);
            xqrs = xqe.executeQuery(cad2);
            resultado(xqrs);
            xqrs = xqe.executeQuery(cad3);
            resultado(xqrs);
            xqe.executeCommand(cad4);
            xqrs = xqe.executeQuery(cad5);
            resultado(xqrs);
            xqe.executeCommand(cad6);
            xqrs = xqe.executeQuery(cad7);
            resultado(xqrs);
            xqrs = xqe.executeQuery(cad8);
            resultado(xqrs);
            
            
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

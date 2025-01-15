/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.existdbinsert;


import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

/**
 *
 * @author tarde
 */
public class ExistDBInsert {
    
    private static final String URI = "xmldb:exist://localhost:8080/exist/xmlrpc";
    private static final String USER = "admin"; 
    private static final String PASSWORD = ""; 

    public static void main(String[] args) {
        Collection col = null;

        try {
            
            Class<?> cl = Class.forName("org.exist.xmldb.DatabaseImpl");
            Database database = (Database) cl.getDeclaredConstructor().newInstance();
            database.setProperty("create-database", "true");
            DatabaseManager.registerDatabase(database);

            
            col = DatabaseManager.getCollection(URI + "/db/pruebas", USER, PASSWORD);
            if (col == null) {
                System.out.println("No se pudo conectar a la colección: /db/pruebas");
                return;
            }

            XMLResource recurso;

            
            recurso = (XMLResource) col.createResource("cars.xml", XMLResource.RESOURCE_TYPE);
            recurso.setContent(
                "<Cars>\n" +
                "  <Brand name =\"Toyota\">\n" +
                "    <Model>Corolla</Model>\n" +
                "    <Model>Camry</Model>\n" +
                "    <Model>RAV4</Model>\n" +
                "  </Brand>\n" +
                "  <Brand name =\"Ford\">\n" +
                "    <Model>Focus</Model>\n" +
                "    <Model>Mustang</Model>\n" +
                "    <Model>Explorer</Model>\n" +
                "  </Brand>\n" +
                "  <Brand name =\"BMW\">\n" +
                "    <Model>320i</Model>\n" +
                "    <Model>X5</Model>\n" +
                "    <Model>M3</Model>\n" +
                "  </Brand>\n" +              
                "</Cars>"
            );
            col.storeResource(recurso);
            System.out.println("Archivo insertado correctamente!!");


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (col != null) {
                    col.close();
                }
            } catch (XMLDBException xe) {
                xe.printStackTrace();
            }
        }
    }
}

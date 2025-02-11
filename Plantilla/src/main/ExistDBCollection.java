/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.io.File;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

/**
 *
 * @author Eduardo
 */
public class ExistDBCollection {
    
    private static final String URI = "xmldb:exist://localhost:8080/exist/xmlrpc";
    private static final String USER = "admin";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        try {
            Collection col = null;

            Class<?> cl = Class.forName("org.exist.xmldb.DatabaseImpl");
            Database database = (Database) cl.getDeclaredConstructor().newInstance();
            database.setProperty("create-database", "true");
            DatabaseManager.registerDatabase(database);

            String rootCollectionPath = "/db";
            Collection rootCollection = DatabaseManager.getCollection(URI + rootCollectionPath, USER, PASSWORD);

            if (rootCollection == null) {
                System.out.println("No se pudo conectar a la colección raíz: " + rootCollectionPath);
                return;
            }

            // Obtener el servicio de gestión de colecciones
            CollectionManagementService mgtService = (CollectionManagementService) rootCollection.getService("CollectionManagementService", "1.0");

            // Crear colecciones en el nivel más alto
            String[] collectionsToCreate = {"/pruebaexamen/biblioteca/novelas"};
            for (String collectionName : collectionsToCreate) {
                System.out.println("Creando colección: " + collectionName);
                mgtService.createCollection(collectionName);
            }

            // Confirmar creación de colecciones
            System.out.println("\nColecciones creadas:");
            for (String collectionName : rootCollection.listChildCollections()) {
                System.out.println(" - " + collectionName);
            }

            //Insertar XML en la carpeta 
            col = DatabaseManager.getCollection(URI + rootCollectionPath + "/pruebaexamen/biblioteca/novelas", USER, PASSWORD);
            if (col == null) {
                System.out.println("No se pudo conectar a la colección: /db/pruebas");
                return;
            }
            File archivoxml = new File("Catalogo.xml");
            if (!archivoxml.exists()) {
                System.out.println("El archivo XML no existe: " + archivoxml.getAbsolutePath());
                return;
            }

            // Crear un recurso XML en la colección
            XMLResource document = (XMLResource) col.createResource(archivoxml.getName(), "XMLResource");
            document.setContent(archivoxml);
            col.storeResource(document);

            // Cerrar conexión
            rootCollection.close();

        } catch (ClassNotFoundException e) {
            System.err.println("Error al cargar el driver: " + e.getMessage());
        } catch (XMLDBException e) {
            System.err.println("Error al interactuar con la base de datos: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

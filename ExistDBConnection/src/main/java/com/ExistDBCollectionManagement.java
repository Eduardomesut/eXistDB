/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com;


import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;

public class ExistDBCollectionManagement {
    private static final String URI = "xmldb:exist://localhost:8080/exist/xmlrpc";
    private static final String USER = "admin"; // Cambia por tu usuario
    private static final String PASSWORD = ""; // Cambia por tu contraseña

    public static void main(String[] args) {
        try {
            // Inicializar el driver de eXist-db
            Class<?> cl = Class.forName("org.exist.xmldb.DatabaseImpl");
            Database database = (Database) cl.getDeclaredConstructor().newInstance();
            database.setProperty("create-database", "true");
            DatabaseManager.registerDatabase(database);

            // Conectar a la colección raíz
            String rootCollectionPath = "/db";
            Collection rootCollection = DatabaseManager.getCollection(URI + rootCollectionPath, USER, PASSWORD);

            if (rootCollection == null) {
                System.out.println("No se pudo conectar a la colección raíz: " + rootCollectionPath);
                return;
            }

            // Obtener el servicio de gestión de colecciones
            CollectionManagementService mgtService = (CollectionManagementService)
                    rootCollection.getService("CollectionManagementService", "1.0");

            // Crear colecciones en el nivel más alto
            String[] collectionsToCreate = {"prueba1", "pruebas"};
            for (String collectionName : collectionsToCreate) {
                System.out.println("Creando colección: " + collectionName);
                mgtService.createCollection(collectionName);
            }

            // Confirmar creación de colecciones
            System.out.println("\nColecciones creadas:");
            for (String collectionName : rootCollection.listChildCollections()) {
                System.out.println(" - " + collectionName);
            }

            // Eliminar colecciones
                String collectionNameremove = "prueba1" ;
                System.out.println("Eliminando colección: " + collectionNameremove);
                mgtService.removeCollection(collectionNameremove);
            

            // Confirmar eliminación de colecciones
            System.out.println("\nColecciones después de la eliminación:");
            for (String collectionName : rootCollection.listChildCollections()) {
                System.out.println(" - " + collectionName);
            }

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

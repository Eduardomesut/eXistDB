/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package xmldb;

/**
 *
 * @author Usuario
 */

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;

public class ExistDBConnection {
    private static final String URI = "xmldb:exist://localhost:8080/exist/xmlrpc";
    private static final String USER = "admin"; // Cambiar si usas otro usuario
    private static final String PASSWORD = ""; // Cambiar por la contraseña del usuario

    public static void main(String[] args) {
        try {
            // Inicializar el driver de eXist-db
            Class<?> cl = Class.forName("org.exist.xmldb.DatabaseImpl");
            Database database = (Database) cl.getDeclaredConstructor().newInstance();
            database.setProperty("create-database", "true");
            DatabaseManager.registerDatabase(database);

           
            // Conectar a la colección raíz o una específica
            String collectionPath = "/db/apps/doc"; // Cambia por tu colección
            Collection collection = DatabaseManager.getCollection(URI + collectionPath, USER, PASSWORD);

            if (collection == null) {
                System.out.println("No se pudo encontrar la colección: " + collectionPath);
                return;
            }

            // Listar subcolecciones
            System.out.println("Subcolecciones:");
            for (String childCollection : collection.listChildCollections()) {
                System.out.println(" - " + childCollection);
            }

            // Listar recursos (documentos) en la colección
            System.out.println("\nDocumentos en la colección:");
            for (String resourceName : collection.listResources()) {
                Resource resource = collection.getResource(resourceName);
                System.out.println(" - " + resource.getId());
            }

            // Cerrar la conexión
            collection.close();
            
        } catch (ClassNotFoundException e) {
            System.err.println("Error al cargar el driver: " + e.getMessage());
        } catch (XMLDBException e) {
            System.err.println("Error al interactuar con la base de datos: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

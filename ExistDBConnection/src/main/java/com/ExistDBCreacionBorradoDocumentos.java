/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com;

/**
 *
 * @author Usuario
 */
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.base.Database;
import org.xmldb.api.modules.XMLResource;
import org.xml.sax.InputSource;
import org.xml.sax.ContentHandler;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.XMLOutputFactory;
import java.io.StringReader;
import java.io.StringWriter;
public class ExistDBCreacionBorradoDocumentos {
    

    private static final String URI = "xmldb:exist://localhost:8080/exist/xmlrpc";
    private static final String USER = "admin"; // Cambia por tu usuario
    private static final String PASSWORD = ""; // Cambia por tu contraseña

    public static void main(String[] args) {
        Collection col = null;

        try {
            // Inicializar el driver de eXist-db
            Class<?> cl = Class.forName("org.exist.xmldb.DatabaseImpl");
            Database database = (Database) cl.getDeclaredConstructor().newInstance();
            database.setProperty("create-database", "true");
            DatabaseManager.registerDatabase(database);

            // Conectar a la colección objetivo
            col = DatabaseManager.getCollection(URI + "/db/pruebas", USER, PASSWORD);
            if (col == null) {
                System.out.println("No se pudo conectar a la colección: /db/pruebas");
                return;
            }

            XMLResource recurso;

            // Crear documento a partir de String
            recurso = (XMLResource) col.createResource("Clientes.xml", XMLResource.RESOURCE_TYPE);
            recurso.setContent(
                "<clientes>\n" +
                "  <cliente DNI=\"78910234X\">\n" +
                "    <apellidos>NADALES</apellidos>\n" +
                "    <CP>44006</CP>\n" +
                "  </cliente>\n" +
                "  <cliente DNI=\"89012345Y\">\n" +
                "    <apellidos>ROJAS</apellidos>\n" +
                "    <CP>29703</CP>\n" +
                "  </cliente>\n" +
                "  <cliente DNI=\"66666666M\">\n" +
                "    <apellidos>SAMPER</apellidos>\n" +
                "    <CP>29730</CP>\n" +
                "  </cliente>\n" +
                "</clientes>"
            );
            col.storeResource(recurso);

            recurso = (XMLResource) col.createResource("Empresa.xml", XMLResource.RESOURCE_TYPE);
            recurso.setContent(
                "<empresa CIF=\"A34246801\">\n" +
                "  MegaExport\n" +
                "  <sedes>\n" +
                "    <sede>LEON</sede>\n" +
                "    <sede>CACERES</sede>\n" +
                "  </sedes>\n" +
                "</empresa>"
            );
            col.storeResource(recurso);

            // Crear documento a partir de objeto SAX
            StringWriter out = new StringWriter();
            XMLOutputFactory xof = XMLOutputFactory.newInstance();
            XMLStreamWriter xsw = xof.createXMLStreamWriter(out);

            xsw.writeStartDocument();
            xsw.writeStartElement("empresa");
            xsw.writeAttribute("CIF", "A34246801");
            xsw.writeCharacters("MegaExport");
            xsw.writeStartElement("sedes");
            xsw.writeStartElement("sede");
            xsw.writeCharacters("LEON");
            xsw.writeEndElement();
            xsw.writeStartElement("sede");
            xsw.writeCharacters("CACERES");
            xsw.writeEndElement();
            xsw.writeEndElement();
            xsw.writeEndElement();
            xsw.writeEndDocument();

            xsw.flush();
            xsw.close();

            recurso = (XMLResource) col.createResource("DatosEmpresa.xml", XMLResource.RESOURCE_TYPE);
            ContentHandler ch = recurso.setContentAsSAX();
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(ch);
            reader.parse(new InputSource(new StringReader(out.toString())));
            col.storeResource(recurso);
            
            // Borrar documento
            col.removeResource(col.getResource("Empresa.xml"));

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

package ejercicio;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Main2 {
    public static void main(String[] args) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        {
            try {
                builder = factory.newDocumentBuilder();
                DOMImplementation implementation = builder.getDOMImplementation();
                Document document = implementation.createDocument(null, "empleados", null);
                RandomAccessFile raf = new RandomAccessFile("EMPLEADOS.DAT", "rw");
                while (raf.getFilePointer() < raf.length()){
                    Element empleadoElement = document.createElement("empleado");
                    int codigo = raf.readInt();
                    Element codigoElement = document.createElement("codigo");
                    Text textCodigo = document.createTextNode(codigo+"");
                    codigoElement.appendChild(textCodigo);
                    empleadoElement.appendChild(codigoElement);

                    String nombre = "";
                    for (int i = 0; i < 20; i++) {
                        nombre += raf.readChar();
                    }
                    Element nombreElement = document.createElement("nombre");
                    Text textNombre = document.createTextNode(nombre.trim());
                    nombreElement.appendChild(textNombre);
                    empleadoElement.appendChild(nombreElement);

                    String direccion = "";
                    for (int i = 0; i < 20; i++) {
                        direccion += raf.readChar();
                    }
                    Element direccionElement = document.createElement("direccion");
                    Text textDireccion = document.createTextNode(direccion.trim());
                    direccionElement.appendChild(textDireccion);
                    empleadoElement.appendChild(direccionElement);

                    float salario = raf.readFloat();
                    Element salarioElement = document.createElement("salario");
                    Text textoSalario = document.createTextNode(salario+"");
                    salarioElement.appendChild(textoSalario);
                    empleadoElement.appendChild(salarioElement);

                    float comision = raf.readFloat();
                    Element comisionElement = document.createElement("comision");
                    Text textoComision = document.createTextNode(comision+"");
                    comisionElement.appendChild(textoComision);
                    empleadoElement.appendChild(comisionElement);


                    document.getDocumentElement().appendChild(empleadoElement);
                }

                Source source = new DOMSource(document);
                Result result = new StreamResult(new File("empleados.xml"));
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.transform(source,result);
            } catch (ParserConfigurationException e) {
                throw new RuntimeException(e);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (TransformerConfigurationException e) {
                throw new RuntimeException(e);
            } catch (TransformerException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

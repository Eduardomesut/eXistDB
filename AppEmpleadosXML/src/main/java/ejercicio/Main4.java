package ejercicio;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

//Hacer a mano un xml de cocineros desde java y guardarlo como.xml
public class Main4 {
    public static void main(String[] args) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation implementation = builder.getDOMImplementation();
            Document document = implementation.createDocument(null, "cocineros", null);
            for (int i = 0; i < 10; i++) {

                Element cocineroElement = document.createElement("cocinero");
                int codigo = i;
                Element codigoElement = document.createElement("codigo");
                Text textCodigo = document.createTextNode(codigo+"");
                codigoElement.appendChild(textCodigo);
                cocineroElement.appendChild(codigoElement);

                String nombre = "Juan" +i;
                Element nombreElement = document.createElement("nombre");
                Text textNombre = document.createTextNode(nombre);
                nombreElement.appendChild(textNombre);
                cocineroElement.appendChild(nombreElement);

                //Falta mas información



                document.getDocumentElement().appendChild(cocineroElement);

            }
            Source source = new DOMSource(document);
            Result result = new StreamResult(new File("cocineros.xml"));
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(source, result);


        }catch (Exception e){
            e.printStackTrace();
        }


    }

}

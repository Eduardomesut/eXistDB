package ejercicio;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

//Pasar de xml a lista de empleados y enseñarlo por pantalla
public class Main3 {
    public static void main(String[] args) {
        ArrayList<Empleado>empleados = new ArrayList<>();
        try {
            File f = new File("empleados.xml");
            if (!f.isFile()){
                System.out.println("No existe el archivo");
                return;
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(f);
            document.getDocumentElement()
                    .normalize();

            NodeList nodeList = document.getElementsByTagName("empleado");
            System.out.println(nodeList.getLength());

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;

                    int codigo = Integer.parseInt(element.getElementsByTagName("codigo").item(0).getTextContent());
                    String nombre = element.getElementsByTagName("nombre").item(0).getTextContent();
                    String direccion = element.getElementsByTagName("direccion").item(0).getTextContent();
                    float salario = Float.parseFloat(element.getElementsByTagName("salario").item(0).getTextContent());
                    float comision = Float.parseFloat(element.getElementsByTagName("comision").item(0).getTextContent());

                    Empleado insert = new Empleado(codigo, nombre, direccion, salario, comision);
                    empleados.add(insert);
                }
            }

            for (Empleado empl:empleados) {
                System.out.println(empl);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

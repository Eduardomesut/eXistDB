/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xqj;

/**
 *
 * @author Usuario
 */
import java.lang.reflect.InvocationTargetException;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQResultSequence;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.xquery.XQItemAccessor;

public class XQJ_Consulta {

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
            // Corrección de la consulta XQuery
            String cad = "for $dept in distinct-values(doc('/db/pruebas/company.xml')/company/employees/employee/department)\n"
                    + "let $average := avg(doc('/db/pruebas/company.xml')/company/employees/employee[department = $dept]/salary)\n"
                    + "return <department name=\"{$dept}\">\n"
                    + "    <average-salary>{ $average }</average-salary>\n"
                    + "</department>";
            String cad2 = "for $dept in distinct-values(doc('/db/pruebas/company.xml')/company/employees/employee/department)\n"
                    + "let $count := count(doc('/db/pruebas/company.xml')/company/employees/employee[department = $dept])\n"
                    + "return <department name=\"{$dept}\">\n"
                    + "    <count>{ $count }</count>\n"
                    + "</department>";

            String cad3 = "for $employee in doc('/db/pruebas/company.xml')/company/employees/employee[salary > 70000]\n"
                    + "return <result>\n"
                    + "    <name>{ $employee/name/text() }</name>\n"
                    + "    <salary>{ $employee/salary/text() }</salary>\n"
                    + "</result>";

            String cad4 = "for $n in doc('/db/pruebas/Clientes.xml')/clientes/cliente "
                    + "where substring($n/CP, 1, 2) = '29' "
                    + "return concat($n/apellidos, '-', string($n/@DNI))";
            
            String cad5 = "for $apellido in doc('/db/pruebas/Clientes.xml')//clientes/cliente/apellidos return $apellido/text()";
            String cad6 = "for $n in doc('/db/pruebas/Clientes.xml')//cliente return $n/apellidos";
            String cad7 = """
                          for $cliente in doc('/db/pruebas/Clientes.xml')//clientes/cliente
                          return 
                              <cliente>
                                  <apellidos>{$cliente/apellidos/text()}</apellidos>
                                  <CP>{$cliente/CP/text()}</CP>
                              </cliente>
                          """;
            String cad8 = """
                          for $empleado in doc('/db/pruebas/company.xml')//company/employees/employee
                          where $empleado/number(salary) > 70000
                          return $empleado/name
                          """;
            String cad9 = """
                          for $empleado in doc('/db/pruebas/company.xml')//company/employees/employee
                          let $departamento := $empleado/department
                          return concat($empleado/name,' ',count($departamento))
                          """;
            String cad10 = """
                          for $departamento in distinct-values(doc('/db/pruebas/company.xml')//company/employees/employee/department)
                          let $numeroempleados := count(for $empleados in doc('/db/pruebas/company.xml')//company/employees/employee
                          where $empleados/department = $departamento
                          return $empleados)
                          return concat($departamento, ':', $numeroempleados)
                          """;
            XQExpression xqe = c.createExpression();
            XQResultSequence xqrs = xqe.executeQuery(cad10);

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
}

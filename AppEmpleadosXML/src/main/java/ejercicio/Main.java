package ejercicio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        ArrayList<Empleado> empleados = new ArrayList<>();
        empleados.add(new Empleado(1, "Alfredo", "Calle Mes",1200,100));
        empleados.add(new Empleado(2, "Luis", "Calle Mes",1300,50));
        empleados.add(new Empleado(3, "Matt", "Calle Mes",1400,500));
        empleados.add(new Empleado(4, "Fred", "Calle Mes",1500,400));
        try {
            RandomAccessFile raf = new RandomAccessFile("EMPLEADOS.DAT", "rw");
            for (Empleado empleado:empleados) {
                raf.writeInt(empleado.getCodigo());
                StringBuffer sb = new StringBuffer(empleado.getNombre());
                sb.setLength(20);
                raf.writeChars(sb.toString());
                sb = new StringBuffer(empleado.getDireccion());
                sb.setLength(20);
                raf.writeChars(sb.toString());
                raf.writeFloat(empleado.getSalario());
                raf.writeFloat(empleado.getComision());

            }

            raf.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}

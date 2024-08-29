
package empmanager;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EmpleadoManager {
    private RandomAccessFile rcods, remps;
    
    public EmpleadoManager() {
        try {
            // Asegurar que el folder de Company exista
            File mf = new File("company");
            mf.mkdir();
            
            // Instanciar los RAFs dentro del folder Company
            rcods = new RandomAccessFile("company/codigos.emp", "rw");
            remps = new RandomAccessFile("company/empleados.emp", "rw");
            
            // Inicializar el archivo de codigos, si es nuevo
            initCodes();
        } catch(IOException e) {
            System.out.println("Error");
        }
    }
    
    private void initCodes() throws IOException {
        if(rcods.length() == 0)
            rcods.writeInt(1);
    }
    
    private int getCode() throws IOException {
        rcods.seek(0);
        int codigo = rcods.readInt();
        rcods.seek(0);
        rcods.writeInt(codigo + 1);
        
        return codigo;
    }
    
    public void addEmployee(String name, double monto) throws IOException {
        remps.seek(remps.length());
        
        int code = getCode();
        remps.writeInt(code);
        remps.writeUTF(name);
        remps.writeDouble(monto);
        remps.writeLong(Calendar.getInstance().getTimeInMillis());
        remps.writeLong(0);
        
        createEmployeeFolders(code);
    }
    
    private String employeeFolder(int code) {
        return "company/empleado" + code;
    }
    
    private void createEmployeeFolders(int code) throws IOException {
        File edir = new File(employeeFolder(code));
        edir.mkdir();
    }
    
    private RandomAccessFile salesFileFor(int code) throws IOException {
        String dirPadre = employeeFolder(code);
        int yearActual = Calendar.getInstance().get(Calendar.YEAR);
        String path = dirPadre + "/ventas" + yearActual + ".emp";
        
        return new RandomAccessFile(path, "rw");
    }
    
    private void createYearSalesFileFor(int code) throws IOException {
        RandomAccessFile raf = salesFileFor(code);
        if(raf.length() == 0) {
            for(int mes = 0; mes < 12; mes++) {
                raf.writeDouble(0);
                raf.writeBoolean(false);
            }
        }
    }
    
    public void listarEmpleadosActivos() throws IOException {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        remps.seek(0);
        System.out.println("==== Empleados ===");
        
        while(remps.getFilePointer() < remps.length()) {
            int code = remps.readInt();
            String nombre = remps.readUTF();
            double salario = remps.readDouble();
            long fechaContratacion = remps.readLong();
            long fechaDespido = remps.readLong();
            
            if(fechaDespido == 0) {
                String date = formatoFecha.format(new Date(fechaContratacion));
                System.out.println("Codigo: " + code + " - Nombre: " + nombre + " - Salario: Lps." 
                        + salario + " - Fecha de contratacion: " + date);
            }
        }
    }
    
    public boolean empleadoActivo(int code) throws IOException {
        remps.seek(0);

        while(remps.getFilePointer() < remps.length()) {
            int empCodigo = remps.readInt();
            String nombre = remps.readUTF();
            double salario = remps.readDouble();
            long fechaContratacion = remps.readLong();
            long fechaDespido = remps.readLong();
            
            if(empCodigo == code) {
                if(fechaDespido == 0) {
                    System.out.println("Codigo: " + empCodigo);
                    System.out.println("Nombre: " + nombre);
                    System.out.println("Salario: " + salario);
                    System.out.println("Fecha de contratacion: " + new Date(fechaContratacion));
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }
    
    public boolean despedirEmpleado(int code) throws IOException {
        remps.seek(0);
        
        while(remps.getFilePointer() < remps.length()) {
            int empCodigo = remps.readInt();
            String nombre = remps.readUTF();
            double salario = remps.readDouble();
            long fechaContratacion = remps.readLong();
            long fechaDespido = remps.readLong();
            
            if(empCodigo == code) {
                if(fechaDespido == 0) {
                    long currentPosition = remps.getFilePointer();
                    remps.seek(currentPosition - 8);
                    remps.writeLong(Calendar.getInstance().getTimeInMillis());
                    return true;
                } else {
                    return false;
                }
            }
        }
        
        System.out.println("El empleado no fue encontrado");
        return false;
    }
}

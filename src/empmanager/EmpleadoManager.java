
package empmanager;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Calendar;

public class EmpleadoManager {
    private RandomAccessFile rcods, remps;
    
    public EmpleadoManager() {
        try {
            
            //1) Asegurar que el folder de Company exista
            File mf = new File("company");
            mf.mkdir();
            
            //2) Instanciar los RAFs dentro del folder Company
            rcods = new RandomAccessFile("company/codigos.emp", "rw");
            remps = new RandomAccessFile("company/empleados.emp", "rw");
            
            //3) Inicializar el archivo de codigos, si es nuevo
            
        } catch(IOException e) {
            System.out.println("Error");
        }
    }
    
    private void initCodes() throws IOException {
        //Cotejar el tamano del array
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
    
    public void addEmploye(String name, double monto) throws IOException {
        remps.seek(remps.length());
        
        //Code
        int code = getCode();
        remps.writeInt(code);
        
        //Nombre
        remps.writeUTF(name);
        
        //Salario
        remps.writeDouble(monto);
        
        //Fecha Contratacion
        remps.writeLong(Calendar.getInstance().getTimeInMillis());
        
        //Fecha Despido
        remps.writeLong(0);
        
        //Crear carpeta y archivo individual de cada empleado
        createEmployeeFolders(code);
    }
    
    private String employeeFolder(int code) {
        return "company/empleado" + code;
    }
    
    private void createEmployeeFolders(int code) throws IOException {
        File edir = new File(employeeFolder(code));
        edir.mkdir();
        
        //Crear archivo del empleado
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
}

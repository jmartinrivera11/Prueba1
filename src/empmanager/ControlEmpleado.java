
package empmanager;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ControlEmpleado {
    public static void main(String[] args) {
        EmpleadoManager empManager = new EmpleadoManager();
        Scanner lea = new Scanner(System.in);
        
        int opcion = 0;
        while(opcion != 5) {
            System.out.println("\n==== Menu Principal ====");
            System.out.println("1) Agregar empleado");
            System.out.println("2) Listar empleados no despedidos");
            System.out.println("3) Despedir empleado");
            System.out.println("4) Buscar empleado activo");
            System.out.println("5) Salir");
            System.out.print("Seleccionar una opcion: ");
            
            try {
                opcion = lea.nextInt();
                lea.nextLine();
                
                switch(opcion) {
                    case 1:
                        try {
                            System.out.print("Ingresar nombre del empleado: ");
                            String nombre = lea.nextLine();
                            System.out.print("Ingresar salario del empleado: ");
                            double salario = lea.nextDouble();
                            empManager.addEmployee(nombre, salario);
                        } catch (InputMismatchException e) {
                            lea.nextLine();
                            System.out.println("Ingresar una entrada valida");
                        }
                        break;
                        
                    case 2:
                        empManager.listarEmpleadosActivos();
                        break;
                        
                    case 3:
                        try {
                            System.out.print("Ingresar codigo del empleado a despedir: ");
                            int code = lea.nextInt();
                            
                            if(empManager.despedirEmpleado(code))
                                System.out.println("El empleado fue despedido");
                            else
                                System.out.println("El empleado ya ha sido despedido o no fue encontrado");
                            
                        } catch (InputMismatchException e) {
                            lea.nextLine();
                            System.out.println("Ingresar una entrada valida");
                        }
                        break;
                        
                    case 4:
                        try {
                            System.out.print("Ingresar codigo del empleado: ");
                            int searchCode = lea.nextInt();
                            if(empManager.empleadoActivo(searchCode)) {
                                System.out.println("Empleado ACTIVO");
                            } else {
                                System.out.println("Empleado INACTIVO o no existe");
                            }
                        } catch (InputMismatchException e) {
                            lea.nextLine();
                            System.out.println("Por favor ingrese un código válido.");
                        }
                        break;
                        
                    case 5:
                        System.out.println("Fin del programa");
                        break;
                        
                    default:
                        System.out.println("Intentar de nuevo, la opcion no es valida");
                        break;
                }
            } catch (InputMismatchException e) {
                lea.nextLine();
                System.out.println("Ingresar una opcion valida");
            } catch (IOException e) {
                System.out.println("ERROR: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }
}

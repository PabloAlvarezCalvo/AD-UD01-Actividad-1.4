/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ad.teis.persistencia;

import ad.teis.model.Persona;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 *
 * @author a21pabloac1
 */

//5. Crea una clase RandomAccessPersistencia que implemente la interfaz Persistencia con RandomAccessFile
public class RandomAccessPersistencia implements IPersistencia {
    private static final int LONG_BYTES_PERSONA = 43;

    @Override
    public void escribirPersona(Persona persona, String ruta) {
        if (persona != null) { 
            try (
                    RandomAccessFile raf = new RandomAccessFile(ruta, "rw");
                ){
                
                StringBuilder sb = new StringBuilder();
                sb.setLength(9);
                
                sb.append(persona.getDni());
                
                raf.seek(raf.length());
                raf.writeLong(persona.getId());
                raf.writeChars(sb.toString());
                //raf.writeUTF(sb.toString());
                raf.writeInt(persona.getEdad());
                raf.writeFloat(persona.getSalario());
            } 
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
                System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
            }
            catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
            }
        }
    }
    
    public void escribirPersonas(ArrayList<Persona> personas, String ruta){
        // debe escribir los datos de todas las personas  en el mismo orden 
        //que escribirPersona. Si el fichero tiene datos, debe comenzar 
        //a escribir desde el final y no sobreescribir todo el fichero. 
         
         if (personas != null) {
            try (RandomAccessFile raf = new RandomAccessFile(ruta, "rw");) {
                 
                raf.seek(raf.length());
                for (Persona persona : personas) {
                    StringBuilder sb = new StringBuilder();
                    sb.setLength(9);

                    sb.append(persona.getDni());

                    raf.seek(raf.length());
                    raf.writeLong(persona.getId());
                    raf.writeChars(sb.toString());
                    //raf.writeUTF(sb.toString());
                    raf.writeInt(persona.getEdad());
                    raf.writeFloat(persona.getSalario());
                }
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
                System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
            }
            catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
            }
         }
     }

    @Override
    public Persona leerDatos(String ruta) {
        long id;
        String dni = "";
        StringBuilder auxDni = new StringBuilder();
        auxDni.setLength(9);
        int edad;
        float salario;
        Persona persona = null;
        
        if (Files.exists(Paths.get(ruta))) {
            try (
                    RandomAccessFile raf = new RandomAccessFile(ruta, "r");
                ){
                
                //(0) long id (8 bytes)
                //(9) String dni (9x2 bytes);
                //(27) int edad (4 bytes);
                //(33) float salario (4 bytes);
                
                //raf.seek(raf.getFilePointer());
                id = raf.readLong();
                
                //raf.seek(9);
                for(int i = 0; i < 9; i++) {
                    //auxDni += raf.readChar();
                    auxDni.append(raf.readChar());
                }
                    
                //raf.seek(19);
                edad = raf.readInt();
                
                //raf.seek(23);
                salario = raf.readFloat();
 
                
                persona = new Persona(id, auxDni.toString(), edad, salario);
                
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
            }
        }
        
        return persona;
    }
    
    public Persona leerPersona(int posicion, String ruta){
        //debe posicionarse en la persona que ocupa la posición indicada 
        //por el parámetro posicion y devolver un objeto Persona 
        //con los datos correspondientes. Si posicion = 1, el puntero del fichero 
        //deberá situarse en el byte 0, si posicion =2, el puntero debe situarse 
        //en el byte= número de bytes que ocupa cada persona, etc.
        
        long id;
        String dni = "";
        StringBuilder auxDni = new StringBuilder();
        auxDni.setLength(9);
        int edad;
        float salario;
        Persona persona = null;
        
        if (Files.exists(Paths.get(ruta))) {
            try (
                    RandomAccessFile raf = new RandomAccessFile(ruta, "r");
                ){
                
                //(0) long id (8 bytes)
                //(9) String dni (9x2 bytes);
                //(27) int edad (4 bytes);
                //(33) float salario (4 bytes);
                
                raf.seek(convertPositionToBytes(posicion));
                
                id = raf.readLong();
                
                //raf.seek(9);
                for(int i = 0; i < 9; i++) {
                    //auxDni += raf.readChar();
                    auxDni.append(raf.readChar());
                }
                
                //raf.seek(19);
                edad = raf.readInt();
                
                //raf.seek(23);
                salario = raf.readFloat();
                
                persona = new Persona(id, auxDni.toString(), edad, salario);
                
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
            } catch (EOFException ex) {
                ex.printStackTrace();
                System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
            }
        }
        
        return persona;
    }
    
    public ArrayList<Persona> leerTodo(String ruta) {
        //Debe leer un fichero desde el principio y construir un ArrayList de Persona
        
        long id;
        String dni = "";
        StringBuilder auxDni = new StringBuilder();
        auxDni.setLength(9);
        int edad;
        float salario;
        Persona persona = null;
        ArrayList<Persona> personas = new ArrayList<>();
        
        try (
                RandomAccessFile raf = new RandomAccessFile(ruta, "r");
            )
        {
            
            do {
                id = raf.readLong();
                for(int i = 0; i < 9; i++) {
                    //auxDni += raf.readChar();
                    auxDni.append(raf.readChar());
                }
                edad = raf.readInt();  
                salario = raf.readFloat();
                
                persona = new Persona(id, dni, edad, salario);
                personas.add(persona);
                
            }while (raf.getFilePointer() < raf.length());
            
        } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
            }
        
        return personas;
    }
    
     Persona add(int posicion, String ruta, Persona persona){
        //debe añadir un objeto Persona en la posición indicada. 
        //La posicion =1, indica posicionarse en el byte cero, etc. 
        //Si ya existía una persona en esa posición, se sobrescribirá.
        return null;
     }
     
     float sumarSalario(int posicion, String ruta, float incremento){
        //Debe situarse en la Persona que ocupa la posicion indicada
        //(mismas consideraciones que en los apartados anteriores con posición), 
        //leer su salario e incrementarlo en la cantidad incremento.
        //Devuelve el nuevo salario.
        return 0.0f;
     }
     
     boolean borrar(int posicion, String ruta, boolean borrado){
        //En lugar de borrar un registro físicamente, 
        //vamos a marcarlo como borrado con un campo booleano indicando 
        //si está borrado o no. Para ello, tendremos que añadir un atributo
        //borrado a la clase Persona y modificar los métodos que hemos creado
        //para escribir y leer ese campo. Tendremos que tener en cuenta que
        //se modifica la longitud en bytes del registro de una objeto Persona.
        //El método borrar buscará en el fichero indicado por ruta, la Persona
        //en la posición indicada en el parámetro y establecerá el valor borrado
        //en el fichero. Prueba desde el Main el método.
        
        return false;
     }
     
    private long convertPositionToBytes(int posicion){
        if (posicion == 1) { return 0; } else { return LONG_BYTES_PERSONA * posicion -1; } 
    }
    
}

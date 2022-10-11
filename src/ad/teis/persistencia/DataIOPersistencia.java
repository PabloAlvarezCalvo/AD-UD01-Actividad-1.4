/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ad.teis.persistencia;

import ad.teis.model.Persona;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author a21pabloac1
 */
public class DataIOPersistencia implements IPersistencia {

    @Override
    public void escribirPersona(Persona persona, String ruta) {
        if (persona != null) {
            try (
                    FileOutputStream fos = new FileOutputStream(ruta);
                    DataOutputStream dos = new DataOutputStream(fos);
                ){
                dos.writeLong(persona.getId());
                dos.writeChars(persona.getDni());
                dos.writeUTF(persona.getDni());
                dos.writeInt(persona.getEdad());
                dos.writeFloat(persona.getSalario());
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
        int edad;
        float salario;
        Persona persona = null;
        
        if (Files.exists(Paths.get(ruta))) {
            try (
                    FileInputStream fos = new FileInputStream(ruta);
                    DataInputStream dis = new DataInputStream(fos);
                ){
                
                id = dis.readLong();
                System.out.println("ID: " + id);
                
                StringBuilder auxDni = new StringBuilder();
                for(int i = 0; i < 9; i++) {
                    //auxDni += dis.readChar();
                    auxDni.append(dis.readChar());
                }

                dni = dis.readUTF();
                
                edad = dis.readInt();
                
                salario = dis.readFloat();
                
                persona = new Persona(id, dni, edad, salario);
                
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
    
    
}

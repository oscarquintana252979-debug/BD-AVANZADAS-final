/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.equipo4.bibliotecamusical.negocio;
import org.mindrot.jbcrypt.BCrypt;
/**
 *
 * @author oscar
 */
public class UtilidadesSeguridad {

    public static String encriptarContrasena(String contrasenaPlana) {
        return BCrypt.hashpw(contrasenaPlana, BCrypt.gensalt());
    }

    public static boolean verificarContrasena(String contrasenaPlana, String contrasenaEncriptada) {
        return BCrypt.checkpw(contrasenaPlana, contrasenaEncriptada);
    }
}
package edu.upc.dsa;

import edu.upc.dsa.models.*;
import org.apache.log4j.Logger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CampusManagerImpl implements CampusManager {
    private static CampusManagerImpl instance;
    private final Map<String, Usuario> usuarios = new HashMap<>();
    private final Map<String, PuntoInteres> puntosDeInteres = new HashMap<>();
    final static Logger logger = Logger.getLogger(CampusManagerImpl.class);

    // Constructor privado para el patrón Singleton
    private CampusManagerImpl() {}

    // Método para obtener la instancia única de la implementación
    public static CampusManagerImpl getInstance() {
        if (instance == null) {
            instance = new CampusManagerImpl();
        }
        return instance;
    }

    @Override
    public void addUsuario(String id, String nombre, String apellidos, String email, LocalDate fechaNacimiento) {
        logger.info("Añadiendo usuario: " + id);
        if (!usuarios.containsKey(id)) {
            usuarios.put(id, new Usuario(id, nombre, apellidos, email, fechaNacimiento));
            logger.info("Usuario añadido: " + nombre + " " + apellidos);
        } else {
            logger.error("Error: El usuario con ID " + id + " ya existe.");
        }
    }

    @Override
    public List<Usuario> listarUsuarios() {
        List<Usuario> listaUsuarios = new ArrayList<>(usuarios.values());
        listaUsuarios.sort((u1, u2) -> (u1.getApellidos() + u1.getNombre()).compareTo(u2.getApellidos() + u2.getNombre()));
        return listaUsuarios;
    }

    @Override
    public Usuario consultarUsuario(String id) {
        Usuario usuario = usuarios.get(id);
        if (usuario == null) {
            logger.error("El usuario con ID " + id + " no existe.");
        }
        return usuario;
    }


    @Override
    public void addPuntoInteres(int x, int y, ElementType tipo) {
        String key = x + "," + y;
        logger.info("Añadiendo punto de interés en coordenadas " + key);
        if (!puntosDeInteres.containsKey(key)) {
            puntosDeInteres.put(key, new PuntoInteres(x, y, tipo));
            logger.info("Punto de interés añadido: " + tipo + " en " + key);
        } else {
            logger.error("Error: Punto de interés ya existe en coordenadas " + key);
        }
    }

    @Override
    public List<PuntoInteres> consultarPuntosPorTipo(ElementType tipo) {
        List<PuntoInteres> result = new ArrayList<>();
        for (PuntoInteres p : puntosDeInteres.values()) {
            if (p.getTipo() == tipo) result.add(p);
        }
        return result;
    }

    @Override
    public void registrarPaso(String idUsuario, int x, int y) {
        String key = x + "," + y;
        Usuario usuario = usuarios.get(idUsuario);
        PuntoInteres punto = puntosDeInteres.get(key);

        if (usuario == null) {
            logger.error("Error: El usuario con ID " + idUsuario + " no existe.");
        } else if (punto == null) {
            logger.error("Error: El punto de interés en coordenadas " + key + " no existe.");
        } else {
            usuario.registrarPaso(punto);
            logger.info("Registrado paso de usuario " + idUsuario + " por punto de interés en " + key);
        }
    }


    @Override
    public List<PuntoInteres> consultarPasosUsuario(String idUsuario) {
        Usuario usuario = usuarios.get(idUsuario);
        return usuario != null ? usuario.getPuntosVisitados() : new ArrayList<>();
    }

    @Override
    public List<Usuario> consultarUsuariosPorPunto(int x, int y) {
        List<Usuario> usuariosPasaron = new ArrayList<>();
        String key = x + "," + y;

        for (Usuario u : usuarios.values()) {
            // Revisar si algún punto visitado por el usuario coincide con las coordenadas
            if (u.getPuntosVisitados().stream().anyMatch(p -> p.getCoordenadaX() == x && p.getCoordenadaY() == y)) {
                usuariosPasaron.add(u);
            }
        }
        return usuariosPasaron;
    }

}

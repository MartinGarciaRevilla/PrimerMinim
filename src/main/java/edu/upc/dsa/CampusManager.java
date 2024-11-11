package edu.upc.dsa;

import edu.upc.dsa.models.*;
import java.time.LocalDate;
import java.util.List;

public interface CampusManager {
    void addUsuario(String id, String nombre, String apellidos, String email, LocalDate fechaNacimiento);
    List<Usuario> listarUsuarios();
    Usuario consultarUsuario(String id);

    void addPuntoInteres(int x, int y, ElementType tipo);
    List<PuntoInteres> consultarPuntosPorTipo(ElementType tipo);

    void registrarPaso(String idUsuario, int x, int y);
    List<PuntoInteres> consultarPasosUsuario(String idUsuario);
    List<Usuario> consultarUsuariosPorPunto(int x, int y);
}

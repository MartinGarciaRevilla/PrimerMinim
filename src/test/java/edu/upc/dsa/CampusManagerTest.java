package edu.upc.dsa;

import edu.upc.dsa.CampusManager;
import edu.upc.dsa.CampusManagerImpl;
import edu.upc.dsa.models.ElementType;
import edu.upc.dsa.models.PuntoInteres;
import edu.upc.dsa.models.Usuario;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CampusManagerTest {
    private CampusManager manager;

    @Before
    public void setUp() {
        manager = CampusManagerImpl.getInstance();

        // Reiniciar el estado del sistema antes de cada prueba
        manager.addUsuario("1", "Juan", "Pérez", "juan@example.com", LocalDate.of(1990, 1, 1));
        manager.addUsuario("2", "Maria", "Lopez", "maria@example.com", LocalDate.of(1985, 5, 15));
        manager.addUsuario("3", "Carlos", "Garcia", "carlos@example.com", LocalDate.of(1992, 3, 12));
        manager.addUsuario("4", "Ana", "Martinez", "ana@example.com", LocalDate.of(1988, 7, 22));
        manager.addUsuario("5", "Luis", "Sanchez", "luis@example.com", LocalDate.of(1995, 9, 30));
    }

    @Test
    public void testListarUsuarios() {
        List<Usuario> usuarios = manager.listarUsuarios();
        assertEquals("La lista de usuarios debe contener 5 usuarios", 5, usuarios.size());
    }

    @Test
    public void testConsultarUsuario() {
        Usuario usuario = manager.consultarUsuario("2"); // ID de Maria, usuario existente
        assertNotNull("El usuario con ID '2' debería existir", usuario);
        assertEquals("El nombre del usuario debería ser 'Maria'", "Maria", usuario.getNombre());
    }


    @Test
    public void testAddPuntoInteresYConsultarPorTipo() {
        manager.addPuntoInteres(10, 10, ElementType.POTION);
        List<PuntoInteres> puntos = manager.consultarPuntosPorTipo(ElementType.POTION);
        assertEquals("Debe haber un punto de interés de tipo POTION", 1, puntos.size());
    }

    @Test
    public void testRegistrarPasoUsuario() {
        // Aseguramos que Carlos y su punto de interés existen antes de registrar el paso
        manager.addUsuario("3", "Carlos", "Garcia", "carlos@example.com", LocalDate.of(1992, 3, 12));
        manager.addPuntoInteres(5, 10, ElementType.WALL);

        // Carlos pasa por el punto (5,10)
        manager.registrarPaso("3", 5, 10);

        // Comprobamos que el usuario Carlos tenga un punto visitado registrado
        List<PuntoInteres> puntosVisitados = manager.consultarPasosUsuario("3");
        assertEquals("El usuario 'Carlos' debería haber visitado un punto de interés", 1, puntosVisitados.size());
    }


    @Test
    public void testConsultarUsuariosPorPunto() {
        // Aseguramos que Maria y el punto de interés (15, 25) existan antes de registrar el paso
        manager.addUsuario("2", "Maria", "Lopez", "maria@example.com", LocalDate.of(1985, 5, 15));
        manager.addPuntoInteres(15, 25, ElementType.BRIDGE);

        // Registramos que Maria pasa por el punto (15, 25)
        manager.registrarPaso("2", 15, 25);

        // Consultamos usuarios que han pasado por el punto (15, 25)
        List<Usuario> usuariosEnPunto = manager.consultarUsuariosPorPunto(15, 25);

        // Verificamos que la lista contiene a Maria
        assertEquals("Debe haber un usuario que pasó por el punto (15,25)", 1, usuariosEnPunto.size());
        assertEquals("El usuario en el punto (15,25) debería ser Maria", "Maria", usuariosEnPunto.get(0).getNombre());
    }

}

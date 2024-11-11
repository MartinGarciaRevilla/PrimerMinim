package edu.upc.dsa.services;

import edu.upc.dsa.CampusManager;
import edu.upc.dsa.CampusManagerImpl;
import edu.upc.dsa.models.ElementType;
import edu.upc.dsa.models.PuntoInteres;
import edu.upc.dsa.models.Usuario;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;

@Api(value = "/campus", description = "Endpoint to Campus Service")
@Path("/campus")
public class CampusService {
    private CampusManager cm;

    public CampusService() {
        this.cm = CampusManagerImpl.getInstance();

        // Usuarios y puntos de interés iniciales
        if (cm.listarUsuarios().isEmpty()) {
            // Usuario 1
            cm.addUsuario("1", "Juan", "Pérez", "juan@example.com", LocalDate.of(1990, 1, 1));
            cm.addPuntoInteres(10, 20, ElementType.DOOR);
            cm.registrarPaso("1", 10, 20);

            // Usuario 2
            cm.addUsuario("2", "Maria", "Lopez", "maria@example.com", LocalDate.of(1985, 5, 15));
            cm.addPuntoInteres(15, 25, ElementType.BRIDGE);
            cm.registrarPaso("2", 15, 25);

            // Usuario 3
            cm.addUsuario("3", "Carlos", "Garcia", "carlos@example.com", LocalDate.of(1992, 3, 12));
            cm.addPuntoInteres(5, 10, ElementType.WALL);
            cm.registrarPaso("3", 5, 10);

            // Usuario 4
            cm.addUsuario("4", "Ana", "Martinez", "ana@example.com", LocalDate.of(1988, 7, 22));
            cm.addPuntoInteres(8, 16, ElementType.TREE);
            cm.registrarPaso("4", 8, 16);

            // Usuario 5
            cm.addUsuario("5", "Luis", "Sanchez", "luis@example.com", LocalDate.of(1995, 9, 30));
            cm.addPuntoInteres(12, 18, ElementType.COIN);
            cm.registrarPaso("5", 12, 18);
        }
    }
    @GET
    @ApiOperation(value = "List all users", notes = "Returns a list of all users ordered by last name and first name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = List.class),
            @ApiResponse(code = 500, message = "Server Error")
    })
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        List<Usuario> users = this.cm.listarUsuarios();
        GenericEntity<List<Usuario>> entity = new GenericEntity<List<Usuario>>(users) {};
        return Response.status(200).entity(entity).build();
    }

    @POST
    @ApiOperation(value = "Add a new user", notes = "Adds a new user to the system")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User successfully added"),
            @ApiResponse(code = 500, message = "Server Error")
    })
    @Path("/users/add/{id}/{nombre}/{apellidos}/{email}/{year}/{month}/{day}")
    public Response addUser(
            @PathParam("id") String id,
            @PathParam("nombre") String nombre,
            @PathParam("apellidos") String apellidos,
            @PathParam("email") String email,
            @PathParam("year") int year,
            @PathParam("month") int month,
            @PathParam("day") int day) {
        LocalDate birthDate = LocalDate.of(year, month, day);
        this.cm.addUsuario(id, nombre, apellidos, email, birthDate);
        return Response.status(200).build();
    }

    @POST
    @ApiOperation(value = "Add a point of interest", notes = "Adds a new point of interest to the campus map")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Point of interest successfully added"),
            @ApiResponse(code = 500, message = "Server Error")
    })
    @Path("/points/add/{x}/{y}/{tipo}")
    public Response addPointOfInterest(
            @PathParam("x") int x,
            @PathParam("y") int y,
            @PathParam("tipo") ElementType tipo) {
        this.cm.addPuntoInteres(x, y, tipo);
        return Response.status(200).build();
    }

    @GET
    @ApiOperation(value = "Get points by type", notes = "Returns a list of all points of a specified type")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = List.class),
            @ApiResponse(code = 404, message = "Type not found"),
            @ApiResponse(code = 500, message = "Server Error")
    })
    @Path("/points/{tipo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPointsByType(@PathParam("tipo") ElementType tipo) {
        List<PuntoInteres> points = this.cm.consultarPuntosPorTipo(tipo);
        if (points.isEmpty()) {
            return Response.status(404).entity("No points of interest found for type: " + tipo).build();
        }
        GenericEntity<List<PuntoInteres>> entity = new GenericEntity<List<PuntoInteres>>(points) {};
        return Response.status(200).entity(entity).build();
    }

    @POST
    @ApiOperation(value = "Register a user passing through a point", notes = "Records that a user has passed through a specified point of interest")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Pass registered successfully"),
            @ApiResponse(code = 404, message = "User or Point not found"),
            @ApiResponse(code = 500, message = "Server Error")
    })
    @Path("/users/pass/{userId}/{x}/{y}")
    public Response registerUserPass(
            @PathParam("userId") String userId,
            @PathParam("x") int x,
            @PathParam("y") int y) {
        try {
            this.cm.registrarPaso(userId, x, y);
            return Response.status(200).build();
        } catch (Exception e) {
            return Response.status(404).entity("User or Point not found").build();
        }
    }

    @GET
    @ApiOperation(value = "Get points visited by user", notes = "Returns the list of points of interest visited by a specified user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = List.class),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 500, message = "Server Error")
    })
    @Path("/users/{userId}/visited")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserVisitedPoints(@PathParam("userId") String userId) {
        List<PuntoInteres> visitedPoints = this.cm.consultarPasosUsuario(userId);
        if (visitedPoints.isEmpty()) {
            return Response.status(404).entity("User not found or no points visited").build();
        }
        GenericEntity<List<PuntoInteres>> entity = new GenericEntity<List<PuntoInteres>>(visitedPoints) {};
        return Response.status(200).entity(entity).build();
    }
}

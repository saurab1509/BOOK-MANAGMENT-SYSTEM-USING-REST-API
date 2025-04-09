package com.bookstore.resource;

import com.bookstore.Book;
import com.bookstore.BookService;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    BookService service = new BookService();

    @GET
    public List<Book> getAllBooks() {
        return service.getAllBooks();
    }

    @POST
    public Response addBook(Book book) {
        service.addBook(book);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") int id, Book updatedBook) {
        boolean updated = service.updateBook(id, updatedBook);
        if (updated) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        boolean deleted = service.deleteBook(id);
        if (deleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}

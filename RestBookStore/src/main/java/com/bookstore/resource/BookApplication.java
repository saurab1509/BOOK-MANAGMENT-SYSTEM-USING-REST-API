package com.bookstore.resource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class BookApplication extends Application {
    // No need to override anything; Jersey scans @Path classes automatically.
}

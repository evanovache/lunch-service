package com.etz.foodapp.order.api;

import com.etz.foodapp.auth.User;
import com.etz.foodapp.common.jpa.EntityManagerProvider;
import com.etz.foodapp.common.time.SystemTimeProvider;
import com.etz.foodapp.order.dto.OrderRequest;
import com.etz.foodapp.order.dto.OrderResponse;
import com.etz.foodapp.order.service.OrderService;

import jakarta.persistence.EntityManager;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/orders") 
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {
    
    @POST 
    public Response placeOrder(OrderRequest request) {

        EntityManager em = EntityManagerProvider.createEntityManager();

        try {
            User user = em.find(User.class, 1L);

            OrderService orderService =  
                    new OrderService(em, new SystemTimeProvider());

            OrderResponse response = orderService.placeOrder(user, request);

            return Response.ok(response).build(); 

        } catch (IllegalStateException e) {

            return Response.status(Response.Status.BAD_REQUEST) 
                           .entity(e.getMessage())
                           .build();

        } finally {
            em.close();
        }
    }
}

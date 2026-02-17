package com.etz.foodapp.order.api;

import com.etz.foodapp.auth.User;
import com.etz.foodapp.order.dto.OrderRequest;
import com.etz.foodapp.order.dto.OrderResponse;
import com.etz.foodapp.order.service.OrderService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/orders") 
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class OrderResource {

    @Inject 
    private OrderService orderService;

    @PersistenceContext 
    private EntityManager em;
    
    @POST 
    public Response placeOrder(OrderRequest request) {
  
        User user = em.find(User.class, 1L);

        OrderResponse response = orderService.placeOrder(user, request);

        return Response.ok(response).build(); 
    }
}

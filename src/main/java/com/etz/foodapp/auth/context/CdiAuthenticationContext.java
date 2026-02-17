package com.etz.foodapp.auth.context;

import com.etz.foodapp.auth.User;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;

@RequestScoped
public class CdiAuthenticationContext implements AuthenticationContext {
    
    @PersistenceContext
    private EntityManager em;

    @Context 
    private SecurityContext securityContext;

    @Override 
    public User getCurrentUser() {
        
        String email = securityContext.getUserPrincipal().getName();

        return em.createQuery(
            "SELECT u FROM User u WHERE u.email = :email", 
            User.class
        ) 
        .setParameter("email", email) 
        .getSingleResult();
    }
}

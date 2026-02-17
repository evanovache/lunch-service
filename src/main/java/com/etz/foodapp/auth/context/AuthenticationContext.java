package com.etz.foodapp.auth.context;

import com.etz.foodapp.auth.User;

public interface AuthenticationContext {
    
    User getCurrentUser();
}

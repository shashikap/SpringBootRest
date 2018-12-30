package com.bokks.micro.springbootrestapi.filters;

import com.bokks.micro.springbootrestapi.model.UserRoles;
import com.bokks.micro.springbootrestapi.service.TokenService;
import com.bokks.micro.springbootrestapi.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
public final class AuthenticationFilter {


    private static final String REALM = "example";
    private static final String AUTHENTICATION_SCHEME = "Bearer";
    @Autowired
    HttpServletRequest request;
    @Autowired
    TokenService tokenService;
    @Autowired
    UserService userService;

    private boolean isTokenBasedAuthentication(String authorizationHeader) {

        // Check if the Authorization header is valid
        // It must not be null and must be prefixed with "Bearer" plus a whitespace
        // The authentication scheme comparison must be case-insensitive
        return authorizationHeader != null && authorizationHeader.toLowerCase()
                .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }

    private boolean isValidToken(String token) {

        // Check if the Authorization header is valid
        // It must not be null and must be prefixed with "Bearer" plus a whitespace
        // The authentication scheme comparison must be case-insensitive
        return tokenService.isTokenValid(token);
    }


    @Around(" @annotation(com.bokks.micro.springbootrestapi.filters.Secured)")
    public Object validateAspect(ProceedingJoinPoint pjp) throws Throwable {

        try {
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method method = signature.getMethod();
            boolean authorizedrequest = false;

            Secured authorizedByType = method.getAnnotation(Secured.class);
            UserRoles[] rolesOfTheAPI = authorizedByType.authorizedBy();


            String authorizationHeaderString = request.getHeader(HttpHeaders.AUTHORIZATION);
            String authorizationTokenWithoutAuthScehma = authorizationHeaderString.substring(AUTHENTICATION_SCHEME.length()).trim();

            if (!isTokenBasedAuthentication(authorizationHeaderString)) {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
            if (!isValidToken(authorizationTokenWithoutAuthScehma)) {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }

            Enum userRole = getUserRolerFromToken(authorizationTokenWithoutAuthScehma);

            System.out.println("############################################ Filter was called ############################################");
            System.out.println("############################################ Value Of the annotation " + rolesOfTheAPI.toString() + " ############################################");

            // Call your Authorization server and check if all is good

            for (Enum rolesAllowed : rolesOfTheAPI) {
                if (rolesAllowed.equals(userRole)) {
                    authorizedrequest = true;
                }
            }

            if (authorizedrequest) {
                return pjp.proceed();
            }

            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }catch (Exception e){

            return new ResponseEntity(e.getMessage(),HttpStatus.UNAUTHORIZED);
        }

    }

    private UserRoles getUserRolerFromToken(String token) {

        String userName = tokenService.findByToken(token).getUsername();
        UserRoles userRoles = userService.findByUsername(userName).getUserRole();

        return userRoles;
    }
}



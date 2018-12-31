package com.bokks.micro.springbootrestapi.filters;

import com.bokks.micro.springbootrestapi.model.UserRoles;
import com.bokks.micro.springbootrestapi.service.TokenService;
import com.bokks.micro.springbootrestapi.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

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
        logger.info("Check the token is in valid format it should followed with 'Bearer' plus a whitespace");
        return authorizationHeader != null && authorizationHeader.toLowerCase()
                .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }

    private boolean isValidToken(String token) {

        // Check if the Authorization header is valid
        // It must not be null and must be prefixed with "Bearer" plus a whitespace
        // The authentication scheme comparison must be case-insensitive
        return tokenService.isTokenValid(token);
    }

    /**
     * This method will be invoke when the APIs having enable the @Secured annotation
     * Where this api will extract the roles assign for the specific apis and authetication
     * header send in the HTTP request.
     * Based on the authorization header method will revoke accessing to the API.
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around(" @annotation(com.bokks.micro.springbootrestapi.filters.Secured)")
    public Object validateAspect(ProceedingJoinPoint pjp) throws Throwable {

        try {
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method method = signature.getMethod();
            boolean authorizedrequest = false;

            Secured authorizedByType = method.getAnnotation(Secured.class);
            UserRoles[] rolesOfTheAPI = authorizedByType.authorizedBy();


            String authorizationHeaderString = request.getHeader(HttpHeaders.AUTHORIZATION);
            logger.info("Header received for Authorization {}",authorizationHeaderString);
            String authorizationTokenWithoutAuthScehma = authorizationHeaderString.substring(AUTHENTICATION_SCHEME.length()).trim();
            logger.info("Token received for Authorization {}",authorizationTokenWithoutAuthScehma);

            if (!isTokenBasedAuthentication(authorizationHeaderString)||!isValidToken(authorizationTokenWithoutAuthScehma)) {
                logger.info("Unauthorized token found");
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }

            Enum userRole = getUserRolerFromToken(authorizationTokenWithoutAuthScehma);

            for (Enum rolesAllowed : rolesOfTheAPI) {
                logger.info("Roles allowed by the API {}",rolesAllowed);
                if (rolesAllowed.equals(userRole)) {
                    logger.info("Valid authorization found in the token provided for the {}",userRole);
                    authorizedrequest = true;
                }
            }

            if (authorizedrequest) {
                logger.info("Valid Authorization provided for method{}",method.getName());
                return pjp.proceed();
            }
            logger.info("Request UnAuthorized");
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            logger.error("Exception thwon while processing  : {}",e.getCause());
            return new ResponseEntity(e.getMessage(),HttpStatus.UNAUTHORIZED);
        }

    }

    private UserRoles getUserRolerFromToken(String token) {

        String userName = tokenService.findByToken(token).getUsername();
        UserRoles userRoles = userService.findByUsername(userName).getUserRole();

        return userRoles;
    }
}



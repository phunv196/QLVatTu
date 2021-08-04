package com.app.filters;

import java.io.IOException;

import com.app.util.Constants;
import jakarta.annotation.Priority;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.core.MultivaluedMap;

@Priority(0)
public class CORSResponseFilter  implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        MultivaluedMap<String, Object> respHeaderMap = responseContext.getHeaders();
        String reqHeaderString = requestContext.getHeaderString("Access-Control-Request-Headers");

        respHeaderMap.add("Access-Control-Allow-Origin", "*");
        respHeaderMap.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        respHeaderMap.add("Access-Control-Allow-Credentials", "true");
        respHeaderMap.add("Access-Control-Allow-Headers","xsrf-token, X-Total-Results, Authorization, Content-type, Content-Disposition, timezone-id");
        //respHeaderMap.add("Access-Control-Allow-Headers", reqHeaderString);
        respHeaderMap.add("X-Powered-By", "Mrin-Order-API");
    }
}

package uk.gov.ida.notification.shared;

import org.apache.http.HttpHeaders;
import org.slf4j.MDC;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

@Provider
public class ProxyNodeLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private ProxyNodeLogger proxyNodeLogger = new ProxyNodeLogger();

    @Override
    public void filter(ContainerRequestContext requestContext) {
        proxyNodeLogger.addContext(ProxyNodeMDCKey.PN_JOURNEY_ID, getJourneyId(requestContext));
        proxyNodeLogger.addContext(ProxyNodeMDCKey.REFERER, requestContext.getHeaderString(HttpHeaders.REFERER));
        proxyNodeLogger.addContext(ProxyNodeMDCKey.RESOURCE_PATH, getResourcePath(requestContext));
        proxyNodeLogger.addContext(ProxyNodeMDCKey.INGRESS_MEDIA_TYPE, requestContext.getMediaType().getType());
        proxyNodeLogger.log(Level.INFO, () -> "ingress");
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        proxyNodeLogger.addContext(ProxyNodeMDCKey.EGRESS_LOCATION, responseContext.getLocation().toString());
        proxyNodeLogger.addContext(ProxyNodeMDCKey.EGRESS_STATUS, String.valueOf(responseContext.getStatus()));
        proxyNodeLogger.addContext(ProxyNodeMDCKey.EGRESS_MEDIA_TYPE, responseContext.getMediaType().getType());
        proxyNodeLogger.log(Level.INFO, () -> "egress");

        String journeyId = getJourneyId(requestContext);
        for (ProxyNodeMDCKey key : ProxyNodeMDCKey.values()) {
            MDC.remove(key.name());
        }
        responseContext.getHeaders().add(ProxyNodeMDCKey.PN_JOURNEY_ID.name(), journeyId);
    }

    private String getResourcePath(ContainerRequestContext requestContext) {
        return requestContext.getUriInfo().getPath();
    }

    private String getJourneyId(ContainerRequestContext requestContext) {
        return Objects.requireNonNullElse(requestContext.getHeaderString(ProxyNodeMDCKey.PN_JOURNEY_ID.name()), UUID.randomUUID().toString());
    }

}

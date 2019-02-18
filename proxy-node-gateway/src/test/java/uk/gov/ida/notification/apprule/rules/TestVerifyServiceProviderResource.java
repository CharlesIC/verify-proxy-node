package uk.gov.ida.notification.apprule.rules;

import org.glassfish.jersey.internal.util.Base64;
import org.opensaml.saml.saml2.core.AuthnRequest;
import uk.gov.ida.notification.contracts.verifyserviceprovider.AuthnRequestGenerationBody;
import uk.gov.ida.notification.contracts.verifyserviceprovider.AuthnRequestResponse;
import uk.gov.ida.notification.saml.SamlBuilder;
import uk.gov.ida.notification.saml.SamlObjectMarshaller;
import uk.gov.ida.notification.shared.Urls;

import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.net.URISyntaxException;

@Path(Urls.VerifyServiceProviderUrls.GENERATE_HUB_AUTHN_REQUEST_ENDPOINT)
@Produces(MediaType.APPLICATION_JSON)
public class TestVerifyServiceProviderResource {

    public static final String REQUEST_ID_HUB = "a hub request id";

    @POST
    @Valid
    public AuthnRequestResponse post(@Valid AuthnRequestGenerationBody request) throws URISyntaxException {
        AuthnRequest authnRequest = SamlBuilder.build(AuthnRequest.DEFAULT_ELEMENT_NAME);
        authnRequest.setID(REQUEST_ID_HUB);
        String encodedAuthnRequest = Base64.encodeAsString(new SamlObjectMarshaller().transformToString(authnRequest));
        return new AuthnRequestResponse(encodedAuthnRequest, REQUEST_ID_HUB, new URI("http://www.hub.com"));
    }
}
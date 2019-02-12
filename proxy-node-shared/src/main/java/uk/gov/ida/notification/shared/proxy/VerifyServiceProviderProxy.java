package uk.gov.ida.notification.shared.proxy;

import uk.gov.ida.jerseyclient.JsonClient;
import uk.gov.ida.notification.contracts.TranslatedHubResponse;
import uk.gov.ida.notification.contracts.VerifyServiceProviderTranslationRequest;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static uk.gov.ida.notification.shared.Urls.VerifyServiceProviderUrls;

public class VerifyServiceProviderProxy {
    private final JsonClient jsonClient;
    private final URI vspUri;

    public VerifyServiceProviderProxy(JsonClient jsonClient, URI vspUri) {
        this.jsonClient = jsonClient;
        this.vspUri = vspUri;
    }

    public TranslatedHubResponse getTranslatedHubResponse(VerifyServiceProviderTranslationRequest request) {
        URI uri = UriBuilder
                .fromUri(vspUri)
                .path(VerifyServiceProviderUrls.TRANSLATE_HUB_RESPONSE_ENDPOINT)
                .build();

        return jsonClient.post(request, uri, TranslatedHubResponse.class);
    }
}
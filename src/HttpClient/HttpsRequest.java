package HttpClient;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.jetbrains.annotations.NotNull;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public interface HttpsRequest {
    static CloseableHttpResponse httpGetRequest(String uri) throws KeyStoreException, NoSuchAlgorithmException, IOException, KeyManagementException {
        return httpGetResponse(uri);
    }
    static String httpGetRequestWithResponse(String uri) throws KeyStoreException, NoSuchAlgorithmException, IOException, KeyManagementException {
        return getString(httpGetResponse(uri));
    }

    static CloseableHttpResponse httpGetResponse(String uri) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, IOException {
        final SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(null, (x509CertChain, authType) -> true)
                .build();

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setSSLContext(sslContext)
                .setConnectionManager(
                        new PoolingHttpClientConnectionManager(
                                RegistryBuilder.<ConnectionSocketFactory>create()
                                        .register("http", PlainConnectionSocketFactory.INSTANCE)
                                        .register("https", new SSLConnectionSocketFactory(sslContext,
                                                NoopHostnameVerifier.INSTANCE))
                                        .build()
                        ))
                .build();

        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        return response;
    }

    @NotNull
    static String getString(CloseableHttpResponse response) throws IOException {
        HttpEntity entity1 = response.getEntity();
        InputStream inputStream = entity1.getContent();
        String content = new String();
        int tmp;
        while ((tmp = inputStream.read()) != -1) {
            content += (char) tmp;
        }
        return content.toString();
    }

}

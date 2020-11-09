package com.family.demo;

import io.netty.channel.ChannelOption;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.tcp.TcpClient;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * @author YongKyu.Han
 * @since 2020-11-02
 */

public class KakaoApiTest {

    @Test
    void test() {
        URI uri = URI.create("https://dapi.kakao.com/v3/search/book?sort=accuracy&page=1&size=10&query=test");
        Consumer<HttpHeaders> consumer = (HttpHeaders header) -> header.add("Authorization", "KakaoAK f109c17917b30395ffa5bbc04fd9c951");
        String apiCall = getApiCall(MediaType.APPLICATION_JSON, uri, consumer, String.class);

        System.out.println(apiCall);
    }

    private static WebClient webClient = WebClient.create();




    public static <T> T getApiCall(MediaType mediaType, URI uri, Consumer<HttpHeaders> httpHeadersConsumer, Class<T> tClass) {
        TcpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10_000);

        return webClient.get().uri(uri)
                .headers(httpHeadersConsumer)
                .accept(mediaType)
                .acceptCharset(StandardCharsets.UTF_8)
                .exchange()
                .flatMap(response -> response.bodyToMono(tClass))
                .block();
    }

    public static <T> T postApiCall(MediaType mediaType, URI uri, Consumer<HttpHeaders> httpHeadersConsumer, Class<T> tClass, MultiValueMap<String, String> multiValueMap) {
        return webClient.post().uri(uri)
                .headers(httpHeadersConsumer)
                .accept(mediaType)
                .body(BodyInserters.fromFormData(multiValueMap))
                .exchange()
                .flatMap(response -> response.bodyToMono(tClass))
                .block();
    }

   /* public static <T> T getDefaultJsonMediaTypeCall(URI uri, Consumer<HttpHeaders> httpHeadersConsumer, Class<T> tClass) {
        return SimsWebClient.getApiCall(MediaType.APPLICATION_JSON, uri, httpHeadersConsumer, tClass);
    }*/

}

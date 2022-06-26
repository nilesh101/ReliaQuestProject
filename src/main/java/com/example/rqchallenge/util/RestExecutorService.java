package com.example.rqchallenge.util;

import com.example.rqchallenge.aspects.Timed;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * REST call to outside world
 */
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class RestExecutorService {

    private final RestTemplate restTemplate;

    /**
     * @param url
     * @param httpEntity
     * @param httpMethod
     * @param responseClass
     * @param <T>
     * @return ResponseEntity of REST API call
     */
    @Retryable(
            value = {HttpClientErrorException.TooManyRequests.class},
            maxAttempts = 10,
            backoff = @Backoff(delay = 1000L)) //Retry 10 times with delay of 1000 ms in case of http client exception
    @Timed
    public <T> ResponseEntity<T> execute(String url, HttpEntity httpEntity, HttpMethod httpMethod, Class<T> responseClass) {
        try {
            log.debug("Calling {} with HTTP {} method.", url, httpMethod.name());
            return restTemplate.exchange(url, httpMethod, httpEntity, responseClass);
        } catch (HttpClientErrorException.TooManyRequests e) {
            throw e;
        } catch (HttpClientErrorException e) {
            log.error("Error occurred while calling url {} with HTTP {} method.", url, httpMethod.name(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred while calling url {} with HTTP {} method.", url, httpMethod.name(), e);
            throw e;
        }
    }

}

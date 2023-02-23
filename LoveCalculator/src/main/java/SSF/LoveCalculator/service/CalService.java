package SSF.LoveCalculator.service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import SSF.LoveCalculator.model.Compatibility;

import org.springframework.data.redis.core.RedisTemplate;


@Service
public class CalService {
    private static final String LOVE_CALC_API_URL = "https://love-calculator.p.rapidapi.com/getPercentage";

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public Optional<Compatibility> calCompatibility(String person1Name, String person2Name) throws IOException {
        
        String loveCalculatorUrl = UriComponentsBuilder
                .fromUriString(LOVE_CALC_API_URL)
                .queryParam("fname", person1Name)
                .queryParam("sname", person2Name)
                .toUriString();
        System.out.println(loveCalculatorUrl);

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = null;
        
        HttpHeaders headers = new HttpHeaders();
        String loverApiKey = System.getenv("LOVER_API_KEY");
        String loverApiHost = System.getenv("LOVER_API_HOST");
        headers.set("X-RapidAPI-Key", loverApiKey);
        headers.set("X-RapidAPI-Host", loverApiHost);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        
        resp = template.exchange(loveCalculatorUrl, HttpMethod.GET, requestEntity, String.class);
        System.out.println(resp);
        Compatibility c = Compatibility.create(resp.getBody());
        System.out.println(c);
        if (c != null) {
            redisTemplate.opsForValue().set(c.getId(), resp.getBody().toString());
            return Optional.of(c);
        }

        return Optional.empty();
    }

    public Compatibility[] getAllMatches() throws IOException {
        
        Set<String> allMatchKeys = redisTemplate.keys("*");
        
        List<Compatibility> matchArr = new LinkedList<Compatibility>();
        
        for (String oneMatchKey : allMatchKeys) {
            String result = (String) redisTemplate.opsForValue().get(oneMatchKey);
            matchArr.add((Compatibility) Compatibility.create(result));
        }

        return matchArr.toArray(new Compatibility[matchArr.size()]);
    }

}

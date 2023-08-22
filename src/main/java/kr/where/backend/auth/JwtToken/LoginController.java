//package kr.where.backend.JwtToken;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
////@Controller
//@Controller
//public class LoginController {
//    @GetMapping("login")
//    public String aaa() {
//        return "redirect:https://api.intra.42.fr/oauth/authorize?client_id=u-s4t2ud-9fd299381a400d4aa6302fc712acd85ee94320a640bb9cce3c68b0e8d6cf5d55&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Ftest%2Fcallback&response_type=code";
//    }
//
//    @GetMapping("/test/callback")
//    public ResponseEntity ttttt(@RequestParam(name = "code") String code) {
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-type", "Application/x-www-form-urlencoded;charset=utf-8");
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
//        body.add("grant_type", "authorization_code");
//        body.add("client_id", "u-s4t2ud-9fd299381a400d4aa6302fc712acd85ee94320a640bb9cce3c68b0e8d6cf5d55");
//        body.add("client_secret", "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");
//        body.add("code", code);
//        body.add("redirect_uri", "http://localhost:8080/test/callback");
//
//        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
//
//        ResponseEntity<String> response = restTemplate.exchange("https://api.intra.42.fr/oauth/token", HttpMethod.POST, entity, String.class);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        OauthToken oauthToken = null;
//
//        try {
//            oauthToken = objectMapper.readValue(response.getBody(), OauthToken.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        System.out.println("\n\n ======= test1 =======");
//        System.out.println("status : " + response.getStatusCode());
//        System.out.println("body : " + response.getBody());
//        return ResponseEntity.ok("hi hi");
//    }
//}

package com.app.site.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.app.site.domain.User;
import com.app.site.dto.UserDto;
import com.app.site.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;
	
	@Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    @Value("${kakao.redirect.uri}")
    private String redirectUri;

	public UserController(@Qualifier("UserService") UserService userService) {
		this.userService = userService;
	}
	
//	@GetMapping("/selectList")
//	public ResponseEntity<List<UserDto>> main() throws Exception {
//		List<UserDto> users = userService.getAllUser();
//		return ResponseEntity.ok(users); 
//	}

	@PostMapping("/saveUser")
	public ResponseEntity<User> saveUser(@RequestBody User user) throws Exception {
		User saveUser = userService.saveUser(user);
		return new ResponseEntity<>(saveUser, HttpStatus.CREATED); 
	}
	
	@PostMapping("/kakao-login")
	public ResponseEntity<Map<String, Object>> kakaoLogin(@RequestBody Map<String, String> requestBody) {
	    String code = requestBody.get("code");

	    // 카카오로 인증 코드를 통해 액세스 토큰 요청
	    String tokenUrl = "https://kauth.kakao.com/oauth/token";
	    

	    // 카카오 API로 액세스 토큰을 요청할 때 보내는 파라미터
	    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	    params.add("grant_type", "authorization_code");
	    params.add("client_id", clientId);
	    params.add("redirect_uri", redirectUri);
	    params.add("code", code);
	    
	    System.out.println("Request Parameters: " + params);

	 // request header 설정
	    HttpHeaders headers = new HttpHeaders();
	    // Content-type을 application/x-www-form-urlencoded 로 설정
	    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

	    // header 와 body로 Request 생성
	    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

	    try {
	        RestTemplate restTemplate = new RestTemplate();
	        // 응답 데이터(json)를 Map 으로 받을 수 있도록 메시지 컨버터 추가
	        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

	        // Post 방식으로 Http 요청
	        // 응답 데이터 형식은 Hashmap 으로 지정
	        ResponseEntity<HashMap> result = restTemplate.postForEntity(tokenUrl, entity, HashMap.class);
	        Map<String, Object> resMap = result.getBody();

			// 응답 데이터 확인
	        System.out.println(resMap);
	        return ResponseEntity.ok(resMap);
	    } catch (Exception e) {
	        e.printStackTrace();
	        Map<String, Object> errorResponse = new HashMap<>();
	        errorResponse.put("error", "Failed to retrieve access token from Kakao.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	    }
//	    if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
//	        Map<String, String> responseBody = response.getBody();
//	        String accessToken = responseBody != null ? responseBody.get("access_token") : null;
//
//	        // 액세스 토큰을 응답으로 반환
//	        Map<String, String> responseMap = new HashMap<>();
//	        responseMap.put("access_token", accessToken);
//
//	        System.out.println("222222222222222222222");
//	    } else {
//	        // 실패 시, 오류 메시지 반환
//	    	System.out.println("333333333333333333");
//	        Map<String, String> errorResponse = new HashMap<>();
//	        errorResponse.put("error", "Failed to retrieve access token from Kakao.");
//	    }
	}
}

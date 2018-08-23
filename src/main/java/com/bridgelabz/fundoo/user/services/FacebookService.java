package com.bridgelabz.fundoo.user.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoo.user.models.FacebookUser;
import com.bridgelabz.fundoo.user.models.User;
import com.bridgelabz.fundoo.user.repositories.UserRepository;
import com.bridgelabz.fundoo.user.utility.TokenProvider;

@Service
public class FacebookService {

	 	String accessToken;
	    @Value("${spring.social.facebook.appId}")
	    String facebookAppId;
	    @Value("${spring.social.facebook.appSecret}")
	    String facebookSecret;
	    
	    @Autowired
	    UserRepository userRepository;
	    
	    @Autowired
	    TokenProvider tokenProvider;

	    public String getName() {
	        Facebook facebook = new FacebookTemplate(accessToken);
	        String[] fields = { "name", "email" };
	        FacebookUser facebookUser=facebook.fetchObject("me", FacebookUser.class, fields);
	        Optional<User> optionalUser=userRepository.findByEmail(facebookUser.getEmail());
	        
	        
	        if(!optionalUser.isPresent()) {
	        	User user=new User();
	        	user.setEmail(facebookUser.getEmail());
	        	user.setName(facebookUser.getName());
	        	userRepository.save(user);
	        	return tokenProvider.generateToken(user.getId());
	        }        
	        return tokenProvider.generateToken(optionalUser.get().getId());
	    }

	    public String createFacebookAuthorizationURL() {
	        FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(facebookAppId, facebookSecret);
	        OAuth2Operations oauthOperations = connectionFactory.getOAuthOperations();
	        OAuth2Parameters params = new OAuth2Parameters();
	        params.setRedirectUri("http://localhost:8091/swagger-ui.html#!/facebook45controller/createFacebookAccessTokenUsingGET");
	        params.setScope("public_profile,email");
	        return oauthOperations.buildAuthorizeUrl(params);
	    }

	    public void createFacebookAccessToken(String code) {
	        FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(facebookAppId, facebookSecret);
	        AccessGrant accessGrant = connectionFactory.getOAuthOperations().exchangeForAccess(code,
	                "http://localhost:8091/swagger-ui.html#!/facebook45controller/createFacebookAccessTokenUsingGET", null);
	        accessToken = accessGrant.getAccessToken();
	    	
	    }


}
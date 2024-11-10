package com.ucsc.mob_backend.service;


import com.ucsc.mob_backend.dto.*;
import com.ucsc.mob_backend.entity.UserData;
import com.ucsc.mob_backend.repository.UserRepository;
import com.ucsc.mob_backend.util.JwtHelper;
import com.ucsc.mob_backend.util.KeyGenarator;
import com.ucsc.mob_backend.util.Role;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final KeyGenarator keyGenarator;



    public ResponseEntity<AuthResponceDTO> login(loginDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password");
        }

        UserData user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtHelper.genarateToken(user);


        HttpCookie cookie = ResponseCookie.from("Authorization", token)
                .path("/")
                .sameSite("None")
                .secure(false)
                .httpOnly(true)
                .maxAge(3600*24*2)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new AuthResponceDTO(token,keyGenarator.generateKey(user.getUsername(),user.getSalt()), user.getRole().toString()));



    }


    public ResponseEntity<AuthResponceDTO> signup(signupDTO request) {
        String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(10);

        UserData user =new UserData();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUsername(request.getEmail());
        user.setSalt(randomAlphanumeric);
        user.setRole(Role.USER);
        try {
            userRepository.save(user);

        }catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Username or email already exists");
        }

        loginDTO loginDTO = new loginDTO(request.getPassword(), user.getUsername());

        CompletableFuture.runAsync(() -> {
            try {
                emailService.sendEmail(
                        user.getUsername(),
                        "Welcome to the Biomark Platform!",
                        getWelcomeEmailBody()
                );
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
        return login(loginDTO);




    }

    private String getWelcomeEmailBody() {
        return """
                            <!DOCTYPE html>
                                        <html lang="en">
                                        <head>
                                        <meta charset="UTF-8">
                                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                        <title>Welcome to Biomark</title>
                                        <style>
                                            body {
                                                margin: 0;
                                                padding: 0;
                                                background-color: #f3f4f6;
                                                font-family: Arial, sans-serif;
                                                color: #333333;
                                            }
                                            .container {
                                                max-width: 600px;
                                                margin: auto;
                                                background-color: #ffffff;
                                                border-radius: 8px;
                                                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
                                                overflow: hidden;
                                                position: relative;
                                            }
                                            .header {
                                                background-color: #020015;
                                                text-align: center;
                                            
                                            }
                                            .header img {
                                                width: 100%;
                                                height: auto;
                                                border-top-left-radius: 8px;
                                                border-top-right-radius: 8px;
                                            }
                                            .header h1 {
                                                margin: 0;
                                                color: #ffffff;
                                                font-size: 26px;
                                                font-weight: 700;
                                            }
                                            .header p {
                                                color: #ffffff;
                                                font-size: 18px;
                                                margin: 10px 0 0;
                                            }
                                            .content {
                                                padding: 5px 30px;
                                                text-align: left;
                                            }
                                            .content p {
                                                font-size: 16px;
                                                line-height: 1.6;
                                                margin: 20px 0 0;
                                            }
                                            .cta {
                                                text-align: center;
                                                padding: 30px;
                                            }
                                            .cta a {
                                                display: inline-block;
                                                background-color: #020015;
                                                color: #ffffff;
                                                padding: 12px 28px;
                                                font-size: 16px;
                                                font-weight: bold;
                                                text-decoration: none;
                                                border-radius: 4px;
                                                transition: background-color 0.3s;
                                            }
                                            .cta a:hover {
                                                background-color: #353535;
                                            }
                                            .divider {
                                                padding: 0 30px;
                                            }
                                            .divider hr {
                                                border: none;
                                                height: 1px;
                                                background-color: #e0e0e0;
                                            }
                                            .features, .tips {
                                                padding: 20px 30px;
                                            }
                                            .features h2, .tips h2 {
                                                font-size: 20px;
                                                color: #1a73e8;
                                                margin: 0;
                                            }
                                            .footer {
                                                background-color: #f3f4f6;
                                                text-align: center;
                                                padding: 30px;
                                                font-size: 14px;
                                                color: #888888;
                                            }
                                            .footer a {
                                                color: #1a73e8;
                                                text-decoration: none;
                                            }
                                            .h-text{
                                                background: linear-gradient(  #020015 30%, #020015 65%, rgb(255, 255, 255) 100%); /* Gradient with fade */
                                                text-align: center;
                                                padding-top: 50px;
                                                padding-bottom: 70px;
                                                margin-bottom: -95px;
                                            }
                                            .h-text  h1 {
                                                margin: 0;
                                                color: #ffffff;
                                                font-size: 26px;
                                                font-weight: 700;
                                            }
                                            .h-text p {
                                                color: #ffffff;
                                                font-size: 18px;
                                                margin: 10px 0 0;
                                            }
                                        </style>
                                        </head>
                                        <body>
                                            <div class="container">
                                               
                                                <!-- Header with Image -->
                                                <div class="header">
                                                    <img src="https://i.ibb.co/VBT0fn9/92d3db7a-f918-4e63-b4ec-03d47e6a5d3b-Artificial-intelligence-in-palm-of-mans-hand.jpg" alt="[Platform Name] Banner">
                                                </div>
                                                <div class="h-text">
                                                    <h1>Welcome to Biomark</h1>
                                                    <p>Empowering you to achieve more</p>
                                                </div>
                                               
                                                <!-- Welcome Message -->
                                                <div class="content">
                                                    <p>Dear User,</p>
                                                     <p>We’re thrilled to welcome you to <strong>Biomark</strong>! Our platform is designed to advance personalized digital services through secure, privacy-focused data collection, and we can’t wait to support you on this journey.</p>
                                                    <p>Here’s everything you need to get started and make the most of <strong>Biomark</strong>.</p>
                                                </div>
                                        
                                                <!-- CTA Button -->
                                                <div class="cta">
                                                    <a href="[Platform URL]">Explore Biomark</a>
                                                </div>
                                        
                                                <!-- Divider -->
                                                <div class="divider">
                                                    <hr>
                                                </div>
                                        
                                               
                                        
                                                <!-- Tips for Getting Started -->
                                                <div class="tips">
                                                    <h2>Getting Started Tips</h2>
                                                    <p>To help you make the most of your new account, here are some tips to get started:</p>
                                                    <ul style="font-size: 16px; line-height: 1.6; padding: 10px 0 0 20px;">
                                                        <li>Complete your profile to enhance your experience and access personalized features.</li>
                                                       <li>Explore Biomark's Knowledge Center to understand how your contributions support meaningful advancements.</li>
                                                       <li>Engage with our community to stay informed on new developments and share your journey.</li>
                                                    </ul>
                                                </div>
                                        
                                                <!-- Footer -->
                                                <div class="footer">
                                                   <p>If you have any questions, feel free to <a href="mailto:support@biomark.com">contact us</a> or visit our <a href="[Help Center URL]">Help Center</a>.</p>
                                                   <p>&copy; 2024 Biomark, Inc. All rights reserved.</p>
                                                </div>
                                            </div>
                                        </body>
                                        </html>
                                        
                """;
    }

    public ResponseEntity<SingleLineResponceDTO> changepassword(UserData userData, ChangePwDTO request) {
        if(passwordEncoder.matches(request.getOldPassword(),userData.getPassword())){
            userData.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(userData);
            return ResponseEntity.ok(new SingleLineResponceDTO("Password changed successfully"));
        }else {
            throw new BadCredentialsException("Incorrect password");
        }

    }

}

package ma.ismail.spring_sec_jwt;

import jakarta.security.auth.message.config.AuthConfig;
import ma.ismail.spring_sec_jwt.entites.AppUser;
import ma.ismail.spring_sec_jwt.filtres.JwtAuthenticationFilter;
import ma.ismail.spring_sec_jwt.filtres.JwtAuthorisationFilter;
import ma.ismail.spring_sec_jwt.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Configuring HttpSecurity
    private PasswordEncoder BCryptPasswordEncoder;

    public SecurityConfig(PasswordEncoder bCryptPasswordEncoder) {
        BCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           final AuthenticationConfiguration authenticationConfiguration
                                           ) throws Exception {
        http.
                csrf((csrf) -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .formLogin(withDefaults())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests((authz) ->

                        authz
//                                .requestMatchers("/refreshtoken").permitAll()
//                                .requestMatchers(HttpMethod.POST,"/role/**").hasAnyAuthority("ADMIN")
//                                .requestMatchers(HttpMethod.GET,"/role/**").hasAnyAuthority("USER","ADMIN")
                                .anyRequest().permitAll()

                )

                .addFilter(new JwtAuthenticationFilter(authenticationConfiguration.getAuthenticationManager()))
                .addFilterBefore(new JwtAuthorisationFilter(), UsernamePasswordAuthenticationFilter.class);
        ;

        return http.build();
    }
    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Autowired
            private AccountService accountService;
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                AppUser appUser=accountService.loadUserByUsername(username);
                if (appUser==null) throw new UsernameNotFoundException("User not found");
                //Collection<GrantedAuthority> authorities= List.of(new SimpleGrantedAuthority("USER"));
                Collection<GrantedAuthority> authorities=appUser.
                        getRoles().stream().
                        map(r->new SimpleGrantedAuthority(r.getRoleName()))
                        .collect(Collectors.toList());
                return new User(username,appUser.getPassword(),authorities);
            }
        };

    }
    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration=new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setExposedHeaders(List.of("x-auth-token"));
        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfiguration);
        return source;
    }
    /*@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }*/
}


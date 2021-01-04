package com.rss.config

import com.rss.security.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.http.HttpMethod
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
open class SecurityConfig(
    private val jwtBuilder: JwtBuilder,
    private val rssUserDetailsService: RssUserDetailsService,
) : WebSecurityConfigurerAdapter() {
    @Bean
    open fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    open fun corsConfigurationSource(): CorsConfigurationSource {
        val source: UrlBasedCorsConfigurationSource = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", CorsConfiguration().applyPermitDefaultValues())
        return source
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth
            .userDetailsService(rssUserDetailsService)
            .passwordEncoder(passwordEncoder())

        super.configure(auth)
    }

    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().disable().authorizeRequests()
            .antMatchers(HttpMethod.POST, "/api/v1/auth/register").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilter(JwtAuthenticationFilter(jwtBuilder, authenticationManager()))
            .addFilter(JwtAuthorizationFilter(jwtBuilder, authenticationManager()))
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }
}

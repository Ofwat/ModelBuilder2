package uk.gov.ofwat.fountain.modelbuilder.config;

import org.springframework.core.io.FileSystemResource;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.kerberos.authentication.KerberosServiceAuthenticationProvider;
import org.springframework.security.kerberos.authentication.sun.SunJaasKerberosTicketValidator;
import org.springframework.security.kerberos.client.config.SunJaasKrb5LoginConfig;
import org.springframework.security.kerberos.client.ldap.KerberosLdapContextSource;
import org.springframework.security.kerberos.web.authentication.SpnegoAuthenticationProcessingFilter;
import org.springframework.security.kerberos.web.authentication.SpnegoEntryPoint;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import uk.gov.ofwat.fountain.modelbuilder.security.*;
import uk.gov.ofwat.fountain.modelbuilder.config.JHipsterProperties;

import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.csrf.CsrfFilter;
import javax.inject.Inject;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import uk.gov.ofwat.fountain.modelbuilder.web.filter.CsrfCookieGeneratorFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final Log logger = LogFactory.getLog(SecurityConfiguration.class);

    @Inject
    private JHipsterProperties jHipsterProperties;

    @Inject
    private AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler;

    @Inject
    private AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler;

    @Inject
    private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

    @Inject
    private Http401UnauthorizedEntryPoint authenticationEntryPoint;

    @Inject
    private UserDetailsService userDetailsService;

    @Inject
    private RememberMeServices rememberMeServices;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        try {
            auth
                .authenticationProvider(activeDirectoryLdapAuthenticationProvider())
                .authenticationProvider(kerberosServiceAuthenticationProvider())
                .userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder());
        } catch (Exception e) {
            throw new BeanInitializationException("Security configuration failed", e);
        }
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .antMatchers("/app/**/*.{js,html}")
            .antMatchers("/bower_components/**")
            .antMatchers("/i18n/**")
            .antMatchers("/content/**")
            .antMatchers("/swagger-ui/index.html")
            .antMatchers("/test/**")
            .antMatchers("/h2-console/**");
    }

    @Bean
    public ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
        return new ActiveDirectoryLdapAuthenticationProvider(jHipsterProperties.getKerberosConfig().getAdDomain(), jHipsterProperties.getKerberosConfig().getAdServer());
    }

    @Bean
    public SpnegoEntryPoint spnegoEntryPoint() {
        return new SpnegoEntryPoint("/app/components/login/login.html");
    }

    @Bean
    public SpnegoAuthenticationProcessingFilter spnegoAuthenticationProcessingFilter(
        AuthenticationManager authenticationManager) {
        SpnegoAuthenticationProcessingFilter filter = new SpnegoAuthenticationProcessingFilter();
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Bean
    public KerberosServiceAuthenticationProvider kerberosServiceAuthenticationProvider() {
        KerberosServiceAuthenticationProvider provider = new KerberosServiceAuthenticationProvider();
        provider.setTicketValidator(sunJaasKerberosTicketValidator());
        provider.setUserDetailsService(ldapUserDetailsService());
        return provider;
    }

    @Bean
    public SunJaasKerberosTicketValidator sunJaasKerberosTicketValidator() {
        SunJaasKerberosTicketValidator ticketValidator = new SunJaasKerberosTicketValidator();
        ticketValidator.setServicePrincipal(jHipsterProperties.getKerberosConfig().getServicePrincipal());
        ticketValidator.setKeyTabLocation(new FileSystemResource(jHipsterProperties.getKerberosConfig().getKeytabLocation()));
        ticketValidator.setDebug(true);
        return ticketValidator;
    }

    @Bean
    public KerberosLdapContextSource kerberosLdapContextSource() {
        KerberosLdapContextSource contextSource = new KerberosLdapContextSource(jHipsterProperties.getKerberosConfig().getAdServer());
        contextSource.setLoginConfig(loginConfig());
        return contextSource;
    }

    @Bean
    public SunJaasKrb5LoginConfig loginConfig() {
        SunJaasKrb5LoginConfig loginConfig = new SunJaasKrb5LoginConfig();
        loginConfig.setKeyTabLocation(new FileSystemResource(jHipsterProperties.getKerberosConfig().getKeytabLocation()));
        loginConfig.setServicePrincipal(jHipsterProperties.getKerberosConfig().getServicePrincipal());
        loginConfig.setDebug(true);
        loginConfig.setIsInitiator(true);
        return loginConfig;
    }

    @Bean
    public LdapAuthoritiesPopulator ldapAuthoritiesPopulator() {
        LdapAuthoritiesPopulator ldapAuthoritiesPopulator = new LdapAuthoritiesPopulator() {
            Collection<SimpleGrantedAuthority> authorities;
            @Override
            public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData, String username) {
                authorities = new ArrayList<SimpleGrantedAuthority>();
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                authorities.add(new SimpleGrantedAuthority("ROLE_FOUNTAINEER"));
                Attributes attributes =  userData.getAttributes();
                NamingEnumeration<?> names = attributes.getAll();
                logger.debug("Iterating Names...");
                //while(names.hasMoreElements()){
                //try {
                //org.springframework.ldap.core.LdapAttribute name = (LdapAttribute)names.next();
                //logger.debug(name.toString());
                //} catch (NamingException e) {
                //	e.printStackTrace();
                //}
                //}
                return authorities;
            }
        };
        return ldapAuthoritiesPopulator;
    }

    @Bean
    public LdapUserDetailsService ldapUserDetailsService() {
        OfwatFilterBasedLdapUserSearch userSearch =
            new OfwatFilterBasedLdapUserSearch(jHipsterProperties.getKerberosConfig().getLdapSearchBase(), jHipsterProperties.getKerberosConfig().getLdapSearchFilter(), kerberosLdapContextSource());
        LdapUserDetailsService service = new LdapUserDetailsService(userSearch, ldapAuthoritiesPopulator());
        service.setUserDetailsMapper(new OfwatLdapUserDetailsMapper());
        return service;
    }

    /*
    protected void configure(HttpSecurity http) throws Exception {
        http
            .exceptionHandling()
            .authenticationEntryPoint(spnegoEntryPoint())
            .and()
            .authorizeRequests()
            .antMatchers("/", "/home").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login").permitAll()
            .and()
            .logout()
            .permitAll()
            .and()
            .addFilterBefore(
                spnegoAuthenticationProcessingFilter(authenticationManagerBean()),
                BasicAuthenticationFilter.class);
    }
    */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        .and()
            //.addFilterAfter(new CsrfCookieGeneratorFilter(), CsrfFilter.class)
            //.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            //.and()
            .exceptionHandling()
            .authenticationEntryPoint(spnegoEntryPoint())
        .and()
            .addFilterBefore(
                spnegoAuthenticationProcessingFilter(authenticationManagerBean()),
                BasicAuthenticationFilter.class)
            .rememberMe()
            .rememberMeServices(rememberMeServices)
            .rememberMeParameter("remember-me")
            .key(jHipsterProperties.getSecurity().getRememberMe().getKey())
        .and()
            .formLogin()
            .loginProcessingUrl("/api/authentication")
            .successHandler(ajaxAuthenticationSuccessHandler)
            .failureHandler(ajaxAuthenticationFailureHandler)
            .usernameParameter("j_username")
            .passwordParameter("j_password")
            .permitAll()
        .and()
            .logout()
            .logoutUrl("/api/logout")
            .logoutSuccessHandler(ajaxLogoutSuccessHandler)
            .permitAll()
        .and()
            .headers()
            .frameOptions()
            .disable()
        .and()
            .authorizeRequests()
            .antMatchers("/api/register").permitAll()
            .antMatchers("/api/activate").permitAll()
            .antMatchers("/api/authenticate").permitAll()
            .antMatchers("/api/account/reset_password/init").permitAll()
            .antMatchers("/api/account/reset_password/finish").permitAll()
            .antMatchers("/api/profile-info").permitAll()
            .antMatchers("/api/**").authenticated()
            .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .antMatchers("/v2/api-docs/**").permitAll()
            .antMatchers("/swagger-resources/configuration/ui").permitAll()
            .antMatchers("/swagger-ui/index.html").hasAuthority(AuthoritiesConstants.ADMIN);

    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
}

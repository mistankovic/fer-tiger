package hr.fer.zari.rasip.tiger;

import hr.fer.zari.rasip.tiger.domain.AppUser;
import hr.fer.zari.rasip.tiger.domain.CollectingUnitType;
import hr.fer.zari.rasip.tiger.domain.UserRole;
import hr.fer.zari.rasip.tiger.service.AppUserService;
import hr.fer.zari.rasip.tiger.service.CollectingUnitTypeService;
import hr.fer.zari.rasip.tiger.service.UserRoleService;

import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		
		acceptAllCertificates();
	}

	/**
	 * Method for setting up socket factory to accept all certificates used by collecting units.
	 * Comment out if valid certificates are used.
	 */
	private static void acceptAllCertificates() {
		TrustManager[] trustAllCerts = new TrustManager[] { 
			    new X509TrustManager() {     
			        public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
			            return new X509Certificate[0];
			        } 
			        public void checkClientTrusted( 
			            java.security.cert.X509Certificate[] certs, String authType) {
			            } 
			        public void checkServerTrusted( 
			            java.security.cert.X509Certificate[] certs, String authType) {
			        }
			    } 
			}; 

			// Install the all-trusting trust manager
			try {
			    SSLContext sc = SSLContext.getInstance("SSL"); 
			    sc.init(null, trustAllCerts, new java.security.SecureRandom()); 
			    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			} catch (GeneralSecurityException e) {
			} 
			// Now an https URL can be accessed without having the certificate in the truststore
	}

	@Component
	public static class Bootstrap implements CommandLineRunner {

		private CollectingUnitTypeService collectingUnitTypeService;
		private UserRoleService userRoleService;
		private AppUserService appUserService;
		private PasswordEncoder passwordEncoder;

		@Autowired
		public Bootstrap(CollectingUnitTypeService collectingUnitTypeService,
				UserRoleService userRoleService, AppUserService appUserService,
				PasswordEncoder passwordEncoder) {
			
			this.collectingUnitTypeService = collectingUnitTypeService;
			this.userRoleService = userRoleService;
			this.appUserService = appUserService;
			this.passwordEncoder = passwordEncoder;
		}

		@Override
		@Transactional
		public void run(String... args) throws Exception {
			long numberOfCollectingUnitTypes = collectingUnitTypeService.count();
			if (numberOfCollectingUnitTypes == 0) {

				CollectingUnitType gsn = new CollectingUnitType();
				gsn.setName("GSN");
				gsn.addSupportedMeasurementUnit("int");
				gsn.addSupportedMeasurementUnit("double");

				collectingUnitTypeService.save(gsn);

				UserRole admin = new UserRole(
						"ADMIN",
						"Can add, edit, delete and archive collecting units and users");
				UserRole basicUser = new UserRole("USER",
						"Standard user. Can view all data but can't change anything");

				userRoleService.save(admin);
				userRoleService.save(basicUser);

				String password = "admin";
				AppUser user = new AppUser("admin@rasip.fer",
						passwordEncoder.encode(password), "rasip", "admin");

				user.addRole(basicUser);
				user.addRole(admin);

				appUserService.save(user);
			}
		}
	}
}
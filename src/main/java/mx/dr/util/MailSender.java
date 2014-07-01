package mx.dr.util;

import java.io.Serializable;
import java.util.ResourceBundle;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Class that allows the sending of emails.
 * Clase que permite el envio de correos electronicos.
 * @version 1.0 
 * @author Jorge Luis Martinez Ramirez
 */
public class MailSender implements Serializable{
	/**
	 * serie
	 */
	private static final long serialVersionUID = -5306564244614702168L;
        /**
         * Instancia unica de envio de correo.
         */
	private static MailSender instance;
        /**
         * Paquete de recursos.
         */
	private ResourceBundle resourceBundle;
        /**
         * Autenticador.
         */
	private InnerAuthenticator authenticator;
	
        /**
         * Constructor hidden by default.
         * Constructor oculto por defecto.
         * @param properties the properties file location of connection / localidad del archivo de propiedades de conexion.
         */
	private MailSender(String properties){
		resourceBundle=ResourceBundle.getBundle(properties);
		String user=resourceBundle.getString("mail.smtp.user");
		String pass=resourceBundle.getString("mail.smtp.password");
		authenticator= new InnerAuthenticator(user,pass);
	}
	/**
         * Gets the instance of the props for sending email.
         * <br/>
         * Obtiene la instancia de la utileria de envio de correo electronico.
         * @param properties file path email settings in the <code>classpath</code> / ruta del archivo de configuracion de correo electronico en el <code>classpath</code>.
         * @return Unique instance of the usefulness of sending mail / Instancia unica de la utilidad de envio de correo.
         */
	public synchronized static MailSender getInstance(String properties){
		if (instance==null){
			instance=new MailSender(properties);
		}
		return instance;
	}
        /**
         * Gets the instance of the props for sending email. Properties: <code>/mailConfig.properties</code>
         * <br/>
         * Obtiene la instancia de la utileria de envio de correo electronico. Properties: <code>/mailConfig.properties</code>
         * @return Unique instance of the usefulness of sending mail / Instancia unica de la utilidad de envio de correo.
         */
        public synchronized static MailSender getInstance(){
		if (instance==null){
			instance=new MailSender("/mailConfig.properties");
		}
		return instance;
	}
	/**
         * Send an simple email.
         * <br/>
         * Envia un correo electronico simple.
         * @param to Email Address / Email Address.
         * @param subject Subject of email / Tema del correo electronico.
         * @param body Message body of email / Cuerpo del mensaje del correo electronico.
         * @return True if the email was sent successfully, false otherwise / Verdadero si el correo electronico fue enviado exitosamente, falso de otra manera.
         */
	public synchronized boolean sendSimpleEmail(String to, String subject,String body){
		//String domain=resourceBundle.getString("mvs.mail.domain");
                boolean sended = false;
		String user=resourceBundle.getString("mail.smtp.user");
		String pass=resourceBundle.getString("mail.smtp.password");
		String from=resourceBundle.getString("mvs.mail.from");
		String pop3=resourceBundle.getString("mvs.mail.pop3");
		
		Session session=Session.getInstance(DRGeneralUtils.convertToProperties(resourceBundle), authenticator);
		//session.setPasswordAuthentication(new URLName(domain),
		//	new PasswordAuthentication(user,pass)); 
		Store sendBox;
		try {
			sendBox = session.getStore("pop3");
			try {
				sendBox.connect(pop3, user, pass);
				MimeMessage message = new MimeMessage(session); 
				try {
					message.setFrom(new InternetAddress(from));
					try {
						message.addRecipient(Message.RecipientType.TO, 
								new InternetAddress(to));
						try {
							message.setSubject(subject);
							try {
								message.setContent(body, "text/html");
								//message.setText(body);
								Transport mta;
								try {
									mta = session.getTransport("smtp");
									try {
										mta.connect();
										try {
											Transport.send(message);
											sended = true;
										} catch (MessagingException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} finally{
											try {
												mta.close();
											} catch (MessagingException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									} catch (MessagingException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} 
								} catch (NoSuchProviderException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}	
							} catch (MessagingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}	
						} catch (MessagingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} catch (AddressException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (AddressException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally{
					try {
						sendBox.close();
						} catch (MessagingException e) {
						e.printStackTrace();
						}
				}
			}catch (MessagingException e1) {
				e1.printStackTrace();
			} 
		} catch (NoSuchProviderException e2) {
			e2.printStackTrace();
		} 
		
		return sended;

	}
	
        /**
         * Inner class that implements a basic user-authentication.
         * <br/>
         * Clase interna que implementa una autenticacion basica por usuario.
         */
	public class InnerAuthenticator extends Authenticator {
		 
		  private String username, password;
		      
		  public InnerAuthenticator(String username, String password){  
		      this.username = username;
		      this.password = password;
		  }
		    
		  public PasswordAuthentication getPasswordAuthentication() {  
		    return new PasswordAuthentication(username, password);
		  }
		 
		}
}

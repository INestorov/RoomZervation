package application.controllers;

import application.entities.KeyEncodingGenerator;
import application.entities.ProductKeyEncoding;
import application.entities.User;
import application.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weilerhaus.productKeys.ProductKeyGenerator;
import com.weilerhaus.productKeys.enums.ProductKeyState;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/mail")
public class MailSenderController {

    //public static MailSenderController mailSenderController;

    @Qualifier(value = "getJavaMailSender")
    @Autowired
    public JavaMailSender helper;

    @Qualifier(value = "getJavaMailSender")
    @Autowired
    public static JavaMailSender excuse;

    @Autowired
    private UserRepository userRepository;

    //    @Autowired
    //    private ActivationKeysRepository activRepository;

    public MailSenderController() {
        //noinspection SpringConfigurationProxyMethods
        helper = getJavaMailSender();
    }

    private ObjectMapper objectMapper = new ObjectMapper();
    private Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder();

    String tmpKey;
    ProductKeyState tmpGeneratedKeyState;
    final List<String> generatedProductKeys = new ArrayList<String>();

    // @formatter:off
    ProductKeyGenerator<ProductKeyEncoding> productKeyGenerator = new KeyEncodingGenerator(
        new ProductKeyEncoding((byte) 24, (byte) 3, (byte) 101),
        new ProductKeyEncoding((byte) 10, (byte) 4, (byte) 56),
        new ProductKeyEncoding((byte) 1, (byte) 2, (byte) 91),
        new ProductKeyEncoding((byte) 7, (byte) 1, (byte) 100),
        new ProductKeyEncoding((byte) 2, (byte) 36, (byte) 45),
        new ProductKeyEncoding((byte) 13, (byte) 5, (byte) 54),
        new ProductKeyEncoding((byte) 21, (byte) 67, (byte) 25),
        new ProductKeyEncoding((byte) 3, (byte) 76, (byte) 12),
        new ProductKeyEncoding((byte) 31, (byte) 22, (byte) 34),
        new ProductKeyEncoding((byte) 15, (byte) 72, (byte) 65)
    );
    // @formatter:on

    /**
     * End point used to send a mail to a specific user.
     *
     * @param to mail address to send
     */
    @RequestMapping("/send/{to}")
    public void sendPassword(@PathVariable("to") String to) {
        String text = "Here is your activation key for resetting your password:\n ";
        String subject = "Password Recovery";
        String encoded = generateKey();
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(to));
        if (encoded == null) {
            sendSimpleMessage(user.get().getMail(), subject,
                "Something went wrong, try again later. \n Sorry for inconvenience.");
        }
        text = text + encoded + "\n \n Have a nice day. \n Admin team.";
        if (user.isPresent()) {
            sendSimpleMessage(user.get().getMail(), subject, text);
        }
        //byte[] digested = decode(encode);
    }

    /**     method for notifying a user if his reservation got overwritten.
     *
     * @param to mail address
     * @param start start time of cancelled reser
     * @param room room.
     */
    public void sendExcuse(String to, String start, String room) {
        String subject = "Reservation cancelled";
        String text = "Due to an uncertain event,"
            + " your reservation" + start + " in room " + room + " was cancelled.\n"
            + "We are sorry about this" + " inconvenience.";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        helper.send(message);
    }


    /** Endpoint for checking the activation key.
     *
     * @param key key sent by user
     * @return check result of type boolean
     */
    @RequestMapping("/check/{key}")
    public boolean verifyKey(@PathVariable String key) {
        System.out.println();
        System.out.println();
        System.out.println("**** VERIFYING KEY ****");
        String productKey = key;
        // @formatter:off
        KeyEncodingGenerator basicProductKeyGenerator = new KeyEncodingGenerator(
            new ProductKeyEncoding((byte) 24, (byte) 3, (byte) 101),
            null,
            new ProductKeyEncoding((byte) 1, (byte) 2, (byte) 91),
            new ProductKeyEncoding((byte) 7, (byte) 1, (byte) 100),
            null,
            null,
            new ProductKeyEncoding((byte) 21, (byte) 67, (byte) 25),
            null,
            new ProductKeyEncoding((byte) 31, (byte) 22, (byte) 34),
            null
        );
        // @formatter:on

        if ((productKey != null) && (productKey.trim().length() > 0)) {
            tmpGeneratedKeyState = basicProductKeyGenerator.verifyProductKey(productKey);

            if (ProductKeyState.KEY_GOOD == tmpGeneratedKeyState) {
                System.out.println("Product Key (GOOD): " + productKey);
                return true;
            } else {
                System.out.println("Product Key (BAD): " + productKey + ":  Key State: "
                    + tmpGeneratedKeyState.name());
                return false;
            }
        } else {
            System.out.println("Unable to validate empty product key.");
            return false;
        }
    }

    /**
     * Method for sending a simple mail.
     *
     * @param to      mail address to send
     * @param subject mail subject
     * @param text    mail text
     */
    public void sendSimpleMessage(
        String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        helper.send(message);
    }

    /**
     * Bean for configuring the JavaMailSender in order to use a Google account.
     *
     * @return mailSender configuration
     */
    @Bean
    public JavaMailSenderImpl getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("mydatabase1011@gmail.com");
        mailSender.setPassword("qvjkwaymxxshuvil");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        return mailSender;
    }

    /**
     * Another method for setting up a template for the message mail.
     *
     * @return mail message template
     */
    @Bean
    public SimpleMailMessage templateSimpleMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(
            "This is the test email template for your email:\n%s\n");
        return message;
    }

    int tmpTryCount;

    /** Method for generating a key.
     *
     * @return
     */
    public String generateKey() {
        System.out.println("**** BUILDING KEY ****");

        final Random randomGenerator = new Random(System.nanoTime());


        tmpKey = null;
        tmpTryCount = 0;

        while ((tmpKey == null) && (tmpTryCount < 10)) {
            try {
                tmpKey = productKeyGenerator.generateProductKey(randomGenerator.nextLong());
            } catch (Exception e) {
                e.printStackTrace();
            }

            tmpTryCount++;
        }
        System.out.println("Attempts " + tmpTryCount);
        if ((tmpKey != null) && (tmpKey.trim().length() > 0)) {
            tmpGeneratedKeyState = productKeyGenerator.verifyProductKey(tmpKey);

            if (ProductKeyState.KEY_GOOD == tmpGeneratedKeyState) {
                generatedProductKeys.add(tmpKey);
                //activRepository.save(new ActivationKeys(tmpKey, id));
                return tmpKey;
            }
        } else {
            System.out.println("Failed to generate product key.");
        }
        return null;
    }

}

package com.DreamFactory.DF.email;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailService emailService;

    private String testEmail;
    private String testSubject;
    private String testPlainText;
    private String testHtmlContent;

    @BeforeEach
    void setUp() {
        testEmail = "test@example.com";
        testSubject = "Test Subject";
        testPlainText = "Test plain text content";
        testHtmlContent = "<html><body><h1>Test HTML content</h1></body></html>";
    }

    @Nested
    class SendDestinationCreatedEmailTests {

        @Test
        void shouldSendDestinationCreatedEmailSuccessfully() {
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
            doNothing().when(mailSender).send(any(MimeMessage.class));

            assertDoesNotThrow(() -> emailService.sendDestinationCreatedEmail(testEmail, testSubject, testPlainText,
                    testHtmlContent));

            verify(mailSender).createMimeMessage();
            verify(mailSender).send(mimeMessage);
        }

        @Test
        void shouldThrowMessagingExceptionWhenMailSenderFails() {
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
            doThrow(new RuntimeException("Mail server error")).when(mailSender).send(any(MimeMessage.class));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> emailService
                    .sendDestinationCreatedEmail(testEmail, testSubject, testPlainText, testHtmlContent));

            assertEquals("Mail server error", exception.getMessage());
            verify(mailSender).createMimeMessage();
            verify(mailSender).send(mimeMessage);
        }

        @Test
        void shouldSendEmailWithCorrectParameters() {
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
            doNothing().when(mailSender).send(any(MimeMessage.class));

            assertDoesNotThrow(() -> emailService.sendDestinationCreatedEmail(testEmail, testSubject, testPlainText,
                    testHtmlContent));

            verify(mailSender).createMimeMessage();
            verify(mailSender).send(mimeMessage);
        }

        @Test
        void shouldHandleEmptyContent() {
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
            doNothing().when(mailSender).send(any(MimeMessage.class));

            assertDoesNotThrow(() -> emailService.sendDestinationCreatedEmail(testEmail, testSubject, "", ""));

            verify(mailSender).createMimeMessage();
            verify(mailSender).send(mimeMessage);
        }
    }

    @Nested
    class SendUserWelcomeEmailTests {

        @Test
        void shouldSendUserWelcomeEmailSuccessfully() {
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
            doNothing().when(mailSender).send(any(MimeMessage.class));

            assertDoesNotThrow(
                    () -> emailService.sendUserWelcomeEmail(testEmail, testSubject, testPlainText, testHtmlContent));

            verify(mailSender).createMimeMessage();
            verify(mailSender).send(mimeMessage);
        }

        @Test
        void shouldThrowMessagingExceptionWhenMailSenderFails() {
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
            doThrow(new RuntimeException("Mail server error")).when(mailSender).send(any(MimeMessage.class));

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> emailService.sendUserWelcomeEmail(testEmail, testSubject, testPlainText, testHtmlContent));

            assertEquals("Mail server error", exception.getMessage());
            verify(mailSender).createMimeMessage();
            verify(mailSender).send(mimeMessage);
        }

        @Test
        void shouldSendWelcomeEmailWithCorrectParameters() {
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
            doNothing().when(mailSender).send(any(MimeMessage.class));

            assertDoesNotThrow(
                    () -> emailService.sendUserWelcomeEmail(testEmail, testSubject, testPlainText, testHtmlContent));

            verify(mailSender).createMimeMessage();
            verify(mailSender).send(mimeMessage);
        }

        @Test
        void shouldHandleEmptyContent() {
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
            doNothing().when(mailSender).send(any(MimeMessage.class));

            assertDoesNotThrow(() -> emailService.sendUserWelcomeEmail(testEmail, testSubject, "", ""));

            verify(mailSender).createMimeMessage();
            verify(mailSender).send(mimeMessage);
        }

        @Test
        void shouldHandleNullContent() {
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> emailService.sendUserWelcomeEmail(testEmail, testSubject, null, null));

            assertEquals("Plain text must not be null", exception.getMessage());
            verify(mailSender).createMimeMessage();
            verify(mailSender, never()).send(any(MimeMessage.class));
        }
    }

}

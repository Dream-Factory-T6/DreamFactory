package com.DreamFactory.DF.email;

import com.DreamFactory.DF.user.model.User;

public class UserEmailTemplates {
    public static String getUserCreatedSubject(){
        return "🖐🏼 Welcome to Dream Factory! Your Account is Ready";
    }

    public static String getUserWelcomeEmailPlainText(User user){
        return String.format("Hello %s! 👋\n\n" +
                        "Welcome to DreamFactory! We're thrilled to have you join our community. 🎉\n\n" +
                        "At DreamFactory, you can explore amazing destinations and share your own travel experiences.\n\n" +
                        "Ready to start your journey? Log in now and let your dreams take flight!\n\n" +
                        "Best regards,\n" +
                        "The DreamFactory Team 🚀",
                user.getUsername());
    }

    public static String getUserWelcomeEmailHtml(User user) {
        return String.format(
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "    <meta charset=\"UTF-8\">" +
                        "    <style>" +
                        "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" + // Color general del texto
                        "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                        "        .header { background: linear-gradient(135deg, #709A95 0%%, #4a6c68 100%%); color: white; padding: 20px; border-radius: 10px; text-align: center; }" +
                        "        .content { background: #EBE1D7; padding: 20px; border-radius: 10px; margin: 20px 0; }" +
                        "        /* Aseguramos que los párrafos dentro de .content sean negros */" +
                        "        .content p { color: black; }" + // Añadido para asegurar el color negro en los párrafos
                        "        /* Aseguramos que los elementos de lista dentro de .content sean negros */" +
                        "        .content ul li { color: black; }" + // Añadido para asegurar el color negro en la lista
                        "        .footer { text-align: center; color: #666; font-size: 14px; margin-top: 20px; }" +
                        "        .emoji { font-size: 1.2em; }" +
                        "        .button {" +
                        "            display: inline-block;" +
                        "            background: linear-gradient(135deg, #709A95 0%%, #4a6c68 100%%);" +
                        "            color: white !important; /* Asegura que el texto del enlace sea blanco */" +
                        "            padding: 10px 20px;" +
                        "            text-decoration: none; /* Elimina el subrayado */" +
                        "            border-radius: 5px;" +
                        "            margin-top: 15px;" +
                        "            border: none; /* Asegura que no haya un borde por defecto */" +
                        "            cursor: pointer; /* Indica que es un elemento clicable */" +
                        "        }" +
                        "    </style>" +
                        "</head>" +
                        "<body>" +
                        "    <div class=\"container\">" +
                        "        <div class=\"header\">" +
                        "            <h1><span class=\"emoji\">🎉</span> Welcome to DreamFactory!</h1>" +
                        "        </div>" +
                        "        <div class=\"content\">" +
                        "            <p>Hello <strong>%s</strong>! <span class=\"emoji\">👋</span></p>" +
                        "            <p>We're absolutely thrilled to have you join our community!</p>" +
                        "            <p>At DreamFactory, you can:</p>" +
                        "            <ul>" +
                        "                <li>Explore amazing destinations</li>" +
                        "                <li>Share your own unique travel experiences</li>" +
                        "            </ul>" +
                        "            <p>Ready to start creating your travel dreams?</p>" +
                        "            <p style=\"text-align: center;\">" +
                        "                <a href=\"http://localhost:8080/swagger-ui/index.html#/\" class=\"button\">Log In Now!</a>" +
                        "            </p>" +
                        "            <p>We can't wait to see what you discover and share! <span class=\"emoji\">✨</span></p>" +
                        "        </div>" +
                        "        <div class=\"footer\">" +
                        "            <p>Best regards,<br>The DreamFactory Team <span class=\"emoji\">🚀</span></p>" +
                        "        </div>" +
                        "    </div>" +
                        "</body>" +
                        "</html>",
                user.getUsername());
    }
}

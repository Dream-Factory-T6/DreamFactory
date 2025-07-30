package com.DreamFactory.DF.email;

import com.DreamFactory.DF.destination.dto.DestinationResponse;
import com.DreamFactory.DF.user.model.User;

public class DestinationEmailTemplates {
    public static String getDestinationCreatedSubject() {
        return "🎉 Your Destination Has Been Created Successfully!";
    }

    public static String getDestinationCreatedPlainText(User user, DestinationResponse destination) {
        return String.format(
                "Hello %s! 👋\n\n" +
                        "🎯 Your destination has been created successfully!\n\n" +
                        "📍 Title: %s\n" +
                        "🌍 Location: %s\n" +
                        "📝 Description: %s\n" +
                        "🖼️ Image URL: %s\n\n" +
                        "✨ Thank you for using DreamFactory! ✨\n\n" +
                        "Best regards,\n" +
                        "The DreamFactory Team 🚀",
                user.getUsername(),
                destination.title(),
                destination.location(),
                destination.description(),
                destination.imageUrl());
    }

    public static String getDestinationCreatedHtml(User user, DestinationResponse destination) {
        return String.format(
                "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "    <meta charset=\"UTF-8\">" +
                        "    <style>" +
                        "        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                        "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                        "        .header { background: linear-gradient(135deg, #709A95 0%%, #4a6c68 100%%); color: white; padding: 20px; border-radius: 10px; text-align: center; }"
                        +
                        "        .content { background: #EBE1D7; padding: 20px; border-radius: 10px; margin: 20px 0; }"
                        +
                        "        .destination-info { background: white; padding: 15px; border-radius: 8px; margin: 10px 0; border-left: 4px solid #563C1A; }"
                        +
                        "        .footer { text-align: center; color: #666; font-size: 14px; margin-top: 20px; }" +
                        "        .emoji { font-size: 1.2em; }" +
                        "    </style>" +
                        "</head>" +
                        "<body>" +
                        "    <div class=\"container\">" +
                        "        <div class=\"header\">" +
                        "            <h1><span class=\"emoji\">🎉</span> Destination Created!</h1>" +
                        "        </div>" +
                        "        <div class=\"content\">" +
                        "            <p>Hello <strong>%s</strong>! <span class=\"emoji\">👋</span></p>" +
                        "            <p>Your destination has been <strong>created successfully!</strong> <span class=\"emoji\">🎯</span></p>"
                        +
                        "            <div class=\"destination-info\">" +
                        "                <p><span class=\"emoji\">📍</span> <strong>Title:</strong> %s</p>" +
                        "                <p><span class=\"emoji\">🌍</span> <strong>Location:</strong> %s</p>" +
                        "                <p><span class=\"emoji\">📝</span> <strong>Description:</strong> %s</p>" +
                        "                <p><span class=\"emoji\">🖼️</span> <strong>Image:</strong> <a href=\"%s\" style=\"color: #667eea;\">View Image</a></p>"
                        +
                        "            </div>" +
                        "            <p><span class=\"emoji\">✨</span> Thank you for using DreamFactory! <span class=\"emoji\">✨</span></p>"
                        +
                        "        </div>" +
                        "        <div class=\"footer\">" +
                        "            <p>Best regards,<br>The DreamFactory Team <span class=\"emoji\">🚀</span></p>" +
                        "        </div>" +
                        "    </div>" +
                        "</body>" +
                        "</html>",
                user.getUsername(),
                destination.title(),
                destination.location(),
                destination.description(),
                destination.imageUrl());
    }
}
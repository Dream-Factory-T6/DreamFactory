![CI](https://github.com/Dream-Factory-T6/DreamFactory/actions/workflows/ci.yml/badge.svg)
# 💭 Dream Factory — Backend API

Dream Factory is a RESTful API developed to manage users’ travel dreams. Each user can share their desired destinations, leave reviews, like other destinations, comunicate in live chat and interact with a secure and modern backend system.
This backend project is built using Java, Spring Boot, and Spring Security. It implements authentication with JWT + Refresh Token, supports Cloudinary for image storage, and includes integrated email notifications, Web Socket for live chat.
The system features reviews, likes/unlikes, and global exception handling to ensure a reliable and user-friendly API experience.

## Index 📋
- [🗝 Key Features](#key-features-)
    - [Security and Authentication 🔒](#security-and-authentication-)
    - [Users 🙋🏻‍♀️](#users-)
    - [Destinations 🌍](#destinations-)
    - [Interaction with Destinations 💟](#interaction-with-destinations-)
    - [API Documentation ⛓](#api-documentation-)
    - [Exception Handling ❗](#exception-handling-)
- [👩🏻‍💻 Technologies Used](#technologies-used-)
- [📁 Clone the Repository](#clone-the-repository-)
    - [Run 🏃🏻‍♀️](#run-)
- [📚 API Endpoints](#-api-endpoints)
    - [Registration / Login](#registration--login)
    - [User](#user)
    - [Destination](#destination)
    - [Review](#review)
    - [Destination Like](#destination-like)
    - [Live Chat](#live-chat)
- [🏃🏻‍♂️ Running Tests](#running-tests-)
- [📊 EER Diagram](#eer-diagram)
- [💬 Chat Flow Diagram](#chat-flow-diagram)
- [📦 Class Diagram](#class-diagram-)
- [👥 Contributors](#contributors-)

## Key Features 🗝

### Security and Authentication 🔒
- Authentication with JWT + Refresh Token

- Route protection using JWT Bearer Token

- User roles: USER and ADMIN

- Implemented login, sign-up, and email notifications:

    Email confirmation upon registration

    Email notification upon submitting a review

### Users 🙋🏻‍♀️
- User registration and login

- Access to personal user data

- Admin panel (for ADMIN users only):

- View all registered users

- Edit user roles and data

- Delete users from the system

### Destinations 🌍
- CRUD operations for destinations (only for the user's own data)

- Admins can manage all destinations

- Destination images are stored using Cloudinary

- Filter destinations by title or location

- Pagination of destination list

- Sort destinations so that user’s own entries appear first

### Interaction with Destinations 💟
- Reviews: users can leave comments, with email notification sent to the author

- Like / Unlike: authenticated users can like or unlike destinations

### API Documentation ⛓
- Integrated with Swagger UI for API visualization and testing

### Exception Handling ❗
- Global exception handling using @ControllerAdvice (GlobalExceptionHandler)

- Returns structured and informative error messages

## Technologies Used 👩🏻‍💻

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![draw.io](https://img.shields.io/badge/draw.io-F08705?style=for-the-badge&logo=diagramsdotnet&logoColor=white)
![Cloudinary](https://img.shields.io/badge/cloudinary-3448C5?style=for-the-badge&logo=cloudinary&logoColor=white)
![Swagger](https://img.shields.io/badge/swagger-%2385EA2D.svg?style=for-the-badge&logo=swagger&logoColor=black)

## Clone the Repository  📁

```bash
git clone https://github.com/Dream-Factory-T6/DreamFactory.git
cd DreamFactory
```
### Run 🏃🏻‍♀️

```bash
./mvnw spring-boot:run
```
or
```bash
mvn spring-boot:run
```
Alternative Way to Run the Application
If you are using an IDE such as IntelliJ IDEA,VS Code etc, you can simply click the “Run” button or run the main application class directly (the one annotated with @SpringBootApplication).
For example, in IntelliJ IDEA, right-click the main class and choose "Run 'DfApplication...main()'".

## 📚 API Endpoints

### Registration / Login

- `POST /api/register` — Create a new user.
- `POST /api/auth/refresh` — Refresh access token using refresh token.
- `POST /api/register/admin` — Create new user by user with role ADMIN.

### User

- `GET /api/users` — Get all users.
- `GET /api/users/{id}` — Get user by ID
- `PUT /api/users/{id}` — Update user by user with role ADMIN.
- `DELETE /api/users/{id}` — Delete user by ID

### Destination

- `GET /api/destinations` — Get all Destination.
- `GET /api/destinations/{id}` — Get Destination by ID.
- `GET /api/destinations/filter` — Get Destination by filter.
- `GET /api/destinations/my-destinations` - Get Destination by username.
- `POST /api/destinations` — Create new Destination.
- `PUT /api/destinations/{id}` — Update Destination by ID
- `DELETE /api/cart/remove/{productId}` — Delete Destination by product ID

### Review

- `GET /api/reviews` — Get all reviews by username from Authorization.
- `GET /api/reviews/destination/{id}` — Get all reviews by Destination ID.
- `POST /api/reviews` — Create new review.
- `PUT /api/reviews/{id}` — Update review by ID.
- `DELETE /api/reviews/{id}` — Delete review by ID.

### Destination Like

- `POST /api/destinations/{id}/likes/toggle` — For Like/UnLike.
- `GET /api/destinations/{id}/likes` — Get all Likes by Destination ID.

### Live Chat

 - `GET /api/chat/room/{roomId}/messages` — Get the latest messages from the room
 - `GET /api/chat/rooms`— Get a list of available rooms
 - `/chat.sendMessage` — Send message
 - `/chat.addUser` — Add user to room
 - `/chat.leaveRoom` — Leave the room

## Running Tests 🏃🏻‍♂️

[![temp-Imagen0jdwy.avif](https://i.postimg.cc/HsZtgLhY/temp-Imagen0jdwy.avif)](https://postimg.cc/MvBB0zwN)

## EER Diagram

[![temp-Image-UYIh0o.avif](https://i.postimg.cc/nLG4d4By/temp-Image-UYIh0o.avif)](https://postimg.cc/Y4jL0mXR)

## Chat Flow Diagram

[![Flow-D-Dream-Factory-1.jpg](https://i.postimg.cc/Yq7LtPQs/Flow-D-Dream-Factory-1.jpg)](https://postimg.cc/BthngMYT)


##  Class Diagram 

[![drem-factory-classes.jpg](https://i.postimg.cc/hPGg8bnG/drem-factory-classes.jpg)](https://postimg.cc/dDgzwCLM)
## Contributors 👩🏻‍💻
Paula Calvo Garcia
    <a href="https://github.com/PCalvoGarcia">
        <picture>
            <source srcset="https://img.icons8.com/ios-glyphs/30/ffffff/github.png" media="(prefers-color-scheme: dark)">
            <source srcset="https://img.icons8.com/ios-glyphs/30/000000/github.png" media="(prefers-color-scheme: light)">
            <img src="https://img.icons8.com/ios-glyphs/30/000000/github.png" alt="GitHub icon"/>
        </picture>
    </a>

Nadiia Alaieva
    <a href="https://github.com/tizzifona">
        <picture>
            <source srcset="https://img.icons8.com/ios-glyphs/30/ffffff/github.png" media="(prefers-color-scheme: dark)">
            <source srcset="https://img.icons8.com/ios-glyphs/30/000000/github.png" media="(prefers-color-scheme: light)">
            <img src="https://img.icons8.com/ios-glyphs/30/000000/github.png" alt="GitHub icon"/>
        </picture>
    </a>

Anna Nepyivoda
    <a href="https://github.com/NepyAnna">
        <picture>
            <source srcset="https://img.icons8.com/ios-glyphs/30/ffffff/github.png" media="(prefers-color-scheme: dark)">
            <source srcset="https://img.icons8.com/ios-glyphs/30/000000/github.png" media="(prefers-color-scheme: light)">
            <img src="https://img.icons8.com/ios-glyphs/30/000000/github.png" alt="GitHub icon"/>
        </picture>
    </a>

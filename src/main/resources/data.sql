-- Insert users
INSERT INTO users (username, email, password) VALUES
('admin', 'admin@happytravel.com', '$2a$10$HsMF2wIVlZAelTWGNHD/r.lbHJemKWx0.HEfqHKHF91CR8R3fDjX2'),
('user', 'user@happytravel.com', '$2a$10$HsMF2wIVlZAelTWGNHD/r.lbHJemKWx0.HEfqHKHF91CR8R3fDjX2'),
('john_doe', 'john_doe@example.com', '$2a$10$HsMF2wIVlZAelTWGNHD/r.lbHJemKWx0.HEfqHKHF91CR8R3fDjX2'),
('jane_smith', 'jane@example.com', '$2a$10$HsMF2wIVlZAelTWGNHD/r.lbHJemKWx0.HEfqHKHF91CR8R3fDjX2'),
('mike_wilson', 'mike@example.com', '$2a$10$HsMF2wIVlZAelTWGNHD/r.lbHJemKWx0.HEfqHKHF91CR8R3fDjX2'),
('sarah_jones', 'sarah@example.com', '$2a$10$HsMF2wIVlZAelTWGNHD/r.lbHJemKWx0.HEfqHKHF91CR8R3fDjX2'),
('david_brown', 'david@example.com', '$2a$10$HsMF2wIVlZAelTWGNHD/r.lbHJemKWx0.HEfqHKHF91CR8R3fDjX2');

-- Insert user roles
INSERT INTO user_roles (user_id, roles) VALUES
(1, 'ADMIN'),
(2, 'USER'),
(3, 'USER'),
(4, 'USER'),
(5, 'USER'),
(6, 'USER'),
(7, 'USER');

-- Insert destinations
INSERT INTO destinations (title, location, description, image_url, user_id, created_at, updated_at) VALUES
-- John's destinations
('Paradise Beach', 'Bali, Indonesia', 'A stunning tropical paradise with crystal clear waters and white sandy beaches. Perfect for relaxation and water sports.', 'https://res.cloudinary.com/dreamfactory/image/upload/v1753096895/1e394614-dca5-41a7-b22c-c94bc9682f27_tn3gxc.jpg', 3, '2023-06-15 14:30:00', '2023-06-20 09:15:00'),
('Mountain Retreat', 'Swiss Alps', 'Breathtaking mountain views with cozy chalets and world-class skiing opportunities. A winter wonderland for adventure seekers.', 'https://res.cloudinary.com/dreamfactory/image/upload/v1753437020/shutterstock_279572969-800x600_ucyhui.jpg', 3, '2023-08-22 11:45:00', '2023-09-05 16:20:00'),
('Desert Oasis', 'Sahara Desert, Morocco', 'Experience the magic of the desert with camel treks, traditional Berber camps, and stunning starry nights.', 'https://res.cloudinary.com/dreamfactory/image/upload/v1753097042/%D0%A1%D0%B0%D1%85%D0%B0%D1%80%D0%B0-7_2_paukz8.jpg', 3, '2023-10-08 08:20:00', '2023-10-12 13:45:00'),

-- Jane's destinations
('Cherry Blossom Festival', 'Tokyo, Japan', 'Witness the magical cherry blossom season in Japan. Beautiful parks, traditional temples, and amazing food culture.', 'https://res.cloudinary.com/dreamfactory/image/upload/v1753101472/0d0c1b3ecee81993150a588c1ccb8cbc-e1557736741191_ui9nxf.jpg', 4, '2023-04-12 16:00:00', '2023-04-18 10:30:00'),
('Northern Lights', 'Reykjavik, Iceland', 'Chase the aurora borealis in Iceland. Hot springs, glaciers, and the most spectacular light show on Earth.', 'https://res.cloudinary.com/dreamfactory/image/upload/v1753101537/aurora-northern-lights-790x474_up4cae.jpg', 4, '2023-11-30 19:15:00', '2023-12-10 14:00:00'),
('Amazon Adventure', 'Manaus, Brazil', 'Explore the world''s largest rainforest. Wildlife watching, river cruises, and indigenous culture experiences.', 'https://res.cloudinary.com/dreamfactory/image/upload/v1753101602/98.jpg_h3jran.png', 4, '2023-07-03 12:45:00', '2023-07-15 17:30:00'),

-- Mike's destinations
('Great Wall Trek', 'Beijing, China', 'Walk along the ancient Great Wall of China. Rich history, stunning architecture, and panoramic mountain views.', 'https://res.cloudinary.com/dreamfactory/image/upload/v1753101671/aa19e128-8df9-4362-92b0-eb7b4aa9b7d2_wamxmj.jpg', 5, '2023-05-20 09:30:00', '2023-05-25 11:45:00'),
('Venice Canals', 'Venice, Italy', 'Float through the romantic canals of Venice. Historic architecture, gondola rides, and authentic Italian cuisine.', 'https://res.cloudinary.com/dreamfactory/image/upload/v1753101732/Italy-Venice-4_n8mqr8.jpg', 5, '2023-09-14 15:20:00', '2023-09-22 08:50:00'),
('Machu Picchu', 'Cusco, Peru', 'Discover the ancient Incan citadel high in the Andes. Archaeological wonder and breathtaking mountain scenery.', 'https://res.cloudinary.com/dreamfactory/image/upload/v1753101796/machu-picchu-peru-foto_eamc3a.jpg', 5, '2023-12-05 06:00:00', '2023-12-12 18:30:00'),

-- Sarah's destinations
('Santorini Sunset', 'Santorini, Greece', 'Experience the most beautiful sunsets in the world. White-washed buildings, blue domes, and Mediterranean charm.', 'https://res.cloudinary.com/dreamfactory/image/upload/v1753102079/Veduta-di-Santorni-Foto-U_u5sigt.jpg', 6, '2023-03-28 13:15:00', '2023-04-02 20:45:00'),
('Taj Mahal', 'Agra, India', 'Visit the iconic symbol of love. Marvel at the stunning white marble architecture and rich Mughal history.', 'https://res.cloudinary.com/dreamfactory/image/upload/v1753102146/GettyImages-1208049833-scaled-e1654782377122.jpg_jzou6e.webp', 6, '2023-06-08 10:00:00', '2023-06-15 14:20:00'),
('Serengeti Safari', 'Tanzania', 'Go on an African safari adventure. Witness the great migration and see the Big Five in their natural habitat.', 'https://res.cloudinary.com/dreamfactory/image/upload/v1753102219/tanzania_ueqwyg.jpg', 6, '2023-08-01 07:30:00', '2023-08-08 16:15:00'),

-- David's destinations
('New York City', 'New York, USA', 'The city that never sleeps. Broadway shows, world-class museums, and iconic landmarks like Times Square.', 'https://res.cloudinary.com/dreamfactory/image/upload/v1753102290/New_York_1_p3fpi4.jpg', 7, '2023-02-15 18:45:00', '2023-02-22 12:30:00'),
('Sydney Opera House', 'Sydney, Australia', 'Iconic architecture and harbor views. Opera performances, guided tours, and beautiful coastal walks.', 'https://res.cloudinary.com/dreamfactory/image/upload/v1753102355/Circular-Quay_Charlotte-Karp_tf0erw.jpg', 7, '2023-10-25 21:00:00', '2023-11-02 09:15:00'),
('Petra Ancient City', 'Jordan', 'Explore the ancient Nabatean city carved into red sandstone cliffs. Archaeological wonder and desert beauty.', 'https://res.cloudinary.com/dreamfactory/image/upload/v1753102430/petra-world-heritage-jordan.jpg_bw9owe.avif', 7, '2023-01-10 14:20:00', '2023-01-18 11:45:00');

-- Insert reviews for each destination (3 reviews per destination)
INSERT INTO reviews (rating, body, user_id, destination_id, created_at) VALUES
-- Reviews for Paradise Beach (destination_id = 1)
(5.0, 'Absolutely stunning! The water is crystal clear and the beach is pristine. Perfect for swimming and snorkeling.', 4, 1, '2023-07-10 16:30:00'),
(4.0, 'Beautiful location but quite touristy. Still worth visiting for the amazing sunsets.', 5, 1, '2023-07-25 14:15:00'),
(5.0, 'Paradise found! The local food is amazing and the people are so friendly.', 6, 1, '2023-08-05 19:45:00'),

-- Reviews for Mountain Retreat (destination_id = 2)
(5.0, 'Incredible skiing experience! The slopes are well-maintained and the views are breathtaking.', 4, 2, '2023-09-15 11:20:00'),
(4.0, 'Great for both beginners and advanced skiers. The chalet was cozy and comfortable.', 5, 2, '2023-09-28 09:45:00'),
(5.0, 'Winter wonderland! The après-ski scene is fantastic and the mountain restaurants are excellent.', 6, 2, '2023-10-12 17:30:00'),

-- Reviews for Desert Oasis (destination_id = 3)
(4.0, 'Magical experience under the stars. The camel trek was unforgettable.', 4, 3, '2023-10-20 08:15:00'),
(5.0, 'The Berber camp was authentic and comfortable. The desert silence is incredible.', 5, 3, '2023-11-02 13:40:00'),
(4.0, 'Amazing cultural experience. The traditional music and food were highlights.', 6, 3, '2023-11-15 20:25:00'),

-- Reviews for Cherry Blossom Festival (destination_id = 4)
(5.0, 'Absolutely magical! The cherry blossoms are more beautiful than photos can capture.', 3, 4, '2023-04-25 10:30:00'),
(4.0, 'Great timing with the festival. The food stalls and traditional performances were amazing.', 5, 4, '2023-05-08 15:20:00'),
(5.0, 'Bucket list experience! The temples and gardens are stunning during sakura season.', 6, 4, '2023-05-20 12:45:00'),

-- Reviews for Northern Lights (destination_id = 5)
(5.0, 'Aurora borealis was spectacular! The hot springs were perfect for warming up.', 3, 5, '2023-12-15 18:00:00'),
(4.0, 'Iceland is incredible. The glaciers and waterfalls are must-see attractions.', 5, 5, '2023-12-28 14:30:00'),
(5.0, 'Once in a lifetime experience! The northern lights danced across the sky for hours.', 6, 5, '2024-01-10 21:15:00'),

-- Reviews for Amazon Adventure (destination_id = 6)
(4.0, 'Incredible wildlife sightings! The river cruise was educational and exciting.', 3, 6, '2023-07-20 09:00:00'),
(5.0, 'The indigenous village visit was eye-opening. The rainforest is truly amazing.', 5, 6, '2023-08-02 16:45:00'),
(4.0, 'Humid but worth it! Saw monkeys, birds, and even a jaguar from afar.', 6, 6, '2023-08-18 11:30:00'),

-- Reviews for Great Wall Trek (destination_id = 7)
(5.0, 'Historical and physically challenging. The views from the wall are incredible.', 3, 7, '2023-06-05 07:30:00'),
(4.0, 'Great workout with amazing history. The restored sections are easier to walk.', 4, 7, '2023-06-18 13:15:00'),
(5.0, 'Bucket list achievement! The less touristy sections offer the best experience.', 6, 7, '2023-07-01 10:45:00'),

-- Reviews for Venice Canals (destination_id = 8)
(5.0, 'Romantic and magical! The gondola ride was expensive but worth every penny.', 3, 8, '2023-09-25 19:30:00'),
(4.0, 'Beautiful architecture everywhere you look. The food is amazing too.', 4, 8, '2023-10-08 15:20:00'),
(5.0, 'Venice is like a fairy tale. Getting lost in the narrow streets is part of the charm.', 6, 8, '2023-10-22 12:10:00'),

-- Reviews for Machu Picchu (destination_id = 9)
(5.0, 'Absolutely breathtaking! The early morning visit was worth the early wake-up call.', 3, 9, '2023-12-20 06:00:00'),
(4.0, 'The altitude takes some getting used to, but the site is incredible.', 4, 9, '2024-01-05 14:30:00'),
(5.0, 'Ancient wonder of the world! The engineering and architecture are mind-blowing.', 6, 9, '2024-01-18 08:45:00'),

-- Reviews for Santorini Sunset (destination_id = 10)
(5.0, 'The most beautiful sunset I''ve ever seen! The white buildings are stunning.', 3, 10, '2023-04-10 18:45:00'),
(4.0, 'Romantic and picturesque. The wine tasting was a great addition to the trip.', 4, 10, '2023-04-25 20:15:00'),
(5.0, 'Oia is magical at sunset. The blue domes against the white buildings are iconic.', 5, 10, '2023-05-12 19:30:00'),

-- Reviews for Taj Mahal (destination_id = 11)
(5.0, 'The marble work is incredible! The symmetry and detail are mind-blowing.', 3, 11, '2023-06-25 09:15:00'),
(4.0, 'Very crowded but still beautiful. Early morning visit is recommended.', 4, 11, '2023-07-08 07:30:00'),
(5.0, 'Symbol of eternal love. The story behind the monument makes it even more special.', 5, 11, '2023-07-22 16:45:00'),

-- Reviews for Serengeti Safari (destination_id = 12)
(5.0, 'Saw the Big Five! The migration was incredible to witness.', 3, 12, '2023-08-15 05:30:00'),
(4.0, 'Amazing wildlife photography opportunities. The guides were very knowledgeable.', 4, 12, '2023-08-28 17:20:00'),
(5.0, 'African adventure of a lifetime! The tented camp experience was authentic and comfortable.', 5, 12, '2023-09-10 12:45:00'),

-- Reviews for New York City (destination_id = 13)
(4.0, 'The energy of the city is incredible! Broadway shows are a must.', 3, 13, '2023-03-05 21:00:00'),
(5.0, 'So much to see and do! Central Park is beautiful and the museums are world-class.', 4, 13, '2023-03-18 14:30:00'),
(4.0, 'The food scene is amazing. Times Square at night is like nothing else.', 5, 13, '2023-04-02 19:15:00'),

-- Reviews for Sydney Opera House (destination_id = 14)
(5.0, 'Iconic architecture! The guided tour was informative and the harbor views are stunning.', 3, 14, '2023-11-10 10:45:00'),
(4.0, 'Beautiful building and great performances. The Sydney Harbour Bridge walk is also recommended.', 4, 14, '2023-11-25 15:20:00'),
(5.0, 'Australian landmark that lives up to the hype. The acoustics inside are incredible.', 5, 14, '2023-12-08 13:30:00'),

-- Reviews for Petra Ancient City (destination_id = 15)
(5.0, 'The Treasury is magnificent! The hike through the Siq is dramatic and exciting.', 3, 15, '2023-02-20 11:30:00'),
(4.0, 'Ancient wonder that''s worth the journey. The rock colors are beautiful in the sunlight.', 4, 15, '2023-03-08 16:45:00'),
(5.0, 'Archaeological treasure! The Monastery hike was challenging but the views were incredible.', 5, 15, '2023-03-25 09:15:00');


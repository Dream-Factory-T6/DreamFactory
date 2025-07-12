-- Insert users
INSERT INTO users (username, email, password) VALUES
('admin', 'admin@happytravel.com', '$2a$10$HsMF2wIVlZAelTWGNHD/r.lbHJemKWx0.HEfqHKHF91CR8R3fDjX2'),
('user', 'user@happytravel.com', '$2a$10$HsMF2wIVlZAelTWGNHD/r.lbHJemKWx0.HEfqHKHF91CR8R3fDjX2'),
('john_doe', 'john@example.com', '$2a$10$HsMF2wIVlZAelTWGNHD/r.lbHJemKWx0.HEfqHKHF91CR8R3fDjX2'),
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
('Paradise Beach', 'Bali, Indonesia', 'A stunning tropical paradise with crystal clear waters and white sandy beaches. Perfect for relaxation and water sports.', 'https://images.unsplash.com/photo-1537953773345-d172ccf13cf1', 3, NOW(), NOW()),
('Mountain Retreat', 'Swiss Alps', 'Breathtaking mountain views with cozy chalets and world-class skiing opportunities. A winter wonderland for adventure seekers.', 'https://images.unsplash.com/photo-1506905925346-21bda4d32df4', 3, NOW(), NOW()),
('Desert Oasis', 'Sahara Desert, Morocco', 'Experience the magic of the desert with camel treks, traditional Berber camps, and stunning starry nights.', 'https://images.unsplash.com/photo-1509316785289-025f5b846b35', 3, NOW(), NOW()),

-- Jane's destinations
('Cherry Blossom Festival', 'Tokyo, Japan', 'Witness the magical cherry blossom season in Japan. Beautiful parks, traditional temples, and amazing food culture.', 'https://images.unsplash.com/photo-1522383225653-ed111181a951', 4, NOW(), NOW()),
('Northern Lights', 'Reykjavik, Iceland', 'Chase the aurora borealis in Iceland. Hot springs, glaciers, and the most spectacular light show on Earth.', 'https://images.unsplash.com/photo-1531366936337-7c912a4589a7', 4, NOW(), NOW()),
('Amazon Adventure', 'Manaus, Brazil', 'Explore the world''s largest rainforest. Wildlife watching, river cruises, and indigenous culture experiences.', 'https://images.unsplash.com/photo-1441974231531-c6227db76b6e', 4, NOW(), NOW()),

-- Mike's destinations
('Great Wall Trek', 'Beijing, China', 'Walk along the ancient Great Wall of China. Rich history, stunning architecture, and panoramic mountain views.', 'https://images.unsplash.com/photo-1508804185872-d7badad00f7d', 5, NOW(), NOW()),
('Venice Canals', 'Venice, Italy', 'Float through the romantic canals of Venice. Historic architecture, gondola rides, and authentic Italian cuisine.', 'https://images.unsplash.com/photo-1523906834658-6e24ef2386f9', 5, NOW(), NOW()),
('Machu Picchu', 'Cusco, Peru', 'Discover the ancient Incan citadel high in the Andes. Archaeological wonder and breathtaking mountain scenery.', 'https://images.unsplash.com/photo-1587595431973-160d0d94add1', 5, NOW(), NOW()),

-- Sarah's destinations
('Santorini Sunset', 'Santorini, Greece', 'Experience the most beautiful sunsets in the world. White-washed buildings, blue domes, and Mediterranean charm.', 'https://images.unsplash.com/photo-1570077188670-e3a8d69ac5ff', 6, NOW(), NOW()),
('Taj Mahal', 'Agra, India', 'Visit the iconic symbol of love. Marvel at the stunning white marble architecture and rich Mughal history.', 'https://images.unsplash.com/photo-1564507592333-c60657eea523', 6, NOW(), NOW()),
('Serengeti Safari', 'Tanzania', 'Go on an African safari adventure. Witness the great migration and see the Big Five in their natural habitat.', 'https://images.unsplash.com/photo-1549366021-9f761d450615', 6, NOW(), NOW()),

-- David's destinations
('New York City', 'New York, USA', 'The city that never sleeps. Broadway shows, world-class museums, and iconic landmarks like Times Square.', 'https://images.unsplash.com/photo-1496442226666-8d4d0e62e6e9', 7, NOW(), NOW()),
('Sydney Opera House', 'Sydney, Australia', 'Iconic architecture and harbor views. Opera performances, guided tours, and beautiful coastal walks.', 'https://images.unsplash.com/photo-1506973035872-a4ec16b8e8d9', 7, NOW(), NOW()),
('Petra Ancient City', 'Jordan', 'Explore the ancient Nabatean city carved into red sandstone cliffs. Archaeological wonder and desert beauty.', 'https://images.unsplash.com/photo-1551698618-1dfe5d97d256', 7, NOW(), NOW());

-- Insert reviews for each destination (3 reviews per destination)
INSERT INTO reviews (rating, body, user_id, destination_id, created_at) VALUES
-- Reviews for Paradise Beach (destination_id = 1)
(5.0, 'Absolutely stunning! The water is crystal clear and the beach is pristine. Perfect for swimming and snorkeling.', 4, 1, NOW()),
(4.0, 'Beautiful location but quite touristy. Still worth visiting for the amazing sunsets.', 5, 1, NOW()),
(5.0, 'Paradise found! The local food is amazing and the people are so friendly.', 6, 1, NOW()),

-- Reviews for Mountain Retreat (destination_id = 2)
(5.0, 'Incredible skiing experience! The slopes are well-maintained and the views are breathtaking.', 4, 2, NOW()),
(4.0, 'Great for both beginners and advanced skiers. The chalet was cozy and comfortable.', 5, 2, NOW()),
(5.0, 'Winter wonderland! The après-ski scene is fantastic and the mountain restaurants are excellent.', 6, 2, NOW()),

-- Reviews for Desert Oasis (destination_id = 3)
(4.0, 'Magical experience under the stars. The camel trek was unforgettable.', 4, 3, NOW()),
(5.0, 'The Berber camp was authentic and comfortable. The desert silence is incredible.', 5, 3, NOW()),
(4.0, 'Amazing cultural experience. The traditional music and food were highlights.', 6, 3, NOW()),

-- Reviews for Cherry Blossom Festival (destination_id = 4)
(5.0, 'Absolutely magical! The cherry blossoms are more beautiful than photos can capture.', 3, 4, NOW()),
(4.0, 'Great timing with the festival. The food stalls and traditional performances were amazing.', 5, 4, NOW()),
(5.0, 'Bucket list experience! The temples and gardens are stunning during sakura season.', 6, 4, NOW()),

-- Reviews for Northern Lights (destination_id = 5)
(5.0, 'Aurora borealis was spectacular! The hot springs were perfect for warming up.', 3, 5, NOW()),
(4.0, 'Iceland is incredible. The glaciers and waterfalls are must-see attractions.', 5, 5, NOW()),
(5.0, 'Once in a lifetime experience! The northern lights danced across the sky for hours.', 6, 5, NOW()),

-- Reviews for Amazon Adventure (destination_id = 6)
(4.0, 'Incredible wildlife sightings! The river cruise was educational and exciting.', 3, 6, NOW()),
(5.0, 'The indigenous village visit was eye-opening. The rainforest is truly amazing.', 5, 6, NOW()),
(4.0, 'Humid but worth it! Saw monkeys, birds, and even a jaguar from afar.', 6, 6, NOW()),

-- Reviews for Great Wall Trek (destination_id = 7)
(5.0, 'Historical and physically challenging. The views from the wall are incredible.', 3, 7, NOW()),
(4.0, 'Great workout with amazing history. The restored sections are easier to walk.', 4, 7, NOW()),
(5.0, 'Bucket list achievement! The less touristy sections offer the best experience.', 6, 7, NOW()),

-- Reviews for Venice Canals (destination_id = 8)
(5.0, 'Romantic and magical! The gondola ride was expensive but worth every penny.', 3, 8, NOW()),
(4.0, 'Beautiful architecture everywhere you look. The food is amazing too.', 4, 8, NOW()),
(5.0, 'Venice is like a fairy tale. Getting lost in the narrow streets is part of the charm.', 6, 8, NOW()),

-- Reviews for Machu Picchu (destination_id = 9)
(5.0, 'Absolutely breathtaking! The early morning visit was worth the early wake-up call.', 3, 9, NOW()),
(4.0, 'The altitude takes some getting used to, but the site is incredible.', 4, 9, NOW()),
(5.0, 'Ancient wonder of the world! The engineering and architecture are mind-blowing.', 6, 9, NOW()),

-- Reviews for Santorini Sunset (destination_id = 10)
(5.0, 'The most beautiful sunset I''ve ever seen! The white buildings are stunning.', 3, 10, NOW()),
(4.0, 'Romantic and picturesque. The wine tasting was a great addition to the trip.', 4, 10, NOW()),
(5.0, 'Oia is magical at sunset. The blue domes against the white buildings are iconic.', 5, 10, NOW()),

-- Reviews for Taj Mahal (destination_id = 11)
(5.0, 'The marble work is incredible! The symmetry and detail are mind-blowing.', 3, 11, NOW()),
(4.0, 'Very crowded but still beautiful. Early morning visit is recommended.', 4, 11, NOW()),
(5.0, 'Symbol of eternal love. The story behind the monument makes it even more special.', 5, 11, NOW()),

-- Reviews for Serengeti Safari (destination_id = 12)
(5.0, 'Saw the Big Five! The migration was incredible to witness.', 3, 12, NOW()),
(4.0, 'Amazing wildlife photography opportunities. The guides were very knowledgeable.', 4, 12, NOW()),
(5.0, 'African adventure of a lifetime! The tented camp experience was authentic and comfortable.', 5, 12, NOW()),

-- Reviews for New York City (destination_id = 13)
(4.0, 'The energy of the city is incredible! Broadway shows are a must.', 3, 13, NOW()),
(5.0, 'So much to see and do! Central Park is beautiful and the museums are world-class.', 4, 13, NOW()),
(4.0, 'The food scene is amazing. Times Square at night is like nothing else.', 5, 13, NOW()),

-- Reviews for Sydney Opera House (destination_id = 14)
(5.0, 'Iconic architecture! The guided tour was informative and the harbor views are stunning.', 3, 14, NOW()),
(4.0, 'Beautiful building and great performances. The Sydney Harbour Bridge walk is also recommended.', 4, 14, NOW()),
(5.0, 'Australian landmark that lives up to the hype. The acoustics inside are incredible.', 5, 14, NOW()),

-- Reviews for Petra Ancient City (destination_id = 15)
(5.0, 'The Treasury is magnificent! The hike through the Siq is dramatic and exciting.', 3, 15, NOW()),
(4.0, 'Ancient wonder that''s worth the journey. The rock colors are beautiful in the sunlight.', 4, 15, NOW()),
(5.0, 'Archaeological treasure! The Monastery hike was challenging but the views were incredible.', 5, 15, NOW());


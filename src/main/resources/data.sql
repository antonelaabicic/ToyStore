INSERT INTO TOY_CATEGORY (NAME)
VALUES
    ('ACTION_FIGURES'), ('DOLLS'), ('VEHICLES'), ('PLUSHIES'), ('OUTDOOR_TOYS'), ('OTHER');

-- ACTION_FIGURES
INSERT INTO TOY (name, description, price, CATEGORY_ID, IMAGE_URL) VALUES
    ('Mega Warrior', 'Poseable action warrior with shield and sword.', 12.99,  1, '/images/superman.jpg'),
    ('Cyber Ninja', 'Stealthy ninja with LED katana.', 15.49,  1, '/images/kakashi.jpg'),
    ('Alien Invader', 'Glow-in-the-dark alien figure.', 11.99,  1, '/images/alien.jpg'),
    ('Galaxy Defender', 'Futuristic space soldier with blaster.', 13.99,  1, '/images/doctor_strange.jpg'),
    ('Mutant Lizard', 'Reptilian villain from the swamps.', 10.50,  1, '/images/lizard.jpg'),
    ('Time Jumper', 'Hero with interchangeable time-travel gear.', 16.75,  1, '/images/chains.jpg'),
    ('Battle Bot', 'Robotic fighter with spinning arms.', 14.25,  1, '/images/optimus_prime.jpg');

-- DOLLS
INSERT INTO TOY (name, description, price, CATEGORY_ID, IMAGE_URL) VALUES
    ('Princess Ella', 'Royal doll with gown and tiara.', 17.99,  2, '/images/princess.jpg'),
    ('Baby Care Doll', 'Realistic baby doll with bottle and blanket.', 19.50,  2, '/images/baby_doll.jpg'),
    ('Styling Head', 'Practice hairstyles on this deluxe doll head.', 22.75,  2, '/images/styling_head.jpg'),
    ('Glitter Pop Doll', 'Doll with surprise glitter fashion.', 18.99,  2, '/images/glitter_doll.jpg'),
    ('Adventure Girl', 'Backpack-wearing doll ready for hikes.', 15.60,  2, '/images/adventure_doll.jpg'),
    ('Doll Family Set', 'Mom, dad, and two kids with accessories.', 25.00,  2, '/images/doll_family.jpg'),
    ('Color Change Doll', 'Hair and clothes change color with water.', 21.49,  2, '/images/color_change_doll.jpg');

-- VEHICLES
INSERT INTO TOY (name, description, price, CATEGORY_ID, IMAGE_URL) VALUES
    ('Fire Rescue Truck', 'Big red fire engine with ladder and siren.', 23.99,  3, '/images/fire_truck.jpg'),
    ('Train Express Set', 'Mini train with 20 pieces of track.', 19.95,  3, '/images/train_set.jpg'),
    ('Speed Racer Cars', '3-pack of colorful racing cars.', 9.99,  3, '/images/speed_racer.jpg'),
    ('Bulldozer Toy', 'Heavy-duty construction toy with movable scoop.', 14.25,  3, '/images/bulldozer.jpg'),
    ('Airport Jet', 'Passenger plane with rolling wheels and stairs.', 16.50,  3, '/images/airport_jet.jpg'),
    ('Tow Truck', 'Toy tow truck with movable arm.', 13.75,  3, '/images/tow_truck.jpg'),
    ('Police Cruiser', 'Police car with lights and sound.', 18.00,  3, '/images/police_cruiser.jpg');

-- PLUSHIES
INSERT INTO TOY (name, description, price, CATEGORY_ID, IMAGE_URL) VALUES
    ('Classic Teddy', 'Fluffy brown bear with bowtie.', 14.99,  4, '/images/classic_teddy.jpg'),
    ('Rainbow Unicorn', 'Magical unicorn with shiny horn.', 17.25,  4, '/images/rainbow_unicorn.jpg'),
    ('Sleepy Bunny', 'Soft white bunny with sleeping cap.', 13.50,  4, '/images/sleepy_bunny.jpg'),
    ('Dino Buddy', 'Cuddly green dinosaur with big eyes.', 15.00,  4, '/images/dino.jpg'),
    ('Cat Plush', 'Gray kitten plush with fish toy.', 12.75,  4, '/images/cat.jpg'),
    ('Giraffe Snuggler', 'Long-neck plush with cozy spots.', 14.30,  4, '/images/giraffe.jpg'),
    ('Dog Plush', 'Cute dog with long ears..', 11.99,  4, '/images/dog.jpg');

-- OUTDOOR_TOYS
INSERT INTO TOY (name, description, price, CATEGORY_ID, IMAGE_URL) VALUES
    ('Foam Rocket Set', 'Launchable rockets with stomp launcher.', 19.95,  5, '/images/foam_rockets.jpg'),
    ('Frisbee Flyer', 'Lightweight frisbee for ages 5 and up.', 7.99,  5, '/images/frisbee.jpg'),
    ('Jump Rope', 'Colorful jump rope with foam handles.', 6.50,  5, '/images/jump_rope.jpg'),
    ('Bubble Blaster', 'Shoots bubbles rapidly, includes soap.', 11.49,  5, '/images/bubble_blaster.jpg'),
    ('Water Balloon Kit', '100+ balloons with quick fill tool.', 9.99,  5, '/images/water_balloons.jpg'),
    ('Mini Basketball Set', 'Indoor/outdoor hoop with soft ball.', 15.00,  5, '/images/mini_hoop.jpg'),
    ('Nerf Football', 'Foam football for safe backyard play.', 13.75,  5, '/images/nerf_football.jpg');

-- OTHER
INSERT INTO TOY (name, description, price, CATEGORY_ID, IMAGE_URL) VALUES
    ('DIY Slime Kit', 'Make your own slime with safe ingredients.', 12.99,  6, '/images/slime_kit.jpg'),
    ('Kids Telescope', 'Entry-level telescope for young explorers.', 29.95,  6, '/images/telescope.jpg'),
    ('Surprise Egg Pack', '5 eggs with mystery mini toys inside.', 10.50,  6, '/images/surprise_eggs.jpg'),
    ('Origami Set', 'Colorful paper and guides to fold animals.', 8.99,  6, '/images/origami_set.jpg'),
    ('Magnet Lab', 'Playset with fun magnetic experiments.', 18.75,  6, '/images/magnet_lab.jpg'),
    ('Glow Sticks Party Pack', 'Glow bracelets and necklaces for parties.', 7.50,  6, '/images/glow_sticks.jpg');

INSERT INTO PAYMENT_METHOD (NAME)
VALUES
    ('PAYPAL'), ('CASH_ON_DELIVERY');

INSERT INTO USERS(username, password, name, surname, email, country, city, address)
VALUES
    ('pperic', '$2a$10$HDaMrzUr4V5I6GfwXa0kxuXW7sMUk8bF/HBmY3ygvSymqsJk8Cx7C', 'Pero', 'Peric', 'pero.peric.admin@gmail.com', 'Hrvatska', 'Zagreb', 'Ulica Potočnica 7'),
    ('aanic', '$2a$10$HDaMrzUr4V5I6GfwXa0kxuXW7sMUk8bF/HBmY3ygvSymqsJk8Cx7C', 'Ana', 'Anic', 'ana.anic@gmail.com', 'Hrvatska', 'Pirovac', 'Primorska 21'),
    ('hhanic', '$2a$10$HDaMrzUr4V5I6GfwXa0kxuXW7sMUk8bF/HBmY3ygvSymqsJk8Cx7C', 'Hana', 'Hanic', 'hana.hanic@gmail.com', 'Hrvatska', 'Zagreb', 'Čulinečka 3');

INSERT INTO ROLE(name)
VALUES
    ('ROLE_USER'),
    ('ROLE_ADMIN');

INSERT INTO USER_ROLE(user_id, role_id)
VALUES
    (1, 2),
    (2, 1),
    (3, 1);


INSERT INTO CART (id, session_id) VALUES (1001, 'test-session-abc');
INSERT INTO CART_ITEM (id, cart_id, toy_id, quantity) VALUES (5001, 1001, 1, 2);
INSERT INTO ORDERS (cart_id, order_date, payment_method_id, user_id)
VALUES (1001, '2024-04-27 10:00:00', 1, 3);





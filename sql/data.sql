/*
TRUNCATE TABLE banks;
TRUNCATE TABLE accounts;
TRUNCATE TABLE users;
TRUNCATE TABLE transactions;
*/

INSERT INTO banks ("name")
VALUES ('Clever-Bank'),
        ('IDEA bank'),
        ('Trust bank'),
        ('Just in time bank'),
        ('Bank Individual');

INSERT INTO users (first_name, last_name)
VALUES ('Aleksandra', 'Fedorova'),
        ('Egor', 'Kiselev'),
        ('Adnrey', 'Parfenov'),
        ('Maxim', 'Orlov'),
        ('Arina', 'Radionova'),
        ('Eva', 'Cudryavceva'),
        ('Kirill', 'Firsov'),
        ('Emilia', 'Belousova'),
        ('Nikita', 'Sokolov'),
        ('Fyodor', 'Galkin'),
        ('Vladislav', 'Kornilov'),
        ('Zlata', 'Volkova'),
        ('Arina', 'Davydova'),
        ('Ksenia', 'Vasilyeva'),
        ('Ekaterina', 'Alyoshina'),
        ('Elizaveta', 'Ivanova'),
        ('Sofya', 'Antonova'),
        ('Matvey', 'Ryabov'),
        ('Ilya', 'Zhukov'),
        ('Daniil','Kozhevnikov');

INSERT INTO accounts (number, amount, cashback_last_date, bank_id, user_id)
VALUES (3273630263392, 1000, '2023-8-30', 1, 1),
        (4670592953284, 215, '2022-10-11', 2, 2),
        (2789246110014, 284515, '2022-12-12', 3, 3),
        (6628633305868, 211255, '2022-12-12', 4, 4),
        (9105836226649, 7687456215, '2022-12-12', 5, 5),
        (1861003776507, 9215, '2022-12-12', 1, 5),
        (4003141709260, 2185, '2022-12-12', 2, 6),
        (4865067839244, 26184556855, '2022-12-12', 3, 7),
        (9891372557308, 21235, '2022-12-12', 4, 7),
        (9561716442278, 658215, '2022-12-12', 5, 7),
        (7001593396778, 216575, '2022-12-12', 1, 8),
        (2928678764069, 217695, '2022-12-12', 2, 9),
        (7613611018128, 278968515, '2022-12-12', 3, 9),
        (6858521297657, 2145, '2022-12-12', 4, 10),
        (3858667348167, 278315, '2022-12-12', 5, 11),
        (4839230112173, 278915, '2022-12-12', 1, 12),
        (1400775315690, 56756215, '2022-12-12', 2, 13),
        (2430474782969, 212315, '2022-12-12', 3, 13),
        (7626830985605, 26815, '2022-12-12', 4, 14),
        (3083996105067, 215695, '2022-12-12', 5, 15),
        (5725879427085, 78978, '2022-12-12', 1, 16),
        (2351462006176, 2597847698, '2022-12-12', 2, 16),
        (3313944756301, 4567457, '2022-12-12', 3, 17),
        (2626274317290, 2745375, '2022-12-12', 4, 17),
        (8279205062287, 245647457, '2022-12-12', 5, 17),
        (3648683404901, 34534537, '2022-12-12', 1, 17),
        (6168954187257, 45645634738, '2022-12-12', 2, 18),
        (1738510497394, 567436, '2022-12-12', 3, 18),
        (7125939690955, 25484546, '2022-12-12', 4, 19),
        (4190877527186, 256854, '2022-12-12', 5, 19),
        (3418005632802, 22348865, '2022-12-12', 1, 19),
        (5818630019372, 2345374, '2022-12-12', 2, 20),
        (7329408493093, 24363473, '2022-12-12', 3, 20),
        (9900545246192, 65854, '2022-12-12', 4, 6),
        (8793717193252, 2345, '2022-12-12', 5, 15),
        (1027626114273, 25675, '2022-12-12', 1, 14),
        (3317255311359, 2679596, '2022-12-12', 2, 11),
        (9177705677756, 27456, '2022-12-12', 3, 18),
        (5896966298768, 212312, '2022-12-12', 4, 9),
        (5781664667936, 215125, '2022-12-12', 5, 18);

INSERT INTO transactions (amount, date_time, recipient_id, sender_id)
VALUES (100, '2021-01-11 10:01:13', 2, 11),
        (1000, '2022-08-21 11:12:12', 5, 6),
        (12340, '2023-03-05 06:05:15', 15, 10),
        (1000, '2023-08-24 11:11:11', 2, 1),
        (111, '2023-11-01 05:05:11', 3, 15),
        (104321, '2023-12-20 01:11:11', 8, 5);

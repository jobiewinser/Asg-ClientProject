INSERT INTO user (forename, surname, email, phone_number, role, password, disabled, activated)
VALUES
       ('John', 'Smith', 'johns@example.com', '03069990541', 0,'$2a$10$zl6xZnSXDVBU.h87wnVJ1.1qFbz6CU/Rz8fe/LOYVjBQ1W9zMOESm', false, true),
       ('Sally', 'Smith', 'sallys@example.com', '03069990980', 2,'$2a$10$zl6xZnSXDVBU.h87wnVJ1.1qFbz6CU/Rz8fe/LOYVjBQ1W9zMOESm', true, true),
       ('Admin', 'User', 'admin@example.com', '03069990348', 3,'$2a$10$zl6xZnSXDVBU.h87wnVJ1.1qFbz6CU/Rz8fe/LOYVjBQ1W9zMOESm', false, true);

INSERT INTO drone(manufacturer, model)
VALUES
       ('DJI', 'Phantom');
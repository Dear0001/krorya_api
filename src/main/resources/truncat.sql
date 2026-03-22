-- Disable foreign key constraints temporarily
SET session_replication_role = 'replica';

-- Truncate tables and reset ID sequence
TRUNCATE TABLE
    file_tb,
    reciept_tb,
    address_tb,
    code_tb,
    credential_tb,
    device_token_tb,
    token_tb,
    favorite_tb,
    feedback_tb,
    photo_tb,
    food_sell_tb,
    food_recipe_tb,
    category_tb,
    cusine_tb,
    user_tb
    RESTART IDENTITY CASCADE;

-- Re-enable foreign key constraints
SET session_replication_role = 'origin';

-- Confirm cleanup
SELECT 'Database cleanup completed! IDs reset to 1.' AS message;

INSERT INTO cusine_tb (cuisine_name) VALUES
                                         ('ឆា'),
                                         ('អាំង'),
                                         ('បំពង'),
                                         ('ចំហុយ'),
                                         ('បុក'),
                                         ('ញាំ'),
                                         ('ឡុកឡាក់'),
                                         ('បាយ'),
                                         ('បាយឆា'),
                                         ('មី'),
                                         ('នំបញ្ចុក'),
                                         ('នំបញ្ចុកសម្លរ'),
                                         ('ស៊ុប'),
                                         ('ការី'),
                                         ('ប្រហុក'),
                                         ('អំបុក'),
                                         ('ទឹកជ្រលក់'),
                                         ('បង្អែម');

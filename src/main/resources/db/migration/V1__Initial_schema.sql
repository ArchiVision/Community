-- V1__Initial_schema.sql

-- Create the table for Gender enum
CREATE TYPE gender_enum AS ENUM ('MAN', 'WOMAN', 'OTHER', 'ANYONE');

-- Create the table for UserFlowState enum
CREATE TYPE userflowstate_enum AS ENUM (
    'START', 'NAME', 'CITY', 'AGE', 'GENDER', 'LOOKING',
    'TOPIC', 'DESCRIPTION', 'APPROVE', 'MATCH', 'PHOTO', 'SETTINGS'
);

-- Create the table for Subscription enum
CREATE TYPE subscription_enum AS ENUM ('VIP', 'PREMIUM', 'NONE');

-- Create the table for UserType enum
CREATE TYPE usertype_enum AS ENUM ('PERSON', 'UNIT', 'ANYONE');

-- Recreate the users table with the updated schema
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       telegram_user_id BIGINT UNIQUE NOT NULL,
                       name VARCHAR(255),
                       username VARCHAR(255),
                       city VARCHAR(255),
                       age BIGINT,
                       gender gender_enum,
                       looking_for usertype_enum DEFAULT 'ANYONE',
                       description TEXT,
                       photo_id VARCHAR(255),
                       user_flow_state userflowstate_enum DEFAULT 'START',
                       subscription subscription_enum DEFAULT 'NONE',
                       user_type usertype_enum DEFAULT 'ANYONE'
);

-- Create the topics table
CREATE TABLE topics (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) UNIQUE NOT NULL
);

-- Create the user_topic join table for the many-to-many relationship between users and topics
CREATE TABLE user_topic (
                            user_id BIGINT NOT NULL,
                            topic_id BIGINT NOT NULL,
                            PRIMARY KEY (user_id, topic_id),
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                            FOREIGN KEY (topic_id) REFERENCES topics(id) ON DELETE CASCADE
);

-- Create the user_likes table
CREATE TABLE user_likes (
                            id SERIAL PRIMARY KEY,
                            user_id BIGINT NOT NULL,
                            liked_user_id BIGINT NOT NULL,
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                            FOREIGN KEY (liked_user_id) REFERENCES users(id) ON DELETE CASCADE
);

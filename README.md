# Community Bot

## Description

Community Bot is a Telegram bot designed to connect people based on their interests and preferences. Built with Spring Boot and Java, the bot offers a range of features to enhance user interaction and community building.

## Dev Features

- **User Registration**: Allows new users to register and create profiles.
- **Interest Matching**: Matches users based on their selected topics of interest.
- **Real-time Notifications**: Notifies users when they have a match.
- **Profile Viewing**: Allows users to view their matches' profiles.
- **Admin Commands**: Provides admin-level commands for better control.
- **Caching**: Uses custom caching for improved performance.
- **State Management**: Utilizes a state machine pattern for handling different user states.
- **Payment System Integration**: Uses WayForPay payment engine to perform user transactions

## Quick Setup

1. **Clone the Repository**

    ```bash
    git clone https://github.com/ArchiVision/Community.git
    ```

2. **Navigate to the Project Directory**

    ```bash
    cd Community
    ```

3. **Install Dependencies**

    ```bash
    mvn install
    ```

4. **Set Environment Variables**

    Create a `.env` file in the root directory and add the following:

    ```env
    TELEGRAM_BOT_USERNAME=your_bot_username
    TELEGRAM_BOT_TOKEN=your_bot_token
    ```

5. **Run the Application**

    ```bash
    mvn spring-boot:run
    ```

## Usage

- Start the bot by sending `/start` command.
- Follow the on-screen instructions to complete your profile.
- Get matched with other users based on your interests.

## License

This open-source project is available under the [MIT License](LICENSE).

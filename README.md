# Community Bot

## Description

This bot is designed to connect people based on their interests and preferences. Built with Spring Boot and Java, the bot offers a range of features to enhance user interaction and community building.

## Architecture / Working flow
![bot_arch](https://github.com/ArchiVision/Community/assets/72043323/22cd79ea-a5fe-470f-ab84-bd5c6c1bdde3)


## Dev Features

- **User Registration**: Allows new users to register and create profiles.
- **Interest Matching**: Matches users based on their selected topics of interest.
- **Real-time Notifications**: Notifies users when they have a match.
- **Admin Commands**: Provides admin-level commands for better control.
- **Caching**: Uses custom caching for improved performance.
- **State Management**: Utilizes a userFlowState machine pattern for handling different user states.
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

    Create a `.env` file in the root directory by the .env-sample one

5. **Get your IPv4 address for prometheus.yml** <br>
    ```java -jar ip-resolver.jar```

6. **Go to project root directory and run command** <br>
     ```
    ./run-project.sh
    if no acccess -> chmod +x run-project.sh
    try again -> ./run-project.sh
    ```

7. **It should run all containers including monitoring and logging**
<img width="1410" alt="image" src="https://github.com/ArchiVision/Community/assets/72043323/b510b515-aff7-4267-955e-a4534584a184">


## Usage

- Start the bot by sending `/start` command.
- Follow the on-screen instructions to complete your profile.
- Get matched with other users based on your interests.

## Technologies

- Java
- Spring(Boot, Data JPA, Web)
- PostgreSQL
- Redis
- RabbitMQ
- AWS (S3, EC2)
- Maven
- GitHub Actions
- JUnit
- Testcontainers
- Awaitility
- Grafana
- ELK
- Prometheus
- Docker
- Flyway
- Ngrok for building webhooks consumers and test bot without deploying

## License

This open-source project is available under the [MIT License](LICENSE).

# spring-boot-sso

# Docker Compose for Local Development

This Docker Compose configuration sets up a development environment for a web application that uses PostgreSQL as the database.

## Prerequisites

- Docker installed on your machine
- Docker Compose installed on your machine

## Getting Started

1. Clone the repository:

```
git clone https://github.com/siuma1109/spring-boot-sso.git
cd spring-boot-sso
```

2. Build the Docker images:

```
docker-compose build
```

3. Start the Docker containers:

```
docker-compose up
```

This will start the following services:

- `postgres`: A PostgreSQL database container with the necessary environment variables and a custom initialization script.
- `app`: A web application container that connects to the PostgreSQL database.

The `app` service will be available at `http://localhost:8080`.

## Database Management

The PostgreSQL database is configured with the following details:

- **Database Name**: `sso`
- **Username**: `root`
- **Password**: `1234`

You can access the database using a PostgreSQL client tool, such as pgAdmin or psql, by connecting to `localhost:5432`.

## Troubleshooting

If you encounter any issues during the setup or while running the application, you can check the logs of the containers:

```
docker-compose logs -f
```

This will display the combined logs of all the containers in your Docker Compose setup.

## Additional Information

- The `psql.dockerfile` and `app.dockerfile` files define the build instructions for the PostgreSQL and web application containers, respectively.
- The `init-sso-db.sh` script is used to initialize the PostgreSQL database with the necessary tables and data.
- You can customize the environment variables, port mappings, and other configuration details in the `docker-compose.yml` file as needed for your project.
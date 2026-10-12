# URL Shortener

[![CI](https://github.com/harshitham1974/url-shortener/actions/workflows/ci.yml/badge.svg)]
(https://github.com/harshitham1974/url-shortener/actions/workflows/ci.yml)

A REST API that turns long URLs into short links and redirects visitors to the original page. Built with Java and Spring Boot, containerized with Docker, and deployed on Render with an automated GitHub Actions pipeline.

**Live demo:** https://url-shortener-f919.onrender.com/swagger-ui/index.html
**Note:** hosted on Render's free tier, so the first request after a period of inactivity can take about a minute while the service wakes up.

## Features

- Shorten any valid `http://` or `https://` URL into a compact code
- Redirect from the short link to the original URL (HTTP 302)
- Submitting the same URL twice returns the same short code instead of creating a duplicate
- Click count tracked for every short link
- Input validation with clear JSON error messages
- Interactive API documentation with Swagger UI
- 8 automated tests (JUnit 5, Mockito, MockMvc)
- Multi-stage Docker build; runs with PostgreSQL through Docker Compose
- CI/CD: every push to `main` is built, tested, and deployed automatically

## How the short code is generated

1. The URL is saved to the database, which assigns it a unique numeric ID (1, 2, 3...).
2. That ID is converted to Base62 (digits, uppercase, lowercase letters) to make a short code.
3. Because database IDs never repeat, the short codes never collide, so no extra uniqueness checks are needed.

## Tech Stack

| Area | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3 (Web, Data JPA, Validation) |
| Database | PostgreSQL (H2 in memory for local runs) |
| Testing | JUnit 5, Mockito, MockMvc |
| API docs | springdoc-openapi (Swagger UI) |
| Containers | Docker, Docker Compose |
| CI/CD | GitHub Actions, Render |

## API Endpoints

| Method | Path | Description |
|---|---|---|
| POST | `/api/shorten` | Creates a short URL. Body: `{"url": "https://example.com"}` |
| GET | `/r/{code}` | Redirects to the original URL |

Example request body:

```json
{ "url": "https://www.example.com/some/very/long/path" }
```

Example response:

```json
{
  "shortUrl": "https://YOUR-APP-NAME.onrender.com/r/1",
  "originalUrl": "https://www.example.com/some/very/long/path"
}
```

Invalid input (for example `not-a-url`) returns HTTP 400:

```json
{ "url": "URL must start with http:// or https://" }
```

An unknown short code returns HTTP 404:

```json
{ "error": "Short URL not found: xyz" }
```

## Run locally

**Option 1: IntelliJ (no database setup needed)**

1. Clone the repo: `git clone https://github.com/harshitham1974/url-shortener.git`
2. Open it in IntelliJ and wait for Maven to finish loading.
3. Run `UrlShortenerApplication`.
4. Open http://localhost:8080/swagger-ui/index.html

**Option 2: Docker (app + PostgreSQL)**

```bash
docker compose up --build
```

Stop with `docker compose down`.

## Run the tests

```bash
mvn test
```

In IntelliJ: right-click `src/test/java` and choose **Run 'All Tests'**.

## Project structure

```
src/main/java/com/urls/urlshortener/
  controller/   HTTP endpoints
  service/      business logic
  repository/   database access (Spring Data JPA)
  entity/       database table mapping
  dto/          request and response objects
  util/         Base62 encoder
  exception/    global error handling
```

## CI/CD

- `.github/workflows/ci.yml` builds the project, runs all tests, and builds the Docker image on every push and pull request to `main`.
- Render deploys the latest `main` automatically after each push.

## Author

Harshitha M
GitHub: [@harshitham1974](https://github.com/harshitham1974)

## License

MIT, see [LICENSE](LICENSE).

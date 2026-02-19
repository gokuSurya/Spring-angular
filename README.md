# Spring-angular

Spring Boot backend with an embedded Angular frontend project.

## Prerequisites
- Java 17+
- Maven 3.9+
- Node.js 18+ and npm

## Project Structure
- `src/main/java` - Spring Boot application and APIs
- `frontend` - Angular application source
- `src/main/resources/static` - Angular build output (served by Spring Boot)

## Build Angular into Spring static resources
```bash
cd frontend
npm install
npm run build
```

The Angular `outputPath` is configured to:
- `../src/main/resources/static`

So generated frontend files are directly served by Spring Boot.

## CSP + Nonce flow
- Backend generates nonce in `CspNonceFilter` and sends it in response header: `X-CSP-Nonce`.
- Backend also sends `Content-Security-Policy` with `script-src 'nonce-<value>'`.
- SPA HTML is served through `WebController`, which injects nonce into every `<script>` tag.
- Backend validates `X-CSP-Nonce` header for unsafe `/api/**` methods (`POST`, `PUT`, `PATCH`, `DELETE`).

## Run Spring Boot
```bash
mvn spring-boot:run
```

## API Routes (String responses)
- `GET /api` - backend status message
- `GET /api/health` - health string
- `GET /api/products`
- `GET /api/products/{id}`
- `POST /api/products`
- `PUT /api/products/{id}`
- `DELETE /api/products/{id}`

## Frontend Route
- `GET /` serves Angular `index.html` with nonce-injected scripts

### Example create payload
```json
{
  "name": "Keyboard",
  "description": "Mechanical keyboard",
  "price": 89.99
}
```

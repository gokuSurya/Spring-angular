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

So the generated frontend files are directly served by Spring Boot.

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
- `GET /` serves Angular `index.html` from `src/main/resources/static`

### Example create payload
```json
{
  "name": "Keyboard",
  "description": "Mechanical keyboard",
  "price": 89.99
}
```

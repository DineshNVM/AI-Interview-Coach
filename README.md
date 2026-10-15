# AI Interview Coach — InterviewIQ

A full-stack **Java 21 / Spring Boot / React TypeScript / PostgreSQL** mock interview project. Built from the supplied AI Interview Coach specification; this version is a functional-oriented **developer prototype**, not a certified production deployment.

## Features implemented in source
- Register/login/logout, bcrypt password hashes, hashed random bearer tokens expiring after 12 hours, per-user record filtering.
- Persisted dashboards and interview history; no invented session scores.
- PDF and DOCX resume text extraction and file storage outside the frontend, with OpenAI resume analysis.
- Resume-and-role-aware question generation, one question at a time, saving answers and generating follow-up questions from actual previous responses.
- Live speech synthesis and browser speech recognition where supported, with text fallback.
- AI performance evaluation and question feedback persisted as JSON in PostgreSQL.
- Interview proctoring consent, camera preview, fullscreen/tab/focus events, optional screen-share status, backend event log, integrity reports.
- Curated official learning resources and progress tracking.
- Swagger OpenAPI, Flyway schema, Docker Compose PostgreSQL, responsive user interface.

## Known limitations (important)
This build has **not** been verified by a passing Maven build or live end-to-end test in the authoring environment, so treat it as a starting codebase requiring integration testing. The browser's face detection is **not implemented**; strict proctoring only verifies the presence of a camera stream, not face count or identity. There is no administrator review UI, automated session termination/escalation, file deletion portal, continuous video recording, or production-grade abuse rate limiter. Browser focus/visibility checks are signals, not proof of cheating. The learning list is curated but is not dynamically tailored to AI assessment output. Score calculation comes from validated AI summary scores rather than an independently weighted deterministic rubric. Resuming failed AI follow-up requests may require manual recovery: a saved answer can precede question generation. Browser microphone recognition availability varies. Camera preview and screen-share status do not store video frames. Missing OpenAI credentials result in explicit errors, not simulated scores. Do not deploy to real job candidates without a threat-model review, privacy/retention work, accessible alternatives, tests, and human review.

## Prerequisites
- Java JDK 21 (check `java -version`)
- Apache Maven 3.9+ (check `mvn -version`)
- Node.js 20+ (check `node -v`)
- Docker Desktop for PostgreSQL or installed PostgreSQL 16+
- OpenAI API key, for live interviews and analyses

## Windows PowerShell startup

In the extracted root project directory:

```powershell
# 1. Start database
 docker compose up -d

# 2. Configure backend for development; replace YOUR_KEY with your own key
$env:DB_URL='jdbc:postgresql://localhost:5432/interview_coach'
$env:DB_USER='coach'
$env:DB_PASSWORD='coach_dev'
$env:OPENAI_API_KEY='YOUR_KEY'
$env:OPENAI_MODEL='gpt-4.1-mini'
$env:CORS_ORIGIN='http://localhost:5173'

# 3. Start backend (in terminal A)
cd backend
mvn spring-boot:run

# 4. Start frontend (in terminal B, from root)
cd frontend
npm install
npm run dev
```

Open **http://localhost:5173**. API health check: **http://localhost:8080/actuator/health**; API docs: **http://localhost:8080/swagger-ui/index.html**.

Do **not** put your OpenAI key in `frontend/.env`; only Java server uses it. PostgreSQL credentials in Docker Compose are development defaults, not appropriate for production.

## Basic usage
1. Create an account and log in.
2. Upload a real PDF/DOCX resume; select **Analyze with AI**.
3. Open New interview; select role, type, difficulty, text/voice mode, number of questions, monitoring mode.
4. For Strict mode acknowledge notice, allow camera, and enter fullscreen. (Face detection is not implemented.)
5. Answer each question. A follow-up is generated from the saved turns.
6. After the final question, generate report; inspect performance and browser integrity separately.
7. Review history, verified learning resource links, and score trajectory.

## API summary
`POST /api/auth/register`, `/login`, `/logout` · `GET /api/dashboard`, `PUT /api/profile` · `GET /api/resumes/latest`, `POST /api/resumes` (multipart field `file`), `POST /api/resumes/analyze?role=...` · `GET/POST /api/interviews`, `GET /api/interviews/{id}`, `POST /api/interviews/{id}/answer`, `/finish`, `/events`, `GET /api/interviews/{id}/integrity` · `GET /api/resources`, `PUT /api/resources/{resourceId}`.

Protected endpoints require `Authorization: Bearer <token>`. Tokens are stored as hashes in the DB, not in plaintext. Frontend stores the issued token in sessionStorage; production systems should consider Secure HttpOnly cookies and CSRF protections.

## Testing and build commands
```powershell
cd backend
mvn clean test
mvn package
cd ..\frontend
npm ci
npm run build
```

Browser hardware permission prompts require manual review. `http://localhost` counts as a secure context for supported getUserMedia/Fullscreen APIs. For deployments, use HTTPS. Browser speech input is most commonly available in Chromium-based browsers and may rely on browser speech services.

## Architecture
- `backend/src/main/java/com/interviewcoach/model`: JPA entities.
- `backend/.../repo`: Spring Data repositories.
- `backend/.../api`: authenticated REST controllers.
- `backend/.../service/AiService.java`: OpenAI HTTP integration and JSON parsing.
- `backend/.../config/SecurityConfig.java`: stateless token filter, authorization, CORS.
- `backend/src/main/resources/db/migration`: Flyway SQL.
- `frontend/src/main.tsx`: React screens, voice and permission flows.
- `frontend/src/api.ts`: typed fetch wrapper.
- `frontend/src/style.css`: responsive styling.

## Browser monitoring caveats
The website can observe its own focus/fullscreen/visibility and permissioned streams, not monitor the entire computer or block external devices. It neither secretly records media nor proves user misconduct. Users should be offered an accessible non-proctored alternative. In production add a data retention policy, delete/export controls, tamper-resistant logging, session-review permissions and detailed consent handling.

## Troubleshooting
- `mvn` not recognized: install Maven, add its `bin` directory to PATH and reopen PowerShell.
- `Connection refused`: ensure `docker compose ps` shows postgres running and port 5432 is free.
- CORS error: check `CORS_ORIGIN` equals your exact frontend origin.
- AI 503: set `OPENAI_API_KEY` on backend. AI 502: check API connectivity, key quota and configured model.
- Microphone or camera blocked: use browser site settings and HTTPS (or localhost).
- Browser speech unavailable: answer by typing instead.
- Frontend can't reach backend: check backend is running on port 8080 and the API URL.

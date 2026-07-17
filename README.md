# Notizen App (Fullstack)

Eine Fullstack-Webanwendung zum Erstellen und Verwalten von Notizen mit Benutzer-Authentifizierung.

---

## Features

- JWT-basierte Authentifizierung (Login & Registrierung)
- Multi-User System – jeder Nutzer sieht ausschließlich eigene Notizen
- Notizen erstellen und löschen
- Geschützte API-Endpunkte via Spring Security
- REST API für Frontend-Kommunikation

---

## Tech Stack

**Backend**
- Java, Spring Boot, Spring Security
- JWT (JSON Web Token)
- PostgreSQL, JPA / Hibernate

**Frontend**
- React, Vite
- Axios, Tailwind CSS

---

## Projektstruktur

```
notev2/
├── notev2-backend/
│   ├── src/
│   └── pom.xml
├── notev2-frontend/
│   ├── src/
│   └── package.json
├── load-test.js
└── README.md
```

---

## Setup (lokal)

**1. Backend starten**

```bash
cd notev2-backend
$env:JWT_SECRET="your_secret_here"  
./mvnw spring-boot:run
```

**2. Frontend starten**

```bash
cd notev2-frontend
npm install
npm run dev
```

**Environment Variablen**

Erstelle eine `.env` Datei im Backend-Ordner (nicht committen):

```
JWT_SECRET=your_super_secret_key **mindenstens 32bit**
```

---

## API Endpoints

**Auth**

| Methode | Endpoint         | Beschreibung              |
|---------|------------------|---------------------------|
| POST    | /auth/register   | Benutzer registrieren     |
| POST    | /auth/login      | Login + JWT erhalten      |
| GET     | /auth/me         | Aktueller Benutzer        |

**Notes**

| Methode | Endpoint         | Beschreibung              |
|---------|------------------|---------------------------|
| GET     | /notes           | Alle Notizen des Nutzers  |
| POST    | /notes           | Neue Notiz erstellen      |
| DELETE  | /notes/{id}      | Notiz löschen             |

---

## Sicherheit

- Passwörter werden mit BCrypt gehasht
- JWT wird für Authentifizierung verwendet
- Geschützte Routen via Spring Security
- Secrets werden über Environment Variablen verwaltet

---

## Performance Testing

Die REST API wurde lokal mit **Grafana k6** unter Last getestet.

### Testumgebung

- Backend: Spring Boot 3
- Datenbank: PostgreSQL (Docker)
- Testtool: Grafana k6
- Testsystem: Lokaler Entwicklungsrechner

### Ergebnisse

| Metrik | 100 Virtual Users | 500 Virtual Users |
|--------|------------------:|------------------:|
| Dauer | 30 s | 30 s |
| Requests | 109.213 | 101.353 |
| Requests/s | 3.638 | 3.365 |
| Durchschnittliche Antwortszeit | 27,38 ms | 146,98 ms |
| p95 Antwortszeit | 83,56 ms | 331,69 ms |
| Maximale Antwortszeit | 282 ms | 1,43 s |
| Fehlerrate | **0 %** | **0,64 %** |

### Beobachtungen

- Die Anwendung blieb bei **100 gleichzeitigen Benutzern** vollständig stabil.
- Auch bei **500 gleichzeitigen Benutzern** blieb die API größtenteils stabil und verarbeitete weiterhin über **3.300 Requests pro Sekunde**.
- Unter hoher Last stieg die CPU-Auslastung des Spring-Boot-Containers auf etwa **450 %** (≈ 4,5 CPU-Kerne) und die PostgreSQL-Datenbank auf etwa **100 %** (≈ 1 CPU-Kern).
- Bei maximaler Last traten vereinzelt Verbindungsfehler auf (0,64 % Fehlerrate), was auf die Auslastungsgrenze des lokalen Testsystems hinweist.

### Test ausführen

```bash
k6 run load-test.js
```
---

## Deployment

- Backend: Render / Railway
- Frontend: Vercel

---

## Geplante Erweiterungen

- Notizen bearbeiten
- Dark Mode
- Responsives Design verbessern
- CI/CD Pipeline mit GitLab

---

## Autor

Vithujan Suthahar

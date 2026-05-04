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

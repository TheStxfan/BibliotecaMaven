# 📚 Biblioteca Scolastica - Applicazione REST API + Frontend Web

Una moderna applicazione per la gestione di una biblioteca scolastica con backend REST API e frontend web responsive.

---

## 🎯 Cosa è Stato Modificato

### Backend
1. ✅ **Aggiunto HttpServer nativo Java** - Espone REST API sulla porta 8080
2. ✅ **Classe HttpServerApi.java** - Gestisce tutti gli endpoint REST
3. ✅ **Dipendenza GSON** - Per serializzazione/deserializzazione JSON
4. ✅ **Metodi nei DAO** - Aggiunti `getAllUtenti()`, `getAllLibri()`, `getAllPrestiti()`
5. ✅ **Main.java modificato** - Avvia il server API invece della console

### Frontend
1. ✅ **index.html** - Interfaccia web moderna e intuitiva
2. ✅ **style.css** - Styling professionale con CSS Grid e Flexbox
3. ✅ **app.js** - Logica JavaScript che comunica con le API

### Configurazione
1. ✅ **pom.xml aggiornato** - Include GSON e maven-shade-plugin
2. ✅ **Fat JAR** - Tutto incluso in un singolo file JAR

---

## 🚀 Quick Start

### 1. Compilare
```bash
cd C:\Users\Utente\Downloads\BibliotecaMaven
mvn clean package
```

### 2. Eseguire
```bash
java -jar target/BibliotecaMaven-0.0.1-SNAPSHOT.jar
```

### 3. Accedere
```
http://localhost:8080
```

---

## 📊 Struttura Applicazione

```
Frontend (http://localhost:8080)
    ├── 👥 Sezione Utenti
    │   ├── Registra Studente
    │   ├── Registra Docente
    │   └── Elenco Utenti
    │
    ├── 📖 Sezione Libri
    │   ├── Aggiungi Libro
    │   └── Catalogo Libri
    │
    └── 📋 Sezione Prestiti
        ├── Crea Prestito
        ├── Restituisci Libro
        └── Prestiti Attivi

Backend (REST API)
    ├── /api/utenti
    │   ├── GET - Lista utenti
    │   └── POST - Registra utente
    │
    ├── /api/libri
    │   ├── GET - Lista libri
    │   └── POST - Aggiungi libro
    │
    └── /api/prestiti
        ├── GET - Lista prestiti
        └── POST - Crea/Restituisci prestito
```

---

## 🏗️ Architettura

### Antes (Console)
```
Main.java → BibliotecaService → DAO → MySQL
```

### Dopo (REST API + Web)
```
index.html (Frontend)
    ↓
app.js (JavaScript)
    ↓
REST API (HttpServerApi)
    ↓
BibliotecaService
    ↓
DAO Layer
    ↓
MySQL Database
```

---

## ✨ Caratteristiche Frontend

- 🎨 **Design Moderno** - Interfaccia intuitiva e professionale
- 📱 **Responsive** - Funziona su desktop e mobile
- ⚡ **Real-time Validation** - Validazione lato client
- 🔄 **Auto-refresh** - Carica automaticamente i dati
- 💬 **Messaggi Feedback** - Conferme di successo/errore
- 🎯 **Tab Navigation** - Facile navigazione tra sezioni

---

## 🔐 Sicurezza Note

Per un ambiente di produzione, considera:
- [ ] Aggiungere autenticazione JWT
- [ ] Implementare HTTPS/SSL
- [ ] Aggiungere CORS restrictions
- [ ] Rate limiting
- [ ] Validazione input lato server avanzata
- [ ] Logging e monitoring

---

## 📦 File Modificati/Creati

### Modificati
- `pom.xml` - Aggiunti GSON
- `src/main/java/com/biblioteca/Main.java` - Modificato per avviare server
- `src/main/java/com/biblioteca/dao/UtenteDAO.java` - Aggiunto `getAllUtenti()`
- `src/main/java/com/biblioteca/dao/LibroDAO.java` - Aggiunto `getAllLibri()`
- `src/main/java/com/biblioteca/dao/PrestitoDAO.java` - Aggiunto `getAllPrestiti()`

### Creati
- `src/main/java/com/biblioteca/api/HttpServerApi.java` - Nuovo REST API server
- `src/main/resources/index.html` - Frontend HTML
- `src/main/resources/style.css` - Styling
- `src/main/resources/app.js` - Logica JavaScript
- `DEPLOYMENT_GUIDE.md` - Guida per NSSM
- `README.md` - Questo file

---

## 🪟 Deploy su Windows con NSSM

Vedi `DEPLOYMENT_GUIDE.md` per:
- Installazione come servizio Windows
- Configurazione auto-start
- Troubleshooting
- Comandi utili

---

## 📝 Esempio di Utilizzo API

### Registrare uno Studente
```bash
curl -X POST http://localhost:8080/api/utenti \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "STUDENTE",
    "nome": "Marco",
    "cognome": "Rossi",
    "email": "marco.rossi@scuola.it",
    "classe": "5A"
  }'
```

### Aggiungere un Libro
```bash
curl -X POST http://localhost:8080/api/libri \
  -H "Content-Type: application/json" \
  -d '{
    "titolo": "Il Signore degli Anelli",
    "autore": "J.R.R. Tolkien",
    "annoPubblicazione": 1954
  }'
```

### Creare un Prestito
```bash
curl -X POST http://localhost:8080/api/prestiti \
  -H "Content-Type: application/json" \
  -d '{
    "azione": "CREA",
    "idUtente": 1,
    "idLibro": 1
  }'
```

---

## 🐛 Troubleshooting

### La porta 8080 è occupata?
Modifica `HttpServerApi.java` linea ~27:
```java
private static final int PORT = 8080; // Cambia qui
```

### Database non trovato?
Verifica credenziali in `DatabaseConnection.java`

### Frontend non carica?
- Verifica che il JAR sia compilato correttamente
- Controlla che le risorse siano in `src/main/resources`
- Vedi il file di log di Maven

---

## 🔄 Workflow di Sviluppo Futuro

1. **Aggiungere Autenticazione** - Login per utenti
2. **Dashboard Statistiche** - Grafici uso biblioteca
3. **Email Notifications** - Reminder per scadenze prestiti
4. **Mobile App** - Versione native con React Native
5. **Admin Panel** - Gestione amministrativa avanzata

---

## 📚 Tecnologie Utilizzate

- **Backend**: Java 8, HttpServer nativa
- **Database**: MySQL
- **Frontend**: HTML5, CSS3, Vanilla JavaScript
- **Build**: Maven
- **Deployment**: NSSM (Windows Service)

---

## 📄 Licenza

Questo progetto è fornito come-è per uso educativo.

---

## 💡 Support

Per problemi:
1. Leggi `DEPLOYMENT_GUIDE.md`
2. Controlla i log di Windows Event Viewer
3. Verifica che MySQL sia in esecuzione
4. Controlla la console di output

---

**🎉 Buona fortuna con la tua biblioteca digitale!**

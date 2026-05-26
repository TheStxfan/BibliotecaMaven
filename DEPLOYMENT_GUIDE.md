# 🚀 Guida di Deployment - Biblioteca Scolastica

## Panoramica
Il backend è ora un server REST API che funziona su HTTP, con un frontend web integrato. È pronto per essere eseguito come servizio Windows con NSSM.

---

## ✅ FASE 1: Compilazione

### Prerequisiti
- **Java JDK 8+** installato
- **Maven** installato e configurato
- **MySQL** server in esecuzione con il database configurato

### Compilare il progetto
```bash
cd C:\Users\Utente\Downloads\BibliotecaMaven
mvn clean package
```

Questo creerà un JAR con tutte le dipendenze in:
```
target\BibliotecaMaven-0.0.1-SNAPSHOT.jar
```

---

## ▶️ FASE 2: Test (Esecuzione Manuale)

### Eseguire l'applicazione
```bash
java -jar target\BibliotecaMaven-0.0.1-SNAPSHOT.jar
```

### Output atteso
```
===== GESTIONE BIBLIOTECA SCOLASTICA =====

✓ Server API avviato su http://localhost:8080
✓ Accedi al frontend su http://localhost:8080
```

### Accedere all'interfaccia
Apri il browser e vai a:
```
http://localhost:8080
```

---

## 🪟 FASE 3: Configurazione come Servizio Windows con NSSM

### Prerequisiti
- **NSSM** (Non-Sucking Service Manager) installato
- Scarica da: https://nssm.cc/download (Extract l'archivio)

### Step 1: Preparare il percorso del JAR
Il JAR si trova in:
```
C:\Users\Utente\Downloads\BibliotecaMaven\target\BibliotecaMaven-0.0.1-SNAPSHOT.jar
```

### Step 2: Installare il servizio
Apri PowerShell **come Amministratore** e esegui:

```powershell
# Naviga nella cartella NSSM (adatta il percorso se necessario)
cd "C:\path\to\nssm\win64"

# Installa il servizio
.\nssm install BibliotecaService "C:\Program Files\Java\jdk1.8.0_XXX\bin\java.exe" "-jar C:\Users\Utente\Downloads\BibliotecaMaven\target\BibliotecaMaven-0.0.1-SNAPSHOT.jar"
```

**Nota:** Sostituisci `C:\Program Files\Java\jdk1.8.0_XXX\bin\java.exe` con il percorso reale della tua JVM.

### Step 3: Configurare il servizio (Opzionale ma consigliato)

Per configurare il comportamento del servizio:
```powershell
# Avvia la GUI di configurazione
.\nssm edit BibliotecaService
```

Suggerimenti di configurazione:
- **Restart on exit:** Sì (per auto-restart se crasha)
- **Startup type:** Automatic
- **Dependencies:** (opzionale) MySQL Service

### Step 4: Avviare il servizio
```powershell
# Avvia il servizio
Start-Service BibliotecaService

# Verifica lo stato
Get-Service BibliotecaService
```

### Step 5: Verificare l'esecuzione
```powershell
# Controlla i log
.\nssm query BibliotecaService

# Visualizza il file di log (se configurato)
.\nssm get BibliotecaService AppParameters
```

---

## 🧹 Comandi Utili NSSM

### Fermare il servizio
```powershell
Stop-Service BibliotecaService
```

### Riavviare il servizio
```powershell
Restart-Service BibliotecaService
```

### Disinstallare il servizio
```powershell
# Da PowerShell come Admin
cd "C:\path\to\nssm\win64"
.\nssm remove BibliotecaService confirm
```

### Visualizzare log
```powershell
# Windows Event Viewer
eventvwr.msc

# O leggi i log NSSM direttamente (se configurati)
```

---

## 🌐 Accesso all'Applicazione

Una volta che il servizio è in esecuzione:

1. **Frontend Web:** `http://localhost:8080`
2. **API REST:** `http://localhost:8080/api`
3. **Gestione servizio:** Services (services.msc)

---

## 📋 Endpoints API Disponibili

### Utenti
- `GET /api/utenti` - Lista tutti gli utenti
- `POST /api/utenti` - Registra nuovo utente (Studente/Docente)

### Libri
- `GET /api/libri` - Lista tutti i libri
- `POST /api/libri` - Aggiungi nuovo libro

### Prestiti
- `GET /api/prestiti` - Lista prestiti attivi
- `POST /api/prestiti` - Crea/Restituisci prestito

---

## ⚙️ Troubleshooting

### Il servizio non parte
1. Verifica che **Java** sia installato e in PATH:
   ```powershell
   java -version
   ```

2. Verifica il percorso del JAR esiste:
   ```powershell
   Test-Path "C:\Users\Utente\Downloads\BibliotecaMaven\target\BibliotecaMaven-0.0.1-SNAPSHOT.jar"
   ```

3. Controlla i log di NSSM in Event Viewer

### Il database non è raggiungibile
1. Verifica che MySQL sia in esecuzione
2. Controlla le credenziali in `DatabaseConnection.java`
3. Verifica che il database e le tabelle siano create

### La porta 8080 è occupata
```powershell
netstat -ano | findstr :8080
```

Se occupata, modifica il file `HttpServerApi.java` (linea `private static final int PORT = 8080;`) e ricompila.

---

## 📝 Note Importanti

- Il JAR include sia il **backend REST API** che il **frontend web**
- Il servizio si avvierà **automaticamente** all'accensione del PC
- È consigliato **configurare backup** del database MySQL
- Per log dettagliati, configura NSSM per scrivere su file

---

## 🎯 Checklist Finale

- [ ] Java JDK 8+ installato
- [ ] Maven installato
- [ ] MySQL in esecuzione con database configurato
- [ ] Progetto compilato con `mvn clean package`
- [ ] JAR generato in `target\`
- [ ] NSSM installato
- [ ] Servizio installato con NSSM
- [ ] Servizio avviato e in esecuzione
- [ ] Frontend raggiungibile su http://localhost:8080
- [ ] API funzionanti (prova registrare un utente)

---

Fatto! 🎉 La tua applicazione è pronta per essere eseguita come servizio Windows professionale!

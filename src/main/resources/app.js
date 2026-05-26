const API_BASE = 'http://localhost:8080/api';

// ==================== TAB MANAGEMENT ====================
document.querySelectorAll('.tab-button').forEach(button => {
    button.addEventListener('click', () => {
        const tabName = button.getAttribute('data-tab');
        switchTab(tabName);
    });
});

function switchTab(tabName) {
    // Nascondi tutti i tab
    document.querySelectorAll('.tab-content').forEach(tab => {
        tab.classList.remove('active');
    });
    
    // Mostra il tab selezionato
    document.getElementById(tabName).classList.add('active');
    
    // Aggiorna bottoni
    document.querySelectorAll('.tab-button').forEach(button => {
        button.classList.remove('active');
        if (button.getAttribute('data-tab') === tabName) {
            button.classList.add('active');
        }
    });
    
    // Carica i dati
    if (tabName === 'utenti') {
        loadUtenti();
    } else if (tabName === 'libri') {
        loadLibri();
    } else if (tabName === 'prestiti') {
        loadPrestiti();
    }
}

// ==================== MESSAGES ====================
function showMessage(message, type = 'info') {
    const messageEl = document.getElementById('message');
    messageEl.textContent = message;
    messageEl.className = `message ${type}`;
    messageEl.classList.remove('hidden');
    
    setTimeout(() => {
        messageEl.classList.add('hidden');
    }, 4000);
}

// ==================== UTENTI ====================
function updateUserForm() {
    const userType = document.getElementById('userType').value;
    const classeGroup = document.getElementById('classeGroup');
    const materiaGroup = document.getElementById('materiaGroup');
    
    if (userType === 'STUDENTE') {
        classeGroup.style.display = 'block';
        materiaGroup.style.display = 'none';
    } else {
        classeGroup.style.display = 'none';
        materiaGroup.style.display = 'block';
    }
}

function registraUtente() {
    const tipo = document.getElementById('userType').value;
    const nome = document.getElementById('userName').value.trim();
    const cognome = document.getElementById('userSurname').value.trim();
    const email = document.getElementById('userEmail').value.trim();
    
    if (!nome || !cognome || !email) {
        showMessage('Compila tutti i campi obbligatori', 'error');
        return;
    }
    
    let body = {
        tipo: tipo,
        nome: nome,
        cognome: cognome,
        email: email
    };
    
    if (tipo === 'STUDENTE') {
        const classe = document.getElementById('userClasse').value.trim();
        if (!classe) {
            showMessage('Inserisci la classe', 'error');
            return;
        }
        body.classe = classe;
    } else {
        const materia = document.getElementById('userMateria').value.trim();
        if (!materia) {
            showMessage('Inserisci la materia', 'error');
            return;
        }
        body.materia = materia;
    }
    
    fetch(`${API_BASE}/utenti`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showMessage(data.message, 'success');
            // Pulisci form
            document.getElementById('userName').value = '';
            document.getElementById('userSurname').value = '';
            document.getElementById('userEmail').value = '';
            document.getElementById('userClasse').value = '';
            document.getElementById('userMateria').value = '';
            // Ricarica lista
            loadUtenti();
        } else {
            showMessage(data.error, 'error');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showMessage('Errore nella registrazione utente', 'error');
    });
}

function loadUtenti() {
    fetch(`${API_BASE}/utenti`)
        .then(response => response.json())
        .then(data => {
            const listEl = document.getElementById('utentiList');
            
            if (!Array.isArray(data) || data.length === 0) {
                listEl.innerHTML = '<div class="empty-message">Nessun utente registrato</div>';
                return;
            }
            
            listEl.innerHTML = data.map((utente, index) => {
                const tipo = utente.tipo || utente.tipoUtente || 'SCONOSCIUTO';
                const extraInfo = tipo === 'STUDENTE' 
                    ? `<p>📚 Classe: <strong>${utente.classe}</strong></p>`
                    : `<p>✏️ Materia: <strong>${utente.materia}</strong></p>`;
                
                const prestitiAttivi = utente.prestitiAttivi || 0;
                const prestitiMassimi = utente.prestitiMassimi || 0;
                
                return `
                    <div class="user-card">
                        <div class="card-header">
                            <div class="card-title">${utente.nome} ${utente.cognome}</div>
                            <div class="card-badge">${tipo}</div>
                        </div>
                        <div class="card-info">
                            <p>📧 Email: ${utente.email}</p>
                            ${extraInfo}
                            <p>📋 Prestiti: <strong>${prestitiAttivi}/${prestitiMassimi}</strong></p>
                            <p>🆔 ID: ${utente.idUtente}</p>
                        </div>
                    </div>
                `;
            }).join('');
        })
        .catch(error => {
            console.error('Error:', error);
            showMessage('Errore nel caricamento utenti', 'error');
        });
}

// ==================== LIBRI ====================
function registraLibro() {
    const titolo = document.getElementById('bookTitle').value.trim();
    const autore = document.getElementById('bookAuthor').value.trim();
    const annoPubblicazione = parseInt(document.getElementById('bookYear').value);
    
    if (!titolo || !autore || !annoPubblicazione) {
        showMessage('Compila tutti i campi obbligatori', 'error');
        return;
    }
    
    const anno = new Date().getFullYear();
    if (annoPubblicazione < 0 || annoPubblicazione > anno) {
        showMessage('Anno di pubblicazione non valido', 'error');
        return;
    }
    
    fetch(`${API_BASE}/libri`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            titolo: titolo,
            autore: autore,
            annoPubblicazione: annoPubblicazione
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showMessage(data.message, 'success');
            document.getElementById('bookTitle').value = '';
            document.getElementById('bookAuthor').value = '';
            document.getElementById('bookYear').value = '';
            loadLibri();
        } else {
            showMessage(data.error, 'error');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showMessage('Errore nella registrazione libro', 'error');
    });
}

function loadLibri() {
    fetch(`${API_BASE}/libri`)
        .then(response => response.json())
        .then(data => {
            const listEl = document.getElementById('libriList');
            
            if (!Array.isArray(data) || data.length === 0) {
                listEl.innerHTML = '<div class="empty-message">Nessun libro nel catalogo</div>';
                return;
            }
            
            listEl.innerHTML = data.map((libro) => {
                const badge = libro.disponibile 
                    ? '<div class="card-badge available">✓ Disponibile</div>'
                    : '<div class="card-badge rented">✗ In Prestito</div>';
                
                return `
                    <div class="book-card">
                        <div class="card-header">
                            <div class="card-title">${libro.titolo}</div>
                            ${badge}
                        </div>
                        <div class="card-info">
                            <p>✍️ Autore: <strong>${libro.autore}</strong></p>
                            <p>📅 Anno: <strong>${libro.annoPubblicazione}</strong></p>
                            <p>🆔 ID: ${libro.idLibro}</p>
                        </div>
                    </div>
                `;
            }).join('');
        })
        .catch(error => {
            console.error('Error:', error);
            showMessage('Errore nel caricamento libri', 'error');
        });
}

// ==================== PRESTITI ====================
function creaPrestito() {
    const idUtente = parseInt(document.getElementById('prestito-idUtente').value);
    const idLibro = parseInt(document.getElementById('prestito-idLibro').value);
    
    if (!idUtente || !idLibro) {
        showMessage('Inserisci ID utente e ID libro', 'error');
        return;
    }
    
    fetch(`${API_BASE}/prestiti`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            azione: 'CREA',
            idUtente: idUtente,
            idLibro: idLibro
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showMessage(data.message, 'success');
            document.getElementById('prestito-idUtente').value = '';
            document.getElementById('prestito-idLibro').value = '';
            loadPrestiti();
            // Ricarica anche i libri per aggiornare la disponibilità
            if (document.getElementById('libri').classList.contains('active')) {
                loadLibri();
            }
        } else {
            showMessage(data.error, 'error');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showMessage('Errore nella creazione prestito', 'error');
    });
}

function restituisciLibro(idPrestito) {
    if (!confirm('Sei sicuro di voler restituire il libro?')) {
        return;
    }
    
    fetch(`${API_BASE}/prestiti`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            azione: 'RESTITUISCI',
            idPrestito: idPrestito
        })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showMessage(data.message, 'success');
            loadPrestiti();
            // Ricarica anche i libri per aggiornare la disponibilità
            if (document.getElementById('libri').classList.contains('active')) {
                loadLibri();
            }
        } else {
            showMessage(data.error, 'error');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showMessage('Errore nella restituzione del libro', 'error');
    });
}

function loadPrestiti() {
    fetch(`${API_BASE}/prestiti`)
        .then(response => response.json())
        .then(data => {
            const listEl = document.getElementById('prestitiList');
            
            if (!Array.isArray(data) || data.length === 0) {
                listEl.innerHTML = '<div class="empty-message">Nessun prestito attivo</div>';
                return;
            }
            
            listEl.innerHTML = data.map((prestito) => {
                const dataP = new Date(prestito.dataPrestito).toLocaleDateString('it-IT');
                const dataR = prestito.dataRestituzione 
                    ? new Date(prestito.dataRestituzione).toLocaleDateString('it-IT')
                    : 'Non restituito';
                
                let statoColor = 'ATTIVO' === prestito.stato 
                    ? '<div class="card-badge available">ATTIVO</div>'
                    : '<div class="card-badge rented">RESTITUITO</div>';
                
                return `
                    <div class="loan-card">
                        <div class="card-header">
                            <div class="card-title">${prestito.libro?.titolo || 'Libro sconosciuto'}</div>
                            ${statoColor}
                        </div>
                        <div class="card-info">
                            <p>👤 Utente: <strong>${prestito.utente?.nome} ${prestito.utente?.cognome}</strong></p>
                            <p>📖 Libro: <strong>${prestito.libro?.autore || 'Autore sconosciuto'}</strong></p>
                            <p>📅 Prestito dal: <strong>${dataP}</strong></p>
                            <p>📅 Restituzione: <strong>${dataR}</strong></p>
                            <p>🆔 ID Prestito: ${prestito.idPrestito}</p>
                        </div>
                        <div class="card-actions">
                            ${prestito.stato === 'ATTIVO' 
                                ? `<button class="btn-success" onclick="restituisciLibro(${prestito.idPrestito})">✓ Restituisci</button>`
                                : ''}
                        </div>
                    </div>
                `;
            }).join('');
        })
        .catch(error => {
            console.error('Error:', error);
            showMessage('Errore nel caricamento prestiti', 'error');
        });
}

// ==================== INIT ====================
document.addEventListener('DOMContentLoaded', () => {
    updateUserForm();
    loadUtenti();
});

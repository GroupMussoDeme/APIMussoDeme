package com.mussodeme.MussoDeme.enums;

public enum StatutAppel {
    EN_COURS,      // Appel en cours d'émission
    TERMINE,       // Appel terminé avec succès
    MANQUE,        // Appel manqué (non répondu)
    REFUSE,        // Appel refusé
    ANNULE,        // Appel annulé par l'appelant
    MESSAGE_VOCALE // Message vocal laissé
}
package com.mussodeme.MussoDeme;

import com.mussodeme.MussoDeme.entities.FemmeRurale;
import com.mussodeme.MussoDeme.services.SMSService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SMSServiceTest {

    @Autowired
    private SMSService smsService;

    @Test
    public void testEnvoiSMSSimple() {
        // Créer un utilisateur de test
        FemmeRurale femmeTest = new FemmeRurale();
        femmeTest.setNom("Test User");
        femmeTest.setNumeroTel("+223XXXXXXXX"); // Remplacer par un vrai numéro
        
        // Envoyer un SMS de test
        smsService.envoyerSMS(femmeTest, "Bienvenue dans MussoDeme! Test réussi.");
        
        System.out.println("SMS de test envoyé avec succès!");
    }
}
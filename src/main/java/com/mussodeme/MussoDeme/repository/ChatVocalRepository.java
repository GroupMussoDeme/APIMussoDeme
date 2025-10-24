package com.mussodeme.MussoDeme.repository;

import com.mussodeme.MussoDeme.entities.ChatVocal;
import com.mussodeme.MussoDeme.entities.Coperative;
import com.mussodeme.MussoDeme.entities.FemmeRurale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatVocalRepository extends JpaRepository<ChatVocal, Long> {

    /**
     * Find all voice messages between two users (private chat)
     */
    @Query("SELECT c FROM ChatVocal c WHERE " +
           "(c.expediteur.id = :user1Id AND c.destinataire.id = :user2Id) OR " +
           "(c.expediteur.id = :user2Id AND c.destinataire.id = :user1Id) " +
           "ORDER BY c.dateEnvoi DESC")
    List<ChatVocal> findMessagesBetweenUsers(@Param("user1Id") Long user1Id, 
                                            @Param("user2Id") Long user2Id);

    /**
     * Find all voice messages in a cooperative
     */
    List<ChatVocal> findByCoperativeId(Long coperativeId);

    /**
     * Find all unread messages for a user (private chats)
     */
    @Query("SELECT c FROM ChatVocal c WHERE c.destinataire.id = :femmeId AND c.lu = false ORDER BY c.dateEnvoi DESC")
    List<ChatVocal> findUnreadMessagesForUser(@Param("femmeId") Long femmeId);

    /**
     * Find all messages sent by a user
     */
    @Query("SELECT c FROM ChatVocal c WHERE c.expediteur.id = :femmeId ORDER BY c.dateEnvoi DESC")
    List<ChatVocal> findByExpediteur(@Param("femmeId") Long femmeId);

    /**
     * Find all messages received by a user (private only)
     */
    @Query("SELECT c FROM ChatVocal c WHERE c.destinataire.id = :femmeId ORDER BY c.dateEnvoi DESC")
    List<ChatVocal> findByDestinataire(@Param("femmeId") Long femmeId);

    /**
     * Count unread messages for a user
     */
    @Query("SELECT COUNT(c) FROM ChatVocal c WHERE c.destinataire.id = :femmeId AND c.lu = false")
    Long countUnreadMessagesByFemmeId(@Param("femmeId") Long femmeId);
    
    /**
     * Find all voice messages between two users (private chat) - original method
     */
    @Query("SELECT c FROM ChatVocal c WHERE " +
           "(c.expediteur = :user1 AND c.destinataire = :user2) OR " +
           "(c.expediteur = :user2 AND c.destinataire = :user1) " +
           "ORDER BY c.dateEnvoi DESC")
    List<ChatVocal> findPrivateChat(@Param("user1") FemmeRurale user1, 
                                     @Param("user2") FemmeRurale user2);

    /**
     * Find all voice messages in a cooperative - original method
     */
    @Query("SELECT c FROM ChatVocal c WHERE c.coperative = :coperative ORDER BY c.dateEnvoi DESC")
    List<ChatVocal> findByCooperative(@Param("coperative") Coperative coperative);

    /**
     * Find all unread messages for a user (private chats) - original method
     */
    @Query("SELECT c FROM ChatVocal c WHERE c.destinataire = :user AND c.lu = false ORDER BY c.dateEnvoi DESC")
    List<ChatVocal> findUnreadMessagesForUserEntity(@Param("user") FemmeRurale user);

    /**
     * Find all messages sent by a user - original method
     */
    @Query("SELECT c FROM ChatVocal c WHERE c.expediteur = :user ORDER BY c.dateEnvoi DESC")
    List<ChatVocal> findByExpediteurEntity(@Param("user") FemmeRurale user);

    /**
     * Find all messages received by a user (private only) - original method
     */
    @Query("SELECT c FROM ChatVocal c WHERE c.destinataire = :user ORDER BY c.dateEnvoi DESC")
    List<ChatVocal> findByDestinataireEntity(@Param("user") FemmeRurale user);

    /**
     * Count unread messages for a user - original method
     */
    @Query("SELECT COUNT(c) FROM ChatVocal c WHERE c.destinataire = :user AND c.lu = false")
    Long countUnreadMessagesForUserEntity(@Param("user") FemmeRurale user);
}
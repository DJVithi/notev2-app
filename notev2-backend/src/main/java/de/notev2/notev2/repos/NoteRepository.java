package de.notev2.notev2.repos;

import de.notev2.notev2.entity.Note;
import de.notev2.notev2.entity.User;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByUser(User user);
    Optional<Note> findById(Long id);
    List<Note> findByUserOrderByIdDesc(User user);
    List<Note> findByUserOrderByCreatedAtDesc(User user);
    
}
